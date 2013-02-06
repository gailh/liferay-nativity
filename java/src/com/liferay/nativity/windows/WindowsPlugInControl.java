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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Map;

public abstract class WindowsPlugInControl {
	public WindowsPlugInControl() {
	}

	public boolean connect() {
		if (_shutdown) {
			return false;
		}
		_receive = new RPCSocketMonitorTask(this);
		
		_receiveExecutor.execute(_receive);
		
		return true;
	}

	public void disconnect() {
		_shutdown = true;

		if (_receive != null) {
			_receive.stop();
		}
		_executor.shutdownNow();
		
		_executor = null;
	}

	public void enableOverlays(boolean enable) {
		_sendCommand(_ENABLE_OVERLAYS, String.valueOf(enable));
	}

	public abstract int getFileIcon(String path);

	public int registerIcon(String path) {
		throw new UnsupportedOperationException();
	}

	public void removeFileIcon(String fileName) {
		throw new UnsupportedOperationException();	
	}

	public void removeFileIcon(String[] fileNames) {
		throw new UnsupportedOperationException();
	}

	public void setContextMenuTitle(String title) {
		_sendCommand(_SET_MENU_TITLE, title);
	}

	public void setIconForFile(String fileName, int iconId) {
		throw new UnsupportedOperationException();
	}

	public void setIconsForFiles(Map<String, Integer> fileIconsMap) {
		throw new UnsupportedOperationException();
	}

	public void unregisterIcon(int id) {
		throw new UnsupportedOperationException();
	}

	protected abstract String[] getMenuItems(String[] files);

	protected abstract void menuItemExecuted(int index, String[] files);

	private void _sendCommand(String command, String value) {
		String message = "{\"command\":\"" + command + "\", \"value\",\"" + value + "\"}";
		_executor.execute(new RPCSocketSendTask(message));
	}
	
	private static final String _ENABLE_OVERLAYS = "enableOverlays";
	private static final String _SET_MENU_TITLE = "setMenuTitle";

	private ExecutorService _executor = Executors.newSingleThreadExecutor();
	private ExecutorService _receiveExecutor = Executors.newSingleThreadExecutor();

	private boolean _shutdown = false;

	private RPCSocketMonitorTask _receive;
	
}