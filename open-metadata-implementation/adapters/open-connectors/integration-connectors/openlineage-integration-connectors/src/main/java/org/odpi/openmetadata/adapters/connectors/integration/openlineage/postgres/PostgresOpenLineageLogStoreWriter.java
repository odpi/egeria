/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage.postgres;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HexFormat;
import java.util.List;


/**
 * PostgresOpenLineageLogStoreWriter issues the SQL that maintains one PostgreSQL OpenLineage log store: it creates
 * the schema and tables, stores each event with its contribution to the digest in one transaction, and purges old
 * records.  Each call takes its own connection from the data source and returns it before it returns.
 */
public class PostgresOpenLineageLogStoreWriter
{
    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    private final DataSource                        dataSource;
    private final PostgresOpenLineageLogStoreSchema schema;


    /**
     * Constructor
     *
     * @param dataSource source of pooled connections (auto-commit disabled)
     * @param schema definitions of the log store
     */
    public PostgresOpenLineageLogStoreWriter(DataSource                        dataSource,
                                             PostgresOpenLineageLogStoreSchema schema)
    {
        this.dataSource = dataSource;
        this.schema     = schema;
    }


    /**
     * Create the schema, its tables and indexes, if they do not exist.
     *
     * @throws SQLException problem with the database
     */
    public void createSchema() throws SQLException
    {
        /*
         * The connection is returned to the pool when this block exits.  The pool runs with auto-commit disabled,
         * so the work is rolled back if the commit is not reached.
         */
        try (java.sql.Connection databaseConnection = dataSource.getConnection();
             Statement           statement          = databaseConnection.createStatement())
        {
            for (String ddl : schema.getDDLStatements())
            {
                statement.execute(ddl);
            }

            databaseConnection.commit();
        }
    }


    /**
     * Store an event and its contribution to the digest in one transaction.  An event that is already stored
     * (because it has been delivered twice) is skipped, so the digest counts it once.
     *
     * @param digest digest of the event
     * @param jsonEvent event as JSON
     * @return true if the event was stored; false if it was already there
     * @throws Exception problem with the database or the event
     */
    public boolean store(PostgresOpenLineageEventDigest digest,
                         String                         jsonEvent) throws Exception
    {
        try (java.sql.Connection databaseConnection = dataSource.getConnection())
        {
            boolean stored = insertEvent(databaseConnection, digest, jsonEvent);

            if (stored)
            {
                upsertRun(databaseConnection, digest);
                upsertRunDatasets(databaseConnection, digest);
                insertAssertions(databaseConnection, digest);
                insertTests(databaseConnection, digest);
            }

            databaseConnection.commit();

            return stored;
        }
    }


    /**
     * Insert the raw event.
     *
     * @param databaseConnection connection in the event's transaction
     * @param digest digest of the event
     * @param jsonEvent event as JSON
     * @return true if it was inserted; false if it was already stored
     * @throws Exception problem with the database or the hash
     */
    private boolean insertEvent(java.sql.Connection            databaseConnection,
                                PostgresOpenLineageEventDigest digest,
                                String                         jsonEvent) throws Exception
    {
        try (PreparedStatement statement = databaseConnection.prepareStatement(schema.getInsertEventSQL()))
        {
            statement.setTimestamp(1, toTimestamp(digest.getEventTime()));
            statement.setString(2, digest.getEventType());
            statement.setString(3, digest.getRunId());
            statement.setString(4, digest.getJobNamespace());
            statement.setString(5, digest.getJobName());
            statement.setString(6, digest.getProducer());
            statement.setString(7, getEventHash(jsonEvent));
            statement.setString(8, jsonEvent);

            return statement.executeUpdate() == 1;
        }
    }


    /**
     * Add the event to its run.
     *
     * @param databaseConnection connection in the event's transaction
     * @param digest digest of the event
     * @throws Exception problem with the database or the facet
     */
    private void upsertRun(java.sql.Connection            databaseConnection,
                           PostgresOpenLineageEventDigest digest) throws Exception
    {
        try (PreparedStatement statement = databaseConnection.prepareStatement(schema.getUpsertRunSQL()))
        {
            statement.setString(1, digest.getRunId());
            statement.setString(2, digest.getJobNamespace());
            statement.setString(3, digest.getJobName());
            statement.setString(4, digest.getRun().status());
            statement.setTimestamp(5, toTimestamp(digest.getRun().startTime()));
            statement.setTimestamp(6, toTimestamp(digest.getRun().endTime()));
            statement.setTimestamp(7, toTimestamp(digest.getEventTime()));
            statement.setTimestamp(8, toTimestamp(digest.getEventTime()));
            statement.setString(9, (digest.getGovernanceAction() == null) ? null : OBJECT_WRITER.writeValueAsString(digest.getGovernanceAction()));

            statement.executeUpdate();
        }
    }


