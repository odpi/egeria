/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.lineageinsight.openlineage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.postgres.PostgresOpenLineageLogStoreSchema;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageEgeriaGovernanceActionRunFacet;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;


/**
 * OpenLineageDatabaseLogStoreReader builds the OpenLineage run history for an analysis window from the digest
 * tables of a PostgreSQL OpenLineage log store (written by the PostgreSQL OpenLineage log store integration
 * connector).  Rather than parsing every event, it reads one row per run, one per dataset each run read or wrote,
 * and the assertions and tests reported in the window - each an indexed range scan.
 * <p>
 * The history differs from the one built by OpenLineageLogStoreReader in one respect: a run that had an event in
 * the window is read whole, so a run that started before the window and finished inside it has its start time,
 * and so its duration.
 * </p>
 */
public class OpenLineageDatabaseLogStoreReader
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Timestamp    EARLIEST      = new Timestamp(0L);
    private static final Timestamp    LATEST        = Timestamp.valueOf("9999-12-31 23:59:59");


    /**
     * Build the history for a window.
     *
     * @param dataSource source of database connections
     * @param schemaName name of the database schema holding the log store
     * @param windowStart start of the window (null for no lower bound)
     * @param windowEnd end of the window (null for no upper bound)
     * @return history
     * @throws SQLException problem reading the database
     */
    public OpenLineageRunHistory read(DataSource dataSource,
                                      String     schemaName,
                                      Date       windowStart,
                                      Date       windowEnd) throws SQLException
    {
        PostgresOpenLineageLogStoreSchema schema  = new PostgresOpenLineageLogStoreSchema(schemaName);
        OpenLineageRunHistory             history = new OpenLineageRunHistory();
        Timestamp                         start   = (windowStart == null) ? EARLIEST : new Timestamp(windowStart.getTime());
        Timestamp                         end     = (windowEnd == null) ? LATEST : new Timestamp(windowEnd.getTime());

        /*
         * Only reads are issued.  The connection runs with auto-commit disabled, so the read-only transaction is
         * rolled back when the connection is returned to the pool.
         */
        try (Connection connection = dataSource.getConnection())
        {
            connection.setReadOnly(true);

            readEventSummary(connection, schema, start, end, history);
            readRuns(connection, schema, start, end, history);
            readRunDatasets(connection, schema, start, end, history);
            readAssertions(connection, schema, start, end, history);
            readTests(connection, schema, start, end, history);

            connection.rollback();
        }

        return history;
    }


    /**
     * Count the events in the window.
     *
     * @param connection database connection
     * @param schema log store definitions
     * @param start window start
     * @param end window end
     * @param history history to update
     * @throws SQLException problem reading the database
     */
    private void readEventSummary(Connection                        connection,
                                  PostgresOpenLineageLogStoreSchema schema,
                                  Timestamp                         start,
                                  Timestamp                         end,
                                  OpenLineageRunHistory             history) throws SQLException
    {
        try (PreparedStatement statement = prepare(connection, schema.getEventSummaryInWindowSQL(), start, end);
             ResultSet         resultSet = statement.executeQuery())
        {
            if (resultSet.next())
            {
                history.recordEventSummary(resultSet.getInt(1), toDate(resultSet.getTimestamp(2)), toDate(resultSet.getTimestamp(3)));
            }
        }
    }


    /**
     * Read the runs with an event in the window.
     *
     * @param connection database connection
     * @param schema log store definitions
     * @param start window start
     * @param end window end
     * @param history history to update
     * @throws SQLException problem reading the database
     */
    private void readRuns(Connection                        connection,
                          PostgresOpenLineageLogStoreSchema schema,
                          Timestamp                         start,
                          Timestamp                         end,
                          OpenLineageRunHistory             history) throws SQLException
    {
        try (PreparedStatement statement = prepare(connection, schema.getRunsInWindowSQL(), start, end);
             ResultSet         resultSet = statement.executeQuery())
        {
            while (resultSet.next())
            {
                OpenLineageRunHistory.Key        jobKey     = jobKey(resultSet, "job_namespace", "job_name");
                OpenLineageRunHistory.JobHistory jobHistory = history.getJob(jobKey);
                String                           runId      = resultSet.getString("run_id");
                OpenLineageRunHistory.RunRecord  run        = new OpenLineageRunHistory.RunRecord(runId, jobKey);

                run.status         = resultSet.getString("status");
                run.startTime      = toDate(resultSet.getTimestamp("start_time"));
                run.endTime        = toDate(resultSet.getTimestamp("end_time"));
                run.firstEventTime = toDate(resultSet.getTimestamp("first_event_time"));
                run.lastEventTime  = toDate(resultSet.getTimestamp("last_event_time"));

                jobHistory.runs.put(runId, run);

                /*
                 * The runs are in time order, so the job keeps the facet of its latest run that has one.
                 */
                OpenLineageEgeriaGovernanceActionRunFacet governanceAction = toGovernanceAction(resultSet.getString("governance_action"));

                if (governanceAction != null)
                {
                    jobHistory.governanceAction = governanceAction;
                }
            }
        }
    }


    /**
     * Read the datasets read and written by the runs with an event in the window.
     *
     * @param connection database connection
     * @param schema log store definitions
     * @param start window start
     * @param end window end
     * @param history history to update
     * @throws SQLException problem reading the database
     */
    private void readRunDatasets(Connection                        connection,
                                 PostgresOpenLineageLogStoreSchema schema,
                                 Timestamp                         start,
                                 Timestamp                         end,
                                 OpenLineageRunHistory             history) throws SQLException
    {
        try (PreparedStatement statement = prepare(connection, schema.getRunDatasetsInWindowSQL(), start, end);
             ResultSet         resultSet = statement.executeQuery())
        {
            while (resultSet.next())
            {
                OpenLineageRunHistory.Key            jobKey         = jobKey(resultSet, "job_namespace", "job_name");
                OpenLineageRunHistory.Key            datasetKey     = jobKey(resultSet, "dataset_namespace", "dataset_name");
                OpenLineageRunHistory.DataSetHistory datasetHistory = history.getDataSet(datasetKey);
                OpenLineageRunHistory.RunRecord      run            = history.getJob(jobKey).runs.get(resultSet.getString("run_id"));
                String                               runId          = resultSet.getString("run_id");
                Date                                 eventTime      = toDate(resultSet.getTimestamp("event_time"));
                Long                                 rowCount       = getLong(resultSet, "row_count");
                Long                                 size           = getLong(resultSet, "size_bytes");
                long[]                               volume         = new long[]{(rowCount == null) ? 0 : rowCount, (size == null) ? 0 : size};

                if (PostgresOpenLineageLogStoreSchema.INPUT_DIRECTION.equals(resultSet.getString("direction")))
                {
                    if (run != null)
                    {
                        run.inputVolumes.put(datasetKey, volume);
                    }

                    datasetHistory.reads.add(new OpenLineageRunHistory.ReadRecord(eventTime, runId, jobKey, rowCount, size));
                }
                else
                {
                    if (run != null)
                    {
                        run.outputVolumes.put(datasetKey, volume);
                    }

                    datasetHistory.writes.add(new OpenLineageRunHistory.WriteRecord(eventTime,
                                                                                    runId,
                                                                                    jobKey,
                                                                                    resultSet.getString("event_type"),
                                                                                    rowCount,
                                                                                    size,
                                                                                    getLong(resultSet, "file_count"),
                                                                                    resultSet.getString("lifecycle_state_change"),
                                                                                    resultSet.getString("subset_type"),
                                                                                    resultSet.getString("dataset_version")));
                }
            }
        }
    }


    /**
     * Read the assertions reported in the window.
     *
     * @param connection database connection
     * @param schema log store definitions
     * @param start window start
     * @param end window end
     * @param history history to update
     * @throws SQLException problem reading the database
     */
    private void readAssertions(Connection                        connection,
                                PostgresOpenLineageLogStoreSchema schema,
                                Timestamp                         start,
                                Timestamp                         end,
                                OpenLineageRunHistory             history) throws SQLException
    {
        try (PreparedStatement statement = prepare(connection, schema.getAssertionsInWindowSQL(), start, end);
             ResultSet         resultSet = statement.executeQuery())
        {
            while (resultSet.next())
            {
                history.getDataSet(jobKey(resultSet, "dataset_namespace", "dataset_name")).assertions.add(
                        new OpenLineageRunHistory.AssertionRecord(toDate(resultSet.getTimestamp("event_time")),
                                                                  resultSet.getString("run_id"),
                                                                  jobKey(resultSet, "job_namespace", "job_name"),
                                                                  resultSet.getString("assertion"),
                                                                  resultSet.getString("column_name"),
                                                                  resultSet.getBoolean("success"),
                                                                  resultSet.getString("severity"),
                                                                  resultSet.getString("assertion_name")));
            }
        }
    }


    /**
     * Read the tests reported in the window.
     *
     * @param connection database connection
     * @param schema log store definitions
     * @param start window start
     * @param end window end
     * @param history history to update
     * @throws SQLException problem reading the database
     */
    private void readTests(Connection                        connection,
                           PostgresOpenLineageLogStoreSchema schema,
                           Timestamp                         start,
                           Timestamp                         end,
                           OpenLineageRunHistory             history) throws SQLException
    {
        try (PreparedStatement statement = prepare(connection, schema.getTestsInWindowSQL(), start, end);
             ResultSet         resultSet = statement.executeQuery())
        {
            while (resultSet.next())
            {
                history.getJob(jobKey(resultSet, "job_namespace", "job_name")).tests.add(
                        new OpenLineageRunHistory.TestRecord(toDate(resultSet.getTimestamp("event_time")),
                                                             resultSet.getString("run_id"),
                                                             resultSet.getString("test_name"),
                                                             resultSet.getString("status"),
                                                             resultSet.getString("test_type"),
                                                             resultSet.getString("severity")));
            }
        }
    }


    /**
     * Prepare a window query.
     *
     * @param connection database connection
     * @param sql query with window start and end parameters
     * @param start window start
     * @param end window end
     * @return statement
     * @throws SQLException problem with the statement
     */
    private PreparedStatement prepare(Connection connection,
                                      String     sql,
                                      Timestamp  start,
                                      Timestamp  end) throws SQLException
    {
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setFetchSize(1000);
        statement.setTimestamp(1, start);
        statement.setTimestamp(2, end);

        return statement;
    }


    /**
     * Build a key from a namespace and name column.
     *
     * @param resultSet current row
     * @param namespaceColumn namespace column
     * @param nameColumn name column
     * @return key
     * @throws SQLException problem reading the row
     */
    private static OpenLineageRunHistory.Key jobKey(ResultSet resultSet,
                                                    String    namespaceColumn,
                                                    String    nameColumn) throws SQLException
    {
        return new OpenLineageRunHistory.Key(PostgresOpenLineageLogStoreSchema.fromStoredNamespace(resultSet.getString(namespaceColumn)),
                                             resultSet.getString(nameColumn));
    }


    /**
     * Read a nullable long column.
     *
     * @param resultSet current row
     * @param column column name
     * @return value or null
     * @throws SQLException problem reading the row
     */
    private static Long getLong(ResultSet resultSet,
                                String    column) throws SQLException
    {
        long value = resultSet.getLong(column);

        return resultSet.wasNull() ? null : value;
    }


    /**
     * Turn the stored facet back into a bean.  A facet that can not be read is ignored.
     *
     * @param json stored facet or null
     * @return facet or null
     */
    static OpenLineageEgeriaGovernanceActionRunFacet toGovernanceAction(String json)
    {
        if (json != null)
        {
            try
            {
                return OBJECT_MAPPER.readValue(json, OpenLineageEgeriaGovernanceActionRunFacet.class);
            }
            catch (Exception unreadable)
            {
                return null;
            }
        }

        return null;
    }


    /**
     * Convert a timestamp to a date.
     *
     * @param timestamp timestamp or null
     * @return date or null
     */
    private static Date toDate(Timestamp timestamp)
    {
        return (timestamp == null) ? null : new Date(timestamp.getTime());
    }
}
