package de.schuette.cobra2D.workbench.runtime;

import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ApplicationResources {

	protected ImageIcon entityIcon;
	protected ImageIcon propertyIcon;
	protected ImageIcon editIcon;
	protected ImageIcon moveToolIcon;
	protected ImageIcon addToolIcon;
	protected ImageIcon removeToolIcon;
	protected ImageIcon rectangleToolIcon;
	protected ImageIcon middleToolIcon;
	protected ImageIcon crossToolIcon;
	protected ImageIcon stopIcon;
	protected ImageIcon startIcon;
	protected ImageIcon okIcon;
	protected ImageIcon cameraIcon;
	protected ImageIcon entitySmallIcon;
	protected ImageIcon entityOnMapIcon;
	protected ImageIcon cameraSmallIcon;
	protected ImageIcon entityOnMapSmallIcon;
	protected ImageIcon noskilledSmallIcon;
	protected ImageIcon noskilledIcon;
	protected ImageIcon previewIcon;
	protected ImageIcon cameraMovementIcon;
	protected ImageIcon editAddIcon;
	protected ImageIcon editDeleteIcon;
	protected ImageIcon editAddSmallIcon;
	protected ImageIcon editRemoveSmallIcon;
	protected ImageIcon animationIcon;
	protected ImageIcon animationSmallIcon;

	public ApplicationResources() {
		this.entityIcon = getImageIconFromResources(RuntimeConstants.ENTITY_TYPE_LIST_ITEM);
		this.propertyIcon = getImageIconFromResources(RuntimeConstants.PROPERTY_ICON);
		this.editIcon = getImageIconFromResources(RuntimeConstants.EDIT_ICON);

		this.moveToolIcon = getImageIconFromResources(RuntimeConstants.MOVE_TOOL_ICON);
		this.addToolIcon = getImageIconFromResources(RuntimeConstants.ADD_TOOL_ICON);
		this.removeToolIcon = getImageIconFromResources(RuntimeConstants.REMOVE_TOOL_ICON);
		this.rectangleToolIcon = getImageIconFromResources(RuntimeConstants.RECTANGLE_TOOL_ICON);
		this.middleToolIcon = getImageIconFromResources(RuntimeConstants.MIDDLE_TOOL_ICON);
		this.crossToolIcon = getImageIconFromResources(RuntimeConstants.CROSS_TOOL_ICON);
		this.stopIcon = getImageIconFromResources(RuntimeConstants.STOP_ICON);
		this.startIcon = getImageIconFromResources(RuntimeConstants.START_ICON);
		this.okIcon = getImageIconFromResources(RuntimeConstants.OK_ICON);
		this.cameraIcon = getImageIconFromResources(RuntimeConstants.CAMERA_ICON);
		this.cameraSmallIcon = getImageIconFromResources(RuntimeConstants.CAMERA_SMALL_ICON);
		this.entitySmallIcon = getImageIconFromResources(RuntimeConstants.ENTITY_SMALL_ICON);
		this.entityOnMapIcon = getImageIconFromResources(RuntimeConstants.ENTITY_ON_MAP_ICON);
		this.entityOnMapSmallIcon = getImageIconFromResources(RuntimeConstants.ENTITY_ON_MAP_SMALL_ICON);
		this.noskilledSmallIcon = getImageIconFromResources(RuntimeConstants.NO_SKILLED_SMALL_ICON);
		this.noskilledIcon = getImageIconFromResources(RuntimeConstants.NO_SKILLED_ICON);
		this.previewIcon = getImageIconFromResources(RuntimeConstants.PREVIEW_ICON);
		this.cameraMovementIcon = getImageIconFromResources(RuntimeConstants.CAMERA_MOVEMENT_ICON);

		this.editAddIcon = getImageIconFromResources(RuntimeConstants.EDIT_ADD_ICON);
		this.editAddSmallIcon = getImageIconFromResources(RuntimeConstants.EDIT_ADD_SMALL_ICON);
		this.editRemoveSmallIcon = getImageIconFromResources(RuntimeConstants.EDIT_REMOVE_SMALL_ICON);
		this.editDeleteIcon = getImageIconFromResources(RuntimeConstants.EDIT_DELETE_ICON);
		this.animationIcon = getImageIconFromResources(RuntimeConstants.ANIMATION_ICON);
		this.animationSmallIcon = getImageIconFromResources(RuntimeConstants.ANIMATION_SMALL_ICON);

	}

	public ImageIcon getAnimationIcon() {
		return animationIcon;
	}

	public ImageIcon getAnimationSmallIcon() {
		return animationSmallIcon;
	}

	public ImageIcon getEditRemoveSmallIcon() {
		return editRemoveSmallIcon;
	}

	public ImageIcon getEditAddSmallIcon() {
		return editAddSmallIcon;
	}

	public ImageIcon getEditAddIcon() {
		return editAddIcon;
	}

	public ImageIcon getEditDeleteIcon() {
		return editDeleteIcon;
	}

	public ImageIcon getCameraMovementIcon() {
		return cameraMovementIcon;
	}

	public ImageIcon getPreviewIcon() {
		return previewIcon;
	}

	public ImageIcon getNoskilledSmallIcon() {
		return noskilledSmallIcon;
	}

	public ImageIcon getNoskilledIcon() {
		return noskilledIcon;
	}

	public ImageIcon getEntityOnMapSmallIcon() {
		return entityOnMapSmallIcon;
	}

	public ImageIcon getCameraSmallIcon() {
		return cameraSmallIcon;
	}

	public ImageIcon getEntityOnMapIcon() {
		return entityOnMapIcon;
	}

	public ImageIcon getEntitySmallIcon() {
		return entitySmallIcon;
	}

	public ImageIcon getCameraIcon() {
		return cameraIcon;
	}

	public ImageIcon getOkIcon() {
		return okIcon;
	}

	public ImageIcon getStopIcon() {
		return stopIcon;
	}

	public ImageIcon getStartIcon() {
		return startIcon;
	}

	public ImageIcon getCrossToolIcon() {
		return crossToolIcon;
	}

	public ImageIcon getMoveToolIcon() {
		return moveToolIcon;
	}

	public ImageIcon getAddToolIcon() {
		return addToolIcon;
	}

	public ImageIcon getRemoveToolIcon() {
		return removeToolIcon;
	}

	public ImageIcon getRectangleToolIcon() {
		return rectangleToolIcon;
	}

	public ImageIcon getEntityIcon() {
		return entityIcon;
	}

	public ImageIcon getPropertyIcon() {
		return propertyIcon;
	}

	public ImageIcon getEditIcon() {
		return editIcon;
	}

	public ImageIcon getMiddleToolIcon() {
		return middleToolIcon;
	}

	protected URL getApplicationResource(String resourceName) {
		URL url = getClass().getClassLoader().getResource(resourceName);
		return url;
	}

	protected ImageIcon getImageIconFromResources(String resourceName) {
		try {
			ImageIcon icon = getImageIconFromResourceInternal(resourceName);
			return icon;

		} catch (Exception e) {
			try {
				// Try default resource for not found resources
				return getImageIconFromResourceInternal(RuntimeConstants.NO_PICTURE_APP_RESOURCE);
			} catch (Exception ex) {
				// Give up!
				return null;
			}
		}
	}

	protected ImageIcon getImageIconFromResourceInternal(String resourceName)
			throws Exception {
		URL url = getApplicationResource(resourceName);
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		Image image = ImageIO.read(in);
		ImageIcon icon = new ImageIcon(image);
		in.close();
		return icon;
	}

}
