/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.db2luw;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ffdc.JDBCErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

import java.util.*;

/**
 * Builds up the definition of a schema, its tables, columns, primary keys, foreign keys and comments, using
 * IBM Db2 for Linux, UNIX and Windows' SQL/SQL-PL dialect.
 * <br><br>
 * Db2's DDL diverges from {@code PostgreSQLSchemaDDL} and {@code MSSQLSchemaDDL} in a few important ways, and is
 * closer to (but not identical to) {@code OracleSchemaDDL}:
 * <ul>
 *     <li>Unlike Oracle, Db2 does support "CREATE SCHEMA" as a genuine namespace-creation statement, so this class
 *     does generate one - unlike an Oracle "schema" (a database user, expected to be pre-provisioned by a DBA).</li>
 *     <li>Db2 SQL has no "IF NOT EXISTS" clause on CREATE TABLE/CREATE SCHEMA/ALTER TABLE ADD, so idempotency is
 *     achieved with Db2's SQL PL equivalent of Oracle's guarded PL/SQL block technique: a compound statement with a
 *     CONTINUE HANDLER that swallows SQLSTATE 42710 (duplicate object - covers both CREATE SCHEMA and CREATE TABLE
 *     on an existing name) or 42711 (duplicate column, for ALTER TABLE ADD).</li>
 *     <li>Unlike Oracle, Db2 does support "COMMENT ON SCHEMA" natively, so a schema description is recorded as a
 *     genuine COMMENT ON SCHEMA statement rather than being silently skipped.  COMMENT ON TABLE and COMMENT ON
 *     COLUMN are also natively supported and, like Oracle's, are idempotent (re-running them simply overwrites the
 *     previous comment), so no guard is required for any of the three.</li>
 *     <li>Like Oracle, Db2 foreign key constraints support no "ON UPDATE" clause at all (Db2 expects primary keys to
 *     be immutable), so only the "ON DELETE" behaviour is generated.</li>
 * </ul>
 * Table names passed to this class are expected to already be schema-qualified (eg "schemaName.tableName"), since
 * - unlike PostgreSQL's {@code currentSchema=} JDBC URL parameter - Db2 has no equivalent way to set an unqualified
 * name's default schema from the connection string.
 */
public class DB2LUWSchemaDDL
{
    private final String            schemaName;
    private final String            schemaDescription;
    private final List<DB2LUWTable> tables;

    public DB2LUWSchemaDDL(String            schemaName,
                           String            schemaDescription,
                           List<DB2LUWTable> tables) throws InvalidParameterException
    {
        if (schemaName == null)
        {
            final String actionDescription = "Manage Db2 for Linux, UNIX and Windows Database Definitions";
            final String parameterName = "schemaName";

            throw new InvalidParameterException(JDBCErrorCode.NULL_SCHEMA_NAME.getMessageDefinition(),
                                                this.getClass().getName(),
                                                actionDescription,
                                                parameterName);
        }

        this.schemaName        = schemaName;
        this.schemaDescription = schemaDescription;
        this.tables            = tables;
    }


    /**
     * Returns the list of DDL statements to define the schema.
     *
     * @return list of statements
     */
    public List<String> getDDLStatements()
    {
        List<String> ddlStatements = new ArrayList<>();

        /*
         * Unlike Oracle, Db2 genuinely creates a namespace here - see the class Javadoc.
         */
        ddlStatements.add(getGuardedDDL("CREATE SCHEMA " + schemaName, "42710"));

        if (schemaDescription != null)
        {
            ddlStatements.add(getSchemaComment(schemaName, schemaDescription));
        }

        if (tables != null)
        {
            for (DB2LUWTable table : tables)
            {
                if (table != null)
                {
                    String qualifiedTableName = table.getTableName(schemaName);

                    /*
                     * Define the table and its columns
                     */
                    ddlStatements.add(getGuardedDDL("CREATE TABLE " + qualifiedTableName + "(" +
                                                            this.getColumnsDDL(qualifiedTableName,
                                                                               table.getPrimaryKeys(),
                                                                               table.getDataColumns(),
                                                                               table.getForeignKeys()) + ")",
                                                    "42710"));

                    if (table.getNewColumns() != null)
                    {
                        ddlStatements.addAll(this.getAlterTableDDL(qualifiedTableName, table.getNewColumns()));
                    }


                    /*
                     * Add the table comment.
                     */
                    if (table.getTableDescription() != null)
                    {
                        ddlStatements.add(getTableComment(qualifiedTableName, table.getTableDescription()));
                    }

                    /*
                     * Add comments for each of the columns.
                     */
                    ddlStatements.addAll(getColumnCommentStatements(qualifiedTableName, table.getPrimaryKeys()));
                    ddlStatements.addAll(getColumnCommentStatements(qualifiedTableName, table.getDataColumns()));
                    ddlStatements.addAll(getColumnCommentStatements(qualifiedTableName, table.getNewColumns()));
                }
            }
        }

        return ddlStatements;
    }


