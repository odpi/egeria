/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.survey;

import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;

import java.util.ArrayList;
import java.util.List;

/**
 * PostgresActionTarget provides the standard action type names to use with the postgres database server survey.
 */
public enum PostgresActionTarget
{
    /**
     * PostgreSQL Database Server.
     */
    DATABASE_SERVER("postgresServer",
                    DeployedImplementationType.POSTGRESQL_SERVER.getDescription(),
                    DeployedImplementationType.POSTGRESQL_SERVER.getAssociatedTypeName(),
                    DeployedImplementationType.POSTGRESQL_SERVER.getDeployedImplementationType()),

    /**
     * PostgreSQL Database.
     */
    DATABASE("postgresDatabase",
             DeployedImplementationType.POSTGRESQL_DATABASE.getDescription(),
             DeployedImplementationType.POSTGRESQL_DATABASE.getAssociatedTypeName(),
             DeployedImplementationType.POSTGRESQL_DATABASE.getDeployedImplementationType()),

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
    PostgresActionTarget(String  name,
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
     * Return the action targets defined in this enum for a PostgreSQL server.
     *
     * @return list
     */
    public static List<ActionTargetType> getPostgresServerActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

       actionTargetTypes.add(DATABASE_SERVER.getActionTargetType());

        return actionTargetTypes;
    }


    /**
     * Return the action targets defined in this enum for a PostgreSQL database.
     *
     * @return list
     */
    public static List<ActionTargetType> getPostgresDatabaseActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        actionTargetTypes.add(DATABASE.getActionTargetType());

        return actionTargetTypes;
    }


    /**
     * Return an action target type for use in the governance service's provider.
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
