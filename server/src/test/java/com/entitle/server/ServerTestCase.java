package com.entitle.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerTestCase
{
    private Server server_;
    private IConnectionFactory connectionFactory_;
    private ISocketGate socketGate_;

    @Before
    public void setUp()
    {
        connectionFactory_ = mock(IConnectionFactory.class);
        socketGate_ = mock(ISocketGate.class);

        server_ = new Server(3000,  connectionFactory_, socketGate_);
    }

    @After
    public void tearDown()
    {

    }

    @Test
    public void server_receives_new_connections() throws IOException, InterruptedException
    {
        AtomicInteger connections = new AtomicInteger(0);
        // wait at least for ten connection
        CountDownLatch countDownLatch = new CountDownLatch(10);

        Socket socket = mock(Socket.class);
        Thread thread = new Thread(server_);

        when(socketGate_.waitAccept(any(ServerSocket.class))).thenAnswer((Answer<Socket>) invocation ->
        {
            countDownLatch.countDown();

            connections.incrementAndGet();

            return socket;
        });

        thread.start();

        countDownLatch.await(1, TimeUnit.SECONDS);

        server_.stop();
        thread.join();

        verify(connectionFactory_, times(connections.get())).create(socket);
    }

    @Test
    public void server_receives_exactly_one_new_connection() throws IOException, InterruptedException
    {
        Socket socket = mock(Socket.class);
        Boolean sleep = true;
        Boolean join = true;

        when(socketGate_.waitAccept(any(ServerSocket.class))).thenAnswer((Answer<Socket>) invocation ->
        {
            synchronized (join)
            {
                join.notify();
            }

            synchronized (sleep)
            {
                sleep.wait();
            }

            return socket;
        });

        Thread thread = new Thread(server_);

        thread.start();

        synchronized (join)
        {
            join.wait();
        }

        synchronized (sleep)
        {
            server_.stop();
            sleep.notify();
        }

        thread.join();

        verify(connectionFactory_, times(1)).create(socket);
    }
}