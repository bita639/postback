package nl.rubix.eos.postback.core.api;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class SubscriptionApiRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("cxfrs:bean:subscriptionApi?bindingStyle=SimpleConsumer")
	    .log("received request")
	    .choice()
	     .when(simple("${header.operationName} == 'subscribe'"))
	      .log("creating subscription with id: ${header.breadcrumbId} and value: ${header.callbackurl}")
	      .setHeader("cache_key", simple("${header.breadcrumbId}"))
	      .setHeader("cache_value", simple("${header.callbackurl}"))
	      .inOut("direct:createCacheEntry")
	      .setBody(simple("${header.breadcrumbId}"))
	     .when(simple("${header.operationName} == 'removeSubscription'"))
	       .setBody(simple("${header.subscriptionid}"))
	       .log("removing subscription for id: ${body}")
	       .inOut("direct:deleteCacheEntry")
	       .setBody(simple("subscription deleted with id: ${body}"))
	     .when(simple("${header.operationName} == 'getSubscription'"))
	       .setBody(simple("${header.subscriptionid}"))
	       .log("retrieving subscription from cache with id: ${body}")
	       .inOut("direct:retrieveCacheEntry")
	     .otherwise()
	       .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
	     .end()
	     .removeHeaders("*")
	     .marshal().json(JsonLibrary.Jackson);
	    
	    from("direct:sendExpiredNotification").routeId("ExpiredNotificationRoute")
	    .onException(java.lang.Exception.class).maximumRedeliveries(3).redeliveryDelay(1000).continued(true).end()
	    .log("The subscription with the following Id is expired: ${body[0]}")
	    .removeHeaders("*")
	    .setHeader("CamelHttpMethod", constant("POST"))
	    .setHeader("routingUrl", simple("http4://${body[1].replaceAll('http://', '')}"))
	    .setBody(simple("Subscription expired with id: ${body[0]}"))
	    .routingSlip(header("routingUrl")).ignoreInvalidEndpoints();
	}

}
