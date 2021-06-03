package de.swt.database;

import de.swt.logic.course.Course;
import de.swt.logic.group.Group;
import de.swt.logic.session.Session;
import de.swt.logic.user.User;
import de.swt.util.AccountType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class DBManager {

    private AsyncMySQL mySQL;

    private void connectToDB() {

        try {
            // usually should be imported from config file but bruh nah
            this.mySQL = new AsyncMySQL("5.196.174.213", 3306, "root", "qexGGHZfFzWyKYE", "testdb");
            System.out.println("Successfully connected to database!");
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    }

    public AsyncMySQL initTables() {

        connectToDB();

        initUsers();
        initCourses();
        initGroups();
        initSessions();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                initLinks();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        return mySQL;
    }

    private void initGroups() {
        // create table for groups
        mySQL.update("CREATE TABLE IF NOT EXISTS groups " +
                "(groupId INT AUTO_INCREMENT, timeTillTermination BIGINT DEFAULT 1, maxGroupSize INT DEFAULT 10," +
                "PRIMARY KEY(groupId));");
    }

    private void initCourses() {
        // create table for course
        mySQL.update("CREATE TABLE IF NOT EXISTS courses " +
                "(courseId INT AUTO_INCREMENT, grade INT DEFAULT 10, gradeName VARCHAR(1) DEFAULT 'a', teacherId INT DEFAULT -1," +
                "PRIMARY KEY(courseId));");
    }

    private void initUsers() {
        // create table for userdata
        mySQL.update("CREATE TABLE IF NOT EXISTS users " +
                "(userId INT AUTO_INCREMENT, firstname VARCHAR(255) DEFAULT 'test', surname VARCHAR(255) DEFAULT 'user', " +
                "userType VARCHAR(255) DEFAULT 'STUDENT', uPassword VARCHAR(255) DEFAULT '123', active BOOL DEFAULT 0, " +
                "PRIMARY KEY(userId));");
    }

    private void initSessions() {
        mySQL.update("CREATE TABLE IF NOT EXISTS sessions " +
                "(sessionId INT AUTO_INCREMENT, remainingTime BIGINT, " +
                "PRIMARY KEY(sessionId));");
    }

    private void initLinks() {
        mySQL.update("CREATE TABLE IF NOT EXISTS userInCourse " +
                "(userId INT NOT NULL, courseId INT NOT NULL, " +
                "PRIMARY KEY(userId, courseId)," +
                "FOREIGN KEY(userId) REFERENCES users(userId)," +
                "FOREIGN KEY(courseId) REFERENCES courses(courseId));");

        mySQL.update("CREATE TABLE IF NOT EXISTS dateInCourse " +
                "(courseId INT NOT NULL, date BIGINT NOT NULL, " +
                "PRIMARY KEY(courseId, date)," +
                "FOREIGN KEY(courseId) REFERENCES courses(courseId));");

        mySQL.update("CREATE TABLE IF NOT EXISTS groupInSession " +
                "(groupId INT NOT NULL, sessionId INT NOT NULL, " +
                "PRIMARY KEY(groupId, sessionId)," +
                "FOREIGN KEY(groupId) REFERENCES groups(groupId)," +
                "FOREIGN KEY(sessionId) REFERENCES sessions(sessionId));");

        mySQL.update("CREATE TABLE IF NOT EXISTS masterInSession " +
                "(sessionId INT NOT NULL, userId INT NOT NULL," +
                "PRIMARY KEY(sessionId, userId)," +
                "FOREIGN KEY(userId) REFERENCES users(userId)," +
                "FOREIGN KEY(sessionId) REFERENCES sessions(sessionId));");

        mySQL.update("CREATE TABLE IF NOT EXISTS userInGroup " +
                "(userId INT NOT NULL, groupId INT NOT NULL, " +
                "PRIMARY KEY(userId, groupId)," +
                "FOREIGN KEY(userId) REFERENCES users(userId)," +
                "FOREIGN KEY(groupId) REFERENCES groups(groupId));");

        mySQL.update("CREATE TABLE IF NOT EXISTS userInSession " +
                "(userId INT NOT NULL, sessionId INT NOT NULL, " +
                "PRIMARY KEY(userId, sessionId)," +
                "FOREIGN KEY(userId) REFERENCES users(userId)," +
                "FOREIGN KEY(sessionId) REFERENCES sessions(sessionId));");
    }

    public int updateUser(User user) {

        int userId = user.getUserId();
        AccountType accountType = user.getAccountType();
        String firstname = user.getFirstname();
        String surname = user.getSurname();
        boolean online = user.isActive();

        ResultSet rs = mySQL.query("SELECT userId FROM users WHERE userId = " + userId);
        try {
            if (rs.next()) {
                mySQL.update("UPDATE users " +
                        "SET userType = '" + accountType.toString() + "', firstname = '" + firstname + "'," +
                        "surname = '" + surname + ", active = " + online + " WHERE userId = " + userId + ";");
            } else {
                mySQL.update("INSERT INTO users (userType, firstname, surname, active) " +
                        "VALUES ('" + accountType.toString() + "', '" + firstname + "','" + surname + "'," + online + ");");
                userId = addID();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    public int updateCourse(Course course) {

        int courseId = course.getCourseId();
        int grade = course.getGrade();
        String name = course.getGradeName();
        int teacherId = course.getTeacherId();

        ArrayList<Integer> participants = course.getUserIds();
        ArrayList<Date> dates = course.getDates();

        ResultSet rs = mySQL.query("SELECT courseId FROM courses WHERE courseId = " + courseId);
        try {
            if (rs.next()) {
                mySQL.update("UPDATE courses SET grade = " + grade + ", gradeName = '" + name + "', teacherId = " + teacherId + " WHERE courseId = " + courseId + ";");
                mySQL.update("DELETE FROM userInCourse WHERE courseId = " + courseId + ";");
                mySQL.update("DELETE FROM dateInCourse WHERE courseId = " + courseId + ";");
            } else {
                mySQL.update("INSERT INTO courses (grade, gradeName) VALUES (" + grade + ", '" + name + "')");
                courseId = addID();
            }
            for (int userId : participants)
                mySQL.update("INSERT INTO userInCourse (userId, courseId) VALUES (" + userId + ", " + courseId + ");");
            for (Date date : dates)
                mySQL.update("INSERT INTO dateInCourse (date, courseId) VALUES (" + date + ", " + courseId + ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseId;
    }

    public int updateGroup(Group group) {

        int groupId = group.getGroupId();
        long timeTillTermination = group.getTimeTillTermination();
        int maxGroupSize = group.getMaxGroupSize();
        ArrayList<Integer> participants = group.getUserIds();

        ResultSet rs = mySQL.query("SELECT groupId FROM groups WHERE groupId = " + groupId);
        try {
            if (rs.next()) {
                mySQL.update("UPDATE groups SET timeTillTermination = " + timeTillTermination + ", maxGroupSize = " + maxGroupSize + " WHERE groupId = " + groupId + ";");
                mySQL.update("DELETE FROM userInGroup WHERE groupId = " + groupId + ";");
            } else {
                mySQL.update("INSERT INTO groups (timeTillTermination, maxGroupSize) VALUES (" + timeTillTermination + ", " + maxGroupSize + ");");
            }
            for (int userId : participants)
                mySQL.update("INSERT INTO userInGroup (userId, groupId) VALUES (" + userId + ", " + groupId + ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupId;
    }

    public void deleteGroup(int groupId) {
        mySQL.update("DELETE FROM groups WHERE groupId = " + groupId + ";");
        mySQL.update("DELETE FROM userInGroups WHERE groupId = " + groupId + ";");
    }

    public void deleteSession(int sessionId) {
        mySQL.update("DELETE FROM sessions WHERE sessionId = " + sessionId + ";");
        mySQL.update("DELETE FROM userInSession WHERE sessionId = " + sessionId + ";");
    }

    public void deleteCourse(int courseId){
        mySQL.update("DELETE FROM courses WHERE courseId = " + courseId + ";");
        mySQL.update("DELETE FROM userInCourse WHERE courseId = " + courseId + ";");
        mySQL.update("DELETE FROM dateInCourse WHERE courseId = " + courseId + ";");
    }

    public int updateSession(Session session) {
        int sessionId = session.getSessionId();
        long remainingTime = session.getRemainingTime();

        ArrayList<Integer> participants = session.getUserIds();
        ArrayList<Integer> masters = session.getMasterIds();
        ArrayList<Integer> groups = session.getGroupIds();

        ResultSet rs = mySQL.query("SELECT sessionId FROM sessions WHERE sessionId = " + sessionId);
        try {
            if (rs.next()) {
                // insert session into db
                mySQL.update("UPDATE sessions SET sessionId = " + sessionId + ", remainingTime = " + remainingTime + ";");
                mySQL.update("DELETE FROM userInSession WHERE sessionId = " + sessionId + ";");
                mySQL.update("DELETE FROM masterInSession WHERE sessionId = " + sessionId + ";");
                mySQL.update("DELETE FROM groupInSession WHERE sessionId = " + sessionId + ";");
            } else {
                mySQL.update("INSERT INTO sessions (remainingTime) VALUES " + remainingTime + ");");
                sessionId = addID();
            }
            for (int participant : participants)
                mySQL.update("INSERT INTO userInSession (sessionId, userId) VALUES (" + sessionId + ", " + participant + ")");
            for (int master : masters)
                mySQL.update("INSERT INTO masterInSession (sessionId, userId) VALUES (" + sessionId + ", " + master + ")");
            for (int group : groups)
                mySQL.update("INSERT INTO groupInSession (sessionId, groupId) VALUES (" + sessionId + ", " + group + ")");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sessionId;
    }

    private int addID() throws SQLException {
        ResultSet rs1 = mySQL.query("SELECT LAST_INSERT_ID()");
        if (rs1.next()) return rs1.getInt("last_insert_id()");
        System.out.println("ERROR in Server: DBManager addID() returned -1");
        return -1;
    }

}

