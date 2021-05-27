package de.swt.rmi;

import de.swt.Server;
import de.swt.database.DBManager;
import de.swt.logic.TestClass;
import de.swt.logic.course.Course;
import de.swt.logic.course.CourseManager;
import de.swt.logic.group.Group;
import de.swt.logic.group.GroupManager;
import de.swt.logic.user.User;
import de.swt.logic.user.UserManager;
import de.swt.manager.CommandManager;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {

    public int port;
    private UserManager userManager;
    private CourseManager courseManager;
    private GroupManager groupManager;
    private Server server;

    public RMIServer() throws RemoteException{
        this.port = 1099;
        this.server = Server.getInstance();
        this.userManager = server.userManager;
        this.courseManager = server.courseManager;
        this.groupManager = server.groupManager;
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
        if(update) {

            // if users first login, register it in command manager
            CommandManager commandManager = server.commandManager;
            HashMap<Integer, ArrayList<String>> hashMap = commandManager.getCommandHashMap();
            if(hashMap.containsKey(user.getId())) {
                hashMap.put(user.getId(), new ArrayList<>());
            }

            // if user is offline remove from commandlist
            if(!user.isOnline()) hashMap.remove(userid);

            userManager.getUserHashMap().remove(user.getId());
            userManager.getUserHashMap().put(user.getId(), user);
            dbManager.updateUser(user);

            // user is updated now so send ping to all connected clients to get the updated User
            System.out.println("SENDING PING MESSAGE!!!");
            for(int ids : hashMap.keySet()) {
                hashMap.get(ids).add("UU:" + user.getId());
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
        if(update) {

            courseManager.getCourseHashMap().remove(course.getId());
            courseManager.getCourseHashMap().put(course.getId(), course);
            dbManager.updateCourse(course);

            // user is updated now so send ping to all connected clients to get the updated User
            HashMap<Integer, ArrayList<String>> hashMap = server.commandManager.getCommandHashMap();
            System.out.println("SENDING PING MESSAGE!!!");
            for(int ids : hashMap.keySet()) {
                hashMap.get(ids).add("CU:" + course.getId());
            }

        } else {
            updatedCourse = courseManager.getCourseHashMap().get(courseid);
            //updatedUser.setFirstname("New NAME LOL!!");
        }
        return updatedCourse;
    }

    @Override
    public Group sendGroup(Group group, int groupid, boolean update) throws RemoteException {
        return null;
    }

    @Override
    public ArrayList<String> accessCommandQueue(int userid) throws RemoteException {
        HashMap<Integer, ArrayList<String>> arrayListHashMap = server.commandManager.getCommandHashMap();
        ArrayList<String> commandList = arrayListHashMap.get(userid);

        // reset command list
        arrayListHashMap.remove(userid);
        arrayListHashMap.put(userid, new ArrayList<>());
        return commandList;
    }

}

