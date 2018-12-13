package com.entitle.server;

import java.net.Socket;

public class ServerConnection implements IServerConnection
{
    private Socket socket_;

    public ServerConnection(Socket socket)
    {
        socket_ = socket;
    }

    @Override
    public void run()
    {
    }
}
