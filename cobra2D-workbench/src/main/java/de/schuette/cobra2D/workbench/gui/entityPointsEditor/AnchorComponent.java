package de.schuette.cobra2D.workbench.gui.entityPointsEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import de.schuette.cobra2D.math.Math2D;

public class AnchorComponent extends JLabel implements MouseListener,
		MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean mouseEntered;

	private int yOffset;

	private int xOffset;

	public AnchorComponent(ImageIcon image) {
		super(image);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		if (mouseEntered) {
			graphics.setXORMode(Color.CYAN);
		} else {
			graphics.setXORMode(Color.MAGENTA);
		}

		super.paintComponent(graphics);
	}

	public void setPosition(Point position) {
		int xm = position.x - Math2D.saveRound(getSize().width / 2.0);
		int ym = position.y - Math2D.saveRound(getSize().height / 2.0);
		this.setLocation(new Point(xm, ym));
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		xOffset = e.getX();
		yOffset = e.getY();
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {
		mouseEntered = true;
		repaint();
	}

	public void mouseExited(MouseEvent e) {
		mouseEntered = false;
		repaint();
	}

	public void mouseDragged(MouseEvent e) {
		Point position = new Point(this.getLocation().x + e.getX(),
				this.getLocation().y + e.getY());
		Point newPos = new Point(position.x, position.y);
		setPosition(newPos);
	}

	public void mouseMoved(MouseEvent e) {

	}
}
