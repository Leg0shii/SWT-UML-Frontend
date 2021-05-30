package de.swt.logic.session;

import lombok.*;

import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
public class Session {
    int id;
    ArrayList<Integer> participants;
    Integer master;
    Integer remainingTime;
    ArrayList<Integer> groups;

    public Session(int master){
        this.id = -1;
        this.participants = new ArrayList<>();
        this.master = master;
        this.remainingTime = 0;
        this.groups = new ArrayList<>();
    }
}