/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformcatalogfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.client.OMAGServerConfigurationClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * PlatformCatalogOrganizationNameFVT covers what happens when an organization name is set after the
 * ecosystem has already been catalogued.
 * <br>
 * There are two organization names in play and the cataloguer uses them in different places:
 * <ul>
 *     <li>the <b>platform's</b> organization name, {@code platform.organization.name} in the platform's
 *     application.properties, which {@code updatePlatform} folds into the platform element's qualified name
 *     and resource name;</li>
 *     <li>each <b>server's</b> organization name, held in that server's configuration document, which the
 *     cataloguer folds into the server element's qualified name, resource name and display name.</li>
 * </ul>
 * Only the second can be changed while the platform is running, so that is what this class changes.  The
 * platform's own organization name is fixed when the Spring context starts and can only be varied by
 * restarting the platform, which a single-platform suite cannot do; what can be asserted about it - that it
 * ends up in the platform element's names, and that the URL root does not - is covered by
 * {@link PlatformCatalogBasicsFVT}.
 * <br>
 * <b>The property under test.</b>  The cataloguer works out which servers it has already catalogued by
 * matching the resource name stored on each server attached to the platform element against the resource
 * name it computes from the live platform.  The organization name is part of that resource name.  So when a
 * server's organization name changes, the stored resource name and the computed one stop matching, and the
 * question this class asks is whether the cataloguer recognises the server it already has and renames it, or
 * fails to recognise it and creates a second element beside the first.
 */
@ExtendWith(OMAGPlatformExtension.class)
@Order(2)
public class PlatformCatalogOrganizationNameFVT
{
    /**
     * The organization name set part-way through this suite.  The integration daemon is the subject because
     * changing a server's configuration document does not disturb the running server - the configuration is
     * read when a server starts - so the daemon carries on running the cataloguer while its stored
     * description of itself changes underneath it.  That is also what makes this a fair test: it is the
     * ordinary way an administrator fills in an organization name that was left blank at install time.
     */
    private static final String NEW_ORGANIZATION_NAME = "Egeria platform-catalog-fvt Organization";

    private static final String SUBJECT_SERVER      = OMAGPlatformExtension.INTEGRATION_DAEMON_NAME;
    private static final String SUBJECT_DESCRIPTION = "Integration daemon running the OMAG Server Platform Cataloguer.";

    private static OpenMetadataStore openMetadataStore;
    private static String            originalServerElementGUID;


    /**
     * Catalog the ecosystem, and take note of the server element as it stands before anything is changed.
     *
     * @throws Exception the platform was never catalogued - nothing after this can pass
     */
    @BeforeAll
    static void catalogThePlatform() throws Exception
    {
        openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        PlatformCatalogDriver.refreshUntilPlatformCatalogued(openMetadataStore);
    }


    /**
     * Put the subject server's configuration document back the way it was found, whatever the tests did to
     * it.  The suite's other classes assume the "no organization name" shape.
     *
     * @throws Exception the organization name could not be cleared
     */
    @AfterAll
    static void clearTheOrganizationName() throws Exception
    {
        setOrganizationName(OMAGPlatformExtension.INITIAL_SERVER_ORGANIZATION_NAME);

        OMAGPlatformExtension.refreshCataloguer();
    }


    /**
     * The starting point: one server element, named without an organization name because the server's
     * configuration document does not have one.
     *
     * @throws Exception a retrieval failed
     */
    @Test
    @Order(1)
    @DisplayName("A server with no organization name is catalogued under its bare name")
    void serverIsCataloguedWithoutAnOrganizationName() throws Exception
    {
        List<OpenMetadataElement> serverElements = serverElementsForSubject();

        assertEquals(1,
                     serverElements.size(),
                     () -> "Expected exactly one element for server " + SUBJECT_SERVER + ", found "
                             + serverElements.size() + ":" + PlatformCatalogFvtTestSupport.describe(serverElements));

        OpenMetadataElement serverElement = serverElements.get(0);

        originalServerElementGUID = serverElement.getElementGUID();

        assertEquals(PlatformCatalogFvtTestSupport.expectedServerResourceName(SUBJECT_SERVER, null),
                     PlatformCatalogFvtTestSupport.getResourceName(serverElement),
                     "A server with no organization name should be catalogued under its bare server name");
    }


