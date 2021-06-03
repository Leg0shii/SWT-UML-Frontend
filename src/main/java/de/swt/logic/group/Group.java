package de.swt.logic.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
public class Group implements Serializable {

    private int groupId;
    private int sessionId;
    private long timeTillTermination;
    private int maxGroupSize;
    private ArrayList<Integer> userIds;

    public Group(){
        groupId = -1;
        sessionId = -1;
        timeTillTermination = -1;
        maxGroupSize = -1;
        userIds = new ArrayList<>();
    }
}


