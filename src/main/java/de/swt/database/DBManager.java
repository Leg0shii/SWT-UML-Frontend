package de.swt.database;

import de.swt.Server;
import de.swt.logic.course.Course;
import de.swt.logic.group.Group;
import de.swt.logic.session.Session;
import de.swt.logic.user.User;
import de.swt.manager.CommandObject;
import de.swt.util.AccountType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class DBManager {

    private AsyncMySQL mySQL;
    private final Server server;

    public DBManager() {
        server = Server.getInstance();
    }

    /**
     * Is used to connect to the database.
     */
    private void connectToDB() {

        try {
            this.mySQL = new AsyncMySQL("5.196.174.213", 3306, "root", "qexGGHZfFzWyKYE", "swt-db");
            System.out.println("Successfully connected to database!");
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Is used to generate all needed tables for the code to work.
     * @return Working connection to the database.
     */
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
                resetServer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        return mySQL;
    }

    /**
     * Initializes the groups table.
     */
    private void initGroups() {
        // create table for groups
        mySQL.update("CREATE TABLE IF NOT EXISTS groups " +
                "(groupId INT AUTO_INCREMENT, timeTillTermination BIGINT DEFAULT 1, maxGroupSize INT DEFAULT 10," +
                "PRIMARY KEY(groupId));");
    }

    /**
     * Initializes the course table.
     */
    private void initCourses() {
        // create table for course
        mySQL.update("CREATE TABLE IF NOT EXISTS courses " +
                "(courseId INT AUTO_INCREMENT, grade INT DEFAULT 10, gradeName VARCHAR(1) DEFAULT 'a', teacherId INT DEFAULT -1," +
                "PRIMARY KEY(courseId));");
    }

    /**
     * Initializes the users table.
     */
    private void initUsers() {
        // create table for userdata
        mySQL.update("CREATE TABLE IF NOT EXISTS users " +
                "(userId INT AUTO_INCREMENT, firstname VARCHAR(255) DEFAULT 'test', surname VARCHAR(255) DEFAULT 'user', " +
                "userType VARCHAR(255) DEFAULT 'STUDENT', uPassword VARCHAR(255) DEFAULT '123', active BOOL DEFAULT 0, " +
                "PRIMARY KEY(userId));");
    }

    /**
     * Initializes the session table.
     */
    private void initSessions() {
        mySQL.update("CREATE TABLE IF NOT EXISTS sessions " +
                "(sessionId INT AUTO_INCREMENT, remainingTime BIGINT, " +
                "PRIMARY KEY(sessionId));");
    }

    /**
     * Initializes the links between different tables.
     */
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

    /**
     * ATTENTION!!! Is used to reset the entire database.
     */
    private void resetServer() {
        mySQL.update("DELETE FROM userInSession;");
        mySQL.update("DELETE FROM userInGroup;");
        mySQL.update("DELETE FROM masterInSession;");
        mySQL.update("DELETE FROM groupInSession;");
        mySQL.update("DELETE FROM sessions;");
        mySQL.update("DELETE FROM groups;");
        mySQL.update("UPDATE users SET active = false");
    }

    /**
     * Is used to update a user directly into the database.
     * @param user User object.
     * @return Id of user.
     */
    public int updateUser(User user) {
        User oldUser = null;
        try {
            oldUser = server.getUserManager().load(user.getUserId());
        } catch (SQLException ignored) {
        }
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
                        "surname = '" + surname + "', active = " + online + " WHERE userId = " + userId + ";");
            } else {
                mySQL.update("INSERT INTO users (userType, firstname, surname, active) " +
                        "VALUES ('" + accountType.toString() + "', '" + firstname + "','" + surname + "'," + online + ");");
                userId = addID();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (oldUser != null){
                updateUser(oldUser);
            }
        }
        return userId;
    }

    /**
     * Is used to update a course directly into the database.
     * @param course Course object.
     * @return Id of course.
     */
    public int updateCourse(Course course) {
        Course oldCourse = null;
        try {
            oldCourse = server.getCourseManager().load(course.getCourseId());
        } catch (SQLException ignored) {
        }
        int courseId = course.getCourseId();
        int grade = course.getGrade();
        String name = course.getGradeName();
        int teacherId = course.getTeacherId();

        ArrayList<Integer> participants = course.getUserIds();
        ArrayList<Date> dates = course.getDates();

        ResultSet rs = mySQL.query("SELECT courseId FROM courses WHERE courseId = " + courseId + ";");
        try {
            if (rs.next()) {
                mySQL.update("UPDATE courses SET grade = " + grade + ", gradeName = '" + name + "', teacherId = " + teacherId + " WHERE courseId = " + courseId + ";");
                mySQL.update("DELETE FROM userInCourse WHERE courseId = " + courseId + ";");
                mySQL.update("DELETE FROM dateInCourse WHERE courseId = " + courseId + ";");
            } else {
                mySQL.update("INSERT INTO courses (grade, gradeName, teacherId) VALUES (" + grade + ", '" + name + "'," + teacherId + ");");
                courseId = addID();
            }
            for (int userId : participants)
                mySQL.update("INSERT INTO userInCourse (userId, courseId) VALUES (" + userId + ", " + courseId + ");");
            for (Date date : dates) {
                long lDate = date.getTime();
                mySQL.update("INSERT INTO dateInCourse (date, courseId) VALUES (" + lDate + ", " + courseId + ");");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (oldCourse != null){
                updateCourse(oldCourse);
            }
        }
        return courseId;
    }

    /**
     * Is used to update a group directly into the database.
     * @param group Group object.
     * @return Id of group.
     */
    public int updateGroup(Group group) {
        Group oldGroup = null;
        try {
            oldGroup = server.getGroupManager().load(group.getGroupId());
        } catch (SQLException ignored) {
        }
        int groupId = group.getGroupId();
        long timeTillTermination = group.getTimeTillTermination();
        int maxGroupSize = group.getMaxGroupSize();
        ArrayList<Integer> participants = group.getUserIds();

        ResultSet rs = mySQL.query("SELECT groupId FROM groups WHERE groupId = " + groupId + ";");
        try {
            if (rs.next()) {
                mySQL.update("UPDATE groups SET timeTillTermination = " + timeTillTermination + ", maxGroupSize = " + maxGroupSize + " WHERE groupId = " + groupId + ";");
                mySQL.update("DELETE FROM userInGroup WHERE groupId = " + groupId + ";");
            } else {
                mySQL.update("INSERT INTO groups (timeTillTermination, maxGroupSize) VALUES (" + timeTillTermination + ", " + maxGroupSize + ");");
                groupId = addID();
                mySQL.update("INSERT INTO groupInSession (sessionId, groupId) VALUES (" + group.getSessionId() + ", " + groupId + ");");
            }
            for (int userId : participants) {
                mySQL.update("INSERT INTO userInGroup (userId, groupId) VALUES (" + userId + ", " + groupId + ");");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (oldGroup != null){
                updateGroup(oldGroup);
            }
        }
        return groupId;
    }

    /**
     * Is used to delete a group object from the database and all its linked parts.
     * @param groupId Id of group.
     */
    public void deleteGroup(int groupId) {
        mySQL.update("DELETE FROM userInGroup WHERE groupId = " + groupId + ";");
        mySQL.update("DELETE FROM groupInSession WHERE groupId = " + groupId + ";");
        mySQL.update("DELETE FROM groups WHERE groupId = " + groupId + ";");
    }

    /**
     * Is used to delete a session object from the database and all its linked parts.
     * @param sessionId Id of session.
     */
    public void deleteSession(int sessionId) {
        mySQL.update("DELETE FROM userInSession WHERE sessionId = " + sessionId + ";");
        mySQL.update("DELETE FROM masterInSession WHERE sessionId =  " + sessionId + ";");
        mySQL.update("DELETE FROM sessions WHERE sessionId = " + sessionId + ";");
    }

    /**
     * Is used to delete a course object from the database and all its linked parts.
     * @param courseId Id of course.
     */
    public void deleteCourse(int courseId) {
        mySQL.update("DELETE FROM userInCourse WHERE courseId = " + courseId + ";");
        mySQL.update("DELETE FROM dateInCourse WHERE courseId = " + courseId + ";");
        mySQL.update("DELETE FROM courses WHERE courseId = " + courseId + ";");
    }

    /**
     * Is used to update a session directly into the database.
     * @param session Session object.
     * @return Id of session.
     */
    public int updateSession(Session session) {
        Session oldSession = null;
        try {
            oldSession = server.getSessionManager().load(session.getSessionId());
        } catch (SQLException ignored) {
        }
        int sessionId = session.getSessionId();
        long remainingTime = session.getRemainingTime();

        ArrayList<Integer> participants = session.getUserIds();
        ArrayList<Integer> masters = session.getMasterIds();
        ArrayList<Integer> groups = session.getGroupIds();

        ResultSet rs = mySQL.query("SELECT sessionId FROM sessions WHERE sessionId = " + sessionId + ";");
        try {
            if (rs.next()) {
                // insert session into db
                mySQL.update("UPDATE sessions SET remainingTime = " + remainingTime + " WHERE sessionId = " + sessionId + ";");
                mySQL.update("DELETE FROM userInSession WHERE sessionId = " + sessionId + ";");
                mySQL.update("DELETE FROM masterInSession WHERE sessionId = " + sessionId + ";");
            } else {
                mySQL.update("INSERT INTO sessions (remainingTime) VALUES (" + remainingTime + ");");
                sessionId = addID();
            }
            for (int participant : participants)
                mySQL.update("INSERT INTO userInSession (sessionId, userId) VALUES (" + sessionId + ", " + participant + ");");
            for (int master : masters)
                mySQL.update("INSERT INTO masterInSession (sessionId, userId) VALUES (" + sessionId + ", " + master + ");");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            if (oldSession != null){
                updateSession(oldSession);
            }
        }
        return sessionId;
    }

    /**
     * Is used to add an id to the object after it was created the first time.
     * @return Newly created object id.
     * @throws SQLException Is thrown when there are complications with the database.
     */
    private int addID() throws SQLException {
        ResultSet rs1 = mySQL.query("SELECT LAST_INSERT_ID();");
        if (rs1.next()) return rs1.getInt("last_insert_id()");
        System.out.println("ERROR in Server: DBManager addID() returned -1");
        return -1;
    }

}

