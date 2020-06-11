package burp;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.OutputFormat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;

public class BurpExtender implements IBurpExtender, IHttpListener, IMessageEditorTabFactory, ITab {
	private static String configPath = "HaE-Java.xml";
	private static List<Item> itemList;
	private static PrintWriter stdout;
	private static IMessageEditorTab markInfoTab;
	private IBurpExtenderCallbacks callbacks;
	private JPanel mainPanel;
	private JFrame frame;
	private JTextField nameField;
	private JTextField regexField;
	private static HashMap<String, MatchInfo> cacheMap = new HashMap<String, MatchInfo>();

	private static class Item {

		String name;
		String color;
		int highlight;
		String regex;
		int extract;
		int enable;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public int getHighlight() {
			return highlight;
		}

		public void setHighlight(int highlight) {
			this.highlight = highlight;
		}

		public String getRegex() {
			return regex;
		}

		public void setRegex(String regex) {
			this.regex = regex;
		}

		public int getExtract() {
			return extract;
		}

		public void setExtract(int extract) {
			this.extract = extract;
		}

		@SuppressWarnings("unused")
		public int getEnable() {
			return enable;
		}

		@SuppressWarnings("unused")
		public void setEnable(int enable) {
			this.enable = enable;
		}

		@Override
		public String toString() {
			return "Item [name=" + name + ", color=" + color + ", highlight=" + highlight + ", regex=" + regex
					+ ", extract=" + extract + ", enable=" + enable + "]";
		}
	}

	private static class MatchInfo {

		List<Item> itemList;
		String markText;

		public List<Item> getItemList() {
			return itemList;
		}

		public void setItemList(List<Item> itemList) {
			this.itemList = itemList;
		}

		public String getMarkText() {
			return markText;
		}

		public void setMarkText(String markText) {
			this.markText = markText;
		}

		@Override
		public String toString() {
			return "MatchInfo [itemList=" + itemList + ", markText=" + markText + "]";
		}

	}

	private MatchInfo regexMatch(String debugFlag, byte[] content) {

		MatchInfo matchInfoCache = getFromCache(content);
		if (matchInfoCache != null) {
			return matchInfoCache;
		}
		List<Item> matchItemList = new ArrayList<Item>();
		MatchInfo matchInfo = new MatchInfo();
		Set<String> markInfoSet = new HashSet<>();
		for (Iterator<Item> i = itemList.iterator(); i.hasNext();) {
			Item item = (Item) i.next();
			try {
				Matcher match = Pattern.compile(item.getRegex()).matcher(new String(content, "UTF-8"));
				String lines = "";
				int count = 0;
				while (match.find()) {
					if (match.groupCount() == 0) {
						lines += match.group() + "\n";

					} else {
						lines += match.group(1) + "\n";
					}
					count++;
				}
				if (count > 0) {
					if (item.getExtract() == 1) {
						markInfoSet.add(item.getName().strip() + " --> " + lines.strip());
					}
					matchItemList.add(item);
				}
			} catch (Exception e) {
				stdout.println(e.getMessage());
			}

		}
		matchInfo.setItemList(matchItemList);
		String markText = new ArrayList<String>(markInfoSet).toString();
		markText = markText.strip().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", "\r\n");
		matchInfo.setMarkText(markText);
		putInCache(content, matchInfo);
		return matchInfo;
	}

