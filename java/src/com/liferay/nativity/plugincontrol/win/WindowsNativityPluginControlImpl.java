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
import com.liferay.nativity.listeners.SocketCloseListener;
import com.liferay.nativity.plugincontrol.NativityPluginControl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dennis Ju
 */
public class WindowsNativityPluginControlImpl extends NativityPluginControl {

	@Override
	public boolean connect() {

		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public boolean disconnect() {

		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public void enableOverlays(boolean enable) {

		// TODO Auto-generated method stub

	}

	public int getIconForFile(String fileName) {
		return _fileIconMap.remove(fileName);
	}

	@Override
	public boolean pluginRunning() {

		// TODO Auto-generated method stub

		return false;
	}

	public void refreshIconForFile(String fileName) {

		// TODO

	}

	@Override
	public int registerIcon(String path) {

		// TODO Auto-generated method stub

		return 0;
	}

	@Override
	public void removeFileIcon(String fileName) {

		// TODO Auto-generated method stub

	}

	@Override
	public void removeFileIcons(String[] fileNames) {

		// TODO Auto-generated method stub

	}

	@Override
	public void setContextMenuTitle(String title) {

		// TODO Auto-generated method stub

	}

	@Override
	public void setIconForFile(String fileName, int iconId) {
		_fileIconMap.put(fileName, iconId);
	}

	@Override
	public void setIconsForFiles(Map<String, Integer> fileIconsMap) {
		_fileIconMap.putAll(fileIconsMap);
	}

	@Override
	public void setMenuItemListener(MenuItemListener menuItemListener) {

		// TODO Auto-generated method stub

	}

	@Override
	public void setSocketCloseListener(
		SocketCloseListener finderCrashListener) {

		// TODO Auto-generated method stub

	}

	@Override
	public boolean startPlugin(String path) {

		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public void unregisterIcon(int id) {

		// TODO Auto-generated method stub

	}

	private Map<String, Integer> _fileIconMap = new HashMap<String, Integer>();

}