    /**
     * Record the datasets read and written by the run.
     *
     * @param databaseConnection connection in the event's transaction
     * @param digest digest of the event
     * @throws Exception problem with the database
     */
    private void upsertRunDatasets(java.sql.Connection            databaseConnection,
                                   PostgresOpenLineageEventDigest digest) throws Exception
    {
        if (digest.getDatasets().isEmpty())
        {
            return;
        }

        try (PreparedStatement statement = databaseConnection.prepareStatement(schema.getUpsertRunDatasetSQL()))
        {
            for (PostgresOpenLineageEventDigest.DatasetRow dataset : digest.getDatasets())
            {
                statement.setString(1, digest.getRunId());
                statement.setString(2, dataset.direction());
                statement.setString(3, dataset.namespace());
                statement.setString(4, dataset.name());
                statement.setTimestamp(5, toTimestamp(digest.getEventTime()));
                statement.setString(6, digest.getEventType());
                setLong(statement, 7, dataset.rowCount());
                setLong(statement, 8, dataset.size());
                setLong(statement, 9, dataset.fileCount());
                statement.setString(10, dataset.lifecycleStateChange());
                statement.setString(11, dataset.subsetType());
                statement.setString(12, dataset.datasetVersion());

                statement.executeUpdate();
            }
        }
    }


    /**
     * Record the data quality assertions.
     *
     * @param databaseConnection connection in the event's transaction
     * @param digest digest of the event
     * @throws Exception problem with the database
     */
    private void insertAssertions(java.sql.Connection            databaseConnection,
                                  PostgresOpenLineageEventDigest digest) throws Exception
    {
        if (digest.getAssertions().isEmpty())
        {
            return;
        }

        try (PreparedStatement statement = databaseConnection.prepareStatement(schema.getInsertAssertionSQL()))
        {
            for (PostgresOpenLineageEventDigest.AssertionRow assertion : digest.getAssertions())
            {
                statement.setTimestamp(1, toTimestamp(digest.getEventTime()));
                statement.setString(2, digest.getRunId());
                statement.setString(3, digest.getJobNamespace());
                statement.setString(4, digest.getJobName());
                statement.setString(5, assertion.datasetNamespace());
                statement.setString(6, assertion.datasetName());
                statement.setString(7, assertion.assertion());
                statement.setString(8, assertion.column());
                statement.setBoolean(9, assertion.success());
                statement.setString(10, assertion.severity());
                statement.setString(11, assertion.name());
                statement.addBatch();
            }

            statement.executeBatch();
        }
    }


    /**
     * Record the tests.
     *
     * @param databaseConnection connection in the event's transaction
     * @param digest digest of the event
     * @throws Exception problem with the database
     */
    private void insertTests(java.sql.Connection            databaseConnection,
                             PostgresOpenLineageEventDigest digest) throws Exception
    {
        if (digest.getTests().isEmpty())
        {
            return;
        }

        try (PreparedStatement statement = databaseConnection.prepareStatement(schema.getInsertTestSQL()))
        {
            for (PostgresOpenLineageEventDigest.TestRow test : digest.getTests())
            {
                statement.setTimestamp(1, toTimestamp(digest.getEventTime()));
                statement.setString(2, digest.getRunId());
                statement.setString(3, digest.getJobNamespace());
                statement.setString(4, digest.getJobName());
                statement.setString(5, test.name());
                statement.setString(6, test.status());
                statement.setString(7, test.type());
                statement.setString(8, test.severity());
                statement.addBatch();
            }

            statement.executeBatch();
        }
    }


    /**
     * Delete the records older than a cut-off time in one transaction.
     *
     * @param cutOff records before this time are deleted
     * @return number of events, runs, assertions and tests deleted (in that order)
     * @throws SQLException problem with the database
     */
    public List<Integer> purge(Date cutOff) throws SQLException
    {
        try (java.sql.Connection databaseConnection = dataSource.getConnection())
        {
            List<Integer> deleted = new ArrayList<>();

            for (String purge : schema.getPurgeSQL())
            {
                try (PreparedStatement statement = databaseConnection.prepareStatement(purge))
                {
                    statement.setTimestamp(1, toTimestamp(cutOff));
                    deleted.add(statement.executeUpdate());
                }
            }

            databaseConnection.commit();

            return deleted;
        }
    }


    /**
     * Return the hash that, with the run, event time and event type, identifies an event.
     *
     * @param jsonEvent event as JSON
     * @return 32 hex characters
     * @throws Exception MD5 is not available
     */
    static String getEventHash(String jsonEvent) throws Exception
    {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        return HexFormat.of().formatHex(messageDigest.digest(jsonEvent.getBytes(StandardCharsets.UTF_8)));
    }


    /**
     * Convert a date to a timestamp.
     *
     * @param date date or null
     * @return timestamp or null
     */
    private static Timestamp toTimestamp(Date date)
    {
        return (date == null) ? null : new Timestamp(date.getTime());
    }


    /**
     * Set a nullable long parameter.
     *
     * @param statement statement
     * @param index parameter index
     * @param value value or null
     * @throws Exception problem with the statement
     */
    private static void setLong(PreparedStatement statement,
                                int               index,
                                Long              value) throws Exception
    {
        if (value == null)
        {
            statement.setNull(index, Types.BIGINT);
        }
        else
        {
            statement.setLong(index, value);
        }
    }
}
