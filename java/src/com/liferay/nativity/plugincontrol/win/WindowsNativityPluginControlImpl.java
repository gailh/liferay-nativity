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

import com.liferay.nativity.Constants;
import com.liferay.nativity.plugincontrol.NativityMessage;
import com.liferay.nativity.plugincontrol.NativityPluginControl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dennis Ju
 */
public class WindowsNativityPluginControlImpl extends NativityPluginControl {

	@Override
	public void connect() throws PluginException {
		_logger.debug("Connecting...");

		if (running()) {
			throw new PluginException(PluginException.ALREADY_CONNECTED);
		}

		_receive = new WindowsReceiveSocket(this);

		_receiveExecutor.execute(_receive);
		_sendExecutor.execute(_send);

		_logger.debug("Done connecting");
	}

	public void disconnect() {

	}

	@Override
	public boolean running() {
		return _send.isConnected();
	}

	@Override
	public String sendMessage(NativityMessage message) {
		_send.send(message);

		return "";
	}

	@Override
	public void setRootFolder(String folder) {
		NativityMessage message = new NativityMessage(
			Constants.SET_ROOT_FOLDER, folder);

		sendMessage(message);
	}

	@Override
	public void setSystemFolder(String folder) {
		NativityMessage message = new NativityMessage(
			Constants.SET_SYSTEM_FOLDER, folder);

		sendMessage(message);
	}

	@Override
	public boolean startPlugin(String path) throws Exception {

		// TODO Auto-generated method stub

		return false;
	}

	private static Logger _logger = LoggerFactory.getLogger(
		WindowsNativityPluginControlImpl.class.getName());

	private WindowsReceiveSocket _receive;

	private ExecutorService _receiveExecutor =
		Executors.newSingleThreadExecutor();

	private WindowsSendSocket _send = new WindowsSendSocket();

	private ExecutorService _sendExecutor = Executors.newSingleThreadExecutor();

}