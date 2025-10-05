/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.enums.LatestChangeAction;
import org.odpi.openmetadata.frameworks.openmetadata.enums.LatestChangeTarget;
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
public class OpenMetadataTypesArchive2_4
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "2.4";
    private static final String                  originatorName     = "ODPi Egeria";
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
    public OpenMetadataTypesArchive2_4()
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
    public OpenMetadataTypesArchive2_4(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive2_0  previousTypes = new OpenMetadataTypesArchive2_0(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();


        /*
         * Calls for new and changed types go here
         */
        update0011ManagingReferenceables();
        update0012SearchKeywords();
        update0030HostsAndPlatforms();
        update0215SoftwareComponents();
        update04xxGovernanceEnums();
        update05xxSchemaAttributes();
        updateClashingControlProperties();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0011 Managing Referenceables - add Anchors Classification and SourcedFrom Relationship and LatestChange
     *                                Classification with associated Enums
     */
    private void update0011ManagingReferenceables()
    {
        this.archiveBuilder.addEnumDef(getLatestChangeTargetEnum());
        this.archiveBuilder.addEnumDef(getLatestChangeActionEnum());
        this.archiveBuilder.addClassificationDef(addAnchorsClassification());

        this.archiveBuilder.addClassificationDef(addLatestChangeClassification());

        this.archiveBuilder.addRelationshipDef(addSourcedFromRelationship());
    }

    private EnumDef getLatestChangeTargetEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(LatestChangeTarget.getOpenTypeGUID(),
                                                        LatestChangeTarget.getOpenTypeName(),
                                                        LatestChangeTarget.getOpenTypeDescription(),
                                                        LatestChangeTarget.getOpenTypeDescriptionGUID(),
                                                        LatestChangeTarget.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (LatestChangeTarget enumValue : LatestChangeTarget.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValue.getOrdinal(),
                                                         enumValue.getName(),
                                                         enumValue.getDescription(),
                                                         enumValue.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValue.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private EnumDef getLatestChangeActionEnum()
    {
        EnumDef enumDef = archiveHelper.getEmptyEnumDef(LatestChangeAction.getOpenTypeGUID(),
                                                        LatestChangeAction.getOpenTypeName(),
                                                        LatestChangeAction.getOpenTypeDescription(),
                                                        LatestChangeAction.getOpenTypeDescriptionGUID(),
                                                        LatestChangeAction.getOpenTypeDescriptionWiki());

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        for (LatestChangeAction enumValue : LatestChangeAction.values())
        {
            elementDef = archiveHelper.getEnumElementDef(enumValue.getOrdinal(),
                                                         enumValue.getName(),
                                                         enumValue.getDescription(),
                                                         enumValue.getDescriptionGUID());

            elementDefs.add(elementDef);

            if (enumValue.isDefault())
            {
                enumDef.setDefaultValue(elementDef);
            }
        }

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private ClassificationDef addLatestChangeClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.LATEST_CHANGE_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();


        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.CHANGE_TARGET));
        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.CHANGE_ACTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CLASSIFICATION_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ATTACHMENT_GUID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ATTACHMENT_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RELATIONSHIP_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.USER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTION_DESCRIPTION));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private ClassificationDef addAnchorsClassification()
    {
        final List<TypeDefLink> linkedToEntities = new ArrayList<>();

        linkedToEntities.add(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));
        linkedToEntities.add(this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION.typeName));
        linkedToEntities.add(this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION_REVIEW.typeName));
        linkedToEntities.add(this.archiveBuilder.getEntityDef(OpenMetadataType.LIKE.typeName));
        linkedToEntities.add(this.archiveBuilder.getEntityDef(OpenMetadataType.RATING.typeName));

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.ANCHORS_CLASSIFICATION,
                                                                                 null,
                                                                                 linkedToEntities,
                                                                                 true);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ANCHOR_GUID));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private RelationshipDef addSourcedFromRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SOURCED_FROM_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "resultingElement";
        final String                     end1AttributeDescription     = "Element created from the template.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "templateElement";
        final String                     end2AttributeDescription     = "Template element providing information.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0012 Search Keywords - add support for search keywords
     */
    private void update0012SearchKeywords()
    {
        this.archiveBuilder.addEntityDef(getSearchKeywordEntity());

        this.archiveBuilder.addRelationshipDef(addSearchKeywordLinkRelationship());
    }

    private EntityDef getSearchKeywordEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SEARCH_KEYWORD,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.OPEN_METADATA_ROOT.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.KEYWORD));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef addSearchKeywordLinkRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "linkedElements";
        final String                     end1AttributeDescription     = "Element described by the search keyword.";
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
        final String                     end2AttributeName            = "searchKeywords";
        final String                     end2AttributeDescription     = "Keywords to describe the element.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SEARCH_KEYWORD.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0030 Hosts and Platforms - correct the type of the endianness property.
     */
    private void update0030HostsAndPlatforms()
    {
        this.archiveBuilder.addTypeDefPatch(correctOperatingPlatformEntity());
    }


    private TypeDefPatch correctOperatingPlatformEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.OPERATING_PLATFORM.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getEnumTypeDefAttribute(OpenMetadataProperty.BYTE_ORDERING));

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0215 Feedback - add DeployedConnector.
     */
    private void update0215SoftwareComponents()
    {
        this.archiveBuilder.addEntityDef(addDeployedConnectorEntity());
    }

    private EntityDef addDeployedConnectorEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DEPLOYED_CONNECTOR,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 05xx - Replace enum properties with numbers and define values using entities so that an organization
     * can choose the values they use.  The enums are retained to provide utilities to define these values from
     * and enum type - and these definitions provide a useful default set.
     */
    private void update04xxGovernanceEnums()
    {
        archiveBuilder.addRelationshipDef(getGovernedByRelationship());

        archiveBuilder.addTypeDefPatch(updateCriticalityClassification());
        archiveBuilder.addTypeDefPatch(updateRetentionClassification());
        archiveBuilder.addTypeDefPatch(updateConfidenceClassification());
        archiveBuilder.addTypeDefPatch(updateConfidentialityClassification());
    }

    private RelationshipDef getGovernedByRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.GOVERNED_BY_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 2.
         */
        final String                     end1AttributeName            = "governedBy";
        final String                     end1AttributeDescription     = "The governance definition that defines how this element is governed.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_DEFINITION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);


        /*
         * Set up end 1.
         */
        final String                     end2AttributeName            = "governedElements";
        final String                     end2AttributeDescription     = "An element that is governed according to the governance definition.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private TypeDefPatch updateCriticalityClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CRITICALITY_LEVEL_IDENTIFIER));

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch updateRetentionClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.RETENTION_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RETENTION_BASIS_IDENTIFIER));

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch updateConfidenceClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONFIDENCE_LEVEL_IDENTIFIER));

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch updateConfidentialityClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONFIDENTIALITY_LEVEL_IDENTIFIER));

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 05xx - Add CalculatedValue.  Also enable
     * all schema types to be in a TypeEmbeddedAttribute classification.
     */
    private void update05xxSchemaAttributes()
    {
        this.archiveBuilder.addTypeDefPatch(addPrecisionToSchemaAttributeEntity());
        this.archiveBuilder.addClassificationDef(getCalculatedValueClassification());
        this.archiveBuilder.addEntityDef(getExternalSchemaTypeEntity());
        this.archiveBuilder.addTypeDefPatch(updateMapFromElementTypeRelationship());
        this.archiveBuilder.addTypeDefPatch(updateMapToElementTypeRelationship());
        this.archiveBuilder.addTypeDefPatch(updateSchemaTypeOptionRelationship());
        this.archiveBuilder.addRelationshipDef(getDerivedSchemaTypeQueryTargetRelationship());
        this.archiveBuilder.addRelationshipDef(getLinkedExternalSchemaTypeRelationship());
    }


    private TypeDefPatch addPrecisionToSchemaAttributeEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SCHEMA_ATTRIBUTE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PRECISION));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private ClassificationDef getCalculatedValueClassification()
    {
        ClassificationDef classificationDef =  archiveHelper.getClassificationDef(OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION,
                                                                                  null,
                                                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                                  false);
        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FORMULA));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private EntityDef getExternalSchemaTypeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.EXTERNAL_SCHEMA_TYPE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName));
    }

    private RelationshipDef getLinkedExternalSchemaTypeRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "usedInSchema";
        final String                     end1AttributeDescription     = "Connection point for a reusable schema type.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ELEMENT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "externalSchemaType";
        final String                     end2AttributeDescription     = "The schema type that is being reused in another schema.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getDerivedSchemaTypeQueryTargetRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "usedBy";
        final String                     end1AttributeDescription     =
                "Use of another schema type to derive all or part of this schema type's value.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ELEMENT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "queryTarget";
        final String                     end2AttributeDescription     = "Used to provide data values to the other schema type.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ELEMENT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.QUERY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.QUERY_ID));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.QUERY_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ISC_QUALIFIED_NAME));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private TypeDefPatch updateSchemaTypeOptionRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "schemaOptionalUses";
        final String                     end1AttributeDescription     = "Schema where this schema type is reused.";
        final String                     end1AttributeDescriptionGUID = null;

        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ELEMENT.typeName),
                                                                                    end1AttributeName,
                                                                                    end1AttributeDescription,
                                                                                    end1AttributeDescriptionGUID,
                                                                                    RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }


    private TypeDefPatch updateMapFromElementTypeRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.MAP_FROM_ELEMENT_TYPE_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "parentMapFrom";
        final String                     end1AttributeDescription     = "Used in map to describe the domain (value mapped from).";
        final String                     end1AttributeDescriptionGUID = null;

        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ELEMENT.typeName),
                                                                                    end1AttributeName,
                                                                                    end1AttributeDescription,
                                                                                    end1AttributeDescriptionGUID,
                                                                                    RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }


    private TypeDefPatch updateMapToElementTypeRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.MAP_TO_ELEMENT_TYPE_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "parentMapTo";
        final String                     end1AttributeDescription     = "Used in map to describe the range (value mapped to).";
        final String                     end1AttributeDescriptionGUID = null;

        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ELEMENT.typeName),
                                                                                    end1AttributeName,
                                                                                    end1AttributeDescription,
                                                                                    end1AttributeDescriptionGUID,
                                                                                    RelationshipEndCardinality.ANY_NUMBER);
        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * A number of types have attributes whose names clash with header (control) attributes. It is not possible to
     * patch an attribute to change its name for compatibility reasons. These patches deprecate the old (clashing)
     * property names, and introduce new (non-clashing) properties to replace them.
     */
    private void updateClashingControlProperties()
    {
        // Comment entity's clashing properties are updated as part of other updates to Comment above
        this.archiveBuilder.addTypeDefPatch(updatePolicyAdministrationPointClassification());
        this.archiveBuilder.addTypeDefPatch(updatePolicyDecisionPointClassification());
        this.archiveBuilder.addTypeDefPatch(updatePolicyEnforcementPointClassification());
        this.archiveBuilder.addTypeDefPatch(updatePolicyInformationPointClassification());
        this.archiveBuilder.addTypeDefPatch(updatePolicyRetrievalPointClassification());
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updatePolicyAdministrationPointClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.POLICY_ADMINISTRATION_POINT_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.POINT_TYPE));

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updatePolicyDecisionPointClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.POLICY_DECISION_POINT_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.POINT_TYPE));

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updatePolicyEnforcementPointClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.POLICY_ENFORCEMENT_POINT_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.POINT_TYPE));

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updatePolicyInformationPointClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.POLICY_INFORMATION_POINT_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.POINT_TYPE));

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updatePolicyRetrievalPointClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.POLICY_RETRIEVAL_POINT_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.POINT_TYPE));

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

}

