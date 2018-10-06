package de.schuette.cobra2D.workbench.notification;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Chris This class is used to transfer notification data from a
 *         notificator to a subscriber. Use this object to store key-value-pairs
 *         for the notification.
 */
public class Notification {

	protected Notificator notificator;
	protected HashMap<String, Object> context = new HashMap<String, Object>();

	/**
	 * Creates a notification.
	 */
	public Notification() {

	}

	/**
	 * @return Returns the notificator as the source object ob the notification.
	 */
	public Notificator getSource() {
		return notificator;
	}

	/**
	 * Used internally by the notification management. There is no need to use
	 * this method outside of the notification management.
	 * 
	 * @param notificator
	 */
	protected void setSource(Notificator notificator) {
		this.notificator = notificator;
	}

	public int size() {
		return context.size();
	}

	public boolean isEmpty() {
		return context.isEmpty();
	}

	public Object get(Object key) {
		return context.get(key);
	}

	public boolean containsKey(Object key) {
		return context.containsKey(key);
	}

	public boolean equals(Object o) {
		return context.equals(o);
	}

	public Object put(String key, Object value) {
		return context.put(key, value);
	}

	public int hashCode() {
		return context.hashCode();
	}

	@Override
	public String toString() {
		String str = "Notification [notificator="
				+ notificator.getClass().getName() + ",\n";
		Iterator<String> it = context.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			Object value = context.get(key);
			str += "- key: " + key + ", value: " + value + ",\n";
		}

		str += "]";
		return str;
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		context.putAll(m);
	}

	public Object remove(Object key) {
		return context.remove(key);
	}

	public void clear() {
		context.clear();
	}

	public boolean containsValue(Object value) {
		return context.containsValue(value);
	}

	public Object clone() {
		return context.clone();
	}

	public Set<String> keySet() {
		return context.keySet();
	}

	public Collection<Object> values() {
		return context.values();
	}

	public Set<Entry<String, Object>> entrySet() {
		return context.entrySet();
	}

}
