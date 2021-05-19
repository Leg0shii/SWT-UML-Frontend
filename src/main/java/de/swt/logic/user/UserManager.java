package de.swt.logic.user;

import de.swt.database.AsyncMySQL;
import de.swt.util.AccountType;

import java.sql.SQLException;
import java.util.HashMap;

public class UserManager {

    private HashMap<Integer, User> userHashMap;
    private AsyncMySQL mySQL;

    public UserManager(AsyncMySQL mySQL) {

        this.mySQL = mySQL;
        this.userHashMap = new HashMap<>();
    }

    public void loadAllUserData() {

        mySQL.query("SELECT * FROM users;", resultSet -> {
            try {
                while(resultSet.next()) {
                    System.out.println("IN!");
                    User user = new User();
                    int userID = resultSet.getInt("userid");
                    user.setID(userID);
                    user.setFirstname(resultSet.getString("prename"));
                    user.setSurname(resultSet.getString("surname"));
                    user.setAccountType(AccountType.valueOf(resultSet.getString("usertype")));
                    // TODO : get courses
                    userHashMap.put(userID, user);
                }
            } catch (SQLException e) { e.printStackTrace(); }
        });

    }

    public HashMap<Integer, User> getUserHashMap() {
        return userHashMap;
    }
}
