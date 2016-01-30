package nl.rubix.eos.postback.core.api;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/")
public class SubscriptionApi {

	@GET
	@Path("/subscribe")
	@Produces(MediaType.APPLICATION_JSON)
	public String subscribe(@QueryParam("callbackurl") String callbackurl) {
		return null;
	}

	@GET
	@Path("{subscriptionid}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getSubscription(@PathParam("subscriptionid") String subscriptionId) {
		return null;
	}

	@DELETE
	@Path("{subscriptionid}")
	public String removeSubscription(@PathParam("subscriptionid") String subscriptionId) {
		return null;
	}
}
