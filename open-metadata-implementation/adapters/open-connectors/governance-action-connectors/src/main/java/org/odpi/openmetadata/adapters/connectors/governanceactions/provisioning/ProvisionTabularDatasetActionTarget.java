/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ActionTargetEnum characterises the action targets that this governance action service works with/
 */
public enum ProvisionTabularDatasetActionTarget
{
    SOURCE_DATA_SET("sourceDataSet",
                    DeployedImplementationType.TABULAR_DATA_SET.getDescription(),
                    DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                    DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType(),
                    null),

    DESTINATION_DATA_SET("destinationDataSet",
                         DeployedImplementationType.TABULAR_DATA_SET.getDescription(),
                         DeployedImplementationType.TABULAR_DATA_SET.getAssociatedTypeName(),
                         DeployedImplementationType.TABULAR_DATA_SET.getDeployedImplementationType(),
                         null),

    ;;


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
    ProvisionTabularDatasetActionTarget(String name,
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
     * Return the targets defined in this enum for a PostgreSQL server.
     *
     * @return list
     */
    public static List<ActionTargetType> getActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        for (ProvisionTabularDatasetActionTarget actionTarget : ProvisionTabularDatasetActionTarget.values())
        {
            actionTargetTypes.add(actionTarget.getActionTargetType());
        }

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
