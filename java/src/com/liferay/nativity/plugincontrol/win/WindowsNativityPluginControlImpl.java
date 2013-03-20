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

import com.liferay.nativity.listeners.MenuItemListener;
import com.liferay.nativity.plugincontrol.NativityPluginControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dennis Ju
 */
public abstract class WindowsNativityPluginControlImpl
	extends NativityPluginControl {

	@Override
	public void addMenuItemListener(MenuItemListener menuItemListener) {
		_listeners.add(menuItemListener);
	}

	@Override
	public void connect() throws PlugInException {
		_logger.debug("Connecting...");

		if (pluginRunning()) {
			throw new PlugInException(PlugInException.ALREADY_CONNECTED);
		}

		_receive = new WindowsReceiveSocket(this);

		_receiveExecutor.execute(_receive);
		_sendExecutor.execute(_send);

		_logger.debug("Done connecting");
	}

	@Override
	public void disableFileIcons() {
		_send.send(_ENABLE_FILE_ICONS, String.valueOf(false));
	}

	public void disconnect() {

	}

	@Override
	public void enableFileIcons() {
		_send.send(_ENABLE_FILE_ICONS, String.valueOf(true));
	}

	@Override
	public void fireMenuItemExecuted(int index, String[] paths) {
		for (MenuItemListener listener : _listeners) {
			listener.onExecuteMenuItem(index, paths);
		}
	}

	public abstract int getFileIconForFile(String path);

	public abstract String[] getHelpItemsForMenus(String[] files);

	@Override
	public boolean pluginRunning() {
		return _send.isConnected();
	}

	@Override
	public void removeFileIcon(String fileName) {
		_send.send(_CLEAR_FILE_ICON, fileName);
	}

	@Override
	public void removeFileIcons(String[] fileNames) {
		_send.send(_CLEAR_FILE_ICON, fileNames);
	}

	@Override
	public void setContextMenuTitle(String title) {
		_send.send(_SET_MENU_TITLE, title);
	}

	@Override
	public void setIconForFile(String fileName, int iconId) {
		_send.send(_UPDATE_FILE_ICON, fileName);
	}

	@Override
	public void setIconsForFiles(Map<String, Integer> fileIconsMap) {
		_send.send(_UPDATE_FILE_ICON, fileIconsMap.keySet().toArray());
	}

	@Override
	public void setRootFolder(String folder) {
		_send.send(_SET_ROOT_FOLDER, folder);
	}

	public void setSystemFolder(String folder) {
		_send.send(_SET_SYSTEM_FOLDER, folder);
	}

	private static final String _CLEAR_FILE_ICON = "clearFileIcon";

	private static final String _ENABLE_FILE_ICONS = "enableFileIcons";
	private static final String _SET_MENU_TITLE = "setMenuTitle";
	private static final String _SET_ROOT_FOLDER = "setRootFolder";
	private static final String _SET_SYSTEM_FOLDER = "setSystemFolder";
	private static final String _UPDATE_FILE_ICON = "updateFileIcon";
	private static Logger _logger = LoggerFactory.getLogger(
		WindowsNativityPluginControlImpl.class.getName());

	private List<MenuItemListener> _listeners =
		new ArrayList<MenuItemListener>();

	private WindowsReceiveSocket _receive;
	private ExecutorService _receiveExecutor =
		Executors.newSingleThreadExecutor();

	private WindowsSendSocket _send = new WindowsSendSocket();
	private ExecutorService _sendExecutor = Executors.newSingleThreadExecutor();

}