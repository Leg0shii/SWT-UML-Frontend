package de.swt.logic.session;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;

@Setter
@Getter
public class Session implements Serializable {
    private int sessionId;
    private ArrayList<Integer> userIds;
    private long remainingTime;
    private ArrayList<Integer> groupIds;
    private ArrayList<Integer> masterIds;

    public Session(){
        sessionId = -1;
        userIds = new ArrayList<>();
        remainingTime = -1;
        groupIds = new ArrayList<>();
        masterIds = new ArrayList<>();
    }
}