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
import com.liferay.nativity.listeners.SocketCloseListener;

import java.util.Map;

/**
 * @author Dennis Ju
 */
public abstract class NativityPluginControl {

	/**
	 * Initialize connection with native service
	 *
	 * @return true if connection is successful
	 */
	public abstract boolean connect();

	/**
	 * Disconnects from plugin service
	 *
	 * @return true if disconnect is successful
	 */
	public abstract boolean disconnect();

	/**
	 * Enable/Disable icon overlay feature
	 *
	 * @param enable pass true is overlay feature should be enabled
	 */
	public abstract void enableOverlays(boolean enable);

	/**
	 * Check to see if the plugin is running
	 *
	 * @return true if plugin is running
	 */
	public abstract boolean pluginRunning();

	/**
	 * Register icon in the service
	 *
	 * @param path to icon file
	 *
	 * @return registered icon id or 0 in case error
	 */
	public abstract int registerIcon(String path);

	/**
	 * Remove icon overlay from file (previously set by setIconForFile)
	 *
	 * @param name of file
	 */
	public abstract void removeFileIcon(String fileName);

	/**
	 * Remove icon overlays from files (previously set by setIconForFile)
	 *
	 * @param array of files
	 */
	public abstract void removeFileIcons(String[] fileNames);

	/**
	 * Set title of root context menu item, all other items will be added as
	 * children of it
	 *
	 * @param new title of item
	 */
	public abstract void setContextMenuTitle(String title);

	/**
	 * Associate icon with fileName
	 *
	 * @param target file name
	 * @param id of icon that should be associated with file
	 */
	public abstract void setIconForFile(String fileName, int iconId);

	/**
	 * Associate icons with multiple fileNames.
	 *
	 * @param map containing icon id values keyed by file name
	 */
	public abstract void setIconsForFiles(Map<String, Integer> fileIconsMap);

	/**
	 * Set the listener that triggers when a context menu opens
	 *
	 * @param a MenuItemListener
	 */
	public abstract void setMenuItemListener(MenuItemListener menuItemListener);

	/**
	 * Set the listener that triggers when the socket to the service is closed
	 *
	 * @param a SocketCloseListener
	 */
	public abstract void setSocketCloseListener(
		SocketCloseListener finderCrashListener);

	/**
	 * Start the plugin
	 * @param The path to the plugin
	 * @return true if plugin succesfully started
	 * @throws IOException
	 * @throws Exception
	 */
	public abstract boolean startPlugin(String path) throws Exception;

	/**
	 * Unregister icon in the service
	 *
	 * @param id of icon previously registered by registerIcon method
	 */
	public abstract void unregisterIcon(int id);

}