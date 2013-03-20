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

import com.liferay.nativity.plugincontrol.linux.LinuxNativityPluginControlImpl;
import com.liferay.nativity.plugincontrol.mac.AppleNativityPluginControlImpl;
import com.liferay.nativity.plugincontrol.win.WindowsNativityPluginControlImpl;
import com.liferay.util.OSDetector;

/**
 * @author Dennis Ju
 */
public class NativityPluginControlUtil {

	public static NativityPluginControl getNativityPluginControl() {
		if (_nativityPluginControl == null) {
			if (OSDetector.isApple()) {
				_nativityPluginControl = new AppleNativityPluginControlImpl();
			}
			else if (OSDetector.isWindows()) {
				_nativityPluginControl = new WindowsNativityPluginControlImpl();
			}
			else if (OSDetector.isUnix()) { //fix for Linux
				_nativityPluginControl = new LinuxNativityPluginControlImpl();
			}
			else {

				// log error

				_nativityPluginControl = null;
			}
		}

		return _nativityPluginControl;
	}

	private static NativityPluginControl _nativityPluginControl;

}