package nl.rubix.eos.postback.core.api;

import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class CreateSubscriptionProcessor implements Processor{

	@Override
	  public void process(Exchange exchange) throws Exception {
	    if (requestIsValid(exchange)) {
	      String callbackUrl = exchange.getIn().getHeader("callbackurl", String.class);
	      String key = createKey(callbackUrl);
	      exchange.getIn().setHeader("cache_key", key);
	      exchange.getIn().setHeader("cache_value", callbackUrl);
	      exchange.getIn().removeHeader("callbackurl");
	    } else {
	      exchange.setException(new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
	        .entity("name parameter is mandatory").build()));
	    }
	  }

	  protected Boolean requestIsValid(Exchange exchange) {
	    Boolean requestIsValid = false;
	    if (exchange.getIn().getHeader("callbackurl", String.class) == null) {
	      requestIsValid = false;
	    } else {
	      requestIsValid = true;
	    }
	    
	    return requestIsValid;
	  }

	  protected String createKey(String callbackUrl) {
	    String key = "";

	    MessageDigest md;
	    if(callbackUrl != null){
	      try {
	        md = MessageDigest.getInstance("MD5");
	        md.update(callbackUrl.getBytes());

	        byte byteData[] = md.digest();

	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	          sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }

	        key = sb.toString();

	      } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	      }
	    }

	    return key;
	  }

}
