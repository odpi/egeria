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
public class OpenMetadataTypesArchive3_1
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "bce3b0a0-662a-4f87-b8dc-844078a11a6e";
    private static final String                  archiveName        = "Open Metadata Types";
    private static final String                  archiveDescription = "Standard types for open metadata repositories.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  archiveVersion     = "3.1";
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
    public OpenMetadataTypesArchive3_1()
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
    public OpenMetadataTypesArchive3_1(OMRSArchiveBuilder archiveBuilder)
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
        OpenMetadataTypesArchive2_11 previousTypes = new OpenMetadataTypesArchive2_11(archiveBuilder);

        /*
         * Pull the types from previous releases.
         */
        previousTypes.getOriginalTypes();

        /*
         * Calls for new and changed types go here
         */
        update0030HostsAndOperatingPlatforms();
        update0035ComplexHosts();
        update0036Storage();
        update0040SoftwareServers();
        update0045ServersAndAssets();
        update0050ApplicationsAndProcesses();
        update0057SoftwareServices();
        update0201Connections();
        update0223Events();
        add0485DataProcessingPurposes();
        update0507ObsoleteDefinitions();

    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * The OperatingPlatform now can record the patch level of the operating system.
     */
    private void update0030HostsAndOperatingPlatforms()
    {
        this.archiveBuilder.addTypeDefPatch(updateOperatingPlatformEntity());
        this.archiveBuilder.addClassificationDef(addSoftwarePackageManifestClassification());
        this.archiveBuilder.addRelationshipDef(addOperatingPlatformManifestRelationship());
    }

    private TypeDefPatch updateOperatingPlatformEntity()
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
        TypeDefAttribute       property;

        property = archiveHelper.getStringTypeDefAttribute(OpenMetadataProperty.OPERATING_SYSTEM_PATCH_LEVEL.name,
                                                           OpenMetadataProperty.OPERATING_SYSTEM_PATCH_LEVEL.description,
                                                           OpenMetadataProperty.OPERATING_SYSTEM_PATCH_LEVEL.descriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    private ClassificationDef addSoftwarePackageManifestClassification()
    {
        final String guid = "e328ae6e-0b16-4490-9883-c953b4258841";

        final String name            = "SoftwarePackageManifest";
        final String description     = "Identifies a collection of software packages.";
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


    private RelationshipDef addOperatingPlatformManifestRelationship()
    {
        final String guid = "e5bd6acf-932c-4d9c-85ff-941a8e4451db";

        final String name            = "OperatingPlatformManifest";
        final String description     = "Defines the base software installed on the operating platform.";
        final String descriptionGUID = null;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "OperatingPlatform";
        final String                     end1AttributeName            = "packagedInOperatingPlatforms";
        final String                     end1AttributeDescription     = "The operating platforms that use this collection of software packages.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "Collection";
        final String                     end2AttributeName            = "includesSoftwarePackages";
        final String                     end2AttributeDescription     = "The collection of software packages that are included in the operating platform.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
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
     * New type for bare metal computer and properties added to HostClusterMember.
     */
    private void update0035ComplexHosts()
    {
        this.archiveBuilder.addEntityDef(addBareMetalComputerEntity());
        this.archiveBuilder.addEntityDef(addVirtualMachineEntity());
        this.archiveBuilder.addEntityDef(addDockerContainerEntity());
        this.archiveBuilder.addEntityDef(addHadoopClusterEntity());
        this.archiveBuilder.addEntityDef(addKubernetesClusterEntity());

        this.archiveBuilder.addTypeDefPatch(deprecateDeployedVirtualContainer());
    }


    private EntityDef addBareMetalComputerEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.BARE_METAL_COMPUTER.typeGUID,
                                                 OpenMetadataType.BARE_METAL_COMPUTER.typeName,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.HOST.typeName),
                                                 OpenMetadataType.BARE_METAL_COMPUTER.description,
                                                 OpenMetadataType.BARE_METAL_COMPUTER.descriptionGUID,
                                                 OpenMetadataType.BARE_METAL_COMPUTER.wikiURL);
    }



    private EntityDef addVirtualMachineEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.VIRTUAL_MACHINE.typeGUID,
                                                 OpenMetadataType.VIRTUAL_MACHINE.typeName,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.HOST.typeName),
                                                 OpenMetadataType.VIRTUAL_MACHINE.description,
                                                 OpenMetadataType.VIRTUAL_MACHINE.descriptionGUID,
                                                 OpenMetadataType.VIRTUAL_MACHINE.wikiURL);
    }


    private EntityDef addDockerContainerEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DOCKER_CONTAINER.typeGUID,
                                                 OpenMetadataType.DOCKER_CONTAINER.typeName,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.VIRTUAL_CONTAINER.typeName),
                                                 OpenMetadataType.DOCKER_CONTAINER.description,
                                                 OpenMetadataType.DOCKER_CONTAINER.descriptionGUID,
                                                 OpenMetadataType.DOCKER_CONTAINER.wikiURL);
    }


    private EntityDef addHadoopClusterEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.HADOOP_CLUSTER.typeGUID,
                                                 OpenMetadataType.HADOOP_CLUSTER.typeName,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.HOST_CLUSTER.typeName),
                                                 OpenMetadataType.HADOOP_CLUSTER.description,
                                                 OpenMetadataType.HADOOP_CLUSTER.descriptionGUID,
                                                 OpenMetadataType.HADOOP_CLUSTER.wikiURL);
    }


    private EntityDef addKubernetesClusterEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.KUBERNETES_CLUSTER.typeGUID,
                                                 OpenMetadataType.KUBERNETES_CLUSTER.typeName,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.HOST_CLUSTER.typeName),
                                                 OpenMetadataType.KUBERNETES_CLUSTER.description,
                                                 OpenMetadataType.KUBERNETES_CLUSTER.descriptionGUID,
                                                 OpenMetadataType.KUBERNETES_CLUSTER.wikiURL);
    }


    /**
     * Deprecate the DeployedVirtualContainer relationship
     *
     * @return patch
     */
    private TypeDefPatch deprecateDeployedVirtualContainer()
    {
        final String typeName = "DeployedVirtualContainer";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * New types for persistent storage.
     */
    private void update0036Storage()
    {
        this.archiveBuilder.addEntityDef(addStorageVolumeEntity());

        this.archiveBuilder.addRelationshipDef(addAttachedStorageRelationship());
    }


    private EntityDef addStorageVolumeEntity()
    {
        final String guid = "14145458-f0d0-4955-8899-b8a2874708c9";

        final String name            = "StorageVolume";
        final String description     = "A persistent storage volume.";
        final String descriptionGUID = null;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                 description,
                                                 descriptionGUID);
    }


    private RelationshipDef addAttachedStorageRelationship()
    {
        final String guid = "2cf1e949-7189-4bf2-8ee4-e1318e59abd7";

        final String name            = "AttachedStorage";
        final String description     = "Links a host to a persistent storage volume.";
        final String descriptionGUID = null;

        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(guid,
                                                                                name,
                                                                                null,
                                                                                description,
                                                                                descriptionGUID,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1EntityType               = "Host";
        final String                     end1AttributeName            = "hosts";
        final String                     end1AttributeDescription     = "The hosts that are accessing the storage.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2EntityType               = "StorageVolume";
        final String                     end2AttributeName            = "storageVolumes";
        final String                     end2AttributeDescription     = "The storage available to a host.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end2EntityType),
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
     * New properties added to ServerAssetUse.
     */
    private void update0045ServersAndAssets()
    {
        this.archiveBuilder.addTypeDefPatch(updateServerAssetUseRelationship());
    }


    private TypeDefPatch updateServerAssetUseRelationship()
    {
        /*
         * Create the Patch
         */
        final String typeName = OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName;

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = OpenMetadataProperty.MINIMUM_INSTANCES.name;
        final String attribute1Description     = OpenMetadataProperty.MINIMUM_INSTANCES.description;
        final String attribute1DescriptionGUID = OpenMetadataProperty.MINIMUM_INSTANCES.descriptionGUID;
        final String attribute2Name            = OpenMetadataProperty.MAXIMUM_INSTANCES.name;
        final String attribute2Description     = OpenMetadataProperty.MAXIMUM_INSTANCES.description;
        final String attribute2DescriptionGUID = OpenMetadataProperty.MAXIMUM_INSTANCES.descriptionGUID;


        property = archiveHelper.getIntTypeDefAttribute(attribute1Name,
                                                        attribute1Description,
                                                        attribute1DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getIntTypeDefAttribute(attribute2Name,
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
     * The ServerEndpoint relationship used to be between an Endpoint and a SoftwareServer.  It is changed to connect
     * an Endpoint with an ITInfrastructure element to allow SoftwareServerPlatforms (like the OMAGServerPlatform)
     * to support endpoints (and hence APIs).
     */
    private void update0040SoftwareServers()
    {
        this.archiveBuilder.addTypeDefPatch(updateServerEndpointRelationship());
    }


    private TypeDefPatch updateServerEndpointRelationship()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Update end 1.
         */
        final String                     end1AttributeName            = "servers";
        final String                     end1AttributeDescription     = "Server supporting this endpoint.";
        final String                     end1AttributeDescriptionGUID = null;


        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.IT_INFRASTRUCTURE.typeName),
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
     * The ApplicationService is designed to represent a business microservice.
     */
    private void update0057SoftwareServices()
    {
        this.archiveBuilder.addEntityDef(addApplicationServiceEntity());
    }



    private EntityDef addApplicationServiceEntity()
    {
        final String guid = "5b7f340e-7dc9-45c0-a636-c20605147c94";

        final String name            = "ApplicationService";
        final String description     = "A software service supporting a single reusable business function.";
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

    private void update0050ApplicationsAndProcesses()
    {
        this.archiveBuilder.addEntityDef(addCatalogEntity());
        this.archiveBuilder.addEntityDef(addDataManagerEntity());
    }


    private EntityDef addCatalogEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.INVENTORY_CATALOG,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName));
    }

    private EntityDef addDataManagerEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_MANAGER,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName));
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */

    private void update0201Connections()
    {
        this.archiveBuilder.addClassificationDef(addConnectorTypeDirectoryClassification());
        this.archiveBuilder.addEntityDef(addConnectorCategoryEntity());
        this.archiveBuilder.addTypeDefPatch(updateConnectorTypeEntity());
        this.archiveBuilder.addRelationshipDef(addConnectorImplementationChoiceRelationship());
    }


    private ClassificationDef addConnectorTypeDirectoryClassification()
    {
        return archiveHelper.getClassificationDef(OpenMetadataType.CONNECTOR_TYPE_DIRECTORY_CLASSIFICATION,
                                                  null,
                                                  this.archiveBuilder.getEntityDef(OpenMetadataType.COLLECTION.typeName),
                                                  false);
    }



    private EntityDef addConnectorCategoryEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.CONNECTOR_CATEGORY,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName));

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DISPLAY_NAME);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.DESCRIPTION);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TARGET_TECHNOLOGY_SOURCE);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TARGET_TECHNOLOGY_NAME);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RECOGNIZED_ADDITIONAL_PROPERTIES);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RECOGNIZED_SECURED_PROPERTIES);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.RECOGNIZED_CONFIGURATION_PROPERTIES);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private TypeDefPatch updateConnectorTypeEntity()
    {
        /*
         * Create the Patch
         */
        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(OpenMetadataType.CONNECTOR_TYPE.typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.SUPPORTED_ASSET_TYPE_NAME);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.EXPECTED_DATA_FORMAT);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONNECTOR_FRAMEWORK_NAME);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONNECTOR_INTERFACE_LANGUAGE);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.CONNECTOR_INTERFACES);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TARGET_TECHNOLOGY_SOURCE);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TARGET_TECHNOLOGY_NAME);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TARGET_TECHNOLOGY_INTERFACES);
        properties.add(property);
        property = archiveHelper.getTypeDefAttribute(OpenMetadataProperty.TARGET_TECHNOLOGY_VERSIONS);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    private RelationshipDef addConnectorImplementationChoiceRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.CONNECTOR_IMPLEMENTATION_CHOICE_RELATIONSHIP,
                                                                                null,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "connectorCategories";
        final String                     end1AttributeDescription     = "The categories that a connector type belongs to.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONNECTOR_CATEGORY.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "connectorTypes";
        final String                     end2AttributeDescription     = "The connector types that support the technology described in the connector category.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.CONNECTOR_TYPE.typeName),
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

    private void update0223Events()
    {
        this.archiveBuilder.addEntityDef(addKafkaTopicEntity());
    }


    private EntityDef addKafkaTopicEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.KAFKA_TOPIC.typeGUID,
                                                 OpenMetadataType.KAFKA_TOPIC.typeName,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.TOPIC.typeName),
                                                 OpenMetadataType.KAFKA_TOPIC.description,
                                                 OpenMetadataType.KAFKA_TOPIC.descriptionGUID,
                                                 OpenMetadataType.KAFKA_TOPIC.wikiURL);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * These types were originally coded in release 1.7 - however the top-level method was not called and so these types did not make the archive.
     */

    private void add0485DataProcessingPurposes()
    {
        this.archiveBuilder.addEntityDef(getDataProcessingPurposeEntity());
        this.archiveBuilder.addEntityDef(getDataProcessingActionEntity());
        this.archiveBuilder.addEntityDef(getDataProcessingDescriptionEntity());

        this.archiveBuilder.addRelationshipDef(this.getPermittedProcessingRelationship());
        this.archiveBuilder.addRelationshipDef(this.getApprovedPurposeRelationship());
        this.archiveBuilder.addRelationshipDef(this.getDetailedProcessingActionsRelationship());
        this.archiveBuilder.addRelationshipDef(this.getDataProcessingSpecificationRelationship());
        this.archiveBuilder.addRelationshipDef(this.getDataProcessingTargetRelationship());
    }


    private EntityDef getDataProcessingDescriptionEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_PROCESSING_DESCRIPTION.typeGUID,
                                                                OpenMetadataType.DATA_PROCESSING_DESCRIPTION.typeName,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_PROCESSING_ACTION.typeName),
                                                                OpenMetadataType.DATA_PROCESSING_DESCRIPTION.description,
                                                                OpenMetadataType.DATA_PROCESSING_DESCRIPTION.descriptionGUID,
                                                                OpenMetadataType.DATA_PROCESSING_DESCRIPTION.wikiURL);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

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


    private EntityDef getDataProcessingPurposeEntity()
    {
        return archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_PROCESSING_PURPOSE.typeGUID,
                                                 OpenMetadataType.DATA_PROCESSING_PURPOSE.typeName,
                                                 this.archiveBuilder.getEntityDef(OpenMetadataType.GOVERNANCE_DEFINITION_TYPE_NAME),
                                                 OpenMetadataType.DATA_PROCESSING_PURPOSE.description,
                                                 OpenMetadataType.DATA_PROCESSING_PURPOSE.descriptionGUID,
                                                 OpenMetadataType.DATA_PROCESSING_PURPOSE.wikiURL);
    }


    private EntityDef getDataProcessingActionEntity()
    {
        EntityDef entityDef = archiveHelper.getDefaultEntityDef(OpenMetadataType.DATA_PROCESSING_ACTION.typeGUID,
                                                                OpenMetadataType.DATA_PROCESSING_ACTION.typeName,
                                                                this.archiveBuilder.getEntityDef(OpenMetadataType.REFERENCEABLE.typeName),
                                                                OpenMetadataType.DATA_PROCESSING_ACTION.description,
                                                                OpenMetadataType.DATA_PROCESSING_ACTION.descriptionGUID,
                                                                OpenMetadataType.DATA_PROCESSING_ACTION.wikiURL);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

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


    private RelationshipDef getPermittedProcessingRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.PERMITTED_PROCESSING_RELATIONSHIP.typeGUID,
                                                                                OpenMetadataType.PERMITTED_PROCESSING_RELATIONSHIP.typeName,
                                                                                null,
                                                                                OpenMetadataType.PERMITTED_PROCESSING_RELATIONSHIP.description,
                                                                                OpenMetadataType.PERMITTED_PROCESSING_RELATIONSHIP.descriptionGUID,
                                                                                OpenMetadataType.PERMITTED_PROCESSING_RELATIONSHIP.wikiURL,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "supportedPurposes";
        final String                     end1AttributeDescription     = "The supported outcomes from the processing.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_PROCESSING_PURPOSE.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "permittedProcessing";
        final String                     end2AttributeDescription     = "The description of the processing that is permitted for the purposes.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_PROCESSING_DESCRIPTION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getApprovedPurposeRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.APPROVED_PURPOSE_RELATIONSHIP.typeGUID,
                                                                                OpenMetadataType.APPROVED_PURPOSE_RELATIONSHIP.typeName,
                                                                                null,
                                                                                OpenMetadataType.APPROVED_PURPOSE_RELATIONSHIP.description,
                                                                                OpenMetadataType.APPROVED_PURPOSE_RELATIONSHIP.descriptionGUID,
                                                                                OpenMetadataType.APPROVED_PURPOSE_RELATIONSHIP.wikiURL,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "approvedForPurposes";
        final String                     end1AttributeDescription     = "The people/processes that have permission to process data.";
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
        final String                     end2AttributeName            = "approvedPurposes";
        final String                     end2AttributeDescription     = "The purposes (outcomes) that the people/processes have permission for.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_PROCESSING_PURPOSE.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getDetailedProcessingActionsRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DETAILED_PROCESSING_ACTION_RELATIONSHIP.typeGUID,
                                                                                OpenMetadataType.DETAILED_PROCESSING_ACTION_RELATIONSHIP.typeName,
                                                                                null,
                                                                                OpenMetadataType.DETAILED_PROCESSING_ACTION_RELATIONSHIP.description,
                                                                                OpenMetadataType.DETAILED_PROCESSING_ACTION_RELATIONSHIP.descriptionGUID,
                                                                                OpenMetadataType.DETAILED_PROCESSING_ACTION_RELATIONSHIP.wikiURL,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "parentProcessingActions";
        final String                     end1AttributeDescription     = "The aggregating processing descriptions.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_PROCESSING_ACTION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "childProcessingActions";
        final String                     end2AttributeDescription     = "The individual actions that make up the data processing description.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_PROCESSING_ACTION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getDataProcessingSpecificationRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DATA_PROCESSING_SPECIFICATION_RELATIONSHIP.typeGUID,
                                                                                OpenMetadataType.DATA_PROCESSING_SPECIFICATION_RELATIONSHIP.typeName,
                                                                                null,
                                                                                OpenMetadataType.DATA_PROCESSING_SPECIFICATION_RELATIONSHIP.description,
                                                                                OpenMetadataType.DATA_PROCESSING_SPECIFICATION_RELATIONSHIP.descriptionGUID,
                                                                                OpenMetadataType.DATA_PROCESSING_SPECIFICATION_RELATIONSHIP.wikiURL,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "dataProcessingElements";
        final String                     end1AttributeDescription     = "The people/processes performing the processing.";
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
        final String                     end2AttributeName            = "dataProcessingDescriptions";
        final String                     end2AttributeDescription     = "The description of the processing.";
        final String                     end2AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_PROCESSING_DESCRIPTION.typeName),
                                                                 end2AttributeName,
                                                                 end2AttributeDescription,
                                                                 end2AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef2(relationshipEndDef);

        return relationshipDef;
    }


    private RelationshipDef getDataProcessingTargetRelationship()
    {
        RelationshipDef relationshipDef = archiveHelper.getBasicRelationshipDef(OpenMetadataType.DATA_PROCESSING_TARGET_RELATIONSHIP.typeGUID,
                                                                                OpenMetadataType.DATA_PROCESSING_TARGET_RELATIONSHIP.typeName,
                                                                                null,
                                                                                OpenMetadataType.DATA_PROCESSING_TARGET_RELATIONSHIP.description,
                                                                                OpenMetadataType.DATA_PROCESSING_TARGET_RELATIONSHIP.descriptionGUID,
                                                                                OpenMetadataType.DATA_PROCESSING_TARGET_RELATIONSHIP.wikiURL,
                                                                                ClassificationPropagationRule.NONE);

        RelationshipEndDef relationshipEndDef;

        /*
         * Set up end 1.
         */
        final String                     end1AttributeName            = "dataProcessingActions";
        final String                     end1AttributeDescription     = "Actions being performed on the data.";
        final String                     end1AttributeDescriptionGUID = null;

        relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(OpenMetadataType.DATA_PROCESSING_ACTION.typeName),
                                                                 end1AttributeName,
                                                                 end1AttributeDescription,
                                                                 end1AttributeDescriptionGUID,
                                                                 RelationshipEndCardinality.ANY_NUMBER);
        relationshipDef.setEndDef1(relationshipEndDef);


        /*
         * Set up end 2.
         */
        final String                     end2AttributeName            = "dataProcessingTarget";
        final String                     end2AttributeDescription     = "The data that is being acted upon.";
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


    private void update0507ObsoleteDefinitions()
    {
        this.archiveBuilder.addTypeDefPatch(deprecateBoundedSchemaType());
        this.archiveBuilder.addTypeDefPatch(deprecateBoundedSchemaElementType());
        this.archiveBuilder.addTypeDefPatch(deprecateArraySchemaType());
        this.archiveBuilder.addTypeDefPatch(deprecateArrayDocumentType());
        this.archiveBuilder.addTypeDefPatch(deprecateSetSchemaType());
        this.archiveBuilder.addTypeDefPatch(deprecateSetDocumentType());
    }


    /**
     * Deprecate the BoundedSchemaType
     *
     * @return patch
     */
    private TypeDefPatch deprecateBoundedSchemaType()
    {
        final String typeName = "BoundedSchemaType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    /**
     * Deprecate the BoundedSchemaElementType
     *
     * @return patch
     */
    private TypeDefPatch deprecateBoundedSchemaElementType()
    {
        final String typeName = "BoundedSchemaElementType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    /**
     * Deprecate the ArraySchemaType
     *
     * @return patch
     */
    private TypeDefPatch deprecateArraySchemaType()
    {
        final String typeName = "ArraySchemaType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    /**
     * Deprecate the ArrayDocumentType
     *
     * @return patch
     */
    private TypeDefPatch deprecateArrayDocumentType()
    {
        final String typeName = "ArrayDocumentType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }

    /**
     * Deprecate the SetSchemaType
     *
     * @return patch
     */
    private TypeDefPatch deprecateSetSchemaType()
    {
        final String typeName = "SetSchemaType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }


    /**
     * Deprecate the SetDocumentType
     *
     * @return patch
     */
    private TypeDefPatch deprecateSetDocumentType()
    {
        final String typeName = "SetDocumentType";

        TypeDefPatch typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);
        typeDefPatch.setTypeDefStatus(TypeDefStatus.DEPRECATED_TYPEDEF);

        return typeDefPatch;
    }
}

