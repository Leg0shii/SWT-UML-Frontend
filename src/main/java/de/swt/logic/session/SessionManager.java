package de.swt.logic.session;

import de.swt.logic.course.Course;
import de.swt.logic.group.Group;
import de.swt.manager.Manager;
import de.swt.util.Client;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SessionManager extends Manager<Session> {
    public SessionManager(Client client) {
        super(client);
    }

    @Override
    public Session load(int id) throws SQLException {
        if (getHashMap().containsKey(id)) {
            return getHashMap().get(id);
        } else {
            ResultSet resultSet = getMySQL().query("SELECT * FROM sessions WHERE sessionId = " + id + ";");
            resultSet.next();
            Session newSession = new Session();
            newSession.setSessionId(id);
            newSession.setRemainingTime(resultSet.getLong("remainingTime"));
            resultSet = getMySQL().query("SELECT userId FROM userInSession WHERE sessionId = " + id + ";");
            newSession.setUserIds(getIds(resultSet, "userId"));
            resultSet = getMySQL().query("SELECT groupId FROM groupInSession WHERE sessionId = " + id + ";");
            newSession.setGroupIds(getIds(resultSet, "groupId"));
            resultSet = getMySQL().query("SELECT userId FROM masterInSession WHERE sessionId = " + id + ";");
            newSession.setMasterIds(getIds(resultSet, "userId"));

            getHashMap().put(id, newSession);

            return newSession;
        }
    }

    @Override
    public void cacheAllData() throws SQLException {
        getHashMap().clear();
        var hashMap = new HashMap<Integer, Session>();
        try {
            hashMap = getClient().getServer().getSessions();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        for (Integer key : hashMap.keySet()){
            getHashMap().put(key, hashMap.get(key));
        }
        /*
        getHashMap().clear();
        ResultSet resultSet = getMySQL().query("SELECT sessionId FROM sessions;");
        while (resultSet.next()){
            load(resultSet.getInt("sessionId"));
        }

         */
    }

    public Session getSessionFromTeacherId(int teacherId){
        for (Session session : getHashMap().values()){
            if (session.getMasterIds().contains(teacherId)){
                return session;
            }
        }
        return null;
    }
}