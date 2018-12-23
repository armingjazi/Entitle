package com.entitle.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class RegistrationJob implements IJob
{
    InputStream inputStream_;

    final
    @Override
    public void accomplish(Socket socket)
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
