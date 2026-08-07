/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.mssql;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ffdc.JDBCErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

import java.util.*;

/**
 * Builds up the definition of a schema, its tables, columns, primary keys, foreign keys and comments, using
 * Microsoft SQL Server's T-SQL dialect.
 * <br><br>
 * Two T-SQL differences from PostgreSQL's DDL drive most of the divergence from {@code PostgreSQLSchemaDDL}:
 * <ul>
 *     <li>T-SQL has no "IF NOT EXISTS" clause on CREATE SCHEMA/CREATE TABLE/ALTER TABLE ADD COLUMN, so existence
 *     checks are built explicitly from {@code sys.schemas}/{@code OBJECT_ID}/{@code COL_LENGTH}.</li>
 *     <li>T-SQL has no "COMMENT ON" statement - descriptions are recorded with the {@code sys.sp_addextendedproperty}
 *     stored procedure instead (the mechanism SQL Server Management Studio itself uses for object descriptions).</li>
 * </ul>
 * Table names passed to this class are expected to already be schema-qualified (eg "schemaName.tableName"), since
 * - unlike PostgreSQL's {@code currentSchema=} JDBC URL parameter - Microsoft SQL Server has no equivalent way to
 * set an unqualified name's default schema from the connection string.
 */
public class MSSQLSchemaDDL
{
    private final String            schemaName;
    private final String            schemaDescription;
    private final List<MSSQLTable>  tables;

