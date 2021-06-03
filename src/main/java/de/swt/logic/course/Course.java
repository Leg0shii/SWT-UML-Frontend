package de.swt.logic.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
public class Course implements Serializable {

    private int courseId;
    private int grade;
    private String gradeName;
    private ArrayList<Date> dates;
    private ArrayList<Integer> userIds;
    private int teacherId;

    public Course(){
        courseId = -1;
        grade = -1;
        gradeName = "";
        dates = new ArrayList<>();
        userIds = new ArrayList<>();
        teacherId = -1;
    }

}

