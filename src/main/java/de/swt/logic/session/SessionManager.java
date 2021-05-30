package de.swt.logic.session;

import de.swt.database.AsyncMySQL;
import de.swt.logic.course.Course;
import de.swt.util.Client;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Getter
@Setter
public class SessionManager {
    private final AsyncMySQL mySQL;
    private final HashMap<Integer, Session> sessionHashMap;
    private final Client client;

    public SessionManager(Client client) {

        this.client = client;
        this.mySQL = client.mySQL;
        this.sessionHashMap = new HashMap<>();
    }

    public Session loadSession(int id) throws SQLException {
        Session session;
        if (!sessionHashMap.containsKey(id)) {
            ResultSet resultSet = mySQL.query("SELECT * FROM sessions WHERE idsession = " + id + ";");
            if (resultSet.next()) {
                session = new Session(-1);
                session.setId(Integer.parseInt(resultSet.getString("idsession")));
                session.setParticipants(parseParticipants(resultSet.getString("participants")));
                session.setMaster(Integer.parseInt(resultSet.getString("master")));
                session.setRemainingTime(Integer.parseInt(resultSet.getString("remainingtime")));
                session.setGroups(parseParticipants(resultSet.getString("groups")));
                sessionHashMap.put(session.getId(), session);
            } else {
                System.out.println("SOMETHING WENT WRONG WHILE LOADING SESSION!!!");
                return null;
            }
        } else session = sessionHashMap.get(id);
        return session;
    }

    public void cacheAllSessionData() {
        this.sessionHashMap.clear();
        ResultSet resultSet = mySQL.query("SELECT idsession FROM sessions;");
        try {
            while (resultSet.next()) {
                loadSession(resultSet.getInt("idsession"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Integer> parseParticipants(String parseString) {
        ArrayList<Integer> list = new ArrayList<>();
        String[] sUserList = parseString.split(";");
        for (String sUser : sUserList) {
            if (!sUser.equals("")) {
                list.add(Integer.parseInt(sUser));
            }
        }
        return list;
    }

}
