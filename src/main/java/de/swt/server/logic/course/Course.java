package de.swt.logic.course;

import de.swt.Server;
import de.swt.logic.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    private int id;
    private int grade;
    private String name;
    private ArrayList<Date> dates;
    private int teacherID;

    public User getTeacher() {
        return Server.getInstance().userManager.getUserHashMap().get(teacherID);
    }

}

