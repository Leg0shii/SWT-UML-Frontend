package de.swt.rmi;

import java.nio.charset.StandardCharsets;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class RMIClient {
    //private int clientID;
    private String registryName;
    private String serverAddress;
    private int serverRMIPort;

    public RMIClient() {
        //Constructor
        //this.clientID = this.generateClientID();
        this.registryName = "RMIServer"; //Name in Registry
        this.serverAddress = "185.181.10.193"; //Bennies Server
        this.serverRMIPort = 1099;
    }

    private int generateClientID() {
        /*
         * Funktion soll ClientID erstellen
         * ggf Anfrage an Server auf offene IDs
         * aus Testzwecken 42
         */
        return 42;
    }

    private String hashFunction(String password) {
        MessageDigest digest = null;
        String passwordHash;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }

        if(digest != null) {
            passwordHash = Arrays.toString(digest.digest(password.getBytes(StandardCharsets.UTF_8)));
        } else {
            passwordHash = "ERROR";
            System.out.println("ERROR {Client.java - hashFunction} can't hash with SHA-256");
        }
        return passwordHash;
    }

    public RMIServerInterface initRMIClient() {

        System.setProperty("java.security.policy","file:///policy.policy");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        RMIClient rmiClient = new RMIClient();
        RMIServerInterface server;

        try {
            Registry registry = LocateRegistry.getRegistry(rmiClient.serverAddress, rmiClient.serverRMIPort);
            server = (RMIServerInterface)registry.lookup(rmiClient.registryName);
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
            e.getCause();
            System.out.println("ERROR WHILE INITIALIZING RMI CLIENT!!!");
            return null;
        }
        return server;
    }
}

