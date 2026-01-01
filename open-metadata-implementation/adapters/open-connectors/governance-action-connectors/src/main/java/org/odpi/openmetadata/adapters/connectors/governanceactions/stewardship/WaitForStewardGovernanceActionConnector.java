/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.ArrayList;
import java.util.List;

/**
 * WaitForStewardGovernanceActionConnector is currently a placeholder for a governance action service
 * that will wait for a steward to complete a to do.
 */
public class WaitForStewardGovernanceActionConnector extends GeneralGovernanceActionService
{
    /**
     * Default constructor
     */
    public WaitForStewardGovernanceActionConnector()
    {
    }


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        List<String>          outputGuards         = new ArrayList<>();
        CompletionStatus      completionStatus     = null;
    }
}
