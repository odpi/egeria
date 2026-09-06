/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolTeam;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Validate that every example document published by the Bitol project can be parsed through the BitolDocument root,
 * is dispatched to the right subclass from its "kind" property, and survives a YAML and a JSON round trip unchanged.
 */
public class BitolDocumentTest
{
    /**
     * Example ODCS documents from https://github.com/bitol-io/open-data-contract-standard/tree/main/docs/examples
     */
    static final String[] ODCS_EXAMPLES = {
            "all-data-types.odcs.yaml",
            "all-schema-types.odcs.yaml",
            "azure-server.odcs.yaml",
            "basic-four-dpo.odcs.yaml",
            "column-accuracy.odcs.yaml",
            "column-completeness.odcs.yaml",
            "column-custom.odcs.yaml",
            "column-validity.odcs.yaml",
            "database-table-sla.odcs.yaml",
            "full-example.odcs.yaml",
            "kafka-schema.odcs.yaml",
            "kafka-schemaregistry.odcs.yaml",
            "kafka-server.odcs.yaml",
            "postgresql-adventureworks-contract.odcs.yaml",
            "service-and-operational-roles.odcs.yaml",
            "table-column-description.odcs.yaml",
            "table-column.odcs.yaml",
            "table-columns-with-partition.odcs.yaml",
    };

    /**
     * Example ODPS documents from https://github.com/bitol-io/open-data-product-standard/tree/main/docs/examples
     */
    static final String[] ODPS_EXAMPLES = {
            "customer-data-product.odps.yaml",
            "simple-data-product.odps.yaml",
    };

    static final ObjectMapper YAML_MAPPER = BitolDocumentFormatter.newYAMLMapper();
    static final ObjectMapper JSON_MAPPER = BitolDocumentFormatter.newJSONMapper();

    /**
     * A mapper without the BitolModule - the beans must still work with it, apart from the legacy team list.
     */
    static final ObjectMapper PLAIN_YAML_MAPPER = new ObjectMapper(new YAMLFactory());


    /**
     * Default constructor
     */
    public BitolDocumentTest()
    {
    }


