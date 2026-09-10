/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol;

import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolAuthoritativeDefinitionType;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolStatus;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolSupportTool;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractLogicalType;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractQualityMetric;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractQualityRule;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractRelationship;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSchemaObject;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSchemaProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractServer;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractServerType;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentTest.YAML_MAPPER;
import static org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentTest.readResource;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Validate the detailed mapping of the ODCS full example onto the DataContract beans.
 */
public class DataContractTest
{
    /**
     * Default constructor
     */
    public DataContractTest()
    {
    }


    /**
     * Check the fundamentals, servers, pricing, team, roles, SLA, support and custom property sections.
     *
     * @throws IOException problem reading the example
     */
    @Test public void testFullExampleSections() throws IOException
    {
        DataContract contract = YAML_MAPPER.readValue(readResource("bitol/odcs/full-example.odcs.yaml"), DataContract.class);

        assertEquals(contract.getApiVersion(), "v3.2.0");
        assertEquals(contract.getId(), "53581432-6c55-4ba2-a65f-72344a91553a");
        assertEquals(contract.getVersion(), "1.1.0");
        assertEquals(BitolStatus.fromValue(contract.getStatus()), BitolStatus.ACTIVE);
        assertEquals(contract.getDomain(), "seller");
        assertEquals(contract.getTenant(), "ClimateQuantumInc");
        assertEquals(contract.getDataProduct(), "my quantum");
        assertEquals(contract.getDescription().getPurpose(), "Views built on top of the seller tables.");
        assertEquals(contract.getDescription().getAuthoritativeDefinitions().get(0).getType(), "privacy-statement");
        assertEquals(contract.getTags(), List.of("transactions"));
        assertEquals(contract.getContractCreatedTs(), "2022-11-15T02:59:43+00:00");

        assertEquals(contract.getAuthoritativeDefinitions().size(), 1);
        assertEquals(contract.getAuthoritativeDefinitions().get(0).getType(), "canonical");
        assertNull(BitolAuthoritativeDefinitionType.fromValue("canonical"));

        /*
         * Servers: the type-specific location properties land in the explicit fields.
         */
        assertEquals(contract.getServers().size(), 1);
        DataContractServer server = contract.getServers().get(0);
        assertEquals(server.getServer(), "my-postgres");
        assertEquals(DataContractServerType.fromValue(server.getType()), DataContractServerType.POSTGRES);
        assertEquals(server.getHost(), "localhost");
        assertEquals(server.getPort(), Integer.valueOf(5432));
        assertEquals(server.getDatabase(), "pypl-edw");
        assertEquals(server.getSchema(), "pp_access_views");
        assertNull(server.getAdditionalProperties(), "all server properties in the example are modelled explicitly");

        assertEquals(contract.getPrice().getPriceAmount().doubleValue(), 9.95, 0.0001);
        assertEquals(contract.getPrice().getPriceCurrency(), "USD");

        assertEquals(contract.getTeam().getName(), "my-team");
        assertEquals(contract.getTeam().getMembers().size(), 3);
        assertEquals(contract.getTeam().getMembers().get(0).getReplacedByUsername(), "mhopper");
        assertEquals(contract.getTeam().getMembers().get(2).getDescription(), "Keeper of the grail");

        assertEquals(contract.getRoles().size(), 4);
        assertEquals(contract.getRoles().get(3).getAccess(), "write");
        assertEquals(contract.getRoles().get(0).getSecondLevelApprovers(), "mandolorian");

        assertEquals(contract.getSlaProperties().size(), 8);
        assertEquals(contract.getSlaProperties().get(0).getProperty(), "latency");
        assertEquals(contract.getSlaProperties().get(0).getValue(), 4);
        assertEquals(contract.getSlaProperties().get(0).getUnit(), "d");
        assertEquals(contract.getSlaProperties().get(1).getValue(), "2022-05-12T09:30:10-08:00");
        assertEquals(contract.getSlaProperties().get(5).getValueExt(), 1);
        assertEquals(contract.getSlaProperties().get(6).getDriver(), "regulatory");

        assertEquals(contract.getSupport().size(), 4);
        assertEquals(BitolSupportTool.fromValue(contract.getSupport().get(0).getTool()), BitolSupportTool.SLACK);
        assertEquals(contract.getSupport().get(1).getUrl(), "mailto:datacontract-ann@bitol.io");
        assertEquals(contract.getSupport().get(3).getCustomProperties().get(0).getProperty(), "servicehours");

        assertEquals(contract.getCustomProperties().size(), 3);
        assertEquals(contract.getCustomProperties().get(2).getValue(), List.of("cluster name"));
    }


