/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.controls;

import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SolutionComponentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

/**
 * DocumentManagementDeployedImplementationType describes the standard deployed implementation types (technology types)
 * supplied with Egeria for documents held in document management systems.  Many of these technologies do not yet have connectors;
 * their catalog templates create just the asset, so that the resources can be catalogued (for example from
 * OpenLineage events) and linked into lineage.
 */
public enum DocumentManagementDeployedImplementationType implements DeployedImplementationTypeDefinition
{
    /**
     * A file stored in Box.  OpenLineage names it box://{host}:{port}/{enterprise ID} + {object key}.
     */
    BOX_FILE("b9e91b07-c863-48b1-836e-23ad2cfab03b",
             "Box File",
             DeployedImplementationType.DOCUMENT,
             OpenMetadataType.DOCUMENT.typeName,
             null,
             "A file stored in Box.  OpenLineage names it box://{host}:{port}/{enterprise ID} + {object key}.",
             "https://www.box.com/",
             null,
             null,
             null),

    /**
     * A document in an IBM FileNet repository.  OpenLineage names it filenet://{host}:{port}/{repository ID} + {object key}.
     */
    IBM_FILENET_DOCUMENT("1db055a0-b4e0-4cab-94c4-4cb8e9b9e377",
                         "IBM FileNet Document",
                         DeployedImplementationType.DOCUMENT,
                         OpenMetadataType.DOCUMENT.typeName,
                         null,
                         "A document in an IBM FileNet repository.  OpenLineage names it filenet://{host}:{port}/{repository ID} + {object key}.",
                         "https://www.ibm.com/products/filenet-content-manager",
                         null,
                         null,
                         null),

    /**
     * A document in a Microsoft SharePoint site.  OpenLineage names it mssharepoint://{host}:{port}/{site} + {object key}.
     */
    MICROSOFT_SHAREPOINT_DOCUMENT("8abe1136-8426-4b0d-bdb2-52b13b1993ce",
                                  "Microsoft SharePoint Document",
                                  DeployedImplementationType.DOCUMENT,
                                  OpenMetadataType.DOCUMENT.typeName,
                                  null,
                                  "A document in a Microsoft SharePoint site.  OpenLineage names it mssharepoint://{host}:{port}/{site} + {object key}.",
                                  "https://www.microsoft.com/microsoft-365/sharepoint",
                                  null,
                                  null,
                                  null)

    ;


    /**
     * Return the matching ENUM for the full definition for the deployed implementation type.
     *
     * @param deployedImplementationType value to match on
     * @return DeployedImplementationType definition
     */
    public static DeployedImplementationTypeDefinition getDefinitionFromDeployedImplementationType(String deployedImplementationType)
    {
        if (deployedImplementationType != null)
        {
            for (DocumentManagementDeployedImplementationType definition : DocumentManagementDeployedImplementationType.values())
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
     * Return a list of definitions for this set of deployed implementation types.
     *
     * @return array of definitions
     */
    public static DeployedImplementationTypeDefinition[] getDefinitions()
    {
        DeployedImplementationTypeDefinition[] definitions = new DeployedImplementationTypeDefinition[values().length];

        for (DocumentManagementDeployedImplementationType definition : DocumentManagementDeployedImplementationType.values())
        {
            definitions[definition.ordinal()] = definition;
        }

        return definitions;
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
    DocumentManagementDeployedImplementationType(String                               guid,
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
        return "DocumentManagementDeployedImplementationType{" + deployedImplementationType + '}';
    }
}
