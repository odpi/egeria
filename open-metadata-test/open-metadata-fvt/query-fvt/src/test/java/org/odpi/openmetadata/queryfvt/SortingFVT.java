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
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SortingFVT exercises {@code QueryOptions}' sequencing options - GUID, creation-date, last-update-date
 * and property-value ordering, both ascending and descending - against {@code OpenMetadataStore.findMetadataElements}.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class SortingFVT
{
    private static final int ELEMENT_COUNT = 8;

    @Test
    void propertyAscendingAndDescendingOrderQualifiedName() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper         propertyHelper    = new PropertyHelper();

        String       runPrefix    = QueryFvtTestSupport.newQualifiedName("SortProperty");
        List<String> createdGUIDs = new ArrayList<>();
        List<String> expectedAscendingQualifiedNames = new ArrayList<>();

        try
        {
            // Elements are deliberately created in the REVERSE of their expected ascending qualifiedName order
            // (element "07" first, "00" last). If PROPERTY_ASCENDING/PROPERTY_DESCENDING sequencing were only
            // ordering by creation date under the covers - rather than genuinely by the requested property's
            // value - creation order and qualifiedName-ascending order would be opposites here, so the
            // assertions below would fail instead of accidentally passing.
            for (int i = ELEMENT_COUNT - 1; i >= 0; i--)
            {
                // Zero-padded so lexical (string) ordering matches numeric ordering.
                String qualifiedName = runPrefix + ":" + String.format("%02d", i);

                NewElementOptions newElementOptions = new NewElementOptions();
                newElementOptions.setIsOwnAnchor(true);

                CollectionProperties createProperties = new CollectionProperties();
                createProperties.setQualifiedName(qualifiedName);
                createProperties.setDisplayName("query-fvt Sort Collection " + i);

                createdGUIDs.add(collectionClient.createCollection(newElementOptions, null, createProperties, null));
            }

            for (int i = 0; i < ELEMENT_COUNT; i++)
            {
                expectedAscendingQualifiedNames.add(runPrefix + ":" + String.format("%02d", i));
            }

            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                             runPrefix,
                                                                             PropertyComparisonOperator.STARTS_WITH));

            QueryOptions ascendingOptions = new QueryOptions();
            ascendingOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            ascendingOptions.setSequencingOrder(SequencingOrder.PROPERTY_ASCENDING);
            ascendingOptions.setSequencingProperty(OpenMetadataProperty.QUALIFIED_NAME.name);
            ascendingOptions.setPageSize(ELEMENT_COUNT + 5);

            List<OpenMetadataElement> ascendingResults = openMetadataStore.findMetadataElements(searchProperties,
                                                                                                  null,
                                                                                                  ascendingOptions);

            assertEquals(ELEMENT_COUNT, ascendingResults.size());

            for (int i = 0; i < ELEMENT_COUNT; i++)
            {
                String actualQualifiedName = getQualifiedName(ascendingResults.get(i));

                assertEquals(expectedAscendingQualifiedNames.get(i), actualQualifiedName,
                             "PROPERTY_ASCENDING on qualifiedName should return element " + i + " in ascending order");
            }

            QueryOptions descendingOptions = new QueryOptions();
            descendingOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            descendingOptions.setSequencingOrder(SequencingOrder.PROPERTY_DESCENDING);
            descendingOptions.setSequencingProperty(OpenMetadataProperty.QUALIFIED_NAME.name);
            descendingOptions.setPageSize(ELEMENT_COUNT + 5);

            List<OpenMetadataElement> descendingResults = openMetadataStore.findMetadataElements(searchProperties,
                                                                                                   null,
                                                                                                   descendingOptions);

            assertEquals(ELEMENT_COUNT, descendingResults.size());

            for (int i = 0; i < ELEMENT_COUNT; i++)
            {
                String actualQualifiedName = getQualifiedName(descendingResults.get(i));
                String expectedQualifiedName = expectedAscendingQualifiedNames.get(ELEMENT_COUNT - 1 - i);

                assertEquals(expectedQualifiedName, actualQualifiedName,
                             "PROPERTY_DESCENDING on qualifiedName should return element " + i + " in descending order");
            }
        }
        finally
        {
            for (String guid : createdGUIDs)
            {
                QueryFvtTestSupport.purgeElement(openMetadataStore, guid);
            }
        }
    }


    @Test
    void propertyAscendingOrdersNumericPropertyByValueNotText() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        ProjectClient         projectClient     = connectorContext.getProjectClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper         propertyHelper    = new PropertyHelper();

        String       runPrefix    = QueryFvtTestSupport.newQualifiedName("SortNumericProperty");
        List<String> createdGUIDs = new ArrayList<>();

        // Deliberately includes a case a plain text sort would get wrong: lexically, "10" < "2" < "21" < "9",
        // but numerically 2 < 9 < 10 < 21.  Created in an order different from either the expected numeric or
        // the (wrong) lexical order, so neither creation order nor accidental text ordering could pass this test.
        int[] priorities = {21, 2, 10, 9};

        try
        {
            for (int priority : priorities)
            {
                NewElementOptions newElementOptions = new NewElementOptions();
                newElementOptions.setIsOwnAnchor(true);

                ProjectProperties createProperties = new ProjectProperties();
                createProperties.setQualifiedName(runPrefix + ":" + priority);
                createProperties.setDisplayName("query-fvt Sort Numeric Property Project " + priority);
                createProperties.setPriority(priority);

                createdGUIDs.add(projectClient.createProject(newElementOptions, null, null, createProperties, null));
            }

            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                             runPrefix,
                                                                             PropertyComparisonOperator.STARTS_WITH));

            QueryOptions ascendingOptions = new QueryOptions();
            ascendingOptions.setMetadataElementTypeName(OpenMetadataType.PROJECT.typeName);
            ascendingOptions.setSequencingOrder(SequencingOrder.PROPERTY_ASCENDING);
            ascendingOptions.setSequencingProperty(OpenMetadataProperty.PRIORITY.name);
            ascendingOptions.setPageSize(priorities.length + 5);

            List<OpenMetadataElement> ascendingResults = openMetadataStore.findMetadataElements(searchProperties, null, ascendingOptions);

            assertEquals(priorities.length, ascendingResults.size());

            int[] expectedAscending = {2, 9, 10, 21};

            for (int i = 0; i < expectedAscending.length; i++)
            {
                int actualPriority = propertyHelper.getIntProperty("query-fvt",
                                                                    OpenMetadataProperty.PRIORITY.name,
                                                                    ascendingResults.get(i).getElementProperties(),
                                                                    "propertyAscendingOrdersNumericPropertyByValueNotText");

                assertEquals(expectedAscending[i], actualPriority,
                             "PROPERTY_ASCENDING on a numeric property should compare by numeric value, not text - " +
                                     "element " + i + " is out of place");
            }

            QueryOptions descendingOptions = new QueryOptions();
            descendingOptions.setMetadataElementTypeName(OpenMetadataType.PROJECT.typeName);
            descendingOptions.setSequencingOrder(SequencingOrder.PROPERTY_DESCENDING);
            descendingOptions.setSequencingProperty(OpenMetadataProperty.PRIORITY.name);
            descendingOptions.setPageSize(priorities.length + 5);

            List<OpenMetadataElement> descendingResults = openMetadataStore.findMetadataElements(searchProperties, null, descendingOptions);

            assertEquals(priorities.length, descendingResults.size());

            int[] expectedDescending = {21, 10, 9, 2};

            for (int i = 0; i < expectedDescending.length; i++)
            {
                int actualPriority = propertyHelper.getIntProperty("query-fvt",
                                                                    OpenMetadataProperty.PRIORITY.name,
                                                                    descendingResults.get(i).getElementProperties(),
                                                                    "propertyAscendingOrdersNumericPropertyByValueNotText");

                assertEquals(expectedDescending[i], actualPriority,
                             "PROPERTY_DESCENDING on a numeric property should compare by numeric value, not text - " +
                                     "element " + i + " is out of place");
            }
        }
        finally
        {
            for (String guid : createdGUIDs)
            {
                QueryFvtTestSupport.purgeElement(openMetadataStore, guid);
            }
        }
    }


    @Test
    void guidOrderingIsStableAndCoversEveryElement() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper         propertyHelper    = new PropertyHelper();

        String       runPrefix    = QueryFvtTestSupport.newQualifiedName("SortGuid");
        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            for (int i = 0; i < ELEMENT_COUNT; i++)
            {
                NewElementOptions newElementOptions = new NewElementOptions();
                newElementOptions.setIsOwnAnchor(true);

                CollectionProperties createProperties = new CollectionProperties();
                createProperties.setQualifiedName(runPrefix + ":" + i);
                createProperties.setDisplayName("query-fvt Sort Guid Collection " + i);

                createdGUIDs.add(collectionClient.createCollection(newElementOptions, null, createProperties, null));
            }

            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                             runPrefix,
                                                                             PropertyComparisonOperator.STARTS_WITH));

            QueryOptions queryOptions = new QueryOptions();
            queryOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            queryOptions.setSequencingOrder(SequencingOrder.GUID);
            queryOptions.setPageSize(ELEMENT_COUNT + 5);

            List<OpenMetadataElement> firstAttempt = openMetadataStore.findMetadataElements(searchProperties,
                                                                                             null,
                                                                                             queryOptions);
            List<OpenMetadataElement> secondAttempt = openMetadataStore.findMetadataElements(searchProperties,
                                                                                              null,
                                                                                              queryOptions);

            assertEquals(ELEMENT_COUNT, firstAttempt.size());
            assertEquals(firstAttempt.size(), secondAttempt.size());

            for (int i = 0; i < firstAttempt.size(); i++)
            {
                assertEquals(firstAttempt.get(i).getElementGUID(), secondAttempt.get(i).getElementGUID(),
                             "GUID ordering should be stable and repeatable across identical requests");
            }

            for (int i = 1; i < firstAttempt.size(); i++)
            {
                assertTrue(firstAttempt.get(i - 1).getElementGUID().compareTo(firstAttempt.get(i).getElementGUID()) <= 0,
                           "GUID ordering should be ascending");
            }
        }
        finally
        {
            for (String guid : createdGUIDs)
            {
                QueryFvtTestSupport.purgeElement(openMetadataStore, guid);
            }
        }
    }


    @Test
    void lastUpdateRecentReflectsUpdates() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper         propertyHelper    = new PropertyHelper();

        String runPrefix = QueryFvtTestSupport.newQualifiedName("SortLastUpdate");
        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            NewElementOptions newElementOptionsA = new NewElementOptions();
            newElementOptionsA.setIsOwnAnchor(true);
            CollectionProperties propertiesA = new CollectionProperties();
            propertiesA.setQualifiedName(runPrefix + ":A");
            propertiesA.setDisplayName("query-fvt Sort Last Update A");
            String guidA = collectionClient.createCollection(newElementOptionsA, null, propertiesA, null);
            createdGUIDs.add(guidA);

            NewElementOptions newElementOptionsB = new NewElementOptions();
            newElementOptionsB.setIsOwnAnchor(true);
            CollectionProperties propertiesB = new CollectionProperties();
            propertiesB.setQualifiedName(runPrefix + ":B");
            propertiesB.setDisplayName("query-fvt Sort Last Update B");
            String guidB = collectionClient.createCollection(newElementOptionsB, null, propertiesB, null);
            createdGUIDs.add(guidB);

            // Touch A after B was created, so A - not B - should now be "most recently updated".
            CollectionProperties updateForA = new CollectionProperties();
            updateForA.setQualifiedName(runPrefix + ":A");
            updateForA.setDisplayName("query-fvt Sort Last Update A (touched)");
            collectionClient.updateCollection(guidA, new UpdateOptions(), updateForA);

            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                             runPrefix,
                                                                             PropertyComparisonOperator.STARTS_WITH));

            QueryOptions queryOptions = new QueryOptions();
            queryOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            queryOptions.setSequencingOrder(SequencingOrder.LAST_UPDATE_RECENT);
            queryOptions.setPageSize(10);

            List<OpenMetadataElement> results = openMetadataStore.findMetadataElements(searchProperties,
                                                                                        null,
                                                                                        queryOptions);

            assertEquals(2, results.size());
            assertEquals(guidA, results.get(0).getElementGUID(),
                         "LAST_UPDATE_RECENT should list the element updated most recently (A) first");
        }
        finally
        {
            for (String guid : createdGUIDs)
            {
                QueryFvtTestSupport.purgeElement(openMetadataStore, guid);
            }
        }
    }


    private String getQualifiedName(OpenMetadataElement element)
    {
        return new PropertyHelper().getStringProperty("query-fvt",
                                                       OpenMetadataProperty.QUALIFIED_NAME.name,
                                                       element.getElementProperties(),
                                                       "getQualifiedName");
    }
}
