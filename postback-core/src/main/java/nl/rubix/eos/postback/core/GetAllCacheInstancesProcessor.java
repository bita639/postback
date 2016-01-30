package nl.rubix.eos.postback.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class GetAllCacheInstancesProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		CacheManager cm = CacheManager.getInstance();
	    Cache cache = cm.getCache(exchange.getIn().getBody(String.class));
	    
	    List<String> itemList = new ArrayList<String>(); 
	    
	    Map<Object, Element> el = cache.getAll(cache.getKeys());
	    
	    for (Entry<Object, Element> element : el.entrySet()) {
	      Element entry = element.getValue();
	      itemList.add(entry.getObjectValue().toString());
	    }
	    
	    exchange.getIn().setBody(itemList);
	}

}
