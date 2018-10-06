package de.schuette.cobra2D.workbench.gui.mapEditor;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JList;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.skills.Camera;
import de.schuette.cobra2D.entity.skills.Renderable;
import de.schuette.cobra2D.map.Map;
import de.schuette.cobra2D.map.MapListener;
import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.ressource.AnimationMemory;
import de.schuette.cobra2D.system.Cobra2DEngine;
import de.schuette.cobra2D.system.Cobra2DLevel;
import de.schuette.cobra2D.workbench.gui.WidgetsUtil;
import de.schuette.cobra2D.workbench.gui.animationEditor.AnimationEditor;
import de.schuette.cobra2D.workbench.gui.animationEditor.AnimationEditorWidget;
import de.schuette.cobra2D.workbench.model.MapModel;
import de.schuette.cobra2D.workbench.notification.Notification;
import de.schuette.cobra2D.workbench.notification.NotificationManager;
import de.schuette.cobra2D.workbench.notification.NotificationSubscriber;
import de.schuette.cobra2D.workbench.notification.NotificationSystem;
import de.schuette.cobra2D.workbench.notification.Notificator;
import de.schuette.cobra2D.workbench.runtime.InstanceUtil;
import de.schuette.cobra2D.workbench.runtime.RuntimeConstants;
import de.schuette.cobra2D.workbench.runtime.WorkbenchRuntime;

