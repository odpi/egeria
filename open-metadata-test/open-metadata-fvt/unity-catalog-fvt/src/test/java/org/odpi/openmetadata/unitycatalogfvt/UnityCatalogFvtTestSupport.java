/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.unitycatalogfvt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared conventions and helpers for the unity-catalog-fvt suite: which Unity Catalog servers it talks to,
 * how the elements the connectors create are named, how it builds the fixtures it needs on the writable
 * server, and how it waits for work that is being done by another server.
 * <br>
 * Two Unity Catalog servers are used, and they are treated very differently.  The <b>read-only</b> server is
 * catalogued, synchronized and surveyed, and nothing is ever written to it - it may well be in use by
 * something else, so the suite relies only on the standard sample it ships with.  The <b>writable</b> server
 * is where the suite builds a catalog of its own, so that it can change Unity Catalog underneath the
 * synchronizer.  Every catalog it creates there starts with the configured prefix, and only catalogs with
 * that prefix are ever deleted.
 * <br>
 * Debris is cleared at the <em>start</em> of a run rather than the end.  Clearing up afterwards leaves the
 * debris behind whenever a run is killed or crashes - which is exactly when it is most likely to be in a
 * state the next run should not inherit.
 */
final class UnityCatalogFvtTestSupport
{
    /**
     * Marker carried by every server asset this suite creates.  The catalog templates build the server's
     * qualified name from its name, so a search for this string finds the servers a run produced.
     */
    static final String TEST_MARKER = "unity-catalog-fvt";

    /**
     * Page size used by every client this suite creates, and configured on every server it starts.
     */
    static final int MAX_PAGE_SIZE = 500;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient   httpClient   = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    private UnityCatalogFvtTestSupport()
    {
        // no instances
    }


    /*
     * =====================================================================================================
     * The Unity Catalog servers under test
     */


    /**
     * Return the host URL (with protocol) of the read-only Unity Catalog server.
     *
     * @return host URL
     */
    static String getReadOnlyHostURL()
    {
        return OMAGPlatformExtension.getProperty("unitycatalog.fvt.readonly.host.url", "http://localhost");
    }


    /**
     * Return the port of the read-only Unity Catalog server.
     *
     * @return port number
     */
    static String getReadOnlyPort()
    {
        return OMAGPlatformExtension.getProperty("unitycatalog.fvt.readonly.port", "8087");
    }


    /**
     * Return the host URL (with protocol) of the writable Unity Catalog server.
     *
     * @return host URL
     */
    static String getWritableHostURL()
    {
        return OMAGPlatformExtension.getProperty("unitycatalog.fvt.writable.host.url", "http://localhost");
    }


    /**
     * Return the port of the writable Unity Catalog server.
     *
     * @return port number
     */
    static String getWritablePort()
    {
        return OMAGPlatformExtension.getProperty("unitycatalog.fvt.writable.port", "8187");
    }


    /**
     * Return the name of the sample catalog the read-only server is expected to hold.
     *
     * @return catalog name
     */
    static String getSampleCatalogName()
    {
        return OMAGPlatformExtension.getProperty("unitycatalog.fvt.sample.catalog", "unity");
    }


    /**
     * Return the name of the schema inside the sample catalog.
     *
     * @return schema name
     */
    static String getSampleSchemaName()
    {
        return OMAGPlatformExtension.getProperty("unitycatalog.fvt.sample.schema", "default");
    }


    /**
     * Return the name to use for a catalog this suite creates on the writable server.  The prefix is what
     * marks a catalog as this suite's to delete.
     *
     * @param purpose short label for what the catalog is for, for example "sync"
     * @return catalog name
     */
    static String fixtureCatalogName(String purpose)
    {
        return OMAGPlatformExtension.getProperty("unitycatalog.fvt.catalog.prefix", "egeria_ucfvt") + "_" + purpose;
    }


    /**
     * Return the network address a Unity Catalog server's catalog template gives its endpoint.  The
     * synchronizers build every qualified name inside the server from this address.
     *
     * @param hostURL host URL with protocol
     * @param port port number
     * @return network address
     */
    static String serverNetworkAddress(String hostURL,
                                       String port)
    {
        return hostURL + ":" + port;
    }


    /*
     * =====================================================================================================
     * Naming
     */


