package de.swt.logic.user;

import de.swt.database.AsyncMySQL;
import de.swt.util.AccountType;
import de.swt.util.Client;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

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

    public boolean userLogin(int userid, String password) {
        ResultSet resultSet = mySQL.query("SELECT upassword FROM users WHERE userid = " + userid + ";");
        // get salt and hash later

        try {
            if (resultSet.next()) {
                if(resultSet.getString("upassword").equals(hashLogin(userid, password))) {
                    User user = userHashMap.get(userid);
                    user.setOnline(true);
                    try {
                        // userid doesnt matter if update = true
                        client.server.sendUser(user, 0, true);
                        client.userid = userid;
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

    private String hashLogin(int userid, String password){
        MessageDigest hasher = null;
        try {
            hasher = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace();
        }
        assert hasher != null;
        return Arrays.toString(hasher.digest((userid + password).getBytes(StandardCharsets.UTF_8))); // This is the value that shall land in the database as "password"
    }

    public void userLogout() {

        User user = userHashMap.get(client.userid);
        user.setOnline(false);
        user.setInCourse(false);
        user.setInGroup(false);

        try {
            client.server.sendUser(user, 0, true);
            System.out.println("LOGUT SUCC...");
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("COULDNT USE sendUser()");
        }
    }

    public boolean checkSessionStarted(Date date) {

        /*Reads an individual date*/
        System.out.println("dd.mm.yyyy HH:mm" + " Please Enter Date!");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        try {
            date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(str);
            System.out.println(dateFormat.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*Reads the current date*/
        Date date2 = new Date();
        System.out.println(dateFormat.format(date2));

        /*Adds 1 hour and 30 minutes to the current time of the date*/
        Date date3 = new Date(date2.getTime() + TimeUnit.HOURS.toMillis(1) + TimeUnit.MINUTES.toMillis(30));
        System.out.println(dateFormat.format(date3));

        /*Checks whether the individual date is within the range of the
        current date and the current date with the added 1 hour and 30 minutes*/
        if (date.before(date3) && date.after(date2)) {
            return true;
        } else {
            return false;
        }
    }

}

