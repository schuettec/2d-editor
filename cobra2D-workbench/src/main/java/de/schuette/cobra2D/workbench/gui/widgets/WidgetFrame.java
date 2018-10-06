package de.schuette.cobra2D.workbench.gui.widgets;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public abstract class WidgetFrame extends JFrame implements WindowListener {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	public WidgetFrame(Component component) {

		this.addWindowListener(this);
		this.setBounds(200, 100, 1200, 900);
		this.setLayout(new BorderLayout());
		this.add(component, BorderLayout.CENTER);
		this.setVisible(true);

	}

	/**
	 * This method is called if the window is closing. Use this method to stop
	 * any working threads.
	 */
	protected abstract void finish();

	public void windowOpened(WindowEvent e) {

	}

	public void windowClosing(WindowEvent e) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				finish();

				setVisible(false);
				dispose();
			}
		});
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {

	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {

	}
}