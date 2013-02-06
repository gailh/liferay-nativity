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

#include "CommunicationProcessor.h"
#include "ConfigurationConstants.h"

using namespace std;

CommunicationProcessor::CommunicationProcessor()
	:_displayIcons(false), _displayContextMenus(false), _contextMenuTitle(L"")
{
	cout<<"Created config manager"<<endl;
}

CommunicationProcessor::~CommunicationProcessor()
{
	delete &_contextMenuTitle;
}

bool CommunicationProcessor::IsDisplayIcons()
{
	return _displayIcons;
}

bool CommunicationProcessor::IsDisplayContextMenus()
{
	return _displayContextMenus;
}

const wchar_t* CommunicationProcessor::GetContextMenuTitle()
{
	return _contextMenuTitle;
}

bool CommunicationProcessor::SetDisplayIcons(bool displayIcons)
{
	_displayIcons = displayIcons;

	return true;
}

bool CommunicationProcessor::SetContextMenuTitle(const wchar_t* contextMenuTitle)
{
	_displayContextMenus = (wcslen(contextMenuTitle) == 0 ? true : false); 

	_contextMenuTitle = contextMenuTitle;

	return true;
}

bool CommunicationProcessor::ProcessMessage(const wchar_t* message)
{
	//{"command":"blah","value":"something"}

	wstring temp(message);

	size_t quote1 = temp.find(QUOTE);
	if(quote1 == string::npos)
	{
		return false;
	}
	
	size_t quote2 = temp.find(QUOTE, quote1 +1);
	if(quote2 == string::npos)
	{
		return false;
	}

	size_t quote3 = temp.find(QUOTE, quote2 +1);
	if(quote3 == string::npos)
	{
		return false;
	}

	size_t quote4 = temp.find(QUOTE, quote3 +1);
	if(quote4 == string::npos)
	{
			return false;
	}

	size_t quote5 = temp.find(QUOTE, quote4 +1);
	if(quote5 == string::npos)
	{
			return false;
	}

	size_t quote6 = temp.find(QUOTE, quote5 +1);
	if(quote6 == string::npos)
	{
			return false;
	}

	//The value can be string, int, or boolean.

	size_t quote7 = temp.find(QUOTE, quote6 +1);
	size_t quote8 = string::npos;

	if(quote7 == string::npos)
	{
		//Not a string 

			quote7 = temp.find(COLON, quote6 +1);
			if(quote7 == string::npos)
			{
				return false;
			}
			else
			{
				quote8 = temp.find(CLOSE_BRACE, quote7 +1);
				if(quote1 == string::npos)
				{
					return false;
				}
			}
	}
	else
	{
		//it is a string

		quote8 = temp.find(QUOTE, quote7 +1);
		if(quote1 == string::npos)
		{
				return false;
		}
	}

	int commandLength = quote4 - quote3 - 1 ;
	int valueLength = quote8 - quote7 - 1;

	cout<<"command length "<<commandLength<<endl;
	cout<<"value length "<<valueLength<<endl;

	wstring command = temp.substr(quote3 + 1, commandLength);
	wstring value = temp.substr(quote7 + 1, valueLength);

	wcout<<"Command "<<command<<endl;
	wcout<<"Value "<<value<<endl;

	if(command.compare(CMD_SET_MENU_TITLE) == 0)
	{
		wcout<<"Setting menu title"<<endl;
		SetContextMenuTitle(value.c_str());
	}
	else if(command.compare(CMD_ENABLE_OVERLAYS) == 0)
	{
		wcout<<"enabling overlays"<<endl;
		if(value.compare(L"1"))
		{
			SetDisplayIcons(true);
		}
		else if(value.compare(L"0"))
		{
			SetDisplayIcons(false);
		}
		else if(value.compare(L"true"))
		{
			SetDisplayIcons(true);
		}
		else if(value.compare(L"false"))
		{
			SetDisplayIcons(false);
		}
		else if(value.compare(L"True"))
		{
			SetDisplayIcons(true);
		}
		else if(value.compare(L"False"))
		{
			SetDisplayIcons(false);
		}
		else if(value.compare(L"TRUE"))
		{
			SetDisplayIcons(true);
		}
		else if(value.compare(L"FALSE"))
		{
			SetDisplayIcons(false);
		}
	}
}