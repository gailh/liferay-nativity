/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.nativity.plugincontrol.mac;

import com.liferay.nativity.listeners.MenuItemListener;
import com.liferay.nativity.listeners.SocketCloseListener;
import com.liferay.nativity.plugincontrol.NativityPluginControl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.Socket;

import java.util.Map.Entry;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dennis Ju
 */
public abstract class AppleNativityPluginControlImpl
	extends NativityPluginControl {

	public void addMenuItemListener(MenuItemListener menuItemListener) {
		_menuItemListener = menuItemListener;
	}

	public void connect() {
		try {
			_serviceSocket = new Socket("127.0.0.1", _serviceSocketPort);

			_serviceBufferedReader = new BufferedReader(
				new InputStreamReader(_serviceSocket.getInputStream()));

			_serviceOutputStream = new DataOutputStream(
				_serviceSocket.getOutputStream());

			_callbackSocket = new Socket("127.0.0.1", _callbackSocketPort);

			_callbackBufferedReader = new BufferedReader(
				new InputStreamReader(_callbackSocket.getInputStream()));

			_callbackOutputStream = new DataOutputStream(
				_callbackSocket.getOutputStream());

			_callbackThread = new ReadThread(this);

			_callbackThread.start();
		}
		catch (IOException e) {
			return;
		}

		return;
	}

	@Override
	public void disableFileIcons() {
		String command = "enableOverlays:" + "0";

		_sendCommand(command);
	}

	public void disconnect() {
		try {
			_serviceSocket.close();
		}
		catch (IOException e) {
			return;
		}

		return;
	}

	@Override
	public void enableFileIcons() {
		String command = "enableOverlays:" + "1";

		_sendCommand(command);
	}

	@Override
	public boolean pluginRunning() {
		boolean running = false;

		try {
			String grepCommand =
				"ps -e | grep com.liferay.FinderPluginHelper | grep -v grep";

			String[] cmd = { "/bin/sh", "-c", grepCommand };

			Process process = Runtime.getRuntime().exec(cmd);

			BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));

			while (bufferedReader.readLine() != null) {
				running = true;
			}

			bufferedReader.close();
		}
		catch (Exception e) {
		}

		_logger.trace("Finder plugin helper running: {}", running);

		return running;
	}

	public int registerIcon(String path) {
		String command = "registerIcon:" + path;

		String reply = _sendCommand(command);

		return Integer.parseInt(reply);
	}

	public void removeFileIcon(String fileName) {
		String command = "removeFileIcon:" + fileName;

		_sendCommand(command);
	}

	public void removeFileIcons(String[] fileNames) {
		StringBuilder sb = new StringBuilder();

		sb.append("removeFileIcons");

		for (String fileName : fileNames) {
			sb.append(":");
			sb.append(fileName);
		}

		_sendCommand(sb.toString());
	}

	@Override
	public void setContextMenuTitle(String title) {
		String command = "setMenuTitle:" + title;

		_sendCommand(command);
	}

	public void setIconForFile(String fileName, int iconId) {
		String command = "setFileIcon:" + fileName + ":" + iconId;

		_sendCommand(command);
	}

	public void setIconsForFiles(Map<String, Integer> fileIconsMap) {
		StringBuilder sb = new StringBuilder();

		sb.append("setFileIcons");

		int i = 0;

		for (Entry<String, Integer> entry : fileIconsMap.entrySet()) {
			sb.append(":");
			sb.append(entry.getKey());
			sb.append(":");
			sb.append(entry.getValue());

			i++;

			if (i == _messageBufferSize) {
				_sendCommand(sb.toString());

				sb = new StringBuilder();

			sb.append("setFileIcons");

				i = 0;
			}
		}

		if (i > 0) {
			_sendCommand(sb.toString());
		}
	}

	public void setSocketCloseListener(
		SocketCloseListener socketCloseListener) {

		_socketCloseListener = socketCloseListener;
	}

	public boolean startPlugin(String path) throws Exception {
		_logger.trace("Starting Finder plugin helper");

		Process process = Runtime.getRuntime().exec(path);

		BufferedReader inputBufferedReader = new BufferedReader(
			new InputStreamReader(process.getInputStream()));

		BufferedReader errorBufferedReader = new BufferedReader(
			new InputStreamReader(process.getErrorStream()));

		String input = inputBufferedReader.readLine();

		while (input != null) {
			input = inputBufferedReader.readLine();
		}

		inputBufferedReader.close();

		String error = errorBufferedReader.readLine();

		if (error != null) {
			errorBufferedReader.close();

			_logger.trace(
				"Finder plugin helper failed to start. Error: {}", error);

			return false;
		}

		errorBufferedReader.close();

		process.waitFor();

		_logger.trace("Finder plugin helper successfully started");

		return true;
	}

	public void unregisterIcon(int id) {
		String command = "unregisterIcon:" + id;

		_sendCommand(command);
	}

	protected class ReadThread extends Thread {

		public ReadThread(AppleNativityPluginControlImpl pluginControl) {
			_pluginControl = pluginControl;
		}

		@Override
		public void run() {
			_pluginControl._doCallbackLoop();
		}

		private AppleNativityPluginControlImpl _pluginControl;

	}

	private void _doCallbackLoop() {
		while (_callbackSocket.isConnected()) {
			try {
				String data = _callbackBufferedReader.readLine();

				if (data == null) {
					_callbackSocket.close();

					_socketCloseListener.onSocketClose();

					break;
				}

				if (data.startsWith("menuQuery:") &&
					(_menuItemListener != null)) {

					String currentFiles = data.substring(10, data.length());

					_currentFiles = currentFiles.split(":");

//					String[] items = _menuItemListener.onPopulateMenuItems(
//						_currentFiles);

					String itemsStr = new String();

//					if (items != null) {
//						for (int i=0; i<items.length; ++i) {
//							if (i > 0) {
//								itemsStr += ":";
//							}
//
//							itemsStr += items[i];
//						}
//					}

					_callbackOutputStream.writeBytes(itemsStr + "\r\n");
				}

				if (data.startsWith("menuExec:") &&
					(_menuItemListener != null)) {

					int titleIndex = data.indexOf(":", 9);

					int menuIndex = Integer.parseInt(
						data.substring(9, titleIndex));

					String menuText = data.substring(titleIndex + 1);

//					_menuItemListener.onExecuteMenuItem(
//						menuIndex, menuText, _currentFiles);
				}
			}
			catch (IOException e) {
			}
		}
	}

	private String _sendCommand(String command) {
		try {
			command += "\r\n";

			_serviceOutputStream.writeBytes(command);

			String reply = _serviceBufferedReader.readLine();

			if (reply == null) {
				_serviceSocket.close();

				_socketCloseListener.onSocketClose();
			}

			return reply;
		}
		catch (IOException e) {
			return null;
		}
	}

	private static int _callbackSocketPort = 33002;

	private static Logger _logger = LoggerFactory.getLogger(
		AppleNativityPluginControlImpl.class.getName());

	private static long _messageBufferSize = 500;

	private static int _serviceSocketPort = 33001;

	private BufferedReader _callbackBufferedReader;
	private DataOutputStream _callbackOutputStream;
	private Socket _callbackSocket;
	private ReadThread _callbackThread;
	private String[] _currentFiles;
	private MenuItemListener _menuItemListener;
	private BufferedReader _serviceBufferedReader;
	private DataOutputStream _serviceOutputStream;
	private Socket _serviceSocket;
	private SocketCloseListener _socketCloseListener;

}