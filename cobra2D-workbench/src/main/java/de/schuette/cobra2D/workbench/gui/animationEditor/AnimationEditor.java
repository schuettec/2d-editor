package de.schuette.cobra2D.workbench.gui.animationEditor;

import java.awt.event.ActionEvent;
import java.awt.image.VolatileImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import de.schuette.cobra2D.ressource.AnimationMemory;
import de.schuette.cobra2D.system.Cobra2DLevel;
import de.schuette.cobra2D.workbench.model.MapModel;
import de.schuette.cobra2D.workbench.notification.Notification;
import de.schuette.cobra2D.workbench.notification.NotificationManager;
import de.schuette.cobra2D.workbench.notification.NotificationSystem;
import de.schuette.cobra2D.workbench.notification.Notificator;
import de.schuette.cobra2D.workbench.runtime.WorkbenchRuntime;

public class AnimationEditor implements Notificator {

	protected MapModel mapModel;

	protected AnimationEditorView view;

	protected String textureAdress = null;
	protected VolatileImage image = null;

	private NotificationSystem manager;

	protected Action selectImageAction = new AbstractAction() {

		public void actionPerformed(ActionEvent e) {
			textureAdress = e.getActionCommand();
			image = mapModel.getCurrentLevel().getImageMemory()
					.getImage(textureAdress);
			view.setImage(image);

			view.setFrameWidth(image.getWidth() / view.getFrameCountX());
			view.setFrameHeight(image.getHeight() / view.getFrameCountY());

			view.setAnimationAdress("animation_" + textureAdress);

		}
	};
	protected Action changeWidth = new AbstractAction() {

		public void actionPerformed(ActionEvent e) {
			if (image != null) {
				int framesX = (image.getWidth() / view.getFrameWidth());
				view.setFrameCountX(framesX);
			}
		}
	};
	protected Action changeHeight = new AbstractAction() {

		public void actionPerformed(ActionEvent e) {
			if (image != null) {
				int framesY = (image.getHeight() / view.getFrameHeight());
				view.setFrameCountY(framesY);
			}
		}
	};
	protected Action changeXFrames = new AbstractAction() {

		public void actionPerformed(ActionEvent e) {
			if (image != null) {
				int framesWidth = (image.getWidth() / view.getFrameCountX());
				view.setFrameWidth(framesWidth);
			}
		}
	};
	protected Action changeYFrames = new AbstractAction() {

		public void actionPerformed(ActionEvent e) {
			if (image != null) {
				int framesHeight = (image.getHeight() / view.getFrameCountY());
				view.setFrameHeight(framesHeight);
			}
		}
	};

	protected Action applyAnimation = new AbstractAction() {

		public void actionPerformed(ActionEvent e) {

			if (textureAdress == null || image == null) {
				JOptionPane
						.showMessageDialog(view,
								"No animation texture selected. Cannot create animation.");
				return;
			}

			String adress = view.getAnimationAdress();
			if (adress == null || adress.trim().length() == 0) {
				adress = "animation_" + textureAdress;
				JOptionPane.showMessageDialog(view,
						"No animation adress was entered. Using " + adress
								+ " for this animation.");
			}
			Cobra2DLevel currentLevel = mapModel.getCurrentLevel();
			AnimationMemory animations = currentLevel.getAnimationMemory();
			animations.loadAnimation(adress, textureAdress,
					view.getFrameWidth(), view.getFrameHeight());

			// Notification System - Send animation added event
			WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
			NotificationManager notificationSystem = runtime
					.getNotificationSystem();
			notificationSystem.addNotificator(AnimationEditor.this);
			Notification notification = new Notification();
			notification.put("eventName", "AnimationAdded");
			notification.put("animationAdress", adress);
			notification.put("mapModel", mapModel);
			manager.notifySubscribers(notification, AnimationEditor.this);

		}
	};

	public AnimationEditor(MapModel mapModel) {
		this.mapModel = mapModel;
		this.view = new AnimationEditorView(mapModel);
		this.view.setSelectImageAction(selectImageAction);
		this.view.setChangeWidthAction(changeWidth);
		this.view.setChangeHeightAction(changeHeight);
		this.view.setChangeXCountAction(changeXFrames);
		this.view.setChangeYCountAction(changeYFrames);
		this.view.setApplyAnimation(applyAnimation);

	}

	public AnimationEditorView getView() {
		return view;
	}

	public void setNotificationSystem(NotificationSystem manager) {
		this.manager = manager;
	}

}
