/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefStatus;
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
public class OpenMetadataTypesArchive3_8
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "3.8";
    private static final String                  originatorName     = "Egeria";
    private static final String                  originatorLicense  = "Apache 2.0";
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
    public OpenMetadataTypesArchive3_8()
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
    public OpenMetadataTypesArchive3_8(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive3_7 previousTypes = new OpenMetadataTypesArchive3_7(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update0011ManagingReferenceables();
        update0015LinkedMediaTypes();
        update0160NoteLogs();
        update0030OperatingPlatforms();
        update0057SoftwareServices();
        update0070NetworksAndGateways();
        update0461GovernanceEngines();
        update0566DesignModelOrganization();
        update0571ConceptModels();
        update0615SchemaExtraction();
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * SourcedFrom relationship has its cardinality the wrong way around
     */
    private void update0011ManagingReferenceables()
    {
        this.archiveBuilder.addTypeDefPatch(updateSourcedFromRelationship());
    }

    private TypeDefPatch updateSourcedFromRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SourcedFrom";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "Referenceable";
        final String                     end1AttributeName            = "resultingElement";
        final String                     end1AttributeDescription     = "Element created from the template.";
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
        final String                     end2AttributeName            = "templateElement";
        final String                     end2AttributeDescription     = "Template element providing information.";
        final String                     end2AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end2Cardinality              = RelationshipEndCardinality.AT_MOST_ONE;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 end2Cardinality);
        typeDefPatch.setEndDef2(relationshipEndDef);

        return typeDefPatch;
    }



    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add multi-link flags
     */
    private void update0015LinkedMediaTypes()
    {
        this.archiveBuilder.addTypeDefPatch(updateExternalReferenceLinkRelationship());
        this.archiveBuilder.addTypeDefPatch(updateMediaReferenceRelationship());
    }

    private TypeDefPatch updateExternalReferenceLinkRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ExternalReferenceLink";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setUpdateMultiLink(true);
        typeDefPatch.setMultiLink(true);

        return typeDefPatch;
    }


    private TypeDefPatch updateMediaReferenceRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "MediaReference";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setUpdateMultiLink(true);
        typeDefPatch.setMultiLink(true);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    /**
     * Add new software services
     */
    private void update0057SoftwareServices()
    {
        this.archiveBuilder.addEntityDef(addMetadataRepositoryServiceEntity());
        this.archiveBuilder.addEntityDef(addSecurityServiceEntity());
    }

    private EntityDef addMetadataRepositoryServiceEntity()
    {
        final String guid            = "27891e52-1255-4a33-98a2-377717a25334";
        final String name            = "MetadataRepositoryService";
        final String description     = "Provides access to a metadata repository - either local or remote.";
        final String descriptionGUID = null;

        final String superTypeName = "SoftwareService";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef addSecurityServiceEntity()
    {
        final String guid            = "2df2069f-6475-400c-bf8c-6d2072a55d47";
        final String name            = "SecurityService";
        final String description     = "Provides security services - classifications identify specific capabilities.";
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
     * Add software package dependency relationship
     */
    private void update0030OperatingPlatforms()
    {
        this.archiveBuilder.addRelationshipDef(addSoftwarePackageDependencyRelationship());
    }

    private RelationshipDef addSoftwarePackageDependencyRelationship()
    {
        final String guid            = "2c05beaf-e313-47f8-ac18-2298140b2ad9";
        final String name            = "SoftwarePackageDependency";
        final String description     = "Shows the software packages being used within an asset.";
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
        final String                     end1EntityType               = "Asset";
        final String                     end1AttributeName            = "runningWithAsset";
        final String                     end1AttributeDescription     = "Assets making use of software package.";
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
        final String                     end2EntityType               = "Collection";
        final String                     end2AttributeName            = "dependsOnSoftwarePackages";
        final String                     end2AttributeDescription     = "Collection of software packages.";
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
     * Simplify modelling networks
     */
    private void update0070NetworksAndGateways()
    {
        this.archiveBuilder.addRelationshipDef(getVisibleEndpointRelationship());
        this.archiveBuilder.addTypeDefPatch(deprecateHostNetworkRelationship());
        this.archiveBuilder.addTypeDefPatch(updateNetworkGatewayLinkRelationship());
    }

    private RelationshipDef getVisibleEndpointRelationship()
    {
        final String guid            = "5e1722c7-0167-49a0-bd77-fbf9dc5eb5bb";
        final String name            = "VisibleEndpoint";
        final String description     = "Shows that network that an endpoint is visible through.";
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
        final String                     end1EntityType               = "Endpoint";
        final String                     end1AttributeName            = "visibleEndpoints";
        final String                     end1AttributeDescription     = "Endpoint callable through network.";
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
        final String                     end2EntityType               = "Network";
        final String                     end2AttributeName            = "visibleInNetwork";
        final String                     end2AttributeDescription     = "Networks from which the endpoint can be called.";
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

    private TypeDefPatch deprecateHostNetworkRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "HostNetwork";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private TypeDefPatch updateNetworkGatewayLinkRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "NetworkGatewayLink";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setUpdateMultiLink(true);
        typeDefPatch.setMultiLink(true);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "name";
        final String attribute1Description     = "Name for the network mapping.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description and purpose of the network mapping.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "externalEndpointAddress";
        final String attribute3Description     = "Network address used by callers to the network gateway.";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "internalEndpointAddress";
        final String attribute4Description     = "Network address that the network gateway maps the request to.";
        final String attribute4DescriptionGUID = null;


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

        typeDefPatch.setPropertyDefinitions(properties);

        return typeDefPatch;
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Enable any person role to author note logs.
     */
    private void update0160NoteLogs()
    {
        this.archiveBuilder.addTypeDefPatch(updateNoteLogAuthorshipRelationship());
    }


    private TypeDefPatch updateNoteLogAuthorshipRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "NoteLogAuthorship";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "PersonRole";
        final String                     end1AttributeName            = "authors";
        final String                     end1AttributeDescription     = "Person contributing to the note log.";
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
        final String                     end2EntityType               = "NoteLog";
        final String                     end2AttributeName            = "authoredNoteLogs";
        final String                     end2AttributeDescription     = "Note log containing contributions.";
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


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * Make supported governance service multi-link and add support for RepositoryGovernanceEngine and RepositoryGovernanceService.
     * ArchiveEngine and ArchiveService are deprecated in favour of these new entity types
     */
    private void update0461GovernanceEngines()
    {
        this.archiveBuilder.addEntityDef(getRepositoryGovernanceEngineEntity());
        this.archiveBuilder.addEntityDef(getRepositoryGovernanceServiceEntity());
        this.archiveBuilder.addTypeDefPatch(updateSupportedGovernanceServiceRelationship());
        this.archiveBuilder.addTypeDefPatch(deprecateArchiveEngine());
        this.archiveBuilder.addTypeDefPatch(deprecateArchiveService());
    }


    private TypeDefPatch updateSupportedGovernanceServiceRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SupportedGovernanceService";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setUpdateMultiLink(true);
        typeDefPatch.setMultiLink(true);

        return typeDefPatch;
    }


    private TypeDefPatch deprecateArchiveEngine()
    {
        /*
         * Create the Patch - the super type is updated so that existing archive engines will run in the repository governance OMES
         */
        final String typeName = "ArchiveEngine";
        final String superTypeName = "RepositoryGovernanceEngine";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));
        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private TypeDefPatch deprecateArchiveService()
    {
        /*
         * Create the Patch - the super type is updated so that existing archive services will run in the repository governance OMES
         */
        final String typeName = "ArchiveService";
        final String superTypeName = "RepositoryGovernanceService";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setSuperType(this.archiveBuilder.getEntityDef(superTypeName));
        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private EntityDef getRepositoryGovernanceEngineEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "2b3bed05-c227-47d7-87a3-139ab0568361";
        final String name            = "RepositoryGovernanceEngine";
        final String description     = "A governance engine for open metadata repositories.";
        final String descriptionGUID = null;
        final String superTypeName   = "GovernanceEngine";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);

    }


    private EntityDef getRepositoryGovernanceServiceEntity()
    {
        /*
         * Build the Entity
         */
        final String guid            = "978e7674-8231-4158-a4e3-a5ccdbcad60e";
        final String name            = "RepositoryGovernanceService";
        final String description     = "A governance service for open metadata repositories.";
        final String descriptionGUID = null;
        final String superTypeName   = "GovernanceService";

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
     * Create a single relationship type to link a design model element to its model.
     */
    private void update0566DesignModelOrganization()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateDesignModelElementOwnershipRelationship());
        this.archiveBuilder.addTypeDefPatch(deprecateDesignModelGroupOwnershipRelationship());
        this.archiveBuilder.addTypeDefPatch(deprecateDesignModelGroupHierarchyRelationship());
        this.archiveBuilder.addRelationshipDef(addDesignModelOwnershipRelationship());
    }

    private TypeDefPatch deprecateDesignModelElementOwnershipRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DesignModelElementOwnership";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    private TypeDefPatch deprecateDesignModelGroupOwnershipRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DesignModelGroupOwnership";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    private TypeDefPatch deprecateDesignModelGroupHierarchyRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DesignModelGroupHierarchy";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    private RelationshipDef addDesignModelOwnershipRelationship()
    {
        final String guid            = "d57043c2-eeab-4167-8d0d-2223af8aee93";
        final String name            = "DesignModelOwnership";
        final String description     = "Links design model elements to their owning model.";
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
        final String                     end1EntityType               = "DesignModel";
        final String                     end1AttributeName            = "owningDesignModel";
        final String                     end1AttributeDescription     = "Owning model.";
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
        final String                     end2EntityType               = "DesignModelElement";
        final String                     end2AttributeName            = "designModelElements";
        final String                     end2AttributeDescription     = "List of elements that belong to this model.";
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
     * Add concept model classification
     */
    private void update0571ConceptModels()
    {
        this.archiveBuilder.addClassificationDef(addConceptModelClassification());
    }

    private ClassificationDef addConceptModelClassification()
    {
        final String guid            = "7149c2de-5f24-4959-9b24-9d5e67709fac";
        final String name            = "ConceptModel";
        final String description     = "Identifies that a design model as a concept model.";
        final String descriptionGUID = null;

        final String linkedToEntity = "DesignModel";

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
     * Remove obsolete relationship
     */
    private void update0615SchemaExtraction()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateDataClassDefinitionRelationship());
        this.archiveBuilder.addTypeDefPatch(updateSchemaAttributeDefinition());
        this.archiveBuilder.addTypeDefPatch(updateRelationshipAnnotation());
    }

    private TypeDefPatch deprecateDataClassDefinitionRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = "DataClassDefinition";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    private TypeDefPatch updateSchemaAttributeDefinition()
    {
        /*
         * Create the Patch
         */
        final String typeName = "SchemaAttributeDefinition";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "assetGUID";
        final String attribute1Description     = "Unique identifier for the analyzed asset.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

    private TypeDefPatch updateRelationshipAnnotation()
    {
        /*
         * Create the Patch
         */
        final String typeName = "RelationshipAnnotation";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "discoveryReportGUID";
        final String attribute1Description     = "Unique identifier for the discovery analysis report that this relationship belongs to.";
        final String attribute1DescriptionGUID = null;

        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }

}

