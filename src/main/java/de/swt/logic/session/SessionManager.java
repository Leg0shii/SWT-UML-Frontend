package de.swt.logic.session;

import de.swt.Server;
import de.swt.logic.group.Group;
import de.swt.manager.Manager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionManager extends Manager {
    public SessionManager(Server server) {
        super(server);
    }

    @Override
    public Object load(int id) throws SQLException {
        if (getHashMap().containsKey(id)) {
            return getHashMap().get(id);
        } else {
            ResultSet resultSet = getMySQL().query("SELECT * FROM sessions WHERE sessionId = " + id + ");");
            resultSet.next();
            Session newSession = new Session();
            newSession.setSessionId(id);
            newSession.setRemainingTime(resultSet.getLong("remainingTime"));
            resultSet = getMySQL().query("SELECT userId FROM userInSession WHERE sessionId = " + id + ");");
            newSession.setUserIds(getIds(resultSet, "userId"));
            resultSet = getMySQL().query("SELECT groupId FROM groupInSession WHERE sessionId = " + id + ");");
            newSession.setGroupIds(getIds(resultSet, "groupId"));

            getHashMap().put(id, newSession);

            return newSession;
        }
    }

    @Override
    public void cacheAllData() throws SQLException {
        getHashMap().clear();
        ResultSet resultSet = getMySQL().query("SELECT sessionId FROM sessions;");
        while (resultSet.next()) {
            load(resultSet.getInt("sessionId"));
        }
    }
}