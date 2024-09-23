/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CatalogServerGovernanceActionConnector creates a server and attaches it to an appropriate integration
 * connector (passed as an action target).
 */
public class CreateServerGovernanceActionConnector extends GeneralGovernanceActionService
{
    /**
     * Default constructor
     */
    public CreateServerGovernanceActionConnector()
    {
    }


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String methodName = "start";

        super.start();

        try
        {
            List<String>              outputGuards        = new ArrayList<>();
            List<NewActionTarget>     outputActionTargets = new ArrayList<>();
            CompletionStatus          completionStatus;
            AuditLogMessageDefinition messageDefinition;
            String                    templateGUID;

            templateGUID = getProperty(CreateServerRequestParameter.TEMPLATE_GUID.getName(), null);

            if (templateGUID == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_TEMPLATE_GUID.getMessageDefinition(governanceServiceName);
                outputGuards.add(CreateServerGuard.MISSING_TEMPLATE.getName());
                completionStatus = CreateServerGuard.MISSING_TEMPLATE.getCompletionStatus();
            }
            else
            {
                String serverGUID = governanceContext.getOpenMetadataStore().getMetadataElementFromTemplate(null,
                                                                                                            null,
                                                                                                            true,
                                                                                                            null,
                                                                                                            null,
                                                                                                            templateGUID,
                                                                                                            null,
                                                                                                            governanceContext.getRequestParameters(),
                                                                                                            null,
                                                                                                            null,
                                                                                                            null,
                                                                                                            true);

                OpenMetadataElement serverElement = governanceContext.getOpenMetadataStore().getMetadataElementByGUID(serverGUID);

                messageDefinition = GovernanceActionConnectorsAuditCode.NEW_ASSET_CREATED.getMessageDefinition(governanceServiceName,
                                                                                                               serverElement.getType().getTypeName(),
                                                                                                               propertyHelper.getStringProperty(governanceServiceName,
                                                                                                                                                OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                                                serverElement.getElementProperties(),
                                                                                                                                                methodName),
                                                                                                               serverGUID);
                NewActionTarget newActionTarget = new NewActionTarget();

                newActionTarget.setActionTargetGUID(serverGUID);
                newActionTarget.setActionTargetName(ActionTarget.NEW_ASSET.name);

                outputActionTargets.add(newActionTarget);

                completionStatus = CatalogServerGuard.SET_UP_COMPLETE.getCompletionStatus();
                outputGuards.add(CatalogServerGuard.SET_UP_COMPLETE.getName());
            }

            auditLog.logMessage(methodName, messageDefinition);

            if (outputActionTargets.isEmpty())
            {
                outputActionTargets = null;
            }

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, outputActionTargets, messageDefinition);
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
