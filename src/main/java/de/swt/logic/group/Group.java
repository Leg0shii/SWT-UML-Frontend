package de.swt.logic.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Group implements Serializable {

    private int groupid;
    private int courseid;
    private int timeTillTermination;
    private int maxGroupSize;
    private ArrayList<Integer> participants;

}

