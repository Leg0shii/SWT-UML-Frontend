package de.swt.rmi;

import de.swt.Server;
import de.swt.logic.course.Course;
import de.swt.logic.group.Group;
import de.swt.logic.session.Session;
import de.swt.logic.user.User;
import de.swt.manager.ServerCommandManager;
import de.swt.manager.UserCommandManager;
import de.swt.manager.CommandObject;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {

    public int port;
    private final ServerCommandManager serverCommandManager;
    private final UserCommandManager userCommandMananger;
    private final Server server;

    public RMIServer() throws RemoteException {
        this.port = 1099;
        this.serverCommandManager = Server.getInstance().getServerCommandManager();
        this.userCommandMananger = Server.getInstance().getUserCommandManager();
        this.server = Server.getInstance();
    }

    @Override
    public LinkedBlockingQueue<CommandObject> accessCommandQueue(int userId) throws RemoteException {
        HashMap<Integer, LinkedBlockingQueue<CommandObject>> userCommandQueue = userCommandMananger.getUserCommandQueue();
        LinkedBlockingQueue<CommandObject> userCommands = new LinkedBlockingQueue<>(userCommandQueue.get(userId));

        // reset command list
        userCommandQueue.get(userId).clear();
        return userCommands;
    }

    @Override
    public HashMap<Integer, Course> getCourses() throws RemoteException {
        return server.getCourseManager().getHashMap();
    }

    @Override
    public HashMap<Integer, User> getUsers() throws RemoteException {
        return server.getUserManager().getHashMap();
    }

    @Override
    public HashMap<Integer, Group> getGroups() throws RemoteException {
        return server.getGroupManager().getHashMap();
    }

    @Override
    public HashMap<Integer, Session> getSessions() throws RemoteException {
        return server.getSessionManager().getHashMap();
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
    public void sendWorkspaceStateSwitch(int originId,String workspaceState) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setOriginId(originId);
        serverCommand.setCommand("WU:"+workspaceState);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    @Override
    public void deleteObject(int originId,int[] id) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setOriginId(originId);
        serverCommand.setCommand("DO:"+id[0]+" "+id[1]);
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
    public void deleteCourse(int courseId) {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("DC:" + courseId);
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

