package de.schuette.cobra2D.workbench.gui.mainframe;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class MapEditorFrame extends JFrame implements WindowListener {

	private JPanel contentPane;

	private Action newLevelAction;
	private Action openLevelAction;
	private Action saveLevelAction;
	private Action saveAsLevelAction;
	private Action exportLevelAction;
	private Action exitLevelAction;
	private Action showImageMemoryAction;
	private Action showAnimationMemoryAction;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MapEditorFrame frame = new MapEditorFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MapEditorFrame() {
		this.addWindowListener(this);

		this.setBounds(200, 100, 1200, 900);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNewLevel = new JMenuItem("New Level");
		mntmNewLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (newLevelAction != null)
					newLevelAction.actionPerformed(e);
			}
		});
		mnFile.add(mntmNewLevel);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmOpenLevel = new JMenuItem("Open Level...");
		mntmOpenLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (openLevelAction != null)
					openLevelAction.actionPerformed(e);
			}
		});
		mnFile.add(mntmOpenLevel);

		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);

		JMenuItem mntmSaveLevel = new JMenuItem("Save Level");
		mntmSaveLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (saveLevelAction != null)
					saveLevelAction.actionPerformed(e);
			}
		});
		mnFile.add(mntmSaveLevel);

		JMenuItem mntmSaveLevelAs = new JMenuItem("Save Level As...");
		mntmSaveLevelAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (saveAsLevelAction != null)
					saveAsLevelAction.actionPerformed(e);
			}
		});
		mnFile.add(mntmSaveLevelAs);

		JSeparator separator_2 = new JSeparator();
		mnFile.add(separator_2);

		JMenuItem mntmExport = new JMenuItem("Export...");
		mntmExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (exportLevelAction != null)
					exportLevelAction.actionPerformed(e);
			}
		});
		mnFile.add(mntmExport);

		JSeparator separator_3 = new JSeparator();
		mnFile.add(separator_3);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (exitLevelAction != null)
					exitLevelAction.actionPerformed(e);
			}
		});
		mnFile.add(mntmExit);

		JMenu mnAnalyse = new JMenu("Tools");
		menuBar.add(mnAnalyse);

		JMenuItem mntmShowImageMemory = new JMenuItem("Show image memory...");
		mntmShowImageMemory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (showImageMemoryAction != null)
					showImageMemoryAction.actionPerformed(e);
			}
		});
		mnAnalyse.add(mntmShowImageMemory);

		JMenuItem mntmShowAnimationMemory = new JMenuItem(
				"Show animation memory...");
		mntmShowAnimationMemory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (showAnimationMemoryAction != null)
					showAnimationMemoryAction.actionPerformed(e);
			}
		});
		mnAnalyse.add(mntmShowAnimationMemory);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		setVisible(true);
	}

	public void windowOpened(WindowEvent e) {

	}

	public void windowClosing(WindowEvent e) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				if (exitLevelAction != null)
					exitLevelAction.actionPerformed(new ActionEvent(this, 0,
							"mainframe-closing"));
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

	public void setNewLevelAction(Action newLevelAction) {
		this.newLevelAction = newLevelAction;
	}

	public void setOpenLevelAction(Action openLevelAction) {
		this.openLevelAction = openLevelAction;
	}

	public void setSaveLevelAction(Action saveLevelAction) {
		this.saveLevelAction = saveLevelAction;
	}

	public void setSaveAsLevelAction(Action saveAsLevelAction) {
		this.saveAsLevelAction = saveAsLevelAction;
	}

	public void setExportLevelAction(Action exportLevelAction) {
		this.exportLevelAction = exportLevelAction;
	}

	public void setExitLevelAction(Action exitLevelAction) {
		this.exitLevelAction = exitLevelAction;
	}

	public void setShowImageMemoryAction(Action showImageMemoryAction) {
		this.showImageMemoryAction = showImageMemoryAction;
	}

	public void setShowAnimationMemoryAction(Action showAnimationMemoryAction) {
		this.showAnimationMemoryAction = showAnimationMemoryAction;
	}

}
