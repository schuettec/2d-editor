package de.schuette.cobra2D.workbench.gui.entityEditor;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import de.schuette.cobra2D.workbench.gui.entityPointsEditor.EntityPointsEditor;
import de.schuette.cobra2D.workbench.model.MapModel;

public class EntityEditorWidget extends JFrame implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntityEditor entityEditor;
	private EntityPointsEditor pointsEditor;
	private MapModel mapModel;
	private EntityModel entityModel;

	public EntityEditorWidget(MapModel mapModel, EntityModel model)
			throws Exception {

		this.mapModel = mapModel;
		this.entityModel = model;

		entityEditor = new EntityEditor(mapModel, model);
		pointsEditor = new EntityPointsEditor(
				entityEditor.getPreviewRenderer(),
				entityEditor.getEntityModel());

		entityEditor.setEntityPointEditor(pointsEditor);
		this.addWindowListener(this);
		this.setBounds(200, 100, 1200, 900);
		this.setLayout(new BorderLayout());
		this.add(pointsEditor.getView(), BorderLayout.NORTH);
		this.add(entityEditor.getView(), BorderLayout.CENTER);
		this.setVisible(true);

	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public EntityModel getEntityModel() {
		return entityModel;
	}

	@Override
	public void dispose() {
		entityEditor.finish();
		super.dispose();
	}

	public void windowOpened(WindowEvent e) {

	}

	public void windowClosing(WindowEvent e) {
		this.dispose();
	}

	public void windowClosed(WindowEvent e) {

	}

	public void windowIconified(WindowEvent e) {
		entityEditor.finish();
	}

	public void windowDeiconified(WindowEvent e) {
		entityEditor.finish();

	}

	public void windowActivated(WindowEvent e) {

	}

	public void windowDeactivated(WindowEvent e) {

	}
}
