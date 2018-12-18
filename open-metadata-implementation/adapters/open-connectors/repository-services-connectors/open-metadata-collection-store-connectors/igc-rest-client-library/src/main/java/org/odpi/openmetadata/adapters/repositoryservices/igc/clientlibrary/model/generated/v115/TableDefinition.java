/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
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
public class TableDefinition extends Reference {

    public static String getIgcTypeId() { return "table_definition"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'long_description' property, displayed as 'Long Description' in the IGC UI.
     */
    protected String long_description;

    /**
     * The 'transformation_project' property, displayed as 'Transformation Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformationProject} object.
     */
    protected Reference transformation_project;

    /**
     * The 'labels' property, displayed as 'Labels' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The 'stewards' property, displayed as 'Stewards' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The 'assigned_to_terms' property, displayed as 'Assigned to Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The 'implements_rules' property, displayed as 'Implements Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The 'governed_by_rules' property, displayed as 'Governed by Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

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
     * The 'blueprint_elements' property, displayed as 'Blueprint Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BlueprintElementLink} objects.
     */
    protected ReferenceList blueprint_elements;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created On' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Modified On' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #transformation_project */ @JsonProperty("transformation_project")  public Reference getTransformationProject() { return this.transformation_project; }
    /** @see #transformation_project */ @JsonProperty("transformation_project")  public void setTransformationProject(Reference transformation_project) { this.transformation_project = transformation_project; }

    /** @see #labels */ @JsonProperty("labels")  public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels")  public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards")  public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards")  public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #implements_rules */ @JsonProperty("implements_rules")  public ReferenceList getImplementsRules() { return this.implements_rules; }
    /** @see #implements_rules */ @JsonProperty("implements_rules")  public void setImplementsRules(ReferenceList implements_rules) { this.implements_rules = implements_rules; }

    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public ReferenceList getGovernedByRules() { return this.governed_by_rules; }
    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public void setGovernedByRules(ReferenceList governed_by_rules) { this.governed_by_rules = governed_by_rules; }

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

    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public ReferenceList getBlueprintElements() { return this.blueprint_elements; }
    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public void setBlueprintElements(ReferenceList blueprint_elements) { this.blueprint_elements = blueprint_elements; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static final Boolean isTableDefinition(Object obj) { return (obj.getClass() == TableDefinition.class); }

}
