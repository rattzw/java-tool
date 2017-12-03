package ratt.tool.capture;

import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TrayIcon;

/**
 * 系统托盘图标.
 * 
 * @author Ratt
 */
public class CaptureTrayIcon extends TrayIcon {
	
	private CaptureWindow window;

	/**
	 * 构造函数.
	 * 
	 * @param window
	 */
	public CaptureTrayIcon(CaptureWindow window) {
		this(getTrayIcon(), getTrayTooltip(), getTrayMenu(window));
		this.window = window;
		addMouseListener(window);
	}

	/**
	 * 构造函数.
	 * 
	 * @param image
	 */
	public CaptureTrayIcon(Image image) {
		super(image);
	}

	/**
	 * 构造函数.
	 * 
	 * @param image
	 * @param tooltip
	 */
	public CaptureTrayIcon(Image image, String tooltip) {
		super(image, tooltip);
	}

	/**
	 * 构造函数.
	 * 
	 * @param image
	 * @param tooltip
	 * @param popupMenu
	 */
	public CaptureTrayIcon(Image image, String tooltip, PopupMenu popupMenu) {
		super(image, tooltip, popupMenu);
	}
	
	/**
	 * 获取系统图标.
	 * 
	 * @return
	 */
	private static Image getTrayIcon() {
		return CaptureUtil.trayImage;
	}

	/**
	 * 获取图标提示.
	 * 
	 * @return
	 */
	private static String getTrayTooltip() {
		return CaptureUtil.getMessage("tray-tooltip");
	}

	/**
	 * 获取弹出菜单.
	 * 
	 * @return
	 */
	private static PopupMenu getTrayMenu(CaptureWindow window) {
		PopupMenu menu = new PopupMenu();
		
		MenuItem openItem = new MenuItem(CaptureUtil.getMessage("tray-menu-open"));
		openItem.addActionListener(window);
		openItem.setActionCommand("open");
		menu.add(openItem);
		menu.addSeparator();
		
		Menu newCaptureMenu = new Menu(CaptureUtil.getMessage("tray-menu-newcapture"));
		menu.add(newCaptureMenu);
		menu.addSeparator();
		
		MenuItem screenItem = new MenuItem(CaptureUtil.getMessage("tray-menu-newscreen"));
		screenItem.addActionListener(window);
		screenItem.setActionCommand("fullscreen");
		MenuItem rectItem = new MenuItem(CaptureUtil.getMessage("tray-menu-newrect"));
		rectItem.addActionListener(window);
		rectItem.setActionCommand("rect");
		MenuItem polygonItem = new MenuItem(CaptureUtil.getMessage("tray-menu-newpolygon"));
		polygonItem.addActionListener(window);
		polygonItem.setActionCommand("polygon");
		polygonItem.setEnabled(false);
		MenuItem windowItem = new MenuItem(CaptureUtil.getMessage("tray-menu-newwindow"));
		windowItem.addActionListener(window);
		windowItem.setActionCommand("window");
		MenuItem scrollWindowItem = new MenuItem(CaptureUtil.getMessage("tray-menu-scrollwindow"));
		scrollWindowItem.addActionListener(window);
		scrollWindowItem.setActionCommand("scrollwindow");
		
		MenuItem fixedSizeItem = new MenuItem(CaptureUtil.getMessage("tray-menu-fixedsize"));
		fixedSizeItem.addActionListener(window);
		fixedSizeItem.setActionCommand("fixedsize");
		fixedSizeItem.setEnabled(false);
		MenuItem anyItem = new MenuItem(CaptureUtil.getMessage("tray-menu-any"));
		anyItem.addActionListener(window);
		anyItem.setActionCommand("any");
		anyItem.setEnabled(false);
		MenuItem icoItem = new MenuItem(CaptureUtil.getMessage("tray-menu-ico"));
		icoItem.addActionListener(window);
		icoItem.setActionCommand("ico");
		icoItem.setEnabled(false);
		
		newCaptureMenu.add(screenItem);
		newCaptureMenu.add(rectItem);
		newCaptureMenu.add(fixedSizeItem);
		newCaptureMenu.add(polygonItem);
		newCaptureMenu.add(anyItem);
		newCaptureMenu.add(windowItem);
		newCaptureMenu.add(scrollWindowItem);
		newCaptureMenu.add(icoItem);
		
		MenuItem optionItem = new MenuItem(CaptureUtil.getMessage("tray-menu-option"));
		optionItem.addActionListener(window);
		optionItem.setActionCommand("option");
		menu.add(optionItem);
		MenuItem aboutItem = new MenuItem(CaptureUtil.getMessage("tray-menu-about"));
		aboutItem.addActionListener(window);
		aboutItem.setActionCommand("about");
		menu.add(aboutItem);
		MenuItem helpItem = new MenuItem(CaptureUtil.getMessage("tray-menu-help"));
		helpItem.addActionListener(window);
		helpItem.setActionCommand("help");
		menu.add(helpItem);
		menu.addSeparator();
		
		MenuItem exitItem = new MenuItem(CaptureUtil.getMessage("tray-menu-exit"));
		exitItem.addActionListener(window);
		exitItem.setActionCommand("exit");
		menu.add(exitItem);
		
		return menu;
	}
}
