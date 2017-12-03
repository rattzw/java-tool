package ratt.tool.capture;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * 矩形截图.
 * 
 * @author Ratt
 */
public class CaptureRectPanel extends JPanel implements MouseListener,
		MouseMotionListener {

	// 截图状态
	public static final int CAPTURE_START = 0x00;
	public static final int CAPTURE_READY = 0x01;
	public static final int CAPTURE = 0x02;
	public static final int CAPTURE_END = 0x03;

	// 移动状态
	public static final int MOVE_READY = 0x10;
	public static final int MOVE = 0x11;

	// 拉伸状态
	public static final int SCALE_START = 0x20;
	public static final int SCALE_READY = 0x21;
	public static final int SCALE = 0x22;
	public static final int SCALE_END = 0x23;

	// 编辑状态
	public static final int EDIT_START = 0x30;
	public static final int EDIT_READY = 0x31;
	public static final int EDIT = 0x32;
	public static final int EDIT_END = 0x33;

	public static final int smallX = 160, smallY = 160, gap = 25,
			position = 50;

	CaptureRectWindow parent;
	BufferedImage screen;
	Color color;
	float mask;
	BufferedImage image;

	int state = CAPTURE_START;
	int startX, startY, nowX, nowY, endX, endY;
	int moveX, moveY, moveNowX, moveNowY;

	public CaptureRectPanel(CaptureRectWindow parent, BufferedImage image,
			Color color, float mask) {
		this.parent = parent;
		this.screen = image;
		this.color = color;
		this.mask = mask;

		init();
		listen();
	}

	public void paintComponent(Graphics g) {

		// 绘制背景
		super.paintComponent(g);
		g.drawImage(this.screen, 0, 0, null);

		// 绘制蒙版
		if (color != null && mask > 0) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					mask));
			g2.setColor(color);
			g2.fillRect(0, 0, screen.getWidth(), screen.getHeight());
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					1f));
		}

		// 绘制移动
		if (state == MOVE) {
			startX = startX + moveNowX - moveX;
			startY = startY + moveNowY - moveY;
			endX = endX + moveNowX - moveX;
			endY = endY + moveNowY - moveY;

			moveX = moveNowX;
			moveY = moveNowY;
		}

		// 绘制截图图像
		if (state == CAPTURE || state == CAPTURE_END || state == MOVE) {
			int mx = (state == CAPTURE_END || state == MOVE) ? endX : nowX;
			int my = (state == CAPTURE_END || state == MOVE) ? endY : nowY;

			// 反向截图特殊处理
			int w = Math.abs(mx - startX);
			int h = Math.abs(my - startY);
			int x = mx < startX ? mx : startX;
			int y = my < startY ? my : startY;
			
			// 移动时左上角特殊处理
			if (x < 0) {
				w = w + x;
				x = 0;
			}
			if (y < 0) {
				h = h + y;
				y = 0;
			}
			
			// 移动时右下角特殊处理
			if (x + w > CaptureUtil.getScreenWidth()) {
				w = CaptureUtil.getScreenWidth() - x;
			}
			if (y + h > CaptureUtil.getScreenHeight()) {
				h = CaptureUtil.getScreenHeight() - y;
			}

			// 大小为0时不绘制
			if (w != 0 && h != 0) {
				image = screen.getSubimage(x, y, w, h);
				g.drawImage(image, x, y, w, h, null);
			}
		}

		// 绘制图像大小
		if (state == CAPTURE || state == CAPTURE_END || state == MOVE) {
			int mx = (state == CAPTURE_END || state == MOVE) ? endX : nowX;
			int my = (state == CAPTURE_END || state == MOVE) ? endY : nowY;

			int x = mx < startX ? mx : startX;
			int y = my < startY ? my : startY;
			int w = Math.abs(mx - startX);
			int h = Math.abs(my - startY);

			g.setColor(Color.WHITE);
			g.drawString(
					CaptureUtil.getMessage("width-text") + w
							+ CaptureUtil.getMessage("height-text") + h, x + 2,
					y - 5);
		}

		// 绘制参考线
		if (CapturePreferences.INSTANCE.showReferenceLine) {
			if (state == CAPTURE_START || state == CAPTURE
					|| state == CAPTURE_END) {
				g.setColor(CapturePreferences.INSTANCE.referenceLineColor);
				g.drawLine(nowX, 0, nowX, CaptureUtil.getScreenHeight());
				g.drawLine(0, nowY, CaptureUtil.getScreenWidth(), nowY);
			}
		}

		// 绘制放大图、缩略图
		if (CapturePreferences.INSTANCE.showThumbnail) {
			if (state == CAPTURE_START || state == CAPTURE
					|| state == CAPTURE_END) {

				int x, y;
				// 鼠标跟随
				if (CapturePreferences.INSTANCE.thumbnailFllowMouse) {
					if (nowX < CaptureUtil.getScreenWidth() / 2) {
						x = nowX + gap;
					} else {
						x = nowX - gap - smallX;
					}
					if (nowY < CaptureUtil.getScreenHeight() / 2) {
						y = nowY + gap;
					} else {
						y = nowY - gap - smallY;
					}
				} 
				// 固定位置
				else {
					if (nowX < CaptureUtil.getScreenWidth() / 2) {
						x = CaptureUtil.getScreenWidth()
								/ CaptureUtil.getScreenHeight() * position;
					} else {
						x = CaptureUtil.getScreenWidth() - smallX
								- CaptureUtil.getScreenWidth()
								/ CaptureUtil.getScreenHeight() * position;
					}
					y = position;
				}

				// 绘制底色，用于边角特殊处理
				g.setColor(Color.WHITE);
				g.fillRect(x, y, smallX, smallY);
				// 绘制边框
				g.setColor(Color.GREEN);
				g.drawRect(x - 1, y - 1, smallX + 1, smallY + 1);

				int iX, iY, iW, iH, dX, dY;
				// 边角特殊处理
				if (nowX < smallX / 4) {
					iX = 0;
					iW = nowX + smallX / 4;
					dX = x + smallX - (iW * 2);
				} else if (nowX > CaptureUtil.getScreenWidth() - smallX / 4) {
					iX = nowX - smallX / 4;
					iW = smallX / 4 + CaptureUtil.getScreenWidth() - nowX;
					dX = x;
				} else {
					iX = nowX - smallX / 4;
					iW = smallX / 2;
					dX = x + smallX - (iW * 2);
				}
				if (nowY < smallY / 4) {
					iY = 0;
					iH = nowY + smallY / 4;
					dY = y + smallY - (iH * 2);
				} else if (nowY > CaptureUtil.getScreenHeight() - smallY / 4) {
					iY = nowY - smallY / 4;
					iH = smallY / 4 + CaptureUtil.getScreenHeight() - nowY;
					dY = y;
				} else {
					iY = nowY - smallY / 4;
					iH = smallY / 2;
					dY = y + smallY - (iH * 2);
				}

				BufferedImage smallImage = screen.getSubimage(iX, iY, iW, iH);
				g.drawImage(smallImage, dX, dY, iW * 2, iH * 2, null);

				g.setColor(Color.RED);
				g.drawLine(x + smallX / 2, y, x + smallX / 2, y + smallY);
				g.drawLine(x, y + smallY / 2, x + smallX, y + smallY / 2);
			}
		}
	}

	private void init() {
		setSize(screen.getWidth(), screen.getHeight());
		setOpaque(false);
		setLayout(null);
		this.requestFocus();
	}

	private void listen() {
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// 右键退出重新截图
		if (e.getButton() == MouseEvent.BUTTON3 && state == CAPTURE_END) {
			state = CAPTURE_START;

			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			repaint();
		}

		// 左键双击完成
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
			parent.parent.imagePanel.imagePanel.image = image;
			parent.parent.setVisible(true);
			parent.setVisible(false);
			parent.repaint();
			parent.dispose();
			parent.parent.endCapture();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

		// 左键
		if (e.getButton() == MouseEvent.BUTTON1) {

			// 开始截图
			if (state == CAPTURE_START) {
				state = CAPTURE_READY;

				startX = e.getX();
				startY = e.getY();

				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			// 开始移动
			else if (state == CAPTURE_END) {
				if (imageCanMove(nowX, nowY)) {
					moveX = e.getX();
					moveY = e.getY();

					state = MOVE_READY;
				} else {
					startX = e.getX();
					startY = e.getY();

					state = CAPTURE_READY;
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		// 左键
		if (e.getButton() == MouseEvent.BUTTON1) {

			// 放弃截图
			if (state == CAPTURE_READY) {
				state = CAPTURE_START;
			}

			// 截图完成
			else if (state == CAPTURE) {
				state = CAPTURE_END;

				endX = nowX;
				endY = nowY;
			}

			// 放弃移动
			else if (state == MOVE_READY) {
				state = CAPTURE_END;
			}

			// 移动完成
			else if (state == MOVE) {
				state = CAPTURE_END;

				nowX = e.getX();
				nowY = e.getY();

				repaint();
			}

		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

		// 开始截图
		if (state == CAPTURE_READY || state == CAPTURE) {
			state = CAPTURE;

			nowX = e.getX();
			nowY = e.getY();

			repaint();
		}

		// 开始移动
		else if (state == MOVE_READY || state == MOVE) {
			state = MOVE;

			moveNowX = e.getX();
			moveNowY = e.getY();

			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// 移动
		if (state == CAPTURE_START) {
			nowX = e.getX();
			nowY = e.getY();

			repaint();
		}

		// 截图结束
		else if (state == CAPTURE_END) {
			nowX = e.getX();
			nowY = e.getY();

			if (imageCanMove(nowX, nowY)) {
				setCursor(new Cursor(Cursor.MOVE_CURSOR));
			} else {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			repaint();
		}
	}

	private boolean imageCanMove(int nowX, int nowY) {
		int sx = endX > startX ? startX : endX;
		int ex = endX < startX ? startX : endX;

		int sy = endY > startY ? startY : endY;
		int ey = endY < startY ? startY : endY;

		if (nowX < ex && nowX > sx && nowY < ey && nowY > sy) {
			return true;
		} else {
			return false;
		}
	}

}
