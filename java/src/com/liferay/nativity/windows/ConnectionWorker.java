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

package com.liferay.nativity.windows;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.Socket;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gail Hernandez
 */
public class ConnectionWorker implements Runnable {

	public ConnectionWorker(
		Socket clientSocket, WindowsPlugInControl plugInControl) {

		_clientSocket = clientSocket;
		_plugInControl = plugInControl;
	}

	@Override
	public void run() {
		_init();

		try {
			StringBuilder sb = new StringBuilder();

			boolean end = false;

			while (!end) {
				int item = _inputStreamReader.read();
				if (item == -1) {
					end = true;
				}
				else {
					char letter = (char)item;

					sb.append(letter);
				}
			}

			
			String message = sb.toString();
			
			message = message.replace("\\", "/");

			if (message.isEmpty()) {
				_returnEmpty();
			}
			else {
				_handle(message);
			}
		}
		catch (Exception e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private void _handle(String receivedMessage) throws IOException {
		
		if(receivedMessage.contains(_MENU_QUERY)) {
			String[] args = _getStringArray(0, receivedMessage);
			String[] menuItems = _plugInControl.getMenuItems(args);
			_outputStreamWriter.write(String.valueOf(menuItems));
		}
		else if(receivedMessage.contains(_MENU_EXEC)) {
			int index = _getInt(0, receivedMessage);
			String[] args = _getStringArray(1, receivedMessage);
			_plugInControl.menuItemExecuted(index, args);
		}
		else if(receivedMessage.contains(_GET_FILE_ICON)) {
			String arg1 = _getString(0, receivedMessage);
			int icon = _plugInControl.getFileIcon(arg1);
			_outputStreamWriter.write(String.valueOf(icon));
		}

		try {

			// Windows expected null terminated string.

			_outputStreamWriter.write("\0");

			_outputStreamWriter.close();
		}
		catch (IOException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private String _getString(int argumentNumber, String receivedMessage) {
		int currentIndex = 0;
		int tempIndex = 0;
		int count = 0;
		while((tempIndex = receivedMessage.indexOf(",", currentIndex)) != -1) {
			if(count == argumentNumber) {
				String temp = receivedMessage.substring(
					currentIndex, tempIndex);
				
				return temp;
			}
			
			currentIndex = tempIndex;
			count++;
		}
		
		return "";
	}

	private int _getInt(int i, String receivedMessage) {
		String value = _getString(i, receivedMessage);
		
		return Integer.valueOf(value);
	}

	private String[] _getStringArray(int startIndex, String receivedMessage) {
		List<String> values = new ArrayList<String>();
		
		int currentIndex = 0;
		int tempIndex = 0;
		int count = 0;
		
		while((tempIndex = receivedMessage.indexOf(",", currentIndex)) != -1) {
			if(count >= startIndex) {
				String temp = receivedMessage.substring(
					currentIndex, tempIndex);
				
				values.add(temp);
			}
			
			currentIndex = tempIndex;
			count++;
		}
		
		return values.toArray(new String[0]);
	}

	private void _init() {
		try {
			_inputStreamReader = new InputStreamReader(
				_clientSocket.getInputStream(), Charset.forName("UTF-16LE"));

			_outputStreamWriter = new OutputStreamWriter(
				_clientSocket.getOutputStream(), Charset.forName("UTF-16LE"));
		}
		catch (IOException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private void _returnEmpty() {
		try {
			_outputStreamWriter.close();
		}
		catch (IOException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private static final String _MENU_QUERY = "menuQuery";
	private static final String _MENU_EXEC = "menuExec";
	private static final String _GET_FILE_ICON = "getFileIcon";

	private static Logger _logger = LoggerFactory.getLogger(
		ConnectionWorker.class.getName());

	private Socket _clientSocket;
	private WindowsPlugInControl _plugInControl;
	private InputStreamReader _inputStreamReader;
	private OutputStreamWriter _outputStreamWriter;

}