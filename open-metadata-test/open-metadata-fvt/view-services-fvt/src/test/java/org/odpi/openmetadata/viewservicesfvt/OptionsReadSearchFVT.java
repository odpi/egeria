/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.rest.NewOpenMetadataElementRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataDeleteRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.UpdatePropertiesRequestBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.OptionsReadSupport.*;

/**
 * OptionsReadSearchFVT checks the paging, query and search options on a search request: each test sets one option
 * and compares the result with the same search without it.
 * <br>
 * The fixture is five DataFile elements (one of them the CSVFile subtype) created one after another, then updated
 * in a different order, and given descriptions and display names that sort in two further orders - so that every
 * sequencing order gives a different, predictable answer.  Searches use the Metadata Expert's generic search by
 * string, which hands the options straight to the open metadata store, except for the governance zone filter,
 * which is applied by the view services' root-element handler and so is tested through Asset Maker.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OptionsReadSearchFVT
{
    private static final String DATA_FILE = OpenMetadataType.DATA_FILE.typeName;
    private static final String CSV_FILE  = OpenMetadataType.CSV_FILE.typeName;
    private static final String LOCATION  = OpenMetadataType.LOCATION.typeName;
    private static final String INCOMPLETE = OpenMetadataType.INCOMPLETE_CLASSIFICATION.typeName;

    private static final String PREFIX = prefix("search");

    /*
     * The part of every fixture qualified name that follows the suite marker: it includes the run identifier, so a
     * contains-search finds the fixture, but nothing starts with it.
     */
    private static final String MIDDLE = PREFIX.substring(ViewServicesFvtTestSupport.TEST_MARKER.length() + 2);

    private static final String REGEX_NAME = "Re.x*[1]+";

    private static final String ZONE_A = "optread-zone-a-" + OMAGPlatformExtension.RUN_ID;
    private static final String ZONE_B = "optread-zone-b-" + OMAGPlatformExtension.RUN_ID;

    private static String alpha = null;
    private static String beta  = null;
    private static String gamma = null;
    private static String delta = null;
    private static String regex = null;

    private static List<String> creationOrder = null;
    private static List<String> updateOrder   = null;
    private static List<String> byDescription = null;
    private static List<String> byDisplayName = null;
    private static List<String> fixture       = null;

    private static final List<String> anchorFixture = new ArrayList<>();


    /**
     * Create the five elements, one after another, then update their descriptions in a different order.
     */
    @BeforeAll
    static void createFixture()
    {
        alpha = createElement(DATA_FILE, PREFIX + "Alpha", "alpha", "initial");
        tick();
        beta = createElement(DATA_FILE, PREFIX + "beta", "beta", "initial");
        tick();
        gamma = createElement(DATA_FILE, PREFIX + "Gamma", "gamma", "initial");
        tick();
        delta = createElement(CSV_FILE, PREFIX + "Delta", "delta", "initial");
        tick();
        regex = createElement(DATA_FILE, PREFIX + REGEX_NAME, REGEX_NAME.toLowerCase(), "initial");

        creationOrder = List.of(alpha, beta, gamma, delta, regex);
        fixture       = creationOrder;

        tick();
        updateDescription(gamma, "d-4");
        tick();
        updateDescription(alpha, "d-3");
        tick();
        updateDescription(regex, "d-5");
        tick();
        updateDescription(delta, "d-2");
        tick();
        updateDescription(beta, "d-1");

        updateOrder   = List.of(gamma, alpha, regex, delta, beta);
        byDescription = List.of(beta, delta, alpha, gamma, regex);
        byDisplayName = List.of(alpha, beta, delta, gamma, regex);
    }


    /**
     * Remove everything the tests created.
     */
    @AfterAll
    static void removeFixture()
    {
        List<String> all = new ArrayList<>(fixture == null ? List.of() : fixture);

        all.addAll(anchorFixture);

        Collections.reverse(all);

        for (String guid : all)
        {
            metadataExpert.postUnchecked("/metadata-elements/" + guid + "/restore",
                                         new org.odpi.openmetadata.commonservices.ffdc.rest.MetadataSourceRequestBody(),
                                         org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse.class);
        }

        purgeAll(all);
    }


    /*
     * ===================================== SearchOptions =====================================
     */


    /**
     * By default the search string is matched anywhere in any string property, ignoring case.
     */
    @Test
    @Order(1)
    void searchDefaultsToContainsIgnoringCase()
    {
        assertEquals(Set.copyOf(fixture), Set.copyOf(only(searchGUIDs(searchFor(MIDDLE)), fixture)),
                     "a contains-search for the middle of the qualified names should find every fixture element");
        assertEquals(Set.copyOf(fixture), Set.copyOf(only(searchGUIDs(searchFor(MIDDLE.toUpperCase())), fixture)),
                     "the default search should ignore case");
    }


    /**
     * startsWith: the value must start with the search string.
     */
    @Test
    @Order(2)
    void startsWith()
    {
        SearchStringRequestBody requestBody = searchFor(MIDDLE);

        requestBody.setStartsWith(true);

        assertTrue(only(searchGUIDs(requestBody), fixture).isEmpty(),
                   "startsWith found elements whose values only contain the search string");

        requestBody.setSearchString(PREFIX);

        assertEquals(Set.copyOf(fixture), Set.copyOf(only(searchGUIDs(requestBody), fixture)),
                     "startsWith with the start of the qualified names should find every fixture element");
    }


    /**
     * endsWith: the value must end with the search string.
     */
    @Test
    @Order(3)
    void endsWith()
    {
        SearchStringRequestBody requestBody = searchFor(MIDDLE);

        requestBody.setEndsWith(true);

        assertTrue(only(searchGUIDs(requestBody), fixture).isEmpty(),
                   "endsWith found elements whose values only contain the search string");

        requestBody.setSearchString("search::Alpha");

        assertEquals(List.of(alpha), only(searchGUIDs(requestBody), fixture),
                     "endsWith with the end of alpha's qualified name should find only alpha");
    }


    /**
     * startsWith and endsWith together: an exact match.
     */
    @Test
    @Order(4)
    void startsWithAndEndsWithIsAnExactMatch()
    {
        SearchStringRequestBody requestBody = searchFor(PREFIX + "Alpha");

        requestBody.setStartsWith(true);
        requestBody.setEndsWith(true);

        assertEquals(List.of(alpha), only(searchGUIDs(requestBody), fixture));

        requestBody.setSearchString(PREFIX + "Alph");

        assertTrue(only(searchGUIDs(requestBody), fixture).isEmpty(), "an exact match found an element on part of its qualified name");
    }


    /**
     * ignoreCase=false makes case significant; ignoreCase=true (the default) does not.
     */
    @Test
    @Order(5)
    void ignoreCase()
    {
        SearchStringRequestBody requestBody = searchFor("search::ALPHA");

        requestBody.setIgnoreCase(true);

        assertEquals(List.of(alpha), only(searchGUIDs(requestBody), fixture), "ignoreCase=true should match whatever the case");

        requestBody.setIgnoreCase(false);

        assertTrue(only(searchGUIDs(requestBody), fixture).isEmpty(), "ignoreCase=false matched a value in a different case");

        requestBody.setSearchString("search::Alpha");

        assertEquals(List.of(alpha), only(searchGUIDs(requestBody), fixture), "ignoreCase=false should match the value in its own case");

        requestBody.setSearchString(PREFIX.toUpperCase());

        assertTrue(only(searchGUIDs(requestBody), fixture).isEmpty(), "ignoreCase=false matched the upper-cased prefix");
    }


    /**
     * A search string that looks like a regular expression is matched literally: the metacharacters match only
     * themselves.
     */
    @Test
    @Order(6)
    void regularExpressionCharactersAreLiteral()
    {
        assertEquals(List.of(regex), only(searchGUIDs(searchFor("search::" + REGEX_NAME)), fixture),
                     "a search string containing . * [ ] + should find the element whose name contains those characters");

        assertTrue(only(searchGUIDs(searchFor(PREFIX + ".*")), fixture).isEmpty(),
                   "'.*' was treated as a regular expression");

        assertTrue(only(searchGUIDs(searchFor("search::A.pha")), fixture).isEmpty(),
                   "'.' was treated as a regular expression wildcard");
    }


    /*
     * ===================================== PagingOptions =====================================
     */


    /**
     * Paging through the results a page at a time returns every element once, in the same order as a single
     * page, with no gaps or duplicates.
     */
    @Test
    @Order(10)
    void pagingHasNoGapsOrDuplicates()
    {
        List<String> paged = new ArrayList<>();
        List<Integer> pageSizes = new ArrayList<>();

        for (int startFrom = 0; startFrom < 20; startFrom += 2)
        {
            SearchStringRequestBody requestBody = ordered(SequencingOrder.PROPERTY_ASCENDING, OpenMetadataProperty.DESCRIPTION.name);

            requestBody.setStartFrom(startFrom);
            requestBody.setPageSize(2);

            List<String> page = searchGUIDs(requestBody);

            assertTrue(page.size() <= 2, "pageSize 2 returned " + page.size() + " elements");

            if (page.isEmpty())
            {
                break;
            }

            pageSizes.add(page.size());
            paged.addAll(page);
        }

        assertEquals(List.of(2, 2, 1), pageSizes, "five elements in pages of two");
        assertEquals(paged.size(), new HashSet<>(paged).size(), "paging returned duplicates: " + paged);
        assertEquals(byDescription, paged, "paging lost or reordered elements");
    }


    /**
     * startFrom skips that many results.
     */
    @Test
    @Order(11)
    void startFrom()
    {
        SearchStringRequestBody requestBody = ordered(SequencingOrder.PROPERTY_ASCENDING, OpenMetadataProperty.DESCRIPTION.name);

        requestBody.setStartFrom(3);

        assertEquals(byDescription.subList(3, 5), searchGUIDs(requestBody));

        requestBody.setStartFrom(5);

        assertTrue(searchGUIDs(requestBody).isEmpty(), "startFrom beyond the results should return nothing");
    }


    /**
     * pageSize 0 means "use the server's maximum page size", so every fixture element comes back; a positive
     * pageSize limits the results.
     */
    @Test
    @Order(12)
    void pageSize()
    {
        SearchStringRequestBody requestBody = ordered(SequencingOrder.PROPERTY_ASCENDING, OpenMetadataProperty.DESCRIPTION.name);

        requestBody.setPageSize(0);

        assertEquals(byDescription, searchGUIDs(requestBody), "pageSize 0 should return all the results");

        requestBody.setPageSize(3);

        assertEquals(byDescription.subList(0, 3), searchGUIDs(requestBody));

        requestBody.setPageSize(1);

        assertEquals(byDescription.subList(0, 1), searchGUIDs(requestBody));
    }


    /*
     * ===================================== QueryOptions: sequencing =====================================
     */


    /**
     * Every SequencingOrder value orders the results as documented.
     */
    @Test
    @Order(20)
    void sequencingOrder()
    {
        assertEquals(creationOrder, searchGUIDs(ordered(SequencingOrder.CREATION_DATE_OLDEST, null)), "CREATION_DATE_OLDEST");
        assertEquals(reversed(creationOrder), searchGUIDs(ordered(SequencingOrder.CREATION_DATE_RECENT, null)), "CREATION_DATE_RECENT");
        assertEquals(updateOrder, searchGUIDs(ordered(SequencingOrder.LAST_UPDATE_OLDEST, null)), "LAST_UPDATE_OLDEST");
        assertEquals(reversed(updateOrder), searchGUIDs(ordered(SequencingOrder.LAST_UPDATE_RECENT, null)), "LAST_UPDATE_RECENT");
        assertEquals(byDescription, searchGUIDs(ordered(SequencingOrder.PROPERTY_ASCENDING, OpenMetadataProperty.DESCRIPTION.name)), "PROPERTY_ASCENDING");
        assertEquals(reversed(byDescription), searchGUIDs(ordered(SequencingOrder.PROPERTY_DESCENDING, OpenMetadataProperty.DESCRIPTION.name)), "PROPERTY_DESCENDING");

        List<String> byGUID = new ArrayList<>(fixture);

        Collections.sort(byGUID);

        assertEquals(byGUID, searchGUIDs(ordered(SequencingOrder.GUID, null)), "GUID");

        List<String> anyOrder = searchGUIDs(ordered(SequencingOrder.ANY, null));

        assertEquals(fixture.size(), anyOrder.size(), "ANY returned the wrong number of elements: " + anyOrder);
        assertEquals(Set.copyOf(fixture), Set.copyOf(anyOrder), "ANY");
    }


    /**
     * The sequencingProperty names the property that PROPERTY_ASCENDING/DESCENDING sort on.
     */
    @Test
    @Order(21)
    void sequencingProperty()
    {
        assertNotEquals(byDescription, byDisplayName, "the fixture should sort differently by the two properties");

        assertEquals(byDisplayName, searchGUIDs(ordered(SequencingOrder.PROPERTY_ASCENDING, OpenMetadataProperty.DISPLAY_NAME.name)),
                     "PROPERTY_ASCENDING on displayName");
        assertEquals(reversed(byDisplayName), searchGUIDs(ordered(SequencingOrder.PROPERTY_DESCENDING, OpenMetadataProperty.DISPLAY_NAME.name)),
                     "PROPERTY_DESCENDING on displayName");
    }


    /*
     * ===================================== QueryOptions: types =====================================
     */


    /**
     * metadataElementTypeName restricts the results to the type and its subtypes.
     */
    @Test
    @Order(30)
    void metadataElementTypeName()
    {
        SearchStringRequestBody requestBody = searchFor(MIDDLE);

        requestBody.setMetadataElementTypeName(DATA_FILE);

        assertEquals(Set.copyOf(fixture), Set.copyOf(searchGUIDs(requestBody)), "DataFile should include its CSVFile subtype");

        requestBody.setMetadataElementTypeName(CSV_FILE);

        assertEquals(List.of(delta), searchGUIDs(requestBody), "CSVFile should return only the CSV file");

        requestBody.setMetadataElementTypeName(LOCATION);

        assertTrue(searchGUIDs(requestBody).isEmpty(), "a type none of the elements has should return nothing");
    }


    /**
     * metadataElementSubtypeNames lists the only subtypes to return; with skipSubtypes it lists the subtypes to
     * leave out.
     */
    @Test
    @Order(31)
    void metadataElementSubtypeNamesAndSkipSubtypes()
    {
        SearchStringRequestBody requestBody = searchFor(MIDDLE);

        requestBody.setMetadataElementTypeName(DATA_FILE);
        requestBody.setMetadataElementSubtypeNames(List.of(CSV_FILE));

        assertEquals(List.of(delta), searchGUIDs(requestBody), "subtype names should restrict the results to those subtypes");

        requestBody.setSkipSubtypes(true);

        assertEquals(Set.of(alpha, beta, gamma, regex), Set.copyOf(searchGUIDs(requestBody)),
                     "skipSubtypes should return everything except the listed subtypes");
    }


    /*
     * ===================================== QueryOptions: classifications =====================================
     */


    /**
     * includeOnlyClassifiedElements returns only elements with the classification; skipClassifiedElements
     * returns only elements without it.
     */
    @Test
    @Order(40)
    void classificationFilters()
    {
        classify(beta, INCOMPLETE, null);
        classify(gamma, INCOMPLETE, null);

        SearchStringRequestBody requestBody = searchFor(MIDDLE);

        requestBody.setIncludeOnlyClassifiedElements(List.of(INCOMPLETE));

        assertEquals(Set.of(beta, gamma), Set.copyOf(only(searchGUIDs(requestBody), fixture)), "includeOnlyClassifiedElements");

        requestBody.setIncludeOnlyClassifiedElements(null);
        requestBody.setSkipClassifiedElements(List.of(INCOMPLETE));

        assertEquals(Set.of(alpha, delta, regex), Set.copyOf(only(searchGUIDs(requestBody), fixture)), "skipClassifiedElements");
    }


    /**
     * The governance zone filter returns only elements in the listed zones.  It is applied by the view services'
     * root-element handler, so it is tested through Asset Maker's asset search.
     */
    @Test
    @Order(41)
    void governanceZoneFilter()
    {
        classify(alpha, OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                 propertyHelper.addStringArrayProperty(null, OpenMetadataProperty.ZONE_MEMBERSHIP.name, List.of(ZONE_A)));
        classify(delta, OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                 propertyHelper.addStringArrayProperty(null, OpenMetadataProperty.ZONE_MEMBERSHIP.name, List.of(ZONE_B)));

        SearchStringRequestBody requestBody = searchFor(MIDDLE);

        List<OpenMetadataRootElement> unfiltered = assetMaker.elements("/assets/by-search-string", requestBody);

        assertTrue(RelatedElementChecks.contains(unfiltered, alpha) && RelatedElementChecks.contains(unfiltered, delta),
                   "without a zone filter both zoned assets should be found");

        requestBody.setGovernanceZoneFilter(List.of(ZONE_A));

        List<OpenMetadataRootElement> filtered = assetMaker.elements("/assets/by-search-string", requestBody);

        assertTrue(RelatedElementChecks.contains(filtered, alpha), "the asset in " + ZONE_A + " was filtered out");
        assertFalse(RelatedElementChecks.contains(filtered, delta), "the asset in " + ZONE_B + " was returned by a filter for " + ZONE_A);
    }


    /*
     * ===================================== QueryOptions: status =====================================
     */


    /**
     * By default DELETED elements are not returned; limitResultsByStatus selects the statuses to return.
     */
    @Test
    @Order(50)
    void limitResultsByStatus()
    {
        OpenMetadataDeleteRequestBody deleteRequestBody = new OpenMetadataDeleteRequestBody();

        deleteRequestBody.setDeleteMethod(DeleteMethod.SOFT_DELETE);

        metadataExpert.call("/metadata-elements/" + regex + "/delete", deleteRequestBody);

        List<String> active = List.of(alpha, beta, gamma, delta);

        assertEquals(Set.copyOf(active), Set.copyOf(searchGUIDs(searchFor(MIDDLE))), "the default should leave out the deleted element");

        assertEquals(Set.copyOf(active), Set.copyOf(searchGUIDs(withStatus(ElementStatus.ACTIVE))), "ACTIVE");
        assertEquals(List.of(regex), searchGUIDs(withStatus(ElementStatus.DELETED)), "DELETED");
        assertEquals(Set.copyOf(fixture), Set.copyOf(searchGUIDs(withStatus(ElementStatus.ACTIVE, ElementStatus.DELETED))), "ACTIVE + DELETED");

        for (ElementStatus elementStatus : List.of(ElementStatus.INVALID, ElementStatus.OTHER, ElementStatus.UNKNOWN))
        {
            assertTrue(searchGUIDs(withStatus(elementStatus)).isEmpty(), "no fixture element has status " + elementStatus);
        }

        OpenMetadataElement deleted = search(withStatus(ElementStatus.DELETED)).get(0);

        assertEquals(ElementStatus.DELETED, deleted.getStatus());
    }


    /*
     * ===================================== QueryOptions: anchors =====================================
     */


    /**
     * anchorGUID restricts a search to the elements anchored to that element.
     */
    @Test
    @Order(60)
    void anchorGUID()
    {
        AnchorFixture anchors = anchorFixture();

        SearchStringRequestBody requestBody = searchFor(anchors.prefix);

        List<String> unrestricted = searchGUIDs(requestBody);

        assertTrue(unrestricted.containsAll(List.of(anchors.room, anchors.other, anchors.building, anchors.file)), "unrestricted: " + unrestricted);

        requestBody.setAnchorGUID(anchors.campus);

        List<String> restricted = searchGUIDs(requestBody);

        assertTrue(restricted.contains(anchors.room), "anchorGUID lost the anchored room: " + restricted);
        assertFalse(restricted.contains(anchors.other), "anchorGUID returned an element with another anchor");
        assertFalse(restricted.contains(anchors.building), "anchorGUID returned an element with another anchor");
        assertFalse(restricted.contains(anchors.file), "anchorGUID returned an element with another anchor");
    }


    /**
     * anchorDomainName restricts a search to the elements whose anchor is in that domain.
     */
    @Test
    @Order(61)
    void anchorDomainName()
    {
        AnchorFixture anchors = anchorFixture();

        String locationDomain = anchorProperty(anchors.room, OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name);
        String fileDomain     = anchorProperty(anchors.file, OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name);

        assertNotNull(locationDomain);
        assertNotEquals(locationDomain, fileDomain, "the fixture needs anchors in two domains");

        SearchStringRequestBody requestBody = searchFor(anchors.prefix);

        requestBody.setAnchorDomainName(locationDomain);

        List<String> restricted = searchGUIDs(requestBody);

        assertTrue(restricted.containsAll(List.of(anchors.room, anchors.campus, anchors.other)), "anchorDomainName lost elements: " + restricted);
        assertFalse(restricted.contains(anchors.file), "anchorDomainName " + locationDomain + " returned an element in domain " + fileDomain);

        requestBody.setAnchorDomainName(fileDomain);

        assertEquals(List.of(anchors.file), searchGUIDs(requestBody), "anchorDomainName " + fileDomain);
    }


    /**
     * anchorScopeGUID restricts a search to the elements whose anchor scope includes that element.
     */
    @Test
    @Order(62)
    void anchorScopeGUID()
    {
        AnchorFixture anchors = anchorFixture();

        SearchStringRequestBody requestBody = searchFor(anchors.prefix);

        requestBody.setAnchorScopeGUID(anchors.building);

        assertEquals(List.of(anchors.room), searchGUIDs(requestBody));
    }


    /*
     * ===================================== Helpers =====================================
     */


    private record AnchorFixture(String prefix, String campus, String building, String room, String other, String file) {}

    private static AnchorFixture anchors = null;


    /**
     * A campus and a building, a room anchored to the campus with the building as its anchor scope, another
     * location that is its own anchor, and a data file (an anchor in another domain).
     */
    private static AnchorFixture anchorFixture()
    {
        if (anchors == null)
        {
            String anchorPrefix = prefix("anchor");

            String campus   = createElement(LOCATION, anchorPrefix + "campus", "campus", null);
            String building = createElement(LOCATION, anchorPrefix + "building", "building", null);
            String other    = createElement(LOCATION, anchorPrefix + "other", "other", null);
            String file     = createElement(DATA_FILE, anchorPrefix + "file", "file", null);

            NewOpenMetadataElementRequestBody requestBody = new NewOpenMetadataElementRequestBody();

            requestBody.setTypeName(LOCATION);
            requestBody.setIsOwnAnchor(false);
            requestBody.setAnchorGUID(campus);
            requestBody.setAnchorScopeGUIDs(List.of(building));
            requestBody.setParentGUID(campus);
            requestBody.setParentRelationshipTypeName(OpenMetadataType.NESTED_LOCATION_RELATIONSHIP.typeName);
            requestBody.setParentAtEnd1(true);
            requestBody.setProperties(new NewElementProperties(propertyHelper.addStringProperty(null, QUALIFIED_NAME, anchorPrefix + "room")));

            String room = metadataExpert.create("/metadata-elements", requestBody);

            anchorFixture.addAll(List.of(room, campus, building, other, file));

            anchors = new AnchorFixture(anchorPrefix, campus, building, room, other, file);
        }

        return anchors;
    }


    private static String anchorProperty(String guid,
                                         String propertyName)
    {
        OpenMetadataElement element = getElement(guid, new GetRequestBody());

        assertNotNull(element);

        for (AttachedClassification classification : element.getClassifications())
        {
            if (OpenMetadataType.ANCHORS_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
            {
                return propertyHelper.getStringProperty("OptionsReadSearchFVT", propertyName, classification.getClassificationProperties(), "anchorProperty");
            }
        }

        return null;
    }


    private static void updateDescription(String guid,
                                          String description)
    {
        UpdatePropertiesRequestBody requestBody = new UpdatePropertiesRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(propertyHelper.addStringProperty(null, DESCRIPTION, description));

        metadataExpert.post("/metadata-elements/" + guid + "/update-properties", requestBody, org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse.class);
    }


    private static SearchStringRequestBody ordered(SequencingOrder sequencingOrder,
                                                   String          sequencingProperty)
    {
        SearchStringRequestBody requestBody = searchFor(MIDDLE);

        requestBody.setSequencingOrder(sequencingOrder);
        requestBody.setSequencingProperty(sequencingProperty);

        return requestBody;
    }


    private static SearchStringRequestBody withStatus(ElementStatus... statuses)
    {
        SearchStringRequestBody requestBody = searchFor(MIDDLE);

        requestBody.setLimitResultsByStatus(List.of(statuses));

        return requestBody;
    }


    private static List<String> reversed(List<String> list)
    {
        List<String> results = new ArrayList<>(list);

        Collections.reverse(results);

        return results;
    }
}
