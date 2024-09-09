/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.enums.ByteOrdering;
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
        add0057IntegrationCapabilities();
        update0150Feedback();
        update0215SoftwareComponents();
        update04xxGovernanceEnums();
        update05xxSchemaAttributes();
        update0545ReferenceData();
        update06xxDiscovery();
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
        final String guid            = "a0b7d7a0-4af5-4539-9b81-cbef52d8cc5d";
        final String name            = "LatestChangeTarget";
        final String description     = "Defines the type of repository element that has changed.";
        final String descriptionGUID = null;

        EnumDef enumDef = archiveHelper.getEmptyEnumDef(guid, name, description, descriptionGUID);

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        final int    element0Ordinal         = 0;
        final String element0Value           = "EntityStatus";
        final String element0Description     = "The status of the anchor entity has changed.";
        final String element0DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element0Ordinal,
                                                     element0Value,
                                                     element0Description,
                                                     element0DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element1Ordinal         = 1;
        final String element1Value           = "EntityProperty";
        final String element1Description     = "A property in the anchor entity has changed.";
        final String element1DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element1Ordinal,
                                                     element1Value,
                                                     element1Description,
                                                     element1DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element2Ordinal         = 2;
        final String element2Value           = "EntityClassification";
        final String element2Description     = "A classification attached to the anchor entity has changed.";
        final String element2DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element2Ordinal,
                                                     element2Value,
                                                     element2Description,
                                                     element2DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element3Ordinal         = 3;
        final String element3Value           = "EntityRelationship";
        final String element3Description     = "A relationship linking the anchor entity to an attachment has changed.";
        final String element3DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element3Ordinal,
                                                     element3Value,
                                                     element3Description,
                                                     element3DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element4Ordinal         = 4;
        final String element4Value           = "Attachment";
        final String element4Description     = "An entity attached either directly or indirectly to the anchor entity has changed.";
        final String element4DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element4Ordinal,
                                                     element4Value,
                                                     element4Description,
                                                     element4DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element5Ordinal         = 5;
        final String element5Value           = "AttachmentStatus";
        final String element5Description     = "The status of an entity attached either directly or indirectly to the anchor entity has changed.";
        final String element5DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element5Ordinal,
                                                     element5Value,
                                                     element5Description,
                                                     element5DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element6Ordinal         = 6;
        final String element6Value           = "AttachmentProperty";
        final String element6Description     = "A property in an entity attached either directly or indirectly to the anchor entity has changed.";
        final String element6DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element6Ordinal,
                                                     element6Value,
                                                     element6Description,
                                                     element6DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element7Ordinal         = 7;
        final String element7Value           = "AttachmentClassification";
        final String element7Description     = "A classification attached to an entity that is, in turn, attached either directly or indirectly to " +
                "the anchor entity has changed.";
        final String element7DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element7Ordinal,
                                                     element7Value,
                                                     element7Description,
                                                     element7DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element8Ordinal         = 8;
        final String element8Value           = "AttachmentRelationship";
        final String element8Description     = "A relationship linking to an entity that is, in turn, attached either directly or indirectly to " +
                "the anchor entity has changed.";
        final String element8DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element8Ordinal,
                                                     element8Value,
                                                     element8Description,
                                                     element8DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element99Ordinal         = 99;
        final String element99Value           = "Other";
        final String element99Description     = "Another type of change.";
        final String element99DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element99Ordinal,
                                                     element99Value,
                                                     element99Description,
                                                     element99DescriptionGUID);
        elementDefs.add(elementDef);

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private EnumDef getLatestChangeActionEnum()
    {
        final String guid            = "032d844b-868f-4c4a-bc5d-81f0f9704c4d";
        final String name            = "LatestChangeAction";
        final String description     = "Defines the type of change that was made to a repository instance.";
        final String descriptionGUID = null;

        EnumDef enumDef = archiveHelper.getEmptyEnumDef(guid, name, description, descriptionGUID);

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        final int    element1Ordinal         = 0;
        final String element1Value           = "Created";
        final String element1Description     = "The target element has been created.";
        final String element1DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element1Ordinal,
                                                     element1Value,
                                                     element1Description,
                                                     element1DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element2Ordinal         = 1;
        final String element2Value           = "Updated";
        final String element2Description     = "The properties of the target element have been changed.";
        final String element2DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element2Ordinal,
                                                     element2Value,
                                                     element2Description,
                                                     element2DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element3Ordinal         = 2;
        final String element3Value           = "Deleted";
        final String element3Description     = "The target element has been deleted.";
        final String element3DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element3Ordinal,
                                                     element3Value,
                                                     element3Description,
                                                     element3DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element6Ordinal         = 99;
        final String element6Value           = "Other";
        final String element6Description     = "Another type of action.";
        final String element6DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element6Ordinal,
                                                     element6Value,
                                                     element6Description,
                                                     element6DescriptionGUID);
        elementDefs.add(elementDef);

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private ClassificationDef addLatestChangeClassification()
    {
        final String guid            = OpenMetadataType.LATEST_CHANGE_CLASSIFICATION.typeGUID;
        final String name            = OpenMetadataType.LATEST_CHANGE_CLASSIFICATION.typeName;
        final String description     = OpenMetadataType.LATEST_CHANGE_CLASSIFICATION.description;
        final String descriptionGUID = OpenMetadataType.LATEST_CHANGE_CLASSIFICATION.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.LATEST_CHANGE_CLASSIFICATION.wikiURL;

        final String linkedToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 descriptionWiki,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.CHANGE_TARGET.name;
        final String attribute1Description     = OpenMetadataProperty.CHANGE_TARGET.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.CHANGE_TARGET.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.CHANGE_ACTION.name;
        final String attribute2Description     = OpenMetadataProperty.CHANGE_ACTION.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.CHANGE_ACTION.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.CLASSIFICATION_NAME.name;
        final String attribute3Description     = OpenMetadataProperty.CLASSIFICATION_NAME.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.CLASSIFICATION_NAME.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.ATTACHMENT_GUID.name;
        final String attribute4Description     = OpenMetadataProperty.ATTACHMENT_GUID.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.ATTACHMENT_GUID.descriptionGUID;
        final String attribute5Name            = OpenMetadataProperty.ATTACHMENT_TYPE.name;
        final String attribute5Description     = OpenMetadataProperty.ATTACHMENT_TYPE.description;
        final String attribute5DescriptionGUID = OpenMetadataProperty.ATTACHMENT_TYPE.descriptionGUID;
        final String attribute6Name            = OpenMetadataProperty.RELATIONSHIP_TYPE.name;
        final String attribute6Description     = OpenMetadataProperty.RELATIONSHIP_TYPE.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.RELATIONSHIP_TYPE.descriptionGUID;
        final String attribute7Name            = OpenMetadataProperty.USER.name;
        final String attribute7Description     = OpenMetadataProperty.USER.description;
        final String attribute7DescriptionGUID = OpenMetadataProperty.USER.descriptionGUID;
        final String attribute8Name            = OpenMetadataProperty.ACTION_DESCRIPTION.name;
        final String attribute8Description     = OpenMetadataProperty.ACTION_DESCRIPTION.description;
        final String attribute8DescriptionGUID = OpenMetadataProperty.ACTION_DESCRIPTION.descriptionGUID;

        property = archiveHelper.getEnumTypeDefAttribute("LatestChangeTarget",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("LatestChangeAction",
                                                         attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute5Name,
                                                           attribute5Description,
                                                           attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute8Name,
                                                           attribute8Description,
                                                           attribute8DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private ClassificationDef addAnchorsClassification()
    {
        final String guid            = OpenMetadataType.ANCHORS_CLASSIFICATION.typeGUID;
        final String name            = OpenMetadataType.ANCHORS_CLASSIFICATION.typeName;
        final String description     = OpenMetadataType.ANCHORS_CLASSIFICATION.description;
        final String descriptionGUID = OpenMetadataType.ANCHORS_CLASSIFICATION.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.ANCHORS_CLASSIFICATION.wikiURL;

        final List<TypeDefLink> linkedToEntities = new ArrayList<>();

        linkedToEntities.add(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));
        linkedToEntities.add(this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION.typeName));
        linkedToEntities.add(this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION_REVIEW.typeName));
        linkedToEntities.add(this.archiveBuilder.getEntityDef(OpenMetadataType.LIKE_TYPE_NAME));
        linkedToEntities.add(this.archiveBuilder.getEntityDef(OpenMetadataType.RATING_TYPE_NAME));

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 descriptionWiki,
                                                                                 linkedToEntities,
                                                                                 true);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.ANCHOR_GUID.name;
        final String attribute1Description     = OpenMetadataProperty.ANCHOR_GUID.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.ANCHOR_GUID.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private RelationshipDef addSourcedFromRelationship()
    {
        final String guid            = OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeGUID;
        final String name            = OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName;
        final String description     = OpenMetadataType.SOURCED_FROM_RELATIONSHIP.description;
        final String descriptionGUID = OpenMetadataType.SOURCED_FROM_RELATIONSHIP.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.SOURCED_FROM_RELATIONSHIP.wikiURL;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                descriptionWiki,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "resultingElement";
        final String                     end1AttributeDescription     = "Element created from the template.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end2AttributeName            = "templateElement";
        final String                     end2AttributeDescription     = "Template element providing information.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
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
        this.archiveBuilder.addRelationshipDef(addRelatedKeywordRelationship());
    }

    private EntityDef getSearchKeywordEntity()
    {
        final String guid            = OpenMetadataType.SEARCH_KEYWORD.typeGUID;
        final String name            = OpenMetadataType.SEARCH_KEYWORD.typeName;
        final String description     = OpenMetadataType.SEARCH_KEYWORD.description;
        final String descriptionGUID = OpenMetadataType.SEARCH_KEYWORD.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.SEARCH_KEYWORD.wikiURL;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                null,
                                                                description,
                                                                descriptionGUID,
                                                                descriptionWiki);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.KEYWORD.name;
        final String attribute1Description     = OpenMetadataProperty.KEYWORD.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.KEYWORD.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef addSearchKeywordLinkRelationship()
    {
        final String guid            = OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeGUID;
        final String name            = OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName;
        final String description     = OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.description;
        final String descriptionGUID = OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.wikiURL;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                descriptionWiki,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "linkedElements";
        final String                     end1AttributeDescription     = "Element described by the search keyword.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.SEARCH_KEYWORD.typeName;
        final String                     end2AttributeName            = "searchKeywords";
        final String                     end2AttributeDescription     = "Keywords to describe the element.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef addRelatedKeywordRelationship()
    {
        final String guid            = OpenMetadataType.RELATED_KEYWORD_RELATIONSHIP.typeGUID;
        final String name            = OpenMetadataType.RELATED_KEYWORD_RELATIONSHIP.typeName;
        final String description     = OpenMetadataType.RELATED_KEYWORD_RELATIONSHIP.description;
        final String descriptionGUID = OpenMetadataType.RELATED_KEYWORD_RELATIONSHIP.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.RELATED_KEYWORD_RELATIONSHIP.wikiURL;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                descriptionWiki,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.SEARCH_KEYWORD.typeName;
        final String                     end1AttributeName            = "relatedKeyword";
        final String                     end1AttributeDescription     = "Keyword with similar meaning or usage.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.SEARCH_KEYWORD.typeName;
        final String                     end2AttributeName            = "relatedKeyword";
        final String                     end2AttributeDescription     = "Keyword with similar meaning or usage.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
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
        final String typeName = "OperatingPlatform";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "endianness";
        final String attribute1Description     = "Deprecated attribute. Use the byteOrdering attribute instead.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = OpenMetadataProperty.BYTE_ORDERING.name;
        final String attribute2Description     = OpenMetadataProperty.BYTE_ORDERING.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.BYTE_ORDERING.descriptionGUID;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute2Name);

        properties.add(property);

        property = archiveHelper.getEnumTypeDefAttribute(ByteOrdering.getOpenTypeName(),
                                                         attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0057 - Integration capabilities describe the different types of integration capabilities.
     * Initially these are Egeria's integration daemons.
     */
    private void add0057IntegrationCapabilities()
    {
        this.archiveBuilder.addEntityDef(addSoftwareServiceEntity());
        this.archiveBuilder.addEntityDef(addMetadataIntegrationServiceEntity());
        this.archiveBuilder.addEntityDef(addMetadataAccessServiceEntity());
        this.archiveBuilder.addEntityDef(addEngineHostingServiceEntity());
        this.archiveBuilder.addEntityDef(addUserViewServiceEntity());
    }


    private EntityDef addSoftwareServiceEntity()
    {
        final String guid            = "f3f69251-adb1-4042-9d95-70082f95a028";
        final String name            = "SoftwareService";
        final String description     = "Defines a capability that provides externally callable functions to other services.";
        final String descriptionGUID = null;

        final String superTypeName = "SoftwareServerCapability";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);

    }


    private EntityDef addMetadataIntegrationServiceEntity()
    {
        final String guid            = "92f7fe27-cd2f-441c-a084-156821aa5bca";
        final String name            = "MetadataIntegrationService";
        final String description     = "Defines a capability that exchanges metadata between servers.";
        final String descriptionGUID = null;

        final String superTypeName = "SoftwareService";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);

    }


    private EntityDef addMetadataAccessServiceEntity()
    {
        final String guid            = "0bc3a16a-e8ed-4ad0-a302-0773365fdef0";
        final String name            = "MetadataAccessService";
        final String description     = "Defines a capability that provides access to stored metadata.";
        final String descriptionGUID = null;

        final String superTypeName = "SoftwareService";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);

    }


    private EntityDef addEngineHostingServiceEntity()
    {
        final String guid            = "90880f0b-c7a3-4d1d-93cc-0b877f27cd33";
        final String name            = "EngineHostingService";
        final String description     = "Defines a capability that provides services that delegate to a hosted engine.";
        final String descriptionGUID = null;

        final String superTypeName = "SoftwareService";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);

    }

    private EntityDef addUserViewServiceEntity()
    {
        final String guid            = "1f83fc7c-75bb-491d-980d-ff9a6f80ae02";
        final String name            = "UserViewService";
        final String description     = "Defines a capability that provides user interfaces access to digital resources.";
        final String descriptionGUID = null;

        final String superTypeName = "SoftwareService";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);

    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0150 Feedback - deprecate anchorGUID.
     */
    private void update0150Feedback()
    {
        this.archiveBuilder.addTypeDefPatch(updateLikeEntity());
        this.archiveBuilder.addTypeDefPatch(updateCommentEntity());
        this.archiveBuilder.addTypeDefPatch(updateRatingEntity());
    }


    private TypeDefPatch updateLikeEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Like";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "anchorGUID";
        final String attribute1Description     = "Deprecated attribute. Use the Anchors classification instead.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);

        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch updateCommentEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Comment";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "anchorGUID";
        final String attribute1Description     = "Deprecated attribute. Use the Anchors classification instead.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);

        properties.add(property);

        final String attribute2Name            = "type";
        final String attribute2Description     = "Deprecated attribute. Use the commentType attribute to describe the type of comment.";
        final String attribute2DescriptionGUID = null;
        final String attribute2ReplacedBy      = "commentType";

        property = archiveHelper.getEnumTypeDefAttribute("CommentType",
                attribute2Name,
                attribute2Description,
                attribute2DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute2ReplacedBy);
        properties.add(property);

        final String attribute3Name            = "commentType";
        final String attribute3Description     = "Type of comment.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("CommentType",
                attribute3Name,
                attribute3Description,
                attribute3DescriptionGUID);

        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch updateRatingEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Rating";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "anchorGUID";
        final String attribute1Description     = "Deprecated attribute. Use the Anchors classification instead.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);

        properties.add(property);

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
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DEPLOYED_CONNECTOR.typeGUID,
                                                 OpenMetadataType.DEPLOYED_CONNECTOR.typeName,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName),
                                                 OpenMetadataType.DEPLOYED_CONNECTOR.description,
                                                 OpenMetadataType.DEPLOYED_CONNECTOR.descriptionGUID,
                                                 OpenMetadataType.DEPLOYED_CONNECTOR.wikiURL);
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
        archiveBuilder.addEntityDef(addGovernanceDomainDescriptionEntity());
        archiveBuilder.addEntityDef(addGovernanceClassificationLevelEntity());

        archiveBuilder.addClassificationDef(addGovernanceDomainSetClassification());
        archiveBuilder.addClassificationDef(addGovernanceClassificationSetClassification());

        archiveBuilder.addRelationshipDef(getGovernedByRelationship());

        archiveBuilder.addTypeDefPatch(updateCriticalityClassification());
        archiveBuilder.addTypeDefPatch(updateRetentionClassification());
        archiveBuilder.addTypeDefPatch(updateConfidenceClassification());
        archiveBuilder.addTypeDefPatch(updateConfidentialityClassification());
        archiveBuilder.addTypeDefPatch(updateGovernanceOfficerEntity());
        archiveBuilder.addTypeDefPatch(updateGovernanceDefinitionEntity());
        archiveBuilder.addTypeDefPatch(updateGovernanceRoleEntity());
        archiveBuilder.addTypeDefPatch(updateGovernanceMetricEntity());
        archiveBuilder.addTypeDefPatch(updateGovernanceZoneEntity());
        archiveBuilder.addTypeDefPatch(updateSubjectAreaDefinitionEntity());
        archiveBuilder.addTypeDefPatch(deprecateGovernanceConfidentialityLevelEntity());
        archiveBuilder.addTypeDefPatch(deprecateZoneGovernanceRelationship());
        archiveBuilder.addTypeDefPatch(deprecateSubjectAreaGovernanceRelationship());
    }

    private EnumDef getConfidentialityLevelEnum()
    {
        final String guid            = "abc48ca2-4d29-4de9-99a1-bc4db9816d68";
        final String name            = "ConfidentialityLevel";
        final String description     = "Defines how confidential a data item is.";
        final String descriptionGUID = null;

        EnumDef enumDef = archiveHelper.getEmptyEnumDef(guid, name, description, descriptionGUID);

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        final int    element1Ordinal         = 0;
        final String element1Value           = "Unclassified";
        final String element1Description     = "The data is public information.";
        final String element1DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element1Ordinal,
                                                     element1Value,
                                                     element1Description,
                                                     element1DescriptionGUID);
        elementDefs.add(elementDef);
        enumDef.setDefaultValue(elementDef);

        final int    element2Ordinal         = 1;
        final String element2Value           = "Internal";
        final String element2Description     = "The data should not be exposed outside of this organization.";
        final String element2DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element2Ordinal,
                                                     element2Value,
                                                     element2Description,
                                                     element2DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element3Ordinal         = 2;
        final String element3Value           = "Confidential";
        final String element3Description     = "The data should be protected and only shared with people with a need to see it.";
        final String element3DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element3Ordinal,
                                                     element3Value,
                                                     element3Description,
                                                     element3DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element4Ordinal         = 3;
        final String element4Value           = "Sensitive";
        final String element4Description     = "The data is sensitive and inappropriate use may adversely impact the data subject.";
        final String element4DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element4Ordinal,
                                                     element4Value,
                                                     element4Description,
                                                     element4DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element5Ordinal         = 4;
        final String element5Value           = "Restricted";
        final String element5Description     = "The data is very valuable and must be restricted to a very small number of people.";
        final String element5DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element5Ordinal,
                                                     element5Value,
                                                     element5Description,
                                                     element5DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element99Ordinal         = 99;
        final String element99Value           = "Other";
        final String element99Description     = "Another confidentially level.";
        final String element99DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element99Ordinal,
                                                     element99Value,
                                                     element99Description,
                                                     element99DescriptionGUID);
        elementDefs.add(elementDef);

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }

    private EntityDef addGovernanceDomainDescriptionEntity()
    {
        final String guid            = "084cd115-5d0d-4f12-8093-697526a120ea";
        final String name            = "GovernanceDomainDescription";
        final String description     = "A description of a governance domain along with an identifier for use in governance definitions.";
        final String descriptionGUID = null;

        final String superTypeName   = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "domainIdentifier";
        final String attribute1Description     = "Identifier used in governance definitions to show which governance domain they belong to.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME.name,
                                                           OpenMetadataProperty.DISPLAY_NAME.description,
                                                           OpenMetadataProperty.DISPLAY_NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private EntityDef addGovernanceClassificationLevelEntity()
    {
        final String guid            = "8af91d61-2ae8-4255-992e-14d7f745a556";
        final String name            = "GovernanceClassificationLevel";
        final String description     = "A value to represent a specific level in a governance classification definition.";
        final String descriptionGUID = null;
        final String superTypeName   = OpenMetadataType.REFERENCEABLE.typeName;

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(superTypeName),
                                                                description,
                                                                descriptionGUID);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "levelIdentifier";
        final String attribute1Description     = "Numeric value for the classification level";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME.name,
                                                           OpenMetadataProperty.DISPLAY_NAME.description,
                                                           OpenMetadataProperty.DISPLAY_NAME.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DESCRIPTION.name,
                                                           OpenMetadataProperty.DESCRIPTION.description,
                                                           OpenMetadataProperty.DESCRIPTION.descriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private ClassificationDef addGovernanceDomainSetClassification()
    {
        final String guid = "e66bb681-99a1-4712-a2c9-712c8b0f83ae";

        final String name            = "GovernanceDomainSet";
        final String description     = "Identifies the definitions for the different governance domains in use by the organization.";
        final String descriptionGUID = null;

        final List<TypeDefLink> linkedToEntities = new ArrayList<>();

        linkedToEntities.add(this.archiveBuilder.getEntityDef("Collection"));

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  linkedToEntities,
                                                  false);
    }

    private ClassificationDef addGovernanceClassificationSetClassification()
    {
        final String guid = "d92b7f31-c92d-418d-b345-ea45bb3f73f5";

        final String name            = "GovernanceClassificationSet";
        final String description     = "Identifies the set of levels that are used within a specific governance classification.";
        final String descriptionGUID = null;

        final List<TypeDefLink> linkedToEntities = new ArrayList<>();

        linkedToEntities.add(this.archiveBuilder.getEntityDef("Collection"));

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 linkedToEntities,
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "domainIdentifier";
        final String attribute1Description     = "Identifier of the governance domain that recognizes this set of levels.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "classificationName";
        final String attribute2Description     = "Name of the classification where this set of levels is used.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "classificationPropertyName";
        final String attribute3Description     = "Name of the property in the classification where this value is used.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private RelationshipDef getGovernedByRelationship()
    {
        final String guid            = "89c3c695-9e8d-4660-9f44-ed971fd55f89";
        final String name            = "GovernedBy";
        final String description     = "Shows the resources that are governed by a specific governance definition.";
        final String descriptionGUID = null;


        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "GovernanceDefinition";
        final String                     end1AttributeName            = "governedBy";
        final String                     end1AttributeDescription     = "The governance definition that defines how this element is governed.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end2AttributeName            = "governedElements";
        final String                     end2AttributeDescription     = "An element that is governed according to the governance definition.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private TypeDefPatch updateCriticalityClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Criticality";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "levelIdentifier";
        final String attribute2Description     = "Defined level for this classification.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch updateRetentionClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Retention";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "basisIdentifier";
        final String attribute2Description     = "Defined level of the retention basis for this classification.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch updateConfidenceClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Confidence";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "levelIdentifier";
        final String attribute2Description     = "Defined level for this classification.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch updateConfidentialityClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Confidentiality";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "level";
        final String attribute1Description     = "Deprecated attribute. Use the levelIdentifier attribute instead.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "levelIdentifier";
        final String attribute2Description     = "Defined level for this classification.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "confidentialityLevel";
        final String attribute3Description     = "Pre-defined level for this classification.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute2Name);

        properties.add(property);

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("ConfidentialityLevel",
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch updateGovernanceOfficerEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "GovernanceOfficer";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "domainIdentifier";
        final String attribute2Description     = "Identifier of the governance domain that this definition belongs to.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch updateGovernanceDefinitionEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "GovernanceDefinition";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "domainIdentifier";
        final String attribute2Description     = "Identifier of the governance domain that this definition belongs to.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch updateSubjectAreaDefinitionEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SubjectAreaDefinition";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "domainIdentifier";
        final String attribute2Description     = "Identifier of the governance domain that this definition belongs to.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch updateGovernanceRoleEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "GovernanceRole";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "domainIdentifier";
        final String attribute2Description     = "Identifier of the governance domain that this definition belongs to.";
        final String attribute2DescriptionGUID = null;

        final String attribute5Name            = "domain";
        final String attribute5Description     = "Governance domain for this governance definition.";
        final String attribute5DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceDomain",
                                                         attribute5Name,
                                                         attribute5Description,
                                                         attribute5DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch updateGovernanceMetricEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "GovernanceMetric";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "domainIdentifier";
        final String attribute2Description     = "Identifier of the governance domain that this definition belongs to.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch updateGovernanceZoneEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "GovernanceZone";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "domainIdentifier";
        final String attribute2Description     = "Identifier of the governance domain that this definition belongs to.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch deprecateGovernanceConfidentialityLevelEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "GovernanceConfidentialityLevel";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    private TypeDefPatch deprecateSubjectAreaGovernanceRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SubjectAreaGovernance";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    private TypeDefPatch deprecateZoneGovernanceRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ZoneGovernance";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 05xx - Deprecate RelationalView classification and replace it with CalculatedValue.  This is because the expression
     * rally belongs on the SchemaType and calculated values are possible on other types of schema.  Also enable
     * all schema types to be in a TypeEmbeddedAttribute classification.
     */
    private void update05xxSchemaAttributes()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateAnchorGUID());
        this.archiveBuilder.addTypeDefPatch(deprecateRelationalViewClassification());
        this.archiveBuilder.addTypeDefPatch(deprecateDerivedSchemaAttributeEntity());
        this.archiveBuilder.addTypeDefPatch(deprecateDerivedRelationalColumnEntity());
        this.archiveBuilder.addTypeDefPatch(deprecateSchemaLinkElementEntity());
        this.archiveBuilder.addTypeDefPatch(deprecateSchemaLinkToTypeRelationship());
        this.archiveBuilder.addTypeDefPatch(deprecateLinkedTypeRelationship());
        this.archiveBuilder.addTypeDefPatch(deprecateSchemaQueryImplementationRelationship());
        this.archiveBuilder.addTypeDefPatch(addPrecisionToSchemaAttributeEntity());
        this.archiveBuilder.addClassificationDef(getCalculatedValueClassification());
        this.archiveBuilder.addEntityDef(getExternalSchemaTypeEntity());
        this.archiveBuilder.addTypeDefPatch(updateMapFromElementTypeRelationship());
        this.archiveBuilder.addTypeDefPatch(updateMapToElementTypeRelationship());
        this.archiveBuilder.addTypeDefPatch(updateSchemaTypeOptionRelationship());
        this.archiveBuilder.addRelationshipDef(getDerivedSchemaTypeQueryTargetRelationship());
        this.archiveBuilder.addRelationshipDef(getLinkedExternalSchemaTypeRelationship());
    }


    private TypeDefPatch deprecateAnchorGUID()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SCHEMA_ELEMENT.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.ANCHOR_GUID.name,
                                                           OpenMetadataProperty.ANCHOR_GUID.description,
                                                           OpenMetadataProperty.ANCHOR_GUID.descriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);

        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private TypeDefPatch deprecateRelationalViewClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "RelationalView";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    private TypeDefPatch deprecateDerivedSchemaAttributeEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DerivedSchemaAttribute";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    private TypeDefPatch deprecateSchemaLinkElementEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SchemaLinkElement";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    private TypeDefPatch deprecateDerivedRelationalColumnEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DerivedRelationalColumn";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    private TypeDefPatch deprecateSchemaLinkToTypeRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SchemaLinkToType";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    private TypeDefPatch deprecateLinkedTypeRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "LinkedType";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    private TypeDefPatch deprecateSchemaQueryImplementationRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SchemaQueryImplementation";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
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
        TypeDefAttribute       property;

        final String attribute1Name            = "precision";
        final String attribute1Description     = "Number of digits after the decimal point.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    private ClassificationDef getCalculatedValueClassification()
    {
        final String guid            = "4814bec8-482d-463d-8376-160b0358e139";
        final String name            = "CalculatedValue";
        final String description     = "A field within a schema that is calculated via the formula and query targets rather than stored.";
        final String descriptionGUID = null;

        ClassificationDef classificationDef =  archiveHelper.getClassificationDef(guid,
                                                                                  name,
                                                                                  null,
                                                                                  description,
                                                                                  descriptionGUID,
                                                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                                  false);
        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "formula";
        final String attribute1Description     = "Expression to create the value.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private EntityDef getExternalSchemaTypeEntity()
    {
        final String guid            = "78de00ea-3d69-47ff-a6d6-767587526624";
        final String name            = "ExternalSchemaType";
        final String description     = "The schema type is defined using an external schema.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                 description,
                                                 descriptionGUID);
    }

    private RelationshipDef getLinkedExternalSchemaTypeRelationship()
    {
        final String guid            = "9a5d78c2-1716-4783-bfc6-c300a9e2d092";
        final String name            = "LinkedExternalSchemaType";
        final String description     = "Links to a reusable schema type that is external to this schema.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = OpenMetadataType.SCHEMA_ELEMENT.typeName;
        final String                     end1AttributeName            = "usedInSchema";
        final String                     end1AttributeDescription     = "Connection point for a reusable schema type.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "externalSchemaType";
        final String                     end2AttributeDescription     = "The schema type that is being reused in another schema.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_TYPE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getDerivedSchemaTypeQueryTargetRelationship()
    {
        final String guid            = "1c2622b7-ac21-413c-89e1-6f61f348cd19";
        final String name            = "DerivedSchemaTypeQueryTarget";
        final String description     = "Details of how a derived schema element is calculated.";
        final String descriptionGUID = null;

        final ClassificationPropagationRule classificationPropagationRule = ClassificationPropagationRule.NONE;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                classificationPropagationRule);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "usedBy";
        final String                     end1AttributeDescription     =
                "Use of another schema type to derive all or part of this schema type's value.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ELEMENT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "queryTarget";
        final String                     end2AttributeDescription     = "Used to provide data values to the other schema type.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ELEMENT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "queryId";
        final String attribute1Description     = "Identifier for placeholder in derived schema type's formula.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        final String attribute2Name            = "query";
        final String attribute2Description     = "Details of how the value(s) is/are retrieved.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

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
        final String typeName = "MapFromElementType";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "parentMapFrom";
        final String                     end1AttributeDescription     = "Used in map to describe the domain (value mapped from).";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ELEMENT.typeName),
                                                                                    end1AttributeName,
                                                                                    end1AttributeDescription,
                                                                                    end1AttributeDescriptionGUID,
                                                                                    end1Cardinality);
        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }


    private TypeDefPatch updateMapToElementTypeRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "MapToElementType";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "parentMapTo";
        final String                     end1AttributeDescription     = "Used in map to describe the range (value mapped to).";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SCHEMA_ELEMENT.typeName),
                                                                                    end1AttributeName,
                                                                                    end1AttributeDescription,
                                                                                    end1AttributeDescriptionGUID,
                                                                                    end1Cardinality);
        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0505 - Deprecate RelationalView classification and replace it with CalculatedValue.  This is because the expression
     * rally belongs on the SchemaType and calculated values are possible on other types of schema
     */
    private void update06xxDiscovery()
    {
        this.archiveBuilder.addTypeDefPatch(updateDiscoveryReportEntity());
        this.archiveBuilder.addTypeDefPatch(updateAnnotationEntity());
        this.archiveBuilder.addTypeDefPatch(updateDataFieldEntity());
    }



    private TypeDefPatch updateDiscoveryReportEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "OpenDiscoveryAnalysisReport";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "anchorGUID";
        final String attribute1Description     = "Deprecated attribute. Use the Anchors classification instead.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);

        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    private TypeDefPatch updateAnnotationEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.ANNOTATION.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "anchorGUID";
        final String attribute1Description     = "Deprecated attribute. Use the Anchors classification instead.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);

        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    private TypeDefPatch updateDataFieldEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DataField";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "anchorGUID";
        final String attribute1Description     = "Deprecated attribute. Use the Anchors classification instead.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);

        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0545 - Add the isDeprecated boolean attribute to ValidValueDefinition.
     */
    private void update0545ReferenceData()
    {
        this.archiveBuilder.addTypeDefPatch(addIsDeprecated());

    }

    private TypeDefPatch addIsDeprecated()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ValidValueDefinition";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name = "isDeprecated";
        final String attribute1Description = "Indicates that this value is deprecated and all uses should be discontinued as soon as possible.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getBooleanTypeDefAttribute(attribute1Name,
                                                            attribute1Description,
                                                            attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

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
        this.archiveBuilder.addTypeDefPatch(updateSoftwareServerPlatformEntity());
        this.archiveBuilder.addTypeDefPatch(updateSoftwareServerEntity());
        this.archiveBuilder.addTypeDefPatch(updateSoftwareServerCapabilityEntity());
        this.archiveBuilder.addTypeDefPatch(updateCloudPlatformClassification());
        this.archiveBuilder.addTypeDefPatch(updateCloudTenantClassification());
        this.archiveBuilder.addTypeDefPatch(updateCloudServiceClassification());
        // Comment entity's clashing properties are updated as part of other updates to Comment above
        this.archiveBuilder.addTypeDefPatch(updateGraphStoreEntity());
        this.archiveBuilder.addTypeDefPatch(updateLogFileEntity());
        this.archiveBuilder.addTypeDefPatch(updateDatabaseEntity());
        this.archiveBuilder.addTypeDefPatch(updateDatabaseServerClassification());
        this.archiveBuilder.addTypeDefPatch(updateMetadataRepositoryEntity());
        this.archiveBuilder.addTypeDefPatch(updateMetadataServerClassification());
        this.archiveBuilder.addTypeDefPatch(updateRepositoryProxyClassification());
        this.archiveBuilder.addTypeDefPatch(updateBusinessCapabilityEntity());
        this.archiveBuilder.addTypeDefPatch(updateEnterpriseAccessLayerEntity());
        this.archiveBuilder.addTypeDefPatch(updateMetadataCollectionEntity());
        this.archiveBuilder.addTypeDefPatch(updateExternalReferenceEntity());
        this.archiveBuilder.addTypeDefPatch(updatePropertyFacetEntity());
        this.archiveBuilder.addTypeDefPatch(updateCohortMemberEntity());
        this.archiveBuilder.addTypeDefPatch(updateImplementationSnippetEntity());
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
    private TypeDefPatch updateSoftwareServerPlatformEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SoftwareServerPlatform";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the deployedImplementationType attribute to describe the type of software server platform.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "deployedImplementationType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute3Name            = "version";
        final String attribute3Description     = "Deprecated attribute. Use the platformVersion attribute to define the version number of software server platform.";
        final String attribute3DescriptionGUID = null;
        final String attribute3ReplacedBy      = "platformVersion";

        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute3ReplacedBy);
        properties.add(property);

        final String attribute4Name            = "platformVersion";
        final String attribute4Description     = "Version number of the software server platform.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateSoftwareServerEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SoftwareServer";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the deployedImplementationType attribute to describe the type of software server.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "deployedImplementationType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute3Name            = "version";
        final String attribute3Description     = "Deprecated attribute. Use the serverVersion attribute to define the version number of software server.";
        final String attribute3DescriptionGUID = null;
        final String attribute3ReplacedBy      = "serverVersion";

        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute3ReplacedBy);
        properties.add(property);

        final String attribute4Name            = "serverVersion";
        final String attribute4Description     = "Version number of the software server.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateSoftwareServerCapabilityEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SoftwareServerCapability";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the deployedImplementationType attribute to describe the type of software server capability.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "capabilityType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute3Name            = "version";
        final String attribute3Description     = "Deprecated attribute. Use the capabilityVersion attribute to define the version number of software server capability.";
        final String attribute3DescriptionGUID = null;
        final String attribute3ReplacedBy      = "capabilityVersion";

        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute3ReplacedBy);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateCloudPlatformClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CLOUD_PLATFORM_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the deployedImplementationType attribute to describe the type of cloud platform.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);
        properties.add(property);

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                           OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.description,
                                                           OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.descriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateCloudTenantClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CLOUD_TENANT_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the tenantType attribute to describe the type of cloud tenant.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.TENANT_TYPE.name);
        properties.add(property);

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.TENANT_TYPE.name,
                                                           OpenMetadataProperty.TENANT_TYPE.description,
                                                           OpenMetadataProperty.TENANT_TYPE.descriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateCloudServiceClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CLOUD_SERVICE_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the serviceType attribute to describe the type of cloud service.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.SERVICE_TYPE.name);
        properties.add(property);

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SERVICE_TYPE.name,
                                                           OpenMetadataProperty.SERVICE_TYPE.description,
                                                           OpenMetadataProperty.SERVICE_TYPE.descriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateGraphStoreEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "GraphStore";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the deployedImplementationType attribute to describe the type of graph store.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateLogFileEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.LOG_FILE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the deployedImplementationType attribute to describe the type of log file.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);
        properties.add(property);

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.PURPOSE.name,
                                                           OpenMetadataProperty.PURPOSE.description,
                                                           OpenMetadataProperty.PURPOSE.descriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateDatabaseEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Database";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the deployedImplementationType attribute to describe the type of database.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);
        properties.add(property);

        final String attribute3Name            = "version";
        final String attribute3Description     = "Deprecated attribute. Use the databaseVersion attribute to define the version number of database.";
        final String attribute3DescriptionGUID = null;
        final String attribute3ReplacedBy      = OpenMetadataProperty.DATABASE_VERSION.name;

        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute3ReplacedBy);
        properties.add(property);

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.DATABASE_VERSION.name,
                                                           OpenMetadataProperty.DATABASE_VERSION.description,
                                                           OpenMetadataProperty.DATABASE_VERSION.descriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateDatabaseServerClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DatabaseServer";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the deployedImplementationType attribute to describe the type of database server.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);
        properties.add(property);

        final String attribute3Name            = "version";
        final String attribute3Description     = "Deprecated attribute. Use the softwareVersion attribute to define the version number of database server software.";
        final String attribute3DescriptionGUID = null;
        final String attribute3ReplacedBy      = "softwareVersion";

        property = archiveHelper.getStringTypeDefAttribute(attribute3Name,
                                                           attribute3Description,
                                                           attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute3ReplacedBy);
        properties.add(property);

        final String attribute4Name            = "softwareVersion";
        final String attribute4Description     = "Version of the database server software.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute4Name,
                                                           attribute4Description,
                                                           attribute4DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateMetadataRepositoryEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "MetadataRepository";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the deployedImplementationType attribute to describe the type of metadata repository.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateMetadataServerClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "MetadataServer";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the deployedImplementationType attribute to describe the type of metadata server.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateRepositoryProxyClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "RepositoryProxy";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the deployedImplementationType attribute to describe the type of repository proxy.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateBusinessCapabilityEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "BusinessCapability";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the businessImplementationType attribute to describe the type of business capability.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "businessImplementationType";

        property = archiveHelper.getEnumTypeDefAttribute("BusinessCapabilityType",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "businessImplementationType";
        final String attribute2Description     = "Type of implemented or deployed business capability.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("BusinessCapabilityType",
                                                         attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateEnterpriseAccessLayerEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "EnterpriseAccessLayer";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "metadataCollectionId";
        final String attribute1Description     = "Deprecated attribute. Use the accessedMetadataCollectionId attribute to define the unique identifier for the metadata collection accessed through this enterprise access layer.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "accessedMetadataCollectionId";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "accessedMetadataCollectionId";
        final String attribute2Description     = "Unique identifier for the metadata collection accessed through this enterprise access layer.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateMetadataCollectionEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "MetadataCollection";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "metadataCollectionId";
        final String attribute1Description     = "Deprecated attribute. Use the managedMetadataCollectionId attribute to define the unique identifier for the metadata collection managed in the local repository.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "managedMetadataCollectionId";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "managedMetadataCollectionId";
        final String attribute2Description     = "Unique identifier for the metadata collection managed in the local repository.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateExternalReferenceEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ExternalReference";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "version";
        final String attribute1Description     = "Deprecated attribute. Use the referenceVersion attribute to define the version number of the external reference.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "referenceVersion";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "referenceVersion";
        final String attribute2Description     = "Version number of the external reference.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updatePropertyFacetEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PROPERTY_FACET.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "version";
        final String attribute1Description     = "Deprecated attribute. Use the schemaVersion attribute to define the version number of the property facet schema.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.SCHEMA_VERSION.name);
        properties.add(property);

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SCHEMA_VERSION.name,
                                                           OpenMetadataProperty.SCHEMA_VERSION.description,
                                                           OpenMetadataProperty.SCHEMA_VERSION.descriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateCohortMemberEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "CohortMember";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "protocolVersion";
        final String attribute2Description     = "Version number of the protocol supported by the cohort registry.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    /**
     * Deprecate clashing properties and add new ones to replace them.
     * @return the type def patch
     */
    private TypeDefPatch updateImplementationSnippetEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ImplementationSnippet";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "version";
        final String attribute1Description     = "Deprecated attribute. Use the snippetVersion attribute to define the version number of the snippet.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "snippetVersion";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "snippetVersion";
        final String attribute2Description     = "Version number of the snippet.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
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
        final String typeName = "PolicyAdministrationPoint";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the pointType attribute to describe type information about the policy administration point.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "pointType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "pointType";
        final String attribute2Description     = "Descriptive type information about the policy administration point.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);

        properties.add(property);


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
        final String typeName = "PolicyDecisionPoint";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the pointType attribute to describe type information about the policy decision point.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "pointType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "pointType";
        final String attribute2Description     = "Descriptive type information about the policy decision point.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);

        properties.add(property);


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
        final String typeName = "PolicyEnforcementPoint";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the pointType attribute to describe type information about the policy enforcement point.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "pointType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "pointType";
        final String attribute2Description     = "Descriptive type information about the policy enforcement point.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);

        properties.add(property);


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
        final String typeName = "PolicyInformationPoint";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the pointType attribute to describe type information about the policy information point.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "pointType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "pointType";
        final String attribute2Description     = "Descriptive type information about the policy information point.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);

        properties.add(property);


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
        final String typeName = "PolicyRetrievalPoint";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "type";
        final String attribute1Description     = "Deprecated attribute. Use the pointType attribute to describe type information about the policy retrieval point.";
        final String attribute1DescriptionGUID = null;
        final String attribute1ReplacedBy      = "pointType";

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute1ReplacedBy);
        properties.add(property);

        final String attribute2Name            = "pointType";
        final String attribute2Description     = "Descriptive type information about the policy retrieval point.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

}

