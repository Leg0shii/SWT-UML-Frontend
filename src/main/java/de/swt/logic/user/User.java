package de.swt.logic.user;

import de.swt.util.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private int userId;
    private AccountType accountType;
    private String firstname;
    private String surname;
    private boolean active;

    public String getFullName() {
        return (firstname + " " + surname);
    }

}
