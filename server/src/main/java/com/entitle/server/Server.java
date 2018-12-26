package com.entitle.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
            IServerConnection connection;
            try
            {
                connection = connectionFactory_.create(serverSocket_);
                connection.start();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    void stop() throws IOException
    {
        running_.set(false);

        // this is known as poison pill, to stop the other thread from waiting on new connection
        new Socket("localhost", 3000);

        serverSocket_.close();
    }
}
