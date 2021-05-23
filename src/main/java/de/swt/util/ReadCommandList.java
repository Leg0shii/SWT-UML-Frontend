package de.swt.util;

import de.swt.logic.user.User;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.TimerTask;

public class ReadCommandList extends TimerTask {

    private Client client;

    public ReadCommandList(Client client) {
        this.client = client;
    }

    @Override
    public void run() {

        try {
            if(client.loggedIn) {
                ArrayList<String> commands = client.server.accessCommandQueue(client.userid);
                for (String command : commands) evaluteCommand(command);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void evaluteCommand(String command) {

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
                    System.out.println("Updated incoming userchange from succ login from " + user.getFullName());
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
                return;
            default:
                return;
        }
    }

}
