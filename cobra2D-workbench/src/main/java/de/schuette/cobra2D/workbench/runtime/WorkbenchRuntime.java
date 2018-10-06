package de.schuette.cobra2D.workbench.runtime;

import java.awt.Color;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.log4j.Logger;

import de.schuette.cobra2D.workbench.notification.NotificationManager;

public class WorkbenchRuntime {
	private static Logger log = Logger.getLogger(WorkbenchRuntime.class);

	private static WorkbenchRuntime instance;

	public static WorkbenchRuntime getInstance() {
		if (instance == null) {
			instance = new WorkbenchRuntime();
		}
		return instance;
	}

	protected TypeRegistry typeRegistry;
	protected ResourceRegistry imageRegistry;
	protected ApplicationResources applicationResources;

	protected NotificationManager notificationSystem;

	private WorkbenchRuntime() {
	}

	private void initialize() {
		typeRegistry = new TypeRegistry(RuntimeConstants.TYPE_DEFINITIONS);
		typeRegistry.loadClasspathTypeRegistry();

		imageRegistry = new ResourceRegistry(ImageIO.getReaderFileSuffixes(),
				RuntimeConstants.IMAGE_RESOURCE_PATH);

		applicationResources = new ApplicationResources();

		notificationSystem = new NotificationManager();
	}

	public void setupNimbusLookAndFeel() {

		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			log.debug("Found look and feel: " + info.getName());
		}

		boolean success = false;
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(info.getName())) {
				try {
					// UIManager.put("nimbusBase", Color.CYAN);
					UIManager.put("nimbusBlueGrey", Color.WHITE);
					UIManager.put("control", Color.white);
					UIManager.put("controlLHighlight", Color.BLACK);

					UIManager.setLookAndFeel(info.getClassName());
					log.info("Nimbus as supported look and feel successfully initialized.");
					success = true;
				} catch (Exception e) {
					log.warn(
							"Cannot set Nimbus look and feel due to an error.",
							e);
					success = false;
				}
				break;
			}
		}

		if (!success) {
			log.warn("Cannot set Nimbus look and feel because it is not installed on your Java JRE.");
		}
	}

	/**
	 * Returns a file object representing the given file with relative path to
	 * the given root directory.
	 * 
	 * @param file
	 *            The file which path is absolute.
	 * @param rootDirectory
	 *            The root directory to which the file should be relative to.
	 * @return The file object that is relative to the root directory.
	 */
	public File fileToRelativeFile(File file, File rootDirectory) {
		String cutOffSequence = rootDirectory.getAbsolutePath()
				+ File.separator;
		String relative = file.getAbsolutePath().replace(cutOffSequence, "");

		return new File(relative);

	}

	public NotificationManager getNotificationSystem() {
		return notificationSystem;
	}

	public ApplicationResources getApplicationResources() {
		return applicationResources;
	}

	public TypeRegistry getTypeRegistry() {
		return typeRegistry;
	}

	public ResourceRegistry getImageRegistry() {
		return imageRegistry;
	}

	@Override
	public String toString() {
		String outStr = "Workbench Runtime:\n - " + typeRegistry.toString()
				+ "\n - Image " + imageRegistry.toString();
		return outStr;
	}

	public void setup(String[] args) {

		// Setup RuntimeConstants to build paths etc.
		RuntimeConstants.setup(args);

		// Initialize this runtime
		initialize();
	}

}
