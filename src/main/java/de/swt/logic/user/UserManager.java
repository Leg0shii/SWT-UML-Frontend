package de.swt.logic.user;

import de.swt.Server;
import de.swt.manager.Manager;
import de.swt.util.AccountType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager extends Manager {
    public UserManager(Server server) {
        super(server);
    }

    @Override
    public Object load(int id) throws SQLException {
        if (getHashMap().containsKey(id)) {
            return getHashMap().get(id);
        } else {
            ResultSet resultSet = getMySQL().query("SELECT * FROM users WHERE userId = " + id + ");");
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
        ResultSet resultSet = getMySQL().query("SELECT userId FROM users;");
        while (resultSet.next()) {
            load(resultSet.getInt("userId"));
        }
    }
}

