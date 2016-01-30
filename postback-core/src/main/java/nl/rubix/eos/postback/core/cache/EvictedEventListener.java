package nl.rubix.eos.postback.core.cache;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;

public class EvictedEventListener extends CacheEventListenerBaseImpl {
	@Produce(uri = "direct:sendExpiredNotification")
	ProducerTemplate producer;

	@Override
	public void notifyElementEvicted(Ehcache cache, Element element) {
		System.out.println("Element evicted: " + element.getObjectValue().toString());
		producer.sendBody(createExpiredNotification(element));
	}

	@Override
	public void notifyElementExpired(Ehcache cache, Element element) {
		System.out.println("Element expired: " + element.getObjectValue().toString());
		producer.sendBody(createExpiredNotification(element));
	}

	private List<String> createExpiredNotification(Element element) {
		List<String> expiredNotification = new ArrayList<String>();

		expiredNotification.add(element.getObjectKey().toString());
		expiredNotification.add(element.getObjectValue().toString());

		return expiredNotification;
	}
}
