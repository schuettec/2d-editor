package de.schuette.cobra2D.workbench.gui.animationEditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import de.schuette.cobra2DSandbox.texture.editing.PreviewPanel;

public class AnimationPreviewPanel extends PreviewPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int frameWidth;
	protected int frameHeight;
	protected int frameCountX;
	protected int frameCountY;

	protected Color drawColor = Color.CYAN;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (image != null) {
			Color lastColor = g.getColor();
			g.setColor(drawColor);

			// Draw animation raster.
			for (int y = 0; y <= frameCountY; y++) {
				Point last = null;
				for (int x = 0; x <= frameCountX; x++) {
					// Start point prepare
					if (last == null) {
						last = new Point(previewPoint.x, previewPoint.y + y
								* frameHeight);
					} else {
						int posX = previewPoint.x + (x * frameWidth);
						int posY = previewPoint.y + (y * frameHeight);

						g.drawLine(last.x, last.y, posX, posY);

						last = new Point(posX, posY);
					}
				}
			}

			for (int x = 0; x <= frameCountX; x++) {
				Point last = null;
				for (int y = 0; y <= frameCountY; y++) {
					// Start point prepare
					if (last == null) {
						last = new Point(previewPoint.x + x * frameWidth,
								previewPoint.y);
					} else {
						int posX = previewPoint.x + (x * frameWidth);
						int posY = previewPoint.y + (y * frameHeight);

						g.drawLine(last.x, last.y, posX, posY);

						last = new Point(posX, posY);
					}
				}
			}

			g.drawRect(previewPoint.x, previewPoint.y,
					frameCountX * frameWidth, frameCountY * frameHeight);

			g.setColor(lastColor);
		}
	}

	public void setAnimationProperties(int frameWidth, int frameHeight,
			int frameCountX, int frameCountY) {
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.frameCountX = frameCountX;
		this.frameCountY = frameCountY;

		repaint();
	}

}
