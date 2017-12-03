package ratt.tool.capture;

import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;

public class CaptureImageScrollPane extends JScrollPane {

	CaptureWindow window;
	CaptureImagePanel imagePanel;

	public CaptureImageScrollPane(CaptureWindow window) {
		super();
		this.window = window;

		init();
	}

	public void init() {
		imagePanel = new CaptureImagePanel(window, this);

		setViewportView(imagePanel);
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	}

	public void setScrollbar() {
		JScrollBar hScrollBar = this.getHorizontalScrollBar();
		JScrollBar vScrollBar = this.getVerticalScrollBar();
		hScrollBar.setValue(0);
		vScrollBar.setValue(0);

		double windowWidth = hScrollBar.getSize().getWidth();
		double windowHeight = vScrollBar.getSize().getHeight();

		double panelWidth = imagePanel.getSize().getWidth();
		double panelHeight = imagePanel.getSize().getHeight();

		JViewport view = getViewport();
		view.scrollRectToVisible(new Rectangle(
				(int) (panelWidth - windowWidth) / 2,
				(int) (panelHeight - windowHeight) / 2, (int) windowWidth,
				(int) windowHeight));
	}
}
