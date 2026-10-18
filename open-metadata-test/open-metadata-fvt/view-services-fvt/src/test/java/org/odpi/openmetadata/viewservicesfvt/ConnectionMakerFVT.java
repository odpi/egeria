/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectorTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EmbeddedConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.VirtualConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFVT.contains;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.assertLinked;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.assertNotLinked;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.named;
import static org.odpi.openmetadata.viewservicesfvt.AssetMakerFvtSupport.newElement;

/**
 * ConnectionMakerFVT covers every request of the Connection Maker OMVS: the create (direct and from template),
 * update, retrieve, query and delete requests for connector types, endpoints and connections, and the links
 * between them and to the assets and other elements they describe.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConnectionMakerFVT
{
    private static final ViewServiceClient connectionMaker = new ViewServiceClient(ViewServiceDescription.CONNECTION_MAKER);

    private static final String PROVIDER_CLASS_NAME = "org.odpi.viewservicesfvt." + OMAGPlatformExtension.RUN_ID.replace("-", "") + ".FVTConnectorProvider";
    private static final String NETWORK_ADDRESS     = "https://localhost:9443/" + ViewServicesFvtTestSupport.runSearchString();

    private static String connectorTypeGUID       = null;
    private static String copiedConnectorTypeGUID = null;
    private static String endpointGUID            = null;
    private static String copiedEndpointGUID      = null;
    private static String connectionGUID          = null;
    private static String copiedConnectionGUID    = null;
    private static String virtualConnectionGUID   = null;

    private static final List<String> connectorTypeGUIDs = new ArrayList<>();
    private static final List<String> endpointGUIDs      = new ArrayList<>();
    private static final List<String> connectionGUIDs    = new ArrayList<>();
    private static final List<String> assetGUIDs         = new ArrayList<>();


    /**
     * Connector types: create, retrieve, update, copy, and find by name, provider class and search string.
     */
    @Test
    @Order(1)
    void connectorTypes()
    {
        ConnectorTypeProperties properties = named(new ConnectorTypeProperties(), "ConnectorType", "fvt-connector-type");

        properties.setConnectorProviderClassName(PROVIDER_CLASS_NAME);
        properties.setSupportedAssetTypeName(OpenMetadataType.DATA_FILE.typeName);

        connectorTypeGUID = create("/connector-types", properties, connectorTypeGUIDs);

        ConnectorTypeProperties retrieved = (ConnectorTypeProperties) retrieve("/connector-types/", connectorTypeGUID).getProperties();

        assertEquals(properties.getQualifiedName(), retrieved.getQualifiedName());
        assertEquals(PROVIDER_CLASS_NAME, retrieved.getConnectorProviderClassName());

        ConnectorTypeProperties update = new ConnectorTypeProperties();

        update.setExpectedDataFormat("csv");

        update("/connector-types/", connectorTypeGUID, update);

        retrieved = (ConnectorTypeProperties) retrieve("/connector-types/", connectorTypeGUID).getProperties();

        assertEquals("csv", retrieved.getExpectedDataFormat());
        assertEquals(PROVIDER_CLASS_NAME, retrieved.getConnectorProviderClassName(), "a merge update should keep the provider class");

        copiedConnectorTypeGUID = copy("/connector-types/from-template", connectorTypeGUID,
                                       named(new ConnectorTypeProperties(), "ConnectorType", "fvt-connector-type-copy"), connectorTypeGUIDs);

        assertEquals("csv", ((ConnectorTypeProperties) retrieve("/connector-types/", copiedConnectorTypeGUID).getProperties()).getExpectedDataFormat(),
                     "the template's expected data format was not copied");

        assertFound("/connector-types/by-name", ViewServicesFvtTestSupport.qualifiedName("ConnectorType", "fvt-connector-type"), connectorTypeGUID, copiedConnectorTypeGUID);

        FilterRequestBody byProvider = new FilterRequestBody();

        byProvider.setFilter(PROVIDER_CLASS_NAME);

        List<OpenMetadataRootElement> found = connectionMaker.elements("/connector-types/by-connector-provider-class-name", byProvider);

        assertTrue(contains(found, connectorTypeGUID) && contains(found, copiedConnectorTypeGUID),
                   "getConnectorTypesByConnectorProvider missed a connector type");

        assertSearchFinds("/connector-types/by-search-string", connectorTypeGUID, copiedConnectorTypeGUID);
    }


    /**
     * Endpoints: create, retrieve, update, copy, and find by name, network address and search string.
     */
    @Test
    @Order(2)
    void endpoints()
    {
        EndpointProperties properties = named(new EndpointProperties(), "Endpoint", "fvt-endpoint");

        properties.setNetworkAddress(NETWORK_ADDRESS);
        properties.setProtocol("https");

        endpointGUID = create("/endpoints", properties, endpointGUIDs);

        EndpointProperties retrieved = (EndpointProperties) retrieve("/endpoints/", endpointGUID).getProperties();

        assertEquals(NETWORK_ADDRESS, retrieved.getNetworkAddress());

        EndpointProperties update = new EndpointProperties();

        update.setEncryptionMethod("TLS");

        update("/endpoints/", endpointGUID, update);

        retrieved = (EndpointProperties) retrieve("/endpoints/", endpointGUID).getProperties();

        assertEquals("TLS", retrieved.getEncryptionMethod());
        assertEquals("https", retrieved.getProtocol(), "a merge update should keep the protocol");

        copiedEndpointGUID = copy("/endpoints/from-template", endpointGUID,
                                  named(new EndpointProperties(), "Endpoint", "fvt-endpoint-copy"), endpointGUIDs);

        assertEquals(NETWORK_ADDRESS, ((EndpointProperties) retrieve("/endpoints/", copiedEndpointGUID).getProperties()).getNetworkAddress(),
                     "the template's network address was not copied");

        assertFound("/endpoints/by-name", ViewServicesFvtTestSupport.qualifiedName("Endpoint", "fvt-endpoint"), endpointGUID, copiedEndpointGUID);

        FilterRequestBody byAddress = new FilterRequestBody();

        byAddress.setFilter(NETWORK_ADDRESS);

        List<OpenMetadataRootElement> found = connectionMaker.elements("/endpoints/by-network-address", byAddress);

        assertTrue(contains(found, endpointGUID) && contains(found, copiedEndpointGUID), "getEndpointsByNetworkAddress missed an endpoint");

        assertSearchFinds("/endpoints/by-search-string", endpointGUID, copiedEndpointGUID);
    }


    /**
     * Connections: create, retrieve, update, copy, and find by name and search string.
     */
    @Test
    @Order(3)
    void connections()
    {
        ConnectionProperties properties = named(new ConnectionProperties(), "Connection", "fvt-connection");

        properties.setUserId("fvt-connection-user");

        connectionGUID = create("/connections", properties, connectionGUIDs);

        ConnectionProperties retrieved = (ConnectionProperties) retrieve("/connections/", connectionGUID).getProperties();

        assertEquals(properties.getQualifiedName(), retrieved.getQualifiedName());
        assertEquals("fvt-connection-user", retrieved.getUserId());

        ConnectionProperties update = new ConnectionProperties();

        update.setDescription("Updated by " + ConnectionMakerFVT.class.getSimpleName());

        update("/connections/", connectionGUID, update);

        retrieved = (ConnectionProperties) retrieve("/connections/", connectionGUID).getProperties();

        assertEquals(update.getDescription(), retrieved.getDescription());
        assertEquals("fvt-connection-user", retrieved.getUserId(), "a merge update should keep the user id");

        copiedConnectionGUID = copy("/connections/from-template", connectionGUID,
                                    named(new ConnectionProperties(), "Connection", "fvt-connection-copy"), connectionGUIDs);

        assertEquals("fvt-connection-user", ((ConnectionProperties) retrieve("/connections/", copiedConnectionGUID).getProperties()).getUserId(),
                     "the template's user id was not copied");

        assertFound("/connections/by-name", ViewServicesFvtTestSupport.qualifiedName("Connection", "fvt-connection"), connectionGUID, copiedConnectionGUID);
        assertSearchFinds("/connections/by-search-string", connectionGUID, copiedConnectionGUID);
    }


    /**
     * A connection is linked to its connector type and endpoint, and embedded in a virtual connection.
     */
    @Test
    @Order(4)
    void connectionStructure()
    {
        String connectorTypePath = "/connections/" + connectionGUID + "/connector-types/" + connectorTypeGUID;

        connectionMaker.call(connectorTypePath + "/attach", new NewRelationshipRequestBody());
        assertLinked(OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName, connectionGUID, connectorTypeGUID);

        String endpointPath = "/connections/" + connectionGUID + "/endpoints/" + endpointGUID;

        connectionMaker.call(endpointPath + "/attach", new NewRelationshipRequestBody());
        assertLinked(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName, connectionGUID, endpointGUID);

        virtualConnectionGUID = create("/connections", named(new VirtualConnectionProperties(), "VirtualConnection", "fvt-virtual-connection"), connectionGUIDs);

        EmbeddedConnectionProperties embeddedConnectionProperties = new EmbeddedConnectionProperties();

        embeddedConnectionProperties.setPosition(1);
        embeddedConnectionProperties.setDisplayName("embedded");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(embeddedConnectionProperties);

        String embeddedPath = "/connections/" + virtualConnectionGUID + "/embedded-connections/" + connectionGUID;

        connectionMaker.call(embeddedPath + "/attach", requestBody);
        assertLinked(OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName, virtualConnectionGUID, connectionGUID);

        connectionMaker.call(embeddedPath + "/detach", new DeleteRelationshipRequestBody());
        assertNotLinked(OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName, virtualConnectionGUID, connectionGUID);

        connectionMaker.call(endpointPath + "/detach", new DeleteRelationshipRequestBody());
        assertNotLinked(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName, connectionGUID, endpointGUID);
    }


    /**
     * detachConnectionConnectorType removes the link made in connectionStructure.
     * <br>
     * Observed: the request succeeds but the ConnectionConnectorType relationship is still there.
     * ConnectionHandler.detachConnectionConnectorType calls detachRelatedElementsInStore with the connector type as
     * end 1 and the connection as end 2, the reverse of linkConnectionConnectorType and of the type definition, so
     * nothing matches.
     */
    @Test
    @Order(41)
    void detachConnectorType()
    {
        connectionMaker.call("/connections/" + connectionGUID + "/connector-types/" + connectorTypeGUID + "/detach", new DeleteRelationshipRequestBody());
        assertNotLinked(OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName, connectionGUID, connectorTypeGUID);
    }


    /**
     * Connections are linked to the resources they connect to, through both the resource and the (deprecated)
     * asset forms of the request; an endpoint is linked to the server it belongs to.
     */
    @Test
    @Order(50)
    void connectionsAndEndpointsForResources()
    {
        String fileGUID = AssetMakerFvtSupport.createAsset(named(new DataFileProperties(), "DataFile", "connected-file"));

        assetGUIDs.add(fileGUID);

        String resourcePath = "/elements/" + fileGUID + "/connections/" + connectionGUID;

        connectionMaker.call(resourcePath + "/attach", new NewRelationshipRequestBody());
        assertLinked(OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName, fileGUID, connectionGUID);

        connectionMaker.call(resourcePath + "/detach", new DeleteRelationshipRequestBody());
        assertNotLinked(OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName, fileGUID, connectionGUID);

        String assetPath = "/assets/" + fileGUID + "/connections/" + copiedConnectionGUID;

        connectionMaker.call(assetPath + "/attach", new NewRelationshipRequestBody());
        assertLinked(OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName, fileGUID, copiedConnectionGUID);

        connectionMaker.call(assetPath + "/detach", new DeleteRelationshipRequestBody());
        assertNotLinked(OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName, fileGUID, copiedConnectionGUID);

        String serverGUID = AssetMakerFvtSupport.createAsset(named(new SoftwareServerProperties(), "SoftwareServer", "endpoint-server"));

        assetGUIDs.add(serverGUID);

        String endpointPath = "/assets/" + serverGUID + "/endpoints/" + endpointGUID;

        connectionMaker.call(endpointPath + "/attach", new NewRelationshipRequestBody());
        assertLinked(OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName, serverGUID, endpointGUID);

        List<OpenMetadataRootElement> endpoints = connectionMaker.elements("/assets/" + serverGUID + "/endpoints/retrieve", new ResultsRequestBody());

        assertTrue(contains(endpoints, endpointGUID), "getEndpointsForInfrastructure did not return the endpoint");
        assertFalse(contains(endpoints, copiedEndpointGUID), "getEndpointsForInfrastructure returned an unlinked endpoint");

        connectionMaker.call(endpointPath + "/detach", new DeleteRelationshipRequestBody());
        assertNotLinked(OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName, serverGUID, endpointGUID);
        assertTrue(connectionMaker.elements("/assets/" + serverGUID + "/endpoints/retrieve", new ResultsRequestBody()).isEmpty(),
                   "getEndpointsForInfrastructure still returns the detached endpoint");
    }


    /**
     * Each delete request removes its elements.
     */
    @Test
    @Order(60)
    void deleteEverything()
    {
        delete("/connections/", connectionGUIDs);
        delete("/endpoints/", endpointGUIDs);
        delete("/connector-types/", connectorTypeGUIDs);

        for (String guid : List.copyOf(assetGUIDs))
        {
            AssetMakerFvtSupport.delete(guid);
            assetGUIDs.remove(guid);
        }
    }


    @AfterAll
    static void cleanUp()
    {
        Function<String, Void> unchecked = path -> {
            connectionMaker.postUnchecked(path, new DeleteElementRequestBody(), VoidResponse.class);
            return null;
        };

        connectionGUIDs.forEach(guid -> unchecked.apply("/connections/" + guid + "/delete"));
        endpointGUIDs.forEach(guid -> unchecked.apply("/endpoints/" + guid + "/delete"));
        connectorTypeGUIDs.forEach(guid -> unchecked.apply("/connector-types/" + guid + "/delete"));
        assetGUIDs.forEach(guid -> AssetMakerFvtSupport.assetMaker.postUnchecked("/assets/" + guid + "/delete", new DeleteElementRequestBody(), VoidResponse.class));
    }


    private static String create(String                     path,
                                 OpenMetadataRootProperties properties,
                                 List<String>               created)
    {
        String guid = connectionMaker.create(path, newElement(properties));

        created.add(guid);

        return guid;
    }


    private static String copy(String                     path,
                               String                     templateGUID,
                               OpenMetadataRootProperties replacementProperties,
                               List<String>               created)
    {
        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(templateGUID);
        requestBody.setReplacementProperties(replacementProperties);

        String guid = connectionMaker.create(path, requestBody);

        created.add(guid);

        assertNotEquals(templateGUID, guid);

        return guid;
    }


    private static OpenMetadataRootElement retrieve(String pathPrefix,
                                                    String guid)
    {
        OpenMetadataRootElement element = connectionMaker.element(pathPrefix + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, pathPrefix + guid + "/retrieve returned nothing");
        assertEquals(guid, element.getElementHeader().getGUID());

        return element;
    }


    private static void update(String                     pathPrefix,
                               String                     guid,
                               OpenMetadataRootProperties properties)
    {
        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        connectionMaker.post(pathPrefix + guid + "/update", requestBody, BooleanResponse.class);
    }


    /**
     * A by-name request finds the element with the exact name, and not its copy.
     */
    private static void assertFound(String path,
                                    String name,
                                    String expectedGUID,
                                    String copyGUID)
    {
        FilterRequestBody requestBody = new FilterRequestBody();

        requestBody.setFilter(name);

        List<OpenMetadataRootElement> found = connectionMaker.elements(path, requestBody);

        assertTrue(contains(found, expectedGUID), path + " did not find " + name);
        assertFalse(contains(found, copyGUID), path + " returned the copy for the original's name");
    }


    private static void assertSearchFinds(String path,
                                          String guid,
                                          String copyGUID)
    {
        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(ViewServicesFvtTestSupport.runSearchString());

        List<OpenMetadataRootElement> found = connectionMaker.elements(path, requestBody);

        assertTrue(contains(found, guid) && contains(found, copyGUID), path + " missed an element");
    }


    private static void delete(String       pathPrefix,
                               List<String> guids)
    {
        for (String guid : List.copyOf(guids))
        {
            connectionMaker.call(pathPrefix + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(connectionMaker, pathPrefix + guid + "/retrieve");
            guids.remove(guid);
        }
    }
}
