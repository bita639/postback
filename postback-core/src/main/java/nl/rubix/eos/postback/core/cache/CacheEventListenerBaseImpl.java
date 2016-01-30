package nl.rubix.eos.postback.core.cache;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

public class CacheEventListenerBaseImpl implements CacheEventListener {

	@Override
	public void dispose() {
	}

	@Override
	public void notifyElementEvicted(Ehcache cache, Element element) {
	}

	@Override
	public void notifyElementExpired(Ehcache cache, Element element) {
	}

	@Override
	public void notifyElementPut(Ehcache cache, Element element)
			throws CacheException {
	}

	@Override
	public void notifyElementRemoved(Ehcache cache, Element element)
			throws CacheException {
	}

	@Override
	public void notifyElementUpdated(Ehcache cache, Element element)
			throws CacheException {
	}

	@Override
	public void notifyRemoveAll(Ehcache cache) {
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
