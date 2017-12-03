package ratt.tool.capture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class CaptureWindowPanel extends JPanel implements MouseListener,
		MouseMotionListener {

	CaptureWindowWindow parent;
	BufferedImage screen;

	int startX, startY, wWidth, wHeight;
	boolean inWindow;

	public CaptureWindowPanel(CaptureWindowWindow parent) {
		this.parent = parent;

		init();
		listen();
	}

	public void paintComponent(Graphics g) {

		// »æÖÆ±³¾°
		if (screen != null) {
			super.paintComponent(g);
			g.drawImage(screen, 0, 0, null);
		}

		// »æÖÆ´°¿Ú
		if (inWindow) {
			g.setColor(Color.RED);
			g.drawRect(startX, startY + 1, wWidth - 2, wHeight - 2);
			g.drawRect(startX + 1, startY + 2, wWidth - 4, wHeight - 4);
		}
	}

	private void init() {

	}

	private void listen() {
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2
				&& inWindow) {
			int x = startX;
			int y = startY;
			int width = wWidth;
			int height = wHeight;
			if (x < 0) {
				x = 0;
			}
			if (y < 0) {
				y = 0;
			}
			if (x + width > CaptureUtil.getScreenWidth()) {
				width = CaptureUtil.getScreenWidth() - x;
			}
			if (y + height > CaptureUtil.getScreenHeight()) {
				height = CaptureUtil.getScreenHeight() - y;
			}

			parent.parent.imagePanel.imagePanel.image = screen.getSubimage(x,
					y, width, height);

			parent.parent.setVisible(true);
			parent.parent.repaint();
			parent.parent.endCapture();

			parent.parent.winWindow = null;
			parent.setVisible(false);
			parent.dispose();
		}
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

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (x < startX || x > startX + wWidth || y < startY
				|| y > startY + wHeight) {
			parent.setVisible(false);
			inWindow = false;
		}
	}
}
