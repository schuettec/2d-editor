package de.schuette.cobra2D.workbench.gui.widgets;

import java.util.List;

import javax.swing.DefaultListModel;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.skills.Camera;

public class EntityListModel extends DefaultListModel<Entity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Class<?>[] filteredTypes = new Class<?>[] { Camera.class };

	public EntityListModel(List<Entity> allEntities) {
		for (Entity element : allEntities) {
			this.addElement(element);
		}

	}
}
