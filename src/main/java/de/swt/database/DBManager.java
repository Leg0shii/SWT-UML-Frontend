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
            this.mySQL = new AsyncMySQL("5.196.174.213", 3306, "root", "qexGGHZfFzWyKYE", "serverpro_db");
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
        // create table for session
        /* mySQL.update("CREATE TABLE IF NOT EXISTS sessions " +
            "(sessionid INT AUTO_INCREMENT, master VARCHAR(25), participants VARCHAR(255), groups VARCHAR(255), remainingtime VARCHAR(45), " +
            "PRIMARY KEY(idsession));");
         */

        return mySQL;
    }

    private void initGroups() {

        mySQL.update("CREATE TABLE IF NOT EXISTS useringroup " +
            "(useringroupid INT AUTO_INCREMENT, userid INT NOT NULL, groupid INT NOT NULL, " +
            "PRIMARY KEY(useringroupid));");

        // create table for groups
        mySQL.update("CREATE TABLE IF NOT EXISTS groups " +
            "(groupid INT AUTO_INCREMENT, ttt INT DEFAULT 1, maxgs INT DEFAULT 10, useringroup INT NOT NULL, " +
            "PRIMARY KEY(groupid), " +
            "FOREIGN KEY (useringroupid) REFERENCES useringroup(useringroupid));");
    }

    private void initCourses() {
        /* mySQL.update("CREATE TABLE IF NOT EXISTS activeuserincourse " +
            "(activeuserincourseid INT AUTO_INCREMENT, userid INT, courseid INT, " +
            "PRIMARY KEY(activeuserincourseid));"); */

        mySQL.update("CREATE TABLE IF NOT EXISTS userincourse " +
            "(userincourseid INT AUTO_INCREMENT, userid INT NOT NULL, courseid INT NOT NULL, " +
            "PRIMARY KEY(userincourseid));");

        mySQL.update("CREATE TABLE IF NOT EXISTS dateincourse " +
            "(dateincourseid INT AUTO_INCREMENT, courseid INT NOT NULL, date DATE NOT NULL, " +
            "PRIMARY KEY(dateincourseid));");

        mySQL.update("CREATE TABLE IF NOT EXISTS groupincourse " +
            "(groupincourseid INT AUTO_INCREMENT, groupid INT NOT NULL, courseid INT NOT NULL, " +
            "PRIMARY KEY(groupincourseid));");

        mySQL.update("CREATE TABLE IF NOT EXISTS masterincourse " +
            "(masterincourseid INT AUTO_INCREMENT, courseid INT NOT NULL, userid INT NOT NULL" +
            "PRIMARY KEY(masterincourseid));");

        // create table for course
        mySQL.update("CREATE TABLE IF NOT EXISTS courses " +
            "(courseid INT AUTO_INCREMENT, grade INT DEFAULT 10, gradename VARCHAR(1) DEFAULT 'a', userincourseid INT NOT NULL, " +
            "dateincourseid INT NOT NULL, masterincourseid INT NOT NULL, groupincourseid INT NOT NULL, " +
            "teacherid INT NOT NULL, remainingtime INT DEFAULT 1, " +
            "PRIMARY KEY(courseid), " +
            "FOREIGN KEY (groupincourseid) REFERENCES groupincourse(groupincourseid), " +
            "FOREIGN KEY (userincourseid) REFERENCES userincourse(userincourseid), " +
            "FOREIGN KEY (dateincourseid) REFERENCES dateincourse(dateincourseid)," +
            "FOREIGN KEY (groupincourseid) REFERENCES masterincourse(groupincourseid)" +
            ");");
    }

    private void initUsers() {
        // create table for userdata
        mySQL.update("CREATE TABLE IF NOT EXISTS users " +
            "(userid INT AUTO_INCREMENT, prename VARCHAR(255) DEFAULT 'test', surname VARCHAR(255) DEFAULT 'user', " +
            "usertype VARCHAR(255) DEFAULT STUDENT, upassword VARCHAR(255) DEFAULT '123', active BOOL DEFAULT 0, " +
            "PRIMARY KEY(userid));");
    }


    public int updateUser(User user) {

        int id = user.getId();
        AccountType accountType = user.getAccountType();
        String firstname = user.getFirstname();
        String surname = user.getSurname();
        String course = courseToString(user.getCourse());
        int online = user.isOnline() ? 1 : 0;
        int isgroup = user.isInGroup() ? 1 : 0;
        int iscourse = user.isInCourse() ? 1 : 0;

        ResultSet rs = mySQL.query("SELECT userid FROM users WHERE userid = " + id);
        try {
            if (rs.next()) {
                mySQL.update("UPDATE users SET usertype = '" + accountType.toString() +
                        "',prename='" + firstname + "',surname='" + surname + "',courseids='" + course +
                        "',isonline=" + online + ",iscourse=" + iscourse + ",isgroup=" + isgroup + " WHERE userid = " + id + ";");
            } else {
                mySQL.update("INSERT INTO users (usertype, prename, surname, courseids, isonline, iscourse, isgroup) VALUES " +
                        "(" + accountType.toString() + ", '" + firstname + "','" + surname + "'," + course +
                        ", " + online + ", " + isgroup + ", " + iscourse + ")");
                ResultSet rs1 = mySQL.query("SELECT LAST_INSERT_ID()");
                if (rs1.next()){
                    id = rs1.getInt("last_insert_id()");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public int updateCourse(Course course) {

        int id = course.getId();
        int grade = course.getGrade();
        String name = course.getName();
        String dates = datesToString(course.getDates());
        User teacher = course.getTeacher();

        ResultSet rs = mySQL.query("SELECT courseid FROM courses WHERE courseid = " + id);
        try {
            if (rs.next()) {
                mySQL.update("UPDATE courses SET grade = " + grade + ",gradename='" + name + "',date='" + dates
                        + "',teacherid=" + teacher.getId() + " WHERE courseid = " + id + ";");
            } else {
                mySQL.update("INSERT INTO courses (grade, gradename, date, teacherid) VALUES " +
                        "(" + grade + ", '" + name + "','" + dates + "'," + teacher.getId() + ")");
                ResultSet rs1 = mySQL.query("SELECT LAST_INSERT_ID()");
                if (rs1.next()){
                    id = rs1.getInt("last_insert_id()");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public int updateGroups(Group group) {

        int groupid = group.getId();
        int courseid = group.getCourseID();
        long timeTillTermination = group.getTimeTillTermination();
        int maxGroupSize = group.getMaxGroupSize();
        String participants = participantsToString(group.getParticipants());

        ResultSet rs = mySQL.query("SELECT groupid FROM groups WHERE groupid = " + groupid);
        try {
            if (rs.next()) {
                mySQL.update("UPDATE groups SET courseid = " + courseid + ",ttt=" + timeTillTermination
                        + ",maxGS=" + maxGroupSize + ",participants='" + participants + "' WHERE groupid = " + groupid + ";");
            } else {
                mySQL.update("INSERT INTO groups (courseid, ttt, maxGS, participants) VALUES " +
                        "(" + courseid + ", " + timeTillTermination + ", " + maxGroupSize + ", '" + participants + "');");
                ResultSet rs1 = mySQL.query("SELECT LAST_INSERT_ID()");
                if (rs1.next()){
                    groupid = rs1.getInt("last_insert_id()");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupid;
    }

    public void deleteGroup(int groupid) {
        mySQL.update("DELETE FROM groups WHERE groupid = " + groupid + ";");
    }

    public void deleteSession(int sessionid) {
        mySQL.update("DELETE FROM sessions WHERE idsession = " + sessionid + ";");
    }

    public int updateSessions(Session session) {
        int sessionid = session.getId();
        String participants = participantsToString(session.getParticipants());
        String master = participantsToString(session.getMaster());
        String groups = participantsToString(session.getGroups());
        long remainingtime = session.getRemainingTime();

        ResultSet rs = mySQL.query("SELECT idsession FROM sessions WHERE idsession = " + sessionid);
        try {
            if (rs.next()) {
                mySQL.update("UPDATE sessions SET idsession = \"" + sessionid + "\",participants=\"" + participants + "\",master=\"" + master
                        + "\",groups=\"" + groups + "\",remainingtime=\"" + remainingtime + "\";");
            } else {
                mySQL.update("INSERT INTO sessions (participants, master, groups, remainingtime) VALUES " +
                        "('" + participants + "', '" + master + "', '" + groups + "', '" + remainingtime + "');");
                ResultSet rs1 = mySQL.query("SELECT LAST_INSERT_ID()");
                if (rs1.next()){
                    sessionid = rs1.getInt("last_insert_id()");
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sessionid;
    }

    private String participantsToString(ArrayList<Integer> part) {
        String participants = "";
        for (int p : part) {
            participants = p + ";" + participants;
        }
        return participants;
    }

    private String datesToString(ArrayList<Date> dates) {
        StringBuilder stringOfDates = new StringBuilder();
        for (Date date : dates) {
            stringOfDates.insert(0, date.getTime() + ";");
        }
        return stringOfDates.toString();
    }

    // TODO :
    private String courseToString(ArrayList<Integer> courses) {
        StringBuilder courseString = new StringBuilder();
        for (int course : courses) {
            courseString.insert(0, course + ";");
        }
        return courseString.toString();
    }

}

