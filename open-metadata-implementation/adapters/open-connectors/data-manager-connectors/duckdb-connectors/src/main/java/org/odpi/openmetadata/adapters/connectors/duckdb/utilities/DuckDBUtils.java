/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.duckdb.utilities;

import org.odpi.openmetadata.adapters.connectors.duckdb.controls.DuckDBConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.duckdb.ffdc.DuckDBAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * DuckDBUtils provides simple functions to work with DuckDB database paths and JDBC connection strings.
 * DuckDB is embedded - there is no server tier and so, unlike the other database connector suites, there is no
 * need to rewrite a server-level URL into a database-level URL.  A DuckDB JDBC URL is simply the "jdbc:duckdb:"
 * prefix followed by either the path to the database file, or the literal value ":memory:" for an in-memory
 * session.
 */
public class DuckDBUtils
{
    /**
     * The configuration value that requests an in-memory (non-persistent) DuckDB database.
     */
    public static final String IN_MEMORY_DATABASE = ":memory:";

    private static final String JDBC_URL_PREFIX = "jdbc:duckdb:";


    /**
     * Return whether the supplied database path represents an in-memory DuckDB session rather than a file on disk.
     *
     * @param databasePath configured value of the database path/name
     * @return boolean flag
     */
    public static boolean isInMemoryDatabase(String databasePath)
    {
        return (databasePath == null) || (databasePath.isBlank()) || (IN_MEMORY_DATABASE.equals(databasePath));
    }


    /**
     * Build the JDBC connection string for a DuckDB database.
     *
     * @param databasePath path to the ".duckdb" file on disk, or ":memory:"/null for an in-memory session
     * @return connection string
     */
    public static String getDatabaseURL(String databasePath)
    {
        if (isInMemoryDatabase(databasePath))
        {
            return JDBC_URL_PREFIX + IN_MEMORY_DATABASE;
        }

        return JDBC_URL_PREFIX + databasePath;
    }


    /**
     * Extract the ATTACH_STATEMENTS configuration property as a list of complete SQL statements.  This deliberately
     * does NOT use the generic comma-separated-list configuration property helper used elsewhere in this connector
     * suite (eg for excludeDatabaseList) - a realistic ATTACH statement almost always contains at least one comma
     * itself (for example, inside its "(TYPE POSTGRES, READ_ONLY)" options list), so splitting the raw value on
     * every comma would corrupt the statements.  Instead, when the configuration property has been supplied as a
     * JSON array (the norm - it arrives already deserialized as a List), each element is used as-is, one statement
     * per list entry.  A configuration property supplied as a single string is treated as one complete statement.
     *
     * @param configurationProperties configuration properties for this survey/catalog target
     * @return list of statements - never null, may be empty
     */
    public static List<String> getAttachStatements(Map<String, Object> configurationProperties)
    {
        List<String> attachStatements = new ArrayList<>();

        if (configurationProperties != null)
        {
            Object attachStatementsProperty = configurationProperties.get(DuckDBConfigurationProperty.ATTACH_STATEMENTS.getName());

            if (attachStatementsProperty instanceof Collection<?> attachStatementsCollection)
            {
                for (Object attachStatement : attachStatementsCollection)
                {
                    if (attachStatement != null)
                    {
                        attachStatements.add(attachStatement.toString());
                    }
                }
            }
            else if (attachStatementsProperty != null)
            {
                attachStatements.add(attachStatementsProperty.toString());
            }
        }

        return attachStatements;
    }


    /**
     * Run the configured ATTACH_STATEMENTS on a freshly-opened DuckDB connection.  DuckDB does not persist
     * ATTACH-ed data sources in the database file between sessions, so a survey or catalog connector's own
     * connection needs to re-issue the original ATTACH (and any INSTALL/LOAD) statements itself before it queries
     * duckdb_databases() - otherwise it will never see any of the database's federation relationships.  Each
     * statement is run independently and defensively: one failing statement (for example, an extension that is
     * already installed, or a stale/unreachable attachment) must not prevent the rest of the statements from
     * running, or the rest of the survey/catalog pass from completing.
     *
     * @param connection     freshly-opened connection to the DuckDB database
     * @param attachStatements SQL statements to run, in order - may be null or empty
     * @param auditLog       logging destination
     * @param connectorName  name of the calling connector - used in audit log messages
     * @param databaseName   name/path of the DuckDB database - used only in audit log messages
     */
    public static void runAttachStatements(Connection   connection,
                                           List<String> attachStatements,
                                           AuditLog     auditLog,
                                           String       connectorName,
                                           String       databaseName)
    {
        if (attachStatements == null)
        {
            return;
        }

        for (String attachStatement : attachStatements)
        {
            if ((attachStatement != null) && (!attachStatement.isBlank()))
            {
                try (Statement statement = connection.createStatement())
                {
                    statement.execute(attachStatement);
                }
                catch (Exception error)
                {
                    if (auditLog != null)
                    {
                        auditLog.logMessage("runAttachStatements",
                                            DuckDBAuditCode.ATTACH_STATEMENT_FAILED.getMessageDefinition(connectorName,
                                                                                                          attachStatement,
                                                                                                          databaseName,
                                                                                                          error.getMessage()));
                    }
                }
            }
        }
    }
}
