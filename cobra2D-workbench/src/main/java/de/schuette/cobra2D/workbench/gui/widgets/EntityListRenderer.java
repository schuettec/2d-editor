package de.schuette.cobra2D.workbench.gui.widgets;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.workbench.runtime.ApplicationResources;
import de.schuette.cobra2D.workbench.runtime.WorkbenchRuntime;

public class EntityListRenderer extends JLabel implements
		ListCellRenderer<Entity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EntityListRenderer() {
		setOpaque(true);

		WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
		ApplicationResources application = runtime.getApplicationResources();
		setIcon(application.getEntityIcon());
	}

	public Component getListCellRendererComponent(JList<? extends Entity> list,
			Entity value, int index, boolean isSelected, boolean cellHasFocus) {

		String labelText = "<html><b>Entity: " + value.getEntityName()
				+ "</b><br/><small>Pos: " + value.getPosition().x + ", "
				+ value.getPosition().y + " Size: " + value.getSize().width
				+ "x" + value.getSize().height + " Texture: "
				+ value.getTextureKey() + "</small></html>";
		setText(labelText);

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		return this;

	}

}