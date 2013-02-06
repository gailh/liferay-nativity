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

#include "SendSocketClient.h"
#include "ConfigurationConstants.h"

using namespace std;

#define DEFAULT_BUFLEN 4096

SendSocketClient::SendSocketClient()
{
}

SendSocketClient::~SendSocketClient()
{
}

bool SendSocketClient::Close()
{
	HRESULT result = shutdown(_clientSocket, SD_SEND);
    
	if (result == SOCKET_ERROR) {
        closesocket(_clientSocket);
        WSACleanup();
        return L"";
    }

	return false;
}

bool SendSocketClient::Initialize()
{
	_clientSocket = INVALID_SOCKET;
		
	int iResult;

	WSADATA wsaData;
	
	struct sockaddr_in clientService;
	
    iResult = WSAStartup(MAKEWORD(2,2), &wsaData);

    if (iResult != NO_ERROR) 
	{
	    return false;
    }

	_clientSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

	if (_clientSocket == INVALID_SOCKET) 
	{
		WSACleanup();
		return false;
	}

	clientService.sin_family = AF_INET;
	clientService.sin_addr.s_addr = inet_addr(SOCKET_ADDRESS);
	clientService.sin_port = htons(SOCKET_PORT_SEND);

	iResult = connect( _clientSocket, (SOCKADDR*) &clientService, sizeof(clientService) );

	if (iResult == SOCKET_ERROR) 
	{
		closesocket(_clientSocket);
		WSACleanup();
		return false;
	}

	return true;
}


bool SendSocketClient::ReceiveResponse(wstring& message)
{
	char rec_buf[DEFAULT_BUFLEN];
	int bytesRead = recv(_clientSocket, rec_buf, DEFAULT_BUFLEN, MSG_WAITALL);
	
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

	delete[] buf;

    return true;
}
bool SendSocketClient::SendMessage(const wchar_t* message)
{
    HRESULT result = send( _clientSocket, (char *)message, (int)(wcslen(message) * 2), 0 );

    if (result == SOCKET_ERROR) 
	{
        Close();
        return false;
    }

	result = shutdown(_clientSocket, SD_SEND);

    if (result == SOCKET_ERROR) 
	{
        Close();
        return false;
    }

	return true;
}

bool SendSocketClient::SendMessageReceiveResponse(const wchar_t* message, wstring& response)
{
	return SendMessage(message) && ReceiveResponse(response);
}
