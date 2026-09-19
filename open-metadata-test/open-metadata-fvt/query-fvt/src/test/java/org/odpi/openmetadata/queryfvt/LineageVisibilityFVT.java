/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.queryfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.PromiseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verify that an element carrying one of the lineage-only classifications - Memento at the end of an element's
 * life, Promise at the start of it - is returned only when the request has {@code forLineage=true}.
 * <br>
 * The two classifications reach the visibility check by the same path but for different reasons: a Memento is
 * kept after its real-world counterpart has gone, a Promise exists before its real-world counterpart has been
 * delivered.  Both are needed to keep a lineage graph complete, and neither should leak into an ordinary query.
 * Each of the ways an element can be reached - a property search, a lookup by unique name, a lookup by GUID and a
 * traversal from a related element - is checked in both directions, because a classification that hides the element
 * from one path but not another is exactly the kind of regression that a single "is it found" check would miss.
 * <br>
 * {@link DeleteMethodFVT} covers the Memento classification being applied by the ARCHIVE delete method; this
 * suite applies the classifications directly, and for Promise goes through the Classification Explorer client
 * that stewards would use.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class LineageVisibilityFVT
{
    private static final String CATEGORY = "LineageVisibility";

    /**
     * Appended to the assertions that check a search found nothing.  Null is the end of a result set;
     * an empty list means only that this batch was filtered out, so a caller that honours the paging
     * contract keeps asking.  Accepting either here would let a regression from one to the other pass unnoticed.
     */
    private static final String NOTHING_MEANS_NULL =
            ".  A search that matches nothing returns null, not an empty list";


    @Test
    void mementoClassifiedElementIsOnlyVisibleToLineageRequests() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient     collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();

        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            TestElements testElements = this.createTestElements(collectionClient, createdGUIDs, "Memento");

            this.assertVisibleToEveryone(openMetadataStore, testElements, OpenMetadataType.MEMENTO_CLASSIFICATION.typeName);

            openMetadataStore.classifyMetadataElementInStore(testElements.elementGUID,
                                                             OpenMetadataType.MEMENTO_CLASSIFICATION.typeName,
                                                             new MetadataSourceOptions(),
                                                             null);

            this.assertVisibleOnlyForLineage(openMetadataStore, testElements, OpenMetadataType.MEMENTO_CLASSIFICATION.typeName);
        }
        finally
        {
            this.purge(openMetadataStore, createdGUIDs);
        }
    }


    @Test
    void promiseClassifiedElementIsOnlyVisibleToLineageRequests() throws Exception
    {
        ConnectorContextBase         connectorContext             = ConnectorContextFactory.newContext();
        CollectionClient             collectionClient             = connectorContext.getCollectionClient();
        ClassificationExplorerClient classificationExplorerClient = connectorContext.getClassificationExplorerClient();
        OpenMetadataStore            openMetadataStore            = connectorContext.getOpenMetadataStore();
        PropertyHelper               propertyHelper               = new PropertyHelper();

        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            TestElements testElements = this.createTestElements(collectionClient, createdGUIDs, "Promise");

            this.assertVisibleToEveryone(openMetadataStore, testElements, OpenMetadataType.PROMISE_CLASSIFICATION.typeName);

            Date dueTime = new Date();

            PromiseProperties promiseProperties = new PromiseProperties();

            promiseProperties.setDeploymentStatus(DeploymentStatus.UNDER_DEVELOPMENT);
            promiseProperties.setDueTime(dueTime);

            classificationExplorerClient.setElementAsPromise(testElements.elementGUID, promiseProperties, new MetadataSourceOptions());

            this.assertVisibleOnlyForLineage(openMetadataStore, testElements, OpenMetadataType.PROMISE_CLASSIFICATION.typeName);

            /*
             * The classification's own properties should have survived the trip through the builder and back.
             */
            AttachedClassification promise = this.getClassification(this.getByGUID(openMetadataStore, testElements.elementGUID, true),
                                                                     OpenMetadataType.PROMISE_CLASSIFICATION.typeName);

            assertNotNull(promise, "The Promise classification should be returned with the element on a lineage request");
            assertEquals(DeploymentStatus.UNDER_DEVELOPMENT.name(),
                         propertyHelper.getEnumPropertySymbolicName(CATEGORY,
                                                                    OpenMetadataProperty.DEPLOYMENT_STATUS.name,
                                                                    promise.getClassificationProperties(),
                                                                    "promiseClassifiedElementIsOnlyVisibleToLineageRequests"),
                         "The deploymentStatus supplied when the element was classified should be returned");
            assertEquals(dueTime,
                         propertyHelper.getDateProperty(CATEGORY,
                                                        OpenMetadataProperty.DUE_TIME.name,
                                                        promise.getClassificationProperties(),
                                                        "promiseClassifiedElementIsOnlyVisibleToLineageRequests"),
                         "The dueTime supplied when the element was classified should be returned");

            /*
             * A promise is a placeholder for the element that will be delivered, so its qualified name is still
             * reserved even though the element is hidden from ordinary requests.  A memento, by contrast, has its
             * qualified name renamed when it is archived, so it does not need to hold on to the original.
             */
            InvalidParameterException duplicateName = assertThrows(InvalidParameterException.class,
                                                                   () -> this.createCollection(collectionClient, createdGUIDs, testElements.qualifiedName, "Duplicate"),
                                                                   "Creating an element with the same qualified name as a Promise should be rejected");

            assertTrue(duplicateName.getMessage().contains(testElements.qualifiedName),
                       "The rejection should name the qualified name that is already in use: " + duplicateName.getMessage());

            /*
             * Delivering the promise: removing the classification makes the element an ordinary element again.
             * The classified element is only visible to lineage requests, so the request to remove the
             * classification has to be a lineage request too, or it cannot find the element to declassify.
             */
            MetadataSourceOptions lineageOptions = new MetadataSourceOptions();

            lineageOptions.setForLineage(true);

            classificationExplorerClient.clearElementAsPromise(testElements.elementGUID, lineageOptions);

            this.assertVisibleToEveryone(openMetadataStore, testElements, OpenMetadataType.PROMISE_CLASSIFICATION.typeName);
        }
        finally
        {
            this.purge(openMetadataStore, createdGUIDs);
        }
    }


    /**
     * The element under test plus the collection it is a member of, so that a traversal from a related
     * element can be checked as well as the direct lookups.
     */
    private static final class TestElements
    {
        private String elementGUID;
        private String qualifiedName;
        private String parentGUID;
    }


    /**
     * Create a parent collection and a member collection.  The member is the element that is classified.
     *
     * @param collectionClient client to create with
     * @param createdGUIDs accumulates everything created so that it can be purged
     * @param label used in the qualified names
     * @return the identifiers of what was created
     * @throws Exception something went wrong
     */
    private TestElements createTestElements(CollectionClient collectionClient,
                                            List<String>     createdGUIDs,
                                            String           label) throws Exception
    {
        TestElements testElements = new TestElements();

        testElements.parentGUID    = this.createCollection(collectionClient, createdGUIDs, label + "Parent");
        testElements.qualifiedName = QueryFvtTestSupport.newQualifiedName(CATEGORY + label);
        testElements.elementGUID   = this.createCollection(collectionClient, createdGUIDs, testElements.qualifiedName, label);

        collectionClient.addToCollection(testElements.parentGUID, testElements.elementGUID, null, null);

        return testElements;
    }


    private String createCollection(CollectionClient collectionClient,
                                    List<String>     createdGUIDs,
                                    String           label) throws Exception
    {
        return this.createCollection(collectionClient, createdGUIDs, QueryFvtTestSupport.newQualifiedName(CATEGORY + label), label);
    }


    private String createCollection(CollectionClient collectionClient,
                                    List<String>     createdGUIDs,
                                    String           qualifiedName,
                                    String           label) throws Exception
    {
        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties properties = new CollectionProperties();

        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName("query-fvt " + CATEGORY + " " + label);

        String guid = collectionClient.createCollection(newElementOptions, null, properties, null);

        createdGUIDs.add(guid);

        return guid;
    }


    /**
     * Before the classification is applied (and again after the Promise is cleared), the element is found by
     * every route whether or not the request is for lineage.
     */
    private void assertVisibleToEveryone(OpenMetadataStore openMetadataStore,
                                         TestElements      testElements,
                                         String            classificationName) throws Exception
    {
        for (boolean forLineage : new boolean[]{ false, true })
        {
            String requestKind = "a request with forLineage=" + forLineage;

            List<OpenMetadataElement> found = this.findByQualifiedName(openMetadataStore, testElements.qualifiedName, forLineage);

            assertNotNull(found, "An element without the " + classificationName + " classification should be found by a property search on " + requestKind);
            assertEquals(1, found.size(), "Exactly one element should match the qualified name on " + requestKind);
            assertEquals(testElements.elementGUID, found.get(0).getElementGUID());

            OpenMetadataElement byUniqueName = this.getByUniqueName(openMetadataStore, testElements.qualifiedName, forLineage);

            assertNotNull(byUniqueName, "An element without the " + classificationName + " classification should be found by unique name on " + requestKind);
            assertEquals(testElements.elementGUID, byUniqueName.getElementGUID());

            OpenMetadataElement byGUID = this.getByGUID(openMetadataStore, testElements.elementGUID, forLineage);

            assertNotNull(byGUID, "An element without the " + classificationName + " classification should be found by GUID on " + requestKind);
            assertNull(this.getClassification(byGUID, classificationName),
                       "The " + classificationName + " classification should not be present on " + requestKind);

            assertTrue(this.getMemberGUIDs(openMetadataStore, testElements.parentGUID, forLineage).contains(testElements.elementGUID),
                       "An element without the " + classificationName + " classification should be reached from a related element on " + requestKind);
        }
    }


    /**
     * Once the classification is applied, the element disappears from every route unless the request is for
     * lineage, in which case every route still finds it and shows the classification.
     */
    private void assertVisibleOnlyForLineage(OpenMetadataStore openMetadataStore,
                                             TestElements      testElements,
                                             String            classificationName) throws Exception
    {
        /*
         * Ordinary requests: the element is invisible by every route.
         */
        assertNull(this.findByQualifiedName(openMetadataStore, testElements.qualifiedName, false),
                   "A property search without forLineage should not find an element classified as " + classificationName + NOTHING_MEANS_NULL);

        assertNull(this.getByUniqueName(openMetadataStore, testElements.qualifiedName, false),
                   "A lookup by unique name without forLineage should not find an element classified as " + classificationName);

        assertThrows(InvalidParameterException.class,
                     () -> this.getByGUID(openMetadataStore, testElements.elementGUID, false),
                     "A lookup by GUID without forLineage should report an element classified as " + classificationName + " as unavailable");

        assertFalse(this.getMemberGUIDs(openMetadataStore, testElements.parentGUID, false).contains(testElements.elementGUID),
                    "A traversal from a related element without forLineage should not reach an element classified as " + classificationName);

        /*
         * Lineage requests: the element is visible by every route, carrying the classification.
         */
        List<OpenMetadataElement> found = this.findByQualifiedName(openMetadataStore, testElements.qualifiedName, true);

        assertNotNull(found, "A property search with forLineage should find an element classified as " + classificationName);
        assertEquals(1, found.size(), "Exactly one element should match the qualified name on a lineage request");
        assertEquals(testElements.elementGUID, found.get(0).getElementGUID());
        assertNotNull(this.getClassification(found.get(0), classificationName),
                      "The " + classificationName + " classification should be returned with the element found by a lineage property search");

        OpenMetadataElement byUniqueName = this.getByUniqueName(openMetadataStore, testElements.qualifiedName, true);

        assertNotNull(byUniqueName, "A lookup by unique name with forLineage should find an element classified as " + classificationName);
        assertEquals(testElements.elementGUID, byUniqueName.getElementGUID());

        OpenMetadataElement byGUID = this.getByGUID(openMetadataStore, testElements.elementGUID, true);

        assertNotNull(byGUID, "A lookup by GUID with forLineage should find an element classified as " + classificationName);
        assertNotNull(this.getClassification(byGUID, classificationName),
                      "The " + classificationName + " classification should be returned with the element found by a lineage GUID lookup");

        assertTrue(this.getMemberGUIDs(openMetadataStore, testElements.parentGUID, true).contains(testElements.elementGUID),
                   "A traversal from a related element with forLineage should reach an element classified as " + classificationName);
    }


    private List<OpenMetadataElement> findByQualifiedName(OpenMetadataStore openMetadataStore,
                                                          String            qualifiedName,
                                                          boolean           forLineage) throws Exception
    {
        PropertyHelper propertyHelper = new PropertyHelper();

        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                         qualifiedName,
                                                                         PropertyComparisonOperator.EQ));

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
        queryOptions.setForLineage(forLineage);

        return openMetadataStore.findMetadataElements(searchProperties, null, queryOptions);
    }


    private OpenMetadataElement getByUniqueName(OpenMetadataStore openMetadataStore,
                                                String            qualifiedName,
                                                boolean           forLineage) throws Exception
    {
        GetOptions getOptions = new GetOptions();

        getOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
        getOptions.setForLineage(forLineage);

        return openMetadataStore.getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name, getOptions);
    }


    private OpenMetadataElement getByGUID(OpenMetadataStore openMetadataStore,
                                          String            elementGUID,
                                          boolean           forLineage) throws Exception
    {
        GetOptions getOptions = new GetOptions();

        getOptions.setForLineage(forLineage);

        return openMetadataStore.getMetadataElementByGUID(elementGUID, getOptions);
    }


    /**
     * Return the members of a collection, paging through the results so the check does not depend on the
     * member under test landing in the first page.
     */
    private Set<String> getMemberGUIDs(OpenMetadataStore openMetadataStore,
                                       String            parentGUID,
                                       boolean           forLineage) throws Exception
    {
        Set<String> memberGUIDs = new HashSet<>();
        int         startFrom   = 0;

        while (true)
        {
            QueryOptions queryOptions = new QueryOptions();

            queryOptions.setStartFrom(startFrom);
            queryOptions.setPageSize(QueryFvtTestSupport.MAX_PAGE_SIZE);
            queryOptions.setForLineage(forLineage);

            RelatedMetadataElementList page = openMetadataStore.getRelatedMetadataElements(parentGUID,
                                                                                           1,
                                                                                           OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                           queryOptions);

            if ((page == null) || (page.getElementList() == null))
            {
                return memberGUIDs;
            }

            for (RelatedMetadataElement relatedElement : page.getElementList())
            {
                if ((relatedElement != null) && (relatedElement.getElement() != null))
                {
                    memberGUIDs.add(relatedElement.getElement().getElementGUID());
                }
            }

            startFrom = startFrom + QueryFvtTestSupport.MAX_PAGE_SIZE;
        }
    }


    private AttachedClassification getClassification(OpenMetadataElement element,
                                                     String              classificationName)
    {
        if ((element != null) && (element.getClassifications() != null))
        {
            for (AttachedClassification classification : element.getClassifications())
            {
                if ((classification != null) && classificationName.equals(classification.getClassificationName()))
                {
                    return classification;
                }
            }
        }

        return null;
    }


    private void purge(OpenMetadataStore openMetadataStore,
                       List<String>      createdGUIDs)
    {
        for (String guid : createdGUIDs)
        {
            QueryFvtTestSupport.purgeElement(openMetadataStore, guid);
        }
    }
}
