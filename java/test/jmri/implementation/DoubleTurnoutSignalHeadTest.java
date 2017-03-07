package jmri.implementation;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jmri.InstanceManager;
import jmri.TurnoutManager;
import jmri.Turnout;
import jmri.NamedBeanHandle;

/**
 *
 * @author Paul Bender Copyright (C) 2017	
 */
public class DoubleTurnoutSignalHeadTest {

    @Test
    public void testCTor() {
        Turnout it = (InstanceManager.getDefault(TurnoutManager.class)).provideTurnout("IT1");
        NamedBeanHandle green = new NamedBeanHandle("green handle",it);
        Turnout it2 = (InstanceManager.getDefault(TurnoutManager.class)).provideTurnout("IT1");
        NamedBeanHandle red = new NamedBeanHandle("red handle",it2);
        DoubleTurnoutSignalHead t = new DoubleTurnoutSignalHead("Test Head",green,red);
        //Assert.assertNotNull("exists",t);
    }

    // The minimal setup for log4J
    @Before
    public void setUp() {
        apps.tests.Log4JFixture.setUp();
        jmri.util.JUnitUtil.resetInstanceManager();
        jmri.util.JUnitUtil.initInternalTurnoutManager();
    }

    @After
    public void tearDown() {
        jmri.util.JUnitUtil.resetInstanceManager();
        apps.tests.Log4JFixture.tearDown();
    }

    private final static Logger log = LoggerFactory.getLogger(DoubleTurnoutSignalHeadTest.class.getName());

}