/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.secretsstore.yaml;

import com.sun.net.httpserver.HttpServer;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreCollectionProperty;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Verify that requesting a token through the token API does not leave an HttpClient behind.  The connector used to
 * create a new HttpClient for every token request and never close it, so each request left a selector thread and
 * connection pool running until the client happened to be garbage collected.
 */
public class TokenAPIClientTest
{
    private static final String COLLECTION             = "tokenCollection";
    private static final String MAPPED_COLLECTION      = "mappedTokenCollection";
    private static final String TOKEN                  = "test-token";
    private static final String MAPPED_TOKEN           = "mapped-test-token";
    private static final String SELECTOR_THREAD_PREFIX = "HttpClient-";
    private static final String SELECTOR_THREAD_SUFFIX = "-SelectorManager";
    private static final int    CONNECTOR_COUNT        = 20;

    private final AtomicInteger tokenRequests       = new AtomicInteger();
    private final AtomicInteger mappedTokenRequests = new AtomicInteger();

    private HttpServer server;
    private Path       secretsFile;


    @BeforeClass
    public void startTokenServer() throws Exception
    {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);

        /*
         * Returns the token as the whole response body.
         */
        server.createContext("/api/token", exchange ->
        {
            tokenRequests.incrementAndGet();

            byte[] body = TOKEN.getBytes(StandardCharsets.UTF_8);

            exchange.sendResponseHeaders(200, body.length);

            try (OutputStream outputStream = exchange.getResponseBody())
            {
                outputStream.write(body);
            }
        });

        /*
         * Returns the token inside a JSON response body, as an OAuth token service does.
         */
        server.createContext("/api/oauth-token", exchange ->
        {
            mappedTokenRequests.incrementAndGet();

            byte[] body = ("{\"access_token\":\"" + MAPPED_TOKEN + "\",\"token_type\":\"Bearer\",\"expires_in\":3600}").getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, body.length);

            try (OutputStream outputStream = exchange.getResponseBody())
            {
                outputStream.write(body);
            }
        });

        server.start();

        String serverURL = "http://localhost:" + server.getAddress().getPort();

        secretsFile = Files.createTempFile("token-api-client", ".omsecrets");

        Files.writeString(secretsFile,
                          "secretsCollections:\n" +
                          "  " + COLLECTION + ":\n" +
                          "    refreshTimeInterval: 10\n" +
                          "    tokenAPI:\n" +
                          "      httpRequestType: POST\n" +
                          "      url: " + serverURL + "/api/token\n" +
                          "      contentType: application/json\n" +
                          "      requestBody:\n" +
                          "        userId: testuser\n" +
                          "  " + MAPPED_COLLECTION + ":\n" +
                          "    refreshTimeInterval: 10\n" +
                          "    tokenAPI:\n" +
                          "      httpRequestType: POST\n" +
                          "      url: " + serverURL + "/api/oauth-token\n" +
                          "      contentType: application/json\n" +
                          "      requestBody:\n" +
                          "        userId: testuser\n" +
                          "      responseBodyMapping:\n" +
                          "        " + SecretsStoreCollectionProperty.TOKEN.getName() + ": access_token\n");
    }


    @AfterClass
    public void stopTokenServer() throws Exception
    {
        server.stop(0);
        Files.deleteIfExists(secretsFile);
    }


    /**
     * Each new connector requests a token through the API.  Once the requests are complete, no HttpClient
     * selector threads are left behind.
     */
    @Test(timeOut = 30000)
    public void testTokenRequestsDoNotLeakHttpClients() throws Exception
    {
        long before        = countSelectorThreads();
        int  requestsStart = tokenRequests.get();

        for (int i = 0; i < CONNECTOR_COUNT; i++)
        {
            assertEquals(newConnector(COLLECTION).getSecret(SecretsStoreCollectionProperty.TOKEN.getName()), TOKEN);
        }

        assertEquals(tokenRequests.get() - requestsStart, CONNECTOR_COUNT);

        /*
         * HttpClient.close() waits for the client to terminate, so the count is checked straight away.  An unclosed
         * client's selector thread only exits once the client is garbage collected - which, on a platform with a
         * large heap, can be long enough for hundreds to build up.
         */
        long after = countSelectorThreads();

        assertTrue(after <= before,
                   "Selector threads grew from " + before + " to " + after + " after " + CONNECTOR_COUNT + " token requests.");
    }


    /**
     * A token retrieved through the API is saved, so the next request does not call the API again.
     */
    @Test(timeOut = 30000)
    public void testTokenIsCached() throws Exception
    {
        YAMLSecretsStoreConnector connector = newConnector(COLLECTION);

        int requestsStart = tokenRequests.get();

        assertEquals(connector.getSecret(SecretsStoreCollectionProperty.TOKEN.getName()), TOKEN);
        assertEquals(connector.getSecret(SecretsStoreCollectionProperty.TOKEN.getName()), TOKEN);

        assertEquals(tokenRequests.get() - requestsStart, 1);
    }


    /**
     * When the token API returns JSON, the response body mapping names the field holding the token.  That token
     * is returned and saved, so the next request does not call the API again.  The mapped value used to be
     * looked up and then discarded, so no token was ever returned and every request called the API.
     */
    @Test(timeOut = 30000)
    public void testMappedTokenIsReturnedAndCached() throws Exception
    {
        YAMLSecretsStoreConnector connector = newConnector(MAPPED_COLLECTION);

        int requestsStart = mappedTokenRequests.get();

        assertEquals(connector.getSecret(SecretsStoreCollectionProperty.TOKEN.getName()), MAPPED_TOKEN);
        assertEquals(connector.getSecret(SecretsStoreCollectionProperty.TOKEN.getName()), MAPPED_TOKEN);

        assertEquals(mappedTokenRequests.get() - requestsStart, 1);
    }


    /**
     * Create and start a connector for a collection in the test secrets file.
     *
     * @param collectionName name of the secrets collection to use
     * @return started connector
     * @throws Exception problem starting it
     */
    private YAMLSecretsStoreConnector newConnector(String collectionName) throws Exception
    {
        Endpoint endpoint = new Endpoint();

        endpoint.setNetworkAddress(secretsFile.toString());

        Connection connection = new Connection();

        connection.setEndpoint(endpoint);
        connection.setConfigurationProperties(Map.of(SecretsStoreConfigurationProperty.SECRETS_COLLECTION_NAME.getName(), collectionName));

        YAMLSecretsStoreConnector connector = new YAMLSecretsStoreConnector();

        connector.initialize("test", connection);
        connector.start();

        return connector;
    }


    /**
     * Count the live HttpClient selector threads in this JVM.
     *
     * @return thread count
     */
    private long countSelectorThreads()
    {
        return Thread.getAllStackTraces().keySet().stream()
                     .map(Thread::getName)
                     .filter(name -> name.startsWith(SELECTOR_THREAD_PREFIX) && name.endsWith(SELECTOR_THREAD_SUFFIX))
                     .count();
    }
}
