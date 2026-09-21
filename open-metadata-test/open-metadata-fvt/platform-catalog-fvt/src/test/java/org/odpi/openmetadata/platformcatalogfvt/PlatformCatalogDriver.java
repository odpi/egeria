/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformcatalogfvt;

import org.odpi.openmetadata.adapters.connectors.controls.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaSoftwareServerTemplateDefinition;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.OMAGServerPlatformPlaceholderProperty;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PlatformCatalogDriver is how the tests make the cataloguer act and then wait for the result.
 * <br>
 * Each refresh is synchronous, but one of them is not enough to get from a standing start to a catalogued
 * ecosystem - see {@link #refreshUntilPlatformCatalogued}, which is where that is explained.
 */
final class PlatformCatalogDriver
{
    private static final PropertyHelper propertyHelper = new PropertyHelper();

    /**
     * The cataloguer's own secrets store.  The connections the Egeria content pack lays down name this path,
     * relative to the platform's working directory, and the platform element the cataloguer creates for the
     * local platform is given the same pair - so a platform element this suite creates by hand has to use
     * them too, or the cataloguer will not be able to open a connector to it.
     */
    private static final String CATALOGUER_SECRETS_STORE      = "secrets/egeria-servers.omsecrets";
    private static final String CATALOGUER_SECRETS_COLLECTION = "OMAGServerPlatformCataloguer";

    private PlatformCatalogDriver()
    {
    }


    /**
     * Refresh the cataloguer until this suite's platform has actually been catalogued.
     * <br>
     * "Catalogued" deliberately means more than "a platform element exists".  The connector creates that
     * element in its {@code start()}, before it has read anything from the platform at all, so an element is
     * there from the moment the integration daemon comes up - and a test that waits only for it is reading a
     * stub.  The pass has finished when {@code updatePlatform} has replaced the interim display name with
     * what the platform calls itself, and every server the platform runs has been attached to it.
     * <br>
     * Several refreshes may be needed.  Everything the connector's {@code refresh()} does is wrapped in one
     * catch-all, so a failure at any point - a REST call to the platform, a name that collides with
     * something already in the repository - abandons the rest of that pass silently, and the work that was
     * left undone is only picked up by a later one.  The audit log is where the reason is; the message to
     * look for is {@code OMAG-CONNECTORS-0001}.
     *
     * @param openMetadataStore store to read through
     * @return the platform element
     * @throws Exception the platform was never catalogued
     */
    static OpenMetadataElement refreshUntilPlatformCatalogued(OpenMetadataStore openMetadataStore) throws Exception
    {
        long timeoutMilliseconds = OMAGPlatformExtension.getLongProperty("platform.catalog.fvt.integration.daemon.timeout.seconds", 180) * 1000;
        long pollMilliseconds    = OMAGPlatformExtension.getLongProperty("platform.catalog.fvt.integration.daemon.poll.seconds", 2) * 1000;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        while (System.currentTimeMillis() < giveUpTime)
        {
            OMAGPlatformExtension.refreshCataloguer();

            for (OpenMetadataElement platform : PlatformCatalogFvtTestSupport.getSuitePlatformElements(openMetadataStore))
            {
                /*
                 * Two things have to be true, and waiting for only the first of them is what used to make
                 * this suite flake.
                 *
                 * The display name says a refresh has read the platform report: the connector's start()
                 * creates the element with the interim name it has to hand - the platform's address - and
                 * only a refresh can replace it with what the platform calls itself.
                 *
                 * Every server being attached says that refresh got all the way to the end.  Waiting for
                 * one server is not enough: the servers are catalogued one at a time, and a test that
                 * starts as soon as the first one appears is racing the connector through the rest.
                 */
                if ((OMAGPlatformExtension.getPlatformName().equals(PlatformCatalogFvtTestSupport.getDisplayName(platform)))
                            && (PlatformCatalogFvtTestSupport.getHostedServers(openMetadataStore, platform.getElementGUID()).size()
                                        >= OMAGPlatformExtension.SUITE_SERVER_NAMES.size()))
                {
                    return platform;
                }
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new IllegalStateException("The OMAG Server Platform Cataloguer did not finish cataloguing the platform at "
                                                + OMAGPlatformExtension.getPlatformURLRoot() + " within "
                                                + (timeoutMilliseconds / 1000) + " seconds.  Its refresh() swallows every"
                                                + " failure, so the reason will be in the integration daemon's audit log"
                                                + " as OMAG-CONNECTORS-0001 - look in"
                                                + " build/platform-catalog-fvt-data/logs/audit.log, or in the console"
                                                + " output that Gradle captured.");
    }


    /**
     * Rename a platform element, so that a test can put the ecosystem into the state an earlier release of
     * the connector would have left it in.
     *
     * @param connectorContext context to update through
     * @param platformGUID element to rename
     * @param qualifiedName name to give it
     * @throws Exception the rename failed
     */
    static void renamePlatform(ConnectorContextBase connectorContext,
                               String               platformGUID,
                               String               qualifiedName) throws Exception
    {
        SoftwareServerPlatformProperties softwareServerPlatformProperties = new SoftwareServerPlatformProperties();

        softwareServerPlatformProperties.setQualifiedName(qualifiedName);

        AssetClient assetClient = connectorContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);

        assetClient.updateAsset(platformGUID, assetClient.getUpdateOptions(true), softwareServerPlatformProperties);
    }


    /**
     * Create a second SoftwareServerPlatform element for the same running platform, reached through a
     * different spelling of its address, and register it as another catalog target of the cataloguer.
     * <br>
     * This is how the suite gets two platforms in front of one cataloguer without starting a second OMAG
     * Server Platform in the same JVM (which the platform's static service registries do not allow).  It is
     * a fair stand-in for the case being tested: the two catalog targets are reached at different URL roots,
     * and every server behind them has the same name - which is precisely the configuration that a real
     * second platform with default server names would present.
     * <br>
     * The element is created exactly the way the cataloguer's own {@code start()} creates the local
     * platform: from the Egeria content pack's OMAG Server Platform template, with a URL-based qualified
     * name supplied as a replacement property.
     *
     * @param connectorContext context to create through
     * @param platformURLRoot the address this platform element points at
     * @return GUID of the new platform element
     * @throws Exception the platform element could not be created or registered
     */
    static String catalogSecondPlatform(ConnectorContextBase connectorContext,
                                        String               platformURLRoot) throws Exception
    {
        OpenMetadataStore openMetadataStore = connectorContext.getOpenMetadataStore();

        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(OMAGServerPlatformPlaceholderProperty.PLATFORM_URL_ROOT.getName(), platformURLRoot);
        placeholderProperties.put(OMAGServerPlatformPlaceholderProperty.PLATFORM_NAME.getName(), "Second view of the platform-catalog-fvt platform");
        placeholderProperties.put(OMAGServerPlatformPlaceholderProperty.PLATFORM_DESCRIPTION.getName(), "The platform-catalog-fvt platform, reached at " + platformURLRoot + ".");
        placeholderProperties.put(OMAGServerPlatformPlaceholderProperty.PLATFORM_USER_ID.getName(), null);
        placeholderProperties.put(PlaceholderProperty.SECRETS_STORE.getName(), CATALOGUER_SECRETS_STORE);
        placeholderProperties.put(PlaceholderProperty.SECRETS_COLLECTION_NAME.getName(), CATALOGUER_SECRETS_COLLECTION);
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), null);
        placeholderProperties.put(PlaceholderProperty.ORGANIZATION_NAME.getName(), null);

        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                              OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                              PlatformCatalogFvtTestSupport.OMAG_SERVER_PLATFORM_TYPE
                                                                                      + "::" + platformURLRoot);

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                            OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                            EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM.getDeployedImplementationType());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                            OpenMetadataProperty.CATEGORY.name,
                                                            "Egeria Deployment");

        String platformGUID = openMetadataStore.getMetadataElementFromTemplate(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName,
                                                                              null,
                                                                              true,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              EgeriaSoftwareServerTemplateDefinition.OMAG_SERVER_PLATFORM_TEMPLATE.getTemplateGUID(),
                                                                              elementProperties,
                                                                              null,
                                                                              placeholderProperties,
                                                                              null,
                                                                              null,
                                                                              null,
                                                                              false);

        AssetClient assetClient = connectorContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);

        CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();

        catalogTargetProperties.setCatalogTargetName("Second view of the platform-catalog-fvt platform(" + platformGUID + ")");

        assetClient.addCatalogTarget(IntegrationConnectorDefinition.OMAG_SERVER_PLATFORM_CATALOGUER.getGUID(),
                                     platformGUID,
                                     assetClient.getMakeAnchorOptions(false),
                                     catalogTargetProperties);

        return platformGUID;
    }
}
