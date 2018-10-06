package de.schuette.cobra2D.workbench.gui.widgets;

import java.util.ArrayList;
import java.util.List;

import de.schuette.cobra2D.workbench.runtime.InstanceUtil;
import de.schuette.cobra2D.workbench.runtime.TypeRegistry;
import de.schuette.cobra2D.workbench.runtime.WorkbenchRuntime;

public class EditableTypeListModel extends TypeListModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EditableTypeListModel(Class<?> type) {
		super(type);
		this.availableItems = new ArrayList<Class<?>>();
		WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
		TypeRegistry types = runtime.getTypeRegistry();
		List<Class<?>> typeList = types.getImplementationsByType(type);

		for (Class<?> c : typeList) {
			if (InstanceUtil.isEditable(c)) {
				availableItems.add(c);
			}
		}

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
