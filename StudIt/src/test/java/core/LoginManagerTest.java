package core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import studit.core.LoginManager;

public class LoginManagerTest{

    @BeforeEach
    public void init() {
        LoginManager.initialize();
    }

    @Test
    public void testMatch() {
        assertFalse(LoginManager.match("user", "aaaa"));
        assertTrue(LoginManager.match("user", "password"));
    }


}