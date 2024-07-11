/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.grave;

import com.grave.Game.ObjectManager;
import com.grave.Networking.NetClient;
import com.grave.Networking.NetServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author koppe
 */
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

    /**
     * Test of main method, of class Graveborn.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Graveborn.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of simpleInitApp method, of class Graveborn.
     */
    @Test
    public void testSimpleInitApp() {
        System.out.println("simpleInitApp");
        Graveborn instance = null;
        instance.simpleInitApp();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of simpleUpdate method, of class Graveborn.
     */
    @Test
    public void testSimpleUpdate() {
        System.out.println("simpleUpdate");
        float tpf = 0.0F;
        Graveborn instance = null;
        instance.simpleUpdate(tpf);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class Graveborn.
     */
    @Test
    public void testDestroy() {
        System.out.println("destroy");
        Graveborn instance = null;
        instance.destroy();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getObjectmanager method, of class Graveborn.
     */
    @Test
    public void testGetObjectmanager() {
        System.out.println("getObjectmanager");
        Graveborn instance = null;
        ObjectManager expResult = null;
        ObjectManager result = instance.getObjectManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServer method, of class Graveborn.
     */
    @Test
    public void testGetServer() {
        System.out.println("getServer");
        Graveborn instance = null;
        NetServer expResult = null;
        NetServer result = instance.getNetServer();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getClient method, of class Graveborn.
     */
    @Test
    public void testGetClient() {
        System.out.println("getClient");
        Graveborn instance = null;
        NetClient expResult = null;
        NetClient result = instance.getNetClient();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
