package de.swt.logic.session;

import de.swt.Server;
import de.swt.database.AsyncMySQL;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public class SessionManager {
    private final HashMap<Integer, Session> sessionHashMap;
    private final Server server;

    public SessionManager(Server server) {

        this.server = server;
        this.sessionHashMap = new HashMap<>();
    }

    public Session loadSession(int id) throws SQLException {
        Session session = null;
        if (!sessionHashMap.containsKey(id)) {
            ResultSet resultSet = server.mySQL.query("SELECT * FROM sessions WHERE idsession = " + id + ";");
            if (resultSet.next()) {
                session = new Session();
                session.setId(Integer.parseInt(resultSet.getString("idsession")));
                session.setParticipants(parseParticipants(resultSet.getString("participants")));
                session.setMaster(parseParticipants(resultSet.getString("master")));
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
        ResultSet resultSet = server.mySQL.query("SELECT idsession FROM sessions;");
        try {
            while (resultSet.next()) {
                loadSession(resultSet.getInt("idsession"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Integer> parseParticipants(String parseString){
        ArrayList<Integer> list = new ArrayList<>();
        String[] sUserList = parseString.split(";");
        for (String sUser : sUserList){
            if (!sUser.equals("")) {
                list.add(Integer.parseInt(sUser));
            }
        }
        return list;
    }

    public Session getSessionFromTeacherId(int teacherId){
        for (Session session : getSessionHashMap().values()){
            if (session == null) return null;
            if (session.getMaster().contains(teacherId)){
                return session;
            }
        }
        return null;
    }

}