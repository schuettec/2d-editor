package de.schuette.cobra2D.workbench.gui.propertyEditor;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import de.schuette.cobra2D.entity.editing.PropertyEditor;

public class ValueRenderer implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		PropertyEditor editor = (PropertyEditor) value;

		JComponent displayComponent = editor.getDisplayComponent();

		return displayComponent;
	}
}
