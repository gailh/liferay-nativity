/**
 *  Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *  
 *  This library is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation; either version 2.1 of the License, or (at your option)
 *  any later version.
 *  
 *  This library is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 *  details.
 */

#ifndef COMMUNICATIONPROCESSOR_H
#define COMMUNICATIONPROCESSOR_H

#pragma once

#include "stdafx.h"

class __declspec(dllexport) CommunicationProcessor
{
public:
	CommunicationProcessor();
	~CommunicationProcessor();

	bool IsDisplayIcons();
	bool IsDisplayContextMenus();
	const wchar_t* GetContextMenuTitle();
	
	bool ProcessMessage(const wchar_t*);
	bool ProcessCommunication(const wchar_t*, const wchar_t*);

private:
	bool _displayIcons;
	bool _displayContextMenus;
	const wchar_t* _contextMenuTitle;

	bool SetDisplayIcons(bool);
	bool SetContextMenuTitle(const wchar_t*);
};

#endif
