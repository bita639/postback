package nl.rubix.eos.postback.core.cache;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;

public class EvictedEventListenerTest extends CamelTestSupport{
	
	EvictedEventListener evictedEventListener;
	CacheManager cm;
	Cache cache;
	
	
	@Before
	public void insantiateEvictedEventListener() throws Exception{
		this.evictedEventListener = new EvictedEventListener();
		this.cm = CacheManager.getInstance();
		this.cache = cm.getCache("UnitTestCache");
	}
	
	
	@Test
	public void testCreateExpiredEventNotification() throws Exception{
		Element element = new Element("Key", "Value");
		
		List<String> result = evictedEventListener.createExpiredNotification(element);
		
		assertEquals("Key should be key", "Key" , result.get(0));
		assertEquals("Value should be Value", "Value", result.get(1));
		assertIsInstanceOf(List.class, result);
	}
	
}
