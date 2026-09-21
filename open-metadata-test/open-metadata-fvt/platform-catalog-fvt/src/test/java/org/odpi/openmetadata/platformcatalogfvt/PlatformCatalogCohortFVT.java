/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformcatalogfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PlatformCatalogCohortFVT is the two-platform test: two OMAG Server Platforms, each with its own metadata
 * access store and its own integration daemon running its own instance of the cataloguer, and the two
 * stores joined in one open metadata repository cohort so that every query either connector makes is
 * federated across both repositories.
 * <br>
 * <b>The two platforms are deliberately indistinguishable by name.</b>  Both are called
 * "platform-catalog-fvt OMAG Server Platform" and both carry the same organization name, which is what two
 * installations look like when nobody has got round to naming them - the shipped defaults are "Development
 * OMAG Server Platform" and an empty organization.  A null organization name on both is the same case: the
 * names the cataloguer builds are made from each platform's address, so neither value enters into them.
 * Until that was fixed, two such platforms both wanted one qualified name and the second one silently
 * became a duplicate of the first.
 * <br>
 * Federation is what makes the second instance of the connector interesting rather than merely present.
 * Each connector reads through its own metadata access store, and through the cohort that store answers
 * with what the other repository holds as well as its own - so each connector can see everything the other
 * one has catalogued, and the question is whether it leaves it alone.
 * <br>
 * This class runs last and starts a second JVM, so it is also the most expensive thing in the suite.
 */
@ExtendWith(OMAGPlatformExtension.class)
@Order(5)
public class PlatformCatalogCohortFVT
{
    private static final SecondPlatform secondPlatform = new SecondPlatform();

    private static ConnectorContextBase firstStoreContext;
    private static OpenMetadataStore    firstStore;
    private static OpenMetadataStore    secondStore;


    /**
     * Bring up the second platform, let both connectors run, and wait until each has catalogued its own
     * platform.
     *
     * @throws Exception the second platform did not start, or one of the connectors never ran
     */
    @BeforeAll
    static void startTheSecondPlatform() throws Exception
    {
        firstStoreContext = ConnectorContextFactory.newContext();
        firstStore        = firstStoreContext.getOpenMetadataStore();

        PlatformCatalogDriver.refreshUntilPlatformCatalogued(firstStore);

        secondPlatform.start();
        secondPlatform.waitForIntegrationGroup();

        secondStore = ConnectorContextFactory.newContext(SecondPlatform.METADATA_STORE_NAME,
                                                          secondPlatform.getPlatformURLRoot())
                                             .getOpenMetadataStore();

        /*
         * Both connectors are given several passes.  The cohort has to finish exchanging registrations
         * before either store can see the other's content, and each connector's first pass is the one that
         * creates its own platform's elements.
         */
        for (int pass = 0; pass < 3; pass++)
        {
            OMAGPlatformExtension.refreshCataloguer();
            secondPlatform.refreshCataloguer();
        }
    }


    /**
     * Stop the second platform whatever happened.
     */
    @AfterAll
    static void stopTheSecondPlatform()
    {
        secondPlatform.stop();
    }


    /**
     * The cohort is working: the first store can see metadata that only the second store holds.  Everything
     * after this is about what the two connectors do with a federated view, so it is worth establishing
     * that there is one before drawing any conclusions from it.
     *
     * @throws Exception a retrieval failed
     */
    @Test
    @Order(1)
    @DisplayName("The two metadata access stores are federated")
    void theStoresAreFederated() throws Exception
    {
        OpenMetadataElement secondPlatformElement = platformElementFor(firstStore, secondPlatform.getPlatformURLRoot());

        assertNotNull(secondPlatformElement,
                      "The first metadata access store cannot see the second platform's element, so either the"
                              + " cohort has not finished exchanging registrations or the second connector has"
                              + " not catalogued its own platform.  Without federation the rest of this class"
                              + " proves nothing.");
    }


