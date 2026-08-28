/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.postgresfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource.PostgresTabularDataSetCollectionProvider;
import org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource.PostgresTabularDataSetProvider;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogDestination;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogRecord;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularDataCollection;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;

import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Exercises the PostgreSQL tabular data set resource connectors directly, against a real PostgreSQL database.
 * <br>
 * These two connectors are the part of {@code postgres-server-connectors} that no governance action process
 * reaches: they are <b>resource</b> connectors, handed to whatever needs to read or write a table, rather than
 * services run by a governance engine.  So this test builds the connection itself and asks the connector
 * broker for the connector, which is exactly what the platform does on behalf of a caller that has an asset.
 * <br>
 * The connection is a <b>virtual</b> one, and that structure is the first thing being tested.  The tabular
 * connector does no SQL of its own: it looks through its embedded connectors for a JDBC resource connector and
 * works through that.  A virtual connection whose embedded connection is missing or is the wrong kind produces
 * a connector that builds successfully and then fails on its first call, so getting a working connector out of
 * the broker already says something.
 * <br>
 * Two behaviours are worth knowing about before reading the assertions, because both shape what can be
 * checked here:
 * <ul>
 *     <li>the connector composes {@code INSERT INTO <table> VALUES (...)} by concatenating the supplied values
 *     <b>verbatim</b>, so a caller writing text has to supply it as a SQL literal, quotes included;</li>
 *     <li>{@code deleteRecord} is not implemented yet - it is a no-op - so it is not exercised here.  A test
 *     that called it and asserted nothing would read as coverage it does not have.</li>
 * </ul>
 * Every count the connector reports is checked against the database directly as well, so a connector that
 * agreed with itself but not with PostgreSQL would still fail.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class PostgresTabularDataSetFVT
{
    /**
     * The schema this test creates inside the suite's database.  Its own, rather than the one
     * {@link PostgresFvtTestSupport#getSchemaName()} prepares, because the connector creates the schema itself
     * as part of {@code setColumnDescriptions} - and that is one of the things being tested.
     */
    private static final String TABULAR_SCHEMA_NAME = "postgres_fvt_tabular";

    private static final String FIRST_TABLE_NAME  = "postgres_fvt_data_set";
    private static final String SECOND_TABLE_NAME = "Second Data Set";


    /**
     * The columns both tables are given.  A string and an integer, so that the connector's mapping from the
     * framework's data types onto PostgreSQL column types is exercised in both directions.
     */
    private static final List<TabularColumnDescription> COLUMN_DESCRIPTIONS =
            List.of(new TabularColumnDescription("row_id", DataType.INT, "Identifier of the row.", false, true),
                    new TabularColumnDescription("row_label", DataType.STRING, "Human readable label.", true, false));


    /**
     * Write a table through the tabular data set connector, and check the rows arrived in PostgreSQL.
     *
     * @throws Exception the connector could not be built, or did not do what it was asked
     */
    @Test
    @DisplayName("The tabular data set connector creates its table and writes records into it")
    public void testTabularDataSetConnector() throws Exception
    {
        Connector connector = getConnector(new PostgresTabularDataSetProvider().getConnectorType(), FIRST_TABLE_NAME);

        assertInstanceOf(WritableTabularDataSource.class,
                         connector,
                         "The connector broker returned a " + connector.getClass().getName() + " for the PostgreSQL tabular data"
                                 + " set connector type, which cannot be written to.");

        WritableTabularDataSource dataSet = (WritableTabularDataSource) connector;

        connector.start();

        try
        {
            /*
             * This is what creates the schema and the table - the connector turns the column descriptions into
             * DDL and issues it.  Nothing has created them beforehand.
             */
            dataSet.setColumnDescriptions(COLUMN_DESCRIPTIONS);

            assertEquals(0,
                         dataSet.getRecordCount(),
                         "The table the connector had just created was not empty.");

            /*
             * Values are inserted verbatim, so the text column is supplied as a SQL literal - see the class
             * comment.
             */
            dataSet.appendRecord(List.of("1", "'first row'"));
            dataSet.appendRecord(List.of("2", "'second row'"));
            dataSet.writeRecord(2, List.of("3", "'third row'"));

            assertEquals(3,
                         dataSet.getRecordCount(),
                         "The connector reported a different number of records than the three that were written.");

            /*
             * Checked against the database as well as against the connector, so that a connector that counts
             * its own writes rather than the table's contents would still fail here.
             */
            try (java.sql.Connection databaseConnection = PostgresFvtTestSupport.getServerUnderTestConnection(PostgresFvtTestSupport.getDatabaseName()))
            {
                assertEquals(3,
                             PostgresFvtTestSupport.getRowCount(databaseConnection, TABULAR_SCHEMA_NAME, FIRST_TABLE_NAME),
                             "The connector reported three records but PostgreSQL holds a different number - the writes were"
                                     + " not committed, or went somewhere else.");
            }
        }
        finally
        {
            connector.disconnect();
        }
    }


    /**
     * Switch the collection connector from one table to another and check that each ends up with its own rows.
     * <br>
     * This is the whole difference between the two connectors: the collection connector can be retargeted at a
     * different table without being rebuilt, which is what lets one connector serve a schema full of data sets.
     * The table name is given in canonical form - capitalised words, separated by spaces - and the connector
     * converts it to the snake case PostgreSQL expects, so the assertion looks for {@code second_data_set}.
     *
     * @throws Exception the connector could not be built, or did not do what it was asked
     */
    @Test
    @DisplayName("The tabular data set collection connector can be retargeted at another table")
    public void testTabularDataSetCollectionConnector() throws Exception
    {
        Connector connector = getConnector(new PostgresTabularDataSetCollectionProvider().getConnectorType(), FIRST_TABLE_NAME);

        assertInstanceOf(TabularDataCollection.class,
                         connector,
                         "The connector broker returned a " + connector.getClass().getName() + " for the PostgreSQL tabular data"
                                 + " set collection connector type, which cannot be retargeted at another table.");

        TabularDataCollection     collection = (TabularDataCollection) connector;
        WritableTabularDataSource dataSet    = (WritableTabularDataSource) connector;

        connector.start();

        try
        {
            collection.setTableName(SECOND_TABLE_NAME, "A second data set in the same schema.");

            dataSet.setColumnDescriptions(COLUMN_DESCRIPTIONS);
            dataSet.appendRecord(List.of("10", "'only row'"));

            assertEquals(1,
                         dataSet.getRecordCount(),
                         "The collection connector wrote to the retargeted table but did not read back what it wrote.");

            try (java.sql.Connection databaseConnection = PostgresFvtTestSupport.getServerUnderTestConnection(PostgresFvtTestSupport.getDatabaseName()))
            {
                assertEquals(1,
                             PostgresFvtTestSupport.getRowCount(databaseConnection, TABULAR_SCHEMA_NAME, "second_data_set"),
                             "The collection connector was retargeted at '" + SECOND_TABLE_NAME + "' but its row did not arrive"
                                     + " in second_data_set - the canonical-to-snake-case conversion did not happen as expected.");
            }
        }
        finally
        {
            connector.disconnect();
        }
    }


    /**
     * Remove the schema the connectors created, so that the tests can be run repeatedly.  Best-effort: a
     * failure here should not turn a passing run into a failing one.
     */
    @AfterAll
    public static void dropTabularSchema()
    {
        if (! OMAGPlatformExtension.getBooleanProperty("postgres.fvt.clear.down", true))
        {
            return;
        }

        try (java.sql.Connection databaseConnection = PostgresFvtTestSupport.getServerUnderTestConnection(PostgresFvtTestSupport.getDatabaseName());
             Statement statement = databaseConnection.createStatement())
        {
            statement.execute("drop schema if exists " + TABULAR_SCHEMA_NAME + " cascade");
        }
        catch (Exception error)
        {
            System.out.println("postgres-fvt: could not drop schema " + TABULAR_SCHEMA_NAME + " ("
                                       + error.getClass().getSimpleName() + ": " + error.getMessage() + ")");
        }
    }


    /**
     * Build the virtual connection the tabular connectors need, and get the connector for it from the broker.
     * <br>
     * The shape is the point: the outer connection names the tabular connector and carries its configuration
     * properties, and the single embedded connection is a JDBC resource connector pointed at the database.  The
     * JDBC URL carries {@code currentSchema} because the tabular connector issues its inserts and counts
     * against an unqualified table name - it relies on the connection's search path to put it in the right
     * schema, even though the DDL it generates names the schema explicitly.
     *
     * @param connectorType connector type published by the connector's own provider
     * @param tableName table the connector should work with
     * @return connector, not yet started
     * @throws Exception the connection could not be turned into a connector
     */
    private Connector getConnector(ConnectorType connectorType,
                                   String        tableName) throws Exception
    {
        String jdbcURL = PostgresFvtTestSupport.getJdbcURL(PostgresFvtTestSupport.getDatabaseName())
                                 + "?currentSchema=" + TABULAR_SCHEMA_NAME;

        Endpoint endpoint = new Endpoint();

        endpoint.setQualifiedName("postgres-fvt:tabular:endpoint");
        endpoint.setDisplayName("postgres-fvt tabular data set database");
        endpoint.setNetworkAddress(jdbcURL);

        Connection jdbcConnection = new Connection();

        jdbcConnection.setQualifiedName("postgres-fvt:tabular:jdbc-connection");
        jdbcConnection.setDisplayName("postgres-fvt JDBC connection");
        jdbcConnection.setConnectorType(new JDBCResourceConnectorProvider().getConnectorType());
        jdbcConnection.setEndpoint(endpoint);
        jdbcConnection.setUserId(OMAGPlatformExtension.getServerUnderTestSecret("userId"));
        jdbcConnection.setClearPassword(OMAGPlatformExtension.getServerUnderTestSecret("clearPassword"));

        EmbeddedConnection embeddedConnection = new EmbeddedConnection();

        embeddedConnection.setPosition(0);
        embeddedConnection.setDisplayName("Database connection");
        embeddedConnection.setEmbeddedConnection(jdbcConnection);

        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(PostgresConfigurationProperty.SCHEMA_NAME.getName(), TABULAR_SCHEMA_NAME);
        configurationProperties.put(PostgresConfigurationProperty.SCHEMA_DESCRIPTION.getName(),
                                    "Tabular data sets written by the postgres-fvt suite.");
        configurationProperties.put(PostgresConfigurationProperty.TABLE_NAME.getName(), tableName);
        configurationProperties.put(PostgresConfigurationProperty.TABLE_DESCRIPTION.getName(),
                                    "A tabular data set written by the postgres-fvt suite.");

        assertNotNull(connectorType,
                      "The connector provider does not publish a connector type, so nothing could look this connector up in the"
                              + " open metadata repositories.");

        VirtualConnection virtualConnection = new VirtualConnection();

        virtualConnection.setQualifiedName("postgres-fvt:tabular:" + connectorType.getQualifiedName() + ":" + tableName);
        virtualConnection.setDisplayName("postgres-fvt tabular data set");
        virtualConnection.setConnectorType(connectorType);
        virtualConnection.setConfigurationProperties(configurationProperties);
        virtualConnection.setEmbeddedConnections(List.of(embeddedConnection));

        Connector connector = new ConnectorBroker(getAuditLog()).getConnector(virtualConnection);

        assertNotNull(connector, "The connector broker returned no connector for " + connectorType.getConnectorProviderClassName() + ".");

        return connector;
    }


    /**
     * Return an audit log that prints to the console.
     * <br>
     * The connectors log their failures rather than throwing them with the detail attached, so a connector
     * given no audit log fails with a message that says less than the one it wrote and threw away.  This is
     * cheaper than it looks and makes a failing run readable.
     *
     * @return audit log
     */
    private AuditLog getAuditLog()
    {
        AuditLogDestination destination = new AuditLogDestination(null)
        {
            @Override
            public void addLogRecord(AuditLogRecord logRecord)
            {
                System.out.println("postgres-fvt: " + logRecord.getMessageId() + " " + logRecord.getMessageText());
            }
        };

        return new AuditLog(destination,
                            0,
                            ComponentDevelopmentStatus.STABLE,
                            "postgres-fvt",
                            "PostgreSQL tabular data set connector tests.",
                            null);
    }
}
