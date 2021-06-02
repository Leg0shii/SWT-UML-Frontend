package de.swt.manager;

import de.swt.util.ServerCommandWorker;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class ServerCommandManager {

    private final LinkedBlockingQueue<CommandObject> serverCommandQueue;

    public ServerCommandManager() {
        this.serverCommandQueue = new LinkedBlockingQueue<>();
        new Thread(() -> {
            Timer timer = new Timer();
            timer.schedule(new ServerCommandWorker(serverCommandQueue), 0, 10);
        }).start();
    }
}
