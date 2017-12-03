package ratt.tool.capture;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import ratt.base.i18n.MessageManager;
import ratt.support.jna.platform.win32.User32Ext;
import ratt.support.jna.platform.win32.Win32Util;

import com.ctreber.aclib.image.ico.ICOFile;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HMENU;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.UINT;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;

/**
 * 工具类.
 * 
 * @author Ratt
 */
public class CaptureUtil {

	public static Image titleImage, trayImage, newCaptureImage,
			newCaptureBigImage, newFullScreenCaptureImage, newRectCaptureImage,
			newPolygonCaptureImage, newWindowCaptureImage,
			newScrollWindowCaptureImage, saveImage, saveBigImage, copyImage,
			selectImage, copyBigImage, clearImage, backwardImage,
			backwardBigImage, forwardImage, forwardBigImage, zoomInImage,
			zoomOutImage, adaptWindowImage, fullScreenImage, handImage,
			turnImage, turnrImage, turnlImage, turnhImage, turnvImage,
			scaleImage, helpImage, aboutImage, editTextImage, editTextBigImage,
			penImage, penBigImage, earseImage, pickImage, ocrImage,
			watermarkImage, icoCaptureImage, fixedsizeCaptureImage,
			anyCaptureImage, dsmImage;

	private static Dimension screenSize;

