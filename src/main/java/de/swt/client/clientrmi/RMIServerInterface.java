package de.swt.client.clientrmi;

import de.swt.client.clientlogic.Course;
import de.swt.client.clientlogic.Group;
import de.swt.client.clientlogic.User;

import java.rmi.*;

public interface RMIServerInterface extends Remote {

    int ping(int val) throws RemoteException;
    User sendUser(User user, int userid, boolean update) throws RemoteException;
    Course sendCourse(Course course, int courseid, boolean update) throws RemoteException;
    Group sendGroup(Group group, int groupid, boolean update) throws RemoteException;
}

