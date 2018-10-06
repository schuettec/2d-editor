package de.schuette.cobra2D.workbench.gui.propertyEditor;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import de.schuette.cobra2D.workbench.runtime.ApplicationResources;
import de.schuette.cobra2D.workbench.runtime.WorkbenchRuntime;

public class LabelRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		String label = (String) value;

		WorkbenchRuntime instance = WorkbenchRuntime.getInstance();
		ApplicationResources applicationResources = instance
				.getApplicationResources();
		this.setIcon(applicationResources.getPropertyIcon());
		this.setText(label);

		return this;
	}
}
