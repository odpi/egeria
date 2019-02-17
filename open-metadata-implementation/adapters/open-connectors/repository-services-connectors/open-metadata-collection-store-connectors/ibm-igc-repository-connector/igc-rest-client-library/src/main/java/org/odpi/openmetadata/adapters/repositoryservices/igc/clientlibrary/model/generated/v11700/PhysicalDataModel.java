/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11700;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code physical_data_model} asset type in IGC, displayed as '{@literal Physical Data Model}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PhysicalDataModel extends Reference {

    public static String getIgcTypeId() { return "physical_data_model"; }
    public static String getIgcTypeDisplayName() { return "Physical Data Model"; }

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
     * The {@code physical_model} property, displayed as '{@literal Physical Model}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link PhysicalDataModel} object.
     */
    protected Reference physical_model;

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
     * The {@code contains_physical_models} property, displayed as '{@literal Contains Physical Models}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link PhysicalDataModel} objects.
     */
    protected ReferenceList contains_physical_models;

    /**
     * The {@code implements_logical_data_models} property, displayed as '{@literal Implements Logical Data Models}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalDataModel} objects.
     */
    protected ReferenceList implements_logical_data_models;

    /**
     * The {@code implemented_by_data_files} property, displayed as '{@literal Implemented By Data Files}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList implemented_by_data_files;

    /**
     * The {@code implemented_by_database_schemas} property, displayed as '{@literal Implemented by Database Schemas}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseSchema} objects.
     */
    protected ReferenceList implemented_by_database_schemas;

    /**
     * The {@code contains_design_tables} property, displayed as '{@literal Contains Design Tables}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DesignTable} objects.
     */
    protected ReferenceList contains_design_tables;

    /**
     * The {@code contains_design_views} property, displayed as '{@literal Contains Design Views}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DesignView} objects.
     */
    protected ReferenceList contains_design_views;

    /**
     * The {@code contains_design_stored_procedures} property, displayed as '{@literal Contains Design Stored Procedures}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DesignStoredProcedure} objects.
     */
    protected ReferenceList contains_design_stored_procedures;

    /**
     * The {@code alias_(business_name)} property, displayed as '{@literal Alias (Business Name)}' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The {@code author} property, displayed as '{@literal Author}' in the IGC UI.
     */
    protected String author;

    /**
     * The {@code target_database} property, displayed as '{@literal Target Database}' in the IGC UI.
     */
    protected String target_database;

    /**
     * The {@code target_dbms} property, displayed as '{@literal Target DBMS}' in the IGC UI.
     */
    protected String target_dbms;

    /**
     * The {@code native_id} property, displayed as '{@literal Native ID}' in the IGC UI.
     */
    protected String native_id;

    /**
     * The {@code package} property, displayed as '{@literal Package}' in the IGC UI.
     */
    @JsonProperty("package") protected String __package;

    /**
     * The {@code namespace} property, displayed as '{@literal Namespace}' in the IGC UI.
     */
    protected String namespace;

    /**
     * The {@code imported_from} property, displayed as '{@literal Imported From}' in the IGC UI.
     */
    protected String imported_from;

    /**
     * The {@code physical_domains} property, displayed as '{@literal Physical Domains}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link PhysicalDomain} objects.
     */
    protected ReferenceList physical_domains;

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

    /** @see #physical_model */ @JsonProperty("physical_model")  public Reference getPhysicalModel() { return this.physical_model; }
    /** @see #physical_model */ @JsonProperty("physical_model")  public void setPhysicalModel(Reference physical_model) { this.physical_model = physical_model; }

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

    /** @see #contains_physical_models */ @JsonProperty("contains_physical_models")  public ReferenceList getContainsPhysicalModels() { return this.contains_physical_models; }
    /** @see #contains_physical_models */ @JsonProperty("contains_physical_models")  public void setContainsPhysicalModels(ReferenceList contains_physical_models) { this.contains_physical_models = contains_physical_models; }

    /** @see #implements_logical_data_models */ @JsonProperty("implements_logical_data_models")  public ReferenceList getImplementsLogicalDataModels() { return this.implements_logical_data_models; }
    /** @see #implements_logical_data_models */ @JsonProperty("implements_logical_data_models")  public void setImplementsLogicalDataModels(ReferenceList implements_logical_data_models) { this.implements_logical_data_models = implements_logical_data_models; }

    /** @see #implemented_by_data_files */ @JsonProperty("implemented_by_data_files")  public ReferenceList getImplementedByDataFiles() { return this.implemented_by_data_files; }
    /** @see #implemented_by_data_files */ @JsonProperty("implemented_by_data_files")  public void setImplementedByDataFiles(ReferenceList implemented_by_data_files) { this.implemented_by_data_files = implemented_by_data_files; }

    /** @see #implemented_by_database_schemas */ @JsonProperty("implemented_by_database_schemas")  public ReferenceList getImplementedByDatabaseSchemas() { return this.implemented_by_database_schemas; }
    /** @see #implemented_by_database_schemas */ @JsonProperty("implemented_by_database_schemas")  public void setImplementedByDatabaseSchemas(ReferenceList implemented_by_database_schemas) { this.implemented_by_database_schemas = implemented_by_database_schemas; }

    /** @see #contains_design_tables */ @JsonProperty("contains_design_tables")  public ReferenceList getContainsDesignTables() { return this.contains_design_tables; }
    /** @see #contains_design_tables */ @JsonProperty("contains_design_tables")  public void setContainsDesignTables(ReferenceList contains_design_tables) { this.contains_design_tables = contains_design_tables; }

    /** @see #contains_design_views */ @JsonProperty("contains_design_views")  public ReferenceList getContainsDesignViews() { return this.contains_design_views; }
    /** @see #contains_design_views */ @JsonProperty("contains_design_views")  public void setContainsDesignViews(ReferenceList contains_design_views) { this.contains_design_views = contains_design_views; }

    /** @see #contains_design_stored_procedures */ @JsonProperty("contains_design_stored_procedures")  public ReferenceList getContainsDesignStoredProcedures() { return this.contains_design_stored_procedures; }
    /** @see #contains_design_stored_procedures */ @JsonProperty("contains_design_stored_procedures")  public void setContainsDesignStoredProcedures(ReferenceList contains_design_stored_procedures) { this.contains_design_stored_procedures = contains_design_stored_procedures; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #author */ @JsonProperty("author")  public String getAuthor() { return this.author; }
    /** @see #author */ @JsonProperty("author")  public void setAuthor(String author) { this.author = author; }

    /** @see #target_database */ @JsonProperty("target_database")  public String getTargetDatabase() { return this.target_database; }
    /** @see #target_database */ @JsonProperty("target_database")  public void setTargetDatabase(String target_database) { this.target_database = target_database; }

    /** @see #target_dbms */ @JsonProperty("target_dbms")  public String getTargetDbms() { return this.target_dbms; }
    /** @see #target_dbms */ @JsonProperty("target_dbms")  public void setTargetDbms(String target_dbms) { this.target_dbms = target_dbms; }

    /** @see #native_id */ @JsonProperty("native_id")  public String getNativeId() { return this.native_id; }
    /** @see #native_id */ @JsonProperty("native_id")  public void setNativeId(String native_id) { this.native_id = native_id; }

    /** @see #__package */ @JsonProperty("package")  public String getPackage() { return this.__package; }
    /** @see #__package */ @JsonProperty("package")  public void setPackage(String __package) { this.__package = __package; }

    /** @see #namespace */ @JsonProperty("namespace")  public String getNamespace() { return this.namespace; }
    /** @see #namespace */ @JsonProperty("namespace")  public void setNamespace(String namespace) { this.namespace = namespace; }

    /** @see #imported_from */ @JsonProperty("imported_from")  public String getImportedFrom() { return this.imported_from; }
    /** @see #imported_from */ @JsonProperty("imported_from")  public void setImportedFrom(String imported_from) { this.imported_from = imported_from; }

    /** @see #physical_domains */ @JsonProperty("physical_domains")  public ReferenceList getPhysicalDomains() { return this.physical_domains; }
    /** @see #physical_domains */ @JsonProperty("physical_domains")  public void setPhysicalDomains(ReferenceList physical_domains) { this.physical_domains = physical_domains; }

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
        "alias_(business_name)",
        "author",
        "target_database",
        "target_dbms",
        "native_id",
        "package",
        "namespace",
        "imported_from",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "alias_(business_name)",
        "author",
        "target_database",
        "target_dbms",
        "native_id",
        "package",
        "namespace",
        "imported_from",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "contains_physical_models",
        "implements_logical_data_models",
        "implemented_by_data_files",
        "implemented_by_database_schemas",
        "contains_design_tables",
        "contains_design_views",
        "contains_design_stored_procedures",
        "physical_domains",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "physical_model",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "contains_physical_models",
        "implements_logical_data_models",
        "implemented_by_data_files",
        "implemented_by_database_schemas",
        "contains_design_tables",
        "contains_design_views",
        "contains_design_stored_procedures",
        "alias_(business_name)",
        "author",
        "target_database",
        "target_dbms",
        "native_id",
        "package",
        "namespace",
        "imported_from",
        "physical_domains",
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
    public static Boolean isPhysicalDataModel(Object obj) { return (obj.getClass() == PhysicalDataModel.class); }

}
