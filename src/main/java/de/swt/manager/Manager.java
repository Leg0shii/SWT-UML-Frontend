package de.swt.manager;

import de.swt.Server;
import de.swt.database.AsyncMySQL;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
public abstract class Manager<Type> {

    private final AsyncMySQL mySQL;
    private final HashMap<Integer, Type> hashMap;
    private final Server server;

    public Manager(Server server) {
        this.server = server;
        this.mySQL = server.getMySQL();
        this.hashMap = new HashMap<>();
    }

    public abstract Type load(int id) throws SQLException;

    public abstract void cacheAllData() throws SQLException;

    public void cacheSingleData(int dataId){
        getHashMap().remove(dataId);
        try {
            load(dataId);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void deleteSingleData(int dataId){
        getHashMap().remove(dataId);
    }

    public ArrayList<Integer> getIds(ResultSet resultSet, String columnName) throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        while (resultSet.next()) {
            Integer id = resultSet.getInt(columnName);
            ids.add(id);
        }
        return ids;
    }

}
