package de.swt.logic.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Session implements Serializable {
    private int sessionId;
    private ArrayList<Integer> userIds;
    private long remainingTime;
    private ArrayList<Integer> groupIds;
    private ArrayList<Integer> masterIds;
}