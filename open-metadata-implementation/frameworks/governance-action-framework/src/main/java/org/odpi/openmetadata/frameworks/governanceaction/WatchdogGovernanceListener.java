/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogGovernanceEvent;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GovernanceServiceException;

/**
 * WatchdogGovernanceListener defines the listener interface implemented by a WatchdogGovernanceService.
 */
public abstract class WatchdogGovernanceListener
{
    /**
     * This method is called each time a requested event is received from the open metadata repositories.
     * It is called for events received after this listener is registered until the the watchdog governance
     * service sets its status in the context as ACTIONED, INVALID, IGNORED or FAILED or it is stopped by an administrator.
     *
     * @param event event containing details of a change to an open metadata element.
     *
     * @throws GovernanceServiceException reports that the event can not be processed (this is logged but
     *                                    no other action is taken).  The listener will continue to be
     *                                    called until the watchdog governance service declares it is complete.
     */
    public abstract void processEvent(WatchdogGovernanceEvent  event) throws GovernanceServiceException;
}
