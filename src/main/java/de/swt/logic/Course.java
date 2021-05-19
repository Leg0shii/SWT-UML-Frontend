package de.swt.logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course{

    private int id;
    private int grade;
    private char name;
    private ArrayList<Date> dates;
    private User teacher;
    private ArrayList<User> students;

}

