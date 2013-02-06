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

#include "ReceiveSocketClient.h"
#include "ConfigurationConstants.h"

using namespace std;

#define DEFAULT_BUFLEN 4096

ReceiveSocketClient::ReceiveSocketClient()
{
}

ReceiveSocketClient::~ReceiveSocketClient()
{
}

bool ReceiveSocketClient::Close()
{
	HRESULT result = shutdown(_clientSocket, SD_SEND);
   
	if (result != SOCKET_ERROR) {
        closesocket(_clientSocket);
        
		WSACleanup();
    
		return true;
    }

	return false;
}

bool ReceiveSocketClient::Initialize()
{
	cout<<"Initializing socket"<<endl;

	_clientSocket = INVALID_SOCKET;
		
	int iResult;

	WSADATA wsaData;
	
	struct sockaddr_in clientService;
	
    iResult = WSAStartup(MAKEWORD(2,2), &wsaData);

    if (iResult != NO_ERROR) 
	{
		cout<<"Unable to start up wsa"<<endl;
	    return false;
    }

	_clientSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

	if (_clientSocket == INVALID_SOCKET) 
	{
		cout<<"Invalid socket"<<endl;

		WSACleanup();
		return false;
	}

	clientService.sin_family = AF_INET;
	clientService.sin_addr.s_addr = inet_addr(SOCKET_ADDRESS);
	clientService.sin_port = htons(SOCKET_PORT_RECEIVE);

	iResult = connect( _clientSocket, (SOCKADDR*) &clientService, sizeof(clientService) );

	if (iResult == SOCKET_ERROR) 
	{
		cout<<"Failed to connect"<<endl;

		closesocket(_clientSocket);
		WSACleanup();
		return false;
	}

	cout<<"Finished Init"<<endl;

	return true;
}


bool ReceiveSocketClient::ReceiveMessage(wstring& message)
{
	cout<<"Going to initilaize"<<endl;
	
	Initialize();
	
	cout<<"Done initilaize"<<endl;

	char rec_buf[DEFAULT_BUFLEN];

	cout<<"Going to recieve"<<endl;

	int bytesRead = recv(_clientSocket, rec_buf, DEFAULT_BUFLEN, MSG_WAITALL);

	if(bytesRead <= 0)
	{
		cout<<"Didn't receive data "<<bytesRead<<endl;
		return false;
	}

	cout<<"Done receive bytes"<<bytesRead<<endl;
	
	wchar_t* buf = new wchar_t[ bytesRead/2 ];
	
	int value;

	int j = 0;

	for(int i = 0; i < bytesRead; i+=2)
	{
		value = rec_buf[i]<<rec_buf[i+1];

		buf[j] = btowc(value);

		j++;
	}

	message.append(buf);

	wcout<<"Finished "<<message<<endl;

	delete[] buf;

    return true;
}
