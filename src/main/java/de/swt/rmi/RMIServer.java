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

    /**
     * Initializes the RMI server with the needed classes on port 1099 for the clients.
     * @throws RemoteException
     */
    public RMIServer() throws RemoteException {
        this.port = 1099;
        this.serverCommandManager = Server.getInstance().getServerCommandManager();
        this.userCommandMananger = Server.getInstance().getUserCommandManager();
        this.server = Server.getInstance();
    }

    /**
     * Allows the user to access its command queue based on the clients id.
     * @param userId Id of the client.
     * @return The clients specific command queue with command objects.
     * @throws RemoteException Thrown if there are complications with the RMI server.
     */
    @Override
    public LinkedBlockingQueue<CommandObject> accessCommandQueue(int userId) throws RemoteException {
        HashMap<Integer, LinkedBlockingQueue<CommandObject>> userCommandQueue = userCommandMananger.getUserCommandQueue();
        LinkedBlockingQueue<CommandObject> userCommands = new LinkedBlockingQueue<>(userCommandQueue.get(userId));

        // reset command list
        userCommandQueue.get(userId).clear();
        return userCommands;
    }

    /**
     * @return HashMap of course ids and courses.
     * @throws RemoteException Thrown if there are complications with the RMI server.
     */
    @Override
    public HashMap<Integer, Course> getCourses() throws RemoteException {
        return server.getCourseManager().getHashMap();
    }

    /**
     * @return HashMap of user ids and users.
     * @throws RemoteException Thrown if there are complications with the RMI server.
     */
    @Override
    public HashMap<Integer, User> getUsers() throws RemoteException {
        return server.getUserManager().getHashMap();
    }

    /**
     * @return HashMap of group ids and groups.
     * @throws RemoteException Thrown if there are complications with the RMI server.
     */
    @Override
    public HashMap<Integer, Group> getGroups() throws RemoteException {
        return server.getGroupManager().getHashMap();
    }

    /**
     * @return HashMap of session ids and sessions.
     * @throws RemoteException Thrown if there are complications with the RMI server.
     */
    @Override
    public HashMap<Integer, Session> getSessions() throws RemoteException {
        return server.getSessionManager().getHashMap();
    }

    /**
     * Updates the user object based on the user query into the database.
     * @param user User object that will be overwritten inside the database.
     * @throws RemoteException Thrown if there are complications with the RMI server.
     */
    @Override
    public void updateUser(User user) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("UU:");
        serverCommand.setUpdatedObject(user);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    /**
     * Updates the course object based on the user query into the database.
     * @param course Course object that will be overwritten inside the database.
     * @throws RemoteException Thrown if there are complications with the RMI server.
     */
    @Override
    public void updateCourse(Course course) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("CU:");
        serverCommand.setUpdatedObject(course);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    /**
     * Updates the group object based on the user query into the database.
     * @param group Group object that will be overwritten inside the database.
     * @throws RemoteException Thrown if there are complications with the RMI server.
     */
    @Override
    public void updateGroup(Group group) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("GU:");
        serverCommand.setUpdatedObject(group);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    /**
     * Updates the session object based on the user query into the database.
     * @param session Session object that will be overwritten inside the database.
     * @throws RemoteException Thrown if there are complications with the RMI server.
     */
    @Override
    public void updateSession(Session session) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("SU:");
        serverCommand.setUpdatedObject(session);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    /**
     * Is called from clients when they update their workspace.
     * @param bytes Workspace bytes.
     * @param originId User id where the workspace update comes from.
     * @throws RemoteException Thrown if there are complications with the RMI server.
     */
    @Override
    public void updateWorkspaceFile(byte[] bytes, int originId) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("FU:");
        serverCommand.setOriginId(originId);
        serverCommand.setWorkspaceFileBytes(bytes);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    /**
     * Is used so that a student client can send a join request to the teacher client.
     * @param originId Student id.
     * @param destinationId Teacher id.
     * @throws RemoteException Thrown if there are complications with the RMI server.
     */
    @Override
    public void sendRequest(int originId, int destinationId) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setOriginId(originId);
        serverCommand.setCommand("RE:" + destinationId);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    /**
     * Is used to send an answer to the student that requested a join.
     * @param originId Student id.
     * @param destinationId Teacher id.
     * @param answer Answer - 0: denied; 1: accepted.
     * @throws RemoteException Thrown if there are complications with the RMI server.
     */
    @Override
    public void sendAnswer(int originId, int destinationId, int answer) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setOriginId(originId);
        serverCommand.setCommand("AN:" + destinationId + " " + answer);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    /**
     * Is used to receive a state switch from a teacher client in a session.
     * @param originId Teacher id.
     * @param workspaceState State of the workspace.
     * @throws RemoteException Thrown if there are complications with the RMI server.
     */
    @Override
    public void sendWorkspaceStateSwitch(int originId,String workspaceState) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setOriginId(originId);
        serverCommand.setCommand("WU:"+workspaceState);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    /**
     * Is used so that a client can delete an object.
     * @param originId User id that wants to delete an object.
     * @param id Ids as array of the object.
     * @throws RemoteException Thrown if there are complications with the RMI server.
     */
    @Override
    public void deleteObject(int originId,int[] id) throws RemoteException {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setOriginId(originId);
        serverCommand.setCommand("DO:"+id[0]+" "+id[1]);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    /**
     * Puts a command into the command queue for the server to schedule the deletion of a group.
     * @param groupId Id of group.
     */
    @Override
    public void deleteGroup(int groupId) {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("DG:" + groupId);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    /**
     * Puts a command into the command queue for the server to schedule the deletion of a session.
     * @param sessionId Id of session.
     */
    @Override
    public void deleteSession(int sessionId) {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("DS:" + sessionId);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    /**
     * Puts a command into the command queue for the server to schedule the deletion of a course.
     * @param courseId Id of course.
     */
    @Override
    public void deleteCourse(int courseId) {
        CommandObject serverCommand = new CommandObject();
        serverCommand.setCommand("DC:" + courseId);
        serverCommandManager.getServerCommandQueue().add(serverCommand);
    }

    /**
     * Schedules to send a task to the clients.
     * @param workspaceBytes bytes of the current workspace.
     * @param taskBytes bytes of the task to be send.
     * @param originId id from where the task is send.
     */
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