    /**
     * Two platforms with identical names are catalogued as two platforms.
     *
     * @throws Exception a retrieval failed
     */
    @Test
    @Order(2)
    @DisplayName("Two identically named platforms are catalogued separately")
    void identicallyNamedPlatformsAreCataloguedSeparately() throws Exception
    {
        OpenMetadataElement firstPlatformElement  = platformElementFor(firstStore, OMAGPlatformExtension.getPlatformURLRoot());
        OpenMetadataElement secondPlatformElement = platformElementFor(firstStore, secondPlatform.getPlatformURLRoot());

        assertNotNull(firstPlatformElement, "The first platform has not been catalogued");
        assertNotNull(secondPlatformElement, "The second platform has not been catalogued");

        assertFalse(firstPlatformElement.getElementGUID().equals(secondPlatformElement.getElementGUID()),
                    "The two platforms are described by one element");

        /*
         * They are named identically, which is the point - so their display names match and only the
         * qualified names, built from their addresses, tell them apart.
         */
        assertEquals(PlatformCatalogFvtTestSupport.getDisplayName(firstPlatformElement),
                     PlatformCatalogFvtTestSupport.getDisplayName(secondPlatformElement),
                     "This test assumes the two platforms are configured with the same name");

        assertFalse(PlatformCatalogFvtTestSupport.getQualifiedName(firstPlatformElement)
                            .equals(PlatformCatalogFvtTestSupport.getQualifiedName(secondPlatformElement)),
                    "Two platforms with the same configured name and organization ended up with the same"
                            + " qualified name, so nothing downstream can tell them apart");
    }


    /**
     * Nothing has been catalogued twice.
     * <br>
     * This is the question the two connector instances raise.  They share one integration connector
     * definition from the content pack, and with a federated view each of them can see the other's catalog
     * targets and the elements behind them.  A connector that treats the other's platform as its own does
     * not overwrite it - it cannot, the element is homed in the other repository - it makes its own copy
     * in its own repository, and the federated view then has two elements for one thing.
     *
     * @throws Exception a retrieval failed
     */
    @Test
    @Order(3)
    @DisplayName("Neither connector catalogues what the other has already catalogued")
    void nothingIsCataloguedTwice() throws Exception
    {
        Map<String, List<String>> platformsByName = byQualifiedName(PlatformCatalogFvtTestSupport.getSuitePlatformElements(firstStore));
        Map<String, List<String>> serversByName   = byQualifiedName(PlatformCatalogFvtTestSupport.getSuiteServerElements(firstStore));

        List<String> duplicated = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : platformsByName.entrySet())
        {
            if (entry.getValue().size() > 1)
            {
                duplicated.add("platform " + entry.getKey() + " -> " + entry.getValue());
            }
        }

        for (Map.Entry<String, List<String>> entry : serversByName.entrySet())
        {
            if (entry.getValue().size() > 1)
            {
                duplicated.add("server " + entry.getKey() + " -> " + entry.getValue());
            }
        }

