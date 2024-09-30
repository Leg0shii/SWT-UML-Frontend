package de.swt.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class InitRMIServer {

    /**
     * Initializes the RMI server so that the clients can connect to it.
     */
    public void initRMIServer() {
        System.setProperty("java.security.policy","file:./test.policy");

        try {
            RMIServer server = new RMIServer();
            LocateRegistry.createRegistry(server.port);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("RMIServer", server);
            System.out.println("Registry provided");
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("RMI ERROR");
        }

    }

}
