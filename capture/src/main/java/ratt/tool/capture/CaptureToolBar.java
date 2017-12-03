package ratt.tool.capture;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import com.jidesoft.swing.JideSplitButton;
import com.jidesoft.swing.JideToggleButton;
import com.jidesoft.swing.JideToggleSplitButton;

public class CaptureToolBar extends JToolBar {

	CaptureWindow window;

	JideSplitButton newCaptureButton;
	JMenuItem newScreeCaptureMenuItem;
	JMenuItem newRectCaptureMenuItem;
	JMenuItem newPolygonCaptureMenuItem;
	JMenuItem newWindowCaptureMenuItem;
	JMenuItem newScrollWindowCaptureMenuItem;
	JMenuItem newIcoCaptureMenuItem;
	JMenuItem newRectSizeCaptureMenuItem;
	JMenuItem newAnyCaptureMenuItem;

	JButton copyButton;
	JButton saveButton;
	JButton forwardButton;
	JButton backwardButton;

	JideSplitButton rotateButton;
	JMenuItem turnRightMenuItem;
	JMenuItem turnLeftMenuItem;
	JMenuItem turnhMenuItem;
	JMenuItem turnvMenuItem;

	JideToggleButton editTextButton;
	JPanel fontPanel;
	
	JideToggleSplitButton penButton;
	JPanel penPanel;
	
	JPanel colorPanel;

	public CaptureToolBar(CaptureWindow window) {
		super();
		this.window = window;

		init();
		listen();
	}

	/**
	 * ≥ı ºªØ.
	 */
	private void init() {

		String newCaptureText = CaptureUtil.getMessage("button-capture");
		newCaptureButton = new JideSplitButton(newCaptureText, new ImageIcon(
				CaptureUtil.newCaptureBigImage));
		newCaptureButton.setToolTipText(newCaptureText);
		newCaptureButton.setVerticalTextPosition(JButton.BOTTOM);
		newCaptureButton.setHorizontalTextPosition(JButton.CENTER);
		add(newCaptureButton);

		newScreeCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-screen"), new ImageIcon(
						CaptureUtil.newFullScreenCaptureImage));
		newScreeCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("full"));
		newCaptureButton.add(newScreeCaptureMenuItem);
		newRectCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-rect"), new ImageIcon(
						CaptureUtil.newRectCaptureImage));
		newRectCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("rect"));
		newCaptureButton.add(newRectCaptureMenuItem);
		newRectSizeCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-fixedsize"), new ImageIcon(
						CaptureUtil.fixedsizeCaptureImage));
		newRectSizeCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("fixedsize"));
		newCaptureButton.add(newRectSizeCaptureMenuItem);
		newPolygonCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-polygon"), new ImageIcon(
						CaptureUtil.newPolygonCaptureImage));
		newPolygonCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("polygon"));
		newCaptureButton.add(newPolygonCaptureMenuItem);
		newAnyCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-any"), new ImageIcon(
						CaptureUtil.anyCaptureImage));
		newAnyCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("any"));
		newCaptureButton.add(newAnyCaptureMenuItem);
		newWindowCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-window"), new ImageIcon(
						CaptureUtil.newWindowCaptureImage));
		newWindowCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("window"));
		newCaptureButton.add(newWindowCaptureMenuItem);
		newScrollWindowCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-scroll-window"),
				new ImageIcon(CaptureUtil.newScrollWindowCaptureImage));
		newScrollWindowCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("scroll"));
		newCaptureButton.add(newScrollWindowCaptureMenuItem);
		newIcoCaptureMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-new-ico"),
				new ImageIcon(CaptureUtil.icoCaptureImage));
		newIcoCaptureMenuItem.setAccelerator(CaptureUtil
				.getShortcutsKey("ico"));
		newCaptureButton.add(newIcoCaptureMenuItem);
		

		String copyText = CaptureUtil.getMessage("button-copy");
		copyButton = new JButton(copyText, new ImageIcon(
				CaptureUtil.copyBigImage));
		copyButton.setToolTipText(copyText);
		copyButton.setVerticalTextPosition(JButton.BOTTOM);
		copyButton.setHorizontalTextPosition(JButton.CENTER);
		// copyButton.setFocusPainted(false);
		add(copyButton);

		String saveText = CaptureUtil.getMessage("button-save");
		saveButton = new JButton(saveText, new ImageIcon(
				CaptureUtil.saveBigImage));
		saveButton.setToolTipText(saveText);
		saveButton.setVerticalTextPosition(JButton.BOTTOM);
		saveButton.setHorizontalTextPosition(JButton.CENTER);
		add(saveButton);

		String forwardText = CaptureUtil.getMessage("button-forward");
		forwardButton = new JButton(forwardText, new ImageIcon(
				CaptureUtil.forwardBigImage));
		forwardButton.setToolTipText(forwardText);
		forwardButton.setVerticalTextPosition(JButton.BOTTOM);
		forwardButton.setHorizontalTextPosition(JButton.CENTER);
		add(forwardButton);

		String backwardText = CaptureUtil.getMessage("button-backward");
		backwardButton = new JButton(backwardText, new ImageIcon(
				CaptureUtil.backwardBigImage));
		backwardButton.setToolTipText(backwardText);
		backwardButton.setVerticalTextPosition(JButton.BOTTOM);
		backwardButton.setHorizontalTextPosition(JButton.CENTER);
		add(backwardButton);

		String rotateText = CaptureUtil.getMessage("button-rotate");
		rotateButton = new JideSplitButton(rotateText, new ImageIcon(
				CaptureUtil.turnImage));
		rotateButton.setToolTipText(rotateText);
		rotateButton.setVerticalTextPosition(JButton.BOTTOM);
		rotateButton.setHorizontalTextPosition(JButton.CENTER);
		add(rotateButton);

		turnRightMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-turnright"), new ImageIcon(
						CaptureUtil.turnrImage));
		turnLeftMenuItem = new JMenuItem(
				CaptureUtil.getMessage("item-turnleft"), new ImageIcon(
						CaptureUtil.turnlImage));
		turnhMenuItem = new JMenuItem(CaptureUtil.getMessage("item-turnh"),
				new ImageIcon(CaptureUtil.turnhImage));
		turnvMenuItem = new JMenuItem(CaptureUtil.getMessage("item-turnv"),
				new ImageIcon(CaptureUtil.turnvImage));

		rotateButton.add(turnRightMenuItem);
		rotateButton.add(turnLeftMenuItem);
		rotateButton.add(turnhMenuItem);
		rotateButton.add(turnvMenuItem);

