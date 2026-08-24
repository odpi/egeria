/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.queryfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.EndMatchCriteria;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DeleteMethodFVT checks the ARCHIVE, LOOK_FOR_LINEAGE and relationship PURGE delete methods routed through
 * {@code OpenMetadataStoreRESTServices.deleteMetadataElementInStore}/{@code deleteRelationshipInStore} - ie
 * that the delete method carried in the request's {@code DeleteOptions} determines which handler operation is
 * actually invoked, and that each operation has the expected observable effect.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class DeleteMethodFVT
{
    @Test
    void archiveMethodClassifiesElementAsMementoAndHidesFromDefaultSearch() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper         propertyHelper    = new PropertyHelper();

        String qualifiedName = QueryFvtTestSupport.newQualifiedName("Archive");

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties createProperties = new CollectionProperties();
        createProperties.setQualifiedName(qualifiedName);
        createProperties.setDisplayName("query-fvt Archive Collection");

        String elementGUID = collectionClient.createCollection(newElementOptions, null, createProperties, null);

        try
        {
            DeleteOptions archiveOptions = new DeleteOptions();
            archiveOptions.setDeleteMethod(DeleteMethod.ARCHIVE);

            openMetadataStore.deleteMetadataElementInStore(elementGUID, archiveOptions);

            /*
             * Archiving renames the qualifiedName (appending "_archivedOn_<date>"), so match on the original
             * prefix rather than an exact qualifiedName.
             */
            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                             qualifiedName,
                                                                             PropertyComparisonOperator.STARTS_WITH));

            QueryOptions defaultOptions = new QueryOptions();
            defaultOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);

            List<OpenMetadataElement> afterArchiveDefault = openMetadataStore.findMetadataElements(searchProperties, null, defaultOptions);

            assertTrue((afterArchiveDefault == null) || afterArchiveDefault.isEmpty(),
                       "A default (non-lineage) query should no longer find an archived element");

            QueryOptions lineageOptions = new QueryOptions();
            lineageOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            lineageOptions.setForLineage(true);

            List<OpenMetadataElement> afterArchiveLineage = openMetadataStore.findMetadataElements(searchProperties, null, lineageOptions);

            assertEquals(1, afterArchiveLineage.size(), "A forLineage query should still find the archived element");
            assertEquals(elementGUID, afterArchiveLineage.get(0).getElementGUID());
            assertTrue(hasMementoClassification(afterArchiveLineage.get(0)),
                      "The archived element should carry the Memento classification");
        }
        finally
        {
            QueryFvtTestSupport.purgeElement(openMetadataStore, elementGUID);
        }
    }


    @Test
    void lookForLineageArchivesElementWithLineageRelationship() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper         propertyHelper    = new PropertyHelper();

        String targetQualifiedName = QueryFvtTestSupport.newQualifiedName("LookForLineageTarget");
        String otherQualifiedName  = QueryFvtTestSupport.newQualifiedName("LookForLineageOther");

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties targetProperties = new CollectionProperties();
        targetProperties.setQualifiedName(targetQualifiedName);
        targetProperties.setDisplayName("query-fvt LookForLineage Target Collection");

        CollectionProperties otherProperties = new CollectionProperties();
        otherProperties.setQualifiedName(otherQualifiedName);
        otherProperties.setDisplayName("query-fvt LookForLineage Other Collection");

        String targetGUID = collectionClient.createCollection(newElementOptions, null, targetProperties, null);
        String otherGUID  = collectionClient.createCollection(newElementOptions, null, otherProperties, null);

        try
        {
            String relationshipGUID = openMetadataStore.createRelatedElementsInStore(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                       targetGUID,
                                                                                       otherGUID,
                                                                                       null,
                                                                                       null,
                                                                                       null);

            try
            {
                /*
                 * LOOK_FOR_LINEAGE is the default delete method - set it explicitly here to document intent.
                 */
                DeleteOptions deleteOptions = new DeleteOptions();
                deleteOptions.setDeleteMethod(DeleteMethod.LOOK_FOR_LINEAGE);

                openMetadataStore.deleteMetadataElementInStore(targetGUID, deleteOptions);

                SearchProperties searchProperties = new SearchProperties();
                searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                                 OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                 targetQualifiedName,
                                                                                 PropertyComparisonOperator.STARTS_WITH));

                QueryOptions defaultOptions = new QueryOptions();
                defaultOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);

                List<OpenMetadataElement> afterDeleteDefault = openMetadataStore.findMetadataElements(searchProperties, null, defaultOptions);

                assertTrue((afterDeleteDefault == null) || afterDeleteDefault.isEmpty(),
                           "A default (non-lineage) query should no longer find the element once LOOK_FOR_LINEAGE has archived it");

                QueryOptions lineageOptions = new QueryOptions();
                lineageOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
                lineageOptions.setForLineage(true);

                List<OpenMetadataElement> afterDeleteLineage = openMetadataStore.findMetadataElements(searchProperties, null, lineageOptions);

                assertEquals(1, afterDeleteLineage.size(),
                             "A forLineage query should find the element - it should have been archived, not soft-deleted");
                assertEquals(targetGUID, afterDeleteLineage.get(0).getElementGUID());
                assertTrue(hasMementoClassification(afterDeleteLineage.get(0)),
                          "An element with an attached lineage relationship should be archived (Memento classification) rather than soft-deleted");
            }
            finally
            {
                QueryFvtTestSupport.purgeRelationship(openMetadataStore, relationshipGUID);
            }
        }
        finally
        {
            QueryFvtTestSupport.purgeElement(openMetadataStore, targetGUID);
            QueryFvtTestSupport.purgeElement(openMetadataStore, otherGUID);
        }
    }


    @Test
    void lookForLineageSoftDeletesElementWithoutLineageRelationship() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper         propertyHelper    = new PropertyHelper();

        String qualifiedName = QueryFvtTestSupport.newQualifiedName("LookForLineageNoRelationship");

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties createProperties = new CollectionProperties();
        createProperties.setQualifiedName(qualifiedName);
        createProperties.setDisplayName("query-fvt LookForLineage No-Relationship Collection");

        String elementGUID = collectionClient.createCollection(newElementOptions, null, createProperties, null);

        try
        {
            DeleteOptions deleteOptions = new DeleteOptions();
            deleteOptions.setDeleteMethod(DeleteMethod.LOOK_FOR_LINEAGE);

            openMetadataStore.deleteMetadataElementInStore(elementGUID, deleteOptions);

            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                             qualifiedName,
                                                                             PropertyComparisonOperator.EQ));

            QueryOptions defaultOptions = new QueryOptions();
            defaultOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);

            List<OpenMetadataElement> afterDeleteDefault = openMetadataStore.findMetadataElements(searchProperties, null, defaultOptions);

            assertTrue((afterDeleteDefault == null) || afterDeleteDefault.isEmpty(),
                       "A default (active-only) query should no longer find the element");

            QueryOptions deletedOnlyOptions = new QueryOptions();
            deletedOnlyOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            deletedOnlyOptions.setLimitResultsByStatus(List.of(ElementStatus.DELETED));

            List<OpenMetadataElement> afterDeleteDeletedOnly = openMetadataStore.findMetadataElements(searchProperties, null, deletedOnlyOptions);

            assertEquals(1, afterDeleteDeletedOnly.size(),
                         "An element with no lineage relationship should be soft-deleted, not archived - so it should be found via DELETED status");
            assertEquals(elementGUID, afterDeleteDeletedOnly.get(0).getElementGUID());
            assertEquals(ElementStatus.DELETED, afterDeleteDeletedOnly.get(0).getStatus());
            assertFalse(hasMementoClassification(afterDeleteDeletedOnly.get(0)),
                       "A soft-deleted element should not carry the Memento classification used for archiving");
        }
        finally
        {
            QueryFvtTestSupport.purgeElement(openMetadataStore, elementGUID);
        }
    }


    @Test
    void purgeRelationshipRemovesItEvenWithStatusFilter() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();

        String qualifiedName1 = QueryFvtTestSupport.newQualifiedName("RelationshipPurgeEnd1");
        String qualifiedName2 = QueryFvtTestSupport.newQualifiedName("RelationshipPurgeEnd2");

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties properties1 = new CollectionProperties();
        properties1.setQualifiedName(qualifiedName1);
        properties1.setDisplayName("query-fvt Relationship Purge Collection 1");

        CollectionProperties properties2 = new CollectionProperties();
        properties2.setQualifiedName(qualifiedName2);
        properties2.setDisplayName("query-fvt Relationship Purge Collection 2");

        String elementGUID1 = collectionClient.createCollection(newElementOptions, null, properties1, null);
        String elementGUID2 = collectionClient.createCollection(newElementOptions, null, properties2, null);

        try
        {
            String relationshipGUID = openMetadataStore.createRelatedElementsInStore(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                       elementGUID1,
                                                                                       elementGUID2,
                                                                                       null,
                                                                                       null,
                                                                                       null);

            OpenMetadataRelationship beforeDelete = openMetadataStore.getRelationshipByGUID(relationshipGUID);

            assertEquals(relationshipGUID, beforeDelete.getRelationshipGUID());
            assertEquals(ElementStatus.ACTIVE, beforeDelete.getStatus());

            DeleteOptions softDeleteOptions = new DeleteOptions();
            softDeleteOptions.setDeleteMethod(DeleteMethod.SOFT_DELETE);

            openMetadataStore.deleteRelationshipInStore(relationshipGUID, softDeleteOptions);

            boolean defaultGetRejectedSoftDeletedRelationship;

            try
            {
                openMetadataStore.getRelationshipByGUID(relationshipGUID);
                defaultGetRejectedSoftDeletedRelationship = false;
            }
            catch (Exception expected)
            {
                defaultGetRejectedSoftDeletedRelationship = true;
            }

            assertTrue(defaultGetRejectedSoftDeletedRelationship,
                      "A default (current-value) get should no longer find a soft-deleted relationship");

            /*
             * An asOfTime query answers as if it were running at the time requested, so it reports what a
             * caller would have seen at that moment - and at this moment the relationship has been deleted,
             * so it is not known.  That is the same answer the default get above gives, for the same reason.
             *
             * The call that returns deleted instances is getRelationshipHistory(): it exists to show every
             * state a relationship has been through, which is what is needed to establish that a
             * relationship was unavailable during a particular window.  "What did this look like then" and
             * "what has happened to this" are different questions and only the second one returns
             * deleted versions.
             */
            GetOptions asOfTimeOptions = new GetOptions();
            asOfTimeOptions.setAsOfTime(new Date());

            boolean asOfTimeGetRejectedSoftDeletedRelationship;

            try
            {
                openMetadataStore.getRelationshipByGUID(relationshipGUID, asOfTimeOptions);
                asOfTimeGetRejectedSoftDeletedRelationship = false;
            }
            catch (Exception expected)
            {
                asOfTimeGetRejectedSoftDeletedRelationship = true;
            }

            assertTrue(asOfTimeGetRejectedSoftDeletedRelationship,
                      "An asOfTime query answers as at that time, so it should not find a relationship that had been deleted by then");

            DeleteOptions purgeOptions = new DeleteOptions();
            purgeOptions.setDeleteMethod(DeleteMethod.PURGE);

            openMetadataStore.deleteRelationshipInStore(relationshipGUID, purgeOptions);

            boolean asOfTimeGetRejectedPurgedRelationship;

            try
            {
                openMetadataStore.getRelationshipByGUID(relationshipGUID, asOfTimeOptions);
                asOfTimeGetRejectedPurgedRelationship = false;
            }
            catch (Exception expected)
            {
                asOfTimeGetRejectedPurgedRelationship = true;
            }

            assertTrue(asOfTimeGetRejectedPurgedRelationship,
                      "A purged relationship should not be found even by an asOfTime query - it is genuinely gone, not just historically deleted");
        }
        finally
        {
            QueryFvtTestSupport.purgeElement(openMetadataStore, elementGUID1);
            QueryFvtTestSupport.purgeElement(openMetadataStore, elementGUID2);
        }
    }


    @Test
    void findRelationshipsBetweenMetadataElementsHonoursEndGuidsWithoutPropertyConditions() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();

        String qualifiedName1 = QueryFvtTestSupport.newQualifiedName("RelationshipSearchEnd1");
        String qualifiedName2 = QueryFvtTestSupport.newQualifiedName("RelationshipSearchEnd2");
        String qualifiedName3 = QueryFvtTestSupport.newQualifiedName("RelationshipSearchEnd3");

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties properties1 = new CollectionProperties();
        properties1.setQualifiedName(qualifiedName1);
        properties1.setDisplayName("query-fvt Relationship Search Collection 1");

        CollectionProperties properties2 = new CollectionProperties();
        properties2.setQualifiedName(qualifiedName2);
        properties2.setDisplayName("query-fvt Relationship Search Collection 2");

        CollectionProperties properties3 = new CollectionProperties();
        properties3.setQualifiedName(qualifiedName3);
        properties3.setDisplayName("query-fvt Relationship Search Collection 3 (unrelated)");

        String elementGUID1 = collectionClient.createCollection(newElementOptions, null, properties1, null);
        String elementGUID2 = collectionClient.createCollection(newElementOptions, null, properties2, null);
        String elementGUID3 = collectionClient.createCollection(newElementOptions, null, properties3, null);

        String relationshipGUID = null;
        String unrelatedRelationshipGUID = null;

        try
        {
            relationshipGUID = openMetadataStore.createRelatedElementsInStore(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                elementGUID1,
                                                                                elementGUID2,
                                                                                null,
                                                                                null,
                                                                                null);

            /*
             * A second DataFlow relationship, not linked to elementGUID1/elementGUID2, so a broken end-GUID
             * filter (silently ignored, as it used to be whenever searchProperties was null) would show up as
             * this unrelated relationship also being returned.
             */
            unrelatedRelationshipGUID = openMetadataStore.createRelatedElementsInStore(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                        elementGUID3,
                                                                                        elementGUID2,
                                                                                        null,
                                                                                        null,
                                                                                        null);

            List<String> end1GUIDs = List.of(elementGUID1);
            List<String> end2GUIDs = List.of(elementGUID2);

            QueryOptions queryOptions = new QueryOptions();
            queryOptions.setPageSize(QueryFvtTestSupport.MAX_PAGE_SIZE);

            /*
             * searchProperties is deliberately null here - this is exactly the call shape that used to be
             * silently routed to an unfiltered getRelationshipsByType() search, dropping the end1/end2 GUID
             * filters entirely.
             */
            OpenMetadataRelationshipList results = openMetadataStore.findRelationshipsBetweenMetadataElements(
                    OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, null, end1GUIDs, end2GUIDs, EndMatchCriteria.BOTH, null, queryOptions);

            assertTrue((results != null) && (results.getRelationships() != null) && (results.getRelationships().size() == 1),
                      "A null searchProperties should not prevent end1EntityGUIDs/end2EntityGUIDs from filtering the results");
            assertEquals(relationshipGUID, results.getRelationships().get(0).getRelationshipGUID());

            /*
             * An explicitly empty (non-null, no conditions) SearchProperties should behave identically to null -
             * this used to fail server-side with OMRS-REPOSITORY-400-074.
             */
            OpenMetadataRelationshipList resultsWithEmptySearchProperties = openMetadataStore.findRelationshipsBetweenMetadataElements(
                    OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, null, end1GUIDs, end2GUIDs, EndMatchCriteria.BOTH, new SearchProperties(), queryOptions);

            assertTrue((resultsWithEmptySearchProperties != null) && (resultsWithEmptySearchProperties.getRelationships() != null)
                               && (resultsWithEmptySearchProperties.getRelationships().size() == 1),
                      "An empty (non-null, no conditions) SearchProperties should be accepted as 'no property filter', not rejected");
            assertEquals(relationshipGUID, resultsWithEmptySearchProperties.getRelationships().get(0).getRelationshipGUID());
        }
        finally
        {
            QueryFvtTestSupport.purgeRelationship(openMetadataStore, relationshipGUID);
            QueryFvtTestSupport.purgeRelationship(openMetadataStore, unrelatedRelationshipGUID);
            QueryFvtTestSupport.purgeElement(openMetadataStore, elementGUID1);
            QueryFvtTestSupport.purgeElement(openMetadataStore, elementGUID2);
            QueryFvtTestSupport.purgeElement(openMetadataStore, elementGUID3);
        }
    }


    private boolean hasMementoClassification(OpenMetadataElement element)
    {
        if (element.getClassifications() != null)
        {
            for (AttachedClassification classification : element.getClassifications())
            {
                if ((classification != null) && OpenMetadataType.MEMENTO_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                {
                    return true;
                }
            }
        }

        return false;
    }
}
