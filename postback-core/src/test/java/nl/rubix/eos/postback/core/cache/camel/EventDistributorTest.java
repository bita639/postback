package nl.rubix.eos.postback.core.cache.camel;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EventDistributorTest {
	  
	  EventDistributor eventDistributor;

	  @Before
	  public void createCacheAndInstantiateProcessor() {
	    this.eventDistributor = new EventDistributor();
	    CacheManager cm = CacheManager.getInstance();
	    cm.addCache("MyTestCache");
	    Cache cache = cm.getCache("MyTestCache");

	    Element element1 = new Element("testKey1", "testValue1");
	    cache.put(element1);
	    
	    Element element2 = new Element("testKey2", "testValue2");
	    cache.put(element2);
	  }
	  
	  @Test
	  public void EventDistributorValidCallbackUrlsTest(){
	    List<String> callbackUrlList = eventDistributor.getSubscribers();
	    assertEquals("First callbackUrl should be http4://testValue1", "http4://testValue1", callbackUrlList.get(1));
	    assertEquals("First callbackUrl should be http4://testValue2", "http4://testValue2", callbackUrlList.get(0));
	  }
	  
	  @Test(expected = Exception.class)
	  public void EventDistributorInValidCallbackUrlsTest(){
	    List<String> callbackUrlList = eventDistributor.getSubscribers();
	    assertEquals("First callbackUrl should be http4://testValue1", "http4://testValue1", callbackUrlList.get(2));
	  }
	  
	  @After
	  public void destroyCache(){
	    CacheManager cm = CacheManager.getInstance();
	    cm.removeAllCaches();
	  }

	}
