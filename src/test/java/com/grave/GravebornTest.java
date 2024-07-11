
package com.grave;

import com.jme3.system.JmeContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GravebornTest {
    
    public GravebornTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void test() {
        String[] args = { "" };
        Arguments arguments = new Arguments(args);
        arguments.ip = "localhost";
        arguments.port = Graveborn.DEFAULT_PORT;
        arguments.serverName = "testserver";
        arguments.clientName = "testclient";
        arguments.mode = Mode.HOST;

        Graveborn app = new Graveborn(arguments);
        app.start(JmeContext.Type.Headless);
    }
}