    /**
     * Return the DDL for ALTER TABLE statements to add new columns.
     *
     * @param qualifiedTableName schema-qualified name of table
     * @param newColumns list of columns to add
     * @return list of statements
     */
    private Collection<String> getAlterTableDDL(String             qualifiedTableName,
                                                List<DB2LUWColumn> newColumns)
    {
        Collection<String> ddlStatements = new ArrayList<>();

        for (DB2LUWColumn column : newColumns)
        {
            String columnDefinition = column.getColumnName() + " " + column.getColumnType().getDB2LUWType();

            if (column.isNotNull())
            {
                columnDefinition = columnDefinition + " not null";
            }

            ddlStatements.add(getGuardedDDL("ALTER TABLE " + qualifiedTableName + " ADD COLUMN " + columnDefinition,
                                            "42711"));
        }
        return ddlStatements;
    }


    /**
     * Wrap a DDL statement in an SQL PL compound statement with a continue handler that ignores the given
     * SQLSTATE - the Db2 equivalent of Oracle's guarded anonymous PL/SQL block technique, since Db2 SQL has no
     * "IF NOT EXISTS" clause on CREATE SCHEMA/CREATE TABLE/ALTER TABLE.
     *
     * @param ddlText the DDL statement text, unquoted
     * @param ignorableSQLState the SQLSTATE to swallow, eg "42710" for "object already exists" or "42711" for
     *                          "column already exists"
     * @return SQL PL compound statement
     */
    private String getGuardedDDL(String ddlText,
                                 String ignorableSQLState)
    {
        return "BEGIN\n" +
                "  DECLARE CONTINUE HANDLER FOR SQLSTATE '" + ignorableSQLState + "' BEGIN END;\n" +
                "  EXECUTE IMMEDIATE " + quoteLiteral(ddlText) + ";\n" +
                "END";
    }


    /**
     * Return the DDL for all the columns in a table with the constraints for primary and foreign keys.
     *
     * @param qualifiedTableName schema-qualified name of table
     * @param primaryKeys list of columns that are the primary keys (maybe null)
     * @param dataColumns list of non-primary key columns (maybe null)
     * @param foreignKeys this of foreign key constraints for any column listed above (maybe null)
     * @return string
     */
    private String getColumnsDDL(String                 qualifiedTableName,
                                 List<DB2LUWColumn>     primaryKeys,
                                 List<DB2LUWColumn>     dataColumns,
                                 List<DB2LUWForeignKey> foreignKeys)
    {
        Map<String, DB2LUWForeignKey> foreignKeyMap = new HashMap<>();

        if (foreignKeys != null)
        {
            for (DB2LUWForeignKey foreignKey : foreignKeys)
            {
                if (foreignKey != null)
                {
                    foreignKeyMap.put(foreignKey.getForeignKeyColumn().getColumnName(), foreignKey);
                }
            }
        }

        boolean firstColumn = true;

        String columnDefinitions = getColumnsDDL(primaryKeys, foreignKeyMap, firstColumn);

        if (! columnDefinitions.isBlank())
        {
            firstColumn = false;
        }

        return  columnDefinitions +
                getColumnsDDL(dataColumns, foreignKeyMap, firstColumn) +
                addPrimaryKeysConstraint(qualifiedTableName, primaryKeys);
    }


    /**
     * Build up the DDL for a list of columns.
     *
     * @param columns list of columns (maybe null)
     * @param foreignKeyMap map of column names to details for all the foreign keys
     * @param firstColumn is this the first column.
     * @return string
     */
    private String getColumnsDDL(List<DB2LUWColumn>            columns,
                                 Map<String, DB2LUWForeignKey> foreignKeyMap,
                                 boolean                       firstColumn)
    {
        if (columns != null)
        {
            StringBuilder stringBuilder = new StringBuilder();

            for (DB2LUWColumn column : columns)
            {
                if (column != null)
                {
                    if (firstColumn)
                    {
                        firstColumn = false;
                    }
                    else
                    {
                        stringBuilder.append(", ");
                    }

                    stringBuilder.append(column.getColumnName());
                    stringBuilder.append(" ");
                    stringBuilder.append(column.getColumnType().getDB2LUWType());

                    if (column.isNotNull())
                    {
                        stringBuilder.append(" not null");
                    }

                    if (foreignKeyMap.get(column.getColumnName()) != null)
                    {
                        DB2LUWForeignKey foreignKey = foreignKeyMap.get(column.getColumnName());

                        stringBuilder.append(" constraint ");
                        stringBuilder.append(foreignKey.getConstraintName());
                        stringBuilder.append(" references ");
                        stringBuilder.append(foreignKey.getReferenceTable().getTableName());
                        stringBuilder.append(" (");
                        stringBuilder.append(foreignKey.getReferenceColumn().getColumnName());
                        stringBuilder.append(")");

                        /*
                         * Like Oracle, Db2 has no "ON UPDATE" clause on foreign key constraints, so only the
                         * "ON DELETE" behaviour can be expressed here.  When cascading delete is not wanted, the
                         * clause is omitted entirely rather than writing "on delete no action" explicitly.
                         */
                        if (foreignKey.isDeleteCascade())
                        {
                            stringBuilder.append(" on delete cascade");
                        }
                    }
                }
            }

            return stringBuilder.toString();
        }

        return "";
    }


