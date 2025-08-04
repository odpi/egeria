/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.controls;

import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CatalogTargetEnum characterises the type of third party technology supported by a specific integration connector.
 * This enables the capability of an integration connector to be correctly matched to the resources and elements
 * that it works with.
 */
public enum JDBCDatabaseCatalogTarget
{
    CATALOG_TARGET("databaseToCatalog",
                   DeployedImplementationType.JDBC_RELATIONAL_DATABASE.getAssociatedTypeName(),
                   DeployedImplementationType.JDBC_RELATIONAL_DATABASE.getDeployedImplementationType(),
                   null);


    /**
     * Catalog target name.
     */
    private final String name;


    /**
     * The open metadata type name of the element that can be a catalog target.
     */
    private final String typeName;


    /**
     * The deployed implementation type allows the connector to be more specific about the resources it works with.
     */
    private final String deployedImplementationType;

    /**
     * A map of property name to property value for values that should match in the catalog target for it to be compatible with this integration
     * connector.
     */
    private final Map<String, String> otherPropertyValues;


    /**
     * Constructor for Enum
     *
     * @param name catalog target name
     * @param typeName open metadata type name for the linked element
     * @param deployedImplementationType deployed implementation type for the linked element
     * @param otherPropertyValues other values
     */
    JDBCDatabaseCatalogTarget(String name, String typeName, String deployedImplementationType, Map<String, String> otherPropertyValues)
    {
        this.name                       = name;
        this.typeName                   = typeName;
        this.deployedImplementationType = deployedImplementationType;
        this.otherPropertyValues        = otherPropertyValues;
    }


    /**
     * Return the catalog target name.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the type name (or super type name) of a permitted catalog target.
     *
     * @return name of an open metadata type
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return a more specific definition of a permitted catalog target.
     *
     * @return deployed implementation type name
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return a map of property name to property value that the catalog target should have to be valid for this integration connector.
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
     * @return list of catalog target type
     */
    public static List<CatalogTargetType> getCatalogTargetTypes()
    {
        List<CatalogTargetType> catalogTargetTypes = new ArrayList<>();

        for (JDBCDatabaseCatalogTarget catalogTarget : JDBCDatabaseCatalogTarget.values())
        {
            catalogTargetTypes.add(catalogTarget.getCatalogTargetType());
        }

        return catalogTargetTypes;
    }


    /**
     * Return the catalog target type for a specific catalog target enum.
     *
     * @return catalog target type
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