        assertTrue(duplicated.isEmpty(),
                   () -> "The federated view has more than one element for the same qualified name, which means"
                           + " both connectors catalogued the same thing into their own repositories:\n    "
                           + String.join("\n    ", duplicated));
    }


    /**
     * Each platform's servers belong to that platform, and carry its address.  With identically named
     * platforms and a federated view, a connector that muddled the two would attach one platform's servers
     * to the other or name them after the wrong address.
     *
     * @throws Exception a retrieval failed
     */
    @Test
    @Order(4)
    @DisplayName("Each platform's servers are its own")
    void eachPlatformKeepsItsOwnServers() throws Exception
    {
        assertServersBelongToTheirPlatform(OMAGPlatformExtension.getPlatformURLRoot());
        assertServersBelongToTheirPlatform(secondPlatform.getPlatformURLRoot());
    }


    /**
     * Every element belongs to exactly one repository.
     * <br>
     * This is the sharper form of "nothing was catalogued twice".  A federated read applies deduplication
     * on the way out, so two elements with the same qualified name in two repositories can come back as
     * one - which would hide precisely the damage two competing connectors would do.  Reading each store
     * separately and comparing where each element is homed does not have that blind spot: an element that
     * both connectors created would be homed in the first repository when read there and in the second when
     * read there.
     *
     * @throws Exception a retrieval failed
     */
    @Test
    @Order(5)
    @DisplayName("Every element is homed in exactly one of the two repositories")
    void everyElementHasOneHome() throws Exception
    {
        Map<String, String> homeByQualifiedName = new LinkedHashMap<>();
        List<String>        contested           = new ArrayList<>();

        for (OpenMetadataStore store : new OpenMetadataStore[]{firstStore, secondStore})
        {
            List<OpenMetadataElement> elements = new ArrayList<>(PlatformCatalogFvtTestSupport.getSuitePlatformElements(store));

            elements.addAll(PlatformCatalogFvtTestSupport.getSuiteServerElements(store));

            for (OpenMetadataElement element : elements)
            {
                String qualifiedName = PlatformCatalogFvtTestSupport.getQualifiedName(element);
                String home          = homeOf(element);

                if ((qualifiedName != null) && (home != null))
                {
                    String knownHome = homeByQualifiedName.putIfAbsent(qualifiedName, home);

                    if ((knownHome != null) && (! knownHome.equals(home)))
                    {
                        contested.add(qualifiedName + " is homed in " + knownHome + " when read through one store"
                                              + " and in " + home + " when read through the other");
                    }
                }
            }
        }

        assertTrue(contested.isEmpty(),
                   () -> "The same element has been created in both repositories, so the two connectors are each"
                           + " cataloguing what the other has already catalogued:\n    "
                           + String.join("\n    ", contested));
    }


    /**
     * Each connector refreshes only the catalog targets registered through its own metadata access store,
     * and leaves the rest of the cohort's to the daemons that registered them.
     * <br>
     * Both daemons run the same integration connector <i>definition</i> from the content pack, so both ask
     * open metadata the same question - "what are the catalog targets of connector {@code dee84e6e-...}?" -
     * and through the cohort that question is answered from every repository at once.  The first part of
     * this test confirms that is still so: the raw query really does return both platforms, which is what
     * makes the filtering worth having rather than a no-op.
     * <br>
     * The second part reads what the second platform's connector actually did.  Its audit log is the only
     * place that can be seen from here, and it is a file this suite created, so it is a fair thing to read.
     *
     * @throws Exception a retrieval failed
     */
    @Test
    @Order(6)
    @DisplayName("Each connector refreshes only the catalog targets registered through its own store")
    void eachConnectorRefreshesOnlyItsOwnTargets() throws Exception
    {
        OpenMetadataElement firstPlatform  = platformElementFor(firstStore, OMAGPlatformExtension.getPlatformURLRoot());
        OpenMetadataElement secondPlatformElement = platformElementFor(firstStore, secondPlatform.getPlatformURLRoot());

        assertNotNull(firstPlatform, "The first platform has not been catalogued");
        assertNotNull(secondPlatformElement, "The second platform has not been catalogued");

        /*
         * The condition being guarded against is still present: asked plainly, open metadata offers each
         * connector every platform in the cohort.
         */
        List<String> everyTarget = catalogTargetGUIDs(firstStoreContext);

        assertTrue(everyTarget.contains(firstPlatform.getElementGUID()) && everyTarget.contains(secondPlatformElement.getElementGUID()),
                   "A plain query for the cataloguer's catalog targets no longer returns both platforms, so"
                           + " either the cohort is not federating or catalog targets are no longer shared."
                           + "  Either way this test is no longer testing what it says it is.");

        String secondPlatformLog = java.nio.file.Files.readString(secondPlatform.getLogFile().toPath());

        assertTrue(secondPlatformLog.contains("OIF-CONNECTOR-0024"),
                   "The second platform's connector never reported leaving any catalog target to another"
                           + " daemon, so it is refreshing the first platform's targets as well as its own");

        /*
         * The sharp end: the second connector must not have synchronized the first platform.  The audit
         * record that says it did names the catalog target, and a catalog target's name carries the GUID of
         * the platform it points at.
         */
        for (String line : secondPlatformLog.split("\n"))
        {
            if (line.contains("OMAG-CONNECTORS-0011"))
            {
                assertFalse(line.contains(firstPlatform.getElementGUID()),
                            () -> "The second platform's connector synchronized the first platform, which was"
                                    + " registered through the first platform's metadata access store:\n    "
                                    + line.trim());
            }
        }
    }


    /**
     * Return the GUIDs of everything the cataloguer has as a catalog target, read through one store.
     *
     * @param connectorContext context to read through
     * @return list of GUIDs
     * @throws Exception the retrieval failed
     */
    private static List<String> catalogTargetGUIDs(ConnectorContextBase connectorContext) throws Exception
    {
        AssetClient assetClient = connectorContext.getAssetClient(OpenMetadataType.SOFTWARE_SERVER_PLATFORM.typeName);

        QueryOptions queryOptions = assetClient.getQueryOptions(0, PlatformCatalogFvtTestSupport.MAX_PAGE_SIZE);

        queryOptions.setGraphQueryDepth(0);

        List<OpenMetadataRootElement> catalogTargets = assetClient.getCatalogTargets(IntegrationConnectorDefinition.OMAG_SERVER_PLATFORM_CATALOGUER.getGUID(),
                                                                                     queryOptions);

        List<String> targetGUIDs = new ArrayList<>();

        if (catalogTargets != null)
        {
            for (OpenMetadataRootElement catalogTarget : catalogTargets)
            {
                if (catalogTarget != null)
                {
                    targetGUIDs.add(catalogTarget.getElementHeader().getGUID());
                }
            }
        }

        return targetGUIDs;
    }


    /**
     * Return the repository an element is homed in.
     *
     * @param element element to read
     * @return metadata collection id, or null
     */
    private static String homeOf(OpenMetadataElement element)
    {
        if (element.getOrigin() != null)
        {
            return element.getOrigin().getHomeMetadataCollectionId();
        }

        return null;
    }


    /**
     * Assert that every server attached to a platform carries that platform's address in its qualified
     * name, and that the platform has some.
     *
     * @param platformURLRoot platform to check
     * @throws Exception a retrieval failed
     */
    private void assertServersBelongToTheirPlatform(String platformURLRoot) throws Exception
    {
        OpenMetadataElement platformElement = platformElementFor(firstStore, platformURLRoot);

        assertNotNull(platformElement, () -> "The platform at " + platformURLRoot + " has not been catalogued");

        List<RelatedMetadataElement> hostedServers = PlatformCatalogFvtTestSupport.getHostedServers(firstStore,
                                                                                                    platformElement.getElementGUID());

        assertFalse(hostedServers.isEmpty(),
                    () -> "No servers are attached to the platform at " + platformURLRoot);

        for (RelatedMetadataElement hostedServer : hostedServers)
        {
            String qualifiedName = PlatformCatalogFvtTestSupport.getQualifiedName(hostedServer.getElement());

            assertTrue((qualifiedName != null) && (qualifiedName.contains(platformURLRoot)),
                       () -> "Server " + qualifiedName + " is attached to the platform at " + platformURLRoot
                               + " but is not named after it");
        }
    }


    /**
     * Group elements by qualified name, so that a name held by more than one element stands out.
     *
     * @param elements elements to group
     * @return map of qualified name to the GUIDs carrying it
     */
    private static Map<String, List<String>> byQualifiedName(List<OpenMetadataElement> elements)
    {
        Map<String, List<String>> grouped = new LinkedHashMap<>();

        for (OpenMetadataElement element : elements)
        {
            String qualifiedName = PlatformCatalogFvtTestSupport.getQualifiedName(element);

            if (qualifiedName != null)
            {
                grouped.computeIfAbsent(qualifiedName, name -> new ArrayList<>()).add(element.getElementGUID());
            }
        }

        return grouped;
    }


    /**
     * Return the platform element catalogued for an address, read through a particular store.
     *
     * @param openMetadataStore store to read through
     * @param platformURLRoot address to look for
     * @return element or null
     * @throws Exception the retrieval failed
     */
    private static OpenMetadataElement platformElementFor(OpenMetadataStore openMetadataStore,
                                                          String            platformURLRoot) throws Exception
    {
        for (OpenMetadataElement platformElement : PlatformCatalogFvtTestSupport.getSuitePlatformElements(openMetadataStore))
        {
            if (PlatformCatalogFvtTestSupport.expectedPlatformQualifiedName(platformURLRoot)
                                             .equals(PlatformCatalogFvtTestSupport.getQualifiedName(platformElement)))
            {
                return platformElement;
            }
        }

        return null;
    }
}
