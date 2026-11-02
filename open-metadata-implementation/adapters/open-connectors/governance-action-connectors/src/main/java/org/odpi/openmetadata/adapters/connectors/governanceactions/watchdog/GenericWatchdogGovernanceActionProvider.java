/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog;


import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceProvider;


/**
 * GenericWatchdogGovernanceActionProvider is the base class for the OCF connector providers of the generic watchdog
 * action services.  The WatchdogActionServiceProvider sets up the standard watchdog request parameters, guards and
 * action targets.
 */
public abstract class GenericWatchdogGovernanceActionProvider extends WatchdogActionServiceProvider
{
}
