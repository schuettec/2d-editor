package de.schuette.cobra2D.workbench.gui.entityEditor;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.log4j.Logger;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.EntityPoint;
import de.schuette.cobra2D.workbench.gui.entityPointsEditor.EntityPointsEditor;
import de.schuette.cobra2D.workbench.gui.mapEditor.CamIgnoringRenderPanel;
import de.schuette.cobra2D.workbench.gui.mapEditor.RenderTimer;
import de.schuette.cobra2D.workbench.model.MapModel;

public class EntityEditor {// implements MapListener {

	protected Logger log = Logger.getLogger(EntityEditor.class);

	protected MapModel mapModel;
	protected EntityModel entityModel;

	protected EntityEditorPanel view;

	private EntityPointsEditor pointsEditor;

	protected RenderTimer mapEditorRenderTimer;

	protected Action selectImageAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			if (entityModel != null) {
				String textureKey = e.getActionCommand();

				Entity entity = entityModel.getEntity();
				entity.changeTexture(textureKey);

				List<EntityPoint> pointList = entity.getPointList();
				pointList.clear();
				pointList.add(new EntityPoint(90, 50, entity));
				pointList.add(new EntityPoint(180, 200, entity));

				if (pointsEditor != null) {
					// Send points editor a message that texture has changed
					pointsEditor.updateAnchors();
				}
				view.updatePropertyEditor();

				// Reset Preview to replace map updates
				// resetPreview();
			}
		}
	};

	protected Action editingFinished = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			log.info("Property changed for entity. Restarting preview...");

			if (pointsEditor != null) {
				// Send points editor a message that texture has changed
				pointsEditor.updateAnchors();
			}

			// Reinitialize entity
			mapModel.reinitializeEntity(entityModel.getEntity());

			// Reset Preview to replace map updates
			// resetPreview();
			resetViewport();
		}

	};

	public EntityEditor(MapModel mapModel, EntityModel entityModel)
			throws EntityEditorException {
		super();
		this.mapModel = mapModel;
		this.entityModel = entityModel;
		this.view = new EntityEditorPanel(entityModel, mapModel);
		try {
			view.setEditingFinishedAction(editingFinished);
			view.setSelectImageAction(selectImageAction);
		} catch (Exception e) {
			throw new EntityEditorException(e.getMessage(), e);
		}

		// Create render timers
		mapEditorRenderTimer = new RenderTimer(view.getPreviewRenderer(), 10);
		// Start rendering
		mapEditorRenderTimer.startRendering();
		resetViewport();
		// startPreviewEngine();
	}

	private void resetViewport() {
		CamIgnoringRenderPanel preview = view.getPreviewRenderer();
		preview.setPointOnMap(entityModel.getEntity().getPosition());
	}

	public void finish() {
		mapEditorRenderTimer.stopRendering();
	}

	public CamIgnoringRenderPanel getPreviewRenderer() {
		return view.getPreviewRenderer();
	}

	public EntityModel getEntityModel() {
		return entityModel;
	}

	/**
	 * This method is used to add a default entity point to the edited entity
	 * but only if there are no entity points defined yet.
	 */
	protected void addDefaultEntityPoint(Entity entity) {
		if (entity.getPointList().isEmpty()) {
			entity.createMiddleEntityPoint();
		}
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public EntityEditorPanel getView() {
		return view;
	}

	/**
	 * This method is used to send texture changed events to the points
	 * editor...Hack hack hack...
	 * 
	 * @param pointsEditor
	 */
	public void setEntityPointEditor(EntityPointsEditor pointsEditor) {
		this.pointsEditor = pointsEditor;
	}

}
