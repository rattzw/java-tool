package ratt.tool.capture;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class CaptureImagePanel extends JPanel {

	public static final int NORMAL_MODE = 0;
	public static final int SELECT_MODE = 1;
	public static final int TEXT_MODE = 2;
	public static final int PEN_MODE = 3;
	public static final int SCALE_MODE = 4;
	public static final int HAND_MODE = 5;
	public static final int ZOOM_IN_MODE = 6;
	public static final int ZOOM_OUT_MODE = 7;

	CaptureWindow window;
	CaptureImageScrollPane pane;

	BufferedImage image; // 图像
	int imgStartX; // 图像开始绘制X轴
	int imgStartY; // 图像开始绘制Y轴

	int extendWidth = 300; // 扩展宽度

	int currentMode = NORMAL_MODE;

	public CaptureImagePanel(CaptureWindow window, CaptureImageScrollPane pane) {
		super();
		this.window = window;
		this.pane = pane;

		init();
		listen();
	}

	/**
	 * 初始化.
	 */
	private void init() {
		setPanelSize();
	}

	/**
	 * 监听.
	 */
	private void listen() {

		// Map<Attribute, Object> map = new HashMap<Attribute, Object>();
		// map.put(TextAttribute.FONT, c.getFont());//原字体
		// map.put(TextAttribute.STRIKETHROUGH,
		// TextAttribute.STRIKETHROUGH_ON);//增加的属性
		// c.setFont(Font.getFont(map));//设置新字体
	}

	/**
	 * 绘图.
	 */
	public void paintComponent(Graphics graphics) {
		// 设置窗口大小
		setPanelSize();

		// 清理原图像
		graphics.clearRect(0, 0, this.getWidth(), this.getHeight());

		// 画背景
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());

		// 画截图
		if (image != null) {
			graphics.drawImage(image, imgStartX, imgStartY,
					imgStartX + image.getWidth(),
					imgStartY + image.getHeight(), 0, 0, image.getWidth(),
					image.getHeight(), null);

			graphics.setColor(Color.BLACK);
			graphics.drawRect(imgStartX - 1, imgStartY - 1,
					image.getWidth() + 2, image.getHeight() + 2);
		}
	}

	/**
	 * 设置大小.
	 */
	private void setPanelSize() {
		// 原有根据图像的大小计算尺寸的方法，会导致ScrollBar无法正常滚动
		// double w = -1, h = -1;
		// if (image != null) {
		// w = image.getWidth();
		// h = image.getHeight();
		// } else {
		// Dimension size = window.getSize();
		// w = size.getWidth();
		// h = size.getHeight();
		// }
		//
		// Dimension size = new Dimension((int) w + extendWidth, (int) (h + h
		// * extendWidth / w));

		int width = CaptureUtil.getScreenWidth();
		int height = CaptureUtil.getScreenHeight();

		if (image != null) {
			width = width > image.getWidth() ? width : image.getWidth();
			height = height > image.getHeight() ? height : image.getHeight();

			// 图片补全
		}

		Dimension size = new Dimension(width + 300, height + 300);
		setPreferredSize(size);
		setSize(size);

		// Dimension size = getSize();
		if (image != null) {
			imgStartX = (int) (size.getWidth() - image.getWidth()) / 2;
			imgStartY = (int) (size.getHeight() - image.getHeight()) / 2;
		}
	}

	public void rotateR() {
		rotate(45);
	}

	public void rotateL() {
		rotate(-90);
	}

	private void rotate(double angle) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.PI * angle / 180, image.getWidth() / 2,
				image.getHeight() / 2);
		AffineTransformOp op = new AffineTransformOp(transform,
				AffineTransformOp.TYPE_BILINEAR);
		image = op.filter(image, null);
	}

	public void rotateH() {
		rotate(new AffineTransform(-1, 0, 0, 1, image.getWidth() - 1, 0)); // 水平翻转
	}

	public void rotateV() {
		rotate(new AffineTransform(1, 0, 0, -1, 0, image.getHeight() - 1)); // 垂直翻转
	}

	private void rotate(AffineTransform transform) {
		AffineTransformOp op = new AffineTransformOp(transform,
				AffineTransformOp.TYPE_BILINEAR);
		image = op.filter(image, null);
	}

	private void scale() {

	}
}
