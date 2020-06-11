package burp;

import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;

public class BurpExtenderSwing {

	private JFrame frame;
	private JTextField nameField;
	private JPanel mainPanel;
	private JTextField regexField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BurpExtenderSwing window = new BurpExtenderSwing();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BurpExtenderSwing() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 684, 434);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPanel = new JTabbedPane(JTabbedPane.TOP);
		mainPanel.add(tabbedPanel);

		JPanel setPanel = new JPanel();
		tabbedPanel.addTab("Set", null, setPanel, null);
		setPanel.setLayout(null);

		JLabel nameLabel = new JLabel("name:");
		nameLabel.setBounds(10, 50, 46, 14);
		setPanel.add(nameLabel);

		nameField = new JTextField();
		nameField.setBounds(85, 37, 568, 65);
		setPanel.add(nameField);
		nameField.setColumns(10);

		JLabel regexLabel = new JLabel("regex:");
		regexLabel.setBounds(10, 145, 46, 14);
		setPanel.add(regexLabel);

		regexField = new JTextField();
		regexField.setBounds(84, 132, 569, 65);
		setPanel.add(regexField);
		regexField.setColumns(10);

		JLabel colorLabel = new JLabel("color:");
		colorLabel.setBounds(10, 213, 46, 14);
		setPanel.add(colorLabel);

		JComboBox<Object> colorComboBox = new JComboBox<Object>();
		colorComboBox.setModel(new DefaultComboBoxModel<Object>(
				new String[] { "red", "orange", "yellow", "green", "cyan", "blue", "pink", "magenta", "gray" }));
		colorComboBox.setBounds(84, 209, 86, 22);
		setPanel.add(colorComboBox);

		JLabel extractLabel = new JLabel("extract:");
		extractLabel.setBounds(10, 257, 46, 14);
		setPanel.add(extractLabel);

		JCheckBox extractCheckBox = new JCheckBox("");
		extractCheckBox.setBounds(85, 248, 97, 23);
		setPanel.add(extractCheckBox);

		JLabel highlightLabel = new JLabel("highlight:");
		highlightLabel.setBounds(10, 301, 46, 14);
		setPanel.add(highlightLabel);

		JCheckBox highlightCheckBox = new JCheckBox("");
		highlightCheckBox.setBounds(85, 292, 97, 23);
		setPanel.add(highlightCheckBox);

		JButton addButton = new JButton("add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		addButton.setBounds(223, 276, 168, 65);
		setPanel.add(addButton);

		JButton reloadButton_1 = new JButton("reload");
		reloadButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		reloadButton_1.setBounds(446, 276, 144, 65);
		setPanel.add(reloadButton_1);

		JPanel configPanel = new JPanel();
		tabbedPanel.addTab("Config", null, configPanel, null);
		configPanel.setLayout(null);

		JScrollPane scrollPanel = new JScrollPane();
		scrollPanel.setBounds(0, 61, 1300, 550);
		configPanel.add(scrollPanel);

		JTextArea configTextArea = new JTextArea();
		scrollPanel.setViewportView(configTextArea);

		JButton loadButton = new JButton("load");
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			}

		});
		loadButton.setBounds(72, 0, 108, 49);
		configPanel.add(loadButton);

		JButton saveButton = new JButton("save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		saveButton.setBounds(297, 0, 108, 49);
		configPanel.add(saveButton);

		JButton reloadButton_2 = new JButton("reload");
		reloadButton_2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			}

		});
		reloadButton_2.setBounds(517, 1, 109, 49);
		configPanel.add(reloadButton_2);
	}
}
