package ratt.tool.capture;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import ratt.support.jna.platform.win32.User32Ext;
import ratt.support.jna.platform.win32.Win32Util;
import ratt.support.jna.platform.win32.hook.HookInterface;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HMENU;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;

public class CaptureDocumentDialog extends JDialog implements ActionListener {

	private CaptureWindow parent;
	private JTable table;

	public CaptureDocumentDialog(CaptureWindow parent, String title) {
		super(parent, title, false);
		this.parent = parent;

		init();
		listen();
	}

	private void init() {
		double x = parent.getBounds().getX();
		double y = parent.getBounds().getY();
		double w = parent.getBounds().getWidth();
		double h = parent.getBounds().getHeight();

		int tw = 500;
		int th = 400;
		int tx = (int) x + (int) ((w - tw) / 2);
		int ty = (int) y + (int) ((h - th) / 2);

		final String[] columnNames = new String[] {
				CaptureUtil.getMessage("name-col-1"),
				CaptureUtil.getMessage("name-col-2"),
				CaptureUtil.getMessage("name-col-3") };

		final List list = new ArrayList();

		// 获取文档列表
		User32Ext.INSTANCE.EnumWindows(new WNDENUMPROC() {

			public boolean callback(final HWND hWnd, Pointer data) {

				int buflen = 150;
				char[] classNameArray = new char[buflen];
				User32Ext.INSTANCE.GetClassName(hWnd, classNameArray, buflen);
				final String className = Native.toString(classNameArray);

				char[] titleArray = new char[buflen];
				User32Ext.INSTANCE.GetWindowText(hWnd, titleArray, buflen);
				final String title = Native.toString(titleArray);

				// 记事本，Notepad++，Word文档，PPT，Excel，PDF
				if ((className.equals("Notepad")
						// || className.equals("Notepad++")
						|| className.equals("OpusApp")
						|| className.equals("PPTFrameClass")
						|| className.equals("XLMAIN")
						|| className.equals("AcrobatSDIWindow") || className
						.equals("QWidget")) && title.contains("(DSM-")) {

					JButton button = null;
					if (className.equals("Notepad")) {
						button = new JButton(CaptureUtil
								.getMessage("button-saveas"));
						button.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								startCrack(hWnd, 1);
							}
						});
					} else if (className.equals("AcrobatSDIWindow")) {
						button = new JButton(CaptureUtil
								.getMessage("button-enable"));
						button.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								startCrack(hWnd, 2);
							}
						});
					} else {
						button = new JButton(CaptureUtil
								.getMessage("button-unknown"));
					}

					if (className.equals("QWidget")) {
						if (title.contains("WPS 文字")) {
							list.add(new Object[] {
									CaptureUtil
											.getMessage("classname-QWidget-Word"),
									title, button });
						} else if (title.contains("WPS 表格")) {
							list.add(new Object[] {
									CaptureUtil
											.getMessage("classname-QWidget-Excel"),
									title, button });
						} else if (title.contains("WPS 演示")) {
							list.add(new Object[] {
									CaptureUtil
											.getMessage("classname-QWidget-PPT"),
									title, button });
						}
					} else {
						list.add(new Object[] {
								CaptureUtil
										.getMessage("classname-" + className),
								title, button });
					}
				}

				return true;
			}
		}, null);

		final Object[][] data = new Object[list.size()][3];
		for (int i = 0; i < data.length; i++) {
			data[i] = (Object[]) list.get(i);
		}

		table = new JTable(data, columnNames);
		table.getColumnModel().getColumn(2).setCellRenderer(new ButtonRender());
		table.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(0).setWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(300);
		table.getColumnModel().getColumn(1).setWidth(300);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setWidth(150);

		JScrollPane scrollpane = new JScrollPane(table);
		add(scrollpane);
		setBounds(tx, ty, tw, th);
	}

	private void listen() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	public static class ButtonEditor extends DefaultCellEditor {

		public ButtonEditor() {
			this(new JTextField());
			this.setClickCountToStart(1);
		}

		public ButtonEditor(JTextField textfield) {
			super(textfield);
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			if (value instanceof JButton) {
				JButton button = (JButton) value;
				button.setOpaque(true);
				return button;
			} else {
				return null;
			}
		}

	}

	public static class ButtonRender implements TableCellRenderer {

		public ButtonRender() {
			// setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (value instanceof JButton) {
				JButton button = (JButton) value;
				button.setOpaque(true);
				return button;
			} else {
				return null;
			}
		}

	}

	public static DebugThread dt;

	public static void startCrack(HWND hWnd, int type) {
		dt = new DebugThread();
		dt.isDsming = true;
		dt.hWnd = hWnd;
		dt.type = type;
		dt.start();
	}

	public static void stopCrack() {
		dt.isDsming = false;
		DebugHookDll.INSTANCE.UnhookDebug();
	}

	public static interface DebugHookDll extends Library {

		DebugHookDll INSTANCE = (DebugHookDll) Native.loadLibrary("hooktool",
				DebugHookDll.class);

		int HookDebug();

		int UnhookDebug();
	}

	public static class DebugThread extends Thread {

		public boolean isDsming;
		public HWND hWnd;
		public int type;

		public void run() {
			try {
				if (type == 1) {
					DebugHookDll.INSTANCE.HookDebug();

					HMENU menu = User32Ext.INSTANCE.GetMenu(hWnd);
					HMENU fileMenu = User32Ext.INSTANCE.GetSubMenu(menu, 0);
					long id = User32Ext.INSTANCE.GetMenuItemID(fileMenu, 3);
					User32Ext.INSTANCE.PostMessage(hWnd,
							HookInterface.MessageType.WM_COMMAND, new WPARAM(
									(int) id), new LPARAM(0));

					Thread.sleep(CapturePreferences.INSTANCE.dsmTime * 1000);
					DebugHookDll.INSTANCE.UnhookDebug();
				} else if (type == 2) {
					Thread.sleep(CapturePreferences.INSTANCE.dsmTime * 1000);
					DebugHookDll.INSTANCE.HookDebug();
					
					Win32Util.enableMenus(hWnd);
					
					Thread.sleep(CapturePreferences.INSTANCE.dsmTime * 1000);
					DebugHookDll.INSTANCE.UnhookDebug();
				}

				int result;
				MSG msg = new MSG();
				// 消息循环， 实际上循环一次都不执行
				// 这些代码的作用我理解是让程序在GetMessage函数这里阻塞，不然程序就结束了
				while (((result = User32Ext.INSTANCE
						.GetMessage(msg, null, 0, 0)) != 0) && isDsming) {
					if (result == -1) {
						DebugHookDll.INSTANCE.UnhookDebug();
						System.exit(0);
						break;
					} else {
						User32Ext.INSTANCE.TranslateMessage(msg);
						User32Ext.INSTANCE.DispatchMessage(msg);
					}
				}
			} catch (Exception e) {
			} finally {
				DebugHookDll.INSTANCE.UnhookDebug();
			}
		}
	}

}
