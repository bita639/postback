package nl.rubix.eos.postback.core.cache.camel;

import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.camel.Exchange;
import org.apache.camel.component.cache.CacheConstants;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetCompleteCacheInstanceProcessorTest extends CamelTestSupport{
	  
	  private GetCompleteCacheInstanceProcessor getCompleteCacheInstanceProcessor;
	  
	  @Before
	  public void createCacheAndInstantiateProcessor(){
	    getCompleteCacheInstanceProcessor = new GetCompleteCacheInstanceProcessor();
	    
	    CacheManager cm = CacheManager.getInstance();
	    cm.addCache("MyTestCache");
	    Cache cache = cm.getCache("MyTestCache");
	    
	    Element element = new Element("testKey", "testValue");
	    cache.put(element);
	  }
	  
	  @Test
	  public void getCompleteCacheInstanceProcessorTest() throws Exception{
	    Exchange testExchange = createExchangeWithBody("test message");
	    testExchange.getIn().setHeader(CacheConstants.CACHE_KEY, "testKey");
	    
	    getCompleteCacheInstanceProcessor.process(testExchange);
	    Map<String,String> resultCacheInstance = testExchange.getIn().getBody(Map.class);
	    
	    assertTrue("CacheInstance must be found", testExchange.getIn().getHeader(CacheConstants.CACHE_ELEMENT_WAS_FOUND,Boolean.class));
	    assertEquals("The id must be testKey", "testKey", resultCacheInstance.get("id"));
	    assertEquals("The callbackUrl must be testValue", "testValue", resultCacheInstance.get("callbackUrl"));
	    assertEquals("The hitcount must be 0", "0", resultCacheInstance.get("hitCount"));
	    assertEquals("The timeToLive must be 120", "120", resultCacheInstance.get("timeToLive"));
	  }
	  
	  @Test
	  public void getCompleteCacheInstanceProcessorNoInstanceFoundTest() throws Exception{
	    Exchange testExchange = createExchangeWithBody("test message");
	    testExchange.getIn().setHeader(CacheConstants.CACHE_KEY, "noEntryWithThisKeyShouldExist");
	    
	    getCompleteCacheInstanceProcessor.process(testExchange);
	    
	    assertFalse("CacheInstance must be found", testExchange.getIn().getHeader(CacheConstants.CACHE_ELEMENT_WAS_FOUND,Boolean.class));
	  }
	  
	  @After
	  public void destroyCache(){
	    CacheManager cm = CacheManager.getInstance();
	    cm.removeAllCaches();
	  }
	  
	}

