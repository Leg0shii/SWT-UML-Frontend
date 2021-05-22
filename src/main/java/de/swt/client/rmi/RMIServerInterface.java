package de.swt.client.rmi;

import de.swt.client.logic.Course;
import de.swt.client.logic.Group;
import de.swt.client.logic.User;

import java.rmi.*;

public interface RMIServerInterface extends Remote {

    int ping(int val) throws RemoteException;
    User sendUser(User user, int userid, boolean update) throws RemoteException;
    Course sendCourse(Course course, int courseid, boolean update) throws RemoteException;
    Group sendGroup(Group group, int groupid, boolean update) throws RemoteException;
}

