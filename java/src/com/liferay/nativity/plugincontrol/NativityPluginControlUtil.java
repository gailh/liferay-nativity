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

	public static NativityPluginControl getPluginControl() {
		if (_pluginControl == null) {
			if (OSDetector.isMinimumAppleVersion(
					OSDetector.MAC_SNOW_LEOPARD_10_6)) {

				_pluginControl = new AppleNativityPluginControlImpl();
			}
			else if (OSDetector.isMinimumWindowsVersion(OSDetector.WIN_VISTA)) {
				_pluginControl = new WindowsNativityPluginControlImpl();
			}
			else if (OSDetector.isUnix()) {
				_pluginControl = new LinuxNativityPluginControlImpl();
			}
			else {
				return null;
			}
		}

		return _pluginControl;
	}

	private static NativityPluginControl _pluginControl;

}