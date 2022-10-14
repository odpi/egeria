/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog;

import org.odpi.openmetadata.frameworks.governanceaction.WatchdogGovernanceListener;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogGovernanceEvent;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GovernanceServiceException;


/**
 * GenericWatchdogListener implements the watchdog listener interface for the GenericWatchdogGovernanceActionConnectors.
 */
public class GenericWatchdogListener extends WatchdogGovernanceListener
{
    GenericWatchdogGovernanceActionConnector connector;


    /**
     * Construct the listener that is registered with the GovernanceContext.
     *
     * @param connector connector to delegate all events to
     */
    public GenericWatchdogListener(GenericWatchdogGovernanceActionConnector connector)
    {
        this.connector = connector;
    }


    /**
     * This method is called each time a requested event is received from the open metadata repositories.
     * It is called for events received after this listener is registered until the watchdog governance
     * service sets its status in the context as ACTIONED, INVALID, IGNORED or FAILED or it is stopped by an administrator shutting down
     * the hosting server or this service explicitly.
     *
     * @param event event containing details of a change to an open metadata element.
     *
     * @throws GovernanceServiceException reports that the event can not be processed (this is logged but
     *                                    no other action is taken).  The listener will continue to be
     *                                    called until the watchdog governance action service declares it is complete
     *                                    or administrator action shuts down the service.
     */
    public void processEvent(WatchdogGovernanceEvent event) throws GovernanceServiceException
    {
        connector.processEvent(event);
    }
}