public class MapEditor implements MapListener, MouseMotionListener,
		MouseListener, Notificator, NotificationSubscriber {
	protected static final Logger log = Logger.getLogger(MapEditor.class);

	protected MapEditorPanel view;
	protected MapModel mapModel;
	protected EditorMode mode;
	private EditorMode lastTool;

	protected RenderTimer mapEditorRenderTimer;
	protected RenderTimer camViewRenderTimer;

	// Mouse dragging offset
	protected int yOffset;
	protected int xOffset;

	// Mouse dragging offset
	protected int xOffsetOnEntity;
	protected int yOffsetOnEntity;
	// Entity dragging will set the following switch to true, so the mouse
	// release event, that marks another entity can be skipped
	protected boolean wasEntityDragging = false;

	// Selected entity
	protected Entity selectedEntity;

	// MapEditor Options
	protected MapEditorSettings settings;
	private NotificationSystem manager;

	protected AbstractAction addAnimationAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			addAnimation();
		}
	};
	protected AbstractAction removeAnimationAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			String animationKey = (String) e.getSource();
			removeAnimation(animationKey);
		}
	};
	protected AbstractAction addEntityFromTypeListAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			addEntity((Class<?>) e.getSource());
		}
	};

	protected AbstractAction addEntityAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			addEntity();
		}
	};

	protected AbstractAction removeEntityAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			removeEntity();
		}
	};

	protected AbstractAction editingFinishedAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			Entity entity = (Entity) e.getSource();
			mapModel.reinitializeEntity(entity);
			notifyEntityEditingFinished(entity);
		}
	};
	protected AbstractAction removeCameraAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			removeCamera();
		}

	};
	protected AbstractAction addCameraAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			addCamera();
		}

	};

	protected AbstractAction selectCameraAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			Entity entity = (Entity) e.getSource();
			view.setSelectedCamera(entity);
		}
	};
	protected AbstractAction selectEntityAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			Entity entity = (Entity) e.getSource();
			setSelectedEntity(entity);

			// FOCUS THE SELECTED ENTITY
			if (settings.isFocusEntityOnListSelect()) {
				CamIgnoringRenderPanel mapEditorRenderer = view
						.getMapEditorRenderer();
				Point renderPointOnMap = mapEditorRenderer.getPointOnMap();
				Point entityPos = entity.getPosition();
				renderPointOnMap.x = entityPos.x
						- Math2D.saveRound(mapEditorRenderer.getWidth() / 2.0)
						+ Math2D.saveRound(entity.getSize().width / 2.0);
				renderPointOnMap.y = entityPos.y
						- Math2D.saveRound(mapEditorRenderer.getHeight() / 2.0)
						+ Math2D.saveRound(entity.getSize().height / 2.0);
			}
		}
	};

	protected AbstractAction selectMoveAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			setMode(EditorMode.MOVE);
		}
	};
	protected AbstractAction addAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			setMode(EditorMode.ADD);
		}
	};
	protected AbstractAction removeAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			setMode(EditorMode.REMOVE);
		}
	};
	protected AbstractAction copyAnimationKeyAction = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			String animationKey = e.getActionCommand();
			StringSelection selection = new StringSelection(animationKey);
			Clipboard clipboard = Toolkit.getDefaultToolkit()
					.getSystemClipboard();
			clipboard.setContents(selection, selection);
		}
	};

	public MapEditor(MapModel mapModel) {

		// Notification system
		WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
		NotificationManager notificationSystem = runtime
				.getNotificationSystem();
		notificationSystem.addSubscriber(this);

		// Create Settings
		settings = new MapEditorSettings();

		// Register map editor to notification manager
		notificationSystem.addNotificator(MapEditor.this);

		this.mapModel = mapModel;
		this.view = new MapEditorPanel(mapModel, mapModel.getEngine());
		this.mapModel.setRenderer(this.view.getCamViewRenderer());
		// Add actions
		this.view.setSelectMoveAction(selectMoveAction);
		this.view.setAddAction(addAction);
		this.view.setRemoveAction(removeAction);
		this.view.setSelectEntityAction(selectEntityAction);
		this.view.setAddCameraAction(addCameraAction);
		this.view.setRemoveCameraAction(removeCameraAction);
		this.view.setSelectCameraAction(selectCameraAction);
		this.view.setEditingFinishedAction(editingFinishedAction);
		this.view.setRemoveEntityAction(removeEntityAction);
		this.view.setAddEntityAction(addEntityAction);
		this.view.setAddEntityFromTypeListAction(addEntityFromTypeListAction);
		this.view.setAddAnimationAction(addAnimationAction);
		this.view.setRemoveAnimationAction(removeAnimationAction);
		this.view.setCopyAnimationKeyAction(copyAnimationKeyAction);

		// Add Mouse control on map editor panel
		CamIgnoringRenderPanel mapEditorRenderer = view.getMapEditorRenderer();
		mapEditorRenderer.addMouseMotionListener(this);
		mapEditorRenderer.addMouseListener(this);

		setMode(EditorMode.MOVE);

		Map map = mapModel.getCurrentLevel().getMap();
		map.addMapListener(this);

		// Create render timers
		mapEditorRenderTimer = new RenderTimer(view.getMapEditorRenderer(), 10);
		camViewRenderTimer = new RenderTimer(view.getCamViewRenderer(), 10);
		// Start rendering
		mapEditorRenderTimer.startRendering();
		camViewRenderTimer.startRendering();

		// Set point on map in view
		Point pointOnMap = mapEditorRenderer.getPointOnMap();
		view.setPointOnMap(pointOnMap);
	}

	private void addAnimation() {
		try {
			AnimationEditorWidget widget = new AnimationEditorWidget(mapModel);
		} catch (Exception e) {
			Notification notification = RuntimeConstants
					.createExceptionNotification(
							"Animation editor error",
							"Cannot start the animation editor due to an error.",
							e, this);
			RuntimeConstants.logAnPresentExceptionNotificationToUser(log,
					notification);
		}
	}

	private void removeAnimation(String animationKey) {
		if (animationKey == null || animationKey.trim().length() == 0) {
			JOptionPane.showMessageDialog(view,
					"Please select an animation in the list before removing.",
					"Remove animation", JOptionPane.WARNING_MESSAGE);
		} else {
			if (settings.isAskBeforeRemoval()) {
				int answer = JOptionPane.showConfirmDialog(view,
						"Are you sure to delete this animation?",
						"Delete animation", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.NO_OPTION) {
					return;
				}
			}

			Cobra2DLevel currentLevel = mapModel.getCurrentLevel();
			AnimationMemory animations = currentLevel.getAnimationMemory();
			animations.remove(animationKey);

			// Notification System - Send animation added event
			WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
			NotificationManager notificationSystem = runtime
					.getNotificationSystem();
			notificationSystem.addNotificator(MapEditor.this);
			Notification notification = new Notification();
			notification.put("eventName", "AnimationRemoved");
			notification.put("mapModel", mapModel);
			manager.notifySubscribers(notification, MapEditor.this);

			// Update the list
			view.updateListViews();
		}

	}

	protected void setMode(EditorMode mode) {
		this.mode = mode;
		view.setCurrentTool(mode);
	}

	public MapEditorPanel getView() {
		return view;
	}

	public void entityAdded(Entity entity) {
		view.updateListViews();
	}

	public void entityRemoved(Entity entity) {
		view.updateListViews();
	}

	public void beforeUpdate() {
	}

	public void afterUpdate() {
	}

	public Entity getSelectedEntity() {
		return selectedEntity;
	}

	public void setSelectedEntity(Entity selectedEntity) {
		// Set selection in the view
		this.selectedEntity = selectedEntity;
		this.view.setSelectedEntity(selectedEntity);

		// Notification
		notifyEntitySelected(selectedEntity);
	}

	protected void notifyEntitySelected(Entity selectedEntity) {
		// Notification System - Send entity selected
		Notification notification = new Notification();
		notification.put("eventName", "entitySelected");
		notification.put("mapModel", mapModel);
		notification.put("selectedEntity", selectedEntity);
		manager.notifySubscribers(notification, MapEditor.this);
	}

	private void notifyEntityEditingFinished(Entity entity) {
		// Notification System - Send entity selected
		Notification notification = new Notification();
		notification.put("eventName", "entityEditingFinished");
		notification.put("mapModel", mapModel);
		notification.put("entity", entity);
		manager.notifySubscribers(notification, MapEditor.this);
	}

	private void notifyException(String title, String message, Exception e,
			Object source) {
		Notification notification = RuntimeConstants
				.createExceptionNotification(title, message, e, source);
		manager.notifySubscribers(notification, MapEditor.this);
	}

	private void addEntity(Point point, Class<?> clazz) {
		if (clazz == null) {
			clazz = WidgetsUtil.showEntityTypeSelection();
		}
		if (clazz != null) {
			try {
				final Entity instance = (Entity) InstanceUtil
						.createDefaultInstance(clazz);
				if (point != null) {
					instance.setPosition(point);
				}

				if (instance instanceof Renderable) {
					try {
						WidgetsUtil.createEntityEditor(mapModel, instance);
					} catch (Exception e) {
						notifyException("Open entity editor",
								"Error opening entity editor", e,
								MapEditor.this);
					}
				} else {
					JOptionPane
							.showMessageDialog(
									view,
									"This entity cannot be edited in the entity editor, because it is not renderable.\nTo edit properties of this entity select it in one of the list views and use the property editor on the right-hand side.",
									"Adding entity",
									JOptionPane.INFORMATION_MESSAGE);
				}

				mapModel.reinitializeEntity(instance);
				Cobra2DLevel level = mapModel.getCurrentLevel();
				level.getMap().addEntity(instance);
				view.updateListViews();
			} catch (Exception e1) {
				notifyException(
						"Entity error",
						"Cannot initialize entity due to an error. Please check implementation.",
						e1, MapEditor.this);
			}
		}
	}

	private void addEntity(Class<?> clazz) {
		CamIgnoringRenderPanel mapEditorRenderer = view.getMapEditorRenderer();
		Point pointOnMap = mapEditorRenderer.getPointOnMap();
		addEntity(pointOnMap, clazz);
	}

	private void addEntity(Point point) {
		addEntity(point, null);
	}

	private void addEntity() {
		CamIgnoringRenderPanel mapEditorRenderer = view.getMapEditorRenderer();
		Point pointOnMap = mapEditorRenderer.getPointOnMap();
		addEntity(pointOnMap, null);
	}

	private void addCamera() {
		Class<?> cameraTypeSelection = WidgetsUtil
				.showTypeSelection(Camera.class);
		if (cameraTypeSelection != null) {
			try {
				Entity instance = (Entity) InstanceUtil
						.createDefaultInstance(cameraTypeSelection);
				instance.initialize(mapModel.getEngine());
				Cobra2DLevel level = mapModel.getCurrentLevel();
				level.getMap().addEntity(instance);
				view.updateListViews();
			} catch (Exception e1) {
				notifyException(
						"Camera error",
						"Cannot initialize camera due to an error. Please check implementation.",
						e1, MapEditor.this);
			}
		}
	}

	private void removeEntity() {
		if (selectedEntity == null) {
			JOptionPane.showMessageDialog(view,
					"Please select an entity before removing.",
					"Remove entity", JOptionPane.WARNING_MESSAGE);
		} else {
			if (settings.isAskBeforeRemoval()) {
				int answer = JOptionPane.showConfirmDialog(view,
						"Are you sure to delete this entity?", "Delete entity",
						JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.NO_OPTION) {
					return;
				}
			}

			Map map = mapModel.getCurrentLevel().getMap();
			map.removeEntity(selectedEntity);
			setSelectedEntity(null);
		}
	}

	private void removeCamera() {
		JList<Entity> cameraList = view.getCameraList();
		Entity selectedCamera = cameraList.getSelectedValue();
		if (selectedCamera == null) {
			JOptionPane.showMessageDialog(view,
					"Please select a camera before removing.", "Remove camera",
					JOptionPane.WARNING_MESSAGE);
		} else {
			if (settings.isAskBeforeRemoval()) {
				int answer = JOptionPane.showConfirmDialog(view,
						"Are you sure to delete this camera?", "Delete camera",
						JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.NO_OPTION) {
					return;
				}
			}

			Cobra2DLevel level = mapModel.getCurrentLevel();
			level.getMap().removeEntity(selectedCamera);
			view.updateListViews();
		}
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		CamIgnoringRenderPanel mapEditorRenderer = view.getMapEditorRenderer();
		Point pointOnMap = mapEditorRenderer.getPointOnMap();

		if (e.getButton() == MouseEvent.BUTTON3) {
			this.lastTool = this.mode;
			setMode(EditorMode.MOVE_CAM);

			// Calculate offset for dragging
			xOffset = e.getPoint().x + pointOnMap.x;
			yOffset = e.getPoint().y + pointOnMap.y;
		}

		if (e.getButton() == MouseEvent.BUTTON1) {
			// Set wasEntityDragging to false
			wasEntityDragging = false;
			// Calculate offset on entity for dragging
			if (selectedEntity != null) {
				xOffsetOnEntity = e.getPoint().x + pointOnMap.x
						- selectedEntity.getPosition().x;
				yOffsetOnEntity = e.getPoint().y + pointOnMap.y
						- selectedEntity.getPosition().y;
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		/*
		 * SELECT/MOVE
		 */
		if (e.getButton() == MouseEvent.BUTTON1 && !wasEntityDragging) {
			CamIgnoringRenderPanel mapEditorRenderer = view
					.getMapEditorRenderer();
			Point pointOnMap = mapEditorRenderer.getPointOnMap();
			// Handle click event if mode is SELECT/MOVE
			if (mode == EditorMode.MOVE || mode == EditorMode.REMOVE) {
				// Select the entity at the click point on map
				int clickXOnMap = e.getPoint().x + pointOnMap.x;
				int clickYOnMap = e.getPoint().y + pointOnMap.y;

				Map map = mapModel.getCurrentLevel().getMap();
				List<Entity> entitiesAt = map.getEntityAt(new Point(
						clickXOnMap, clickYOnMap));

				if (selectedEntity == null) {
					// Set new selection if there was none
					if (!entitiesAt.isEmpty()) {
						setSelectedEntity(entitiesAt.get(0));
					}
				} else {
					// Set new selection if there was none
					// if there are more entities hit
					if (entitiesAt.size() > 1) {
						// Selection strategy
						if (entitiesAt.contains(selectedEntity)) {
							// Get index of selected and set index+1 entity as
							// selected entity
							int index = entitiesAt.indexOf(selectedEntity);
							// If index+1 is available
							if (entitiesAt.size() > index + 1) {
								// Get index +1
								Entity entity = entitiesAt.get(index + 1);
								// And set this entity as selected
								setSelectedEntity(entity);
							} else {
								// Set Null or first from list
								if (entitiesAt.isEmpty()) {
									setSelectedEntity(null);
								} else {
									Entity entity = entitiesAt.get(0);
									setSelectedEntity(entity);
								}
							}
						} else {
							// Set Null or first from list
							if (entitiesAt.isEmpty()) {
								setSelectedEntity(null);
							} else {
								Entity entity = entitiesAt.get(0);
								setSelectedEntity(entity);
							}
						}
					} else {
						// Set Null or first from list
						if (entitiesAt.isEmpty()) {
							setSelectedEntity(null);
						} else {
							setSelectedEntity(entitiesAt.get(0));
						}
					}
				}
			}
		}

		/*
		 * MOVE CAMERA
		 */
		if (e.getButton() == MouseEvent.BUTTON3) {
			setMode(this.lastTool);
		}

		/*
		 * REMOVE ENTITY
		 */
		if (e.getButton() == MouseEvent.BUTTON1 && mode == EditorMode.REMOVE) {
			removeEntity();
		}

		/*
		 * ADD ENTITY
		 */
		if (e.getButton() == MouseEvent.BUTTON1 && mode == EditorMode.ADD) {
			CamIgnoringRenderPanel mapEditorRenderer = view
					.getMapEditorRenderer();
			Point pointOnMap = new Point(mapEditorRenderer.getPointOnMap());
			pointOnMap.x += e.getPoint().x;
			pointOnMap.y += e.getPoint().y;
			addEntity(pointOnMap);
		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {
		/*
		 * MOVE CAMERA
		 */
		if (this.mode == EditorMode.MOVE_CAM) {

			CamIgnoringRenderPanel mapEditorRenderer = view
					.getMapEditorRenderer();
			Point pointOnMap = mapEditorRenderer.getPointOnMap();

			// modify point on map
			pointOnMap.x = xOffset - e.getPoint().x;
			pointOnMap.y = yOffset - e.getPoint().y;

			view.setPointOnMap(pointOnMap);
		}

		if (this.mode == EditorMode.MOVE) {
			if (selectedEntity != null) {
				CamIgnoringRenderPanel mapEditorRenderer = view
						.getMapEditorRenderer();
				Point renderPointOnMap = mapEditorRenderer.getPointOnMap();

				// Move entity based on the offset calculated in mousePressed
				// and the point of the cursor.
				Point pointOnMap = new Point(e.getPoint().x
						+ renderPointOnMap.x, e.getPoint().y
						+ renderPointOnMap.y);
				Point newEntityPos = new Point(pointOnMap.x - xOffsetOnEntity,
						pointOnMap.y - yOffsetOnEntity);
				selectedEntity.setPosition(newEntityPos);
				wasEntityDragging = true;
			}
		}

	}

	public void mouseMoved(MouseEvent e) {

	}

	@Deprecated
	public Cobra2DEngine getInitializeEngine() {
		return mapModel.getEngine();
	}

	public void finish() {
		camViewRenderTimer.stopRendering();
		mapEditorRenderTimer.stopRendering();
	}

	public void setNotificationSystem(NotificationSystem manager) {
		this.manager = manager;
	}

	public List<Class<? extends Notificator>> getNotificationTypes() {
		List<Class<? extends Notificator>> types = new ArrayList<Class<? extends Notificator>>();
		types.add(AnimationEditor.class);
		return types;
	}

	public void receiveNotification(Notification notification,
			Notificator notificator,
			Class<? extends Notificator> notificatorType) {
		if (notification.containsKey("eventName")) {
			String eventName = (String) notification.get("eventName");
			if (eventName.equalsIgnoreCase("AnimationAdded")) {
				view.updateListViews();
			}
		}
	}

}
