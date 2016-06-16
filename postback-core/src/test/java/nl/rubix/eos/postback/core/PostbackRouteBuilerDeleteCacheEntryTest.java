package nl.rubix.eos.postback.core;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.camel.Exchange;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PostbackRouteBuilerDeleteCacheEntryTest extends CamelBlueprintTestSupport{
	
	CacheManager cm;
	Cache cache;

	@Before
	public void insantiateEvictedEventListener() throws Exception {
		this.cm = CacheManager.getInstance();
		cm.addCacheIfAbsent("MyTestCache");
		this.cache = cm.getCache("MyTestCache");

		Element element = new Element("testKey", "testValue");
		cache.put(element);
	}

	@Override
	protected String getBlueprintDescriptor() {
		return "OSGI-INF/blueprint/postback.xml";
	}
	
	@Test
	public void deleteCacheEntryTest() throws Exception{
		Exchange exchange = createExchangeWithBody("testKey");

		template.send("direct:deleteCacheEntry", exchange);
		
		Element element = readCacheElement("testKey");
		
		assertNull(element);
	}
	
	private Element readCacheElement(String key) {
		Cache cache = cm.getCache("MyTestCache");
		Element element = cache.get(key);
		
		return element;
	}
	
	@After
	public void purgeCache() throws Exception {
		Cache cache = cm.getCache("MyTestCache");
		cache.removeAll();
	}
}
