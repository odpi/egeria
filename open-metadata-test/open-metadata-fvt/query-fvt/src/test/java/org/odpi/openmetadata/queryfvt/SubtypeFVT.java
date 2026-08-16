/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.queryfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ProjectClient;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SubtypeFVT exercises {@code QueryOptions.metadataElementSubtypeNames} together with the
 * {@code skipSubtypes} flag: querying a parent type ("Referenceable") while either restricting results to
 * a named list of subtypes ({@code skipSubtypes=false}, the default), or excluding that same list of
 * subtypes from an otherwise unrestricted search ({@code skipSubtypes=true}).
 * <br>
 * Two different Referenceable subtypes are used - Collection and Project - so the include and exclude
 * result sets are exact complements of each other within this test's own qualified-name prefix, which
 * makes the assertions precise regardless of whatever other Collections/Projects the loaded content-packs
 * archives may also contain.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class SubtypeFVT
{
    private static final int EACH_TYPE_COUNT = 5;
    private static final int NO_PROPERTIES_CHECK_COUNT = 3;

    @Test
    void includeAndExcludeSubtypesAreComplementary() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        ProjectClient          projectClient     = connectorContext.getProjectClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper         propertyHelper    = new PropertyHelper();

        String       runPrefix        = QueryFvtTestSupport.newQualifiedName("Subtype");
        List<String> collectionGUIDs  = new ArrayList<>();
        List<String> projectGUIDs     = new ArrayList<>();
        List<String> allCreatedGUIDs  = new ArrayList<>();

        try
        {
            for (int i = 0; i < EACH_TYPE_COUNT; i++)
            {
                NewElementOptions newElementOptions = new NewElementOptions();
                newElementOptions.setIsOwnAnchor(true);

                CollectionProperties collectionProperties = new CollectionProperties();
                collectionProperties.setQualifiedName(runPrefix + ":Collection:" + i);
                collectionProperties.setDisplayName("query-fvt Subtype Collection " + i);

                String guid = collectionClient.createCollection(newElementOptions, null, collectionProperties, null);
                collectionGUIDs.add(guid);
                allCreatedGUIDs.add(guid);
            }

            for (int i = 0; i < EACH_TYPE_COUNT; i++)
            {
                NewElementOptions newElementOptions = new NewElementOptions();
                newElementOptions.setIsOwnAnchor(true);

                ProjectProperties projectProperties = new ProjectProperties();
                projectProperties.setQualifiedName(runPrefix + ":Project:" + i);
                projectProperties.setDisplayName("query-fvt Subtype Project " + i);

                String guid = projectClient.createProject(newElementOptions, null, null, projectProperties, null);
                projectGUIDs.add(guid);
                allCreatedGUIDs.add(guid);
            }

            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                             runPrefix,
                                                                             PropertyComparisonOperator.STARTS_WITH));

            // No subtype restriction at all: every element this test created should come back.
            QueryOptions unrestrictedOptions = new QueryOptions();
            unrestrictedOptions.setMetadataElementTypeName(OpenMetadataType.REFERENCEABLE.typeName);
            unrestrictedOptions.setPageSize(2 * EACH_TYPE_COUNT + 5);

            List<OpenMetadataElement> unrestrictedResults = openMetadataStore.findMetadataElements(searchProperties, null, unrestrictedOptions);

            assertEquals(2 * EACH_TYPE_COUNT, unrestrictedResults.size(),
                         "With no subtype filter, both the Collections and the Projects this test created should be found");

            // Include only Collection: skipSubtypes = false (the default).
            QueryOptions includeOptions = new QueryOptions();
            includeOptions.setMetadataElementTypeName(OpenMetadataType.REFERENCEABLE.typeName);
            includeOptions.setMetadataElementSubtypeNames(List.of(OpenMetadataType.COLLECTION.typeName));
            includeOptions.setSkipSubtypes(false);
            includeOptions.setPageSize(2 * EACH_TYPE_COUNT + 5);

            List<OpenMetadataElement> includeResults = openMetadataStore.findMetadataElements(searchProperties, null, includeOptions);
            Set<String>               includeGUIDs   = includeResults.stream().map(OpenMetadataElement::getElementGUID).collect(Collectors.toSet());

            assertEquals(EACH_TYPE_COUNT, includeResults.size(),
                         "skipSubtypes=false with metadataElementSubtypeNames=[Collection] should return only this test's Collections");
            assertEquals(new HashSet<>(collectionGUIDs), includeGUIDs,
                         "The include-list result set should be exactly the Collections this test created");

            long includeCount = openMetadataStore.countMetadataElements(OpenMetadataType.REFERENCEABLE.typeName,
                                                                         List.of(OpenMetadataType.COLLECTION.typeName),
                                                                         searchProperties,
                                                                         null);

            assertEquals(EACH_TYPE_COUNT, includeCount, "countMetadataElements should agree with findMetadataElements for the include case");

            // Exclude Collection: skipSubtypes = true - everything else (the Projects) should come back instead.
            QueryOptions excludeOptions = new QueryOptions();
            excludeOptions.setMetadataElementTypeName(OpenMetadataType.REFERENCEABLE.typeName);
            excludeOptions.setMetadataElementSubtypeNames(List.of(OpenMetadataType.COLLECTION.typeName));
            excludeOptions.setSkipSubtypes(true);
            excludeOptions.setPageSize(2 * EACH_TYPE_COUNT + 5);

            List<OpenMetadataElement> excludeResults = openMetadataStore.findMetadataElements(searchProperties, null, excludeOptions);
            Set<String>               excludeGUIDs   = excludeResults.stream().map(OpenMetadataElement::getElementGUID).collect(Collectors.toSet());

            assertEquals(EACH_TYPE_COUNT, excludeResults.size(),
                         "skipSubtypes=true with metadataElementSubtypeNames=[Collection] should return everything except this test's Collections");
            assertEquals(new HashSet<>(projectGUIDs), excludeGUIDs,
                         "The exclude-list result set should be exactly the Projects this test created");

            // The include and exclude sets should be exact complements within this test's own data.
            assertTrue(java.util.Collections.disjoint(includeGUIDs, excludeGUIDs),
                       "The include and exclude result sets should not overlap");

            Set<String> union = new HashSet<>(includeGUIDs);
            union.addAll(excludeGUIDs);
            assertEquals(new HashSet<>(allCreatedGUIDs), union,
                         "Together, the include and exclude result sets should account for every element this test created");
        }
        finally
        {
            for (String guid : allCreatedGUIDs)
            {
                QueryFvtTestSupport.purgeElement(openMetadataStore, guid);
            }
        }
    }


    @Test
    void subtypeFilterAppliesWithoutPropertyOrClassificationConditions() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        ProjectClient          projectClient     = connectorContext.getProjectClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();

        String       runPrefix       = QueryFvtTestSupport.newQualifiedName("SubtypeNoProperties");
        List<String> collectionGUIDs = new ArrayList<>();
        List<String> projectGUIDs    = new ArrayList<>();
        List<String> allCreatedGUIDs = new ArrayList<>();

        try
        {
            for (int i = 0; i < NO_PROPERTIES_CHECK_COUNT; i++)
            {
                NewElementOptions newElementOptions = new NewElementOptions();
                newElementOptions.setIsOwnAnchor(true);

                CollectionProperties collectionProperties = new CollectionProperties();
                collectionProperties.setQualifiedName(runPrefix + ":Collection:" + i);
                collectionProperties.setDisplayName("query-fvt SubtypeNoProperties Collection " + i);

                String guid = collectionClient.createCollection(newElementOptions, null, collectionProperties, null);
                collectionGUIDs.add(guid);
                allCreatedGUIDs.add(guid);
            }

            for (int i = 0; i < NO_PROPERTIES_CHECK_COUNT; i++)
            {
                NewElementOptions newElementOptions = new NewElementOptions();
                newElementOptions.setIsOwnAnchor(true);

                ProjectProperties projectProperties = new ProjectProperties();
                projectProperties.setQualifiedName(runPrefix + ":Project:" + i);
                projectProperties.setDisplayName("query-fvt SubtypeNoProperties Project " + i);

                String guid = projectClient.createProject(newElementOptions, null, null, projectProperties, null);
                projectGUIDs.add(guid);
                allCreatedGUIDs.add(guid);
            }

            /*
             * Deliberately no searchProperties/matchClassifications here - this is exactly the call shape that
             * used to be silently routed to an unfiltered getMetadataElementsByType() search, dropping the
             * metadataElementSubtypeNames/skipSubtypes filter entirely (mirroring the equivalent bug that was
             * fixed for findRelationshipsBetweenMetadataElements()). Since there is no property condition to
             * narrow the search to just this test's own elements, the whole-repository Collection corpus has
             * to be paged through in full - comparing against countMetadataElements() was tried and rejected:
             * that method deliberately does not apply forLineage filtering ("not used - accepted for call-site
             * parity"), so it is not a safe invariant to compare against findMetadataElements(), which does.
             */
            Set<String> visitedGUIDs = new HashSet<>();
            int         startFrom    = 0;
            int         pagesFetched = 0;
            int         maxPages     = 20;

            while (true)
            {
                QueryOptions queryOptions = new QueryOptions();
                queryOptions.setMetadataElementTypeName(OpenMetadataType.REFERENCEABLE.typeName);
                queryOptions.setMetadataElementSubtypeNames(List.of(OpenMetadataType.COLLECTION.typeName));
                queryOptions.setSkipSubtypes(false);
                queryOptions.setStartFrom(startFrom);
                queryOptions.setPageSize(QueryFvtTestSupport.MAX_PAGE_SIZE);
                queryOptions.setSequencingOrder(SequencingOrder.GUID);

                List<OpenMetadataElement> page = openMetadataStore.findMetadataElements(null, null, queryOptions);

                if ((page == null) || page.isEmpty())
                {
                    break;
                }

                for (OpenMetadataElement element : page)
                {
                    visitedGUIDs.add(element.getElementGUID());
                }

                pagesFetched++;
                startFrom += QueryFvtTestSupport.MAX_PAGE_SIZE;

                assertTrue(pagesFetched <= maxPages,
                          "Paging did not terminate within " + maxPages + " pages - either the subtype filter " +
                                  "genuinely was dropped (returning every Referenceable, not just Collections), " +
                                  "or the reference archives have grown enough to need a higher bound here");
            }

            for (String collectionGUID : collectionGUIDs)
            {
                assertTrue(visitedGUIDs.contains(collectionGUID),
                          "This test's own Collection " + collectionGUID + " should be found when searching by " +
                                  "subtype with no property/classification conditions");
            }

            for (String projectGUID : projectGUIDs)
            {
                assertTrue(! visitedGUIDs.contains(projectGUID),
                          "This test's own Project " + projectGUID + " should be excluded by the subtype filter " +
                                  "even with no property/classification conditions - if it appears here, the " +
                                  "subtype filter was silently dropped");
            }
        }
        finally
        {
            for (String guid : allCreatedGUIDs)
            {
                QueryFvtTestSupport.purgeElement(openMetadataStore, guid);
            }
        }
    }
}
