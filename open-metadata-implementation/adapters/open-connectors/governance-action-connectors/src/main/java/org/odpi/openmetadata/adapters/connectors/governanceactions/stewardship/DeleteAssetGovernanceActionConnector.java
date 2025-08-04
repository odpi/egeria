/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * DeleteAssetGovernanceActionConnector deleted an asset and passes its GUID as an action target for follow on work.
 */
public class DeleteAssetGovernanceActionConnector extends GeneralGovernanceActionService
{
    /**
     * Default constructor
     */
    public DeleteAssetGovernanceActionConnector()
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
            List<String>              outputGuards        = new ArrayList<>();
            CompletionStatus          completionStatus;
            AuditLogMessageDefinition messageDefinition;
            String                    templateGUID;

            templateGUID = getProperty(ManageAssetRequestParameter.TEMPLATE_GUID.getName(), null);

            if (templateGUID == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_TEMPLATE_GUID.getMessageDefinition(governanceServiceName);
                outputGuards.add(ManageAssetGuard.MISSING_TEMPLATE.getName());
                completionStatus = ManageAssetGuard.MISSING_TEMPLATE.getCompletionStatus();
            }
            else
            {
                String assetGUID = governanceContext.getOpenMetadataStore().getMetadataElementFromTemplate(null,
                                                                                                            null,
                                                                                                            true,
                                                                                                            null,
                                                                                                            null,
                                                                                                            null,
                                                                                                            templateGUID,
                                                                                                            null,
                                                                                                            governanceContext.getRequestParameters(),
                                                                                                            null,
                                                                                                            null,
                                                                                                            null,
                                                                                                            true);

                OpenMetadataElement assetElement = governanceContext.getOpenMetadataStore().getMetadataElementByGUID(assetGUID);

                messageDefinition = GovernanceActionConnectorsAuditCode.NEW_ASSET_DELETED.getMessageDefinition(governanceServiceName,
                                                                                                               assetElement.getType().getTypeName(),
                                                                                                               propertyHelper.getStringProperty(governanceServiceName,
                                                                                                                                                OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                                                assetElement.getElementProperties(),
                                                                                                                                                methodName),
                                                                                                               assetGUID);

                governanceContext.getOpenMetadataStore().deleteMetadataElementInStore(assetElement.getElementGUID(), true);

                completionStatus = ManageAssetGuard.DELETE_COMPLETE.getCompletionStatus();
                outputGuards.add(ManageAssetGuard.DELETE_COMPLETE.getName());
            }

            auditLog.logMessage(methodName, messageDefinition);

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null, messageDefinition);
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
