/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.queryfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * StatusFVT checks that a soft-deleted element - one of this test's own elements, since content-pack
 * archive elements are read-only - disappears from the default (active-only) view of
 * {@code OpenMetadataStore.findMetadataElements} but can still be found by explicitly widening
 * {@code QueryOptions.limitResultsByStatus} to include {@code ElementStatus.DELETED}.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class StatusFVT
{
    @Test
    void softDeletedElementOnlyFoundWithStatusFilter() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper         propertyHelper    = new PropertyHelper();

        String qualifiedName = QueryFvtTestSupport.newQualifiedName("Status");

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties createProperties = new CollectionProperties();
        createProperties.setQualifiedName(qualifiedName);
        createProperties.setDisplayName("query-fvt Status Collection");

        String elementGUID = collectionClient.createCollection(newElementOptions, null, createProperties, null);

        try
        {
            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                             qualifiedName,
                                                                             PropertyComparisonOperator.EQ));

            QueryOptions defaultOptions = new QueryOptions();
            defaultOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);

            List<OpenMetadataElement> beforeDelete = openMetadataStore.findMetadataElements(searchProperties, null, defaultOptions);

            assertEquals(1, beforeDelete.size(), "The newly created, still-active element should be found by a default (active-only) query");
            assertEquals(elementGUID, beforeDelete.get(0).getElementGUID());

            DeleteOptions deleteOptions = new DeleteOptions();
            deleteOptions.setDeleteMethod(DeleteMethod.SOFT_DELETE);
            deleteOptions.setCascadedDelete(true);

            collectionClient.deleteCollection(elementGUID, deleteOptions);

            List<OpenMetadataElement> afterDeleteDefault = openMetadataStore.findMetadataElements(searchProperties, null, defaultOptions);

            assertTrue((afterDeleteDefault == null) || afterDeleteDefault.isEmpty(),
                       "A default (active-only) query should no longer find the element once it has been soft-deleted");

            QueryOptions activeOnlyOptions = new QueryOptions();
            activeOnlyOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            activeOnlyOptions.setLimitResultsByStatus(List.of(ElementStatus.ACTIVE));

            List<OpenMetadataElement> afterDeleteActiveOnly = openMetadataStore.findMetadataElements(searchProperties, null, activeOnlyOptions);

            assertTrue((afterDeleteActiveOnly == null) || afterDeleteActiveOnly.isEmpty(),
                       "Explicitly asking for ACTIVE-only should also no longer find the soft-deleted element");

            QueryOptions deletedOnlyOptions = new QueryOptions();
            deletedOnlyOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            deletedOnlyOptions.setLimitResultsByStatus(List.of(ElementStatus.DELETED));

            List<OpenMetadataElement> afterDeleteDeletedOnly = openMetadataStore.findMetadataElements(searchProperties, null, deletedOnlyOptions);

            assertEquals(1, afterDeleteDeletedOnly.size(),
                         "limitResultsByStatus=[DELETED] should still find the soft-deleted element");
            assertEquals(elementGUID, afterDeleteDeletedOnly.get(0).getElementGUID());
            assertEquals(ElementStatus.DELETED, afterDeleteDeletedOnly.get(0).getStatus(),
                         "The returned element's own status should now report DELETED");

            QueryOptions bothStatusesOptions = new QueryOptions();
            bothStatusesOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            bothStatusesOptions.setLimitResultsByStatus(List.of(ElementStatus.ACTIVE, ElementStatus.DELETED));

            List<OpenMetadataElement> afterDeleteBothStatuses = openMetadataStore.findMetadataElements(searchProperties, null, bothStatusesOptions);

            assertEquals(1, afterDeleteBothStatuses.size(),
                         "limitResultsByStatus=[ACTIVE, DELETED] should still find the one (now deleted) element");
        }
        finally
        {
            QueryFvtTestSupport.purgeElement(openMetadataStore, elementGUID);
        }
    }


    @Test
    void purgedElementIsGoneEvenWithStatusFilter() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper         propertyHelper    = new PropertyHelper();

        String qualifiedName = QueryFvtTestSupport.newQualifiedName("StatusPurge");

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties createProperties = new CollectionProperties();
        createProperties.setQualifiedName(qualifiedName);
        createProperties.setDisplayName("query-fvt Status Purge Collection");

        String elementGUID = collectionClient.createCollection(newElementOptions, null, createProperties, null);

        QueryFvtTestSupport.purgeElement(openMetadataStore, elementGUID);

        SearchProperties searchProperties = new SearchProperties();
        searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                         qualifiedName,
                                                                         PropertyComparisonOperator.EQ));

        QueryOptions anyStatusOptions = new QueryOptions();
        anyStatusOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
        anyStatusOptions.setLimitResultsByStatus(List.of(ElementStatus.values()));

        List<OpenMetadataElement> afterPurge = openMetadataStore.findMetadataElements(searchProperties, null, anyStatusOptions);

        assertFalse((afterPurge != null) && (!afterPurge.isEmpty()),
                    "A purged element should not be found even when every status is included in the search");
    }
}
