/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationPropagationRule;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttributeStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefStatus;
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
 * <a href="https://egeria-project.org/types/">The Open Metadata Type System</a>
 * </p>
 * <p>
 * There are 8 areas, each covering a different topic area of metadata.  The module breaks down the process of creating
 * the models into the areas and then the individual models to simplify the maintenance of this class
 * </p>
 */
public class OpenMetadataTypesArchive3_2
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "3.2";
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
    public OpenMetadataTypesArchive3_2()
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
    public OpenMetadataTypesArchive3_2(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive3_1 previousTypes = new OpenMetadataTypesArchive3_1(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update0030HostsAndOperatingPlatforms();
        update0421GovernanceClassificationLevels();
        update0422GovernanceActionClassifications();
        update0450GovernanceRollout();
        update0455ExceptionManagement();
        update0465DuplicateProcessing();
        update0620DataProfiling();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Add support for a software archive.
     */
    private void update0030HostsAndOperatingPlatforms()
    {
        this.archiveBuilder.addEntityDef(addSoftwareArchiveEntity());
    }


    private EntityDef addSoftwareArchiveEntity()
    {
        final String guid = "4c4bfc3f-1374-4e4c-a76d-c8e82b2cafaa";

        final String name            = "SoftwareArchive";
        final String description     = "A collection of runnable software components.";
        final String descriptionGUID = null;

        final String superTypeName = "Collection";

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
     * Add support for configurable governance status levels.
     */
    private void update0421GovernanceClassificationLevels()
    {
        this.archiveBuilder.addEntityDef(addGovernanceStatusLevelEntity());
        this.archiveBuilder.addClassificationDef(addGovernanceStatusSetClassification());
    }


    private EntityDef addGovernanceStatusLevelEntity()
    {
        final String guid            = "a518de03-0f72-4944-9cd5-e05b43ae9c5e";
        final String name            = "GovernanceStatusLevel";
        final String description     = "A value to represent a specific level of status in a governance element.";
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
        final String attribute2Name            = OpenMetadataProperty.DISPLAY_NAME.name;
        final String attribute2Description     = OpenMetadataProperty.DISPLAY_NAME.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.DISPLAY_NAME.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.DESCRIPTION.name;
        final String attribute3Description     = OpenMetadataProperty.DESCRIPTION.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.DESCRIPTION.descriptionGUID;

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

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private ClassificationDef addGovernanceStatusSetClassification()
    {
        final String guid = "c13261bb-0cfe-4540-a44a-cca2b14f412b";

        final String name            = "GovernanceStatusSet";
        final String description     = "Identifies the set of levels that are used to describe the status of a governance element.";
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


        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Deprecate the use of enums in the governance action classifications.
     */
    private void update0422GovernanceActionClassifications()
    {
        this.archiveBuilder.addTypeDefPatch(updateImpactClassification());
        this.archiveBuilder.addTypeDefPatch(updateCriticalityClassification());
        this.archiveBuilder.addTypeDefPatch(updateConfidentialityClassification());
        this.archiveBuilder.addTypeDefPatch(updateConfidenceClassification());
        this.archiveBuilder.addTypeDefPatch(updateRetentionClassification());
    }

    private TypeDefPatch updateImpactClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName = "Impact";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "status";
        final String attribute1Description     = "Deprecated attribute. Use the statusIdentifier attribute to describe the status of this classification.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "statusIdentifier";
        final String attribute2Description     = "Description of the status of this classification.  Values defined by GovernanceStatusLevel.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "level";
        final String attribute3Description     = "Deprecated attribute. Use the severityIdentifier attribute to describe the severity level of this classification.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "levelIdentifier";
        final String attribute4Description     = "Deprecated attribute. Use the severityIdentifier attribute to describe the severity level of this classification.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "severityIdentifier";
        final String attribute5Description     = "Defined level of severity for this classification. Values defined by GovernanceClassificationLevel.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = OpenMetadataProperty.STEWARD_TYPE_NAME.name;
        final String attribute6Description     = OpenMetadataProperty.STEWARD_TYPE_NAME.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.STEWARD_TYPE_NAME.descriptionGUID;
        final String attribute7Name            = OpenMetadataProperty.STEWARD_PROPERTY_NAME.name;
        final String attribute7Description     = OpenMetadataProperty.STEWARD_PROPERTY_NAME.description;
        final String attribute7DescriptionGUID = OpenMetadataProperty.STEWARD_PROPERTY_NAME.descriptionGUID;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceClassificationStatus",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute2Name);
        properties.add(property);

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("ImpactSeverity",
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute5Name);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute4Name,
                                                        attribute4Description,
                                                        attribute4DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute5Name);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute5Name,
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

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
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

        final String attribute1Name            = "status";
        final String attribute1Description     = "Deprecated attribute. Use the statusIdentifier attribute to describe the status of this classification.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "statusIdentifier";
        final String attribute2Description     = "Description of the status of this classification.  Values defined by GovernanceStatusLevel.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "level";
        final String attribute3Description     = "Deprecated attribute. Use the levelIdentifier attribute to describe the criticality level of this classification.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "levelIdentifier";
        final String attribute4Description     = "Defined criticality level for this classification. Values defined by GovernanceClassificationLevel.";
        final String attribute4DescriptionGUID = null;
        final String attribute6Name            = OpenMetadataProperty.STEWARD_TYPE_NAME.name;
        final String attribute6Description     = OpenMetadataProperty.STEWARD_TYPE_NAME.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.STEWARD_TYPE_NAME.descriptionGUID;
        final String attribute7Name            = OpenMetadataProperty.STEWARD_PROPERTY_NAME.name;
        final String attribute7Description     = OpenMetadataProperty.STEWARD_PROPERTY_NAME.description;
        final String attribute7DescriptionGUID = OpenMetadataProperty.STEWARD_PROPERTY_NAME.descriptionGUID;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceClassificationStatus",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute2Name);
        properties.add(property);

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("CriticalityLevel",
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute4Name);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute4Name,
                                                        attribute4Description,
                                                        attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
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

        final String attribute1Name            = "status";
        final String attribute1Description     = "Deprecated attribute. Use the statusIdentifier attribute to describe the status of this classification.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "statusIdentifier";
        final String attribute2Description     = "Description of the status of this classification.  Values defined by GovernanceStatusLevel.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "level";
        final String attribute3Description     = "Deprecated attribute. Use the levelIdentifier attribute to describe the confidentiality level of this classification.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "levelIdentifier";
        final String attribute4Description     = "Defined confidentiality level for this classification. Values defined by GovernanceClassificationLevel.";
        final String attribute4DescriptionGUID = null;
        final String attribute6Name            = OpenMetadataProperty.STEWARD_TYPE_NAME.name;
        final String attribute6Description     = OpenMetadataProperty.STEWARD_TYPE_NAME.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.STEWARD_TYPE_NAME.descriptionGUID;
        final String attribute7Name            = OpenMetadataProperty.STEWARD_PROPERTY_NAME.name;
        final String attribute7Description     = OpenMetadataProperty.STEWARD_PROPERTY_NAME.description;
        final String attribute7DescriptionGUID = OpenMetadataProperty.STEWARD_PROPERTY_NAME.descriptionGUID;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceClassificationStatus",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute2Name);
        properties.add(property);

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute3Name,
                                                        attribute3Description,
                                                        attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute4Name);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute4Name,
                                                        attribute4Description,
                                                        attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
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

        final String attribute1Name            = "status";
        final String attribute1Description     = "Deprecated attribute. Use the statusIdentifier attribute to describe the status of this classification.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "statusIdentifier";
        final String attribute2Description     = "Description of the status of this classification.  Values defined by GovernanceStatusLevel.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "level";
        final String attribute3Description     = "Deprecated attribute. Use the levelIdentifier attribute to describe the confidence level of this classification.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "levelIdentifier";
        final String attribute4Description     = "Defined confidence level for this classification. Values defined by GovernanceClassificationLevel.";
        final String attribute4DescriptionGUID = null;
        final String attribute6Name            = OpenMetadataProperty.STEWARD_TYPE_NAME.name;
        final String attribute6Description     = OpenMetadataProperty.STEWARD_TYPE_NAME.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.STEWARD_TYPE_NAME.descriptionGUID;
        final String attribute7Name            = OpenMetadataProperty.STEWARD_PROPERTY_NAME.name;
        final String attribute7Description     = OpenMetadataProperty.STEWARD_PROPERTY_NAME.description;
        final String attribute7DescriptionGUID = OpenMetadataProperty.STEWARD_PROPERTY_NAME.descriptionGUID;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceClassificationStatus",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute2Name);
        properties.add(property);

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("ConfidenceLevel",
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute4Name);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute4Name,
                                                        attribute4Description,
                                                        attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
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

        final String attribute1Name            = "status";
        final String attribute1Description     = "Deprecated attribute. Use the statusIdentifier attribute to describe the status of this classification.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "statusIdentifier";
        final String attribute2Description     = "Description of the status of this classification.  Values defined by GovernanceStatusLevel.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "basis";
        final String attribute3Description     = "Deprecated attribute. Use the basisIdentifier attribute to describe the retention basis of this classification.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "basisIdentifier";
        final String attribute4Description     = "Defined retention basis for this classification. Values defined by GovernanceClassificationLevel.";
        final String attribute4DescriptionGUID = null;
        final String attribute6Name            = OpenMetadataProperty.STEWARD_TYPE_NAME.name;
        final String attribute6Description     = OpenMetadataProperty.STEWARD_TYPE_NAME.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.STEWARD_TYPE_NAME.descriptionGUID;
        final String attribute7Name            = OpenMetadataProperty.STEWARD_PROPERTY_NAME.name;
        final String attribute7Description     = OpenMetadataProperty.STEWARD_PROPERTY_NAME.description;
        final String attribute7DescriptionGUID = OpenMetadataProperty.STEWARD_PROPERTY_NAME.descriptionGUID;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceClassificationStatus",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute2Name);
        properties.add(property);

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
                                                        attribute2Description,
                                                        attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("RetentionBasis",
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(attribute4Name);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute4Name,
                                                        attribute4Description,
                                                        attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute6Name,
                                                           attribute6Description,
                                                           attribute6DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute7Name,
                                                           attribute7Description,
                                                           attribute7DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add GovernanceExpectations classification for Referenceables.
     */
    private void update0450GovernanceRollout()
    {
        this.archiveBuilder.addClassificationDef(getGovernanceExpectationsClassification());
    }

    private ClassificationDef getGovernanceExpectationsClassification()
    {
        final String guid            = "fcda7261-865d-464d-b279-7d9880aaab39";
        final String name            = "GovernanceExpectations";
        final String description     = "A set of expectation values on the performance and use of the connected resource.";
        final String descriptionGUID = null;

        final String linkedToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "counts";
        final String attribute1Description     = "A set of metric name to count value pairs.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "values";
        final String attribute2Description     = "A set of metric name to string value pairs.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "flags";
        final String attribute3Description     = "A set of metric name to boolean value pairs.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getMapStringIntTypeDefAttribute(attribute1Name,
                                                                 attribute1Description,
                                                                 attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute2Name,
                                                                    attribute2Description,
                                                                    attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringBooleanTypeDefAttribute(attribute3Name,
                                                                     attribute3Description,
                                                                     attribute3DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add support for lineage logs and their analysis.
     */
    private void update0455ExceptionManagement()
    {
        this.archiveBuilder.addClassificationDef(getLogAnalysisClassification());
        this.archiveBuilder.addClassificationDef(getMeteringLogFileClassification());
        this.archiveBuilder.addClassificationDef(getLineageLogFileClassification());
        this.archiveBuilder.addClassificationDef(getLineageLogClassification());

        this.archiveBuilder.addTypeDefPatch(updateExceptionBacklogClassification());
        this.archiveBuilder.addTypeDefPatch(updateMeteringLogClassification());
        this.archiveBuilder.addTypeDefPatch(updateAuditLogClassification());
    }


    private ClassificationDef getLogAnalysisClassification()
    {
        final String guid            = "38cf214c-244d-435c-a328-251026356e6b";
        final String name            = "LogAnalysis";
        final String description     = "A set of results from the analysis of a log record - or collection of log records.";
        final String descriptionGUID = null;

        final String linkedToEntity = OpenMetadataType.ASSET.typeName;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.NOTES.name;
        final String attribute1Description     = OpenMetadataProperty.NOTES.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.NOTES.descriptionGUID;
        final String attribute2Name            = "process";
        final String attribute2Description     = "Unique identifier of the automated process that produced this analysis.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = OpenMetadataProperty.SOURCE.name;
        final String attribute3Description     = OpenMetadataProperty.SOURCE.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.SOURCE.descriptionGUID;
        final String attribute4Name            = "counts";
        final String attribute4Description     = "A set of metric name to count value pairs.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "values";
        final String attribute5Description     = "A set of metric name to string value pairs.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "flags";
        final String attribute6Description     = "A set of metric name to boolean value pairs.";
        final String attribute6DescriptionGUID = null;

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
        property = archiveHelper.getMapStringIntTypeDefAttribute(attribute4Name,
                                                                 attribute4Description,
                                                                 attribute4DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute5Name,
                                                                    attribute5Description,
                                                                    attribute5DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringBooleanTypeDefAttribute(attribute6Name,
                                                                     attribute6Description,
                                                                     attribute6DescriptionGUID);
        properties.add(property);

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private ClassificationDef getMeteringLogFileClassification()
    {
        final String guid            = "5ceb0c07-4271-4910-9e24-b0894f395d93";
        final String name            = "MeteringLogFile";
        final String description     = "A data file containing resource use events.";
        final String descriptionGUID = null;

        final String linkedToEntity = OpenMetadataType.DATA_FILE.typeName;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }

    private ClassificationDef getLineageLogFileClassification()
    {
        final String guid            = "9992758d-d7dd-432d-b84e-62fe007a6364";
        final String name            = "LineageLogFile";
        final String description     = "A data file containing operational lineage events.";
        final String descriptionGUID = null;

        final String linkedToEntity = OpenMetadataType.DATA_FILE.typeName;

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }

    private ClassificationDef getLineageLogClassification()
    {
        final String guid            = "876e55db-27b9-4132-ad00-bbf882ea8e8a";
        final String name            = "LineageLog";
        final String description     = "A collection of related lineage log records.";
        final String descriptionGUID = null;

        final String linkedToEntity = OpenMetadataType.ASSET.typeName;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.NOTES.name;
        final String attribute1Description     = OpenMetadataProperty.NOTES.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.NOTES.descriptionGUID;
        final String attribute2Name            = "process";
        final String attribute2Description     = "Unique identifier of the automated process that processes this lineage log.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = OpenMetadataProperty.SOURCE.name;
        final String attribute3Description     = OpenMetadataProperty.SOURCE.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.SOURCE.descriptionGUID;

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

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }

    private TypeDefPatch updateAuditLogClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName       = "AuditLog";
        final String linkedToEntity = OpenMetadataType.ASSET.typeName; // Current value is DataSet
        final String description    = "A collection of related audit log records.";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setDescription(description);

        List<TypeDefLink> linkToList = new ArrayList<>();
        linkToList.add(this.archiveBuilder.getEntityDef(linkedToEntity));
        typeDefPatch.setValidEntityDefs(linkToList);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.NOTES.name;
        final String attribute1Description     = OpenMetadataProperty.NOTES.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.NOTES.descriptionGUID;
        final String attribute2Name            = "process";
        final String attribute2Description     = "Unique identifier of the automated process that processes this audit log.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = OpenMetadataProperty.SOURCE.name;
        final String attribute3Description     = OpenMetadataProperty.SOURCE.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.SOURCE.descriptionGUID;

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

    private TypeDefPatch updateMeteringLogClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName       = "MeteringLog";
        final String linkedToEntity = OpenMetadataType.ASSET.typeName; // Current value is DataSet
        final String description    = "A collection of related metering log records.";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setDescription(description);

        List<TypeDefLink> linkToList = new ArrayList<>();
        linkToList.add(this.archiveBuilder.getEntityDef(linkedToEntity));
        typeDefPatch.setValidEntityDefs(linkToList);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.NOTES.name;
        final String attribute1Description     = OpenMetadataProperty.NOTES.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.NOTES.descriptionGUID;
        final String attribute2Name            = "process";
        final String attribute2Description     = "Unique identifier of the automated process that processes this metering log.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = OpenMetadataProperty.SOURCE.name;
        final String attribute3Description     = OpenMetadataProperty.SOURCE.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.SOURCE.descriptionGUID;

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

    private TypeDefPatch updateExceptionBacklogClassification()
    {
        /*
         * Create the Patch
         */
        final String typeName       = "ExceptionBacklog";
        final String linkedToEntity = OpenMetadataType.ASSET.typeName; // Current value is DataSet
        final String description    = "A collection of exceptions that need to be resolved";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setDescription(description);

        List<TypeDefLink> linkToList = new ArrayList<>();
        linkToList.add(this.archiveBuilder.getEntityDef(linkedToEntity));
        typeDefPatch.setValidEntityDefs(linkToList);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.NOTES.name;
        final String attribute1Description     = OpenMetadataProperty.NOTES.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.NOTES.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.STEWARD.name;
        final String attribute2Description     = OpenMetadataProperty.STEWARD.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.STEWARD.descriptionGUID;
        final String attribute3Name            = OpenMetadataProperty.STEWARD_TYPE_NAME.name;
        final String attribute3Description     = OpenMetadataProperty.STEWARD_TYPE_NAME.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.STEWARD_TYPE_NAME.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.STEWARD_PROPERTY_NAME.name;
        final String attribute4Description     = OpenMetadataProperty.STEWARD_PROPERTY_NAME.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.STEWARD_PROPERTY_NAME.descriptionGUID;
        final String attribute5Name            = "process";
        final String attribute5Description     = "Unique identifier of the automated process that processes this exception backlog.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = OpenMetadataProperty.SOURCE.name;
        final String attribute6Description     = OpenMetadataProperty.SOURCE.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.SOURCE.descriptionGUID;

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

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    
    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Update the duplicate detection relationships.
     */
    private void update0465DuplicateProcessing()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateKnownDuplicateLink());
        this.archiveBuilder.addClassificationDef(addConsolidatedDuplicateClassification());
        this.archiveBuilder.addRelationshipDef(addPeerDuplicateLinkRelationship());
        this.archiveBuilder.addRelationshipDef(addConsolidatedDuplicateLinkRelationship());
    }


    private TypeDefPatch deprecateKnownDuplicateLink()
    {
        final String typeName = "KnownDuplicateLink";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private ClassificationDef addConsolidatedDuplicateClassification()
    {
        final String guid            = "e40e80d7-5a29-482c-9a88-0dc7251f08de";
        final String name            = "ConsolidatedDuplicate";
        final String description     = "An element that has be formed by combining the properties, classifications and relationships from multiple duplicate entities.";
        final String descriptionGUID = null;

        final String linkedToEntity = OpenMetadataType.REFERENCEABLE.typeName;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(linkedToEntity),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "statusIdentifier";
        final String attribute2Description     = "Status of the consolidated entity. Value defined by GovernanceClassificationLevel.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = OpenMetadataProperty.STEWARD.name;
        final String attribute3Description     = OpenMetadataProperty.STEWARD.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.STEWARD.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.STEWARD_TYPE_NAME.name;
        final String attribute4Description     = OpenMetadataProperty.STEWARD_TYPE_NAME.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.STEWARD_TYPE_NAME.descriptionGUID;
        final String attribute5Name            = OpenMetadataProperty.STEWARD_PROPERTY_NAME.name;
        final String attribute5Description     = OpenMetadataProperty.STEWARD_PROPERTY_NAME.description;
        final String attribute5DescriptionGUID = OpenMetadataProperty.STEWARD_PROPERTY_NAME.descriptionGUID;
        final String attribute6Name            = OpenMetadataProperty.SOURCE.name;
        final String attribute6Description     = OpenMetadataProperty.SOURCE.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.SOURCE.descriptionGUID;
        final String attribute7Name            = OpenMetadataProperty.NOTES.name;
        final String attribute7Description     = OpenMetadataProperty.NOTES.description;
        final String attribute7DescriptionGUID = OpenMetadataProperty.NOTES.descriptionGUID;

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
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

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private RelationshipDef addPeerDuplicateLinkRelationship()
    {
        final String guid            = "a94b2929-9e62-4b12-98ab-8ac45691e5bd";
        final String name            = "PeerDuplicateLink";
        final String description     = "Link between detected duplicate entities.";
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
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "peerDuplicateOrigin";
        final String                     end1AttributeDescription     = "Oldest element.";
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
        final String                     end2AttributeName            = "peerDuplicatePartner";
        final String                     end2AttributeDescription     = "Newest element.";
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

        final String attribute2Name            = "statusIdentifier";
        final String attribute2Description     = "Status of the duplicate processing. Value defined by GovernanceClassificationLevel.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = OpenMetadataProperty.STEWARD.name;
        final String attribute3Description     = OpenMetadataProperty.STEWARD.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.STEWARD.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.STEWARD_TYPE_NAME.name;
        final String attribute4Description     = OpenMetadataProperty.STEWARD_TYPE_NAME.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.STEWARD_TYPE_NAME.descriptionGUID;
        final String attribute5Name            = OpenMetadataProperty.STEWARD_PROPERTY_NAME.name;
        final String attribute5Description     = OpenMetadataProperty.STEWARD_PROPERTY_NAME.description;
        final String attribute5DescriptionGUID = OpenMetadataProperty.STEWARD_PROPERTY_NAME.descriptionGUID;
        final String attribute6Name            = OpenMetadataProperty.SOURCE.name;
        final String attribute6Description     = OpenMetadataProperty.SOURCE.description;
        final String attribute6DescriptionGUID = OpenMetadataProperty.SOURCE.descriptionGUID;
        final String attribute7Name            = OpenMetadataProperty.NOTES.name;
        final String attribute7Description     = OpenMetadataProperty.NOTES.description;
        final String attribute7DescriptionGUID = OpenMetadataProperty.NOTES.descriptionGUID;

        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
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

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef addConsolidatedDuplicateLinkRelationship()
    {
        final String guid            = "a1fabffd-d6ec-4b2d-bfe4-646f27c07c82";
        final String name            = "ConsolidatedDuplicateLink";
        final String description     = "Link between a detected duplicate entity and an entity that contains the combined values of this entity and its other duplicates.";
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
        final String                     end1EntityType               = OpenMetadataType.REFERENCEABLE.typeName;
        final String                     end1AttributeName            = "consolidatedDuplicateOrigin";
        final String                     end1AttributeDescription     = "Detected duplicate element - the source of the properties.";
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
        final String                     end2AttributeName            = "consolidatedDuplicateResult";
        final String                     end2AttributeDescription     = "Element resulting from combining the duplicate entities.";
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


    /**
     * Add the fingerprint annotation used for de-duplicating assets with the same content.
     */
    private void update0620DataProfiling()
    {
        this.archiveBuilder.addEntityDef(addFingerprintAnnotationEntity());
    }


    private EntityDef addFingerprintAnnotationEntity()
    {
        final String guid = "b3adca2a-ce66-4b29-bf2e-7406ada8ab49";

        final String name            = "FingerprintAnnotation";
        final String description     = "An annotation capturing asset fingerprint information.";
        final String descriptionGUID = null;

        final String superTypeName = "DataFieldAnnotation";

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

        final String attribute1Name            = "fingerprint";
        final String attribute1Description     = "A string value that represents the content of the asset.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "hash";
        final String attribute2Description     = "An integer value that represents the content of the asset.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "fingerprintAlgorithm";
        final String attribute3Description     = "The algorithm use to generate either the fingerprint.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "hashAlgorithm";
        final String attribute4Description     = "The algorithm use to generate either the hash.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
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

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */
}

