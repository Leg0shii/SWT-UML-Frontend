package de.swt.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class InitRMIServer {

    public void initRMIServer() {
        System.setProperty("java.security.policy","file:./test.policy");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

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
