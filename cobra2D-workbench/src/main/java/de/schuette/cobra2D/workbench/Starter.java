package de.schuette.cobra2D.workbench;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.schuette.cobra2D.workbench.gui.mainframe.MapEditorController;
import de.schuette.cobra2D.workbench.gui.widgets.ErrorMessageSubscriber;
import de.schuette.cobra2D.workbench.model.MapModel;
import de.schuette.cobra2D.workbench.notification.Notification;
import de.schuette.cobra2D.workbench.notification.NotificationManager;
import de.schuette.cobra2D.workbench.notification.NotificationSubscriber;
import de.schuette.cobra2D.workbench.notification.Notificator;
import de.schuette.cobra2D.workbench.notification.WildcardNotificationType;
import de.schuette.cobra2D.workbench.runtime.WorkbenchRuntime;

public class Starter implements NotificationSubscriber {
	protected static Logger log = Logger.getLogger(Starter.class);

	public static void main(String[] args) throws Exception {

		WorkbenchRuntime instance = WorkbenchRuntime.getInstance();
		instance.setup(args);
		instance.setupNimbusLookAndFeel();

		// Add some subscribers to the notification system
		NotificationManager notificationSystem = instance
				.getNotificationSystem();
		notificationSystem.addSubscriber(new Starter());

		// Error Subscriber to present exceptions to user and log
		notificationSystem.addSubscriber(new ErrorMessageSubscriber());

		MapModel mapModel = new MapModel();

		MapEditorController mapEditorController = new MapEditorController(
				mapModel);
		// EntityEditorWidget editor = WidgetsUtil.createEntityEditor(mapModel);

		// AnimationEditorWidget animEdit = new AnimationEditorWidget(mapModel);

		// Cobra2DLevel level = mapModel.getCurrentLevel();
		// AnimationMemory anims = level.getAnimationMemory();
		// anims.loadAnimation("walkcyle_harvey_animation",
		// "walkcyle_harvey_png",
		// 480 / 4, 480 / 4);
		//
		// JList<String> animList =
		// WidgetsUtil.createAnimationMemoryList(mapModel
		// .getCurrentLevel().getAnimationMemory());
		// WidgetFrame animationList = new WidgetFrame(animList);

		// Map map = level.getMap();

		// for (int i = 0; i < 10; i++) {
		// TextureEntity floor = new TextureEntity();
		// floor.setTextureKey("floor_png");
		// floor.setPosition(new Point(200 * i, 200));
		// floor.initialize(editor.getInitializeEngine());
		// floor.createRectangleEntityPoints();
		// map.addEntity(floor);
		//
		// Smoke smoke = new Smoke();
		// smoke.setEntityName("Smokjes " + i);
		// smoke.setTextureKey("smoke_png");
		// smoke.setPosition(new Point(200 * i, 200));
		// smoke.setParticleCount(5);
		// smoke.setMaxRadius(150);
		// smoke.initialize(editor.getInitializeEngine());
		// smoke.createRectangleEntityPoints();
		// map.addEntity(smoke);
		// }
	}

	public List<Class<? extends Notificator>> getNotificationTypes() {
		List<Class<? extends Notificator>> types = new ArrayList<Class<? extends Notificator>>();
		types.add(WildcardNotificationType.class);
		return types;
	}

	public void receiveNotification(Notification notification,
			Notificator notificator,
			Class<? extends Notificator> notificatorType) {
		log.debug("\n" + notification);
	}
}
