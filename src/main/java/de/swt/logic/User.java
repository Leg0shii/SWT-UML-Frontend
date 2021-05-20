package de.swt.logic;

import de.swt.util.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;
    private AccountType accountType;
    private String firstname;
    private String surname;
    private int[] course;
    private boolean online;

    public User(int id, String firstname, String surname) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
    }

    public String getFullName() {
        return (firstname + " " + surname);
    }

}

