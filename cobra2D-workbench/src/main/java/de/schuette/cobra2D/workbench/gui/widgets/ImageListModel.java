package de.schuette.cobra2D.workbench.gui.widgets;

import java.awt.image.VolatileImage;
import java.util.HashMap;

import javax.swing.DefaultListModel;

import de.schuette.cobra2D.ressource.ImageMemory;

public class ImageListModel extends DefaultListModel<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ImageMemory dataSource;

	public ImageListModel(ImageMemory dataSource) {
		this.dataSource = dataSource;
		setKeyword(null);
	}

	public void setKeyword(String keyword) {
		this.clear();
		HashMap<String, VolatileImage> images = dataSource.getImages();

		if (keyword != null && keyword.length() > 0) {
			for (String imageKey : images.keySet()) {
				if (imageKey != null && imageKey.contains(keyword)) {
					this.addElement(imageKey);
				}
			}
		} else {
			for (String imageKey : images.keySet()) {
				this.addElement(imageKey);
			}
		}

	}

}
