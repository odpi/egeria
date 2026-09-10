/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolContext;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolSynonym;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractEnumValue;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractLogicalType;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSchemaObject;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSchemaProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractServerType;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentTest.readResource;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Validate the features added in ODCS v3.2.0 and ODPS v1.1.0 using the examples published with those releases.
 */
public class BitolStandardsUpdateTest
{
    private static final ObjectMapper YAML_MAPPER = BitolDocumentFormatter.newYAMLMapper();


    /**
     * Default constructor
     */
    public BitolStandardsUpdateTest()
    {
    }


    private static DataContract contract(String name) throws IOException
    {
        return YAML_MAPPER.readValue(readResource("bitol/odcs/" + name), DataContract.class);
    }


    private static DataContractSchemaProperty property(DataContractSchemaObject object, String name)
    {
        for (DataContractSchemaProperty property : object.getProperties())
        {
            if (name.equals(property.getName()))
            {
                return property;
            }
        }

        throw new AssertionError("No property " + name + " in " + object.getName());
    }


    /**
     * The current versions are those of the new releases, and the older versions are still accepted.
     */
    @Test public void testVersions()
    {
        assertEquals(DataContract.CURRENT_API_VERSION, "v3.2.0");
        assertEquals(DataProduct.CURRENT_API_VERSION, "v1.1.0");

        DataContract contract = new DataContract();
        contract.setApiVersion("v3.1.0");
        assertTrue(contract.hasSupportedApiVersion());
        contract.setApiVersion("v3.2.0");
        assertTrue(contract.hasSupportedApiVersion());

        DataProduct product = new DataProduct();
        product.setApiVersion("v1.1.0");
        assertTrue(product.hasSupportedApiVersion());
    }


    /**
     * RFC 0033: enumerations on schema properties.  The YAML key is "enum", which is a Java keyword, so the bean field is
     * called enumValues.
     */
    @Test public void testEnum() throws IOException
    {
        DataContract               contract = contract("enum.odcs.yaml");
        DataContractSchemaProperty status   = property(contract.getSchema().get(0), "status");

        assertNotNull(status.getEnumValues());
        assertEquals(status.getEnumValues().size(), 4);

        DataContractEnumValue cancelled = status.getEnumValues().get(3);

        assertEquals(cancelled.getValue(), "cancelled");
        assertEquals(cancelled.getLabel(), "Cancelled");
        assertEquals(cancelled.getTags(), List.of("terminal"));
        assertEquals(cancelled.getAuthoritativeDefinitions().get(0).getType(), "businessDefinition");

        DataContractSchemaProperty priority = property(contract.getSchema().get(0), "priority");

        assertEquals(priority.getEnumValues().get(0).getValue(), 1, "numeric enum values keep their type");
        assertEquals(priority.getEnumValues().get(2).getCustomProperties().get(0).getProperty(), "slaHoursOverride");

        String yaml = BitolDocumentFormatter.toYAML(contract);

        assertTrue(yaml.contains("enum:"), "the enum array is written back under its standard name");
        assertTrue(! yaml.contains("enumValues"), "the Java field name must not leak into the document");
    }


    /**
     * RFC 0030: map logical type with key and value definitions.
     */
    @Test public void testMap() throws IOException
    {
        DataContract               contract = contract("map.odcs.yaml");
        DataContractSchemaProperty details  = property(contract.getSchema().get(0), "product_details");

        assertEquals(DataContractLogicalType.fromValue(details.getLogicalType()), DataContractLogicalType.MAP);
        assertNotNull(details.getMap());
        assertEquals(details.getMap().getKey().getLogicalType(), "string");
        assertEquals(details.getMap().getValue().getLogicalType(), "object");
        assertEquals(details.getMap().getValue().getProperties().size(), 3, "a map value may itself be a structure");

        DataContractSchemaProperty counts = property(contract.getSchema().get(0), "daily_counts");

        assertEquals(counts.getMap().getValue().getLogicalTypeOptions().getMinimum(), 0);
    }


    /**
     * RFC 0042: vector logical type and its options.
     */
    @Test public void testVector() throws IOException
    {
        DataContract               contract  = contract("vector.odcs.yaml");
        DataContractSchemaProperty embedding = property(contract.getSchema().get(0), "body_embedding");

        assertEquals(DataContractLogicalType.fromValue(embedding.getLogicalType()), DataContractLogicalType.VECTOR);
        assertEquals(embedding.getLogicalTypeOptions().getDimensions(), 1536);
        assertEquals(embedding.getLogicalTypeOptions().getElementType(), "float32");
        assertEquals(embedding.getLogicalTypeOptions().getDistanceMetric(), "cosine");
        assertEquals(embedding.getLogicalTypeOptions().getNormalized(), Boolean.TRUE);
        assertEquals(embedding.getLogicalTypeOptions().getEmbeddingModel(), "openai/text-embedding-3-small");
        assertEquals(embedding.getLogicalTypeOptions().getEmbeddingModelVersion(), "2024-01-25");
        assertNull(embedding.getLogicalTypeOptions().getAdditionalProperties(), "the vector options are first-class, not overflow");
    }


