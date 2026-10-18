/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * The request body for configuring a PostgreSQL local repository is optional, so the storage properties may be
 * null.  The secrets store endpoint already fell back to its example value, but the database URL dereferenced the
 * missing properties.
 */
public class PostgresRepositoryConnectionTest
{
    private static final String DATABASE_URL_PROPERTY = "databaseURL";


    @Test
    public void testNullStorageProperties()
    {
        Connection connection = new ConnectorConfigurationFactory().getPostgresRepositoryLocalConnection("testServer", null);

        assertNotNull(getJDBCResourceConnection(connection).getEndpoint().getNetworkAddress());
    }


    @Test
    public void testSuppliedDatabaseURL()
    {
        final String databaseURL = "jdbc:postgresql://localhost:5442/egeria";

        Connection connection = new ConnectorConfigurationFactory().getPostgresRepositoryLocalConnection("testServer",
                                                                                                        Map.of(DATABASE_URL_PROPERTY, databaseURL));

        assertEquals(getJDBCResourceConnection(connection).getEndpoint().getNetworkAddress(), databaseURL);
    }


    private static Connection getJDBCResourceConnection(Connection postgresConnection)
    {
        return ((VirtualConnection)postgresConnection).getEmbeddedConnections().get(0).getEmbeddedConnection();
    }
}
