/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import com.fasterxml.jackson.databind.JsonNode;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Shared set-up for the Runtime Manager FVT classes.
 * <br>
 * Most Runtime Manager requests act on a running platform or server through the connector for the
 * SoftwareServerPlatform or SoftwareServer asset that describes it.  The connector is built from the
 * asset's connection: its connector type names the Egeria system connector to use, its endpoint holds the
 * platform's URL root and, for a server, its configuration properties hold the server's name.  This class
 * catalogues such assets with Asset Maker and Connection Maker so the requests reach this suite's own
 * in-process platform, and removes them again.
 * <br>
 * It also drives the GET and DELETE requests, which {@link ViewServiceClient} does not cover, and requests
 * whose bodies are plain strings or beans from modules this suite does not depend on, reading the
 * responses as JSON.
 */
class RuntimeManagerFvtSupport
{
    static final ViewServiceClient runtimeManager  = new ViewServiceClient(ViewServiceDescription.RUNTIME_MANAGER);
    static final ViewServiceClient assetMaker      = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);
    static final ViewServiceClient connectionMaker = new ViewServiceClient(ViewServiceDescription.CONNECTION_MAKER);

    private static final String CONNECTOR_PACKAGE = "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.";

    static final String PLATFORM_PROVIDER               = CONNECTOR_PACKAGE + "platform.OMAGServerPlatformProvider";
    static final String METADATA_ACCESS_SERVER_PROVIDER = CONNECTOR_PACKAGE + "servers.MetadataAccessServerProvider";
    static final String VIEW_SERVER_PROVIDER            = CONNECTOR_PACKAGE + "servers.ViewServerProvider";
    static final String ENGINE_HOST_PROVIDER            = CONNECTOR_PACKAGE + "servers.EngineHostProvider";
    static final String INTEGRATION_DAEMON_PROVIDER     = CONNECTOR_PACKAGE + "servers.IntegrationDaemonProvider";

    static final String OMAG_SERVER_PLATFORM   = "OMAG Server Platform";
    static final String METADATA_ACCESS_SERVER = "Metadata Access Server";
    static final String VIEW_SERVER            = "View Server";
    static final String ENGINE_HOST            = "Engine Host";
    static final String INTEGRATION_DAEMON     = "Integration Daemon";

    private static final RestTemplate restTemplate = new RestTemplate();


    /**
     * The elements that describe one catalogued platform or server.
     *
     * @param assetGUID the SoftwareServerPlatform or SoftwareServer
     * @param connectionGUID its connection
     * @param endpointGUID the connection's endpoint
     */
    record CataloguedResource(String assetGUID, String connectionGUID, String endpointGUID) {}


    /**
     * Catalogue this suite's platform as an OMAG Server Platform.
     *
     * @param label distinguishes this catalogue entry's qualified names
     * @param platformName the platform's resource name, which the platform connector reports
     * @return the catalogued elements
     */
    static CataloguedResource catalogPlatform(String label,
                                              String platformName)
    {
        SoftwareServerPlatformProperties properties = new SoftwareServerPlatformProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SoftwareServerPlatform", label));
        properties.setDisplayName(platformName);
        properties.setResourceName(platformName);
        properties.setDescription("Catalogued by the Runtime Manager FVT.");
        properties.setDeployedImplementationType(OMAG_SERVER_PLATFORM);

        return catalog(label, properties, PLATFORM_PROVIDER, null);
    }


    /**
     * Catalogue an OMAG Server running (or not) on this suite's platform.
     *
     * @param label distinguishes this catalogue entry's qualified names
     * @param serverName the OMAG Server's name, which the server connector calls
     * @param deployedImplementationType kind of OMAG Server
     * @param connectorProviderClassName the server connector to use
     * @return the catalogued elements
     */
    static CataloguedResource catalogServer(String label,
                                            String serverName,
                                            String deployedImplementationType,
                                            String connectorProviderClassName)
    {
        SoftwareServerProperties properties = new SoftwareServerProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SoftwareServer", label));
        properties.setDisplayName(serverName);
        properties.setResourceName(serverName);
        properties.setDescription("Catalogued by the Runtime Manager FVT.");
        properties.setDeployedImplementationType(deployedImplementationType);

        return catalog(label, properties, connectorProviderClassName, Map.of("serverName", serverName));
    }


    /**
     * Create the asset, its connection and endpoint, and link them to the connector type of the requested
     * connector.
     */
    private static CataloguedResource catalog(String                     label,
                                              OpenMetadataRootProperties assetProperties,
                                              String                     connectorProviderClassName,
                                              Map<String, Object>        configurationProperties)
    {
        String assetGUID = assetMaker.create("/assets", newElement(assetProperties));

        EndpointProperties endpointProperties = new EndpointProperties();

        endpointProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Endpoint", label));
        endpointProperties.setDisplayName("Runtime Manager FVT endpoint " + label);
        endpointProperties.setNetworkAddress(OMAGPlatformExtension.getPlatformURLRoot());

        String endpointGUID = connectionMaker.create("/endpoints", newElement(endpointProperties));

        ConnectionProperties connectionProperties = new ConnectionProperties();

        connectionProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Connection", label));
        connectionProperties.setDisplayName("Runtime Manager FVT connection " + label);
        connectionProperties.setConfigurationProperties(configurationProperties);

        String connectionGUID = connectionMaker.create("/connections", newElement(connectionProperties));

        connectionMaker.call("/connections/" + connectionGUID + "/connector-types/" + connectorTypeGUID(connectorProviderClassName) + "/attach",
                             new NewRelationshipRequestBody());
        connectionMaker.call("/connections/" + connectionGUID + "/endpoints/" + endpointGUID + "/attach",
                             new NewRelationshipRequestBody());
        connectionMaker.call("/assets/" + assetGUID + "/connections/" + connectionGUID + "/attach",
                             new NewRelationshipRequestBody());

        return new CataloguedResource(assetGUID, connectionGUID, endpointGUID);
    }


    /**
     * Remove a catalogued platform or server and its connection and endpoint.
     *
     * @param resource the elements to remove (may be null if set-up failed)
     */
    static void delete(CataloguedResource resource)
    {
        if (resource != null)
        {
            assetMaker.call("/assets/" + resource.assetGUID() + "/delete", new DeleteElementRequestBody());
            connectionMaker.call("/connections/" + resource.connectionGUID() + "/delete", new DeleteElementRequestBody());
            connectionMaker.call("/endpoints/" + resource.endpointGUID() + "/delete", new DeleteElementRequestBody());

            ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + resource.assetGUID() + "/retrieve");
            ViewServicesFvtTestSupport.assertGone(connectionMaker, "/connections/" + resource.connectionGUID() + "/retrieve");
        }
    }


    /**
     * Find the connector type that the CoreContentPack defines for a connector provider.
     *
     * @param connectorProviderClassName provider class
     * @return connector type GUID
     */
    static String connectorTypeGUID(String connectorProviderClassName)
    {
        FilterRequestBody requestBody = new FilterRequestBody();

        requestBody.setFilter(connectorProviderClassName);

        List<OpenMetadataRootElement> connectorTypes = connectionMaker.elements("/connector-types/by-connector-provider-class-name", requestBody);

        assertFalse(connectorTypes.isEmpty(), "No connector type for " + connectorProviderClassName);

        return connectorTypes.get(0).getElementHeader().getGUID();
    }


    /**
     * Wrap properties in a create request for an element that is its own anchor.
     *
     * @param properties properties
     * @return request body
     */
    static NewElementRequestBody newElement(OpenMetadataRootProperties properties)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return requestBody;
    }


    /*
     * ====================================================================================================
     * Raw requests, read as JSON.
     */


    /**
     * Send a request to the Runtime Manager and return its response as JSON, unchecked.
     *
     * @param method HTTP method
     * @param path path below the Runtime Manager root
     * @param requestBody body (may be null); a String is sent as text/plain, anything else as JSON
     * @return the response
     */
    static JsonNode exchange(HttpMethod method,
                             String     path,
                             Object     requestBody)
    {
        String url = OMAGPlatformExtension.getPlatformURLRoot()
                + "/servers/" + OMAGPlatformExtension.VIEW_SERVER_NAME
                + "/api/open-metadata/" + ViewServiceDescription.RUNTIME_MANAGER.getViewServiceURLMarker()
                + path;

        HttpHeaders headers = new HttpHeaders();

        if (requestBody instanceof String)
        {
            headers.setContentType(MediaType.TEXT_PLAIN);
        }
        else if (requestBody != null)
        {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        JsonNode response = restTemplate.exchange(url, method, new HttpEntity<>(requestBody, headers), JsonNode.class).getBody();

        assertNotNull(response, "Runtime Manager returned no response to " + method + " " + path);

        return response;
    }


    /**
     * Send a request and check that it succeeded.
     *
     * @param method HTTP method
     * @param path path below the Runtime Manager root
     * @param requestBody body (may be null)
     * @return the response
     */
    static JsonNode succeed(HttpMethod method,
                            String     path,
                            Object     requestBody)
    {
        JsonNode response = exchange(method, path, requestBody);

        assertEquals(200,
                     response.path("relatedHTTPCode").asInt(),
                     "Runtime Manager refused " + method + " " + path + ": " + response.path("exceptionErrorMessage").asText()
                             + " [" + response.path("exceptionClassName").asText() + "]");

        return response;
    }


    /**
     * Send a request that is expected to be refused and return the response so the caller can check why.
     *
     * @param method HTTP method
     * @param path path below the Runtime Manager root
     * @param requestBody body (may be null)
     * @return the response
     */
    static JsonNode refuse(HttpMethod method,
                           String     path,
                           Object     requestBody)
    {
        JsonNode response = exchange(method, path, requestBody);

        assertNotEquals(200, response.path("relatedHTTPCode").asInt(),
                        "Runtime Manager accepted " + method + " " + path + ", which should have been refused");

        return response;
    }
}
