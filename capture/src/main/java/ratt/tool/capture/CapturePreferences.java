package ratt.tool.capture;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.KeyStroke;

/**
 * ��ѡ��.
 * 
 * @author Ratt
 */
public class CapturePreferences {

	/**
	 * ��ѡ���ļ�����·��.
	 */
	private static final String PREF_FILE = System.getProperty("user.home")
			+ "/.capture";

	/**
	 * ��ݼ�����.
	 */
	private static Map<String, Integer> keyModifiers = new HashMap<String, Integer>();
	static {
		keyModifiers.put("CTRL", KeyEvent.CTRL_MASK);
		keyModifiers.put("ALT", KeyEvent.ALT_MASK);
		keyModifiers.put("SHIFT", KeyEvent.SHIFT_MASK);
	}

	/**
	 * F1-F12���ܼ�.
	 */
	private static Map<String, Integer> keyFunctions = new HashMap<String, Integer>();
	static {
		keyFunctions.put("F1", KeyEvent.VK_F1);
		keyFunctions.put("F2", KeyEvent.VK_F2);
		keyFunctions.put("F3", KeyEvent.VK_F3);
		keyFunctions.put("F4", KeyEvent.VK_F4);
		keyFunctions.put("F5", KeyEvent.VK_F5);
		keyFunctions.put("F6", KeyEvent.VK_F6);
		keyFunctions.put("F7", KeyEvent.VK_F7);
		keyFunctions.put("F8", KeyEvent.VK_F8);
		keyFunctions.put("F9", KeyEvent.VK_F9);
		keyFunctions.put("F10", KeyEvent.VK_F10);
		keyFunctions.put("F11", KeyEvent.VK_F11);
		keyFunctions.put("F12", KeyEvent.VK_F12);
		keyFunctions.put("DEL", KeyEvent.VK_DELETE);
		keyFunctions.put("DELETE", KeyEvent.VK_DELETE);
	}

	/**
	 * Ψһʵ��.
	 */
	public static CapturePreferences INSTANCE = new CapturePreferences();

