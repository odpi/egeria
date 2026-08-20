/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.NetworkClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.NetworkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.NetworkGatewayLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.NetworkGatewayProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * NetworkClientBVT exercises the create/get/update/find/delete lifecycle of {@link NetworkClient}, one of the connector
 * context clients, against the running BVT server, together with the network gateway entity and the VisibleEndpoint and NetworkGatewayLink relationships.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class NetworkClientBVT
{
    @Test
    void networkLifecycle() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        NetworkClient client = connectorContext.getNetworkClient();

        String qualifiedName = "open-metadata-bvt:Network:" + UUID.randomUUID();

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        NetworkProperties createProperties = new NetworkProperties();
        createProperties.setQualifiedName(qualifiedName);
        createProperties.setDisplayName("BVT Network");
        createProperties.setDescription("Created by the open-metadata-bvt build verification test suite");

        String elementGUID = client.createNetwork(newElementOptions, null, createProperties, null);

        assertNotNull(elementGUID, "createNetwork should return a GUID");

        OpenMetadataRootElement retrievedElement = client.getNetworkByGUID(elementGUID, new GetOptions());

        assertNotNull(retrievedElement, "getNetworkByGUID should find the element that was just created");
        assertEquals(elementGUID, retrievedElement.getElementHeader().getGUID());
        assertEquals(qualifiedName, ((NetworkProperties) retrievedElement.getProperties()).getQualifiedName());

        NetworkProperties updateProperties = new NetworkProperties();
        updateProperties.setQualifiedName(qualifiedName);
        updateProperties.setDisplayName("BVT Network (updated)");

        boolean updateOccurred = client.updateNetwork(elementGUID, new UpdateOptions(), updateProperties);

        assertTrue(updateOccurred, "updateNetwork should report that an update occurred");

        OpenMetadataRootElement updatedElement = client.getNetworkByGUID(elementGUID, new GetOptions());

        assertEquals("BVT Network (updated)", ((NetworkProperties) updatedElement.getProperties()).getDisplayName());

        List<OpenMetadataRootElement> foundElements = client.findNetworks(qualifiedName, new SearchOptions());

        assertNotNull(foundElements, "findNetworks should return a (possibly empty) list, not null");
        assertTrue(foundElements.stream().anyMatch(element -> elementGUID.equals(element.getElementHeader().getGUID())),
                   "findNetworks should find the element by its qualified name");

        List<OpenMetadataRootElement> namedElements = client.getNetworksByName(qualifiedName, null);

        assertNotNull(namedElements, "getNetworksByName should return a (possibly empty) list, not null");

        client.deleteNetwork(elementGUID, new DeleteOptions());

        assertThrows(Exception.class,
                     () -> client.getNetworkByGUID(elementGUID, new GetOptions()),
                     "getNetworkByGUID should no longer find the element after it has been deleted");
    }


    @Test
    void networkRelationships() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        NetworkClient        client           = connectorContext.getNetworkClient();

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        NetworkProperties networkProperties = new NetworkProperties();
        networkProperties.setQualifiedName("open-metadata-bvt:Network:" + UUID.randomUUID());
        networkProperties.setDisplayName("BVT Network");

        NetworkGatewayProperties gatewayProperties = new NetworkGatewayProperties();
        gatewayProperties.setQualifiedName("open-metadata-bvt:NetworkGateway:" + UUID.randomUUID());
        gatewayProperties.setDisplayName("BVT NetworkGateway");

        String networkGUID = client.createNetwork(newElementOptions, null, networkProperties, null);
        String gatewayGUID = client.createNetworkGateway(newElementOptions, null, gatewayProperties, null);

        assertNotNull(networkGUID, "createNetwork should return a GUID");
        assertNotNull(gatewayGUID, "createNetworkGateway should return a GUID");

        OpenMetadataRootElement gateway = client.getNetworkGatewayByGUID(gatewayGUID, new GetOptions());

        assertNotNull(gateway, "getNetworkGatewayByGUID should find the gateway that was just created");

        /*
         * NetworkGatewayLink is a multi-link relationship - the link returns the relationship GUID and both
         * the update and the detach are addressed by it.  Linking the same pair twice must produce two
         * distinct relationships.
         */
        NetworkGatewayLinkProperties linkProperties = new NetworkGatewayLinkProperties();
        linkProperties.setExternalEndpointAddress("bvt-external-1");

        String linkOneGUID = client.linkNetworkGateway(gatewayGUID, networkGUID, new MakeAnchorOptions(), linkProperties);
        String linkTwoGUID = client.linkNetworkGateway(gatewayGUID, networkGUID, new MakeAnchorOptions(), linkProperties);

        assertNotNull(linkOneGUID, "linkNetworkGateway should return the new relationship's GUID");
        assertNotNull(linkTwoGUID, "linkNetworkGateway should return the new relationship's GUID");
        assertNotEquals(linkOneGUID, linkTwoGUID,
                        "NetworkGatewayLink is multi-link so linking the same pair twice must create two relationships");

        linkProperties.setExternalEndpointAddress("bvt-external-1-updated");

        client.updateNetworkGatewayLink(linkOneGUID, new UpdateOptions(), linkProperties);

        client.detachNetworkGateway(linkOneGUID, new DeleteOptions());
        client.detachNetworkGateway(linkTwoGUID, new DeleteOptions());

        client.deleteNetworkGateway(gatewayGUID, new DeleteOptions());
        client.deleteNetwork(networkGUID, new DeleteOptions());
    }
}
