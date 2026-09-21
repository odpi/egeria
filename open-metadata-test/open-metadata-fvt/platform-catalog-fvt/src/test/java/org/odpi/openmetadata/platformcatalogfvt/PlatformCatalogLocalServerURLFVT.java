/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformcatalogfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PlatformCatalogLocalServerURLFVT covers the deployment where the integration daemon running the
 * cataloguer and the metadata access store it writes to are <b>not</b> on the same OMAG Server Platform.
 * <br>
 * The connector can always name the metadata access store's platform, because that is the server it talks
 * to, and it catalogs that platform in every deployment.  Where the daemon is somewhere else, that address
 * says nothing about where the connector is actually running, and the platform hosting the connector would
 * go uncatalogued unless somebody registered it by hand.  The connector now also catalogs the address the
 * local server records for itself - its {@code localServerURL}, reaching the connector as
 * {@code getLocalServerURL()} - whenever it is set and different.
 * <br>
 * <b>How the split is staged.</b>  A second OMAG Server Platform cannot be started in the same JVM, so the
 * suite changes the integration daemon's own recorded address to a different spelling of the platform's
 * address instead.  That is enough: the two addresses differ as strings, which is all the connector
 * compares, and both are reachable, so the platform behind the second one can actually be catalogued.  What
 * it does not reproduce is a genuinely separate platform with its own servers, so this class asserts only
 * on the registration and leaves what gets catalogued behind it to
 * {@link PlatformCatalogMultiplePlatformsFVT}.
 * <br>
 * This class runs last: it leaves the daemon's configuration document pointing at the other address.
 */
@ExtendWith(OMAGPlatformExtension.class)
@Order(4)
public class PlatformCatalogLocalServerURLFVT
{
    private static ConnectorContextBase connectorContext;
    private static OpenMetadataStore    openMetadataStore;


    /**
     * Catalog the ecosystem in the ordinary way before the daemon is moved.
     *
     * @throws Exception the platform was never catalogued - nothing after this can pass
     */
    @BeforeAll
    static void catalogThePlatform() throws Exception
    {
        connectorContext  = ConnectorContextFactory.newContext();
        openMetadataStore = connectorContext.getOpenMetadataStore();

        PlatformCatalogDriver.refreshUntilPlatformCatalogued(openMetadataStore);
    }


    /**
     * With the daemon and the metadata access store on the same platform - the ordinary deployment, and how
     * this suite is configured until the next test moves it - the connector has one platform to look after
     * and registers it once.  It does not register the same platform twice under two names just because it
     * has two ways of naming it.
     *
     * @throws Exception a retrieval failed
     */
    @Test
    @Order(1)
    @DisplayName("One platform is registered when the daemon and the metadata store share it")
    void oneRegistrationWhenTheDaemonSharesThePlatform() throws Exception
    {
        OpenMetadataElement platform = platformElementFor(OMAGPlatformExtension.getPlatformURLRoot());

        assertNotNull(platform,
                      "The platform hosting the metadata access store has not been catalogued at all");

        assertTrue(isCatalogTargetOfTheCataloguer(platform.getElementGUID()),
                   "The platform hosting the metadata access store is not one of the cataloguer's catalog targets");
    }