    /**
     * RFC 0051: deprecated flag on schema objects and properties, including nested properties.
     */
    @Test public void testDeprecated() throws IOException
    {
        DataContract             contract = contract("deprecated.odcs.yaml");
        DataContractSchemaObject legacy   = contract.getSchema().get(0);

        assertEquals(legacy.getDeprecated(), Boolean.TRUE);
        assertNull(property(legacy, "order_id").getDeprecated());
        assertEquals(property(legacy, "category_id").getDeprecated(), Boolean.TRUE);
        assertEquals(property(legacy, "category").getProperties().get(1).getDeprecated(), Boolean.TRUE);
    }


    /**
     * RFC 0034 and RFC 0041: semantic types and synonyms.
     */
    @Test public void testSemanticTypesAndSynonyms() throws IOException
    {
        DataContract             contract = contract("measures-and-dimensions.odcs.yaml");
        DataContractSchemaObject turnover = contract.getSchema().get(0);

        assertEquals(turnover.getSynonyms().size(), 2);
        assertEquals(turnover.getSynonyms().get(1).getLocale(), "fr-FR");

        assertNull(property(turnover, "country_code").getSemanticType());
        assertEquals(property(turnover, "country_code_dim").getSemanticType(), "dimension");

        DataContractSchemaProperty total = property(turnover, "total_turnover_euros");

        assertEquals(total.getSemanticType(), "measure");
        assertEquals(total.getTransformLogic(), "SUM(turnover_euros)");

        List<BitolSynonym> synonyms = total.getSynonyms();

        assertEquals(synonyms.size(), 3);
        assertEquals(synonyms.get(0).getSynonym(), "TO");
        assertEquals(synonyms.get(0).getSource(), "finance-team");
        assertEquals(synonyms.get(2).getId(), "sales-fr");
    }


    /**
     * RFC 0038: the AI context block, in both its object form and its string shorthand, at the contract and schema
     * object levels.
     */
    @Test public void testContext() throws IOException
    {
        String yaml = """
                apiVersion: v3.2.0
                kind: DataContract
                id: ctx-1
                version: 1.0.0
                status: active
                context:
                  instructions: Use this table for revenue reporting only.
                  verifiedStatements:
                    - id: q1
                      question: What was the total revenue last month?
                      answer: SUM(amount) over the previous calendar month.
                      tags: [finance]
                    - question: How many orders were placed today?
                  constraints:
                    - constraint: Never expose customer names.
                      authoritativeDefinitions:
                        - url: https://policy.example.com/pii
                          type: businessDefinition
                schema:
                  - name: revenue
                    context: Rows are one per settled transaction.
                    properties:
                      - name: amount
                        logicalType: number
                """;

        DataContract contract = BitolDocumentFormatter.parseDataContract(yaml);
        BitolContext context  = contract.getContext();

        assertNotNull(context);
        assertEquals(context.getInstructions(), "Use this table for revenue reporting only.");
        assertEquals(context.getVerifiedStatements().size(), 2);
        assertEquals(context.getVerifiedStatements().get(0).getAnswer(), "SUM(amount) over the previous calendar month.");
        assertNull(context.getVerifiedStatements().get(1).getAnswer(), "a sample question has no answer");
        assertEquals(context.getConstraints().get(0).getConstraint(), "Never expose customer names.");
        assertEquals(context.getConstraints().get(0).getAuthoritativeDefinitions().get(0).getUrl(), "https://policy.example.com/pii");

        BitolContext objectContext = contract.getSchema().get(0).getContext();

        assertNotNull(objectContext, "the string shorthand is accepted");
        assertEquals(objectContext.getInstructions(), "Rows are one per settled transaction.");
        assertNull(objectContext.getVerifiedStatements());

        /*
         * Round trip through YAML and JSON keeps the block.
         */
        DataContract reread = BitolDocumentFormatter.parseDataContract(BitolDocumentFormatter.toYAML(contract));

        assertEquals(reread.getContext(), context);
        assertEquals(reread.getSchema().get(0).getContext(), objectContext);
        assertEquals(BitolDocumentFormatter.parseDataContract(BitolDocumentFormatter.toJSON(contract)), contract);
    }


