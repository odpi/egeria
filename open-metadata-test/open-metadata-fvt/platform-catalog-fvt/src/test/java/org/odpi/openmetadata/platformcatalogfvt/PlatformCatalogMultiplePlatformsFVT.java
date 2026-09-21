/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformcatalogfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PlatformCatalogMultiplePlatformsFVT covers what happens when more than one OMAG Server Platform is in
 * play - the case an Egeria deployment of any size is always in.
 * <br>
 * <b>How two platforms are staged.</b>  A second OMAG Server Platform cannot be started in the same JVM:
 * the platform keeps its live servers in static registries, so a second Spring context would share the
 * first's servers rather than have its own.  Instead, the suite catalogs the one running platform twice,
 * through two SoftwareServerPlatform elements that reach it at two different spellings of its address
 * ({@code http://localhost:port} and {@code http://127.0.0.1:port}).  That reproduces the part of the real
 * situation that this connector's naming conventions have to cope with: two catalog targets at two
 * addresses, with servers of exactly the same names behind both of them.  What it does not reproduce is two
 * platforms with genuinely different configurations, so the tests here stay away from anything that would
 * depend on that.
 * <br>
 * This class runs last: it deliberately leaves an extra catalog target and, if the connector duplicates the
 * platform, extra elements behind.
 */
@ExtendWith(OMAGPlatformExtension.class)
@Order(3)
public class PlatformCatalogMultiplePlatformsFVT
{
    private static ConnectorContextBase connectorContext;
    private static OpenMetadataStore    openMetadataStore;
    private static OpenMetadataElement  firstPlatformElement;
    private static String               secondPlatformGUID;


    /**
     * Catalog the ecosystem through the connector's own catalog target before adding a second one.
     *
     * @throws Exception the platform was never catalogued - nothing after this can pass
     */
    @BeforeAll
    static void catalogThePlatform() throws Exception
    {
        connectorContext     = ConnectorContextFactory.newContext();
        openMetadataStore    = connectorContext.getOpenMetadataStore();
        firstPlatformElement = PlatformCatalogDriver.refreshUntilPlatformCatalogued(openMetadataStore);
    }


    /**
     * Restarting the integration daemon - and with it the connector - does not catalog the platform a second
     * time.
     * <br>
     * This is the multi-platform question in its smallest form.  The connector catalogs the local platform
     * in {@code start()}, creating the element from a template with a qualified name built from the
     * platform's URL root, and relying on the template call's "retrieve if it already exists" behaviour to
     * find the element a previous run created rather than making another.  The first refresh after that then
     * renames the element: {@code updatePlatform} replaces the URL-based qualified name with one built from
     * the platform's configured name and organization (see
     * {@link PlatformCatalogBasicsFVT#platformIsRenamedAwayFromItsCreationName}).  Whether the lookup on the
     * next start still finds it is what this test asks.
     * <br>
     * A restart is not an unusual event: it happens on every platform restart, every redeployment and every
     * time an administrator restarts the integration daemon.
     *
     * @throws Exception a retrieval or restart failed
     */
    @Test
    @Order(3)
    @DisplayName("Restarting the connector does not catalog the platform a second time")
    void restartingTheConnectorDoesNotDuplicateThePlatform() throws Exception
    {
        List<OpenMetadataElement> platformsBefore = PlatformCatalogFvtTestSupport.getSuitePlatformElements(openMetadataStore);

        /*
         * The restart is what this test is about: it is the point at which the connector runs its start()
         * again.  The daemon registers the connector asynchronously, so this waits for the integration group
         * to come back up before asking for a refresh.
         */
        OMAGPlatformExtension.restartIntegrationDaemon();

        PlatformCatalogDriver.refreshUntilPlatformCatalogued(openMetadataStore);

        List<OpenMetadataElement> platformsAfter = PlatformCatalogFvtTestSupport.getSuitePlatformElements(openMetadataStore);

        assertEquals(platformsBefore.size(),
                     platformsAfter.size(),
                     () -> "Restarting the integration daemon added " + (platformsAfter.size() - platformsBefore.size())
                             + " platform element(s).  The connector's start() looks the local platform up by a"
                             + " qualified name built from its URL root, but its first refresh renames the element"
                             + " to one built from the platform's configured name - so the lookup no longer finds"
                             + " it and creates another:" + PlatformCatalogFvtTestSupport.describe(platformsAfter));
    }


    /**
     * A second platform, reached at a different address, is catalogued as a second platform - not folded
     * into the first.
     * <br>
     * The platform element's qualified name is built from the platform's configured name and organization
     * name and nothing else, so two platforms that have not been given distinctive names want the same
     * qualified name.  Two default installations look exactly like that: {@code platform.name} defaults to
     * "Development OMAG Server Platform" and {@code platform.organization.name} defaults to empty.
     * <br>
     * This suite's platform does have a distinctive name, so both catalog targets here resolve to the same
     * configured name for the same reason a pair of default platforms would - which is what makes the
     * collision visible.
     *
     * @throws Exception a retrieval or catalog target registration failed
     */
    @Test
    @Order(1)
    @DisplayName("A second platform at a different address is catalogued separately")
    void secondPlatformIsCataloguedSeparately() throws Exception
    {
        String secondPlatformURLRoot = OMAGPlatformExtension.getPlatformURLRoot().replace("localhost", "127.0.0.1");

        assertNotEquals(OMAGPlatformExtension.getPlatformURLRoot(),
                        secondPlatformURLRoot,
                        "The second address is the same as the first - the platform URL root is not what this test assumes");

        secondPlatformGUID = PlatformCatalogDriver.catalogSecondPlatform(connectorContext, secondPlatformURLRoot);

        OMAGPlatformExtension.refreshCataloguer();
        OMAGPlatformExtension.refreshCataloguer();

        List<OpenMetadataElement> platforms = PlatformCatalogFvtTestSupport.getSuitePlatformElements(openMetadataStore);

        assertTrue(platforms.size() >= 2,
                   () -> "Expected the two catalog targets to be described by two platform elements, found "
                           + platforms.size() + ":" + PlatformCatalogFvtTestSupport.describe(platforms));

        /*
         * Both elements survived, so they must have ended up with different qualified names - which is the
         * thing in doubt, since the cataloguer builds that name from the platform's configured name and
         * organization and neither of those differs between the two catalog targets.
         */
        String firstQualifiedName  = PlatformCatalogFvtTestSupport.getQualifiedName(platformFor(platforms, firstPlatformElement.getElementGUID()));
        String secondQualifiedName = PlatformCatalogFvtTestSupport.getQualifiedName(platformFor(platforms, secondPlatformGUID));

        assertNotEquals(firstQualifiedName,
                        secondQualifiedName,
                        "The two platform elements have the same qualified name, so nothing downstream can tell"
                                + " them apart and a lookup by name returns whichever one it happens to find first");
    }


    /**
     * The servers behind the second platform are catalogued as that platform's own servers, even though
     * every one of them has the same name as a server on the first.
     * <br>
     * This is the question a real deployment runs into immediately: servers are named for what they do -
     * "active-metadata-store", "integration-daemon", "view-server" - so the same names appear on every
     * platform in the estate.  The qualified name the cataloguer gives a server is qualified by the
     * platform's URL root, which is what should keep them apart.
     *
     * @throws Exception a retrieval failed
     */
    @Test
    @Order(2)
    @DisplayName("Identically named servers on two platforms are catalogued separately")
    void identicallyNamedServersAreKeptApart() throws Exception
    {
        List<OpenMetadataElement> platforms = PlatformCatalogFvtTestSupport.getSuitePlatformElements(openMetadataStore);

        assertTrue(platforms.size() >= 2, "This test needs the two platform elements the previous test created");

        List<String> firstPlatformServerGUIDs  = new ArrayList<>();
        List<String> secondPlatformServerGUIDs = new ArrayList<>();

        for (OpenMetadataElement platform : platforms)
        {
            if ((! platform.getElementGUID().equals(firstPlatformElement.getElementGUID()))
                        && (! platform.getElementGUID().equals(secondPlatformGUID)))
            {
                continue;
            }

            List<String> serverGUIDs = (platform.getElementGUID().equals(firstPlatformElement.getElementGUID()))
                                               ? firstPlatformServerGUIDs
                                               : secondPlatformServerGUIDs;

            for (RelatedMetadataElement hostedServer : PlatformCatalogFvtTestSupport.getHostedServers(openMetadataStore,
                                                                                                      platform.getElementGUID()))
            {
                serverGUIDs.add(hostedServer.getElement().getElementGUID());
            }
        }

        assertFalse(firstPlatformServerGUIDs.isEmpty(), "The first platform has no servers attached to it");
        assertFalse(secondPlatformServerGUIDs.isEmpty(),
                    "The second platform has no servers attached to it, so the cataloguer either did not process"
                            + " its catalog target or attached its servers to the first platform instead");

        for (String serverGUID : secondPlatformServerGUIDs)
        {
            assertFalse(firstPlatformServerGUIDs.contains(serverGUID),
                        () -> "Server element " + serverGUID + " is attached to both platforms.  A server runs on"
                                + " one platform; an element shared between two of them cannot describe either"
                                + " accurately, and whichever catalog target refreshes last overwrites the other's"
                                + " view of it");
        }

        /*
         * Each server element should name the platform it was catalogued from, which is the mechanism that is
         * supposed to keep the two sets apart.
         */
        for (String serverGUID : secondPlatformServerGUIDs)
        {
            OpenMetadataElement serverElement = openMetadataStore.getMetadataElementByGUID(serverGUID);

            String qualifiedName = PlatformCatalogFvtTestSupport.getQualifiedName(serverElement);

            assertTrue(qualifiedName.contains("127.0.0.1"),
                       () -> "Server element " + qualifiedName + " is attached to the second platform but is"
                               + " qualified by the first platform's address");
        }
    }


    /**
     * A platform element left under the name an earlier release of the connector gave it is adopted and put
     * right, not catalogued a second time.
     * <br>
     * This is the upgrade path.  An ecosystem catalogued by an earlier release has its platform elements
     * named after each platform's own name and organization, which is not the name this connector looks
     * them up by; without the migration in {@code start()}, the first start after the upgrade would create a
     * new element for every platform and leave the old ones - with their servers, their endpoints and their
     * history - stranded.
     * <br>
     * The state is staged by renaming the platform element back to the old convention, which is exactly what
     * the upgraded ecosystem looks like, and then restarting the connector.
     *
     * @throws Exception a retrieval, rename or restart failed
     */
    @Test
    @Order(4)
    @DisplayName("A platform catalogued under an earlier release's name is adopted, not catalogued again")
    void platformCataloguedUnderAnOlderNameIsAdopted() throws Exception
    {
        List<OpenMetadataElement> platformsBefore = PlatformCatalogFvtTestSupport.getSuitePlatformElements(openMetadataStore);

        String legacyQualifiedName = PlatformCatalogFvtTestSupport.legacyPlatformQualifiedName(OMAGPlatformExtension.getPlatformOrganizationName(),
                                                                                                OMAGPlatformExtension.getPlatformName());

        PlatformCatalogDriver.renamePlatform(connectorContext, firstPlatformElement.getElementGUID(), legacyQualifiedName);

        OMAGPlatformExtension.restartIntegrationDaemon();

        PlatformCatalogDriver.refreshUntilPlatformCatalogued(openMetadataStore);

        List<OpenMetadataElement> platformsAfter = PlatformCatalogFvtTestSupport.getSuitePlatformElements(openMetadataStore);

        assertEquals(platformsBefore.size(),
                     platformsAfter.size(),
                     () -> "Restarting the connector against a platform element left under an earlier release's"
                             + " name added " + (platformsAfter.size() - platformsBefore.size())
                             + " platform element(s) instead of adopting the one that was already there:"
                             + PlatformCatalogFvtTestSupport.describe(platformsAfter));

        OpenMetadataElement adopted = openMetadataStore.getMetadataElementByGUID(firstPlatformElement.getElementGUID());

        assertNotNull(adopted, "The platform element that was renamed has gone");

        assertEquals(PlatformCatalogFvtTestSupport.expectedPlatformQualifiedName(OMAGPlatformExtension.getPlatformURLRoot()),
                     PlatformCatalogFvtTestSupport.getQualifiedName(adopted),
                     "The adopted platform element was not renamed back to the name the connector looks it up"
                             + " by, so the next restart will not find it either");
    }


    /**
     * Find one platform element in a list by its GUID.
     *
     * @param platforms elements to search
     * @param platformGUID GUID to look for
     * @return the element, or null
     */
    private OpenMetadataElement platformFor(List<OpenMetadataElement> platforms,
                                            String                    platformGUID)
    {
        for (OpenMetadataElement platform : platforms)
        {
            if (platform.getElementGUID().equals(platformGUID))
            {
                return platform;
            }
        }

        return null;
    }
}
