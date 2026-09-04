/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.baudot.controls;

import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;


/**
 * BaudotCatalogTarget describes the kinds of element that can be a catalog target of the Baudot Subscription
 * Manager.  There is one: a notification type.  Each notification type handed to Baudot as a catalog target
 * has its subscribers notified - on Baudot's refresh cycle for the welcome, one-time and periodic
 * notifications, and as the events arrive for changes to the notification type's monitored resources.
 */
public enum BaudotCatalogTarget
{
    /**
     * A notification type whose subscribers the Baudot Subscription Manager notifies.
     */
    NOTIFICATION_TYPE("notificationType",
                      "A notification type whose subscribers the Baudot Subscription Manager notifies.  The notification type's monitored resources are watched for changes, and its subscribers receive welcome, one-time or periodic notifications according to the notification type's properties.",
                      OpenMetadataType.NOTIFICATION_TYPE.typeName,
                      null),

    ;

    /**
     * Catalog target name.
     */
    private final String name;

    /**
     * Description of the catalog target.
     */
    private final String description;

    /**
     * The open metadata type name of the element that can be a catalog target.
     */
    private final String typeName;

    /**
     * The deployed implementation type allows the connector to be more specific about the resources it works with.
     */
    private final String deployedImplementationType;


    /**
     * Constructor for the enum.
     *
     * @param name catalogTargetName
     * @param description description of the target
     * @param typeName open metadata type name for the linked element
     * @param deployedImplementationType deployed implementation type for the linked element - null for any
     */
    BaudotCatalogTarget(String name,
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
     * Return the catalog target name.
     *
     * @return string
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the catalog target.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the type name of the element that is linked to for this type of catalog target.
     *
     * @return type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return the deployed implementation type for the catalog target - null if any type.
     *
     * @return string
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return the catalog target types that the Baudot Subscription Manager supports, for its connector
     * provider's specification.
     *
     * @return list of catalog target types
     */
    public static List<CatalogTargetType> getCatalogTargetTypes()
    {
        List<CatalogTargetType> catalogTargetTypes = new ArrayList<>();

        for (BaudotCatalogTarget catalogTarget : BaudotCatalogTarget.values())
        {
            catalogTargetTypes.add(catalogTarget.getCatalogTargetType());
        }

        return catalogTargetTypes;
    }


    /**
     * Return the catalog target type for a specific enum.
     *
     * @return catalog target type
     */
    public CatalogTargetType getCatalogTargetType()
    {
        CatalogTargetType catalogTargetType = new CatalogTargetType();

        catalogTargetType.setName(name);
        catalogTargetType.setTypeName(typeName);
        catalogTargetType.setDeployedImplementationType(deployedImplementationType);

        return catalogTargetType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "BaudotCatalogTarget{name='" + name + "'}";
    }
}
