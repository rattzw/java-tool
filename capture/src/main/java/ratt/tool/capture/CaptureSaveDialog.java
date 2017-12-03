package ratt.tool.capture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * �ļ�ѡ��.
 * 
 * @author Ratt
 */
public class CaptureSaveDialog extends JFileChooser {

	/**
	 * ���к�.
	 */
	private static final long serialVersionUID = -246414736727338043L;

	/**
	 * ���캯��.
	 */
	public CaptureSaveDialog() {
		super();
		init();
	}

	/**
	 * ��ʼ��.
	 */
	private void init() {
		FileFilter currentFilter = new FileNameExtensionFilter("GIF�ļ�(*.gif)",
				"gif");

		addChoosableFileFilter(currentFilter);
		addChoosableFileFilter(new FileNameExtensionFilter(
				"JPG�ļ�(*.jpg|*.jpeg)", "jpg", "jpeg"));
		addChoosableFileFilter(new FileNameExtensionFilter("PNG�ļ�(*.png)",
				"png"));
		addChoosableFileFilter(new FileNameExtensionFilter("BMP�ļ�(*.bmp)",
				"bmp"));

		setFileFilter(currentFilter);
		setAcceptAllFileFilterUsed(false);
	}

	/**
	 * �����ļ�.
	 * 
	 * @param image
	 * @throws IOException
	 */
	public void save(BufferedImage image) throws IOException {
		File file = getSelectedFile();

		FileNameExtensionFilter filter = (FileNameExtensionFilter) getFileFilter();
		String suffix = filter.getExtensions()[0];
		if (!file.getName().endsWith("." + suffix)) {
			file = new File(file.getAbsolutePath() + "." + suffix);
		}
		ImageIO.write(image, filter.getExtensions()[0], file);
	}
}
