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
    private final UserManager userManager;
    private final CourseManager courseManager;
    private final GroupManager groupManager;
    private final SessionManager sessionManager;
    private final Server server;
    private final DBManager dbManager;
    private final ServerCommandManager serverCommandManager;
    private final UserCommandMananger userCommandMananger;

    public RMIServer() throws RemoteException {
        this.port = 1099;
        this.server = Server.getInstance();
        this.userManager = server.getUserManager();
        this.courseManager = server.getCourseManager();
        this.groupManager = server.getGroupManager();
        this.sessionManager = server.getSessionManager();
        this.dbManager = server.getDbManager();
        this.serverCommandManager = server.getServerCommandManager();
        this.userCommandMananger = server.getUserCommandMananger();
    }

    @Override
    public User getUser(int userId) throws RemoteException {
        return (User) userManager.getHashMap().get(userId);
    }

    @Override
    public Course getCourse(int courseId) throws RemoteException {
        return (Course) courseManager.getHashMap().get(courseId);
    }

    @Override
    public Group getGroup(int groupId) throws RemoteException {
        return (Group) groupManager.getHashMap().get(groupId);
    }

    @Override
    public Session getSession(int sessionId) throws RemoteException {
        return (Session) sessionManager.getHashMap().get(sessionId);
    }

    @Override
    public void updateUser(User user) throws RemoteException {
        dbManager.updateUser(user);
        userManager.getHashMap().remove(user.getUserId());
        userManager.getHashMap().put(user.getUserId(), user);
    }

    @Override
    public void updateCourse(Course course) throws RemoteException {
        dbManager.updateCourse(course);
        courseManager.getHashMap().remove(course.getCourseId());
        courseManager.getHashMap().put(course.getCourseId(), course);
    }

    @Override
    public void updateGroup(Group group) throws RemoteException {
        dbManager.updateGroup(group);
        groupManager.getHashMap().remove(group.getGroupId());
        groupManager.getHashMap().put(group.getGroupId(), group);
    }

    @Override
    public void updateSession(Session session) throws RemoteException {
        dbManager.updateSession(session);
        sessionManager.getHashMap().remove(session.getSessionId());
        sessionManager.getHashMap().put(session.getSessionId(), session);
    }

    @Override
    public ArrayList<CommandObject> accessCommandQueue(int userid) throws RemoteException {
        HashMap<Integer, ArrayList<CommandObject>> arrayListHashMap = server.getUserCommandMananger().getUserCommandQueue();
        ArrayList<CommandObject> commandList = arrayListHashMap.get(userid);

        // reset command list
        arrayListHashMap.remove(userid);
        arrayListHashMap.put(userid, new ArrayList<>());
        return commandList;
    }

    @Override
    public void updateWorkspaceFile(byte[] bytes, int id) throws RemoteException {

        HashMap<Integer, ArrayList<CommandObject>> hashMap = server.getUserCommandMananger().getUserCommandQueue();
        CommandObject commandObject = new CommandObject();

        for (Group group : (Group[]) groupManager.getHashMap().values().toArray()) {
            if (group.getUserIds().contains(id)) {
                for (int ids : group.getUserIds()) {
                    if (id != ids) {
                        System.out.println("[" + id + "]: workspace update group ping.");
                        commandObject.setCommand("FU:");
                        commandObject.setWorkspaceFileBytes(bytes);
                        commandObject.setTaskBytes(null);
                        hashMap.get(ids).add(commandObject);
                    }
                }
                return;
            }
        }

        for (Session session : (Session[]) sessionManager.getHashMap().values().toArray()) {
            if (session.getMasterIds().contains(id)) {
                for (int ids : session.getUserIds()) {
                    if (id != ids) {
                        System.out.println("[" + id + "]: workspace update session ping.");
                        commandObject.setCommand("FU:");
                        commandObject.setWorkspaceFileBytes(bytes);
                        commandObject.setTaskBytes(null);
                        hashMap.get(ids).add(commandObject);
                    }
                }
            }
        }
    }

    @Override
    public int sendRequest(int originId, int teacherId) throws RemoteException {

        HashMap<Integer, ArrayList<CommandObject>> hashMap = server.getUserCommandMananger().getUserCommandQueue();
        CommandObject commandObject = new CommandObject();
        if (userManager.getHashMap().containsKey(teacherId)) {
            System.out.println("[" + originId + "]: user to teacher request ping.");
            commandObject.setCommand("RE:" + originId);
            commandObject.setWorkspaceFileBytes(null);
            commandObject.setTaskBytes(null);
            hashMap.get(teacherId).add(commandObject);
        }
        return 0;
    }

    @Override
    public int sendAnswer(int originid, int answer, int teacherid) throws RemoteException {
        HashMap<Integer, ArrayList<CommandObject>> hashMap = server.getUserCommandMananger().getUserCommandQueue();
        CommandObject commandObject = new CommandObject();
        if (userManager.getHashMap().containsKey(originid)) {
            System.out.println("[" + teacherid + "]: teacher to user accept ping.");
            commandObject.setCommand("AN:" + originid + " " + answer + " " + teacherid);
            commandObject.setWorkspaceFileBytes(null);
            commandObject.setTaskBytes(null);
            hashMap.get(originid).add(commandObject);
        }
        return 0;
    }

    @Override
    public void deleteGroup(int groupId) {
        dbManager.deleteGroup(groupId);
    }

    @Override
    public void deleteSession(int sessionId) {
        dbManager.deleteSession(sessionId);
    }

    @Override
    public void sendTask(byte[] workspaceBytes, byte[] taskBytes, int id) {
        HashMap<Integer, ArrayList<CommandObject>> hashMap = server.getUserCommandMananger().getUserCommandQueue();
        CommandObject commandObject = new CommandObject();
        Session session = sessionManager.getSessionFromTeacherId(id);
        for (int partId : session.getUserIds()) {
            if (partId == id) {
                continue;
            }
            System.out.println("[" + id + "]: sending task ping.");
            commandObject.setCommand("ST:" + id);
            commandObject.setWorkspaceFileBytes(workspaceBytes);
            commandObject.setTaskBytes(taskBytes);
            hashMap.get(partId).add(commandObject);
        }
    }

}

