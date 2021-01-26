/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


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
 * OpenMetadataTypesArchive builds an open metadata archive containing all of the standard open metadata types.
 * These types have hardcoded dates and guids so that however many times this archive is rebuilt, it will
 * produce the same content.
 * <p>
 * Details of the open metadata types are documented on the wiki:
 * <a href="https://egeria.odpi.org/open-metadata-publication/website/open-metadata-types/">The Open Metadata Type System</a>
 * </p>
 * <p>
 * There are 8 areas, each covering a different topic area of metadata.  The module breaks down the process of creating
 * the models into the areas and then the individual models to simplify the maintenance of this class
 * </p>
 */
public class OpenMetadataTypesArchive2_5
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "2.5";
    private static final String                  originatorName     = "ODPi Egeria";
    private static final String                  originatorLicense  = "Apache 2.0";
    private static final Date                    creationDate       = new Date(1588261366992L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";


    private OMRSArchiveBuilder archiveBuilder;
    private OMRSArchiveHelper  archiveHelper;


    /**
     * Default constructor sets up the archive builder.  This in turn sets up the header for the archive.
     */
    public OpenMetadataTypesArchive2_5()
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
    public OpenMetadataTypesArchive2_5(OMRSArchiveBuilder archiveBuilder)
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
     * Returns the open metadata type archive containing all of the standard open metadata types.
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
        OpenMetadataTypesArchive2_4 previousTypes = new OpenMetadataTypesArchive2_4(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();


        /*
         * Calls for new and changed types go here
         */
        update0010BaseEntity();
        update0017ExternalIdentifiers();
        add0056AssetManager();
        add0395SupplementaryProperties();
        update0750DataPassing();
        update0770LineageMapping();

    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0010 Add a common root entity
     */
    private void update0010BaseEntity()
    {
        this.archiveBuilder.addEntityDef(addOpenMetadataRootEntity());

        this.archiveBuilder.addTypeDefPatch(updateReferenceable());
        this.archiveBuilder.addTypeDefPatch(updateLastAttachment());
        this.archiveBuilder.addTypeDefPatch(updateSearchKeyword());
        this.archiveBuilder.addTypeDefPatch(updateLike());
        this.archiveBuilder.addTypeDefPatch(updateRating());
        this.archiveBuilder.addTypeDefPatch(updateInformalTag());
        this.archiveBuilder.addTypeDefPatch(updateAnnotation());
        this.archiveBuilder.addTypeDefPatch(updateAnnotationReview());
        this.archiveBuilder.addTypeDefPatch(updateDataField());

        this.archiveBuilder.addTypeDefPatch(updateAnchorsClassification());

    }


    private EntityDef addOpenMetadataRootEntity()
    {
        final String guid            = "4e7761e8-3969-4627-8f40-bfe3cde85a1d";
        final String name            = "OpenMetadataRoot";
        final String description     = "Common root for all open metadata entity types.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 null,
                                                 description,
                                                 descriptionGUID);
    }


    private TypeDefPatch updateReferenceable()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Referenceable";

        final String superTypeName = "OpenMetadataRoot";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateLastAttachment()
    {
        /*
         * Create the Patch
         */
        final String typeName = "LastAttachment";

        final String superTypeName = "OpenMetadataRoot";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateSearchKeyword()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SearchKeyword";

        final String superTypeName = "OpenMetadataRoot";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateLike()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Like";

        final String superTypeName = "OpenMetadataRoot";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateRating()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Rating";

        final String superTypeName = "OpenMetadataRoot";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateInformalTag()
    {
        /*
         * Create the Patch
         */
        final String typeName = "InformalTag";

        final String superTypeName = "OpenMetadataRoot";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateAnnotation()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Annotation";

        final String superTypeName = "OpenMetadataRoot";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateAnnotationReview()
    {
        /*
         * Create the Patch
         */
        final String typeName = "AnnotationReview";

        final String superTypeName = "OpenMetadataRoot";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateDataField()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DataField";

        final String superTypeName = "OpenMetadataRoot";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));

        return typeDefPatch;
    }


    private TypeDefPatch updateAnchorsClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Anchors";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        final List<TypeDefLink> linkedToEntities = new ArrayList<>();

        linkedToEntities.add(this.archiveBuilder.getEntityDef("OpenMetadataRoot"));

        typeDefPatch.setValidEntityDefs(linkedToEntities);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0017 External Identifier - add new properties for integration
     */
    private void update0017ExternalIdentifiers()
    {
        this.archiveBuilder.addEnumDef(getPermittedSynchronizationEnum());
        this.archiveBuilder.addTypeDefPatch(updateExternalIdScopeRelationship());
        this.archiveBuilder.addTypeDefPatch(updateExternalIdLinkRelationship());
    }


    private EnumDef getPermittedSynchronizationEnum()
    {
        final String guid            = "973a9f4c-93fa-43a5-a0c5-d97dbd164e78";
        final String name            = "PermittedSynchronization";
        final String description     = "Defines the synchronization rules between a third party technology and open metadata.";
        final String descriptionGUID = null;

        EnumDef enumDef = archiveHelper.getEmptyEnumDef(guid, name, description, descriptionGUID);

        ArrayList<EnumElementDef> elementDefs = new ArrayList<>();
        EnumElementDef            elementDef;

        final int    element1Ordinal         = 0;
        final String element1Value           = "BothDirections";
        final String element1Description     = "Metadata exchange is permitted in both directions.";
        final String element1DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element1Ordinal,
                                                     element1Value,
                                                     element1Description,
                                                     element1DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element2Ordinal         = 1;
        final String element2Value           = "ToThirdParty";
        final String element2Description     = "The third party technology is logically downstream of open metadata and is just receiving metadata.";
        final String element2DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element2Ordinal,
                                                     element2Value,
                                                     element2Description,
                                                     element2DescriptionGUID);
        elementDefs.add(elementDef);

        final int    element3Ordinal         = 2;
        final String element3Value           = "FromThirdParty";
        final String element3Description     = "The third party technology is logically upstream and is publishing metadata to open metadata.";
        final String element3DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element3Ordinal,
                                                     element3Value,
                                                     element3Description,
                                                     element3DescriptionGUID);
        elementDefs.add(elementDef);


        final int    element8Ordinal         = 99;
        final String element8Value           = "Other";
        final String element8Description     = "Another synchronization rule.";
        final String element8DescriptionGUID = null;

        elementDef = archiveHelper.getEnumElementDef(element8Ordinal,
                                                     element8Value,
                                                     element8Description,
                                                     element8DescriptionGUID);
        elementDefs.add(elementDef);

        enumDef.setElementDefs(elementDefs);

        return enumDef;
    }


    private TypeDefPatch updateExternalIdScopeRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ExternalIdScope";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "permittedSynchronization";
        final String attribute1Description     =
                "Defines the permitted directions of flow of metadata updates between open metadata and a third party technology.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Additional description of the type of synchronization occurring.";
        final String attribute2DescriptionGUID = null;


        property = archiveHelper.getEnumTypeDefAttribute("PermittedSynchronization",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private TypeDefPatch updateExternalIdLinkRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ExternalIdLink";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);


        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "OpenMetadataRoot";
        final String                     end1AttributeName            = "resources";
        final String                     end1AttributeDescription     = "Resource being identified.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        typeDefPatch.setEndDef1(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "lastSynchronized";
        final String attribute1Description     =
                "Timestamp documenting the last time the metadata in the external metadata source was synchronized with open metadata element.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "mappingProperties";
        final String attribute2Description     = "Additional properties to aid the mapping to the the element in an external metadata source.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getDateTypeDefAttribute(attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute2Name,
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
     * 0056 - Asset Managers are metadata stores that are not functional enough to register as a cohort member.
     */
    private void add0056AssetManager()
    {
        this.archiveBuilder.addClassificationDef(addAssetManagerClassification());
    }


    private ClassificationDef addAssetManagerClassification()
    {
        final String guid            = "03170ce7-edf1-4e94-b6ab-2d5cbbf1f13c";
        final String name            = "AssetManager";
        final String description     = "Defines a capability that manages metadata about assets.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Referenceable";

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);

    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0395 Supplementary Properties
     */
    private void add0395SupplementaryProperties()
    {
        this.archiveBuilder.addClassificationDef(addElementSupplementClassification());
        this.archiveBuilder.addRelationshipDef(getSupplementaryPropertiesRelationship());
    }


    private ClassificationDef addElementSupplementClassification()
    {
        final String guid            = "58520015-ce6e-47b7-a1fd-864030544819";
        final String name            = "ElementSupplement";
        final String description     = "Identifies a glossary term that is being used to supplement asset descriptions.";
        final String descriptionGUID = null;

        final String linkedToEntity = "GlossaryTerm";

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);

    }


    private RelationshipDef getSupplementaryPropertiesRelationship()
    {
        final String guid            = "2bb10ba5-7aa2-456a-8b3a-8fdbd75c95cd";
        final String name            = "SupplementaryProperties";
        final String description     =
                "Provides additional descriptive properties to augment technical metadata extracted from a third party technology.";
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
        final String                     end1EntityType               = "Referenceable";
        final String                     end1AttributeName            = "supplementsElement";
        final String                     end1AttributeDescription     = "Describes this technical element.";
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
        final String                     end2EntityType               = "GlossaryTerm";
        final String                     end2AttributeName            = "supplementaryProperties";
        final String                     end2AttributeDescription     = "Provides more information about this element.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

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


    private void update0750DataPassing()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateProcessInputRelationship());
        this.archiveBuilder.addTypeDefPatch(deprecateProcessOutputRelationship());
        this.archiveBuilder.addTypeDefPatch(updateProcessCallRelationship());
        this.archiveBuilder.addRelationshipDef(addDataFlowRelationship());
        this.archiveBuilder.addRelationshipDef(addControlFlowRelationship());
    }


    private TypeDefPatch deprecateProcessInputRelationship()
    {
        final String typeName = "ProcessInput";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private TypeDefPatch deprecateProcessOutputRelationship()
    {
        final String typeName = "ProcessInput";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private TypeDefPatch updateProcessCallRelationship()
    {
        final String typeName    = "ProcessCall";
        final String description = "Shows a request-response call between two elements.";


        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setDescription(description);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "Referenceable";
        final String                     end1AttributeName            = "caller";
        final String                     end1AttributeDescription     = "Call originator.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        typeDefPatch.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "Referenceable";
        final String                     end2AttributeName            = "called";
        final String                     end2AttributeDescription     = "Called element that performs the processing.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        typeDefPatch.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "qualifiedName";
        final String attribute1Description     = "Unique name of the call relationship.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description and purpose of the call.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "formula";
        final String attribute3Description     = "Function that determines the subset of the data that flows.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
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

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    private RelationshipDef addDataFlowRelationship()
    {
        final String guid            = "d2490c0c-06cc-458a-add2-33cf2f5dd724";
        final String name            = "DataFlow";
        final String description     = "Shows that data flows in one direction from one element to another.";
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
        final String                     end1EntityType               = "Referenceable";
        final String                     end1AttributeName            = "dataSupplier";
        final String                     end1AttributeDescription     = "Caller element.";
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
        final String                     end2EntityType               = "Referenceable";
        final String                     end2AttributeName            = "dataConsumer";
        final String                     end2AttributeDescription     = "Called element.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
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

        final String attribute1Name            = "qualifiedName";
        final String attribute1Description     = "Unique name of the flow relationship.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description and purpose of the flow.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "formula";
        final String attribute3Description     = "Function that determines the subset of the data that flows.";
        final String attribute3DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
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

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef addControlFlowRelationship()
    {
        final String guid            = "35450726-1c32-4d41-b928-22db6d1ae2f4";
        final String name            = "ControlFlow";
        final String description     = "Shows that when one element completes processing, control passes to the next element.";
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
        final String                     end1EntityType               = "Referenceable";
        final String                     end1AttributeName            = "currentStep";
        final String                     end1AttributeDescription     = "Element that executes first.";
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
        final String                     end2EntityType               = "Referenceable";
        final String                     end2AttributeName            = "nextStep";
        final String                     end2AttributeDescription     = "Element that executes next.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
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

        final String attribute1Name            = "qualifiedName";
        final String attribute1Description     = "Unique name of the control flow relationship.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description and purpose of the control flow.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "guard";
        final String attribute3Description     = "Function that must be true to travel down this control flow.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
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

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void update0770LineageMapping()
    {
        this.archiveBuilder.addTypeDefPatch(updateLineageMappingRelationship());
    }


    private TypeDefPatch updateLineageMappingRelationship()
    {
        final String typeName = "LineageMapping";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "Referenceable";
        final String                     end1AttributeName            = "sourceElement";
        final String                     end1AttributeDescription     = "Source Attribute.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 end1Cardinality);
        typeDefPatch.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "Referenceable";
        final String                     end2AttributeName            = "targetElement";
        final String                     end2AttributeDescription     = "Target Attribute.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.ANY_NUMBER;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }
}

