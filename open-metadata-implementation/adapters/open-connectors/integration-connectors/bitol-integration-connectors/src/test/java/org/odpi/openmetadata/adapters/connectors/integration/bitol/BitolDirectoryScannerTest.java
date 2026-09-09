/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Validate the directory scanner used by the Bitol files receiver.
 */
public class BitolDirectoryScannerTest
{
    /**
     * Default constructor
     */
    public BitolDirectoryScannerTest()
    {
    }


    private static String readResource(String resourceName) throws IOException
    {
        try (InputStream stream = BitolDirectoryScannerTest.class.getClassLoader().getResourceAsStream(resourceName))
        {
            assertNotNull(stream, "Missing test resource: " + resourceName);

            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }


    /**
     * New and changed Bitol documents are published once; other files and hidden directories are ignored.
     *
     * @throws IOException problem with the temporary directory
     */
    @Test public void testScan() throws IOException
    {
        Path root = Files.createTempDirectory("bitol-scanner-test");

        try
        {
            Path contracts = Files.createDirectories(root.resolve("contracts"));
            Path hidden    = Files.createDirectories(root.resolve(".git").resolve("objects"));

            Path contractFile = contracts.resolve("table-column.odcs.yaml");
            Path productFile  = root.resolve("simple-data-product.odps.yaml");

            Files.writeString(contractFile, readResource("bitol/table-column.odcs.yaml"));
            Files.writeString(productFile, readResource("bitol/simple-data-product.odps.yaml"));
            Files.writeString(root.resolve("build.yaml"), "name: not-a-bitol-document\nsteps: []\n");
            Files.writeString(root.resolve("README.md"), "kind: DataContract\n");
            Files.writeString(root.resolve("broken.yml"), "{ not: [ yaml");
            Files.writeString(hidden.resolve("ignored.yaml"), readResource("bitol/simple-data-product.odps.yaml"));

            BitolDirectoryScanner scanner   = new BitolDirectoryScanner(root.toFile(), "test");
            List<String>          published = new ArrayList<>();

            List<BitolDirectoryScanner.PublishedDocument> results = scanner.scan(published::add);

            assertEquals(results.size(), 2, "one contract and one product expected");
            assertEquals(published.size(), 2);

            for (BitolDirectoryScanner.PublishedDocument result : results)
            {
                if (result.file().getName().equals("table-column.odcs.yaml"))
                {
                    assertEquals(result.kind(), BitolDocument.DATA_CONTRACT_KIND);
                }
                else if (result.file().getName().equals("simple-data-product.odps.yaml"))
                {
                    assertEquals(result.kind(), BitolDocument.DATA_PRODUCT_KIND);
                }
                else
                {
                    fail("unexpected file published: " + result.file());
                }
            }

            /*
             * A second scan with no changes publishes nothing.
             */
            assertTrue(scanner.scan(published::add).isEmpty());
            assertEquals(published.size(), 2);

            /*
             * Changing a file publishes it again; the other file is not republished.
             */
            Files.writeString(productFile, readResource("bitol/simple-data-product.odps.yaml").replace("Simple Data Product", "Changed Data Product"));
            File productAsFile = productFile.toFile();
            assertTrue(productAsFile.setLastModified(productAsFile.lastModified() + 5000), "unable to bump the last modified time");

            results = scanner.scan(published::add);

            assertEquals(results.size(), 1);
            assertEquals(results.get(0).file().getName(), "simple-data-product.odps.yaml");
            assertEquals(published.size(), 3);
            assertTrue(published.get(2).contains("Changed Data Product"));

            /*
             * A new file in a new sub-directory is picked up.
             */
            Path more = Files.createDirectories(root.resolve("more"));
            Files.writeString(more.resolve("another.json"), "{ \"kind\": \"DataContract\", \"apiVersion\": \"v3.1.0\", \"id\": \"x\", \"version\": \"1.0.0\", \"status\": \"active\" }");

            results = scanner.scan(published::add);

            assertEquals(results.size(), 1);
            assertEquals(results.get(0).kind(), BitolDocument.DATA_CONTRACT_KIND);
            assertTrue(results.get(0).file().getName().endsWith(".json"));
        }
        finally
        {
            org.apache.commons.io.FileUtils.deleteDirectory(root.toFile());
        }
    }


    /**
     * A missing directory is reported as an IOException so the connector can log and retry.
     */
    @Test public void testMissingDirectory()
    {
        BitolDirectoryScanner scanner = new BitolDirectoryScanner(new File("/this/does/not/exist/bitol"), "test");

        try
        {
            scanner.scan(rawDocument -> fail("nothing should be published"));
            fail("missing directory should fail");
        }
        catch (IOException expected)
        {
            assertTrue(expected.getMessage().contains("Not a readable directory"));
        }

        assertFalse(BitolDirectoryScanner.isCandidateFile("contract.txt"));
        assertTrue(BitolDirectoryScanner.isCandidateFile("contract.YAML"));
        assertTrue(BitolDirectoryScanner.isCandidateFile("contract.yml"));
        assertTrue(BitolDirectoryScanner.isCandidateFile("contract.json"));
        assertEquals(FileBasedBitolStoreConnector.sanitize("../1.0.0/x"), ".._1.0.0_x");
        assertEquals(BitolFilesReceiverIntegrationConnector.stripFilePrefix("file:///tmp/contracts"), "/tmp/contracts");
        assertEquals(BitolFilesReceiverIntegrationConnector.stripFilePrefix("/tmp/contracts"), "/tmp/contracts");
    }
}
