package de.swt.manager;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class UserCommandMananger {

    private final HashMap<Integer, ArrayList<CommandObject>> userCommandQueue;

    public UserCommandMananger() {
        this.userCommandQueue = new HashMap<>();
    }

}
