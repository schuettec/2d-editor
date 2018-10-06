package de.schuette.cobra2D.workbench.runtime;

import java.awt.image.RGBImageFilter;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.skills.AlwaysVisible;
import de.schuette.cobra2D.entity.skills.Camera;
import de.schuette.cobra2D.entity.skills.Moveable;
import de.schuette.cobra2D.entity.skills.Obstacle;
import de.schuette.cobra2D.entity.skills.Renderable;
import de.schuette.cobra2D.ressource.ImageMemory;
import de.schuette.cobra2D.system.Cobra2DConstants;
import de.schuette.cobra2D.system.Cobra2DConstants.RessourceType;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2D.workbench.notification.Notification;
import de.schuette.cobra2DSandbox.modifier.Modifier;

public class RuntimeConstants {
	private static Logger log = Logger.getLogger(RuntimeConstants.class);

	public static String ROOT_PATH_STR;

	public static String RESOURCE_PATH_STR = "game-resources";
	public static final String IMAGE_FOLDER_STR = "textures";

	public static final Class<?>[] TYPE_DEFINITIONS = new Class<?>[] {
			Entity.class, AlwaysVisible.class, Camera.class, Moveable.class,
			Obstacle.class, Renderable.class, RGBImageFilter.class,
			Modifier.class };

	/*
	 * Variables available after setup()
	 */
	private static RessourceType RESOURCE_TYPE;
	public static File ROOT_PATH;
	public static File RESOURCE_PATH;
	public static File IMAGE_RESOURCE_PATH;

	public static void setup(String[] args) {

		// Setting the variables in RuntimeConstants
		if (parametersContains("classpath", args)) {
			RESOURCE_TYPE = RessourceType.CLASSPATH;
			Cobra2DEngine.setupEnvironment(RESOURCE_TYPE);
			ROOT_PATH_STR = "./src/main/resources/";
		} else if (parametersContains("currentDir", args)) {
			RESOURCE_TYPE = RessourceType.INSTALL_DIR;
			Cobra2DEngine.setupEnvironment(RESOURCE_TYPE);
			ROOT_PATH_STR = ".";
		} else {
			System.out
					.println("Start this runtime environment with option -classpath to load all resources from your classpath or with -currentDir to load all resources from current directory ./game-resources/.");
			System.exit(1);
		}

		ROOT_PATH = new File(ROOT_PATH_STR);
		RESOURCE_PATH = new File(ROOT_PATH_STR + File.separator
				+ RESOURCE_PATH_STR);
		IMAGE_RESOURCE_PATH = new File(ROOT_PATH_STR + File.separator
				+ RESOURCE_PATH_STR + File.separator + IMAGE_FOLDER_STR);

		log.info("Setting up path settings for runtime.");
		log.info("Resource loading strategy      : " + RESOURCE_TYPE);
		log.info("Root path                (path): " + ROOT_PATH.getPath());
		log.info("Root path            (absolute): "
				+ ROOT_PATH.getAbsolutePath());
		log.info("Resource path            (path): " + RESOURCE_PATH.getPath());
		log.info("Resource path        (absolute): "
				+ RESOURCE_PATH.getAbsolutePath());
		log.info("Image resource path      (path): "
				+ IMAGE_RESOURCE_PATH.getPath());
		log.info("Image resource path  (absolute): "
				+ IMAGE_RESOURCE_PATH.getAbsolutePath());
	}

	/*
	 * Start argument identifier
	 */

	public static final String RESOURCES = "resources";

	/*
	 * Constants you should not care of.
	 */

	public static final String NO_PICTURE_APP_RESOURCE = "application-resources/no-resource.png";
	public static final String ENTITY_TYPE_LIST_ITEM = "application-resources/entity-list-item.png";
	public static final String EDIT_ICON = "application-resources/edit-icon.png";

