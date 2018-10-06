package de.schuette.cobra2D.workbench.notification;

import java.util.List;

public interface NotificationSubscriber {
	/**
	 * This method is called by the notification system to obtain the list of
	 * notification types this subscriber is interested in.
	 * 
	 * @return Return an array of notificator classes, in which the subscriber
	 *         is interested in.
	 */
	public List<Class<? extends Notificator>> getNotificationTypes();

	/**
	 * This method is called to signal a notification from one of the
	 * notificators this subscriber is interested in.
	 * 
	 * @param notification
	 *            The notification
	 * @param notificator
	 *            Send from this notificator
	 * @param notificatorType
	 *            The type of the sending notificator.
	 */
	public void receiveNotification(Notification notification,
			Notificator notificator,
			Class<? extends Notificator> notificatorType);
}
