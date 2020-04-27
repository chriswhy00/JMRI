package apps.startup.configurexml;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * StartupActionsPreferencesPanelXmlTest.java
 *
 * Test for the StartupActionsPreferencesPanelXml class
 *
 * @author   Paul Bender  Copyright (C) 2016
 */
public class StartupActionsPreferencesPanelXmlTest {

    @Test
    public void testCtor(){
      Assert.assertNotNull("StartupActionsPreferencesPanelXml constructor",new StartupActionsPreferencesPanelXml());
    }

    @Before
    public void setUp() {
        JUnitUtil.setUp();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }

}

