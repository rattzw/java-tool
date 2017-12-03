package ratt.tool.capture;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.awt.AWTUtilities;

/**
 * 截图主窗口.
 * 
 * @author Ratt
 */
public class CaptureWindow extends JFrame implements ActionListener,
		ChangeListener, MouseListener {

	CaptureWindowWindow winWindow; // 指定窗口

	CaptureMenuBar menuBar; // 菜单栏
	CaptureToolBar toolBar; // 工具栏
	CaptureImageScrollPane imagePanel; // 图片操作面板
	CaptureTrayIcon trayIcon; // 系统托盘

	/**
	 * 构造函数.
	 */
	public CaptureWindow() {
		super();

		init();
		listen();
	}

	/**
	 * 初始化.
	 */
	private void init() {
		// 全局设置
		CaptureUtil.setDefaultUI();

		// 布局
		getContentPane().setLayout(new BorderLayout());

		// 菜单栏
		menuBar = new CaptureMenuBar(this);
		setJMenuBar(menuBar);

		// 工具栏
		toolBar = new CaptureToolBar(this);
		add(toolBar, BorderLayout.PAGE_START);

		// 图片操作面板
		imagePanel = new CaptureImageScrollPane(this);
		add(imagePanel, BorderLayout.CENTER);

		// 系统托盘
		trayIcon = new CaptureTrayIcon(this);
		try {
			SystemTray.getSystemTray().add(trayIcon);
		} catch (Exception e) {
		}

		// 大小、位置设值
		int w = CapturePreferences.INSTANCE.width;
		int h = CapturePreferences.INSTANCE.height;

		int x = CapturePreferences.INSTANCE.startX;
		int y = CapturePreferences.INSTANCE.startY;
		if (x == -1) {
			x = (CaptureUtil.getScreenWidth() - w) / 2;
		}
		if (y == -1) {
			y = (CaptureUtil.getScreenHeight() - h) / 2;
		}
		setLocation(x, y);
		setSize(w, h);

		// 标题、图标设置
		setTitle(CaptureUtil.getMessage("window-title"));
		setIconImage(CaptureUtil.titleImage);

		// 其他设置，最大化、关闭
		setAlwaysOnTop(CapturePreferences.INSTANCE.alwaysOnTop);
		AWTUtilities.setWindowOpacity(this,
				CapturePreferences.INSTANCE.clarity / 100f);
	}

	/**
	 * 监听.
	 */
	private void listen() {
		// 关闭按钮
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (!CapturePreferences.INSTANCE.closeNotHint) {
					int option = JOptionPane.showConfirmDialog(
							CaptureWindow.this,
							CaptureUtil.getMessage("min-to-tray"),
							CaptureUtil.getMessage("min-to-tray-ask"),
							JOptionPane.YES_NO_OPTION);

					CapturePreferences.INSTANCE.closeNotHint = true;

					if (option == JOptionPane.YES_OPTION) {
						CapturePreferences.INSTANCE.closeToMin = true;
					} else if (option == JOptionPane.NO_OPTION) {
						CapturePreferences.INSTANCE.closeToMin = false;
					}

				}

				CaptureWindow.this.menuBar.closeToHideMenuItem
						.setSelected(CapturePreferences.INSTANCE.closeToMin);
				if (CapturePreferences.INSTANCE.closeToMin) {
					CaptureWindow.this.setVisible(false);
				} else {
					Hook.unhook();
					CapturePreferences.save(CaptureWindow.this);
					CaptureWindow.this.dispose();
					System.exit(0);
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();

		// 打开应用
		if ("open".equals(command)) {
			CaptureUtil.openWindow(this);
		}

		// 退出应用
		else if ("exit".equals(command)) {
			Hook.unhook();
			CapturePreferences.save(this);
			dispose();
			System.exit(0);
		}

		// 全屏截图
		else if ("fullscreen".equals(command)) {
			boolean visible = CapturePreferences.INSTANCE.hideWhenCapture;
			setVisible(!visible);

			try {
				if (CapturePreferences.INSTANCE.hideWhenCapture) {
					Thread.sleep(CapturePreferences.INSTANCE.lazy);
				}

				Robot robot = new Robot();
				BufferedImage image = robot.createScreenCapture(new Rectangle(
						CaptureUtil.getScreenSize()));
				imagePanel.imagePanel.image = image;
				imagePanel.repaint();
				CaptureUtil.openWindow(this);
				imagePanel.setScrollbar();
				endCapture();
			} catch (Exception exp) {
				String title = CaptureUtil
						.getMessage("full-screen-error-title");
				String msg = CaptureUtil.getMessage(
						"full-screen-error-message", exp.getMessage());
				JOptionPane.showMessageDialog(CaptureUtil.getParent(this), msg,
						title, JOptionPane.ERROR_MESSAGE);
			}
		}

		// 矩形区域
		else if ("rect".equals(command)) {
			boolean visible = CapturePreferences.INSTANCE.hideWhenCapture;
			setVisible(!visible);

			try {
				if (CapturePreferences.INSTANCE.hideWhenCapture) {
					Thread.sleep(CapturePreferences.INSTANCE.lazy);
				}

				CaptureRectWindow rectWindow = new CaptureRectWindow(this);
				rectWindow.setVisible(true);
			} catch (Exception exp) {
				String title = CaptureUtil.getMessage("rect-error-title");
				String msg = CaptureUtil.getMessage("rect-error-message",
						exp.getMessage());
				JOptionPane.showMessageDialog(CaptureUtil.getParent(this), msg,
						title, JOptionPane.ERROR_MESSAGE);
			}
		}

		// 多边形区域
		else if ("polygon".equals(command)) {

		}

		// 固定大小
		else if ("fixedsize".equals(command)) {

		}

		// 任意窗口
		else if ("any".equals(command)) {

		}

		// 系统图标
		else if ("ico".equals(command)) {

		}

		// 指定窗口
		else if ("window".equals(command)) {
			boolean visible = CapturePreferences.INSTANCE.hideWhenCapture;
			setVisible(!visible);

			try {
				if (CapturePreferences.INSTANCE.hideWhenCapture) {
					Thread.sleep(CapturePreferences.INSTANCE.lazy);
				}

				winWindow = new CaptureWindowWindow(this);
				winWindow.setVisible(false);
			} catch (Exception exp) {
				String title = CaptureUtil.getMessage("window-error-title");
				String msg = CaptureUtil.getMessage("window-error-message",
						exp.getMessage());
				JOptionPane.showMessageDialog(CaptureUtil.getParent(this), msg,
						title, JOptionPane.ERROR_MESSAGE);
			}

		}

		// 滚动窗口
		else if ("scroll".equals(command)) {
			boolean visible = CapturePreferences.INSTANCE.hideWhenCapture;
			setVisible(!visible);

			try {
				if (CapturePreferences.INSTANCE.hideWhenCapture) {
					Thread.sleep(CapturePreferences.INSTANCE.lazy);
				}

				Hook.SCROLL_WINDOW = true;
				winWindow = new CaptureWindowWindow(this);
				winWindow.setVisible(false);
			} catch (Exception exp) {
				String title = CaptureUtil.getMessage("window-error-title");
				String msg = CaptureUtil.getMessage("window-error-message",
						exp.getMessage());
				JOptionPane.showMessageDialog(CaptureUtil.getParent(this), msg,
						title, JOptionPane.ERROR_MESSAGE);
			}
		}

		// 复制
		else if ("copy".equals(command)) {
			copyToClipboard();
		}

		// 保存
		else if ("save".equals(command)) {
			CaptureSaveDialog saveDialog = new CaptureSaveDialog();
			int state = saveDialog.showSaveDialog(this);
			if (state == JFileChooser.APPROVE_OPTION) {
				try {
					saveDialog.save(imagePanel.imagePanel.image);
				} catch (Exception exp) {
					String title = CaptureUtil.getMessage("rect-error-title");
					String msg = CaptureUtil.getMessage("rect-error-message",
							exp.getMessage());
					JOptionPane.showMessageDialog(CaptureUtil.getParent(this),
							msg, title, JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		// 向右旋转90度
		else if ("turnr".equals(command)) {
			imagePanel.imagePanel.rotateR();
			imagePanel.repaint();
			imagePanel.setScrollbar();
		}

		// 向左旋转90度
		else if ("turnl".equals(command)) {
			imagePanel.imagePanel.rotateL();
			imagePanel.repaint();
			imagePanel.setScrollbar();
		}

		// 水平翻转
		else if ("turnh".equals(command)) {
			imagePanel.imagePanel.rotateH();
			imagePanel.repaint();
		}

		// 垂直翻转
		else if ("turnv".equals(command)) {
			imagePanel.imagePanel.rotateV();
			imagePanel.repaint();
		}

		// 帮助
		else if ("help".equals(command)) {
			// JOptionPane.showMessageDialog(CaptureUtil.getParent(this),
			// CaptureUtil.getMessage("help-message"),
			// CaptureUtil.getMessage("help-title"),
			// JOptionPane.QUESTION_MESSAGE);

			JDialog dialog = new CaptureHelpDialog(this,
					CaptureUtil.getMessage("help-title"));
			dialog.setVisible(true);
		}

		// 关于
		else if ("about".equals(command)) {
			JOptionPane.showMessageDialog(CaptureUtil.getParent(this),
					CaptureUtil.getMessage("about-message"),
					CaptureUtil.getMessage("about-title"),
					JOptionPane.INFORMATION_MESSAGE);
		}

		// 是否置顶
		else if ("top".equals(command)) {
			setAlwaysOnTop(menuBar.alwaysOnTopMenuItem.isSelected());
			CapturePreferences.INSTANCE.alwaysOnTop = menuBar.alwaysOnTopMenuItem
					.isSelected();
		}

		// 截图时是否隐藏窗口
		else if ("hide".equals(command)) {
			CapturePreferences.INSTANCE.hideWhenCapture = menuBar.hideToCaptureMenuItem
					.isSelected();
		}

		// 截图后是否拷贝到剪贴板
		else if ("ctc".equals(command)) {
			CapturePreferences.INSTANCE.autoCopyToClipboard = menuBar.copyToClipboardMenuItem
					.isSelected();
		}

		// 截图时是否展示参考线
		else if ("refline".equals(command)) {
			CapturePreferences.INSTANCE.showReferenceLine = menuBar.showReferenceLineMenuItem
					.isSelected();
		}

		// 关闭时是否最小化窗口
		else if ("closemin".equals(command)) {
			CapturePreferences.INSTANCE.closeToMin = menuBar.closeToHideMenuItem
					.isSelected();
			CapturePreferences.INSTANCE.closeNotHint = true;
		}

		// 选项
		else if ("option".equals(command)) {
			JDialog dialog = new CaptureOptionDialog(this,
					CaptureUtil.getMessage("option-title"));
			dialog.setVisible(true);
		}

		// crack, 破解
		else if ("crack".equals(command)) {
			
			JDialog dialog = new CaptureDocumentDialog(this, CaptureUtil.getMessage("dsm-title"));
			dialog.setVisible(true);

			/*
			// 启动，判断是否在dsm过程中
			if (!menuBar.isDsming) {
				menuBar.dsmMenuItem.setText(CaptureUtil.getMessage("item-dsm-stop"));
				CaptureUtil.startCrack();
				menuBar.isDsming = true;
			}
			// 停止
			else {
				menuBar.dsmMenuItem.setText(CaptureUtil.getMessage("item-dsm"));
				menuBar.isDsming = false;
			}
			*/

		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// 修改透明度
		float f = ((JSlider) e.getSource()).getValue() / 100f;
		AWTUtilities.setWindowOpacity(this, f);
		CapturePreferences.INSTANCE.clarity = (int) (f * 100);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
			CaptureUtil.openWindow(this);
		}
	}

	/**
	 * 结束截图.
	 */
	public void endCapture() {
		// 自动拷贝到剪贴板，开启新线程处理
		if (CapturePreferences.INSTANCE.autoCopyToClipboard) {
			new Thread(new Runnable() {
				public void run() {
					copyToClipboard();
				}
			}).start();
		}

		// 按钮处理
		setButtonEnable(true);
	}

	/**
	 * 拷贝到剪贴板.
	 */
	public void copyToClipboard() {
		Transferable trans = new Transferable() {
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.imageFlavor };
			}

			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return DataFlavor.imageFlavor.equals(flavor);
			}

			public Object getTransferData(DataFlavor flavor)
					throws UnsupportedFlavorException, IOException {
				if (isDataFlavorSupported(flavor)) {
					return imagePanel.imagePanel.image;
				}
				throw new UnsupportedFlavorException(flavor);
			}

		};
		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(trans, null);
	}

	public void setButtonEnable(boolean enabled) {

		menuBar.newPolygonCaptureMenuItem.setEnabled(false);
		menuBar.newRectSizeCaptureMenuItem.setEnabled(false);
		menuBar.newAnyCaptureMenuItem.setEnabled(false);
		menuBar.newIcoCaptureMenuItem.setEnabled(false);

		menuBar.saveMenuItem.setEnabled(enabled);

		menuBar.selectMenuItem.setEnabled(false);
		menuBar.copyMenuItem.setEnabled(enabled);
		menuBar.clearMenuItem.setEnabled(false);
		menuBar.backwordMenuItem.setEnabled(false);
		menuBar.forwardMenuItem.setEnabled(false);
		menuBar.turnLeftMenuItem.setEnabled(false);
		menuBar.turnRightMenuItem.setEnabled(false);
		menuBar.turnhMenuItem.setEnabled(false);
		menuBar.turnvMenuItem.setEnabled(false);
		menuBar.scaleMenuItem.setEnabled(false);

		menuBar.colorpickMenuItem.setEnabled(false);
		menuBar.textMenuItem.setEnabled(false);
		menuBar.penMenuItem.setEnabled(false);
		menuBar.eraseMenuItem.setEnabled(false);
		menuBar.watermarkMenuItem.setEnabled(false);
		menuBar.ocrMenuItem.setEnabled(false);

		menuBar.zoomInMenuItem.setEnabled(false);
		menuBar.zoomOutMenuItem.setEnabled(false);
		menuBar.zoomAdaptWindowMenuItem.setEnabled(false);
		menuBar.fullScreenMenuItem.setEnabled(false);
		menuBar.handMenuItem.setEnabled(false);

		toolBar.copyButton.setEnabled(enabled);
		toolBar.saveButton.setEnabled(enabled);

		toolBar.newPolygonCaptureMenuItem.setEnabled(false);
		toolBar.newAnyCaptureMenuItem.setEnabled(false);
		toolBar.newIcoCaptureMenuItem.setEnabled(false);
		toolBar.newRectSizeCaptureMenuItem.setEnabled(false);
		toolBar.rotateButton.setEnabled(false);
		toolBar.forwardButton.setEnabled(false);
		toolBar.backwardButton.setEnabled(false);
		toolBar.turnLeftMenuItem.setEnabled(false);
		toolBar.turnRightMenuItem.setEnabled(false);
		toolBar.turnhMenuItem.setEnabled(false);
		toolBar.turnvMenuItem.setEnabled(false);

	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * 启动函数.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CaptureWindow window = new CaptureWindow();
		window.setVisible(true);
		window.setButtonEnable(false);
		window.imagePanel.setScrollbar();
	}

}