    public MSSQLSchemaDDL(String           schemaName,
                          String           schemaDescription,
                          List<MSSQLTable> tables) throws InvalidParameterException
    {
        if (schemaName == null)
        {
            final String actionDescription = "Manage Microsoft SQL Server Database Definitions";
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

        ddlStatements.add("IF NOT EXISTS (SELECT 1 FROM sys.schemas WHERE name = " + quoteLiteral(schemaName) + ") EXEC('CREATE SCHEMA " + schemaName + "');");

        if (schemaDescription != null)
        {
            ddlStatements.add(getSchemaComment(schemaName, schemaDescription));
        }

        if (tables != null)
        {
            for (MSSQLTable table : tables)
            {
                if (table != null)
                {
                    String qualifiedTableName = table.getTableName(schemaName);

                    /*
                     * Define the table and its columns
                     */
                    ddlStatements.add("IF OBJECT_ID(" + quoteLiteral(qualifiedTableName) + ", N'U') IS NULL CREATE TABLE " +
                                              qualifiedTableName + "(" +
                                              this.getColumnsDDL(qualifiedTableName,
                                                                 table.getPrimaryKeys(),
                                                                 table.getDataColumns(),
                                                                 table.getForeignKeys()) + ");");

                    if (table.getNewColumns() != null)
                    {
                        ddlStatements.addAll(this.getAlterTableDDL(qualifiedTableName, table.getNewColumns()));
                    }


                    /*
                     * Add the table comment.
                     */
                    if (table.getTableDescription() != null)
                    {
                        ddlStatements.add(getTableComment(schemaName, table.getTableName(), table.getTableDescription()));
                    }

                    /*
                     * Add comments for each of the columns.
                     */
                    ddlStatements.addAll(getColumnCommentStatements(table.getTableName(), table.getPrimaryKeys()));
                    ddlStatements.addAll(getColumnCommentStatements(table.getTableName(), table.getDataColumns()));
                    ddlStatements.addAll(getColumnCommentStatements(table.getTableName(), table.getNewColumns()));
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
    private Collection<String> getAlterTableDDL(String            qualifiedTableName,
                                                List<MSSQLColumn> newColumns)
    {
        Collection<String> ddlStatements = new ArrayList<>();

        for (MSSQLColumn column : newColumns)
        {
            ddlStatements.add("IF COL_LENGTH(" + quoteLiteral(qualifiedTableName) + ", " + quoteLiteral(column.getColumnName()) + ") IS NULL " +
                                      "ALTER TABLE " + qualifiedTableName + " ADD " + column.getColumnName() + " " + column.getColumnType().getMssqlType() + ";");
        }
        return ddlStatements;
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
    private String getColumnsDDL(String                qualifiedTableName,
                                 List<MSSQLColumn>     primaryKeys,
                                 List<MSSQLColumn>     dataColumns,
                                 List<MSSQLForeignKey> foreignKeys)
    {
        Map<String, MSSQLForeignKey> foreignKeyMap = new HashMap<>();

        if (foreignKeys != null)
        {
            for (MSSQLForeignKey foreignKey : foreignKeys)
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
    private String getColumnsDDL(List<MSSQLColumn>            columns,
                                 Map<String, MSSQLForeignKey> foreignKeyMap,
                                 boolean                      firstColumn)
    {
        if (columns != null)
        {
            StringBuilder stringBuilder = new StringBuilder();

            for (MSSQLColumn column : columns)
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
                    stringBuilder.append(column.getColumnType().getMssqlType());

                    if (column.isNotNull())
                    {
                        stringBuilder.append(" not null");
                    }

                    if (foreignKeyMap.get(column.getColumnName()) != null)
                    {
                        MSSQLForeignKey foreignKey = foreignKeyMap.get(column.getColumnName());

                        stringBuilder.append(" constraint ");
                        stringBuilder.append(foreignKey.getConstraintName());
                        stringBuilder.append(" references ");
                        stringBuilder.append(foreignKey.getReferenceTable().getTableName());
                        stringBuilder.append(" (");
                        stringBuilder.append(foreignKey.getReferenceColumn().getColumnName());
                        stringBuilder.append(")");
                        stringBuilder.append(" on update ");
                        if (foreignKey.isUpdateCascade())
                        {
                            stringBuilder.append("cascade");
                        }
                        else
                        {
                            stringBuilder.append("no action");
                        }
                        stringBuilder.append(" on delete ");
                        if (foreignKey.isDeleteCascade())
                        {
                            stringBuilder.append("cascade");
                        }
                        else
                        {
                            stringBuilder.append("no action");
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
    private String addPrimaryKeysConstraint(String            qualifiedTableName,
                                            List<MSSQLColumn> primaryKeys)
    {
        if (primaryKeys != null)
        {
            StringBuilder stringBuilder = new StringBuilder(", constraint " + getConstraintName(qualifiedTableName) + "_pk primary key (");

            boolean firstPrimaryKey = true;

            for (MSSQLColumn primaryKey : primaryKeys)
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
     * Constraint names must be unique within a database (not just a schema) in SQL Server, so the schema-qualified
     * table name is turned into a single identifier-safe token rather than reusing the bare table name.
     *
     * @param qualifiedTableName schema-qualified name of the table
     * @return string safe to embed in a constraint name
     */
    private String getConstraintName(String qualifiedTableName)
    {
        return qualifiedTableName.replace(".", "_");
    }


    /**
     * Return the comment statement for a schema based on its description.
     *
     * @param schemaName name of schema
     * @param description description - may have dodgy characters
     * @return string (not null)
     */
    private String getSchemaComment(String schemaName,
                                    String description)
    {
        return "EXEC sys.sp_addextendedproperty @name = N'MS_Description', @value = " + quoteLiteral(description) + ", " +
                "@level0type = N'SCHEMA', @level0name = " + quoteLiteral(schemaName) + ";";
    }


    /**
     * Return the comment statement for a table based on its description.
     *
     * @param schemaName name of schema
     * @param tableName bare (unqualified) name of table
     * @param description description - may have dodgy characters
     * @return string (not null)
     */
    private String getTableComment(String schemaName,
                                   String tableName,
                                   String description)
    {
        return "EXEC sys.sp_addextendedproperty @name = N'MS_Description', @value = " + quoteLiteral(description) + ", " +
                "@level0type = N'SCHEMA', @level0name = " + quoteLiteral(schemaName) + ", " +
                "@level1type = N'TABLE', @level1name = " + quoteLiteral(tableName) + ";";
    }


    /**
     * Return the comment statements for a list of columns.
     *
     * @param tableName bare (unqualified) name of table
     * @param columns list of columns (maybe null)
     * @return list of comment statements
     */
    private List<String> getColumnCommentStatements(String            tableName,
                                                     List<MSSQLColumn> columns)
    {
        List<String> ddlStatements = new ArrayList<>();

        if (columns != null)
        {
            for (MSSQLColumn column : columns)
            {
                if ((column != null) && (column.getColumnDescription() != null))
                {
                    ddlStatements.add("EXEC sys.sp_addextendedproperty @name = N'MS_Description', @value = " + quoteLiteral(column.getColumnDescription()) + ", " +
                                              "@level0type = N'SCHEMA', @level0name = " + quoteLiteral(schemaName) + ", " +
                                              "@level1type = N'TABLE', @level1name = " + quoteLiteral(tableName) + ", " +
                                              "@level2type = N'COLUMN', @level2name = " + quoteLiteral(column.getColumnName()) + ";");
                }
            }
        }

        return ddlStatements;
    }


    /**
     * Turn a value into a single-quoted, escaped T-SQL nvarchar literal.
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

        return "N'" + value.replace("'", "''") + "'";
    }
}
