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
import de.swt.manager.CommandManager;
import de.swt.manager.CommandObject;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {

    public int port;
    private UserManager userManager;
    private CourseManager courseManager;
    private GroupManager groupManager;
    private SessionManager sessionManager;
    private Server server;

    public RMIServer() throws RemoteException {
        this.port = 1099;
        this.server = Server.getInstance();
        this.userManager = server.userManager;
        this.courseManager = server.courseManager;
        this.groupManager = server.groupManager;
        this.sessionManager = server.sessionManager;
    }

    @Override
    public TestClass testClass(TestClass testClass) throws RemoteException {
        testClass.setFirstname("NEWNAME!!!");
        return testClass;
    }

    @Override
    public int ping(int val) throws RemoteException {
        return (int) Math.pow(val, 2);
    }

    @Override
    public User sendUser(User user, int userid, boolean update) throws RemoteException {
        DBManager dbManager = server.dbManager;
        User updatedUser = user;

        // use update = true if you want to update the user data -> tells all other clients to update it too
        // for this path the userid is ignored, it is only used to retrieve the user when update = false
        if (update) {

            // if users first login, register it in command manager
            CommandManager commandManager = server.commandManager;
            HashMap<Integer, ArrayList<CommandObject>> hashMap = commandManager.getCommandHashMap();
            CommandObject commandObject = new CommandObject();
            if (hashMap.containsKey(user.getId())) {
                hashMap.put(user.getId(), new ArrayList<>());
            }

            // if user is offline remove from commandlist
            if (!user.isOnline()) hashMap.remove(userid);

            int id = dbManager.updateUser(user);
            this.userManager.cacheAllUserData();
            if (userid < 0) {
                updatedUser = this.userManager.loadUser(id);
            } else {
                updatedUser = this.userManager.loadUser(userid);
            }

            // user is updated now so send ping to all connected clients to get the updated User
            System.out.println("SENDING PING MESSAGE!!!");
            for (int ids : hashMap.keySet()) {
                commandObject.setCommand("UU:" + updatedUser.getId());
                commandObject.setWorkspaceFileBytes(null);
                commandObject.setTaskBytes(null);
                hashMap.get(ids).add(commandObject);
            }

        } else {
            updatedUser = userManager.getUserHashMap().get(userid);
            //updatedUser.setFirstname("New NAME LOL!!");
        }
        return updatedUser;
    }

    @Override
    public Course sendCourse(Course course, int courseid, boolean update) throws RemoteException {
        DBManager dbManager = server.dbManager;
        Course updatedCourse = course;

        // use update = true if you want to update the user data -> tells all other clients to update it too
        // for this path the userid is ignored, it is only used to retrieve the user when update = false
        if (update) {

            int id = dbManager.updateCourse(course);
            courseManager.cacheAllCourseData();
            if (courseid < 0) {
                updatedCourse = courseManager.loadCourse(id);
            } else {
                updatedCourse = courseManager.loadCourse(courseid);
            }

            // user is updated now so send ping to all connected clients to get the updated User
            HashMap<Integer, ArrayList<CommandObject>> hashMap = server.commandManager.getCommandHashMap();
            CommandObject commandObject = new CommandObject();
            System.out.println("SENDING COURSE PING MESSAGE!!!");
            for (int ids : hashMap.keySet()) {
                commandObject.setCommand("CU:" + updatedCourse.getId());
                commandObject.setWorkspaceFileBytes(null);
                commandObject.setTaskBytes(null);
                hashMap.get(ids).add(commandObject);
            }

        } else {
            updatedCourse = courseManager.getCourseHashMap().get(courseid);
            //updatedUser.setFirstname("New NAME LOL!!");
        }
        return updatedCourse;
    }

    @Override
    public Group sendGroup(Group group, int groupid, boolean update) throws RemoteException {
        DBManager dbManager = server.dbManager;
        Group updatedGroup;

        // use update = true if you want to update the user data -> tells all other clients to update it too
        // for this path the userid is ignored, it is only used to retrieve the user when update = false
        if (update) {

            int id = dbManager.updateGroups(group);
            groupManager.cacheAllGroupData();
            if (groupid < 0) {
                updatedGroup = groupManager.loadGroup(id);
            } else {
                updatedGroup = groupManager.loadGroup(groupid);
            }

            Session session = new Session();
            try {
                session = sessionManager.loadSession(updatedGroup.getCourseID());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            session.getGroups().add(updatedGroup.getId());
            try {
                sendSession(session, session.getId(), true);
            } catch (Exception ignored) {

            }

            // user is updated now so send ping to all connected clients to get the updated User
            HashMap<Integer, ArrayList<CommandObject>> hashMap = server.commandManager.getCommandHashMap();
            CommandObject commandObject = new CommandObject();
            System.out.println("SENDING GROUP PING MESSAGE!!!");
            for (int ids : session.getParticipants()) {
                commandObject.setCommand("GU:" + updatedGroup.getId());
                commandObject.setWorkspaceFileBytes(null);
                commandObject.setTaskBytes(null);
                hashMap.get(ids).add(commandObject);
            }

        } else {
            updatedGroup = groupManager.getGroupHashMap().get(groupid);
            //updatedUser.setFirstname("New NAME LOL!!");
        }
        return updatedGroup;
    }

    @Override
    public Session sendSession(Session session, int idsession, boolean update) throws RemoteException {
        DBManager dbManager = server.dbManager;
        Session updatedSession = session;

        // use update = true if you want to update the user data -> tells all other clients to update it too
        // for this path the userid is ignored, it is only used to retrieve the user when update = false
        if (update) {

            int id = dbManager.updateSessions(session);
            sessionManager.cacheAllSessionData();
            try {
                if (idsession < 0) {
                    updatedSession = sessionManager.loadSession(id);
                } else {
                    updatedSession = sessionManager.loadSession(idsession);
                }
            } catch (Exception ignored){
            }


            // user is updated now so send ping to all connected clients to get the updated User
            HashMap<Integer, ArrayList<CommandObject>> hashMap = server.commandManager.getCommandHashMap();
            CommandObject commandObject = new CommandObject();
            System.out.println("SENDING SESSION PING MESSAGE!!!");
            for (int ids : hashMap.keySet()) {
                commandObject.setCommand("SU:" + updatedSession.getId());
                commandObject.setWorkspaceFileBytes(null);
                commandObject.setTaskBytes(null);
                hashMap.get(ids).add(commandObject);
            }

        } else {
            updatedSession = sessionManager.getSessionHashMap().get(idsession);
            //updatedUser.setFirstname("New NAME LOL!!");
        }
        return updatedSession;
    }

    @Override
    public ArrayList<CommandObject> accessCommandQueue(int userid) throws RemoteException {
        HashMap<Integer, ArrayList<CommandObject>> arrayListHashMap = server.commandManager.getCommandHashMap();
        ArrayList<CommandObject> commandList = arrayListHashMap.get(userid);

        // reset command list
        arrayListHashMap.remove(userid);
        arrayListHashMap.put(userid, new ArrayList<>());
        return commandList;
    }

    @Override
    public void updateWorkspaceFile(byte[] bytes, int id) throws RemoteException {

        HashMap<Integer, ArrayList<CommandObject>> hashMap = server.commandManager.getCommandHashMap();
        CommandObject commandObject = new CommandObject();
        System.out.println("SENDING JOIN REQUEST PING MESSAGE!!!");

        for (Group group : groupManager.getGroupHashMap().values()) {
            if (group.getParticipants().contains(id)) {
                for (int ids : group.getParticipants()) {
                    if (id != ids) {
                        commandObject.setCommand("FU:");
                        commandObject.setWorkspaceFileBytes(bytes);
                        commandObject.setTaskBytes(null);
                        hashMap.get(ids).add(commandObject);
                    }
                }
                return;
            }
        }

        for (Session session : sessionManager.getSessionHashMap().values()) {
            if (session.getMaster().contains(id)) {
                for (int ids : session.getParticipants()) {
                    if (id != ids) {
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
    public int sendRequest(int originid, int teacherid) throws RemoteException {

        HashMap<Integer, ArrayList<CommandObject>> hashMap = server.commandManager.getCommandHashMap();
        CommandObject commandObject = new CommandObject();
        if (userManager.getUserHashMap().containsKey(teacherid)) {
            commandObject.setCommand("RE:" + originid);
            commandObject.setWorkspaceFileBytes(null);
            commandObject.setTaskBytes(null);
            hashMap.get(teacherid).add(commandObject);
        }
        System.out.println("SENDING SESSION REQUEST PING MESSAGE!!!");
        return 0;
    }

    @Override
    public int sendAnswer(int originid, int answer, int teacherid) throws RemoteException {
        HashMap<Integer, ArrayList<CommandObject>> hashMap = server.commandManager.getCommandHashMap();
        CommandObject commandObject = new CommandObject();
        System.out.println("SENDING SESSION ANSWER PING MESSAGE!!!");
        if (userManager.getUserHashMap().containsKey(originid)) {
            commandObject.setCommand("AN:" + originid + " " + answer + " " + teacherid);
            commandObject.setWorkspaceFileBytes(null);
            commandObject.setTaskBytes(null);
            hashMap.get(originid).add(commandObject);
        }
        return 0;
    }

    @Override
    public void deleteGroup(int groupId) {
        HashMap<Integer, ArrayList<CommandObject>> hashMap = server.commandManager.getCommandHashMap();
        CommandObject commandObject = new CommandObject();
        System.out.println("SENDING DELETE GROUP PING MESSAGE!!!");
        Group group = groupManager.getGroupHashMap().get(groupId);
        Session session = sessionManager.getSessionHashMap().get(group.getCourseID());
        for (User user : userManager.getUserHashMap().values()) {
            if (session.getParticipants().contains(user.getId())) {
                commandObject.setCommand("DG:" + groupId);
                commandObject.setWorkspaceFileBytes(null);
                commandObject.setTaskBytes(null);
                hashMap.get(user.getId()).add(commandObject);
            }
        }
        server.dbManager.deleteGroup(groupId);
    }

    @Override
    public void deleteSession(int sessionId) {
        HashMap<Integer, ArrayList<CommandObject>> hashMap = server.commandManager.getCommandHashMap();
        CommandObject commandObject = new CommandObject();
        System.out.println("SENDING DELETE SESSION PING MESSAGE!!!");
        Session session = sessionManager.getSessionHashMap().get(sessionId);
        for (Integer groupId : session.getGroups()) {
            deleteGroup(groupId);
        }
        for (User user : userManager.getUserHashMap().values()) {
            if (session.getParticipants().contains(user.getId())) {
                commandObject.setCommand("DS:" + sessionId);
                commandObject.setWorkspaceFileBytes(null);
                commandObject.setTaskBytes(null);
                hashMap.get(user.getId()).add(commandObject);
            }
        }
        server.dbManager.deleteSession(sessionId);
    }

    @Override
    public void sendTask(byte[] workspaceBytes, byte[] taskBytes, int id) {
        HashMap<Integer, ArrayList<CommandObject>> hashMap = server.commandManager.getCommandHashMap();
        CommandObject commandObject = new CommandObject();
        System.out.println("SENDING SEND TASK PING MESSAGE!!!");
        Session session = sessionManager.getSessionFromTeacherId(id);
        for (int partId : session.getParticipants()) {
            if (partId == id) {
                continue;
            }
            commandObject.setCommand("ST:" + id);
            commandObject.setWorkspaceFileBytes(workspaceBytes);
            commandObject.setTaskBytes(taskBytes);
            hashMap.get(partId).add(commandObject);
        }
    }

}

