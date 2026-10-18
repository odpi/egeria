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
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.HostClusterMemberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.HostClusterProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.HostProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.NetworkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.NetworkGatewayLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.NetworkGatewayProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.VisibleEndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.DevopsPipelineFvtSupport.*;

/**
 * DevopsPipelineNetworkFVT covers the Devops Pipeline OMVS network requests: the network and network gateway
 * lifecycles (create, create from template, update, retrieve, by-name, search, delete), the VisibleEndpoint
 * relationship between an endpoint (made by Connection Maker) and a network, the NetworkGatewayLink between a
 * gateway and a network (attach, update, detach by relationship GUID) and HostClusterMember between a host
 * cluster and a host (both made by Asset Maker).
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DevopsPipelineNetworkFVT
{
    private static String networkGUID        = null;
    private static String networkGatewayGUID = null;


    /**
     * A network and a network gateway can be created and retrieved, and each retrieve request returns only its
     * own type.
     */
    @Test
    @Order(1)
    void createAndRetrieve()
    {
        NetworkProperties networkProperties = named(new NetworkProperties(), "Network", "net-1");

        networkGUID = devops.create("/networks", newElement(networkProperties));

        OpenMetadataRootElement network = retrieve(devops, "/networks/" + networkGUID + "/retrieve");

        assertEquals(OpenMetadataType.NETWORK.typeName, network.getElementHeader().getType().getTypeName());
        assertEquals(networkProperties.getQualifiedName(), qualifiedName(network));
        assertTrue(network.getProperties() instanceof NetworkProperties, "retrieve returned " + network.getProperties());

        NetworkGatewayProperties gatewayProperties = named(new NetworkGatewayProperties(), "NetworkGateway", "gateway-1");

        networkGatewayGUID = devops.create("/network-gateways", newElement(gatewayProperties));

        OpenMetadataRootElement gateway = retrieve(devops, "/network-gateways/" + networkGatewayGUID + "/retrieve");

        assertEquals(OpenMetadataType.NETWORK_GATEWAY.typeName, gateway.getElementHeader().getType().getTypeName());
        assertEquals(gatewayProperties.getQualifiedName(), qualifiedName(gateway));
    }


    /**
     * Merge updates of the network and the gateway are visible on retrieve and keep the other properties.
     */
    @Test
    @Order(2)
    void update()
    {
        assertUpdated("/networks/" + networkGUID, new NetworkProperties(), "Network");
        assertUpdated("/network-gateways/" + networkGatewayGUID, new NetworkGatewayProperties(), "NetworkGateway");
    }


    /**
     * Both element types can be found by name and by search string, and each find request is scoped to its own
     * type - a search string that matches both the network and the gateway returns only the requested type.
     */
    @Test
    @Order(3)
    void find()
    {
        assertFound("/networks", "Network", "net-1", networkGUID, OpenMetadataType.NETWORK.typeName);
        assertFound("/network-gateways", "NetworkGateway", "gateway-1", networkGatewayGUID, OpenMetadataType.NETWORK_GATEWAY.typeName);
    }


    /**
     * A network and a gateway made from templates are new elements of the same type with the replacement
     * qualified names.
     */
    @Test
    @Order(4)
    void createFromTemplate()
    {
        assertTemplateCopy("/networks", networkGUID, new NetworkProperties(), "Network", OpenMetadataType.NETWORK.typeName);
        assertTemplateCopy("/network-gateways", networkGatewayGUID, new NetworkGatewayProperties(), "NetworkGateway", OpenMetadataType.NETWORK_GATEWAY.typeName);
    }


    /**
     * VisibleEndpoint links an endpoint to the network it is visible in; it is visible from both ends until
     * detached.
     */
    @Test
    @Order(5)
    void visibleEndpoint()
    {
        EndpointProperties endpointProperties = named(new EndpointProperties(), "Endpoint", "visible");

        endpointProperties.setNetworkAddress("https://view-services-fvt.example.com:9443");

        String endpointGUID = connectionMaker.create("/endpoints", newElement(endpointProperties));

        String path = "/endpoints/" + endpointGUID + "/visible-endpoints/" + networkGUID;

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(new VisibleEndpointProperties());

        devops.call(path + "/attach", requestBody);

        String relationship = OpenMetadataType.VISIBLE_ENDPOINT.typeName;

        assertEquals(1, related(retrieve(devops, "/networks/" + networkGUID + "/retrieve"), relationship, endpointGUID).size(),
                     "the network does not show the endpoint visible in it");
        assertEquals(1, related(retrieve(connectionMaker, "/endpoints/" + endpointGUID + "/retrieve"), relationship, networkGUID).size(),
                     "the endpoint does not show the network it is visible in");

        devops.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(related(retrieve(devops, "/networks/" + networkGUID + "/retrieve"), relationship, endpointGUID).isEmpty(),
                   "the endpoint is still visible in the network after detach");

        connectionMaker.call("/endpoints/" + endpointGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(connectionMaker, "/endpoints/" + endpointGUID + "/retrieve");
    }


    /**
     * NetworkGatewayLink is managed by its relationship GUID: attach returns it, update changes its properties and
     * detach removes it.
     */
    @Test
    @Order(6)
    void networkGatewayLink()
    {
        NetworkGatewayLinkProperties linkProperties = new NetworkGatewayLinkProperties();

        linkProperties.setDisplayName("Gateway into net-1");
        linkProperties.setExternalEndpointAddresses(List.of("203.0.113.10"));
        linkProperties.setInternalEndpointAddresses(List.of("10.0.0.1"));

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(linkProperties);

        String linkGUID = devops.post("/network-gateways/" + networkGatewayGUID + "/network-gateway-links/" + networkGUID + "/attach",
                                      requestBody,
                                      GUIDResponse.class).getGUID();

        assertNotNull(linkGUID, "linkNetworkGateway returned no relationship GUID");

        String relationship = OpenMetadataType.NETWORK_GATEWAY_LINK_RELATIONSHIP.typeName;

        List<RelatedMetadataElementSummary> links = related(retrieve(devops, "/network-gateways/" + networkGatewayGUID + "/retrieve"), relationship, networkGUID);

        assertEquals(1, links.size(), "the gateway does not show its link to the network");
        assertEquals(linkGUID, links.get(0).getRelationshipHeader().getGUID());
        assertEquals(1, related(retrieve(devops, "/networks/" + networkGUID + "/retrieve"), relationship, networkGatewayGUID).size(),
                     "the network does not show its gateway");

        NetworkGatewayLinkProperties updatedProperties = new NetworkGatewayLinkProperties();

        updatedProperties.setDescription("Updated by the Devops Pipeline FVT");

        UpdateRelationshipRequestBody updateRequestBody = new UpdateRelationshipRequestBody();

        updateRequestBody.setMergeUpdate(true);
        updateRequestBody.setProperties(updatedProperties);

        devops.call("/network-gateway-links/" + linkGUID + "/update", updateRequestBody);

        links = related(retrieve(devops, "/network-gateways/" + networkGatewayGUID + "/retrieve"), relationship, networkGUID);

        assertEquals(1, links.size());
        assertTrue(links.get(0).getRelationshipProperties() instanceof NetworkGatewayLinkProperties,
                   "the link has no NetworkGatewayLinkProperties: " + links.get(0).getRelationshipProperties());

        NetworkGatewayLinkProperties retrieved = (NetworkGatewayLinkProperties) links.get(0).getRelationshipProperties();

        assertEquals("Updated by the Devops Pipeline FVT", retrieved.getDescription());
        assertEquals("Gateway into net-1", retrieved.getDisplayName(), "a merge update should keep the display name");

        devops.call("/network-gateway-links/" + linkGUID + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(related(retrieve(devops, "/network-gateways/" + networkGatewayGUID + "/retrieve"), relationship, networkGUID).isEmpty(),
                   "the gateway is still linked to the network after detach");
    }


    /**
     * HostClusterMember links a host cluster to a member host, with the member's role; it is visible from both
     * ends until detached.
     */
    @Test
    @Order(7)
    void hostClusterMember()
    {
        String clusterGUID = createAsset(named(new HostClusterProperties(), "HostCluster", "cluster"));
        String hostGUID    = createAsset(named(new HostProperties(), "Host", "cluster-member"));

        String path = "/host-clusters/" + clusterGUID + "/host-cluster-members/" + hostGUID;

        HostClusterMemberProperties memberProperties = new HostClusterMemberProperties();

        memberProperties.setMemberRole("worker");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(memberProperties);

        devops.call(path + "/attach", requestBody);

        String relationship = OpenMetadataType.HOST_CLUSTER_MEMBER_RELATIONSHIP.typeName;

        List<RelatedMetadataElementSummary> members = related(asset(clusterGUID), relationship, hostGUID);

        assertEquals(1, members.size(), "the host cluster does not show its member host");
        assertTrue(members.get(0).getRelationshipProperties() instanceof HostClusterMemberProperties properties &&
                           "worker".equals(properties.getMemberRole()),
                   "the member role was not saved: " + members.get(0).getRelationshipProperties());
        assertEquals(1, related(asset(hostGUID), relationship, clusterGUID).size(),
                     "the host does not show its cluster");

        devops.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(related(asset(clusterGUID), relationship, hostGUID).isEmpty(),
                   "the host is still a member of the cluster after detach");

        deleteAsset(hostGUID);
        deleteAsset(clusterGUID);
    }


    /**
     * Deleting the network and the gateway removes them.
     */
    @Test
    @Order(8)
    void delete()
    {
        devops.call("/network-gateways/" + networkGatewayGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(devops, "/network-gateways/" + networkGatewayGUID + "/retrieve");

        devops.call("/networks/" + networkGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(devops, "/networks/" + networkGUID + "/retrieve");
    }


    private static void assertUpdated(String                     elementPath,
                                      OpenMetadataRootProperties properties,
                                      String                     typeName)
    {
        String description = "Updated " + typeName + " by the Devops Pipeline FVT";

        ((ReferenceableProperties) properties).setDescription(description);

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        assertTrue(devops.post(elementPath + "/update", requestBody, BooleanResponse.class).getFlag(),
                   elementPath + "/update reported that nothing was updated");

        OpenMetadataRootElement element = retrieve(devops, elementPath + "/retrieve");

        assertTrue(element.getProperties() instanceof ReferenceableProperties referenceableProperties &&
                           description.equals(referenceableProperties.getDescription()) &&
                           (typeName + " " + (typeName.equals("Network") ? "net-1" : "gateway-1")).equals(referenceableProperties.getDisplayName()),
                   "the update is not visible, or did not keep the display name: " + element.getProperties());
    }


    private static void assertFound(String rootPath,
                                    String typeName,
                                    String name,
                                    String expectedGUID,
                                    String expectedTypeName)
    {
        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(ViewServicesFvtTestSupport.qualifiedName(typeName, name));

        List<OpenMetadataRootElement> named = devops.elements(rootPath + "/by-name", byName);

        assertEquals(1, named.size(), rootPath + "/by-name should return exactly one element: " + named);
        assertEquals(expectedGUID, named.get(0).getElementHeader().getGUID());

        SearchStringRequestBody search = new SearchStringRequestBody();

        search.setSearchString(ViewServicesFvtTestSupport.runSearchString() + "::Network");

        List<OpenMetadataRootElement> found = devops.elements(rootPath + "/by-search-string", search);

        assertTrue(contains(found, expectedGUID), rootPath + "/by-search-string did not return " + expectedGUID);

        for (OpenMetadataRootElement element : found)
        {
            assertEquals(expectedTypeName, element.getElementHeader().getType().getTypeName(),
                         rootPath + "/by-search-string returned an element of the wrong type");
        }
    }


    private static void assertTemplateCopy(String                     rootPath,
                                           String                     templateGUID,
                                           OpenMetadataRootProperties replacement,
                                           String                     typeName,
                                           String                     expectedTypeName)
    {
        String newQualifiedName = ViewServicesFvtTestSupport.qualifiedName(typeName, "from-template");

        ((ReferenceableProperties) replacement).setQualifiedName(newQualifiedName);

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(templateGUID);
        requestBody.setIsOwnAnchor(true);
        requestBody.setReplacementProperties(replacement);

        String copyGUID = devops.create(rootPath + "/from-template", requestBody);

        assertNotEquals(templateGUID, copyGUID);

        OpenMetadataRootElement copy = retrieve(devops, rootPath + "/" + copyGUID + "/retrieve");

        assertEquals(expectedTypeName, copy.getElementHeader().getType().getTypeName());
        assertEquals(newQualifiedName, qualifiedName(copy));

        devops.call(rootPath + "/" + copyGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(devops, rootPath + "/" + copyGUID + "/retrieve");
    }
}
