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
import java.io.OutputStreamWriter;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gail Hernandez
 */
public class RPCSocketSendTask implements Runnable {

	public RPCSocketSendTask(String command) {
		_command = command;
	}

	public void run() {
		_logger.debug("Running task {}", _command);
		
		if(_serverSocket == null) {
			try {
				_logger.debug("New server socket");
	
				_serverSocket = new ServerSocket(33002);
			}
			catch (IOException ioe) {
				_logger.error(ioe.getMessage(), ioe);
				return;
			}
		}
		
		send();
	}
	
	public void disconnect() {
		if(_serverSocket != null) {
			try {
				_serverSocket.close();
			} 
			catch (IOException e) {
				_logger.error(e.getMessage(), e);
			}
		}
		
		_serverSocket = null;
	}
	
	public void send() {
		try {
			_logger.trace("Waiting for connection to send {}", _command);
			
			Socket clientSocket = _serverSocket.accept();

			_logger.debug("Received connection sending");
			
			OutputStreamWriter _outputStreamWriter = new OutputStreamWriter(
				clientSocket.getOutputStream(), Charset.forName("UTF-16LE"));
			
			_outputStreamWriter.write(_command);
			
			_outputStreamWriter.write("\0");

			_outputStreamWriter.close();
			
			_logger.debug("Finished sending");
			
			disconnect();
		}
		catch (SocketException se) {

			// Expected when socket is closed.

		}
		catch (IOException e) {
			_logger.error(e.getMessage(), e);
		}
		catch(Exception e) {
			_logger.error(e.getMessage(), e);
		}
	}
	
	private String _command;

	private static Logger _logger = LoggerFactory.getLogger(
		RPCSocketSendTask.class.getName());

	private ServerSocket _serverSocket;
}