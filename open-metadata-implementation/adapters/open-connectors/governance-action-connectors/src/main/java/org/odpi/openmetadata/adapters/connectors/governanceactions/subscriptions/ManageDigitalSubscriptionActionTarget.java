/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.subscriptions;

import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.ProvisionTabularDatasetActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ActionTargetEnum characterises the action targets that this governance action service works with.
 */
public enum ManageDigitalSubscriptionActionTarget
{
    /**
     * The digital subscription that is being managed.
     */
    DIGITAL_SUBSCRIPTION("digitalSubscription",
                               "The digital subscription that is being managed.",
                               OpenMetadataType.DIGITAL_SUBSCRIPTION.typeName,
                               null,
                               null),

    /**
     * An actor requesting the digital subscription.
     */
    DIGITAL_SUBSCRIPTION_REQUESTER("digitalSubscriptionRequester",
                               "An actor requesting the digital subscription.",
                               OpenMetadataType.ACTOR.typeName,
                               null,
                               null),

    /**
     * An actor that owns the resource being subscribed to.
     */
    DIGITAL_PRODUCT_OWNER("digitalProductOwner",
                               "An actor that owns the resource being subscribed to.",
                               OpenMetadataType.ACTOR.typeName,
                               null,
                               null),

    /**
     * The element that is being subscribed to.
     */
    DIGITAL_SUBSCRIPTION_ITEM("digitalSubscriptionItem",
                               "The element that is being subscribed to.",
                               OpenMetadataType.REFERENCEABLE.typeName,
                               null,
                               null),


    /**
     * The Asset that is the target of the subscription.
     */
    DIGITAL_SUBSCRIPTION_TARGET(ProvisionTabularDatasetActionTarget.DESTINATION_DATA_SET.getName(),
                                "The Asset that is the target of the subscription.",
                                DeployedImplementationType.DATA_ASSET.getAssociatedTypeName(),
                                DeployedImplementationType.DATA_ASSET.getDeployedImplementationType(),
                                null),


    /**
     * The Asset that is the source of the subscription.
     */
    DIGITAL_SUBSCRIPTION_SOURCE(ProvisionTabularDatasetActionTarget.SOURCE_DATA_SET.getName(),
                                "The Asset that is the source of the subscription.",
                                DeployedImplementationType.DATA_ASSET.getAssociatedTypeName(),
                                DeployedImplementationType.DATA_ASSET.getDeployedImplementationType(),
                                null),


    /**
     * The governance action type that implements the provisioning action.
     */
    PROVISIONING_ACTION_TYPE("provisioningGovernanceActionType",
                       "The governance action type that implements the provisioning action.",
                       OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName,
                       null,
                       null),

    /**
     * The governance action type that implements the cancel-subscription action.
     */
    CANCELLING_ACTION_TYPE("cancellingGovernanceActionType",
                             "The governance action type that implements the cancel-subscription action.",
                             OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName,
                             null,
                             null),


    /**
     * The notification type that is used to control the subscription manager.
     */
    NOTIFICATION_TYPE("subscriptionManagerNotificationType",
                      "The notification type that is used to control the subscription manager.",
                      OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName,
                      null,
                      null),


    /**
     * The type of licence that is granted to the subscriber's asset.
     */
    LICENSE_TYPE("licenseType",
                      "The type of licence that is granted to the subscriber's asset.",
                      OpenMetadataType.LICENSE_TYPE.typeName,
                      null,
                      null),


    /**
     * The service level object offered by this subscription.
     */
    SERVICE_LEVEL_OBJECTIVE("serviceLevelObjective",
                 "The service level object offered by this subscription.",
                 OpenMetadataType.LICENSE_TYPE.typeName,
                 null,
                 null),


    ;


    /**
     * Catalog target name.
     */
    private final String name;

    /**
     * Description of the target.
     */
    public final String description;

    /**
     * The open metadata type name of the element that can be a target.
     */
    private final String typeName;


