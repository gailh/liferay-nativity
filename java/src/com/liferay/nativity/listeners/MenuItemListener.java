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

package com.liferay.nativity.listeners;

/**
 * @author Dennis Ju
 */
public interface MenuItemListener {

	/**
	 * Callback method that executes when user selects custom menu item
	 *
	 * @param index of menu item (index in the array returned by previous
	 *        getMenuItems call)
	 * @param text of menu item
	 * @param files array on which context menu item executed
	 */
	public void onExecuteMenuItem(int index, String menuText, String[] paths);

	/**
	 * Callback method called by native plugin when context menu executed on one
	 * or more files. User code can override this method to add a number of
	 * additional items to context menu.
	 *
	 * @param array of file names on which context menu executed
	 *
	 * @return array of menu items that should be added to context menu, or null
	 *         if additional context menu not needed
	 */
	public String[] onPopulateMenuItems(String[] paths);

}