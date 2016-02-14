package nl.rubix.eos.postback.core.api;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class SubscriptionApiRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		restConfiguration()
			.component("jetty")
			.host("localhost")
			.port(2345)
			.bindingMode(RestBindingMode.json)
			.dataFormatProperty("prettyPrint", "true");
		
		rest("/subscription")
		.get("/subscribe")
			.produces("application/json")
			.to("direct:subscribe")
		.get("{subscriptionid}")
			.produces("application/json")
			.to("direct:getSubscriptionDetails")
		.delete("{subscriptionid}")
			.to("direct:unsubscribe");
		
		from("direct:subscribe").routeId("Subscribe")
		.log("creating subscription with id: ${header.breadcrumbId} and value: ${header.callbackurl}")
	    .processRef("createSubscriptionProcessor")
	    .inOut("direct:createCacheEntry")
	    .setBody(simple("${header.cache_key}"))
	    .removeHeaders("*");
		
		from("direct:getSubscriptionDetails").routeId("GetSubscriptionDetails")
		.setBody(simple("${header.subscriptionid}"))
	    .log("retrieving subscription from cache with id: ${body}")
	    .inOut("direct:retrieveCacheEntry")
	    .removeHeaders("*");
		
		from("direct:unsubscribe").routeId("Unsubscribe")
		.setBody(simple("${header.subscriptionid}"))
	    .log("removing subscription for id: ${body}")
	    .inOut("direct:deleteCacheEntry")
	    .setBody(simple("subscription deleted with id: ${body}"))
	    .removeHeaders("*");
		
	    
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
