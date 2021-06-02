package de.swt.manager;

import lombok.Getter;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class UserCommandMananger {

    private final HashMap<Integer, LinkedBlockingQueue<CommandObject>> userCommandQueue;

    public UserCommandMananger() {
        this.userCommandQueue = new HashMap<>();
    }

}
