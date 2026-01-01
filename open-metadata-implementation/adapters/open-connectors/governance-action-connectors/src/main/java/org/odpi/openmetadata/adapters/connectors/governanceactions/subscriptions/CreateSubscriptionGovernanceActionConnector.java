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
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementActorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementItemProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalSubscriberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalSubscriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationSubscriberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
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
            String                    subscriptionIdentifier = super.getProperty(ManageDigitalSubscriptionRequestParameter.SUBSCRIPTION_IDENTIFIER.getName(), null);
            String                    subscriptionDescription = super.getProperty(ManageDigitalSubscriptionRequestParameter.SUBSCRIPTION_DESCRIPTION.getName(), null);

            /*
             * Supplied by the caller
             */
            ActionTargetElement       targetAsset = super.getActionTarget(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_TARGET.getName());
            ActionTargetElement       subscriptionRequester = super.getActionTarget(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_REQUESTER.getName());

            /*
             * Set up by the harvester
             */
            ActionTargetElement       subscriptionItem = super.getActionTarget(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_ITEM.getName()); // the product
            List<ActionTargetElement> productOwners = super.getAllActionTargets(ManageDigitalSubscriptionActionTarget.DIGITAL_PRODUCT_OWNER.getName());
            ActionTargetElement       provisioningActionType = super.getActionTarget(ManageDigitalSubscriptionActionTarget.PROVISIONING_ACTION_TYPE.getName());
            ActionTargetElement       cancellingActionType = super.getActionTarget(ManageDigitalSubscriptionActionTarget.CANCELLING_ACTION_TYPE.getName());
            List<ActionTargetElement> serviceLevelObjectives = super.getAllActionTargets(ManageDigitalSubscriptionActionTarget.SERVICE_LEVEL_OBJECTIVE.getName());

            if (subscriptionName == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.MISSING_REQUEST_PARAMETER.getMessageDefinition(governanceServiceName, ManageDigitalSubscriptionRequestParameter.SUBSCRIPTION_NAME.getName());
                outputGuards.add(ManageDigitalSubscriptionGuard.MISSING_REQUEST_PARAMETER.getName());
                completionStatus = ManageDigitalSubscriptionGuard.MISSING_REQUEST_PARAMETER.getCompletionStatus();
            }
            else if (subscriptionIdentifier == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.MISSING_REQUEST_PARAMETER.getMessageDefinition(governanceServiceName, ManageDigitalSubscriptionRequestParameter.SUBSCRIPTION_IDENTIFIER.getName());
                outputGuards.add(ManageDigitalSubscriptionGuard.MISSING_REQUEST_PARAMETER.getName());
                completionStatus = ManageDigitalSubscriptionGuard.MISSING_REQUEST_PARAMETER.getCompletionStatus();
            }
            else if (targetAsset == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.MISSING_ACTION_TARGET.getMessageDefinition(governanceServiceName, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_TARGET.getName());
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

                /*
                 * Create the subscription
                 */
                String subscriptionGUID = this.setUpSubscription(subscriptionName,
                                                                 subscriptionIdentifier,
                                                                 subscriptionDescription,
                                                                 subscriptionItemGUID,
                                                                 subscriptionRequesterGUID,
                                                                 targetAsset.getActionTargetGUID(),
                                                                 provisioningActionType.getActionTargetGUID(),
                                                                 cancellingActionType.getActionTargetGUID(),
                                                                 productOwners,
                                                                 serviceLevelObjectives);

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


    /**
     * Set up the subscription(s) for the requested product (or product family).
     *
     * @param subscriptionName name of the subscription
     * @param subscriptionIdentifier identifier use to locate the correct notification
     * @param subscriptionDescription description of the subscription
     * @param productGUID unique identifier of the product that the subscription is for
     * @param subscriptionRequesterGUID unique identifier of the actor requesting the subscription
     * @param targetAssetGUID the destination for the product data
     * @param provisioningActionTypeGUID the governance action used to provision to the target data source
     * @param cancellingActionTypeGUID the governance action used to cancel the subscription
     * @param productOwners list of product owners (typically this is one product manager role)
     * @param serviceLevelObjectives one of more service levels that the provisioning pipeline is coded to meet.
     * @return unique identifier of the new subscription
     * @throws InvalidParameterException bad parameter
     * @throws UserNotAuthorizedException this governance service is not authorized to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private String setUpSubscription(String                    subscriptionName,
                                     String                    subscriptionIdentifier,
                                     String                    subscriptionDescription,
                                     String                    productGUID,
                                     String                    subscriptionRequesterGUID,
                                     String                    targetAssetGUID,
                                     String                    provisioningActionTypeGUID,
                                     String                    cancellingActionTypeGUID,
                                     List<ActionTargetElement> productOwners,
                                     List<ActionTargetElement> serviceLevelObjectives) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {

        CollectionClient productClient = governanceContext.getCollectionClient(OpenMetadataType.DIGITAL_PRODUCT.typeName);

        OpenMetadataRootElement productDetails = productClient.getCollectionByGUID(productGUID, productClient.getGetOptions());

        if (productDetails != null)
        {
            List<RelatedMetadataElementSummary> productGovernanceDefinitions = productDetails.getGovernedBy();
            List<RelatedMetadataElementSummary> collectionMembers            = productDetails.getCollectionMembers();

            /*
             * The product may be a product or a product family.  The subscription is the same,
             * but only the product with at least one asset as aa member has a pipeline set up.
             * A nested subscription is set up for each product nested in the product families.
             */
            String subscriptionGUID = this.createSubscription(subscriptionName,
                                                              subscriptionIdentifier,
                                                              subscriptionDescription,
                                                              productGUID,
                                                              productDetails.getElementHeader().getType().getTypeName(),
                                                              subscriptionRequesterGUID,
                                                              productOwners);

            if (collectionMembers != null)
            {
                setUpCancellationProcess(subscriptionGUID,
                                         targetAssetGUID,
                                         cancellingActionTypeGUID,
                                         productGovernanceDefinitions);

                addServiceLevelObjectives(subscriptionGUID,
                                          serviceLevelObjectives);

                /*
                 * Search the members to detect nested products and/or assets
                 */
                List<String> sourceAssetGUIDs     = new ArrayList<>();

                for (RelatedMetadataElementSummary collectionMember : collectionMembers)
                {
                    if (collectionMember != null)
                    {
                        if (propertyHelper.isTypeOf(collectionMember.getRelatedElement().getElementHeader(), OpenMetadataType.DIGITAL_PRODUCT.typeName))
                        {
                            String childSubscriptionGUID = this.setUpSubscription(subscriptionName,
                                                                                  subscriptionIdentifier,
                                                                                  subscriptionDescription,
                                                                                  collectionMember.getRelatedElement().getElementHeader().getGUID(),
                                                                                  subscriptionRequesterGUID,
                                                                                  targetAssetGUID,
                                                                                  provisioningActionTypeGUID,
                                                                                  cancellingActionTypeGUID,
                                                                                  productOwners,
                                                                                  serviceLevelObjectives);

                            CollectionMembershipProperties collectionMembershipProperties = new CollectionMembershipProperties();

                            collectionMembershipProperties.setMembershipType("nested-subscription");

                            productClient.addToCollection(subscriptionGUID, childSubscriptionGUID, new MakeAnchorOptions(productClient.getMetadataSourceOptions()), collectionMembershipProperties);
                        }
                        else if (propertyHelper.isTypeOf(collectionMember.getRelatedElement().getElementHeader(), OpenMetadataType.ASSET.typeName))
                        {
                            sourceAssetGUIDs.add(collectionMember.getRelatedElement().getElementHeader().getGUID());
                        }
                    }
                }

                if (! sourceAssetGUIDs.isEmpty())
                {
                    for (String sourceAssetGUID : sourceAssetGUIDs)
                    {
                        AssetClient assetClient = governanceContext.getAssetClient();

                        OpenMetadataRootElement asset = assetClient.getAssetByGUID(sourceAssetGUID, assetClient.getGetOptions());

                        if ((asset != null) && (asset.getMonitoredThrough() != null))
                        {
                            String  notificationTypeGUID = null;

                            for (RelatedMetadataElementSummary notificationType : asset.getMonitoredThrough())
                            {
                                if ((notificationType != null) &&
                                        (notificationType.getRelatedElement().getProperties() instanceof NotificationTypeProperties notificationTypeProperties) &&
                                        (subscriptionIdentifier.equals(notificationTypeProperties.getIdentifier())))
                                {
                                    notificationTypeGUID = notificationType.getRelatedElement().getElementHeader().getGUID();
                                }
                            }

                            if (notificationTypeGUID != null)
                            {
                                setUpProvisioningPipeline(sourceAssetGUID,
                                                          targetAssetGUID,
                                                          notificationTypeGUID,
                                                          provisioningActionTypeGUID,
                                                          subscriptionGUID,
                                                          subscriptionRequesterGUID,
                                                          productGovernanceDefinitions);
                            }
                        }
                    }
                }
            }
        }

        return null;
    }


    /**
     * Create the subscription element and link the agreement actors (product owners and subscriber) and
     * agreement item (product).
     *
     * @param subscriptionName name for the subscription type
     * @param subscriptionIdentifier identifier for the subscription type
     * @param subscriptionDescription description for the subscription type
     * @param productGUID unique identifier for the product
     * @param productTypeName this is the type of product (product or product group)
     * @param subscriptionRequesterGUID this is actor requesting the subscription
     * @param productOwners list of owners (typically one product manager)
     *
     * @return guid of the subscription element
     * @throws InvalidParameterException bad parameter
     * @throws UserNotAuthorizedException this governance service is not authorized to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private String createSubscription(String                    subscriptionName,
                                      String                    subscriptionIdentifier,
                                      String                    subscriptionDescription,
                                      String                    productGUID,
                                      String                    productTypeName,
                                      String                    subscriptionRequesterGUID,
                                      List<ActionTargetElement> productOwners) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        CollectionClient collectionClient = governanceContext.getCollectionClient(OpenMetadataType.DIGITAL_SUBSCRIPTION.typeName);

        DigitalSubscriptionProperties digitalSubscriptionProperties = new DigitalSubscriptionProperties();

        digitalSubscriptionProperties.setQualifiedName(OpenMetadataType.DIGITAL_SUBSCRIPTION.typeName + "::" + subscriptionName + "::" + new Date());
        digitalSubscriptionProperties.setDisplayName(subscriptionName);
        digitalSubscriptionProperties.setIdentifier(subscriptionIdentifier);
        digitalSubscriptionProperties.setDescription(subscriptionDescription);

        NewElementOptions newElementOptions = new NewElementOptions(collectionClient.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorGUID(productGUID);
        newElementOptions.setParentAtEnd1(false);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName);
        newElementOptions.setParentGUID(productGUID);

        AgreementItemProperties agreementItemProperties = new AgreementItemProperties();

        agreementItemProperties.setAgreementItemId(productTypeName);
        agreementItemProperties.setAgreementStart(new Date());

        String subscriptionGUID = collectionClient.createCollection(newElementOptions,
                                                                    null,
                                                                    digitalSubscriptionProperties,
                                                                    agreementItemProperties);

        AgreementActorProperties agreementActorProperties = new AgreementActorProperties();

        agreementActorProperties.setActorName(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_REQUESTER.getName());

        collectionClient.linkAgreementActor(subscriptionGUID,
                                            subscriptionRequesterGUID,
                                            new MakeAnchorOptions(collectionClient.getMetadataSourceOptions()),
                                            agreementActorProperties);

        for (ActionTargetElement productOwner : productOwners)
        {
            if (productOwner != null)
            {
                agreementActorProperties.setActorName(ManageDigitalSubscriptionActionTarget.DIGITAL_PRODUCT_OWNER.getName());

                collectionClient.linkAgreementActor(subscriptionGUID,
                                                    productOwner.getActionTargetGUID(),
                                                    new MakeAnchorOptions(collectionClient.getMetadataSourceOptions()),
                                                    agreementActorProperties);
            }
        }

        return subscriptionGUID;
    }



    /**
     * Set up the cancellation process for a subscription.  It is attached to subscription so the subscriber can
     * easily find it.
     *
     * @param subscriptionGUID unique identifier of the subscription
     * @param targetAssetGUID the destination asset
     * @param cancellingActionTypeGUID the governance action type to act as a template for the new process
     * @param productGovernanceDefinitions list of product governance definitions (including license that will need to be cancelled with the subscription).
     *
     * @throws InvalidParameterException bad parameter
     * @throws UserNotAuthorizedException this governance service is not authorized to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private void setUpCancellationProcess(String                              subscriptionGUID,
                                          String                              targetAssetGUID,
                                          String                              cancellingActionTypeGUID,
                                          List<RelatedMetadataElementSummary> productGovernanceDefinitions) throws InvalidParameterException,
                                                                                                                   PropertyServerException,
                                                                                                                   UserNotAuthorizedException
    {
        OpenMetadataStore openMetadataStore = governanceContext.getOpenMetadataStore();

        /*
         * Use the governance action type as a template to create the specific cancel process for the subscription.
         */
        String cancelSubscriptionGUID = governanceContext.createProcessFromGovernanceActionType(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName + "::" + ManageDigitalSubscriptionActionTarget.CANCELLING_ACTION_TYPE.getName() + "::" + subscriptionGUID,
                                                                                                ManageDigitalSubscriptionActionTarget.CANCELLING_ACTION_TYPE.getName(),
                                                                                                ManageDigitalSubscriptionActionTarget.CANCELLING_ACTION_TYPE.description,
                                                                                                cancellingActionTypeGUID,
                                                                                                null,
                                                                                                subscriptionGUID,
                                                                                                null);

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                       cancelSubscriptionGUID,
                                                       subscriptionGUID,
                                                       null,
                                                       null,
                                                       propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION.getName()));

        if (targetAssetGUID != null)
        {
            /*
             * If the subscription is associated with an asset then link the asset and any licenses to the cancel
             * process, so it can cancel the license with the subscription.
             */
            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                           cancelSubscriptionGUID,
                                                           targetAssetGUID,
                                                           null,
                                                           null,
                                                           propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_TARGET.getName()));

            if (productGovernanceDefinitions != null)
            {
                for (RelatedMetadataElementSummary productGovernanceDefinition : productGovernanceDefinitions)
                {
                    if ((productGovernanceDefinition != null) && (propertyHelper.isTypeOf(productGovernanceDefinition.getRelatedElement().getElementHeader(), OpenMetadataType.LICENSE_TYPE.typeName)))
                    {
                        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                                       cancelSubscriptionGUID,
                                                                       productGovernanceDefinition.getRelatedElement().getElementHeader().getGUID(),
                                                                       null,
                                                                       null,
                                                                       propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.LICENSE_TYPE.getName()));
                    }
                }
            }
        }

        /*
         * The new cancellation process is connected to the subscription's resource list for east access.
         */
        ClassificationManagerClient classificationManagerClient = governanceContext.getClassificationManagerClient();

        ResourceListProperties resourceListProperties = new  ResourceListProperties();

        resourceListProperties.setResourceUse(ResourceUse.CANCEL_SUBSCRIPTION.getResourceUse());
        resourceListProperties.setDescription(ResourceUse.CANCEL_SUBSCRIPTION.getDescription());

        classificationManagerClient.addResourceListToElement(subscriptionGUID,
                                                             cancelSubscriptionGUID,
                                                             new MakeAnchorOptions(classificationManagerClient.getMetadataSourceOptions()),
                                                             resourceListProperties);


    }


    /**
     * Set up the cancellation process for a subscription.  It is attached to subscription so the subscriber can
     * easily find it.
     *
     * @param subscriptionGUID unique identifier of the subscription
     * @param serviceLevelObjectives details of the service
     *
     * @throws InvalidParameterException bad parameter
     * @throws UserNotAuthorizedException this governance service is not authorized to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private void addServiceLevelObjectives(String                    subscriptionGUID,
                                           List<ActionTargetElement> serviceLevelObjectives) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        if (serviceLevelObjectives != null)
        {
            GovernanceDefinitionClient governanceDefinitionClient = governanceContext.getGovernanceDefinitionClient();

            GovernedByProperties governedByProperties = new GovernedByProperties();

            governedByProperties.setLabel("service level objective");
            governedByProperties.setDescription("The level of service to expect through this subscription");

            for (ActionTargetElement serviceLevelObjective : serviceLevelObjectives)
            {
                if (serviceLevelObjective != null)
                {
                    governanceDefinitionClient.addGovernanceDefinitionToElement(subscriptionGUID,
                                                                                serviceLevelObjective.getActionTargetGUID(),
                                                                                new MakeAnchorOptions(governanceDefinitionClient.getMetadataSourceOptions()),
                                                                                governedByProperties);
                }
            }
        }
    }


    /**
     * Create the provisioning pipeline to deliver data to the consumer.  This is all of the work needed to
     * connect a real product (with data) into the subscription.
     *
     * @param sourceAssetGUID this is the product's asset
     * @param targetAssetGUID asset provided by the caller that defines where data is to be sent
     * @param notificationTypeGUID notification type that controls calls to the pipeline when data changes
     * @param provisioningActionTypeGUID definition of the service to call
     * @param subscriptionGUID set anchor to subscription so that the cancel subscription removes the pipeline too
     * @param subscriptionRequesterGUID unique identifier of the subscription requester
     * @param productGovernanceDefinitions the governance definitions
     *
     * @throws InvalidParameterException bad parameter
     * @throws UserNotAuthorizedException this governance service is not authorized to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    private void   setUpProvisioningPipeline(String                              sourceAssetGUID,
                                             String                              targetAssetGUID,
                                             String                              notificationTypeGUID,
                                             String                              provisioningActionTypeGUID,
                                             String                              subscriptionGUID,
                                             String                              subscriptionRequesterGUID,
                                             List<RelatedMetadataElementSummary> productGovernanceDefinitions) throws InvalidParameterException,
                                                                                                                      PropertyServerException,
                                                                                                                      UserNotAuthorizedException
    {
        OpenMetadataStore openMetadataStore = governanceContext.getOpenMetadataStore();
        GovernanceDefinitionClient governanceDefinitionClient = governanceContext.getGovernanceDefinitionClient(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName);

        /*
         * The governance action type is used as a template to create the process.
         */
        String provisioningProcessGUID = governanceContext.createProcessFromGovernanceActionType(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName + "::" + ManageDigitalSubscriptionActionTarget.PROVISIONING_ACTION_TYPE.getName() + "::" + subscriptionGUID,
                                                                                                 ManageDigitalSubscriptionActionTarget.PROVISIONING_ACTION_TYPE.getName(),
                                                                                                 ManageDigitalSubscriptionActionTarget.PROVISIONING_ACTION_TYPE.description,
                                                                                                 provisioningActionTypeGUID,
                                                                                                 null,
                                                                                                 subscriptionGUID, // anchorGUID
                                                                                                 null);

        /*
         * The source and destination assets are connected to the governance action process so it knows which assets to work with.
         */
        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                       provisioningProcessGUID,
                                                       targetAssetGUID,
                                                       null,
                                                       null,
                                                       propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_TARGET.getName()));

        openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                       provisioningProcessGUID,
                                                       sourceAssetGUID,
                                                       null,
                                                       null,
                                                       propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_SOURCE.getName()));


        /*
         * Linking the pipeline to the notification type ensures that the pipeline is called whenever the watchdog
         * detects changes in the data.
         */
        NotificationSubscriberProperties notificationSubscriberProperties = new NotificationSubscriberProperties();

        notificationSubscriberProperties.setActivityStatus(ActivityStatus.IN_PROGRESS);
        notificationSubscriberProperties.setLabel("provisioning-process");
        notificationSubscriberProperties.setDescription("This process will pass any changes to the product data on to the subscriber.");

        governanceDefinitionClient.linkNotificationSubscriber(notificationTypeGUID,
                                                              provisioningProcessGUID,
                                                              new MakeAnchorOptions(governanceDefinitionClient.getMetadataSourceOptions()),
                                                              notificationSubscriberProperties);


        /*
         * This relationship provides traceability from the subscription to the pipeline that is providing the service.
         * This allows the status of the pipeline to be monitored by the subscriber.
         */
        ImplementedByProperties implementedByProperties = new ImplementedByProperties();

        implementedByProperties.setRole("provisioning-process");
        implementedByProperties.setDescription("This process will pass any changes to the product data on to the subscriber.");

        governanceDefinitionClient.linkDesignToImplementation(subscriptionGUID,
                                                              provisioningProcessGUID,
                                                              new MakeAnchorOptions(governanceDefinitionClient.getMetadataSourceOptions()),
                                                              implementedByProperties);

        /*
         * Link the subscription to the destination asset,
         */
        CollectionClient collectionClient = governanceContext.getCollectionClient();
        MakeAnchorOptions makeAnchorOptions = new MakeAnchorOptions(collectionClient.getMetadataSourceOptions());

        DigitalSubscriberProperties digitalSubscriberProperties = new DigitalSubscriberProperties();

        digitalSubscriberProperties.setSubscriberId(subscriptionRequesterGUID);

        collectionClient.linkSubscriber(targetAssetGUID,
                                        subscriptionGUID,
                                        makeAnchorOptions,
                                        digitalSubscriberProperties);

        collectionClient.linkSubscriber(targetAssetGUID, subscriptionGUID, makeAnchorOptions, null);

        /*
         * Add the license to the destination data asset.
         */
        if (productGovernanceDefinitions != null)
        {
            for (RelatedMetadataElementSummary governanceDefinition : productGovernanceDefinitions)
            {
                if ((governanceDefinition != null) && (propertyHelper.isTypeOf(governanceDefinition.getRelatedElement().getElementHeader(), OpenMetadataType.LICENSE_TYPE.typeName)))
                {
                    LicenseProperties licenseProperties = new LicenseProperties();

                    licenseProperties.setCoverageStart(new Date());
                    licenseProperties.setLicenseGUID(subscriptionGUID);

                    governanceDefinitionClient.licenseElement(targetAssetGUID,
                                                              governanceDefinition.getRelatedElement().getElementHeader().getGUID(),
                                                              new MakeAnchorOptions(governanceDefinitionClient.getMetadataSourceOptions()),
                                                              licenseProperties);
                }
            }
        }
    }
}