    /**
     * Return the name to use for a Unity Catalog server asset created by one test.  Each test uses a
     * different name because the catalog template builds the asset's qualified name from it.
     *
     * @param purpose short label for what the test is doing, for example "catalog"
     * @return server name
     */
    static String serverUnderTestName(String purpose)
    {
        return TEST_MARKER + "-" + purpose;
    }


    /**
     * Return the qualified name the Unity Catalog server catalog template gives a server asset.
     *
     * @param serverName server name supplied to the template
     * @return qualified name
     */
    static String serverAssetQualifiedName(String serverName)
    {
        return UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getDeployedImplementationType() + "::" + serverName;
    }


    /**
     * Return the qualified name the synchronizers give an element found inside a Unity Catalog server.  It is
     * built from the server's network address and the element's Unity Catalog full name - not from the server
     * asset's name - which is why no two server assets in this suite catalogue the same server at once.
     *
     * @param type deployed implementation type of the element
     * @param networkAddress network address of the Unity Catalog server
     * @param fullName dotted Unity Catalog full name, for example "unity.default.numbers"
     * @return qualified name
     */
    static String ucElementQualifiedName(UnityCatalogDeployedImplementationType type,
                                         String                                 networkAddress,
                                         String                                 fullName)
    {
        return type.getDeployedImplementationType() + "::" + networkAddress + "::" + fullName;
    }


