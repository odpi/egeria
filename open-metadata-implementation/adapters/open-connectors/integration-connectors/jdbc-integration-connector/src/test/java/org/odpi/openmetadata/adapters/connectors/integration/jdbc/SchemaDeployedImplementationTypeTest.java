/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


/**
 * Checks the technology type given to the schemas catalogued below a database, so that services that act on an
 * asset's technology type recognise them.
 */
public class SchemaDeployedImplementationTypeTest
{
    /**
     * A schema's type follows its database's.
     */
    @Test
    public void testFollowsDatabaseType()
    {
        assertEquals(RelationalDatabaseCataloguer.getSchemaDeployedImplementationType("PostgreSQL Relational Database", "Oracle"),
                     "PostgreSQL Relational Database Schema", "the database's type wins over the product name");
        assertEquals(RelationalDatabaseCataloguer.getSchemaDeployedImplementationType("Oracle Relational Database", null),
                     "Oracle Relational Database Schema");
        assertEquals(RelationalDatabaseCataloguer.getSchemaDeployedImplementationType("Microsoft SQL Server Relational Database", null),
                     "Microsoft SQL Server Relational Database Schema");
        assertEquals(RelationalDatabaseCataloguer.getSchemaDeployedImplementationType("DuckDB Relational Database", null),
                     "DuckDB Relational Database Schema");
    }


    /**
     * A database of another type - such as the product name this connector gives the databases it creates - is
     * matched on the JDBC driver's product name, and anything else is a JDBC schema.
     */
    @Test
    public void testFallsBackToProductName()
    {
        assertEquals(RelationalDatabaseCataloguer.getSchemaDeployedImplementationType("PostgreSQL", "PostgreSQL"),
                     "PostgreSQL Relational Database Schema");
        assertEquals(RelationalDatabaseCataloguer.getSchemaDeployedImplementationType(null, " microsoft sql server "),
                     "Microsoft SQL Server Relational Database Schema");
        assertEquals(RelationalDatabaseCataloguer.getSchemaDeployedImplementationType(null, "H2"),
                     "JDBC Relational Database Schema");
        assertEquals(RelationalDatabaseCataloguer.getSchemaDeployedImplementationType(null, null),
                     "JDBC Relational Database Schema");
    }
}
