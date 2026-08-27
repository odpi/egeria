/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.queryfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ClassificationCondition;
import org.odpi.openmetadata.frameworks.openmetadata.search.MatchCriteria;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchClassifications;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verify that searching by classification keeps up with the classifications actually on the elements.
 * <br>
 * A classification filter that has quietly stopped filtering returns more than it should rather than
 * failing, so it is only caught by asserting the negative: that an element which should not be in the
 * results is not in them.  This exercises three ways of being absent - never classified, classified then
 * declassified, and excluded by NONE - because they reach the repository differently and have each been
 * wrong at some point.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ClassificationSearchFVT
{
    private static final String CATEGORY            = "ClassificationSearch";
    private static final String CLASSIFICATION_NAME = OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName;


    @Test
    void classificationSearchFollowsClassifyAndDeclassify() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient     collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            String classifiedGUID   = this.createCollection(collectionClient, createdGUIDs, "Classified");
            String declassifiedGUID = this.createCollection(collectionClient, createdGUIDs, "Declassified");
            String plainGUID        = this.createCollection(collectionClient, createdGUIDs, "Plain");

            openMetadataStore.classifyMetadataElementInStore(classifiedGUID, CLASSIFICATION_NAME, new MetadataSourceOptions(), null);
            openMetadataStore.classifyMetadataElementInStore(declassifiedGUID, CLASSIFICATION_NAME, new MetadataSourceOptions(), null);

            /*
             * Both classified elements are found; the one that was never classified is not.
             */
            Set<String> found = this.findByClassification(openMetadataStore, MatchCriteria.ALL);

            assertTrue(found.contains(classifiedGUID),
                       "A classified element should be found by a search for its classification");
            assertTrue(found.contains(declassifiedGUID),
                       "A classified element should be found by a search for its classification");
            assertFalse(found.contains(plainGUID),
                        "An element that was never classified should not be found by a search for that classification");

            /*
             * Removing the classification has to remove the element from the results.  A classification is
             * removed by soft-deleting its row rather than by taking it away, so a search that does not
             * exclude deleted rows keeps matching a classification that is no longer there.
             */
            openMetadataStore.declassifyMetadataElementInStore(declassifiedGUID, CLASSIFICATION_NAME, new MetadataSourceOptions());

            found = this.findByClassification(openMetadataStore, MatchCriteria.ALL);

            assertTrue(found.contains(classifiedGUID),
                       "The still-classified element should continue to be found");
            assertFalse(found.contains(declassifiedGUID),
                        "A declassified element should no longer be found by a search for that classification");

            /*
             * NONE is the other direction: everything except the classified elements, which has to include
             * the element that has never carried a classification at all.
             */
            Set<String> excluded = this.findByClassification(openMetadataStore, MatchCriteria.NONE);

            assertTrue(excluded.contains(plainGUID),
                       "An element with no classifications should be found when NONE is requested");
            assertTrue(excluded.contains(declassifiedGUID),
                       "A declassified element should be found when NONE is requested");
            assertFalse(excluded.contains(classifiedGUID),
                        "A classified element should not be found when NONE is requested");
        }
        finally
        {
            for (String guid : createdGUIDs)
            {
                QueryFvtTestSupport.purgeElement(openMetadataStore, guid);
            }
        }
    }


    /**
     * Create a collection and remember it for clean up.
     *
     * @param collectionClient client to create through
     * @param createdGUIDs list to record the new element in
     * @param label distinguishes the elements of one run from each other
     * @return new element's unique identifier
     * @throws Exception the element could not be created
     */
    private String createCollection(CollectionClient collectionClient,
                                    List<String>     createdGUIDs,
                                    String           label) throws Exception
    {
        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties properties = new CollectionProperties();

        properties.setQualifiedName(QueryFvtTestSupport.newQualifiedName(CATEGORY + ":" + label));
        properties.setDisplayName("query-fvt " + CATEGORY + " " + label);

        String elementGUID = collectionClient.createCollection(newElementOptions, null, properties, null);

        createdGUIDs.add(elementGUID);

        return elementGUID;
    }


    /**
     * Search for collections by the classification under test.
     *
     * @param openMetadataStore store to search
     * @param matchCriteria whether the classification must be present or absent
     * @return unique identifiers of the elements found
     * @throws Exception the search failed
     */
    private Set<String> findByClassification(OpenMetadataStore openMetadataStore,
                                             MatchCriteria     matchCriteria) throws Exception
    {
        ClassificationCondition condition = new ClassificationCondition();

        condition.setName(CLASSIFICATION_NAME);

        List<ClassificationCondition> conditions = new ArrayList<>();

        conditions.add(condition);

        SearchClassifications searchClassifications = new SearchClassifications();

        searchClassifications.setConditions(conditions);
        searchClassifications.setMatchCriteria(matchCriteria);

        List<OpenMetadataElement> results = openMetadataStore.findMetadataElements(OpenMetadataType.COLLECTION.typeName,
                                                                                    null,
                                                                                    null,
                                                                                    searchClassifications,
                                                                                    0,
                                                                                    0);
        Set<String> guids = new HashSet<>();

        if (results != null)
        {
            for (OpenMetadataElement result : results)
            {
                if (result != null)
                {
                    guids.add(result.getElementGUID());
                }
            }
        }

        return guids;
    }
}
