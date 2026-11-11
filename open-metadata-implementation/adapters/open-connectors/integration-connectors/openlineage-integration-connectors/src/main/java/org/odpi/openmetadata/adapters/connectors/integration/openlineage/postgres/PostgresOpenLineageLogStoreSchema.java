/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage.postgres;

import java.util.List;


/**
 * PostgresOpenLineageLogStoreSchema defines the tables of a PostgreSQL OpenLineage log store and the SQL that
 * writes, purges and reads them.  It is shared by the connector that writes the log store and the services
 * (such as the Lovelace OpenLineage analysis services) that read it, so the table definitions live in one place.
 * <p>
 * The log store keeps each event twice:
 * </p>
 * <ul>
 *     <li><b>ol_event</b> - the event exactly as it was received, as JSONB, for analysis that is not anticipated
 *     here.  The (run_id, event_time, event_type, event_hash) key means an event that is delivered twice (for example,
 *     when a Kafka topic is replayed) is stored once - and only the first delivery updates the digest.</li>
 *     <li>a digest, maintained as each event arrives, that holds the shape the analysis services need:
 *         <ul>
 *             <li><b>ol_run</b> - one row per run: its job, status, start and end time, and the egeria governance
 *             action facet.</li>
 *             <li><b>ol_run_dataset</b> - one row per run per input (I) or output (O) dataset with the statistics
 *             reported for it.</li>
 *             <li><b>ol_assertion</b> - each data quality assertion reported on an input dataset.</li>
 *             <li><b>ol_test</b> - each test reported in a run's test facet.</li>
 *         </ul>
 *     </li>
 * </ul>
 * <p>
 * Reading a time window from the digest is a few indexed range scans that return one row per run, rather than
 * a parse of every event, so the cost of an analysis follows the number of runs, not the number of events.
 * </p>
 */
public class PostgresOpenLineageLogStoreSchema
{
    /**
     * Raw event table.
     */
    public static final String EVENT_TABLE         = "ol_event";

    /**
     * One row per run.
     */
    public static final String RUN_TABLE           = "ol_run";

    /**
     * One row per run per input or output dataset.
     */
    public static final String RUN_DATASET_TABLE   = "ol_run_dataset";

    /**
     * Data quality assertions.
     */
    public static final String ASSERTION_TABLE     = "ol_assertion";

    /**
     * Tests from the test run facet.
     */
    public static final String TEST_TABLE          = "ol_test";

    /**
     * Value of the direction column for an input dataset.
     */
    public static final String INPUT_DIRECTION     = "I";

    /**
     * Value of the direction column for an output dataset.
     */
    public static final String OUTPUT_DIRECTION    = "O";

    private final String schemaName;
    private final String quotedSchemaName;


    /**
     * Create the definitions for a schema.
     *
     * @param schemaName name of the database schema
     * @throws IllegalArgumentException the schema name is missing or can not be used as a quoted identifier
     */
    public PostgresOpenLineageLogStoreSchema(String schemaName)
    {
        if (! isValidSchemaName(schemaName))
        {
            throw new IllegalArgumentException("Invalid database schema name: " + schemaName);
        }

        this.schemaName       = schemaName;
        this.quotedSchemaName = "\"" + schemaName + "\"";
    }


    /**
     * Check that a schema name can be used.  The name is always quoted in the SQL, so any characters are allowed
     * other than a double quote (which would end the identifier) and the null character.  PostgreSQL truncates
     * identifiers longer than 63 bytes, which would quietly use a different schema, so those are rejected too.
     *
     * @param schemaName proposed name
     * @return true if the name can be used
     */
    public static boolean isValidSchemaName(String schemaName)
    {
        return (schemaName != null) &&
                (! schemaName.isBlank()) &&
                (schemaName.indexOf('"') < 0) &&
                (schemaName.indexOf('\0') < 0) &&
                (schemaName.getBytes(java.nio.charset.StandardCharsets.UTF_8).length <= 63);
    }


    /**
     * Return the schema name.
     *
     * @return name
     */
    public String getSchemaName()
    {
        return schemaName;
    }


    /**
     * Return the qualified, quoted name of a table in this schema.
     *
     * @param tableName unqualified table name
     * @return qualified name
     */
    public String table(String tableName)
    {
        return quotedSchemaName + "." + tableName;
    }