    /**
     * Read a test resource as a string.
     *
     * @param resourceName path below src/test/resources
     * @return file content
     * @throws IOException problem reading the resource
     */
    static String readResource(String resourceName) throws IOException
    {
        try (InputStream stream = BitolDocumentTest.class.getClassLoader().getResourceAsStream(resourceName))
        {
            assertNotNull(stream, "Missing test resource: " + resourceName);

            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }


    /**
     * Parse a document, write it out again in both encodings and check that what comes back is identical.
     *
     * @param document parsed document
     * @throws IOException problem serializing/deserializing
     */
    static void validateRoundTrip(BitolDocument document) throws IOException
    {
        String        yaml         = YAML_MAPPER.writeValueAsString(document);
        BitolDocument fromYAML     = YAML_MAPPER.readValue(yaml, BitolDocument.class);

        assertEquals(fromYAML, document, "YAML round trip changed the document");
        assertEquals(fromYAML.hashCode(), document.hashCode());
        assertEquals(fromYAML.toString(), document.toString());

        String        json         = JSON_MAPPER.writeValueAsString(document);
        BitolDocument fromJSON     = JSON_MAPPER.readValue(json, BitolDocument.class);

        assertEquals(fromJSON, document, "JSON round trip changed the document");

        /*
         * The YAML reader must also accept the JSON form (JSON is valid YAML).
         */
        BitolDocument fromJSONViaYAMLReader = YAML_MAPPER.readValue(json, BitolDocument.class);

        assertEquals(fromJSONViaYAMLReader, document, "Reading JSON with the YAML reader changed the document");

        /*
         * The beans only use jackson-annotations, so a plain mapper (for example Spring's) gives the same result for
         * documents in the current form.
         */
        BitolDocument fromPlainMapper = PLAIN_YAML_MAPPER.readValue(yaml, BitolDocument.class);

        assertEquals(fromPlainMapper, document, "A plain ObjectMapper without BitolModule changed the document");
    }


    /**
     * Every ODCS example parses to a DataContract and round trips.
     *
     * @throws IOException problem reading the examples
     */
    @Test public void testODCSExamples() throws IOException
    {
        for (String example : ODCS_EXAMPLES)
        {
            BitolDocument document = YAML_MAPPER.readValue(readResource("bitol/odcs/" + example), BitolDocument.class);

            assertTrue(document instanceof DataContract, example + " should parse to a DataContract");
            assertEquals(document.getKind(), BitolDocument.DATA_CONTRACT_KIND, example);
            assertNotNull(document.getId(), example + " should have an id");
            assertNotNull(document.getVersion(), example + " should have a version");
            assertNotNull(document.getStatus(), example + " should have a status");
            assertTrue(document.hasSupportedApiVersion(), example + " apiVersion " + document.getApiVersion());

            validateRoundTrip(document);
        }
    }


    /**
     * Every ODPS example parses to a DataProduct and round trips.
     *
     * @throws IOException problem reading the examples
     */
    @Test public void testODPSExamples() throws IOException
    {
        for (String example : ODPS_EXAMPLES)
        {
            BitolDocument document = YAML_MAPPER.readValue(readResource("bitol/odps/" + example), BitolDocument.class);

            assertTrue(document instanceof DataProduct, example + " should parse to a DataProduct");
            assertEquals(document.getKind(), BitolDocument.DATA_PRODUCT_KIND, example);
            assertNotNull(document.getId(), example + " should have an id");
            assertNotNull(document.getStatus(), example + " should have a status");
            assertTrue(document.hasSupportedApiVersion(), example + " apiVersion " + document.getApiVersion());

            validateRoundTrip(document);
        }
    }


    /**
     * A document can also be parsed directly into its concrete class, and new documents carry the right kind and
     * apiVersion without the caller setting them.
     *
     * @throws IOException problem parsing
     */
    @Test public void testDefaults() throws IOException
    {
        DataContract contract = new DataContract();

        assertEquals(contract.getKind(), BitolDocument.DATA_CONTRACT_KIND);
        assertEquals(contract.getApiVersion(), DataContract.CURRENT_API_VERSION);
        assertTrue(contract.hasSupportedApiVersion());

        contract.setApiVersion("v2.2.2");
        assertFalse(contract.hasSupportedApiVersion(), "v2.x contracts have a different structure and are not supported");

        contract.setApiVersion(null);
        assertFalse(contract.hasSupportedApiVersion());

        DataProduct product = new DataProduct();

        assertEquals(product.getKind(), BitolDocument.DATA_PRODUCT_KIND);
        assertEquals(product.getApiVersion(), DataProduct.CURRENT_API_VERSION);
        assertTrue(product.hasSupportedApiVersion());

        product.setApiVersion("v0.9.0");
        assertTrue(product.hasSupportedApiVersion(), "the v0.9.0 pre-release has the same structure as v1.0.0");

        /*
         * A document read into the concrete class does not need the kind property.
         */
        DataContract minimal = YAML_MAPPER.readValue("id: abc\nversion: 1.0.0\nstatus: draft\napiVersion: v3.1.0\n", DataContract.class);

        assertEquals(minimal.getId(), "abc");
        assertEquals(minimal.getKind(), BitolDocument.DATA_CONTRACT_KIND);

        /*
         * Unknown top-level properties are ignored rather than failing the parse.
         */
        DataContract withExtra = YAML_MAPPER.readValue("kind: DataContract\nid: abc\nversion: 1.0.0\nstatus: draft\napiVersion: v3.1.0\nfutureProperty: 1\n", DataContract.class);

        assertEquals(withExtra.getId(), "abc");
        assertNull(withExtra.getName());
    }


    /**
     * Older ODCS v3 documents supply the team as a bare list of members.
     *
     * @throws IOException problem parsing
     */
    @Test public void testLegacyTeamList() throws IOException
    {
        String legacy = "kind: DataContract\n" +
                        "apiVersion: v3.0.0\n" +
                        "id: legacy\n" +
                        "version: 1.0.0\n" +
                        "status: active\n" +
                        "team:\n" +
                        "  - username: ceastwood\n" +
                        "    role: Data Scientist\n" +
                        "  - username: mhopper\n" +
                        "    role: Owner\n";

        DataContract contract = YAML_MAPPER.readValue(legacy, DataContract.class);
        BitolTeam    team     = contract.getTeam();

        assertNotNull(team);
        assertNull(team.getName());
        assertEquals(team.getMembers().size(), 2);
        assertEquals(team.getMembers().get(0).getUsername(), "ceastwood");
        assertEquals(team.getMembers().get(1).getRole(), "Owner");

        /*
         * Without BitolModule the legacy form is rejected.
         */
        try
        {
            PLAIN_YAML_MAPPER.readValue(legacy, DataContract.class);
            fail("legacy team list should need BitolModule");
        }
        catch (IOException expected)
        {
            // expected
        }

        /*
         * The team is written back in the current (object) form and re-reads identically.
         */
        validateRoundTrip(contract);

        String modern = "kind: DataContract\n" +
                        "apiVersion: v3.1.0\n" +
                        "id: modern\n" +
                        "version: 1.0.0\n" +
                        "status: active\n" +
                        "team:\n" +
                        "  name: my-team\n" +
                        "  members:\n" +
                        "    - username: ceastwood\n";

        contract = YAML_MAPPER.readValue(modern, DataContract.class);

        assertEquals(contract.getTeam().getName(), "my-team");
        assertEquals(contract.getTeam().getMembers().size(), 1);
    }
}