    /**
     * Check the schema section including nested relationships and quality rules.
     *
     * @throws IOException problem reading the example
     */
    @Test public void testFullExampleSchema() throws IOException
    {
        DataContract contract = YAML_MAPPER.readValue(readResource("bitol/odcs/full-example.odcs.yaml"), DataContract.class);

        assertEquals(contract.getSchema().size(), 2);

        DataContractSchemaObject table = contract.getSchema().get(0);

        assertEquals(table.getId(), "tbl_obj");
        assertEquals(table.getName(), "tbl");
        assertEquals(table.getPhysicalName(), "tbl_1");
        assertEquals(table.getPhysicalType(), "table");
        assertEquals(table.getBusinessName(), "Core Payment Metrics");
        assertEquals(table.getTags(), List.of("finance", "payments"));
        assertEquals(table.getAuthoritativeDefinitions().size(), 2);
        assertEquals(table.getDataGranularityDescription(), "Aggregation on columns txn_ref_dt, pmt_txn_id");
        assertEquals(table.getCustomProperties().get(0).getValue(), List.of("txn_ref_dt", "rcvr_id"));

        /*
         * Composite key relationship at the schema object level: from and to are lists.
         */
        assertEquals(table.getRelationships().size(), 1);
        DataContractRelationship composite = table.getRelationships().get(0);
        assertEquals(composite.getType(), "foreignKey");
        assertEquals(composite.getFrom(), List.of("tbl.rcvr_id", "tbl.rcvr_cntry_code"));
        assertEquals(composite.getTo(), List.of("receivers.id", "receivers.country_code"));
        assertEquals(composite.getCustomProperties().get(1).getValue(), "many-to-one");

        /*
         * Object level quality rule.
         */
        assertEquals(table.getQuality().size(), 1);
        DataContractQualityRule rowCount = table.getQuality().get(0);
        assertEquals(DataContractQualityMetric.fromValue(rowCount.getMetric()), DataContractQualityMetric.ROW_COUNT);
        assertEquals(rowCount.getMustBeGreaterThan().longValue(), 1000000L);
        assertEquals(rowCount.getMethod(), "reconciliation");
        assertEquals(rowCount.getSchedule(), "0 20 * * *");

        /*
         * Properties.
         */
        assertEquals(table.getProperties().size(), 3);

        DataContractSchemaProperty txnRefDt = table.getProperties().get(0);
        assertEquals(txnRefDt.getName(), "transaction_reference_date");
        assertEquals(txnRefDt.getPhysicalName(), "txn_ref_dt");
        assertEquals(DataContractLogicalType.fromValue(txnRefDt.getLogicalType()), DataContractLogicalType.DATE);
        assertEquals(txnRefDt.getPhysicalType(), "date");
        assertEquals(txnRefDt.getPrimaryKey(), Boolean.FALSE);
        assertEquals(txnRefDt.getPrimaryKeyPosition(), Integer.valueOf(-1));
        assertEquals(txnRefDt.getPartitioned(), Boolean.TRUE);
        assertEquals(txnRefDt.getPartitionKeyPosition(), Integer.valueOf(1));
        assertEquals(txnRefDt.getClassification(), "public");
        assertEquals(txnRefDt.getTransformSourceObjects().size(), 3);
        assertTrue(txnRefDt.getTransformLogic().startsWith("sel t1.txn_dt"));
        assertEquals(txnRefDt.getExamples(), List.of("2022-10-03", "2020-01-28"));
        assertTrue(txnRefDt.getTags().isEmpty());
        assertEquals(txnRefDt.getRequired(), Boolean.FALSE);
        assertNull(txnRefDt.getUnique(), "unique is not set in the example so stays null rather than defaulting");

        DataContractSchemaProperty rcvrId = table.getProperties().get(1);
        assertEquals(rcvrId.getPrimaryKey(), Boolean.TRUE);
        assertEquals(rcvrId.getPrimaryKeyPosition(), Integer.valueOf(1));
        assertEquals(rcvrId.getPhysicalType(), "varchar(18)");
        assertEquals(rcvrId.getClassification(), "restricted");
        /*
         * Property level relationship: from is implicit.
         */
        assertEquals(rcvrId.getRelationships().size(), 1);
        assertNull(rcvrId.getRelationships().get(0).getFrom());
        assertEquals(rcvrId.getRelationships().get(0).getTo(), List.of("receivers.id"));

        DataContractSchemaProperty countryCode = table.getProperties().get(2);
        assertEquals(countryCode.getEncryptedName(), "rcvr_cntry_code_encrypted");
        assertEquals(countryCode.getAuthoritativeDefinitions().size(), 3);
        assertEquals(BitolAuthoritativeDefinitionType.fromValue(countryCode.getAuthoritativeDefinitions().get(2).getType()),
                     BitolAuthoritativeDefinitionType.IMPLEMENTATION);
        assertEquals(countryCode.getQuality().size(), 1);
        DataContractQualityRule nullValues = countryCode.getQuality().get(0);
        assertEquals(nullValues.getMetric(), "nullValues");
        assertEquals(nullValues.getMustBe(), 0);
        assertEquals(nullValues.getDimension(), "completeness");
        assertEquals(nullValues.getSeverity(), "error");
        assertEquals(nullValues.getCustomProperties().size(), 3);
        assertNull(nullValues.getCustomProperties().get(0).getValue());
        assertEquals(nullValues.getCustomProperties().get(2).getValue(), "Greater than");

        DataContractSchemaObject receivers = contract.getSchema().get(1);
        assertEquals(receivers.getProperties().size(), 5);
        assertEquals(receivers.getProperties().get(0).getUnique(), Boolean.TRUE);
        assertEquals(receivers.getProperties().get(0).getRequired(), Boolean.TRUE);
        assertFalse(receivers.getProperties().get(3).getRequired());
        assertNotNull(receivers.getProperties().get(3).getRelationships());
    }


