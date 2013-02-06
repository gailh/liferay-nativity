/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
		if(!testPlugIn.connect()) {
			_logger.debug("Unable to connect");
		}
		
		_logger.debug("Connected");
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		String read = "";
		boolean stop = false;
		try {
			while(!stop) {
				_logger.debug("Loop start...");
				
				testPlugIn.setContextMenuTitle("Test");
				
				try {
					Thread.sleep(1000);
				} 
				catch (InterruptedException e) {
					_logger.error(e.getMessage(), e);
				}
				
				testPlugIn.enableOverlays(true);
				
				try {
					Thread.sleep(1000);
				} 
				catch (InterruptedException e) {
					_logger.error(e.getMessage(), e);
				}
				
				_logger.debug("Ready?");
				
				if(bufferedReader.ready()) {
					_logger.debug("Reading...");
					
					read = bufferedReader.readLine();
					
					_logger.debug("Read {}", read);
					
					if(read.length() > 0) {
						stop = true;
					}
					
					_logger.debug("Stopping {}", stop);
				}
			}
		} catch (IOException e) {
			_logger.error(e.getMessage(), e);
		}
		
		_logger.debug("Disconnecting");
		
		testPlugIn.disconnect();
		
		_logger.debug("Done");
	}
	
	private static void _intitializeLogging() {
		File file = new File("java/nativity-log4j.xml");

		if (file.exists()) {
			DOMConfigurator.configure(file.getPath());
		}
	}

	private static Logger _logger = LoggerFactory.getLogger(
		TestPlugIn.class.getName());

}
