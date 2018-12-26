package com.entitle.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoopbackMessageJob implements IJob
{
    final
    @Override
    public void accomplish(Socket socket)
    {
        ObjectOutputStream out;

        try
        {
            out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            String response = "message received:" + in.readObject();
            out.writeObject(response);

            out.close();
            in.close();

        } catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
