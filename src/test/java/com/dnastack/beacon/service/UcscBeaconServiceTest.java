/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dnastack.beacon.service;

import com.dnastack.beacon.core.Beacon;
import com.dnastack.beacon.core.BeaconResponse;
import com.dnastack.beacon.core.Query;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 */
public class UcscBeaconServiceTest {

    public UcscBeaconServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of executeQuery method, of class UcscBeaconService.
     */
//    @Test
    public void testExecuteQuery() {
        System.out.println("executeQuery");
        Beacon beacon = null;
        Query query = null;
        UcscBeaconService instance = new UcscBeaconServiceImpl();
        BeaconResponse expResult = null;
        BeaconResponse result = instance.executeQuery(beacon, query);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class UcscBeaconServiceImpl extends UcscBeaconService {
    }

}
