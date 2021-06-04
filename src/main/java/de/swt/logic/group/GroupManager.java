package de.swt.logic.group;

import de.swt.Server;
import de.swt.manager.Manager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupManager extends Manager<Group> {
    public GroupManager(Server server) {
        super(server);
    }

    @Override
    public Group load(int id) throws SQLException {
        if (getHashMap().containsKey(id)) {
            return getHashMap().get(id);
        } else {
            ResultSet resultSet = getMySQL().query("SELECT * FROM groups WHERE groupId = " + id + ";");
            resultSet.next();
            Group newGroup = new Group();
            newGroup.setGroupId(id);
            newGroup.setMaxGroupSize(resultSet.getInt("maxGroupSize"));
            newGroup.setTimeTillTermination(resultSet.getLong("timeTillTermination"));
            resultSet = getMySQL().query("SELECT userId FROM userInGroup WHERE groupId = " + id + ";");
            newGroup.setUserIds(getIds(resultSet, "userId"));
            resultSet = getMySQL().query("SELECT sessionId FROM groupInSession WHERE groupId = " + id + ";");
            if (resultSet.next()) {
                newGroup.setSessionId(resultSet.getInt("sessionId"));
            }

            getHashMap().put(id, newGroup);

            return newGroup;
        }
    }

    @Override
    public void cacheAllData() throws SQLException {
        getHashMap().clear();
        ResultSet resultSet = getMySQL().query("SELECT groupId FROM groups;");
        while (resultSet.next()) {
            load(resultSet.getInt("groupId"));
        }
    }
}
