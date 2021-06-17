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

    /**
     * @param server The server parameter.
     */
    public Manager(Server server) {
        this.server = server;
        this.mySQL = server.getMySQL();
        this.hashMap = new HashMap<>();
    }

    /**
     * Is used to load a new object of type Type from the database or HashMap.
     * @param id Object id.
     * @return Object of type Type.
     * @throws SQLException Is thrown when there are complications with the database.
     */
    public abstract Type load(int id) throws SQLException;

    /**
     * Is used to load all available data from the database into the HashMap of corresponding manager.
     * @throws SQLException Is thrown when there are complications with the database.
     */
    public abstract void cacheAllData() throws SQLException;

    /**
     * Is used to replace an object based on its id with a new one from the database.
     * @param dataId Object id.
     */
    public void cacheSingleData(int dataId){
        getHashMap().remove(dataId);
        try {
            load(dataId);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Deletes an object based on its id from the HashMap.
     * @param dataId Object id.
     */
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
