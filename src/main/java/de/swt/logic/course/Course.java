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
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable {

    private int courseId;
    private int grade;
    private String gradeName;
    private ArrayList<Date> dates;
    private int teacherId;

}

