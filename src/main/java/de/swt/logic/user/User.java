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
public class User implements Serializable {

    private int userId;
    private AccountType accountType;
    private String firstname;
    private String surname;
    private boolean active;

    public User(){
        userId = -1;
        accountType = null;
        firstname = "";
        surname = "";
        active = false;
    }

    /**
     * Is used to get the full name of a user.
     * @return Full name of user.
     */
    public String getFullName() {
        return (firstname + " " + surname);
    }

}
