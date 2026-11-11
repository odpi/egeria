/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage.postgres;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.expectThrows;

/**
 * Validate the helpers of the PostgreSQL OpenLineage log store that do not need a database.
 */
public class PostgresOpenLineageLogStoreSchemaTest
{
    /**
     * Schema names are quoted, so only names that would end the identifier, or be truncated, are rejected.
     */
    @Test
    public void testSchemaNames()
    {
        assertTrue(PostgresOpenLineageLogStoreSchema.isValidSchemaName("open_lineage"));
        assertTrue(PostgresOpenLineageLogStoreSchema.isValidSchemaName("Open Lineage-Log"));
        assertFalse(PostgresOpenLineageLogStoreSchema.isValidSchemaName(null));
        assertFalse(PostgresOpenLineageLogStoreSchema.isValidSchemaName("  "));
        assertFalse(PostgresOpenLineageLogStoreSchema.isValidSchemaName("bad\"; DROP SCHEMA public; --"));
        assertFalse(PostgresOpenLineageLogStoreSchema.isValidSchemaName("x".repeat(64)));

        expectThrows(IllegalArgumentException.class, () -> new PostgresOpenLineageLogStoreSchema("a\"b"));

        PostgresOpenLineageLogStoreSchema schema = new PostgresOpenLineageLogStoreSchema("Open Lineage");

        assertEquals(schema.table(PostgresOpenLineageLogStoreSchema.RUN_TABLE), "\"Open Lineage\".ol_run");
        assertTrue(schema.getDDLStatements().get(0).contains("CREATE SCHEMA IF NOT EXISTS \"Open Lineage\""));
        assertTrue(schema.getDDLStatements().stream().allMatch(ddl -> ddl.contains("IF NOT EXISTS")), "every statement must be safe to rerun");
        assertEquals(schema.getPurgeSQL().size(), 4);
    }


    /**
     * A missing namespace is stored as an empty string and read back as null.
     */
    @Test
    public void testNamespaces()
    {
        assertEquals(PostgresOpenLineageLogStoreSchema.toStoredNamespace(null), "");
        assertEquals(PostgresOpenLineageLogStoreSchema.toStoredNamespace("ns"), "ns");
        assertNull(PostgresOpenLineageLogStoreSchema.fromStoredNamespace(""));
        assertNull(PostgresOpenLineageLogStoreSchema.fromStoredNamespace(null));
        assertEquals(PostgresOpenLineageLogStoreSchema.fromStoredNamespace("ns"), "ns");
    }


    /**
     * The daysToCleanUp value may be a number or a string; anything else, or a negative number, means no purge.
     */
    @Test
    public void testDaysToCleanUp()
    {
        assertEquals(PostgresOpenLineageLogStoreCatalogTargetProcessor.parseDaysToCleanUp(30), Long.valueOf(30));
        assertEquals(PostgresOpenLineageLogStoreCatalogTargetProcessor.parseDaysToCleanUp(30L), Long.valueOf(30));
        assertEquals(PostgresOpenLineageLogStoreCatalogTargetProcessor.parseDaysToCleanUp(" 7 "), Long.valueOf(7));
        assertEquals(PostgresOpenLineageLogStoreCatalogTargetProcessor.parseDaysToCleanUp(0), Long.valueOf(0));
        assertNull(PostgresOpenLineageLogStoreCatalogTargetProcessor.parseDaysToCleanUp(null));
        assertNull(PostgresOpenLineageLogStoreCatalogTargetProcessor.parseDaysToCleanUp(-1));
        assertNull(PostgresOpenLineageLogStoreCatalogTargetProcessor.parseDaysToCleanUp("a week"));
        assertNull(PostgresOpenLineageLogStoreCatalogTargetProcessor.parseDaysToCleanUp(1.5));
    }


    /**
     * The event hash identifies identical deliveries of an event.
     *
     * @throws Exception MD5 not available
     */
    @Test
    public void testEventHash() throws Exception
    {
        String hash = PostgresOpenLineageLogStoreWriter.getEventHash("{\"eventType\":\"START\"}");

        assertEquals(hash.length(), 32);
        assertEquals(hash, PostgresOpenLineageLogStoreWriter.getEventHash("{\"eventType\":\"START\"}"));
        assertFalse(hash.equals(PostgresOpenLineageLogStoreWriter.getEventHash("{\"eventType\":\"COMPLETE\"}")));
    }
}
