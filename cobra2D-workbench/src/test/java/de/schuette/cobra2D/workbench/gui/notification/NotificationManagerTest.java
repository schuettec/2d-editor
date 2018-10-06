package de.schuette.cobra2D.workbench.gui.notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.schuette.cobra2D.workbench.notification.Notification;
import de.schuette.cobra2D.workbench.notification.NotificationManager;
import de.schuette.cobra2D.workbench.notification.NotificationSubscriber;
import de.schuette.cobra2D.workbench.notification.NotificationSystem;
import de.schuette.cobra2D.workbench.notification.Notificator;
import de.schuette.cobra2D.workbench.notification.WildcardNotificationType;

public class NotificationManagerTest {

	private static final int NOTIFICATION_COUNT_2 = 10;
	private static final int NOTIFICATION_COUNT_1 = 5;

	private NotificationManager manager;

	private NotificationSource1 source1;
	private Thread sender1;
	private NotificationSource2 source2;
	private Thread sender2;

	private NotificationSubscriber subscriber1;
	private NotificationSubscriber subscriber2;
	private WildcardSubscriber wildcardSubscriber;

	private int notificationsReceived1;
	private int notificationsReceived2;
	private int wildcardNotificationsReceived;

	@Before
	public void setUp() throws Exception {
		// Create notification system
		this.manager = new NotificationManager();

		// Create sender
		this.source1 = new NotificationSource1();
		this.source2 = new NotificationSource2();

		// Create subscriber
		this.subscriber1 = new Subscriber1();
		this.subscriber2 = new Subscriber2();
		this.wildcardSubscriber = new WildcardSubscriber();

		// Register to notification system
		this.manager.addNotificator(source1);
		this.manager.addNotificator(source2);
		this.manager.addSubscriber(subscriber1);
		this.manager.addSubscriber(subscriber2);
		this.manager.addSubscriber(wildcardSubscriber);

		// Reset counters
		notificationsReceived1 = 0;
		notificationsReceived2 = 0;
		wildcardNotificationsReceived = 0;

		// Create subscriber
		this.subscriber1 = new Subscriber1();
		this.subscriber2 = new Subscriber2();
	}

	@After
	public void tearDown() throws Exception {
		// Tidy up the notification system
		manager.removeNotificator(source1);
		manager.removeNotificator(source2);

		manager.removeSubscriber(subscriber1);
		manager.removeSubscriber(subscriber2);
		manager.removeSubscriber(wildcardSubscriber);

		// Forget threads
		sender1 = null;
		sender2 = null;

		// Reset everything
		this.source1 = null;
		this.source2 = null;
		subscriber1 = null;
		subscriber2 = null;
		wildcardSubscriber = null;

	}

	@Test
	public void testPositive() {
		// Send an amount of notifications asynchronous
		source1.sendNotifications(NOTIFICATION_COUNT_1);
		source2.sendNotifications(NOTIFICATION_COUNT_2);

		// Wait for senders to finish
		try {
			sender1.join();
			sender2.join();
		} catch (InterruptedException e) {
			// Nothing to handle :/
		}

		assertEquals(NOTIFICATION_COUNT_1, notificationsReceived1);
		assertEquals(NOTIFICATION_COUNT_2, notificationsReceived2);
		assertEquals(NOTIFICATION_COUNT_1 + NOTIFICATION_COUNT_2,
				wildcardNotificationsReceived);

	}

	class Subscriber1 implements NotificationSubscriber {

		public void receiveNotification(Notification notification,
				Notificator notificator,
				Class<? extends Notificator> notificatorType) {
			assertTrue(notification.containsKey("id"));
			int id = (Integer) notification.get("id");
			assertEquals(id, 1);
			notificationsReceived1++;
		}

		public List<Class<? extends Notificator>> getNotificationTypes() {
			List<Class<? extends Notificator>> types = new ArrayList<Class<? extends Notificator>>();
			types.add(NotificationSource1.class);
			return types;
		}
	};

	class Subscriber2 implements NotificationSubscriber {

		public void receiveNotification(Notification notification,
				Notificator notificator,
				Class<? extends Notificator> notificatorType) {
			assertTrue(notification.containsKey("id"));
			int id = (Integer) notification.get("id");
			assertEquals(id, 2);
			notificationsReceived2++;
		}

		public List<Class<? extends Notificator>> getNotificationTypes() {
			List<Class<? extends Notificator>> types = new ArrayList<Class<? extends Notificator>>();
			types.add(NotificationSource2.class);
			return types;
		}
	};

	class WildcardSubscriber implements NotificationSubscriber {

		public void receiveNotification(Notification notification,
				Notificator notificator,
				Class<? extends Notificator> notificatorType) {
			assertTrue(notification.containsKey("id"));
			int id = (Integer) notification.get("id");
			boolean idExpected = (id == 1) || (id == 2);
			assertTrue(idExpected);
			wildcardNotificationsReceived++;
		}

		public List<Class<? extends Notificator>> getNotificationTypes() {
			List<Class<? extends Notificator>> types = new ArrayList<Class<? extends Notificator>>();
			types.add(WildcardNotificationType.class);
			return types;
		}
	};

	class NotificationSource1 implements Notificator {

		private NotificationSystem manager;

		public void sendNotifications(final int notifications) {
			sender1 = new Thread(new Runnable() {

				public void run() {
					for (int i = 0; i < notifications; i++) {
						// First check if manager was injected
						if (manager != null) {
							Notification notification = new Notification();
							notification.put("id", 1);
							manager.notifySubscribers(notification,
									NotificationSource1.this);
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
							}
						}
					}
				}
			});
			sender1.start();

		}

		public void setNotificationSystem(NotificationSystem manager) {
			this.manager = manager;
		}
	}

	class NotificationSource2 implements Notificator {

		private NotificationSystem manager;

		public void sendNotifications(final int notifications) {
			sender2 = new Thread(new Runnable() {

				public void run() {
					for (int i = 0; i < notifications; i++) {
						// First check if manager was injected
						if (manager != null) {
							Notification notification = new Notification();
							notification.put("id", 2);
							manager.notifySubscribers(notification,
									NotificationSource2.this);
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
							}
						}
					}
				}
			});
			sender2.start();

		}

		public void setNotificationSystem(NotificationSystem manager) {
			this.manager = manager;
		}
	}

}
