package de.swt.logic.user;

import de.swt.Server;
import de.swt.database.AsyncMySQL;
import de.swt.util.AccountType;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class UserManager {

    private Server server;
    private AsyncMySQL mySQL;
    private HashMap<Integer, User> userHashMap;

    public UserManager(Server server) {

        this.server = server;
        this.mySQL = server.mySQL;
        this.userHashMap = new HashMap<>();
    }

    public User loadUser(int id) {
        User user = null;
        if (!userHashMap.containsKey(id)) {
            ResultSet resultSet = mySQL.query("SELECT * FROM users WHERE userid = " + id + ";");
            try {
                if (resultSet.next()) {
                    user = new User();
                    user.setId(id);
                    user.setFirstname(resultSet.getString("prename"));
                    user.setSurname(resultSet.getString("surname"));
                    user.setAccountType(AccountType.valueOf(resultSet.getString("usertype")));
                    user.setOnline(false);
                    user.setCourse(loadCourses(id));
                    userHashMap.put(id, user);
                } else {
                    System.out.println("SOMETHING WENT WRONG WHILE LOADING USER!!!");
                    return null;
                }
            } catch (SQLException ignored) { }
        } else user = userHashMap.get(id);
        return user;
    }

    public void cacheAllUserData() {
        ResultSet resultSetID = mySQL.query("SELECT userid FROM users;");
        try {
            while (resultSetID.next()) {
                loadUser(resultSetID.getInt("userid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Integer> loadCourses(int userid) throws SQLException {
        ArrayList<Integer> courseList = new ArrayList<>();
        ResultSet resultSetUser = mySQL.query("SELECT courseids FROM users WHERE userid = " + userid + ";");
        if(resultSetUser.next()) {
            String[] courses = resultSetUser.getString("courseids").split(";");
            for(String course : courses) {
                courseList.add(Integer.parseInt(course));
            }
        } else {
            System.out.println("ERROR IN USER MANAGER!!!");
            return null;
        }
        return courseList;
    }


}

