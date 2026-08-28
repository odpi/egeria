/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.duplicatefvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DeduplicationRetrievalFVT checks the deduplication that the repository handler performs on the way out of
 * the repository - the behaviour every caller depends on without knowing it is there - and the
 * {@code forDuplicateProcessing} switch that turns it off.
 * <br>
 * The fixture it works from is stable: the small validated cluster (set 6) is confirmed and classified but
 * has fewer members than Mendel's consolidation cluster size, so Mendel leaves it exactly as the archive
 * loaded it however many times it runs.  That matters because these assertions are about counts, and a
 * fixture that Mendel rewrote underneath them would make the results depend on test ordering.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class DeduplicationRetrievalFVT
{
    /**
     * Two elements that share a qualified name, are linked with a VALIDATED duplicate link and are both
     * classified as KnownDuplicate are combined into one on retrieval.  This is the whole point of duplicate
     * management: a caller that knows nothing about any of it sees one element.
     * <br>
     * The check is made by looking the shared qualified name up as a unique name.  That call fails when it
     * finds more than one element, so it succeeding is itself the evidence that the two were combined - and
     * it is the call an ordinary caller would make, rather than a search whose results have to be counted.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Validated, classified duplicates are combined on retrieval")
    public void testValidatedDuplicatesAreCombined() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataElement combinedElement = openMetadataStore.getMetadataElementByUniqueName(DuplicateFvtTestSupport.SMALL_CLUSTER_QUALIFIED_NAME,
                                                                                                OpenMetadataProperty.QUALIFIED_NAME.name);

        assertNotNull(combinedElement,
                       "Nothing was returned for the qualified name " + DuplicateFvtTestSupport.SMALL_CLUSTER_QUALIFIED_NAME
                               + ", which two validated, classified duplicates share");

        Set<String> clusterGUIDs = Set.of(DuplicateFvtTestSupport.SMALL_CLUSTER_GUID_ONE,
                                           DuplicateFvtTestSupport.SMALL_CLUSTER_GUID_TWO);

        assertTrue(clusterGUIDs.contains(combinedElement.getElementGUID()),
                    "The combined element " + combinedElement.getElementGUID() + " is not one of the cluster's members " + clusterGUIDs);
    }


    /**
     * The same name looked up with {@code forDuplicateProcessing} set returns both underlying elements.
     * This is the view a steward's tooling needs - it has to see the duplicates to be able to resolve them -
     * and the contrast with the combined result above is what shows the deduplication is doing the work.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("forDuplicateProcessing shows the underlying duplicates")
    public void testForDuplicateProcessingShowsBothElements() throws Exception
    {
        OpenMetadataClient openMetadataClient = ConnectorContextFactory.newOpenMetadataClient();

        List<OpenMetadataElement> combinedElements = getElementsByQualifiedName(openMetadataClient,
                                                                                 DuplicateFvtTestSupport.SMALL_CLUSTER_QUALIFIED_NAME,
                                                                                 false);

        assertEquals(1,
                      combinedElements.size(),
                      "Expected the two validated duplicates sharing qualified name "
                              + DuplicateFvtTestSupport.SMALL_CLUSTER_QUALIFIED_NAME + " to be returned as one combined element, but got "
                              + describe(combinedElements));

        List<OpenMetadataElement> separateElements = getElementsByQualifiedName(openMetadataClient,
                                                                                 DuplicateFvtTestSupport.SMALL_CLUSTER_QUALIFIED_NAME,
                                                                                 true);

        assertEquals(2,
                      separateElements.size(),
                      "Expected forDuplicateProcessing to show both duplicates sharing qualified name "
                              + DuplicateFvtTestSupport.SMALL_CLUSTER_QUALIFIED_NAME + ", but got " + describe(separateElements));

        List<String> separateGUIDs = separateElements.stream().map(OpenMetadataElement::getElementGUID).toList();

        assertTrue(separateGUIDs.contains(DuplicateFvtTestSupport.SMALL_CLUSTER_GUID_ONE),
                    "Cluster member one is missing from " + separateGUIDs);
        assertTrue(separateGUIDs.contains(DuplicateFvtTestSupport.SMALL_CLUSTER_GUID_TWO),
                    "Cluster member two is missing from " + separateGUIDs);
    }


    /**
     * Elements that merely share a qualified name are not combined.  Deduplication acts on the links and
     * classifications a steward has confirmed, not on a resemblance it has noticed for itself, so the
     * unmanaged pair is returned as two elements whichever way it is asked for.
     * <br>
     * This uses the pair that no other test looks up.  The pair the detection tests use is deliberately
     * driven through detection, validation and combination by those tests, so an assertion that it stays
     * unmanaged would pass or fail on the order the test classes happened to run in.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Unlinked, unclassified duplicates are left alone")
    public void testUnmanagedDuplicatesAreNotCombined() throws Exception
    {
        OpenMetadataClient openMetadataClient = ConnectorContextFactory.newOpenMetadataClient();

        List<OpenMetadataElement> elements = getElementsByQualifiedName(openMetadataClient,
                                                                         DuplicateFvtTestSupport.UNTOUCHED_QUALIFIED_NAME,
                                                                         false);

        assertEquals(2,
                      elements.size(),
                      "Expected the two unmanaged duplicates sharing qualified name "
                              + DuplicateFvtTestSupport.UNTOUCHED_QUALIFIED_NAME
                              + " to be returned separately - nothing has confirmed they are the same thing - but got " + describe(elements));
    }


    /**
     * The elements of the validated cluster are not combined when the retrieval is done element by element
     * with forDuplicateProcessing set: each unique identifier still resolves to its own element.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("forDuplicateProcessing retrieves each duplicate by its own identifier")
    public void testForDuplicateProcessingByGUID() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        for (String memberGUID : List.of(DuplicateFvtTestSupport.SMALL_CLUSTER_GUID_ONE,
                                          DuplicateFvtTestSupport.SMALL_CLUSTER_GUID_TWO))
        {
            OpenMetadataElement element = openMetadataStore.getMetadataElementByGUID(memberGUID,
                                                                                      DuplicateFvtTestSupport.getForDuplicateProcessing());

            assertNotNull(element, "Cluster member " + memberGUID + " could not be retrieved with forDuplicateProcessing set");
            assertEquals(memberGUID,
                          element.getElementGUID(),
                          "Retrieving " + memberGUID + " with forDuplicateProcessing set returned a different element");

            assertTrue(DuplicateFvtTestSupport.hasClassification(element, OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName),
                        "Cluster member " + memberGUID + " has lost its KnownDuplicate classification");
        }
    }


    /**
     * A retired duplicate link means the elements are no longer treated as the same thing.  Whatever Mendel
     * has or has not done to the classifications by the time this runs, the two elements are returned
     * separately - they never shared a qualified name, so nothing would combine them anyway, and this is the
     * assertion that a DEPRECATED link cannot combine anything.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("A retired duplicate link does not combine its elements")
    public void testRetiredLinkDoesNotCombine() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataElement elementOne = openMetadataStore.getMetadataElementByGUID(DuplicateFvtTestSupport.RETIRED_GUID_ONE);
        OpenMetadataElement elementTwo = openMetadataStore.getMetadataElementByGUID(DuplicateFvtTestSupport.RETIRED_GUID_TWO);

        assertNotNull(elementOne, "The first element of the retired pair could not be retrieved");
        assertNotNull(elementTwo, "The second element of the retired pair could not be retrieved");

        assertFalse(elementOne.getElementGUID().equals(elementTwo.getElementGUID()),
                     "The elements of the retired pair were combined, even though their duplicate link is DEPRECATED");
    }




    /**
     * Retrieve the elements with an exact qualified name, with the deduplication either on or off.
     * <br>
     * This uses the client's exact-match, list-returning lookup rather than a search string: the string
     * search treats its argument as a pattern over any string property, and returned nothing at all for
     * these elements.
     *
     * @param openMetadataClient client to read through
     * @param qualifiedName qualified name to look for
     * @param forDuplicateProcessing should the deduplication be switched off?
     * @return the elements returned - empty if there are none
     * @throws Exception the retrieval failed
     */
    private List<OpenMetadataElement> getElementsByQualifiedName(OpenMetadataClient openMetadataClient,
                                                                 String             qualifiedName,
                                                                 boolean            forDuplicateProcessing) throws Exception
    {
        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setForDuplicateProcessing(forDuplicateProcessing);
        queryOptions.setPageSize(DuplicateFvtTestSupport.MAX_PAGE_SIZE);

        List<OpenMetadataElement> elements = openMetadataClient.getMetadataElementsByPropertyValue(OMAGPlatformExtension.USER_ID,
                                                                                                   List.of(OpenMetadataProperty.QUALIFIED_NAME.name),
                                                                                                   qualifiedName,
                                                                                                   queryOptions);

        if (elements == null)
        {
            return new ArrayList<>();
        }

        return elements;
    }


    /**
     * Describe a list of elements for a failure message.
     *
     * @param elements elements to describe
     * @return their unique identifiers
     */
    private String describe(List<OpenMetadataElement> elements)
    {
        return elements.stream().map(OpenMetadataElement::getElementGUID).toList().toString();
    }
}
