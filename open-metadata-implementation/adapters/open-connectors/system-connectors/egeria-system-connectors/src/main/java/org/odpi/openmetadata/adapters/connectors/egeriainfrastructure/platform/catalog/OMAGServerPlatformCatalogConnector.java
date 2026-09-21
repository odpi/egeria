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
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.implementations.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OMAGServerPlatformCatalogConnector extends DynamicIntegrationConnectorBase
{
    /**
     * The platforms this connector has catalogued and registered as its own catalog targets, keyed by the
     * address each was reached at.  A platform in here has been dealt with; one that is missing is one that
     * {@link #registerLocalPlatforms} still has to get to, which is how a registration that failed at
     * start-up is picked up again by the next refresh.
     * <br>
     * These are only the platforms this connector registers for itself.  Platforms attached as catalog
     * targets by anybody else are not in here and are not affected by any of this - they are read afresh
     * from open metadata on every refresh.
     */
    private final Map<String, String> monitoredPlatforms = new LinkedHashMap<>();

    final static String EGERIA_DEPLOYMENT_CATEGORY = "Egeria Deployment";



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

        this.registerLocalPlatforms(methodName);

        /*
         * Record the start.  This is logged after the platforms have been registered rather than before,
         * so that the message says which platforms are being monitored instead of always saying none.
         */
        logRecord(methodName,
                  OMAGConnectorAuditCode.EGERIA_CONNECTOR_START.getMessageDefinition(connectorName,
                                                                                     monitoredPlatforms.keySet().toString()));
    }


    /**
     * Refresh the connector's catalog targets.
     * <br>
     * Before the ordinary refresh, any local platform that could not be registered earlier is tried again.
     * Registering a platform means writing to the metadata access store, and a store that was restarting,
     * or had not finished loading, when this connector started would otherwise leave it with no catalog
     * target at all and no way of ever acquiring one: {@code start()} runs once, and its failures are
     * swallowed so as not to take the whole integration daemon down with them.
     *
     * @throws ConnectorCheckedException a problem with the connector
     * @throws UserNotAuthorizedException the connector was disconnected
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "refresh";

        this.registerLocalPlatforms(methodName);

        super.refresh();
    }


    /**
     * Make sure each of the platforms this connector is responsible for is catalogued and registered as one
     * of its catalog targets.  A platform that has already been dealt with is skipped, so this is cheap to
     * call on every refresh.
     *
     * @param methodName calling method
     */
    private void registerLocalPlatforms(String methodName)
    {
        for (String platformURLRoot : this.getLocalPlatformURLRoots())
        {
            if (! monitoredPlatforms.containsKey(platformURLRoot))
            {
                try
                {
                    monitoredPlatforms.put(platformURLRoot, this.registerPlatformAsCatalogTarget(platformURLRoot));
                }
                catch (Exception error)
                {
                    /*
                     * Reported rather than rethrown: one platform that cannot be registered yet must not
                     * stop the other, nor stop the connector refreshing the catalog targets it does have.
                     * The next refresh tries again.
                     */
                    logRecord(methodName,
                              OMAGConnectorAuditCode.PLATFORM_REGISTRATION_DEFERRED.getMessageDefinition(connectorName,
                                                                                                         platformURLRoot,
                                                                                                         error.getClass().getName(),
                                                                                                         error.getMessage()));
                }
            }
        }
    }


    /**
     * Return the addresses of the platforms this connector registers for itself.
     * <br>
     * The metadata access server's platform is always one of them - it is the platform this connector can
     * always name, and in the ordinary deployment, where the integration daemon and the metadata store sit
     * together, it is also the platform the connector is running on.
     * <br>
     * Where they are not together, the connector is running somewhere the metadata access server's address
     * says nothing about, and that platform deserves cataloguing too.  The local server's own address is
     * added in that case.  It is null on a server configured without a localServerURL, which is why it is
     * only ever added when it is both set and different.
     *
     * @return list of platform addresses, in the order they should be registered
     */
    private List<String> getLocalPlatformURLRoots()
    {
        List<String> platformURLRoots = new ArrayList<>();

        String metadataAccessServerPlatformURLRoot = integrationContext.getMetadataAccessServerPlatformURLRoot();

        if (metadataAccessServerPlatformURLRoot != null)
        {
            platformURLRoots.add(metadataAccessServerPlatformURLRoot);
        }

        String localServerURL = integrationContext.getLocalServerURL();

        if ((localServerURL != null) && (! localServerURL.equals(metadataAccessServerPlatformURLRoot)))
        {
            platformURLRoots.add(localServerURL);
        }

        return platformURLRoots;
    }


    /**
     * Catalog the platform at an address and register it as one of this connector's catalog targets.  Both
     * steps find and reuse what is already there, so calling this for a platform that is already catalogued
     * and already registered changes nothing.
     *
     * @param platformURLRoot address of the platform
     * @return unique identifier of the platform element
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private String registerPlatformAsCatalogTarget(String platformURLRoot) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "registerPlatformAsCatalogTarget";

        /*
         * Check to see if the platform is catalogued.  This may simply retrieve an existing element
         * or create a new one.
         */
        String platformGUID = catalogPlatform(platformURLRoot);

        /*
         * Now retrieve the full platform element to see if it is registered as a catalog target.
         */
        AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);

        OpenMetadataRootElement softwarePlatform = assetClient.getAssetByGUID(platformGUID, assetClient.getGetOptions());

        if ((softwarePlatform != null) && (softwarePlatform.getProperties() instanceof SoftwareServerPlatformProperties softwareServerPlatformProperties))
        {
            if (this.noteLocalMetadataCollection(softwarePlatform))
            {
                /*
                 * Already registered as a catalog target.  Nothing to do.
                 */
                return platformGUID;
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

            logRecord(methodName,
                      OMAGConnectorAuditCode.PLATFORM_CATALOG_TARGET_ADDED.getMessageDefinition(connectorName,
                                                                                                platformURLRoot,
                                                                                                platformGUID));

            /*
             * Read the platform back so that the relationship just written can be looked at - see
             * noteLocalMetadataCollection for why that is worth a second read.
             */
            this.noteLocalMetadataCollection(assetClient.getAssetByGUID(platformGUID, assetClient.getGetOptions()));
        }

        return platformGUID;
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
     * Look for this connector's own catalog target on a platform and, if it is there, tell the framework
     * which metadata collection homes it.
     * <br>
     * That identifier is what lets an integration daemon tell its own catalog targets from those registered
     * by another daemon whose metadata access store is in the same cohort - see
     * {@code RequestedCatalogTargetsManager.setLocalMetadataCollectionId}.  It has to be read off an
     * instance this connector is certain it created, and a catalog target it registered itself is exactly
     * that: written through this daemon's own metadata access store, and therefore homed in that store's
     * metadata collection.
     * <br>
     * Nothing else available to a connector will do.  The name of a metadata collection is optional and can
     * be set to anything, so it cannot stand in for the identifier, and no API hands a connector the
     * identifier of the store it writes through.
     *
     * @param softwarePlatform platform element to look at - may be null
     * @return true if this connector already has this platform as a catalog target
     */
    private boolean noteLocalMetadataCollection(OpenMetadataRootElement softwarePlatform)
    {
        if ((softwarePlatform == null) || (softwarePlatform.getRefreshedByConnectors() == null))
        {
            return false;
        }

        for (RelatedMetadataElementSummary refreshedByConnector : softwarePlatform.getRefreshedByConnectors())
        {
            if ((refreshedByConnector != null) &&
                    (refreshedByConnector.getRelatedElement().getElementHeader().getGUID().equals(integrationContext.getIntegrationConnectorGUID())))
            {
                if ((refreshedByConnector.getRelationshipHeader() != null) &&
                        (refreshedByConnector.getRelationshipHeader().getOrigin() != null))
                {
                    String homeMetadataCollectionId = refreshedByConnector.getRelationshipHeader().getOrigin().getHomeMetadataCollectionId();

                    if (homeMetadataCollectionId != null)
                    {
                        super.catalogTargetsManager.setLocalMetadataCollectionId(homeMetadataCollectionId);
                    }
                }

                return true;
            }
        }

        return false;
    }


    /**
     * Rename the local platform's element if an earlier release of this connector left it under a name that
     * cannot be found again.
     * <br>
     * A platform element is created with a qualified name built from the platform's address, and that is the
     * name {@link #catalogPlatform} looks it up by every time the connector starts.  Earlier releases
     * replaced that name on the first refresh with one built from the platform's own name and organization -
     * which identifies no particular running platform, and which the next start could not find.  The result
     * was a fresh platform element, and a fresh catalog target, on every restart.
     * <br>
     * The element is recognised here by the platform address recorded in its additional properties, which is
     * written on every refresh and is not affected by any of the renaming.  An element that was created but
     * never refreshed has no additional properties - but it also still has its original name, so the
     * ordinary lookup finds it and there is nothing here to do.
     *
     * @param platformURLRoot address of the local platform
     * @param platformQualifiedName the name the platform element should be carrying
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private void adoptPlatformCataloguedUnderAnOlderName(String platformURLRoot,
                                                         String platformQualifiedName) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "adoptPlatformCataloguedUnderAnOlderName";

        OpenMetadataStore openMetadataAccess = integrationContext.getOpenMetadataStore();

        /*
         * Nothing to correct if an element is already carrying the name.  This is the ordinary case - every
         * start after the first one - so it is settled with a single lookup.
         */
        if (openMetadataAccess.getMetadataElementByUniqueName(platformQualifiedName,
                                                              OpenMetadataProperty.QUALIFIED_NAME.name) != null)
        {
            return;
        }

        OpenMetadataElement previouslyCatalogued = this.getPlatformElementForAddress(openMetadataAccess, platformURLRoot);

        if (previouslyCatalogued == null)
        {
            return;
        }

        String previousQualifiedName = propertyHelper.getStringProperty(connectorName,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        previouslyCatalogued.getElementProperties(),
                                                                        methodName);

        SoftwareServerPlatformProperties softwareServerPlatformProperties = new SoftwareServerPlatformProperties();

        softwareServerPlatformProperties.setQualifiedName(platformQualifiedName);

        AssetClient assetClient = integrationContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);

        assetClient.updateAsset(previouslyCatalogued.getElementGUID(),
                                assetClient.getUpdateOptions(true),
                                softwareServerPlatformProperties);

        logRecord(methodName,
                  OMAGConnectorAuditCode.PLATFORM_NAME_MIGRATED.getMessageDefinition(connectorName,
                                                                                     previouslyCatalogued.getElementGUID(),
                                                                                     previousQualifiedName,
                                                                                     platformQualifiedName));
    }


    /**
     * Return the platform element that describes the platform at a particular address, or null if no such
     * element has been catalogued.
     * <br>
     * Every platform element is read and its recorded address compared, rather than the repository being
     * asked to do the matching.  The address lives in the element's additional properties, which is a map,
     * and there is no property condition that reaches inside one.  An ecosystem has a handful of platforms
     * in it, and this runs once per connector start, so reading them is not worth avoiding.
     *
     * @param openMetadataAccess access to open metadata
     * @param platformURLRoot address to look for
     * @return the element, or null
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException no repo
     * @throws UserNotAuthorizedException security problem
     */
    private OpenMetadataElement getPlatformElementForAddress(OpenMetadataStore openMetadataAccess,
                                                             String            platformURLRoot) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getPlatformElementForAddress";

        int startFrom = 0;

        List<OpenMetadataElement> platformElements = openMetadataAccess.findMetadataElements(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             startFrom,
                                                                                             integrationContext.getMaxPageSize());

        while ((platformElements != null) && (! platformElements.isEmpty()))
        {
            for (OpenMetadataElement platformElement : platformElements)
            {
                /*
                 * The content packs ship template platforms, and a template is not a running platform.
                 */
                if ((platformElement != null) && (! this.isTemplate(platformElement)))
                {
                    Map<String, String> additionalProperties = propertyHelper.getStringMapFromProperty(connectorName,
                                                                                                        OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                                                        platformElement.getElementProperties(),
                                                                                                        methodName);

                    if ((additionalProperties != null) && (platformURLRoot.equals(additionalProperties.get("platformURLRoot"))))
                    {
                        return platformElement;
                    }
                }
            }

            startFrom = startFrom + platformElements.size();

            platformElements = openMetadataAccess.findMetadataElements(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        startFrom,
                                                                        integrationContext.getMaxPageSize());
        }

        return null;
    }


    /**
     * Is this element one of the content packs' templates?
     *
     * @param element element to test
     * @return boolean
     */
    private boolean isTemplate(OpenMetadataElement element)
    {
        if (element.getClassifications() != null)
        {
            for (AttachedClassification classification : element.getClassifications())
            {
                if ((classification != null) && (OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName.equals(classification.getClassificationName())))
                {
                    return true;
                }
            }
        }

        return false;
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

        String platformQualifiedName = EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType() + "::" + platformURLRoot;

        /*
         * An earlier release of this connector renamed the platform element on its first refresh, to a name
         * built from the platform's own name and organization.  Put that right before going any further: the
         * template call below finds an element that already carries the name it is about to use, so once the
         * old element has been renamed it is simply found rather than duplicated.
         */
        this.adoptPlatformCataloguedUnderAnOlderName(platformURLRoot, platformQualifiedName);

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

        /*
         * The platform has not been asked what it calls itself yet - that is the first refresh's job, and it
         * replaces this with the platform's real name.  A value is still needed here, because a placeholder
         * with nothing to substitute is left in the element verbatim, and an element carrying a literal
         * "~{resourceName}~" is worse than one named after the address it was found at.
         */
        placeholderProperties.put(PlaceholderProperty.RESOURCE_NAME.getName(), platformURLRoot);
        placeholderProperties.put(PlaceholderProperty.SECRETS_STORE.getName(), super.getSecretsLocation(SecretsStorePurpose.REST_BEARER_TOKEN.getName()));
        placeholderProperties.put(PlaceholderProperty.SECRETS_COLLECTION_NAME.getName(), super.getSecretsCollectionName(SecretsStorePurpose.REST_BEARER_TOKEN.getName()));
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), null);
        placeholderProperties.put(PlaceholderProperty.ORGANIZATION_NAME.getName(), null);

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
