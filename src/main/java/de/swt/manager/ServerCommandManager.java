package de.swt.manager;

import lombok.Getter;

import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class ServerCommandManager {

    private final LinkedBlockingQueue<CommandObject> serverCommandQueue;

    public ServerCommandManager() {
        this.serverCommandQueue = new LinkedBlockingQueue<>();
    }
}
