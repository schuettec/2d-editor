package de.schuette.cobra2D.workbench.gui.widgets;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.HashMap;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import de.schuette.cobra2D.rendering.RenderToolkit;
import de.schuette.cobra2D.ressource.Animation;
import de.schuette.cobra2D.ressource.AnimationMemory;

public class AnimationListRenderer extends JLabel implements
		ListCellRenderer<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int MAX_HEIGHT = 100;

	private HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	private HashMap<String, Dimension> dimensions = new HashMap<String, Dimension>();

	private AnimationMemory dataSource;

	public AnimationListRenderer(AnimationMemory dataSource) {
		this(dataSource, MAX_HEIGHT);
	}

	public AnimationListRenderer(AnimationMemory dataSource, int height) {
		if (dataSource == null)
			throw new IllegalArgumentException("Image Memory cannot be null");

		this.dataSource = dataSource;

		Set<String> keys = dataSource.getAnimations().keySet();
		for (String key : keys) {
			Animation animation = dataSource.getAnimation(key);
			VolatileImage image = animation.getImage(animation
					.getPictureCount() / 2);
			dimensions.put(key,
					new Dimension(image.getWidth(), image.getHeight()));
			Dimension newDim = new Dimension(MAX_HEIGHT, MAX_HEIGHT);
			VolatileImage resized = RenderToolkit.resize(image, newDim);
			BufferedImage snapShot = resized.getSnapshot();
			images.put(key, snapShot);
		}
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList<? extends String> list,
			String value, int index, boolean isSelected, boolean cellHasFocus) {
		BufferedImage image = images.get(value);
		this.setIcon(new ImageIcon(image));
		Animation animation = dataSource.getAnimation(value);

		Dimension size = dimensions.get(value);
		String labelText = "<html><b> Animation: " + value
				+ "</b><br/><small> Size: " + size.width + "x" + size.height
				+ "px, " + animation.getPictureCount() + " Frames, "
				+ animation.getCols() + " columns and " + animation.getRows()
				+ " rows</small></html>";
		setText(labelText);

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		return this;

	}
}