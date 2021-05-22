package de.swt.logic;

import de.swt.database.AsyncMySQL;
import de.swt.util.AccountType;
import de.swt.util.Client;
import lombok.Getter;
import lombok.Setter;

import java.rmi.RemoteException;
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

    private int[] loadCourses(int userid) throws SQLException {
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

    public boolean userLogin(int userid, String password) {
        ResultSet resultSet = mySQL.query("SELECT upassword FROM users WHERE userid = " + userid + ";");
        // get salt and hash later

        try {
            if (resultSet.next()) {
                if(resultSet.getString("upassword").equals(password)) {
                    User user = userHashMap.get(userid);
                    user.setOnline(true);
                    try {
                        client.server.sendUser(user, userid, true);
                        System.out.println("LOGIN SUCC...");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        System.out.println("COULDNT USE sendUser()");
                        return false;
                    }
                    return true;
                }
            } else {
                System.out.println("LOGIN FAILED...");
                return false;
            }
        } catch (SQLException ignored) {
            System.out.println("LOGIN FAILED...");
            return false;
        }
        return false;
    }

    public void userLogout() {
        userHashMap.get(client.userid).setOnline(false);
    }

    public boolean canJoinCourse(int courseid) {
        return userHashMap.get(client.userid).getCourse()[0] == courseid
            || userHashMap.get(client.userid).getCourse()[1] == courseid;
    }

}

