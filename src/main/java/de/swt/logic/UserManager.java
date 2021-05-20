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

    public Course[] loadCourses(int userid) throws SQLException {
        Course[] courses = new Course[2];
        ResultSet resultSetUser = mySQL.query("SELECT mcourseid,scourseid FROM users WHERE userid = " + userid + ";");
        ResultSet resultSetCourseM;
        ResultSet resultSetCourseS;

        if (resultSetUser.next()) {
            resultSetCourseM = mySQL.query("SELECT * FROM courses WHERE courseid = " + resultSetUser.getInt("mcourseid") + ";");
            resultSetCourseS = mySQL.query("SELECT * FROM courses WHERE courseid = " + resultSetUser.getInt("scourseid") + ";");
        } else {
            System.out.println("ERROR OCCURED WHILE LOADING IN COURSES!!!");
            return null;
        }

        if (resultSetCourseM.next() && resultSetCourseS.next()) {
            courses[0] = client.courseManager.loadCourse(resultSetCourseM.getInt("courseid"));
            courses[1] = client.courseManager.loadCourse(resultSetCourseS.getInt("courseid"));
        } else {
            System.out.println("ERROR OCCURED WHILE LOADING IN COURSES!!!");
            return null;
        }
        return courses;
    }

}