    /**
     * When the daemon records a different address for its own platform from the one the metadata access
     * store is reached at, the connector catalogs that platform too and takes it under management.
     * <br>
     * Without this, the platform an integration daemon runs on is invisible to open metadata in any split
     * deployment - which is every deployment beyond the smallest.
     *
     * @throws Exception a retrieval, configuration change or restart failed
     */
    @Test
    @Order(2)
    @DisplayName("A local server URL that differs from the metadata store's platform is catalogued too")
    void localServerURLIsCataloguedWhenItDiffers() throws Exception
    {
        String localPlatformURLRoot = OMAGPlatformExtension.getPlatformURLRoot().replace("localhost", "LOCALHOST");

        assertNotEquals(OMAGPlatformExtension.getPlatformURLRoot(),
                        localPlatformURLRoot,
                        "The two addresses are the same - the platform URL root is not what this test assumes");

        assertEquals(null,
                     platformElementFor(localPlatformURLRoot),
                     "This test needs to start from a platform that has not been catalogued at this address");

        /*
         * Tell the daemon it is running on the platform at the other address, and restart it so that the
         * connector's start() runs again and sees it.
         */
        OMAGServerConfigurationClient configurationClient = OMAGPlatformExtension.getServerConfigurationClient(OMAGPlatformExtension.INTEGRATION_DAEMON_NAME);

        OMAGPlatformExtension.setBasicServerProperties(configurationClient,
                                                       OMAGPlatformExtension.INITIAL_SERVER_ORGANIZATION_NAME,
                                                       OMAGPlatformExtension.INTEGRATION_DAEMON_DESCRIPTION,
                                                       localPlatformURLRoot);

        OMAGPlatformExtension.restartIntegrationDaemon();

        PlatformCatalogDriver.refreshUntilPlatformCatalogued(openMetadataStore);

        OpenMetadataElement localPlatform = platformElementFor(localPlatformURLRoot);

        String cataloguedPlatforms = PlatformCatalogFvtTestSupport.describe(PlatformCatalogFvtTestSupport.getSuitePlatformElements(openMetadataStore));

        assertNotNull(localPlatform,
                      () -> "The platform the integration daemon records as its own, " + localPlatformURLRoot
                              + ", was not catalogued.  The connector only ever catalogs the platform hosting"
                              + " the metadata access store, so an integration daemon running anywhere else is"
                              + " invisible:" + cataloguedPlatforms);

        assertTrue(isCatalogTargetOfTheCataloguer(localPlatform.getElementGUID()),
                   "The daemon's own platform was catalogued but not registered as a catalog target, so nothing"
                           + " will ever refresh it");

        /*
         * The platform hosting the metadata access store is still looked after.  It is the one platform the
         * connector can always name, and moving the daemon must not cost it.
         */
        OpenMetadataElement metadataStorePlatform = platformElementFor(OMAGPlatformExtension.getPlatformURLRoot());

        assertNotNull(metadataStorePlatform,
                      "The platform hosting the metadata access store is no longer catalogued");

        assertTrue(isCatalogTargetOfTheCataloguer(metadataStorePlatform.getElementGUID()),
                   "The platform hosting the metadata access store stopped being one of the cataloguer's catalog"
                           + " targets when the daemon was given a different address of its own");
    }


    /**
     * Return the platform element catalogued for an address, or null if there is not one.
     *
     * @param platformURLRoot address to look for
     * @return element or null
     * @throws Exception the retrieval failed
     */
    private static OpenMetadataElement platformElementFor(String platformURLRoot) throws Exception
    {
        return openMetadataStore.getMetadataElementByUniqueName(PlatformCatalogFvtTestSupport.expectedPlatformQualifiedName(platformURLRoot),
                                                                OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                new GetOptions());
    }


    /**
     * Is this platform one of the cataloguer's catalog targets?  A platform that is catalogued but not
     * registered is a snapshot that nothing will ever bring up to date.
     *
     * @param platformGUID platform to check
     * @return boolean
     * @throws Exception the retrieval failed
     */
    private static boolean isCatalogTargetOfTheCataloguer(String platformGUID) throws Exception
    {
        AssetClient assetClient = connectorContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);

        OpenMetadataRootElement platform = assetClient.getAssetByGUID(platformGUID, assetClient.getGetOptions());

        if ((platform != null) && (platform.getRefreshedByConnectors() != null))
        {
            for (RelatedMetadataElementSummary refreshedByConnector : platform.getRefreshedByConnectors())
            {
                if ((refreshedByConnector != null) &&
                        (IntegrationConnectorDefinition.OMAG_SERVER_PLATFORM_CATALOGUER.getGUID().equals(refreshedByConnector.getRelatedElement().getElementHeader().getGUID())))
                {
                    return true;
                }
            }
        }

        return false;
    }
}
