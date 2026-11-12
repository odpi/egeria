/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat;

import org.odpi.openmetadata.frameworks.integration.dcat.jsonld.JsonLdGraph;
import org.odpi.openmetadata.frameworks.integration.dcat.jsonld.JsonLdNode;
import org.odpi.openmetadata.frameworks.integration.dcat.jsonld.JsonLdReader;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatCatalog;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDataService;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDataset;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDatasetSeries;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDistribution;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDocument;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

/**
 * Check that DCAT documents written in the different styles of JSON-LD are read correctly, and that a document
 * written by Egeria can be read back without loss.
 */
public class DcatDocumentFormatterTest
{
    /**
     * Read a test document from the classpath.
     *
     * @param name file name in the dcat resources folder
     * @return content of the file
     * @throws IOException file not found
     */
    public static String readResource(String name) throws IOException
    {
        try (InputStream stream = DcatDocumentFormatterTest.class.getResourceAsStream("/dcat/" + name))
        {
            if (stream == null)
            {
                throw new IOException("Missing test resource " + name);
            }

            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }


    /**
     * A dataset written with prefixes, nested nodes and typed values.
     *
     * @throws IOException test failure
     */
    @Test
    public void testPrefixedDataset() throws IOException
    {
        DcatDocument document = DcatDocumentFormatter.parseDocument(readResource("air-quality-dataset.jsonld"));

        assertTrue(DcatDocumentFormatter.hasContent(document));
        assertNull(document.getCatalogs());
        assertNull(document.getDataServices());
        assertEquals(document.getDatasets().size(), 1);

        DcatDataset dataset = document.getDatasets().get(0);

        assertEquals(dataset.getIri(), "https://data.example.gov/dataset/air-quality-2025");
        assertEquals(dataset.getNodeId(), dataset.getIri());
        assertEquals(dataset.getTitle(), "Air quality measurements 2025");
        assertEquals(dataset.getTitleTranslations().get("fr"), "Mesures de la qualité de l'air 2025");
        assertTrue(dataset.getDescription().startsWith("Hourly readings"));
        assertEquals(dataset.getIdentifiers(), List.of("air-quality-2025"));
        assertEquals(dataset.getIssued(), "2025-01-15");
        assertEquals(dataset.getModified(), "2025-11-02T09:30:00Z");
        assertEquals(dataset.getLanguages(), List.of("http://publications.europa.eu/resource/authority/language/ENG"));
        assertEquals(dataset.getPublisher().getName(), "Environment Agency");
        assertEquals(dataset.getPublisher().getIri(), "https://data.example.gov/org/environment-agency");
        assertEquals(dataset.getPublisher().getAgentType(), DcatVocabulary.ORGANIZATION);
        assertEquals(dataset.getPublisher().getHomepage(), "https://environment.example.gov");
        assertEquals(dataset.getCreators().get(0).getName(), "Air Quality Monitoring Team");
        assertEquals(dataset.getContactPoints().get(0).getName(), "Open Data Helpdesk");
        assertEquals(dataset.getContactPoints().get(0).getEmail(), "opendata@environment.example.gov");
        assertEquals(dataset.getContactPoints().get(0).getTelephone(), "+44-20-7946-0000");
        assertEquals(dataset.getKeywords(), List.of("air quality", "pollution", "environment"));
        assertEquals(dataset.getThemes().size(), 2);
        assertEquals(dataset.getThemes().get(0).getLabel(), "Environment");
        assertEquals(dataset.getThemes().get(1).getIri(), "http://publications.europa.eu/resource/authority/data-theme/HEAL");
        assertNull(dataset.getThemes().get(1).getLabel());
        assertEquals(dataset.getLicense().getIri(), "https://creativecommons.org/licenses/by/4.0/");
        assertEquals(dataset.getAccessRights(), "http://publications.europa.eu/resource/authority/access-right/PUBLIC");
        assertEquals(dataset.getConformsTo(), List.of("https://www.w3.org/TR/vocab-ssn/"));
        assertEquals(dataset.getLandingPage(), "https://data.example.gov/air-quality");
        assertEquals(dataset.getDocumentation(), List.of("https://data.example.gov/air-quality/methodology"));
        assertEquals(dataset.getVersion(), "2.1");
        assertEquals(dataset.getAccrualPeriodicity(), "http://publications.europa.eu/resource/authority/frequency/HOURLY");
        assertEquals(dataset.getSpatial(), List.of("http://sws.geonames.org/2635167/"));
        assertEquals(dataset.getTemporal().get(0).getStartDate(), "2025-01-01");
        assertEquals(dataset.getTemporal().get(0).getEndDate(), "2025-12-31");
        assertEquals(dataset.getTemporalResolution(), "PT1H");

        /*
         * A property that is not part of DCAT is preserved.
         */
        assertEquals(dataset.getOtherProperties().get("http://data.europa.eu/r5r/applicableLegislation"),
                     List.of("http://data.europa.eu/eli/reg_impl/2023/138/oj"));
        assertEquals(dataset.getOtherProperties().size(), 1);

        assertEquals(dataset.getDistributions().size(), 2);

        DcatDistribution csv = dataset.getDistributions().get(0);

        assertEquals(csv.getIri(), "https://data.example.gov/dataset/air-quality-2025/csv");
        assertEquals(csv.getTitle(), "Air quality 2025 (CSV)");
        assertEquals(csv.getDownloadURL(), "https://data.example.gov/files/air-quality-2025.csv");
        assertEquals(csv.getAccessURL(), "https://data.example.gov/air-quality");
        assertEquals(csv.getFormat(), "CSV");
        assertEquals(csv.getByteSize(), Long.valueOf(5242880L));
        assertEquals(csv.getChecksumAlgorithm(), "http://spdx.org/rdf/terms#checksumAlgorithm_sha256");
        assertTrue(csv.getChecksumValue().startsWith("9f86d081"));
        assertNull(csv.getOtherProperties());

        DcatDistribution api = dataset.getDistributions().get(1);

        assertNull(api.getDownloadURL());
        assertEquals(api.getAccessServices(), List.of("https://data.example.gov/service/air-quality-api"));
    }


    /**
     * A flattened catalog that uses a default vocabulary, a language map, typed terms and keyword aliases.
     *
     * @throws IOException test failure
     */
    @Test
    public void testFlattenedCatalog() throws IOException
    {
        DcatDocument document = DcatDocumentFormatter.parseDocument(readResource("transport-catalog.jsonld"));

        assertEquals(document.getCatalogs().size(), 1);
        assertEquals(document.getDatasets().size(), 3);
        assertEquals(document.getDataServices().size(), 1);

        DcatCatalog catalog = document.getCatalogs().get(0);

        assertEquals(catalog.getTitle(), "Department for Transport open data");
        assertEquals(catalog.getTitleTranslations().get("cy"), "Data agored yr Adran Drafnidiaeth");
        assertEquals(catalog.getHomepage(), "https://transport.example.gov");
        assertEquals(catalog.getDatasetIRIs().size(), 3);
        assertEquals(catalog.getServiceIRIs(), List.of("https://transport.example.gov/service/timetable-api"));

        /*
         * The publisher is described in a separate node that the catalog refers to.
         */
        assertEquals(catalog.getPublisher().getName(), "Department for Transport");
        assertEquals(catalog.getPublisher().getIri(), "https://transport.example.gov/org/dft");

        DcatDatasetSeries series = (DcatDatasetSeries) document.getDatasets().get(0);

        assertEquals(series.getTitle(), "Bus timetables");
        assertEquals(series.getSeriesMembers(), List.of("https://transport.example.gov/dataset/bus-timetables-2025-q1",
                                                        "https://transport.example.gov/dataset/bus-timetables-2025-q2"));

        DcatDataset quarter = document.getDatasets().get(1);

        assertFalse(quarter instanceof DcatDatasetSeries);
        assertEquals(quarter.getInSeries(), List.of("https://transport.example.gov/dataset/bus-timetables"));
        assertEquals(quarter.getKeywords(), List.of("bus", "timetable"));
        assertEquals(quarter.getDistributions().get(0).getDownloadURL(), "https://transport.example.gov/files/bus-2025-q1.zip");
        assertEquals(quarter.getDistributions().get(0).getFormat(), "GTFS");
        assertNull(quarter.getDistributions().get(0).getIri());

        DcatDataService service = document.getDataServices().get(0);

        assertEquals(service.getEndpointURLs(), List.of("https://api.transport.example.gov/timetables/v2"));
        assertEquals(service.getEndpointDescriptions(), List.of("https://api.transport.example.gov/timetables/v2/openapi.json"));
        assertEquals(service.getServesDatasets().size(), 2);
    }


    /**
     * A catalog that relies on a context that is not embedded in the document.  The context is not retrieved, so the
     * standard names of the DCAT terms are used.
     *
     * @throws IOException test failure
     */
    @Test
    public void testRemoteContext() throws IOException
    {
        String      rawDocument = readResource("open-data-catalog.json");
        JsonLdGraph graph       = JsonLdReader.read(rawDocument);

        assertEquals(graph.getUnresolvedContexts().size(), 1);

        DcatDocument document = DcatDocumentFormatter.parseDocument(rawDocument);

        assertEquals(document.getCatalogs().size(), 1);
        assertEquals(document.getDatasets().size(), 2);

        DcatCatalog catalog = document.getCatalogs().get(0);

        assertEquals(catalog.getIri(), "https://agency.example.gov/data.json");
        assertEquals(catalog.getDatasetIRIs().size(), 2);

        DcatDataset markets = document.getDatasets().get(0);

        /*
         * The datasets have no IRI, so the catalog refers to them through their identifiers in the document.
         */
        assertNull(markets.getIri());
        assertTrue(markets.getNodeId().startsWith(JsonLdNode.BLANK_NODE_PREFIX));
        assertTrue(catalog.getDatasetIRIs().contains(markets.getNodeId()));

        assertEquals(markets.getTitle(), "Farmers market directory");
        assertEquals(markets.getKeywords(), List.of("farmers markets", "food"));
        assertEquals(markets.getModified(), "2025-06-30");
        assertEquals(markets.getPublisher().getName(), "Agricultural Marketing Service");
        assertEquals(markets.getContactPoints().get(0).getName(), "Jane Doe");
        assertEquals(markets.getContactPoints().get(0).getEmail(), "jane.doe@agency.example.gov");
        assertEquals(markets.getIdentifiers(), List.of("https://agency.example.gov/id/farmers-markets"));
        assertEquals(markets.getLicense().getIri(), "https://creativecommons.org/publicdomain/zero/1.0/");
        assertEquals(markets.getDistributions().get(0).getDownloadURL(), "https://agency.example.gov/files/farmers-markets.csv");
        assertEquals(markets.getDistributions().get(0).getMediaType(), "text/csv");

        /*
         * A term that only the missing context defines is kept under the name of that context.
         */
        assertEquals(markets.getOtherProperties().get("https://project-open-data.cio.gov/v1.1/schema/catalog.jsonld#accessLevel"), List.of("public"));
    }


    /**
     * Documents written by Egeria can be read back without loss.
     *
     * @throws IOException test failure
     */
    @Test
    public void testRoundTrip() throws IOException
    {
        for (String resourceName : new String[]{"air-quality-dataset.jsonld", "transport-catalog.jsonld"})
        {
            DcatDocument original  = DcatDocumentFormatter.parseDocument(readResource(resourceName));
            String       generated = DcatDocumentFormatter.toJSONLD(original);
            DcatDocument reloaded  = DcatDocumentFormatter.parseDocument(generated);

            assertEquals(reloaded, original, "Round trip of " + resourceName + " via " + generated);

            /*
             * The second generation is identical to the first, which is how a publisher knows that nothing has changed.
             */
            assertEquals(DcatDocumentFormatter.toJSONLD(reloaded), generated);
        }
    }


    /**
     * A single resource is written as the document; many resources are written as a graph.
     *
     * @throws IOException test failure
     */
    @Test
    public void testDocumentShape() throws IOException
    {
        String single = DcatDocumentFormatter.toJSONLD(DcatDocumentFormatter.parseDocument(readResource("air-quality-dataset.jsonld")));

        assertTrue(single.contains("\"@context\""));
        assertFalse(single.contains("\"@graph\""));
        assertTrue(single.contains("\"@type\" : \"dcat:Dataset\""));
        assertTrue(single.contains("\"dcatap:applicableLegislation\""));
        assertTrue(single.contains("\"dcatap\" : \"http://data.europa.eu/r5r/\""));

        String graph = DcatDocumentFormatter.toJSONLD(DcatDocumentFormatter.parseDocument(readResource("transport-catalog.jsonld")));

        assertTrue(graph.contains("\"@graph\""));
        assertTrue(graph.contains("\"dcat:DatasetSeries\""));
    }


    /**
     * Documents that are not JSON-LD, or that have nothing to catalog, are detected.
     *
     * @throws IOException test failure
     */
    @Test
    public void testInvalidDocuments() throws IOException
    {
        assertThrows(IOException.class, () -> DcatDocumentFormatter.parseDocument("@prefix dcat: <http://www.w3.org/ns/dcat#> ."));
        assertThrows(IOException.class, () -> DcatDocumentFormatter.parseDocument("\"just a string\""));
        assertThrows(IOException.class, () -> DcatDocumentFormatter.parseDocument(null));

        DcatDocument empty = DcatDocumentFormatter.parseDocument("{\"@context\": {\"schema\": \"http://schema.org/\"}, \"@type\": \"schema:Person\", \"schema:name\": \"Nobody\"}");

        assertNotNull(empty);
        assertFalse(DcatDocumentFormatter.hasContent(empty));
    }
}
