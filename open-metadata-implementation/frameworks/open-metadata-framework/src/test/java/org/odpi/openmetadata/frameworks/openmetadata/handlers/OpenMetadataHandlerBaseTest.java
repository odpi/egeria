/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.mockito.ArgumentCaptor;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataHierarchySummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.MatchCriteria;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyCondition;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertThrows;

/**
 * OpenMetadataHandlerBaseTest covers the request building that all the open metadata handlers share.
 * <br><br>
 * Every typed handler - and there are over fifty of them - inherits its retrieval methods from this class.
 * What those methods do is translate a caller's request into a query for the metadata store: which type to
 * limit the search to, which properties to match a name against, and how exactly to match it.  Nothing has
 * tested any of that, and the only coverage it has is through FVT suites that need a platform, a database
 * and several minutes.
 * <br><br>
 * It is worth testing directly because the failure mode is an empty answer rather than an error.  A search
 * built as a fuzzy match where an exact one was meant returns too much; built the other way round it returns
 * nothing; and a type filter that overwrites the caller's own silently widens the search.  In each case the
 * repository does as it is asked, the call succeeds, and the result is wrong.
 * <br><br>
 * The metadata store is a mock here, so these tests assert on <b>what the handler asked for</b> rather than
 * on what came back.  That is the part this class is responsible for.
 */
public class OpenMetadataHandlerBaseTest
{
    private static final String LOCAL_SERVER  = "testServer";
    private static final String LOCAL_SERVICE = "testService";
    private static final String USER_ID       = "testUser";
    private static final String METHOD_NAME   = "testMethod";

    private OpenMetadataClient      openMetadataClient;
    private OpenMetadataHandlerBase handler;


    /**
     * A handler over a mocked metadata store, typed as Collection so that the default type filter has a
     * value to be checked against.
     *
     * @throws Exception not expected
     */
    @BeforeMethod
    public void setUp() throws Exception
    {
        openMetadataClient = mock(OpenMetadataClient.class);

        when(openMetadataClient.getMaxPagingSize()).thenReturn(500);
        when(openMetadataClient.findMetadataElements(anyString(), any(), any(), any())).thenReturn(null);
        when(openMetadataClient.findMetadataElementsWithString(anyString(), any(), any())).thenReturn(null);

        handler = new OpenMetadataHandlerBase(LOCAL_SERVER,
                                              null,
                                              LOCAL_SERVICE,
                                              openMetadataClient,
                                              OpenMetadataType.COLLECTION.typeName);
    }


    /**
     * A retrieval by name asks for an <b>exact</b> match, on each property name offered, with any one of
     * them matching being enough.
     * <br><br>
     * This is the assertion that matters most in this class.  "Find the collection called Clinical Trials"
     * and "find the collections whose name contains Clinical Trials" are different questions, and the
     * difference is one flag on the search options.  Asked the wrong way the call still succeeds - it just
     * answers a question nobody asked.
     *
     * @throws Exception not expected
     */
    @Test
    public void aRetrievalByNameAsksForAnExactMatch() throws Exception
    {
        handler.getRootElementsByName(USER_ID,
                                      "Clinical Trials",
                                      List.of("qualifiedName", "displayName"),
                                      new QueryOptions(),
                                      METHOD_NAME);

        ArgumentCaptor<SearchProperties> searchProperties = ArgumentCaptor.forClass(SearchProperties.class);

        /*
         * Bare any() rather than any(Type.class): the classification search is null when the caller asked
         * for no classification filter, and Mockito's typed matcher does not match null.
         */
        verify(openMetadataClient).findMetadataElements(eq(USER_ID),
                                                        searchProperties.capture(),
                                                        any(),
                                                        any());

        SearchProperties captured = searchProperties.getValue();

        assertNotNull(captured, "the handler asked for no search properties, so the name was not used at all");
        assertEquals(captured.getMatchCriteria(),
                     MatchCriteria.ANY,
                     "a name may be held in any of the property names offered, so any one of them matching" +
                             " has to be enough");
        assertEquals(captured.getConditions().size(),
                     2,
                     "both property names offered should be searched");

        for (PropertyCondition condition : captured.getConditions())
        {
            assertEquals(condition.getOperator(),
                         PropertyComparisonOperator.EQ,
                         "a retrieval by name has to be an exact, case sensitive match - anything looser" +
                                 " answers a different question, and anything tighter finds nothing");
        }
    }


    /**
     * The handler's own type is used to limit the search when the caller did not ask for a type.
     *
     * @throws Exception not expected
     */
    @Test
    public void theHandlersTypeLimitsTheSearchByDefault() throws Exception
    {
        handler.getRootElementsByName(USER_ID,
                                      "Clinical Trials",
                                      List.of("qualifiedName"),
                                      new QueryOptions(),
                                      METHOD_NAME);

        assertEquals(capturedQueryOptions().getMetadataElementTypeName(),
                     OpenMetadataType.COLLECTION.typeName,
                     "a handler that did not limit the search to its own type would answer with elements of" +
                             " every other type as well");
    }


