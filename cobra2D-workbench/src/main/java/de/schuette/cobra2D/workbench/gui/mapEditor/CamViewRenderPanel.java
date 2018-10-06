package de.schuette.cobra2D.workbench.gui.mapEditor;

import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import de.schuette.cobra2D.map.Map;
import de.schuette.cobra2D.rendering.PanelRenderer;
import de.schuette.cobra2D.system.Cobra2DEngine;

public class CamViewRenderPanel extends PanelRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map map;
	private Cobra2DEngine engine;

	public CamViewRenderPanel(Map map, final Cobra2DEngine engine) {
		this.map = map;
		this.engine = engine;

		this.addComponentListener(new ComponentListener() {

			public void componentShown(ComponentEvent e) {
				CamViewRenderPanel.this.initializeRenderer(engine, getWidth(),
						getHeight(), 32, 0, false);
			}

			public void componentResized(ComponentEvent e) {
				if (getWidth() >= 100 && getHeight() >= 100) {
					CamViewRenderPanel.this.initializeRenderer(engine,
							getWidth(), getHeight(), 32, 0, false);
				}
			}

			public void componentMoved(ComponentEvent e) {

			}

			public void componentHidden(ComponentEvent e) {

			}
		});
	}

	@Override
	protected void paintComponent(Graphics gr) {
		// Only for gui designer compatibility
		if (map != null) {
			map.sendVisibleObjectToCameras();
		}
		super.paintComponent(gr);
	}
}
