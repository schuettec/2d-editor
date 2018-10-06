package de.schuette.cobra2D.workbench.runtime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResourceRegistry {

	protected String[] fileSuffixes;
	protected File ressourceFolder;

	protected List<File> resourceFiles;

	/**
	 * Constructs a image registry on the given resource folder
	 * 
	 * @param ressourceFolder
	 *            The resource folder containing the images.
	 */
	public ResourceRegistry(String[] fileSuffixes, File ressourceFolder) {
		super();
		if (fileSuffixes == null || fileSuffixes.length == 0)
			throw new IllegalArgumentException(
					"File suffixes cannot be null or empty.");
		if (ressourceFolder != null && !ressourceFolder.isDirectory())
			throw new IllegalArgumentException(
					"Ressource folder must be a valid folder in filesystem!");
		this.fileSuffixes = fileSuffixes;
		this.ressourceFolder = ressourceFolder;

		refreshImageRegistry();
	}

	/**
	 * Synchronizes the image registry with the filesystem.
	 */
	public void refreshImageRegistry() {
		this.resourceFiles = loadImageRegistry(ressourceFolder);
	}

	/**
	 * Loads the image registry with filenames to supported image resources on
	 * the resource folder.
	 * 
	 * @param ressourceFolder
	 *            The resource folder for this image registry.
	 * @return Returns a new image registry data structure.
	 */
	protected List<File> loadImageRegistry(File ressourceFolder) {
		List<File> imageRegistry = new ArrayList<File>();

		File[] fileList = this.ressourceFolder.listFiles();

		for (File file : fileList) {
			String filename = file.getName();
			for (String suffix : fileSuffixes) {
				if (filename.endsWith(suffix)) {
					imageRegistry.add(file);
					break;
				}
			}
		}

		return imageRegistry;
	}

	/**
	 * Returns the list of all resource files with absolute path.
	 * 
	 * @return
	 */
	public List<File> getResourceFiles() {
		return new ArrayList<File>(resourceFiles);
	}

	/**
	 * Return the list of all resource file with relative path to the given root
	 * directory.
	 * 
	 * @param rootDirectory
	 *            The root directory all paths should become relative to
	 * @return Returns the list of resource filenames relative to the given
	 *         path.
	 */
	public List<String> getResourceFilesRelative(File rootDirectory) {
		String cutOffSequence = rootDirectory.getAbsolutePath()
				+ File.separator;
		ArrayList<String> newList = new ArrayList<String>();
		for (File file : resourceFiles) {
			String relative = file.getAbsolutePath()
					.replace(cutOffSequence, "");
			newList.add(relative);
		}
		return newList;

	}

	@Override
	public String toString() {
		String outStr = "Resource Registry: ";
		List<File> files = getResourceFiles();
		if (files.size() > 0) {
			outStr += "\n";
			for (File f : files) {
				outStr += "~ File: " + f.getName() + ", ";
			}
			if (outStr.length() > 2)
				outStr = outStr.substring(0, outStr.length() - 2);
		} else {
			outStr += "Empty.";
		}
		return outStr;
	}
}
