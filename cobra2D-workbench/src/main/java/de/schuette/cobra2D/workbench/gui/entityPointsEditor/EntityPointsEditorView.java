package de.schuette.cobra2D.workbench.gui.entityPointsEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import de.schuette.cobra2D.workbench.runtime.ApplicationResources;
import de.schuette.cobra2D.workbench.runtime.WorkbenchRuntime;

public class EntityPointsEditorView extends JPanel {

	private JButton btnSelectmove;
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnRectangle;
	private JButton btnMiddlepoint;
	private JLabel current;
	private Action selectMoveAction;
	private Action addAction;
	private Action rectangleAction;
	private Action middlepointAction;
	private Action removeAction;

	/**
	 * Create the panel.
	 */
	protected EntityPointsEditorView() {
		setLayout(new BorderLayout(0, 0));

		JToolBar toolBar = new JToolBar();
		toolBar.setPreferredSize(new Dimension(100, 55));

		add(toolBar);

		current = new JLabel();
		setCurrentTool(EditorMode.MOVE);
		toolBar.add(current);
		toolBar.add(new JSeparator());

		btnSelectmove = new JButton("Select/Move");
		btnSelectmove.setActionCommand("move");
		btnSelectmove.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				selectMoveAction.actionPerformed(e);
			}
		});
		btnSelectmove
				.setToolTipText("Select/Move an entity point in the prerender view.");
		toolBar.add(btnSelectmove);

		btnAdd = new JButton("Add");
		btnAdd.setActionCommand("add");
		btnAdd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				addAction.actionPerformed(e);
			}
		});
		btnAdd.setToolTipText("Add an entity point by clicking into the prerender view.");
		toolBar.add(btnAdd);

		btnRemove = new JButton("Remove");
		btnRemove.setActionCommand("remove");
		btnRemove.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				removeAction.actionPerformed(e);
			}
		});
		btnRemove
				.setToolTipText("Remove an entity point by clicking a point in the prerender view.");
		toolBar.add(btnRemove);

		btnRectangle = new JButton("Create Rectangle");
		btnRectangle.setActionCommand("rectangle");
		btnRectangle.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				rectangleAction.actionPerformed(e);
			}
		});
		btnRectangle
				.setToolTipText("Create a rectangle shape of entity points based on the texture size.");
		toolBar.add(btnRectangle);

		btnMiddlepoint = new JButton("Create center point");
		btnMiddlepoint.setActionCommand("middle");
		btnMiddlepoint.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				middlepointAction.actionPerformed(e);
			}
		});
		btnMiddlepoint
				.setToolTipText("Create an entity point in the middle of the entity.");
		toolBar.add(btnMiddlepoint);

		setButtonIcons();
	}

	protected void setCurrentTool(EditorMode mode) {
		WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
		ApplicationResources resources = runtime.getApplicationResources();

		switch (mode) {
		case ADD:
			current.setIcon(resources.getAddToolIcon());
			break;
		case MOVE:
			current.setIcon(resources.getMoveToolIcon());
			break;
		case REMOVE:
			current.setIcon(resources.getRemoveToolIcon());
		default:
			break;
		}
	}

	private void setButtonIcons() {
		WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
		ApplicationResources resources = runtime.getApplicationResources();

		btnSelectmove.setIcon(resources.getMoveToolIcon());
		btnAdd.setIcon(resources.getAddToolIcon());
		btnRemove.setIcon(resources.getRemoveToolIcon());
		btnRectangle.setIcon(resources.getRectangleToolIcon());
		btnMiddlepoint.setIcon(resources.getMiddleToolIcon());
	}

	protected void setSelectMoveAction(Action action) {
		this.selectMoveAction = action;
	}

	protected void setAddAction(Action action) {
		this.addAction = action;
	}

	protected void setRemoveAction(Action action) {
		this.removeAction = action;
	}

	protected void setRectangleAction(Action action) {
		this.rectangleAction = action;
	}

	protected void setMiddlepointAction(Action action) {
		this.middlepointAction = action;
	}
}
