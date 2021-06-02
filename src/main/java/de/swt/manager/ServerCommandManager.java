package de.swt.manager;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class ServerCommandManager {

    private final HashMap<Integer, CommandObject> serverCommandQueue;

    public ServerCommandManager() {
        this.serverCommandQueue = new HashMap<>();
    }
}
