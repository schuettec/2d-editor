package de.schuette.cobra2D.workbench.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;

import de.schuette.cobra2D.entity.Entity;
import de.schuette.cobra2D.entity.skills.Camera;
import de.schuette.cobra2D.ressource.AnimationMemory;
import de.schuette.cobra2D.ressource.ImageMemory;
import de.schuette.cobra2D.system.Cobra2DLevel;
import de.schuette.cobra2D.workbench.gui.entityEditor.EntityEditorException;
import de.schuette.cobra2D.workbench.gui.entityEditor.EntityEditorWidget;
import de.schuette.cobra2D.workbench.gui.entityEditor.EntityModel;
import de.schuette.cobra2D.workbench.gui.widgets.AnimationListModel;
import de.schuette.cobra2D.workbench.gui.widgets.AnimationListRenderer;
import de.schuette.cobra2D.workbench.gui.widgets.EditableTypeListModel;
import de.schuette.cobra2D.workbench.gui.widgets.EntityListModel;
import de.schuette.cobra2D.workbench.gui.widgets.EntityListRenderer;
import de.schuette.cobra2D.workbench.gui.widgets.ImageListDetailedRenderer;
import de.schuette.cobra2D.workbench.gui.widgets.ImageListModel;
import de.schuette.cobra2D.workbench.gui.widgets.ImageListRenderer;
import de.schuette.cobra2D.workbench.gui.widgets.TypeListModel;
import de.schuette.cobra2D.workbench.gui.widgets.TypeListRenderer;
import de.schuette.cobra2D.workbench.gui.widgets.TypeListWidget;
import de.schuette.cobra2D.workbench.model.MapModel;
import de.schuette.cobra2D.workbench.runtime.ApplicationResources;
import de.schuette.cobra2D.workbench.runtime.WorkbenchRuntime;

public class WidgetsUtil {

	/**
	 * Shows a dialog to the user to specify an entity type. If the user does
	 * not cancel this option, this method creates an entity editor that will
	 * store the resulting entity in the given map model after finishing the
	 * editor. If the user cancels this action, null is returned and nothing
	 * will happen.
	 * 
	 * @param mapModel
	 *            The map model to store the created entity.
	 * @return Returns an instance of EntityEditor, a container for model, view
	 *         and controller or null, if an exception occurred or user canceled
	 *         this option.
	 * @throws Exception
	 */
	public static EntityEditorWidget createEntityEditor(MapModel mapModel)
			throws Exception {
		Class<? extends Entity> entityType = WidgetsUtil
				.showEditableEntitySelection();
		if (entityType != null) {
			EntityEditorWidget editor;
			try {
				EntityModel model = new EntityModel(entityType);
				editor = new EntityEditorWidget(mapModel, model);
			} catch (EntityEditorException e) {
				JOptionPane.showMessageDialog(null,
						"The chosen entity is not editable due to the following error:\n"
								+ e.getLocalizedMessage(), "Error editing!",
						JOptionPane.ERROR_MESSAGE);
				return null;
			}

			return editor;
		} else {
			return null;
		}
	}

