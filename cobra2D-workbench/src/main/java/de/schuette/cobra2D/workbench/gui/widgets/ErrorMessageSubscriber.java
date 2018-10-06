package de.schuette.cobra2D.workbench.gui.widgets;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.schuette.cobra2D.workbench.notification.Notification;
import de.schuette.cobra2D.workbench.notification.NotificationSubscriber;
import de.schuette.cobra2D.workbench.notification.Notificator;
import de.schuette.cobra2D.workbench.notification.WildcardNotificationType;
import de.schuette.cobra2D.workbench.runtime.RuntimeConstants;

public class ErrorMessageSubscriber implements NotificationSubscriber {

	private static Logger log = Logger.getLogger(ErrorMessageSubscriber.class);

	public List<Class<? extends Notificator>> getNotificationTypes() {
		List<Class<? extends Notificator>> types = new ArrayList<Class<? extends Notificator>>();
		types.add(WildcardNotificationType.class);
		return types;
	}

	public void receiveNotification(Notification notification,
			Notificator notificator,
			Class<? extends Notificator> notificatorType) {
		RuntimeConstants.logAnPresentExceptionNotificationToUser(log,
				notification);
	}

}
