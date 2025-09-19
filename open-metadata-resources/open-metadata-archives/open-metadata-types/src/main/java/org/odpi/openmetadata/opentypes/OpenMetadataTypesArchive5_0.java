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
public class OpenMetadataTypesArchive5_0
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "5.0";
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
    public OpenMetadataTypesArchive5_0()
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
    public OpenMetadataTypesArchive5_0(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive4_3 previousTypes = new OpenMetadataTypesArchive4_3(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Add the type updates
         */
        update0010BaseModel();
        update0011UpdateTemplates();
        update0021Collections();
        update0050ApplicationsAndProcesses();
        update0130Projects();
        update0210DataStores();
        update0220DataFiles();
        update0221MediaFiles();
        add0226ArchiveFiles();
        update0280SoftwareArtifacts();
        update0461GovernanceEngines();
        update0464IntegrationGroups();
        update0545ReferenceData();
        update0462GovernanceActionTypes();
        add00475ContextEvents();
        add0603SurveyReports();
        update0690RequestForAction();
        add0755UltimateSourcesDestinations();
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0010BaseModel()
    {
        this.archiveBuilder.addTypeDefPatch(updateAnchors());
    }

    private TypeDefPatch updateAnchors()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ANCHOR_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ANCHOR_DOMAIN_NAME));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0021Collections()
    {
        this.archiveBuilder.addClassificationDef(getRootCollectionClassification());
        this.archiveBuilder.addClassificationDef(getHomeCollectionClassification());
        this.archiveBuilder.addClassificationDef(getRecentAccessClassification());
        this.archiveBuilder.addClassificationDef(getWorkItemListClassification());
    }


    private ClassificationDef getRootCollectionClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.ROOT_COLLECTION_CLASSIFICATION,
                                                  this.archiveBuilder.getClassificationDef(OpenMetadataType.COLLECTION_ROLE_CLASSIFICATION.typeName),
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName),
                                                  false);
    }

    private ClassificationDef getHomeCollectionClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.HOME_COLLECTION_CLASSIFICATION,
                                                  this.archiveBuilder.getClassificationDef(OpenMetadataType.COLLECTION_ROLE_CLASSIFICATION.typeName),
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName),
                                                  false);
    }


    private ClassificationDef getRecentAccessClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.RECENT_ACCESS_COLLECTION_CLASSIFICATION,
                                                  this.archiveBuilder.getClassificationDef(OpenMetadataType.COLLECTION_ROLE_CLASSIFICATION.typeName),
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName),
                                                  false);
    }


    private ClassificationDef getWorkItemListClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.WORK_ITEM_LIST_COLLECTION_CLASSIFICATION,
                                                  this.archiveBuilder.getClassificationDef(OpenMetadataType.COLLECTION_ROLE_CLASSIFICATION.typeName),
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName),
                                                  false);
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add more software server capabilities
     */
    private void update0050ApplicationsAndProcesses()
    {
        this.archiveBuilder.addEntityDef(addEventManagerEntity());
        this.archiveBuilder.addEntityDef(addAuthorizationManagerEntity());
        this.archiveBuilder.addEntityDef(addUserAuthenticationManagerEntity());
    }


    /**
     * This new subtype of software server capability for event managers.
     *
     * @return entity definition
     */
    private EntityDef addEventManagerEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.EVENT_MANAGER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_CAPABILITY.typeName));
    }


    /**
     * This new subtype of software server capability for event managers.
     *
     * @return entity definition
     */
    private EntityDef addAuthorizationManagerEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.AUTHORIZATION_MANAGER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_CAPABILITY.typeName));
    }


    /**
     * This new subtype of software server capability for event managers.
     *
     * @return entity definition
     */
    private EntityDef addUserAuthenticationManagerEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.USER_AUTHENTICATION_MANAGER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_CAPABILITY.typeName));
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * update0130Projects
     */
    private void update0130Projects()
    {
        this.archiveBuilder.addTypeDefPatch(updateProject());
        this.archiveBuilder.addClassificationDef(getPersonalProjectClassification());
        this.archiveBuilder.addClassificationDef(getStudyProjectClassification());
    }


    private TypeDefPatch updateProject()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.PROJECT.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PROJECT_HEALTH));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PRIORITY));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PROJECT_PHASE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MISSION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PURPOSES));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * Add PersonalProjectProperties classification to allow people to use projects to organize their work.
     *
     * @return classification def
     */
    private ClassificationDef getPersonalProjectClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.PERSONAL_PROJECT_CLASSIFICATION,
                                                  this.archiveBuilder.getClassificationDef(OpenMetadataType.PROJECT_ROLE_CLASSIFICATION.typeName),
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.PROJECT.typeName),
                                                  false);
    }



    /**
     * Add StudyProject classification to allow specific types of investigations to be characterized.
     *
     * @return classification def
     */
    private ClassificationDef getStudyProjectClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.STUDY_PROJECT_CLASSIFICATION,
                                                  this.archiveBuilder.getClassificationDef(OpenMetadataType.PROJECT_ROLE_CLASSIFICATION.typeName),
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.PROJECT.typeName),
                                                  false);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * update0011UpdateTemplates
     */
    private void update0011UpdateTemplates()
    {
        this.archiveBuilder.addRelationshipDef(addCatalogTemplateRelationship());
        this.archiveBuilder.addTypeDefPatch(updateTemplateClassification());
    }


    /**
     * Add CatalogTemplate
     *
     * @return relationship def
     */
    private RelationshipDef addCatalogTemplateRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "implementationTypes";
        final String                     end1AttributeDescription     = "Description of a type of technology.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.OPEN_METADATA_ROOT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "templatesForCataloguing";
        final String                     end2AttributeDescription     = "Template element for a new catalog entry.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.OPEN_METADATA_ROOT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;

    }


    /**
     * Add new properties to TemplateClassification to describe the replacement properties and the
     * placeholder properties.
     *
     * @return  patch
     */
    private TypeDefPatch updateTemplateClassification()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.VERSION_IDENTIFIER));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Update 0210 DataStores model with new DataScope classification.
     */
    private void update0210DataStores()
    {
        this.archiveBuilder.addClassificationDef(getDataScopeClassification());
    }


    /**
     * Add the DataScope classification to allow details of the scope of data represented by the attached element.
     *
     * @return classification def
     */
    private ClassificationDef getDataScopeClassification()
    {
        ClassificationDef classificationDef = archiveHelper.getClassificationDef(OpenMetadataType.DATA_SCOPE_CLASSIFICATION,
                                                                                 null,
                                                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                                 false);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MIN_LONGITUDE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MIN_LATITUDE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MAX_LONGITUDE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MAX_LATITUDE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MIN_HEIGHT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.MAX_HEIGHT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_COLLECTION_START_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DATA_COLLECTION_END_TIME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ADDITIONAL_PROPERTIES));

        classificationDef.setPropertiesDefinition(properties);

        return classificationDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0220DataFiles()
    {
        this.archiveBuilder.addTypeDefPatch(updateDataFile());
        this.archiveBuilder.addEntityDef(addXMLFileEntity());
        this.archiveBuilder.addEntityDef(addSpreadsheetFileEntity());
    }


    /**
     * Update DataFile with an explicit property for fileExtension.  Prior to this change the file extension
     * was stored in the fileType property.  The fileType property is now a logical fileType.
     *
     * @return type def patch
     */
    private TypeDefPatch updateDataFile()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.DATA_FILE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.FILE_EXTENSION));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /**
     * Add SpreadsheetFile
     *
     * @return entity def
     */
    private EntityDef addSpreadsheetFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SPREADSHEET_FILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName));
    }


    /**
     * Add XMLFile
     *
     * @return entity def
     */
    private EntityDef addXMLFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.XML_FILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Update 0221 Media Files models with new types of files
     */
    private void update0221MediaFiles()
    {
        this.archiveBuilder.addEntityDef(addAudioFileEntity());
        this.archiveBuilder.addEntityDef(addVideoFileEntity());
        this.archiveBuilder.addEntityDef(add3DImageFileEntity());
        this.archiveBuilder.addEntityDef(addRasterFileEntity());
        this.archiveBuilder.addEntityDef(addVectorFileEntity());
    }


    /**
     * Add AudioFile.
     *
     * @return entity def
     */
    private EntityDef addAudioFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.AUDIO_FILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.MEDIA_FILE.typeName));
    }


    /**
     * Add VideoFile.
     *
     * @return entity def
     */
    private EntityDef addVideoFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.VIDEO_FILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.MEDIA_FILE.typeName));
    }


    /**
     * Add 3DImageFile.
     *
     * @return entity def
     */
    private EntityDef add3DImageFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.THREE_D_IMAGE_FILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.MEDIA_FILE.typeName));
    }


    /**
     * Add RasterFile.
     *
     * @return entity def
     */
    private EntityDef addRasterFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.RASTER_FILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.MEDIA_FILE.typeName));
    }


    /**
     * Add VectorFile
     *
     * @return entity def
     */
    private EntityDef addVectorFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.VECTOR_FILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.MEDIA_FILE.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Add new model 0226 ArchiveFiles
     */
    private void add0226ArchiveFiles()
    {
        this.archiveBuilder.addEntityDef(addArchiveFileEntity());
        this.archiveBuilder.addRelationshipDef(addArchiveContentsRelationship());
    }


    /**
     * Add ArchiveFile
     *
     * @return entity def
     */
    private EntityDef addArchiveFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.ARCHIVE_FILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName));
    }


    /**
     * Add ArchiveContents
     *
     * @return relationship def
     */
    private RelationshipDef addArchiveContentsRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ARCHIVE_CONTENTS_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "packagedInArchiveFiles";
        final String                     end1AttributeDescription     = "Associated archive file.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ARCHIVE_FILE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "archiveFileContents";
        final String                     end2AttributeDescription     = "Collection describing the archive's contents.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName),
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

    private void update0280SoftwareArtifacts()
    {
        this.archiveBuilder.addEntityDef(addSourceCodeFileEntity());
        this.archiveBuilder.addEntityDef(addBuildInstructionFileEntity());
        this.archiveBuilder.addEntityDef(addExecutableFileEntity());
        this.archiveBuilder.addEntityDef(addScriptFileEntity());
        this.archiveBuilder.addEntityDef(addPropertiesFileEntity());
        this.archiveBuilder.addEntityDef(addYAMLFileEntity());
    }


    /**
     * Add SourceCodeFile
     *
     * @return entity def
     */
    private EntityDef addSourceCodeFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SOURCE_CODE_FILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName));
    }


    /**
     * Add BuildInstructionFile
     *
     * @return entity def
     */
    private EntityDef addBuildInstructionFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.BUILD_INSTRUCTION_FILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName));
    }


    /**
     * Add ExecutableFile
     *
     * @return entity def
     */
    private EntityDef addExecutableFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.EXECUTABLE_FILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName));
    }


    /**
     * Add ScriptFile
     *
     * @return entity def
     */
    private EntityDef addScriptFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SCRIPT_FILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName));
    }


    /**
     * Add PropertiesFile
     *
     * @return entity def
     */
    private EntityDef addPropertiesFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.PROPERTIES_FILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_FILE.typeName));
    }


    /**
     * Add YAMLFile
     *
     * @return entity def
     */
    private EntityDef addYAMLFileEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.YAML_FILE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.PROPERTIES_FILE.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0461GovernanceEngines()
    {
        this.archiveBuilder.addEntityDef(getSurveyActionEngineEntity());
        this.archiveBuilder.addEntityDef(getSurveyActionServiceEntity());
    }

    private EntityDef getSurveyActionEngineEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SURVEY_ACTION_ENGINE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_ENGINE.typeName));
    }

    private EntityDef getSurveyActionServiceEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.SURVEY_ACTION_SERVICE,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_SERVICE.typeName));
    }




    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0464IntegrationGroups()
    {
        this.archiveBuilder.addTypeDefPatch(updateCatalogTarget());
    }

    /**
     * Add configurationProperties
     *
     * @return patch
     */
    private TypeDefPatch updateCatalogTarget()
    {
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TEMPLATES));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONNECTION_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.METADATA_SOURCE_QUALIFIED_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONFIGURATION_PROPERTIES));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add00475ContextEvents()
    {
        this.archiveBuilder.addEntityDef(addContextEventEntity());
        this.archiveBuilder.addRelationshipDef(addContextEventEvidenceRelationship());
        this.archiveBuilder.addRelationshipDef(addContextEventForTimelineEffectsRelationship());
        this.archiveBuilder.addRelationshipDef(addContextEventImpactRelationship());
        this.archiveBuilder.addRelationshipDef(addDependentContextEventRelationship());
        this.archiveBuilder.addRelationshipDef(addRelatedContextEventRelationship());
        this.archiveBuilder.addClassificationDef(getContextEventCollectionClassification());
    }

    private EntityDef addContextEventEntity()
    {
        /*
         * Build the Entity
         */
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.CONTEXT_EVENT,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EVENT_EFFECT));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PLANNED_START_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTUAL_START_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PLANNED_DURATION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTUAL_DURATION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REPEAT_INTERVAL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PLANNED_COMPLETION_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTUAL_COMPLETION_DATE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REFERENCE_EFFECTIVE_FROM));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.REFERENCE_EFFECTIVE_TO));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef addContextEventEvidenceRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CONTEXT_EVENT_EVIDENCE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "relatedToContextEvents";
        final String                     end1AttributeDescription     = "Description of a related event.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONTEXT_EVENT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "contextEventEvidence";
        final String                     end2AttributeDescription     = "Descriptions of notifications, incidents and other indications that the context event is underway.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef addContextEventForTimelineEffectsRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CONTEXT_EVENT_FOR_TIMELINE_EFFECTS_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "eventEffectedResources";
        final String                     end1AttributeDescription     = "Entities whose data is impacted by the context event.";
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
        final String                     end2AttributeName            = "associatedEventForTimelines";
        final String                     end2AttributeDescription     = "Descriptions of events that have impacted this resource.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONTEXT_EVENT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef addContextEventImpactRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CONTEXT_EVENT_IMPACT_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "eventImpacts";
        final String                     end1AttributeDescription     = "Resources impacted by the context event.";
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
        final String                     end2AttributeName            = "impactedByContextEvents";
        final String                     end2AttributeDescription     = "Descriptions of context events affecting this resource and the action taken.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONTEXT_EVENT.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SEVERITY_LEVEL_IDENTIFIER));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }

    private ClassificationDef getContextEventCollectionClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.CONTEXT_EVENT_COLLECTION_CLASSIFICATION,
                                                  this.archiveBuilder.getClassificationDef(OpenMetadataType.COLLECTION_ROLE_CLASSIFICATION.typeName),
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName),
                                                  false);
    }

    private RelationshipDef addDependentContextEventRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "parentContextEvents";
        final String                     end1AttributeDescription     = "Context event that is driving the child context events.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONTEXT_EVENT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "childContextEvents";
        final String                     end2AttributeDescription     = "Subsequent context events created to help manage the parent context events.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONTEXT_EVENT.typeName),
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

    private RelationshipDef addRelatedContextEventRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.RELATED_CONTEXT_EVENT_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "relatedContextEvents";
        final String                     end1AttributeDescription     = "Context event that is affecting the other context event.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONTEXT_EVENT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "relatedContextEvents";
        final String                     end2AttributeDescription     = "Context event that is affecting the other context event.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONTEXT_EVENT.typeName),
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
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONFIDENCE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_TYPE_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.STEWARD_PROPERTY_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SOURCE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.NOTES));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0545ReferenceData()
    {
        this.archiveBuilder.addRelationshipDef(getConsistentValidValuesRelationship());
        this.archiveBuilder.addRelationshipDef(getValidValueAssociationRelationship());
        this.archiveBuilder.addRelationshipDef(getSpecificationPropertyAssignmentRelationship());
    }


    private RelationshipDef getConsistentValidValuesRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "consistentValue";
        final String                     end1AttributeDescription     = "Valid value for another valid value set (property) that is consistent with this value.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.VALID_VALUE_DEFINITION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "consistentValue";
        final String                     end2AttributeDescription     = "Valid value for another valid value set (property) that is consistent with this value.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.VALID_VALUE_DEFINITION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);


        return relationshipDef;
    }


    private RelationshipDef getValidValueAssociationRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "associatedValues1";
        final String                     end1AttributeDescription     = "Valid value at end one of this association.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.VALID_VALUE_DEFINITION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "associatedValues2";
        final String                     end2AttributeDescription     = "Valid value at end two of this association.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.VALID_VALUE_DEFINITION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ASSOCIATION_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ASSOCIATION_TYPE));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ADDITIONAL_PROPERTIES));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getSpecificationPropertyAssignmentRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "implementingItem";
        final String                     end1AttributeDescription     = "Connector or template, or similar implementation.";
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
        final String                     end2AttributeName            = "specificationProperty";
        final String                     end2AttributeDescription     = "Valid value representing a property type that controls its behaviour.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.VALID_VALUE_DEFINITION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PROPERTY_NAME));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0462GovernanceActionTypes()
    {
        this.archiveBuilder.addTypeDefPatch(updateGovernanceActionType());
    }

    private TypeDefPatch updateGovernanceActionType()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.PRODUCED_GUARDS));

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0603SurveyReports()
    {
        this.archiveBuilder.addEntityDef(addSurveyReportEntity());
        this.archiveBuilder.addRelationshipDef(getReportedAnnotationRelationship());
        this.archiveBuilder.addRelationshipDef(getAssociatedAnnotationRelationship());
    }

    private EntityDef addSurveyReportEntity()
    {
        /*
         * Build the Entity
         */
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.SURVEY_REPORT,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REPORT.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ANALYSIS_PARAMETERS));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ANALYSIS_STEP));

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }

    private RelationshipDef getReportedAnnotationRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "fromSurveyReport";
        final String                     end1AttributeDescription     = "The report that the annotations belong to.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.SURVEY_REPORT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.AT_MOST_ONE);
        relationshipDef.setEndDef1(relationshipEndDef);

        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "reportedAnnotations";
        final String                     end2AttributeDescription     = "The annotations providing the contents for the report.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }

    private RelationshipDef getAssociatedAnnotationRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "annotationSubject";
        final String                     end1AttributeDescription     = "The element described in the annotation.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.OPEN_METADATA_ROOT.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "associatedAnnotations";
        final String                     end2AttributeDescription     = "The annotations describing the element or its real-world counterpart.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.ANNOTATION.typeName),
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

    private void update0690RequestForAction()
    {
        this.archiveBuilder.addRelationshipDef(getRequestForActionTargetRelationship());
    }

    private RelationshipDef getRequestForActionTargetRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.REQUEST_FOR_ACTION_TARGET,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "identifiedInRequestForActions";
        final String                     end1AttributeDescription     = "Request for action that originated the action.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "requestForActionTargets";
        final String                     end2AttributeDescription     = "Elements that originated the data.";
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

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ACTION_TARGET_NAME));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0755UltimateSourcesDestinations()
    {
        this.archiveBuilder.addRelationshipDef(getUltimateSourceRelationship());
        this.archiveBuilder.addRelationshipDef(getUltimateDestinationRelationship());
    }

    private RelationshipDef getUltimateSourceRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ULTIMATE_SOURCE,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        relationshipDef.setMultiLink(true);
        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "downstreamFromLineageSource";
        final String                     end1AttributeDescription     = "Elements that receives data from the ultimate source (via other processing).";
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
        final String                     end2AttributeName            = "ultimateSources";
        final String                     end2AttributeDescription     = "Elements that originated the data.";
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

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ISC_QUALIFIED_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.HOPS));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }


    private RelationshipDef getUltimateDestinationRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.ULTIMATE_DESTINATION,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        relationshipDef.setMultiLink(true);
        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "upstreamFromLineageDestination";
        final String                     end1AttributeDescription     = "Elements that sends data from the ultimate destination (via other processing).";
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
        final String                     end2AttributeName            = "ultimateDestinations";
        final String                     end2AttributeDescription     = "Elements that ultimately receive the data.";
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

        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.ISC_QUALIFIED_NAME));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.LABEL));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION));
        properties.add(archiveHelper.getTypeDefAttribute(OpenMetadataProperty.HOPS));

        relationshipDef.setPropertiesDefinition(properties);

        return relationshipDef;
    }
}

