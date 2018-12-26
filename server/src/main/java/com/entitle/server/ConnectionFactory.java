package com.entitle.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionFactory implements IConnectionFactory
{
    private final IJob job_;

    ConnectionFactory(IJob job)
    {
        job_ = job;
    }

    @Override
    public IServerConnection create(ServerSocket serverSocket) throws IOException
    {
        Socket socket = serverSocket.accept();

        return new ServerConnection(socket, job_);
    }
}
