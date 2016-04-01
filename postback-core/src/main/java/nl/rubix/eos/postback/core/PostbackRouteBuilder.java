package nl.rubix.eos.postback.core;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolProfileBuilder;
import org.apache.camel.component.cache.CacheConstants;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.spi.ThreadPoolProfile;

public class PostbackRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		ThreadPoolProfile customThreadPoolProfile = new ThreadPoolProfileBuilder("eventDistributionThreadPool").poolSize(25).maxPoolSize(50).maxQueueSize(1000).build();
	    ModelCamelContext context = getContext();
	    context.getExecutorServiceManager().registerThreadPoolProfile(customThreadPoolProfile);
	    
	    from("direct:createCacheEntry").routeId("CreateCacheEntry")
	      .setBody(simple("${header.cache_value}"))
	      .log("creating cache entry with key: ${header.cache_key} and value: ${body}")
	      .setHeader(CacheConstants.CACHE_OPERATION, constant(CacheConstants.CACHE_OPERATION_ADD))
	      .setHeader(CacheConstants.CACHE_KEY, simple("${header.cache_key}"))
	      .to("cache://MyTestCache" +
	          "?maxElementsInMemory=1000" +
	          "&memoryStoreEvictionPolicy=MemoryStoreEvictionPolicy.LFU" +
	          "&timeToLiveSeconds=300" +
	          "&timeToIdleSeconds=300" +
	          "&diskPersistent=true" +
	          "&diskExpiryThreadIntervalSeconds=300" +
	          "&eventListenerRegistry=#eventListenerRegistry")
	      .log("cache entry created with key: ${header.cache_key}");
	    
	    from("direct:retrieveCacheEntry").routeId("RetrieveCacheEntry")
	      .log("retrieving cache entry with key: ${body}")
	      .setHeader(CacheConstants.CACHE_OPERATION, constant(CacheConstants.CACHE_OPERATION_GET))
	      .setHeader(CacheConstants.CACHE_KEY, simple("${body}"))
	      .processRef("getCompleteCacheInstanceProcessor")
	      .choice().when(header(CacheConstants.CACHE_ELEMENT_WAS_FOUND))
	        .log("Cache entry was found: ${body} and headers: ${headers}")
	       .otherwise()
	         .setBody(simple("No cache entry found with id: ${header.subscriptionid}"))
	         .log("${body}")
	      .end();
	    
	    from("direct:deleteCacheEntry").routeId("DeleteCacheEntry")
	      .log("deleting cache entry with key: ${body}")
	      .setHeader(CacheConstants.CACHE_OPERATION, constant(CacheConstants.CACHE_OPERATION_DELETE))
	      .setHeader(CacheConstants.CACHE_KEY, simple("${body}"))
	      .to("cache://MyTestCache")
	      .log("entry Deleted");
	    
	    
	    from("timer:foo?period=3000").routeId("distributeEventsToSubscribers")
	      .onException(java.lang.Exception.class).maximumRedeliveries(3).redeliveryDelay(100).continued(true).end()
	      .log("message received")
	      .setBody(simple("message received at: ${date:now:yyyy-MM-dd'T'HH:mm:ss:SSS}"))
	      .removeHeader("*")
	      .setHeader("CamelHttpMethod", constant("POST"))
	      .recipientList()
	        .method(nl.rubix.eos.postback.core.cache.camel.EventDistributor.class, "getSubscribers")
	        .executorServiceRef("eventDistributionThreadPool");
	    
	}

}
