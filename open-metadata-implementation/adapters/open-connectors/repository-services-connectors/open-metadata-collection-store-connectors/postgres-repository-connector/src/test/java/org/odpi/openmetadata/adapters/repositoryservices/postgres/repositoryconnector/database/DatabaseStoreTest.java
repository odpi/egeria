/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.database;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Verify the parts of DatabaseStore that can be tested without a database.
 */
public class DatabaseStoreTest
{
    /**
     * A value placed in SQL text is quoted so that a single quote inside it cannot end the literal.
     */
    @Test
    public void testSQLStringLiteral()
    {
        assertEquals(DatabaseStore.getSQLStringLiteral("abc-123"), "'abc-123'");
        assertEquals(DatabaseStore.getSQLStringLiteral("x' or '1'='1"), "'x'' or ''1''=''1'");
        assertEquals(DatabaseStore.getSQLStringLiteral(null), "null");
    }
}
