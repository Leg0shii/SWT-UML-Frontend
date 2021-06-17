package de.swt.rmi;

import de.swt.logic.course.Course;
import de.swt.logic.group.Group;
import de.swt.logic.session.Session;
import de.swt.logic.user.User;
import de.swt.manager.CommandObject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Methods that the RMI Server offers for the clients.
 */
public interface RMIServerInterface extends Remote {

    LinkedBlockingQueue<CommandObject> accessCommandQueue(int userId) throws RemoteException;

    HashMap<Integer, Course> getCourses() throws RemoteException;

    HashMap<Integer, User> getUsers() throws RemoteException;

    HashMap<Integer, Group> getGroups() throws RemoteException;

    HashMap<Integer, Session> getSessions() throws RemoteException;

    void updateUser(User user) throws RemoteException;

    void updateCourse(Course course) throws RemoteException;

    void updateGroup(Group group) throws RemoteException;

    void updateSession(Session session) throws RemoteException;

    void deleteGroup(int groupId) throws RemoteException;

    void deleteSession(int sessionId) throws RemoteException;

    void deleteCourse(int courseId) throws RemoteException;

    void sendTask(byte[] workspaceBytes, byte[] taskBytes, int id) throws RemoteException;

    void updateWorkspaceFile(byte[] bytes, int id) throws RemoteException;

    void sendRequest(int originId, int destinationId) throws RemoteException;

    void sendAnswer(int originId, int destinationId, int answer) throws RemoteException;

    void sendWorkspaceStateSwitch(int originId, String workspaceState) throws RemoteException;

    void deleteObject(int originId, int[] id) throws RemoteException;
}

