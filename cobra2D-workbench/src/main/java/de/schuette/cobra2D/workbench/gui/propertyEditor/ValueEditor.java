package de.schuette.cobra2D.workbench.gui.propertyEditor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.editing.EditableUtil;
import de.schuette.cobra2D.entity.editing.PropertyEditor;
import de.schuette.cobra2D.workbench.runtime.ApplicationResources;
import de.schuette.cobra2D.workbench.runtime.WorkbenchRuntime;

public class ValueEditor extends AbstractCellEditor implements TableCellEditor,
		TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PropertyEditor editor;
	private Entity entity;
	private JTable table;

	private boolean isButtonColumnEditor;

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		final DefaultTableModel model = (DefaultTableModel) table.getModel();
		this.table = table;
		this.editor = (PropertyEditor) model.getValueAt(row, column - 1);
		this.entity = (Entity) model.getValueAt(row, column);

		WorkbenchRuntime instance = WorkbenchRuntime.getInstance();
		ApplicationResources applicationResources = instance
				.getApplicationResources();
		ImageIcon editIcon = applicationResources.getEditIcon();
		JButton button = new JButton(editIcon);
		button.addActionListener(new ActionListener() {

			/*
			 * The button has been pressed. Stop editing and invoke the custom
			 * Action
			 */
			public void actionPerformed(ActionEvent event) {
				try {
					EditableUtil.showEditor(editor, entity);
					model.fireTableDataChanged();

				} catch (Exception e) {
					JOptionPane
							.showMessageDialog(
									null,
									"Cannot modify the selected value, due to an error.",
									"Editing error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		});

		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(UIManager.getColor("Button.background"));
		}

		return button;
	}

	/*
	 * When the mouse is pressed the editor is invoked. If you then then drag
	 * the mouse to another cell before releasing it, the editor is still
	 * active. Make sure editing is stopped when the mouse is released.
	 */
	// public void mousePressed(MouseEvent e) {
	// if (table.isEditing() && table.getCellEditor() == this)
	// isButtonColumnEditor = true;
	// }
	//
	// public void mouseReleased(MouseEvent e) {
	// if (isButtonColumnEditor && table.isEditing())
	// table.getCellEditor().stopCellEditing();
	//
	// isButtonColumnEditor = false;
	// }
	//
	// public void mouseClicked(MouseEvent e) {
	// }
	//
	// public void mouseEntered(MouseEvent e) {
	// }
	//
	// public void mouseExited(MouseEvent e) {
	// }

	public Object getCellEditorValue() {
		// We always return dat shit, because the "value" never changes.
		return entity;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		WorkbenchRuntime instance = WorkbenchRuntime.getInstance();
		ApplicationResources applicationResources = instance
				.getApplicationResources();
		ImageIcon editIcon = applicationResources.getEditIcon();
		JButton button = new JButton(editIcon);

		return button;
	}
}
