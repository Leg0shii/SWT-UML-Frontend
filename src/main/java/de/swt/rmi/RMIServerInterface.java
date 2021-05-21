package de.swt.rmi;

import de.swt.logic.Course;
import de.swt.logic.Group;
import de.swt.logic.User;

import java.rmi.*;

public interface RMIServerInterface extends Remote {

    User sendUser(User user, int userid, boolean update) throws RemoteException;
    Course sendCourse(Course course, int courseid, boolean update) throws RemoteException;
    Group sendGroup(Group group, int groupid, boolean update) throws RemoteException;
}

