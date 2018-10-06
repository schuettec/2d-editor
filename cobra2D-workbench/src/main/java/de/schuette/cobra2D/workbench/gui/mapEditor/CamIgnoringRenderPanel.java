package de.schuette.cobra2D.workbench.gui.mapEditor;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JPanel;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.skills.Renderable;
import de.schuette.cobra2D.map.Map;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2DSandbox.camera.CameraUtil;

/**
 * @author Chris This renderer is used to render the map ignoring the camera
 *         definition within the map but rendering with an universal map editor
 *         camera.
 */
public class CamIgnoringRenderPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean drawEntityCenterPoint = true;
	protected boolean drawEntityPoints = true;
	protected boolean drawEntityLines = true;
	protected boolean drawEntities = true;

	protected Map map;

	protected Point pointOnMap = new Point(0, 0);

	public CamIgnoringRenderPanel(Map map) {
		if (map == null)
			throw new IllegalArgumentException("Map object cannot be null.");
		this.map = map;
	}

	@Override
	protected void paintComponent(Graphics gr) {
		super.paintComponent(gr);

		Graphics2D graphics = (Graphics2D) gr;

		// // Clear now the screen buffer
		// TODO: Needed?
		// graphics.setColor(Color.BLACK);
		// graphics.fillRect(0, 0, getWidth(), getHeight());

		List<Renderable> visibleRenderable = map.getVisibleRenderable(
				pointOnMap.x, pointOnMap.y, getWidth(), getHeight());

		Rectangle viewport = new Rectangle(pointOnMap, getSize());
		for (Renderable r : visibleRenderable) {
			Entity e = (Entity) r;
			if (e instanceof Renderable) {

				final Renderable renderable = (Renderable) e;
				if (isDrawEntities()) {
					final Point relativePosition = Math2D
							.getRelativePointTranslation(e, viewport);
					renderable.render(graphics, relativePosition);
				}
				if (isDrawEntityLines()) {
					CameraUtil.drawEntityLines(e, viewport.x, viewport.y,
							graphics);
				}
				if (isDrawEntityPoints()) {
					CameraUtil.drawEntityPoints(e, viewport.x, viewport.y,
							graphics);
				}

				if (isDrawEntityCenterPoint()) {
					CameraUtil.drawCenterPoint(e, viewport.x, viewport.y,
							graphics);
				}

			}
		}

	}

	public Rectangle getViewPortOnMap() {
		Rectangle rect = new Rectangle(pointOnMap.x, pointOnMap.y, getWidth(),
				getHeight());
		return rect;
	}

	public boolean isDrawEntityCenterPoint() {
		return drawEntityCenterPoint;
	}

	public void setDrawEntityCenterPoint(boolean drawEntityCenterPoint) {
		this.drawEntityCenterPoint = drawEntityCenterPoint;
	}

	public boolean isDrawEntityPoints() {
		return drawEntityPoints;
	}

	public void setDrawEntityPoints(boolean drawEntityPoints) {
		this.drawEntityPoints = drawEntityPoints;
	}

	public boolean isDrawEntityLines() {
		return drawEntityLines;
	}

	public void setDrawEntityLines(boolean drawEntityLines) {
		this.drawEntityLines = drawEntityLines;
	}

	public boolean isDrawEntities() {
		return drawEntities;
	}

	public void setDrawEntities(boolean drawEntities) {
		this.drawEntities = drawEntities;
	}

	public Point getPointOnMap() {
		return pointOnMap;
	}

	public void setPointOnMap(Point pointOnMap) {
		this.pointOnMap = pointOnMap;
	}

}