	/**
	 * Shows a dialog to the user to specify an entity type. If the user does
	 * not cancel this option, this method creates an entity editor that will
	 * store the resulting entity in the given map model after finishing the
	 * editor. If the user cancels this action, null is returned and nothing
	 * will happen.
	 * 
	 * @param mapModel
	 *            The map model to store the created entity.
	 * @return Returns an instance of EntityEditor, a container for model, view
	 *         and controller or null, if an exception occurred or user canceled
	 *         this option.
	 * @throws Exception
	 */
	public static EntityEditorWidget createEntityEditor(MapModel mapModel,
			Entity entity) throws Exception {
		EntityEditorWidget editor;
		try {
			EntityModel model = new EntityModel(entity);
			editor = new EntityEditorWidget(mapModel, model);
		} catch (EntityEditorException e) {
			JOptionPane.showMessageDialog(null,
					"The chosen entity is not editable due to the following error:\n"
							+ e.getLocalizedMessage(), "Error editing!",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return editor;
	}

	/**
	 * Opens the modal class-type selection dialog and returns the selected type
	 * if it is is closed by the user.
	 * 
	 * @param type
	 *            The super-type of the listed items. Note: This type must be
	 *            one of the types registered in
	 *            RuntimeConstants.TYPE_DEFINITIONS
	 * @return Returns a class object representing the implementation the user
	 *         chose. If the dialog was aborted by the cancel button null is
	 *         returned.
	 */
	public static Class<?> showTypeSelection(Class<?> type) {
		TypeListWidget dialog = new TypeListWidget(type,
				new TypeListModel(type));
		return dialog.getSelectedEntityType();
	}

	/**
	 * Opens the modal class-type selection dialog and returns the selected type
	 * if it is is closed by the user. Only types marked as editable annotation
	 * will be shown for selection.
	 * 
	 * @return Returns a class object representing the implementation of an
	 *         editable entity.
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends Entity> showEditableEntitySelection() {
		TypeListWidget dialog = new TypeListWidget(Entity.class,
				new EditableTypeListModel(Entity.class));
		return (Class<? extends Entity>) dialog.getSelectedEntityType();
	}

	/**
	 * Opens the modal entity-type selection dialog and returns the selected
	 * entity type if it is is closed by the user.
	 * 
	 * @return Returns an entity class object representing the implementation
	 *         the user chose. If the dialog was aborted by the cancel button
	 *         null is returned.
	 */
	public static Class<?> showEntityTypeSelection() {
		TypeListWidget dialog = new TypeListWidget();
		return dialog.getSelectedEntityType();
	}

	/**
	 * Creates a list of textures from the given image memory.
	 * 
	 * @param imageMemory
	 *            The image memory.
	 * @return Returns a JList for the image memory.
	 */
	public static JList<String> createImageMemoryList(ImageMemory imageMemory) {
		ImageListModel imgModel = new ImageListModel(imageMemory);
		ImageListRenderer imgRenderer = new ImageListRenderer(imageMemory);
		JList<String> list = new JList<String>(imgModel);
		list.setVisibleRowCount(1);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setCellRenderer(imgRenderer);
		return list;
	}

	/**
	 * Creates a detailes list of textures in the image memory.
	 * 
	 * @param imageMemory
	 *            The image memory.
	 * @return Returns a JList for the image memory.
	 */
	public static JList<String> createImageMemoryDetailedList(
			ImageMemory imageMemory) {
		ImageListModel imgModel = new ImageListModel(imageMemory);
		ImageListDetailedRenderer imgRenderer = new ImageListDetailedRenderer(
				imageMemory);
		JList<String> list = new JList<String>(imgModel);
		// list.setVisibleRowCount(1);
		// list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setCellRenderer(imgRenderer);
		return list;
	}

	public static JList<Class<?>> createTypeList(Class<?> type, ImageIcon icon) {
		TypeListModel model = new TypeListModel(type);
		JList<Class<?>> list = new JList<Class<?>>(model);
		TypeListRenderer renderer = new TypeListRenderer();
		renderer.setIcon(icon);
		list.setCellRenderer(renderer);

		return list;
	}

	/**
	 * Creates a list of textures from the given image memory.
	 * 
	 * @param imageMemory
	 *            The image memory.
	 * @return Returns a JList for the image memory.
	 */
	public static JList<String> createAnimationMemoryList(
			AnimationMemory animationMemory) {
		AnimationListModel imgModel = new AnimationListModel(animationMemory);
		AnimationListRenderer imgRenderer = new AnimationListRenderer(
				animationMemory);
		JList<String> list = new JList<String>(imgModel);
		// list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setCellRenderer(imgRenderer);
		return list;
	}

	/**
	 * Creates a list of textures from the given image memory.
	 * 
	 * @param imageMemory
	 *            The image memory.
	 * @return Returns a JList for the image memory.
	 */
	public static JList<Entity> createEntityList(Cobra2DLevel level) {
		List<Entity> allEntities = level.getMap().getAllEntities();
		EntityListModel model = new EntityListModel(allEntities);
		EntityListRenderer renderer = new EntityListRenderer();

		WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
		ApplicationResources application = runtime.getApplicationResources();
		ImageIcon icon = application.getEntityOnMapIcon();
		renderer.setIcon(icon);
		JList<Entity> list = new JList<Entity>(model);
		// list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setCellRenderer(renderer);
		return list;
	}

	public static JList<Entity> createEntityList(List<Entity> entities) {
		EntityListModel model = new EntityListModel(entities);
		EntityListRenderer renderer = new EntityListRenderer();
		JList<Entity> list = new JList<Entity>(model);
		// list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setCellRenderer(renderer);
		return list;
	}

	public static JList<Entity> createCameraList(List<Camera> cameras) {
		EntityListModel model = new EntityListModel(castSafetyToEntity(cameras));
		EntityListRenderer renderer = new EntityListRenderer();
		WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
		ApplicationResources application = runtime.getApplicationResources();
		ImageIcon icon = application.getCameraIcon();
		renderer.setIcon(icon);
		JList<Entity> list = new JList<Entity>(model);
		list.setCellRenderer(renderer);
		return list;
	}

	public static JList<Entity> createCameraList(Cobra2DLevel level) {
		List<Camera> cams = level.getMap().getCameras();
		List<Entity> allEntities = castSafetyToEntity(cams);

		EntityListModel model = new EntityListModel(allEntities);
		EntityListRenderer renderer = new EntityListRenderer();

		WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
		ApplicationResources application = runtime.getApplicationResources();
		ImageIcon icon = application.getCameraIcon();
		renderer.setIcon(icon);

		JList<Entity> list = new JList<Entity>(model);
		list.setCellRenderer(renderer);
		return list;
	}

	private static List<Entity> castSafetyToEntity(List<?> objects) {
		List<Entity> allEntities = new ArrayList<Entity>();

		String errorClass = null;
		boolean castError = false;
		for (Object c : objects) {
			try {
				allEntities.add((Entity) c);
			} catch (ClassCastException e) {
				castError = true;
				errorClass = c.getClass().getName();
			}
		}

		if (castError) {
			JOptionPane
					.showMessageDialog(
							null,
							"There was an implementation found, that is not of class-type Entity.\nThis is an error in the game classes implementation.\nSome objects my be missing in the list views. Class: "
									+ errorClass,
							"Camera implementation error",
							JOptionPane.ERROR_MESSAGE);
		}

		return allEntities;
	}

}
