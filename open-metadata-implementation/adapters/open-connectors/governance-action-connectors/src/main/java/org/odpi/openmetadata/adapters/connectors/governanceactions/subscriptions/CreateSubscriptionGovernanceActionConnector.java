/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.subscriptions;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationManagerClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.GovernanceDefinitionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementActorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementItemProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalSubscriberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalSubscriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationSubscriberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CreateSubscriptionGovernanceActionConnector creates an asset and passes its GUID as an action target for follow on work.
 */
public class CreateSubscriptionGovernanceActionConnector extends GeneralGovernanceActionService
{
    /**
     * Default constructor
     */
    public CreateSubscriptionGovernanceActionConnector()
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
            List<NewActionTarget>     outputActionTargets = new ArrayList<>();
            CompletionStatus          completionStatus;
            AuditLogMessageDefinition messageDefinition;
            String                    subscriptionName = super.getProperty(ManageDigitalSubscriptionRequestParameter.SUBSCRIPTION_NAME.getName(), null);
            String                    subscriptionDescription = super.getProperty(ManageDigitalSubscriptionRequestParameter.SUBSCRIPTION_DESCRIPTION.getName(), null);
            ActionTargetElement       targetAsset = super.getActionTarget(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_TARGET.getName());
            ActionTargetElement       sourceAsset = super.getActionTarget(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_SOURCE.getName());
            ActionTargetElement       notificationType = super.getActionTarget(ManageDigitalSubscriptionActionTarget.NOTIFICATION_TYPE.getName());
            ActionTargetElement       subscriptionItem = super.getActionTarget(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_ITEM.getName());
            ActionTargetElement       subscriptionRequester = super.getActionTarget(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_REQUESTER.getName());
            List<ActionTargetElement> productOwners = super.getAllActionTargets(ManageDigitalSubscriptionActionTarget.DIGITAL_PRODUCT_OWNER.getName());
            List<ActionTargetElement> serviceLevelObjectives = super.getAllActionTargets(ManageDigitalSubscriptionActionTarget.SERVICE_LEVEL_OBJECTIVE.getName());
            ActionTargetElement       licenseType = super.getActionTarget(ManageDigitalSubscriptionActionTarget.LICENSE_TYPE.getName());
            ActionTargetElement       provisioningActionType = super.getActionTarget(ManageDigitalSubscriptionActionTarget.PROVISIONING_ACTION_TYPE.getName());
            ActionTargetElement       cancellingActionType = super.getActionTarget(ManageDigitalSubscriptionActionTarget.CANCELLING_ACTION_TYPE.getName());

