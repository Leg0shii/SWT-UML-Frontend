package de.swt.util;

import de.swt.gui.GUIManager;
import de.swt.logic.session.Session;
import de.swt.logic.user.User;
import de.swt.manager.CommandObject;

import javax.swing.*;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class ReadCommandList extends TimerTask {

    private final Client client;
    private final GUIManager guiManager;

    public ReadCommandList() {
        this.client = Client.getInstance();
        this.guiManager = client.getGuiManager();
    }

    @Override
    public void run() {
        try {
            LinkedBlockingQueue<CommandObject> commands = client.getServer().accessCommandQueue(client.getUserId());
            if (commands != null) {
                for (CommandObject command : commands) evaluteCommand(command);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void evaluteCommand(CommandObject command) {

        int originId = command.getOriginId();
        byte[] workspaceBytes = command.getWorkspaceFileBytes();
        byte[] taskBytes = command.getTaskBytes();
        String[] keyArgs = command.getCommand().split(":");
        String[] args = new String[0];
        if (keyArgs.length > 1) {
            args = keyArgs[1].split(" ");
        }

        switch (keyArgs[0]) {
            case "FU" -> {
                try {
                    guiManager.syncWorkspace(workspaceBytes);
                    System.out.println("[FU] Syncing Workspace");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case "RE" -> {
                int studentId;
                try {
                    studentId = Integer.parseInt(args[0]);
                    User user = client.getUserManager().load(studentId);
                    guiManager.sendRequest(user);
                    System.out.println("Join Request from " + studentId);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            case "AN" -> {
                int answer;
                try {
                    answer = Integer.parseInt(args[0]);
                    if (answer == 1) {
                        Session session = client.getSessionManager().getSessionFromTeacherId(originId);
                        session.getUserIds().add(client.getUserId());
                        client.getServer().updateSession(session);
                        client.setCurrentSession(session);
                        client.getGuiManager().switchToWorkspaceGUI();
                    } else {
                        switch (client.getGuiManager().getLanguage()) {
                            case ENGLISH:
                                JOptionPane.showMessageDialog(null, "Teacher denied Request!");
                            case GERMAN:
                                JOptionPane.showMessageDialog(null, "Lehrer hat Anfrage abgelehnt!");
                        }
                    }
                    System.out.println("Request Answer received: " + answer);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            case "ST" -> {
                try {
                    client.getGuiManager().clearDrawingPanel();
                    client.getGuiManager().syncWorkspace(workspaceBytes);
                    client.getGuiManager().sendTaskProposition();
                    client.getGuiManager().setTask(Arrays.toString(taskBytes));
                    System.out.println("Task proposition received");
                } catch (Exception ignored) {

                }
            }
        }
    }

}
