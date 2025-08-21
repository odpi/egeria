/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.ArrayList;
import java.util.List;

/**
 * WriteAuditLogMessageGovernanceActionConnector writes requested messages to the Audit Log.
 */
public class WriteAuditLogMessageGovernanceActionConnector extends GeneralGovernanceActionService
{
    /**
     * Default constructor
     */
    public WriteAuditLogMessageGovernanceActionConnector()
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

        String messageText = getProperty(WriteAuditLogRequestParameter.MESSAGE_TEXT.getName(), "This is the default Message Text");

        try
        {
            List<String> outputGuards = new ArrayList<>();

            AuditLogMessageDefinition auditLogMessageDefinition = GovernanceActionConnectorsAuditCode.BLANK_INFO_LOG_MESSAGE.getMessageDefinition(messageText);

            auditLog.logMessage(methodName, auditLogMessageDefinition);

            outputGuards.add(WriteAuditLogGuard.MESSAGE_WRITTEN.getName());
            governanceContext.recordCompletionStatus(WriteAuditLogGuard.MESSAGE_WRITTEN.getCompletionStatus(),
                                                     outputGuards,
                                                     null,
                                                     null,
                                                     auditLogMessageDefinition);
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
