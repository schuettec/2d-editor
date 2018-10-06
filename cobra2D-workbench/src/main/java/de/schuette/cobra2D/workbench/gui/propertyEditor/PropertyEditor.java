package de.schuette.cobra2D.workbench.gui.propertyEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.editing.EditableUtil;

public class PropertyEditor extends JPanel implements TableModelListener {
	private static final String EDITING_FINISHED_ACTION_COMMAND = "editing-finished";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	private Action editingFinishedAction;

	public PropertyEditor() {
		setLayout(new BorderLayout(0, 0));
		this.setMinimumSize(new Dimension(225, 10));
		table = new JTable();
		table.setRowHeight(40);
		add(table);
	}

	public void setEntity(Entity entity) {
		// Build table column names
		Vector<String> columnNames = new Vector<String>();
		columnNames.add("Property");
		columnNames.add("Value");
		columnNames.add("Edit");

		// Build data
		@SuppressWarnings("rawtypes")
		Vector data = new Vector();

		TableModel dataModel = new DefaultTableModel(data, columnNames) {
			/**
		     * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 2;
			}
		};
		dataModel.addTableModelListener(this);

		Class<?> clazz = entity.getClass();
		HashMap<Class<?>, Field[]> editableFields = EditableUtil
				.getEditableFields(clazz);

		Iterator<Class<?>> it = editableFields.keySet().iterator();
		while (it.hasNext()) {
			Class<?> inheritedFrom = it.next();
			Field[] fields = editableFields.get(inheritedFrom);

			if (fields.length == 0) {
				// Add nothing for this origin
				continue;
			} else {
				for (Field field : fields) {
					de.schuette.cobra2D.entity.editing.PropertyEditor editor = EditableUtil
							.getEditor(field, entity);

					Vector row = new Vector();
					row.add(field.getName());
					row.add(editor);
					row.add(entity);
					data.add(row);
				}
			}

		}
		// Set the model
		table.setModel(dataModel);

		// Set the renderers and editors
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setCellRenderer(new LabelRenderer());
		columnModel.getColumn(1).setCellRenderer(new ValueRenderer());
		ValueEditor valueEditor = new ValueEditor();
		columnModel.getColumn(2).setCellRenderer(valueEditor);
		columnModel.getColumn(2).setCellEditor(valueEditor);
		columnModel.getColumn(2).setPreferredWidth(40);

		columnModel.getColumn(0).setPreferredWidth(100);
		columnModel.getColumn(1).setPreferredWidth(100);
		columnModel.getColumn(2).setPreferredWidth(25);
		columnModel.getColumn(2).setMinWidth(25);
		columnModel.getColumn(2).setMaxWidth(25);
	}

	public void update() {
		table.repaint();
	}

	public void setEditingFinishedAction(Action editingFinishedAction) {
		this.editingFinishedAction = editingFinishedAction;
	}

	public void tableChanged(TableModelEvent e) {
		if (editingFinishedAction != null) {
			Entity entity = (Entity) table.getModel().getValueAt(
					e.getFirstRow(), 2);
			editingFinishedAction.actionPerformed(new ActionEvent(entity, 0,
					EDITING_FINISHED_ACTION_COMMAND));
		}
	}

}
