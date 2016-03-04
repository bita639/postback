package nl.rubix.eos.portback.core;

import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.camel.Exchange;
import org.apache.camel.component.cache.CacheConstants;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PostbackRouteBuilderRetrieveCacheEntryTest extends CamelBlueprintTestSupport {

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
	public void retrieveCacheEntryTest() throws Exception {
		Exchange exchange = createExchangeWithBody("testKey");

		Exchange response = template.send("direct:retrieveCacheEntry", exchange);

		Map<String, String> resultCacheInstance = response.getIn().getBody(Map.class);

		assertTrue("CacheInstance must be found", response.getIn().getHeader(CacheConstants.CACHE_ELEMENT_WAS_FOUND, Boolean.class));
		assertEquals("The id must be testKey", "testKey", resultCacheInstance.get("id"));
		assertEquals("The callbackUrl must be testValue", "testValue", resultCacheInstance.get("callbackUrl"));
		assertEquals("The hitcount must be 0", "0", resultCacheInstance.get("hitCount"));
		assertEquals("The timeToLive must be 300", "300", resultCacheInstance.get("timeToLive"));
	}
	
	@After
	public void purgeCache() throws Exception {
		Cache cache = cm.getCache("MyTestCache");
		cache.removeAll();
	}
}
