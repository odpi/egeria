/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.wedgwood;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ActionTargetType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ActionTargetEnum characterises the action targets that this governance action service works with.
 * Both are data sets: a single tabular data set, or a collection of them - a digital product family's asset on
 * the source side, a schema of tables on the destination side.  The service delivers a collection a table at a
 * time.
 */
public enum WedgwoodProvisionSubscriptionActionTarget
{
    /**
     * The tabular data set, or collection of tabular data sets, to copy from.
     */
    SOURCE_DATA_SET("sourceDataSet",
                    "The tabular data set, or collection of tabular data sets, to copy from.  A collection - such as a digital product family's asset - is delivered a table at a time.",
                    DeployedImplementationType.DATA_SET.getAssociatedTypeName(),
                    DeployedImplementationType.DATA_SET.getDeployedImplementationType(),
                    null),

    /**
     * The tabular data set, or collection of tabular data sets, to copy to.
     */
    DESTINATION_DATA_SET("destinationDataSet",
                         "The tabular data set, or collection of tabular data sets, to copy to.  A collection - such as a database schema - receives one table per source table.",
                         DeployedImplementationType.DATA_SET.getAssociatedTypeName(),
                         DeployedImplementationType.DATA_SET.getDeployedImplementationType(),
                         null),

    ;


    /**
     * Action target name
     */
    private final String name;

    /**
     * Description of the action target.
     */
    public final String description;

    /**
     * The open metadata type name of the element that can be this type of action target.
     */
    private final String typeName;


    /**
     * The deployed implementation type allows the service to be more specific about the resources it works with.
     */
    private final String deployedImplementationType;

    /**
     * A map of property name to property value for values that should match in the action target for it to be compatible with this integration
     * connector.
     */
    private final Map<String, String> otherPropertyValues;


    /**
     * Constructor for the enum.
     *
     * @param name ActionTargetName
     * @param description description of action target
     * @param typeName Open Metadata Type Name for action target
     * @param deployedImplementationType optional deployed implementation type
     * @param otherPropertyValues other values
     */
    WedgwoodProvisionSubscriptionActionTarget(String name,
                                              String description,
                                              String typeName,
                                              String deployedImplementationType,
                                              Map<String, String> otherPropertyValues)
    {
        this.name                       = name;
        this.description                = description;
        this.typeName                   = typeName;
        this.deployedImplementationType = deployedImplementationType;
        this.otherPropertyValues        = otherPropertyValues;
    }


    /**
     * Return the name of the action target.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the action target.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the type name of the element that is linked to for this type of action target.
     *
     * @return type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return the preferred value of the deployed implementation type of the element that is linked to for
     * this type of action target.  This is typically only set for assets.
     *
     * @return preferred value string
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return a map of property name to property value that the elements linked to this action target should have.
     *
     * @return map
     */
    public Map<String, String> getOtherPropertyValues()
    {
        return otherPropertyValues;
    }



    /**
     * Return all the action targets defined in this enum.
     *
     * @return list
     */
    public static List<ActionTargetType> getActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        for (WedgwoodProvisionSubscriptionActionTarget actionTarget : WedgwoodProvisionSubscriptionActionTarget.values())
        {
            actionTargetTypes.add(actionTarget.getActionTargetType());
        }

        return actionTargetTypes;
    }


    /**
     * Return the action target type for a specific action target enum.
     *
     * @return action target type
     */
    public ActionTargetType getActionTargetType()
    {
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(name);
        actionTargetType.setDescription(description);
        actionTargetType.setOpenMetadataTypeName(typeName);
        actionTargetType.setDeployedImplementationType(deployedImplementationType);

        return actionTargetType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ActionTargetEnum{actionTargetName='" + name + "'}";
    }
}
