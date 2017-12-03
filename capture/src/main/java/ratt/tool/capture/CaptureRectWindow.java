package ratt.tool.capture;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class CaptureRectWindow extends JFrame {

	CaptureWindow parent;
	CaptureRectPanel panel;

	public CaptureRectWindow(CaptureWindow parent) throws Exception {
		this.parent = parent;

		init();
		listen();
	}

	private void init() throws Exception {
		Robot robot = new Robot();
		BufferedImage image = robot.createScreenCapture(new Rectangle(
				CaptureUtil.getScreenSize()));

		panel = new CaptureRectPanel(this, image, Color.BLACK, 0.3f);
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
					quit();
				}
			}
		});
	}
	
	public void quit(){
		parent.setVisible(true);
		setVisible(false);
		dispose();
	}
}
