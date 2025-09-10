/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.unitycatalog.controls;

import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TargetEnum characterises the type of third party technology supported by a collection of connectors.
 * This enables the capability of these connectors to be correctly matched to the resources and elements
 * that they work with.
 */
public enum UnityCatalogTarget
{
    UNITY_CATALOG_SERVER_TARGET("unityCatalogServer",
                                UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getDescription(),
                                UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getAssociatedTypeName(),
                                UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getDeployedImplementationType(),
                                null),

    UNITY_CATALOG_CATALOG_TARGET("unityCatalogCatalog",
                                 UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDescription(),
                                 UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getAssociatedTypeName(),
                                 UnityCatalogDeployedImplementationType.OSS_UC_CATALOG.getDeployedImplementationType(),
                                 null),

    UNITY_CATALOG_CONNECTOR_TARGET("unityCatalogConnector",
                                   DeployedImplementationType.INTEGRATION_CONNECTOR.getDescription(),
                                   DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                                   DeployedImplementationType.INTEGRATION_CONNECTOR.getDeployedImplementationType(),
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
    UnityCatalogTarget(String              name, 
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
     * Return all the catalog types defined for the server.
     *
     * @return list of target type
     */
    public static List<CatalogTargetType> getServerCatalogTargetTypes()
    {
        List<CatalogTargetType> catalogTargetTypes = new ArrayList<>();

        catalogTargetTypes.add(UNITY_CATALOG_SERVER_TARGET.getCatalogTargetType());
        catalogTargetTypes.add(UNITY_CATALOG_CONNECTOR_TARGET.getCatalogTargetType());

        return catalogTargetTypes;
    }


    /**
     * Return all the catalog types defined for the server.
     *
     * @return list of target type
     */
    public static List<CatalogTargetType> getUCCatalogTargetTypes()
    {
        List<CatalogTargetType> catalogTargetTypes = new ArrayList<>();

        catalogTargetTypes.add(UNITY_CATALOG_CATALOG_TARGET.getCatalogTargetType());

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
     * Return the action targets defined in this enum for a UC Catalog.
     * It is currently matched to the server asset.  Later enhancements may allow it to also
     * be connected to the desired data asset.
     *
     * @return list
     */
    public static List<ActionTargetType> getSchemaActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        actionTargetTypes.add(UNITY_CATALOG_SERVER_TARGET.getActionTargetType());

        return actionTargetTypes;
    }


    /**
     * Return the action targets defined in this enum for a UC Catalog.
     * It is currently matched to the server asset.  Later enhancements may allow it to also
     * be connected to the desired data asset.
     *
     * @return list
     */
    public static List<ActionTargetType> getCatalogActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        actionTargetTypes.add(UNITY_CATALOG_SERVER_TARGET.getActionTargetType());

        return actionTargetTypes;
    }


    /**
     * Return the action targets defined in this enum for a UC Server.
     *
     * @return list
     */
    public static List<ActionTargetType> getServerActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        actionTargetTypes.add(UNITY_CATALOG_SERVER_TARGET.getActionTargetType());

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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TargetEnum{catalogTargetName='" + name + "'}";
    }
}
