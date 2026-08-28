/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.duplicatefvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.StatusIdentifier;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * DuplicateDetectionFVT checks what the generic handler does when a lookup by a name that should be unique
 * turns up more than one element: it fails the request - it cannot know which element the caller meant - and
 * it records the elements it found as discovered duplicates, so that the next thing along (a steward, or
 * Mendel) has something to work with.
 * <br>
 * This is the entry point of the whole duplicate management loop.  Before it existed, an ambiguous unique
 * name produced an exception and nothing else: the duplicates stayed invisible until somebody went looking.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class DuplicateDetectionFVT
{
    /**
     * Looking up an ambiguous unique name fails, and leaves a DISCOVERED duplicate link between the elements
     * that made it ambiguous.
     *
     * @throws Exception an unexpected failure is a test failure
     */
    @Test
    @DisplayName("An ambiguous unique name is reported and recorded")
    public void testAmbiguousUniqueNameIsRecorded() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        /*
         * The two elements of the undetected pair share a qualified name and nothing else, so this lookup is
         * ambiguous and is expected to fail.
         */
        try
        {
            openMetadataStore.getMetadataElementByUniqueName(DuplicateFvtTestSupport.UNDETECTED_QUALIFIED_NAME,
                                                              OpenMetadataProperty.QUALIFIED_NAME.name);

            fail("Looking up the ambiguous unique name " + DuplicateFvtTestSupport.UNDETECTED_QUALIFIED_NAME
                         + " should have failed - two elements share it");
        }
        catch (Exception expected)
        {
            /*
             * The message names the duplicates it found.  The exception itself is the contract; the
             * assertion below is about what it left behind.
             */
            System.out.println("duplicate-fvt: ambiguous unique name reported as expected - " + expected.getMessage());
        }

        /*
         * The lookup should have linked the two elements it could not choose between.  The link is retrieved
         * with forDuplicateProcessing set, because the repository handler removes duplicate links from the
         * results of an ordinary request.
         */
        OpenMetadataRelationship duplicateLink = DuplicateFvtTestSupport.getDuplicateLink(openMetadataStore,
                                                                                           DuplicateFvtTestSupport.UNDETECTED_GUID_ONE,
                                                                                           DuplicateFvtTestSupport.UNDETECTED_GUID_TWO);

        if (duplicateLink == null)
        {
            /*
             * The elements are linked oldest-first, and the two were created in the same archive load, so
             * which of them ends up at end one is not guaranteed - try the other direction before failing.
             */
            duplicateLink = DuplicateFvtTestSupport.getDuplicateLink(openMetadataStore,
                                                                      DuplicateFvtTestSupport.UNDETECTED_GUID_TWO,
                                                                      DuplicateFvtTestSupport.UNDETECTED_GUID_ONE);
        }

        assertNotNull(duplicateLink,
                       "The ambiguous unique name lookup did not record a duplicate link between "
                               + DuplicateFvtTestSupport.UNDETECTED_GUID_ONE + " and " + DuplicateFvtTestSupport.UNDETECTED_GUID_TWO);

        assertEquals(StatusIdentifier.DISCOVERED.getOrdinal(),
                      DuplicateFvtTestSupport.getStatusIdentifier(duplicateLink),
                      "The duplicate link recorded by the ambiguous unique name lookup should be DISCOVERED - it is a marker for a"
                              + " steward, not a decision");
    }


    /**
     * The marker is not created twice.  A repeated lookup of the same ambiguous name finds the link that is
     * already there rather than adding another - otherwise every failed retrieval would add one, and a name
     * looked up often would accumulate them indefinitely.
     *
     * @throws Exception an unexpected failure is a test failure
     */
    @Test
    @DisplayName("Repeating an ambiguous lookup does not add more links")
    public void testRepeatedLookupDoesNotDuplicateTheMarker() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        for (int attempt = 0; attempt < 3; attempt++)
        {
            try
            {
                openMetadataStore.getMetadataElementByUniqueName(DuplicateFvtTestSupport.UNDETECTED_QUALIFIED_NAME,
                                                                  OpenMetadataProperty.QUALIFIED_NAME.name);
            }
            catch (Exception expected)
            {
                // the lookup is expected to fail every time - the name is still ambiguous
            }
        }

        int linkCount = countDuplicateLinks(openMetadataStore,
                                             DuplicateFvtTestSupport.UNDETECTED_GUID_ONE,
                                             DuplicateFvtTestSupport.UNDETECTED_GUID_TWO);

        assertEquals(1,
                      linkCount,
                      "Repeated lookups of the same ambiguous unique name should reuse the duplicate link that is already there,"
                              + " but " + linkCount + " links were found between the two elements");
    }


    /**
     * Count the duplicate links between two elements, in either direction.
     *
     * @param openMetadataStore store to read through
     * @param elementOneGUID first element
     * @param elementTwoGUID second element
     * @return number of links
     * @throws Exception the retrieval failed
     */
    private int countDuplicateLinks(OpenMetadataStore openMetadataStore,
                                    String            elementOneGUID,
                                    String            elementTwoGUID) throws Exception
    {
        return DuplicateFvtTestSupport.getDuplicateLinks(openMetadataStore, elementOneGUID, elementTwoGUID).size();
    }
}
