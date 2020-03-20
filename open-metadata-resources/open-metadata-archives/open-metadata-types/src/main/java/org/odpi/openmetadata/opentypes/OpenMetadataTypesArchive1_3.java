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
public class OpenMetadataTypesArchive1_3
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveVersion     = "1.3";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  originatorName     = "ODPi Egeria";
    private static final String                  originatorLicense  = "Apache 2.0";
    private static final Date                    creationDate       = new Date(1516313040008L);

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
    public OpenMetadataTypesArchive1_3()
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
    public OpenMetadataTypesArchive1_3(OMRSArchiveBuilder archiveBuilder)
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
             * Calls for new types go here
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
     * Add the new types from this archive to the archive builder supplied in the
     * constructor.
     */
    public void getOriginalTypes()
    {
        OpenMetadataTypesArchive1_2  previousTypes = new OpenMetadataTypesArchive1_2(archiveBuilder);

        /*
         * Call each of the methods to systematically add the contents of the archive.
         * The original types are added first.
         */
        previousTypes.getOriginalTypes();

        this.update0205ConnectionLinkage();
        this.update0605OpenDiscoveryAnalysisReports();
        this.add0655AssetDeduplication();
    }

    /**
     * 0205 Connection Linkage defines virtual connections
     */
    private void update0205ConnectionLinkage()
    {
        this.archiveBuilder.addTypeDefPatch(updateEmbeddedConnection());
    }


    /**
     * 0205 EmbeddedConnection is missing the position property
     */
    private TypeDefPatch updateEmbeddedConnection()
    {
        /*
         * Create the Patch
         */
        final String typeName = "EmbeddedConnection";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);


        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name = "position";
        final String attribute1Description = "Position that embedded connection should be processed.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * 0605 Open Discovery Analysis Reports group annotations
     */
    private void update0605OpenDiscoveryAnalysisReports()
    {
        this.archiveBuilder.addTypeDefPatch(updateOpenDiscoveryAnalysisReport());
    }


    /**
     * 0605 Add discovery request step for long running discovery services
     */
    private TypeDefPatch updateOpenDiscoveryAnalysisReport()
    {
        /*
         * Create the Patch
         */
        final String typeName = "OpenDiscoveryAnalysisReport";

        TypeDefPatch  typeDefPatch =  archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);


        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name = "discoveryRequestStep";
        final String attribute1Description = "Current Step that an in-progress discovery service request has reached.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /**
     * 580 Solution Blueprints enable the recording of solution component models
     */
    private void add0655AssetDeduplication()
    {
        this.archiveBuilder.addEntityDef(getSuspectDuplicateAnnotationEntity());
        this.archiveBuilder.addEntityDef(getDivergentDuplicateAnnotationEntity());
        this.archiveBuilder.addEntityDef(getDivergentValueAnnotationEntity());
        this.archiveBuilder.addEntityDef(getDivergentClassificationAnnotationEntity());
        this.archiveBuilder.addEntityDef(getDivergentRelationshipAnnotationEntity());
        this.archiveBuilder.addEntityDef(getDivergentAttachmentAnnotationEntity());
        this.archiveBuilder.addEntityDef(getDivergentAttachmentValueAnnotationEntity());
        this.archiveBuilder.addEntityDef(getDivergentAttachmentClassificationAnnotationEntity());
        this.archiveBuilder.addEntityDef(getDivergentAttachmentRelationshipAnnotationEntity());
    }


    private EntityDef getSuspectDuplicateAnnotationEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "f703a621-4078-4c07-ab22-e7c334b94235";
        final String name            = "SuspectDuplicateAnnotation";
        final String description     = "Annotation linking referenceables that are suspected of being duplicates.";
        final String descriptionGUID = null;
        final String superTypeName   = "Annotation";

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

        final String attribute1Name            = "duplicateAnchorGUIDs";
        final String attribute1Description     = "List of unique identifiers for the suspects.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "matchingPropertyNames";
        final String attribute2Description     = "List of properties that are the same in the suspects.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "matchingClassificationNames";
        final String attribute3Description     = "List of classifications that are the same in the suspects.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "matchingAttachmentGUIDs";
        final String attribute4Description     = "List of attachments that are the same in the suspects.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "matchingRelationshipGUIDs";
        final String attribute5Description     = "List of direct relationships that are the same in the suspects.";
        final String attribute5DescriptionGUID = null;

        property = archiveHelper.getArrayStringTypeDefAttribute(attribute1Name,
                                                                attribute1Description,
                                                                attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute2Name,
                                                                attribute2Description,
                                                                attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute3Name,
                                                                attribute3Description,
                                                                attribute3DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute4Name,
                                                                attribute4Description,
                                                                attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute5Name,
                                                                attribute5Description,
                                                                attribute5DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getDivergentDuplicateAnnotationEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "251e443c-dee0-47fa-8a73-1a9d511915a0";
        final String name            = "DivergentDuplicateAnnotation";
        final String description     = "Annotation documenting differences in the values of acknowledged duplicates.";
        final String descriptionGUID = null;
        final String superTypeName   = "Annotation";

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

        final String attribute1Name            = "duplicateAnchorGUID";
        final String attribute1Description     = "Unique identifier of the duplicate where the differences have been found.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getDivergentValueAnnotationEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "b86cdded-1078-4e42-b6ba-a718c2c67f62";
        final String name            = "DivergentValueAnnotation";
        final String description     = "Annotation documenting differences in the property values of acknowledged duplicates.";
        final String descriptionGUID = null;
        final String superTypeName   = "DivergentDuplicateAnnotation";

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

        final String attribute1Name            = "divergentPropertyNames";
        final String attribute1Description     = "Names of the properties where a difference has been found.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getArrayStringTypeDefAttribute(attribute1Name,
                                                                attribute1Description,
                                                                attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getDivergentClassificationAnnotationEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "8efd6257-a53e-451d-abfc-8e4899c38b1f";
        final String name            = "DivergentClassificationAnnotation";
        final String description     = "Annotation documenting differences in a classification of acknowledged duplicates.";
        final String descriptionGUID = null;
        final String superTypeName   = "DivergentDuplicateAnnotation";

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

        final String attribute1Name            = "divergentClassificationName";
        final String attribute1Description     = "Name of the classification where a difference has been found.";
        final String attribute1DescriptionGUID = null;

        final String attribute2Name            = "divergentClassificationPropertyNames";
        final String attribute2Description     = "Names of the properties where a difference has been found.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute2Name,
                                                                attribute2Description,
                                                                attribute2DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getDivergentRelationshipAnnotationEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "b6c6938a-fdc9-438f-893c-0b5b1d4a5bb3";
        final String name            = "DivergentRelationshipAnnotation";
        final String description     = "Annotation documenting differences in a relationships of acknowledged duplicates.";
        final String descriptionGUID = null;
        final String superTypeName   = "DivergentDuplicateAnnotation";

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

        final String attribute1Name            = "divergentRelationshipGUID";
        final String attribute1Description     = "Unique identifier of the relationship where a difference has been found.";
        final String attribute1DescriptionGUID = null;

        final String attribute2Name            = "divergentRelationshipPropertyNames";
        final String attribute2Description     = "Names of the properties where a difference has been found.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute2Name,
                                                                attribute2Description,
                                                                attribute2DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }



    private EntityDef getDivergentAttachmentAnnotationEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "f3ed48bc-b0ea-4e1f-a8ab-75f9f3cf87a6";
        final String name            = "DivergentAttachmentAnnotation";
        final String description     = "Annotation documenting differences in the attachments of acknowledged duplicates.";
        final String descriptionGUID = null;
        final String superTypeName   = "DivergentDuplicateAnnotation";

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

        final String attribute1Name            = "attachmentGUID";
        final String attribute1Description     = "Unique identifier of the attachment where the differences have been found.";
        final String attribute1DescriptionGUID = null;

        final String attribute2Name            = "duplicateAttachmentGUID";
        final String attribute2Description     = "Unique identifier of the attachment in the duplicate where the differences have been found.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getDivergentAttachmentValueAnnotationEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "e22a1ffe-bd90-4faf-b6a1-13fafb7948a2";
        final String name            = "DivergentAttachmentValueAnnotation";
        final String description     = "Annotation documenting differences in the property values in attachments of acknowledged duplicates.";
        final String descriptionGUID = null;
        final String superTypeName   = "DivergentAttachmentAnnotation";

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

        final String attribute1Name            = "divergentPropertyNames";
        final String attribute1Description     = "Names of the properties where a difference has been found.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getArrayStringTypeDefAttribute(attribute1Name,
                                                                attribute1Description,
                                                                attribute1DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getDivergentAttachmentClassificationAnnotationEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "a2a5cb74-f8e0-470f-be71-26b7e32166a6";
        final String name            = "DivergentAttachmentClassificationAnnotation";
        final String description     = "Annotation documenting differences in a classification of an attachment of acknowledged duplicates.";
        final String descriptionGUID = null;
        final String superTypeName   = "DivergentAttachmentAnnotation";

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

        final String attribute1Name            = "divergentClassificationName";
        final String attribute1Description     = "Name of the classification where a difference has been found.";
        final String attribute1DescriptionGUID = null;

        final String attribute2Name            = "divergentClassificationPropertyNames";
        final String attribute2Description     = "Names of the properties where a difference has been found.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute2Name,
                                                                attribute2Description,
                                                                attribute2DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private EntityDef getDivergentAttachmentRelationshipAnnotationEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "5613677a-865f-474e-8044-4167fa5a31b9";
        final String name            = "DivergentAttachmentRelationshipAnnotation";
        final String description     = "Annotation documenting differences in a relationships of an attachment of acknowledged duplicates.";
        final String descriptionGUID = null;
        final String superTypeName   = "DivergentAttachmentAnnotation";

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

        final String attribute1Name            = "divergentRelationshipGUID";
        final String attribute1Description     = "Unique identifier of the relationship where a difference has been found.";
        final String attribute1DescriptionGUID = null;

        final String attribute2Name            = "divergentRelationshipPropertyNames";
        final String attribute2Description     = "Names of the properties where a difference has been found.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute2Name,
                                                                attribute2Description,
                                                                attribute2DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }
}