    /**
     * Return the statements that create the schema, its tables and their indexes.  Each one does nothing if the
     * object already exists, so they are run each time the log store starts.
     *
     * @return list of DDL statements
     */
    public List<String> getDDLStatements()
    {
        return List.of("CREATE SCHEMA IF NOT EXISTS " + quotedSchemaName,

                       "CREATE TABLE IF NOT EXISTS " + table(EVENT_TABLE) + " (" +
                               "event_id bigserial PRIMARY KEY, " +
                               "event_time timestamptz NOT NULL, " +
                               "event_type varchar(32) NOT NULL, " +
                               "run_id varchar(64) NOT NULL, " +
                               "job_namespace text NOT NULL, " +
                               "job_name text NOT NULL, " +
                               "producer text, " +
                               "event_hash char(32) NOT NULL, " +
                               "stored_time timestamptz NOT NULL DEFAULT now(), " +
                               "event jsonb NOT NULL)",
                       "CREATE UNIQUE INDEX IF NOT EXISTS ol_event_identity ON " + table(EVENT_TABLE) + " (run_id, event_time, event_type, event_hash)",
                       "CREATE INDEX IF NOT EXISTS ol_event_time ON " + table(EVENT_TABLE) + " (event_time)",
                       "CREATE INDEX IF NOT EXISTS ol_event_job ON " + table(EVENT_TABLE) + " (job_namespace, job_name, event_time)",

                       "CREATE TABLE IF NOT EXISTS " + table(RUN_TABLE) + " (" +
                               "run_id varchar(64) PRIMARY KEY, " +
                               "job_namespace text NOT NULL, " +
                               "job_name text NOT NULL, " +
                               "status varchar(32), " +
                               "start_time timestamptz, " +
                               "end_time timestamptz, " +
                               "first_event_time timestamptz NOT NULL, " +
                               "last_event_time timestamptz NOT NULL, " +
                               "governance_action jsonb)",
                       "CREATE INDEX IF NOT EXISTS ol_run_last_event ON " + table(RUN_TABLE) + " (last_event_time)",
                       "CREATE INDEX IF NOT EXISTS ol_run_job ON " + table(RUN_TABLE) + " (job_namespace, job_name, first_event_time)",

                       "CREATE TABLE IF NOT EXISTS " + table(RUN_DATASET_TABLE) + " (" +
                               "run_id varchar(64) NOT NULL REFERENCES " + table(RUN_TABLE) + " (run_id) ON DELETE CASCADE, " +
                               "direction char(1) NOT NULL, " +
                               "dataset_namespace text NOT NULL, " +
                               "dataset_name text NOT NULL, " +
                               "event_time timestamptz NOT NULL, " +
                               "event_type varchar(32) NOT NULL, " +
                               "row_count bigint, " +
                               "size_bytes bigint, " +
                               "file_count bigint, " +
                               "lifecycle_state_change varchar(32), " +
                               "subset_type text, " +
                               "dataset_version text, " +
                               "PRIMARY KEY (run_id, direction, dataset_namespace, dataset_name))",
                       "CREATE INDEX IF NOT EXISTS ol_run_dataset_dataset ON " + table(RUN_DATASET_TABLE) + " (dataset_namespace, dataset_name, event_time)",

                       "CREATE TABLE IF NOT EXISTS " + table(ASSERTION_TABLE) + " (" +
                               "event_time timestamptz NOT NULL, " +
                               "run_id varchar(64) NOT NULL, " +
                               "job_namespace text NOT NULL, " +
                               "job_name text NOT NULL, " +
                               "dataset_namespace text NOT NULL, " +
                               "dataset_name text NOT NULL, " +
                               "assertion text, " +
                               "column_name text, " +
                               "success boolean NOT NULL, " +
                               "severity text, " +
                               "assertion_name text)",
                       "CREATE INDEX IF NOT EXISTS ol_assertion_time ON " + table(ASSERTION_TABLE) + " (event_time)",
                       "CREATE INDEX IF NOT EXISTS ol_assertion_dataset ON " + table(ASSERTION_TABLE) + " (dataset_namespace, dataset_name, event_time)",

                       "CREATE TABLE IF NOT EXISTS " + table(TEST_TABLE) + " (" +
                               "event_time timestamptz NOT NULL, " +
                               "run_id varchar(64) NOT NULL, " +
                               "job_namespace text NOT NULL, " +
                               "job_name text NOT NULL, " +
                               "test_name text, " +
                               "status text, " +
                               "test_type text, " +
                               "severity text)",
                       "CREATE INDEX IF NOT EXISTS ol_test_time ON " + table(TEST_TABLE) + " (event_time)");
    }


    /*
     * Writing.
     */

