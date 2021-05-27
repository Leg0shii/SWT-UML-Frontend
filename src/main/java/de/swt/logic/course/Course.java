package de.swt.logic.course;

import de.swt.logic.user.User;
import de.swt.util.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable {

    private int id;
    private int grade;
    private String name;
    private ArrayList<Date> dates;
    private int teacherID;

    public User getTeacher() {
        return Client.getInstance().userManager.getUserHashMap().get(teacherID);
    }

}

