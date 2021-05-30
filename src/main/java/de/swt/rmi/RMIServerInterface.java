package de.swt.rmi;

import de.swt.logic.TestClass;
import de.swt.logic.course.Course;
import de.swt.logic.group.Group;
import de.swt.logic.session.Session;
import de.swt.logic.user.User;
import de.swt.manager.CommandObject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RMIServerInterface extends Remote {

    TestClass testClass(TestClass testClass) throws RemoteException;
    int ping(int val) throws RemoteException;

    User sendUser(User user, int userid, boolean update) throws RemoteException;
    Course sendCourse(Course course, int courseid, boolean update) throws RemoteException;
    Group sendGroup(Group group, int groupid, boolean update) throws RemoteException;
    Session sendSession(Session session, int idsession, boolean update) throws RemoteException;
    ArrayList<CommandObject> accessCommandQueue(int userid) throws RemoteException;
    void updateWorkspaceFile(byte[] bytes, int id) throws RemoteException;
}

