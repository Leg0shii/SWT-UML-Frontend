package de.swt.logic.session;

import de.swt.Server;
import de.swt.manager.Manager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionManager extends Manager<Session> {
    public SessionManager(Server server) {
        super(server);
    }

    /**
     * Is used to load in the session object from the database and saves it if not saved into the HashMap.
     * @param id Session id.
     * @return Session object.
     * @throws SQLException Is thrown when there are complications with the database.
     */
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

    /**
     * Is used to load in all session objects from the database and to save them into the HashMap.
     * @throws SQLException Is thrown when there are complications with the database.
     */
    @Override
    public void cacheAllData() throws SQLException {
        getHashMap().clear();
        ResultSet resultSet = getMySQL().query("SELECT sessionId FROM sessions;");
        while (resultSet.next()) {
            load(resultSet.getInt("sessionId"));
        }
    }

    /**
     * Is used to get the session from a teacher id.
     * @param teacherId Id of teacher.
     * @return Session object.
     */
    public Session getSessionFromTeacherId(int teacherId){
        for (Object object : getHashMap().values()){
            Session session = (Session) object;
            if (session.getMasterIds().contains(teacherId)){
                return session;
            }
        }
        return null;
    }
}