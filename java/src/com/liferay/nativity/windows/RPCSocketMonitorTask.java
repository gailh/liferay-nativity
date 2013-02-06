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

package com.liferay.nativity.windows;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gail Hernandez
 */
public class RPCSocketMonitorTask implements Runnable {

	public RPCSocketMonitorTask(WindowsPlugInControl plugInControl) {
		_plugInControl = plugInControl;
	}

	public void run() {
		synchronized(this) {
			if (_running) {
				return;
			}

			_running = true;
		}

		_logger.trace("Running listener");

		while (_serverSocket == null) {
			try {
				_logger.debug("New server socket");

				_serverSocket = new ServerSocket(33001);
			}
			catch (IOException ioe) {
				_logger.error(ioe.getMessage(), ioe);

				return;
			}
		}

		while (_running) {
			_waitForConnection();
		}

		_logger.trace("Completed");
	}
	
	public void stop() {
		synchronized(this) {
			if (!_running) {
				return;
			}

			_running = false;
		}

		try {
			_serverSocket.close();
		}
		catch (IOException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private void _waitForConnection() {
		try {
			_logger.trace("Waiting for connection");

			Socket clientSocket = _serverSocket.accept();

			_connectionExecutor.execute(
				new ConnectionWorker(clientSocket, _plugInControl));
		}
		catch (SocketException se) {

			// Expected when socket is closed.

		}
		catch (IOException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private static Logger _logger = LoggerFactory.getLogger(
		RPCSocketMonitorTask.class.getName());

	private Executor _connectionExecutor = Executors.newSingleThreadExecutor();

	private boolean _running;

	private ServerSocket _serverSocket;
	
	private WindowsPlugInControl _plugInControl;

}