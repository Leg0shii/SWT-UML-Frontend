package de.swt.logic.group;

import de.swt.Server;
import de.swt.logic.user.User;
import de.swt.util.AccountType;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Getter
public class GroupManager {

    private Server server;
    private HashMap<Integer, Group> groupHashMap;

    public GroupManager(Server server) {

        this.groupHashMap = new HashMap<>();
        this.server = server;
    }

    public Group loadGroup(int id) {
        Group group = null;
        if (!groupHashMap.containsKey(id)) {
            ResultSet resultSet = server.mySQL.query("SELECT * FROM groups WHERE groupid = " + id + ";");
            try {
                if (resultSet.next()) {
                    group = new Group();
                    group.setGroupid(resultSet.getInt("groupid"));
                    group.setMaxGroupSize(resultSet.getInt("maxGS"));
                    group.setCourseid(resultSet.getInt("courseid"));
                    group.setParticipants(getParticipantsFromString("participants"));
                    group.setTimeTillTermination(resultSet.getInt("ttt"));
                    groupHashMap.put(id, group);
                } else {
                    System.out.println("SOMETHING WENT WRONG WHILE LOADING GROUP!!!");
                    return null;
                }
            } catch (SQLException ignored) { }
        } else group = groupHashMap.get(id);
        return group;
    }

    public void cacheAllGroupData() {
        ResultSet resultSet = server.mySQL.query("SELECT * FROM groups;");
        try {
            while (resultSet.next()) {
                loadGroup(resultSet.getInt("groupid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Integer> getParticipantsFromString(String string) {
        String[] stringDates = string.split(";");
        ArrayList<Integer> list = new ArrayList<>();
        for (String singleDate : stringDates) {
            list.add(Integer.parseInt(singleDate));
        }
        return list;
    }

}