    /**
     * A type the caller asked for is left alone.
     * <br><br>
     * The default above must not become an override: a caller narrowing the search to a subtype - asking a
     * collection handler for root collections only - has to get the subtype it asked for, not every
     * collection there is.
     *
     * @throws Exception not expected
     */
    @Test
    public void aTypeTheCallerAskedForIsNotOverwritten() throws Exception
    {
        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setMetadataElementTypeName(OpenMetadataType.DIGITAL_PRODUCT.typeName);

        handler.getRootElementsByName(USER_ID,
                                      "Clinical Trials",
                                      List.of("qualifiedName"),
                                      queryOptions,
                                      METHOD_NAME);

        assertEquals(capturedQueryOptions().getMetadataElementTypeName(),
                     OpenMetadataType.DIGITAL_PRODUCT.typeName,
                     "the caller narrowed the search to a subtype and the handler widened it again");
    }


    /**
     * The caller's own options are not modified, only copied.
     * <br><br>
     * Callers reuse a QueryOptions across several calls.  A handler that set the type name on the object it
     * was handed would leave the first call's type filter on every call after it.
     *
     * @throws Exception not expected
     */
    @Test
    public void theCallersOptionsAreLeftAsTheyWere() throws Exception
    {
        QueryOptions suppliedOptions = new QueryOptions();

        handler.getRootElementsByName(USER_ID,
                                      "Clinical Trials",
                                      List.of("qualifiedName"),
                                      suppliedOptions,
                                      METHOD_NAME);

        assertEquals(suppliedOptions.getMetadataElementTypeName(),
                     null,
                     "the handler set a type name on the caller's own options, so the next call made with" +
                             " them would be silently filtered by this call's type");
    }


    /**
     * A request that cannot succeed is refused before the metadata store is troubled with it.
     * <br><br>
     * Not only politeness: a missing name reaching the repository as a search for nothing comes back as an
     * empty list, which the caller cannot tell from "there is nothing called that".
     */
    @Test
    public void arequestThatCannotSucceedNeverReachesTheStore()
    {
        assertThrows(InvalidParameterException.class,
                     () -> handler.getRootElementsByName(USER_ID, null, List.of("qualifiedName"),
                                                         new QueryOptions(), METHOD_NAME));
        assertThrows(InvalidParameterException.class,
                     () -> handler.getRootElementsByName(USER_ID, "", List.of("qualifiedName"),
                                                         new QueryOptions(), METHOD_NAME));
        assertThrows(InvalidParameterException.class,
                     () -> handler.getRootElementsByName(null, "Clinical Trials", List.of("qualifiedName"),
                                                         new QueryOptions(), METHOD_NAME));

        verifyNoQueryWasMade();
    }


    /**
     * A page size larger than the store allows is refused rather than sent and trimmed.
     */
    @Test
    public void aPageSizeTheStoreCannotHonourIsRefused()
    {
        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setPageSize(501);

        assertThrows(InvalidParameterException.class,
                     () -> handler.getRootElementsByName(USER_ID, "Clinical Trials", List.of("qualifiedName"),
                                                         queryOptions, METHOD_NAME));

        verifyNoQueryWasMade();
    }


    /**
     * A search by string goes to the store's search call, limited to the handler's type, with the caller's
     * string passed through as given.
     * <br><br>
     * The search string reaches the repository as a regular expression that has to match the whole property
     * value, so what the caller supplies matters a great deal - and this asserts that the handler neither
     * decorates it nor drops it.  A handler that quietly wrapped or escaped it would change the meaning of
     * every caller's search.
     *
     * @throws Exception not expected
     */
    @Test
    public void aSearchByStringIsPassedThroughAsGiven() throws Exception
    {
        handler.findRootElements(USER_ID, ".*Clinical.*", new SearchOptions(), METHOD_NAME);

        ArgumentCaptor<String>        searchString  = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<SearchOptions> searchOptions = ArgumentCaptor.forClass(SearchOptions.class);

        verify(openMetadataClient).findMetadataElementsWithString(eq(USER_ID),
                                                                  searchString.capture(),
                                                                  searchOptions.capture());

        assertEquals(searchString.getValue(),
                     ".*Clinical.*",
                     "the handler changed the caller's search string, which changes what the search means");
        assertEquals(searchOptions.getValue().getMetadataElementTypeName(),
                     OpenMetadataType.COLLECTION.typeName,
                     "a search by string should still be limited to the handler's own type");
    }


