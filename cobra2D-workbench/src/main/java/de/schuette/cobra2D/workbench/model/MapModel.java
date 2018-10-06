package de.schuette.cobra2D.workbench.model;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2D.system.Cobra2DLevel;
import de.schuette.cobra2D.workbench.gui.mapEditor.CamViewRenderPanel;
import de.schuette.cobra2D.workbench.notification.Notification;
import de.schuette.cobra2D.workbench.notification.NotificationSystem;
import de.schuette.cobra2D.workbench.notification.Notificator;
import de.schuette.cobra2D.workbench.runtime.RuntimeConstants;

public class MapModel implements Notificator {

	protected static Logger log = Logger.getLogger(MapModel.class);

	protected Cobra2DLevel currentLevel;
	private Cobra2DEngine engine;

	private NotificationSystem notificationManager;

	public MapModel() throws Exception {
		currentLevel = new Cobra2DLevel();
		RuntimeConstants.loadImageMemoryFromRuntime(currentLevel
				.getImageMemory());

		createEngineForInit();
	}

	private void createEngineForInit() {
		// Create Engine
		try {
			Properties config = RuntimeConstants
					.getEnginePropertiesNoMapUpdatesNoRenderer();
			this.engine = new Cobra2DEngine(config);
			this.engine.loadLevel(currentLevel);
			// TODO: Uncomment this last change if textures are missing
			// Cobra2DLevel level = this.engine.getLevel();
			// RuntimeConstants.loadImageMemoryFromRuntime(level.getImageMemory());

		} catch (Exception e) {
			log.error("Cannot initialize engine for panel renderer.", e);
		}
	}

	protected void notifyEngineError(Exception engineException) {
		Notification notification = RuntimeConstants
				.createExceptionNotification(
						"Cobra2DEngine initialize error",
						"Cannot initialize Cobra2DEngine due to an error. Please check level, entity implementation or engine configurations.",
						engineException, this);
		notificationManager.notifySubscribers(notification, this);
	}

	public Cobra2DLevel getCurrentLevel() {
		return currentLevel;
	}

	public void setRenderer(CamViewRenderPanel camViewRenderer) {
		this.engine.setRenderer(camViewRenderer);
	}

	public Cobra2DEngine getEngine() {
		return engine;
	}

	public void setNotificationSystem(NotificationSystem manager) {
		this.notificationManager = manager;
	}

	/**
	 * This method is used to reinitialize entities after editing. This is
	 * neccessary to make sure that the entity state matches the property
	 * changes. To do this job, the engine is paused during reinitialization of
	 * the entity.
	 * 
	 * @param entity
	 */
	public void reinitializeEntity(Entity entity) {
		engine.pauseEngine();
		entity.initialize(engine);
		engine.resumeEngine();
	}
}
