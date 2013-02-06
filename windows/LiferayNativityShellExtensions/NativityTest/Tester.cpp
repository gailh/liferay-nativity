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

#include "Tester.h"
#include "TestData.h"

#include "ContextMenuUtil.h"
#include "CommunicationSocket.h"
#include <iostream>

using namespace std;

Tester::Tester()
{
}

Tester::~Tester()
{
}

bool Tester::TestNativityUtil()
{
	cout<<"Beginning test nativity util"<<endl;
	
	CommunicationSocket* fakePlugInToServer = new CommunicationSocket(SERVER_TO_JAVA_RECEIVE_SOCKET);
	CommunicationSocket* fakeServerToPlugIn = new CommunicationSocket(SERVER_TO_JAVA_SEND_SOCKET);
	CommunicationSocket* fakeContextMenu = new CommunicationSocket(EXTENSION_TO_SERVER_SOCKET);
	
	
	wstring* response = new wstring();

	for(int i = 0; i < 100000; i++)
	{
		wstring* message = new wstring();
		if(fakePlugInToServer->ReceiveResponseOnly(*message))
		{
			wcout<<"Received Message "<<*message<<endl;
		}
		else
		{
			wcout<<"Unable to receive message"<<endl;
		}
		delete message;

		/*if(fakeServerToPlugIn->SendMessageOnly(L"Blah"))
		{
			wcout<<"Sent message to plug in"<<endl;
		}
		else
		{
			wcout<<"Unable to send message"<<endl;
		}

		if(fakeContextMenu->SendMessageReceiveResponse(L"Blah", *response))
		{
			wcout<<"Sent and received response "<<*response<<endl;
		}
		else
		{
			wcout<<"Unable to send and receive response"<<endl;
		}*/

		::Sleep(2000);
	}

	delete fakeContextMenu;
	delete fakePlugInToServer;
	delete fakePlugInToServer;

	return true;
}

bool Tester::TestContextMenuUtil()
{
	cout<<"Beginning test context menu util"<<endl;
	ContextMenuUtil contextMenuUtil;

    if(!contextMenuUtil.AddFile(IN_FILE_1))
	{
		cout<<"Unable to add file"<<endl;
		return false;
	}


	/*int GetActionIndex(const wchar_t*);

	bool GetHelpText(int, std::wstring&);

	bool GetMenus(std::list<std::wstring*>&);

	bool GetRootText(std::wstring&);

	bool GetVerbText(int, std::wstring&);

	bool IsMenuNeeded(void);
	
	bool PerformAction(int);*/

}
 