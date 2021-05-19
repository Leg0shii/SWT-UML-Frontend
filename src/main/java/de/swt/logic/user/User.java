package de.swt.logic.user;

import de.swt.logic.course.Course;
import de.swt.util.AccountType;

public class User {

    private int id;
    private AccountType accountType;
    private String firstname;
    private String surname;
    private Course[] course;

    public User() {

    }

    public User(int id, String firstname, String surname){
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.course = loadUserCourses();
    }

    // TODO : de.swt.logic
    private Course[] loadUserCourses() {
        // 2 courses, one main course and one course that can be exchanged
        return null;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Course[] getCourse() {
        return course;
    }

    public void setCourse(Course[] course) {
        this.course = course;
    }

    public String getFullName() {
        return this.firstname + " " + this.surname;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}

