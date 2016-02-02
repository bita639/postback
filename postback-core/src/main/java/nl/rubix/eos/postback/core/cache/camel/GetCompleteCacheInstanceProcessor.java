package nl.rubix.eos.postback.core.cache.camel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cache.CacheConstants;

public class GetCompleteCacheInstanceProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		CacheManager cm = CacheManager.getInstance();
		Cache cache = cm.getCache("MyTestCache");

		Element cacheInstance = cache.getQuiet(exchange.getIn().getHeader(CacheConstants.CACHE_KEY));
		Map<String, String> cacheInstanceAttributes = new HashMap<String, String>();

		if (cacheInstance != null) {
			exchange.getIn().setHeader(CacheConstants.CACHE_ELEMENT_WAS_FOUND, true);

			cacheInstanceAttributes.put("creationTime", new Date(cacheInstance.getCreationTime()).toString());
			cacheInstanceAttributes.put("expirationTime", new Date(cacheInstance.getExpirationTime()).toString());
			cacheInstanceAttributes.put("hitCount", Long.toString(cacheInstance.getHitCount()));
			cacheInstanceAttributes.put("lastAccessTime", new Date(cacheInstance.getLastAccessTime()).toString());
			cacheInstanceAttributes.put("lastUpdatedTime", new Date(cacheInstance.getLastUpdateTime()).toString());
			cacheInstanceAttributes.put("id", cacheInstance.getObjectKey().toString());
			cacheInstanceAttributes.put("callbackUrl", cacheInstance.getObjectValue().toString());
			cacheInstanceAttributes.put("timeToLive", Integer.toString(cacheInstance.getTimeToLive()));
		} else {
			exchange.getIn().setHeader(CacheConstants.CACHE_ELEMENT_WAS_FOUND, false);
		}

		exchange.getIn().setBody(cacheInstanceAttributes, Map.class);
	}

}
