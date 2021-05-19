package de.swt.logic.course;

import de.swt.logic.user.User;

import java.util.ArrayList;
import java.util.Date;

public class Course{

    private int id;
    private int grade;
    private char name;
    private ArrayList<Date> dates;
    private User teacher;
    private ArrayList<User> students;

    public Course(int id, int grade, char name, ArrayList<Date> dates, User teacher, ArrayList<User> students){
        this.id = id;
        this.grade = grade;
        this.name = name;
        this.dates = dates;
        this.teacher = teacher;
        this.students = students;
    }

    public void resetStudents(){
        this.students = new ArrayList<>();
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public char getName() {
        return name;
    }

    public void setName(char name) {
        this.name = name;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public ArrayList<Date> getDates(){
        return dates;
    }

    public void setDates(ArrayList<Date> dates){
        this.dates = dates;
    }

    public ArrayList<User> getStudents(){
        return students;
    }

    public void setStudents(ArrayList<User> students){
        this.students = students;
    }
}

