/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog;

import org.odpi.openmetadata.adapters.connectors.controls.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.OMAGServerPlatformPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.properties.*;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStorePurpose;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.*;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaSoftwareServerTemplateDefinition;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.ffdc.OMAGConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformConnector;
import org.odpi.openmetadata.adminservices.configuration.registration.ServerTypeClassification;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.Map;

public class OMAGServerPlatformCatalogConnector extends DynamicIntegrationConnectorBase
{
    Map<String, PlatformDetails> monitoredPlatforms = new HashMap<>();

    final static String EGERIA_DEPLOYMENT_CATEGORY = "Egeria Deployment";


    /**
     * PlatformDetails acts as a cache of knowledge about a particular platform.
     */
    static class PlatformDetails
    {
        String                      platformRootURL       = null;
        String                      platformGUID          = null;
        String                      platformDisplayName   = null;
        OMAGServerPlatformConnector platformConnector     = null;

        Map<String, String>         knownServerNameToGUID = new HashMap<>();

        @Override
        public String toString()
        {
            return "PlatformDetails{" +
                    "platformRootURL='" + platformRootURL + '\'' +
                    ", platformGUID='" + platformGUID + '\'' +
                    ", platformDisplayName='" + platformDisplayName + '\'' +
                    ", platformConnector=" + platformConnector +
                    ", knownServerNameToGUID=" + knownServerNameToGUID +
                    '}';
        }
    }



    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        /*
         * Record the start
         */
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OMAGConnectorAuditCode.EGERIA_CONNECTOR_START.getMessageDefinition(connectorName,
                                                                                                   monitoredPlatforms.toString()));
        }

        try
        {
            /*
             * Check to see if the local platform is catalogued.  This may simply retrieve an existing element
             * or create a new one.
             */
            String platformGUID = catalogPlatform(integrationContext.getMetadataAccessServerPlatformURLRoot());

            /*
             * Now retrieve the full platform element to see if it is registered as a catalog target.
             */
            AssetClient   assetClient   = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);

            OpenMetadataRootElement softwarePlatform = assetClient.getAssetByGUID(platformGUID, assetClient.getGetOptions());

            if ((softwarePlatform != null) && (softwarePlatform.getProperties() instanceof SoftwareServerPlatformProperties softwareServerPlatformProperties))
            {
                if (softwarePlatform.getRefreshedByConnectors() != null)
                {
                    for (RelatedMetadataElementSummary refreshedByConnector : softwarePlatform.getRefreshedByConnectors())
                    {
                        if ((refreshedByConnector != null) &&
                                (refreshedByConnector.getRelatedElement().getElementHeader().getGUID().equals(integrationContext.getIntegrationConnectorGUID())))
                        {
                            /*
                             * Already registered as a catalog target.  Nothing to do.
                             */
                            return;
                        }
                    }
                }


                /*
                 * If the platform is not registered as a catalog target, then register it.
                 * Once registered, the platform's metadata will be refreshed on each refresh() call.
                 * This is performed by OMAGServerPlatformCatalogTargetProcessor.
                 */
                CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();
                catalogTargetProperties.setCatalogTargetName(softwareServerPlatformProperties.getDisplayName() + "(" + softwarePlatform.getElementHeader().getGUID() + ")");
                assetClient.addCatalogTarget(integrationContext.getIntegrationConnectorGUID(),
                                             platformGUID,
                                             assetClient.getMakeAnchorOptions(false),
                                             catalogTargetProperties);
            }
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                OMAGConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                 error.getClass().getName(),
                                                                                                 methodName,
                                                                                                 error.getMessage()));
        }
    }


    /**
     * Create a new catalog target processor (typically inherits from CatalogTargetProcessorBase).
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param catalogTargetContext   specialized context for this catalog target
     * @param connectorToTarget      connector to access the target resource
     * @return new processor based on the catalog target information
     * @throws ConnectorCheckedException  a problem with setting up the catalog target.
     * @throws UserNotAuthorizedException the connector has been disconnected
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget retrievedCatalogTarget, CatalogTargetContext catalogTargetContext, Connector connectorToTarget) throws ConnectorCheckedException, UserNotAuthorizedException
    {
        if (propertyHelper.isTypeOf(retrievedCatalogTarget.getCatalogTargetElement().getElementHeader(), OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName))
        {
            return new OMAGServerPlatformCatalogTargetProcessor(retrievedCatalogTarget,
                                                                 catalogTargetContext,
                                                                 connectorToTarget,
                                                                 connectorName,
                                                                 auditLog);
        }

        return null;
    }


    /**
     * Return the templateGUID for the server.
     *
     * @param serverType server type
     * @return string
     */
    private String getTemplateGUID(String serverType)
    {
        if (ServerTypeClassification.INTEGRATION_DAEMON.getServerTypeName().equals(serverType))
        {
            return EgeriaSoftwareServerTemplateDefinition.INTEGRATION_DAEMON_TEMPLATE.getTemplateGUID();
        }
        else if (ServerTypeClassification.ENGINE_HOST.getServerTypeName().equals(serverType))
        {
            return EgeriaSoftwareServerTemplateDefinition.ENGINE_HOST_TEMPLATE.getTemplateGUID();
        }
        else if (ServerTypeClassification.METADATA_ACCESS_STORE.getServerTypeName().equals(serverType) ||
                ServerTypeClassification.METADATA_ACCESS_POINT.getServerTypeName().equals(serverType) ||
                ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName().equals(serverType))
        {
            return EgeriaSoftwareServerTemplateDefinition.METADATA_ACCESS_SERVER_TEMPLATE.getTemplateGUID();
        }
        else if (ServerTypeClassification.VIEW_SERVER.getServerTypeName().equals(serverType))
        {
            return EgeriaSoftwareServerTemplateDefinition.VIEW_SERVER_TEMPLATE.getTemplateGUID();
        }
        else
        {
            return EgeriaSoftwareServerTemplateDefinition.OMAG_SERVER_PLATFORM_TEMPLATE.getTemplateGUID();
        }
    }


    /**
     * Return the url of the server.
     *
     * @param serverType server type
     * @return string
     */
    private String getURL(String serverType)
    {
        if (ServerTypeClassification.INTEGRATION_DAEMON.getServerTypeName().equals(serverType))
        {
            return "https://egeria-project.org/concepts/integration-daemon/";
        }
        else if (ServerTypeClassification.ENGINE_HOST.getServerTypeName().equals(serverType))
        {
            return "https://egeria-project.org/concepts/engine-host/";
        }
        else if (ServerTypeClassification.METADATA_ACCESS_STORE.getServerTypeName().equals(serverType))
        {
            return "https://egeria-project.org/concepts/metadata-access-store/";
        }
        else if (ServerTypeClassification.METADATA_ACCESS_POINT.getServerTypeName().equals(serverType))
        {
            return "https://egeria-project.org/concepts/metadata-access-point/";
        }
        else if (ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName().equals(serverType))
        {
            return "https://egeria-project.org/concepts/metadata-access-server/";
        }
        else if (ServerTypeClassification.VIEW_SERVER.getServerTypeName().equals(serverType))
        {
            return "https://egeria-project.org/concepts/view-server/";
        }
        else
        {
            return "https://egeria-project.org/concepts/omag-server-platform/";
        }
    }



    /**
     * Return the unique identifier of the solution component for the server.
     *
     * @param serverType server type
     * @return string
     */
    private String getSolutionComponentGUID(String serverType)
    {
        if (ServerTypeClassification.INTEGRATION_DAEMON.getServerTypeName().equals(serverType))
        {
            return EgeriaDeployedImplementationType.INTEGRATION_DAEMON.getSolutionComponentGUID();
        }
        else if (ServerTypeClassification.ENGINE_HOST.getServerTypeName().equals(serverType))
        {
            return EgeriaDeployedImplementationType.ENGINE_HOST.getSolutionComponentGUID();
        }
        else if (ServerTypeClassification.METADATA_ACCESS_STORE.getServerTypeName().equals(serverType))
        {
            return EgeriaDeployedImplementationType.METADATA_ACCESS_STORE.getSolutionComponentGUID();
        }
        else if (ServerTypeClassification.METADATA_ACCESS_POINT.getServerTypeName().equals(serverType) ||
                 ServerTypeClassification.METADATA_ACCESS_SERVER.getServerTypeName().equals(serverType))
        {
            return EgeriaDeployedImplementationType.METADATA_ACCESS_SERVER.getSolutionComponentGUID();
        }
        else if (ServerTypeClassification.VIEW_SERVER.getServerTypeName().equals(serverType))
        {
            return EgeriaDeployedImplementationType.VIEW_SERVER.getSolutionComponentGUID();
        }

        return EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getSolutionComponentGUID();
    }



    /**
     * Create a metadata element to represent the local platform.
     *
     * @param platformURLRoot location of the platform
     * @return platform GUID
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private String catalogPlatform(String platformURLRoot) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        OpenMetadataStore openMetadataAccess = integrationContext.getOpenMetadataStore();

        String templateGUID = this.getTemplateGUID(null);

        Map<String, String> placeholderProperties = new HashMap<>();

        /*
         * These properties are initial properties for platform.  These will be overridden by the real values in the
         * platform application.properties.
         */
        placeholderProperties.put(OMAGServerPlatformPlaceholderProperty.PLATFORM_URL_ROOT.getName(), platformURLRoot);
        placeholderProperties.put(OMAGServerPlatformPlaceholderProperty.PLATFORM_NAME.getName(), "Local OMAG Server Platform");
        placeholderProperties.put(OMAGServerPlatformPlaceholderProperty.PLATFORM_DESCRIPTION.getName(), "OMAG Server Platform running on " + platformURLRoot + ".");
        placeholderProperties.put(OMAGServerPlatformPlaceholderProperty.PLATFORM_USER_ID.getName(), null);
        placeholderProperties.put(PlaceholderProperty.SECRETS_STORE.getName(), super.getSecretsLocation(SecretsStorePurpose.REST_BEARER_TOKEN.getName()));
        placeholderProperties.put(PlaceholderProperty.SECRETS_COLLECTION_NAME.getName(), super.getSecretsCollectionName(SecretsStorePurpose.REST_BEARER_TOKEN.getName()));
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), null);
        placeholderProperties.put(PlaceholderProperty.ORGANIZATION_NAME.getName(), null);

        String platformQualifiedName = EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType() + "::" + platformURLRoot;

        /*
         * Replacement properties are used to override the standard naming conventions for software servers and to
         * ensure this connector is able to match the operational configuration with the values in open metadata.
         * It is also an opportunity to test this feature.
         */
        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               platformQualifiedName);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                             EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.CATEGORY.name,
                                                             EGERIA_DEPLOYMENT_CATEGORY);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.URL.name,
                                                             this.getURL(null));

        String platformGUID = openMetadataAccess.getMetadataElementFromTemplate(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName,
                                                                              null,
                                                                              true,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              templateGUID,
                                                                              elementProperties,
                                                                              null,
                                                                              placeholderProperties,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              false);

        GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();

        String solutionComponentGUID = this.getSolutionComponentGUID(null);

        if (solutionComponentGUID != null)
        {
            ImplementedByProperties implementedByProperties = new ImplementedByProperties();

            implementedByProperties.setRole("running instance");
            implementedByProperties.setDescription("Server instance discovered by " + connectorName + ".");

            governanceDefinitionClient.linkDesignToImplementation(solutionComponentGUID, platformGUID, new MakeAnchorOptions(governanceDefinitionClient.getMetadataSourceOptions()), implementedByProperties);
        }

        return platformGUID;
    }
}
