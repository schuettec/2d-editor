package de.schuette.cobra2D.workbench.gui.entityEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.BevelBorder;

import de.schuette.cobra2D.map.Map;
import de.schuette.cobra2D.ressource.ImageMemory;
import de.schuette.cobra2D.workbench.gui.WidgetsUtil;
import de.schuette.cobra2D.workbench.gui.mapEditor.CamIgnoringRenderPanel;
import de.schuette.cobra2D.workbench.gui.mapEditor.MapEditorRenderPanel;
import de.schuette.cobra2D.workbench.gui.propertyEditor.PropertyEditor;
import de.schuette.cobra2D.workbench.model.MapModel;

public class EntityEditorPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected EntityModel model;
	protected MapModel mapModel;

	protected JList<String> list;
	private JPanel pnlBottomSidebar;

	private JLabel lblEntityType;
	private JScrollPane propertyScroller;
	private PropertyEditor propertyEditor;

	private JPanel pnlRendererContainer;

	private MapEditorRenderPanel mapEditorRenderer;

	private Action selectImageAction;

	/**
	 * Create the panel.
	 * 
	 * @throws Exception
	 */
	public EntityEditorPanel(EntityModel model, MapModel mapModel) {

		setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		add(splitPane);

		pnlRendererContainer = new JPanel(new BorderLayout());
		splitPane.setRightComponent(pnlRendererContainer);

		// Render Panel
		Map map = new Map();
		map.addEntity(model.getEntity());
		mapEditorRenderer = new MapEditorRenderPanel(map);
		pnlRendererContainer.add(mapEditorRenderer);

		JPanel pnlLeftSidebar = new JPanel();
		pnlLeftSidebar.setPreferredSize(new Dimension(400, 10));
		pnlLeftSidebar.setMinimumSize(new Dimension(235, 10));
		pnlLeftSidebar.setBorder(new BevelBorder(BevelBorder.RAISED, null,
				null, null, null));
		splitPane.setLeftComponent(pnlLeftSidebar);
		pnlLeftSidebar.setLayout(new BorderLayout(0, 0));

		JPanel pnlPropertiesTop = new JPanel();
		pnlLeftSidebar.add(pnlPropertiesTop, BorderLayout.NORTH);
		pnlPropertiesTop.setLayout(new BorderLayout(0, 0));

		JLabel lblPropertiesDesc = new JLabel("Entity Properties:");
		pnlPropertiesTop.add(lblPropertiesDesc);

		lblEntityType = new JLabel("New label");
		lblEntityType.setFont(new Font("Tahoma", Font.PLAIN, 9));
		pnlPropertiesTop.add(lblEntityType, BorderLayout.SOUTH);

		propertyScroller = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pnlLeftSidebar.add(propertyScroller, BorderLayout.CENTER);

		propertyEditor = new PropertyEditor();
		propertyScroller.setViewportView(propertyEditor);

		pnlBottomSidebar = new JPanel();
		pnlBottomSidebar.setPreferredSize(new Dimension(10, 120));
		pnlBottomSidebar.setMinimumSize(new Dimension(10, 120));
		add(pnlBottomSidebar, BorderLayout.SOUTH);
		pnlBottomSidebar.setLayout(new BorderLayout(0, 0));

		connectEntityModel(model);
		connectMapModel(mapModel);
	}

	public CamIgnoringRenderPanel getPreviewRenderer() {
		return mapEditorRenderer;
	}

	private void connectEntityModel(EntityModel model) {
		this.model = model;
		Class<?> entityType = model.getEntityType();
		lblEntityType.setText(entityType.getCanonicalName());
		try {
			propertyEditor.setEntity(model.getEntity());
		} catch (Exception e) {
			// TODO: Put this to notification system
			e.printStackTrace();
		}
	}

	public void updatePropertyEditor() {
		propertyEditor.update();
	}

	private void connectMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
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
		pnlBottomSidebar.removeAll();
		JScrollPane pnlBottomScroller = new JScrollPane(list);
		pnlBottomScroller
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		pnlBottomSidebar.add(pnlBottomScroller);
		pnlBottomSidebar.validate();
	}

	protected void setSelectImageAction(Action action) {
		this.selectImageAction = action;
	}

	protected void setEditingFinishedAction(Action editingFinishedAction) {
		propertyEditor.setEditingFinishedAction(editingFinishedAction);
	}

}
