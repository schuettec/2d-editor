package de.schuette.cobra2D.workbench.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class NotificationManager implements NotificationSystem {

	protected Logger logger = Logger.getLogger(NotificationManager.class);

	protected HashMap<Class<? extends Notificator>, Notificator> notificators = new HashMap<Class<? extends Notificator>, Notificator>();
	protected HashMap<Class<? extends Notificator>, List<NotificationSubscriber>> typeToSubscribers = new HashMap<Class<? extends Notificator>, List<NotificationSubscriber>>();

	protected List<NotificationSubscriber> wildcardSubscriber = new ArrayList<NotificationSubscriber>();

	/**
	 * Use this method to register a notificator to the notification subscriber
	 * system.
	 * 
	 * @param notificator
	 *            The notificator that sends notifications to subscribers.
	 * @return Returns the notification system which is used to send
	 *         notifications.
	 */
	public void addNotificator(Notificator notificator) {
		if (notificator == null)
			throw new IllegalArgumentException("Arguments cannot be null!");

		// Add notificator
		notificators.put(notificator.getClass(), notificator);

		logger.debug("Notificator added to notification manager: "
				+ notificator.getClass().getName());

		// Create subscriber list for this notificator if not exist.
		Class<? extends Notificator> clazz = notificator.getClass();
		if (!typeToSubscribers.containsKey(clazz)) {
			List<NotificationSubscriber> subscriberList = new ArrayList<NotificationSubscriber>();
			typeToSubscribers.put(clazz, subscriberList);
			logger.debug("Subscriber list does not exist, created.");
		}

		notificator.setNotificationSystem(this);
	}

	/**
	 * Use this method to unregister from the notification system.
	 * 
	 * @param notificator
	 *            The notificator to unregister.
	 */
	public void removeNotificator(Notificator notificator) {
		// Only delete notificator reference
		if (notificators.containsKey(notificator.getClass())) {
			notificators.remove(notificator.getClass());
			notificator.setNotificationSystem(null);
			logger.debug("Notificator removed: "
					+ notificator.getClass().getName());
		}
	}

	/**
	 * Use this method to register a subscriber to the list of the specified
	 * notificator.
	 * 
	 * @param subscriber
	 *            The subscriber to add.
	 * @param notificatorType
	 *            The type of notifications to receive.
	 */
	protected void addSubscriber(NotificationSubscriber subscriber,
			Class<? extends Notificator> notificatorType) {
		if (subscriber == null || notificatorType == null)
			throw new IllegalArgumentException("Arguments cannot be null!");

		// Add subscriber to the list of the notificatorType
		if (typeToSubscribers.containsKey(notificatorType)) {
			// Get the subscriber list and add subscriber
			List<NotificationSubscriber> subscriberList = typeToSubscribers
					.get(notificatorType);
			subscriberList.add(subscriber);
			logger.debug("Subscriber (" + subscriber.getClass().getSimpleName()
					+ ") added to subscriber list for "
					+ notificatorType.getClass().getName());
		} else {
			// If this subscriber list does not exist, add it to make later
			// use
			// possible
			List<NotificationSubscriber> subscriberList = new ArrayList<NotificationSubscriber>();
			subscriberList.add(subscriber);
			typeToSubscribers.put(notificatorType, subscriberList);
			logger.debug("Subscriber (" + subscriber.getClass().getSimpleName()
					+ ") added to subscriber list for "
					+ notificatorType.getClass().getName());
		}
	}

	/**
	 * Use this method to unregister a subscriber from the list of the specified
	 * notificator.
	 * 
	 * @param subscriber
	 *            The subscriber to unregister.
	 * @param notificatorType
	 *            The type of notifications to not receive anymore.
	 */
	protected void removeSubscriber(NotificationSubscriber subscriber,
			Class<? extends Notificator> notificatorType) {

		if (subscriber == null || notificatorType == null)
			throw new IllegalArgumentException("Arguments cannot be null!");

		if (typeToSubscribers.containsKey(notificatorType)) {
			List<NotificationSubscriber> list = typeToSubscribers
					.get(notificatorType);
			list.remove(subscriber);

			logger.debug("Subscriber (" + subscriber.getClass().getSimpleName()
					+ ") removed from subscriber list for "
					+ notificatorType.getClass().getName());
		}
	}

	/**
	 * Use this method to register a subscriber to the system
	 * 
	 * @param subscriber
	 *            The subscriber to add.
	 */
	public void addSubscriber(NotificationSubscriber subscriber) {
		if (subscriber == null)
			throw new IllegalArgumentException("Arguments cannot be null!");

		List<Class<? extends Notificator>> notificationTypes = subscriber
				.getNotificationTypes();

		// Check for wildcard subscribers and put them to another list
		if (notificationTypes.contains(WildcardNotificationType.class)) {
			wildcardSubscriber.add(subscriber);
			logger.debug("Subscriber (" + subscriber.getClass().getSimpleName()
					+ ") added to wildcard subscriber list.");
		} else {
			// If no wildcard was set:
			for (Class<? extends Notificator> type : notificationTypes) {
				addSubscriber(subscriber, type);
			}
		}
	}

	/**
	 * Use this method to unregister a subscriber to the system
	 * 
	 * @param subscriber
	 *            The subscriber to remove.
	 */
	public void removeSubscriber(NotificationSubscriber subscriber) {
		if (subscriber == null)
			throw new IllegalArgumentException("Arguments cannot be null!");

		List<Class<? extends Notificator>> notificationTypes = subscriber
				.getNotificationTypes();
		// Check for wildcard subscribers and remove them from another list
		if (notificationTypes.contains(WildcardNotificationType.class)) {
			wildcardSubscriber.remove(subscriber);
			logger.debug("Subscriber (" + subscriber.getClass().getSimpleName()
					+ ") removed from wildcard subscriber list.");
		} else {
			// If no wildcard was set
			for (Class<? extends Notificator> type : notificationTypes) {
				removeSubscriber(subscriber, type);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.schuette.cobra2D.workbench.gui.notification.NotificationSystem#
	 * notifySubscribers
	 * (de.schuette.cobra2D.workbench.gui.notification.Notificator,
	 * de.schuette.cobra2D.workbench.gui.notification.Notification)
	 */
	public void notifySubscribers(Notification notification,
			Notificator notificator) {
		// Set the source of this notification.
		notification.setSource(notificator);

		Class<? extends Notificator> notificatorType = notificator.getClass();
		if (!typeToSubscribers.containsKey(notificatorType)) {
			throw new IllegalAccessError("No notificator with type "
					+ notificatorType + " registered.");
		} else {

			List<NotificationSubscriber> wildcardSubscribers = new ArrayList<NotificationSubscriber>(
					wildcardSubscriber);
			List<NotificationSubscriber> subscriberList = typeToSubscribers
					.get(notificatorType);
			for (NotificationSubscriber subscriber : subscriberList) {
				subscriber.receiveNotification(notification, notificator,
						notificatorType);
				// Remove the subscriber from the wildcard list to do not notify
				// them multiple times
				if (wildcardSubscribers.contains(subscriber)) {
					wildcardSubscribers.remove(subscriber);
				}

			}

			// Notify all wildcard subscribers
			boolean wildcardSubscribersNotified = false;
			for (NotificationSubscriber subscriber : wildcardSubscribers) {
				subscriber.receiveNotification(notification, notificator,
						notificatorType);
				wildcardSubscribersNotified = true;
			}

			if (wildcardSubscribersNotified) {
				logger.debug("Notification from "
						+ notificator.getClass().getName()
						+ " broadcasted to all subscribers.");
			}

		}
	}

}
