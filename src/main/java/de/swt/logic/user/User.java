package de.swt.logic.user;

import de.swt.util.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

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

    public String getFullName() {
        return (firstname + " " + surname);
    }

}
