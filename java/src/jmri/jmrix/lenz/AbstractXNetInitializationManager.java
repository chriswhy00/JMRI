package jmri.jmrix.lenz;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;

/**
 * This class provides a base implementation for Command Station/interface
 * dependent initilization for XpressNet. It adds the appropriate Managers via
 * the Initialization Manager based on the Command Station Type.
 *
 * @author Paul Bender Copyright (C) 2003-2010
 */
public abstract class AbstractXNetInitializationManager {

    protected XNetSystemConnectionMemo systemMemo;

    /**
     * Define timeout used during initialization
     * @return timout value in milliseconds
     */
    protected int getInitTimeout() {
        return 30000;
    }

    public AbstractXNetInitializationManager(XNetSystemConnectionMemo memo) {
        /* spawn a thread to request version information and wait for the 
         command station to respond */
        if (log.isDebugEnabled()) {
            log.debug("Starting XpressNet Initialization Process");
        }
        systemMemo = memo;
        new XNetInitializer(this);

        // Since we can't currently reconfigure the user interface after  
        // initilization, We need to wait for the initilization thread 
        // to finish before we can continue.  The wait  can be removed IF 
        // we revisit the GUI initilization process.
        synchronized (this) {
            log.debug("start wait");
            new jmri.util.WaitHandler(this);
            log.debug("end wait");
        }

        init();
    }

    protected abstract void init();

    /* Interal class to configure the XNet implementation */
    protected class XNetInitializer implements XNetListener {

        private final javax.swing.Timer initTimer; // Timer used to let he
        // command station response time 
        // out, and configure the defaults.

        private final Object parent;

        public XNetInitializer(Object Parent) {

            parent = Parent;

            initTimer = setupInitTimer();

            // Register as an XpressNet Listener
            systemMemo.getXNetTrafficController().addXNetListener(XNetInterface.CS_INFO, this);

            //Send Information request to LI100/LI100
         /* First, we need to send a request for the Command Station
             hardware and software version */
            XNetMessage msg = XNetMessage.getCSVersionRequestMessage();
            //Then Send the version request to the controller
            systemMemo.getXNetTrafficController().sendXNetMessage(msg, this);
        }

        protected javax.swing.Timer setupInitTimer() {
            // Initialize and start initilization timeout timer.
            javax.swing.Timer retVal = new javax.swing.Timer(getInitTimeout(),
                    (ActionEvent e) -> {
                                    /* If the timer times out, notify any 
                                     waiting objects, and dispose of
                                     this thread */
                                    if (log.isDebugEnabled()) {
                                        log.debug("Timeout waiting for Command Station Response");
                                    }
                                    finish();
                                });
            retVal.setInitialDelay(getInitTimeout());
            retVal.start();
            return retVal;
        }

        @SuppressFBWarnings(value = "NO_NOTIFY_NOT_NOTIFYALL", justification = "There should only ever be one thread waiting for this method (the designated parent, which started the thread).")
        private void finish() {
            initTimer.stop();
            // Notify the parent
            try {
                synchronized (parent) {
                    parent.notify();
                }
            } catch (Exception e) {
                log.error("Exception {] while notifying initilization thread.",e);
            }
            if (log.isDebugEnabled()) {
                log.debug("Notification Sent");
            }
            // Then dispose of this object
            dispose();
        }

        // listen for the responses from the LI100/LI101
        @Override
        public void message(XNetReply l) {
            // Check to see if this is a response with the Command Station 
            // Version Info
            if (l.getElement(0) == XNetConstants.CS_SERVICE_MODE_RESPONSE &&
                    l.getElement(1) == XNetConstants.CS_SOFTWARE_VERSION) {
                // This is the Command Station Software Version Response
                    systemMemo.getXNetTrafficController()
                            .getCommandStation()
                            .setCommandStationSoftwareVersion(l);
                    systemMemo.getXNetTrafficController()
                            .getCommandStation()
                            .setCommandStationType(l);
                    finish();
            }
        }

        // listen for the messages to the LI100/LI101
        @Override
        public void message(XNetMessage l) {
            // we aren't concerned with incoming messages in this class.
        }

        // Handle a timeout notification
        @Override
        public void notifyTimeout(XNetMessage msg) {
            if (log.isDebugEnabled()) {
                log.debug("Notified of timeout on message {}",msg);
            }
        }

        public void dispose() {
            systemMemo.getXNetTrafficController().removeXNetListener(XNetInterface.CS_INFO, this);
        }
    }

    private static final Logger log = LoggerFactory.getLogger(AbstractXNetInitializationManager.class);

}
