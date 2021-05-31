package de.swt.logic.group;

import de.swt.database.AsyncMySQL;
import de.swt.logic.user.User;
import de.swt.util.AccountType;
import de.swt.util.Client;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@Setter
@Getter
public class GroupManager {

    private Client client;
    private AsyncMySQL mySQL;
    private HashMap<Integer, Group> groupHashMap;

    public GroupManager(Client client) {
        this.client = client;
        this.mySQL = client.mySQL;
        this.groupHashMap = new HashMap<>();
    }

    public Group loadGroup(int id) throws SQLException {
        Group group;
        if (!groupHashMap.containsKey(id)) {
            ResultSet resultSet = mySQL.query("SELECT * FROM groups WHERE groupid = " + id + ";");
            if (resultSet.next()) {
                group = new Group();
                group.setId(id);
                group.setCourseID(Integer.parseInt(resultSet.getString("courseid")));
                group.setTimeTillTermination(resultSet.getInt("ttt"));
                group.setMaxGroupSize(Integer.parseInt(resultSet.getString("maxGS")));
                group.setParticipants(parseParticipants(resultSet.getString("participants")));
                groupHashMap.put(id, group);
            } else {
                System.out.println("SOMETHING WENT WRONG WHILE LOADING GROUP!!!");
                return null;
            }
        } else group = groupHashMap.get(id);
        return group;
    }

    private ArrayList<Integer> parseParticipants(String parseString){
        ArrayList<Integer> list = new ArrayList<>();
        String[] sUserList = parseString.split(";");
        for (String sUser : sUserList){
            list.add(Integer.parseInt(sUser));
        }
        return list;
    }

    public void cacheAllGroupData() {
        this.groupHashMap.clear();
        ResultSet resultSet = mySQL.query("SELECT groupid FROM groups;");
        try {
            while (resultSet.next()) {
                loadGroup(resultSet.getInt("groupid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

