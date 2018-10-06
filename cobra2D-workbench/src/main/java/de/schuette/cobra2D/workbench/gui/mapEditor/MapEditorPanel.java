package de.schuette.cobra2D.workbench.gui.mapEditor;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2D.system.Cobra2DLevel;
import de.schuette.cobra2D.workbench.gui.WidgetsUtil;
import de.schuette.cobra2D.workbench.gui.propertyEditor.PropertyEditor;
import de.schuette.cobra2D.workbench.gui.widgets.EntityListModel;
import de.schuette.cobra2D.workbench.gui.widgets.EntityListRenderer;
import de.schuette.cobra2D.workbench.model.MapModel;
import de.schuette.cobra2D.workbench.runtime.ApplicationResources;
import de.schuette.cobra2D.workbench.runtime.WorkbenchRuntime;

public class MapEditorPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JList<Entity> entityList;
	private MapModel mapModel;
	private JSplitPane tabbingToEntityList;

	private JScrollPane cameraListPanel;
	private JList<Entity> cameraList;

	private JScrollPane entityListPanel;
	private JScrollPane noSkilledListPanel;
	private JList<Entity> noskilledList;

	private JButton btnSelectmove;
	private ActionListener selectMoveAction;

	private JButton btnAdd;
	private ActionListener addAction;

	private JButton btnRemove;
	private ActionListener removeAction;

	private JLabel current;

	private CamViewRenderPanel camViewRenderer;
	private MapEditorRenderPanel mapEditorRenderer;

	private AbstractAction selectEntityAction;

	private PropertyEditor propertyEditor;

	private JLabel lblEntityType;

	private JLabel lblPropertiesDesc;

	private JButton btnAddCamera;
	private AbstractAction addCameraAction;

	private JButton btnRemoveCamera;
	private ActionListener removeCameraAction;

	private AbstractAction selectCameraAction;
	private JPanel buttonEntityListContainer;
	private JLabel lblEntityList;

	private JButton btnAddEntity;
	private ActionListener addEntityAction;

	private JButton btnRemovEentity;
	private ActionListener removeEntityAction;
	private JPanel pnlNoSkilledHeader;
	private JLabel label;

	private Action addEntityFromTypeListAction;

	private JList<Class<?>> typeList;
	private JLabel lblPointonmap;

	private JList<String> animationList;
	private Action copyAnimationKeyAction;

	private JButton btnAddAnimation;
	private Action addAnimationAction;

	private JButton btnRemoveAnimation;
	private Action removeAnimationAction;

	private JScrollPane animationScroller;

	/**
	 * Create the panel.
	 */
	public MapEditorPanel(MapModel mapModel, Cobra2DEngine engine) {

		WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
		ApplicationResources application = runtime.getApplicationResources();

		this.mapModel = mapModel;
		setLayout(new BorderLayout(0, 0));

		/*
		 * Build the rendering components
		 */
		camViewRenderer = new CamViewRenderPanel(mapModel.getCurrentLevel()
				.getMap(), engine);
		mapEditorRenderer = new MapEditorRenderPanel(mapModel.getCurrentLevel()
				.getMap());

		/*
		 * Lets build gui
		 */
		JToolBar toolBar = new JToolBar();
		current = new JLabel();
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

		// Adding Toolbar Icons

		btnSelectmove.setIcon(application.getMoveToolIcon());
		btnAdd.setIcon(application.getEditAddIcon());
		btnRemove.setIcon(application.getEditDeleteIcon());
		/*
		 * View Tabs
		 */

		JTabbedPane mapViewToCamView = new JTabbedPane();

		JPanel rendererContainer = new JPanel(new BorderLayout());
		rendererContainer.add(toolBar, BorderLayout.NORTH);

		lblPointonmap = new JLabel("Camera position:");
		toolBar.add(lblPointonmap);
		rendererContainer.add(mapEditorRenderer, BorderLayout.CENTER);

		mapViewToCamView.addTab("Map Editor View",
				application.getPreviewIcon(), rendererContainer);

		JPanel camRendererContainer = new JPanel(new BorderLayout());
		camRendererContainer.add(camViewRenderer, BorderLayout.CENTER);

		mapViewToCamView.addTab("Cam View", application.getPreviewIcon(),
				camRendererContainer);

		JSplitPane previewToEntityList = new JSplitPane();
		previewToEntityList.setOneTouchExpandable(true);
		// previewToEntityList.setPreferredSize(new Dimension(400, 25));
		previewToEntityList
				.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		add(previewToEntityList);

		tabbingToEntityList = new JSplitPane();
		// tabbingToEntityList.setPreferredSize(new Dimension(400, 25));
		tabbingToEntityList.setOrientation(JSplitPane.VERTICAL_SPLIT);
		previewToEntityList.setLeftComponent(tabbingToEntityList);
		previewToEntityList.setRightComponent(mapViewToCamView);

		// Add cam list container
		JPanel buttonCameraListContainer = new JPanel(new FlowLayout(
				FlowLayout.LEFT));
		buttonCameraListContainer.setBorder(new EtchedBorder(
				EtchedBorder.LOWERED, null, null));
		JLabel lblCamerasDefinedFor = new JLabel(
				"<html><b>Cameras defined for this level:</b></html>",
				application.getCameraSmallIcon(), SwingConstants.LEFT);
		// lblCamerasDefinedFor.setPreferredSize(new Dimension(280, 35));
		lblCamerasDefinedFor.setMinimumSize(new Dimension(171, 14));
		buttonCameraListContainer.add(lblCamerasDefinedFor);

		btnAddCamera = new JButton(application.getEditAddSmallIcon());
		btnAddCamera.setActionCommand("add.camera");
		btnAddCamera.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (addCameraAction != null) {
					addCameraAction.actionPerformed(e);
				}
			}
		});
		btnAddCamera.setToolTipText("Add a new camera entity to the map.");
		buttonCameraListContainer.add(btnAddCamera);

		btnRemoveCamera = new JButton(application.getEditRemoveSmallIcon());
		btnRemoveCamera.setActionCommand("remove.camera");
		btnRemoveCamera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (removeCameraAction != null) {
					removeCameraAction.actionPerformed(e);
				}
			}
		});
		btnRemoveCamera.setToolTipText("Remove a camera entity to the map.");
		buttonCameraListContainer.add(btnRemoveCamera);

		cameraListPanel = new JScrollPane();
		cameraListPanel
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// cameraListPanel.setPreferredSize(new Dimension(320, 200));
		cameraListPanel.setColumnHeaderView(buttonCameraListContainer);

		// Add no skilled container
		noSkilledListPanel = new JScrollPane();
		noSkilledListPanel
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// noSkilledListPanel.setPreferredSize(new Dimension(320, 200));

		pnlNoSkilledHeader = new JPanel();
		pnlNoSkilledHeader.setBorder(new EtchedBorder(EtchedBorder.LOWERED,
				null, null));
		label = new JLabel(
				"<html><b>No skilled entities on the map:</b></html>",
				application.getNoskilledSmallIcon(), SwingConstants.LEFT);
		label.setMinimumSize(new Dimension(101, 14));
		label.setPreferredSize(new Dimension(280, 35));

		pnlNoSkilledHeader.add(label);
		{
			FlowLayout flowLayout = (FlowLayout) pnlNoSkilledHeader.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
		}
		pnlNoSkilledHeader.setBorder(new EtchedBorder(EtchedBorder.LOWERED,
				null, null));
		noSkilledListPanel.setColumnHeaderView(pnlNoSkilledHeader);

		// Add the entity type list
		ImageIcon entityIcon = application.getEntityIcon();
		typeList = WidgetsUtil.createTypeList(Entity.class, entityIcon);
		typeList.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {

			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseExited(MouseEvent e) {

			}

			public void mouseEntered(MouseEvent e) {

			}

			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1
						&& e.getClickCount() == 2) {
					if (addEntityFromTypeListAction != null) {
						addEntityFromTypeListAction
								.actionPerformed(new ActionEvent(typeList
										.getSelectedValue(), 0,
										"addEntityFromTypeList"));
					}
				}
			}
		});
		JScrollPane typeScroller = new JScrollPane(typeList);
		typeScroller
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JLabel lblEntityImpls = new JLabel(
				"<html><b>Available entity types:</b></html>",
				application.getEntitySmallIcon(), SwingConstants.LEFT);
		lblEntityImpls.setPreferredSize(new Dimension(280, 35));
		lblEntityImpls.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null,
				null));
		typeScroller.setColumnHeaderView(lblEntityImpls);

		/*
		 * Animation memory list
		 */
		// Add the entity type list
		JPanel animationListContainer = new JPanel(new FlowLayout(
				FlowLayout.LEFT));
		animationListContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED,
				null, null));
		animationList = WidgetsUtil.createAnimationMemoryList(mapModel
				.getCurrentLevel().getAnimationMemory());
		this.animationScroller = new JScrollPane(animationList);
		animationScroller
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JLabel lblAnimationList = new JLabel(
				"<html><b>Animation memory:</b><br/><small>Doubleclick to copy animation key to clipboard.</small></html>",
				application.getAnimationSmallIcon(), SwingConstants.LEFT);
		lblAnimationList.setMinimumSize(new Dimension(171, 14));
		lblAnimationList.setPreferredSize(new Dimension(280, 35));
		animationListContainer.add(lblAnimationList);
		btnAddAnimation = new JButton(application.getEditAddSmallIcon());
		btnAddAnimation.setActionCommand("add.animation");
		btnAddAnimation.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (addAnimationAction != null) {
					addAnimationAction.actionPerformed(e);
				}
			}
		});
		btnAddAnimation
				.setToolTipText("Add a new animation to the animation memory.");
		animationListContainer.add(btnAddAnimation);

		btnRemoveAnimation = new JButton(application.getEditRemoveSmallIcon());
		btnRemoveAnimation.setActionCommand("remove.animation");
		btnRemoveAnimation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (removeAnimationAction != null) {
					e.setSource(animationList.getSelectedValue());
					removeAnimationAction.actionPerformed(e);
				}
			}
		});
		btnRemoveAnimation
				.setToolTipText("Remove an animation from the animation memory.");
		animationListContainer.add(btnRemoveAnimation);
		animationScroller.setColumnHeaderView(animationListContainer);

		/*
		 * Adding tabs to the upper tab view
		 */
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		// Add the camera list tab
		ImageIcon cameraIcon = application.getCameraSmallIcon();
		tabbedPane.addTab("Cameras", cameraIcon, cameraListPanel);

		// Add the noskilled list tab
		ImageIcon noSkilledIcon = application.getNoskilledSmallIcon();
		tabbedPane.addTab("No-skilled", noSkilledIcon, noSkilledListPanel);

		// Add the entity list tab
		ImageIcon typeIcon = application.getEntitySmallIcon();
		tabbedPane.addTab("Entity Types", typeIcon, typeScroller);

		// Add the animation list tab
		ImageIcon animationIcon = application.getAnimationSmallIcon();
		tabbedPane.addTab("Animation Memory", animationIcon, animationScroller);
		/*
		 * Continue...
		 */
		tabbingToEntityList.setLeftComponent(tabbedPane);

		entityListPanel = new JScrollPane();
		entityListPanel
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// entityListPanel.setPreferredSize(new Dimension(320, 300));

		tabbingToEntityList.setRightComponent(entityListPanel);

		buttonEntityListContainer = new JPanel();
		buttonEntityListContainer.setBorder(new EtchedBorder(
				EtchedBorder.LOWERED, null, null));
		{
			FlowLayout flowLayout = (FlowLayout) buttonEntityListContainer
					.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
		}
		entityListPanel.setColumnHeaderView(buttonEntityListContainer);

		lblEntityList = new JLabel("<html><b>Entities on the map:</b></html>",
				application.getEntityOnMapSmallIcon(), JLabel.LEFT);
		lblEntityList.setMinimumSize(new Dimension(171, 14));
		lblEntityList.setPreferredSize(new Dimension(280, 35));
		buttonEntityListContainer.add(lblEntityList);

		btnAddEntity = new JButton(application.getEditAddSmallIcon());
		btnAddEntity.setActionCommand("remove.add");
		btnAddEntity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (addEntityAction != null) {
					addEntityAction.actionPerformed(e);
				}
			}
		});
		btnAddEntity.setToolTipText("Adds an entity to the map.");
		buttonEntityListContainer.add(btnAddEntity);

		btnRemovEentity = new JButton(application.getEditRemoveSmallIcon());
		btnRemovEentity.setActionCommand("remove.entity");
		btnRemovEentity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (removeEntityAction != null) {
					removeEntityAction.actionPerformed(e);
				}
			}
		});
		btnRemovEentity
				.setToolTipText("Removes the selected entity from the map.");
		buttonEntityListContainer.add(btnRemovEentity);
		tabbingToEntityList.validate();

		updateListViews();

		JPanel propertyEditorContainer = new JPanel(new BorderLayout());
		propertyEditorContainer.setBorder(BorderFactory.createTitledBorder(""));
		add(propertyEditorContainer, BorderLayout.EAST);

		JPanel propertyHeader = new JPanel(new BorderLayout());
		lblPropertiesDesc = new JLabel("Entity Properties:");
		propertyHeader.add(lblPropertiesDesc, BorderLayout.NORTH);

		this.lblEntityType = new JLabel("New label");
		lblEntityType.setFont(new Font("Tahoma", Font.PLAIN, 9));
		propertyHeader.add(lblEntityType, BorderLayout.SOUTH);

		propertyEditorContainer.add(propertyHeader, BorderLayout.NORTH);

		this.propertyEditor = new PropertyEditor();
		// propertyEditor.setPreferredSize(new Dimension(225, 0));
		propertyEditorContainer.add(propertyEditor, BorderLayout.CENTER);

	}

	protected void setCurrentTool(final EditorMode mode) {

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
				ApplicationResources resources = runtime
						.getApplicationResources();

				switch (mode) {
				case ADD:
					current.setIcon(resources.getEditAddIcon());
					break;
				case MOVE:
					current.setIcon(resources.getMoveToolIcon());
					break;
				case REMOVE:
					current.setIcon(resources.getEditDeleteIcon());
					break;
				case MOVE_CAM:
					current.setIcon(resources.getCameraMovementIcon());
					break;
				}
			}
		});
	}

	public void updateListViews() {
		Cobra2DLevel level = mapModel.getCurrentLevel();
		cameraList = WidgetsUtil.createCameraList(level);
		cameraList.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {

			}

			public void mousePressed(MouseEvent e) {
				if (selectCameraAction != null
						&& cameraList.getSelectedValue() != null) {
					selectCameraAction.actionPerformed(new ActionEvent(
							cameraList.getSelectedValue(), 0, "cameraSelected"));
				}
			}

			public void mouseReleased(MouseEvent e) {

			}

			public void mouseEntered(MouseEvent e) {

			}

			public void mouseExited(MouseEvent e) {

			}

		});
		// NoSkilled List
		WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
		ApplicationResources application = runtime.getApplicationResources();

		EntityListModel model = new EntityListModel(level.getMap()
				.getNoSkilledEntities());
		EntityListRenderer renderer = new EntityListRenderer();
		renderer.setIcon(application.getNoskilledIcon());
		noskilledList = new JList<Entity>(model);
		noskilledList.setCellRenderer(renderer);

		animationList = WidgetsUtil.createAnimationMemoryList(mapModel
				.getCurrentLevel().getAnimationMemory());
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
					if (copyAnimationKeyAction != null)
						copyAnimationKeyAction.actionPerformed(new ActionEvent(
								animationList, 0, animationList
										.getSelectedValue()));
				}
			}
		});

		entityList = WidgetsUtil.createEntityList(level);
		entityList.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {

			}

			public void mousePressed(MouseEvent e) {
				Entity selected = entityList.getSelectedValue();
				if (selected != null)
					selectEntityAction.actionPerformed(new ActionEvent(
							selected, 0, "entitySelected"));
			}

			public void mouseReleased(MouseEvent e) {

			}

			public void mouseEntered(MouseEvent e) {

			}

			public void mouseExited(MouseEvent e) {

			}

		});

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				animationScroller.setViewportView(animationList);
				animationScroller.validate();

				cameraListPanel.setViewportView(cameraList);
				cameraListPanel.validate();

				noSkilledListPanel.setViewportView(noskilledList);
				noSkilledListPanel.validate();

				entityListPanel.setViewportView(entityList);
				entityListPanel.validate();
			}
		});

	}

	public void setAddAnimationAction(Action addAnimationAction) {
		this.addAnimationAction = addAnimationAction;
	}

	public void setRemoveAnimationAction(Action removeAnimationAction) {
		this.removeAnimationAction = removeAnimationAction;
	}

	public void setAddEntityAction(ActionListener addEntityAction) {
		this.addEntityAction = addEntityAction;
	}

	public void setRemoveEntityAction(ActionListener removeEntityAction) {
		this.removeEntityAction = removeEntityAction;
	}

	public void setEditingFinishedAction(Action editingFinishedAction) {
		propertyEditor.setEditingFinishedAction(editingFinishedAction);
	}

	public void setSelectEntityAction(AbstractAction selectEntityAction) {
		this.selectEntityAction = selectEntityAction;
	}

	public void setSelectMoveAction(AbstractAction selectMoveAction) {
		this.selectMoveAction = selectMoveAction;
	}

	public void setAddAction(AbstractAction addAction) {
		this.addAction = addAction;
	}

	public void setRemoveAction(AbstractAction removeAction) {
		this.removeAction = removeAction;
	}

	public void setAddCameraAction(AbstractAction addCameraAction) {
		this.addCameraAction = addCameraAction;
	}

	public void setRemoveCameraAction(ActionListener removeCameraAction) {
		this.removeCameraAction = removeCameraAction;
	}

	public CamViewRenderPanel getCamViewRenderer() {
		return camViewRenderer;
	}

	public CamIgnoringRenderPanel getMapEditorRenderer() {
		return mapEditorRenderer;
	}

	public void setAddEntityFromTypeListAction(
			Action addEntityFromTypeListAction) {
		this.addEntityFromTypeListAction = addEntityFromTypeListAction;
	}

	public JList<Entity> getCameraList() {
		return cameraList;
	}

	public void setSelectedCamera(Entity camera) {
		setEntityForPropertyEditor(camera);
		cameraList.setSelectedValue(camera, true);
	}

	public void setCopyAnimationKeyAction(Action copyAnimationKeyAction) {
		this.copyAnimationKeyAction = copyAnimationKeyAction;
	}

	public void setSelectedEntity(Entity selectedEntity) {
		if (selectedEntity != null) {
			setEntityForPropertyEditor(selectedEntity);
		}

		// Set the selected entity in the list an scroll to the selected
		// item
		entityList.setSelectedValue(selectedEntity, true);
		// Set selected entity in renderer to highlight it.
		mapEditorRenderer.setSelectedEntity(selectedEntity);
	}

	public void setEntityForPropertyEditor(Entity entity) {
		// Set entity to property editor
		Class<?> entityType = entity.getClass();
		lblEntityType.setText(entityType.getCanonicalName());
		propertyEditor.setEntity(entity);
	}

	public void setSelectCameraAction(AbstractAction selectCameraAction) {
		this.selectCameraAction = selectCameraAction;

	}

	public void setPointOnMap(Point pointOnMap) {
		lblPointonmap.setText("Camera position: " + pointOnMap.x + ", "
				+ pointOnMap.y);
	}
}
