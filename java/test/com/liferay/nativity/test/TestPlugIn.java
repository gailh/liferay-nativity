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

package com.liferay.nativity.test;

import com.liferay.nativity.plugincontrol.win.WindowsNativityPluginControlImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gail Hernandez
 */
public class TestPlugIn extends WindowsNativityPluginControlImpl {

	public TestPlugIn() {
		_logger.debug("TestPlugIn");

		_random = new Random();
	}

	@Override
	public void fireMenuItemExecuted(int index, String[] paths) {
		_logger.debug("Fired {} : {}", index, paths);
	}

	public int getFileIconForFile(String path) {
		return _random.nextInt(10);
	}

	@Override
	public String[] getHelpItemsForMenus(String[] files) {
		_logger.debug("getHelpItemsForMenus {}", files);

		int count = _random.nextInt(10);

		List<String> items = new ArrayList<String>();

		for (int i = 0; i < count; i++) {
			items.add("Help " + i);
		}

		return items.toArray(new String[0]);
	}

	public String[] getMenuItems(String[] files) {
		_logger.debug("getMenuItems {}", files);

		int count = _random.nextInt(20);
		while (count < 3) {
			count = _random.nextInt(20);
		}

		List<String> items = new ArrayList<String>();

		for (int i = 0; i < count; i++) {
			items.add("Menu " + i);
		}

		return items.toArray(new String[0]);
	}

	protected void menuItemExecuted(int index, String[] files) {
		_logger.debug("menuItemExecuted {} {}", index, files);
	}

	private static Logger _logger = LoggerFactory.getLogger(
			TestPlugIn.class.getName());

	private Random _random;

}