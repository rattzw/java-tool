package ratt.tool.capture;

import java.io.IOException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;

public class CaptureHelpDialog extends JDialog {

	private CaptureWindow parent;
	private JTabbedPane tab;
	private JTabbedPane helpTab;
	private JTabbedPane updateTab;

	public CaptureHelpDialog(CaptureWindow parent, String title) {
		super(parent, title, false);
		this.parent = parent;

		init();
		listen();
	}

	private void init() {
		tab = new JTabbedPane();

		helpTab = new JTabbedPane(JTabbedPane.LEFT);

		JEditorPane e1 = null;
		try {
			URL u1 = CaptureHelpDialog.class.getResource(CaptureUtil
					.getMessage("help-subtab-fullscreen-html"));
			e1 = new JEditorPane(u1);
			e1.setEditable(false);
		} catch (IOException e) {
		}
		JScrollPane s1 = new JScrollPane();
		JViewport v1 = s1.getViewport();
		v1.add(e1);
		helpTab.addTab(CaptureUtil.getMessage("help-subtab-fullscreen"), s1);

		JEditorPane e2 = null;
		try {
			URL u2 = CaptureHelpDialog.class.getResource(CaptureUtil
					.getMessage("help-subtab-rect-html"));
			e2 = new JEditorPane(u2);
			e2.setEditable(false);
		} catch (IOException e) {
		}
		JScrollPane s2 = new JScrollPane();
		JViewport v2 = s2.getViewport();
		v2.add(e2);
		helpTab.addTab(CaptureUtil.getMessage("help-subtab-rect"), s2);

		JEditorPane e3 = null;
		try {
			URL u3 = CaptureHelpDialog.class.getResource(CaptureUtil
					.getMessage("help-subtab-window-html"));
			e3 = new JEditorPane(u3);
			e3.setEditable(false);
		} catch (IOException e) {
		}
		JScrollPane s3 = new JScrollPane();
		JViewport v3 = s3.getViewport();
		v3.add(e3);
		helpTab.addTab(CaptureUtil.getMessage("help-subtab-window"), s3);

		JEditorPane e4 = null;
		try {
			URL u4 = CaptureHelpDialog.class.getResource(CaptureUtil
					.getMessage("help-subtab-scrollwindow-html"));
			e4 = new JEditorPane(u4);
			e4.setEditable(false);
		} catch (IOException e) {
		}
		JScrollPane s4 = new JScrollPane();
		JViewport v4 = s4.getViewport();
		v4.add(e4);
		helpTab.addTab(CaptureUtil.getMessage("help-subtab-scrollwindow"), s4);

		JEditorPane e5 = null;
		try {
			URL u5 = CaptureHelpDialog.class.getResource(CaptureUtil
					.getMessage("update-subtab-v1-html"));
			e5 = new JEditorPane(u5);
			e5.setEditable(false);
		} catch (IOException e) {
		}
		JScrollPane s5 = new JScrollPane();
		JViewport v5 = s5.getViewport();
		v5.add(e5);
		updateTab = new JTabbedPane(JTabbedPane.LEFT);
		updateTab.addTab(CaptureUtil.getMessage("update-subtab-v1"), s5);

		tab.addTab(CaptureUtil.getMessage("help-tab-help"), helpTab);
		tab.addTab(CaptureUtil.getMessage("help-tab-update"), updateTab);

		double x = parent.getBounds().getX();
		double y = parent.getBounds().getY();
		double w = parent.getBounds().getWidth();
		double h = parent.getBounds().getHeight();

		int tw = 750;
		int th = 500;
		int tx = (int) x + (int) ((w - tw) / 2);
		int ty = (int) y + (int) ((h - th) / 2);

		add(tab);
		setBounds(tx, ty, tw, th);
	}

	private void listen() {

	}
}
