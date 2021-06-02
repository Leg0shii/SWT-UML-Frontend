package de.swt.logic.session;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
public class Session implements Serializable {
    private int id;
    private ArrayList<Integer> participants;
    private ArrayList<Integer> master;
    private long remainingTime;
    private ArrayList<Integer> groups;

    public Session(){
        this.id = -1;
        this.participants = new ArrayList<>();
        this.master = new ArrayList<>();
        this.remainingTime = 0;
        this.groups = new ArrayList<>();
    }
}