    /**
     * Insert a raw event.  Returns no row if the event is already stored.
     * Parameters: event_time, event_type, run_id, job_namespace, job_name, producer, event_hash, event.
     *
     * @return SQL
     */
    public String getInsertEventSQL()
    {
        return "INSERT INTO " + table(EVENT_TABLE) + " (event_time, event_type, run_id, job_namespace, job_name, producer, event_hash, event) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?::jsonb) " +
                "ON CONFLICT (run_id, event_time, event_type, event_hash) DO NOTHING";
    }


    /**
     * Add an event to its run.  The rules match those of the original file-based reader: the earliest START sets the
     * start time; the latest COMPLETE, FAIL or ABORT sets the end time and the final status; RUNNING replaces only
     * a START status; and the egeria governance action facet is kept from the last event that carried it.
     * PostgreSQL's LEAST and GREATEST ignore nulls, so an event that does not set a time leaves it unchanged.
     * Parameters: run_id, job_namespace, job_name, status, start_time, end_time, first_event_time, last_event_time,
     * governance_action.
     *
     * @return SQL
     */
    public String getUpsertRunSQL()
    {
        return "INSERT INTO " + table(RUN_TABLE) + " AS r (run_id, job_namespace, job_name, status, start_time, end_time, first_event_time, last_event_time, governance_action) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::jsonb) " +
                "ON CONFLICT (run_id) DO UPDATE SET " +
                "status = CASE " +
                    "WHEN EXCLUDED.end_time IS NOT NULL AND (r.end_time IS NULL OR EXCLUDED.end_time > r.end_time) THEN EXCLUDED.status " +
                    "WHEN EXCLUDED.status = 'START' THEN COALESCE(r.status, EXCLUDED.status) " +
                    "WHEN EXCLUDED.status = 'RUNNING' AND (r.status IS NULL OR r.status = 'START') THEN EXCLUDED.status " +
                    "ELSE r.status END, " +
                "start_time = LEAST(r.start_time, EXCLUDED.start_time), " +
                "end_time = GREATEST(r.end_time, EXCLUDED.end_time), " +
                "first_event_time = LEAST(r.first_event_time, EXCLUDED.first_event_time), " +
                "last_event_time = GREATEST(r.last_event_time, EXCLUDED.last_event_time), " +
                "governance_action = COALESCE(EXCLUDED.governance_action, r.governance_action)";
    }


    /**
     * Record a dataset read or written by a run.  The first sighting registers it (so a run that has only started
     * is counted).  A later sighting is used if it carries statistics or is not a START event: if it is the
     * newest event for the dataset, by event time, it sets the record and any statistics it does not repeat are
     * kept; if it is older (because events arrived out of order) it only fills in the values that are missing.
     * So the result does not depend on the order in which the events arrive.
     * Parameters: run_id, direction, dataset_namespace, dataset_name, event_time, event_type, row_count,
     * size_bytes, file_count, lifecycle_state_change, subset_type, dataset_version.
     *
     * @return SQL
     */
    public String getUpsertRunDatasetSQL()
    {
        final String newer = "(EXCLUDED.event_time >= d.event_time)";

        return "INSERT INTO " + table(RUN_DATASET_TABLE) + " AS d (run_id, direction, dataset_namespace, dataset_name, event_time, event_type, " +
                "row_count, size_bytes, file_count, lifecycle_state_change, subset_type, dataset_version) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (run_id, direction, dataset_namespace, dataset_name) DO UPDATE SET " +
                "event_time = CASE WHEN " + newer + " THEN EXCLUDED.event_time ELSE d.event_time END, " +
                "event_type = CASE WHEN " + newer + " THEN EXCLUDED.event_type ELSE d.event_type END, " +
                preferNewer("row_count", newer) + ", " +
                preferNewer("size_bytes", newer) + ", " +
                preferNewer("file_count", newer) + ", " +
                preferNewer("lifecycle_state_change", newer) + ", " +
                preferNewer("subset_type", newer) + ", " +
                preferNewer("dataset_version", newer) + " " +
                "WHERE EXCLUDED.row_count IS NOT NULL OR EXCLUDED.size_bytes IS NOT NULL OR EXCLUDED.event_type <> 'START'";
    }


    /**
     * Return the assignment for a column of ol_run_dataset that takes the value from the newer event when it has
     * one, and from the older event otherwise.
     *
     * @param column column name
     * @param newer condition that is true when the incoming event is the newer
     * @return SQL fragment
     */
    private static String preferNewer(String column,
                                      String newer)
    {
        return column + " = CASE WHEN " + newer + " THEN COALESCE(EXCLUDED." + column + ", d." + column + ") " +
                "ELSE COALESCE(d." + column + ", EXCLUDED." + column + ") END";
    }


