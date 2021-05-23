package de.swt.manager;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class CommandManager {

    private HashMap<Integer, ArrayList<String>> commandHashMap;

    public CommandManager() {
        this.commandHashMap = new HashMap<>();
    }

}
