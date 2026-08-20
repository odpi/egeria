/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OperatingPlatformClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.OperatingPlatformManifestProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.OperatingPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.OperatingPlatformUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwarePackageDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwarePackageManifestProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
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
 * OperatingPlatformClientBVT exercises the create/get/update/find/delete lifecycle of {@link OperatingPlatformClient}, one of the connector
 * context clients, against the running BVT server, together with its OperatingPlatformUse, OperatingPlatformManifest and SoftwarePackageDependency relationships and the SoftwarePackageManifest classification.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class OperatingPlatformClientBVT
{
    @Test
    void operatingPlatformLifecycle() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        OperatingPlatformClient client = connectorContext.getOperatingPlatformClient();

        String qualifiedName = "open-metadata-bvt:OperatingPlatform:" + UUID.randomUUID();

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        OperatingPlatformProperties createProperties = new OperatingPlatformProperties();
        createProperties.setQualifiedName(qualifiedName);
        createProperties.setDisplayName("BVT OperatingPlatform");
        createProperties.setDescription("Created by the open-metadata-bvt build verification test suite");

        String elementGUID = client.createOperatingPlatform(newElementOptions, null, createProperties, null);

        assertNotNull(elementGUID, "createOperatingPlatform should return a GUID");

        OpenMetadataRootElement retrievedElement = client.getOperatingPlatformByGUID(elementGUID, new GetOptions());

        assertNotNull(retrievedElement, "getOperatingPlatformByGUID should find the element that was just created");
        assertEquals(elementGUID, retrievedElement.getElementHeader().getGUID());
        assertEquals(qualifiedName, ((OperatingPlatformProperties) retrievedElement.getProperties()).getQualifiedName());

        OperatingPlatformProperties updateProperties = new OperatingPlatformProperties();
        updateProperties.setQualifiedName(qualifiedName);
        updateProperties.setDisplayName("BVT OperatingPlatform (updated)");

        boolean updateOccurred = client.updateOperatingPlatform(elementGUID, new UpdateOptions(), updateProperties);

        assertTrue(updateOccurred, "updateOperatingPlatform should report that an update occurred");

        OpenMetadataRootElement updatedElement = client.getOperatingPlatformByGUID(elementGUID, new GetOptions());

        assertEquals("BVT OperatingPlatform (updated)", ((OperatingPlatformProperties) updatedElement.getProperties()).getDisplayName());

        List<OpenMetadataRootElement> foundElements = client.findOperatingPlatforms(qualifiedName, new SearchOptions());

        assertNotNull(foundElements, "findOperatingPlatforms should return a (possibly empty) list, not null");
        assertTrue(foundElements.stream().anyMatch(element -> elementGUID.equals(element.getElementHeader().getGUID())),
                   "findOperatingPlatforms should find the element by its qualified name");

        List<OpenMetadataRootElement> namedElements = client.getOperatingPlatformsByName(qualifiedName, null);

        assertNotNull(namedElements, "getOperatingPlatformsByName should return a (possibly empty) list, not null");

        client.deleteOperatingPlatform(elementGUID, new DeleteOptions());

        assertThrows(Exception.class,
                     () -> client.getOperatingPlatformByGUID(elementGUID, new GetOptions()),
                     "getOperatingPlatformByGUID should no longer find the element after it has been deleted");
    }


    @Test
    void operatingPlatformRelationships() throws Exception
    {
        ConnectorContextBase    connectorContext = ConnectorContextFactory.newContext();
        OperatingPlatformClient client           = connectorContext.getOperatingPlatformClient();

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        OperatingPlatformProperties platformOne = new OperatingPlatformProperties();
        platformOne.setQualifiedName("open-metadata-bvt:OperatingPlatform:" + UUID.randomUUID());
        platformOne.setDisplayName("BVT OperatingPlatform one");

        OperatingPlatformProperties platformTwo = new OperatingPlatformProperties();
        platformTwo.setQualifiedName("open-metadata-bvt:OperatingPlatform:" + UUID.randomUUID());
        platformTwo.setDisplayName("BVT OperatingPlatform two");

        String platformOneGUID = client.createOperatingPlatform(newElementOptions, null, platformOne, null);
        String platformTwoGUID = client.createOperatingPlatform(newElementOptions, null, platformTwo, null);

        /*
         * The relationship ends are typed: OperatingPlatformUse runs to ITInfrastructure, the manifest and the
         * software package dependency run to a Collection, and the dependency starts at an Asset.
         */
        AssetProperties infrastructureProperties = new AssetProperties();
        infrastructureProperties.setTypeName("ITInfrastructure");
        infrastructureProperties.setQualifiedName("open-metadata-bvt:ITInfrastructure:" + UUID.randomUUID());
        infrastructureProperties.setDisplayName("BVT ITInfrastructure");

        String infrastructureGUID = connectorContext.getAssetClient().createAsset(newElementOptions, null, infrastructureProperties, null);

        CollectionProperties collectionProperties = new CollectionProperties();
        collectionProperties.setQualifiedName("open-metadata-bvt:Collection:" + UUID.randomUUID());
        collectionProperties.setDisplayName("BVT software package collection");

        String collectionGUID = connectorContext.getCollectionClient().createCollection(newElementOptions, null, collectionProperties, null);

        OperatingPlatformUseProperties useProperties = new OperatingPlatformUseProperties();
        useProperties.setDeployer("open-metadata-bvt");

        client.linkOperatingPlatformUse(platformOneGUID, infrastructureGUID, new MakeAnchorOptions(), useProperties);
        client.detachOperatingPlatformUse(platformOneGUID, infrastructureGUID, new DeleteOptions());

        client.linkOperatingPlatformManifest(platformOneGUID, collectionGUID, new MakeAnchorOptions(), new OperatingPlatformManifestProperties());
        client.detachOperatingPlatformManifest(platformOneGUID, collectionGUID, new DeleteOptions());

        client.linkSoftwarePackageDependency(infrastructureGUID, collectionGUID, new MakeAnchorOptions(), new SoftwarePackageDependencyProperties());
        client.detachSoftwarePackageDependency(infrastructureGUID, collectionGUID, new DeleteOptions());

        /*
         * SoftwarePackageManifest is a classification rather than a relationship.
         */
        client.setSoftwarePackageManifest(collectionGUID, new SoftwarePackageManifestProperties(), new MetadataSourceOptions());
        client.clearSoftwarePackageManifest(collectionGUID, new MetadataSourceOptions());

        connectorContext.getCollectionClient().deleteCollection(collectionGUID, new DeleteOptions());
        connectorContext.getAssetClient().deleteAsset(infrastructureGUID, new DeleteOptions());
        client.deleteOperatingPlatform(platformOneGUID, new DeleteOptions());
        client.deleteOperatingPlatform(platformTwoGUID, new DeleteOptions());
    }
}
