/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * DaysOfWeekGovernanceActionConnector uses the current time to output the day of the week as a guard.
 * It does not require any specific request parameters or action targets.
 */
public class DaysOfWeekGovernanceActionConnector extends GeneralGovernanceActionService
{
    /**
     * Default constructor
     */
    public DaysOfWeekGovernanceActionConnector()
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

        try
        {
            List<String>     outputGuards     = new ArrayList<>();
            CompletionStatus completionStatus = CompletionStatus.ACTIONED;


            LocalDate date   = LocalDate.now(); // LocalDate = 2010-02-23
            DayOfWeek dow    = date.getDayOfWeek();  // Extracts a `DayOfWeek` enum object.
            String    output = dow.getDisplayName(TextStyle.FULL, Locale.US); // String = Tuesday

            outputGuards.add(output.toLowerCase(Locale.ROOT));

            governanceContext.recordCompletionStatus(completionStatus,
                                                     outputGuards,
                                                     null,
                                                     null,
                                                     GovernanceActionConnectorsAuditCode.DAY_OF_THE_WEEK.getMessageDefinition(governanceServiceName, output));
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }
}
