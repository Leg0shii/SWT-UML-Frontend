package de.swt.logic;

import de.swt.database.AsyncMySQL;
import de.swt.util.AccountType;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
@Getter
public class UserManager {

    private HashMap<Integer, User> userHashMap;
    private HashMap<Integer, Course> courseHashMap;
    private final AsyncMySQL mySQL;

    public UserManager(AsyncMySQL mySQL) {

        this.mySQL = mySQL;
        this.userHashMap = new HashMap<>();
        this.courseHashMap = new HashMap<>();
    }

    public void loadUser(int id, String password) {
        AtomicInteger i = new AtomicInteger();
        if(userHashMap.containsKey(id)) {
            // check for user password
            mySQL.query("SELECT password FROM users WHERE id = " + id + ";", resultSet -> i.set(queryUserData(resultSet, id, password)));
        } else {
            System.out.println("Password is wrong!");
        }
        while(i.get() != 1);
    }

    private int queryUserData(ResultSet resultSet, int id, String password) {
        try {
            if(resultSet.next()) {
                if(resultSet.getString("password").equals(password)) {
                    userHashMap.get(id).setOnline(true);
                }
            } else System.out.println("§cUser doesnt exist");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void cacheAllUserData() {
        AtomicInteger i = new AtomicInteger();
        mySQL.query("SELECT * FROM users;", resultSet -> i.set(queryUserData(resultSet)));
        while(i.get() != 1);
    }

    private int queryUserData(ResultSet resultSet) {
        try {
            while(resultSet.next()) {
                User user = new User();
                int userID = resultSet.getInt("userid");
                user.setId(userID);
                user.setFirstname(resultSet.getString("prename"));
                user.setSurname(resultSet.getString("surname"));
                user.setAccountType(AccountType.valueOf(resultSet.getString("usertype")));
                user.setOnline(false);
                // TODO : get courses
                userHashMap.put(userID, user);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 1;
    }

    private int queryAllCourseData(int id, ResultSet resultSet) {
        try {
            if(resultSet.next()) {
                Course[] course = new Course[2];
                course[0] = courseHashMap.get(resultSet.getInt("mcourseid"));
                course[1] = courseHashMap.get(resultSet.getInt("scourseid"));
                userHashMap.get(id).setCourse(course);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 1;
    }

    public void loadAllUserCourses(int id) {
        AtomicInteger i = new AtomicInteger();
        mySQL.query("SELECT mcourseid,scourseid FROM users WHERE userid = " + id + ";", resultSet -> i.set(queryAllCourseData(id, resultSet)));
        while(i.get() != 1);
    }

}

