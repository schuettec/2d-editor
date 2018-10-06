package de.schuette.cobra2D.workbench.gui.entityPointsEditor;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.EntityPoint;
import de.schuette.cobra2D.math.Line;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.workbench.gui.entityEditor.EntityModel;
import de.schuette.cobra2D.workbench.runtime.ApplicationResources;

public class EntityPointsEditor implements MouseListener, ComponentListener {

	protected JPanel renderview;
	protected EntityPointsEditorView view;
	protected EntityModel model;
	protected EditorMode mode;

	class EntityPointAnchor extends AnchorComponent {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected EntityPoint point;

		public EntityPointAnchor(ImageIcon image) {
			super(image);
		}

		public void setEntityPoint(EntityPoint point) {
			this.point = point;

			// Point middle = new Point(
			// Math2D.saveRound(renderview.getSize().width / 2.0),
			// Math2D.saveRound(renderview.getSize().height / 2.0));
			// double radius = point.getRadius();
			// double degrees = (point.getDegrees() + point.getEntity()
			// .getDegrees());
			// Point circle = Math2D.getCircle(middle, radius, degrees);
			// setPosition(circle);
			Entity entity = point.getEntity();
			Point epoint = point.getCurrentPosition();
			int xm = epoint.x
					// + Math2D.saveRound(renderview.getSize().width / 2.0)
					- Math2D.saveRound(getSize().width / 2.0)
					- entity.getPosition().x;
			int ym = epoint.y
					// + Math2D.saveRound(renderview.getSize().height / 2.0)
					- Math2D.saveRound(getSize().height / 2.0)
					- entity.getPosition().y;
			this.setLocation(new Point(xm, ym));
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);

			Entity entity = model.getEntity();
			List<EntityPoint> pointList = entity.getPointList();

			if (pointList.size() > 1) {
				if (mode == EditorMode.REMOVE) {
					pointList.remove(point);
					updateAnchors();
				}
			} else {
				JOptionPane
						.showMessageDialog(
								renderview,
								"You are about to remove the last entity point.\nAt least one entity point is needed to display the entity through a camera. ",
								"Cannot remove point", JOptionPane.OK_OPTION);
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (mode == EditorMode.MOVE) {
				super.mouseDragged(e);
				// Calc new entity point position
				Point pos = this.getLocation();
				updateEntityPoint(pos);
			}
		}

		private void updateEntityPoint(Point pos) {
			pos.x += Math2D.saveRound(this.getSize().width / 2.0)
					+ point.getEntity().getPosition().x;
			pos.y += Math2D.saveRound(this.getSize().height / 2.0)
					+ point.getEntity().getPosition().y;
			// pos.x -= Math2D.saveRound(renderview.getSize().width / 2.0);
			// pos.y -= Math2D.saveRound(renderview.getSize().height / 2.0);
			// pos.x += Math2D.saveRound(point.getEntity().getSize().width /
			// 2.0);
			// pos.y += Math2D.saveRound(point.getEntity().getSize().height /
			// 2.0);
			point.setCurrentPosition(pos);
			point.getEntity().resetPoints();
		}

	};

