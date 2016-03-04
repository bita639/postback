package nl.rubix.eos.portback.core;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.camel.Exchange;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PostbackRouteBuilderCreateCacheEntryTest extends CamelBlueprintTestSupport {

	CacheManager cm;

	@Before
	public void insantiateEvictedEventListener() throws Exception {
		this.cm = CacheManager.getInstance();
	}

	@Override
	protected String getBlueprintDescriptor() {
		return "OSGI-INF/blueprint/postback.xml";
	}

	@Test
	public void testCreateCacheEntryRoute() throws Exception {
		
		String key = "dummyKey";
		String value = "dummycache value";
		
		Exchange exchange = createExchangeWithBody("");
		exchange.getIn().setHeader("cache_key", key);
		exchange.getIn().setHeader("cache_value", value);

		template.send("direct:createCacheEntry", exchange);

		String response = (String) readCacheElement(key).getObjectValue();

		assertEquals("Cache element value should be dummycacke value", value, response);
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
