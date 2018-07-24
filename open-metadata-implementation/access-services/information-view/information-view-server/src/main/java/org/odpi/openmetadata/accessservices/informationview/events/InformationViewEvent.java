/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InformationViewEvent {

    private String tableName;
    private String tableQualifiedName;
    private String tableTypeQualifiedName;
    private String schemaName;
    private String schemaQualifiedName;
    private String schemaTypeQualifiedName;
    private String databaseName;
    private String databaseQualifiedName;
    private ConnectionDetails connectionDetails;
    private List<DerivedColumnDetail> derivedColumns;

    /**
     * Return the name of the table
     *
     * @return name of the table
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * set up the table name
     *
     * @param tableName - name of the table
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Return the name of the schema
     *
     * @return the name of the schema
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * Return the name of the schema
     *
     * @param schemaName - name of the schema
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * Return the connection details
     *
     * @return properties of the connection
     */
    public ConnectionDetails getConnectionDetails() {
        return connectionDetails;
    }

    /**
     * Set up the connection details
     *
     * @param connectionDetails - properties of the connection
     */
    public void setConnectionDetails(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    /**
     * Return list of derived columns
     *
     * @return the list of the properties for each derived column
     */
    public List<DerivedColumnDetail> getDerivedColumns() {
        return derivedColumns;
    }

    /**
     * Set up the list of derived columns
     *
     * @param derivedColumns - list of properties for each derived columns
     */
    public void setDerivedColumns(List<DerivedColumnDetail> derivedColumns) {
        this.derivedColumns = derivedColumns;
    }

    /**
     *
     * @return the name of the database
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Set up the name of the database
     *
     * @param databaseName - name of database
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * Return qualified name property for table
     *
     * @return qualified name
     */
    public String getTableQualifiedName() {
        return tableQualifiedName;
    }

    /**
     * Set up the qualified name of the table
     *
     * @param tableQualifiedName - qualified name of the table entity
     */
    public void setTableQualifiedName(String tableQualifiedName) {
        this.tableQualifiedName = tableQualifiedName;
    }

    /**
     * Return qualified name property for table type
     *
     * @return the qualified name of table type
     */
    public String getTableTypeQualifiedName() {
        return tableTypeQualifiedName;
    }

    /**
     * Set up the qualified name of the table type
     *
     * @param tableTypeQualifiedName - qualified name for table type
     */
    public void setTableTypeQualifiedName(String tableTypeQualifiedName) {
        this.tableTypeQualifiedName = tableTypeQualifiedName;
    }

    /**
     * Return qualified name property for schema
     *
     * @return qualified name of schema
     */
    public String getSchemaQualifiedName() {
        return schemaQualifiedName;
    }

    /**
     * Set up the qualified name of the schema
     *
     * @param schemaQualifiedName - qualified name of schema
     */
    public void setSchemaQualifiedName(String schemaQualifiedName) {
        this.schemaQualifiedName = schemaQualifiedName;
    }

    /**
     * Return qualified name property for database
     *
     * @return qualified name of database
     */
    public String getDatabaseQualifiedName() {
        return databaseQualifiedName;
    }

    /**
     * Set up the qualified name of the database
     *
     * @param databaseQualifiedName -  qualified name of database
     */
    public void setDatabaseQualifiedName(String databaseQualifiedName) {
        this.databaseQualifiedName = databaseQualifiedName;
    }

    /**
     * Return qualified name property for schema type
     *
     * @return qualified name of schema type
     */
    public String getSchemaTypeQualifiedName() {
        return schemaTypeQualifiedName;
    }

    /**
     * Set up the qualified name of the schema type
     *
     * @param schemaTypeQualifiedName  -  qualified name of schema type
     */
    public void setSchemaTypeQualifiedName(String schemaTypeQualifiedName) {
        this.schemaTypeQualifiedName = schemaTypeQualifiedName;
    }
}
