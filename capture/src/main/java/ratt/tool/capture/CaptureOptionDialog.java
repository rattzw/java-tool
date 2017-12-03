package ratt.tool.capture;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class CaptureOptionDialog extends JDialog implements ActionListener,
		MouseListener {

	private CaptureWindow parent;
	private JTabbedPane tab;
	private JTabbedPane helpTab;
	private JTabbedPane updateTab;
	private JComboBox langCombobox;
	private JCheckBox alwaysOnTop;
	private JCheckBox closeToMin;
	private JCheckBox hideWindow;
	private JCheckBox autoCopy;
	private JCheckBox showReferenceLine;
	private JLabel refLineColor;
	private JLabel colorLabel;
	private JCheckBox showThumbnail;
	private JCheckBox thumbnailFllowMouse;

	private JTextField lazyText;
	private JTextField lazyWindowText;
	private JTextField dsmTimeText;
	private JButton okButton;
	private JButton cancelButton;

	public CaptureOptionDialog(CaptureWindow parent, String title) {
		super(parent, title, false);
		this.parent = parent;

		init();
		listen();
	}

	private void init() {
		double x = parent.getBounds().getX();
		double y = parent.getBounds().getY();
		double w = parent.getBounds().getWidth();
		double h = parent.getBounds().getHeight();

		int tw = 500;
		int th = 400;
		int tx = (int) x + (int) ((w - tw) / 2);
		int ty = (int) y + (int) ((h - th) / 2);

		// 添加主面板
		tab = new JTabbedPane();

		JPanel p1 = new JPanel();
		JScrollPane s1 = new JScrollPane();
		JViewport v1 = s1.getViewport();
		v1.add(p1);
		tab.addTab(CaptureUtil.getMessage("option-common-tab"), s1);

		// 添加通用配置
		p1.setLayout(null);

		JLabel langLabel = new JLabel(CaptureUtil.getMessage("lang-label"));
		langLabel.setBounds(20, 10, 60, 25);
		langLabel.setHorizontalAlignment(JLabel.RIGHT);
		p1.add(langLabel);

		langCombobox = new JComboBox();
		langCombobox.addItem(CaptureUtil.getMessage("lang-zh_CN"));
		langCombobox.setBounds(85, 10, 80, 25);
		p1.add(langCombobox);

		JPanel optionPanel = new JPanel();
		optionPanel.setBorder(new TitledBorder(CaptureUtil
				.getMessage("common-option-label")));
		optionPanel.setBounds(20, 50, 440, 220);
		optionPanel.setLayout(null);

		alwaysOnTop = new JCheckBox(CaptureUtil.getMessage("item-alwaysontop"),
				CapturePreferences.INSTANCE.alwaysOnTop);
		alwaysOnTop.setBounds(40, 20, 160, 20);
		closeToMin = new JCheckBox(CaptureUtil.getMessage("item-closetohide"),
				CapturePreferences.INSTANCE.closeToMin);
		closeToMin.setBounds(240, 20, 160, 20);
		hideWindow = new JCheckBox(
				CaptureUtil.getMessage("item-hidetocapture"),
				CapturePreferences.INSTANCE.hideWhenCapture);
		hideWindow.setBounds(40, 45, 160, 20);
		autoCopy = new JCheckBox(
				CaptureUtil.getMessage("item-copytoclipboard"),
				CapturePreferences.INSTANCE.autoCopyToClipboard);
		autoCopy.setBounds(240, 45, 160, 20);

		showReferenceLine = new JCheckBox(
				CaptureUtil.getMessage("item-showreferenceline"),
				CapturePreferences.INSTANCE.showReferenceLine);
		showReferenceLine.setBounds(40, 80, 160, 20);
		refLineColor = new JLabel(CaptureUtil.getMessage("item-refline-color"));
		refLineColor.setHorizontalAlignment(JLabel.RIGHT);
		refLineColor.setBounds(240, 80, 75, 20);
		colorLabel = new JLabel();
		colorLabel.setOpaque(true);
		colorLabel
				.setBackground(CapturePreferences.INSTANCE.referenceLineColor);
		colorLabel.setBounds(320, 80, 40, 20);
		colorLabel.setEnabled(CapturePreferences.INSTANCE.showReferenceLine);
		refLineColor.setEnabled(CapturePreferences.INSTANCE.showReferenceLine);

		showThumbnail = new JCheckBox(
				CaptureUtil.getMessage("item-show-thumbnail"),
				CapturePreferences.INSTANCE.showThumbnail);
		showThumbnail.setBounds(40, 105, 160, 20);
		thumbnailFllowMouse = new JCheckBox(
				CaptureUtil.getMessage("item-thumbnail-fllow-mouse"),
				CapturePreferences.INSTANCE.thumbnailFllowMouse);
		thumbnailFllowMouse.setBounds(240, 105, 160, 20);
		thumbnailFllowMouse
				.setEnabled(CapturePreferences.INSTANCE.showThumbnail);

		JLabel lazyTimeLabel = new JLabel(
				CaptureUtil.getMessage("item-lazy-label"));
		lazyTimeLabel.setBounds(40, 140, 172, 20);
		lazyTimeLabel.setHorizontalAlignment(JLabel.RIGHT);
		lazyText = new JTextField(
				String.valueOf(CapturePreferences.INSTANCE.lazy));
		lazyText.setBounds(220, 140, 40, 20);
		JLabel lazyWindowTimeLabel = new JLabel(
				CaptureUtil.getMessage("item-lazy-window-label"));
		lazyWindowTimeLabel.setBounds(40, 165, 172, 20);
		lazyWindowTimeLabel.setHorizontalAlignment(JLabel.RIGHT);
		lazyWindowText = new JTextField(
				String.valueOf(CapturePreferences.INSTANCE.windowLazy));
		lazyWindowText.setBounds(220, 165, 40, 20);
		JLabel dsmTimeLabel = new JLabel(
				CaptureUtil.getMessage("item-dsm-time-label"));
		dsmTimeLabel.setBounds(40, 190, 172, 20);
		dsmTimeLabel.setHorizontalAlignment(JLabel.RIGHT);
		dsmTimeText = new JTextField(
				String.valueOf(CapturePreferences.INSTANCE.dsmTime));
		dsmTimeText.setBounds(220, 190, 40, 20);

		optionPanel.add(alwaysOnTop);
		optionPanel.add(closeToMin);
		optionPanel.add(hideWindow);
		optionPanel.add(autoCopy);

		optionPanel.add(showReferenceLine);
		optionPanel.add(refLineColor);
		optionPanel.add(colorLabel);
		optionPanel.add(showThumbnail);
		optionPanel.add(thumbnailFllowMouse);

		optionPanel.add(lazyTimeLabel);
		optionPanel.add(lazyText);
		optionPanel.add(lazyWindowTimeLabel);
		optionPanel.add(lazyWindowText);
		optionPanel.add(dsmTimeLabel);
		optionPanel.add(dsmTimeText);

		p1.add(optionPanel);

		okButton = new JButton(CaptureUtil.getMessage("button-ok"));
		cancelButton = new JButton(CaptureUtil.getMessage("button-cancel"));
		okButton.setBounds(150, 280, 80, 25);
		cancelButton.setBounds(250, 280, 80, 25);

		p1.add(okButton);
		p1.add(cancelButton);

		// 添加快捷键配置
		String[] columnNames = new String[] {
				CaptureUtil.getMessage("shortcut-col1"),
				CaptureUtil.getMessage("shortcut-col2") };

		String[][] data = new String[CapturePreferences.INSTANCE.shortcutsKeyMap.size()][2];
		int count = 0;
		for (Entry<String, String> entry : CapturePreferences.INSTANCE.shortcutsKeyMap
				.entrySet()) {
			data[count][0] = CaptureUtil.getMessage("shortcut-" + entry.getKey());
			data[count++][1] = entry.getValue().toUpperCase();
		}

		JTable table = new JTable(data, columnNames);
		table.setEnabled(false);
		JScrollPane scrollpane = new JScrollPane(table);

		tab.addTab(CaptureUtil.getMessage("option-shortcut-tab"), scrollpane);

		// 设置大小
		add(tab);
		setBounds(tx, ty, tw, th);
	}

	private void listen() {
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);

		showReferenceLine.addActionListener(this);
		showThumbnail.addActionListener(this);

		colorLabel.addMouseListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// ok
		if (e.getSource() == okButton) {
			// 校验延迟是否合法
			try {
				CapturePreferences.INSTANCE.lazy = Integer.parseInt(lazyText
						.getText());
			} catch (Exception exp) {
				String title = CaptureUtil.getMessage("lazy-error-title");
				String msg = CaptureUtil.getMessage("lazy-error-message",
						lazyText.getText());
				JOptionPane.showMessageDialog(this, msg, title,
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				CapturePreferences.INSTANCE.windowLazy = Integer
						.parseInt(lazyWindowText.getText());
			} catch (Exception exp) {
				String title = CaptureUtil
						.getMessage("lazy-window-error-title");
				String msg = CaptureUtil.getMessage(
						"lazy-window-error-message", lazyWindowText.getText());
				JOptionPane.showMessageDialog(this, msg, title,
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			try {
				CapturePreferences.INSTANCE.dsmTime = Integer
						.parseInt(dsmTimeText.getText());
			} catch (Exception exp) {
				String title = CaptureUtil
						.getMessage("dsm-time-error-title");
				String msg = CaptureUtil.getMessage(
						"dsm-time-error-message", dsmTimeText.getText());
				JOptionPane.showMessageDialog(this, msg, title,
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			CapturePreferences.INSTANCE.alwaysOnTop = alwaysOnTop.isSelected();
			parent.menuBar.alwaysOnTopMenuItem.setSelected(alwaysOnTop
					.isSelected());
			parent.setAlwaysOnTop(alwaysOnTop.isSelected());

			CapturePreferences.INSTANCE.closeToMin = closeToMin.isSelected();
			CapturePreferences.INSTANCE.closeNotHint = true;
			parent.menuBar.closeToHideMenuItem.setSelected(closeToMin
					.isSelected());

			CapturePreferences.INSTANCE.hideWhenCapture = hideWindow
					.isSelected();
			parent.menuBar.hideToCaptureMenuItem.setSelected(hideWindow
					.isSelected());

			CapturePreferences.INSTANCE.autoCopyToClipboard = autoCopy
					.isSelected();
			parent.menuBar.copyToClipboardMenuItem.setSelected(autoCopy
					.isSelected());

			CapturePreferences.INSTANCE.showReferenceLine = showReferenceLine
					.isSelected();
			parent.menuBar.showReferenceLineMenuItem
					.setSelected(showReferenceLine.isSelected());
			if (CapturePreferences.INSTANCE.showReferenceLine) {
				CapturePreferences.INSTANCE.referenceLineColor = colorLabel
						.getBackground();
			}
			CapturePreferences.INSTANCE.showThumbnail = showThumbnail
					.isSelected();
			if (CapturePreferences.INSTANCE.showThumbnail) {
				CapturePreferences.INSTANCE.thumbnailFllowMouse = thumbnailFllowMouse
						.isSelected();
			}

			setVisible(false);
			dispose();
		}
		// cancel
		else if (e.getSource() == cancelButton) {
			setVisible(false);
			dispose();
		}
		// referenceline
		else if (e.getSource() == showReferenceLine) {
			colorLabel.setEnabled(showReferenceLine.isSelected());
			refLineColor.setEnabled(showReferenceLine.isSelected());
		}
		// showThumbnail
		else if (e.getSource() == showThumbnail) {
			thumbnailFllowMouse.setEnabled(showThumbnail.isSelected());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// colorpick
		if (e.getSource() == colorLabel && showReferenceLine.isSelected()) {
			final JColorChooser chooser = new JColorChooser(
					colorLabel.getBackground());
			final JDialog dialog = JColorChooser.createDialog(this,
					CaptureUtil.getMessage("title-select-reference-color"),
					true, chooser, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							colorLabel.setBackground(chooser.getColor());
						}
					}, null);
			dialog.setVisible(true);
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

}
