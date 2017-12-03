package ratt.tool.capture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 文件选择.
 * 
 * @author Ratt
 */
public class CaptureSaveDialog extends JFileChooser {

	/**
	 * 序列号.
	 */
	private static final long serialVersionUID = -246414736727338043L;

	/**
	 * 构造函数.
	 */
	public CaptureSaveDialog() {
		super();
		init();
	}

	/**
	 * 初始化.
	 */
	private void init() {
		FileFilter currentFilter = new FileNameExtensionFilter("GIF文件(*.gif)",
				"gif");

		addChoosableFileFilter(currentFilter);
		addChoosableFileFilter(new FileNameExtensionFilter(
				"JPG文件(*.jpg|*.jpeg)", "jpg", "jpeg"));
		addChoosableFileFilter(new FileNameExtensionFilter("PNG文件(*.png)",
				"png"));
		addChoosableFileFilter(new FileNameExtensionFilter("BMP文件(*.bmp)",
				"bmp"));

		setFileFilter(currentFilter);
		setAcceptAllFileFilterUsed(false);
	}

	/**
	 * 保存文件.
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
