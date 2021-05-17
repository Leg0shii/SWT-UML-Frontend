package logic.group;

import logic.user.User;

import java.util.ArrayList;

public class Group{

    private int number;
    private int timeTillTermination;
    private int maxGroupSize;
    private ArrayList<User> participants;
    private String workspace;

    public Group(int tNumber, int timeTillTermination, int maxGroupSize, ArrayList<User> participants, String workspace){

        this.number = tNumber;
        this.timeTillTermination = timeTillTermination;
        this.maxGroupSize = maxGroupSize;
        this.participants = participants;
        this.workspace = workspace;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTimeTillTermination() {
        return timeTillTermination;
    }

    public void setTimeTillTermination(int timeTillTermination) {
        this.timeTillTermination = timeTillTermination;
    }

    public int getMaxGroupSize() {
        return maxGroupSize;
    }

    public void setMaxGroupSize(int maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }

    public ArrayList<User> getParticipants(){
        return participants;
    }

    public void setParticipants(ArrayList<User> participants){
        this.participants = participants;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }
}

