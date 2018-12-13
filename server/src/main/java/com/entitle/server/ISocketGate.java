package com.entitle.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public interface ISocketGate
{
    Socket waitAccept(ServerSocket serverSocket) throws IOException;
}