	public static final String MOVE_TOOL_ICON = "application-resources/select-tool.png";
	public static final String ADD_TOOL_ICON = "application-resources/add-tool.png";
	public static final String REMOVE_TOOL_ICON = "application-resources/remove-tool.png";
	public static final String RECTANGLE_TOOL_ICON = "application-resources/rectangle-tool.png";
	public static final String MIDDLE_TOOL_ICON = "application-resources/middle-tool.png";
	public static final String CROSS_TOOL_ICON = "application-resources/cross.png";
	public static final String STOP_ICON = "application-resources/animation-stop.png";
	public static final String START_ICON = "application-resources/animation-start.png";
	public static final String OK_ICON = "application-resources/apply.png";
	public static final String CAMERA_ICON = "application-resources/camera.png";
	public static final String ENTITY_SMALL_ICON = "application-resources/entity-list-item-small.png";
	public static final String ENTITY_ON_MAP_ICON = "application-resources/entity-on-map.png";
	public static final String CAMERA_SMALL_ICON = "application-resources/camera-small.png";
	public static final String ENTITY_ON_MAP_SMALL_ICON = "application-resources/entity-on-map-small.png";
	public static final String NO_SKILLED_SMALL_ICON = "application-resources/noskilled-small.png";
	public static final String NO_SKILLED_ICON = "application-resources/noskilled.png";
	public static final String PROPERTY_ICON = "application-resources/property-icon.png";
	public static final String PREVIEW_ICON = "application-resources/preview.png";
	public static final String CAMERA_MOVEMENT_ICON = "application-resources/camera-movement.png";
	public static final String EDIT_ADD_ICON = "application-resources/edit-add.png";
	public static final String EDIT_DELETE_ICON = "application-resources/edit-delete.png";
	public static final String EDIT_ADD_SMALL_ICON = "application-resources/edit-add-small.png";
	public static final String EDIT_REMOVE_SMALL_ICON = "application-resources/edit-remove-small.png";
	public static final String ANIMATION_ICON = "application-resources/animationIcon.png";
	public static final String ANIMATION_SMALL_ICON = "application-resources/animationIconSmall.png";

	public static Properties getEnginePropertiesMapUpdates() {
		final Properties engineConfiguration = new Properties();
		engineConfiguration.put(Cobra2DConstants.RESOLUTION_X, "1024"); // 1920
		engineConfiguration.put(Cobra2DConstants.RESOLUTION_Y, "768"); // 1080
		engineConfiguration.put(Cobra2DConstants.BIT_DEPHT, "32");
		engineConfiguration.put(Cobra2DConstants.REFRESH_REATE, "60");
		engineConfiguration.put(Cobra2DConstants.REQUESTED_FPS, "100");
		engineConfiguration.put(Cobra2DConstants.FULLSCREEN, "false");
		engineConfiguration.put(Cobra2DConstants.MAP_UPDATE, "true");
		engineConfiguration.put(Cobra2DConstants.USE_RENDERER, "true");
		engineConfiguration.put(Cobra2DConstants.DEFAULT_CONTROLLER, "false");

		return engineConfiguration;
	}

	public static Properties getEnginePropertiesNoMapUpdates() {
		final Properties engineConfiguration = new Properties();
		engineConfiguration.put(Cobra2DConstants.RESOLUTION_X, "300"); // 1920
		engineConfiguration.put(Cobra2DConstants.RESOLUTION_Y, "300"); // 1080
		engineConfiguration.put(Cobra2DConstants.BIT_DEPHT, "32");
		engineConfiguration.put(Cobra2DConstants.REFRESH_REATE, "60");
		engineConfiguration.put(Cobra2DConstants.REQUESTED_FPS, "100");
		engineConfiguration.put(Cobra2DConstants.FULLSCREEN, "false");
		engineConfiguration.put(Cobra2DConstants.MAP_UPDATE, "false");
		engineConfiguration.put(Cobra2DConstants.USE_RENDERER, "true");
		engineConfiguration.put(Cobra2DConstants.DEFAULT_CONTROLLER, "false");

		return engineConfiguration;
	}

