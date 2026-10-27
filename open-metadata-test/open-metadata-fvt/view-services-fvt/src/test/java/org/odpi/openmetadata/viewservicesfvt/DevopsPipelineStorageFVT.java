/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.AttachedStorageProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.HostProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.StorageVolumeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.StoredOnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.DevopsPipelineFvtSupport.*;

/**
 * DevopsPipelineStorageFVT covers the Devops Pipeline OMVS storage volume requests: the storage volume lifecycle
 * (create, create from template, update, retrieve, by-name, search, delete) and its two relationships -
 * AttachedStorage from a piece of IT infrastructure (a Host made by Asset Maker) and StoredOn from a data store
 * (a DataFile made by Asset Maker).
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DevopsPipelineStorageFVT
{
    private static String storageVolumeGUID = null;
    private static String hostGUID          = null;
    private static String dataFileGUID      = null;


    /**
     * A new storage volume can be retrieved with the properties it was created with.
     */
    @Test
    @Order(1)
    void createAndRetrieveStorageVolume()
    {
        StorageVolumeProperties properties = named(new StorageVolumeProperties(), "StorageVolume", "volume-1");

        properties.setStorageCapacity(500L);
        properties.setUnits("GB");

        storageVolumeGUID = devops.create("/storage-volumes", newElement(properties));

        OpenMetadataRootElement element = retrieve(devops, "/storage-volumes/" + storageVolumeGUID + "/retrieve");

        assertEquals(OpenMetadataType.STORAGE_VOLUME.typeName, element.getElementHeader().getType().getTypeName());
        assertTrue(element.getProperties() instanceof StorageVolumeProperties, "retrieve returned " + element.getProperties());

        StorageVolumeProperties retrieved = (StorageVolumeProperties) element.getProperties();

        assertEquals(properties.getQualifiedName(), retrieved.getQualifiedName());
        assertEquals(500, retrieved.getStorageCapacity());
        assertEquals("GB", retrieved.getUnits());
    }


    /**
     * A merge update changes only the properties it supplies.
     */
    @Test
    @Order(2)
    void updateStorageVolume()
    {
        StorageVolumeProperties properties = new StorageVolumeProperties();

        properties.setDescription("Updated by the Devops Pipeline FVT");
        properties.setStorageCapacity(750L);

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        assertTrue(devops.post("/storage-volumes/" + storageVolumeGUID + "/update", requestBody, BooleanResponse.class).getFlag(),
                   "updateStorageVolume reported that nothing was updated");

        StorageVolumeProperties retrieved = (StorageVolumeProperties) retrieve(devops, "/storage-volumes/" + storageVolumeGUID + "/retrieve").getProperties();

        assertEquals("Updated by the Devops Pipeline FVT", retrieved.getDescription());
        assertEquals(750, retrieved.getStorageCapacity());
        assertEquals("GB", retrieved.getUnits(), "a merge update should keep the properties it did not supply");
    }


    /**
     * The storage volume is found by its exact name and by a search string, and a search for this run's elements
     * does not return elements of other types.
     */
    @Test
    @Order(3)
    void findStorageVolume()
    {
        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(ViewServicesFvtTestSupport.qualifiedName("StorageVolume", "volume-1"));

        List<OpenMetadataRootElement> named = devops.elements("/storage-volumes/by-name", byName);

        assertEquals(1, named.size(), "by-name should return exactly the one storage volume: " + named);
        assertEquals(storageVolumeGUID, named.get(0).getElementHeader().getGUID());

        SearchStringRequestBody search = new SearchStringRequestBody();

        search.setSearchString(ViewServicesFvtTestSupport.runSearchString() + "::StorageVolume");

        List<OpenMetadataRootElement> found = devops.elements("/storage-volumes/by-search-string", search);

        assertTrue(contains(found, storageVolumeGUID), "findStorageVolumes did not return " + storageVolumeGUID);

        for (OpenMetadataRootElement element : found)
        {
            assertEquals(OpenMetadataType.STORAGE_VOLUME.typeName, element.getElementHeader().getType().getTypeName(),
                         "findStorageVolumes returned an element that is not a storage volume");
        }
    }


    /**
     * A storage volume made from the first one as a template is a new storage volume with the replacement
     * qualified name and the template's other properties.
     */
    @Test
    @Order(4)
    void createStorageVolumeFromTemplate()
    {
        StorageVolumeProperties replacement = new StorageVolumeProperties();

        replacement.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("StorageVolume", "from-template"));

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(storageVolumeGUID);
        requestBody.setIsOwnAnchor(true);
        requestBody.setReplacementProperties(replacement);

        String copyGUID = devops.create("/storage-volumes/from-template", requestBody);

        assertNotEquals(storageVolumeGUID, copyGUID);

        OpenMetadataRootElement copy = retrieve(devops, "/storage-volumes/" + copyGUID + "/retrieve");

        assertEquals(OpenMetadataType.STORAGE_VOLUME.typeName, copy.getElementHeader().getType().getTypeName());
        assertEquals(replacement.getQualifiedName(), qualifiedName(copy));
        assertEquals("GB", ((StorageVolumeProperties) copy.getProperties()).getUnits(), "the template's units should be copied");

        devops.call("/storage-volumes/" + copyGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(devops, "/storage-volumes/" + copyGUID + "/retrieve");
    }


    /**
     * AttachedStorage links a host to the storage volume; it is visible from both ends until it is detached.
     */
    @Test
    @Order(5)
    void attachedStorage()
    {
        hostGUID = createAsset(named(new HostProperties(), "Host", "storage-host"));

        String path = "/it-infrastructure/" + hostGUID + "/attached-storage/" + storageVolumeGUID;

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(new AttachedStorageProperties());

        devops.call(path + "/attach", requestBody);

        String relationship = OpenMetadataType.ATTACHED_STORAGE_RELATIONSHIP.typeName;

        assertEquals(1, related(retrieve(devops, "/storage-volumes/" + storageVolumeGUID + "/retrieve"), relationship, hostGUID).size(),
                     "the storage volume does not show the host it is attached to");
        assertEquals(1, related(asset(hostGUID), relationship, storageVolumeGUID).size(),
                     "the host does not show its attached storage volume");

        devops.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(related(retrieve(devops, "/storage-volumes/" + storageVolumeGUID + "/retrieve"), relationship, hostGUID).isEmpty(),
                   "the storage volume is still attached to the host after detach");
        assertTrue(related(asset(hostGUID), relationship, storageVolumeGUID).isEmpty(),
                   "the host still shows the storage volume after detach");
    }


    /**
     * StoredOn links a data store to the storage volume, with its label; it is visible from both ends until it is
     * detached.
     */
    @Test
    @Order(6)
    void storedOn()
    {
        DataFileProperties dataFileProperties = named(new DataFileProperties(), "DataFile", "stored-on");

        dataFileProperties.setPathName("/data/view-services-fvt/" + OMAGPlatformExtension.RUN_ID + "/stored-on.csv");

        dataFileGUID = createAsset(dataFileProperties);

        String path = "/data-stores/" + dataFileGUID + "/stored-on/" + storageVolumeGUID;

        StoredOnProperties storedOnProperties = new StoredOnProperties();

        storedOnProperties.setLabel("primary copy");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(storedOnProperties);

        devops.call(path + "/attach", requestBody);

        String relationship = OpenMetadataType.STORED_ON_RELATIONSHIP.typeName;

        List<RelatedMetadataElementSummary> fromVolume = related(retrieve(devops, "/storage-volumes/" + storageVolumeGUID + "/retrieve"), relationship, dataFileGUID);

        assertEquals(1, fromVolume.size(), "the storage volume does not show the data file stored on it");
        assertTrue(fromVolume.get(0).getRelationshipProperties() instanceof StoredOnProperties properties &&
                           "primary copy".equals(properties.getLabel()),
                   "the StoredOn label was not saved: " + fromVolume.get(0).getRelationshipProperties());
        assertEquals(1, related(asset(dataFileGUID), relationship, storageVolumeGUID).size(),
                     "the data file does not show the storage volume it is stored on");

        devops.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(related(retrieve(devops, "/storage-volumes/" + storageVolumeGUID + "/retrieve"), relationship, dataFileGUID).isEmpty(),
                   "the data file is still stored on the storage volume after detach");
    }


    /**
     * Deleting the storage volume removes it; the elements it was linked to are cleaned up through Asset Maker.
     */
    @Test
    @Order(7)
    void deleteStorageVolume()
    {
        devops.call("/storage-volumes/" + storageVolumeGUID + "/delete", new DeleteElementRequestBody());

        ViewServicesFvtTestSupport.assertGone(devops, "/storage-volumes/" + storageVolumeGUID + "/retrieve");

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(ViewServicesFvtTestSupport.qualifiedName("StorageVolume", "volume-1"));

        assertFalse(contains(devops.elements("/storage-volumes/by-name", byName), storageVolumeGUID),
                    "by-name still returns the deleted storage volume");

        deleteAsset(hostGUID);
        deleteAsset(dataFileGUID);
    }
}
