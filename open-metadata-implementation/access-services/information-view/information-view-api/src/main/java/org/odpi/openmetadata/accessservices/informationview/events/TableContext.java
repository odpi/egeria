/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.informationview.events;

public class TableContext {
    private String tableName;
    private String tableQualifiedName;
    private String tableTypeQualifiedName;
    private String schemaName;
    private String schemaQualifiedName;
    private String schemaTypeQualifiedName;
    private String databaseName;
    private String databaseQualifiedName;

    public TableContext() {
    }

    /**
     * Return the name of the table
     *
     * @return name of the table
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * set up the name of the table
     *
     * @param tableName - name of the table
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Return the name of the schema
     *
     * @return name of the schema
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * set up the name of the schema
     *
     * @param schemaName - name of the schema
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * Return the name of the database
     *
     * @return name of the database
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * set up the name of the database
     *
     * @param databaseName - name of the database
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * Return the qualified name of the table
     *
     * @return qualified name of the table
     */
    public String getTableQualifiedName() {
        return tableQualifiedName;
    }

    /**
     * set up the qualified name of the table
     *
     * @param tableQualifiedName - qualified name of the table
     */
    public void setTableQualifiedName(String tableQualifiedName) {
        this.tableQualifiedName = tableQualifiedName;
    }

    /**
     * Return the qualified name of the table type
     *
     * @return qualified name of the table type
     */
    public String getTableTypeQualifiedName() {
        return tableTypeQualifiedName;
    }

    /**
     * set up the qualified name of the table type
     *
     * @param tableTypeQualifiedName - qualified name of the table type
     */
    public void setTableTypeQualifiedName(String tableTypeQualifiedName) {
        this.tableTypeQualifiedName = tableTypeQualifiedName;
    }

    /**
     * Return the qualified  name of the schema
     *
     * @return qualified name of the schema
     */
    public String getSchemaQualifiedName() {
        return schemaQualifiedName;
    }

    /**
     * set up the qualified name of the table
     *
     * @param schemaQualifiedName - qualified name of the schema
     */
    public void setSchemaQualifiedName(String schemaQualifiedName) {
        this.schemaQualifiedName = schemaQualifiedName;
    }

    /**
     * Return the qualified name of the database
     *
     * @return qualified name of the database
     */
    public String getDatabaseQualifiedName() {
        return databaseQualifiedName;
    }

    /**
     * set up the qualified name of the database
     *
     * @param databaseQualifiedName - qualified name of the database
     */
    public void setDatabaseQualifiedName(String databaseQualifiedName) {
        this.databaseQualifiedName = databaseQualifiedName;
    }

    /**
     * Return the qualified  name of the schema type
     *
     * @return qualified name of the schema type
     */
    public String getSchemaTypeQualifiedName() {
        return schemaTypeQualifiedName;
    }

    /**
     * set up the qualified name of the schema type
     *
     * @param schemaTypeQualifiedName - qualified name of the schema type
     */
    public void setSchemaTypeQualifiedName(String schemaTypeQualifiedName) {
        this.schemaTypeQualifiedName = schemaTypeQualifiedName;
    }

    @Override
    public String toString() {
        return "TableContext{" +
                "tableName='" + tableName + '\'' +
                ", tableQualifiedName='" + tableQualifiedName + '\'' +
                ", tableTypeQualifiedName='" + tableTypeQualifiedName + '\'' +
                ", schemaName='" + schemaName + '\'' +
                ", schemaQualifiedName='" + schemaQualifiedName + '\'' +
                ", schemaTypeQualifiedName='" + schemaTypeQualifiedName + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", databaseQualifiedName='" + databaseQualifiedName + '\'' +
                '}';
    }
}