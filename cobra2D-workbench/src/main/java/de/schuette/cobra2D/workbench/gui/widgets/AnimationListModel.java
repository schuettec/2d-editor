package de.schuette.cobra2D.workbench.gui.widgets;

import java.util.HashMap;

import javax.swing.DefaultListModel;

import de.schuette.cobra2D.ressource.Animation;
import de.schuette.cobra2D.ressource.AnimationMemory;

public class AnimationListModel extends DefaultListModel<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	AnimationMemory dataSource;

	public AnimationListModel(AnimationMemory dataSource) {
		this.dataSource = dataSource;
		setKeyword(null);
	}

	public void setKeyword(String keyword) {
		this.clear();
		HashMap<String, Animation> images = dataSource.getAnimations();

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
