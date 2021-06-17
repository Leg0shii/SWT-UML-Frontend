package de.swt.manager;

import lombok.Getter;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class UserCommandManager {

    private final HashMap<Integer, LinkedBlockingQueue<CommandObject>> userCommandQueue;

    /**
     * Initializes a new HashMap for user commands that users can access through RMI.
     */
    public UserCommandManager() {
        this.userCommandQueue = new HashMap<>();
    }

}
