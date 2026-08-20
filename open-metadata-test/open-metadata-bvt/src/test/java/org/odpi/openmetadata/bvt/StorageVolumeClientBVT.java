/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.StorageVolumeClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.AttachedStorageProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.StorageVolumeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.StoredOnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
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
 * StorageVolumeClientBVT exercises the create/get/update/find/delete lifecycle of {@link StorageVolumeClient}, one of the connector
 * context clients, against the running BVT server, together with its AttachedStorage and StoredOn relationships.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class StorageVolumeClientBVT
{
    @Test
    void storageVolumeLifecycle() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        StorageVolumeClient client = connectorContext.getStorageVolumeClient();

        String qualifiedName = "open-metadata-bvt:StorageVolume:" + UUID.randomUUID();

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        StorageVolumeProperties createProperties = new StorageVolumeProperties();
        createProperties.setQualifiedName(qualifiedName);
        createProperties.setDisplayName("BVT StorageVolume");
        createProperties.setDescription("Created by the open-metadata-bvt build verification test suite");

        String elementGUID = client.createStorageVolume(newElementOptions, null, createProperties, null);

        assertNotNull(elementGUID, "createStorageVolume should return a GUID");

        OpenMetadataRootElement retrievedElement = client.getStorageVolumeByGUID(elementGUID, new GetOptions());

        assertNotNull(retrievedElement, "getStorageVolumeByGUID should find the element that was just created");
        assertEquals(elementGUID, retrievedElement.getElementHeader().getGUID());
        assertEquals(qualifiedName, ((StorageVolumeProperties) retrievedElement.getProperties()).getQualifiedName());

        StorageVolumeProperties updateProperties = new StorageVolumeProperties();
        updateProperties.setQualifiedName(qualifiedName);
        updateProperties.setDisplayName("BVT StorageVolume (updated)");

        boolean updateOccurred = client.updateStorageVolume(elementGUID, new UpdateOptions(), updateProperties);

        assertTrue(updateOccurred, "updateStorageVolume should report that an update occurred");

        OpenMetadataRootElement updatedElement = client.getStorageVolumeByGUID(elementGUID, new GetOptions());

        assertEquals("BVT StorageVolume (updated)", ((StorageVolumeProperties) updatedElement.getProperties()).getDisplayName());

        List<OpenMetadataRootElement> foundElements = client.findStorageVolumes(qualifiedName, new SearchOptions());

        assertNotNull(foundElements, "findStorageVolumes should return a (possibly empty) list, not null");
        assertTrue(foundElements.stream().anyMatch(element -> elementGUID.equals(element.getElementHeader().getGUID())),
                   "findStorageVolumes should find the element by its qualified name");

        List<OpenMetadataRootElement> namedElements = client.getStorageVolumesByName(qualifiedName, null);

        assertNotNull(namedElements, "getStorageVolumesByName should return a (possibly empty) list, not null");

        client.deleteStorageVolume(elementGUID, new DeleteOptions());

        assertThrows(Exception.class,
                     () -> client.getStorageVolumeByGUID(elementGUID, new GetOptions()),
                     "getStorageVolumeByGUID should no longer find the element after it has been deleted");
    }


    @Test
    void storageVolumeRelationships() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        StorageVolumeClient  client           = connectorContext.getStorageVolumeClient();

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        StorageVolumeProperties volumeOne = new StorageVolumeProperties();
        volumeOne.setQualifiedName("open-metadata-bvt:StorageVolume:" + UUID.randomUUID());
        volumeOne.setDisplayName("BVT StorageVolume - attached storage");

        StorageVolumeProperties volumeTwo = new StorageVolumeProperties();
        volumeTwo.setQualifiedName("open-metadata-bvt:StorageVolume:" + UUID.randomUUID());
        volumeTwo.setDisplayName("BVT StorageVolume - stored on");

        String volumeOneGUID = client.createStorageVolume(newElementOptions, null, volumeOne, null);
        String volumeTwoGUID = client.createStorageVolume(newElementOptions, null, volumeTwo, null);

        /*
         * AttachedStorage and StoredOn are both uni-link relationships so the attach/detach pair is
         * addressed by the two end GUIDs.
         */
        client.linkAttachedStorage(volumeOneGUID, volumeTwoGUID, new MakeAnchorOptions(), new AttachedStorageProperties());
        client.detachAttachedStorage(volumeOneGUID, volumeTwoGUID, new DeleteOptions());

        /*
         * StoredOn runs from a DataStore to a StorageVolume, so end 1 has to be a data store rather than
         * another volume.
         */
        AssetProperties dataStoreProperties = new AssetProperties();
        dataStoreProperties.setTypeName("DataStore");
        dataStoreProperties.setQualifiedName("open-metadata-bvt:DataStore:" + UUID.randomUUID());
        dataStoreProperties.setDisplayName("BVT DataStore");

        String dataStoreGUID = connectorContext.getAssetClient().createAsset(newElementOptions, null, dataStoreProperties, null);

        StoredOnProperties storedOnProperties = new StoredOnProperties();
        storedOnProperties.setLabel("BVT stored on");

        client.linkStoredOn(dataStoreGUID, volumeTwoGUID, new MakeAnchorOptions(), storedOnProperties);
        client.detachStoredOn(dataStoreGUID, volumeTwoGUID, new DeleteOptions());

        connectorContext.getAssetClient().deleteAsset(dataStoreGUID, new DeleteOptions());

        client.deleteStorageVolume(volumeOneGUID, new DeleteOptions());
        client.deleteStorageVolume(volumeTwoGUID, new DeleteOptions());
    }
}