    /**
     * New server types, a port held as a variable reference (RFC 0050), and the physical encoding (RFC 0043).
     */
    @Test public void testServers() throws IOException
    {
        DataContract hana = contract("hana-server.odcs.yaml");

        assertEquals(DataContractServerType.fromValue(hana.getServers().get(0).getType()), DataContractServerType.HANA);
        assertEquals(hana.getServers().get(0).getPort(), 30015, "a numeric port stays numeric");

        DataContract encoded = contract("s3-server-encoding.odcs.yaml");

        assertEquals(encoded.getServers().get(0).getEncoding(), "UTF-8");
        assertEquals(encoded.getServers().get(1).getEncoding(), "ISO-8859-1");

        DataContract variables = BitolDocumentFormatter.parseDataContract("""
                apiVersion: v3.2.0
                kind: DataContract
                id: var-1
                version: 1.0.0
                status: active
                servers:
                  - server: prod
                    type: postgresql
                    host: ${DB_HOST}
                    port: ${DB_PORT:-5432}
                    database: orders
                """);

        assertEquals(variables.getServers().get(0).getPort(), "${DB_PORT:-5432}", "a variable reference is preserved verbatim");
        assertTrue(BitolDocumentFormatter.toYAML(variables).contains("${DB_PORT:-5432}"));

        for (String type : new String[]{"iceberg", "exasol", "teradata", "ingres", "vectorwise", "versant", "poet", "btrieve", "fastobjects"})
        {
            assertNotNull(DataContractServerType.fromValue(type), type + " is a known server type");
        }
    }


    /**
     * RFC 0035 vendor attribution on custom properties, RFC 0046 SLA extensions and RFC 0047 relationship ids.
     */
    @Test public void testExtensions() throws IOException
    {
        DataContract vendor = contract("vendor.odcs.yaml");

        assertEquals(vendor.getCustomProperties().get(0).getVendor(), "confluent");
        assertEquals(vendor.getCustomProperties().get(0).getId(), "prop_gov_object");
        assertEquals(vendor.getCustomProperties().get(1).getVendor(), "zeenea");
        assertNull(vendor.getCustomProperties().get(2).getVendor());

        DataContract sla = contract("sla-extensions.odcs.yaml");

        assertEquals(sla.getSlaProperties().get(0).getCustomProperties().get(0).getValue(), "pol-123");
        assertEquals(sla.getSlaProperties().get(1).getAuthoritativeDefinitions().get(0).getUrl(), "https://wiki.acme.com/sla/latency");
        assertEquals(sla.getSlaProperties().get(1).getCustomProperties().size(), 2);

        DataContract ids = contract("stable-id-special-characters.odcs.yaml");

        assertEquals(ids.getSchema().get(0).getId(), "fdir:dataset");
        assertEquals(ids.getSchema().get(0).getProperties().get(1).getId(), "urn:uuid:0f6d2c11-4a7e-4a1d-9b3f-8c5e2a7d1b40");
    }


    /**
     * ODPS v1.1.0: product type, deprecated flags, synonyms and context on products and ports, vendor attribution and
     * variables.
     */
    @Test public void testDataProduct() throws IOException
    {
        DataProduct deprecated = YAML_MAPPER.readValue(readResource("bitol/odps/deprecated-data-product.odps.yaml"), DataProduct.class);

        assertEquals(deprecated.getApiVersion(), "v1.1.0");
        assertEquals(deprecated.getInputPorts().get(0).getDeprecated(), Boolean.TRUE);
        assertEquals(deprecated.getOutputPorts().get(0).getDeprecated(), Boolean.TRUE);
        assertNull(deprecated.getOutputPorts().get(1).getDeprecated());
        assertEquals(deprecated.getManagementPorts().get(0).getDeprecated(), Boolean.TRUE);

        DataProduct vendor = YAML_MAPPER.readValue(readResource("bitol/odps/vendor-custom-properties.odps.yaml"), DataProduct.class);

        assertEquals(vendor.getCustomProperties().get(0).getVendor(), "zeenea");
        assertEquals(vendor.getCustomProperties().get(1).getDescription(), "Cluster name for specific applications");

        DataProduct variables = YAML_MAPPER.readValue(readResource("bitol/odps/variables.odps.yaml"), DataProduct.class);

        assertEquals(variables.getTenant(), "${TENANT:-RetailCorp}");

        DataProduct product = BitolDocumentFormatter.parseDataProduct("""
                apiVersion: v1.1.0
                kind: DataProduct
                id: prod-1
                version: 1.0.0
                status: active
                type: consumerAligned
                deprecated: true
                synonyms:
                  - synonym: Customer 360
                    locale: en-US
                context:
                  instructions: Prefer the tables port for analytics.
                outputPorts:
                  - name: tables
                    synonyms:
                      - synonym: customer tables
                    context:
                      constraints:
                        - constraint: Do not join to the raw events port.
                """);

        assertEquals(product.getType(), "consumerAligned");
        assertEquals(product.getDeprecated(), Boolean.TRUE);
        assertEquals(product.getSynonyms().get(0).getSynonym(), "Customer 360");
        assertEquals(product.getContext().getInstructions(), "Prefer the tables port for analytics.");
        assertNull(product.getOutputPorts().get(0).getContractId(), "contractId is optional in v1.1.0");
        assertEquals(product.getOutputPorts().get(0).getSynonyms().get(0).getSynonym(), "customer tables");
        assertEquals(product.getOutputPorts().get(0).getContext().getConstraints().get(0).getConstraint(), "Do not join to the raw events port.");
        assertEquals(BitolDocumentFormatter.parseDataProduct(BitolDocumentFormatter.toYAML(product)), product);
    }
}
