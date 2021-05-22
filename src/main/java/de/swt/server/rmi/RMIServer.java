package de.swt.server.rmi;

import de.swt.server.Server;
import de.swt.server.database.DBManager;
import de.swt.server.logic.course.Course;
import de.swt.server.logic.course.CourseManager;
import de.swt.server.logic.group.Group;
import de.swt.server.logic.group.GroupManager;
import de.swt.server.logic.user.User;
import de.swt.server.logic.user.UserManager;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {

    public int port;
    private UserManager userManager;
    private CourseManager courseManager;
    private GroupManager groupManager;
    private Server server;

    public RMIServer(Server server) throws RemoteException{
        this.port = 1099;
        this.userManager = server.userManager;
        this.courseManager = server.courseManager;
        this.groupManager = server.groupManager;
        this.server = server;
    }

    @Override
    public int ping(int val) throws RemoteException {
        return (int) Math.pow(val, 2);
    }

    @Override
    public User sendUser(User user, int userid, boolean update) throws RemoteException {
        DBManager dbManager = server.dbManager;
        User updatedUser = user;
        if(update) {
            // if ip address not in IPAdressManager : add it
            String clientHost = null;
            HashMap<Integer, String> ipMap = server.ipAdressManager.getIpHashMap();

            try { clientHost = getClientHost(); } catch (ServerNotActiveException ignored) { }
            if(!ipMap.containsKey(userid)) ipMap.put(userid, clientHost);

            userManager.getUserHashMap().remove(user.getId());
            userManager.getUserHashMap().put(user.getId(), user);
            dbManager.updateUser(user);

            // user is updated now so send ping to all connected clients to get the updated User
            server.serverConn.sendBroadcastPingMessage("UU:userid");

        } else {
            updatedUser = userManager.getUserHashMap().get(userid);
        }
        return updatedUser;
    }

    @Override
    public Course sendCourse(Course course, int courseid, boolean update) throws RemoteException {
        return null;
    }

    @Override
    public Group sendGroup(Group group, int groupid, boolean update) throws RemoteException {
        return null;
    }
}