            if (subscriptionName == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.MISSING_REQUEST_PARAMETER.getMessageDefinition(governanceServiceName, ManageDigitalSubscriptionRequestParameter.SUBSCRIPTION_NAME.getName());
                outputGuards.add(ManageDigitalSubscriptionGuard.MISSING_REQUEST_PARAMETER.getName());
                completionStatus = ManageDigitalSubscriptionGuard.MISSING_REQUEST_PARAMETER.getCompletionStatus();
            }
            else if (targetAsset == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.MISSING_ACTION_TARGET.getMessageDefinition(governanceServiceName, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_TARGET.getName());
                outputGuards.add(ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getName());
                completionStatus = ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getCompletionStatus();
            }
            else if (sourceAsset == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.MISSING_ACTION_TARGET.getMessageDefinition(governanceServiceName, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_SOURCE.getName());
                outputGuards.add(ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getName());
                completionStatus = ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getCompletionStatus();
            }
            else if (notificationType == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.MISSING_ACTION_TARGET.getMessageDefinition(governanceServiceName, ManageDigitalSubscriptionActionTarget.NOTIFICATION_TYPE.getName());
                outputGuards.add(ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getName());
                completionStatus = ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getCompletionStatus();
            }
            else if (provisioningActionType == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.MISSING_ACTION_TARGET.getMessageDefinition(governanceServiceName, ManageDigitalSubscriptionActionTarget.PROVISIONING_ACTION_TYPE.getName());
                outputGuards.add(ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getName());
                completionStatus = ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getCompletionStatus();
            }
            else if (cancellingActionType == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.MISSING_ACTION_TARGET.getMessageDefinition(governanceServiceName, ManageDigitalSubscriptionActionTarget.CANCELLING_ACTION_TYPE.getName());
                outputGuards.add(ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getName());
                completionStatus = ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getCompletionStatus();
            }
            else if (subscriptionRequester == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.MISSING_ACTION_TARGET.getMessageDefinition(governanceServiceName, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_REQUESTER.getName());
                outputGuards.add(ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getName());
                completionStatus = ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getCompletionStatus();
            }
            else if ((productOwners == null) || (productOwners.isEmpty()))
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.MISSING_ACTION_TARGET.getMessageDefinition(governanceServiceName, ManageDigitalSubscriptionActionTarget.DIGITAL_PRODUCT_OWNER.getName());
                outputGuards.add(ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getName());
                completionStatus = ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getCompletionStatus();
            }
            else if (subscriptionItem == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.MISSING_ACTION_TARGET.getMessageDefinition(governanceServiceName, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_ITEM.getName());
                outputGuards.add(ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getName());
                completionStatus = ManageDigitalSubscriptionGuard.MISSING_ACTION_TARGET.getCompletionStatus();
            }
            else
            {
                String subscriptionItemGUID = subscriptionItem.getActionTargetGUID();
                String subscriptionItemType = subscriptionItem.getTargetElement().getType().getTypeName();
                String subscriptionItemName = propertyHelper.getStringProperty(governanceServiceName,
                                                                               OpenMetadataProperty.DISPLAY_NAME.name,
                                                                               subscriptionItem.getTargetElement().getElementProperties(),
                                                                               methodName);

                String subscriptionRequesterGUID = subscriptionRequester.getActionTargetGUID();
                String subscriptionRequesterType = subscriptionRequester.getTargetElement().getType().getTypeName();
                String subscriptionRequesterName = propertyHelper.getStringProperty(governanceServiceName,
                                                                                    OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                    subscriptionRequester.getTargetElement().getElementProperties(),
                                                                                    methodName);

                CollectionClient collectionClient = governanceContext.getCollectionClient(OpenMetadataType.DIGITAL_SUBSCRIPTION.typeName);

                DigitalSubscriptionProperties digitalSubscriptionProperties = new DigitalSubscriptionProperties();

                digitalSubscriptionProperties.setQualifiedName(OpenMetadataType.DIGITAL_SUBSCRIPTION.typeName + "::" + subscriptionName + "::" + new Date());
                digitalSubscriptionProperties.setDisplayName(subscriptionName);
                digitalSubscriptionProperties.setDescription(subscriptionDescription);

                NewElementOptions newElementOptions = new NewElementOptions(collectionClient.getMetadataSourceOptions());

                newElementOptions.setIsOwnAnchor(false);
                newElementOptions.setAnchorGUID(subscriptionItemGUID);
                newElementOptions.setParentAtEnd1(false);
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName);
                newElementOptions.setParentGUID(subscriptionItemGUID);

                AgreementItemProperties agreementItemProperties = new AgreementItemProperties();

                agreementItemProperties.setAgreementItemId(OpenMetadataType.DIGITAL_PRODUCT.typeName);
                agreementItemProperties.setAgreementStart(new Date());

                String subscriptionGUID = collectionClient.createCollection(newElementOptions,
                                                                            null,
                                                                            digitalSubscriptionProperties,
                                                                            agreementItemProperties);

                AgreementActorProperties agreementActorProperties = new AgreementActorProperties();

                agreementActorProperties.setActorName(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_REQUESTER.getName());

                collectionClient.linkAgreementActor(subscriptionGUID,
                                                    subscriptionRequesterGUID,
                                                    collectionClient.getMetadataSourceOptions(),
                                                    agreementActorProperties);

                for (ActionTargetElement productOwner : productOwners)
                {
                    if (productOwner != null)
                    {
                        agreementActorProperties.setActorName(ManageDigitalSubscriptionActionTarget.DIGITAL_PRODUCT_OWNER.getName());

                        collectionClient.linkAgreementActor(subscriptionGUID,
                                                            productOwner.getActionTargetGUID(),
                                                            collectionClient.getMetadataSourceOptions(),
                                                            agreementActorProperties);
                    }
                }

                OpenMetadataStore openMetadataStore = governanceContext.getOpenMetadataStore();

                String cancelSubscriptionGUID = governanceContext.createProcessFromGovernanceActionType(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName + "::" + ManageDigitalSubscriptionActionTarget.CANCELLING_ACTION_TYPE.getName() + "::" + subscriptionGUID,
                                                                                                        ManageDigitalSubscriptionActionTarget.CANCELLING_ACTION_TYPE.getName(),
                                                                                                        ManageDigitalSubscriptionActionTarget.CANCELLING_ACTION_TYPE.description,
                                                                                                        cancellingActionType.getActionTargetGUID(),
                                                                                                        null,
                                                                                                        subscriptionGUID,
                                                                                                        subscriptionItemGUID);

                openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                               cancelSubscriptionGUID,
                                                               subscriptionGUID,
                                                               null,
                                                               null,
                                                               propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION.getName()));

                openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                               cancelSubscriptionGUID,
                                                               targetAsset.getActionTargetGUID(),
                                                               null,
                                                               null,
                                                               propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_TARGET.getName()));

                if (licenseType != null)
                {
                    openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                                   cancelSubscriptionGUID,
                                                                   licenseType.getActionTargetGUID(),
                                                                   null,
                                                                   null,
                                                                   propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.LICENSE_TYPE.getName()));
                }

                ClassificationManagerClient classificationManagerClient = governanceContext.getClassificationManagerClient();

                ResourceListProperties resourceListProperties = new  ResourceListProperties();

                resourceListProperties.setResourceUse(ResourceUse.CANCEL_SUBSCRIPTION.getResourceUse());
                resourceListProperties.setDescription(ResourceUse.CANCEL_SUBSCRIPTION.getDescription());

                classificationManagerClient.addResourceListToElement(subscriptionGUID,
                                                                     cancelSubscriptionGUID,
                                                                     classificationManagerClient.getMetadataSourceOptions(),
                                                                     resourceListProperties);

                GovernanceDefinitionClient governanceDefinitionClient = governanceContext.getGovernanceDefinitionClient();

                if (serviceLevelObjectives != null)
                {
                    GovernedByProperties governedByProperties = new GovernedByProperties();

                    governedByProperties.setLabel("service level objective");
                    governedByProperties.setDescription("The level of service to expect through this subscription");

                    for (ActionTargetElement serviceLevelObjective : serviceLevelObjectives)
                    {
                        if (serviceLevelObjective != null)
                        {
                            governanceDefinitionClient.addGovernanceDefinitionToElement(subscriptionGUID,
                                                                                        serviceLevelObjective.getActionTargetGUID(),
                                                                                        governanceDefinitionClient.getMetadataSourceOptions(),
                                                                                        governedByProperties);
                        }
                    }
                }

                if (licenseType != null)
                {
                    LicenseProperties licenseProperties = new LicenseProperties();

                    licenseProperties.setCoverageStart(new Date());
                    licenseProperties.setLicenseGUID(subscriptionGUID);

                    governanceDefinitionClient.licenseElement(targetAsset.getActionTargetGUID(),
                                                              licenseType.getActionTargetGUID(),
                                                              governanceDefinitionClient.getMetadataSourceOptions(),
                                                              licenseProperties);
                }

                DigitalSubscriberProperties digitalSubscriberProperties = new DigitalSubscriberProperties();

                digitalSubscriberProperties.setSubscriberId(subscriptionRequesterGUID);

                collectionClient.linkSubscriber(targetAsset.getActionTargetGUID(),
                                                subscriptionGUID,
                                                collectionClient.getMetadataSourceOptions(),
                                                digitalSubscriberProperties);


                String provisioningProcessGUID = governanceContext.createProcessFromGovernanceActionType(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName + "::" + ManageDigitalSubscriptionActionTarget.PROVISIONING_ACTION_TYPE.getName() + "::" + subscriptionGUID,
                                                                                                         ManageDigitalSubscriptionActionTarget.PROVISIONING_ACTION_TYPE.getName(),
                                                                                                         ManageDigitalSubscriptionActionTarget.PROVISIONING_ACTION_TYPE.description,
                                                                                                         provisioningActionType.getActionTargetGUID(),
                                                                                                         null,
                                                                                                         subscriptionGUID,
                                                                                                         null);

                openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                               provisioningProcessGUID,
                                                               targetAsset.getActionTargetGUID(),
                                                               null,
                                                               null,
                                                               propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_TARGET.getName()));

                openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                               provisioningProcessGUID,
                                                               sourceAsset.getActionTargetGUID(),
                                                               null,
                                                               null,
                                                               propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_SOURCE.getName()));

                NotificationSubscriberProperties notificationSubscriberProperties = new NotificationSubscriberProperties();

                notificationSubscriberProperties.setActivityStatus(ActivityStatus.IN_PROGRESS);
                notificationSubscriberProperties.setLabel("provisioning-process");
                notificationSubscriberProperties.setDescription("This process will pass any changes to the product data on to the subscriber.");

                governanceDefinitionClient.linkNotificationSubscriber(notificationType.getActionTargetGUID(),
                                                                      provisioningProcessGUID,
                                                                      governanceDefinitionClient.getMetadataSourceOptions(),
                                                                      notificationSubscriberProperties);

                messageDefinition = GovernanceActionConnectorsAuditCode.NEW_SUBSCRIPTION_CREATED.getMessageDefinition(governanceServiceName,
                                                                                                                      subscriptionName,
                                                                                                                      subscriptionGUID,
                                                                                                                      subscriptionItemType,
                                                                                                                      subscriptionItemName,
                                                                                                                      subscriptionItemGUID,
                                                                                                                      subscriptionRequesterType,
                                                                                                                      subscriptionRequesterName,
                                                                                                                      subscriptionRequesterGUID);

                logRecord(methodName, messageDefinition);

                NewActionTarget newActionTarget = new NewActionTarget();

                newActionTarget.setActionTargetGUID(subscriptionGUID);
                newActionTarget.setActionTargetName(ActionTarget.NEW_DIGITAL_SUBSCRIPTION.name);

                outputActionTargets.add(newActionTarget);

                completionStatus = ManageDigitalSubscriptionGuard.SET_UP_COMPLETE.getCompletionStatus();
                outputGuards.add(ManageDigitalSubscriptionGuard.SET_UP_COMPLETE.getName());
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
