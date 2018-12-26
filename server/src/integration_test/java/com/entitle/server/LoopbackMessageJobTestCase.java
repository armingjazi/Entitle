package com.entitle.server;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoopbackMessageJobTestCase
{
    private Server server_;
    private Thread serverThread_;

    @Before
    public void setUp()
    {
        LoopbackMessageJob loopbackMessage_Job_ = new LoopbackMessageJob();
        ConnectionFactory connectionFactory_ = new ConnectionFactory(loopbackMessage_Job_);

        server_ = new Server(3000, connectionFactory_);
        serverThread_ = new Thread(server_);
        serverThread_.start();
    }

    @After
    public void tearDown()
    {
        try
        {
            server_.stop();
            serverThread_.join();
        } catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void connect_to_server() throws IOException, ClassNotFoundException
    {
        Socket clientSocket = new Socket("localhost", 3000);
        String expectedMessage = "expectedMessage";

        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

        out.writeObject(expectedMessage);
        String response = (String) in.readObject();

        clientSocket.close();

        Assert.assertEquals("message received:" + expectedMessage, response);
    }
}