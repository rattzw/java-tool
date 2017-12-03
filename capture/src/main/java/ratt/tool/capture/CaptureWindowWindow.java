package ratt.tool.capture;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;

/**
 * 展示指定窗口截图的主窗口, 使用JDialog可以不出现状态栏图标.
 * 
 * @author Ratt
 */
public class CaptureWindowWindow extends JDialog {

	CaptureWindow parent;
	CaptureWindowPanel panel;

	public CaptureWindowWindow(CaptureWindow parent) throws Exception {
		this.parent = parent;

		init();
		listen();
	}

	private void init() throws Exception {
		panel = new CaptureWindowPanel(this);
		getContentPane().add(panel);
		getContentPane().requestFocus();

		setSize(CaptureUtil.getScreenSize());
		setResizable(false);
		setUndecorated(true);
		setAlwaysOnTop(true);
	}

	private void listen() {
		addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					// 回到截图状态
					CaptureWindowWindow.this.setVisible(false);
					CaptureWindowWindow.this.panel.inWindow = false;
				}
			}
		});
	}

	public void quit() {
		parent.setVisible(true);
		setVisible(false);
		parent.winWindow = null;
		dispose();
	}
}
