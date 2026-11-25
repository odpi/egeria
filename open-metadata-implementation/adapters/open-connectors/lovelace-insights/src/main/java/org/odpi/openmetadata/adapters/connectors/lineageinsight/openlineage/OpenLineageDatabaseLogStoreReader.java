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
import java.util.EnumSet;
import java.util.Set;


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
 * <p>
 * A log store can hold millions of these rows, for a few hundred jobs and datasets.  The caller says which parts of
 * the history it needs, and only the tables that hold those parts are read; the rows of a job or dataset share one
 * key.  When the runs or the dataset activity are not read, the history holds only the jobs and datasets that the
 * other parts mention, and the number of jobs and datasets in the window is counted by the database instead.
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
        return read(dataSource, schemaName, windowStart, windowEnd, EnumSet.allOf(OpenLineageRunHistory.Content.class));
    }


    /**
     * Build the parts of the history for a window that an analysis needs.
     *
     * @param dataSource source of database connections
     * @param schemaName name of the database schema holding the log store
     * @param windowStart start of the window (null for no lower bound)
     * @param windowEnd end of the window (null for no upper bound)
     * @param content parts of the history to keep
     * @return history
     * @throws SQLException problem reading the database
     */
    public OpenLineageRunHistory read(DataSource                         dataSource,
                                      String                             schemaName,
                                      Date                               windowStart,
                                      Date                               windowEnd,
                                      Set<OpenLineageRunHistory.Content> content) throws SQLException
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
            boolean keepRuns     = content.contains(OpenLineageRunHistory.Content.RUNS);
            boolean keepActivity = content.contains(OpenLineageRunHistory.Content.DATASET_ACTIVITY);

            if (keepRuns)
            {
                readRuns(connection, schema, start, end, history);
            }

            if ((keepRuns) || (keepActivity))
            {
                readRunDatasets(connection, schema, start, end, history, keepRuns, keepActivity);
            }

            if (content.contains(OpenLineageRunHistory.Content.ASSERTIONS))
            {
                readAssertions(connection, schema, start, end, history);
            }

            if (content.contains(OpenLineageRunHistory.Content.TESTS))
            {
                readTests(connection, schema, start, end, history);
            }

            /*
             * Without the runs, the history holds only the jobs that the other parts mention.  They still need
             * their governance action facet, and the number of jobs in the window comes from a count.  The same
             * goes for the number of datasets when no run's datasets were read.
             */
            if (! keepRuns)
            {
                readJobGovernanceActions(connection, schema, start, end, history);

                history.recordJobCount(readCount(connection, schema.getJobCountInWindowSQL(), start, end));

                if (! keepActivity)
                {
                    history.recordDataSetCount(readCount(connection, schema.getDatasetCountInWindowSQL(), start, end));
                }
            }

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
                OpenLineageRunHistory.Key        jobKey     = key(history, resultSet, "job_namespace", "job_name");
                OpenLineageRunHistory.JobHistory jobHistory = history.getJob(jobKey);
                String                           runId      = resultSet.getString("run_id");
                OpenLineageRunHistory.RunRecord  run        = new OpenLineageRunHistory.RunRecord(runId, jobKey);

                run.status         = history.shared(resultSet.getString("status"));
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
     * @param keepVolumes whether to record the volumes on each run (the runs must have been read)
     * @param keepActivity whether to keep each read and write of a dataset
     * @throws SQLException problem reading the database
     */
    private void readRunDatasets(Connection                        connection,
                                 PostgresOpenLineageLogStoreSchema schema,
                                 Timestamp                         start,
                                 Timestamp                         end,
                                 OpenLineageRunHistory             history,
                                 boolean                           keepVolumes,
                                 boolean                           keepActivity) throws SQLException
    {
        try (PreparedStatement statement = prepare(connection, schema.getRunDatasetsInWindowSQL(), start, end);
             ResultSet         resultSet = statement.executeQuery())
        {
            while (resultSet.next())
            {
                OpenLineageRunHistory.Key            jobKey         = key(history, resultSet, "job_namespace", "job_name");
                OpenLineageRunHistory.Key            datasetKey     = key(history, resultSet, "dataset_namespace", "dataset_name");
                OpenLineageRunHistory.DataSetHistory datasetHistory = history.getDataSet(datasetKey);
                OpenLineageRunHistory.JobHistory     jobHistory     = history.getJob(jobKey);

                String                          storedRunId = resultSet.getString("run_id");
                OpenLineageRunHistory.RunRecord run         = keepVolumes ? jobHistory.runs.get(storedRunId) : null;
                String                          runId       = (run != null) ? run.runId : storedRunId;
                Long                            rowCount    = getLong(resultSet, "row_count");
                Long                            size        = getLong(resultSet, "size_bytes");
                boolean                         input       = PostgresOpenLineageLogStoreSchema.INPUT_DIRECTION.equals(resultSet.getString("direction"));

                if (run != null)
                {
                    long[] volume = new long[]{(rowCount == null) ? 0 : rowCount, (size == null) ? 0 : size};

                    if (input)
                    {
                        run.inputVolumes.put(datasetKey, volume);
                    }
                    else
                    {
                        run.outputVolumes.put(datasetKey, volume);
                    }
                }

                if (keepActivity)
                {
                    Date eventTime = toDate(resultSet.getTimestamp("event_time"));

                    if (input)
                    {
                        datasetHistory.reads.add(new OpenLineageRunHistory.ReadRecord(eventTime, runId, jobKey, rowCount, size));
                    }
                    else
                    {
                        datasetHistory.writes.add(new OpenLineageRunHistory.WriteRecord(eventTime,
                                                                                        runId,
                                                                                        jobKey,
                                                                                        history.shared(resultSet.getString("event_type")),
                                                                                        rowCount,
                                                                                        size,
                                                                                        getLong(resultSet, "file_count"),
                                                                                        history.shared(resultSet.getString("lifecycle_state_change")),
                                                                                        history.shared(resultSet.getString("subset_type")),
                                                                                        resultSet.getString("dataset_version")));
                    }
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
                history.getDataSet(key(history, resultSet, "dataset_namespace", "dataset_name")).assertions.add(
                        new OpenLineageRunHistory.AssertionRecord(toDate(resultSet.getTimestamp("event_time")),
                                                                  resultSet.getString("run_id"),
                                                                  key(history, resultSet, "job_namespace", "job_name"),
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
                history.getJob(key(history, resultSet, "job_namespace", "job_name")).tests.add(
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
     * Give each job already in the history the governance action facet of its latest run that has one.  It is used
     * when the runs are not read, so that the jobs of a governance action process are still recognised.
     *
     * @param connection database connection
     * @param schema log store definitions
     * @param start window start
     * @param end window end
     * @param history history to update
     * @throws SQLException problem reading the database
     */
    private void readJobGovernanceActions(Connection                        connection,
                                          PostgresOpenLineageLogStoreSchema schema,
                                          Timestamp                         start,
                                          Timestamp                         end,
                                          OpenLineageRunHistory             history) throws SQLException
    {
        if (history.getJobs().isEmpty())
        {
            return;
        }

        try (PreparedStatement statement = prepare(connection, schema.getJobGovernanceActionsInWindowSQL(), start, end);
             ResultSet         resultSet = statement.executeQuery())
        {
            while (resultSet.next())
            {
                OpenLineageRunHistory.JobHistory jobHistory = history.getJobs().get(key(history, resultSet, "job_namespace", "job_name"));

                if (jobHistory != null)
                {
                    jobHistory.governanceAction = toGovernanceAction(resultSet.getString("governance_action"));
                }
            }
        }
    }


    /**
     * Run a window query that returns a single count.
     *
     * @param connection database connection
     * @param sql query with window start and end parameters
     * @param start window start
     * @param end window end
     * @return count
     * @throws SQLException problem reading the database
     */
    private int readCount(Connection connection,
                          String     sql,
                          Timestamp  start,
                          Timestamp  end) throws SQLException
    {
        try (PreparedStatement statement = prepare(connection, sql, start, end);
             ResultSet         resultSet = statement.executeQuery())
        {
            return resultSet.next() ? resultSet.getInt(1) : 0;
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
     * Return the history's key for a namespace and name column.
     *
     * @param history history that holds the keys
     * @param resultSet current row
     * @param namespaceColumn namespace column
     * @param nameColumn name column
     * @return key shared by every record of the job or dataset
     * @throws SQLException problem reading the row
     */
    private static OpenLineageRunHistory.Key key(OpenLineageRunHistory history,
                                                 ResultSet             resultSet,
                                                 String                namespaceColumn,
                                                 String                nameColumn) throws SQLException
    {
        return history.key(PostgresOpenLineageLogStoreSchema.fromStoredNamespace(resultSet.getString(namespaceColumn)),
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
