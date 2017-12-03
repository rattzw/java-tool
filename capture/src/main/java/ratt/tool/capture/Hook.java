package ratt.tool.capture;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import ratt.support.jna.platform.win32.Gdi32Ext;
import ratt.support.jna.platform.win32.User32Ext;
import ratt.support.jna.platform.win32.User32Ext.Point;
import ratt.support.jna.platform.win32.User32Ext.SCROLLINFO;
import ratt.support.jna.platform.win32.Win32Util;
import ratt.support.jna.platform.win32.hook.HookStructure.MSLLHOOKSTRUCT;
import ratt.support.jna.platform.win32.hook.proc.LowLevelMouseProc;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinDef.UINT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.sun.jna.ptr.IntByReference;

/**
 * ����.
 * 
 * @author Ratt
 */
public class Hook {

	static HHOOK mouseHHK; // Windows��깳��
	static HHOOK keyboardHHK; // Windows���̹���
	static CaptureWindow window; // ��ͼ����
	static boolean CTRL_DOWN; // �Ƿ���CTRL�������ƴ󴰿ڲ���׽
	static boolean ALT_DOWN; // �Ƿ���ALT�������Ʋ�׽�������λ�õ���С����
	static Point point; // ͣ�����ٺ������ϲ��϶�����Ҫ��ͼ�Ĵ���
	static long waitTime; // ͣ��ʱ��
	static boolean painting; // ��ǰ���ڽ�ͼ��

	static boolean SCROLL_WINDOW; // ��������

	public static void main(String[] args) {

		// ��ͼ����
		window = new CaptureWindow();
		window.setVisible(true);
		window.setButtonEnable(false);
		window.imagePanel.setScrollbar();

		// ��깳��
		LowLevelMouseProc mouseHook = new LowLevelMouseProc() {

			@Override
			public LRESULT callback(int nCode, WPARAM wParam,
					MSLLHOOKSTRUCT lParam) {
				if (window != null && window.winWindow != null
						&& !window.winWindow.panel.inWindow && !painting) {
					switch (wParam.intValue()) {
					case User32Ext.WM_MOUSEMOVE:
						if (point != null) {
							point.clear();
						}
						point = new Point(lParam.pt.x, lParam.pt.y);
						waitTime = System.currentTimeMillis();
						break;
					}
				} else {

				}
				return User32.INSTANCE.CallNextHookEx(mouseHHK, nCode, wParam,
						lParam.getPointer());
			}

		};

		// ע�ṳ��
		HMODULE hModMouse = Kernel32.INSTANCE.GetModuleHandle(null);
		mouseHHK = User32.INSTANCE.SetWindowsHookEx(WinUser.WH_MOUSE_LL,
				mouseHook, hModMouse, 0);

		LowLevelKeyboardProc keyboardHook = new LowLevelKeyboardProc() {
			@Override
			public LRESULT callback(int nCode, WPARAM wParam,
					KBDLLHOOKSTRUCT lParam) {
				if (window != null && window.winWindow != null) {
					int w = wParam.intValue();
					// ����alt��ʱw=WM_SYSKEYDOWN; ���������󲿷ּ�ʱw=WM_KEYDOWN
					if (w == WinUser.WM_KEYDOWN || w == WinUser.WM_SYSKEYDOWN) {
						// ESC
						if (lParam.vkCode == 27) {
							if (!SCROLL_WINDOW) {
								if (!window.winWindow.isVisible()) {
									window.winWindow.quit();
									if (point != null) {
										point.clear();
										point = null;
									}
									waitTime = -1;
								}
							} else {
								// SCROLL_WINDOW = false;
								// window.winWindow.quit();
								// if (point != null) {
								// point.clear();
								// point = null;
								// }
								// waitTime = -1;
							}
						}
						// CTRL
						else if (lParam.vkCode == 17
								|| Win32Util.getKeyString(lParam).toLowerCase()
										.contains("ctrl")) {
							CTRL_DOWN = true;
						}
						// ALT
						else if (lParam.vkCode == 18
								|| Win32Util.getKeyString(lParam).toLowerCase()
										.contains("alt")) {
							ALT_DOWN = true;
						}

					} else if (w == WinUser.WM_KEYUP
							|| w == WinUser.WM_SYSKEYUP) {
						// CTRL
						if (lParam.vkCode == 17
								|| Win32Util.getKeyString(lParam).toLowerCase()
										.contains("ctrl")) {
							CTRL_DOWN = false;
						}
						// ALT
						else if (lParam.vkCode == 18
								|| Win32Util.getKeyString(lParam).toLowerCase()
										.contains("alt")) {
							ALT_DOWN = false;
						}
					}
				}
				return User32.INSTANCE.CallNextHookEx(keyboardHHK, nCode,
						wParam, lParam.getPointer());
			}
		};

		// ���̹��ӵľ��
		final HMODULE hModKey = Kernel32.INSTANCE.GetModuleHandle(null);
		keyboardHHK = User32.INSTANCE.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL,
				keyboardHook, hModKey, 0);

