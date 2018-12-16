package com.entitle.server;

import java.net.Socket;

public class ConnectionFactory implements IConnectionFactory
{
    private final IJob job_;

    public ConnectionFactory(IJob job)
    {
        job_ = job;
    }

    @Override
    public Runnable create(Socket socket)
    {
        return new ServerConnection(socket, job_);
    }
}
