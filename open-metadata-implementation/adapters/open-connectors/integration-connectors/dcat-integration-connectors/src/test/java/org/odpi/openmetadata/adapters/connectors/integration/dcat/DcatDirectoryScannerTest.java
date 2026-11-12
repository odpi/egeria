/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.dcat;

import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

/**
 * Check that the directory scanner publishes the DCAT documents it finds once per change, and leaves other files alone.
 */
public class DcatDirectoryScannerTest
{
    private static final String DATASET = """
            {
              "@context": { "dcat": "http://www.w3.org/ns/dcat#", "dct": "http://purl.org/dc/terms/" },
              "@id": "https://data.example.gov/dataset/%s",
              "@type": "dcat:Dataset",
              "dct:title": "Dataset %s"
            }
            """;


    /**
     * The file extensions used for JSON-LD are recognized.
     */
    @Test
    public void testCandidateFiles()
    {
        assertTrue(DcatDirectoryScanner.isCandidateFile("catalog.jsonld"));
        assertTrue(DcatDirectoryScanner.isCandidateFile("DATA.JSON"));
        assertFalse(DcatDirectoryScanner.isCandidateFile("catalog.ttl"));
        assertFalse(DcatDirectoryScanner.isCandidateFile("catalog.rdf"));
        assertFalse(DcatDirectoryScanner.isCandidateFile("README.md"));
    }


    /**
     * New and changed DCAT documents are published; unchanged documents, other JSON files, files that are not JSON-LD
     * and files in hidden directories are not.
     *
     * @throws IOException test failure
     */
    @Test
    public void testScan() throws IOException
    {
        Path directory = Files.createTempDirectory("dcat-scanner-test");

        try
        {
            Path nested = Files.createDirectories(directory.resolve("portal/exports"));
            Path hidden = Files.createDirectories(directory.resolve(".git"));

            Files.writeString(directory.resolve("one.jsonld"), DATASET.formatted("one", "one"), StandardCharsets.UTF_8);
            Files.writeString(nested.resolve("two.json"), DATASET.formatted("two", "two"), StandardCharsets.UTF_8);
            Files.writeString(hidden.resolve("hidden.jsonld"), DATASET.formatted("hidden", "hidden"), StandardCharsets.UTF_8);
            Files.writeString(directory.resolve("package.json"), "{\"name\": \"not-dcat\", \"version\": \"1.0.0\"}", StandardCharsets.UTF_8);
            Files.writeString(directory.resolve("broken.jsonld"), "{ this is not JSON", StandardCharsets.UTF_8);
            Files.writeString(directory.resolve("catalog.ttl"), "@prefix dcat: <http://www.w3.org/ns/dcat#> .", StandardCharsets.UTF_8);

            DcatDirectoryScanner scanner   = new DcatDirectoryScanner(directory.toFile(), "test");
            List<String>         published = new ArrayList<>();

            List<File> files = scanner.scan(published::add);

            assertEquals(files.size(), 2, "Unexpected files published: " + files);
            assertEquals(published.size(), 2);

            /*
             * Nothing has changed, so nothing is published.
             */
            published.clear();

            assertTrue(scanner.scan(published::add).isEmpty());
            assertTrue(published.isEmpty());

            /*
             * A changed file is published again.
             */
            Path changed = directory.resolve("one.jsonld");

            Files.writeString(changed, DATASET.formatted("one", "one (revised)"), StandardCharsets.UTF_8);
            assertTrue(changed.toFile().setLastModified(System.currentTimeMillis() + 5000));

            files = scanner.scan(published::add);

            assertEquals(files.size(), 1);
            assertEquals(files.get(0).getName(), "one.jsonld");
            assertTrue(published.get(0).contains("one (revised)"));
        }
        finally
        {
            deleteRecursively(directory.toFile());
        }
    }


    /**
     * A directory that does not exist is reported.
     */
    @Test
    public void testMissingDirectory()
    {
        DcatDirectoryScanner scanner = new DcatDirectoryScanner(new File("no-such-directory-" + System.nanoTime()), "test");

        assertThrows(IOException.class, () -> scanner.scan(rawDocument -> { }));
    }


    /**
     * The publisher writes a file only when its content changes, and names files so that assets with the same name
     * do not overwrite each other.
     *
     * @throws Exception test failure
     */
    @Test
    public void testPublisherFiles() throws Exception
    {
        assertEquals(DcatPublisherIntegrationConnector.getFileName("Air quality: measurements / 2025", "0a743555-c7db-4efb-91b5-affe0149a5ba"),
                     "Air-quality-measurements-2025-0a743555.jsonld");
        assertEquals(DcatPublisherIntegrationConnector.getFileName(null, "0a743555-c7db-4efb-91b5-affe0149a5ba"), "0a743555.jsonld");
        assertEquals(DcatPublisherIntegrationConnector.getFileName("???", "short"), "short.jsonld");

        Path directory = Files.createTempDirectory("dcat-publisher-test");

        try
        {
            File file = new File(directory.toFile(), "datasets/one.jsonld");

            assertTrue(DcatPublisherIntegrationConnector.writeIfChanged(file, "{}"));
            assertFalse(DcatPublisherIntegrationConnector.writeIfChanged(file, "{}"));
            assertTrue(DcatPublisherIntegrationConnector.writeIfChanged(file, "{ }"));
            assertEquals(Files.readString(file.toPath(), StandardCharsets.UTF_8), "{ }");
        }
        finally
        {
            deleteRecursively(directory.toFile());
        }
    }


    /**
     * Remove a directory and its contents.
     *
     * @param file directory or file
     */
    private void deleteRecursively(File file)
    {
        File[] children = file.listFiles();

        if (children != null)
        {
            for (File child : children)
            {
                deleteRecursively(child);
            }
        }

        if (! file.delete())
        {
            file.deleteOnExit();
        }
    }
}
