/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.definitions;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * Describes the standard deployed implementation types supplied with Egeria for working with Apache Atlas.
 */
public enum EgeriaDeployedImplementationType implements DeployedImplementationTypeDefinition
{

    /**
     * An Open Metadata and Governance (OMAG) platform for running one to many OMAG Servers.
     */
    OMAG_SERVER_PLATFORM("e76e450d-9533-4e98-b83d-c3ea7f8b0a08",
                         "OMAG Server Platform",
                         DeployedImplementationType.SOFTWARE_SERVER,
                         OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName,
                         null,
                         "An Open Metadata and Governance (OMAG) runtime for running one to many OMAG Servers.",
                         "https://egeria-project.org/concepts/omag-server-platform/"),


    /**
     * An Open Metadata and Governance (OMAG) Server.
     */
    OMAG_SERVER("91abf546-f8f3-4d51-aa37-8839582a3ddc",
                "OMAG Server",
                DeployedImplementationType.SOFTWARE_SERVER,
                OpenMetadataType.SOFTWARE_SERVER.typeName,
                null,
                "A server providing Open Metadata and Governance (OMAG) services.",
                "https://egeria-project.org/concepts/omag-server/"),

    /**
     * A server that runs governance engines.
     */
    ENGINE_HOST("5cd74a31-9a1f-40c6-8ce2-f0d803b76db8",
                "Engine Host",
                EgeriaDeployedImplementationType.OMAG_SERVER,
                OpenMetadataType.SOFTWARE_SERVER.typeName,
                null,
                "A server that runs governance engines.",
                "https://egeria-project.org/concepts/engine-host/"),

    /**
     * A server that runs integration connectors that synchronize metadata between different types of technologies.
     */
    INTEGRATION_DAEMON("b2d8d57f-df68-4280-9a41-3df830c3444f",
                       "Integration Daemon",
                       EgeriaDeployedImplementationType.OMAG_SERVER,
                       OpenMetadataType.SOFTWARE_SERVER.typeName,
                       null,
                       "A server that runs integration connectors that synchronize metadata between different types of technologies.",
                       "https://egeria-project.org/concepts/integration-daemon/"),

    /**
     * A server that provides access to one or more open metadata repositories.
     */
    METADATA_ACCESS_SERVER("940dda2a-e86d-4ba2-b889-500431d47f70",
                           "Metadata Access Server",
                           EgeriaDeployedImplementationType.OMAG_SERVER,
                           OpenMetadataType.SOFTWARE_SERVER.typeName,
                           null,
                           "A server that provides access to one or more open metadata repositories.",
                           "https://egeria-project.org/concepts/metadata-access-server/"),

    /**
     * A server that provides access to end user open metadata and governance services.
     */
    VIEW_SERVER("aa29cd35-c14a-4998-ab53-acc09a363d4b",
                "View Server",
                EgeriaDeployedImplementationType.OMAG_SERVER,
                OpenMetadataType.SOFTWARE_SERVER.typeName,
                null,
                "A server that provides access to end user open metadata and governance services.",
                "https://egeria-project.org/concepts/metadata-access-server/"),

    ;


    /**
     * Return the matching ENUM to make use of the full definition for the deployed implementation type.
     *
     * @param deployedImplementationType value to match on
     * @return DeployedImplementationType definition
     */
    public static DeployedImplementationTypeDefinition getDefinitionFromDeployedImplementationType(String deployedImplementationType)
    {
        if (deployedImplementationType != null)
        {
            for (EgeriaDeployedImplementationType definition : EgeriaDeployedImplementationType.values())
            {
                if (definition.getDeployedImplementationType().equals(deployedImplementationType))
                {
                    return definition;
                }
            }
        }

        return null;
    }


    private final String                               guid;
    private final String                               deployedImplementationType;
    private final DeployedImplementationTypeDefinition isATypeOf;
    private final String                               associatedTypeName;
    private final String                               associatedClassification;
    private final String                               description;
    private final String                               wikiLink;


    /**
     * Constructor for individual enum value.
     *
     * @param guid unique identifier of technology type (deployedImplementationType)
     * @param deployedImplementationType value for deployedImplementationType
     * @param isATypeOf optional deployed implementation type that this type "inherits" from
     * @param associatedTypeName the open metadata type where this value is used
     * @param associatedClassification the open metadata classification where this value is used
     * @param description description of the type
     * @param wikiLink url link to more information (optional)
     */
    EgeriaDeployedImplementationType(String                               guid,
                                     String                               deployedImplementationType,
                                     DeployedImplementationTypeDefinition isATypeOf,
                                     String                               associatedTypeName,
                                     String                               associatedClassification,
                                     String                               description,
                                     String                               wikiLink)
    {
        this.guid = guid;
        this.deployedImplementationType = deployedImplementationType;
        this.isATypeOf = isATypeOf;
        this.associatedTypeName = associatedTypeName;
        this.associatedClassification = associatedClassification;
        this.description = description;
        this.wikiLink = wikiLink;
    }


    /**
     * Return the guid for the deployed technology type - can be null.
     *
     * @return string
     */
    @Override
    public String getGUID()
    {
        return guid;
    }


    /**
     * Return preferred value for deployed implementation type.
     * 
     * @return string
     */
    @Override
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return the optional deployed implementation type that this technology is a tye of.
     *
     * @return deployed implementation type enum
     */
    @Override
    public DeployedImplementationTypeDefinition getIsATypeOf()
    {
        return isATypeOf;
    }


    /**
     * Return the type name that this deployed implementation type is associated with.
     * 
     * @return string
     */
    @Override
    public String getAssociatedTypeName()
    {
        return associatedTypeName;
    }


    /**
     * Return the optional classification name that this deployed implementation type is associated with.
     *
     * @return string
     */
    @Override
    public String getAssociatedClassification()
    {
        return associatedClassification;
    }


    /**
     * Return the qualified name for this deployed implementation type.
     *
     * @return string
     */
    @Override
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(associatedTypeName,
                                                OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                null,
                                                deployedImplementationType);
    }


    /**
     * Return the description for this value.
     * 
     * @return string
     */
    @Override
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the URL to more information.
     *
     * @return string url
     */
    @Override
    public String getWikiLink()
    {
        return wikiLink;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "DeployedImplementationType{" + deployedImplementationType + '}';
    }
}
