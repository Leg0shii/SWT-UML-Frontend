package de.swt.manager;

import lombok.Getter;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class UserCommandManager {

    private final HashMap<Integer, LinkedBlockingQueue<CommandObject>> userCommandQueue;

    public UserCommandManager() {
        this.userCommandQueue = new HashMap<>();
    }

}
