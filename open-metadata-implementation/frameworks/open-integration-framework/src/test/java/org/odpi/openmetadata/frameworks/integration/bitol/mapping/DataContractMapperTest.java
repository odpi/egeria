/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.mapping;

import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractQualityRule;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSchemaProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractServer;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ConfidentialityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.DataQualityRuleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ConfidentialityLevel;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Validate the parts of the data contract mapping that do not need a metadata store: the properties built for data
 * fields and quality rules from the ODCS full example, and the helper functions.
 */
public class DataContractMapperTest
{
    /**
     * Default constructor
     */
    public DataContractMapperTest()
    {
    }


    private static String readResource(String resourceName) throws IOException
    {
        try (InputStream stream = DataContractMapperTest.class.getClassLoader().getResourceAsStream(resourceName))
        {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }


    /**
     * Data field properties and classifications are derived from the schema property.
     *
     * @throws IOException problem reading the example
     */
    @Test public void testDataFieldMapping() throws IOException
    {
        DataContract       contract = BitolDocumentFormatter.parseDataContract(readResource("bitol/odcs/full-example.odcs.yaml"));
        DataContractMapper mapper   = new DataContractMapper(null);

        DataContractSchemaProperty txnRefDt = contract.getSchema().get(0).getProperties().get(0);
        DataFieldProperties        field    = mapper.getDataFieldProperties(txnRefDt, "DataContract::x::1.1.0::Schema::tbl::transaction_reference_date", "1.1.0");

        assertEquals(field.getQualifiedName(), "DataContract::x::1.1.0::Schema::tbl::transaction_reference_date");
        assertEquals(field.getDisplayName(), "transaction reference date", "business name preferred");
        assertEquals(field.getDataType(), "date", "logical type preferred");
        assertEquals(field.getNamePatterns(), List.of("transaction_reference_date", "txn_ref_dt"));
        assertTrue(field.getIsNullable(), "required=false means nullable");
        assertTrue(field.getAllowsDuplicateValues());
        assertTrue(field.getIsPartitionKey());
        assertEquals(field.getPartitionKeyPosition(), 1);
        assertEquals(field.getVersionIdentifier(), "1.1.0");
        assertEquals(field.getAdditionalProperties().get("bitol.physicalType"), "date");
        assertEquals(field.getAdditionalProperties().get("bitol.transformSourceObjects"), "table_name_1, table_name_2, table_name_3");
        assertEquals(field.getAdditionalProperties().get("bitol.examples"), "2022-10-03, 2020-01-28");
        assertEquals(field.getAdditionalProperties().get("anonymizationStrategy"), "none", "custom properties keep their own names");

        Map<String, ClassificationProperties> classifications = mapper.getFieldClassifications(txnRefDt);
        assertFalse(classifications.containsKey(OpenMetadataType.PRIMARY_KEY_CLASSIFICATION.typeName));
        assertEquals(((ConfidentialityProperties) classifications.get(OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName)).getConfidentialityLevel(),
                     ConfidentialityLevel.UNCLASSIFIED.getOrdinal(), "public maps to unclassified");

        DataContractSchemaProperty rcvrId = contract.getSchema().get(0).getProperties().get(1);
        field = mapper.getDataFieldProperties(rcvrId, "qn", "1.1.0");
        assertFalse(field.getIsPartitionKey());
        assertEquals(field.getPartitionKeyPosition(), 0, "-1 in the document means not set");
        assertEquals(field.getAdditionalProperties().get("bitol.primaryKeyPosition"), "1");

        classifications = mapper.getFieldClassifications(rcvrId);
        assertTrue(classifications.containsKey(OpenMetadataType.PRIMARY_KEY_CLASSIFICATION.typeName));
        assertEquals(((ConfidentialityProperties) classifications.get(OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName)).getConfidentialityLevel(),
                     ConfidentialityLevel.RESTRICTED.getOrdinal());

        DataContractSchemaProperty receiverId = contract.getSchema().get(1).getProperties().get(0);
        field = mapper.getDataFieldProperties(receiverId, "qn", "1.1.0");
        assertFalse(field.getIsNullable(), "required=true");
        assertFalse(field.getAllowsDuplicateValues(), "unique=true");
    }


    /**
     * Quality rules carry the check terms and the comparison.
     *
     * @throws IOException problem reading the example
     */
    @Test public void testQualityRuleMapping() throws IOException
    {
        DataContract       contract = BitolDocumentFormatter.parseDataContract(readResource("bitol/odcs/full-example.odcs.yaml"));
        DataContractMapper mapper   = new DataContractMapper(null);

        DataContractQualityRule   rowCount = contract.getSchema().get(0).getQuality().get(0);
        DataQualityRuleProperties rule     = mapper.getDataQualityRuleProperties(rowCount, "qn::Quality::1", "DataContract::x::1.1.0::Schema::tbl");

        assertEquals(rule.getDisplayName(), "rowCount", "metric used when there is no name");
        assertEquals(rule.getCheckType(), "library");
        assertEquals(rule.getQualityDimension(), "completeness");
        assertEquals(rule.getMetric(), "rowCount");
        assertEquals(rule.getComparisonOperator(), "mustBeGreaterThan");
        assertEquals(rule.getThresholdValues(), List.of("1000000"));
        assertEquals(rule.getMethod(), "reconciliation");
        assertEquals(rule.getSeverity(), "error");
        assertEquals(rule.getBusinessImpact(), "operational");
        assertEquals(rule.getSchedule(), "0 20 * * *");
        assertEquals(rule.getScheduler(), "cron");
        assertEquals(rule.getScope(), "DataContract::x::1.1.0::Schema::tbl");
        assertNull(rule.getExpression());

        DataContractQualityRule nullValues = contract.getSchema().get(0).getProperties().get(2).getQuality().get(0);
        rule = mapper.getDataQualityRuleProperties(nullValues, "qn", "scope");
        assertEquals(rule.getComparisonOperator(), "mustBe");
        assertEquals(rule.getThresholdValues(), List.of("0"));
        assertEquals(rule.getAdditionalProperties().get("COMPARISON_TYPE"), "Greater than");

        DataContractQualityRule between = new DataContractQualityRule();
        between.setMustBeBetween(List.of(10, 20.5));
        assertEquals(DataContractMapper.getComparison(between).getKey(), "mustBeBetween");
        assertEquals(DataContractMapper.getComparison(between).getValue(), List.of("10", "20.5"));
        assertNull(DataContractMapper.getComparison(new DataContractQualityRule()));
    }


    /**
     * Helper functions.
     */
    @Test public void testHelpers()
    {
        assertEquals(DataContractMapper.getConfidentialityLevel("Confidential"), ConfidentialityLevel.CONFIDENTIAL);
        assertEquals(DataContractMapper.getConfidentialityLevel("restricted"), ConfidentialityLevel.RESTRICTED);
        assertEquals(DataContractMapper.getConfidentialityLevel("internal"), ConfidentialityLevel.INTERNAL);
        assertEquals(DataContractMapper.getConfidentialityLevel("PII"), ConfidentialityLevel.SENSITIVE);
        assertNull(DataContractMapper.getConfidentialityLevel("purple"));
        assertNull(DataContractMapper.getConfidentialityLevel(null));

        assertEquals(DataContractMapper.getNamePatterns("a", "b"), List.of("a", "b"));
        assertEquals(DataContractMapper.getNamePatterns("a", "a"), List.of("a"));
        assertEquals(DataContractMapper.getNamePatterns("a", null), List.of("a"));
        assertNull(DataContractMapper.getNamePatterns(null, null));

        DataContractServer server = new DataContractServer();
        server.setHost("db.example.com");
        assertEquals(DataContractMapper.getServerAddress(server), "db.example.com");
        server.setPort(5432);
        assertEquals(DataContractMapper.getServerAddress(server), "db.example.com:5432");
        server.setLocation("s3://bucket/path");
        assertEquals(DataContractMapper.getServerAddress(server), "s3://bucket/path", "location wins");
        assertNull(DataContractMapper.getServerAddress(new DataContractServer()));
    }
}
