package de.schuette.cobra2D.workbench.gui.widgets;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import de.schuette.cobra2D.workbench.runtime.ApplicationResources;
import de.schuette.cobra2D.workbench.runtime.WorkbenchRuntime;

public class TypeListRenderer extends JLabel implements
		ListCellRenderer<Class<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TypeListRenderer() {
		setOpaque(true);
		WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
		ApplicationResources application = runtime.getApplicationResources();
		setIcon(application.getEntityIcon());
	}

	public Component getListCellRendererComponent(
			JList<? extends Class<?>> list, Class<?> value, int index,
			boolean isSelected, boolean cellHasFocus) {

		String labelText = "<html>" + value.getSimpleName() + "<br/><small>"
				+ value.getCanonicalName() + "</small></html>";
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