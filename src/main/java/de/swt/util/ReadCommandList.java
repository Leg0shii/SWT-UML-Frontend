package de.swt.util;

import de.swt.logic.course.Course;
import de.swt.logic.group.Group;
import de.swt.logic.session.Session;
import de.swt.logic.user.User;
import de.swt.manager.CommandObject;

import javax.swing.*;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimerTask;

public class ReadCommandList extends TimerTask {

    private Client client;

    public ReadCommandList(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            ArrayList<CommandObject> commands = client.server.accessCommandQueue(client.userid);
            if (commands != null) {
                for (CommandObject command : commands) evaluteCommand(command);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void evaluteCommand(CommandObject command) {

        String[] keyArgs = command.getCommand().split(":");
        String[] args = new String[0];
        if (keyArgs.length > 1) {
            args = keyArgs[1].split(" ");
        }

        switch (keyArgs[0]) {
            case "UU":
                int clientID;
                try {
                    clientID = Integer.parseInt(args[0]);
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
                client.guiManager.updateGUIS();
                break;
            case "CU":
                int courseID;
                try {
                    courseID = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignored) {
                    System.out.println("COURSE ID IS NOT A NUMBER");
                    return;
                }
                client.guiManager.updateGUIS();
                try {
                    client.courseManager.getCourseHashMap().remove(courseID);
                    Course course = client.server.sendCourse(null, courseID, false);
                    client.courseManager.getCourseHashMap().put(courseID, course);
                    System.out.println("Updated incoming courseChange. Updated following courseID: " + course.getId());
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
                client.guiManager.updateGUIS();
                break;
            case "GU":
                int groupID;
                try {
                    groupID = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignored) {
                    System.out.println("GROUP ID IS NOT A NUMBER");
                    return;
                }
                try {
                    client.groupManager.getGroupHashMap().remove(groupID);
                    Group group = client.server.sendGroup(null, groupID, false);
                    client.groupManager.getGroupHashMap().put(groupID, group);
                    client.guiManager.updateGUIS();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
                client.guiManager.updateGUIS();
                System.out.println("Updated incoming groupChange. Updated following groupID: " + groupID);
                break;
            case "SU":
                int sessionID;
                try {
                    sessionID = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignored) {
                    System.out.println("SESSION ID IS NOT A NUMBER");
                    return;
                }
                try {
                    client.sessionManager.getSessionHashMap().remove(sessionID);
                    Session session = client.server.sendSession(null, sessionID, false);
                    client.sessionManager.getSessionHashMap().put(sessionID, session);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
                client.guiManager.updateGUIS();
                System.out.println("Updated incoming sessionChange. Updated following sessionID: " + sessionID);
                break;
            case "FU":
                byte[] filebytes = command.getWorkspaceFileBytes();
                try {
                    client.guiManager.syncWorkspace(filebytes);
                    System.out.println("Updated incoming WorkspaceChange. Updated some Objects");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                client.guiManager.updateGUIS();
                break;
            case "RE":
                int originId;
                try {
                    originId = Integer.parseInt(args[0]);
                    User user = client.userManager.loadUser(originId);
                    client.guiManager.workspaceGUI.sendRequest(user);
                    System.out.println("Received incoming Join Request from "+originId);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                client.guiManager.updateGUIS();
                break;
            case "AN":
                int answer;
                int teacherId;
                try {
                    answer = Integer.parseInt(args[1]);
                    teacherId = Integer.parseInt(args[2]);
                    System.out.println("Received incoming Join Answer from "+teacherId);
                    if (answer == 1) {
                        Session session = client.sessionManager.getSessionFromTeacherId(teacherId);
                        session.getParticipants().add(client.userid);
                        client.server.sendSession(session, -1, true);
                        client.guiManager.currentSession = session;
                        client.guiManager.switchToWorkspaceGUI();
                    } else {
                        switch (client.guiManager.language) {
                            case ENGLISH:
                                JOptionPane.showMessageDialog(null, "Teacher denied Request!");
                            case GERMAN:
                                JOptionPane.showMessageDialog(null, "Lehrer hat Anfrage abgelehnt!");
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                client.guiManager.updateGUIS();
                break;
            case "DG":
                int groupId = -1;
                try {
                    groupId = Integer.parseInt(args[0]);
                    client.guiManager.currentGroup = null;
                    client.groupManager.getGroupHashMap().remove(groupId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Received incoming Delete Group, Deleted "+groupId);
                client.guiManager.updateGUIS();
                break;
            case "DS":
                int sessionId;
                try {
                    sessionId = Integer.parseInt(args[0]);
                    System.out.println("Received incoming Delete Session, Deleted "+sessionId);
                    client.guiManager.currentSession = null;
                    client.sessionManager.getSessionHashMap().remove(sessionId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                client.guiManager.updateGUIS();
                break;
            case "ST":
                int teacherId2 = -1;
                byte[] workspaceBytes = command.getWorkspaceFileBytes();
                byte[] taskBytes = command.getTaskBytes();
                try {
                    teacherId2 = Integer.parseInt(args[0]);
                    while (client.guiManager.workspaceGUI.removeLastDrawnObject()){}
                    while (client.guiManager.workspaceGUI.removeLastAnnotations()){}
                    client.guiManager.syncWorkspace(workspaceBytes);
                    client.guiManager.workspaceGUI.sendTaskProposition();
                    client.guiManager.workspaceGUI.setTask(Arrays.toString(taskBytes));
                }catch (Exception ignored){

                }
                System.out.println("Received incoming Send Task from Teacher: "+teacherId2);
                break;
            default:
        }
    }

}
