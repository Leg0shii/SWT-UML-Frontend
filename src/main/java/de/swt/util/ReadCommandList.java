package de.swt.util;

import de.swt.logic.course.Course;
import de.swt.logic.group.Group;
import de.swt.logic.session.Session;
import de.swt.logic.user.User;
import de.swt.manager.CommandObject;

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
            ArrayList<CommandObject> commands = client.server.accessCommandQueue(client.userid);
            if(commands != null) {
                for (CommandObject command : commands) evaluteCommand(command);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void evaluteCommand(CommandObject command) {

        String[] keyArgs = command.getCommand().split(":");
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
                break;
            case "CU":
                int courseID;
                try { courseID = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignored) {
                    System.out.println("COURSE ID IS NOT A NUMBER");
                    return;
                }
                try {
                    client.courseManager.getCourseHashMap().remove(courseID);
                    Course course = client.server.sendCourse(null, courseID, false);
                    client.courseManager.getCourseHashMap().put(courseID, course);
                    System.out.println("Updated incoming courseChange. Updated following courseID: " + course.getId());
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
                break;
            case "GU":
                int groupID;
                try { groupID = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignored) {
                    System.out.println("GROUP ID IS NOT A NUMBER");
                    return;
                }
                try {
                    client.groupManager.getGroupHashMap().remove(groupID);
                    Group group = client.server.sendGroup(null, groupID, false);
                    client.groupManager.getGroupHashMap().put(groupID, group);
                    System.out.println("Updated incoming groupChange. Updated following groupID: " + group.getId());
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
                break;
            case "SU":
                int sessionID;
                try { sessionID = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignored) {
                    System.out.println("SESSION ID IS NOT A NUMBER");
                    return;
                }
                try {
                    client.sessionManager.getSessionHashMap().remove(sessionID);
                    Session session = client.server.sendSession(null, sessionID, false);
                    client.sessionManager.getSessionHashMap().put(sessionID, session);
                    System.out.println("Updated incoming sessionChange. Updated following sessionID: " + session.getId());
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
                break;
            case "FU":

                // save file here
                break;
            default:
        }
    }

}
