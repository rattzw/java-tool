package ratt.tool.capture;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.awt.AWTUtilities;

/**
 * 菜单栏.
 * 
 * @author Ratt
 */
public class CaptureMenuBar extends JMenuBar {

	CaptureWindow window;

	JMenu fileMenu;
	JMenu newCaptureMenu;
	JMenuItem newScreeCaptureMenuItem;
	JMenuItem newRectCaptureMenuItem;
	JMenuItem newRectSizeCaptureMenuItem;
	JMenuItem newPolygonCaptureMenuItem;
	JMenuItem newAnyCaptureMenuItem;
	JMenuItem newWindowCaptureMenuItem;
	JMenuItem newScrollWindowCaptureMenuItem;
	JMenuItem newIcoCaptureMenuItem;
	JMenuItem saveMenuItem;
	JMenuItem exitMenuItem;

	JMenu editMenu;
	JMenuItem selectMenuItem;
	JMenuItem copyMenuItem;
	JMenuItem clearMenuItem;
	JMenuItem forwardMenuItem;
	JMenuItem backwordMenuItem;
	JMenuItem turnRightMenuItem;
	JMenuItem turnLeftMenuItem;
	JMenuItem turnhMenuItem;
	JMenuItem turnvMenuItem;
	JMenuItem scaleMenuItem;

	JMenu toolMenu;
	JMenuItem colorpickMenuItem;
	JMenuItem textMenuItem;
	JMenuItem penMenuItem;
	JMenuItem eraseMenuItem;
	JMenuItem watermarkMenuItem;
	JMenuItem ocrMenuItem;
	JMenuItem dsmMenuItem;
	boolean isDsming = false;

	JMenu viewMenu;
	JMenuItem zoomInMenuItem;
	JMenuItem zoomOutMenuItem;
	JMenuItem zoomAdaptWindowMenuItem;
	JMenuItem fullScreenMenuItem;
	JMenuItem handMenuItem;

	JMenu optionMenu;
	JMenu clarityMenu;
	JSlider claritySlider;
	JCheckBoxMenuItem closeToHideMenuItem;
	JCheckBoxMenuItem hideToCaptureMenuItem;
	JCheckBoxMenuItem copyToClipboardMenuItem;
	JCheckBoxMenuItem alwaysOnTopMenuItem;
	JCheckBoxMenuItem showReferenceLineMenuItem;
	JMenuItem optionMenuItem;

	JMenu helpMenu;
	JMenuItem aboutMenuItem;
	JMenuItem helpMenuItem;

	public CaptureMenuBar(CaptureWindow window) {
		super();
		this.window = window;

		init();
		listen();
	}

