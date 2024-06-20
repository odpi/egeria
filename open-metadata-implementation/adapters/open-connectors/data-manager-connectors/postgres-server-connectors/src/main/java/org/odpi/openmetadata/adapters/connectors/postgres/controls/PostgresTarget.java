/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.postgres.controls;

import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CatalogTargetEnum characterises the type of third party technology supported by a specific integration connector.
 * This enables the capability of an integration connector to be correctly matched to the resources and elements
 * that it works with.
 */
public enum PostgresTarget
{
    SERVER("postgreSQLServer",
                   DeployedImplementationType.POSTGRESQL_SERVER.getDescription(),
                   DeployedImplementationType.POSTGRESQL_SERVER.getAssociatedTypeName(),
                   DeployedImplementationType.POSTGRESQL_SERVER.getDeployedImplementationType(),
                   null),

    /**
     * PostgreSQL Database.
     */
    DATABASE("postgresDatabase",
             DeployedImplementationType.POSTGRESQL_DATABASE.getDescription(),
             DeployedImplementationType.POSTGRESQL_DATABASE.getAssociatedTypeName(),
             DeployedImplementationType.POSTGRESQL_DATABASE.getDeployedImplementationType(),
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
    PostgresTarget(String name,
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
     * Return all the catalog types defined by this enum.
     *
     * @return list of target type
     */
    public static List<CatalogTargetType> getPostgresServerCatalogTargetTypes()
    {
        List<CatalogTargetType> catalogTargetTypes = new ArrayList<>();

        catalogTargetTypes.add(SERVER.getCatalogTargetType());

        return catalogTargetTypes;
    }


    /**
     * Return the target type for a specific target enum.
     *
     * @return target type
     */
    public CatalogTargetType getCatalogTargetType()
    {
        CatalogTargetType catalogTargetType = new CatalogTargetType();

        catalogTargetType.setName(name);
        catalogTargetType.setTypeName(typeName);
        catalogTargetType.setDeployedImplementationType(deployedImplementationType);
        catalogTargetType.setOtherPropertyValues(otherPropertyValues);

        return catalogTargetType;
    }



    /**
     * Return the targets defined in this enum for a PostgreSQL server.
     *
     * @return list
     */
    public static List<ActionTargetType> getPostgresServerActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        actionTargetTypes.add(SERVER.getActionTargetType());

        return actionTargetTypes;
    }


    /**
     * Return the targets defined in this enum for a PostgreSQL database.
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
        return "CatalogTargetEnum{catalogTargetName='" + name + "'}";
    }
}
