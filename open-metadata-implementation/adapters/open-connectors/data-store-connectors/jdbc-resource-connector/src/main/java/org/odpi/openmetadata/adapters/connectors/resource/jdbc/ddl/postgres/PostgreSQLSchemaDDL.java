/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ffdc.JDBCErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds up the definition of a schema, its tables, columns, primary keys, foreign keys and comments.
 */
public class PostgreSQLSchemaDDL
{
    private final String                schemaName;
    private final String                schemaDescription;
    private final List<PostgreSQLTable> tables;

    public PostgreSQLSchemaDDL(String                schemaName,
                               String                schemaDescription,
                               List<PostgreSQLTable> tables) throws InvalidParameterException
    {
        if (schemaName == null)
        {
            final String actionDescription = "Manage PostgreSQL Database Definitions";
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

        ddlStatements.add("create schema if not exists " + schemaName + ";");

        if (schemaDescription != null)
        {
            ddlStatements.add(getComment("schema",
                                         schemaName,
                                         schemaDescription));
        }

        if (tables != null)
        {
            for (PostgreSQLTable table : tables)
            {
                if (table != null)
                {
                    /*
                     * Define the table and its columns
                     */
                    ddlStatements.add("create table if not exists " +
                                              table.getTableName() + "(" +
                                              this.getColumnsDDL(table.getTableName(),
                                                                 table.getPrimaryKeys(),
                                                                 table.getDataColumns(),
                                                                 table.getForeignKeys()) + ");");

                    /*
                     * Add the table comment.
                     */
                    if (table.getTableDescription() != null)
                    {
                        ddlStatements.add(getComment("table",
                                                     table.getTableName(),
                                                     table.getTableDescription()));
                    }

                    /*
                     * Add comments for each of the columns.
                     */
                    ddlStatements.addAll(getColumnCommentStatements(table.getTableName(), table.getPrimaryKeys()));
                    ddlStatements.addAll(getColumnCommentStatements(table.getTableName(), table.getDataColumns()));
                }
            }
        }

        return ddlStatements;
    }


    /**
     * Return the DDL for all the columns in a table with the constraints for primary and foreign keys.
     *
     * @param tableName name of table
     * @param primaryKeys list of columns that are the primary keys (maybe null)
     * @param dataColumns list of non-primary key columns (maybe null)
     * @param foreignKeys this of foreign key constraints for any column listed above (maybe null)
     * @return string
     */
    private String getColumnsDDL(String                     tableName,
                                 List<PostgreSQLColumn>     primaryKeys,
                                 List<PostgreSQLColumn>     dataColumns,
                                 List<PostgreSQLForeignKey> foreignKeys)
    {
        Map<String, PostgreSQLForeignKey> foreignKeyMap = new HashMap<>();

        if (foreignKeys != null)
        {
            for (PostgreSQLForeignKey foreignKey : foreignKeys)
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
                addPrimaryKeysConstraint(tableName, primaryKeys);
    }


    /**
     * Build up the DDL for a list of columns.
     *
     * @param columns list of columns (maybe null)
     * @param foreignKeyMap map of column names to details for all the foreign keys
     * @param firstColumn is this the first column.
     * @return string
     */
    private String getColumnsDDL(List<PostgreSQLColumn>            columns,
                                 Map<String, PostgreSQLForeignKey> foreignKeyMap,
                                 boolean                           firstColumn)
    {
        if (columns != null)
        {
            StringBuilder stringBuilder = new StringBuilder();

            for (PostgreSQLColumn column : columns)
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
                    stringBuilder.append(column.getColumnType().getPostgresType());

                    if (column.isNotNull())
                    {
                        stringBuilder.append(" not null");
                    }

                    if (foreignKeyMap.get(column.getColumnName()) != null)
                    {
                        PostgreSQLForeignKey foreignKey = foreignKeyMap.get(column.getColumnName());

                        stringBuilder.append(" constraint ");
                        stringBuilder.append(foreignKey.getConstraintName());
                        stringBuilder.append(" references ");
                        stringBuilder.append(foreignKey.getReferenceTable().getTableName());
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
     * @param tableName name of the table
     * @param primaryKeys list of primary keys or null
     * @return string
     */
    private String addPrimaryKeysConstraint(String                 tableName,
                                            List<PostgreSQLColumn> primaryKeys)
    {
        if (primaryKeys != null)
        {
            StringBuilder stringBuilder = new StringBuilder(", constraint " + tableName + "_pk primary key (");

            boolean firstPrimaryKey = true;

            for (PostgreSQLColumn primaryKey : primaryKeys)
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
     * Return the comment statements for a list of columns.
     *
     * @param tableName name of table
     * @param columns list of columns (maybe null)
     * @return list of comment statements
     */
    private List<String> getColumnCommentStatements(String                 tableName,
                                                    List<PostgreSQLColumn> columns)
    {
        List<String> ddlStatements = new ArrayList<>();

        if (columns != null)
        {
            for (PostgreSQLColumn column : columns)
            {
                if ((column != null) && (column.getColumnDescription() != null))
                {
                    ddlStatements.add(getComment("column",
                                                 tableName + "." + column.getColumnName(),
                                                 column.getColumnDescription()));
                }
            }
        }

        return ddlStatements;
    }


    /**
     * Build up a comment for an element based on its description.
     *
     * @param definitionType type of definition (schema, table, column)
     * @param definitionName name of definition
     * @param definitionDescription description - may have dodgy characters
     * @return string (not null)
     */
    private String getComment(String definitionType,
                              String definitionName,
                              String definitionDescription)
    {
        if (definitionDescription != null)
        {
            String[] descriptionParts = definitionDescription.split("'");

            StringBuilder stringBuilder = new StringBuilder("comment on ");

            stringBuilder.append(definitionType);
            stringBuilder.append(" ");
            stringBuilder.append(definitionName);
            stringBuilder.append(" is '");

            if (descriptionParts.length == 1)
            {
                stringBuilder.append(definitionDescription);
            }
            else
            {
                int index = 0;

                while (index < descriptionParts.length - 1)
                {
                    stringBuilder.append(descriptionParts[index]);
                    stringBuilder.append("''");

                    index ++;
                }

                stringBuilder.append(descriptionParts[index]);
            }

            stringBuilder.append("';");

            return stringBuilder.toString();
        }

        return "";
    }
}