	@Override
	public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
		if (messageIsRequest) {
			return;
		}
		MatchInfo matchInfo = regexMatch("processHttpMessage Of IHttpListener Interface", messageInfo.getResponse());
		List<Item> matchItemList = matchInfo.getItemList();
		int red = 0, orange = 0, yellow = 0, green = 0, cyan = 0, blue = 0, pink = 0, magenta = 0, gray = 0;
		if (!matchItemList.isEmpty()) {
			for (Iterator<Item> i = matchItemList.iterator(); i.hasNext();) {
				Item item = (Item) i.next();
				if (item.getHighlight() == 1) {
					switch (item.getColor()) {
					case "red":
						red++;
						break;
					case "orange":
						orange++;
						break;
					case "yellow":
						yellow++;
						break;
					case "green":
						green++;
						break;
					case "cyan":
						cyan++;
						break;
					case "blue":
						blue++;
						break;
					case "pink":
						pink++;
						break;
					case "magenta":
						magenta++;
						break;
					case "gray":
						gray++;
						break;
					default:
						break;
					}
				}

			}
			if (red >= 1 || orange >= 2) {
				messageInfo.setHighlight("red");
			} else if (orange == 1 || yellow >= 2) {
				messageInfo.setHighlight("orange");
			} else if (yellow == 1 || green >= 2) {
				messageInfo.setHighlight("yellow");
			} else if (green == 1 || cyan >= 2) {
				messageInfo.setHighlight("green");
			} else if (cyan == 1 || blue >= 2) {
				messageInfo.setHighlight("cyan");
			} else if (blue == 1 || pink >= 2) {
				messageInfo.setHighlight("blue");
			} else if (pink == 1 || magenta >= 2) {
				messageInfo.setHighlight("pink");
			} else if (magenta == 1 || gray >= 2) {
				messageInfo.setHighlight("magenta");
			} else if (gray == 1) {
				messageInfo.setHighlight("gray");
			}
		}
	}

	@Override
	public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
		stdout = new PrintWriter(callbacks.getStdout(), true);
		this.callbacks = callbacks;
		stdout.println(
				"HaE-Java(Java Version Of BurpSuite Highlighter and Extractor)\r\nAuthor: undefinedsec\r\nGithub: https://github.com/undefinedsec/HaE-Java");

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				initialize();
			}
		});

		initConfig();
		callbacks.setExtensionName("HaE-Java");
		callbacks.registerHttpListener(BurpExtender.this);
		callbacks.registerMessageEditorTabFactory(BurpExtender.this);
	}

	@Override
	public IMessageEditorTab createNewInstance(IMessageEditorController controller, boolean editable) {
		markInfoTab = new MarkInfoTab(controller, editable);
		return markInfoTab;
	}

	class MarkInfoTab implements IMessageEditorTab {
		private ITextEditor markInfoText;
		private byte[] currentMessage;

		public MarkInfoTab(IMessageEditorController controller, boolean editable) {
			markInfoText = callbacks.createTextEditor();
			markInfoText.setEditable(editable);
		}

		@Override
		public String getTabCaption() {
			return "MarkInfo-Java";
		}

		@Override
		public Component getUiComponent() {
			return markInfoText.getComponent();
		}

		@Override
		public boolean isEnabled(byte[] content, boolean isRequest) {
			if (isRequest) {
				return false;
			} else {
				MatchInfo itemMarkInfo = regexMatch("isEnabled Of MarkInfoTab Class", content);
				if (itemMarkInfo.getMarkText() == null) {
					return false;
				} else {
					List<Item> matchItemList = itemMarkInfo.getItemList();
					if (!matchItemList.isEmpty()) {
						for (Iterator<Item> i = matchItemList.iterator(); i.hasNext();) {
							Item item = (Item) i.next();
							if (item.getExtract() == 1) {
								return true;
							}

						}

					}
				}

			}
			return false;
		}

		@Override
		public byte[] getMessage() {
			return currentMessage;
		}

		@Override
		public boolean isModified() {
			return markInfoText.isTextModified();
		}

		@Override
		public byte[] getSelectedData() {
			return markInfoText.getSelectedText();
		}

		@Override
		public void setMessage(byte[] content, boolean isRequest) {
			if (content == null) {
				markInfoText.setText(null);
			} else {
				MatchInfo itemMarkInfo = regexMatch("setMessage Of MarkInfoTab Class", content);
				String markText = itemMarkInfo.getMarkText();
				markInfoText.setText(markText.getBytes());
			}
			currentMessage = content;
		}
	}

	@Override
	public String getTabCaption() {
		return "HaE-Java";
	}

	@Override
	public Component getUiComponent() {
		return mainPanel;
	}

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
				String name = nameField.getText();
				String color = colorComboBox.getSelectedItem().toString();
				int highlight = highlightCheckBox.isSelected() ? 1 : 0;
				String regex = regexField.getText();
				int extract = extractCheckBox.isSelected() ? 1 : 0;
				int enable = 1;
				addConfigItem(name, color, highlight, regex, extract, enable);
			}
		});
		addButton.setBounds(223, 276, 168, 65);
		setPanel.add(addButton);

		JButton reloadButton_1 = new JButton("reload");
		reloadButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadConfig();
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
			public void actionPerformed(ActionEvent e) {
				String config = readConfig(configPath);
				configTextArea.setText(config);
			}
		});
		loadButton.setBounds(72, 0, 108, 49);
		configPanel.add(loadButton);

		JButton saveButton = new JButton("save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String configText = configTextArea.getText();
				try {
					saveConfig(configText);
				} catch (IOException e1) {
					stdout.println(e1.getMessage());
				}
			}
		});
		saveButton.setBounds(297, 0, 108, 49);
		configPanel.add(saveButton);

		JButton reloadButton_2 = new JButton("reload");
		reloadButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadConfig();
			}
		});
		reloadButton_2.setBounds(517, 1, 109, 49);
		configPanel.add(reloadButton_2);

		callbacks.customizeUiComponent(mainPanel);
		callbacks.customizeUiComponent(tabbedPanel);
		callbacks.customizeUiComponent(setPanel);
		callbacks.customizeUiComponent(configPanel);
		callbacks.addSuiteTab(BurpExtender.this);

	}

	private static List<Item> initConfig() {
		try {
			File configFile = new File(".", configPath);
			List<Node> itemNodeList = new SAXReader().read(configFile).selectNodes("//config/item");
			itemList = new ArrayList<Item>();
			if (itemNodeList != null) {
				for (Iterator<Node> i = itemNodeList.iterator(); i.hasNext();) {
					Node itemNode = (Node) i.next();
					if (Integer.parseInt(itemNode.valueOf("enable").strip()) == 1) {
						Item item = new Item();
						item.setName(itemNode.valueOf("name"));
						item.setColor(itemNode.valueOf("color"));
						item.setHighlight(Integer.parseInt(itemNode.valueOf("highlight")));
						item.setRegex(itemNode.valueOf("regex"));
						item.setExtract(Integer.parseInt(itemNode.valueOf("extract")));
						itemList.add(item);
					}
				}
			}
			return itemList;
		} catch (Exception e) {
			stdout.println(e.getMessage());
			stdout.println("Please put HaE-Java.xml in the same directory of burp");
		}
		return null;
	}

	private static void addConfigItem(String name, String color, int highlight, String regex, int extract, int enable) {
		try {
			File configFile = new File(".", configPath);
			Document document = new SAXReader().read(configFile);
			Element itemElement = document.getRootElement();
			Element valueElement = itemElement.addElement("item");
			Element nameElement = valueElement.addElement("name");
			nameElement.setText(name);
			Element colorElement = valueElement.addElement("color");
			colorElement.setText(color);
			Element highlightElement = valueElement.addElement("highlight");
			highlightElement.setText(String.valueOf(highlight));
			Element regexElement = valueElement.addElement("regex");
			regexElement.setText(regex);
			Element extractElement = valueElement.addElement("extract");
			extractElement.setText(String.valueOf(extract));
			Element enableElement = valueElement.addElement("enable");
			enableElement.setText(String.valueOf(enable));

			Writer osWrite = new OutputStreamWriter(new FileOutputStream(configFile));
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(osWrite, format);
			writer.write(document);
			writer.flush();
			writer.close();

		} catch (Exception e) {
			stdout.println(e.getMessage());
		}
	}

	private static String readConfig(String configPath) {
		String config = "";
		try {
			Scanner scanner = new Scanner(new File(".", configPath));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				config = config + line.strip();
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			stdout.println(e.getMessage());
		}

		XmlFormatter formatter = new XmlFormatter();
		config = formatter.format(config);

		return config;

	}

	private static void saveConfig(String configText) throws IOException {
//		String config = "";
		try {
//			XmlFormatter formatter = new XmlFormatter();
//			config = formatter.format(config);
			File file = new File(".", configPath);
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(configText);
			out.close();
		} catch (Exception e) {
			stdout.println(e.getMessage());
		}
	}

	private void reloadConfig() {
		itemList.clear();
		initConfig();
	}

	private static String getMd5(String content) {
		StringBuffer hexString = new StringBuffer();
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(content.getBytes());
			byte[] hash = md.digest();

			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			stdout.println(e.getMessage());
		}
		return null;
	}

	private static void putInCache(byte[] contentByte, MatchInfo matchInfo) {
		String Content;
		try {
			Content = new String(contentByte, "utf-8");
			String md5 = getMd5(Content);
			cacheMap.put(md5, matchInfo);
		} catch (UnsupportedEncodingException e) {
			stdout.println(e.getMessage());
		}

	}

	private static MatchInfo getFromCache(byte[] contentByte) {
		String Content;
		try {
			Content = new String(contentByte, "utf-8");
			String md5 = getMd5(Content);
			MatchInfo matchInfo = cacheMap.get(md5);
			return matchInfo;
		} catch (UnsupportedEncodingException e) {
			stdout.println(e.getMessage());
		}
		return null;
	}

	public static void main(String[] args) {
	}

}
