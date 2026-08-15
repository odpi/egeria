/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
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
 * AssetClientBVT exercises the create/get/update/find/delete lifecycle of
 * {@link AssetClient}, one of the connector context clients, against the running BVT server.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class AssetClientBVT
{
    @Test
    void assetLifecycle() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        AssetClient           assetClient      = connectorContext.getAssetClient();

        String qualifiedName = "open-metadata-bvt:Asset:" + UUID.randomUUID();

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        AssetProperties createProperties = new AssetProperties();
        createProperties.setQualifiedName(qualifiedName);
        createProperties.setDisplayName("BVT Asset");
        createProperties.setDescription("Created by the open-metadata-bvt build verification test suite");

        String assetGUID = assetClient.createAsset(newElementOptions, null, createProperties, null);

        assertNotNull(assetGUID, "createAsset should return a GUID");

        OpenMetadataRootElement retrievedElement = assetClient.getAssetByGUID(assetGUID, new GetOptions());

        assertNotNull(retrievedElement, "getAssetByGUID should find the asset that was just created");
        assertEquals(assetGUID, retrievedElement.getElementHeader().getGUID());
        assertEquals(qualifiedName, ((AssetProperties) retrievedElement.getProperties()).getQualifiedName());
        assertEquals("BVT Asset", ((AssetProperties) retrievedElement.getProperties()).getDisplayName());

        AssetProperties updateProperties = new AssetProperties();
        updateProperties.setQualifiedName(qualifiedName);
        updateProperties.setDisplayName("BVT Asset (updated)");
        updateProperties.setDescription("Updated by the open-metadata-bvt build verification test suite");

        boolean updateOccurred = assetClient.updateAsset(assetGUID, new UpdateOptions(), updateProperties);

        assertTrue(updateOccurred, "updateAsset should report that an update occurred");

        OpenMetadataRootElement updatedElement = assetClient.getAssetByGUID(assetGUID, new GetOptions());

        assertEquals("BVT Asset (updated)", ((AssetProperties) updatedElement.getProperties()).getDisplayName());

        List<OpenMetadataRootElement> foundElements = assetClient.findAssets(qualifiedName, new SearchOptions());

        assertNotNull(foundElements, "findAssets should return a (possibly empty) list, not null");
        assertTrue(foundElements.stream().anyMatch(element -> assetGUID.equals(element.getElementHeader().getGUID())),
                   "findAssets should find the asset by its qualified name");

        assetClient.deleteAsset(assetGUID, new DeleteOptions());

        assertThrows(Exception.class,
                     () -> assetClient.getAssetByGUID(assetGUID, new GetOptions()),
                     "getAssetByGUID should no longer find the asset after it has been deleted");
    }
}
