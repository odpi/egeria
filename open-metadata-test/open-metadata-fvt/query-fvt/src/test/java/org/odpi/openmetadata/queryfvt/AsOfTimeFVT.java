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
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AsOfTimeFVT checks that the PostgreSQL repository connector's bi-temporal support genuinely lets a
 * query see the repository as it was at an earlier moment in time: an element is created, updated and
 * then soft-deleted, each separated by a recorded checkpoint time, and queries using
 * {@code asOfTime} set to each checkpoint should reproduce the element's state (properties, and whether
 * it existed/was active at all) as it was at that moment - not its current state.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class AsOfTimeFVT
{
    /**
     * Appended to the assertions that check a search found nothing.  Null is the end of a result set;
     * an empty list means only that this batch was filtered out, so a caller that honours the paging
     * contract keeps asking.  Accepting either here would let a regression from one to the other -
     * which would make every such caller page for ever - pass unnoticed.
     */
    private static final String NOTHING_MEANS_NULL =
            ".  A search that matches nothing returns null, not an empty list";


    @Test
    void asOfTimeReproducesHistoricalState() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper         propertyHelper    = new PropertyHelper();

        String qualifiedName = QueryFvtTestSupport.newQualifiedName("AsOfTime");

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties createProperties = new CollectionProperties();
        createProperties.setQualifiedName(qualifiedName);
        createProperties.setDisplayName("query-fvt AsOfTime Collection (version 1)");

        String elementGUID = collectionClient.createCollection(newElementOptions, null, createProperties, null);

        try
        {
            Thread.sleep(250);
            Date afterCreateCheckpoint = new Date();
            Thread.sleep(250);

            CollectionProperties updateProperties = new CollectionProperties();
            updateProperties.setQualifiedName(qualifiedName);
            updateProperties.setDisplayName("query-fvt AsOfTime Collection (version 2)");
            collectionClient.updateCollection(elementGUID, new UpdateOptions(), updateProperties);

            Thread.sleep(250);
            Date afterUpdateCheckpoint = new Date();
            Thread.sleep(250);

            DeleteOptions deleteOptions = new DeleteOptions();
            deleteOptions.setDeleteMethod(DeleteMethod.SOFT_DELETE);
            deleteOptions.setCascadedDelete(true);
            collectionClient.deleteCollection(elementGUID, deleteOptions);

            Thread.sleep(250);
            Date afterDeleteCheckpoint = new Date();

            // As of a time before the update: should see version 1's properties.
            GetOptions beforeUpdateGetOptions = new GetOptions();
            beforeUpdateGetOptions.setAsOfTime(afterCreateCheckpoint);

            OpenMetadataElement beforeUpdateElement = openMetadataStore.getMetadataElementByGUID(elementGUID, beforeUpdateGetOptions);

            assertEquals("query-fvt AsOfTime Collection (version 1)", getDisplayName(propertyHelper, beforeUpdateElement),
                         "asOfTime set to just after creation should show the original displayName, not the later update");

            // As of a time after the update but before the delete: should see version 2's properties, still active.
            GetOptions afterUpdateGetOptions = new GetOptions();
            afterUpdateGetOptions.setAsOfTime(afterUpdateCheckpoint);

            OpenMetadataElement afterUpdateElement = openMetadataStore.getMetadataElementByGUID(elementGUID, afterUpdateGetOptions);

            assertEquals("query-fvt AsOfTime Collection (version 2)", getDisplayName(propertyHelper, afterUpdateElement),
                         "asOfTime set to just after the update should show the updated displayName");

            // A search, as of that same "after update, before delete" time, should still find the element -
            // at that point in history it was active.
            SearchProperties searchProperties = new SearchProperties();
            searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                             qualifiedName,
                                                                             PropertyComparisonOperator.EQ));

            QueryOptions afterUpdateQueryOptions = new QueryOptions();
            afterUpdateQueryOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            afterUpdateQueryOptions.setAsOfTime(afterUpdateCheckpoint);

            List<OpenMetadataElement> afterUpdateResults = openMetadataStore.findMetadataElements(searchProperties, null, afterUpdateQueryOptions);

            assertEquals(1, afterUpdateResults.size(),
                         "A default (active-only) search as of a time before the delete should still find the element");

            // As of a time after the delete, a default (active-only) search should no longer find it.
            QueryOptions afterDeleteQueryOptions = new QueryOptions();
            afterDeleteQueryOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            afterDeleteQueryOptions.setAsOfTime(afterDeleteCheckpoint);

            List<OpenMetadataElement> afterDeleteResults = openMetadataStore.findMetadataElements(searchProperties, null, afterDeleteQueryOptions);

            assertNull(afterDeleteResults,
                       "A default (active-only) search as of a time after the delete should no longer find the element"
                               + NOTHING_MEANS_NULL);
        }
        finally
        {
            QueryFvtTestSupport.purgeElement(openMetadataStore, elementGUID);
        }
    }


    private String getDisplayName(PropertyHelper propertyHelper, OpenMetadataElement element)
    {
        return propertyHelper.getStringProperty("query-fvt",
                                                 OpenMetadataProperty.DISPLAY_NAME.name,
                                                 element.getElementProperties(),
                                                 "getDisplayName");
    }
}
