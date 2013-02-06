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

#ifndef JAVASOCKECLIENT_H
#define JAVASOCKECLIENT_H

#pragma once

#pragma warning (disable : 4251)

#include "stdafx.h"

class SendSocketClient
{
public:
	SendSocketClient();

	~SendSocketClient();

	bool SendMessageReceiveResponse(const wchar_t* message, std::wstring& response);
	
private:
	bool Close();

	bool Initialize();
	
	bool ReceiveResponse(std::wstring &message);

	bool SendMessage(const wchar_t *message);

	SOCKET _clientSocket;
};

#endif