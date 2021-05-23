package de.swt.logic.user;

import de.swt.util.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private int id;
    private AccountType accountType;
    private String firstname;
    private String surname;
    private ArrayList<Integer> course;
    private boolean online;
    private boolean isInGroup;
    private boolean isInCourse;

    public User(int id, String firstname, String surname) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
    }

    public String getFullName() {
        return (firstname + " " + surname);
    }

}
