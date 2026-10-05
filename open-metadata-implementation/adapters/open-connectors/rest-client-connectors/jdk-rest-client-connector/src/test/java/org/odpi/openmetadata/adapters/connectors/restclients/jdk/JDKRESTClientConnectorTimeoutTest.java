/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients.jdk;

import com.sun.net.httpserver.HttpServer;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Verify that a REST call to a server that never answers ends at the request timeout.  The client used to have a
 * connect timeout only, so such a call - to a deadlocked server, for example - waited for ever.
 */
public class JDKRESTClientConnectorTimeoutTest
{
    private HttpServer server;
    private String     serverURL;


    @BeforeClass
    public void startServer() throws Exception
    {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);

        /*
         * Accepts the request and then never answers within the test's lifetime.
         */
        server.createContext("/never-answers", exchange ->
        {
            try
            {
                Thread.sleep(60000);
            }
            catch (InterruptedException interrupted)
            {
                Thread.currentThread().interrupt();
            }

            exchange.close();
        });

        server.setExecutor(Executors.newCachedThreadPool(runnable ->
        {
            Thread thread = new Thread(runnable, "never-answers");
            thread.setDaemon(true);
            return thread;
        }));

        server.start();

        serverURL = "http://localhost:" + server.getAddress().getPort();
    }


    @AfterClass
    public void stopServer()
    {
        server.stop(0);
    }


    /**
     * A call to a server that never answers fails once the configured request timeout has passed.
     */
    @Test(timeOut = 30000)
    public void testCallEndsAtRequestTimeout() throws Exception
    {
        JDKRESTClientConnector connector = newConnector(Map.of(RESTClientConnector.REQUEST_TIMEOUT_PROPERTY, 1));

        long start = System.currentTimeMillis();

        try
        {
            connector.callGetRESTCallNoParams("testCallEndsAtRequestTimeout", String.class, serverURL + "/never-answers");
            fail("A call to a server that never answers returned.");
        }
        catch (Exception expected)
        {
            long elapsed = System.currentTimeMillis() - start;

            assertTrue(elapsed < 15000, "The call took " + elapsed + "ms to time out with a 1 second limit.");
        }
    }


    /**
     * The limit comes from the connection, then the system property, then the default, and zero means no limit.
     */
    @Test
    public void testRequestTimeoutSettings() throws Exception
    {
        String previous = System.getProperty(RESTClientConnector.REQUEST_TIMEOUT_SYSTEM_PROPERTY);

        try
        {
            System.clearProperty(RESTClientConnector.REQUEST_TIMEOUT_SYSTEM_PROPERTY);
            assertTrue(newConnector(null).getRequestTimeoutSeconds() == RESTClientConnector.DEFAULT_REQUEST_TIMEOUT_SECONDS);

            System.setProperty(RESTClientConnector.REQUEST_TIMEOUT_SYSTEM_PROPERTY, "45");
            assertTrue(newConnector(null).getRequestTimeoutSeconds() == 45L);
            assertTrue(newConnector(Map.of(RESTClientConnector.REQUEST_TIMEOUT_PROPERTY, "7")).getRequestTimeoutSeconds() == 7L);
            assertTrue(newConnector(Map.of(RESTClientConnector.REQUEST_TIMEOUT_PROPERTY, 0)).getRequestTimeoutSeconds() == 0L);
        }
        finally
        {
            if (previous == null)
            {
                System.clearProperty(RESTClientConnector.REQUEST_TIMEOUT_SYSTEM_PROPERTY);
            }
            else
            {
                System.setProperty(RESTClientConnector.REQUEST_TIMEOUT_SYSTEM_PROPERTY, previous);
            }
        }
    }


    /**
     * Create and start a connector for the test server.
     *
     * @param configurationProperties connection configuration properties, or null
     * @return started connector
     * @throws Exception problem starting it
     */
    private JDKRESTClientConnector newConnector(Map<String, Object> configurationProperties) throws Exception
    {
        Endpoint endpoint = new Endpoint();

        endpoint.setNetworkAddress(serverURL);

        Connection connection = new Connection();

        connection.setEndpoint(endpoint);

        if (configurationProperties != null)
        {
            connection.setConfigurationProperties(new HashMap<>(configurationProperties));
        }

        JDKRESTClientConnector connector = new JDKRESTClientConnector();

        connector.initialize("JDKRESTClientConnectorTimeoutTest", connection);
        connector.start();

        return connector;
    }
}