	/**
	 * Ĭ��ʵ��.
	 */
	private static final CapturePreferences DEFAULT_INSTANCE = new CapturePreferences();
	static {
		// Ĭ������
		DEFAULT_INSTANCE.closeNotHint = false;
		DEFAULT_INSTANCE.closeToMin = true;

		DEFAULT_INSTANCE.alwaysOnTop = false;
		DEFAULT_INSTANCE.hideWhenCapture = true;
		DEFAULT_INSTANCE.autoCopyToClipboard = true;
		DEFAULT_INSTANCE.showReferenceLine = true;
		DEFAULT_INSTANCE.thumbnailFllowMouse = true;
		DEFAULT_INSTANCE.showThumbnail = true;
		DEFAULT_INSTANCE.referenceLineColorString = "FFFFFF";
		DEFAULT_INSTANCE.referenceLineColor = Color.BLACK;

		DEFAULT_INSTANCE.startX = -1;
		DEFAULT_INSTANCE.startY = -1;
		DEFAULT_INSTANCE.width = CaptureUtil.getScreenWidth() * 2 / 3;
		DEFAULT_INSTANCE.height = CaptureUtil.getScreenHeight() * 2 / 3;

		DEFAULT_INSTANCE.language = "zh_CN";
		DEFAULT_INSTANCE.fontName = "΢���ź�";
		DEFAULT_INSTANCE.fontStyle = 0;
		DEFAULT_INSTANCE.fontSize = 12;
		DEFAULT_INSTANCE.font = new Font(DEFAULT_INSTANCE.fontName,
				DEFAULT_INSTANCE.fontStyle, DEFAULT_INSTANCE.fontSize);

		DEFAULT_INSTANCE.clarity = 100;

		DEFAULT_INSTANCE.shortcutsKeyMap = new LinkedHashMap<String, String>();
		DEFAULT_INSTANCE.shortcutsKeyMap.put("select", "ctrl+D");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("copy", "ctrl+C");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("clear", "DELETE");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("save", "ctrl+S");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("exit", "ctrl+Q");

		DEFAULT_INSTANCE.shortcutsKeyMap.put("full", "ctrl+A");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("rect", "ctrl+R");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("fixedsize", "ctrl+F");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("polygon", "ctrl+P");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("any", "ctrl+N");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("window", "ctrl+W");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("scroll", "ctrl+L");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("ico", "ctrl+I");

		DEFAULT_INSTANCE.shortcutsKeyMap.put("forward", "ctrl+Y");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("backward", "ctrl+Z");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("scale", "ctrl+T");

		DEFAULT_INSTANCE.shortcutsKeyMap.put("colorpick", "alt+C");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("text", "alt+T");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("pen", "alt+P");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("earse", "alt+E");

		DEFAULT_INSTANCE.shortcutsKeyMap.put("zoomin", "Z");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("zoomout", "alt+Z");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("hand", "H");

		DEFAULT_INSTANCE.shortcutsKeyMap.put("option", "ctrl+O");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("help", "F1");
		DEFAULT_INSTANCE.shortcutsKeyMap.put("about", "F2");

		// DEFAULT_INSTANCE.shortcutsKeyMap.put("esc", "ESC"); // �˳���ͼ��ݼ����������޸�
		// DEFAULT_INSTANCE.shortcutsKeyMap.put("ctrl", "CTRL"); //
		// ���ش󴰿ڿ�ݼ����������޸�
		// DEFAULT_INSTANCE.shortcutsKeyMap.put("alt", "ALT"); // ����С���ڿ�ݼ����������޸�
		DEFAULT_INSTANCE.shortcutsKeyStrokeMap = getKeyShorcuts(DEFAULT_INSTANCE.shortcutsKeyMap);

		DEFAULT_INSTANCE.lazy = 200;
		DEFAULT_INSTANCE.windowLazy = 100;
		DEFAULT_INSTANCE.ctrlSize = 100;
		DEFAULT_INSTANCE.dsmTime = 5;
	}

	/**
	 * �Ƿ��һ������.
	 */
	public static boolean FIRST_LANUCH = true;