		// ��������ʱ����߳�
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					long now = System.currentTimeMillis();

					// ͣ��ʱ���ж�
					if (point != null
							&& waitTime != -1
							&& now - waitTime >= CapturePreferences.INSTANCE.windowLazy) {
						painting = true;

						HWND hWnd = User32Ext.INSTANCE.WindowFromPoint(point);
						if (hWnd != null) {

							// ��ȡ�õ����С����
							if (ALT_DOWN) {
								hWnd = Win32Util.getChildWindow(hWnd, point,
										true);
							}

							// ��ȡ���ڴ�С
							RECT rect = new RECT();
							User32Ext.INSTANCE.GetWindowRect(hWnd, rect);

							RECT cRect = new RECT();
							User32Ext.INSTANCE.GetClientRect(hWnd, cRect);

							// ���ھ����С
							int height = rect.bottom - rect.top;
							int width = rect.right - rect.left;

							// ʵ�ʴ��ڴ�С������������
							int cHeight = cRect.bottom - cRect.top;
							int cWidth = cRect.right - cRect.left;

							// �жϴ��ڴ�С
							if (CTRL_DOWN
									&& (height > CaptureUtil.getScreenHeight()
											- CapturePreferences.INSTANCE.ctrlSize && width > CaptureUtil
											.getScreenWidth()
											- CapturePreferences.INSTANCE.ctrlSize)) {
							} else {
								// �жϾ��������Ƿ���ӣ��������򴰿��ö�
								// �жϾ����Ƿ���ӣ������г���ǰ���Ӵ��ڲ���һ�ж�
								// boolean result = User32Ext.INSTANCE
								// .InvalidateRect(hWnd, rect, true);
								// if (result) {
								// System.out.println("result="+result);
								// User32Ext.INSTANCE.SetForegroundWindow(Win32Util
								// .getParentWindow(hWnd, true));
								// }
								// User32.INSTANCE.SetWindowPos(hWnd, arg1,
								// rect.left, rect.top, width, height, arg6);
								//
								// try {
								// Thread.sleep(50);
								// } catch (InterruptedException e) {
								// }

								IntByReference min = new IntByReference();
								IntByReference max = new IntByReference();
								User32Ext.INSTANCE.GetScrollRange(hWnd,
										User32Ext.SB_VERT, min, max);

								// DSM���⴦��
								// HWND pWnd = Win32Util.getParentWindow(hWnd,
								// true);
								// String title =
								// Win32Util.getWindowTitle(pWnd);
								// if (title.indexOf("(DSM-") != -1) {
								// // ���ʹ�����Ϣ
								// // User32Ext.INSTANCE.SendMessage(pWnd, 1,
								// // (byte) 9, (byte) 0);
								// Win32Util.enableWindow(pWnd);
								// Win32Util.enableMenu(pWnd);
								// }

								// �������ڲ��ҿɹ���
								if (SCROLL_WINDOW && max.getValue() > 0) {
									window.winWindow.panel.inWindow = true;

									HDC hdc = null;
									try {
										hdc = User32Ext.INSTANCE
												.GetDC(User32Ext.INSTANCE
														.GetDesktopWindow());

										// ���ƾ���
										// Gdi32Ext.INSTANCE.SetBkColor(hdc,
										// new RGB((byte) 0, (byte) 0,
										// (byte) 0));
										Gdi32Ext.INSTANCE
												.Rectangle(hdc, rect.left - 1,
														rect.top - 1,
														rect.right + 1,
														rect.bottom + 1);

										// ���͹�����Ϣ���ص�left+top
										User32Ext.INSTANCE.SendMessage(hWnd,
												new UINT(User32Ext.WM_VSCROLL),
												new WPARAM(User32Ext.SB_TOP), // SB_THUMBPOSITION��Ч
												new LPARAM(0));
										User32Ext.INSTANCE.SendMessage(hWnd,
												new UINT(User32Ext.WM_HSCROLL),
												new WPARAM(User32Ext.SB_LEFT), // SB_THUMBPOSITION��Ч
												new LPARAM(0));

										// ��ҳ�������ж�λ���Ƿ���ͬ����ͬ��˵��ĩβ
										BufferedImage image = null;
										int currentPos = 0, beforePos = -1;
										SCROLLINFO info = new SCROLLINFO();
										info.fMask = new UINT(User32Ext.SIF_ALL);
										User32Ext.INSTANCE.GetScrollInfo(hWnd,
												User32Ext.SB_VERT, info);

										while (currentPos != beforePos) {

											// ��ͼ����ƴ��ͼƬ
											BufferedImage nowImage = null;
											try {
												Thread.sleep(200);
												Robot robot = new Robot();

												nowImage = robot
														.createScreenCapture(new Rectangle(
																rect.left
																		+ cRect.left,
																rect.top
																		+ cRect.top,
																cWidth, cHeight));

											} catch (Exception e) {
											}

											User32Ext.INSTANCE
													.SendMessage(
															hWnd,
															new UINT(
																	User32Ext.WM_VSCROLL),
															new WPARAM(
																	User32Ext.SB_PAGEDOWN),
															new LPARAM(0));

											int previousPos = beforePos;
											beforePos = currentPos;
											currentPos = User32Ext.INSTANCE
													.GetScrollPos(hWnd,
															User32Ext.SB_VERT);
											if (beforePos == currentPos) {
												// ȥ���ظ�
												image = CaptureUtil
														.mergeVImageExcept(
																image,
																nowImage,
																currentPos
																		- previousPos,
																info.nPage
																		.intValue(),
																cHeight);
											} else {
												image = CaptureUtil
														.mergeVImage(image,
																nowImage);
											}
										}

										window.winWindow.dispose();
										window.winWindow = null;
										window.imagePanel.imagePanel.image = image;
										window.endCapture();
										window.setVisible(true);
										window.repaint();
										SCROLL_WINDOW = false;

										// �ָ�ԭλ��
										// ָ��λ�õ���Ϣ������Ч
										// ��ȡ��ǰλ�á�line_up�߶ȡ�page_up�߶ȣ���ͨ��ѭ�����лָ�
									} catch (Exception e) {
										throw new IllegalStateException(e);
									} finally {
										if (hdc != null) {
											User32Ext.INSTANCE.ReleaseDC(hWnd,
													hdc);
										}
									}
								}
								// �ǹ������ڣ� ��ͼ�����Ʊ߿�
								else {
									try {
										Robot robot = new Robot();
										window.winWindow.panel.screen = robot
												.createScreenCapture(new Rectangle(
														CaptureUtil
																.getScreenSize()));
									} catch (Exception e) {
									}

									if (window.winWindow != null) {
										window.winWindow.panel.startX = rect.left;
										window.winWindow.panel.startY = rect.top;
										window.winWindow.panel.wWidth = width;
										window.winWindow.panel.wHeight = height;
										window.winWindow.panel.inWindow = true;
										window.winWindow.setVisible(true);
										window.winWindow.repaint();
									}
								}
							}

							rect.clear();
							cRect.clear();
						}

						point.clear();
						point = null;
						waitTime = -1;
						painting = false;
					}

					try {
						long time = 20 - (System.currentTimeMillis() - now);
						Thread.sleep(time > 0 ? time : 0);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();

		// ��Ϣѭ���� �����̣߳�ʵ����ѭ��һ�ζ���ִ��
		int result;
		MSG msg = new MSG();
		while ((result = User32.INSTANCE.GetMessage(msg, null, 0, 0)) != 0) {
			if (result == -1) {
				User32.INSTANCE.UnhookWindowsHookEx(mouseHHK);
				User32.INSTANCE.UnhookWindowsHookEx(keyboardHHK);
				break;
			} else {
				User32.INSTANCE.TranslateMessage(msg);
				User32.INSTANCE.DispatchMessage(msg);
			}
		}
	}

	public static void unhook() {
		try {
			User32.INSTANCE.UnhookWindowsHookEx(mouseHHK);
			User32.INSTANCE.UnhookWindowsHookEx(keyboardHHK);
		} catch (Exception e) {
		}
	}
}
