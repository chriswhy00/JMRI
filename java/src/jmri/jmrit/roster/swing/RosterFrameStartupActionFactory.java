package jmri.jmrit.roster.swing;

import jmri.util.startup.AbstractStartupActionFactory;
import jmri.util.startup.StartupActionFactory;
import java.util.Locale;
import org.openide.util.lookup.ServiceProvider;

/**
 * {@link jmri.util.startup.StartupActionFactory} for the
 * {@link jmri.jmrit.roster.swing.RosterFrameAction}.
 *
 * @author Randall Wood Copyright (C) 2016
 */
@ServiceProvider(service = StartupActionFactory.class)
public class RosterFrameStartupActionFactory extends AbstractStartupActionFactory {

    @Override
    public String getTitle(Class<?> clazz, Locale locale) throws IllegalArgumentException {
        if (!clazz.equals(RosterFrameAction.class)) {
            throw new IllegalArgumentException();
        }
        return Bundle.getMessage(locale, "RosterFrameAction"); // NOI18N
    }

    @Override
    public Class<?>[] getActionClasses() {
        return new Class[]{RosterFrameAction.class};
    }

}
