package util;

public class User {

    //Variablen von User
    private String id;
    private String firstname;
    private String surname;
    private Course course;

    //Constructor für User mit Attributen und Zuweisung
    public User(String id, String firstname, String surname){
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.course = loadUserCourses();
    }

    private Course loadUserCourses() {
        // Datenbank Anfrage
        return null;
    }

    public String getFullName() {
        return this.firstname + " " + this.surname;
    }

    // Getter und Setter
    public String getID() {
        return id;
    }

    public void setID(String id) {
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

