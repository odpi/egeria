/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.duplicatefvt;

import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared conventions, fixture identifiers and helpers used across the duplicate-fvt test suite.
 * <br>
 * The fixture is seeded by {@link DuplicateArchiveWriter} from the constants below.  Every identifier is
 * fixed rather than generated so that a test can name the element it means, and every qualified name starts
 * with {@link #QUALIFIED_NAME_PREFIX} so that this suite's debris can always be found and purged - the
 * PostgreSQL repository behind this suite persists across runs, so anything a failed run left behind would
 * otherwise accumulate and confuse the next run's assertions.
 */
final class DuplicateFvtTestSupport
{
    /**
     * Every qualified name this suite creates starts with this prefix.
     */
    static final String QUALIFIED_NAME_PREFIX = "duplicate-fvt:";

    static final int MAX_PAGE_SIZE = 100;

    /**
     * Used to read property values out of the beans the repository returns.  A property value is a typed
     * object, not a string - reading one by picking the digits out of its toString() reports whatever
     * numbers happen to appear in the type information as well, which is how every status in this suite
     * once read back as 5.
     */
    private static final PropertyHelper propertyHelper = new PropertyHelper();

    /*
     * Set 1 - two elements sharing a qualified name, with nothing linking them.
     */
    static final String UNDETECTED_QUALIFIED_NAME = QUALIFIED_NAME_PREFIX + "Undetected";
    static final String UNDETECTED_GUID_ONE       = "7de67abe-f385-42f2-b695-94eae88f7575";
    static final String UNDETECTED_GUID_TWO       = "9acf47b6-3ede-4d10-b070-673eae57ac83";

    /*
     * Set 7 - two elements sharing a qualified name, with nothing linking them, that no test ever looks up
     * by unique name.  Set 1 cannot be used to check that unmanaged duplicates are left alone, because the
     * detection tests deliberately look its name up and that starts the very processing - marker, then
     * validation, then combination - that the assertion is checking does not happen on its own.  This set is
     * touched by nothing, so it stays unmanaged whatever order the tests run in.
     */
    static final String UNTOUCHED_QUALIFIED_NAME = QUALIFIED_NAME_PREFIX + "Untouched";
    static final String UNTOUCHED_GUID_ONE       = "844ea001-4861-4f25-8dad-f53fdfaefd6e";
    static final String UNTOUCHED_GUID_TWO       = "016c5a83-dc3f-4c64-be7e-23782d0da7e1";

    /*
     * Set 2 - two elements sharing a qualified name, linked as discovered duplicates.
     */
    static final String CLOSE_MATCH_QUALIFIED_NAME = QUALIFIED_NAME_PREFIX + "CloseMatch";
    static final String CLOSE_MATCH_GUID_ONE       = "18a2cfd2-9a0f-480c-8ac3-63b142740088";
    static final String CLOSE_MATCH_GUID_TWO       = "a12619e0-dd60-448b-8152-6add6caa9a3e";
    static final String CLOSE_MATCH_LINK_GUID      = "2e6c0e4f-b224-4c2c-9333-72459e09c6bf";

    /*
     * Set 3 - two elements with different qualified names, linked as discovered duplicates.
     */
    static final String DISTANT_MATCH_QUALIFIED_NAME_ONE = QUALIFIED_NAME_PREFIX + "DistantMatch:one";
    static final String DISTANT_MATCH_QUALIFIED_NAME_TWO = QUALIFIED_NAME_PREFIX + "DistantMatch:two";
    static final String DISTANT_MATCH_GUID_ONE           = "1b6a9fb9-d378-4641-8a97-9c9020d2e4b5";
    static final String DISTANT_MATCH_GUID_TWO           = "23334a40-9a65-4576-8e34-cbb0e6d545ba";
    static final String DISTANT_MATCH_LINK_GUID          = "7c550d2c-3c33-4932-af39-2f9bcc4241f3";

    /*
     * Set 4 - three elements sharing a qualified name, already validated and classified.
     */
    static final String CLUSTER_QUALIFIED_NAME = QUALIFIED_NAME_PREFIX + "Cluster";
    static final String CLUSTER_GUID_ONE       = "e7b5c968-f2ae-4705-aced-e87c4b43559c";
    static final String CLUSTER_GUID_TWO       = "9f1ed4d8-934e-4fe9-bac0-bc1d941f1226";
    static final String CLUSTER_GUID_THREE     = "0df076b6-eb09-43e7-997b-08de1a110703";
    static final String CLUSTER_LINK_GUID_ONE  = "55c6d103-b706-4f09-bae7-bb3b8dcba98d";
    static final String CLUSTER_LINK_GUID_TWO  = "cfe198c3-34b5-40af-93be-4e66ea61ca5a";

    /*
     * Set 6 - two elements sharing a qualified name, already validated and classified, but too few of them
     * to reach the consolidation cluster size.  The repository handler combines these on retrieval and
     * Mendel leaves them alone, which makes them a stable fixture for the retrieval tests.
     */
    static final String SMALL_CLUSTER_QUALIFIED_NAME = QUALIFIED_NAME_PREFIX + "SmallCluster";
    static final String SMALL_CLUSTER_GUID_ONE       = "21c017c3-6121-4780-a8a5-29705406356b";
    static final String SMALL_CLUSTER_GUID_TWO       = "37583797-333b-4b0b-8b55-5aaf077c191b";
    static final String SMALL_CLUSTER_LINK_GUID      = "da460400-dbdf-4fad-a083-6ce2d7233bd4";

    /*
     * Set 5 - two classified elements whose only link a steward has retired.
     */
    static final String RETIRED_QUALIFIED_NAME_ONE = QUALIFIED_NAME_PREFIX + "Retired:one";
    static final String RETIRED_QUALIFIED_NAME_TWO = QUALIFIED_NAME_PREFIX + "Retired:two";
    static final String RETIRED_GUID_ONE           = "7b8460ab-cae7-4008-8a5f-f15f5d11a68d";
    static final String RETIRED_GUID_TWO           = "81e33a5d-b846-408a-bf74-ed61a0837a6c";
    static final String RETIRED_LINK_GUID          = "133d24b2-9fcd-4c3c-944e-4dbd62158fac";


    /**
     * Every element the archive lays down, by unique identifier.  The identifiers are fixed, so the fixture
     * can be cleared before a run without having to search for it - which matters because the string search
     * does not find these elements, and an exact-name lookup would miss the consolidated elements that
     * Mendel derives names for.
     */
    static final List<String> FIXTURE_ELEMENT_GUIDS = List.of(UNDETECTED_GUID_ONE,
                                                               UNDETECTED_GUID_TWO,
                                                               UNTOUCHED_GUID_ONE,
                                                               UNTOUCHED_GUID_TWO,
                                                               CLOSE_MATCH_GUID_ONE,
                                                               CLOSE_MATCH_GUID_TWO,
                                                               DISTANT_MATCH_GUID_ONE,
                                                               DISTANT_MATCH_GUID_TWO,
                                                               CLUSTER_GUID_ONE,
                                                               CLUSTER_GUID_TWO,
                                                               CLUSTER_GUID_THREE,
                                                               SMALL_CLUSTER_GUID_ONE,
                                                               SMALL_CLUSTER_GUID_TWO,
                                                               RETIRED_GUID_ONE,
                                                               RETIRED_GUID_TWO);


    private DuplicateFvtTestSupport()
    {
        // no instances
    }


    /**
     * Return query options that ask for the duplicates to be shown as they are, rather than combined.  This
     * is the switch a steward's tooling sets to see what is really in the repository.
     *
     * @return query options with forDuplicateProcessing set
     */
    static QueryOptions forDuplicateProcessing()
    {
        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setForDuplicateProcessing(true);
        queryOptions.setPageSize(MAX_PAGE_SIZE);

        return queryOptions;
    }


    /**
     * Return get options that ask for the duplicates to be shown as they are, rather than combined.
     *
     * @return get options with forDuplicateProcessing set
     */
    static GetOptions getForDuplicateProcessing()
    {
        GetOptions getOptions = new GetOptions();

        getOptions.setForDuplicateProcessing(true);

        return getOptions;
    }


    /**
     * Return search options that ask for the duplicates to be shown as they are, rather than combined.
     *
     * @return search options with forDuplicateProcessing set
     */
    static SearchOptions searchForDuplicateProcessing()
    {
        SearchOptions searchOptions = new SearchOptions();

        searchOptions.setForDuplicateProcessing(true);
        searchOptions.setPageSize(MAX_PAGE_SIZE);

        return searchOptions;
    }


    /**
     * Retrieve one of the fixture's duplicate links.  The retrieval is always done with
     * forDuplicateProcessing set, because the repository handler removes duplicate links from the results of
     * an ordinary request - a check that forgot this would report every link as missing.
     *
     * @param openMetadataStore store to read through
     * @param endOneGUID element at end one of the link
     * @param endTwoGUID element at end two of the link
     * @return the relationship, or null if the two elements are not linked
     * @throws Exception the retrieval failed
     */
    static OpenMetadataRelationship getDuplicateLink(OpenMetadataStore openMetadataStore,
                                                     String            endOneGUID,
                                                     String            endTwoGUID) throws Exception
    {
        Map<String, OpenMetadataRelationship> duplicateLinks = getDuplicateLinks(openMetadataStore, endOneGUID, endTwoGUID);

        if (duplicateLinks.isEmpty())
        {
            return null;
        }

        return duplicateLinks.values().iterator().next();
    }


    /**
     * Return the duplicate links between two elements, keyed by relationship identifier.
     * <br>
     * Keying by identifier is what makes this a count of links rather than a count of results: the same
     * relationship can legitimately be reported more than once - it has a version history, and a match is
     * checked in both directions - and counting results would report one link as several.
     *
     * @param openMetadataStore store to read through
     * @param endOneGUID element at one end of the link
     * @param endTwoGUID element at the other end
     * @return map of relationship identifier to relationship - empty if the two are not linked
     * @throws Exception the retrieval failed
     */
    static Map<String, OpenMetadataRelationship> getDuplicateLinks(OpenMetadataStore openMetadataStore,
                                                                   String            endOneGUID,
                                                                   String            endTwoGUID) throws Exception
    {
        Map<String, OpenMetadataRelationship> duplicateLinks = new HashMap<>();

        OpenMetadataRelationshipList relationships =
                openMetadataStore.findRelationshipsBetweenMetadataElements(OpenMetadataType.PEER_DUPLICATE_LINK.typeName,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            forDuplicateProcessing());

        if ((relationships != null) && (relationships.getRelationships() != null))
        {
            for (OpenMetadataRelationship relationship : relationships.getRelationships())
            {
                if ((relationship != null) && (relationship.getElementAtEnd1() != null) && (relationship.getElementAtEnd2() != null))
                {
                    String actualEndOne = relationship.getElementAtEnd1().getGUID();
                    String actualEndTwo = relationship.getElementAtEnd2().getGUID();

                    if ((endOneGUID.equals(actualEndOne) && endTwoGUID.equals(actualEndTwo))
                                || (endOneGUID.equals(actualEndTwo) && endTwoGUID.equals(actualEndOne)))
                    {
                        duplicateLinks.put(relationship.getRelationshipGUID(), relationship);
                    }
                }
            }
        }

        return duplicateLinks;
    }


    /**
     * Return the status identifier recorded on a duplicate link.
     *
     * @param relationship the duplicate link
     * @return status identifier, or -1 if the property is not set
     */
    static int getStatusIdentifier(OpenMetadataRelationship relationship)
    {
        final String methodName = "getStatusIdentifier";

        if (relationship == null)
        {
            return -1;
        }

        return propertyHelper.getIntProperty(QUALIFIED_NAME_PREFIX,
                                              OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                              relationship.getRelationshipProperties(),
                                              methodName);
    }


    /**
     * Determine whether an element carries a particular classification.
     *
     * @param element element to test
     * @param classificationName name of the classification to look for
     * @return boolean flag
     */
    static boolean hasClassification(OpenMetadataElement element,
                                     String              classificationName)
    {
        if ((element != null) && (element.getClassifications() != null))
        {
            for (AttachedClassification classification : element.getClassifications())
            {
                if ((classification != null) && (classificationName.equals(classification.getClassificationName())))
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Retrieve every element in the repository whose qualified name starts with this suite's prefix, with
     * the deduplication switched off so that each duplicate is returned separately.
     *
     * @param openMetadataStore store to read through
     * @return list of elements - empty if the fixture has not been loaded
     * @throws Exception the retrieval failed
     */
    static List<OpenMetadataElement> getAllFixtureElements(OpenMetadataStore openMetadataStore) throws Exception
    {
        List<OpenMetadataElement> fixtureElements = new ArrayList<>();

        SearchOptions searchOptions = searchForDuplicateProcessing();

        searchOptions.setMetadataElementTypeName(OpenMetadataType.REFERENCEABLE.typeName);

        List<OpenMetadataElement> retrievedElements = openMetadataStore.findMetadataElementsWithString(QUALIFIED_NAME_PREFIX, searchOptions);

        if (retrievedElements != null)
        {
            fixtureElements.addAll(retrievedElements);
        }

        return fixtureElements;
    }


    /**
     * Permanently remove an element.  Used by the leftover-element cleanup that runs before the fixture is
     * loaded, so that a previous run's debris cannot be mistaken for this run's fixture.
     * <br><br>
     * A cleanup failure is reported rather than discarded, but does not stop the run: the elements are keyed
     * by fixed identifiers, so a leftover element is usually the same element the fixture is about to
     * recreate, and the archive load handles that case itself.
     *
     * @param openMetadataStore store to delete through
     * @param elementGUID element to remove
     */
    static void purgeElement(OpenMetadataStore openMetadataStore,
                             String            elementGUID)
    {
        try
        {
            DeleteOptions softDeleteOptions = new DeleteOptions();

            softDeleteOptions.setForDuplicateProcessing(true);
            softDeleteOptions.setForLineage(true);

            try
            {
                openMetadataStore.deleteMetadataElementInStore(elementGUID, softDeleteOptions);
            }
            catch (Exception alreadyDeleted)
            {
                // ordinary - the element may already be soft-deleted
            }
        }
        catch (Exception error)
        {
            System.err.println("duplicate-fvt: could not purge element " + elementGUID + " - "
                                       + error.getClass().getSimpleName() + ": " + error.getMessage());
        }
    }
}
