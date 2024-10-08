package de.swt.logic.group;

import de.swt.logic.course.Course;
import de.swt.manager.Manager;
import de.swt.util.Client;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupManager extends Manager<Group> {
    public GroupManager(Client client) {
        super(client);
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
        var hashMap = new HashMap<Integer, Group>();
        try {
            hashMap = getClient().getServer().getGroups();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        for (Integer key : hashMap.keySet()){
            getHashMap().put(key, hashMap.get(key));
        }
        /*
        getHashMap().clear();
        ResultSet resultSet = getMySQL().query("SELECT groupId FROM groups;");
        while (resultSet.next()){
            load(resultSet.getInt("groupId"));
        }
         */
    }
}
