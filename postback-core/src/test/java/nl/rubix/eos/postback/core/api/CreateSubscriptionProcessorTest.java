package nl.rubix.eos.postback.core.api;

import org.apache.camel.Exchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;

public class CreateSubscriptionProcessorTest extends CamelTestSupport{
	  
	  private CreateSubscriptionProcessor createSubscriptionProcessor;
	  
	  @Before
	  public void instantiateProcessor(){
	    this.createSubscriptionProcessor = new CreateSubscriptionProcessor();
	  }
	  
	  @Test
	  public void requestIsValidTestTrue(){
	    Exchange testExchange = createExchangeWithBody("test");
	    testExchange.getIn().setHeader("callbackurl", "testcallbackurl");
	    assertTrue("Exchange must have callbackurl header", createSubscriptionProcessor.requestIsValid(testExchange));
	  }
	  
	  @Test
	  public void requestIsValidTestFalse(){
	    Exchange testExchange = createExchangeWithBody("test");
	    assertFalse("Exchange must have callbackurl header", createSubscriptionProcessor.requestIsValid(testExchange));
	  }
	  
	  @Test(expected = Exception.class)
	  public void requestIsValidTestException(){
	    assertTrue("Exchange must have callbackurl header", createSubscriptionProcessor.requestIsValid(null));
	  }
	  
	  @Test
	  public void createKeyTest(){
	    String testMsg = "this is a test";
	    assertEquals("create key must return a MD5 hash of the provided String","54b0c58c7ce9f2a8b551351102ee0938", createSubscriptionProcessor.createKey(testMsg));
	  }
	  
	  @Test
	  public void createKeyTestEmpty(){
	    assertEquals("create key will return an emtpy string if no key can be created","", createSubscriptionProcessor.createKey(null));
	  }
	  
	  @Test
	  public void createSubscriptionProcessorTestValid() throws Exception{
	    Exchange testExchange = createExchangeWithBody("test");
	    testExchange.getIn().setHeader("callbackurl", "testcallbackurl");
	    createSubscriptionProcessor.process(testExchange);
	    assertEquals("Exchange must contain cache_key header", "d074f6062a2b52a6e84c3cc245a2042f", testExchange.getIn().getHeader("cache_Key", String.class));
	    assertEquals("Exchange must contain cache_value header", "testcallbackurl", testExchange.getIn().getHeader("cache_value", String.class));
	    assertNull("Exchange must not have callbackurl header anymore", testExchange.getIn().getHeader("callbackurl"));
	  }
	  
	  @Test
	  public void createSubscriotionProcessorTestInvalid() throws Exception{
	    Exchange testExchange = createExchangeWithBody("test");
	    createSubscriptionProcessor.process(testExchange);
	    System.out.println(testExchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE));
	    assertEquals("Should be 400 bad request", testExchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE),new Integer(400));
	  }

	}