    /**
     * Check nested object and array properties.  In this example the nesting is inside the array item descriptions.
     *
     * @throws IOException problem reading the example
     */
    @Test public void testAllSchemaTypes() throws IOException
    {
        DataContract contract = YAML_MAPPER.readValue(readResource("bitol/odcs/all-schema-types.odcs.yaml"), DataContract.class);

        assertEquals(contract.getApiVersion(), "v3.0.2");
        assertTrue(contract.hasSupportedApiVersion());
        assertEquals(contract.getSchema().size(), 3);

        DataContractSchemaObject anObject = contract.getSchema().get(1);
        assertEquals(anObject.getName(), "AnObject");
        assertEquals(DataContractLogicalType.fromValue(anObject.getLogicalType()), DataContractLogicalType.OBJECT);
        assertEquals(anObject.getProperties().size(), 1);

        DataContractSchemaProperty streetLines = anObject.getProperties().get(0);
        assertEquals(streetLines.getName(), "street_lines");
        assertEquals(DataContractLogicalType.fromValue(streetLines.getLogicalType()), DataContractLogicalType.ARRAY);
        assertNotNull(streetLines.getItems());
        assertEquals(streetLines.getItems().getLogicalType(), "string");
        assertEquals(streetLines.getItems().getPhysicalType(), "string");
        assertNull(streetLines.getItems().getName());
        assertNull(streetLines.getProperties());

        DataContractSchemaObject anotherObject = contract.getSchema().get(2);
        DataContractSchemaProperty x = anotherObject.getProperties().get(0);
        assertEquals(x.getName(), "x");
        assertEquals(x.getItems().getLogicalType(), "object");
        assertEquals(x.getItems().getProperties().size(), 2);
        assertEquals(x.getItems().getProperties().get(0).getName(), "id");
        assertEquals(x.getItems().getProperties().get(1).getPhysicalType(), "VARCHAR(15)");
        assertFalse(x.getItems().getProperties().get(1).getName().isEmpty());
    }
}