    /**
     * Insert an assertion.
     * Parameters: event_time, run_id, job_namespace, job_name, dataset_namespace, dataset_name, assertion,
     * column_name, success, severity, assertion_name.
     *
     * @return SQL
     */
    public String getInsertAssertionSQL()
    {
        return "INSERT INTO " + table(ASSERTION_TABLE) + " (event_time, run_id, job_namespace, job_name, dataset_namespace, dataset_name, " +
                "assertion, column_name, success, severity, assertion_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }


    /**
     * Insert a test.
     * Parameters: event_time, run_id, job_namespace, job_name, test_name, status, test_type, severity.
     *
     * @return SQL
     */
    public String getInsertTestSQL()
    {
        return "INSERT INTO " + table(TEST_TABLE) + " (event_time, run_id, job_namespace, job_name, test_name, status, test_type, severity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }


    /*
     * Purging.
     */

    /**
     * Return the statements that delete the records older than a cut-off time.  Each takes the cut-off as its
     * only parameter.  A run is deleted once its last event is older than the cut-off, and its datasets go with it.
     *
     * @return list of SQL statements
     */
    public List<String> getPurgeSQL()
    {
        return List.of("DELETE FROM " + table(EVENT_TABLE) + " WHERE event_time < ?",
                       "DELETE FROM " + table(RUN_TABLE) + " WHERE last_event_time < ?",
                       "DELETE FROM " + table(ASSERTION_TABLE) + " WHERE event_time < ?",
                       "DELETE FROM " + table(TEST_TABLE) + " WHERE event_time < ?");
    }


    /*
     * Reading a time window.  Each statement takes the window start and the window end as its parameters.
     */

    /**
     * The runs that had an event in the window, oldest first.
     *
     * @return SQL
     */
    public String getRunsInWindowSQL()
    {
        return "SELECT run_id, job_namespace, job_name, status, start_time, end_time, first_event_time, last_event_time, governance_action " +
                "FROM " + table(RUN_TABLE) + " WHERE last_event_time >= ? AND first_event_time <= ? ORDER BY first_event_time";
    }


    /**
     * The datasets read or written by the runs that had an event in the window, oldest first.
     *
     * @return SQL
     */
    public String getRunDatasetsInWindowSQL()
    {
        return "SELECT d.run_id, d.direction, d.dataset_namespace, d.dataset_name, d.event_time, d.event_type, d.row_count, d.size_bytes, " +
                "d.file_count, d.lifecycle_state_change, d.subset_type, d.dataset_version, r.job_namespace, r.job_name " +
                "FROM " + table(RUN_DATASET_TABLE) + " d JOIN " + table(RUN_TABLE) + " r ON r.run_id = d.run_id " +
                "WHERE r.last_event_time >= ? AND r.first_event_time <= ? ORDER BY d.event_time";
    }


    /**
     * The assertions reported in the window, oldest first.
     *
     * @return SQL
     */
    public String getAssertionsInWindowSQL()
    {
        return "SELECT event_time, run_id, job_namespace, job_name, dataset_namespace, dataset_name, assertion, column_name, success, severity, assertion_name " +
                "FROM " + table(ASSERTION_TABLE) + " WHERE event_time >= ? AND event_time <= ? ORDER BY event_time";
    }


    /**
     * The tests reported in the window, oldest first.
     *
     * @return SQL
     */
    public String getTestsInWindowSQL()
    {
        return "SELECT event_time, run_id, job_namespace, job_name, test_name, status, test_type, severity " +
                "FROM " + table(TEST_TABLE) + " WHERE event_time >= ? AND event_time <= ? ORDER BY event_time";
    }


    /**
     * The number of events in the window, and the times of the first and last.
     *
     * @return SQL
     */
    public String getEventSummaryInWindowSQL()
    {
        return "SELECT count(*), min(event_time), max(event_time) FROM " + table(EVENT_TABLE) + " WHERE event_time >= ? AND event_time <= ?";
    }


    /**
     * Namespaces are optional in an event but part of the key in the digest tables, which can not hold null.
     * A missing namespace is stored as an empty string.
     *
     * @param namespace namespace from the event
     * @return value to store
     */
    public static String toStoredNamespace(String namespace)
    {
        return (namespace == null) ? "" : namespace;
    }


    /**
     * Turn a stored namespace back into the value from the event.
     *
     * @param storedNamespace value from the database
     * @return namespace, or null if the event had none
     */
    public static String fromStoredNamespace(String storedNamespace)
    {
        return ((storedNamespace == null) || (storedNamespace.isEmpty())) ? null : storedNamespace;
    }
}
