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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gail Hernandez
 */
public class NativeMessage {

	public List<String> getArgs() {
		if (_args == null) {
			_args = new ArrayList<String>();
		}

		return _args;
	}

	public String getCommand() {
		return _command;
	}

	public void setArgs(List<String> args) {
		_args = args;
	}

	public void setCommand(String command) {
		_command = command;
	}

	public String toString() {
		return "Command " + _command + " " + getArgs().toString();
	}

	private List<String> _args;
	private String _command;

}