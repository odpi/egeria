/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.controls;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

/**
 * ActionTarget provides some standard action type names to use in the implementation of governance services.
 * Using standard action target names helps to simplify the use of the governance services in
 * governance action processes.
 */
public enum ActionTarget
{
    /**
     * Any type of element.
     */
    ANY_ELEMENT("*",
                "Any type of element.",
                OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                null),

    /**
     * Any element of type Referenceable (or a subtype of).
     */
    ANY_REFERENCEABLE("*",
                      "Any element of type Referenceable (or a subtype of).",
                      OpenMetadataType.REFERENCEABLE.typeName,
                      null),

    /**
     * Any element of type Referenceable (or a subtype of).
     */
    ANY_ASSET("*",
              "Any element of type Asset (or a subtype of).",
              OpenMetadataType.ASSET.typeName,
              null),


    /**
     * A newly created Asset (or a subtype of).
     */
    NEW_ASSET("newAsset",
              "A newly created Asset (or a subtype of).",
              OpenMetadataType.ASSET.typeName,
              null),

    /**
     * A person or team who is accountable for the correct operation of a process.
     */
    PROCESS_OWNER("processOwner",
               "A person or team who is accountable for the correct operation of a process.",
               OpenMetadataType.ACTOR.typeName,
               null),

    /**
     * A person or team who is accountable for the correct management and use of a particular collection of data.
     */
    DATA_OWNER("dataOwner",
              "A person or team who is accountable for the correct management and use of a particular collection of data.",
              OpenMetadataType.ACTOR.typeName,
              null),


    /**
     * A person that is described by some data.
     */
    DATA_SUBJECT("dataSubject",
               "A person that is described by some data.",
               OpenMetadataType.ACTOR.typeName,
               null),


    /**
     * The person who can be contacted if there are problems with the data from this hospital.
     */
    CUSTODIAN("custodian",
              "The person, or team who is responsible for the correct management and use of a particular collection of data inside the consuming organization.",
              OpenMetadataType.ACTOR.typeName,
              null),

    /**
     * The person who can be contacted if there are problems with the data from this hospital.
     */
    STEWARD("steward",
              "The person, or team who is responsible for setting up the governance metadata associated with a resource.",
              OpenMetadataType.ACTOR.typeName,
              null),

    /**
     * The person who can be contacted if there are problems with the data from this hospital.
     */
    INTEGRATION_CONNECTOR("integrationConnector",
                          "An integration connector that the service should work with.",
                          DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                          DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType()),
    ;


    /**
     * Action target name
     */
    public final String name;

    /**
     * Description of the action target.
     */
    public final String description;


    /**
     * The open metadata type name of the element that can be this type of action target.
     */
    public final String typeName;


    /**
     * The deployed implementation type allows the service to be more specific about the resources it works with.
     */
    private final String deployedImplementationType;


    /**
     * Constructor for the enum.
     *
     * @param name ActionTargetName
     * @param description description of action target
     * @param typeName Open Metadata Type Name for action target
     * @param deployedImplementationType optional deployed implementation type
     */
    ActionTarget(String  name,
                 String  description,
                 String  typeName,
                 String  deployedImplementationType)
    {
        this.name                       = name;
        this.description                = description;
        this.typeName                   = typeName;
        this.deployedImplementationType = deployedImplementationType;
    }


    /**
     * Return all the action targets defined in this enum.
     *
     * @return list
     */
    public static List<ActionTargetType> getActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        for (ActionTarget actionTarget : ActionTarget.values())
        {
            actionTargetTypes.add(actionTarget.getActionTargetType());
        }

        return actionTargetTypes;
    }


    /**
     * Return an action target type for use in the governance action service's provider.
     *
     * @return action target type
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ActionTarget{name='" + name + "}";
    }
}