    /**
     * Setting the organization name afterwards renames the server that is already catalogued; it does not
     * catalog a second one.
     * <br>
     * A server is one thing in the real world, and the organization that owns it changing its mind about
     * what it is called does not make it two.  A duplicate here is not cosmetic: the two elements are both
     * attached to the platform, both are returned by a search for the server, and the one left behind is
     * frozen at the moment the name changed - it keeps its old endpoint, its old configuration and its old
     * deployment status for ever, because nothing will ever match it again.
     *
     * @throws Exception a retrieval or configuration change failed
     */
    @Test
    @Order(2)
    @DisplayName("Setting the organization name renames the catalogued server rather than duplicating it")
    void settingTheOrganizationNameRenamesTheServer() throws Exception
    {
        assertNotNull(originalServerElementGUID, "The starting-point test did not run");

        setOrganizationName(NEW_ORGANIZATION_NAME);

        OMAGPlatformExtension.refreshCataloguer();

        List<OpenMetadataElement> serverElements = serverElementsForSubject();

        assertEquals(1,
                     serverElements.size(),
                     () -> "Setting an organization name on " + SUBJECT_SERVER + " left " + serverElements.size()
                             + " elements for one server.  The cataloguer matches the servers already attached to"
                             + " the platform by resource name, and the organization name is part of that resource"
                             + " name, so a server whose organization name has changed is not recognised as one it"
                             + " already has:" + PlatformCatalogFvtTestSupport.describe(serverElements));

        OpenMetadataElement serverElement = serverElements.get(0);

        assertEquals(originalServerElementGUID,
                     serverElement.getElementGUID(),
                     "The server element that survives the organization name change is not the one that was"
                             + " already there, so everything else attached to the original - its endpoint, its"
                             + " capabilities, its place in any lineage - has been left behind with it");

        assertEquals(PlatformCatalogFvtTestSupport.expectedServerResourceName(SUBJECT_SERVER, NEW_ORGANIZATION_NAME),
                     PlatformCatalogFvtTestSupport.getResourceName(serverElement),
                     "The server element was not renamed to include the new organization name");
    }


    /**
     * Clearing the organization name again puts the server back under its bare name, still as one element.
     * The reverse direction matters as much as the forward one: an administrator who sets an organization
     * name, sees what it does to the catalog and takes it out again should be back where they started.
     *
     * @throws Exception a retrieval or configuration change failed
     */
    @Test
    @Order(3)
    @DisplayName("Clearing the organization name again renames the server back")
    void clearingTheOrganizationNameRenamesTheServerBack() throws Exception
    {
        setOrganizationName(OMAGPlatformExtension.INITIAL_SERVER_ORGANIZATION_NAME);

        OMAGPlatformExtension.refreshCataloguer();

        List<OpenMetadataElement> serverElements = serverElementsForSubject();

        assertEquals(1,
                     serverElements.size(),
                     () -> "Clearing the organization name on " + SUBJECT_SERVER + " left " + serverElements.size()
                             + " elements for one server:" + PlatformCatalogFvtTestSupport.describe(serverElements));

        assertEquals(PlatformCatalogFvtTestSupport.expectedServerResourceName(SUBJECT_SERVER, null),
                     PlatformCatalogFvtTestSupport.getResourceName(serverElements.get(0)),
                     "The server element was not renamed back to its bare server name");
    }


    /**
     * Change the organization name held in the subject server's configuration document.
     * <br>
     * The whole set of basic properties has to be sent, not just the organization name: the admin service
     * replaces them as a group, so sending the organization name alone would clear the server's userId, its
     * secrets store and its URL root along with it.
     *
     * @param organizationName new organization name - null to clear it
     * @throws Exception the configuration document could not be changed
     */
    private static void setOrganizationName(String organizationName) throws Exception
    {
        OMAGServerConfigurationClient configurationClient = OMAGPlatformExtension.getServerConfigurationClient(SUBJECT_SERVER);

        OMAGPlatformExtension.setBasicServerProperties(configurationClient, organizationName, SUBJECT_DESCRIPTION);
    }


    /**
     * Return every catalogued server element for the subject server, whatever it is currently called.  The
     * search is on the qualified name, which contains the server's own name whether or not an organization
     * name has been folded in beside it.
     *
     * @return list
     * @throws Exception a retrieval failed
     */
    private static List<OpenMetadataElement> serverElementsForSubject() throws Exception
    {
        return PlatformCatalogFvtTestSupport.getServerElementsFor(openMetadataStore, SUBJECT_SERVER);
    }
}