	/**
	 * ������ѡ��.
	 */
	public static void load() {

		Properties props = new Properties();
		File file = new File(PREF_FILE);
		FIRST_LANUCH = !file.exists();

		if (FIRST_LANUCH) {
			INSTANCE = DEFAULT_INSTANCE;
			return;
		}

		try {
			props.load(new FileReader(file));
		} catch (Exception e) {
			INSTANCE = DEFAULT_INSTANCE;
			return;
		}

		// ��������
		INSTANCE.closeNotHint = "true"
				.equals(props.getProperty("closeNotHint"));
		INSTANCE.closeToMin = "true".equals(props.getProperty("closeToMin"));
		INSTANCE.alwaysOnTop = "true".equals(props.getProperty("alwaysOnTop"));
		INSTANCE.hideWhenCapture = "true".equals(props
				.getProperty("hideWhenCapture"));
		INSTANCE.autoCopyToClipboard = "true".equals(props
				.getProperty("autoCopyToClipboard"));
		INSTANCE.showReferenceLine = "true".equals(props
				.getProperty("showReferenceLine"));
		INSTANCE.referenceLineColorString = props
				.getProperty("referenceLineColorString");
		Color color = getColor(INSTANCE.referenceLineColorString);
		INSTANCE.referenceLineColor = color == null ? DEFAULT_INSTANCE.referenceLineColor
				: color;
		INSTANCE.referenceLineColorString = getColor(INSTANCE.referenceLineColor);
		INSTANCE.showThumbnail = "true".equals(props
				.getProperty("showThumbnail"));
		INSTANCE.thumbnailFllowMouse = "true".equals(props
				.getProperty("thumbnailFllowMouse"));

		INSTANCE.startX = getInt(props, "startX", DEFAULT_INSTANCE.startX);
		INSTANCE.startY = getInt(props, "startY", DEFAULT_INSTANCE.startY);
		INSTANCE.width = getInt(props, "width", DEFAULT_INSTANCE.width);
		INSTANCE.height = getInt(props, "height", DEFAULT_INSTANCE.height);

		INSTANCE.lazy = getInt(props, "lazy", (int) DEFAULT_INSTANCE.lazy);
		INSTANCE.windowLazy = getInt(props, "windowLazy",
				(int) DEFAULT_INSTANCE.windowLazy);
		INSTANCE.clarity = getInt(props, "clarity", DEFAULT_INSTANCE.clarity);

		INSTANCE.language = getValue(props, "language",
				DEFAULT_INSTANCE.language);
		INSTANCE.fontName = getValue(props, "fontName",
				DEFAULT_INSTANCE.fontName);
		INSTANCE.fontStyle = getInt(props, "fontStyle",
				DEFAULT_INSTANCE.fontStyle);
		INSTANCE.fontSize = getInt(props, "fontSize", DEFAULT_INSTANCE.fontSize);
		INSTANCE.font = new Font(INSTANCE.fontName, INSTANCE.fontStyle,
				INSTANCE.fontSize);

		INSTANCE.shortcutsKeyMap = DEFAULT_INSTANCE.shortcutsKeyMap;
		INSTANCE.shortcutsKeyStrokeMap = DEFAULT_INSTANCE.shortcutsKeyStrokeMap;
		
		INSTANCE.dsmTime =  getInt(props, "dsmTime", DEFAULT_INSTANCE.dsmTime);
	}

	/**
	 * ������ѡ��.
	 */
	public static void save(CaptureWindow window) {
		Properties props = new Properties();
		File file = new File(PREF_FILE);
		
		props.setProperty("closeNotHint", String.valueOf(INSTANCE.closeNotHint));
		props.setProperty("closeToMin", String.valueOf(INSTANCE.closeToMin));
		props.setProperty("alwaysOnTop", String.valueOf(INSTANCE.alwaysOnTop));
		props.setProperty("hideWhenCapture", String.valueOf(INSTANCE.hideWhenCapture));
		props.setProperty("autoCopyToClipboard", String.valueOf(INSTANCE.autoCopyToClipboard));
		props.setProperty("showReferenceLine", String.valueOf(INSTANCE.showReferenceLine));
		props.setProperty("referenceLineColorString", getColor(INSTANCE.referenceLineColor));
		props.setProperty("showThumbnail", String.valueOf(INSTANCE.showThumbnail));
		props.setProperty("thumbnailFllowMouse", String.valueOf(INSTANCE.thumbnailFllowMouse));
		
		props.setProperty("startX", String.valueOf(window.getX()));
		props.setProperty("startY", String.valueOf(window.getY()));
		props.setProperty("width", String.valueOf(window.getWidth()));
		props.setProperty("height", String.valueOf(window.getHeight()));
		
		props.setProperty("lazy", String.valueOf(INSTANCE.lazy));
		props.setProperty("windowLazy", String.valueOf(INSTANCE.windowLazy));
		props.setProperty("clarity", String.valueOf(INSTANCE.clarity));
		props.setProperty("dsmTime", String.valueOf(INSTANCE.dsmTime));
		
		props.setProperty("language", INSTANCE.language);

		try {
			props.store(
					new FileWriter(file),
					"last-update="
							+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS")
									.format(new Date()));
		} catch (Exception e) {
		}
	}

	boolean closeNotHint; // �ر�ʱ�Ƿ�����ʾ
	boolean closeToMin; // �ر�ʱ�Ƿ���С��

