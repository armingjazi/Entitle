package com.entitle.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketGate implements ISocketGate
{
    @Override
    public Socket waitAccept(ServerSocket serverSocket) throws IOException
    {
        return serverSocket.accept();
    }
}
