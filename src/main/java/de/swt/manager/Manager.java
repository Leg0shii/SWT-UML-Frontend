package de.swt.manager;

import de.swt.database.AsyncMySQL;
import de.swt.util.Client;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
public abstract class Manager<Type> {

    private final AsyncMySQL mySQL;
    private final HashMap<Integer, Type> hashMap;
    private final Client client;

    public Manager(Client client) {
        this.client = client;
        this.mySQL = client.getMySQL();
        this.hashMap = new HashMap<>();
    }

    public abstract Type load(int id) throws SQLException;

    public abstract void cacheAllData() throws SQLException;

    public ArrayList<Integer> getIds(ResultSet resultSet, String columnName) throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        while (resultSet.next()) {
            Integer id = resultSet.getInt(columnName);
            ids.add(id);
        }
        return ids;
    }

}
