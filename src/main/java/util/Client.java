package util;

import database.AsyncMySQL;
import database.DBManager;
import logic.user.User;
import logic.user.UserManager;

public class Client {

    public AsyncMySQL mySQL;
    public User user;
    public DBManager dbManager;
    public UserManager userManager;
    public static Client instance;

    public void onStart() throws InterruptedException {

        instance = this;
        dbManager = new DBManager();

        mySQL = dbManager.initTables();

        int userID = 0; //get from login later
        userManager = new UserManager(mySQL);
        userManager.loadAllUserData();
        user = userManager.getUserHashMap().get(userID);

        System.out.println("IN2"); // shows that IN2 is called before the "IN" which are inside userManager.loadAllUserData();
        // just a lazy thing because the db connection is threaded so it runs in the background
        // meanwhile code continues and it would call for loop with no data inside
        // this could be solved by waiting 10s when loggin in (loading screen for example)
        Thread.sleep(1000);

        System.out.println("IN3");
        for(int key : userManager.getUserHashMap().keySet()) {
            System.out.println(userManager.getUserHashMap().get(key).getFullName());
        }

    }

    public void onDisable() {



    }

    public static Client getInstance() {
        return instance;
    }

}
