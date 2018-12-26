package com.entitle.server;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;

public class ServerTestCase
{
    private Server server_;
    private IConnectionFactory connectionFactory_;

    @Before
    public void setUp()
    {
        connectionFactory_ = mock(IConnectionFactory.class);

        server_ = new Server(3000,  connectionFactory_);
    }

    @After
    public void tearDown()
    {

    }

    @Test
    public void server_receives_new_connections_and_starts_them() throws IOException, InterruptedException
    {
        AtomicInteger connections = new AtomicInteger(0);
        // wait at least for ten connection
        CountDownLatch countDownLatch = new CountDownLatch(10);

        IServerConnection serverConnection = mock(IServerConnection.class);
        Thread thread = new Thread(server_);

        when(connectionFactory_.create(any(ServerSocket.class))).thenAnswer((Answer<IServerConnection>) invocation ->
        {
            countDownLatch.countDown();

            connections.incrementAndGet();

            return serverConnection;
        });

        thread.start();

        countDownLatch.await(1, TimeUnit.SECONDS);

        server_.stop();
        thread.join();

        verify(serverConnection, times(connections.get())).start();
    }

    @Test
    public void server_receives_exactly_one_new_connection() throws IOException, InterruptedException
    {
        IServerConnection serverConnection = mock(IServerConnection.class);
        Boolean sleep = true;
        Boolean join = true;

        when(connectionFactory_.create(any(ServerSocket.class))).thenAnswer((Answer<IServerConnection>) invocation ->
        {
            synchronized (join)
            {
                join.notify();
            }

            synchronized (sleep)
            {
                sleep.wait();
            }

            return serverConnection;
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

        verify(serverConnection, times(1)).start();
    }
}