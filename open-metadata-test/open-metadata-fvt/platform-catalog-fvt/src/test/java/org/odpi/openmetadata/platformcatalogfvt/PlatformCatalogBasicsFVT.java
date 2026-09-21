/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformcatalogfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PlatformCatalogBasicsFVT covers what the OMAG Server Platform Cataloguer is for: given a running OMAG
 * Server Platform as a catalog target, it should produce one platform element that describes it, one server
 * element for each server the platform has run, and the DeployedOn relationships that put those servers on
 * that platform - and doing it again should not produce any of it twice.
 */
@ExtendWith(OMAGPlatformExtension.class)
@Order(1)
public class PlatformCatalogBasicsFVT
{
    private static OpenMetadataStore   openMetadataStore;
    private static OpenMetadataElement platformElement;


    /**
     * Let the cataloguer catalog the platform before any of the assertions run.  The connector registers the
     * local platform as its own catalog target when it starts, so this is a matter of waiting for it rather
     * than setting anything up.
     *
     * @throws Exception the platform was never catalogued - nothing after this can pass
     */
    @BeforeAll
    static void catalogThePlatform() throws Exception
    {
        openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        platformElement   = PlatformCatalogDriver.refreshUntilPlatformCatalogued(openMetadataStore);
    }


    /**
     * The platform the cataloguer is running on is catalogued exactly once, and described from the
     * platform's own public properties rather than from whatever the element was created with.
     *
     * @throws Exception a retrieval failed
     */
    @Test
    @DisplayName("The local platform is catalogued once, and named from its public properties")
    void platformIsCataloguedOnce() throws Exception
    {
        List<OpenMetadataElement> platforms = PlatformCatalogFvtTestSupport.getSuitePlatformElements(openMetadataStore);

        assertEquals(1,
                     platforms.size(),
                     () -> "Expected exactly one platform element for the platform under test, found "
                             + platforms.size() + ":" + PlatformCatalogFvtTestSupport.describe(platforms));

        OpenMetadataElement platform = platforms.get(0);

        assertEquals(PlatformCatalogFvtTestSupport.expectedPlatformQualifiedName(OMAGPlatformExtension.getPlatformURLRoot()),
                     PlatformCatalogFvtTestSupport.getQualifiedName(platform),
                     "The platform element's qualified name is not the one the cataloguer's naming convention produces");

        assertEquals(PlatformCatalogFvtTestSupport.expectedPlatformResourceName(OMAGPlatformExtension.getPlatformOrganizationName(),
                                                                                OMAGPlatformExtension.getPlatformName()),
                     PlatformCatalogFvtTestSupport.getResourceName(platform),
                     "The platform element's resource name is not the one the cataloguer's naming convention produces");

        assertEquals(OMAGPlatformExtension.getPlatformName(),
                     PlatformCatalogFvtTestSupport.getDisplayName(platform),
                     "The platform element's display name is not the platform.name from application.properties");

        assertEquals(PlatformCatalogFvtTestSupport.OMAG_SERVER_PLATFORM_TYPE,
                     PlatformCatalogFvtTestSupport.getDeployedImplementationType(platform),
                     "The platform element is not marked as an OMAG Server Platform");

        /*
         * The URL root is the one thing that identifies this platform instance rather than its configured
         * name, and after updatePlatform() has run it only survives in the additional properties.
         */
        assertEquals(OMAGPlatformExtension.getPlatformURLRoot(),
                     PlatformCatalogFvtTestSupport.getAdditionalProperties(platform).get("platformURLRoot"),
                     "The platform element does not record the URL root it was catalogued from");
    }


    /**
     * Refreshing the platform does not rename it.  The qualified name the cataloguer's {@code start()}
     * created the element with is the one {@code start()} will look it up by next time, so a refresh that
     * moved it would leave the following start unable to find its own platform - and cataloguing it a
     * second time.  See
     * {@link PlatformCatalogMultiplePlatformsFVT#restartingTheConnectorDoesNotDuplicateThePlatform}.
     * <br>
     * The platform's configured name and organization are not lost by this; they go into the element's
     * display name and resource name, which is checked by {@link #platformIsCataloguedOnce}.
     *
     * @throws Exception a retrieval failed
     */
    @Test
    @DisplayName("Refreshing does not rename the platform away from the name it is looked up by")
    void refreshingDoesNotRenameThePlatform() throws Exception
    {
        String creationQualifiedName = PlatformCatalogFvtTestSupport.expectedPlatformQualifiedName(OMAGPlatformExtension.getPlatformURLRoot());

        OpenMetadataElement platformByCreationName = openMetadataStore.getMetadataElementByUniqueName(creationQualifiedName,
                                                                                                      OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                      new GetOptions());

        assertNotNull(platformByCreationName,
                      "The platform element can no longer be found by the qualified name the cataloguer's"
                              + " start() creates it with, so the next start will not find it either and will"
                              + " catalog the platform all over again.");

        assertEquals(platformElement.getElementGUID(),
                     platformByCreationName.getElementGUID(),
                     "The element found by the creation-time qualified name is not the platform element");
    }


