package de.swt.logic.user;

import de.swt.logic.course.Course;
import de.swt.logic.session.Session;
import de.swt.manager.Manager;
import de.swt.util.AccountType;
import de.swt.util.Client;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserManager extends Manager<User> {
    public UserManager(Client client) {
        super(client);
    }

    @Override
    public User load(int id) throws SQLException {
        if (getHashMap().containsKey(id)) {
            return getHashMap().get(id);
        } else {
            ResultSet resultSet = getMySQL().query("SELECT * FROM users WHERE userId = " + id + ";");
            resultSet.next();
            User newUser = new User();
            newUser.setUserId(id);
            newUser.setAccountType(AccountType.valueOf(resultSet.getString("userType")));
            newUser.setFirstname(resultSet.getString("firstname"));
            newUser.setSurname(resultSet.getString("surname"));
            newUser.setActive(resultSet.getBoolean("active"));

            getHashMap().put(id, newUser);

            return newUser;
        }
    }

    @Override
    public void cacheAllData() throws SQLException {
        getHashMap().clear();
        var hashMap = new HashMap<Integer, User>();
        try {
            hashMap = getClient().getServer().getUsers();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        for (Integer key : hashMap.keySet()){
            getHashMap().put(key, hashMap.get(key));
        }
        /*
        getHashMap().clear();
        ResultSet resultSet = getMySQL().query("SELECT userId FROM users;");
        while (resultSet.next()){
            load(resultSet.getInt("userId"));
        }

         */
    }

    public User login(int userId, String password) {
        User user = null;
        try {
            ResultSet resultSet = getMySQL().query("SELECT uPassword FROM users WHERE userId = " + userId + ";");
            if (resultSet.next()) {
                if (resultSet.getString("uPassword").equals(password)) {
                    user = load(userId);
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return user;
    }
}