    /**
     * The query options the handler actually sent.
     *
     * @return captured options
     * @throws Exception not expected
     */
    private QueryOptions capturedQueryOptions() throws Exception
    {
        ArgumentCaptor<QueryOptions> queryOptions = ArgumentCaptor.forClass(QueryOptions.class);

        verify(openMetadataClient).findMetadataElements(anyString(),
                                                        any(),
                                                        any(),
                                                        queryOptions.capture());

        return queryOptions.getValue();
    }


    /**
     * Assert that neither of the store's search calls was made.
     */
    private void verifyNoQueryWasMade()
    {
        try
        {
            verify(openMetadataClient, never()).findMetadataElements(anyString(), any(), any(), any());
            verify(openMetadataClient, never()).findMetadataElementsWithString(anyString(), any(), any());
        }
        catch (Exception error)
        {
            throw new AssertionError("unable to verify the metadata store calls", error);
        }
    }


    /**
     * Relationships that have already been retrieved survive a filter that excludes the relationship used to
     * expand them.
     * <br><br>
     * A Person's performsRoles come from PersonRoleAppointment, and each role is then expanded through
     * AssignmentScope.  Asking for only PersonRoleAppointment used to drop the roles altogether because the
     * filter was applied to the expansion relationship as if it were the relationship that found them.
     *
     * @throws Exception not expected
     */
    @Test
    public void aFilteredOutExpansionLeavesTheRetrievedElementsInPlace() throws Exception
    {
        ElementHeader relationshipHeader = new ElementHeader();
        relationshipHeader.setGUID("appointment-guid");

        RelatedMetadataElementSummary role = new RelatedMetadataElementSummary();
        role.setRelationshipHeader(relationshipHeader);

        List<RelatedMetadataElementSummary> performsRoles = List.of(role);

        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setGraphQueryDepth(3);
        queryOptions.setIncludeOnlyRelationships(List.of(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName));

        List<RelatedMetadataElementSummary> results = handler.getElementHierarchies(USER_ID,
                                                                                    performsRoles,
                                                                                    1,
                                                                                    OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                                    null,
                                                                                    queryOptions,
                                                                                    1);

        assertSame(results, performsRoles, "the role appointments were dropped by the relationship filter");
        verify(openMetadataClient, never()).getRelatedMetadataElements(anyString(), anyString(), anyInt(), any(), any());
    }


    /**
     * The members of a solution blueprint are expanded down both the solution composition and the collection
     * membership relationships, so that the members of a solution boundary nested in the blueprint are returned
     * as its nested elements, and are expanded in turn to pick up their solution linking wires.
     * <br><br>
     * A collection membership that leads back up to a parent collection is not part of the hierarchy below the
     * member.  It is returned as a side link because CollectionMembership is also listed as a side relationship.
     *
     * @throws Exception not expected
     */
    @Test
    public void membersOfANestedCollectionAreNestedElementsAndParentCollectionsAreSideLinks() throws Exception
    {
        final String blueprintGUID = "blueprint-guid";
        final String boundaryGUID  = "boundary-guid";
        final String insideGUID    = "inside-component-guid";
        final String outsideGUID   = "outside-component-guid";

        /*
         * The boundary is a member of the blueprint and has one member of its own.
         */
        when(openMetadataClient.getRelatedMetadataElements(anyString(), eq(boundaryGUID), anyInt(), any(), any()))
                .thenReturn(related(relationship("blueprint-to-boundary", OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, blueprintGUID, OpenMetadataType.SOLUTION_BLUEPRINT.typeName, true),
                                    relationship("boundary-to-inside", OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, insideGUID, OpenMetadataType.SOLUTION_COMPONENT.typeName, false)));

        /*
         * The component inside the boundary is wired to the component outside it.
         */
        when(openMetadataClient.getRelatedMetadataElements(anyString(), eq(insideGUID), anyInt(), any(), any()))
                .thenReturn(related(relationship("boundary-to-inside", OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, boundaryGUID, OpenMetadataType.SOLUTION_BOUNDARY.typeName, true),
                                    relationship("wire", OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName, outsideGUID, OpenMetadataType.SOLUTION_COMPONENT.typeName, false)));

        QueryOptions queryOptions = new QueryOptions();

        List<RelatedMetadataElementSummary> results = handler.getMixedElementHierarchies(USER_ID,
                                                                                         List.of(member("blueprint-to-boundary", boundaryGUID)),
                                                                                         1,
                                                                                         List.of(OpenMetadataType.SOLUTION_COMPOSITION_RELATIONSHIP.typeName,
                                                                                                 OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName),
                                                                                         List.of(OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName,
                                                                                                 OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName),
                                                                                         queryOptions,
                                                                                         1);

        assertEquals(results.size(), 1);

        RelatedMetadataHierarchySummary boundary = (RelatedMetadataHierarchySummary) results.get(0);

        assertNotNull(boundary.getNestedElements(), "the boundary's member was not followed");
        assertEquals(boundary.getNestedElements().size(), 1);
        assertEquals(boundary.getSideLinks().size(), 1, "the membership of the parent blueprint is a side link, as it is for a component");
        assertEquals(boundary.getSideLinks().get(0).getRelatedElement().getElementHeader().getGUID(), blueprintGUID);

        RelatedMetadataHierarchySummary inside = (RelatedMetadataHierarchySummary) boundary.getNestedElements().get(0);

        assertEquals(inside.getRelatedElement().getElementHeader().getGUID(), insideGUID);
        assertNull(inside.getNestedElements());
        assertNotNull(inside.getSideLinks(), "the wires of a component inside a boundary are needed to draw the blueprint");
        assertEquals(inside.getSideLinks().size(), 2, "the membership of the boundary and the wire");
        assertEquals(inside.getSideLinks().get(0).getRelatedElement().getElementHeader().getGUID(), boundaryGUID);
        assertEquals(inside.getSideLinks().get(1).getRelationshipHeader().getGUID(), "wire");
        assertFalse(inside.getSideLinks().get(1).getRelatedElementAtEnd1());
    }


