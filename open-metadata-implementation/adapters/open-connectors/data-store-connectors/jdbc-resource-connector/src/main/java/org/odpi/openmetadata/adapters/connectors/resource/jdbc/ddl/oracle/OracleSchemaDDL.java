/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.oracle;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ffdc.JDBCErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

import java.util.*;

/**
 * Builds up the definition of a schema, its tables, columns, primary keys, foreign keys and comments, using
 * Oracle Database's SQL/PL-SQL dialect.
 * <br><br>
 * Oracle's DDL diverges from both {@code PostgreSQLSchemaDDL} and {@code MSSQLSchemaDDL} in a few important ways:
 * <ul>
 *     <li>An Oracle "schema" is a database user - there is no separate namespace-creation statement equivalent to
 *     "CREATE SCHEMA".  Users are expected to be provisioned ahead of time (with their tablespace quota and
 *     session/table privileges set up by a DBA), so no schema-creation DDL is generated here.</li>
 *     <li>Oracle SQL has no "IF NOT EXISTS" clause on CREATE TABLE/ALTER TABLE ADD, so idempotency is achieved with
 *     the standard Oracle technique of wrapping the DDL in an anonymous PL/SQL block that ignores
 *     ORA-00955 (name already used by an existing object) / ORA-01430 (column being added already exists).</li>
 *     <li>Oracle has no "COMMENT ON SCHEMA"/"COMMENT ON USER" statement, so a schema description cannot be recorded
 *     natively and is silently skipped.  COMMENT ON TABLE and COMMENT ON COLUMN, however, are natively supported
 *     and are idempotent (re-running them simply overwrites the previous comment), so no guard is required there.</li>
 *     <li>Oracle foreign key constraints support no "ON UPDATE" clause at all (Oracle expects primary keys to be
 *     immutable), so only the "ON DELETE" behaviour is generated.</li>
 * </ul>
 * Table names passed to this class are expected to already be schema-qualified (eg "schemaName.tableName"), since
 * - unlike PostgreSQL's {@code currentSchema=} JDBC URL parameter - Oracle has no equivalent way to set an
 * unqualified name's default schema from the connection string.
 */
public class OracleSchemaDDL
{
    private final String            schemaName;
    private final String            schemaDescription;
    private final List<OracleTable> tables;

    public OracleSchemaDDL(String           schemaName,
                           String           schemaDescription,
                           List<OracleTable> tables) throws InvalidParameterException
    {
        if (schemaName == null)
        {
            final String actionDescription = "Manage Oracle Database Definitions";
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
         * Oracle has no schema-creation or schema-comment statement - see the class Javadoc.  The Oracle user
         * that this schema maps to, and its privileges/tablespace quota, are expected to already exist.
         */

        if (tables != null)
        {
            for (OracleTable table : tables)
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
                                                    -955));

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
                                                List<OracleColumn> newColumns)
    {
        Collection<String> ddlStatements = new ArrayList<>();

        for (OracleColumn column : newColumns)
        {
            String columnDefinition = column.getColumnName() + " " + column.getColumnType().getOracleType();

            if (column.isNotNull())
            {
                columnDefinition = columnDefinition + " not null";
            }

            ddlStatements.add(getGuardedDDL("ALTER TABLE " + qualifiedTableName + " ADD (" + columnDefinition + ")",
                                            -1430));
        }
        return ddlStatements;
    }


