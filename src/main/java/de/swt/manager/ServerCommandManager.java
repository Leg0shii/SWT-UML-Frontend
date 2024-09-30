package de.swt.manager;

import lombok.Getter;

import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class ServerCommandManager {

    private final LinkedBlockingQueue<CommandObject> serverCommandQueue;

    /**
     * Initializes a new HashMap for server commands that the server can access.
     */
    public ServerCommandManager() {
        this.serverCommandQueue = new LinkedBlockingQueue<>();
    }
}
