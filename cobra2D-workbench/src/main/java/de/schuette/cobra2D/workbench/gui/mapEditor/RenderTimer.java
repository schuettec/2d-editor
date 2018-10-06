package de.schuette.cobra2D.workbench.gui.mapEditor;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

/**
 * @author Chris This class is used to repaint a component in a render cycle.
 *         This is useful to render to Swing components with passive rendering.
 */
public class RenderTimer {

	protected Timer timer;
	protected long frameDelay;
	protected boolean running;
	protected Component component;

	class AnimationTimer extends TimerTask {
		@Override
		public void run() {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					component.repaint();
				}
			});
		}
	}

	/**
	 * Constructs a render timer for the given component.
	 * 
	 * @param component
	 *            The component to repaint in a render cycle.
	 */
	public RenderTimer(Component component, long frameDelay) {
		this.frameDelay = frameDelay;
		this.component = component;
		this.component.addComponentListener(new ComponentListener() {

			public void componentShown(ComponentEvent e) {
				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						startRendering();
					}
				});
			}

			public void componentResized(ComponentEvent e) {

			}

			public void componentMoved(ComponentEvent e) {

			}

			public void componentHidden(ComponentEvent e) {
				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						stopRendering();
					}
				});
			}
		});
	}

	/**
	 * Constructs a render timer for the given component.
	 * 
	 * @param component
	 *            The component to repaint in a render cycle.
	 */
	public RenderTimer(Component component) {
		this(component, 5);
	}

	/**
	 * Stops rendering of the map object to the current renderer.
	 */
	public void stopRendering() {
		if (timer != null) {
			timer.cancel();
			timer = null;
			running = false;
		}
	}

	/**
	 * Starts the rendering of the map object to the current renderer.
	 */
	public void startRendering() {
		if (this.timer == null) {
			this.timer = new Timer();
			timer.schedule(new AnimationTimer(), 0, frameDelay);
			running = true;
		} else {
			stopRendering();
			startRendering();
		}
	}

	/**
	 * Stops and restarts the rendering.
	 */
	protected void resetRendering() {
		stopRendering();
		startRendering();
	}

	/**
	 * @return Returns the current delay in milliseconds between the frames.
	 */
	public long getFrameDelay() {
		return frameDelay;
	}

	/**
	 * @param frameDelay
	 *            Sets the delay between the frames in milliseconds. Used to
	 *            slow down or make animation more smooth.
	 */
	public void setFrameDelay(long frameDelay) {
		this.frameDelay = frameDelay;

		if (running)
			resetRendering();
	}

	/**
	 * @return Returns true if the render cycle is running. False otherwise.
	 */
	public boolean isRunning() {
		return running;
	}

}
