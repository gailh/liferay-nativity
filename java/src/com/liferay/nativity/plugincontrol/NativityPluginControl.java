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

import com.liferay.nativity.modules.contextmenu.ContextMenuControl;
import com.liferay.nativity.modules.fileicon.FileIconControl;
import com.liferay.nativity.plugincontrol.mac.MessageListener;
import com.liferay.nativity.plugincontrol.win.PluginException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Ju
 */
public abstract class NativityPluginControl {

	public NativityPluginControl() {
		_messageListeners = new ArrayList<MessageListener>();
	}

	public void addMessageListener(MessageListener messageListener) {
		_messageListeners.add(messageListener);
	}

	public abstract void connect() throws PluginException;

	public abstract void disconnect();

	public List<NativityMessage> fireMessageListener(NativityMessage message) {
		List<NativityMessage> responses = new ArrayList<NativityMessage>();

		for (MessageListener messageListener : _messageListeners) {
			NativityMessage responseMessage = messageListener.onMessageReceived(
				message);

			responses.add(responseMessage);
		}

		return responses;
	}

	public abstract boolean running();

	public abstract String sendMessage(NativityMessage message);

	public abstract boolean startPlugin(String path) throws Exception;

	protected ContextMenuControl contextMenuControl;

	protected FileIconControl fileIconControl;

	private List<MessageListener> _messageListeners;

}