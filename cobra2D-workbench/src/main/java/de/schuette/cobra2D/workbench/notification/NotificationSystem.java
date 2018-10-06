package de.schuette.cobra2D.workbench.notification;

public interface NotificationSystem {
	/**
	 * As notificator registered at the notification manager, use this method to
	 * send notifications to subscribers.
	 * 
	 * @param notificator
	 *            You as the notificator.
	 * @param notification
	 *            The notification to send to the subscribers.
	 */
	public void notifySubscribers(Notification notification,
			Notificator notificator);
}
