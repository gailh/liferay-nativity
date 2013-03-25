/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

#include "CommunicationProcessor.h"
#include "RegistryUtil.h"
#include "FileUtil.h"
#include "UtilConstants.h"

#include <fstream>
#include <iostream>
#include <vector>

using namespace std;

ContextMenuUtil::ContextMenuUtil() : _helpTextList(0), _menuList(0), _rootMenu(0)
{
	_communicationSocket = new CommunicationSocket(PORT);
	_selectedFiles = new vector<wstring>;
}

ContextMenuUtil::~ContextMenuUtil(void)
{
	if(_communicationSocket != 0)
	{
		delete _communicationSocket;
	}

	if(_helpTextList != 0)
	{
		delete _helpTextList;
	}

	if(_menuList != 0)
	{
		delete _menuList;
	}

	if(_rootMenu != 0)
	{
		delete _rootMenu;
	}

	_selectedFiles = 0;
}

bool ContextMenuUtil::AddFile(wstring* file)
{
	_selectedFiles->push_back(*file);

	return true;
}

int ContextMenuUtil::GetActionIndex(wstring* command)
{
	vector<wstring*>::iterator menuIterator = _menuList->begin();

	int index = 0;

	while(menuIterator != _menuList->end()) 
	{
		wstring* menuName = *menuIterator;

		if(menuName->compare(*command) == 0)
		{
			return index;
		}

		menuIterator++;
		index++;
	}

	return -1;
}

bool ContextMenuUtil::GetHelpText(unsigned int index, wstring* helpText)
{
	if(_helpTextList == 0)
	{
		return false;
	}

	if(index >= _helpTextList->size())
	{
		return false;
	}

	helpText = _helpTextList->at(index);
	return true;
}

bool ContextMenuUtil::GetMenus(vector<std::wstring*> *menuList)
{
	if(_menuList == 0)
	{
		return false;
	}

	*menuList = *_menuList;
	return true;
}

bool ContextMenuUtil::GetRootText(wstring* rootText)
{
	if(_rootMenu == 0)
	{
		return false;
	}

	*rootText = *_rootMenu;
	return true;
}

bool ContextMenuUtil::GetVerbText(int index, wstring& verbText)
{
	return _GetCommandText(index, verbText);
}

bool ContextMenuUtil::IsMenuNeeded(void)
{  
	bool needed = false;
	
	if(FileUtil::IsChildFileOfRoot(_selectedFiles))
	{
		needed = true;
	}

	return needed;
}

bool ContextMenuUtil::InitMenus(void)
{
	_rootMenu = new wstring();

	if(!RegistryUtil::ReadRegistry(REGISTRY_ROOT_KEY, REGISTRY_MENU_TITLE, _rootMenu))
	{
		return false;
	}

	if(!_GetMenuList())
	{
		return false;
	}

	if(!_GetHelpText())
	{
		return false;
	}

	return true;
}

bool ContextMenuUtil::PerformAction(int index)
{
	wstring* command = new wstring();
	wstring* response = new wstring();
	bool success = false;

	if(_GenerateMessage(PERFORM_ACTION, index, command))
	{
		if(_communicationSocket->SendMessageReceiveResponse(command->c_str(), response))
		{
			success = true;
		}
	}

	delete response;
	delete command;
	return success;
}

bool ContextMenuUtil::_GetMenuList(void)
{
	wstring* getMenuMessage = new wstring();
	wstring* getMenuReceived = new wstring();
	_menuList = new vector<wstring*>();

	bool success = false;

	if(_GenerateMessage(GET_MENU_LIST, -1, getMenuMessage))
	{
		if(_communicationSocket->SendMessageReceiveResponse(getMenuMessage->c_str(), getMenuReceived))
		{
			if(CommunicationProcessor::ProcessResponse(getMenuReceived, _menuList))
			{
				success = true;
			}
		}
	}

	delete getMenuMessage;
	delete getMenuReceived;

	return success;
}

bool ContextMenuUtil::_GetCommandText(unsigned int index, wstring& commandText)
{
	if(index < _selectedFiles->size())
	{
		return false;
	}

	vector<wstring>::iterator commandIterator = _selectedFiles->begin();

    std::advance(commandIterator, index);

	commandText = *commandIterator;

	return true;
}

bool ContextMenuUtil::_GetHelpText(void)
{
	wstring* getHelpMessage = new wstring();
	wstring* getHelpReceived = new wstring();
	_helpTextList = new vector<wstring*>();

	bool success = false;

	if(_GenerateMessage(GET_HELP_ITEMS, -1, getHelpMessage))
	{
		if(_communicationSocket->SendMessageReceiveResponse(getHelpMessage->c_str(), getHelpReceived))
		{
			if(CommunicationProcessor::ProcessResponse(getHelpReceived, _helpTextList))
			{
				success = true;
			}
		}
	}

	delete getHelpMessage;
	delete getHelpReceived;

	return success;
}

bool ContextMenuUtil::_GenerateMessage(const wchar_t* command, int cmdIndex, wstring* message)
{
	//{cmd:1,args:["args","arg2","arg3"]}
	message->append(OPEN_CURLY_BRACE);
	message->append(QUOTE);
	message->append(COMMAND);
	message->append(QUOTE);
	message->append(COLON);

	message->append(QUOTE);
	message->append(command);
	message->append(QUOTE);

	message->append(COMMA);
	message->append(QUOTE);
	message->append(ARGS);
	message->append(QUOTE);
	message->append(COLON);
	message->append(OPEN_BRACE);

	if(cmdIndex >= 0){
		wchar_t* buf =  new wchar_t(10);

		_itow_s(cmdIndex, buf, 10, 10);

		message->append(QUOTE);
		message->append(buf);
		message->append(QUOTE);
		message->append(COMMA);
	}

	vector<wstring>::iterator fileIterator = _selectedFiles->begin();

	int index = 0;

	while(fileIterator != _selectedFiles->end()) 
	{
		if(index > 0)
		{
			message->append(COMMA);
		}

		message->append(QUOTE);

		wstring file = *fileIterator;

		message->append(file.c_str());
		message->append(QUOTE);

		fileIterator++;
		index++;
	}

	message->append(CLOSE_BRACE);
	message->append(CLOSE_CURLY_BRACE);

	return true;
}
