/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.database;

import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify the parts of QueryBuilder that can be tested without a database.
 */
public class QueryBuilderTest
{
    /**
     * A list of guids is searched for with IN rather than an OR chain.  PostgreSQL plans a long OR chain as a
     * parallel, JIT-compiled query, which crashes some builds of the server; an IN list is planned as a single,
     * serial index scan.
     */
    @Test
    public void testGUIDListUsesIn() throws Exception
    {
        QueryBuilder queryBuilder = new QueryBuilder("entity", "entity_attribute_value", null, "test");

        queryBuilder.setGUIDList(List.of("guid-1", "guid-2", "guid-3"));

        String whereClause = queryBuilder.getAsOfTimeWhereClause();

        assertTrue(whereClause.contains("instance_guid in ('guid-1', 'guid-2', 'guid-3')"), whereClause);
        assertFalse(whereClause.contains(" or "), whereClause);
    }


    /**
     * A quote inside a guid is escaped so that it cannot end the literal.
     */
    @Test
    public void testGUIDListIsEscaped() throws Exception
    {
        QueryBuilder queryBuilder = new QueryBuilder("entity", "entity_attribute_value", null, "test");

        queryBuilder.setGUIDList(List.of("x' or '1'='1"));

        assertTrue(queryBuilder.getAsOfTimeWhereClause().contains("instance_guid in ('x'' or ''1''=''1')"));
    }


    /**
     * With no guid list there is no guid clause at all.
     */
    @Test
    public void testNoGUIDList() throws Exception
    {
        QueryBuilder queryBuilder = new QueryBuilder("entity", "entity_attribute_value", null, "test");

        assertFalse(queryBuilder.getAsOfTimeWhereClause().contains("instance_guid"));
    }
}
