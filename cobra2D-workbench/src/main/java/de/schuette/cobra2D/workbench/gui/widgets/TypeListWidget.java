package de.schuette.cobra2D.workbench.gui.widgets;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import de.schuette.cobra2D.entity.Entity;

public class TypeListWidget extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JList<Class<?>> list;
	private DefaultListModel<Class<?>> model;
	private boolean aborted;

	private Action cancelAction = new AbstractAction("Cancel") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			aborted = true;
			dispose();
		}

	};

	private Action okAction = new AbstractAction("OK") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			aborted = false;
			dispose();
		}

	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TypeListWidget dialog = new TypeListWidget();
			System.out.println(dialog.getSelectedEntityType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Class<?> getSelectedEntityType() {
		if (aborted) {
			return null;
		} else {
			return list.getSelectedValue();
		}
	}

	public TypeListWidget() {
		this(Entity.class, new TypeListModel(Entity.class));
	}

	/**
	 * Create the dialog.
	 */
	public TypeListWidget(Class<?> type, DefaultListModel<Class<?>> listModel) {
		// Save Model
		this.model = listModel;
		setTitle("Select " + type.getSimpleName() + " type...");
		setBounds(100, 100, 408, 625);
		// Locate in center
		setLocationRelativeTo(null);
		addWindowListener(new WindowListener() {

			public void windowOpened(WindowEvent e) {

			}

			public void windowIconified(WindowEvent e) {

			}

			public void windowDeiconified(WindowEvent e) {

			}

			public void windowDeactivated(WindowEvent e) {

			}

			public void windowClosing(WindowEvent e) {
				aborted = true;
				dispose();
			}

			public void windowClosed(WindowEvent e) {

			}

			public void windowActivated(WindowEvent e) {

			}
		});
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel pnlInputContainer = new JPanel();
			contentPanel.add(pnlInputContainer, BorderLayout.NORTH);
			pnlInputContainer.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblEntityType = new JLabel("Entity type:");
				pnlInputContainer.add(lblEntityType, BorderLayout.WEST);
			}
			{
				final JTextField txtEntityType = new JTextField();
				txtEntityType.addKeyListener(new KeyListener() {

					public void keyTyped(KeyEvent e) {
						if (model instanceof TypeListModel) {
							((TypeListModel) model).setKeyword(txtEntityType
									.getText());
						}
						list.setSelectedIndex(0);
					}

					public void keyReleased(KeyEvent e) {
						// TODO Auto-generated method stub

					}

					public void keyPressed(KeyEvent e) {
						// TODO Auto-generated method stub

					}
				});
				pnlInputContainer.add(txtEntityType, BorderLayout.CENTER);
				txtEntityType.setColumns(10);
			}
			{
				JPanel pnlTextContainer = new JPanel();
				pnlInputContainer.add(pnlTextContainer, BorderLayout.NORTH);
				pnlTextContainer.setLayout(new BorderLayout(0, 0));
				{
					JLabel lblText = new JLabel(
							"Enter type name or a part of its qualified type name:");
					pnlTextContainer.add(lblText);
				}
				{
					Component verticalStrut = Box.createVerticalStrut(5);
					pnlTextContainer.add(verticalStrut, BorderLayout.SOUTH);
				}
				{
					Component verticalStrut = Box.createVerticalStrut(5);
					pnlTextContainer.add(verticalStrut, BorderLayout.NORTH);
				}
			}
		}
		{
			JPanel pnlListContainer = new JPanel();
			contentPanel.add(pnlListContainer, BorderLayout.CENTER);
			pnlListContainer.setLayout(new BorderLayout(0, 0));
			{
				JScrollPane listScroller = new JScrollPane();
				pnlListContainer.add(listScroller);
				{
					list = new JList<Class<?>>(model);
					list.setCellRenderer(new TypeListRenderer());
					list.setSelectedIndex(0);
					list.addMouseListener(new MouseListener() {

						public void mouseReleased(MouseEvent e) {
							// TODO Auto-generated method stub

						}

						public void mousePressed(MouseEvent e) {
							// TODO Auto-generated method stub

						}

						public void mouseExited(MouseEvent e) {
							// TODO Auto-generated method stub

						}

						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub

						}

						public void mouseClicked(MouseEvent e) {
							if (e.getClickCount() > 1) {
								okAction.actionPerformed(new ActionEvent(e
										.getSource(), e.getID(), "ok"));
							}

						}
					});
					listScroller.setViewportView(list);
				}
			}
			{
				Component verticalStrut = Box.createVerticalStrut(5);
				pnlListContainer.add(verticalStrut, BorderLayout.NORTH);
			}
		}
		{
			Component horizontalStrut = Box.createHorizontalStrut(4);
			contentPanel.add(horizontalStrut, BorderLayout.WEST);
		}
		{
			Component horizontalStrut = Box.createHorizontalStrut(5);
			contentPanel.add(horizontalStrut, BorderLayout.EAST);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okayButton = new JButton(okAction);

				// configure the Action with the accelerator (aka: short cut)
				okAction.putValue(Action.ACCELERATOR_KEY,
						KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));

				InputMap inputMap = okayButton
						.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
				inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
						okAction);
				ActionMap actionMap = okayButton.getActionMap();
				actionMap.put(okAction, okAction);

				buttonPane.add(okayButton);
				getRootPane().setDefaultButton(okayButton);
			}
			{
				JButton cancelButton = new JButton(cancelAction);

				// configure the Action with the accelerator (aka: short cut)
				cancelAction.putValue(Action.ACCELERATOR_KEY,
						KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));

				InputMap inputMap = cancelButton
						.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
				inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
						cancelAction);
				ActionMap actionMap = cancelButton.getActionMap();
				actionMap.put(cancelAction, cancelAction);

				buttonPane.add(cancelButton);
			}
		}
		setVisible(true);
		toFront();
	}
}
