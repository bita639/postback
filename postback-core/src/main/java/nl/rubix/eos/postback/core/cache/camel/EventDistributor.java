package nl.rubix.eos.postback.core.cache.camel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EventDistributor {
	
	public List<String> getSubscribers() {
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache("MyTestCache");

		List<String> subscriberList = new ArrayList<String>();

		Map<Object, Element> el = cache.getAll(cache.getKeys());

		for (Entry<Object, Element> element : el.entrySet()) {
			Element entry = element.getValue();
			subscriberList.add("http4://" + entry.getObjectValue().toString());
		}
		return subscriberList;
	}
}
