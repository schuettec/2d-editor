package de.schuette.cobra2D.workbench.gui.animationEditor;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import de.schuette.cobra2D.workbench.model.MapModel;
import de.schuette.cobra2D.workbench.notification.Notification;
import de.schuette.cobra2D.workbench.notification.NotificationManager;
import de.schuette.cobra2D.workbench.notification.NotificationSubscriber;
import de.schuette.cobra2D.workbench.notification.Notificator;
import de.schuette.cobra2D.workbench.runtime.WorkbenchRuntime;

public class AnimationEditorWidget extends JFrame implements WindowListener,
		NotificationSubscriber {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AnimationEditor animationEditor;

	public AnimationEditorWidget(MapModel mapModel) throws Exception {

		this.animationEditor = new AnimationEditor(mapModel);

		AnimationEditorView view = animationEditor.getView();

		this.addWindowListener(this);
		this.setBounds(200, 100, 1200, 900);
		this.setLayout(new BorderLayout());
		this.add(view, BorderLayout.CENTER);
		this.setVisible(true);

		WorkbenchRuntime instance = WorkbenchRuntime.getInstance();
		NotificationManager notificationSystem = instance
				.getNotificationSystem();
		notificationSystem.addSubscriber(this);

	}

	public void windowOpened(WindowEvent e) {

	}

	public void windowClosing(WindowEvent e) {
		this.dispose();
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {

	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {

	}

	public List<Class<? extends Notificator>> getNotificationTypes() {
		List<Class<? extends Notificator>> types = new ArrayList<Class<? extends Notificator>>();
		types.add(AnimationEditor.class);
		return types;
	}

	public void receiveNotification(Notification notification,
			Notificator notificator,
			Class<? extends Notificator> notificatorType) {
		if (notification.containsKey("eventName")) {
			String eventName = (String) notification.get("eventName");
			if (eventName.equalsIgnoreCase("AnimationAdded")) {
				this.dispose();
			}
		}
	}
}
