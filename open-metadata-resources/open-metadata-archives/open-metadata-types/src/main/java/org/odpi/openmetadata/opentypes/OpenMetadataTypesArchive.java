/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
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
public class OpenMetadataTypesArchive
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "1.8";
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
        OpenMetadataTypesArchive1_7  previousTypes = new OpenMetadataTypesArchive1_7(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();


        /*
         * Calls for new types go here
         */
        update0010BaseModel();
        add0057IntegrationCapabilities();
        update0224Databases();
        update0512DerivedSchemaAttributes();
        update0130Projects();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * 0010 Base Model - add Template Classification.
     */
    private void update0010BaseModel()
    {
        this.archiveBuilder.addClassificationDef(addTemplateClassification());
    }


    private ClassificationDef addTemplateClassification()
    {
        final String guid = "25fad4a2-c2d6-440d-a5b1-e537881f84ee";

        final String name            = "Template";
        final String description     = "Marks the referenceable as a template for creating new objects.";
        final String descriptionGUID = null;

        final String linkedToEntity = "Referenceable";

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


        final String attribute1Name            = "name";
        final String attribute1Description     = "Unique name of the template.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of the template and how/where it is used.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "additionalProperties";
        final String attribute3Description     = "Additional information that is useful to the consumer of the template.";
        final String attribute3DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                           attribute2Description,
                                                           attribute2DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringStringTypeDefAttribute(attribute3Name,
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
     * 0057 - Integration capabilities describe the different types of integration capabilities.
     * Initially these are Egeria's integration daemons.
     */
    private void add0057IntegrationCapabilities()
    {
        this.archiveBuilder.addEntityDef(addMetadataIntegrationCapabilityEntity());

        this.archiveBuilder.addClassificationDef(addDataPlatformIntegrationClassification());
        this.archiveBuilder.addClassificationDef(addDataEngineIntegrationClassification());
    }


    private EntityDef addMetadataIntegrationCapabilityEntity()
    {
        final String guid            = "cc6d2d77-626c-4d0d-aa22-0a491b5fee94";
        final String name            = "MetadataIntegrationCapability";
        final String description     = "Defines a capability that exchanges metadata between servers.";
        final String descriptionGUID = null;

        final String superTypeName = "SoftwareServerCapability";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);

    }


    private ClassificationDef addDataPlatformIntegrationClassification()
    {
        final String guid = "2356af59-dda5-45ad-927f-540bed6b281d";

        final String name            = "DataPlatformIntegration";
        final String description     = "Integrating metadata about data platforms.";
        final String descriptionGUID = null;

        final String linkedToEntity = "MetadataIntegrationCapability";

        return archiveHelper.getClassificationDef(guid,
                                                  name,
                                                  null,
                                                  description,
                                                  descriptionGUID,
                                                  this.archiveBuilder.getEntityDef(linkedToEntity),
                                                  false);
    }

    /**
     * 0130 - update ProjectScope description
     */
    private void update0130Projects()
    {
        this.archiveBuilder.addTypeDefPatch(updateProjectScopeRelationship());
    }

    /**
     * The ProjectScope has an attribute with the incorrect type of Date. It is not possible to patch an attribute to change its type for
     * compatibility reasons. This patch deprecates the old scopeDescription (with Date type) and introduces a new description (with
     * String type).
     *
     * @return the type def patch
     */
    private TypeDefPatch updateProjectScopeRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ProjectScope";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "scopeDescription";
        final String attribute1Description     = "Deprecated attribute. Use the description attribute to describe the scope.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getDateTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        property.setAttributeStatus(TypeDefAttributeStatus.DEPRECATED_ATTRIBUTE);

        properties.add(property);

        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of how each item is related to the project.";
        final String attribute2DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute2Name,
                                                         attribute2Description,
                                                         attribute2DescriptionGUID);

        properties.add(property);


        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private ClassificationDef addDataEngineIntegrationClassification()
    {
        final String guid = "b675a6d1-7dd7-4b20-b91b-43b358dfe0cf";

        final String name            = "DataEngineIntegration";
        final String description     = "Integrating metadata from data engines.";
        final String descriptionGUID = null;

        final String linkedToEntity = "MetadataIntegrationCapability";

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
     * 0224 - Add the databasePlatform to cover the software server capability for the database server.
     */
    private void update0224Databases()
    {
        this.archiveBuilder.addEntityDef(addDatabasePlatformEntity());
    }


    private EntityDef addDatabasePlatformEntity()
    {
        final String guid            = "68b35c1e-6c28-4ac3-94f9-2c3dbcbb79e9";
        final String name            = "DatabasePlatform";
        final String description     = "Defines a capability that manages data organized as relational schemas.";
        final String descriptionGUID = null;

        final String superTypeName = "SoftwareServerCapability";

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
     * 0512 - Add the queryId to identify how the query is used in a complex formula.
     */
    private void update0512DerivedSchemaAttributes()
    {
        this.archiveBuilder.addTypeDefPatch(updateSchemaQueryImplementationRelationship());
    }


    private TypeDefPatch updateSchemaQueryImplementationRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SchemaQueryImplementation";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "queryId";
        final String attribute1Description     = "Identifier for placeholder in derived schema attribute's formula.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;

    }





    /*
     * ========================================================================
     * Below are place holders for types to be introduced in future releases.
     * ========================================================================
     */


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0217 Automated Processes defines a Process is automated (as opposed to manual procedure).
     */
    private void add0217AutomatedProcesses()
    {
        /* placeholder */
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0260Transformations()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void add0265AnalyticsAssets()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    private void add0270IoTAssets()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0280ModelAssets()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * 0435 Governance Rules define details of a governance rule implementation.
     */
    private void add0435GovernanceRules()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0447GovernanceProcesses()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0452GovernanceDaemons()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0480RightsManagement()
    {
        /* placeholder */
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0550LogicSpecificationModel()
    {
        /* placeholder */
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void add0560MappingModel()
    {
        /* placeholder */
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


}

