/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.mapping;

import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolContext;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolCustomProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolSynonym;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractEnumValue;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSchemaProperty;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataFieldProperties;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the ODCS v3.2.0 and ODPS v1.1.0 features are carried into open metadata properties and can be read
 * back by the generator helpers, without a metadata store.
 */
public class BitolStandardsUpdateMappingTest
{
    /**
     * Default constructor
     */
    public BitolStandardsUpdateMappingTest()
    {
    }


    private static String readResource(String resourceName) throws IOException
    {
        try (InputStream stream = BitolStandardsUpdateMappingTest.class.getClassLoader().getResourceAsStream(resourceName))
        {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }


    private static DataContractSchemaProperty property(DataContract contract, String name)
    {
        for (DataContractSchemaProperty property : contract.getSchema().get(0).getProperties())
        {
            if (name.equals(property.getName()))
            {
                return property;
            }
        }

        throw new AssertionError("No property " + name);
    }


    /**
     * Synonyms become aliases on the data field and are kept in full as JSON; the semantic type, deprecated flag and
     * enumeration are kept as additional properties.
     */
    @Test public void testDataFieldExtensions() throws IOException
    {
        DataContractMapper mapper = new DataContractMapper(null);

        DataContract metrics = BitolDocumentFormatter.parseDataContract(readResource("bitol/odcs/measures-and-dimensions.odcs.yaml"));
        DataFieldProperties total = mapper.getDataFieldProperties(property(metrics, "total_turnover_euros"), "qn", "1.0.0");

        assertEquals(total.getAliases(), List.of("TO", "Sales", "Chiffre d'affaires"));
        assertEquals(total.getAdditionalProperties().get("bitol.semanticType"), "measure");

        List<BitolSynonym> synonyms = BitolGeneratorBase.getSynonyms(total.getAdditionalProperties());

        assertEquals(synonyms, property(metrics, "total_turnover_euros").getSynonyms(), "synonyms round trip with their locale, source and id");
        assertNull(BitolGeneratorBase.getDeprecated(total.getAdditionalProperties()));

        DataContract deprecated = BitolDocumentFormatter.parseDataContract(readResource("bitol/odcs/deprecated.odcs.yaml"));
        DataFieldProperties category = mapper.getDataFieldProperties(property(deprecated, "category_id"), "qn", "1.0.0");

        assertEquals(category.getAdditionalProperties().get("bitol.deprecated"), "true");
        assertEquals(BitolGeneratorBase.getDeprecated(category.getAdditionalProperties()), Boolean.TRUE);

        DataContract enums = BitolDocumentFormatter.parseDataContract(readResource("bitol/odcs/enum.odcs.yaml"));
        DataFieldProperties status = mapper.getDataFieldProperties(property(enums, "status"), "qn", "1.0.0");

        List<DataContractEnumValue> restored = BitolGeneratorBase.fromJSONList(status.getAdditionalProperties().get("bitol.enum"), DataContractEnumValue.class);

        assertEquals(restored, property(enums, "status").getEnumValues(), "the enumeration round trips through JSON");

        DataContract vectors = BitolDocumentFormatter.parseDataContract(readResource("bitol/odcs/vector.odcs.yaml"));
        DataFieldProperties embedding = mapper.getDataFieldProperties(property(vectors, "body_embedding"), "qn", "1.0.0");

        assertEquals(embedding.getDataType(), "vector");
        assertEquals(embedding.getAdditionalProperties().get("bitol.dimensions"), "1536");
        assertEquals(embedding.getAdditionalProperties().get("bitol.normalized"), "true");
        assertEquals(embedding.getAdditionalProperties().get("bitol.embeddingModel"), "openai/text-embedding-3-small");
    }


    /**
     * Custom properties with a vendor, description or id are kept in full and restored by the generator; plain ones
     * are still exposed as simple additional properties.
     */
    @Test public void testCustomPropertyVendors() throws IOException
    {
        DataContract       vendor = BitolDocumentFormatter.parseDataContract(readResource("bitol/odcs/vendor.odcs.yaml"));
        DataContractMapper mapper = new DataContractMapper(null);

        Map<String, String> additionalProperties = new HashMap<>();

        mapper.addCustomProperties(vendor.getCustomProperties(), additionalProperties);

        assertEquals(additionalProperties.get("genericFlag"), "true");
        assertNotNull(additionalProperties.get("bitol.customProperties"), "the detailed list is kept because vendors are present");

        List<BitolCustomProperty> restored = BitolGeneratorBase.getCustomProperties(additionalProperties);

        assertEquals(restored.size(), 3);
        assertEquals(restored.get(0).getVendor(), "confluent");
        assertEquals(restored.get(0).getId(), "prop_gov_object");
        assertEquals(restored.get(1).getVendor(), "zeenea");
        assertNull(restored.get(2).getVendor());

        /*
         * Without vendors the simple map is enough and no JSON copy is kept.
         */
        Map<String, String> plain = new HashMap<>();
        BitolCustomProperty simple = new BitolCustomProperty();
        simple.setProperty("refRulesetName");
        simple.setValue("gcsc.ruleset.name");
        mapper.addCustomProperties(List.of(simple), plain);

        assertNull(plain.get("bitol.customProperties"));
        assertEquals(BitolGeneratorBase.getCustomProperties(plain).get(0).getValue(), "gcsc.ruleset.name");
    }


    /**
     * The AI context block round trips through the JSON kept on the element.
     */
    @Test public void testContextRoundTrip() throws IOException
    {
        DataContract contract = BitolDocumentFormatter.parseDataContract("""
                apiVersion: v3.2.0
                kind: DataContract
                id: ctx-2
                version: 1.0.0
                status: active
                context:
                  instructions: Use for reporting only.
                  constraints:
                    - constraint: Never expose customer names.
                """);

        DataContractMapper  mapper               = new DataContractMapper(null);
        Map<String, String> additionalProperties = new HashMap<>();

        additionalProperties.put("bitol.context", mapper.toJSON(contract.getContext()));

        BitolContext restored = BitolGeneratorBase.getContext(additionalProperties);

        assertEquals(restored, contract.getContext());
        assertTrue(additionalProperties.get("bitol.context").contains("Never expose customer names."));
        assertNull(BitolGeneratorBase.getContext(new HashMap<>()));
    }
}
