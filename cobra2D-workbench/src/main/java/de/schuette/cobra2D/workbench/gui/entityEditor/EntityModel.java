package de.schuette.cobra2D.workbench.gui.entityEditor;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.workbench.runtime.InstanceUtil;

public class EntityModel {
	protected Class<? extends Entity> entityType;
	protected Entity entity;

	/**
	 * This constructor is used to create a new instance of an entity. It is
	 * used if the entity editor was launched to create a new entity.
	 * 
	 * @param entityType
	 * @throws Exception
	 */
	public EntityModel(Class<? extends Entity> entityType) throws Exception {
		boolean editable = InstanceUtil.isEditable(entityType);
		if (!editable) {
			throw new EntityEditorException(
					"The chosen entity type is not editable. Choose another entity type to edit.");
		}
		this.entityType = entityType;
		createEntity();
	}

	public EntityModel(Entity entity) {
		this.entity = entity;
		this.entityType = entity.getClass();
	}

	/**
	 * Create an entity instance of the given entity type;
	 * 
	 * @throws Exception
	 */
	private void createEntity() throws Exception {
		this.entity = InstanceUtil.createDefaultInstance(entityType);
	}

	public Entity getEntity() {
		return entity;
	}

	public Class<? extends Entity> getEntityType() {
		return entityType;
	}

}
