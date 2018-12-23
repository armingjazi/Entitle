package com.entitle.server;

import java.io.IOException;
import java.net.ServerSocket;

public interface IConnectionFactory
{
    IServerConnection create(ServerSocket serverSocket) throws IOException;
}
