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
        } catch (SQLException | ClassNotFoundException throwables) { throwables.printStackTrace(); }

    }

    public AsyncMySQL initTables() {

        connectToDB();

        // create table for userdata
        // decided to create two fields for courses instead of creating another table and saving all users that would be in a course in there
        mySQL.update("CREATE TABLE IF NOT EXISTS users (userid INT AUTO_INCREMENT" +
            ", prename VARCHAR(255), surname VARCHAR(255), usertype VARCHAR(255), courseids VARCHAR(255)" +
            ", upassword VARCHAR(255), isonline BOOL, iscourse BOOL, isgroup BOOL, PRIMARY KEY(userid));");

        // create table for courseata
        // date format: STRING: "millis;millis;millis"
        mySQL.update("CREATE TABLE IF NOT EXISTS courses (courseid INT AUTO_INCREMENT, grade INT" +
            ", gradename VARCHAR(10), date VARCHAR(500), teacherid INT, PRIMARY KEY(courseid));");

        //create table for groupdata
        // participants format: STRING: "id;id;id;id;id"
        mySQL.update("CREATE TABLE IF NOT EXISTS groups (groupid INT AUTO_INCREMENT, courseid INT" +
            ", ttt INT, maxGS INT, participants VARCHAR(255), PRIMARY KEY(groupid));");

        return mySQL;
    }

    public void updateUser(User user) {

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
            if(rs.next()) {
                mySQL.update("UPDATE users SET usertype = '" + accountType.toString() +
                    "',prename='" + firstname + "',surname='" + surname + "',courseids='" + course +
                    "',isonline=" + online + ",iscourse=" + iscourse + ",isgroup=" + isgroup + " WHERE userid = " + id + ";");
            } else {
                mySQL.update("INSERT INTO users (usertype, prename, surname, courseids, isonline, iscourse, isgroup) VALUES " +
                    "(" + accountType.toString() + ", '" + firstname + "','" + surname + "'," + course +
                    ", " + online + ", " + isgroup + ", " + iscourse + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCourse(Course course) {

        int id = course.getId();
        int grade = course.getGrade();
        String name = course.getName();
        String dates = datesToString(course.getDates());
        User teacher = course.getTeacher();

        ResultSet rs = mySQL.query("SELECT courseid FROM courses WHERE courseid = " + id);
        try {
            if(rs.next()) {
                mySQL.update("UPDATE courses SET grade = " + grade + ",gradename='" + name + "',date='" + dates
                    + "',teacherid=" + teacher.getId() + " WHERE courseid = " + id + ";");
            } else {
                mySQL.update("INSERT INTO courses (grade, gradename, date, teacherid) VALUES " +
                    "(" + grade + ", '" + name + "','" + dates + "'," + teacher.getId() + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGroups(Group group) {

        int groupid = group.getId();
        int courseid = group.getCourseID();
        int timeTillTermination = group.getTimeTillTermination();
        int maxGroupSize = group.getMaxGroupSize();
        String participants = participantsToString(group.getParticipants());

        mySQL.update("UPDATE groups SET courseid = " + courseid + ",ttt=" + timeTillTermination
            + ",maxGS=" + maxGroupSize + ",participants='" + participants + "' WHERE groupid = " + groupid + ";");
    }

    public void updateSessions(Session session){
        int sessionid = session.getId();
        String participants = participantsToString(session.getParticipants());
        String master = participantsToString(session.getMaster());
        String groups = participantsToString(session.getGroups());
        int remainingtime = session.getRemainingTime();

        mySQL.update("UPDATE sessions SET idsession = \""+sessionid+"\",participants=\""+participants+"\",master=\""+master
                +"\",groups=\""+groups+"\",remainingtime=\""+remainingtime+"\";");

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
        for(Date date : dates) {
            stringOfDates.insert(0, date.getTime() + ";");
        }
        return stringOfDates.toString();
    }

    // TODO :
    private String courseToString(ArrayList<Integer> courses) {
        StringBuilder courseString = new StringBuilder();
        for(int course : courses) {
            courseString.insert(0, course + ";");
        }
        return courseString.toString();
    }

}

