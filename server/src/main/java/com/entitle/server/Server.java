package com.entitle.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

class Server implements IServer
{
    private ServerSocket serverSocket_;
    private IConnectionFactory connectionFactory_;
    private ISocketGate socketGate_;
    private AtomicBoolean running_;

    Server(int port, IConnectionFactory serverConnectionFactory, ISocketGate socketGate)
    {
        connectionFactory_ = serverConnectionFactory;
        socketGate_ = socketGate;

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
            try (Socket socket = socketGate_.waitAccept(serverSocket_))
            {
                Runnable connection = connectionFactory_.create(socket);
                new Thread(connection).start();

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    void stop() throws IOException
    {
        serverSocket_.close();
        running_.set(false);
    }
}
