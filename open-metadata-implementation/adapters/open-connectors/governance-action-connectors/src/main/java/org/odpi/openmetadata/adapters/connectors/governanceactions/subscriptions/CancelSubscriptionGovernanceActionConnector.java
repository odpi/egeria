/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.subscriptions;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship.ManageAssetGuard;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

/**
 * CancelSubscriptionGovernanceActionConnector cancels an active subscription.
 */
public class CancelSubscriptionGovernanceActionConnector extends GeneralGovernanceActionService
{
    /**
     * Default constructor
     */
    public CancelSubscriptionGovernanceActionConnector()
    {
    }


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException a problem within the governance action service.
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
            List<NewActionTarget>     outputActionTargets = new ArrayList<>();
            CompletionStatus          completionStatus;
            AuditLogMessageDefinition messageDefinition;

            ActionTargetElement subscription = super.getActionTarget(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION.getName());

            if (subscription == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.MISSING_ACTION_TARGET.getMessageDefinition(governanceServiceName, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION.getName());
                outputGuards.add(ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getName());
                completionStatus = ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getCompletionStatus();
            }
            else
            {
                governanceContext.getCollectionClient().deleteCollection(subscription.getActionTargetGUID(), null);

                ActionTargetElement licenceType = super.getActionTarget(ManageDigitalSubscriptionActionTarget.LICENSE_TYPE.getName());
                ActionTargetElement targetAsset = super.getActionTarget(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_TARGET.getName());

                if ((licenceType != null) && (targetAsset != null))
                {
                    OpenMetadataRelationshipList licenses = governanceContext.getOpenMetadataStore().getMetadataElementRelationships(targetAsset.getActionTargetGUID(),
                                                                                                                                     licenceType.getActionTargetGUID(),
                                                                                                                                     OpenMetadataType.LICENSE_RELATIONSHIP.typeName,
                                                                                                                                     0, 0);

                    if ((licenses != null) && (licenses.getElementList() != null))
                    {
                        for (OpenMetadataRelationship licence : licenses.getElementList())
                        {
                            if (licence != null)
                            {
                                String licenseGUID = propertyHelper.getStringProperty(governanceServiceName,
                                                                                      OpenMetadataProperty.LICENSE_GUID.name,
                                                                                      licence.getRelationshipProperties(),
                                                                                      methodName);
                                if (subscription.getActionTargetGUID().equals(licenseGUID))
                                {
                                    governanceContext.getOpenMetadataStore().deleteRelationshipInStore(licence.getRelationshipGUID());
                                }
                            }
                        }
                    }
                }

                messageDefinition = GovernanceActionConnectorsAuditCode.SERVICE_COMPLETED_SUCCESSFULLY.getMessageDefinition(governanceServiceName);

                completionStatus = ManageAssetGuard.DELETE_COMPLETE.getCompletionStatus();
                outputGuards.add(ManageAssetGuard.DELETE_COMPLETE.getName());
            }

            auditLog.logMessage(methodName, messageDefinition);

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
