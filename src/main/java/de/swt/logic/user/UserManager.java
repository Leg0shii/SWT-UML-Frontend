package de.swt.logic.user;

import de.swt.Server;
import de.swt.manager.Manager;
import de.swt.util.AccountType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class UserManager extends Manager<User> {
    public UserManager(Server server) {
        super(server);
    }

    /**
     * Is used to load in the user object from the database and saves it if not saved into the HashMap.
     * @param id User id.
     * @return User object.
     * @throws SQLException Is thrown when there are complications with the database.
     */
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

    /**
     * Is used to load in all user objects from the database and to save them into the HashMap.
     * @throws SQLException Is thrown when there are complications with the database.
     */
    @Override
    public void cacheAllData() throws SQLException {
        ArrayList<User> knownUsers = new ArrayList<>(getHashMap().values());
        ArrayList<User> newUsers = new ArrayList<>();
        getHashMap().clear();
        ResultSet resultSet = getMySQL().query("SELECT userId FROM users;");
        while (resultSet.next()) {
            User newUser = load(resultSet.getInt("userId"));
            if (!knownUsers.contains(newUser)){
                newUsers.add(newUser);
            }
        }
        for (User user: newUsers){
            getServer().getUserCommandManager().getUserCommandQueue().put(user.getUserId(),new LinkedBlockingQueue<>());
        }
    }
}

