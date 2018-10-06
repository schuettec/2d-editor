package de.schuette.cobra2D.workbench.gui.widgets;

import java.util.List;

import javax.swing.DefaultListModel;

import de.schuette.cobra2D.workbench.runtime.TypeRegistry;
import de.schuette.cobra2D.workbench.runtime.WorkbenchRuntime;

public class TypeListModel extends DefaultListModel<Class<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Class<?> type;
	List<Class<?>> availableItems;

	public TypeListModel(Class<?> type) {
		this.type = type;
		WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
		TypeRegistry types = runtime.getTypeRegistry();
		this.availableItems = types.getImplementationsByType(type);

		setKeyword(null);
	}

	public void setKeyword(String keyword) {
		this.clear();

		if (keyword != null && keyword.length() > 0) {
			for (Class<?> item : availableItems) {
				String qualifiedName = item.getCanonicalName();
				if (qualifiedName != null && qualifiedName.contains(keyword)) {
					this.addElement(item);
				}
			}
		} else {
			for (Class<?> item : availableItems) {
				this.addElement(item);
			}
		}

	}

}
