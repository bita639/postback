package nl.rubix.eos.portback.core;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import nl.rubix.eos.postback.core.GetAllCacheInstancesProcessor;
import nl.rubix.eos.postback.core.cache.EvictedEventListener;

import org.apache.camel.Exchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetAllCacheInstancesProcessorTest extends CamelTestSupport{
	
	EvictedEventListener evictedEventListener;
	CacheManager cm;
	Cache cache;
	GetAllCacheInstancesProcessor processor;
	
	
	@Before
	public void insantiateEvictedEventListener() throws Exception{
		this.evictedEventListener = new EvictedEventListener();
		this.cm = CacheManager.getInstance();
		cache = new Cache("UnitTestCache", 2, false, false, 100000L, 100000L);
		cm.addCache(cache);
		this.processor = new GetAllCacheInstancesProcessor();
		populateUnitTestCache();
	}
	
	private void populateUnitTestCache() {
		Element element1 = new Element("Key1", "Value1");
		Element element2 = new Element("Key2", "Value2");
		
		cache.put(element1);
		cache.put(element2);
	}

	@Test 
	public void getAllCacheInstancesProcessorTest() throws Exception{
		Exchange testMsg = createExchangeWithBody("UnitTestCache");
		
		processor.process(testMsg);
		
		assertIsInstanceOf(List.class, testMsg.getIn().getBody());
		assertEquals("Number of items in the list should be 2", 2, testMsg.getIn().getBody(List.class).size());
		assertEquals("Value of the second item should be Value2", "Value2", testMsg.getIn().getBody(List.class).get(0));
	}
	
	@After
	public void destroyCache() throws Exception{
		cm.removeCache("UnitTestCache");
	}
}
