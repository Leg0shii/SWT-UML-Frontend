package de.swt.rmi;

import de.swt.logic.Course;
import de.swt.logic.User;

import java.rmi.*;
import java.util.ArrayList;

public interface RMIServerInterface extends Remote {

    boolean login(int clientID, String pwdHash) throws RemoteException;
    boolean logout(int clientID) throws RemoteException;
    ArrayList<User> getCurrentUserList() throws RemoteException;
    ArrayList<Course> getCurrentCourseList() throws RemoteException;
}

