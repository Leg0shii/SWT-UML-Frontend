package de.swt.logic.user;

import de.swt.util.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private int id;
    private AccountType accountType;
    private String firstname;
    private String surname;
    private int[] course;
    private boolean online;

}