    /**
     * Return the placeholder values the Unity Catalog server catalog template needs.
     *
     * @param serverName name of the server asset
     * @param hostURL host URL with protocol
     * @param port port number
     * @return placeholder values
     */
    static Map<String, String> serverTemplatePlaceholders(String serverName,
                                                          String hostURL,
                                                          String port)
    {
        Map<String, String> placeholders = new HashMap<>();

        placeholders.put(PlaceholderProperty.HOST_URL.getName(), hostURL);
        placeholders.put(PlaceholderProperty.PORT_NUMBER.getName(), port);
        placeholders.put(PlaceholderProperty.SERVER_NAME.getName(), serverName);
        placeholders.put(PlaceholderProperty.RESOURCE_NAME.getName(), serverName);
        placeholders.put(PlaceholderProperty.DESCRIPTION.getName(), "Unity Catalog server catalogued by the " + TEST_MARKER + " suite.");
        placeholders.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), "0.3");

        return placeholders;
    }


    /*
     * =====================================================================================================
     * Repository clean-up
     */


    /**
     * Empty the repository this suite's metadata access store runs on, by dropping and recreating its
     * PostgreSQL schema.
     * <br>
     * This replaces clearing up through the metadata APIs, which cannot do the job: a survey's annotations are
     * anchored to their survey report and named after what they measured, so neither a marker sweep nor a
     * type sweep finds them.  It also stops a new engine host from picking up a previous run's abandoned
     * engine actions as it starts.
     *
     * @throws Exception the schema could not be recreated, which would make the run's results meaningless
     */
    static void emptyRepository() throws Exception
    {
        if (! OMAGPlatformExtension.getBooleanProperty("unitycatalog.fvt.clear.down", true))
        {
            System.out.println("unity-catalog-fvt: leaving the previous run's repository in place -"
                                       + " unitycatalog.fvt.clear.down is false.  Tests that assert on what this run created may"
                                       + " see an earlier run's elements too.");
            return;
        }

        String userId   = OMAGPlatformExtension.getRepositorySecret("userId");
        String password = OMAGPlatformExtension.getRepositorySecret("clearPassword");

        if (userId == null)
        {
            throw new IllegalStateException("No credentials for the repository's PostgreSQL server - check that"
                                                    + " unitycatalog.fvt.repository.secrets.store names a readable secrets store and that"
                                                    + " unitycatalog.fvt.repository.secrets.collection names a collection inside it.");
        }

        String schemaName = "repository_" + OMAGPlatformExtension.METADATA_STORE_NAME;
        String url        = OMAGPlatformExtension.getProperty("unitycatalog.fvt.repository.database.url",
                                                              "jdbc:postgresql://localhost:5442/egeria");

        try (Connection connection = DriverManager.getConnection(url, userId, password);
             Statement  statement  = connection.createStatement())
        {
            statement.execute("DROP SCHEMA IF EXISTS " + schemaName + " CASCADE");
            statement.execute("CREATE SCHEMA " + schemaName);
        }

        System.out.println("unity-catalog-fvt: repository schema " + schemaName + " recreated empty");
    }


    /**
     * Permanently remove an element, whatever status it is in.  Failures are swallowed: this is best-effort
     * clean-up, not something a test should fail on.  PURGE only succeeds on an element that is already
     * soft-deleted, so this soft-deletes first.
     *
     * @param openMetadataStore store to delete through
     * @param elementGUID element to remove
     */
    static void purgeElement(OpenMetadataStore openMetadataStore,
                             String            elementGUID)
    {
        try
        {
            DeleteOptions softDeleteOptions = new DeleteOptions();

            softDeleteOptions.setDeleteMethod(DeleteMethod.SOFT_DELETE);
            softDeleteOptions.setCascadedDelete(true);
            softDeleteOptions.setForLineage(true);

            openMetadataStore.deleteMetadataElementInStore(elementGUID, softDeleteOptions);
        }
        catch (Exception ignored)
        {
            // Best-effort - the element may already be deleted, for example by a cascade from its own anchor.
        }

        try
        {
            DeleteOptions purgeOptions = new DeleteOptions();

            purgeOptions.setDeleteMethod(DeleteMethod.PURGE);
            purgeOptions.setCascadedDelete(true);
            purgeOptions.setForLineage(true);

            openMetadataStore.deleteMetadataElementInStore(elementGUID, purgeOptions);
        }
        catch (Exception ignored)
        {
            // Best-effort clean-up - nothing further can be done if this fails.
        }
    }


    /*
     * =====================================================================================================
     * Fixtures on the writable Unity Catalog server.  These go straight to Unity Catalog's REST API rather
     * than through the resource connector, so that setting up a test does not depend on the code under test.
     */


    /**
     * Return the root of the Unity Catalog REST API on the writable server.
     *
     * @return URL
     */
    private static String writableAPIRoot()
    {
        return serverNetworkAddress(getWritableHostURL(), getWritablePort()) + "/api/2.1/unity-catalog";
    }


    /**
     * Send one request to the writable server and insist that it worked.
     *
     * @param method HTTP method
     * @param path path below the API root
     * @param body JSON body, or null
     * @return response body
     * @throws Exception the request failed
     */
    private static String callWritableServer(String method,
                                             String path,
                                             String body) throws Exception
    {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(writableAPIRoot() + path))
                                                 .timeout(Duration.ofSeconds(30))
                                                 .header("Content-Type", "application/json");

        if (body == null)
        {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        }
        else
        {
            builder.method(method, HttpRequest.BodyPublishers.ofString(body));
        }

        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());

        if ((response.statusCode() < 200) || (response.statusCode() > 299))
        {
            throw new IllegalStateException(method + " " + writableAPIRoot() + path + " returned HTTP " + response.statusCode()
                                                    + ": " + response.body());
        }

        return response.body();
    }


    /**
     * List the names of the catalogs on a Unity Catalog server, straight from its REST API.  This is a read,
     * so it is safe against the read-only server too.
     *
     * @param networkAddress network address of the server
     * @return catalog names
     * @throws Exception the server could not be read
     */
    static List<String> listCatalogs(String networkAddress) throws Exception
    {
        List<String> catalogNames = new ArrayList<>();

        HttpRequest request = HttpRequest.newBuilder(URI.create(networkAddress + "/api/2.1/unity-catalog/catalogs"))
                                         .timeout(Duration.ofSeconds(30))
                                         .GET()
                                         .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        for (JsonNode catalog : objectMapper.readTree(response.body()).path("catalogs"))
        {
            catalogNames.add(catalog.path("name").asText());
        }

        return catalogNames;
    }


    /**
     * Delete a catalog on the writable server, with everything in it, if it exists.  Only catalogs carrying
     * this suite's prefix may be deleted - anything else on the server is not this suite's to remove.
     *
     * @param catalogName catalog to remove
     * @throws Exception the catalog exists and could not be deleted
     */
    static void deleteWritableCatalog(String catalogName) throws Exception
    {
        String prefix = OMAGPlatformExtension.getProperty("unitycatalog.fvt.catalog.prefix", "egeria_ucfvt");

        if (! catalogName.startsWith(prefix))
        {
            throw new IllegalArgumentException("Refusing to delete catalog " + catalogName + " - it does not start with " + prefix);
        }

        if (listCatalogs(serverNetworkAddress(getWritableHostURL(), getWritablePort())).contains(catalogName))
        {
            callWritableServer("DELETE", "/catalogs/" + catalogName + "?force=true", null);
        }
    }


    /**
     * Create a catalog on the writable server.
     *
     * @param catalogName name of the catalog
     * @throws Exception the request failed
     */
    static void createWritableCatalog(String catalogName) throws Exception
    {
        ObjectNode body = objectMapper.createObjectNode();

        body.put("name", catalogName);
        body.put("comment", "Created by the " + TEST_MARKER + " suite - safe to delete.");

        callWritableServer("POST", "/catalogs", objectMapper.writeValueAsString(body));
    }


    /**
     * Create a schema on the writable server.
     *
     * @param catalogName catalog to create it in
     * @param schemaName name of the schema
     * @throws Exception the request failed
     */
    static void createWritableSchema(String catalogName,
                                     String schemaName) throws Exception
    {
        ObjectNode body = objectMapper.createObjectNode();

        body.put("name", schemaName);
        body.put("catalog_name", catalogName);

        callWritableServer("POST", "/schemas", objectMapper.writeValueAsString(body));
    }


    /**
     * Create an external Delta table on the writable server, with an integer "id" column and a string
     * "description" column.  The storage location is never written to - Unity Catalog records it without
     * checking it.
     *
     * @param catalogName catalog to create it in
     * @param schemaName schema to create it in
     * @param tableName name of the table
     * @throws Exception the request failed
     */
    static void createWritableTable(String catalogName,
                                    String schemaName,
                                    String tableName) throws Exception
    {
        ObjectNode body = objectMapper.createObjectNode();

        body.put("name", tableName);
        body.put("catalog_name", catalogName);
        body.put("schema_name", schemaName);
        body.put("table_type", "EXTERNAL");
        body.put("data_source_format", "DELTA");
        body.put("storage_location", "/tmp/" + catalogName + "/" + schemaName + "/" + tableName);

        ArrayNode columns = body.putArray("columns");

        addColumn(columns, "id", "int", "integer", "INT", 0, false);
        addColumn(columns, "description", "string", "string", "STRING", 1, true);

        callWritableServer("POST", "/tables", objectMapper.writeValueAsString(body));
    }


    /**
     * Add one column definition to a create-table request.
     *
     * @param columns column array
     * @param name column name
     * @param typeText Unity Catalog type text
     * @param jsonType Spark JSON type name
     * @param typeName Unity Catalog type name
     * @param position column position
     * @param nullable can the column be null
     */
    private static void addColumn(ArrayNode columns,
                                  String    name,
                                  String    typeText,
                                  String    jsonType,
                                  String    typeName,
                                  int       position,
                                  boolean   nullable)
    {
        ObjectNode column = columns.addObject();

        column.put("name", name);
        column.put("type_text", typeText);
        column.put("type_json", "{\"name\":\"" + name + "\",\"type\":\"" + jsonType + "\",\"nullable\":" + nullable + ",\"metadata\":{}}");
        column.put("type_name", typeName);
        column.put("position", position);
        column.put("nullable", nullable);
    }


    /**
     * Delete a table on the writable server.
     *
     * @param catalogName catalog holding it
     * @param schemaName schema holding it
     * @param tableName name of the table
     * @throws Exception the request failed
     */
    static void deleteWritableTable(String catalogName,
                                    String schemaName,
                                    String tableName) throws Exception
    {
        callWritableServer("DELETE", "/tables/" + catalogName + "." + schemaName + "." + tableName, null);
    }


    /**
     * Create an external volume on the writable server.
     *
     * @param catalogName catalog to create it in
     * @param schemaName schema to create it in
     * @param volumeName name of the volume
     * @throws Exception the request failed
     */
    static void createWritableVolume(String catalogName,
                                     String schemaName,
                                     String volumeName) throws Exception
    {
        ObjectNode body = objectMapper.createObjectNode();

        body.put("name", volumeName);
        body.put("catalog_name", catalogName);
        body.put("schema_name", schemaName);
        body.put("volume_type", "EXTERNAL");
        body.put("storage_location", "/tmp/" + catalogName + "/" + schemaName + "/" + volumeName);

        callWritableServer("POST", "/volumes", objectMapper.writeValueAsString(body));
    }


    /*
     * =====================================================================================================
     * Waiting for work that is happening somewhere else
     */


    /**
     * A condition that a test is prepared to wait for.
     */
    @FunctionalInterface
    interface WaitableCondition
    {
        /**
         * Has the thing being waited for happened yet?
         *
         * @return true when it has
         * @throws Exception checking threw - treated as fatal rather than as "not yet"
         */
        boolean isMet() throws Exception;
    }


    /**
     * Wait until the supplied condition is true, or report that it never became true.
     *
     * @param description what is being waited for, used in the failure message
     * @param condition the thing being waited for
     * @throws Exception the condition never became true, or checking it threw
     */
    static void waitFor(String            description,
                        WaitableCondition condition) throws Exception
    {
        long timeoutMilliseconds = OMAGPlatformExtension.getLongProperty("unitycatalog.fvt.refresh.timeout.seconds", 180) * 1000;
        long pollMilliseconds    = OMAGPlatformExtension.getLongProperty("unitycatalog.fvt.refresh.poll.seconds", 2) * 1000;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        while (System.currentTimeMillis() < giveUpTime)
        {
            if (condition.isMet())
            {
                return;
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new AssertionError(description + " did not happen within " + (timeoutMilliseconds / 1000)
                                         + " seconds.  The audit log at build/unity-catalog-fvt-data/logs/audit.log says what the"
                                         + " servers were doing while this test waited.");
    }


    /**
     * Wait for an element with the supplied qualified name to appear, and return it.
     *
     * @param openMetadataStore store to search
     * @param qualifiedName qualified name to wait for
     * @param description what this element is, used in the failure message
     * @return the element
     * @throws Exception it never appeared
     */
    static OpenMetadataElement waitForElement(OpenMetadataStore openMetadataStore,
                                              String            qualifiedName,
                                              String            description) throws Exception
    {
        List<OpenMetadataElement> holder = new ArrayList<>();

        waitFor(description + " (" + qualifiedName + ")",
                () ->
                {
                    OpenMetadataElement element = openMetadataStore.getMetadataElementByUniqueName(qualifiedName,
                                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name);

                    if (element != null)
                    {
                        holder.add(element);
                        return true;
                    }

                    return false;
                });

        return holder.get(0);
    }


    /**
     * Wait for an element with the supplied qualified name to disappear.
     *
     * @param openMetadataStore store to search
     * @param qualifiedName qualified name to wait for
     * @param description what this element is, used in the failure message
     * @throws Exception it never went away
     */
    static void waitForElementToGo(OpenMetadataStore openMetadataStore,
                                   String            qualifiedName,
                                   String            description) throws Exception
    {
        waitFor(description + " to be removed (" + qualifiedName + ")",
                () -> openMetadataStore.getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name) == null);
    }


    /*
     * =====================================================================================================
     * Assertions shared by more than one test
     */


    /**
     * Return one string property of an element, or null if it does not have one by that name.
     *
     * @param element element to read
     * @param propertyName name of the property wanted
     * @return property value as a string, or null
     */
    static String getStringProperty(OpenMetadataElement element,
                                    String              propertyName)
    {
        if ((element == null) || (element.getElementProperties() == null))
        {
            return null;
        }

        Map<String, String> properties = element.getElementProperties().getPropertiesAsStrings();

        return (properties == null) ? null : properties.get(propertyName);
    }


    /**
     * Search one set of properties for placeholder markers that were never substituted.  Finding one means a
     * catalog template's substitution did not happen: the element is carrying a variable name where a real
     * value belongs.
     *
     * @param location where these properties were found, for the failure message
     * @param properties properties to search, may be null
     * @return one description per property still holding a marker, empty if there are none
     */
    static List<String> findPlaceholders(String            location,
                                         ElementProperties properties)
    {
        List<String> found = new ArrayList<>();

        if ((properties == null) || (properties.getPropertiesAsStrings() == null))
        {
            return found;
        }

        for (Map.Entry<String, String> property : properties.getPropertiesAsStrings().entrySet())
        {
            String value = property.getValue();

            if ((value != null) && value.contains("~{") && value.contains("}~"))
            {
                found.add(location + " -> " + property.getKey() + " = \"" + value + "\"");
            }
        }

        return found;
    }
}
