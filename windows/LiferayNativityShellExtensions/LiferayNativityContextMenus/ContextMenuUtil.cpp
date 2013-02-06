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

#include "ContextMenuUtil.h"
#include "ContextMenuContants.h"

#include <fstream>

using namespace std;

ContextMenuUtil::ContextMenuUtil()
{
	_communicationSocket = new CommunicationSocket(PORT);
	_selectedFiles = new std::list<const wchar_t*>;
}

ContextMenuUtil::~ContextMenuUtil(void)
{
	delete _communicationSocket;
	_selectedFiles = 0;
}

bool ContextMenuUtil::AddFile(const wchar_t* file)
{
	_selectedFiles->push_front(file);
	return true;
}

int ContextMenuUtil::GetActionIndex(const wchar_t* command)
{
	list<const wchar_t*>::iterator menuIterator = _menuList->begin();

	int index = 0;

	while(menuIterator != _menuList->end()) 
	{
		const wchar_t* menuName = *menuIterator;

		if(menuName == command)
		{
			return index;
		}

		menuIterator++;
		index++;
	}

	return -1;
}

bool ContextMenuUtil::GetHelpText(int index, wstring& helpText)
{
	/*if(index >= _helpTextList->size())
	{
		return false;
	}

	helpText = _helpTextList[index];
	*/
	return true;
}

bool ContextMenuUtil::GetMenus(std::list<std::wstring*> &menuList)
{
	//menuList = *_menuList;
	return false;
}

bool ContextMenuUtil::GetRootText(wstring& helpText)
{
	//helpText = *_rootMenu;

	return false;
}

bool ContextMenuUtil::GetVerbText(int index, wstring& verbText)
{
	return _GetCommandText(index, verbText);
}

bool ContextMenuUtil::IsMenuNeeded(void)
{  
	if(_menuList->size() == 0)
	{
		return false;
	}

	return true;
}

bool ContextMenuUtil::PerformAction(int index)
{
	wstring command;
	if(!_GetCommandText(index, command))
	{
		return false;
	}

	if(!_GenerateMessage(PERFORM_ACTION, command))
	{
		return false;
	}

	return true;
}

bool ContextMenuUtil::_GetCommandText(int index, wstring& commandText)
{
	if(index < _selectedFiles->size())
	{
		return false;
	}

	list<const wchar_t*>::iterator commandIterator = _selectedFiles->begin();

    std::advance(commandIterator, index);

	commandText = *commandIterator;

	return true;
}

bool ContextMenuUtil::_GenerateMessage(int command, std::wstring& message)
{
	//{cmd=1,args=["args","arg2","arg3"]}
	message.append(OPEN_BRACE);
	message.append(CMD);
	message.append(EQUAL_SIGN);

	/*wstring str_cmd = to_wstring(command);
	message.append(str_cmd);*/

	message.append(COMMA);
	message.append(ARGS);
	message.append(EQUAL_SIGN);
	message.append(OPEN_BRACKET);

	list<const wchar_t*>::iterator fileIterator = _selectedFiles->begin();

	int index = 0;

	while(fileIterator != _selectedFiles->end()) 
	{
		if(index > 0)
		{
			message.append(COMMA);
		}

		message.append(QUOTE);
		message.append(*fileIterator);
		message.append(QUOTE);

		fileIterator++;
		index++;
	}

	message.append(CLOSE_BRACKET);
	message.append(CLOSE_BRACE);

	return true;
}

bool ContextMenuUtil::_InitMenus()
{
	/*wstring getRootMessage;
	if(!_GenerateMessage(GET_ROOT_TEXT, getRootMessage))
	{
		return false;
	}

	wstring getRootReceived;
	if(!_communicationSocket->SendMessageReceiveResponse(getRootMessage.c_str(), getRootReceived))
	{
		return false;
	}

	if(!_ParseMessage(getRootReceived.c_str(), _rootMenu))
	{
		return false;
	}

	wstring getMenuMessage;
	if(!_GenerateMessage(GET_MENU_LIST, getMenuMessage))
	{
		return false;
	}

	wstring getMenuReceived;
	if(!_communicationSocket->SendMessageReceiveResponse(getMenuMessage.c_str(), getMenuReceived))
	{
		return false;
	}

	_menuList = new std::list<const wchar_t*>();

	if(!_ParseMessageToList(getMenuReceived.c_str(), *_menuList))
	{
		return false;
	}

	wstring getHelpMessage;
	if(!_GenerateMessage(GET_HELP_TEXT, getHelpMessage))
	{
		return false;
	}

	wstring getHelpReceived;
	if(!_communicationSocket->SendMessageReceiveResponse(getHelpMessage.c_str(), getHelpReceived))
	{
		return false;
	}

	_helpTextList = new std::list<const wchar_t*>();

	if(!_ParseMessageToList(getHelpReceived.c_str(), *_helpTextList))
	{
		return false;
	}

	return true;*/

	return false;
}

bool ContextMenuUtil::_ParseMessage(const wchar_t*, std::wstring&)
{
	return false;
}

bool ContextMenuUtil::_ParseMessageToList(const wchar_t*, std::list<std::wstring*>&)
{
	return false;
}
