/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CollectionClientBVT exercises the create/get/update/find/delete lifecycle of
 * {@link CollectionClient}, one of the connector context clients, against the running BVT server.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class CollectionClientBVT
{
    @Test
    void collectionLifecycle() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient = connectorContext.getCollectionClient();

        String qualifiedName = "open-metadata-bvt:Collection:" + UUID.randomUUID();

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        CollectionProperties createProperties = new CollectionProperties();
        createProperties.setQualifiedName(qualifiedName);
        createProperties.setDisplayName("BVT Collection");
        createProperties.setDescription("Created by the open-metadata-bvt build verification test suite");

        String collectionGUID = collectionClient.createCollection(newElementOptions, null, createProperties, null);

        assertNotNull(collectionGUID, "createCollection should return a GUID");

        OpenMetadataRootElement retrievedElement = collectionClient.getCollectionByGUID(collectionGUID, new GetOptions());

        assertNotNull(retrievedElement, "getCollectionByGUID should find the collection that was just created");
        assertEquals(collectionGUID, retrievedElement.getElementHeader().getGUID());
        assertEquals(qualifiedName, ((CollectionProperties) retrievedElement.getProperties()).getQualifiedName());
        assertEquals("BVT Collection", ((CollectionProperties) retrievedElement.getProperties()).getDisplayName());

        CollectionProperties updateProperties = new CollectionProperties();
        updateProperties.setQualifiedName(qualifiedName);
        updateProperties.setDisplayName("BVT Collection (updated)");
        updateProperties.setDescription("Updated by the open-metadata-bvt build verification test suite");

        boolean updateOccurred = collectionClient.updateCollection(collectionGUID, new UpdateOptions(), updateProperties);

        assertTrue(updateOccurred, "updateCollection should report that an update occurred");

        OpenMetadataRootElement updatedElement = collectionClient.getCollectionByGUID(collectionGUID, new GetOptions());

        assertEquals("BVT Collection (updated)", ((CollectionProperties) updatedElement.getProperties()).getDisplayName());

        List<OpenMetadataRootElement> foundElements = collectionClient.findCollections(qualifiedName, new SearchOptions());

        assertNotNull(foundElements, "findCollections should return a (possibly empty) list, not null");
        assertTrue(foundElements.stream().anyMatch(element -> collectionGUID.equals(element.getElementHeader().getGUID())),
                   "findCollections should find the collection by its qualified name");

        collectionClient.deleteCollection(collectionGUID, new DeleteOptions());

        assertThrows(Exception.class,
                     () -> collectionClient.getCollectionByGUID(collectionGUID, new GetOptions()),
                     "getCollectionByGUID should no longer find the collection after it has been deleted");
    }
}
