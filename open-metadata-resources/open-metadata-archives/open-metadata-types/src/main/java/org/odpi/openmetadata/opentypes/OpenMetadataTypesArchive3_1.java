/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
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
        final String typeName = "OperatingPlatform";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "operatingSystemPatchLevel";
        final String attribute1Description     = "Level of patches applied to the operating system.";
        final String attribute1DescriptionGUID = null;


        property = archiveHelper.getStringTypeDefAttribute(attribute1Name,
                                                           attribute1Description,
                                                           attribute1DescriptionGUID);
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
        final String                     end1EntityType               = "OperatingPlatform";
        final String                     end1AttributeName            = "packagedInOperatingPlatforms";
        final String                     end1AttributeDescription     = "The operating platforms that use this collection of software packages.";
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
        final String                     end2AttributeName            = "includesSoftwarePackages";
        final String                     end2AttributeDescription     = "The collection of software packages that are included in the operating platform.";
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
        final String guid = "8ef355d4-5cd7-4038-8337-62671b088920";

        final String name            = "BareMetalComputer";
        final String description     = "A computer that is hosting software directly on its operating system.";
        final String descriptionGUID = null;

        final String superTypeName = "Host";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }



    private EntityDef addVirtualMachineEntity()
    {
        final String guid = "28452091-6b27-4f40-8e31-47ce34f58387";

        final String name            = "VirtualMachine";
        final String description     = "A virtual machine that uses a hypervisor to virtualize hardware.";
        final String descriptionGUID = null;

        final String superTypeName = "Host";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef addDockerContainerEntity()
    {
        final String guid = "9882b8aa-eba3-4a30-94c6-43117efd11cc";

        final String name            = "DockerContainer";
        final String description     = "A virtual container using the docker platform.";
        final String descriptionGUID = null;

        final String superTypeName = "VirtualContainer";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef addHadoopClusterEntity()
    {
        final String guid = "abc27cf7-e526-4d1b-9c25-7dd60a7993e4";

        final String name            = "HadoopCluster";
        final String description     = "A cluster of nodes for big data workloads.";
        final String descriptionGUID = null;

        final String superTypeName = "HostCluster";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef addKubernetesClusterEntity()
    {
        final String guid = "101f1c93-7f5d-44e2-9ea4-5cf21726ba5c";

        final String name            = "KubernetesCluster";
        final String description     = "A host cluster managing containerized applications.";
        final String descriptionGUID = null;

        final String superTypeName = "HostCluster";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
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

        final String superTypeName = OpenMetadataType.REFERENCEABLE.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private RelationshipDef addAttachedStorageRelationship()
    {
        final String guid = "2cf1e949-7189-4bf2-8ee4-e1318e59abd7";

        final String name            = "AttachedStorage";
        final String description     = "Links a host to a persistent storage volume.";
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
        final String                     end1EntityType               = "Host";
        final String                     end1AttributeName            = "hosts";
        final String                     end1AttributeDescription     = "The hosts that are accessing the storage.";
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
        final String                     end2EntityType               = "StorageVolume";
        final String                     end2AttributeName            = "storageVolumes";
        final String                     end2AttributeDescription     = "The storage available to a host.";
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
        final String typeName = "ServerEndpoint";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Update end 1.
         */
        final String                     end1EntityType               = "ITInfrastructure";
        final String                     end1AttributeName            = "servers";
        final String                     end1AttributeDescription     = "Server supporting this endpoint.";
        final String                     end1AttributeDescriptionGUID = null;
        final RelationshipEndCardinality end1Cardinality              = RelationshipEndCardinality.ANY_NUMBER;


        RelationshipEndDef relationshipEndDef = archiveHelper.getRelationshipEndDef(this.archiveBuilder.getEntityDef(end1EntityType),
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
        final String guid = OpenMetadataType.CATALOG.typeGUID;

        final String name            = OpenMetadataType.CATALOG.typeName;
        final String description     = OpenMetadataType.CATALOG.description;
        final String descriptionGUID = OpenMetadataType.CATALOG.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.CATALOG.wikiURL;

        final String superTypeName = OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);
    }

    private EntityDef addDataManagerEntity()
    {
        final String guid = OpenMetadataType.DATA_MANAGER.typeGUID;

        final String name            = OpenMetadataType.DATA_MANAGER.typeName;
        final String description     = OpenMetadataType.DATA_MANAGER.description;
        final String descriptionGUID = OpenMetadataType.DATA_MANAGER.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.DATA_MANAGER.wikiURL;

        final String superTypeName = OpenMetadataType.SOFTWARE_SERVER_CAPABILITY.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);
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
        final String guid = "9678ef11-ed7e-404b-a041-736df7514339";

        final String name            = "ConnectorTypeDirectory";
        final String description     = "Identifies a collection of related connector types.";
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



    private EntityDef addConnectorCategoryEntity()
    {
        final String guid = "fb60761f-7afd-4d3d-9efa-24bc85a7b22e";

        final String name            = "ConnectorCategory";
        final String description     = "A detailed description of the effect of some data processing.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.REFERENCEABLE.typeName;

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

        final String attribute1Name            = "displayName";
        final String attribute1Description     = "Consumable name for the connector category, suitable for reports and user interfaces.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "description";
        final String attribute2Description     = "Description of the connector category.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "targetTechnologySource";
        final String attribute3Description     = "Name of the organization providing the technology that the connectors access. For example, Apache Software Foundation";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "targetTechnologyName";
        final String attribute4Description     = "Name of the technology that the connectors access. For example, Apache Kafka.";
        final String attribute4DescriptionGUID = null;
        final String attribute7Name            = "recognizedAdditionalProperties";
        final String attribute7Description     = "List of additional connection property names supported by the connector implementations.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "recognizedSecuredProperties";
        final String attribute8Description     = "List of secured connection property names supported by the connector implementations.";
        final String attribute8DescriptionGUID = null;
        final String attribute9Name            = "recognizedConfigurationProperties";
        final String attribute9Description     = "List of secured connection property names supported by the connector implementations.";
        final String attribute9DescriptionGUID = null;

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
        property = archiveHelper.getMapStringBooleanTypeDefAttribute(attribute7Name,
                                                                     attribute7Description,
                                                                     attribute7DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringBooleanTypeDefAttribute(attribute8Name,
                                                                     attribute8Description,
                                                                     attribute8DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getMapStringBooleanTypeDefAttribute(attribute9Name,
                                                                     attribute9Description,
                                                                     attribute9DescriptionGUID);
        properties.add(property);

        entityDef.setPropertiesDefinition(properties);

        return entityDef;
    }


    private TypeDefPatch updateConnectorTypeEntity()
    {
        /*
         * Create the Patch
         */
        final String typeName = "ConnectorType";

        TypeDefPatch  typeDefPatch = archiveBuilder.getPatchForType(typeName);

        typeDefPatch.setUpdatedBy(originatorName);
        typeDefPatch.setUpdateTime(creationDate);

        /*
         * Build the attributes
         */
        List<TypeDefAttribute> properties = new ArrayList<>();
        TypeDefAttribute       property;

        final String attribute1Name            = "supportedAssetTypeName";
        final String attribute1Description     = "Type of asset supported by the connector implementation.";
        final String attribute1DescriptionGUID = null;
        final String attribute2Name            = "expectedDataFormat";
        final String attribute2Description     = "Description of the format of the data expected by the connector implementation.";
        final String attribute2DescriptionGUID = null;
        final String attribute3Name            = "connectorFrameworkName";
        final String attribute3Description     = "Name of the framework that the connector implements. The default is 'Open Connector Framework (OCF)'";
        final String attribute3DescriptionGUID = null;
        final String attribute4Name            = "connectorInterfaceLanguage";
        final String attribute4Description     = "The programming language used to implement the connector's interface.";
        final String attribute4DescriptionGUID = null;
        final String attribute5Name            = "connectorInterfaces";
        final String attribute5Description     = "List of interfaces supported by the connector.";
        final String attribute5DescriptionGUID = null;
        final String attribute6Name            = "targetTechnologySource";
        final String attribute6Description     = "Name of the organization providing the technology that the connectors access. For example, Apache Software Foundation";
        final String attribute6DescriptionGUID = null;
        final String attribute7Name            = "targetTechnologyName";
        final String attribute7Description     = "Name of the technology that the connectors access. For example, Apache Kafka.";
        final String attribute7DescriptionGUID = null;
        final String attribute8Name            = "targetTechnologyInterfaces";
        final String attribute8Description     = "Names of the technology's interfaces that the connectors use.";
        final String attribute8DescriptionGUID = null;
        final String attribute9Name            = "targetTechnologyVersions";
        final String attribute9Description     = "List of versions of the technology that the connector implementation supports.";
        final String attribute9DescriptionGUID = null;

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
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute5Name,
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
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute8Name,
                                                                attribute8Description,
                                                                attribute8DescriptionGUID);
        properties.add(property);
        property = archiveHelper.getArrayStringTypeDefAttribute(attribute9Name,
                                                                attribute9Description,
                                                                attribute9DescriptionGUID);
        properties.add(property);

        typeDefPatch.setPropertyDefinitions(properties);
        return typeDefPatch;
    }


    private RelationshipDef addConnectorImplementationChoiceRelationship()
    {
        final String guid = "633648f3-c951-4ad7-b975-9fc04e0f3d2e";

        final String name            = "ConnectorImplementationChoice";
        final String description     = "Relates a connector category for a specific type of technology with the connector types that support it.";
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
        final String                     end1EntityType               = "ConnectorCategory";
        final String                     end1AttributeName            = "connectorCategories";
        final String                     end1AttributeDescription     = "The categories that a connector type belongs to.";
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
        final String                     end2EntityType               = "ConnectorType";
        final String                     end2AttributeName            = "connectorTypes";
        final String                     end2AttributeDescription     = "The connector types that support the technology described in the connector category.";
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

    private void update0223Events()
    {
        this.archiveBuilder.addEntityDef(addKafkaTopicEntity());
    }


    private EntityDef addKafkaTopicEntity()
    {
        final String guid = OpenMetadataType.KAFKA_TOPIC.typeGUID;

        final String name            = OpenMetadataType.KAFKA_TOPIC.typeName;
        final String description     = OpenMetadataType.KAFKA_TOPIC.description;
        final String descriptionGUID = OpenMetadataType.KAFKA_TOPIC.descriptionGUID;
        final String descriptionWiki = OpenMetadataType.KAFKA_TOPIC.wikiURL;

        final String superTypeName = OpenMetadataType.TOPIC.typeName;

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID,
                                                 descriptionWiki);
    }


    /*
     * -------------------------------------------------------------------------------------------------------
     */


    /**
     * These types were originally coded in release 1.7 - however the top-level method was not called and so these types did not make the archive.
     */

    private void add0485DataProcessingPurposes()
    {
        this.archiveBuilder.addEntityDef(getDataProcessingDescriptionEntity());
        this.archiveBuilder.addEntityDef(getDataProcessingPurposeEntity());
        this.archiveBuilder.addEntityDef(getDataProcessingActionEntity());

        this.archiveBuilder.addRelationshipDef(this.getPermittedProcessingRelationship());
        this.archiveBuilder.addRelationshipDef(this.getApprovedPurposeRelationship());
        this.archiveBuilder.addRelationshipDef(this.getDetailedProcessingActionsRelationship());
        this.archiveBuilder.addRelationshipDef(this.getDataProcessingSpecificationRelationship());
        this.archiveBuilder.addRelationshipDef(this.getDataProcessingTargetRelationship());
    }


    private EntityDef getDataProcessingDescriptionEntity()
    {
        final String guid = "685f91fb-c74b-437b-a9b6-c5e557c6d3b2";

        final String name            = "DataProcessingDescription";
        final String description     = "A detailed description of the effect of some data processing.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.REFERENCEABLE.typeName;

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

        final String attribute1Name            = "displayName";
        final String attribute1Description     = "Name of the data processing description.";
        final String attribute1DescriptionGUID = null;

        final String attribute2Name            = "description";
        final String attribute2Description     = "Brief description of the data processing description.";
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


    private EntityDef getDataProcessingPurposeEntity()
    {
        final String guid = "9062df4c-9f4a-4012-a67a-968d7a3f4bcf";

        final String name            = "DataProcessingPurpose";
        final String description     = "Expected outcome, service or value from processing.";
        final String descriptionGUID = null;

        final String superTypeName = "GovernanceDefinition";

        return archiveHelper.getDefaultEntityDef(guid,
                                                 name,
                                                 this.archiveBuilder.getEntityDef(superTypeName),
                                                 description,
                                                 descriptionGUID);
    }


    private EntityDef getDataProcessingActionEntity()
    {
        final String guid = "7f53928f-9148-4710-ad37-47633f33cb08";

        final String name            = "DataProcessingAction";
        final String description     = "Description of the processing on a single target item.";
        final String descriptionGUID = null;

        final String superTypeName = OpenMetadataType.REFERENCEABLE.typeName;

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

        final String attribute1Name            = "displayName";
        final String attribute1Description     = "Name of the processing action.";
        final String attribute1DescriptionGUID = null;

        final String attribute2Name            = "description";
        final String attribute2Description     = "Brief description of the processing action.";
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


    private RelationshipDef getPermittedProcessingRelationship()
    {
        final String guid = "b472a2ec-f419-4d3f-86fb-e9d97365f961";

        final String name            = "PermittedProcessing";
        final String description     = "Relationship relates data processing descriptions with purposes (outcomes).";
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
        final String                     end1EntityType               = "DataProcessingPurpose";
        final String                     end1AttributeName            = "supportedPurposes";
        final String                     end1AttributeDescription     = "The supported outcomes from the processing.";
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
        final String                     end2EntityType               = "DataProcessingDescription";
        final String                     end2AttributeName            = "permittedProcessing";
        final String                     end2AttributeDescription     = "The description of the processing that is permitted for the purposes.";
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


    private RelationshipDef getApprovedPurposeRelationship()
    {
        final String guid = "33ec3aaa-dfb6-4f58-8d5d-c42d077be1b3";

        final String name            = "ApprovedPurpose";
        final String description     = "Relationship identifying the proposes that processes/people have permission to process data for.";
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
        final String                     end1AttributeName            = "approvedForPurposes";
        final String                     end1AttributeDescription     = "The people/processes that have permission to process data.";
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
        final String                     end2EntityType               = "DataProcessingPurpose";
        final String                     end2AttributeName            = "approvedPurposes";
        final String                     end2AttributeDescription     = "The purposes (outcomes) that the people/processes have permission for.";
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


    private RelationshipDef getDetailedProcessingActionsRelationship()
    {
        final String guid = "0ac0e793-6727-45d2-9403-06bd19d9ce2e";

        final String name            = "DetailedProcessingActions";
        final String description     = "Relationship identifying the individual actions in a data processing description.";
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
        final String                     end1EntityType               = "DataProcessingDescription";
        final String                     end1AttributeName            = "parentProcessingDescriptions";
        final String                     end1AttributeDescription     = "The aggregating processing descriptions.";
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
        final String                     end2EntityType               = "DataProcessingAction";
        final String                     end2AttributeName            = "dataProcessingActions";
        final String                     end2AttributeDescription     = "The individual actions that make up the data processing description.";
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


    private RelationshipDef getDataProcessingSpecificationRelationship()
    {
        final String guid = "1dfdec0f-f206-4db7-bac8-ec344205fb3c";

        final String name            = "DataProcessingSpecification";
        final String description     = "Relationship identifying the processing being performed by processes or people.";
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
        final String                     end1AttributeName            = "dataProcessingElements";
        final String                     end1AttributeDescription     = "The people/processes performing the processing.";
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
        final String                     end2EntityType               = "DataProcessingDescription";
        final String                     end2AttributeName            = "dataProcessingDescriptions";
        final String                     end2AttributeDescription     = "The description of the processing.";
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


    private RelationshipDef getDataProcessingTargetRelationship()
    {
        final String guid = "6ad18aa4-f5fc-47e7-99e1-80acfc536c9a";

        final String name            = "DataProcessingTarget";
        final String description     = "Relationship identifying the actions being performed on data.";
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
        final String                     end1EntityType               = "DataProcessingAction";
        final String                     end1AttributeName            = "dataProcessingActions";
        final String                     end1AttributeDescription     = "Actions being performed on the data.";
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
        final String                     end2AttributeName            = "dataProcessingTarget";
        final String                     end2AttributeDescription     = "The data that is being acted upon.";
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

