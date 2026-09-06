/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.mapping;

import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolCustomProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractQualityRule;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSLAProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Validate the parts of the generators that do not need a metadata store, and the lossless round trip of the SLA
 * properties through the JSON fragment kept on the service level objective.
 */
public class BitolGeneratorTest
{
    /**
     * Default constructor
     */
    public BitolGeneratorTest()
    {
    }


    private static String readResource(String resourceName) throws IOException
    {
        try (InputStream stream = BitolGeneratorTest.class.getClassLoader().getResourceAsStream(resourceName))
        {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }


    /**
     * Status and custom property derivation.
     */
    @Test public void testFundamentalHelpers()
    {
        assertEquals(BitolGeneratorBase.getBitolStatus(ContentStatus.ACTIVE, null), "active");
        assertEquals(BitolGeneratorBase.getBitolStatus(ContentStatus.DRAFT, null), "draft");
        assertEquals(BitolGeneratorBase.getBitolStatus(ContentStatus.PREPARED, null), "draft");
        assertEquals(BitolGeneratorBase.getBitolStatus(ContentStatus.PROPOSED, null), "proposed");
        assertEquals(BitolGeneratorBase.getBitolStatus(ContentStatus.DEPRECATED, null), "deprecated");
        assertEquals(BitolGeneratorBase.getBitolStatus(ContentStatus.OBSOLETE, null), "retired");
        assertEquals(BitolGeneratorBase.getBitolStatus(ContentStatus.OTHER, "in-review"), "in-review");
        assertNull(BitolGeneratorBase.getBitolStatus(null, "x"));

        Map<String, String> additionalProperties = Map.of("bitol.domain", "seller",
                                                          "bitol.kind", "DataProduct",
                                                          "refRulesetName", "gcsc.ruleset.name",
                                                          "somePropertyName", "property.value");

        List<BitolCustomProperty> customProperties = BitolGeneratorBase.getCustomProperties(additionalProperties);

        assertEquals(customProperties.size(), 2, "bitol bookkeeping is not a custom property");
        assertEquals(customProperties.get(0).getProperty(), "refRulesetName");
        assertEquals(customProperties.get(0).getValue(), "gcsc.ruleset.name");
        assertEquals(BitolGeneratorBase.getBitolValue(additionalProperties, "domain"), "seller");
        assertNull(BitolGeneratorBase.getBitolValue(additionalProperties, "tenant"));
        assertNull(BitolGeneratorBase.getCustomProperties(null));
        assertNull(BitolGeneratorBase.getCustomProperties(Map.of("bitol.only", "x")));
    }


    /**
     * The comparison operators round trip through the stored operator name and threshold strings.
     */
    @Test public void testComparisonRoundTrip()
    {
        DataContractQualityRule rule = new DataContractQualityRule();

        DataContractGenerator.setComparison(rule, "mustBeGreaterThan", List.of("1000000"));
        assertEquals(rule.getMustBeGreaterThan(), 1000000L);
        assertEquals(DataContractMapper.getComparison(rule).getKey(), "mustBeGreaterThan");

        rule = new DataContractQualityRule();
        DataContractGenerator.setComparison(rule, "mustBe", List.of("0"));
        assertEquals(rule.getMustBe(), 0L);

        rule = new DataContractQualityRule();
        DataContractGenerator.setComparison(rule, "mustBe", List.of("PASS"));
        assertEquals(rule.getMustBe(), "PASS", "non-numeric thresholds stay strings");

        rule = new DataContractQualityRule();
        DataContractGenerator.setComparison(rule, "mustBeBetween", List.of("10", "20.5"));
        assertEquals(rule.getMustBeBetween(), List.of(10L, 20.5));
        assertEquals(DataContractMapper.getComparison(rule).getValue(), List.of("10", "20.5"));

        rule = new DataContractQualityRule();
        DataContractGenerator.setComparison(rule, "unknownOperator", List.of("1"));
        assertNull(DataContractMapper.getComparison(rule));

        assertEquals(DataContractGenerator.toNumber("42"), 42L);
        assertEquals(DataContractGenerator.toNumber("4.2"), 4.2);
        assertNull(DataContractGenerator.toNumber("many"));
    }


    /**
     * SLA properties survive the JSON fragment kept on the service level objective.
     *
     * @throws IOException problem reading the example
     */
    @Test public void testSLAFragmentRoundTrip() throws IOException
    {
        DataContract contract = BitolDocumentFormatter.parseDataContract(readResource("bitol/odcs/full-example.odcs.yaml"));

        String                        json     = BitolDocumentFormatter.toJSONFragment(contract.getSlaProperties());
        List<DataContractSLAProperty> restored = BitolDocumentFormatter.fromJSONFragmentList(json, DataContractSLAProperty.class);

        assertEquals(restored, contract.getSlaProperties());
        assertEquals(restored.size(), 8);
        assertEquals(restored.get(0).getProperty(), "latency");
        assertEquals(restored.get(0).getValue(), 4);
        assertEquals(restored.get(6).getDriver(), "regulatory");
    }
}
