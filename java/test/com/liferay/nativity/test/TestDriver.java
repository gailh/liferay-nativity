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

import com.liferay.nativity.plugincontrol.win.PlugInException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.xml.DOMConfigurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gail Hernandez
 */
public class TestDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		_intitializeLogging();

		_logger.debug("main");

		TestPlugIn testPlugIn = new TestPlugIn();

		BufferedReader bufferedReader = new BufferedReader(
			new InputStreamReader(System.in));

		try {
			testPlugIn.connect();
		}
		catch (PlugInException e1) {
			_logger.error(e1.getMessage(), e1);
		}

		String read = "";
		boolean stop = false;
		try {
			while (!stop) {
				_list = !_list;

				_logger.debug("Loop start...");

				_logger.debug("_enableFileIcons");
				_enableFileIcons(testPlugIn);

				_logger.debug("_setMenuTitle");
				_setMenuTitle(testPlugIn);

				_logger.debug("_setRootFolder");
				_setRootFolder(testPlugIn);

				_logger.debug("_setSystemFolder");
				_setSystemFolder(testPlugIn);

				_logger.debug("_updateFileIcon");
				_updateFileIcon(testPlugIn);

				_logger.debug("_clearFileIcon");
				_clearFileIcon(testPlugIn);

				_logger.debug("Ready?");

				if (bufferedReader.ready()) {
					_logger.debug("Reading...");

					read = bufferedReader.readLine();

					_logger.debug("Read {}", read);

					if (read.length() > 0) {
						stop = true;
					}

					_logger.debug("Stopping {}", stop);
				}
			}
		} catch (IOException e) {
			_logger.error(e.getMessage(), e);
		}

		_logger.debug("Done");
	}

	private static void _clearFileIcon(TestPlugIn testPlugIn) {
		if (_list) {
			String[] fileNames = new String[]
			{
				"C:/Users/liferay/Documents/liferay-sync/My Documents (test)/" +
				"temp",
				"C:/Users/liferay/Documents/liferay-sync/" +
				"My Documents (test)temp/Sync.pptx"};

			testPlugIn.removeFileIcons(fileNames);
		}
		else {
			testPlugIn.removeFileIcon(
				"C:/Users/liferay/Documents/liferay-sync/My Documents (test)/" +
				"temp");
		}

		try {
			Thread.sleep(_waitTime);
		}
		catch (InterruptedException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private static void _enableFileIcons(TestPlugIn testPlugIn) {
		testPlugIn.enableFileIcons();

		try {
			Thread.sleep(_waitTime);
		}
		catch (InterruptedException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private static void _intitializeLogging() {
		File file = new File("java/nativity-log4j.xml");

		if (file.exists()) {
			DOMConfigurator.configure(file.getPath());
		}
	}

	private static void _setMenuTitle(TestPlugIn testPlugIn) {
		testPlugIn.setContextMenuTitle("Test");

		try {
			Thread.sleep(_waitTime);
		}
		catch (InterruptedException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private static void _setRootFolder(TestPlugIn testPlugIn) {
		testPlugIn.setRootFolder("C:/Users/liferay/Documents/liferay-sync");

		try {
			Thread.sleep(_waitTime);
		}
		catch (InterruptedException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private static void _setSystemFolder(TestPlugIn testPlugIn) {
		testPlugIn.setSystemFolder("C:/Users/liferay/Documents/liferay-sync");

		try {
			Thread.sleep(_waitTime);
		}
		catch (InterruptedException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private static void _updateFileIcon(TestPlugIn testPlugIn) {
		if (_list) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put(
				"C:/Users/liferay/Documents/liferay-sync/" +
				"My Documents (test)/temp", 1);

			map.put(
				"C:/Users/liferay/Documents/liferay-sync/" +
				"My Documents (test)/temp/Sync.pptx", 2);

			testPlugIn.setIconsForFiles(map);
		}
		else {
			testPlugIn.setIconForFile(
				"C:/Users/liferay/Documents/liferay-sync/" +
				"My Documents (test)/temp", 1);
		}

		try {
			Thread.sleep(_waitTime);
		}
		catch (InterruptedException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private static boolean _list = false; private static int _waitTime = 1000;
	private static Logger _logger = LoggerFactory.getLogger(
		TestPlugIn.class.getName());

}