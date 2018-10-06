package de.schuette.cobra2D.workbench.gui.animationEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.VolatileImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.schuette.cobra2D.math.Math2D;
import de.schuette.cobra2D.ressource.Animation;
import de.schuette.cobra2D.ressource.ImageMemory;
import de.schuette.cobra2D.workbench.gui.WidgetsUtil;
import de.schuette.cobra2D.workbench.model.MapModel;
import de.schuette.cobra2D.workbench.runtime.WorkbenchRuntime;
import de.schuette.cobra2DSandbox.texture.editing.PreviewPanel;

public class AnimationEditorView extends JPanel {

	private Action selectImageAction;
	private Action changeWidthAction;
	private Action changeHeightAction;
	private Action changeXCountAction;
	private Action changeYCountAction;
	private Action applyAnimation;

	private JList<String> list;
	private AnimationPreviewPanel pnlPreview;

	private JSpinner frameWidth;
	private JSpinner frameHeight;
	private JSpinner framesX;
	private JSpinner framesY;
	private JTextField txtAnimationadress;
	private JButton btnStop;
	private JButton btnStart;

	private VolatileImage image;
	private int currentImage = 0;
	private Animation animationPreview;
	private Timer timer;
	private PreviewPanel previewPanel;
	private JSpinner previewDelay;
	private JButton btnCreateAnimation;

	class AnimationTimer extends TimerTask {
		@Override
		public void run() {
			if (animationPreview != null) {
				int max = animationPreview.getPictureCount();
				if (currentImage < max) {
					VolatileImage image = animationPreview
							.getImage(currentImage);
					previewPanel.setImage(image);

					int x = Math2D.saveRound(previewPanel.getWidth() / 2.0
							- getFrameWidth() / 2.0);
					int y = Math2D.saveRound(previewPanel.getHeight() / 2.0
							- getFrameHeight() / 2.0);

					Point previewPoint = new Point(x, y);

					previewPanel.setRenderPoint(previewPoint);

					currentImage++;
				} else {
					currentImage = 0;
				}
			}
		}
	}

	/**
	 * Create the panel.
	 */
	public AnimationEditorView(MapModel mapModel) {
		setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);

		pnlPreview = new AnimationPreviewPanel();
		splitPane.setRightComponent(pnlPreview);

		JPanel pnlAnimationPreview = new JPanel();
		pnlAnimationPreview.setPreferredSize(new Dimension(200, 10));
		splitPane.setLeftComponent(pnlAnimationPreview);
		pnlAnimationPreview.setLayout(new BorderLayout(0, 0));

		JPanel pnlPreviewControl = new JPanel();
		pnlAnimationPreview.add(pnlPreviewControl, BorderLayout.SOUTH);

		WorkbenchRuntime runtime = WorkbenchRuntime.getInstance();
		ImageIcon stopIcon = runtime.getApplicationResources().getStopIcon();
		ImageIcon startIcon = runtime.getApplicationResources().getStartIcon();

