/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.egeria;

import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaSoftwareServerTemplateDefinition;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformProvider;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider;
import org.odpi.openmetadata.adminservices.configuration.registration.*;
import org.odpi.openmetadata.contentpacks.core.ContentPackDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationGroupDefinition;
import org.odpi.openmetadata.contentpacks.core.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.core.CorePackArchiveWriter;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStorePurpose;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;


/**
 * EgeriaArchiveWriter creates an open metadata archive that includes the connectors and reference data that
 * survey and catalog the Egeria's infrastructure..
 */
public class EgeriaArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public EgeriaArchiveWriter()
    {
        super(ContentPackDefinition.EGERIA_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.EGERIA_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.EGERIA_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.EGERIA_CONTENT_PACK.getArchiveFileName(),
              new OpenMetadataArchive[]{new CorePackArchiveWriter().getOpenMetadataArchive()});
    }


    /**
     * Implemented by subclass to add the content.
     */
    @Override
    public void getArchiveContent()
    {
        /*
         * Add valid metadata values for deployedImplementationType.  The GUIDs are saved in a look-up map
         * to make it easy to link other elements to these valid values later.
         */
        for (EgeriaDeployedImplementationType deployedImplementationType : EgeriaDeployedImplementationType.values())
        {
            this.addDeployedImplementationType(deployedImplementationType.getGUID(),
                                               deployedImplementationType.getDeployedImplementationType(),
                                               deployedImplementationType.getAssociatedTypeName(),
                                               deployedImplementationType.getQualifiedName(),
                                               deployedImplementationType.getDescription(),
                                               deployedImplementationType.getWikiLink(),
                                               deployedImplementationType.getIsATypeOf());
        }

        /*
         * Egeria also has valid values for its implementation.  These are useful when cataloguing Egeria.
         */
        Map<String, String> serviceGUIDs = new HashMap<>();

        /*
         * Next are the common services of Egeria.
         */
        for (CommonServicesDescription commonServicesDescription : CommonServicesDescription.values())
        {
            if (commonServicesDescription.getServiceDevelopmentStatus() != ComponentDevelopmentStatus.DEPRECATED)
            {
                String guid = this.addDeployedImplementationType(null,
                                                                 commonServicesDescription.getServiceName(),
                                                                 OpenMetadataType.SOFTWARE_SERVICE.typeName,
                                                                 commonServicesDescription.getServiceDescription(),
                                                                 commonServicesDescription.getServiceWiki());

                serviceGUIDs.put(commonServicesDescription.getServiceName(), guid);
            }
        }

        /*
         * These services support the governance servers.
         */
        for (GovernanceServicesDescription governanceServicesDescription : GovernanceServicesDescription.values())
        {
            if (governanceServicesDescription.getServiceDevelopmentStatus() != ComponentDevelopmentStatus.DEPRECATED)
            {
                String guid = this.addDeployedImplementationType(null,
                                                                 governanceServicesDescription.getServiceName(),
                                                                 OpenMetadataType.SOFTWARE_SERVICE.typeName,
                                                                 governanceServicesDescription.getServiceDescription(),
                                                                 governanceServicesDescription.getServiceWiki());

                serviceGUIDs.put(governanceServicesDescription.getServiceName(), guid);
            }
        }

        /*
         * The access services are found in the Metadata Access Server and Metadata Access Point OMAG Servers.
         */
        String serverTypeGUID  = archiveHelper.queryGUID(EgeriaDeployedImplementationType.METADATA_ACCESS_SERVER.getQualifiedName());

        for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
        {
            if (accessServiceDescription.getServiceDevelopmentStatus() != ComponentDevelopmentStatus.DEPRECATED)
            {
                String guid = this.addDeployedImplementationType(null,
                                                                 accessServiceDescription.getServiceName(),
                                                                 OpenMetadataType.SOFTWARE_SERVICE.typeName,
                                                                 accessServiceDescription.getServiceDescription(),
                                                                 accessServiceDescription.getServiceWiki());

                serviceGUIDs.put(accessServiceDescription.getServiceName(), guid);

                archiveHelper.addResourceListRelationshipByGUID(serverTypeGUID,
                                                                guid,
                                                                ResourceUse.HOSTED_SERVICE.getResourceUse(),
                                                                ResourceUse.HOSTED_SERVICE.getDescription());
            }
        }

        /*
         * View services are found in the View Server.  They call an access service.
         */
        serverTypeGUID = archiveHelper.queryGUID(EgeriaDeployedImplementationType.VIEW_SERVER.getQualifiedName());

        for (ViewServiceDescription viewServiceDescription : ViewServiceDescription.values())
        {
            if (viewServiceDescription.getViewServiceDevelopmentStatus() != ComponentDevelopmentStatus.DEPRECATED)
            {
                String guid = this.addDeployedImplementationType(null,
                                                                 viewServiceDescription.getViewServiceFullName(),
                                                                 OpenMetadataType.SOFTWARE_SERVICE.typeName,
                                                                 viewServiceDescription.getViewServiceDescription(),
                                                                 viewServiceDescription.getViewServiceWiki());

                archiveHelper.addResourceListRelationshipByGUID(serverTypeGUID,
                                                                guid,
                                                                ResourceUse.HOSTED_SERVICE.getResourceUse(),
                                                                ResourceUse.HOSTED_SERVICE.getDescription());

                archiveHelper.addResourceListRelationshipByGUID(guid,
                                                                serviceGUIDs.get(viewServiceDescription.getViewServicePartnerService()),
                                                                ResourceUse.CALLED_SERVICE.getResourceUse(),
                                                                ResourceUse.CALLED_SERVICE.getDescription());
            }
        }

        /*
         * Engine services are found in the Engine Host.   They call an access service.  They also
         * support a particular type of governance engine and governance service.
         */
        serverTypeGUID = archiveHelper.queryGUID(deployedImplementationTypeQNAMEs.get(EgeriaDeployedImplementationType.ENGINE_HOST.getDeployedImplementationType()));

        for (EngineServiceDescription engineServiceDescription : EngineServiceDescription.values())
        {
            if (engineServiceDescription.getEngineServiceDevelopmentStatus() != ComponentDevelopmentStatus.DEPRECATED)
            {
                String guid = this.addDeployedImplementationType(null,
                                                                 engineServiceDescription.getEngineServiceFullName(),
                                                                 OpenMetadataType.SOFTWARE_SERVICE.typeName,
                                                                 engineServiceDescription.getEngineServiceDescription(),
                                                                 engineServiceDescription.getEngineServiceWiki());

                archiveHelper.addResourceListRelationshipByGUID(serverTypeGUID,
                                                                guid,
                                                                ResourceUse.HOSTED_SERVICE.getResourceUse(),
                                                                ResourceUse.HOSTED_SERVICE.getDescription());

                archiveHelper.addResourceListRelationshipByGUID(guid,
                                                                serviceGUIDs.get(engineServiceDescription.getEngineServicePartnerService()),
                                                                ResourceUse.CALLED_SERVICE.getResourceUse(),
                                                                ResourceUse.CALLED_SERVICE.getDescription());

                String governanceEngineGUID  = archiveHelper.queryGUID(deployedImplementationTypeQNAMEs.get(engineServiceDescription.getHostedGovernanceEngineDeployedImplementationType()));
                String governanceServiceGUID = archiveHelper.queryGUID(deployedImplementationTypeQNAMEs.get(engineServiceDescription.getHostedGovernanceServiceDeployedImplementationType()));

                if (governanceEngineGUID != null)
                {
                    archiveHelper.addResourceListRelationshipByGUID(guid,
                                                                    governanceEngineGUID,
                                                                    ResourceUse.HOSTED_GOVERNANCE_ENGINE.getResourceUse(),
                                                                    ResourceUse.HOSTED_GOVERNANCE_ENGINE.getDescription());

                    if (governanceServiceGUID != null)
                    {
                        archiveHelper.addResourceListRelationshipByGUID(governanceEngineGUID,
                                                                        governanceServiceGUID,
                                                                        ResourceUse.HOSTED_CONNECTOR.getResourceUse(),
                                                                        ResourceUse.HOSTED_CONNECTOR.getDescription());
                    }
                }
            }
        }

        for (EgeriaSoftwareServerTemplateDefinition templateDefinition : EgeriaSoftwareServerTemplateDefinition.values())
        {
            createSoftwareServerCatalogTemplate(templateDefinition.getTemplateGUID(),
                                                templateDefinition.getQualifiedName(),
                                                templateDefinition.getTemplateName(),
                                                templateDefinition.getTemplateDescription(),
                                                templateDefinition.getTemplateVersionIdentifier(),
                                                templateDefinition.getDeployedImplementationType(),
                                                templateDefinition.getSoftwareCapabilityType(),
                                                templateDefinition.getSoftwareCapabilityName(),
                                                templateDefinition.getServerName(),
                                                templateDefinition.getElementVersionIdentifier(),
                                                templateDefinition.getServerDescription(),
                                                templateDefinition.getUserId(),
                                                templateDefinition.getConnectorTypeGUID(),
                                                templateDefinition.getNetworkAddress(),
                                                templateDefinition.getConfigurationProperties(),
                                                templateDefinition.getSecretsCollectionName(),
                                                templateDefinition.getSecretsStorePurpose(),
                                                templateDefinition.getSecretsStoreConnectorTypeGUID(),
                                                templateDefinition.getSecretsStoreFileName(),
                                                templateDefinition.getReplacementAttributes(),
                                                templateDefinition.getPlaceholders());
        }

        this.addDataAssetCatalogTemplates(ContentPackDefinition.EGERIA_CONTENT_PACK);


        /*
         * Create Egeria's integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.EGERIA_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.EGERIA_CONTENT_PACK,
                                       IntegrationGroupDefinition.EGERIA);

        /*
         * Add catalog templates
         */
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.EGERIA_CONTENT_PACK);
        this.addDataAssetCatalogTemplates(ContentPackDefinition.EGERIA_CONTENT_PACK);

        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.EGERIA_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.EGERIA_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.EGERIA_CONTENT_PACK);

        /*
         * Define the solution components for this solution.
         */
        this.addSolutionBlueprints(ContentPackDefinition.EGERIA_CONTENT_PACK);
        this.addSolutionLinkingWires(ContentPackDefinition.EGERIA_CONTENT_PACK);

        //this.addDefaultOMAGServerPlatform();

        /*
         * Saving the GUIDs means tha the guids in the archive are stable between runs of the archive writer.
         */
        archiveHelper.saveGUIDs();
        archiveHelper.saveUsedGUIDs();
    }


    /**
     * This entry is used by Runtime Manager to display the platform report for 9443
     */
    private void addDefaultOMAGServerPlatform()
    {
        final String               guid     = "44bf319f-1e41-4da1-b771-2753b92b631a";
        OMAGServerPlatformProvider provider = new OMAGServerPlatformProvider();

        DeployedImplementationTypeDefinition deployedImplementationType = EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM;
        DeployedImplementationTypeDefinition softwareCapabilityType     = DeployedImplementationType.USER_AUTHENTICATION_MANAGER;
        String                               softwareCapabilityName     = "User Token Manager";
        String serverName = "Default Local OMAG Server Platform";
        String userId = "defaultplatformnpa";
        String connectorTypeGUID = provider.getConnectorType().getGUID();
        String networkAddress = "https://localhost:9443";

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType() + "::" + serverName;
        String               versionIdentifier  = "6.0-SNAPSHOT";
        String               description     = "Default OMAG Server Platform running on local host and port 9443.";
        List<Classification> classifications = null;


        if (deployedImplementationType.getAssociatedClassification() != null)
        {
            classifications    = new ArrayList<>();
            classifications.add(archiveHelper.getServerPurposeClassification(deployedImplementationType.getAssociatedClassification(), null));
        }

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  serverName,
                                                  deployedImplementationType.getDeployedImplementationType(),
                                                  versionIdentifier,
                                                  description,
                                                  null,
                                                  null,
                                                  classifications);
        assert(guid.equals(assetGUID));

        archiveHelper.addSoftwareCapability(softwareCapabilityType.getAssociatedTypeName(),
                                            qualifiedName + "::" + softwareCapabilityName,
                                            softwareCapabilityName,
                                            null,
                                            softwareCapabilityType.getDeployedImplementationType(),
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            (Classification)null,
                                            assetGUID,
                                            deployedImplementationType.getAssociatedTypeName(),
                                            OpenMetadataType.ASSET.typeName,
                                            null);

        archiveHelper.addSupportedSoftwareCapabilityRelationship(qualifiedName + "::" + softwareCapabilityName,
                                                                 qualifiedName,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 1);

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
                                                        null,
                                                        qualifiedName + ":Endpoint",
                                                        serverName + " endpoint",
                                                        null,
                                                        networkAddress,
                                                        null,
                                                        null);

        archiveHelper.addServerEndpointRelationship(assetGUID, endpointGUID);

        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(PlaceholderProperty.SECRETS_STORE.getName(), "loading-bay/secrets/default.omsecrets");

        String connectionGUID = archiveHelper.addConnection(OpenMetadataType.VIRTUAL_CONNECTION.typeName,
                                                            qualifiedName + ":Connection",
                                                            serverName + " connection",
                                                            null,
                                                            userId,
                                                            null,
                                                            null,
                                                            null,
                                                            configurationProperties,
                                                            null,
                                                            connectorTypeGUID,
                                                            endpointGUID,
                                                            assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            OpenMetadataType.ASSET.typeName,
                                                            null);

        archiveHelper.addSecretsConnection(connectionGUID,
                                           qualifiedName,
                                           serverName,
                                           assetGUID,
                                           deployedImplementationType.getAssociatedTypeName(),
                                           OpenMetadataType.ASSET.typeName,
                                           null,
                                           "OMAGConnectors",
                                           SecretsStorePurpose.REST_BEARER_TOKEN.getName(),
                                           new YAMLSecretsStoreProvider().getConnectorType().getGUID(),
                                           "loading-bay/secrets/egeria-servers.omsecrets");

        archiveHelper.addConnectionForAsset(assetGUID, connectionGUID);
    }


    /**
     * Add a new valid values record for a deployed implementation type.
     *
     * @param guid unique identifier of technology type (deployedImplementationType)
     * @param deployedImplementationType preferred value
     * @param associatedTypeName         specific type name to tie it to (maybe null)
     * @param description                description of this value
     * @param wikiLink                   optional URL link to more information
     * @return unique identifier of the deployedImplementationType
     */
    private String addDeployedImplementationType(String guid,
                                                 String deployedImplementationType,
                                                 String associatedTypeName,
                                                 String description,
                                                 String wikiLink)
    {
        String qualifiedName = constructValidValueQualifiedName(associatedTypeName,
                                                                OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                null,
                                                                deployedImplementationType);

        return super.addDeployedImplementationType(guid, deployedImplementationType, associatedTypeName, qualifiedName, description, wikiLink, null);
    }


    /**
     * Main program to initiate the archive writer for the connector types for data store connectors supported by
     * the Egeria project.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            EgeriaArchiveWriter archiveWriter = new EgeriaArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}