	public static Properties getEnginePropertiesNoMapUpdatesNoRenderer() {
		final Properties engineConfiguration = new Properties();
		engineConfiguration.put(Cobra2DConstants.RESOLUTION_X, "300"); // 1920
		engineConfiguration.put(Cobra2DConstants.RESOLUTION_Y, "300"); // 1080
		engineConfiguration.put(Cobra2DConstants.BIT_DEPHT, "32");
		engineConfiguration.put(Cobra2DConstants.REFRESH_REATE, "60");
		engineConfiguration.put(Cobra2DConstants.REQUESTED_FPS, "100");
		engineConfiguration.put(Cobra2DConstants.FULLSCREEN, "false");
		engineConfiguration.put(Cobra2DConstants.MAP_UPDATE, "false");
		engineConfiguration.put(Cobra2DConstants.USE_RENDERER, "false");
		engineConfiguration.put(Cobra2DConstants.DEFAULT_CONTROLLER, "false");

		return engineConfiguration;
	}

	/**
	 * This method is used to create a uniform notification format to send
	 * exceptions via notification manager.
	 * 
	 * @param e
	 *            The exception to send via notification
	 * @param source
	 *            The source of the exception, or an object for further
	 *            debugging.
	 * @return The prepared notification. Use this instance to add further
	 *         informations about this exception.
	 */
	public static Notification createExceptionNotification(String userTitle,
			String userMessage, Exception e, Object source) {
		// Notification System - Send entity selected
		Notification notification = new Notification();
		notification.put("eventName", "exception");
		notification.put("source", source);
		notification.put("exception", e);
		notification.put("title", userTitle);
		notification.put("message", userMessage);
		return notification;
	}

	public static boolean isExceptionNotification(Notification notification) {
		return (notification.containsKey("eventName")
				&& notification.containsKey("source")
				&& notification.containsKey("exception")
				&& notification.containsKey("title") && notification
					.containsKey("message"));
	}

	public static Exception getExceptionFromNotification(
			Notification notification) {
		if (isExceptionNotification(notification)) {
			try {
				return (Exception) notification.get("exception");
			} catch (Throwable t) {
				throw new IllegalArgumentException(
						"Notification is not in the correct format for exception notfication.");
			}
		} else {
			throw new IllegalArgumentException(
					"Notification is not in the correct format for exception notfication.");
		}
	}

	public static String getTitleFromNotification(Notification notification) {
		if (isExceptionNotification(notification)) {
			try {
				return (String) notification.get("title");
			} catch (Throwable t) {
				throw new IllegalArgumentException(
						"Notification is not in the correct format for exception notfication.");
			}
		} else {
			throw new IllegalArgumentException(
					"Notification is not in the correct format for exception notfication.");
		}
	}

	public static String getMessageFromNotification(Notification notification) {

		if (isExceptionNotification(notification)) {
			try {
				return (String) notification.get("message");
			} catch (Throwable t) {
				throw new IllegalArgumentException(
						"Notification is not in the correct format for exception notfication.");
			}
		} else {
			throw new IllegalArgumentException(
					"Notification is not in the correct format for exception notfication.");
		}
	}

	public static void logAnPresentExceptionNotificationToUser(Logger logger,
			Notification notification) {
		if (isExceptionNotification(notification)) {
			JOptionPane.showMessageDialog(null,
					getMessageFromNotification(notification),
					getTitleFromNotification(notification),
					JOptionPane.ERROR_MESSAGE);
			logger.error(getMessageFromNotification(notification),
					getExceptionFromNotification(notification));
		}
	}

	public static void loadImageMemoryFromRuntime(ImageMemory imageMemory)
			throws Exception {
		WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
		ResourceRegistry imageRegistry = runtime.getImageRegistry();
		List<File> imageFiles = imageRegistry.getResourceFiles();

		for (File file : imageFiles) {
			String address = file.getName().replace('.', '_');
			File fileRelative;
			fileRelative = runtime.fileToRelativeFile(file,
					RuntimeConstants.ROOT_PATH);
			String path = fileRelative.getPath().replace(File.separatorChar,
					'/');
			String urlString = "resource:" + path;
			try {
				URL resourceURL = new URL(urlString);
				imageMemory.loadImage(address, resourceURL);
			} catch (MalformedURLException e) {
				throw new Exception("Cannot load " + fileRelative.getPath()
						+ " from the resource folder to image memory.");
			}

		}
	}

	private static boolean parametersContains(String argument, String[] args) {

		for (String str : args) {
			if (str.equalsIgnoreCase("-" + argument))
				return true;
		}

		return false;
	}
}