    /**
     * Identify a table's primary keys (if any).
     *
     * @param qualifiedTableName schema-qualified name of the table
     * @param primaryKeys list of primary keys or null
     * @return string
     */
    private String addPrimaryKeysConstraint(String             qualifiedTableName,
                                            List<DB2LUWColumn> primaryKeys)
    {
        if (primaryKeys != null)
        {
            StringBuilder stringBuilder = new StringBuilder(", constraint " + getConstraintName(qualifiedTableName) + "_pk primary key (");

            boolean firstPrimaryKey = true;

            for (DB2LUWColumn primaryKey : primaryKeys)
            {
                if (primaryKey != null)
                {
                    if (firstPrimaryKey)
                    {
                        firstPrimaryKey = false;
                    }
                    else
                    {
                        stringBuilder.append(", ");
                    }

                    stringBuilder.append(primaryKey.getColumnName());
                }
            }

            stringBuilder.append(")");

            return stringBuilder.toString();
        }

        return "";
    }


    /**
     * Constraint names must be unique within a Db2 schema, but the schema-qualified table name is still turned
     * into a single identifier-safe token here for consistency and to keep the same safety margin as the other
     * vendor connectors.
     *
     * @param qualifiedTableName schema-qualified name of the table
     * @return string safe to embed in a constraint name
     */
    private String getConstraintName(String qualifiedTableName)
    {
        return qualifiedTableName.replace(".", "_");
    }


    /**
     * Return the native Db2 COMMENT ON SCHEMA statement for a schema based on its description.  Unlike Oracle,
     * which has no schema-comment statement, Db2 for Linux, UNIX and Windows supports this natively.
     *
     * @param schemaName name of schema
     * @param description description - may have dodgy characters
     * @return string (not null)
     */
    private String getSchemaComment(String schemaName,
                                    String description)
    {
        return "COMMENT ON SCHEMA " + schemaName + " IS " + quoteLiteral(description);
    }


    /**
     * Return the native Db2 COMMENT ON TABLE statement for a table based on its description.  Unlike the guarded
     * compound statements used for CREATE SCHEMA/CREATE TABLE/ALTER TABLE above, this is executed as a plain SQL
     * statement, so must not have a trailing semicolon.
     *
     * @param qualifiedTableName schema-qualified name of table
     * @param description description - may have dodgy characters
     * @return string (not null)
     */
    private String getTableComment(String qualifiedTableName,
                                   String description)
    {
        return "COMMENT ON TABLE " + qualifiedTableName + " IS " + quoteLiteral(description);
    }


    /**
     * Return the comment statements for a list of columns, using the native Db2 COMMENT ON COLUMN statement.
     * As with getTableComment() above, these are executed as plain SQL statements, so must not have a trailing
     * semicolon.
     *
     * @param qualifiedTableName schema-qualified name of table
     * @param columns list of columns (maybe null)
     * @return list of comment statements
     */
    private List<String> getColumnCommentStatements(String             qualifiedTableName,
                                                     List<DB2LUWColumn> columns)
    {
        List<String> ddlStatements = new ArrayList<>();

        if (columns != null)
        {
            for (DB2LUWColumn column : columns)
            {
                if ((column != null) && (column.getColumnDescription() != null))
                {
                    ddlStatements.add("COMMENT ON COLUMN " + qualifiedTableName + "." + column.getColumnName() + " IS " +
                                              quoteLiteral(column.getColumnDescription()));
                }
            }
        }

        return ddlStatements;
    }


    /**
     * Turn a value into a single-quoted, escaped Db2 SQL string literal.
     *
     * @param value value to quote - may have embedded single quotes
     * @return string (not null)
     */
    private String quoteLiteral(String value)
    {
        if (value == null)
        {
            return "NULL";
        }

        return "'" + value.replace("'", "''") + "'";
    }
}
