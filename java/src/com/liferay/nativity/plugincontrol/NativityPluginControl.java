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

package com.liferay.nativity.plugincontrol;

import com.liferay.nativity.listeners.MenuItemListener;
import com.liferay.nativity.plugincontrol.win.PlugInException;

import java.util.Map;

/**
 * @author Dennis Ju
 */
public abstract class NativityPluginControl {

	/**
	 * Set the listener that triggers when a context menu opens
	 *
	 * @param a MenuItemListener
	 */
	public abstract void addMenuItemListener(MenuItemListener menuItemListener);

	public abstract void connect() throws PlugInException;

	public abstract void disableFileIcons();

	public abstract void disconnect();

	/**
	 * Enable/Disable icon overlay feature
	 *
	 * @param enable pass true is overlay feature should be enabled
	 */
	public abstract void enableFileIcons();

	public abstract void fireMenuItemExecuted(int index, String[] paths);
	public abstract String[] getMenuItems(String[] paths);

	/**
	 * Check to see if the plugin is running
	 *
	 * @return true if plugin is running
	 */
	public abstract boolean pluginRunning();

	public abstract void removeFileIcon(String fileName);

	public abstract void removeFileIcons(String[] fileNames);

	/**
	 * Set title of root context menu item, all other items will be added as
	 * children of it
	 *
	 * @param new title of item
	 */
	public abstract void setContextMenuTitle(String title);

	public abstract void setIconForFile(String fileName, int iconId);

	public abstract void setIconsForFiles(Map<String, Integer> fileIconsMap);

	public abstract void setRootFolder(String folder);

}