	boolean alwaysOnTop; // ����ʼ���ö�
	int clarity; // ����͸����
	boolean hideWhenCapture; // ��ͼʱ�Ƿ����ص�ǰ����
	boolean autoCopyToClipboard; // ��ͼ���ʱ�Ƿ��Զ����Ƶ�������
	boolean showReferenceLine; // ��ͼʱ�Ƿ���ʾ�ο���
	boolean showThumbnail; // �Ƿ���ʾ����ͼ
	boolean thumbnailFllowMouse; // ����ͼ�Ƿ�������
	Color referenceLineColor; // �ο�����ɫ
	String referenceLineColorString; // �ο�����ɫ���壺#ffffff

	int startX, startY; // ����λ��
	int width, height; // ���ڴ�С

	String language; // ������Ϣ

	Font font; // ����
	String fontName; // ��������
	int fontSize; // �����С
	int fontStyle; // �����ʽ, 0-����

	Map<String, String> shortcutsKeyMap;// ��ݼ�
	Map<String, KeyStroke> shortcutsKeyStrokeMap; // ��ݼ�

	String textFontName;
	String textFontSize;
	String textFontStyle;
	String textFontColor;
	String penHeight;
	String penStyle;
	String penColor;

	long lazy = 200;// ȫ����ͼ�ӳ�ʱ��
	long windowLazy = 100;// ָ�����ں͹������ڽ�ͼ�ӳ�ʱ��
	int ctrlSize = 100; // CTRL���ƵĴ��ڴ�С
	int dsmTime = 5; // dsm�ƽ�ȴ�ʱ�䣬Ĭ��5��
	
	/**
	 * ˽�й��캯��.
	 */
	private CapturePreferences() {
	}

	private static Color getColor(String color) {
		if (color == null || color.length() != 6) {
			return null;
		}

		try {
			int r = Integer.parseInt(color.substring(0, 2), 16);
			int g = Integer.parseInt(color.substring(2, 4), 16);
			int b = Integer.parseInt(color.substring(4), 16);
			return new Color(r, g, b);
		} catch (Exception e) {
			return null;
		}
	}

	private static String getColor(Color color) {
		if (color == null) {
			return null;
		}

		return getDoubleInt(Integer.toString(color.getRed(), 16))
				+ getDoubleInt(Integer.toString(color.getGreen(), 16))
				+ getDoubleInt(Integer.toString(color.getBlue(), 16));
	}

	private static String getDoubleInt(String integer) {
		if (integer != null) {
			switch (integer.length()) {
			case 1:
				return "0" + integer;
			case 2:
				return integer;
			}
		}

		return "00";
	}

	private static String getValue(Properties props, String key,
			String defaultValue) {
		String value = props.getProperty(key);
		return value == null ? defaultValue : value;
	}

	private static int getInt(Properties props, String key, int defaultValue) {
		String value = getValue(props, key, String.valueOf(defaultValue));
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	private static Map<String, KeyStroke> getKeyShorcuts(
			Map<String, String> keyMap) {
		if (keyMap == null) {
			return null;
		}

		Map<String, KeyStroke> keyStrokeMap = new HashMap<String, KeyStroke>();

		for (Entry<String, String> entry : keyMap.entrySet()) {
			String value = entry.getValue().toUpperCase();
			String[] values = value.split("\\+");
			if (values.length == 0) {
				continue;
			}

			int modifiers = 0;
			if (values.length > 1) {
				for (int i = 0; i < values.length - 1; i++) {
					modifiers += keyModifiers.get(values[i]);
				}

			}

			String key = values[values.length - 1];
			if (key.length() > 1) {
				keyStrokeMap.put(entry.getKey(), KeyStroke.getKeyStroke(
						keyFunctions.get(key), modifiers));
			} else {
				keyStrokeMap.put(entry.getKey(),
						KeyStroke.getKeyStroke(key.charAt(0), modifiers));
			}

		}

		return keyStrokeMap;
	}
}
