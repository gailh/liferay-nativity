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

package com.liferay.nativity.modules.contextmenu.win;

import com.liferay.nativity.Constants;
import com.liferay.nativity.modules.contextmenu.ContextMenuControlBase;
import com.liferay.nativity.plugincontrol.NativityMessage;
import com.liferay.nativity.plugincontrol.NativityPluginControl;
import com.liferay.nativity.plugincontrol.mac.MessageListener;

import java.util.List;

/**
 * @author Dennis Ju
 */
public abstract class WindowsContextMenuControlImpl
	extends ContextMenuControlBase {

	public WindowsContextMenuControlImpl(NativityPluginControl pluginControl) {
		super(pluginControl);

		MessageListener messageListener = new MessageListener() {
			@Override
			public NativityMessage onMessageReceived(NativityMessage message) {
				List<String> args = (List<String>)message.getValue();

				String command = message.getCommand();

				if (command.equals(Constants.GET_MENU_LIST)) {
					String[] menuItems = getMenuItems(
						args.toArray(new String[args.size()]));

					return new NativityMessage(
						Constants.GET_MENU_LIST, menuItems);
				}
				else if (command.equals(Constants.GET_HELP_ITEMS)) {
					String[] helpItems = getHelpItemsForMenus(
						args.toArray(new String[args.size()]));

					return new NativityMessage(
						Constants.GET_HELP_ITEMS, helpItems);
				}
				else if (command.equals(Constants.PERFORM_ACTION)) {
					int index = Integer.valueOf(args.get(0));

					args.remove(0);

					//TODO pass title
					onExecuteMenuItem(
						index, "", args.toArray(new String[args.size()]));

					return null;
				}

				return null;
			}
		};

		pluginControl.addMessageListener(messageListener);
	}

	@Override
	public void setContextMenuTitle(String title) {
		NativityMessage message = new NativityMessage(
			Constants.SET_MENU_TITLE, title);

		pluginControl.sendMessage(message);
	}

}