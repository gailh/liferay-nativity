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

package com.liferay.nativity.modules.contextmenu.mac;

import com.liferay.nativity.Constants;
import com.liferay.nativity.modules.contextmenu.ContextMenuControlBase;
import com.liferay.nativity.plugincontrol.NativityMessage;
import com.liferay.nativity.plugincontrol.NativityPluginControl;
import com.liferay.nativity.plugincontrol.mac.MessageListener;

import flexjson.JSONSerializer;

import java.util.List;
import java.util.Map;

/**
 * @author Dennis Ju
 */
public abstract class AppleContextMenuControlImpl
	extends ContextMenuControlBase {

	public AppleContextMenuControlImpl(NativityPluginControl pluginControl) {
		super(pluginControl);

		MessageListener messageListener = new MessageListener() {
			@Override
			public NativityMessage onMessageReceived(NativityMessage message) {
				String command = message.getCommand();

				if (command.equals(Constants.MENU_QUERY)) {
					_currentFiles = (List<String>)message.getValue();

					String[] currentFilesArray =
						(String[])_currentFiles.toArray(
							new String[_currentFiles.size()]);

					String[] items = getMenuItems(currentFilesArray);

					return new NativityMessage(Constants.MENU_ITEMS, items);
				}
				else if (command.equals(Constants.MENU_EXEC)) {
					Map<String, Object> args =
						(Map<String, Object>)message.getValue();

					int menuIndex = (Integer)args.get(Constants.MENU_INDEX);
					String menuText = (String)args.get(Constants.MENU_TEXT);

					String[] currentFiles =
						(String[])_currentFiles.toArray(
							new String[_currentFiles.size()]);

					onExecuteMenuItem(menuIndex, menuText, currentFiles);
				}

				return null;
			}
		};

		pluginControl.addMessageListener(messageListener);
	}

	@Override
	public final void setContextMenuTitle(String title) {
		NativityMessage message = new NativityMessage(
			Constants.SET_MENU_TITLE, title);

		pluginControl.sendMessage(message);
	}

	private static JSONSerializer _jsonSerializer = new JSONSerializer();
	private List<String> _currentFiles;

}