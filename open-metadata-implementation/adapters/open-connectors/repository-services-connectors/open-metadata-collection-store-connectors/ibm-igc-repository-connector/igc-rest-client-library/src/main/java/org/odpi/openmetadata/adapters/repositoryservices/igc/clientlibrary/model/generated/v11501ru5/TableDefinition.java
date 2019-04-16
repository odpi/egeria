/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11501ru5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code table_definition} asset type in IGC, displayed as '{@literal Table Definition}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("table_definition")
public class TableDefinition extends Reference {

    public static String getIgcTypeDisplayName() { return "Table Definition"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code long_description} property, displayed as '{@literal Long Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code transformation_project} property, displayed as '{@literal Transformation Project}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformationProject} object.
     */
    protected Reference transformation_project;

    /**
     * The {@code labels} property, displayed as '{@literal Labels}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The {@code stewards} property, displayed as '{@literal Stewards}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The {@code assigned_to_terms} property, displayed as '{@literal Assigned to Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The {@code implements_rules} property, displayed as '{@literal Implements Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The {@code governed_by_rules} property, displayed as '{@literal Governed by Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The {@code column_definitions} property, displayed as '{@literal Column Definitions}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ColumnDefinition} objects.
     */
    protected ReferenceList column_definitions;

    /**
     * The {@code included_by_stages} property, displayed as '{@literal Included by Stages}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList included_by_stages;

    /**
     * The {@code foreign_keys} property, displayed as '{@literal Foreign Keys}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ForeignKeyDefinition} objects.
     */
    protected ReferenceList foreign_keys;

    /**
     * The {@code referenced_by_foreign_keys} property, displayed as '{@literal Referenced by Foreign Keys}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ForeignKeyDefinition} objects.
     */
    protected ReferenceList referenced_by_foreign_keys;

    /**
     * The {@code data_source_type} property, displayed as '{@literal Data Source Type}' in the IGC UI.
     */
    protected String data_source_type;

    /**
     * The {@code data_source_name} property, displayed as '{@literal Data Source Name}' in the IGC UI.
     */
    protected String data_source_name;

    /**
     * The {@code machine_or_platform_type} property, displayed as '{@literal Machine or Platform Type}' in the IGC UI.
     */
    protected String machine_or_platform_type;

    /**
     * The {@code mainframe_access_type} property, displayed as '{@literal Mainframe Access Type}' in the IGC UI.
     */
    protected String mainframe_access_type;

    /**
     * The {@code owner} property, displayed as '{@literal Owner}' in the IGC UI.
     */
    protected String owner;

    /**
     * The {@code referenced_by_data_sources} property, displayed as '{@literal Referenced by Data Sources}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Datagroup} object.
     */
    protected Reference referenced_by_data_sources;

    /**
     * The {@code table_type} property, displayed as '{@literal Table Type}' in the IGC UI.
     */
    protected String table_type;

    /**
     * The {@code computer} property, displayed as '{@literal Computer}' in the IGC UI.
     */
    protected String computer;

    /**
     * The {@code software_product} property, displayed as '{@literal Software Product}' in the IGC UI.
     */
    protected String software_product;

    /**
     * The {@code data_store} property, displayed as '{@literal Data Store}' in the IGC UI.
     */
    protected String data_store;

    /**
     * The {@code data_schema} property, displayed as '{@literal Data Schema}' in the IGC UI.
     */
    protected String data_schema;

    /**
     * The {@code data_collection} property, displayed as '{@literal Data Collection}' in the IGC UI.
     */
    protected String data_collection;

    /**
     * The {@code data_connection} property, displayed as '{@literal Data Connection}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DsdataConnection} object.
     */
    protected Reference data_connection;

    /**
     * The {@code blueprint_elements} property, displayed as '{@literal Blueprint Elements}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BlueprintElementLink} objects.
     */
    protected ReferenceList blueprint_elements;

    /**
     * The {@code in_collections} property, displayed as '{@literal In Collections}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

    /**
     * The {@code created_by} property, displayed as '{@literal Created By}' in the IGC UI.
     */
    protected String created_by;

    /**
     * The {@code created_on} property, displayed as '{@literal Created On}' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The {@code modified_by} property, displayed as '{@literal Modified By}' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The {@code modified_on} property, displayed as '{@literal Modified On}' in the IGC UI.
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

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "data_source_type",
        "data_source_name",
        "machine_or_platform_type",
        "mainframe_access_type",
        "owner",
        "table_type",
        "computer",
        "software_product",
        "data_store",
        "data_schema",
        "data_collection",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "data_source_type",
        "data_source_name",
        "machine_or_platform_type",
        "mainframe_access_type",
        "owner",
        "table_type",
        "computer",
        "software_product",
        "data_store",
        "data_schema",
        "data_collection",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "column_definitions",
        "included_by_stages",
        "foreign_keys",
        "referenced_by_foreign_keys",
        "blueprint_elements",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "transformation_project",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "column_definitions",
        "included_by_stages",
        "foreign_keys",
        "referenced_by_foreign_keys",
        "data_source_type",
        "data_source_name",
        "machine_or_platform_type",
        "mainframe_access_type",
        "owner",
        "referenced_by_data_sources",
        "table_type",
        "computer",
        "software_product",
        "data_store",
        "data_schema",
        "data_collection",
        "data_connection",
        "blueprint_elements",
        "in_collections",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isTableDefinition(Object obj) { return (obj.getClass() == TableDefinition.class); }

}
