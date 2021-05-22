package de.swt.rmi;

import de.swt.logic.course.Course;
import de.swt.logic.group.Group;
import de.swt.logic.user.User;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {

    int ping(int val) throws RemoteException;
    User sendUser(User user, int userid, boolean update) throws RemoteException;
    Course sendCourse(Course course, int courseid, boolean update) throws RemoteException;
    Group sendGroup(Group group, int groupid, boolean update) throws RemoteException;
}