	protected AbstractAction selectMoveAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			mode = EditorMode.MOVE;
			view.setCurrentTool(mode);
		}
	};
	protected AbstractAction addAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			mode = EditorMode.ADD;
			view.setCurrentTool(mode);
		}
	};
	protected AbstractAction removeAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			mode = EditorMode.REMOVE;
			view.setCurrentTool(mode);
		}
	};
	protected AbstractAction rectangleAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			Entity entity = model.getEntity();
			entity.getPointList().clear();
			entity.createRectangleEntityPoints();
			updateAnchors();
		}
	};
	protected AbstractAction middleAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			Entity entity = model.getEntity();
			entity.getPointList().clear();
			entity.createMiddleEntityPoint();
			updateAnchors();
		}
	};

	public EntityPointsEditor(JPanel renderview, EntityModel model) {
		this.renderview = renderview;
		this.renderview.addMouseListener(this);
		this.renderview.addComponentListener(this);

		this.view = new EntityPointsEditorView();

		// Add actions
		this.view.setSelectMoveAction(selectMoveAction);
		this.view.setAddAction(addAction);
		this.view.setRemoveAction(removeAction);
		this.view.setRectangleAction(rectangleAction);
		this.view.setMiddlepointAction(middleAction);

		this.model = model;

		this.mode = EditorMode.MOVE;
		this.view.setCurrentTool(mode);

		updateAnchors();
	}

	public void updateAnchors() {
		// Remove all anchors first
		renderview.removeAll();

		ApplicationResources resource = new ApplicationResources();
		ImageIcon crossIcon = resource.getCrossToolIcon();

		Entity entity = model.getEntity();
		List<EntityPoint> pointList = entity.getPointList();
		for (int i = 0; i < pointList.size(); i++) {
			EntityPointAnchor anchor = new EntityPointAnchor(crossIcon);
			renderview.add(anchor);
			EntityPoint p = pointList.get(i);
			anchor.setEntityPoint(p);
			// anchor.updateEntityPoint(anchor.getLocation());
		}
	}

	public EntityPointsEditorView getView() {
		return view;
	}

	public void componentResized(ComponentEvent e) {
		updateAnchors();
	}

	public void componentMoved(ComponentEvent e) {

	}

	public void componentShown(ComponentEvent e) {
		updateAnchors();
	}

	public void componentHidden(ComponentEvent e) {

	}

	public void mouseClicked(MouseEvent e) {
		if (mode == EditorMode.ADD) {
			// Calc new entity point position
			Entity entity = model.getEntity();
			Point pos = e.getPoint();
			Point middle = new Point(
					Math2D.saveRound(renderview.getSize().width / 2.0),
					Math2D.saveRound(renderview.getSize().height / 2.0));
			Line controlLine = new Line(pos, middle);
			/*
			 * Find the line between entity points that intersects the control
			 * line with the highest radius.
			 */

			int pCollideIndex = -1;
			double maxDistance = 0.0;
			// Go through all entity points
			List<EntityPoint> pointList = entity.getPointList();
			for (int i = 0; i < pointList.size(); i++) {
				Point start;
				Point end;
				// Get startpoint of line
				{
					EntityPoint p = pointList.get(i);
					double pDegrees = p.getDegrees();
					double pRadius = p.getRadius();
					start = Math2D.getCircle(middle, pRadius, pDegrees);
				}
				// Get endpoint of line
				if (i < pointList.size() - 1) {
					// Get next as endpoint
					EntityPoint p = pointList.get(i + 1);
					double pDegrees = p.getDegrees();
					double pRadius = p.getRadius();
					end = Math2D.getCircle(middle, pRadius, pDegrees);
				} else {
					// Get first point as endpoint
					EntityPoint p = pointList.get(0);
					double pDegrees = p.getDegrees();
					double pRadius = p.getRadius();
					end = Math2D.getCircle(middle, pRadius, pDegrees);
				}

				// Create line between start and end
				Line testLine = new Line(start, end);

				// Test intersection of controlLine and testLine
				Point cross = controlLine.schneidetLinie(testLine);
				if (cross != null) {
					// If the intersection has a higher distance than
					// maxDistance, store the current entity point index as
					// start point.
					double distance = Math2D.getEntfernung(middle, cross);
					if (distance >= maxDistance) {
						pCollideIndex = i;
						maxDistance = distance;
					}
				}

			}

			// If no intersection of control line was found, add point elsewhere
			int radius = Math2D.saveRound(Math2D.getEntfernung(pos, middle));
			double degrees = Math2D.getAngle(middle, pos);
			EntityPoint newpoint = new EntityPoint(degrees, radius, entity);
			if (pCollideIndex == -1) {
				entity.getPointList().add(newpoint);
			} else {
				entity.getPointList().add(pCollideIndex + 1, newpoint);
			}
			updateAnchors();
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}