	/**
	 * 加载资源.
	 */
	static {
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		CapturePreferences.load();
		MessageManager.setLocale(CapturePreferences.INSTANCE.language);
		MessageManager.registerMessage("capture", "ratt.tool.capture");

		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		LookAndFeelFactory
				.installJideExtension(LookAndFeelFactory.EXTENSION_STYLE_VSNET_WITHOUT_MENU);

		try {
			ICOFile file = null;
			file = new ICOFile(CaptureUtil.class.getResource("icons/title.ico"));
			titleImage = (Image) file.getImages().get(0);
			trayImage = (Image) file.getImages().get(1);
			newCaptureImage = (Image) file.getImages().get(1);
			newCaptureBigImage = (Image) file.getImages().get(0);

			file = new ICOFile(
					CaptureUtil.class.getResource("icons/fullscreen.ico"));
			newFullScreenCaptureImage = (Image) file.getImages().get(5);

			file = new ICOFile(CaptureUtil.class.getResource("icons/rect.ico"));
			newRectCaptureImage = (Image) file.getImages().get(1);

			file = new ICOFile(
					CaptureUtil.class.getResource("icons/polygon.ico"));
			newPolygonCaptureImage = (Image) file.getImages().get(0);

			file = new ICOFile(
					CaptureUtil.class.getResource("icons/window.ico"));
			newWindowCaptureImage = (Image) file.getImages().get(1);

			file = new ICOFile(
					CaptureUtil.class.getResource("icons/scroll-window.ico"));
			newScrollWindowCaptureImage = (Image) file.getImages().get(1);

			file = new ICOFile(CaptureUtil.class.getResource("icons/save.ico"));
			saveImage = (Image) file.getImages().get(1);
			saveBigImage = (Image) file.getImages().get(0);

			file = new ICOFile(CaptureUtil.class.getResource("icons/copy.ico"));
			copyImage = (Image) file.getImages().get(1);
			copyBigImage = (Image) file.getImages().get(0);

			file = new ICOFile(
					CaptureUtil.class.getResource("icons/select.ico"));
			selectImage = (Image) file.getImages().get(1);

			file = new ICOFile(CaptureUtil.class.getResource("icons/clear.ico"));
			clearImage = (Image) file.getImages().get(1);

			file = new ICOFile(
					CaptureUtil.class.getResource("icons/backward.ico"));
			backwardImage = (Image) file.getImages().get(1);
			backwardBigImage = (Image) file.getImages().get(0);

			file = new ICOFile(
					CaptureUtil.class.getResource("icons/forward.ico"));
			forwardImage = (Image) file.getImages().get(1);
			forwardBigImage = (Image) file.getImages().get(0);

			file = new ICOFile(
					CaptureUtil.class.getResource("icons/zoom_in.ico"));
			zoomInImage = (Image) file.getImages().get(1);

			file = new ICOFile(
					CaptureUtil.class.getResource("icons/zoom_out.ico"));
			zoomOutImage = (Image) file.getImages().get(1);

			file = new ICOFile(
					CaptureUtil.class.getResource("icons/zoom_adapt.ico"));
			adaptWindowImage = (Image) file.getImages().get(1);

			file = new ICOFile(
					CaptureUtil.class.getResource("icons/zoom_screen.ico"));
			fullScreenImage = (Image) file.getImages().get(1);

			file = new ICOFile(CaptureUtil.class.getResource("icons/hand.ico"));
			handImage = (Image) file.getImages().get(0);

			file = new ICOFile(CaptureUtil.class.getResource("icons/turnr.ico"));
			turnrImage = (Image) file.getImages().get(1);
			turnImage = (Image) file.getImages().get(0);

			file = new ICOFile(CaptureUtil.class.getResource("icons/turnl.ico"));
			turnlImage = (Image) file.getImages().get(1);

			file = new ICOFile(CaptureUtil.class.getResource("icons/turnh.ico"));
			turnhImage = (Image) file.getImages().get(1);

			file = new ICOFile(CaptureUtil.class.getResource("icons/turnv.ico"));
			turnvImage = (Image) file.getImages().get(1);

			file = new ICOFile(CaptureUtil.class.getResource("icons/scale.ico"));
			scaleImage = (Image) file.getImages().get(1);

			file = new ICOFile(CaptureUtil.class.getResource("icons/help.ico"));
			helpImage = (Image) file.getImages().get(0);

			file = new ICOFile(CaptureUtil.class.getResource("icons/about.ico"));
			aboutImage = (Image) file.getImages().get(0);

			file = new ICOFile(
					CaptureUtil.class.getResource("icons/edittext.ico"));
			editTextImage = (Image) file.getImages().get(1);
			editTextBigImage = (Image) file.getImages().get(0);

			file = new ICOFile(CaptureUtil.class.getResource("icons/pen.ico"));
			penImage = (Image) file.getImages().get(1);
			penBigImage = (Image) file.getImages().get(0);

			file = new ICOFile(CaptureUtil.class.getResource("icons/earse.ico"));
			earseImage = (Image) file.getImages().get(1);

			file = new ICOFile(CaptureUtil.class.getResource("icons/pick.ico"));
			pickImage = (Image) file.getImages().get(1);

			file = new ICOFile(
					CaptureUtil.class.getResource("icons/watermark.ico"));
			watermarkImage = (Image) file.getImages().get(1);

			file = new ICOFile(CaptureUtil.class.getResource("icons/ocr.ico"));
			ocrImage = (Image) file.getImages().get(1);

			file = new ICOFile(CaptureUtil.class.getResource("icons/crack.ico"));
			dsmImage = (Image) file.getImages().get(1);

			file = new ICOFile(CaptureUtil.class.getResource("icons/ico.ico"));
			icoCaptureImage = (Image) file.getImages().get(0);

			file = new ICOFile(
					CaptureUtil.class.getResource("icons/fixedsize.ico"));
			fixedsizeCaptureImage = (Image) file.getImages().get(0);

			file = new ICOFile(CaptureUtil.class.getResource("icons/any.ico"));
			anyCaptureImage = (Image) file.getImages().get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String getMessage(String key, Object... params) {
		return MessageManager.getMessage(key, params);
	}

	public static KeyStroke getShortcutsKey(String name) {
		return CapturePreferences.INSTANCE.shortcutsKeyStrokeMap.get(name);
	}

	public static void setDefaultUI() {
		Font font = new Font("微软雅黑", Font.PLAIN, 12);

		Enumeration<Object> keys = UIManager.getDefaults().keys();
		Object key = null;
		Object value = null;
		while (keys.hasMoreElements()) {
			key = keys.nextElement();
			value = UIManager.get(key);
			if (key instanceof String) {
				/** 设置全局的背景色 */
				// if(((String) key).endsWith(".background")) {
				// UIManager.put(key, Color.white);
				// }
			}

			/** 设置全局的字体 */
			if (value instanceof Font) {
				UIManager.put(key, font);
			}
		}
	}

	public static Dimension getScreenSize() {
		return screenSize;
	}

	public static int getScreenWidth() {
		return screenSize.width;
	}

	public static int getScreenHeight() {
		return screenSize.height;
	}

	public static void openWindow(JFrame window) {
		window.setVisible(true);
		window.setExtendedState(JFrame.NORMAL); // 原以为会导致窗口回到启动尺寸，结果不会
		// window.getContentPane().requestFocus();
	}

	public static Component getParent(JFrame window) {
		if (!window.isVisible()
				|| window.getExtendedState() == JFrame.ICONIFIED) {
			return null;
		}

		return window;
	}

	public static BufferedImage mergeVImage(BufferedImage image1,
			BufferedImage image2) {

		if (image1 == null) {
			return image2;
		}

		if (image2 == null) {
			return image1;
		}

		BufferedImage result = new BufferedImage(image1.getWidth(),
				image1.getHeight() + image2.getHeight(), image1.getType());

		int[] array1 = new int[image1.getWidth() * image1.getHeight()];
		image1.getRGB(0, 0, image1.getWidth(), image1.getHeight(), array1, 0,
				image1.getWidth());
		result.setRGB(0, 0, image1.getWidth(), image1.getHeight(), array1, 0,
				image1.getWidth());

		int[] array2 = new int[image2.getWidth() * image2.getHeight()];
		image2.getRGB(0, 0, image2.getWidth(), image2.getHeight(), array2, 0,
				image2.getWidth());
		result.setRGB(0, image1.getHeight(), image2.getWidth(),
				image2.getHeight(), array2, 0, image2.getWidth());

		return result;
	}

	/**
	 * 
	 * @param image1
	 * @param image2
	 * @param scrollHeight
	 *            滚动条移动距离.
	 * @param rectHeight
	 *            滚动条一页的距离.
	 * @param screenHeight
	 *            一屏的大小.
	 * @return
	 */
	public static BufferedImage mergeVImageExcept(BufferedImage image1,
			BufferedImage image2, int scrollHeight, int rectHeight,
			int screenHeight) {

		if (image1 == null) {
			return image2;
		}
		if (image2 == null) {
			return image2;
		}

		int height = (int) (scrollHeight * screenHeight * 1.0 / (rectHeight - 1));
		int one = screenHeight / rectHeight;

		int[] array1 = new int[image1.getWidth()];
		image1.getRGB(0, image1.getHeight() - 1, image1.getWidth(), 1, array1,
				0, image1.getWidth());

		int[] array2 = new int[image2.getWidth()];
		for (int i = 0 - one; i < one; i++) {
			if (image2.getHeight() - height + i < 0 || i > height - 1) {
				continue;
			}
			image2.getRGB(0, image2.getHeight() - height + i,
					image2.getWidth(), 1, array2, 0, image2.getWidth());
			if (Arrays.equals(array1, array2)) {
				height = height - i;
				break;
			}
		}

		return mergeVImage(
				image1,
				image2.getSubimage(0, image2.getHeight() - height + 1,
						image2.getWidth(), height - 1));
	}

	public static BufferedImage mergeHImage(BufferedImage image1,
			BufferedImage image2) {

		if (image1 == null) {
			return image2;
		}

		if (image2 == null) {
			return image1;
		}

		BufferedImage result = new BufferedImage(image1.getWidth()
				+ image2.getWidth(), image1.getHeight(), image1.getType());

		int[] array1 = new int[image1.getWidth() * image1.getHeight()];
		image1.getRGB(0, 0, image1.getWidth(), image1.getHeight(), array1, 0,
				image1.getWidth());
		result.setRGB(0, 0, image1.getWidth(), image1.getHeight(), array1, 0,
				image1.getWidth());

		int[] array2 = new int[image2.getWidth() * image2.getHeight()];
		image2.getRGB(0, 0, image2.getWidth(), image2.getHeight(), array2, 0,
				image2.getWidth());
		result.setRGB(image1.getWidth(), 0, image2.getWidth(),
				image2.getHeight(), array2, 0, image2.getWidth());

		return result;
	}
}
