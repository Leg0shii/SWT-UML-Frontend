package de.swt.logic.session;

import de.swt.logic.user.User;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
public class Session implements Serializable {
    int id;
    ArrayList<Integer> participants;
    ArrayList<Integer> master;
    Integer remainingTime;
    ArrayList<Integer> groups;

    public Session(){
        this.id = -1;
        this.participants = new ArrayList<>();
        this.master = new ArrayList<>();
        this.remainingTime = 0;
        this.groups = new ArrayList<>();
    }
}