    /**
     * The deployed implementation type allows the connector to be more specific about the resources it works with.
     */
    private final String deployedImplementationType;

    /**
     * A map of property name to property value for values that should match in the target for it to be compatible with this integration
     * connector.
     */
    private final Map<String, String> otherPropertyValues;


    /**
     * Constructor for Enum
     *
     * @param name target name
     * @param description description of target
     * @param typeName open metadata type name for the linked element
     * @param deployedImplementationType deployed implementation type for the linked element
     * @param otherPropertyValues other values
     */
    ManageDigitalSubscriptionActionTarget(String              name,
                                          String              description,
                                          String              typeName,
                                          String              deployedImplementationType,
                                          Map<String, String> otherPropertyValues)
    {
        this.name                       = name;
        this.description                = description;
        this.typeName                   = typeName;
        this.deployedImplementationType = deployedImplementationType;
        this.otherPropertyValues        = otherPropertyValues;
    }


    /**
     * Return the target name.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the target.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the type name (or super type name) of a permitted target.
     *
     * @return name of an open metadata type
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return a more specific definition of a permitted target.
     *
     * @return deployed implementation type name
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return a map of property name to property value that the target should have to be valid for this integration connector.
     *
     * @return map of string to string
     */
    public Map<String, String> getOtherPropertyValues()
    {
        return otherPropertyValues;
    }



    /**
     * Return the targets defined in this enum for all action targets.
     *
     * @return list
     */
    public static List<ActionTargetType> getActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        for (ManageDigitalSubscriptionActionTarget actionTarget : ManageDigitalSubscriptionActionTarget.values())
        {
            actionTargetTypes.add(actionTarget.getActionTargetType());
        }

        return actionTargetTypes;
    }


    /**
     * Return the targets defined in this enum for create subscription.
     *
     * @return list
     */
    public static List<ActionTargetType> getCreateSubscriptionActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        actionTargetTypes.add(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_ITEM.getActionTargetType());
        actionTargetTypes.add(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_REQUESTER.getActionTargetType());
        actionTargetTypes.add(ManageDigitalSubscriptionActionTarget.DIGITAL_PRODUCT_OWNER.getActionTargetType());
        actionTargetTypes.add(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_SOURCE.getActionTargetType());
        actionTargetTypes.add(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_TARGET.getActionTargetType());
        actionTargetTypes.add(ManageDigitalSubscriptionActionTarget.PROVISIONING_ACTION_TYPE.getActionTargetType());
        actionTargetTypes.add(ManageDigitalSubscriptionActionTarget.CANCELLING_ACTION_TYPE.getActionTargetType());
        actionTargetTypes.add(ManageDigitalSubscriptionActionTarget.NOTIFICATION_TYPE.getActionTargetType());
        actionTargetTypes.add(ManageDigitalSubscriptionActionTarget.LICENSE_TYPE.getActionTargetType());
        actionTargetTypes.add(ManageDigitalSubscriptionActionTarget.SERVICE_LEVEL_OBJECTIVE.getActionTargetType());

        return actionTargetTypes;
    }


    /**
     * Return the targets defined in this enum for create subscription.
     *
     * @return list
     */
    public static List<ActionTargetType> getCancelSubscriptionActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        actionTargetTypes.add(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION.getActionTargetType());
        actionTargetTypes.add(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_TARGET.getActionTargetType());
        actionTargetTypes.add(ManageDigitalSubscriptionActionTarget.LICENSE_TYPE.getActionTargetType());

        return actionTargetTypes;
    }


    /**
     * Return an target type for use in the governance service's provider.
     *
     * @return target type
     */
    public ActionTargetType getActionTargetType()
    {
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(name);
        actionTargetType.setDescription(description);
        actionTargetType.setTypeName(typeName);
        actionTargetType.setDeployedImplementationType(deployedImplementationType);

        return actionTargetType;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ActionTargetEnum{actionTargetName='" + name + "'}";
    }
}
