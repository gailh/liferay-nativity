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

package com.liferay.nativity.plugincontrol.win;

import flexjson.JSONDeserializer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.Socket;

import java.nio.charset.Charset;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gail Hernandez
 */
public class MessageProcessor implements Runnable {

	public MessageProcessor(
		Socket clientSocket, WindowsNativityPluginControlImpl plugIn) {

		_clientSocket = clientSocket;
		_plugIn = plugIn;
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

	private String _format(String[] menuItems) {
		StringBuilder string = new StringBuilder();
		string.append("{");
		string.append(_QUOTE);
		string.append(_RESPONSE);
		string.append(_QUOTE);
		string.append(_COLON);
		string.append("[");

		int i = 0;
		for (String menu : menuItems) {
			if (i > 0) {
				string.append(",");
			}

			string.append(_QUOTE);
			string.append(menu);
			string.append(_QUOTE);
			i++;
		}

		string.append("]");
		string.append("}");

		return string.toString();
	}

	private String _formatIcon(int icon) {
		StringBuilder string = new StringBuilder();
		string.append("{");
		string.append(_QUOTE);
		string.append(_RESPONSE);
		string.append(_QUOTE);
		string.append(_COLON);
		string.append(_QUOTE);
		string.append(String.valueOf(icon));
		string.append(_QUOTE);
		string.append("}");

		return string.toString();
	}

	private String _getString(int argumentNumber, String receivedMessage) {
		int currentIndex = 0;
		int tempIndex = 0;
		int count = 0;
		while ((tempIndex = receivedMessage.indexOf(",", currentIndex)) != -1) {
			if (count == argumentNumber) {
				String temp = receivedMessage.substring(
					currentIndex, tempIndex);

				return temp;
			}

			currentIndex = tempIndex;
			count++;
		}

		return "";
	}

	private void _handle(String receivedMessage) throws IOException {
		_logger.debug("Message {}", receivedMessage);

		JSONDeserializer<NativeMessage> jsonDeserializer =
			new JSONDeserializer<NativeMessage>();

		try {
			NativeMessage message = jsonDeserializer.deserialize(
				receivedMessage, NativeMessage.class);

			String command = message.getCommand();
			List<String> args = message.getArgs();

			if (command.contains(_GET_MENU_LIST)) {
				String[] menuItems = _plugIn.getMenuItems(
					args.toArray(new String[] {}));

				String response = _format(menuItems);
				_outputStreamWriter.write(response);
				_outputStreamWriter.write("\0");

			}
			else if (command.contains(_GET_HELP_ITEMS)) {
				String[] helpItems = _plugIn.getHelpItemsForMenus(
					args.toArray(new String[] {}));

				String response = _format(helpItems);
				_outputStreamWriter.write(response);
				_outputStreamWriter.write("\0");

			}
			else if (command.contains(_PERFORM_ACTION)) {
				int index = Integer.valueOf(args.get(0));
				args.remove(0);
				_plugIn.fireMenuItemExecuted(
					index, args.toArray(new String[] {}));

				_returnEmpty();
				return;
			}
			else if (command.contains(_GET_FILE_OVERLAY_ID)) {
				if(args.size() > 0) {
					String arg1 = args.get(0);
					int icon = _plugIn.getFileIconForFile(arg1);
					String response = _formatIcon(icon);
					_outputStreamWriter.write(response);
				}
			
				_outputStreamWriter.write("\0");
			}
		}
		catch (IOException e) {
			_logger.error(e.getMessage(), e);
		}
		finally {

			// Windows expects null terminated string.
			if(!_clientSocket.isOutputShutdown()) {
				_outputStreamWriter.close();
			}
		}
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

	private static final String _COLON = ":";

	private static final String _GET_FILE_OVERLAY_ID = "getFileOverlayId";
	private static final String _GET_HELP_ITEMS = "getHelpItemsForMenus";
	private static final String _GET_MENU_LIST = "getMenuList";
	private static final String _PERFORM_ACTION = "performAction";
	private static final String _QUOTE = "\"";
	private static final String _RESPONSE = "response";
	private static Logger _logger = LoggerFactory.getLogger(
		MessageProcessor.class.getName());

	private Socket _clientSocket;

	private InputStreamReader _inputStreamReader;
	private OutputStreamWriter _outputStreamWriter;
	private WindowsNativityPluginControlImpl _plugIn;

}