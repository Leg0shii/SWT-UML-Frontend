package de.swt.rmi;

import de.swt.logic.course.Course;
import de.swt.logic.group.Group;
import de.swt.logic.session.Session;
import de.swt.logic.user.User;
import de.swt.manager.CommandObject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RMIServerInterface extends Remote {

    User getUser(int userId) throws RemoteException;
    void updateUser(User user) throws RemoteException;

    Course getCourse(int courseId) throws RemoteException;
    void updateCourse(Course course) throws RemoteException;

    Group getGroup(int groupId) throws RemoteException;
    void updateGroup(Group group) throws RemoteException;
    void deleteGroup(int groupId) throws RemoteException;

    Session getSession(int sessionId) throws RemoteException;
    void updateSession(Session session) throws RemoteException;
    void deleteSession(int sessionId) throws RemoteException;

    ArrayList<CommandObject> accessCommandQueue(int userid) throws RemoteException;
    void sendTask(byte[] workspaceBytes, byte[] taskBytes, int id) throws RemoteException;

    void updateWorkspaceFile(byte[] bytes, int id) throws RemoteException;
    int sendRequest(int originid, int teacherid) throws RemoteException;
    int sendAnswer(int originid, int answer, int teacherid) throws RemoteException;

}