    /**
     * Wrap a DDL statement in an anonymous PL/SQL block that ignores the given ORA error number - the standard
     * Oracle technique for making CREATE/ALTER statements idempotent, since Oracle SQL has no "IF NOT EXISTS"
     * clause on them.
     *
     * @param ddlText the DDL statement text, unquoted
     * @param ignorableErrorNumber the (negative) ORA error number to swallow, eg -955 for
     *                              "name is already used by an existing object"
     * @return anonymous PL/SQL block
     */
    private String getGuardedDDL(String ddlText,
                                 int    ignorableErrorNumber)
    {
        return "BEGIN\n" +
                "  EXECUTE IMMEDIATE " + quoteLiteral(ddlText) + ";\n" +
                "EXCEPTION\n" +
                "  WHEN OTHERS THEN\n" +
                "    IF SQLCODE != " + ignorableErrorNumber + " THEN\n" +
                "      RAISE;\n" +
                "    END IF;\n" +
                "END;";
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
                                 List<OracleColumn>     primaryKeys,
                                 List<OracleColumn>     dataColumns,
                                 List<OracleForeignKey> foreignKeys)
    {
        Map<String, OracleForeignKey> foreignKeyMap = new HashMap<>();

        if (foreignKeys != null)
        {
            for (OracleForeignKey foreignKey : foreignKeys)
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
    private String getColumnsDDL(List<OracleColumn>            columns,
                                 Map<String, OracleForeignKey> foreignKeyMap,
                                 boolean                       firstColumn)
    {
        if (columns != null)
        {
            StringBuilder stringBuilder = new StringBuilder();

            for (OracleColumn column : columns)
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
                    stringBuilder.append(column.getColumnType().getOracleType());

                    if (column.isNotNull())
                    {
                        stringBuilder.append(" not null");
                    }

                    if (foreignKeyMap.get(column.getColumnName()) != null)
                    {
                        OracleForeignKey foreignKey = foreignKeyMap.get(column.getColumnName());

                        stringBuilder.append(" constraint ");
                        stringBuilder.append(foreignKey.getConstraintName());
                        stringBuilder.append(" references ");
                        stringBuilder.append(foreignKey.getReferenceTable().getTableName());
                        stringBuilder.append(" (");
                        stringBuilder.append(foreignKey.getReferenceColumn().getColumnName());
                        stringBuilder.append(")");

                        /*
                         * Oracle has no "ON UPDATE" clause on foreign key constraints, so only the "ON DELETE"
                         * behaviour can be expressed here.  When cascading delete is not wanted, the clause is
                         * omitted entirely rather than writing "on delete no action", which is not valid Oracle
                         * syntax.
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
                                            List<OracleColumn> primaryKeys)
    {
        if (primaryKeys != null)
        {
            StringBuilder stringBuilder = new StringBuilder(", constraint " + getConstraintName(qualifiedTableName) + "_pk primary key (");

            boolean firstPrimaryKey = true;

            for (OracleColumn primaryKey : primaryKeys)
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
     * Constraint names must be unique within an Oracle schema (a looser rule than Microsoft SQL Server, where they
     * must be unique within the whole database), but the schema-qualified table name is still turned into a single
     * identifier-safe token here for consistency and to keep the same safety margin.
     *
     * @param qualifiedTableName schema-qualified name of the table
     * @return string safe to embed in a constraint name
     */
    private String getConstraintName(String qualifiedTableName)
    {
        return qualifiedTableName.replace(".", "_");
    }


    /**
     * Return the native Oracle COMMENT ON TABLE statement for a table based on its description.  Unlike the guarded
     * PL/SQL blocks used for CREATE TABLE/ALTER TABLE above, this is executed as a plain SQL statement (via
     * {@code JDBCResourceConnector.issueSQLCommand}), so - unlike a PL/SQL block's mandatory closing "END;" - it
     * must not have a trailing semicolon, or Oracle JDBC rejects it with ORA-00911 (invalid character).
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
     * Return the comment statements for a list of columns, using the native Oracle COMMENT ON COLUMN statement.
     * As with getTableComment() above, these are executed as plain SQL statements, so must not have a trailing
     * semicolon.
     *
     * @param qualifiedTableName schema-qualified name of table
     * @param columns list of columns (maybe null)
     * @return list of comment statements
     */
    private List<String> getColumnCommentStatements(String             qualifiedTableName,
                                                     List<OracleColumn> columns)
    {
        List<String> ddlStatements = new ArrayList<>();

        if (columns != null)
        {
            for (OracleColumn column : columns)
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
     * Turn a value into a single-quoted, escaped Oracle SQL string literal.
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
