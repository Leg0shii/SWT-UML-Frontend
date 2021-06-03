package de.swt.rmi;

import de.swt.logic.course.Course;
import de.swt.logic.group.Group;
import de.swt.logic.session.Session;
import de.swt.logic.user.User;
import de.swt.manager.CommandObject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public interface RMIServerInterface extends Remote {

    LinkedBlockingQueue<CommandObject> accessCommandQueue(int userid) throws RemoteException;

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

}

