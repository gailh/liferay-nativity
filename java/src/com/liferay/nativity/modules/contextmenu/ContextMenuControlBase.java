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

package com.liferay.nativity.modules.contextmenu;

import com.liferay.nativity.plugincontrol.NativityPluginControl;

/**
 * @author Dennis Ju
 */
public abstract class ContextMenuControlBase {

	public ContextMenuControlBase(NativityPluginControl pluginControl) {
		this.pluginControl = pluginControl;
	}

	public abstract String[] getHelpItemsForMenus(String[] paths);

	public abstract String[] getMenuItems(String[] paths);

	public abstract void onExecuteMenuItem(
		int menuIndex, String menuText, String[] paths);

	public abstract void setContextMenuTitle(String title);

	protected NativityPluginControl pluginControl;

}