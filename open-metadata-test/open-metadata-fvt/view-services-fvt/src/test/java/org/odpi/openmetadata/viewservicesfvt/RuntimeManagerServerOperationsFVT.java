/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.client.MetadataAccessStoreConfigurationClient;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RuntimeManagerServerOperationsFVT covers the Runtime Manager requests that operate an OMAG Server through
 * its catalogue entry: start it, report on it, load open metadata archives into it, connect it to cohorts,
 * shut it down and unregister it.
 * <br>
 * Starting and stopping the suite's own servers would break every other test, so this class configures a
 * third, private metadata access store on the suite's platform (in-memory, with only the open metadata
 * types it loads itself at start-up), catalogues it with a connection that points at the platform, and drives that server through
 * the Runtime Manager from first start to unregistration.  Each step is checked against the platform
 * itself, and each archive load against the server's own type store.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RuntimeManagerServerOperationsFVT
{
    private static final String SERVER_NAME = "viewServicesFvtRuntimeManagerStore";
    private static final String COHORT_NAME = "viewServicesFvtNoSuchCohort";

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final RestTemplate restTemplate = new RestTemplate();

    private static RuntimeManagerFvtSupport.CataloguedResource server = null;


    /**
     * Configure the private metadata access store (without starting it) and catalogue it.
     *
     * @throws Exception configuration failure
     */
    @Test
    @Order(1)
    void configureAndCatalogServer() throws Exception
    {
        MetadataAccessStoreConfigurationClient configurationClient = new MetadataAccessStoreConfigurationClient(SERVER_NAME,
                                                                                                                  OMAGPlatformExtension.getPlatformURLRoot(),
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  OMAGPlatformExtension.USER_ID,
                                                                                                                  null);

        configurationClient.clearOMAGServerConfig();
        configurationClient.setServerUserId(OMAGPlatformExtension.USER_ID);
        configurationClient.setBasicServerProperties("Egeria view-services-fvt",
                                                     "Metadata access store started and stopped by the Runtime Manager FVT.",
                                                     OMAGPlatformExtension.USER_ID,
                                                     "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider",
                                                     "build/view-services-fvt-data/secrets.omsecrets",
                                                     "view-services-fvt",
                                                     OMAGPlatformExtension.getPlatformURLRoot(),
                                                     1000);
        configurationClient.setInMemLocalRepository();
        configurationClient.addConsoleAuditLogDestination(new ArrayList<>());

        assertFalse(platformServices().getActiveServers().contains(SERVER_NAME), SERVER_NAME + " is already running");

        server = RuntimeManagerFvtSupport.catalogServer("runtime-manager-store",
                                                        SERVER_NAME,
                                                        RuntimeManagerFvtSupport.METADATA_ACCESS_SERVER,
                                                        RuntimeManagerFvtSupport.METADATA_ACCESS_SERVER_PROVIDER);
    }


    /**
     * The server starts from its stored configuration and then reports itself running.
     *
     * @throws Exception platform query failure
     */
    @Test
    @Order(2)
    void activateServer() throws Exception
    {
        JsonNode response = RuntimeManagerFvtSupport.succeed(HttpMethod.POST, "/omag-servers/" + server.assetGUID() + "/instance", null);

        assertTrue(response.path("successMessage").asText().contains(SERVER_NAME),
                   "activateWithStoredConfig returned an unexpected message: " + response.path("successMessage").asText());
        assertTrue(platformServices().getActiveServers().contains(SERVER_NAME), SERVER_NAME + " did not start");

        JsonNode report = RuntimeManagerFvtSupport.succeed(HttpMethod.GET, "/omag-servers/" + server.assetGUID() + "/instance/report", null).path("element");

        assertEquals(SERVER_NAME, report.path("serverName").asText());
        assertEquals("RUNNING", report.path("serverActiveStatus").asText());
    }


    /**
     * Loading archive files through both forms of the request adds the types each archive defines: the
     * CocoTypesArchive content pack, then an archive file written by this test.
     *
     * @throws IOException the archive file cannot be written
     */
    @Test
    @Order(3)
    void loadArchiveFiles() throws IOException
    {
        assertTrue(typeDefKnown("Asset"), SERVER_NAME + " did not load the open metadata types at start-up");
        assertFalse(typeDefKnown("BiopsyReport"), SERVER_NAME + " has the Coco types before CocoTypesArchive was loaded");

        RuntimeManagerFvtSupport.succeed(HttpMethod.POST,
                                         "/omag-servers/" + server.assetGUID() + "/instance/load/open-metadata-archives/file",
                                         contentPack("CocoTypesArchive.omarchive").getAbsolutePath());

        assertTrue(typeDefKnown("BiopsyReport"), "CocoTypesArchive.omarchive did not load");

        String typeName    = newTypeName();
        File   archiveFile = new File("build/view-services-fvt-data/" + typeName + ".omarchive").getAbsoluteFile();

        archiveFile.getParentFile().mkdirs();
        objectMapper.writeValue(archiveFile, archiveDefining(typeName));

        assertFalse(typeDefKnown(typeName));

        RuntimeManagerFvtSupport.succeed(HttpMethod.POST,
                                         "/metadata-access-stores/" + server.assetGUID() + "/instance/load/open-metadata-archives/file",
                                         archiveFile.getAbsolutePath());

        assertTrue(typeDefKnown(typeName), archiveFile.getName() + " did not load");

        archiveFile.delete();
    }


    /**
     * Archives passed in the request body, through both forms of the request, add their types too.
     *
     * @throws IOException the template archive cannot be read
     */
    @Test
    @Order(4)
    void loadArchiveContent() throws IOException
    {
        for (String path : new String[]{ "/omag-servers/", "/metadata-access-stores/" })
        {
            String typeName = newTypeName();

            assertFalse(typeDefKnown(typeName));

            RuntimeManagerFvtSupport.succeed(HttpMethod.POST,
                                             path + server.assetGUID() + "/instance/load/open-metadata-archives/archive-content",
                                             archiveDefining(typeName));

            assertTrue(typeDefKnown(typeName), "archive content loaded through " + path + " did not add " + typeName);
        }
    }


    /**
     * The private server is not configured to join any cohort, so connect, disconnect and unregister (both
     * the GET and POST forms) all succeed but report false: the server has no cohort of that name to act on.
     * Joining a real cohort would need an event bus, which this suite does not have.
     */
    @Test
    @Order(5)
    void cohortRequestsFindNoCohort()
    {
        for (String operation : new String[]{ "connect", "disconnect", "unregister" })
        {
            String path = "/cohort-members/" + server.assetGUID() + "/cohorts/" + COHORT_NAME + "/" + operation;

            for (HttpMethod method : new HttpMethod[]{ HttpMethod.GET, HttpMethod.POST })
            {
                JsonNode response = RuntimeManagerFvtSupport.succeed(method, path, (method == HttpMethod.POST) ? Map.of("class", "NullRequestBody") : null);

                assertFalse(response.path("flag").asBoolean(true),
                            method + " " + operation + " reported acting on a cohort the server does not belong to: " + response);
            }
        }
    }


    /**
     * The server shuts down, then restarts.
     *
     * @throws Exception platform query failure
     */
    @Test
    @Order(6)
    void shutdownAndRestartServer() throws Exception
    {
        RuntimeManagerFvtSupport.succeed(HttpMethod.DELETE, "/omag-servers/" + server.assetGUID() + "/instance", null);

        assertFalse(platformServices().getActiveServers().contains(SERVER_NAME), SERVER_NAME + " is still running after shutdownServer");
        assertTrue(platformServices().isServerKnown(SERVER_NAME), SERVER_NAME + " was unregistered by shutdownServer");

        RuntimeManagerFvtSupport.succeed(HttpMethod.POST, "/omag-servers/" + server.assetGUID() + "/instance", null);

        assertTrue(platformServices().getActiveServers().contains(SERVER_NAME), SERVER_NAME + " did not restart");
    }


    /**
     * Shutting down and unregistering the server removes it from the platform.
     *
     * @throws Exception platform query failure
     */
    @Test
    @Order(7)
    void shutdownAndUnregisterServer() throws Exception
    {
        RuntimeManagerFvtSupport.succeed(HttpMethod.DELETE, "/omag-servers/" + server.assetGUID(), null);

        assertFalse(platformServices().getActiveServers().contains(SERVER_NAME), SERVER_NAME + " is still running after shutdownAndUnregisterServer");
        assertTrue(platformServices().isServerKnown(SERVER_NAME),
                   "shutdownAndUnregisterServer unregisters the server from its cohorts; it should keep the server's configuration");
    }


    /**
     * Remove the catalogue entry and the private server's configuration.
     *
     * @throws Exception configuration failure
     */
    @Test
    @Order(99)
    void cleanUp() throws Exception
    {
        RuntimeManagerFvtSupport.delete(server);

        new MetadataAccessStoreConfigurationClient(SERVER_NAME,
                                                   OMAGPlatformExtension.getPlatformURLRoot(),
                                                   null,
                                                   null,
                                                   null,
                                                   OMAGPlatformExtension.USER_ID,
                                                   null).clearOMAGServerConfig();
    }


    private static String newTypeName()
    {
        return "ViewServicesFvtType" + UUID.randomUUID().toString().substring(0, 8);
    }


    private static PlatformServicesClient platformServices() throws Exception
    {
        return new PlatformServicesClient("view-services-fvt Platform",
                                          OMAGPlatformExtension.getPlatformURLRoot(),
                                          null,
                                          null,
                                          null,
                                          OMAGPlatformExtension.USER_ID,
                                          null);
    }


    /**
     * Ask the private server's own repository whether it knows a type.
     *
     * @param typeName type to look up
     * @return whether the type is defined
     */
    private static boolean typeDefKnown(String typeName)
    {
        JsonNode response = restTemplate.postForObject(OMAGPlatformExtension.getPlatformURLRoot() + "/servers/" + SERVER_NAME
                                                               + "/open-metadata/repository-services/users/" + OMAGPlatformExtension.USER_ID
                                                               + "/types/typedef/name/" + typeName,
                                                       Map.of("class", "GetRequest"),
                                                       JsonNode.class);

        assertNotNull(response);

        return (response.path("relatedHTTPCode").asInt() == 200) && typeName.equals(response.path("typeDef").path("name").asText());
    }


    /**
     * Build a small archive that defines one new entity type, from the CocoTypesArchive's BiopsyReport.
     *
     * @param typeName name of the new type
     * @return archive content
     * @throws IOException the template archive cannot be read
     */
    private static JsonNode archiveDefining(String typeName) throws IOException
    {
        ObjectNode archive     = (ObjectNode) objectMapper.readTree(contentPack("CocoTypesArchive.omarchive"));
        String     archiveGUID = UUID.randomUUID().toString();

        ObjectNode archiveProperties = (ObjectNode) archive.path("archiveProperties");

        archiveProperties.put("archiveGUID", archiveGUID);
        archiveProperties.put("archiveName", typeName + "Archive");
        archiveProperties.put("archiveDescription", "Defines " + typeName + " for the Runtime Manager FVT.");

        ObjectNode typeStore  = (ObjectNode) archive.path("archiveTypeStore");
        ObjectNode entityDef  = (ObjectNode) typeStore.path("newTypeDefs").path(0).deepCopy();
        ArrayNode  properties = objectMapper.createArrayNode();

        /*
         * Keep only the string attribute so the new type does not depend on the Coco enum.
         */
        entityDef.path("propertiesDefinition").forEach(attribute ->
        {
            if ("PRIMITIVE".equals(attribute.path("attributeType").path("category").asText()))
            {
                properties.add(attribute);
            }
        });

        entityDef.put("guid", UUID.randomUUID().toString());
        entityDef.put("name", typeName);
        entityDef.put("origin", archiveGUID);
        entityDef.put("description", "A type defined by the Runtime Manager FVT.");
        entityDef.set("propertiesDefinition", properties);

        typeStore.remove("attributeTypeDefs");
        typeStore.set("newTypeDefs", objectMapper.createArrayNode().add(entityDef));

        return archive;
    }


    /**
     * Locate a file in the repo's content-packs directory.
     *
     * @param fileName archive file name
     * @return the file
     */
    private static File contentPack(String fileName)
    {
        File candidate = new File(System.getProperty("user.dir")).getAbsoluteFile();

        while (candidate != null)
        {
            File archive = new File(new File(candidate, "content-packs"), fileName);

            if (archive.isFile())
            {
                return archive;
            }

            candidate = candidate.getParentFile();
        }

        throw new IllegalStateException("Could not find content-packs/" + fileName + " above " + System.getProperty("user.dir"));
    }
}