		btnStop = new JButton(stopIcon);
		btnStop.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stopAnimationPreview();
			}

		});

		JLabel lblPreviewFrameDelay = new JLabel("Preview frame delay:");
		pnlPreviewControl.add(lblPreviewFrameDelay);

		previewDelay = new JSpinner();
		previewDelay.setModel(new SpinnerNumberModel(200, 10, 1000, 10));
		previewDelay.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				stopAnimationPreview();
				startAnimationPreview();
			}
		});
		pnlPreviewControl.add(previewDelay);
		pnlPreviewControl.add(btnStop);

		btnStart = new JButton(startIcon);
		btnStart.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				startAnimationPreview();
			}
		});
		pnlPreviewControl.add(btnStart);

		previewPanel = new PreviewPanel();
		pnlAnimationPreview.add(previewPanel, BorderLayout.CENTER);

		ImageMemory imageMemory = mapModel.getCurrentLevel().getImageMemory();
		list = WidgetsUtil.createImageMemoryList(imageMemory);
		list.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
				if (selectImageAction != null) {
					selectImageAction.actionPerformed(new ActionEvent(list, 0,
							list.getSelectedValue()));
				}
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		JScrollPane pnlBottomScroller = new JScrollPane(list);
		pnlBottomScroller.setPreferredSize(new Dimension(10, 120));
		pnlBottomScroller.setMinimumSize(new Dimension(10, 120));
		pnlBottomScroller
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		add(pnlBottomScroller, BorderLayout.SOUTH);

		JPanel pnlSettings = new JPanel();
		add(pnlSettings, BorderLayout.NORTH);
		pnlSettings.setLayout(new BorderLayout(0, 0));

		JPanel pnlConfirm = new JPanel();
		pnlConfirm.setToolTipText("");
		pnlConfirm.setPreferredSize(new Dimension(150, 10));
		pnlSettings.add(pnlConfirm, BorderLayout.WEST);

		ImageIcon okIcon = runtime.getApplicationResources().getOkIcon();
		pnlConfirm.setLayout(new BorderLayout(0, 0));

		btnCreateAnimation = new JButton("Create animation", okIcon);
		btnCreateAnimation.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnCreateAnimation.setHorizontalTextPosition(SwingConstants.CENTER);
		btnCreateAnimation
				.setToolTipText("Finishes the work on this animation and add the result to the animation memory of the current level.");
		btnCreateAnimation.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (applyAnimation != null) {
					applyAnimation.actionPerformed(e);
				}
			}
		});
		pnlConfirm.add(btnCreateAnimation);

		Component horizontalStrut = Box.createHorizontalStrut(15);
		pnlConfirm.add(horizontalStrut, BorderLayout.EAST);

		JPanel panel = new JPanel();
		pnlSettings.add(panel);
		panel.setLayout(new GridLayout(3, 6, 20, 10));

		JLabel lblAnimationAdress = new JLabel("Animation adress:");
		panel.add(lblAnimationAdress);

		txtAnimationadress = new JTextField();
		panel.add(txtAnimationadress);
		txtAnimationadress.setColumns(10);

		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		panel.add(rigidArea);

		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		panel.add(rigidArea_1);

		JLabel lblFrameWidth = new JLabel("Frame width:");
		panel.add(lblFrameWidth);

		frameWidth = new JSpinner();
		frameWidth
				.setModel(new SpinnerNumberModel(10, 1, Integer.MAX_VALUE, 1));
		frameWidth.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				if (changeWidthAction != null) {
					changeWidthAction.actionPerformed(new ActionEvent(
							frameWidth, 0, "FrameWidth changed"));
				}
				updateAnimationPreview();
			}
		});
		panel.add(frameWidth);
		frameWidth.setPreferredSize(new Dimension(40, 25));

		JLabel lblFrameCountX = new JLabel("Number of frames X:");
		panel.add(lblFrameCountX);

		framesX = new JSpinner();
		framesX.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				if (changeXCountAction != null) {
					changeXCountAction.actionPerformed(new ActionEvent(framesX,
							0, "FramesX changed"));
				}
				updateAnimationPreview();
			}
		});
		framesX.setModel(new SpinnerNumberModel(10, 1, Integer.MAX_VALUE, 1));
		panel.add(framesX);
		framesX.setPreferredSize(new Dimension(40, 25));

		JLabel lblFrameHeight = new JLabel("Frame height:");
		panel.add(lblFrameHeight);

		frameHeight = new JSpinner();
		frameHeight
				.setModel(new SpinnerNumberModel(10, 1, Integer.MAX_VALUE, 1));
		frameHeight.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				if (changeHeightAction != null) {
					changeHeightAction.actionPerformed(new ActionEvent(
							frameHeight, 0, "FrameHeight changed"));
				}

				updateAnimationPreview();
			}
		});
		frameHeight.setPreferredSize(new Dimension(40, 25));
		panel.add(frameHeight);

		framesY = new JSpinner();
		framesY.setModel(new SpinnerNumberModel(10, 1, Integer.MAX_VALUE, 1));
		framesY.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				if (changeYCountAction != null) {
					changeYCountAction.actionPerformed(new ActionEvent(framesY,
							0, "FramesY changed"));
				}
				updateAnimationPreview();
			}

		});

		JLabel lblFrameCountY = new JLabel("Number of frames Y:");
		panel.add(lblFrameCountY);
		framesY.setPreferredSize(new Dimension(40, 25));
		panel.add(framesY);

		Component verticalStrut = Box.createVerticalStrut(5);
		pnlSettings.add(verticalStrut, BorderLayout.NORTH);

		Component verticalStrut_1 = Box.createVerticalStrut(5);
		pnlSettings.add(verticalStrut_1, BorderLayout.SOUTH);

		Component horizontalStrut_1 = Box.createHorizontalStrut(5);
		pnlSettings.add(horizontalStrut_1, BorderLayout.EAST);

		updateAnimationPreview();

	}

	private void stopAnimationPreview() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private void startAnimationPreview() {
		if (this.timer == null) {
			this.timer = new Timer();
			timer.schedule(new AnimationTimer(), 0,
					(Integer) previewDelay.getValue());
		} else {
			stopAnimationPreview();
			startAnimationPreview();
		}
	}

	protected void setSelectImageAction(Action selectImageAction) {
		this.selectImageAction = selectImageAction;
	}

	protected void setChangeWidthAction(Action changeWidthAction) {
		this.changeWidthAction = changeWidthAction;
	}

	protected void setChangeHeightAction(Action changeHeightAction) {
		this.changeHeightAction = changeHeightAction;
	}

	protected void setChangeXCountAction(Action changeXCountAction) {
		this.changeXCountAction = changeXCountAction;
	}

	protected void setChangeYCountAction(Action changeYCountAction) {
		this.changeYCountAction = changeYCountAction;
	}

	public void setImage(VolatileImage image) {
		pnlPreview.setImage(image);
		this.image = image;
		updateAnimationPreview();
	}

	public int getFrameWidth() {
		return (Integer) frameWidth.getValue();
	}

	public int getFrameHeight() {
		return (Integer) frameHeight.getValue();
	}

	public int getFrameCountX() {
		return (Integer) framesX.getValue();
	}

	public int getFrameCountY() {
		return (Integer) framesY.getValue();
	}

	public void setFrameWidth(int frameWidthVal) {
		frameWidth.setValue(frameWidthVal);
		updateAnimationPreview();
	}

	public void setFrameHeight(int frameHeightVal) {
		frameHeight.setValue(frameHeightVal);
		updateAnimationPreview();
	}

	public void setFrameCountX(int frameCountX) {
		framesX.setValue(frameCountX);
		updateAnimationPreview();
	}

	public void setFrameCountY(int frameCountY) {
		framesY.setValue(frameCountY);
		updateAnimationPreview();
	}

	public String getAnimationAdress() {
		return txtAnimationadress.getText();
	}

	protected void updateAnimationPreview() {
		boolean wasRunning = (timer != null);

		stopAnimationPreview();

		pnlPreview.setAnimationProperties(getFrameWidth(), getFrameHeight(),
				getFrameCountX(), getFrameCountY());

		if (image != null) {
			animationPreview = new Animation(image, getFrameWidth(),
					getFrameHeight());
		}

		if (wasRunning)
			startAnimationPreview();
	}

	public void setAnimationAdress(String string) {
		txtAnimationadress.setText(string);
	}

	public void setApplyAnimation(Action applyAnimation) {
		this.applyAnimation = applyAnimation;
	}

}
