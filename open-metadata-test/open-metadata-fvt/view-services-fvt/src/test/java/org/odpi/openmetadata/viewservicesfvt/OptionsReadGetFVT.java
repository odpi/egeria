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
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataHierarchySummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonRoleAppointmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.rest.HistoryRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataElementsResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.UpdatePropertiesRequestBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.OptionsReadSupport.*;

/**
 * OptionsReadGetFVT checks the options that shape what a retrieve returns (GetOptions), the effective time on
 * reads (BasicOptions.effectiveTime) and the history options (HistoricalQueryOptions).
 * <br>
 * The relationship options are checked on a Person retrieved through the Actor Manager: the person is appointed
 * to three person roles, one of which has an assignment scope (so the roles are a two-level hierarchy), and lists
 * a resource through a ResourceList relationship.  The view services assemble the retrieved element from these
 * relationships, so each option's effect shows in which of them come back.  The time-based options are checked
 * on the Metadata Expert's generic element requests.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OptionsReadGetFVT
{
    private static final String LOCATION           = OpenMetadataType.LOCATION.typeName;
    private static final String DATA_FILE          = OpenMetadataType.DATA_FILE.typeName;
    private static final String RESOURCE_LIST      = OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName;
    private static final String ROLE_APPOINTMENT   = OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName;
    private static final String ASSIGNMENT_SCOPE   = OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName;

    private static final String PREFIX = prefix("get");

    private static final List<String> ROLE_NAMES = List.of("role-1", "role-2", "role-3");

    private static String       personGUID   = null;
    private static List<String> roleGUIDs    = null;
    private static String       scopedRole   = null;
    private static String       scopeGUID    = null;
    private static String       resourceGUID = null;

    private static final List<String> created = new ArrayList<>();


    /**
     * Create the person, its three roles (one with an assignment scope) and its resource.
     */
    @BeforeAll
    static void createPersonWithRoles()
    {
        PersonProperties personProperties = new PersonProperties();

        personProperties.setQualifiedName(PREFIX + "person");
        personProperties.setDisplayName("Options person");

        personGUID = actorManager.create("/actor-profiles", AssetMakerFvtSupport.newElement(personProperties));
        created.add(personGUID);

        roleGUIDs = new ArrayList<>();

        for (String roleName : ROLE_NAMES)
        {
            PersonRoleProperties roleProperties = new PersonRoleProperties();

            roleProperties.setQualifiedName(PREFIX + roleName);
            roleProperties.setDisplayName(roleName);
            roleProperties.setScope("view-services-fvt");

            String roleGUID = actorManager.create("/actor-roles", AssetMakerFvtSupport.newElement(roleProperties));

            created.add(roleGUID);
            roleGUIDs.add(roleGUID);

            NewRelationshipRequestBody appointment = new NewRelationshipRequestBody();

            appointment.setProperties(new PersonRoleAppointmentProperties());

            actorManager.call("/actor-roles/" + roleGUID + "/person-role-appointments/" + personGUID + "/attach", appointment);
        }

        scopedRole = roleGUIDs.get(0);
        scopeGUID  = createElement(LOCATION, PREFIX + "scope", "scope", null);
        created.add(scopeGUID);

        relate(ASSIGNMENT_SCOPE, scopedRole, scopeGUID, null, null);

        resourceGUID = createElement(LOCATION, PREFIX + "resource", "resource", null);
        created.add(resourceGUID);

        relate(RESOURCE_LIST, personGUID, resourceGUID, null, null);
    }


    /**
     * Remove everything the tests created.
     */
    @AfterAll
    static void removeFixture()
    {
        List<String> all = new ArrayList<>(created);

        Collections.reverse(all);

        purgeAll(all);
    }


    /*
     * ===================================== Relationship options =====================================
     */


    /**
     * With the default options the person comes back with all three roles, the resource and the roles' scope.
     */
    @Test
    @Order(1)
    void defaultsReturnEveryRelationship()
    {
        OpenMetadataRootElement person = person(new GetRequestBody());

        assertEquals(Set.copyOf(roleGUIDs), performsRoles(person), "performsRoles");
        assertTrue(RelatedElementChecks.isLinked(person, RESOURCE_LIST, resourceGUID), "the resource list is missing");
        assertEquals(Set.of(scopeGUID), nestedGUIDs(person, scopedRole), "the role's assignment scope is missing");
    }


    /**
     * skipRelationships drops the named relationship type and keeps the others.
     */
    @Test
    @Order(2)
    void skipRelationships()
    {
        GetRequestBody requestBody = new GetRequestBody();

        requestBody.setSkipRelationships(List.of(RESOURCE_LIST));

        OpenMetadataRootElement person = person(requestBody);

        assertFalse(RelatedElementChecks.isLinked(person, RESOURCE_LIST, resourceGUID), "skipRelationships returned the skipped relationship");
        assertEquals(Set.copyOf(roleGUIDs), performsRoles(person), "skipRelationships lost relationships it was not asked to skip");

        requestBody.setSkipRelationships(List.of(ROLE_APPOINTMENT));

        person = person(requestBody);

        assertTrue(performsRoles(person).isEmpty(), "skipRelationships returned the skipped role appointments");
        assertTrue(RelatedElementChecks.isLinked(person, RESOURCE_LIST, resourceGUID), "skipRelationships lost the resource list");
    }


    /**
     * includeOnlyRelationships keeps only the named relationship types.  A person retrieved with only
     * PersonRoleAppointment still has its performsRoles, and the roles are not expanded through relationships
     * that were not asked for.
     */
    @Test
    @Order(3)
    void includeOnlyRelationships()
    {
        GetRequestBody requestBody = new GetRequestBody();

        requestBody.setIncludeOnlyRelationships(List.of(ROLE_APPOINTMENT));

        OpenMetadataRootElement person = person(requestBody);

        assertEquals(Set.copyOf(roleGUIDs), performsRoles(person), "includeOnlyRelationships=[PersonRoleAppointment] lost performsRoles");
        assertFalse(RelatedElementChecks.isLinked(person, RESOURCE_LIST, resourceGUID), "includeOnlyRelationships returned a resource list");
        assertTrue(nestedGUIDs(person, scopedRole).isEmpty(), "the role was expanded through AssignmentScope, which was not included");

        requestBody.setIncludeOnlyRelationships(List.of(ROLE_APPOINTMENT, ASSIGNMENT_SCOPE));

        person = person(requestBody);

        assertEquals(Set.copyOf(roleGUIDs), performsRoles(person));
        assertEquals(Set.of(scopeGUID), nestedGUIDs(person, scopedRole), "the role's assignment scope should be included when asked for");

        requestBody.setIncludeOnlyRelationships(List.of(RESOURCE_LIST));

        person = person(requestBody);

        assertTrue(RelatedElementChecks.isLinked(person, RESOURCE_LIST, resourceGUID), "includeOnlyRelationships=[ResourceList] lost the resource list");
        assertTrue(performsRoles(person).isEmpty(), "includeOnlyRelationships=[ResourceList] returned role appointments");
    }


    /**
     * relationshipsPageSize limits the relationships of a kind returned with the element; 0 means all of them.
     */
    @Test
    @Order(4)
    void relationshipsPageSize()
    {
        GetRequestBody requestBody = new GetRequestBody();

        requestBody.setIncludeOnlyRelationships(List.of(ROLE_APPOINTMENT));
        requestBody.setRelationshipsPageSize(2);

        assertEquals(2, performsRoles(person(requestBody)).size(), "relationshipsPageSize 2 should return two of the three roles");

        requestBody.setRelationshipsPageSize(0);

        assertEquals(Set.copyOf(roleGUIDs), performsRoles(person(requestBody)), "relationshipsPageSize 0 should return every role");
    }


    /**
     * graphQueryDepth 0 retrieves no relationships, 1 retrieves the element's own relationships, and 2 or more
     * also expands the related elements (the role's assignment scope).
     */
    @Test
    @Order(5)
    void graphQueryDepth()
    {
        GetRequestBody requestBody = new GetRequestBody();

        requestBody.setGraphQueryDepth(0);

        OpenMetadataRootElement person = person(requestBody);

        assertTrue(RelatedElementChecks.relationships(person).isEmpty(),
                   "graphQueryDepth 0 returned relationships: " + RelatedElementChecks.relationships(person).size());

        requestBody.setGraphQueryDepth(1);

        person = person(requestBody);

        assertEquals(Set.copyOf(roleGUIDs), performsRoles(person), "graphQueryDepth 1 should return the person's roles");
        assertTrue(RelatedElementChecks.isLinked(person, RESOURCE_LIST, resourceGUID));
        assertTrue(nestedGUIDs(person, scopedRole).isEmpty(), "graphQueryDepth 1 expanded the role");

        for (int depth : List.of(2, 3))
        {
            requestBody.setGraphQueryDepth(depth);

            person = person(requestBody);

            assertEquals(Set.copyOf(roleGUIDs), performsRoles(person));
            assertEquals(Set.of(scopeGUID), nestedGUIDs(person, scopedRole), "graphQueryDepth " + depth + " should expand the role to its scope");
        }
    }


    /**
     * maxMermaidNodeCount 0 means no mermaid graph; otherwise the graph includes the related elements.
     */
    @Test
    @Order(6)
    void maxMermaidNodeCountZeroAndDefault()
    {
        OpenMetadataRootElement person = person(new GetRequestBody());

        assertNotNull(person.getMermaidGraph(), "the default options should produce a mermaid graph");

        for (String roleName : ROLE_NAMES)
        {
            assertTrue(person.getMermaidGraph().contains("**" + roleName + "**"), "the default mermaid graph is missing " + roleName);
        }

        GetRequestBody requestBody = new GetRequestBody();

        requestBody.setMaxMermaidNodeCount(0);

        assertNull(person(requestBody).getMermaidGraph(), "maxMermaidNodeCount 0 should produce no mermaid graph");
    }


    /**
     * maxMermaidNodeCount limits the elements linked by one kind of relationship that appear in the graph.
     */
    @Test
    @Order(7)
    void maxMermaidNodeCountLimitsNodes()
    {
        GetRequestBody requestBody = new GetRequestBody();

        requestBody.setMaxMermaidNodeCount(1);

        String mermaidGraph = person(requestBody).getMermaidGraph();

        assertNotNull(mermaidGraph);

        int rolesInGraph = 0;

        for (String roleName : ROLE_NAMES)
        {
            if (mermaidGraph.contains("**" + roleName + "**"))
            {
                rolesInGraph++;
            }
        }

        assertEquals(1, rolesInGraph, "maxMermaidNodeCount 1 should show one of the three roles:\n" + mermaidGraph);
        assertTrue(mermaidGraph.contains("plus 2 Items"), "the roles left out should be summarized in a 'more' node:\n" + mermaidGraph);
    }


    /*
     * ===================================== Element options =====================================
     */


    /**
     * metadataElementTypeName on a retrieve: the element is returned when it is of that type (or a subtype of
     * it) and not otherwise.
     */
    @Test
    @Order(10)
    void metadataElementTypeNameOnGet()
    {
        GetRequestBody requestBody = new GetRequestBody();

        requestBody.setMetadataElementTypeName(LOCATION);

        assertNotNull(getElement(resourceGUID, requestBody), "a Location should be returned when Location is asked for");

        requestBody.setMetadataElementTypeName(OpenMetadataType.REFERENCEABLE.typeName);

        assertNotNull(getElement(resourceGUID, requestBody), "a Location should be returned when its supertype is asked for");

        requestBody.setMetadataElementTypeName(DATA_FILE);

        assertNull(getElement(resourceGUID, requestBody), "a Location was returned when a DataFile was asked for");
    }


    /**
     * asOfTime returns the element as it was at that time.
     */
    @Test
    @Order(11)
    void asOfTime()
    {
        Date beforeCreation = new Date();

        tick();

        String guid = createElement(LOCATION, PREFIX + "as-of-time", "as-of-time", "version-1");

        created.add(guid);

        tick();
        Date afterFirstVersion = new Date();
        tick();

        updateDescription(guid, "version-2");

        assertEquals("version-2", stringProperty(getElement(guid, new GetRequestBody()), DESCRIPTION));

        GetRequestBody requestBody = new GetRequestBody();

        requestBody.setAsOfTime(afterFirstVersion);

        OpenMetadataElement asItWas = getElement(guid, requestBody);

        assertNotNull(asItWas, "asOfTime after creation should find the element");
        assertEquals("version-1", stringProperty(asItWas, DESCRIPTION), "asOfTime should return the version in force at that time");

        requestBody.setAsOfTime(beforeCreation);

        assertNull(getElement(guid, requestBody), "asOfTime before the element was created should not find it");
    }


    /**
     * viewElementGUID marks a request as assembling an element's view: the elements it returns are not reported
     * as found by a search (METADATA-OBSERVABILITY-0008/0009 audit log messages, which feed findCount).  The
     * in-process platform's console audit log writes to System.out, which the test captures.
     */
    @Test
    @Order(12)
    void viewElementGUID()
    {
        String viewElement = createElement(LOCATION, PREFIX + "view", "view", null);
        String asset       = createElement(DATA_FILE, PREFIX + "view-asset", "view-asset", null);

        created.add(viewElement);
        created.add(asset);

        relate(RESOURCE_LIST, viewElement, asset, null, null);

        String searchOutput = captureStandardOut(() -> assertEquals(List.of(asset), relatedGUIDs(viewElement, new ResultsRequestBody())));

        assertTrue(findReported(searchOutput, asset), "without viewElementGUID the related asset should be reported as found:\n" + searchOutput);

        ResultsRequestBody requestBody = new ResultsRequestBody();

        requestBody.setViewElementGUID(viewElement);

        String viewOutput = captureStandardOut(() -> assertEquals(List.of(asset), relatedGUIDs(viewElement, requestBody)));

        assertFalse(findReported(viewOutput, asset), "with viewElementGUID the related asset should not be reported as found:\n" + viewOutput);
    }


    /*
     * ===================================== Effective time =====================================
     */


    /**
     * An element whose effectivity starts tomorrow is not returned for an effective time of now, but is for a
     * time inside its effectivity window.
     */
    @Test
    @Order(20)
    void effectiveTimeOnElements()
    {
        long now = System.currentTimeMillis();

        String future = createElement(LOCATION, PREFIX + "effective-future", "effective-future", null,
                                      new Date(now + ONE_DAY), new Date(now + (3 * ONE_DAY)));
        String always = createElement(LOCATION, PREFIX + "effective-always", "effective-always", null);

        created.add(future);
        created.add(always);

        GetRequestBody getRequestBody = new GetRequestBody();

        getRequestBody.setEffectiveTime(new Date(now));

        assertNull(getElement(future, getRequestBody), "an element not yet in effect was returned for an effective time of now");
        assertNotNull(getElement(always, getRequestBody));

        getRequestBody.setEffectiveTime(new Date(now + (2 * ONE_DAY)));

        assertNotNull(getElement(future, getRequestBody), "the element should be returned for a time inside its effectivity window");

        getRequestBody.setEffectiveTime(new Date(now + (4 * ONE_DAY)));

        assertNull(getElement(future, getRequestBody), "an element no longer in effect was returned");

        SearchStringRequestBody searchRequestBody = searchFor(PREFIX + "effective-");

        searchRequestBody.setEffectiveTime(new Date(now));

        assertEquals(List.of(always), only(searchGUIDs(searchRequestBody), List.of(future, always)),
                     "a search for now should only find the element in effect now");

        searchRequestBody.setEffectiveTime(new Date(now + (2 * ONE_DAY)));

        assertEquals(Set.of(future, always), Set.copyOf(only(searchGUIDs(searchRequestBody), List.of(future, always))),
                     "a search inside the window should find both elements");
    }


    /**
     * A relationship whose effectivity starts tomorrow is not returned for an effective time of now, but is for a
     * time inside its effectivity window.
     */
    @Test
    @Order(21)
    void effectiveTimeOnRelationships()
    {
        long now = System.currentTimeMillis();

        String from = createElement(LOCATION, PREFIX + "relationship-from", "relationship-from", null);
        String to   = createElement(LOCATION, PREFIX + "relationship-to", "relationship-to", null);

        created.add(from);
        created.add(to);

        relate(RESOURCE_LIST, from, to, new Date(now + ONE_DAY), new Date(now + (3 * ONE_DAY)));

        ResultsRequestBody requestBody = new ResultsRequestBody();

        requestBody.setEffectiveTime(new Date(now));

        assertTrue(relatedGUIDs(from, requestBody).isEmpty(), "a relationship not yet in effect was returned for an effective time of now");

        requestBody.setEffectiveTime(new Date(now + (2 * ONE_DAY)));

        assertEquals(List.of(to), relatedGUIDs(from, requestBody), "the relationship should be returned for a time inside its effectivity window");
    }


    /*
     * ===================================== HistoricalQueryOptions =====================================
     */


    /**
     * The history of an element with three versions comes back newest first by default, and oldest first when
     * oldestFirst is set.
     */
    @Test
    @Order(30)
    void historyOrder()
    {
        History history = history();

        List<String> newestFirst = historyDescriptions(history.guid, new HistoryRequestBody());

        assertTrue(newestFirst.containsAll(List.of("history-1", "history-2", "history-3")), "history: " + newestFirst);
        assertInOrder(newestFirst, "history-3", "history-2", "history-1");

        HistoryRequestBody requestBody = new HistoryRequestBody();

        requestBody.setOldestFirst(true);

        assertInOrder(historyDescriptions(history.guid, requestBody), "history-1", "history-2", "history-3");
    }


    /**
     * fromTime and toTime select the versions in force during that window.  The versions are more than a second
     * apart (see historyTimeWindowMillisecondPrecision for versions closer together).
     */
    @Test
    @Order(31)
    void historyTimeWindow()
    {
        assertTimeWindow(history());
    }


    /**
     * fromTime and toTime are dates with millisecond precision, so they select versions made milliseconds apart.
     */
    @Test
    @Order(32)
    void historyTimeWindowMillisecondPrecision()
    {
        assertTimeWindow(history("history-ms", 25));
    }


    private static void assertTimeWindow(History history)
    {
        assertEquals(Set.of("history-1", "history-2", "history-3"), Set.copyOf(historyDescriptions(history.guid, new HistoryRequestBody())),
                     "with no window every version should be returned");

        HistoryRequestBody requestBody = new HistoryRequestBody();

        requestBody.setToTime(history.afterFirst);

        List<String> untilFirst = historyDescriptions(history.guid, requestBody);

        assertTrue(untilFirst.contains("history-1"), "toTime after the first version should include it: " + untilFirst);
        assertFalse(untilFirst.contains("history-2") || untilFirst.contains("history-3"), "toTime returned later versions: " + untilFirst);

        requestBody = new HistoryRequestBody();
        requestBody.setFromTime(history.afterSecond);

        List<String> fromSecond = historyDescriptions(history.guid, requestBody);

        assertTrue(fromSecond.contains("history-3"), "fromTime should include the latest version: " + fromSecond);
        assertFalse(fromSecond.contains("history-1"), "fromTime returned a version replaced before it: " + fromSecond);

        requestBody = new HistoryRequestBody();
        requestBody.setFromTime(history.afterFirst);
        requestBody.setToTime(history.afterSecond);

        List<String> window = historyDescriptions(history.guid, requestBody);

        assertTrue(window.contains("history-2"), "the window should include the version in force throughout it: " + window);
        assertFalse(window.contains("history-3"), "the window included a version created after it ended: " + window);
    }


    private record History(String guid, Date afterFirst, Date afterSecond) {}

    private static History history = null;


    /**
     * An element with three versions more than two seconds apart, and a time between each pair of them.
     */
    private static History history()
    {
        if (history == null)
        {
            history = history("history", 1050);
        }

        return history;
    }


    /**
     * Create an element with three versions, pausing before and after the time recorded between each pair.
     */
    private static History history(String name,
                                   long   pauseMillis)
    {
        String guid = createElement(LOCATION, PREFIX + name, name, "history-1");

        created.add(guid);

        pause(pauseMillis);
        Date afterFirst = new Date();
        pause(pauseMillis);

        updateDescription(guid, "history-2");

        pause(pauseMillis);
        Date afterSecond = new Date();
        pause(pauseMillis);

        updateDescription(guid, "history-3");

        return new History(guid, afterFirst, afterSecond);
    }


    private static void pause(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException interrupted)
        {
            Thread.currentThread().interrupt();
        }
    }


    /*
     * ===================================== Helpers =====================================
     */


    private static OpenMetadataRootElement person(GetRequestBody requestBody)
    {
        OpenMetadataRootElement person = actorManager.element("/actor-profiles/" + personGUID + "/retrieve", requestBody);

        assertNotNull(person, "the person was not returned");

        return person;
    }


    private static Set<String> performsRoles(OpenMetadataRootElement person)
    {
        Set<String> results = new TreeSet<>();

        if (person.getPerformsRoles() != null)
        {
            for (RelatedMetadataElementSummary role : person.getPerformsRoles())
            {
                results.add(role.getRelatedElement().getElementHeader().getGUID());
            }
        }

        return results;
    }


    /**
     * Return the GUIDs of the elements nested under one of the person's roles.
     */
    private static Set<String> nestedGUIDs(OpenMetadataRootElement person,
                                           String                  roleGUID)
    {
        Set<String> results = new TreeSet<>();

        if (person.getPerformsRoles() != null)
        {
            for (RelatedMetadataElementSummary role : person.getPerformsRoles())
            {
                if ((roleGUID.equals(role.getRelatedElement().getElementHeader().getGUID())) &&
                        (role instanceof RelatedMetadataHierarchySummary hierarchy) &&
                        (hierarchy.getNestedElements() != null))
                {
                    for (RelatedMetadataElementSummary nested : hierarchy.getNestedElements())
                    {
                        results.add(nested.getRelatedElement().getElementHeader().getGUID());
                    }
                }
            }
        }

        return results;
    }


    private static boolean findReported(String output,
                                        String guid)
    {
        for (String line : output.split("\n"))
        {
            if ((line.contains("METADATA-OBSERVABILITY-0008") || line.contains("METADATA-OBSERVABILITY-0009")) && line.contains(guid))
            {
                return true;
            }
        }

        return false;
    }


    private static void updateDescription(String guid,
                                          String description)
    {
        UpdatePropertiesRequestBody requestBody = new UpdatePropertiesRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(propertyHelper.addStringProperty(null, DESCRIPTION, description));

        metadataExpert.post("/metadata-elements/" + guid + "/update-properties", requestBody, org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse.class);
    }


    private static List<String> historyDescriptions(String             guid,
                                                    HistoryRequestBody requestBody)
    {
        List<OpenMetadataElement> versions = metadataExpert.post("/metadata-elements/" + guid + "/history",
                                                                 requestBody,
                                                                 OpenMetadataElementsResponse.class).getElements();
        List<String> results = new ArrayList<>();

        if (versions != null)
        {
            for (OpenMetadataElement version : versions)
            {
                results.add(stringProperty(version, DESCRIPTION));
            }
        }

        return results;
    }


    /**
     * The first occurrence of each value comes in the order given.
     */
    private static void assertInOrder(List<String> values,
                                      String...    expectedOrder)
    {
        int previous = -1;

        for (String expected : expectedOrder)
        {
            int index = values.indexOf(expected);

            assertTrue(index > previous, expected + " is out of order in " + values);

            previous = index;
        }
    }
}
