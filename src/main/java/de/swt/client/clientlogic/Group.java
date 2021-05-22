package de.swt.client.clientlogic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Group{

    private int number;
    private int timeTillTermination;
    private int maxGroupSize;
    private ArrayList<User> participants;
    private String workspace;

}

