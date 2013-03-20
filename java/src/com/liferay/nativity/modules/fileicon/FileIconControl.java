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

package com.liferay.nativity.modules.fileicon;

import com.liferay.nativity.modules.fileicon.mac.AppleFileIconControlImpl;
import com.liferay.nativity.modules.fileicon.win.WindowsFileIconControlImpl;
import com.liferay.nativity.plugincontrol.NativityPluginControl;
import com.liferay.util.OSDetector;

import java.util.Map;

/**
 * @author Dennis Ju
 */
public abstract class FileIconControl extends FileIconControlBase {

	public FileIconControl(NativityPluginControl pluginControl) {
		super(pluginControl);

		if (_fileIconControlBaseDelegate == null) {
			if (OSDetector.isApple()) {
				_fileIconControlBaseDelegate = createAppleFileIconControlBase();
			}
			else if (OSDetector.isWindows()) {
				_fileIconControlBaseDelegate =
					createWindowsFileIconControlBase();
			}
		}
	}

	@Override
	public void disableFileIcons() {
		_fileIconControlBaseDelegate.disableFileIcons();
	}

	@Override
	public void enableFileIcons() {
		_fileIconControlBaseDelegate.enableFileIcons();
	}

	// Windows only

	@Override
	public abstract int getIconForFile(String path);

	@Override
	public int registerIcon(String path) {
		return _fileIconControlBaseDelegate.registerIcon(path);
	}

	@Override
	public void removeAllFileIcons() {
		_fileIconControlBaseDelegate.removeAllFileIcons();
	}

	@Override
	public void removeFileIcon(String fileName) {
		_fileIconControlBaseDelegate.removeFileIcon(fileName);
	}

	@Override
	public void removeFileIcons(String[] fileNames) {
		_fileIconControlBaseDelegate.removeFileIcons(fileNames);
	}

	@Override
	public void setIconForFile(String fileName, int iconId) {
		_fileIconControlBaseDelegate.setIconForFile(fileName, iconId);
	}

	@Override
	public void setIconsForFiles(Map<String, Integer> fileIconsMap) {
		_fileIconControlBaseDelegate.setIconsForFiles(fileIconsMap);
	}

	@Override
	public void setRootFolder(String folder) {
		_fileIconControlBaseDelegate.setRootFolder(folder);
	}

	@Override
	public void setSystemFolder(String folder) {
		_fileIconControlBaseDelegate.setSystemFolder(folder);
	}

	@Override
	public void unregisterIcon(int id) {
		_fileIconControlBaseDelegate.unregisterIcon(id);
	}

	protected FileIconControlBase createAppleFileIconControlBase() {
		return new AppleFileIconControlImpl(pluginControl) {
			@Override
			public int getIconForFile(String path) {
				return FileIconControl.this.getIconForFile(path);
			}
		};
	}

	protected FileIconControlBase createWindowsFileIconControlBase() {
		return new WindowsFileIconControlImpl(pluginControl) {
			@Override
			public int getIconForFile(String path) {
				return FileIconControl.this.getIconForFile(path);
			}
		};
	}

	private FileIconControlBase _fileIconControlBaseDelegate;

}