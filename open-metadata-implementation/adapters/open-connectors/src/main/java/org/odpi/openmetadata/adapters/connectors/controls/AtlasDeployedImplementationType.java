/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.controls;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SolutionComponentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * Describes the standard deployed implementation types supplied with Egeria for working with Apache Atlas.
 */
public enum AtlasDeployedImplementationType implements DeployedImplementationTypeDefinition
{

    /**
     * A data catalog for the Hadoop ecosystem.
     */
    APACHE_ATLAS_SERVER("234a0b86-fb96-409a-92df-e550a4c089d5",
                        "Apache Atlas Server",
                        DeployedImplementationType.METADATA_SERVER,
                        OpenMetadataType.SOFTWARE_SERVER.typeName,
                        null,
                        "A data catalog for the Hadoop ecosystem.",
                        "https://atlas.apache.org/",
                        "2bdfd011-bf61-4382-a134-47e9f6af1f82",
                        SolutionComponentType.SOFTWARE_SERVICE.getSolutionComponentType(),
                        "APACHE-ATLAS-SERVER"),

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
            for (AtlasDeployedImplementationType definition : AtlasDeployedImplementationType.values())
            {
                if (definition.getDeployedImplementationType().equals(deployedImplementationType))
                {
                    return definition;
                }
            }
        }

        return null;
    }


    /**
     * Return a list of GUIDs for this set of deployed implementation types.
     *
     * @return array of guids
     */
    public static String[] getGUIDs()
    {
        String[] guids = new String[values().length];

        for (AtlasDeployedImplementationType definition : AtlasDeployedImplementationType.values())
        {
            guids[definition.ordinal()] = definition.getGUID();
        }

        return guids;
    }


    private final String                               guid;
    private final String                               deployedImplementationType;
    private final DeployedImplementationTypeDefinition isATypeOf;
    private final String                               associatedTypeName;
    private final String                               associatedClassification;
    private final String                               description;
    private final String                               wikiLink;
    private final String                               solutionComponentGUID;
    private final String                               solutionComponentType;
    private final String                               solutionComponentIdentifier;


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
     * @param solutionComponentGUID unique identifier of the solution component that this deployed implementation type is associated with (optional)
     * @param solutionComponentType type of the solution component that this deployed implementation type is associated with (optional)
     * @param solutionComponentIdentifier  identifier of the solution component that this deployed implementation type is associated with (optional)
     */
    AtlasDeployedImplementationType(String                               guid,
                                    String                               deployedImplementationType,
                                    DeployedImplementationTypeDefinition isATypeOf,
                                    String                               associatedTypeName,
                                    String                               associatedClassification,
                                    String                               description,
                                    String                               wikiLink,
                                    String                               solutionComponentGUID,
                                    String                               solutionComponentType,
                                    String                               solutionComponentIdentifier)
    {
        this.guid = guid;
        this.deployedImplementationType = deployedImplementationType;
        this.isATypeOf = isATypeOf;
        this.associatedTypeName = associatedTypeName;
        this.associatedClassification = associatedClassification;
        this.description = description;
        this.wikiLink = wikiLink;
        this.solutionComponentGUID = solutionComponentGUID;
        this.solutionComponentType = solutionComponentType;
        this.solutionComponentIdentifier = solutionComponentIdentifier;
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
     * Return the optional unique identifier of the solution component that this deployed implementation type is associated with.
     *
     * @return string
     */
    @Override
    public String getSolutionComponentGUID()
    {
        return solutionComponentGUID;
    }


    /**
     * Return the solution component type that this deployed implementation type is associated with.
     *
     * @return string
     */
    @Override
    public String getSolutionComponentType()
    {
        return solutionComponentType;
    }


    /**
     * Return the solution component identifier that this deployed implementation type is associated with.
     *
     * @return string
     */
    @Override
    public String getSolutionComponentIdentifier()
    {
        return solutionComponentIdentifier;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "AtlasDeployedImplementationType{" + deployedImplementationType + '}';
    }
}
