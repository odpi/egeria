/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.RuntimeManagerFvtSupport.runtimeManager;

/**
 * RuntimeManagerPlatformsFVT covers the Runtime Manager requests that find the catalogued platforms and
 * servers, and the reports that describe them as they are running.
 * <br>
 * The suite's own platform, metadata access store and view server are catalogued with connections that
 * point at them, so the platform report, connector type lookup and server reports reach real, running
 * Egeria components and their results can be checked against what the suite knows about them.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RuntimeManagerPlatformsFVT
{
    private static final String PLATFORM_NAME = "View services FVT platform " + OMAGPlatformExtension.RUN_ID;

    private static RuntimeManagerFvtSupport.CataloguedResource platform      = null;
    private static RuntimeManagerFvtSupport.CataloguedResource metadataStore = null;
    private static RuntimeManagerFvtSupport.CataloguedResource viewServer    = null;


    /**
     * Catalogue the platform and its two servers.
     */
    @Test
    @Order(1)
    void catalogPlatformAndServers()
    {
        platform      = RuntimeManagerFvtSupport.catalogPlatform("platform", PLATFORM_NAME);
        metadataStore = RuntimeManagerFvtSupport.catalogServer("metadata-store",
                                                               OMAGPlatformExtension.METADATA_STORE_NAME,
                                                               RuntimeManagerFvtSupport.METADATA_ACCESS_SERVER,
                                                               RuntimeManagerFvtSupport.METADATA_ACCESS_SERVER_PROVIDER);
        viewServer    = RuntimeManagerFvtSupport.catalogServer("view-server",
                                                               OMAGPlatformExtension.VIEW_SERVER_NAME,
                                                               RuntimeManagerFvtSupport.VIEW_SERVER,
                                                               RuntimeManagerFvtSupport.VIEW_SERVER_PROVIDER);
    }


    /**
     * The platform is found by name, by deployed implementation type (but not among the templates) and by
     * its unique identifier.
     */
    @Test
    @Order(2)
    void findPlatform()
    {
        assertTrue(guids(runtimeManager.elements("/platforms/by-name", byName(PLATFORM_NAME))).contains(platform.assetGUID()),
                   "getPlatformsByName did not return the catalogued platform");

        assertTrue(guids(runtimeManager.elements("/platforms/by-deployed-implementation-type",
                                                 byName(RuntimeManagerFvtSupport.OMAG_SERVER_PLATFORM))).contains(platform.assetGUID()),
                   "getPlatformsByDeployedImplType did not return the catalogued platform");
        assertFalse(guids(runtimeManager.elements("/platforms/by-deployed-implementation-type?getTemplates=true",
                                                  byName(RuntimeManagerFvtSupport.OMAG_SERVER_PLATFORM))).contains(platform.assetGUID()),
                    "getPlatformsByDeployedImplType returned the platform as a template");

        OpenMetadataRootElement element = runtimeManager.element("/platforms/" + platform.assetGUID(), new GetRequestBody());

        assertNotNull(element, "getPlatformByGUID returned nothing");
        assertEquals(platform.assetGUID(), element.getElementHeader().getGUID());

        SoftwareServerPlatformProperties properties = assertInstanceOf(SoftwareServerPlatformProperties.class, element.getProperties());

        assertEquals(PLATFORM_NAME, properties.getResourceName());
        assertEquals(RuntimeManagerFvtSupport.OMAG_SERVER_PLATFORM, properties.getDeployedImplementationType());
    }


    /**
     * The platform report comes from the running platform through the catalogued connection: it carries the
     * catalogued name, the platform's URL root and origin, and the suite's two running servers.
     */
    @Test
    @Order(3)
    void platformReport()
    {
        JsonNode report = RuntimeManagerFvtSupport.succeed(HttpMethod.GET, "/platforms/" + platform.assetGUID() + "/report", null).path("element");

        assertEquals(PLATFORM_NAME, report.path("defaultPlatformName").asText());
        assertEquals(OMAGPlatformExtension.getPlatformURLRoot(), report.path("platformURLRoot").asText());
        assertFalse(report.path("platformOrigin").asText().isBlank(), "platform report has no origin");
        assertTrue(report.path("registeredOMAGServices").size() > 0, "platform report lists no registered services");

        List<String> serverNames = new ArrayList<>();

        /*
         * OMAGServerPlatformProperties.getOMAGServers() puts the servers under the JSON name "omagservers".
         */

        report.path("omagservers").forEach(server -> serverNames.add(server.path("serverName").asText()));

        assertTrue(serverNames.contains(OMAGPlatformExtension.METADATA_STORE_NAME), "platform report omits the metadata store: " + serverNames);
        assertTrue(serverNames.contains(OMAGPlatformExtension.VIEW_SERVER_NAME), "platform report omits the view server: " + serverNames);
    }


    /**
     * The platform describes the connector type of a connector provider it can load.
     */
    @Test
    @Order(4)
    void connectorType()
    {
        JsonNode connectorType = RuntimeManagerFvtSupport.succeed(HttpMethod.GET,
                                                                  "/platforms/" + platform.assetGUID() + "/connector-types/" + RuntimeManagerFvtSupport.PLATFORM_PROVIDER,
                                                                  null).path("connectorType");

        assertEquals(RuntimeManagerFvtSupport.PLATFORM_PROVIDER, connectorType.path("connectorProviderClassName").asText());
        assertEquals(RuntimeManagerFvtSupport.connectorTypeGUID(RuntimeManagerFvtSupport.PLATFORM_PROVIDER), connectorType.path("guid").asText(),
                     "the platform's connector type does not match the one in the CoreContentPack");
    }


    /**
     * The servers are found by name, by deployed implementation type (but not among the templates) and by
     * their unique identifiers.
     */
    @Test
    @Order(5)
    void findServers()
    {
        assertTrue(guids(runtimeManager.elements("/software-servers/by-name", byName(OMAGPlatformExtension.METADATA_STORE_NAME))).contains(metadataStore.assetGUID()),
                   "getServersByName did not return the catalogued metadata store");
        assertFalse(guids(runtimeManager.elements("/software-servers/by-name", byName(OMAGPlatformExtension.METADATA_STORE_NAME))).contains(viewServer.assetGUID()),
                    "getServersByName returned a server with a different name");

        assertTrue(guids(runtimeManager.elements("/software-servers/by-deployed-implementation-type",
                                                 byName(RuntimeManagerFvtSupport.VIEW_SERVER))).contains(viewServer.assetGUID()),
                   "getServersByDeployedImplType did not return the catalogued view server");
        assertFalse(guids(runtimeManager.elements("/software-servers/by-deployed-implementation-type?getTemplates=true",
                                                  byName(RuntimeManagerFvtSupport.VIEW_SERVER))).contains(viewServer.assetGUID()),
                    "getServersByDeployedImplType returned the view server as a template");

        OpenMetadataRootElement element = runtimeManager.element("/software-servers/" + metadataStore.assetGUID(), new GetRequestBody());

        assertNotNull(element, "getServerByGUID returned nothing");

        SoftwareServerProperties properties = assertInstanceOf(SoftwareServerProperties.class, element.getProperties());

        assertEquals(OMAGPlatformExtension.METADATA_STORE_NAME, properties.getResourceName());
        assertEquals(RuntimeManagerFvtSupport.METADATA_ACCESS_SERVER, properties.getDeployedImplementationType());
    }


    /**
     * Each server's report comes from the running server through its catalogued connection.
     */
    @Test
    @Order(6)
    void serverReports()
    {
        for (RuntimeManagerFvtSupport.CataloguedResource server : List.of(metadataStore, viewServer))
        {
            String expectedName = (server == metadataStore) ? OMAGPlatformExtension.METADATA_STORE_NAME : OMAGPlatformExtension.VIEW_SERVER_NAME;

            JsonNode report = RuntimeManagerFvtSupport.succeed(HttpMethod.GET, "/omag-servers/" + server.assetGUID() + "/instance/report", null).path("element");

            assertEquals(expectedName, report.path("serverName").asText());
            assertEquals("RUNNING", report.path("serverActiveStatus").asText(), "server report for " + expectedName);
        }
    }


    /**
     * Remove the catalogue entries.
     */
    @Test
    @Order(99)
    void cleanUp()
    {
        RuntimeManagerFvtSupport.delete(viewServer);
        RuntimeManagerFvtSupport.delete(metadataStore);
        RuntimeManagerFvtSupport.delete(platform);
    }


    private static FilterRequestBody byName(String name)
    {
        FilterRequestBody requestBody = new FilterRequestBody();

        requestBody.setFilter(name);

        return requestBody;
    }


    private static List<String> guids(List<OpenMetadataRootElement> elements)
    {
        return elements.stream().map(element -> element.getElementHeader().getGUID()).toList();
    }
}
