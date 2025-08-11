/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OpenMetadataTypesArchive builds an open metadata archive containing all the standard open metadata types.
 * These types have hardcoded dates and guids so that however many times this archive is rebuilt, it will
 * produce the same content.
 * <p>
 * Details of the open metadata types are documented on the wiki:
 * <a href="https://egeria-project.org/types/">The Open Metadata Type System</a>
 * </p>
 * <p>
 * There are 8 areas, each covering a different topic area of metadata.  The module breaks down the process of creating
 * the models into the areas and then the individual models to simplify the maintenance of this class
 * </p>
 */
public class OpenMetadataTypesArchive
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "5.4-SNAPSHOT";
    private static final String                  originatorName     = "Egeria";
    private static final String                  originatorLicense  = "Apache-2.0";
    private static final Date                    creationDate       = new Date(1588261366992L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";


    private final OMRSArchiveBuilder archiveBuilder;
    private final OMRSArchiveHelper  archiveHelper;

    /**
     * Default constructor sets up the archive builder.  This in turn sets up the header for the archive.
     */
    public OpenMetadataTypesArchive()
    {
        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     archiveVersion,
                                                     originatorName,
                                                     originatorLicense,
                                                     creationDate,
                                                     null);

        this.archiveHelper = new OMRSArchiveHelper(archiveBuilder,
                                                   archiveGUID,
                                                   originatorName,
                                                   creationDate,
                                                   versionNumber,
                                                   versionName);
    }


    /**
     * Chained constructor sets up the archive builder.  This in turn sets up the header for the archive.
     *
     * @param archiveBuilder accumulator for types
     */
    public OpenMetadataTypesArchive(OMRSArchiveBuilder archiveBuilder)
    {
        this.archiveBuilder = archiveBuilder;

        this.archiveHelper = new OMRSArchiveHelper(archiveBuilder,
                                                   archiveGUID,
                                                   originatorName,
                                                   creationDate,
                                                   versionNumber,
                                                   versionName);
    }


    /**
     * Return the unique identifier for this archive.
     *
     * @return String guid
     */
    public String getArchiveGUID()
    {
        return archiveGUID;
    }


    /**
     * Returns the open metadata type archive containing all the standard open metadata types.
     *
     * @return populated open metadata archive object
     */
    public OpenMetadataArchive getOpenMetadataArchive()
    {
        final String methodName = "getOpenMetadataArchive";

        if (this.archiveBuilder != null)
        {
            /*
             * Build the type archive.
             */
            this.getOriginalTypes();

            /*
             * The completed archive is ready to be packaged up and returned
             */
            return this.archiveBuilder.getOpenMetadataArchive();
        }
        else
        {
            /*
             * This is a logic error since it means the creation of the archive builder threw an exception
             * in the constructor and so this object should not be used.
             */
            throw new OMRSLogicErrorException(OMRSErrorCode.ARCHIVE_UNAVAILABLE.getMessageDefinition(),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Add the types from this archive to the archive builder supplied in the
     * constructor.
     */
    public void getOriginalTypes()
    {
        OpenMetadataTypesArchive5_3 previousTypes = new OpenMetadataTypesArchive5_3(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * New types for this release
         */
        update00XXExternalReferences();
        update0021Collections();
        update04xxGovernanceDefinitions();
        update0484Agreements();
        update07xxImplementationRelationships();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

     private void update00XXExternalReferences()
     {
         this.archiveBuilder.addEntityDef(getExternalDataSourceEntity());
         this.archiveBuilder.addEntityDef(getExternalModelSourceEntity());
         this.archiveBuilder.addEntityDef(getCitedDocumentEntity());

         this.archiveBuilder.addRelationshipDef(getCitedDocumentLinkRelationship());
         this.archiveBuilder.addRelationshipDef(getContractLinkRelationship());
     }



    private EntityDef getExternalDataSourceEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.EXTERNAL_DATA_SOURCE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.EXTERNAL_REFERENCE.typeName));
    }


    private EntityDef getExternalModelSourceEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.EXTERNAL_MODEL_SOURCE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.EXTERNAL_REFERENCE.typeName));
    }


    private EntityDef getCitedDocumentEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.CITED_DOCUMENT,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.EXTERNAL_REFERENCE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NUMBER_OF_PAGES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PAGE_RANGE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PUBLICATION_SERIES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PUBLICATION_SERIES_VOLUME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PUBLISHER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EDITION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FIRST_PUB_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PUBLICATION_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PUBLICATION_CITY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PUBLICATION_YEAR));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PUBLICATION_NUMBERS));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private RelationshipDef getContractLinkRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CONTRACT_LINK_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "agreements";
        final String                     end1AttributeDescription     = "Agreements related to the contract.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.AGREEMENT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "contracts";
        final String                     end2AttributeDescription     = "Details of the contract documents.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CITED_DOCUMENT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONTRACT_ID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONTRACT_LIAISON));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONTRACT_LIAISON_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONTRACT_LIAISON_PROPERTY_NAME));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getCitedDocumentLinkRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CITED_DOCUMENT_LINK_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        relationshipDef.setMultiLink(true);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "citingItems";
        final String                     end1AttributeDescription     = "Items that are referencing this work.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "citedDocuments";
        final String                     end2AttributeDescription     = "Link to the documents located at an external source.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CITED_DOCUMENT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REFERENCE_ID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PAGES));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * A new classification for Collection
     */
    private void update0021Collections()
    {
        this.archiveBuilder.addClassificationDef(getNamespaceClassification());
        this.archiveBuilder.addClassificationDef(getReferenceListClassification());
    }

    private ClassificationDef getNamespaceClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.NAMESPACE_COLLECTION_CLASSIFICATION,
                                                  this.archiveBuilder.getClassificationDef(OpenMetadataType.COLLECTION_CATEGORY_CLASSIFICATION.typeName),
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName),
                                                  false);

    }


    private ClassificationDef getReferenceListClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.REFERENCE_LIST_CLASSIFICATION,
                                                  this.archiveBuilder.getClassificationDef(OpenMetadataType.COLLECTION_CATEGORY_CLASSIFICATION.typeName),
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * A new classification for Agreement
     */
    private void update0484Agreements()
    {
        this.archiveBuilder.addClassificationDef(getDataSharingAgreementClassification());
    }

    private ClassificationDef getDataSharingAgreementClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.DATA_SHARING_AGREEMENT_CLASSIFICATION,
                                                  this.archiveBuilder.getClassificationDef(OpenMetadataType.COLLECTION_CATEGORY_CLASSIFICATION.typeName),
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.AGREEMENT.typeName),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * A variety of changes to improve consistency and flexibility of the governance types
     */
    private void update04xxGovernanceDefinitions()
    {
        this.archiveBuilder.addEntityDef(getMethodologyEntity());
        this.archiveBuilder.addClassificationDef(getDigitalResourceOriginClassification());
        this.archiveBuilder.addClassificationDef(getZoneMembershipClassification());
    }



    private EntityDef getMethodologyEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.METHODOLOGY,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_PROCEDURE.typeName));
    }


    private ClassificationDef getDigitalResourceOriginClassification()
    {
        ClassificationDef classificationDef =  archiveHelper.getClassificationDef(OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION,
                                                                                  null,
                                                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                  false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ORGANIZATION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ORGANIZATION_PROPERTY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.BUSINESS_CAPABILITY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.BUSINESS_CAPABILITY_PROPERTY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.OTHER_ORIGIN_VALUES));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private ClassificationDef getZoneMembershipClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.OPEN_METADATA_ROOT.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ZONE_MEMBERSHIP));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */



    private void update07xxImplementationRelationships()
    {
        this.archiveBuilder.addRelationshipDef(getImplementationResourceRelationship());

    }


    private RelationshipDef getImplementationResourceRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "usedInImplementationOf";
        final String                     end1AttributeDescription     = "Place where the linked resources could be used as part of an implementation.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "implementationResources";
        final String                     end2AttributeDescription     = "Useful components in creating an implementation.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

}

