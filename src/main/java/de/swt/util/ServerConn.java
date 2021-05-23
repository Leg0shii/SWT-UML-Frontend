package de.swt.util;

import de.swt.manager.CommandManager;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ServerConn {

    private final int serverPort;
    private ServerSocket serverSocket;
    private byte[] byteMessage;

    private CommandManager adressManager;

    public ServerConn(CommandManager adressManager, int serverPort) throws IOException {
        this.serverSocket = new ServerSocket(serverPort);
        this.byteMessage = new byte[1000];
        this.serverPort = serverPort;
        this.adressManager = adressManager;
    }

    public void sendBroadcastPingMessage(String message) {

        /* for(int key : adressManager.getIpHashMap().keySet()) {
            try {
                InetAddress addr = InetAddress.getByName(adressManager.getIpHashMap().get(key));
                System.out.println("TO ADR: " + addr.getHostAddress());

                // socket the clients connect to to recieve the outputstream from
                Socket sock = new Socket(addr, 21);

                byte bytePacket[] = convertToBytePacket(message);
                OutputStream out = sock.getOutputStream();
                DataOutputStream dos = new DataOutputStream(out);

                dos.write(bytePacket, 0, bytePacket.length);
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } */
    }

    private byte[] convertToBytePacket(String message) {

        byte[] byteMessage = message.getBytes(Charset.defaultCharset());
        int messageLength = byteMessage.length;

        // sets length of message into two byte array
        byte[] bytesLength = new byte[2];
        bytesLength[0] = (byte) (messageLength & 0xFF);
        bytesLength[1] = (byte) ((messageLength >> 8) & 0xFF);

        // converts message into bytes
        byte[] bytePacket = new byte[2 + messageLength];
        System.arraycopy(bytesLength, 0, bytePacket,0, bytesLength.length);
        System.arraycopy(byteMessage, 0, bytePacket, bytesLength.length, byteMessage.length);
        return bytePacket;
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
        evaluateCommand(dataString);
    }

    private void evaluateCommand(String command) {
        String[] keyArgs = command.split(":");
        String[] args = keyArgs[1].split(" ");

        switch (keyArgs[0]) {
            case "LOGIN":

                return;
            case "LOGOUT":

                return;
            default:
                return;
        }
    }

    // for streaming screen later!!!
    public void listenForUDP() throws IOException {
        byte[] buffer = new byte[65507];
        DatagramSocket serverSocketUDP = new DatagramSocket(serverPort);
        System.out.println("UDP started!");
        while (true) {
            DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
            serverSocketUDP.receive(datagramPacket);

            // "KEY:value1 value2 value3"
            // For example: "LOGIN:cliendID"
            String command = new String(datagramPacket.getData());
            evaluateCommand(command);

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
