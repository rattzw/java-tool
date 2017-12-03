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
 * 钩子.
 * 
 * @author Ratt
 */
public class Hook {

	static HHOOK mouseHHK; // Windows鼠标钩子
	static HHOOK keyboardHHK; // Windows键盘钩子
	static CaptureWindow window; // 截图窗口
	static boolean CTRL_DOWN; // 是否按下CTRL键，控制大窗口不捕捉
	static boolean ALT_DOWN; // 是否按下ALT键，控制捕捉鼠标所在位置的最小窗口
	static Point point; // 停留三百毫秒以上才认定是需要截图的窗口
	static long waitTime; // 停留时间
	static boolean painting; // 当前正在截图中

	static boolean SCROLL_WINDOW; // 滚动窗口

	public static void main(String[] args) {

		// 截图窗口
		window = new CaptureWindow();
		window.setVisible(true);
		window.setButtonEnable(false);
		window.imagePanel.setScrollbar();

		// 鼠标钩子
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

		// 注册钩子
		HMODULE hModMouse = Kernel32.INSTANCE.GetModuleHandle(null);
		mouseHHK = User32.INSTANCE.SetWindowsHookEx(WinUser.WH_MOUSE_LL,
				mouseHook, hModMouse, 0);

		LowLevelKeyboardProc keyboardHook = new LowLevelKeyboardProc() {
			@Override
			public LRESULT callback(int nCode, WPARAM wParam,
					KBDLLHOOKSTRUCT lParam) {
				if (window != null && window.winWindow != null) {
					int w = wParam.intValue();
					// 按下alt键时w=WM_SYSKEYDOWN; 按下其他大部分键时w=WM_KEYDOWN
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

		// 键盘钩子的句柄
		final HMODULE hModKey = Kernel32.INSTANCE.GetModuleHandle(null);
		keyboardHHK = User32.INSTANCE.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL,
				keyboardHook, hModKey, 0);

		// 启动监听时间的线程
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					long now = System.currentTimeMillis();

					// 停留时间判断
					if (point != null
							&& waitTime != -1
							&& now - waitTime >= CapturePreferences.INSTANCE.windowLazy) {
						painting = true;

						HWND hWnd = User32Ext.INSTANCE.WindowFromPoint(point);
						if (hWnd != null) {

							// 获取该点的最小窗口
							if (ALT_DOWN) {
								hWnd = Win32Util.getChildWindow(hWnd, point,
										true);
							}

							// 获取窗口大小
							RECT rect = new RECT();
							User32Ext.INSTANCE.GetWindowRect(hWnd, rect);

							RECT cRect = new RECT();
							User32Ext.INSTANCE.GetClientRect(hWnd, cRect);

							// 窗口句柄大小
							int height = rect.bottom - rect.top;
							int width = rect.right - rect.left;

							// 实际窗口大小，不含滚动条
							int cHeight = cRect.bottom - cRect.top;
							int cWidth = cRect.right - cRect.left;

							// 判断窗口大小
							if (CTRL_DOWN
									&& (height > CaptureUtil.getScreenHeight()
											- CapturePreferences.INSTANCE.ctrlSize && width > CaptureUtil
											.getScreenWidth()
											- CapturePreferences.INSTANCE.ctrlSize)) {
							} else {
								// 判断矩形区域是否可视，不可视则窗口置顶
								// 判断矩形是否可视，可以列出当前可视窗口并逐一判断
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

								// DSM特殊处理
								// HWND pWnd = Win32Util.getParentWindow(hWnd,
								// true);
								// String title =
								// Win32Util.getWindowTitle(pWnd);
								// if (title.indexOf("(DSM-") != -1) {
								// // 发送窗口消息
								// // User32Ext.INSTANCE.SendMessage(pWnd, 1,
								// // (byte) 9, (byte) 0);
								// Win32Util.enableWindow(pWnd);
								// Win32Util.enableMenu(pWnd);
								// }

								// 滚动窗口并且可滚动
								if (SCROLL_WINDOW && max.getValue() > 0) {
									window.winWindow.panel.inWindow = true;

									HDC hdc = null;
									try {
										hdc = User32Ext.INSTANCE
												.GetDC(User32Ext.INSTANCE
														.GetDesktopWindow());

										// 绘制矩形
										// Gdi32Ext.INSTANCE.SetBkColor(hdc,
										// new RGB((byte) 0, (byte) 0,
										// (byte) 0));
										Gdi32Ext.INSTANCE
												.Rectangle(hdc, rect.left - 1,
														rect.top - 1,
														rect.right + 1,
														rect.bottom + 1);

										// 发送滚动消息，回到left+top
										User32Ext.INSTANCE.SendMessage(hWnd,
												new UINT(User32Ext.WM_VSCROLL),
												new WPARAM(User32Ext.SB_TOP), // SB_THUMBPOSITION无效
												new LPARAM(0));
										User32Ext.INSTANCE.SendMessage(hWnd,
												new UINT(User32Ext.WM_HSCROLL),
												new WPARAM(User32Ext.SB_LEFT), // SB_THUMBPOSITION无效
												new LPARAM(0));

										// 逐页滚动，判断位置是否相同，相同则说明末尾
										BufferedImage image = null;
										int currentPos = 0, beforePos = -1;
										SCROLLINFO info = new SCROLLINFO();
										info.fMask = new UINT(User32Ext.SIF_ALL);
										User32Ext.INSTANCE.GetScrollInfo(hWnd,
												User32Ext.SB_VERT, info);

										while (currentPos != beforePos) {

											// 截图，并拼接图片
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
												// 去除重复
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

										// 恢复原位置
										// 指定位置的消息发送无效
										// 获取当前位置、line_up高度、page_up高度，并通过循环进行恢复
									} catch (Exception e) {
										throw new IllegalStateException(e);
									} finally {
										if (hdc != null) {
											User32Ext.INSTANCE.ReleaseDC(hWnd,
													hdc);
										}
									}
								}
								// 非滚动窗口， 截图并绘制边框
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

		// 消息循环， 堵塞线程，实际上循环一次都不执行
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
