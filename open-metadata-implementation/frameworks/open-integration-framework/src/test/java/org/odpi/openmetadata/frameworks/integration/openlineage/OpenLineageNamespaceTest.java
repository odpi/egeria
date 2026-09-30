/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Verify the parsing and comparison of OpenLineage namespaces and Egeria network addresses.
 */
public class OpenLineageNamespaceTest
{
    @Test public void testOpenLineageNamespaces()
    {
        OpenLineageNamespace postgres = OpenLineageNamespace.fromNamespace("postgres://DB.example.com:5432");

        assertEquals(postgres.scheme(), "postgres");
        assertEquals(postgres.host(), "db.example.com");
        assertEquals(postgres.port(), "5432");

        OpenLineageNamespace noPort = OpenLineageNamespace.fromNamespace("postgresql://db.example.com");

        assertEquals(noPort.scheme(), "postgres", "Alternative spellings of the scheme are reduced to the OpenLineage one");
        assertEquals(noPort.port(), "5432", "The default port is filled in");

        assertEquals(OpenLineageNamespace.fromNamespace("sqlserver://h:1433").scheme(), "mssql");
        assertEquals(OpenLineageNamespace.fromNamespace("athena://x").scheme(), "awsathena");
        assertEquals(OpenLineageNamespace.fromNamespace("file").scheme(), "file");
        assertNull(OpenLineageNamespace.fromNamespace("file").host());
        assertEquals(OpenLineageNamespace.fromNamespace("arn:aws:glue:us-east-1:123").scheme(), "arn");
    }


    @Test public void testJDBCURLs()
    {
        OpenLineageNamespace postgres = OpenLineageNamespace.fromNetworkAddress("jdbc:postgresql://localhost:5442/sales?currentSchema=public");

        assertEquals(postgres.scheme(), "postgres");
        assertEquals(postgres.host(), "localhost");
        assertEquals(postgres.port(), "5442");
        assertEquals(postgres.database(), "sales");
        assertEquals(postgres.schema(), "public");

        OpenLineageNamespace sqlServer = OpenLineageNamespace.fromNetworkAddress("jdbc:sqlserver://sql.example.com:1433;databaseName=Orders;encrypt=true");

        assertEquals(sqlServer.scheme(), "mssql");
        assertEquals(sqlServer.host(), "sql.example.com");
        assertEquals(sqlServer.database(), "Orders");

        OpenLineageNamespace oracleService = OpenLineageNamespace.fromNetworkAddress("jdbc:oracle:thin:@//ora.example.com:1521/FREEPDB1");

        assertEquals(oracleService.scheme(), "oracle");
        assertEquals(oracleService.host(), "ora.example.com");
        assertEquals(oracleService.port(), "1521");
        assertEquals(oracleService.database(), "FREEPDB1");

        OpenLineageNamespace oracleSID = OpenLineageNamespace.fromNetworkAddress("jdbc:oracle:thin:@ora.example.com:1521:ORCL");

        assertEquals(oracleSID.database(), "ORCL");

        OpenLineageNamespace db2 = OpenLineageNamespace.fromNetworkAddress("jdbc:db2://db2.example.com:50000/BANK");

        assertEquals(db2.scheme(), "db2");
        assertEquals(db2.database(), "BANK");

        OpenLineageNamespace duckDB = OpenLineageNamespace.fromNetworkAddress("jdbc:duckdb:/data/sales.duckdb");

        assertEquals(duckDB.scheme(), "duckdb");
        assertNull(duckDB.host(), "A DuckDB database has no server");
    }


    @Test public void testOtherAddresses()
    {
        OpenLineageNamespace bootstrap = OpenLineageNamespace.fromNetworkAddress("oak.local:9194");

        assertNull(bootstrap.scheme());
        assertEquals(bootstrap.host(), "oak.local");
        assertEquals(bootstrap.port(), "9194");

        OpenLineageNamespace url = OpenLineageNamespace.fromNetworkAddress("http://localhost:8087/api/2.1/unity-catalog");

        assertEquals(url.scheme(), "http");
        assertEquals(url.host(), "localhost");
        assertEquals(url.port(), "8087");

        assertNull(OpenLineageNamespace.fromNetworkAddress("/a/local/path"));
        assertNull(OpenLineageNamespace.fromNetworkAddress("egeria.omag.server.outTopic"), "A topic name is not an address");
    }


    @Test public void testSameServer()
    {
        OpenLineageNamespace namespace = OpenLineageNamespace.fromNamespace("postgres://Localhost:5442");

        assertTrue(namespace.isSameServer(OpenLineageNamespace.fromNetworkAddress("jdbc:postgresql://localhost:5442/sales")));
        assertFalse(namespace.isSameServer(OpenLineageNamespace.fromNetworkAddress("jdbc:postgresql://localhost:5432/sales")), "Different port");
        assertFalse(namespace.isSameServer(OpenLineageNamespace.fromNetworkAddress("jdbc:sqlserver://localhost:5442;databaseName=sales")), "Different technology");

        assertTrue(OpenLineageNamespace.fromNamespace("postgres://db.example.com").isSameServer(OpenLineageNamespace.fromNetworkAddress("jdbc:postgresql://db.example.com:5432/sales")),
                   "A missing port is the technology's default port");

        assertTrue(OpenLineageNamespace.fromNamespace("kafka://oak.local:9194").isSameServer(OpenLineageNamespace.fromNetworkAddress("oak.local:9194")),
                   "A bootstrap server has no scheme but matches the broker");
        assertTrue(OpenLineageNamespace.fromNamespace("kafka://broker").isSameServer(OpenLineageNamespace.fromNetworkAddress("broker:9092")),
                   "Kafka's default port applies to the namespace without a port");
    }


    @Test public void testIsOpenLineageNamespace()
    {
        assertTrue(OpenLineageNamespace.isOpenLineageNamespace("postgres://h:5432"));
        assertTrue(OpenLineageNamespace.isOpenLineageNamespace("file"));
        assertTrue(OpenLineageNamespace.isOpenLineageNamespace("bigquery"));
        assertTrue(OpenLineageNamespace.isOpenLineageNamespace("egeria"));
        assertTrue(OpenLineageNamespace.isOpenLineageNamespace("arn:aws:glue:us-east-1:123"));
        assertFalse(OpenLineageNamespace.isOpenLineageNamespace("catalog.schema"), "A Unity Catalog namespace path is not an OpenLineage namespace");
        assertFalse(OpenLineageNamespace.isOpenLineageNamespace("Coco Pharmaceuticals"));
        assertFalse(OpenLineageNamespace.isOpenLineageNamespace(null));
    }
}
