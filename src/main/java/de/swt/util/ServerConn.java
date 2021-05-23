package de.swt.util;

import de.swt.logic.user.User;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.rmi.RemoteException;

public class ServerConn {

    private final int serverPort;
    private final ServerSocket serverSocket;
    private final byte[] byteMessage;

    private final Client client;

    public ServerConn(Client client, int serverPort) throws IOException {
        this.client = client;
        this.serverSocket = new ServerSocket(serverPort);
        this.byteMessage = new byte[1000];
        this.serverPort = serverPort;
    }

    public void listenForTCP() throws IOException {

        Socket clientSocket = serverSocket.accept();
        DataInputStream input = new DataInputStream(clientSocket.getInputStream());

        // use 1st and 2nd byte to define length!!!!
        int bytesRead;
        byteMessage[1] = input.readByte();
        byteMessage[0] = input.readByte();
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteMessage, 0 , 2);

        // this is transforms the bytes to read from current 2 bytes into int (big endian)
        int bytesToRead = byteBuffer.getShort();
        boolean end = false;
        String dataString = "";

        // reads until the defined byte length is reached
        while(!end) {
            bytesRead = input.read(byteMessage);
            dataString += new String(byteMessage, 0 , bytesRead);
            if(dataString.length() == bytesToRead) end = true;
        }

        // continue here with evalutating the recieved ping message from Client
        System.out.println("Recieved message: " + dataString);
        //evaluateCommand(dataString);
    }

    public void startServer() {
        Thread tcpListener = new Thread(() -> {
            try {
                //listenForUDP();
                listenForTCP();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        tcpListener.start();
    }

}
