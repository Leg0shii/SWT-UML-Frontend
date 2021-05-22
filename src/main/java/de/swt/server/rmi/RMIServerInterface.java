package de.swt.server.rmi;

import de.swt.server.logic.course.Course;
import de.swt.server.logic.group.Group;
import de.swt.server.logic.user.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {

    int ping(int val) throws RemoteException;
    User sendUser(User user, int userid, boolean update) throws RemoteException;
    Course sendCourse(Course course, int courseid, boolean update) throws RemoteException;
    Group sendGroup(Group group, int groupid, boolean update) throws RemoteException;
}

