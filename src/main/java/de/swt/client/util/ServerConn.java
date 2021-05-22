package de.swt.client.util;

import de.swt.client.logic.User;

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
        byteMessage[0] = input.readByte();
        byteMessage[1] = input.readByte();
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
        evaluateCommand(dataString);
    }

    private void evaluateCommand(String command) {
        String[] keyArgs = command.split(":");
        String[] args = keyArgs[1].split(" ");

        switch (keyArgs[0]) {
            case "UU":
                int clientID;
                try { clientID = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignored) {
                    System.out.println("CLIENT ID IS NOT A NUMBER");
                    return;
                }
                try {
                    client.userManager.getUserHashMap().remove(clientID);
                    User user = client.server.sendUser(null, clientID, false);
                    client.userManager.getUserHashMap().put(clientID, user);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
                return;
            case "LOGOUT":

                return;
            default:
                return;
        }
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
