package com.entitle.server;

import java.net.Socket;

public interface IConnectionFactory
{
    Runnable create(Socket socket);
}
