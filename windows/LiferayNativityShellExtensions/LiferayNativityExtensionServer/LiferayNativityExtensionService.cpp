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

#include "LiferayNativityExtensionService.h"
#include "ThreadPool.h"
#include "ConfigurationConstants.h"

using namespace std;

LiferayNativityExtensionService::LiferayNativityExtensionService(
	PWSTR pszServiceName, BOOL fCanStop, BOOL fCanShutdown, 
    BOOL fCanPauseContinue)
: CServiceBase(pszServiceName, fCanStop, fCanShutdown, fCanPauseContinue)
{
	cout<<"Creating service"<<endl;
	
    stopped = FALSE;

	cout<<"Creating stop event"<<endl;

    stoppedEvent = CreateEvent(NULL, TRUE, FALSE, NULL);
    
	cout<<"Done creating stop event"<<endl;

	if (stoppedEvent == NULL)
    {
		cout<<"Failed to create event"<<endl;
        throw GetLastError();
    }

	_receiveSocketClient = new ReceiveSocketClient();
	_sendSocketClient = new SendSocketClient();
	_communicationProcessor = new CommunicationProcessor();
}

LiferayNativityExtensionService::~LiferayNativityExtensionService(void)
{
    if (stoppedEvent != NULL)
    {
        CloseHandle(stoppedEvent);
        stoppedEvent = NULL;
    }
}

void LiferayNativityExtensionService::OnStart(DWORD dwArgc, LPWSTR *lpszArgv)
{
    WriteEventLogEntry(
		L"Liferay Nativity Service in OnStart", EVENTLOG_INFORMATION_TYPE);

    CThreadPool::QueueUserWorkItem(
		&LiferayNativityExtensionService::ServiceWorkerThread, this);
}

void LiferayNativityExtensionService::ServiceWorkerThread(void)
{
	cout<<"Staring service worker thread"<<endl;
	
	WriteEventLogEntry(
		L"Liferay Nativity Extension Service worker thread", 
		EVENTLOG_INFORMATION_TYPE);

	bool receivedMessage;
	wstring command;
	wstring clientMessage;
	wstring response;

    while (!stopped)
	{
		cout<<"Loop"<<endl;

		if(_receiveSocketClient->ReceiveMessage(command))
		{
			_communicationProcessor->ProcessMessage(command.c_str());
			command.clear();
		}

		if(!stopped) 
		{
			if(_sendSocketClient->SendMessageReceiveResponse(clientMessage.c_str(), response))
			{
				_communicationProcessor->ProcessCommunication(clientMessage.c_str(), response.c_str());
			}
		}
    }

	WriteEventLogEntry(
		L"Liferay Nativity Extension Service worker thread stopping", 
		EVENTLOG_INFORMATION_TYPE);

    SetEvent(stoppedEvent);
}

void LiferayNativityExtensionService::Test()
{
	cout<<"Staring to test worker thread"<<endl;
	ServiceWorkerThread();
}

//   Be sure to periodically call ReportServiceStatus() with 
//   SERVICE_STOP_PENDING if the procedure is going to take long time. 
//
void LiferayNativityExtensionService::OnStop()
{
    WriteEventLogEntry(
		L"Liferay Nativity Extension Service in OnStop", 
		EVENTLOG_INFORMATION_TYPE);
   
	stopped = TRUE;
    
	if (WaitForSingleObject(stoppedEvent, INFINITE) != WAIT_OBJECT_0)
    {
        throw GetLastError();
    }
}