    /**
     * Every server this suite put on the platform is catalogued, attached to the platform element by a
     * DeployedOn relationship, and named by the cataloguer's convention - which qualifies a server by the
     * platform's URL root, so that servers with the same name on different platforms stay apart.
     *
     * @throws Exception a retrieval failed
     */
    @Test
    @DisplayName("Every server on the platform is catalogued and attached to it")
    void serversAreCatalogued() throws Exception
    {
        List<RelatedMetadataElement> hostedServers = PlatformCatalogFvtTestSupport.getHostedServers(openMetadataStore,
                                                                                                    platformElement.getElementGUID());

        List<String> hostedResourceNames = new ArrayList<>();

        for (RelatedMetadataElement hostedServer : hostedServers)
        {
            hostedResourceNames.add(PlatformCatalogFvtTestSupport.getResourceName(hostedServer.getElement()));
        }

        for (String serverName : OMAGPlatformExtension.SUITE_SERVER_NAMES)
        {
            String expectedResourceName = PlatformCatalogFvtTestSupport.expectedServerResourceName(serverName,
                                                                                                    OMAGPlatformExtension.INITIAL_SERVER_ORGANIZATION_NAME);

            assertTrue(hostedResourceNames.contains(expectedResourceName),
                       () -> "Server " + serverName + " is not attached to the platform element.  Attached: "
                               + hostedResourceNames);
        }
    }


    /**
     * A catalogued server's qualified name carries the platform's URL root.  This is the property that keeps
     * two platforms' identically named servers apart, so it is worth asserting on its own rather than only
     * through the platform's attachments.
     *
     * @throws Exception a retrieval failed
     */
    @Test
    @DisplayName("A server's qualified name is qualified by the platform's URL root")
    void serverQualifiedNamesAreQualifiedByThePlatform() throws Exception
    {
        List<RelatedMetadataElement> hostedServers = PlatformCatalogFvtTestSupport.getHostedServers(openMetadataStore,
                                                                                                    platformElement.getElementGUID());

        assertFalse(hostedServers.isEmpty(), "No servers are attached to the platform element");

        for (RelatedMetadataElement hostedServer : hostedServers)
        {
            String qualifiedName = PlatformCatalogFvtTestSupport.getQualifiedName(hostedServer.getElement());

            assertNotNull(qualifiedName, "A server attached to the platform has no qualified name");

            assertTrue(qualifiedName.contains(OMAGPlatformExtension.getPlatformURLRoot()),
                       () -> "Server qualified name '" + qualifiedName + "' does not contain the platform's URL root '"
                               + OMAGPlatformExtension.getPlatformURLRoot() + "', so it would collide with a"
                               + " same-named server on another platform");
        }
    }


    /**
     * What the cataloguer recorded matches what the platform actually reports.  The two are compared by
     * server name, because that is the only thing the platform report and the metadata element are certain
     * to share once the naming conventions have been applied.
     *
     * @throws Exception a retrieval failed
     */
    @Test
    @DisplayName("Every server the platform reports has been catalogued")
    void everyServerThePlatformReportsIsCatalogued() throws Exception
    {
        PlatformServicesClient platformServicesClient = OMAGPlatformExtension.getPlatformServicesClient();

        List<String> knownServers = platformServicesClient.getKnownServers();

        assertNotNull(knownServers, "The platform reports no known servers at all");

        List<OpenMetadataElement> cataloguedServers = PlatformCatalogFvtTestSupport.getSuiteServerElements(openMetadataStore);

        for (String serverName : knownServers)
        {
            boolean found = false;

            for (OpenMetadataElement cataloguedServer : cataloguedServers)
            {
                String resourceName = PlatformCatalogFvtTestSupport.getResourceName(cataloguedServer);

                if ((resourceName != null) && (resourceName.endsWith(serverName)))
                {
                    found = true;
                    break;
                }
            }

            assertTrue(found,
                       () -> "The platform reports server '" + serverName + "' but the cataloguer has not"
                               + " catalogued it.  Catalogued:" + PlatformCatalogFvtTestSupport.describe(cataloguedServers));
        }
    }


    /**
     * Refreshing again changes nothing.  This is the property the whole connector rests on: it runs on a
     * timer, so every pass after the first is a pass over metadata it created itself, and a pass that cannot
     * recognise its own work duplicates it.
     *
     * @throws Exception a retrieval failed
     */
    @Test
    @DisplayName("A second refresh does not duplicate anything")
    void refreshIsIdempotent() throws Exception
    {
        List<OpenMetadataElement> platformsBefore = PlatformCatalogFvtTestSupport.getSuitePlatformElements(openMetadataStore);
        List<OpenMetadataElement> serversBefore   = PlatformCatalogFvtTestSupport.getSuiteServerElements(openMetadataStore);

        List<RelatedMetadataElement> attachedBefore = PlatformCatalogFvtTestSupport.getHostedServers(openMetadataStore,
                                                                                                     platformElement.getElementGUID());

        OMAGPlatformExtension.refreshCataloguer();

        List<OpenMetadataElement> platformsAfter = PlatformCatalogFvtTestSupport.getSuitePlatformElements(openMetadataStore);
        List<OpenMetadataElement> serversAfter   = PlatformCatalogFvtTestSupport.getSuiteServerElements(openMetadataStore);

        List<RelatedMetadataElement> attachedAfter = PlatformCatalogFvtTestSupport.getHostedServers(openMetadataStore,
                                                                                                    platformElement.getElementGUID());

        assertEquals(platformsBefore.size(),
                     platformsAfter.size(),
                     () -> "A second refresh changed the number of platform elements:"
                             + PlatformCatalogFvtTestSupport.describe(platformsAfter));

        assertEquals(serversBefore.size(),
                     serversAfter.size(),
                     () -> "A second refresh changed the number of server elements:"
                             + PlatformCatalogFvtTestSupport.describe(serversAfter));

        assertEquals(attachedBefore.size(),
                     attachedAfter.size(),
                     "A second refresh changed the number of servers attached to the platform");
    }
}