    /**
     * A member of a solution blueprint that is also a member of another collection keeps that membership as a side link.
     *
     * @throws Exception not expected
     */
    @Test
    public void aMembershipOfAnotherCollectionIsASideLink() throws Exception
    {
        final String componentGUID = "component-guid";

        when(openMetadataClient.getRelatedMetadataElements(anyString(), eq(componentGUID), anyInt(), any(), any()))
                .thenReturn(related(relationship("blueprint-to-component", OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, "blueprint-guid", OpenMetadataType.SOLUTION_BLUEPRINT.typeName, true),
                                    relationship("other-to-component", OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, "other-blueprint-guid", OpenMetadataType.SOLUTION_BLUEPRINT.typeName, true),
                                    relationship("parent-to-component", OpenMetadataType.SOLUTION_COMPOSITION_RELATIONSHIP.typeName, "parent-component-guid", OpenMetadataType.SOLUTION_COMPONENT.typeName, true)));

        List<RelatedMetadataElementSummary> results = handler.getMixedElementHierarchies(USER_ID,
                                                                                         List.of(member("blueprint-to-component", componentGUID)),
                                                                                         1,
                                                                                         List.of(OpenMetadataType.SOLUTION_COMPOSITION_RELATIONSHIP.typeName,
                                                                                                 OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName),
                                                                                         List.of(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName),
                                                                                         new QueryOptions(),
                                                                                         1);

        RelatedMetadataHierarchySummary component = (RelatedMetadataHierarchySummary) results.get(0);

        assertNull(component.getNestedElements(), "nothing is below the component");
        assertEquals(component.getSideLinks().size(), 2, "both collection memberships are side links; the parent component is not");
    }


    private RelatedMetadataElementSummary member(String relationshipGUID,
                                                 String elementGUID)
    {
        ElementHeader relationshipHeader = new ElementHeader();
        relationshipHeader.setGUID(relationshipGUID);

        ElementHeader elementHeader = new ElementHeader();
        elementHeader.setGUID(elementGUID);

        MetadataElementSummary element = new MetadataElementSummary();
        element.setElementHeader(elementHeader);

        RelatedMetadataElementSummary member = new RelatedMetadataElementSummary();
        member.setRelationshipHeader(relationshipHeader);
        member.setRelatedElement(element);

        return member;
    }


    private RelatedMetadataElement relationship(String  relationshipGUID,
                                                String  relationshipTypeName,
                                                String  elementGUID,
                                                String  elementTypeName,
                                                boolean elementAtEnd1)
    {
        OpenMetadataElement element = new OpenMetadataElement();
        element.setElementGUID(elementGUID);
        element.setType(type(elementTypeName));

        RelatedMetadataElement relatedMetadataElement = new RelatedMetadataElement();
        relatedMetadataElement.setRelationshipGUID(relationshipGUID);
        relatedMetadataElement.setType(type(relationshipTypeName));
        relatedMetadataElement.setElement(element);
        relatedMetadataElement.setElementAtEnd1(elementAtEnd1);

        return relatedMetadataElement;
    }


    private ElementType type(String typeName)
    {
        ElementType elementType = new ElementType();
        elementType.setTypeName(typeName);

        return elementType;
    }


    private RelatedMetadataElementList related(RelatedMetadataElement... relatedMetadataElements)
    {
        RelatedMetadataElementList relatedMetadataElementList = new RelatedMetadataElementList();
        relatedMetadataElementList.setElementList(List.of(relatedMetadataElements));

        return relatedMetadataElementList;
    }
}
