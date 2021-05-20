package de.swt.logic;

import de.swt.database.AsyncMySQL;
import de.swt.util.AccountType;
import de.swt.util.Client;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

@Setter
@Getter
public class UserManager {

    private Client client;
    private AsyncMySQL mySQL;
    private HashMap<Integer, User> userHashMap;

    public UserManager(Client client) {

        this.client = client;
        this.mySQL = client.mySQL;
        this.userHashMap = new HashMap<>();
    }

    public User loadUser(int id) throws SQLException {
        User user;
        if (!userHashMap.containsKey(id)) {
            ResultSet resultSet = mySQL.query("SELECT * FROM users WHERE userid = " + id + ";");
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
        } else user = userHashMap.get(id);
        return user;
    }

    public void cacheAllUserData() {
        ResultSet resultSet = mySQL.query("SELECT * FROM users;");
        try {
            while (resultSet.next()) {
                loadUser(resultSet.getInt("userid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int[] loadCourses(int userid) throws SQLException {
        int[] courses = new int[2];
        ResultSet resultSetUser = mySQL.query("SELECT mcourseid,scourseid FROM users WHERE userid = " + userid + ";");
        if(resultSetUser.next()) {
            courses[0] = resultSetUser.getInt("mcourseid");
            courses[1] = resultSetUser.getInt("scourseid");
        } else {
            System.out.println("ERROR IN USER MANAGER!!!");
            return null;
        }
        return courses;
    }

}

