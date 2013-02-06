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

#ifndef CONTEXTMENUUTIL_H
#define CONTEXTMENUUTIL_H

#pragma once

#include "stdafx.h"

class __declspec(dllexport) ContextMenuUtil
{
public:
	ContextMenuUtil();

	~ContextMenuUtil(void);

	bool AddFile(const wchar_t*);

	int GetActionIndex(const wchar_t*);

	bool GetHelpText(int, std::wstring&);

	bool GetMenus(std::list<std::wstring*>&);

	bool GetRootText(std::wstring&);

	bool GetVerbText(int, std::wstring&);

	bool IsMenuNeeded(void);
	
	bool PerformAction(int);

private:

	bool _GetCommandText(int,std::wstring&);

	bool _GenerateMessage(int, std::wstring&);

	bool _InitMenus(); 

	bool _ParseMessage(const wchar_t*, std::wstring&);

	bool _ParseMessageToList(const wchar_t*, std::list<std::wstring*>&);

	CommunicationSocket* _communicationSocket;
	
	std::list<const wchar_t*>* _helpTextList;

	std::list<const wchar_t*>* _menuList;

	const wchar_t* _rootMenu;

	std::list<const wchar_t*>* _selectedFiles;

	
};

#endif
