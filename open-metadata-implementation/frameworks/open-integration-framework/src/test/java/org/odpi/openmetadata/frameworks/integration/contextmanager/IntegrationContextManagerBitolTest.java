/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.contextmanager;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentListener;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

/**
 * Validate the routing of Bitol documents from the context manager to the registered listeners.
 */
public class IntegrationContextManagerBitolTest
{
    /**
     * Minimal concrete context manager for the test.  The audit log has no destination so messages are discarded.
     */
    private static class TestContextManager extends IntegrationContextManager
    {
        TestContextManager()
        {
            super.auditLog = new AuditLog(null, 1, ComponentDevelopmentStatus.IN_DEVELOPMENT, "test", null, null);
        }

        @Override
        public void createClients() throws InvalidParameterException
        {
        }

        @Override
        public OpenMetadataEventClient createEventClient(String connectorId) throws InvalidParameterException
        {
            return null;
        }
    }


    /**
     * Listener that records what it receives.
     */
    private static class RecordingListener implements BitolDocumentListener
    {
        final List<DataContract> contracts     = new ArrayList<>();
        final List<String>       contractRaws  = new ArrayList<>();
        final List<DataProduct>  products      = new ArrayList<>();
        final List<String>       productRaws   = new ArrayList<>();

        @Override
        public void processDataContract(DataContract dataContract,
                                        String       rawDocument)
        {
            contracts.add(dataContract);
            contractRaws.add(rawDocument);
        }

        @Override
        public void processDataProduct(DataProduct dataProduct,
                                       String      rawDocument)
        {
            products.add(dataProduct);
            productRaws.add(rawDocument);
        }
    }


    /**
     * Listener that always fails - the manager must carry on to the other listeners.
     */
    private static class FailingListener implements BitolDocumentListener
    {
        @Override
        public void processDataContract(DataContract dataContract,
                                        String       rawDocument)
        {
            throw new RuntimeException("listener failure");
        }

        @Override
        public void processDataProduct(DataProduct dataProduct,
                                       String      rawDocument)
        {
            throw new RuntimeException("listener failure");
        }
    }


    private static final String V2_CONTRACT = "kind: DataContract\napiVersion: v2.2.2\nuuid: 53581432\nversion: 1.0.0\nstatus: active\n";


    /**
     * Default constructor
     */
    public IntegrationContextManagerBitolTest()
    {
    }


    private static String readResource(String resourceName) throws IOException
    {
        try (InputStream stream = IntegrationContextManagerBitolTest.class.getClassLoader().getResourceAsStream(resourceName))
        {
            assertNotNull(stream, "Missing test resource: " + resourceName);

            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }


    /**
     * Documents are routed by kind, whichever publish method is used.
     *
     * @throws IOException problem reading the examples
     */
    @Test public void testRoutingByKind() throws IOException
    {
        String contract = readResource("bitol/odcs/full-example.odcs.yaml");
        String product  = readResource("bitol/odps/simple-data-product.odps.yaml");

        TestContextManager manager  = new TestContextManager();
        RecordingListener  listener = new RecordingListener();

        manager.registerListener(listener);

        manager.publishBitolDocument(contract);
        manager.publishBitolDocument(product);
        manager.publishDataContract(contract);
        manager.publishDataProduct(product);

        assertEquals(listener.contracts.size(), 2);
        assertEquals(listener.products.size(), 2);
        assertEquals(listener.contracts.get(0).getId(), "53581432-6c55-4ba2-a65f-72344a91553a");
        assertEquals(listener.products.get(1).getName(), "Simple Data Product");
        assertSame(listener.contractRaws.get(0), contract, "the raw document is passed through untouched");
        assertSame(listener.productRaws.get(1), product);
    }


    /**
     * A document of the wrong kind for the method, an unknown kind or a malformed document is not delivered.
     *
     * @throws IOException problem reading the examples
     */
    @Test public void testRejectedDocuments() throws IOException
    {
        String contract = readResource("bitol/odcs/full-example.odcs.yaml");
        String product  = readResource("bitol/odps/simple-data-product.odps.yaml");

        TestContextManager manager  = new TestContextManager();
        RecordingListener  listener = new RecordingListener();

        manager.registerListener(listener);

        manager.publishDataContract(product);
        manager.publishDataProduct(contract);
        manager.publishBitolDocument("kind: SomethingElse\nid: x\n");
        manager.publishBitolDocument("id: x\nstatus: active\n");
        manager.publishBitolDocument("{ not: [ yaml");
        manager.publishBitolDocument(null);
        manager.publishDataContract((String) null);
        manager.publishDataContract((DataContract) null);
        manager.publishDataProduct((DataProduct) null);

        assertTrue(listener.contracts.isEmpty());
        assertTrue(listener.products.isEmpty());
    }


    /**
     * An old (v2.x) contract has a supported kind but an unsupported structure: it is delivered with a null bean so
     * that it can still be stored or forwarded.
     */
    @Test public void testUnsupportedVersion()
    {
        TestContextManager manager  = new TestContextManager();
        RecordingListener  listener = new RecordingListener();

        manager.registerListener(listener);

        manager.publishDataContract(V2_CONTRACT);

        assertEquals(listener.contracts.size(), 1);
        assertNull(listener.contracts.get(0));
        assertSame(listener.contractRaws.get(0), V2_CONTRACT);
        assertTrue(listener.products.isEmpty());
    }


    /**
     * Beans are serialized to YAML for the raw document, and a failing listener does not stop delivery to the others.
     *
     * @throws IOException problem reading the examples
     */
    @Test public void testBeanPublishAndFailingListener() throws IOException
    {
        DataContract contract = BitolDocumentFormatter.parseDataContract(readResource("bitol/odcs/full-example.odcs.yaml"));
        DataProduct  product  = BitolDocumentFormatter.parseDataProduct(readResource("bitol/odps/customer-data-product.odps.yaml"));

        TestContextManager manager  = new TestContextManager();
        RecordingListener  listener = new RecordingListener();

        manager.registerListener(new FailingListener());
        manager.registerListener(listener);
        manager.registerListener(new FailingListener());

        manager.publishDataContract(contract);
        manager.publishDataProduct(product);

        assertEquals(listener.contracts.size(), 1);
        assertSame(listener.contracts.get(0), contract);
        assertEquals(BitolDocumentFormatter.parseDataContract(listener.contractRaws.get(0)), contract, "raw form is the YAML of the bean");

        assertEquals(listener.products.size(), 1);
        assertSame(listener.products.get(0), product);
        assertEquals(BitolDocumentFormatter.parseDataProduct(listener.productRaws.get(0)), product);
    }
}