	/**
	 * 初始化.
	 */
	private void init() {
		// 文件菜单
		fileMenu = new JMenu(CaptureUtil.getMessage("menu-file"));
		fileMenu.setMnemonic('F');
		add(fileMenu);

		newCaptureMenu = new JMenu(CaptureUtil.getMessage("item-new"));
		newCaptureMenu.setIcon(new ImageIcon(CaptureUtil.newCaptureImage));

		newScreeCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-screen"), new ImageIcon(
						CaptureUtil.newFullScreenCaptureImage));
		newScreeCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("full"));
		newCaptureMenu.add(newScreeCaptureMenuItem);
		newRectCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-rect"), new ImageIcon(
						CaptureUtil.newRectCaptureImage));
		newRectCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("rect"));
		newCaptureMenu.add(newRectCaptureMenuItem);
		newRectSizeCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-fixedsize"), new ImageIcon(
						CaptureUtil.fixedsizeCaptureImage));
		newRectSizeCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("fixedsize"));
		newCaptureMenu.add(newRectSizeCaptureMenuItem);
		newPolygonCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-polygon"), new ImageIcon(
						CaptureUtil.newPolygonCaptureImage));
		newPolygonCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("polygon"));
		newCaptureMenu.add(newPolygonCaptureMenuItem);
		newAnyCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-any"), new ImageIcon(
						CaptureUtil.anyCaptureImage));
		newAnyCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("any"));
		newCaptureMenu.add(newAnyCaptureMenuItem);
		newWindowCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-window"), new ImageIcon(
						CaptureUtil.newWindowCaptureImage));
		newWindowCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("window"));
		newCaptureMenu.add(newWindowCaptureMenuItem);
		newScrollWindowCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-scroll-window"),
				new ImageIcon(CaptureUtil.newScrollWindowCaptureImage));
		newScrollWindowCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("scroll"));
		newCaptureMenu.add(newScrollWindowCaptureMenuItem);
		newIcoCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-ico"),
				new ImageIcon(CaptureUtil.icoCaptureImage));
		newIcoCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("ico"));
		newCaptureMenu.add(newIcoCaptureMenuItem);
		

		fileMenu.add(newCaptureMenu);

		saveMenuItem = new JMenuItem(CaptureUtil.getMessage("item-save"),
				new ImageIcon(CaptureUtil.saveImage));
		saveMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("save"));
		exitMenuItem = new JMenuItem(CaptureUtil.getMessage("item-exit"));
		exitMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("exit"));

		fileMenu.add(saveMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		// 编辑菜单
		editMenu = new JMenu(CaptureUtil.getMessage("menu-edit"));
		editMenu.setMnemonic('E');
		add(editMenu);

		selectMenuItem = new JMenuItem(CaptureUtil.getMessage("item-select"),
				new ImageIcon(CaptureUtil.selectImage));
		selectMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("select"));
		copyMenuItem = new JMenuItem(CaptureUtil.getMessage("item-copy"),
				new ImageIcon(CaptureUtil.copyImage));
		copyMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("copy"));
		clearMenuItem = new JMenuItem(CaptureUtil.getMessage("item-clear"),
				new ImageIcon(CaptureUtil.clearImage));
		clearMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("clear"));

		forwardMenuItem = new JMenuItem(CaptureUtil.getMessage("item-forward"),
				new ImageIcon(CaptureUtil.forwardImage));
		forwardMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("forward"));
		backwordMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-backward"), new ImageIcon(
						CaptureUtil.backwardImage));
		backwordMenuItem
				.setAccelerator(CaptureUtil.getShortcutsKey("backward"));

		turnRightMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-turnright"), new ImageIcon(
						CaptureUtil.turnrImage));
		turnLeftMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-turnleft"), new ImageIcon(
						CaptureUtil.turnlImage));
		turnhMenuItem = new JMenuItem(CaptureUtil.getMessage("item-turnh"),
				new ImageIcon(CaptureUtil.turnhImage));
		turnvMenuItem = new JMenuItem(CaptureUtil.getMessage("item-turnv"),
				new ImageIcon(CaptureUtil.turnvImage));
		scaleMenuItem = new JMenuItem(CaptureUtil.getMessage("item-scale"),
				new ImageIcon(CaptureUtil.scaleImage));
		scaleMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("scale"));

		editMenu.add(selectMenuItem);
		editMenu.add(copyMenuItem);
		editMenu.add(clearMenuItem);
		editMenu.addSeparator();
		editMenu.add(forwardMenuItem);
		editMenu.add(backwordMenuItem);
		editMenu.addSeparator();
		editMenu.add(turnRightMenuItem);
		editMenu.add(turnLeftMenuItem);
		editMenu.add(turnhMenuItem);
		editMenu.add(turnvMenuItem);
		editMenu.add(scaleMenuItem);

		// 工具菜单
		toolMenu = new JMenu(CaptureUtil.getMessage("menu-tool"));
		toolMenu.setMnemonic('T');
		add(toolMenu);

		colorpickMenuItem = new JMenuItem(CaptureUtil.getMessage("item-pick"),
				new ImageIcon(CaptureUtil.pickImage));
		colorpickMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("colorpick"));
		textMenuItem = new JMenuItem(CaptureUtil.getMessage("item-text"),
				new ImageIcon(CaptureUtil.editTextImage));
		textMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("text"));
		penMenuItem = new JMenuItem(CaptureUtil.getMessage("item-pen"),
				new ImageIcon(CaptureUtil.penImage));
		penMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("pen"));
		eraseMenuItem = new JMenuItem(CaptureUtil.getMessage("item-erase"),
				new ImageIcon(CaptureUtil.earseImage));
		eraseMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("earse"));
		watermarkMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-watermark"), new ImageIcon(
						CaptureUtil.watermarkImage));
		ocrMenuItem = new JMenuItem(CaptureUtil.getMessage("item-ocr"),
				new ImageIcon(CaptureUtil.ocrImage));
		dsmMenuItem = new JMenuItem(CaptureUtil.getMessage("item-dsm"),
				new ImageIcon(CaptureUtil.dsmImage));
		

		toolMenu.add(colorpickMenuItem);
		toolMenu.add(textMenuItem);
		toolMenu.add(penMenuItem);
		toolMenu.add(eraseMenuItem);
		toolMenu.addSeparator();
		toolMenu.add(watermarkMenuItem);
		toolMenu.add(ocrMenuItem);
		toolMenu.add(dsmMenuItem);

		// 视图菜单
		viewMenu = new JMenu(CaptureUtil.getMessage("menu-view"));
		viewMenu.setMnemonic('V');
		add(viewMenu);

		zoomInMenuItem = new JMenuItem(CaptureUtil.getMessage("item-zoomin"),
				new ImageIcon(CaptureUtil.zoomInImage));
		zoomInMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("zoomin"));
		zoomOutMenuItem = new JMenuItem(CaptureUtil.getMessage("item-zoomout"),
				new ImageIcon(CaptureUtil.zoomOutImage));
		zoomOutMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("zoomout"));
		zoomAdaptWindowMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-adaptwindow"), new ImageIcon(
						CaptureUtil.adaptWindowImage));
		fullScreenMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-fullscreen"), new ImageIcon(
						CaptureUtil.fullScreenImage));
		handMenuItem = new JMenuItem(CaptureUtil.getMessage("item-hand"),
				new ImageIcon(CaptureUtil.handImage));
		handMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("hand"));

		viewMenu.add(zoomInMenuItem);
		viewMenu.add(zoomOutMenuItem);
		viewMenu.add(zoomAdaptWindowMenuItem);
		viewMenu.add(fullScreenMenuItem);
		viewMenu.add(handMenuItem);

		// 选项菜单
		optionMenu = new JMenu(CaptureUtil.getMessage("menu-option"));
		optionMenu.setMnemonic('O');
		add(optionMenu);

		clarityMenu = new JMenu(CaptureUtil.getMessage("menu-clarity"));
		claritySlider = new JSlider(0, 100, 100);
		claritySlider.putClientProperty("JSlider.isFilled", Boolean.TRUE);
		claritySlider.setPaintTicks(true);
		claritySlider.setMajorTickSpacing(20);
		claritySlider.setMinorTickSpacing(20);
		claritySlider.setPaintLabels(true);
		claritySlider.setSnapToTicks(true);
		claritySlider.setValue(CapturePreferences.INSTANCE.clarity);
		clarityMenu.add(claritySlider);

		alwaysOnTopMenuItem = new JCheckBoxMenuItem(
				CaptureUtil.getMessage("item-alwaysontop"), false);
		alwaysOnTopMenuItem.setSelected(CapturePreferences.INSTANCE.alwaysOnTop);
		closeToHideMenuItem = new JCheckBoxMenuItem(
				CaptureUtil.getMessage("item-closetohide"), true);
		closeToHideMenuItem.setSelected(CapturePreferences.INSTANCE.closeToMin);
		hideToCaptureMenuItem = new JCheckBoxMenuItem(
				CaptureUtil.getMessage("item-hidetocapture"), true);
		hideToCaptureMenuItem.setSelected(CapturePreferences.INSTANCE.hideWhenCapture);
		copyToClipboardMenuItem = new JCheckBoxMenuItem(
				CaptureUtil.getMessage("item-copytoclipboard"), true);
		copyToClipboardMenuItem.setSelected(CapturePreferences.INSTANCE.autoCopyToClipboard);
		showReferenceLineMenuItem = new JCheckBoxMenuItem(
				CaptureUtil.getMessage("item-showreferenceline"), false);
		showReferenceLineMenuItem.setSelected(CapturePreferences.INSTANCE.showReferenceLine);
		optionMenuItem = new JMenuItem(CaptureUtil.getMessage("item-option"));
		optionMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("option"));

		optionMenu.add(alwaysOnTopMenuItem);
		optionMenu.add(clarityMenu);
		optionMenu.addSeparator();
		optionMenu.add(closeToHideMenuItem);
		optionMenu.add(hideToCaptureMenuItem);
		optionMenu.add(copyToClipboardMenuItem);
		optionMenu.add(showReferenceLineMenuItem);
		optionMenu.addSeparator();
		optionMenu.add(optionMenuItem);

		// 帮助菜单
		helpMenu = new JMenu(CaptureUtil.getMessage("menu-help"));
		helpMenu.setMnemonic('H');
		add(helpMenu);

		helpMenuItem = new JMenuItem(CaptureUtil.getMessage("item-help"),
				new ImageIcon(CaptureUtil.helpImage));
		helpMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("help"));
		aboutMenuItem = new JMenuItem(CaptureUtil.getMessage("item-about"),
				new ImageIcon(CaptureUtil.aboutImage));
		aboutMenuItem.setAccelerator(CaptureUtil.getShortcutsKey("about"));
		helpMenu.add(helpMenuItem);
		helpMenu.add(aboutMenuItem);
	}

	/**
	 * 监听.
	 */
	private void listen() {
		claritySlider.addChangeListener(window);
		
		optionMenuItem.addActionListener(window);
		optionMenuItem.setActionCommand("option");
		
		alwaysOnTopMenuItem.addActionListener(window);
		alwaysOnTopMenuItem.setActionCommand("top");
		
		hideToCaptureMenuItem.addActionListener(window);
		hideToCaptureMenuItem.setActionCommand("hide");
		
		copyToClipboardMenuItem.addActionListener(window);
		copyToClipboardMenuItem.setActionCommand("ctc");
		
		showReferenceLineMenuItem.addActionListener(window);
		showReferenceLineMenuItem.setActionCommand("refline");
		
		closeToHideMenuItem.addActionListener(window);
		closeToHideMenuItem.setActionCommand("closemin");

		clearMenuItem.addActionListener(window);
		clearMenuItem.setActionCommand("clear");

		helpMenuItem.addActionListener(window);
		helpMenuItem.setActionCommand("help");

		aboutMenuItem.addActionListener(window);
		aboutMenuItem.setActionCommand("about");

		exitMenuItem.addActionListener(window);
		exitMenuItem.setActionCommand("exit");

		newScreeCaptureMenuItem.addActionListener(window);
		newScreeCaptureMenuItem.setActionCommand("fullscreen");

		newRectCaptureMenuItem.addActionListener(window);
		newRectCaptureMenuItem.setActionCommand("rect");
		
		newRectSizeCaptureMenuItem.addActionListener(window);
		newRectSizeCaptureMenuItem.setActionCommand("fixedsize");
		
		newAnyCaptureMenuItem.addActionListener(window);
		newAnyCaptureMenuItem.setActionCommand("any");
		
		newIcoCaptureMenuItem.addActionListener(window);
		newIcoCaptureMenuItem.setActionCommand("ico");
		
		newPolygonCaptureMenuItem.addActionListener(window);
		newPolygonCaptureMenuItem.setActionCommand("polygon");
		
		newWindowCaptureMenuItem.addActionListener(window);
		newWindowCaptureMenuItem.setActionCommand("window");
		
		newScrollWindowCaptureMenuItem.addActionListener(window);
		newScrollWindowCaptureMenuItem.setActionCommand("scroll");

		turnRightMenuItem.addActionListener(window);
		turnRightMenuItem.setActionCommand("turnr");

		turnLeftMenuItem.addActionListener(window);
		turnLeftMenuItem.setActionCommand("turnl");

		turnhMenuItem.addActionListener(window);
		turnhMenuItem.setActionCommand("turnh");

		turnvMenuItem.addActionListener(window);
		turnvMenuItem.setActionCommand("turnv");
		
		copyMenuItem.addActionListener(window);
		copyMenuItem.setActionCommand("copy");
		
		saveMenuItem.addActionListener(window);
		saveMenuItem.setActionCommand("save");
		
		dsmMenuItem.addActionListener(window);
		dsmMenuItem.setActionCommand("crack");
	}
}