//		addSeparator();
//		
//		String editTextText = CaptureUtil.getMessage("button-edittext");
//		editTextButton = new JideToggleButton(editTextText, new ImageIcon(
//				CaptureUtil.editTextBigImage));
//		editTextButton.setToolTipText(editTextText);
//		editTextButton.setVerticalTextPosition(JButton.BOTTOM);
//		editTextButton.setHorizontalTextPosition(JButton.CENTER);
//		add(editTextButton);
//		
//		fontPanel = new JPanel();
//		fontPanel.setBorder(saveButton.getBorder());
//		LayoutManager layout = new GridLayout(2, 1);
//		fontPanel.setLayout(layout);
//		fontPanel.setPreferredSize(new Dimension(50,50));
//		fontPanel.setSize(new Dimension(50,50));
//		JTextField field = new JTextField(3);
//		field.setPreferredSize(new Dimension(50,50));
//		field.setMaximumSize(field.getPreferredSize());
//		JComboBox combobox = new JComboBox();
//		combobox.setPreferredSize(new Dimension(50,50));
//		fontPanel.add(field);
//		fontPanel.add(combobox);
//		add(fontPanel);
//		addSeparator();
//		
//		String penText = CaptureUtil.getMessage("button-pen");
//		penButton = new JideToggleSplitButton(penText, new ImageIcon(
//				CaptureUtil.penBigImage));
//		penButton.setToolTipText(penText);
//		penButton.setVerticalTextPosition(JButton.BOTTOM);
//		penButton.setHorizontalTextPosition(JButton.CENTER);
//		add(penButton);
//		
//		penPanel = new JPanel();
//		add(penPanel);
//		addSeparator();
//		
//		colorPanel = new JPanel();
//		add(colorPanel);
	}

	private void listen() {

		newCaptureButton.addActionListener(window);
		newCaptureButton.setActionCommand("rect");
		
		newScreeCaptureMenuItem.addActionListener(window);
		newScreeCaptureMenuItem.setActionCommand("fullscreen");
		
		newRectCaptureMenuItem.addActionListener(window);
		newRectCaptureMenuItem.setActionCommand("rect");
		
		newPolygonCaptureMenuItem.addActionListener(window);
		newPolygonCaptureMenuItem.setActionCommand("polygon");
		
		newWindowCaptureMenuItem.addActionListener(window);
		newWindowCaptureMenuItem.setActionCommand("window");
		
		newScrollWindowCaptureMenuItem.addActionListener(window);
		newScrollWindowCaptureMenuItem.setActionCommand("scroll");
		
		newRectSizeCaptureMenuItem.addActionListener(window);
		newRectSizeCaptureMenuItem.setActionCommand("fixedsize");
		
		newAnyCaptureMenuItem.addActionListener(window);
		newAnyCaptureMenuItem.setActionCommand("any");
		
		newIcoCaptureMenuItem.addActionListener(window);
		newIcoCaptureMenuItem.setActionCommand("ico");
		
		rotateButton.addActionListener(window);
		rotateButton.setActionCommand("turnr");
		
		turnRightMenuItem.addActionListener(window);
		turnRightMenuItem.setActionCommand("turnr");
		
		turnLeftMenuItem.addActionListener(window);
		turnLeftMenuItem.setActionCommand("turnl");
		
		turnhMenuItem.addActionListener(window);
		turnhMenuItem.setActionCommand("turnh");
		
		turnvMenuItem.addActionListener(window);
		turnvMenuItem.setActionCommand("turnv");
		
		copyButton.addActionListener(window);
		copyButton.setActionCommand("copy");
		
		saveButton.addActionListener(window);
		saveButton.setActionCommand("save");
	}
}
