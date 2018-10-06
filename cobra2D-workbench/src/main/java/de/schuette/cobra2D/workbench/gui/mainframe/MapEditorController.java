package de.schuette.cobra2D.workbench.gui.mainframe;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JScrollPane;

import de.schuette.cobra2D.workbench.gui.WidgetsUtil;
import de.schuette.cobra2D.workbench.gui.mapEditor.MapEditor;
import de.schuette.cobra2D.workbench.gui.widgets.WidgetFrame;
import de.schuette.cobra2D.workbench.model.MapModel;

public class MapEditorController {
	private Action newLevelAction;
	private Action openLevelAction;
	private Action saveLevelAction;
	private Action saveAsLevelAction;
	private Action exportLevelAction;
	private Action exitLevelAction;
	private Action showImageMemoryAction = new AbstractAction() {

		public void actionPerformed(ActionEvent e) {
			final JList<String> imageList = WidgetsUtil
					.createImageMemoryDetailedList(model.getCurrentLevel()
							.getImageMemory());

			imageList.addMouseListener(new MouseListener() {

				public void mouseReleased(MouseEvent e) {

				}

				public void mousePressed(MouseEvent e) {

				}

				public void mouseExited(MouseEvent e) {

				}

				public void mouseEntered(MouseEvent e) {

				}

				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() > 1) {
						String imageKey = imageList.getSelectedValue();
						StringSelection selection = new StringSelection(
								imageKey);
						Clipboard clipboard = Toolkit.getDefaultToolkit()
								.getSystemClipboard();
						clipboard.setContents(selection, selection);
					}
				}
			});

			WidgetFrame imageMemoryFrame = new WidgetFrame(new JScrollPane(
					imageList)) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				protected void finish() {
					this.dispose();
				}
			};
			imageMemoryFrame.setTitle("Image Memory - all loaded Resources");
		}
	};

	private Action showAnimationMemoryAction = new AbstractAction() {

		public void actionPerformed(ActionEvent e) {
			final JList<String> animationList = WidgetsUtil
					.createAnimationMemoryList(model.getCurrentLevel()
							.getAnimationMemory());

			animationList.addMouseListener(new MouseListener() {

				public void mouseReleased(MouseEvent e) {

				}

				public void mousePressed(MouseEvent e) {

				}

				public void mouseExited(MouseEvent e) {

				}

				public void mouseEntered(MouseEvent e) {

				}

				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() > 1) {
						String animationKey = animationList.getSelectedValue();
						StringSelection selection = new StringSelection(
								animationKey);
						Clipboard clipboard = Toolkit.getDefaultToolkit()
								.getSystemClipboard();
						clipboard.setContents(selection, selection);
					}
				}
			});

			WidgetFrame imageMemoryFrame = new WidgetFrame(new JScrollPane(
					animationList)) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				protected void finish() {
					this.dispose();
				}
			};
			imageMemoryFrame.setTitle("Image Memory - all loaded Resources");
		}
	};

	private MapModel model;
	private MapEditorFrame view;
	private MapEditor editor;

	public MapEditorController(MapModel model) {
		if (model == null)
			throw new IllegalArgumentException("Map model cannot be null!");
		this.model = model;

		this.editor = new MapEditor(model);
		this.view = new MapEditorFrame();
		view.add(editor.getView(), BorderLayout.CENTER);
		view.setShowImageMemoryAction(showImageMemoryAction);

	}

}
