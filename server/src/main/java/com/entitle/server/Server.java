package com.entitle.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

class Server implements IServer
{
    private ServerSocket serverSocket_;
    private IConnectionFactory connectionFactory_;
    private AtomicBoolean running_;

    Server(int port, IConnectionFactory serverConnectionFactory)
    {
        connectionFactory_ = serverConnectionFactory;

        try
        {
            serverSocket_ = new ServerSocket(port);
            running_ = new AtomicBoolean(true);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        while(running_.get())
        {
            IServerConnection connection = null;
            try
            {
                connection = connectionFactory_.create(serverSocket_);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            connection.start();
        }
    }

    void stop() throws IOException
    {
        serverSocket_.close();
        running_.set(false);
    }
}
