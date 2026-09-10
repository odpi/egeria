/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.lineageinsight.openlineage;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ActionTargetType;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenLineageAnalysisActionTarget provides the action targets supported by the OpenLineage analysis Lovelace services.
 */
public enum OpenLineageAnalysisActionTarget
{
    /**
     * Folder holding the OpenLineage log store.
     */
    OPEN_LINEAGE_LOG_STORE("openLineageLogStore",
                           "Folder asset for the directory holding the OpenLineage log store written by the file-based OpenLineage log store integration connector.  " +
                                   "The service reads every .openlineageevent file below it.",
                           DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getAssociatedTypeName(),
                           DeployedImplementationType.FILE_SYSTEM_DIRECTORY.getDeployedImplementationType()),
    ;

    public final String name;
    public final String description;
    public final String typeName;
    public final String deployedImplementationType;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the action target
     * @param description description of the action target
     * @param typeName open metadata type name of the element that can be an action target
     * @param deployedImplementationType deployed implementation type of the element
     */
    OpenLineageAnalysisActionTarget(String name,
                                    String description,
                                    String typeName,
                                    String deployedImplementationType)
    {
        this.name                       = name;
        this.description                = description;
        this.typeName                   = typeName;
        this.deployedImplementationType = deployedImplementationType;
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
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the open metadata type name of the action target.
     *
     * @return type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return the deployed implementation type of the action target.
     *
     * @return deployed implementation type
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Retrieve all the defined action targets
     *
     * @return list of action target types
     */
    public static List<ActionTargetType> getActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        for (OpenLineageAnalysisActionTarget actionTarget : OpenLineageAnalysisActionTarget.values())
        {
            actionTargetTypes.add(actionTarget.getActionTargetType());
        }

        return actionTargetTypes;
    }


    /**
     * Return the action target type for this action target.
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
        actionTargetType.setRequired(false);

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
        return "OpenLineageAnalysisActionTarget{ name=" + name + "}";
    }
}
