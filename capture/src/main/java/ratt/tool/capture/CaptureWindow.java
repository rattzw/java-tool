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
 * ��ͼ������.
 * 
 * @author Ratt
 */
public class CaptureWindow extends JFrame implements ActionListener,
		ChangeListener, MouseListener {

	CaptureWindowWindow winWindow; // ָ������

	CaptureMenuBar menuBar; // �˵���
	CaptureToolBar toolBar; // ������
	CaptureImageScrollPane imagePanel; // ͼƬ�������
	CaptureTrayIcon trayIcon; // ϵͳ����

	/**
	 * ���캯��.
	 */
	public CaptureWindow() {
		super();

		init();
		listen();
	}

	/**
	 * ��ʼ��.
	 */
	private void init() {
		// ȫ������
		CaptureUtil.setDefaultUI();

		// ����
		getContentPane().setLayout(new BorderLayout());

		// �˵���
		menuBar = new CaptureMenuBar(this);
		setJMenuBar(menuBar);

		// ������
		toolBar = new CaptureToolBar(this);
		add(toolBar, BorderLayout.PAGE_START);

		// ͼƬ�������
		imagePanel = new CaptureImageScrollPane(this);
		add(imagePanel, BorderLayout.CENTER);

		// ϵͳ����
		trayIcon = new CaptureTrayIcon(this);
		try {
			SystemTray.getSystemTray().add(trayIcon);
		} catch (Exception e) {
		}

		// ��С��λ����ֵ
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

		// ���⡢ͼ������
		setTitle(CaptureUtil.getMessage("window-title"));
		setIconImage(CaptureUtil.titleImage);

		// �������ã���󻯡��ر�
		setAlwaysOnTop(CapturePreferences.INSTANCE.alwaysOnTop);
		AWTUtilities.setWindowOpacity(this,
				CapturePreferences.INSTANCE.clarity / 100f);
	}

	/**
	 * ����.
	 */
	private void listen() {
		// �رհ�ť
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

		// ��Ӧ��
		if ("open".equals(command)) {
			CaptureUtil.openWindow(this);
		}

		// �˳�Ӧ��
		else if ("exit".equals(command)) {
			Hook.unhook();
			CapturePreferences.save(this);
			dispose();
			System.exit(0);
		}

		// ȫ����ͼ
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

		// ��������
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

		// ���������
		else if ("polygon".equals(command)) {

		}

		// �̶���С
		else if ("fixedsize".equals(command)) {

		}

		// ���ⴰ��
		else if ("any".equals(command)) {

		}

		// ϵͳͼ��
		else if ("ico".equals(command)) {

		}

		// ָ������
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

		// ��������
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

		// ����
		else if ("copy".equals(command)) {
			copyToClipboard();
		}

		// ����
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

		// ������ת90��
		else if ("turnr".equals(command)) {
			imagePanel.imagePanel.rotateR();
			imagePanel.repaint();
			imagePanel.setScrollbar();
		}

		// ������ת90��
		else if ("turnl".equals(command)) {
			imagePanel.imagePanel.rotateL();
			imagePanel.repaint();
			imagePanel.setScrollbar();
		}

		// ˮƽ��ת
		else if ("turnh".equals(command)) {
			imagePanel.imagePanel.rotateH();
			imagePanel.repaint();
		}

		// ��ֱ��ת
		else if ("turnv".equals(command)) {
			imagePanel.imagePanel.rotateV();
			imagePanel.repaint();
		}

		// ����
		else if ("help".equals(command)) {
			// JOptionPane.showMessageDialog(CaptureUtil.getParent(this),
			// CaptureUtil.getMessage("help-message"),
			// CaptureUtil.getMessage("help-title"),
			// JOptionPane.QUESTION_MESSAGE);

			JDialog dialog = new CaptureHelpDialog(this,
					CaptureUtil.getMessage("help-title"));
			dialog.setVisible(true);
		}

		// ����
		else if ("about".equals(command)) {
			JOptionPane.showMessageDialog(CaptureUtil.getParent(this),
					CaptureUtil.getMessage("about-message"),
					CaptureUtil.getMessage("about-title"),
					JOptionPane.INFORMATION_MESSAGE);
		}

		// �Ƿ��ö�
		else if ("top".equals(command)) {
			setAlwaysOnTop(menuBar.alwaysOnTopMenuItem.isSelected());
			CapturePreferences.INSTANCE.alwaysOnTop = menuBar.alwaysOnTopMenuItem
					.isSelected();
		}

		// ��ͼʱ�Ƿ����ش���
		else if ("hide".equals(command)) {
			CapturePreferences.INSTANCE.hideWhenCapture = menuBar.hideToCaptureMenuItem
					.isSelected();
		}

		// ��ͼ���Ƿ񿽱���������
		else if ("ctc".equals(command)) {
			CapturePreferences.INSTANCE.autoCopyToClipboard = menuBar.copyToClipboardMenuItem
					.isSelected();
		}

		// ��ͼʱ�Ƿ�չʾ�ο���
		else if ("refline".equals(command)) {
			CapturePreferences.INSTANCE.showReferenceLine = menuBar.showReferenceLineMenuItem
					.isSelected();
		}

		// �ر�ʱ�Ƿ���С������
		else if ("closemin".equals(command)) {
			CapturePreferences.INSTANCE.closeToMin = menuBar.closeToHideMenuItem
					.isSelected();
			CapturePreferences.INSTANCE.closeNotHint = true;
		}

		// ѡ��
		else if ("option".equals(command)) {
			JDialog dialog = new CaptureOptionDialog(this,
					CaptureUtil.getMessage("option-title"));
			dialog.setVisible(true);
		}

		// crack, �ƽ�
		else if ("crack".equals(command)) {
			
			JDialog dialog = new CaptureDocumentDialog(this, CaptureUtil.getMessage("dsm-title"));
			dialog.setVisible(true);

			/*
			// �������ж��Ƿ���dsm������
			if (!menuBar.isDsming) {
				menuBar.dsmMenuItem.setText(CaptureUtil.getMessage("item-dsm-stop"));
				CaptureUtil.startCrack();
				menuBar.isDsming = true;
			}
			// ֹͣ
			else {
				menuBar.dsmMenuItem.setText(CaptureUtil.getMessage("item-dsm"));
				menuBar.isDsming = false;
			}
			*/

		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// �޸�͸����
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
	 * ������ͼ.
	 */
	public void endCapture() {
		// �Զ������������壬�������̴߳���
		if (CapturePreferences.INSTANCE.autoCopyToClipboard) {
			new Thread(new Runnable() {
				public void run() {
					copyToClipboard();
				}
			}).start();
		}

		// ��ť����
		setButtonEnable(true);
	}

	/**
	 * ������������.
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
	 * ��������.
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
