package de.schuette.cobra2D.workbench.gui.mapEditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.List;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.skills.Renderable;
import de.schuette.cobra2D.map.Map;
import de.schuette.cobra2DSandbox.camera.CameraUtil;

public class MapEditorRenderPanel extends CamIgnoringRenderPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Entity selectedEntity;

	protected int rasterindex;

	public MapEditorRenderPanel(Map map) {
		super(map);
	}

	public Entity getSelectedEntity() {
		return selectedEntity;
	}

	public void setSelectedEntity(Entity selectedEntity) {
		this.selectedEntity = selectedEntity;
	}

	@Override
	protected void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		Graphics2D graphics = (Graphics2D) gr;
		Color oldColor = graphics.getColor();
		Stroke oldStroke = graphics.getStroke();

		List<Renderable> visibleRenderable = map.getVisibleRenderable(
				pointOnMap.x, pointOnMap.y, getWidth(), getHeight());

		Rectangle viewport = new Rectangle(pointOnMap, getSize());
		for (Renderable r : visibleRenderable) {
			Entity e = (Entity) r;

			if (selectedEntity == e) {
				Stroke stroke = new BasicStroke(4, BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_BEVEL, 10, new float[] { 10 },
						rasterindex);
				rasterindex = (rasterindex + 1) % 1000;

				graphics.setColor(Color.CYAN);
				graphics.setStroke(stroke);
			} else {
				graphics.setColor(Color.RED);
			}

			if (e instanceof Renderable) {

				if (isDrawEntityLines()) {
					CameraUtil.drawEntityLines(e, viewport.x, viewport.y,
							graphics);
				}
				if (isDrawEntityPoints()) {
					CameraUtil.drawEntityPoints(e, viewport.x, viewport.y,
							graphics);
				}

				graphics.setColor(Color.CYAN);
				if (isDrawEntityCenterPoint()) {
					CameraUtil.drawCenterPoint(e, viewport.x, viewport.y,
							graphics);
				}

			}
			// Point gPointStart = Math2D.getRelativePointTranslation(
			// selectedEntity.getPosition(), getViewPortOnMap());
			// g.drawRect(gPointStart.x, gPointStart.y,
			// selectedEntity.getSize().width,
			// selectedEntity.getSize().height);

			graphics.setStroke(oldStroke);
			graphics.setColor(oldColor);
		}
	}
}
