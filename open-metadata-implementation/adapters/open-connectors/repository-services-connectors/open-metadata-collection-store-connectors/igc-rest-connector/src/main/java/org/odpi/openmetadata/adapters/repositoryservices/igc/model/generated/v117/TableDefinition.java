/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'table_definition' asset type in IGC, displayed as 'Table Definition' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TableDefinition extends MainObject {

    public static final String IGC_TYPE_ID = "table_definition";

    /**
     * The 'transformation_project' property, displayed as 'Transformation Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformationProject} object.
     */
    protected Reference transformation_project;

    /**
     * The 'column_definitions' property, displayed as 'Column Definitions' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ColumnDefinition} objects.
     */
    protected ReferenceList column_definitions;

    /**
     * The 'included_by_stages' property, displayed as 'Included by Stages' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList included_by_stages;

    /**
     * The 'foreign_keys' property, displayed as 'Foreign Keys' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ForeignKeyDefinition} objects.
     */
    protected ReferenceList foreign_keys;

    /**
     * The 'referenced_by_foreign_keys' property, displayed as 'Referenced by Foreign Keys' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ForeignKeyDefinition} objects.
     */
    protected ReferenceList referenced_by_foreign_keys;

    /**
     * The 'data_source_type' property, displayed as 'Data Source Type' in the IGC UI.
     */
    protected String data_source_type;

    /**
     * The 'data_source_name' property, displayed as 'Data Source Name' in the IGC UI.
     */
    protected String data_source_name;

    /**
     * The 'machine_or_platform_type' property, displayed as 'Machine or Platform Type' in the IGC UI.
     */
    protected String machine_or_platform_type;

    /**
     * The 'mainframe_access_type' property, displayed as 'Mainframe Access Type' in the IGC UI.
     */
    protected String mainframe_access_type;

    /**
     * The 'owner' property, displayed as 'Owner' in the IGC UI.
     */
    protected String owner;

    /**
     * The 'referenced_by_data_sources' property, displayed as 'Referenced by Data Sources' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Datagroup} object.
     */
    protected Reference referenced_by_data_sources;

    /**
     * The 'table_type' property, displayed as 'Table Type' in the IGC UI.
     */
    protected String table_type;

    /**
     * The 'computer' property, displayed as 'Computer' in the IGC UI.
     */
    protected String computer;

    /**
     * The 'software_product' property, displayed as 'Software Product' in the IGC UI.
     */
    protected String software_product;

    /**
     * The 'data_store' property, displayed as 'Data Store' in the IGC UI.
     */
    protected String data_store;

    /**
     * The 'data_schema' property, displayed as 'Data Schema' in the IGC UI.
     */
    protected String data_schema;

    /**
     * The 'data_collection' property, displayed as 'Data Collection' in the IGC UI.
     */
    protected String data_collection;

    /**
     * The 'data_connection' property, displayed as 'Data Connection' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DsdataConnection} object.
     */
    protected Reference data_connection;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #transformation_project */ @JsonProperty("transformation_project")  public Reference getTransformationProject() { return this.transformation_project; }
    /** @see #transformation_project */ @JsonProperty("transformation_project")  public void setTransformationProject(Reference transformation_project) { this.transformation_project = transformation_project; }

    /** @see #column_definitions */ @JsonProperty("column_definitions")  public ReferenceList getColumnDefinitions() { return this.column_definitions; }
    /** @see #column_definitions */ @JsonProperty("column_definitions")  public void setColumnDefinitions(ReferenceList column_definitions) { this.column_definitions = column_definitions; }

    /** @see #included_by_stages */ @JsonProperty("included_by_stages")  public ReferenceList getIncludedByStages() { return this.included_by_stages; }
    /** @see #included_by_stages */ @JsonProperty("included_by_stages")  public void setIncludedByStages(ReferenceList included_by_stages) { this.included_by_stages = included_by_stages; }

    /** @see #foreign_keys */ @JsonProperty("foreign_keys")  public ReferenceList getForeignKeys() { return this.foreign_keys; }
    /** @see #foreign_keys */ @JsonProperty("foreign_keys")  public void setForeignKeys(ReferenceList foreign_keys) { this.foreign_keys = foreign_keys; }

    /** @see #referenced_by_foreign_keys */ @JsonProperty("referenced_by_foreign_keys")  public ReferenceList getReferencedByForeignKeys() { return this.referenced_by_foreign_keys; }
    /** @see #referenced_by_foreign_keys */ @JsonProperty("referenced_by_foreign_keys")  public void setReferencedByForeignKeys(ReferenceList referenced_by_foreign_keys) { this.referenced_by_foreign_keys = referenced_by_foreign_keys; }

    /** @see #data_source_type */ @JsonProperty("data_source_type")  public String getDataSourceType() { return this.data_source_type; }
    /** @see #data_source_type */ @JsonProperty("data_source_type")  public void setDataSourceType(String data_source_type) { this.data_source_type = data_source_type; }

    /** @see #data_source_name */ @JsonProperty("data_source_name")  public String getDataSourceName() { return this.data_source_name; }
    /** @see #data_source_name */ @JsonProperty("data_source_name")  public void setDataSourceName(String data_source_name) { this.data_source_name = data_source_name; }

    /** @see #machine_or_platform_type */ @JsonProperty("machine_or_platform_type")  public String getMachineOrPlatformType() { return this.machine_or_platform_type; }
    /** @see #machine_or_platform_type */ @JsonProperty("machine_or_platform_type")  public void setMachineOrPlatformType(String machine_or_platform_type) { this.machine_or_platform_type = machine_or_platform_type; }

    /** @see #mainframe_access_type */ @JsonProperty("mainframe_access_type")  public String getMainframeAccessType() { return this.mainframe_access_type; }
    /** @see #mainframe_access_type */ @JsonProperty("mainframe_access_type")  public void setMainframeAccessType(String mainframe_access_type) { this.mainframe_access_type = mainframe_access_type; }

    /** @see #owner */ @JsonProperty("owner")  public String getOwner() { return this.owner; }
    /** @see #owner */ @JsonProperty("owner")  public void setOwner(String owner) { this.owner = owner; }

    /** @see #referenced_by_data_sources */ @JsonProperty("referenced_by_data_sources")  public Reference getReferencedByDataSources() { return this.referenced_by_data_sources; }
    /** @see #referenced_by_data_sources */ @JsonProperty("referenced_by_data_sources")  public void setReferencedByDataSources(Reference referenced_by_data_sources) { this.referenced_by_data_sources = referenced_by_data_sources; }

    /** @see #table_type */ @JsonProperty("table_type")  public String getTableType() { return this.table_type; }
    /** @see #table_type */ @JsonProperty("table_type")  public void setTableType(String table_type) { this.table_type = table_type; }

    /** @see #computer */ @JsonProperty("computer")  public String getComputer() { return this.computer; }
    /** @see #computer */ @JsonProperty("computer")  public void setComputer(String computer) { this.computer = computer; }

    /** @see #software_product */ @JsonProperty("software_product")  public String getSoftwareProduct() { return this.software_product; }
    /** @see #software_product */ @JsonProperty("software_product")  public void setSoftwareProduct(String software_product) { this.software_product = software_product; }

    /** @see #data_store */ @JsonProperty("data_store")  public String getDataStore() { return this.data_store; }
    /** @see #data_store */ @JsonProperty("data_store")  public void setDataStore(String data_store) { this.data_store = data_store; }

    /** @see #data_schema */ @JsonProperty("data_schema")  public String getDataSchema() { return this.data_schema; }
    /** @see #data_schema */ @JsonProperty("data_schema")  public void setDataSchema(String data_schema) { this.data_schema = data_schema; }

    /** @see #data_collection */ @JsonProperty("data_collection")  public String getDataCollection() { return this.data_collection; }
    /** @see #data_collection */ @JsonProperty("data_collection")  public void setDataCollection(String data_collection) { this.data_collection = data_collection; }

    /** @see #data_connection */ @JsonProperty("data_connection")  public Reference getDataConnection() { return this.data_connection; }
    /** @see #data_connection */ @JsonProperty("data_connection")  public void setDataConnection(Reference data_connection) { this.data_connection = data_connection; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isTableDefinition(Object obj) { return (obj.getClass() == TableDefinition.class); }

}
