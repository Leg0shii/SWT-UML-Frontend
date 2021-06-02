package de.swt.rmi;

import de.swt.Server;
import de.swt.database.DBManager;
import de.swt.logic.course.Course;
import de.swt.logic.course.CourseManager;
import de.swt.logic.group.Group;
import de.swt.logic.group.GroupManager;
import de.swt.logic.session.Session;
import de.swt.logic.session.SessionManager;
import de.swt.logic.user.User;
import de.swt.logic.user.UserManager;
import de.swt.manager.ServerCommandManager;
import de.swt.manager.UserCommandMananger;
import de.swt.manager.CommandObject;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {

    public int port;
    private final ServerCommandManager serverCommandManager;
    private final UserCommandMananger userCommandMananger;

    public RMIServer() throws RemoteException {
        this.port = 1099;
        this.serverCommandManager = Server.getInstance().getServerCommandManager();
        this.userCommandMananger = Server.getInstance().getUserCommandMananger();
    }

    @Override
    public ArrayList<CommandObject> accessCommandQueue(int userid) throws RemoteException {
        HashMap<Integer, ArrayList<CommandObject>> userCommandQueue = userCommandMananger.getUserCommandQueue();
        ArrayList<CommandObject> userCommands = userCommandQueue.get(userid);

        // reset command list
        userCommandQueue.remove(userid);
        userCommandQueue.put(userid, new ArrayList<>());
        return userCommands;
    }

    @Override
    public void updateUser(User user) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("UU:");
        serverCommand.setUpdatedObject(user);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    @Override
    public void updateCourse(Course course) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("CU:");
        serverCommand.setUpdatedObject(course);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    @Override
    public void updateGroup(Group group) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("GU:");
        serverCommand.setUpdatedObject(group);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    @Override
    public void updateSession(Session session) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("SU:");
        serverCommand.setUpdatedObject(session);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    @Override
    public void updateWorkspaceFile(byte[] bytes, int originId) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("FU:");
        serverCommand.setOriginId(originId);
        serverCommand.setWorkspaceFileBytes(bytes);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    @Override
    public void sendRequest(int originId, int destinationId) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setOriginId(originId);
        serverCommand.setCommand("RE:" + destinationId);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    @Override
    public void sendAnswer(int originId, int destinationId, int answer) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setOriginId(originId);
        serverCommand.setCommand("AN:" + destinationId + " " + answer);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    @Override
    public void deleteGroup(int groupId) {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("DG:" + groupId);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    @Override
    public void deleteSession(int sessionId) {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("DS:" + sessionId);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    @Override
    public void sendTask(byte[] workspaceBytes, byte[] taskBytes, int originId) {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setOriginId(originId);
        serverCommand.setWorkspaceFileBytes(workspaceBytes);
        serverCommand.setTaskBytes(taskBytes);
        serverCommand.setCommand("ST:");
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

}

