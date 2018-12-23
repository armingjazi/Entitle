package com.entitle.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import org.mockito.stubbing.Answer;

public class RegistrationJobTestCase
{
    private Server server_;
    private ConnectionFactory connectionFactory_;
    private RegistrationJob registrationJob;

    @Before
    public void setUp()
    {
        registrationJob = new RegistrationJob();
        connectionFactory_ = new ConnectionFactory(registrationJob);

        server_ = new Server(3000,  connectionFactory_);
    }

    @After
    public void tearDown()
    {

    }

    @Test
    public void connect_to_server()
    {
    }
}