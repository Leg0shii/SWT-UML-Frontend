package de.swt.logic.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testgetFullName() {
        User u = new User(1234, "Hans", "Wusrt");
        assertEquals(u.getFullName(), "Hans Wusrt");
        assertTrue(u.getId() == 1234, "Is id 1234?");
        assertFalse(u.getSurname() == "Wurst", "is surname !not! wurst since its not supposed to?");
    }

}