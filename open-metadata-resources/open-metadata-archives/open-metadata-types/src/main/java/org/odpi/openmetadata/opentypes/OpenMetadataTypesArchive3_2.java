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

        EntityDef entityDef = archiveHelper.getDefaultEntityDef(guid,
                                                                name,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
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

        final String attribute3Name            = "level";
        final String attribute3Description     = "Deprecated attribute. Use the severityIdentifier attribute to describe the severity level of this classification.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "levelIdentifier";
        final String attribute4Description     = "Deprecated attribute. Use the severityIdentifier attribute to describe the severity level of this classification.";
        final String attribute4DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceClassificationStatus",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.STATUS_IDENTIFIER.name);
        properties.add(property);

        property = archiveHelper.getEnumTypeDefAttribute("ImpactSeverity",
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.SEVERITY_IDENTIFIER.name);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute4Name,
                                                        attribute4Description,
                                                        attribute4DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.SEVERITY_IDENTIFIER.name);
        properties.add(property);


        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STATUS_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SEVERITY_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_PROPERTY_NAME));


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
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
        TypeDefAttribute       property;

        final String attribute1Name            = "status";
        final String attribute1Description     = "Deprecated attribute. Use the statusIdentifier attribute to describe the status of this classification.";
        final String attribute1DescriptionGUID = null;

        final String attribute3Name            = "level";
        final String attribute3Description     = "Deprecated attribute. Use the levelIdentifier attribute to describe the criticality level of this classification.";
        final String attribute3DescriptionGUID = null;


        property = archiveHelper.getEnumTypeDefAttribute("GovernanceClassificationStatus",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.STATUS_IDENTIFIER.name);
        properties.add(property);
        property = archiveHelper.getEnumTypeDefAttribute("CriticalityLevel",
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.LEVEL_IDENTIFIER.name);
        properties.add(property);

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STATUS_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LEVEL_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_PROPERTY_NAME));

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
        TypeDefAttribute       property;

        final String attribute1Name            = "status";
        final String attribute1Description     = "Deprecated attribute. Use the statusIdentifier attribute to describe the status of this classification.";
        final String attribute1DescriptionGUID = null;
        final String attribute3Name            = "level";
        final String attribute3Description     = "Deprecated attribute. Use the levelIdentifier attribute to describe the confidentiality level of this classification.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceClassificationStatus",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.STATUS_IDENTIFIER.name);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute3Name,
                                                        attribute3Description,
                                                        attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.LEVEL_IDENTIFIER.name);
        properties.add(property);

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STATUS_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LEVEL_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_PROPERTY_NAME));

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
        TypeDefAttribute       property;

        final String attribute1Name            = "status";
        final String attribute1Description     = "Deprecated attribute. Use the statusIdentifier attribute to describe the status of this classification.";
        final String attribute1DescriptionGUID = null;
        final String attribute3Name            = "level";
        final String attribute3Description     = "Deprecated attribute. Use the levelIdentifier attribute to describe the confidence level of this classification.";
        final String attribute3DescriptionGUID = null;

        property = archiveHelper.getEnumTypeDefAttribute("GovernanceClassificationStatus",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.STATUS_IDENTIFIER.name);
        properties.add(property);

        property = archiveHelper.getEnumTypeDefAttribute("ConfidenceLevel",
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.LEVEL_IDENTIFIER.name);
        properties.add(property);

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STATUS_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LEVEL_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_PROPERTY_NAME));

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
        TypeDefAttribute       property;

        final String attribute1Name            = "status";
        final String attribute1Description     = "Deprecated attribute. Use the statusIdentifier attribute to describe the status of this classification.";
        final String attribute1DescriptionGUID = null;

        final String attribute3Name            = "basis";
        final String attribute3Description     = "Deprecated attribute. Use the basisIdentifier attribute to describe the retention basis of this classification.";
        final String attribute3DescriptionGUID = null;


        property = archiveHelper.getEnumTypeDefAttribute("GovernanceClassificationStatus",
                                                         attribute1Name,
                                                         attribute1Description,
                                                         attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.STATUS_IDENTIFIER.name);
        properties.add(property);

        property = archiveHelper.getEnumTypeDefAttribute("RetentionBasis",
                                                         attribute3Name,
                                                         attribute3Description,
                                                         attribute3DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);
        property.setReplacedByAttribute(OpenMetadataProperty.RETENTION_BASIS_IDENTIFIER.name);
        properties.add(property);

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STATUS_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RETENTION_BASIS_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_PROPERTY_NAME));

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
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.GOVERNANCE_EXPECTATIONS_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.COUNTS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.VALUES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FLAGS));

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

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "process";
        final String attribute2Description     = "Unique identifier of the automated process that produced this analysis.";
        final String attribute2DescriptionGUID = null;
        final String attribute4Name            = "counts";
        final String attribute4Description     = "A set of metric name to count value pairs.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "values";
        final String attribute5Description     = "A set of metric name to string value pairs.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "flags";
        final String attribute6Description     = "A set of metric name to boolean value pairs.";
        final String attribute6DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
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

    private ClassificationDef getLineageLogClassification()
    {
        final String guid            = "876e55db-27b9-4132-ad00-bbf882ea8e8a";
        final String name            = "LineageLog";
        final String description     = "A collection of related lineage log records.";
        final String descriptionGUID = null;

        ClassificationDef classificationDef = archiveHelper.getClassificationDef(guid,
                                                                                 name,
                                                                                 null,
                                                                                 description,
                                                                                 descriptionGUID,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "process";
        final String attribute2Description     = "Unique identifier of the automated process that processes this lineage log.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
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
        final String description    = "A collection of related audit log records.";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setDescription(description);

        List<TypeDefLink> linkToList = new ArrayList<>();
        linkToList.add(this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName));
        typeDefPatch.setValidEntityDefs(linkToList);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "process";
        final String attribute2Description     = "Unique identifier of the automated process that processes this audit log.";
        final String attribute2DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
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
        final String description    = "A collection of related metering log records.";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setDescription(description);

        List<TypeDefLink> linkToList = new ArrayList<>();
        linkToList.add(this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName));
        typeDefPatch.setValidEntityDefs(linkToList);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute2Name            = "process";
        final String attribute2Description     = "Unique identifier of the automated process that processes this metering log.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
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
        final String description    = "A collection of exceptions that need to be resolved";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setDescription(description);

        List<TypeDefLink> linkToList = new ArrayList<>();
        linkToList.add(this.archiveBuilder.getEntityDef(OpenMetadataType.ASSET.typeName));
        typeDefPatch.setValidEntityDefs(linkToList);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute3Name            = OpenMetadataProperty.STEWARD_TYPE_NAME.name;
        final String attribute3Description     = OpenMetadataProperty.STEWARD_TYPE_NAME.description;
        final String attribute3DescriptionGUID = OpenMetadataProperty.STEWARD_TYPE_NAME.descriptionGUID;
        final String attribute4Name            = OpenMetadataProperty.STEWARD_PROPERTY_NAME.name;
        final String attribute4Description     = OpenMetadataProperty.STEWARD_PROPERTY_NAME.description;
        final String attribute4DescriptionGUID = OpenMetadataProperty.STEWARD_PROPERTY_NAME.descriptionGUID;
        final String attribute5Name            = "process";
        final String attribute5Description     = "Unique identifier of the automated process that processes this exception backlog.";
        final String attribute5DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.NOTES.name,
                                                           OpenMetadataProperty.NOTES.description,
                                                           OpenMetadataProperty.NOTES.descriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.STEWARD.name,
                                                           OpenMetadataProperty.STEWARD.description,
                                                           OpenMetadataProperty.STEWARD.descriptionGUID);
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
        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.SOURCE.name,
                                                           OpenMetadataProperty.SOURCE.description,
                                                           OpenMetadataProperty.SOURCE.descriptionGUID);
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
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.CONSOLIDATED_DUPLICATE_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STATUS_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_PROPERTY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SOURCE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NOTES));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    private RelationshipDef addPeerDuplicateLinkRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.PEER_DUPLICATE_LINK,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "peerDuplicateOrigin";
        final String                     end1AttributeDescription     = "Oldest element.";
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
        final String                     end2AttributeName            = "peerDuplicatePartner";
        final String                     end2AttributeDescription     = "Newest element.";
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

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STATUS_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_PROPERTY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SOURCE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NOTES));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef addConsolidatedDuplicateLinkRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "consolidatedDuplicateOrigin";
        final String                     end1AttributeDescription     = "Detected duplicate element - the source of the properties.";
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
        final String                     end2AttributeName            = "consolidatedDuplicateResult";
        final String                     end2AttributeDescription     = "Element resulting from combining the duplicate entities.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
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

