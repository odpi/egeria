/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.duplicatefvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.StatusIdentifier;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MendelDuplicateManagementFVT drives the Mendel Automated Duplicate Manager and checks each of the three
 * things it does with the duplicate links it finds.
 * <br>
 * Mendel's refresh is triggered explicitly rather than waited for: its configured interval is 24 hours, and
 * a suite that waited for it would never finish.  The refresh is synchronous, so by the time the call
 * returns the connector has completed all three of its passes over the fixture.  One refresh is enough for
 * every assertion here, so it is done once for the class.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class MendelDuplicateManagementFVT
{
    /**
     * Run Mendel once before the assertions.  Everything this class checks is a consequence of that one
     * refresh.
     *
     * @throws Exception a failure to refresh is fatal to every test in this class
     */
    @BeforeAll
    public static void refreshMendel() throws Exception
    {
        OMAGPlatformExtension.refreshMendel();
    }


    /**
     * Two elements of the same type with the same qualified name are a close match, so Mendel confirms them
     * on its own authority: it moves the link to VALIDATED and classifies both elements as KnownDuplicate.
     * That combination is what makes the repository handler start combining them.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Mendel validates a close match and classifies both elements")
    public void testCloseMatchIsValidated() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataRelationship duplicateLink = DuplicateFvtTestSupport.getDuplicateLink(openMetadataStore,
                                                                                           DuplicateFvtTestSupport.CLOSE_MATCH_GUID_ONE,
                                                                                           DuplicateFvtTestSupport.CLOSE_MATCH_GUID_TWO);

        assertNotNull(duplicateLink, "The close match pair's duplicate link has disappeared");

        assertEquals(StatusIdentifier.VALIDATED.getOrdinal(),
                      DuplicateFvtTestSupport.getStatusIdentifier(duplicateLink),
                      "Mendel should have validated the close match pair - the two elements are the same type with the same"
                              + " qualified name");

        for (String elementGUID : List.of(DuplicateFvtTestSupport.CLOSE_MATCH_GUID_ONE,
                                           DuplicateFvtTestSupport.CLOSE_MATCH_GUID_TWO))
        {
            OpenMetadataElement element = openMetadataStore.getMetadataElementByGUID(elementGUID,
                                                                                      DuplicateFvtTestSupport.getForDuplicateProcessing());

            assertNotNull(element, "Close match element " + elementGUID + " could not be retrieved");
            assertTrue(DuplicateFvtTestSupport.hasClassification(element, OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName),
                        "Mendel validated the close match pair's link but did not classify " + elementGUID + " as a KnownDuplicate."
                                + "  Both are needed before the elements are combined on retrieval.");
        }
    }


    /**
     * Two elements that do not share a qualified name are not a close match, whatever linked them.  Mendel
     * leaves the link alone and asks a steward instead - and creates the role to ask, if it is not there.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Mendel refers a distant match to a steward")
    public void testDistantMatchIsReferredToASteward() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataRelationship duplicateLink = DuplicateFvtTestSupport.getDuplicateLink(openMetadataStore,
                                                                                           DuplicateFvtTestSupport.DISTANT_MATCH_GUID_ONE,
                                                                                           DuplicateFvtTestSupport.DISTANT_MATCH_GUID_TWO);

        assertNotNull(duplicateLink, "The distant match pair's duplicate link has disappeared");

        assertEquals(StatusIdentifier.DISCOVERED.getOrdinal(),
                      DuplicateFvtTestSupport.getStatusIdentifier(duplicateLink),
                      "Mendel should have left the distant match pair's link as DISCOVERED - the two elements do not share a"
                              + " qualified name, so combining them is a steward's decision");

        for (String elementGUID : List.of(DuplicateFvtTestSupport.DISTANT_MATCH_GUID_ONE,
                                           DuplicateFvtTestSupport.DISTANT_MATCH_GUID_TWO))
        {
            OpenMetadataElement element = openMetadataStore.getMetadataElementByGUID(elementGUID,
                                                                                      DuplicateFvtTestSupport.getForDuplicateProcessing());

            assertNotNull(element, "Distant match element " + elementGUID + " could not be retrieved");
            assertFalse(DuplicateFvtTestSupport.hasClassification(element, OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName),
                         "Mendel classified " + elementGUID + " as a KnownDuplicate even though it referred the decision to a steward");
        }

        /*
         * The decision is passed on as a to do, and the to dos are assigned to a role that Mendel creates
         * the first time it needs one.
         */
        assertNotNull(findElementByQualifiedName(openMetadataStore,
                                                  OpenMetadataType.PERSON_ROLE.typeName,
                                                  OpenMetadataType.PERSON_ROLE.typeName + "::DuplicateMetadataSteward"),
                       "Mendel did not create the DuplicateMetadataSteward person role to assign its to dos to");

        assertTrue(countToDos(openMetadataStore) > 0,
                    "Mendel did not raise a to do for the duplicates it could not decide about");
    }


    /**
     * When a steward retires the last live duplicate link on an element, the element should stop being
     * treated as a duplicate.  Mendel takes the KnownDuplicate classification off.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Mendel retires the classifications of separated duplicates")
    public void testRetiredDuplicatesAreDeclassified() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        for (String elementGUID : List.of(DuplicateFvtTestSupport.RETIRED_GUID_ONE,
                                           DuplicateFvtTestSupport.RETIRED_GUID_TWO))
        {
            OpenMetadataElement element = openMetadataStore.getMetadataElementByGUID(elementGUID,
                                                                                      DuplicateFvtTestSupport.getForDuplicateProcessing());

            assertNotNull(element, "Retired duplicate " + elementGUID + " could not be retrieved");
            assertFalse(DuplicateFvtTestSupport.hasClassification(element, OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName),
                         "Mendel should have removed the KnownDuplicate classification from " + elementGUID + " - its only duplicate"
                                 + " link has been DEPRECATED by a steward, so there is nothing left for it to be combined with");
        }
    }


    /**
     * A cluster of validated duplicates that reaches the configured size is consolidated into a single
     * element, which is linked back to each of its members and carries a validated ConsolidatedDuplicate
     * classification - the combination the repository handler looks for when it returns the consolidated
     * element in place of the members.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Mendel consolidates a validated cluster")
    public void testValidatedClusterIsConsolidated() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        String consolidatedElementGUID = null;

        for (String memberGUID : List.of(DuplicateFvtTestSupport.CLUSTER_GUID_ONE,
                                          DuplicateFvtTestSupport.CLUSTER_GUID_TWO,
                                          DuplicateFvtTestSupport.CLUSTER_GUID_THREE))
        {
            /*
             * Read as stored, not combined: a consolidated member is returned as the consolidated element
             * with every member's relationships merged onto it, so a combined view would report each member
             * as having one link per member.
             */
            RelatedMetadataElementList consolidatedLinks =
                    openMetadataStore.getRelatedMetadataElements(memberGUID,
                                                                  1,
                                                                  OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK.typeName,
                                                                  DuplicateFvtTestSupport.forDuplicateProcessing());

            assertNotNull(consolidatedLinks, "Cluster member " + memberGUID + " is not linked to a consolidated element");
            assertNotNull(consolidatedLinks.getElementList(), "Cluster member " + memberGUID + " is not linked to a consolidated element");
            assertEquals(1,
                          consolidatedLinks.getElementList().size(),
                          "Cluster member " + memberGUID + " should be linked to exactly one consolidated element");

            RelatedMetadataElement consolidatedLink = consolidatedLinks.getElementList().get(0);

            if (consolidatedElementGUID == null)
            {
                consolidatedElementGUID = consolidatedLink.getElement().getElementGUID();
            }
            else
            {
                assertEquals(consolidatedElementGUID,
                              consolidatedLink.getElement().getElementGUID(),
                              "The members of the cluster were consolidated into more than one element");
            }
        }

        assertNotNull(consolidatedElementGUID, "The validated cluster was not consolidated");

        OpenMetadataElement consolidatedElement = openMetadataStore.getMetadataElementByGUID(consolidatedElementGUID,
                                                                                              DuplicateFvtTestSupport.getForDuplicateProcessing());

        assertNotNull(consolidatedElement, "The consolidated element " + consolidatedElementGUID + " could not be retrieved");

        assertTrue(DuplicateFvtTestSupport.hasClassification(consolidatedElement,
                                                              OpenMetadataType.CONSOLIDATED_DUPLICATE_CLASSIFICATION.typeName),
                    "The consolidated element is missing its ConsolidatedDuplicate classification, so the retrieval processing will"
                            + " ignore it and go on returning the members separately");

        /*
         * Only one of the members carried the Confidentiality classification.  Once the cluster is consolidated
         * that member is no longer returned, so a consolidated element without the classification means the
         * steward's decision has been silently dropped.
         */
        assertTrue(DuplicateFvtTestSupport.hasClassification(consolidatedElement,
                                                              OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName),
                    "The consolidated element did not pick up the Confidentiality classification that cluster member "
                            + DuplicateFvtTestSupport.CLUSTER_GUID_THREE + " carries");

        /*
         * The duplicate management classifications belong to the members, not to the element that replaces
         * them - a consolidated element that claims to be a KnownDuplicate has nothing to be a duplicate of.
         */
        assertFalse(DuplicateFvtTestSupport.hasClassification(consolidatedElement,
                                                               OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName),
                     "The consolidated element picked up the KnownDuplicate classification from its members");
    }


    /**
     * A cluster with fewer members than the configured cluster size is left alone.  This is the other half
     * of the consolidation rule, and the reason the retrieval tests have a stable fixture to work from.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Mendel leaves a cluster below the configured size alone")
    public void testSmallClusterIsNotConsolidated() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        for (String memberGUID : List.of(DuplicateFvtTestSupport.SMALL_CLUSTER_GUID_ONE,
                                          DuplicateFvtTestSupport.SMALL_CLUSTER_GUID_TWO))
        {
            RelatedMetadataElementList consolidatedLinks =
                    openMetadataStore.getRelatedMetadataElements(memberGUID,
                                                                  1,
                                                                  OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK.typeName,
                                                                  DuplicateFvtTestSupport.forDuplicateProcessing());

            boolean consolidated = (consolidatedLinks != null)
                                           && (consolidatedLinks.getElementList() != null)
                                           && (! consolidatedLinks.getElementList().isEmpty());

            assertFalse(consolidated,
                         "The small cluster has only two members, which is fewer than the configured cluster size, so Mendel should"
                                 + " not have consolidated it - but " + memberGUID + " is linked to a consolidated element");
        }
    }


    /**
     * Find an element by its exact qualified name, with the deduplication switched off.
     *
     * @param openMetadataStore store to read through
     * @param typeName type to search within
     * @param qualifiedName qualified name to look for
     * @return the element, or null if it is not there
     * @throws Exception the retrieval failed
     */
    private OpenMetadataElement findElementByQualifiedName(OpenMetadataStore openMetadataStore,
                                                           String            typeName,
                                                           String            qualifiedName) throws Exception
    {
        SearchOptions searchOptions = DuplicateFvtTestSupport.searchForDuplicateProcessing();

        searchOptions.setMetadataElementTypeName(typeName);

        List<OpenMetadataElement> elements = openMetadataStore.findMetadataElementsWithString(qualifiedName, searchOptions);

        if ((elements != null) && (! elements.isEmpty()))
        {
            return elements.get(0);
        }

        return null;
    }


    /**
     * Count the to dos that Mendel has raised.  They are recognisable by the qualified name it gives them,
     * which starts with the connector's name.
     *
     * @param openMetadataStore store to read through
     * @return number of to dos
     * @throws Exception the retrieval failed
     */
    private int countToDos(OpenMetadataStore openMetadataStore) throws Exception
    {
        SearchOptions searchOptions = DuplicateFvtTestSupport.searchForDuplicateProcessing();

        searchOptions.setMetadataElementTypeName(OpenMetadataType.TO_DO.typeName);

        List<OpenMetadataElement> toDos = openMetadataStore.findMetadataElementsWithString(OMAGPlatformExtension.MENDEL_CONNECTOR_NAME,
                                                                                            searchOptions);

        if (toDos == null)
        {
            return 0;
        }

        return toDos.size();
    }
}
