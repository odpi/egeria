/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502sp5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'design_view' asset type in IGC, displayed as 'Design View' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DesignView extends Reference {

    public static String getIgcTypeId() { return "design_view"; }
    public static String getIgcTypeDisplayName() { return "Design View"; }

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
     * The 'physical_data_model' property, displayed as 'Physical Data Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link PhysicalDataModel} object.
     */
    protected Reference physical_data_model;

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
     * The 'implements_logical_entities' property, displayed as 'Implements Logical Entities' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalEntity} objects.
     */
    protected ReferenceList implements_logical_entities;

    /**
     * The 'implemented_by_data_file_elements' property, displayed as 'Implemented by Data File Records' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataFileRecord} objects.
     */
    protected ReferenceList implemented_by_data_file_elements;

    /**
     * The 'implemented_by_database_tables_views' property, displayed as 'Implemented by Database Tables or Views' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Datagroup} objects.
     */
    protected ReferenceList implemented_by_database_tables_views;

    /**
     * The 'design_columns' property, displayed as 'Design Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DesignColumn} objects.
     */
    protected ReferenceList design_columns;

    /**
     * The 'design_keys' property, displayed as 'Design Keys' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DesignKey} objects.
     */
    protected ReferenceList design_keys;

    /**
     * The 'design_foreign_keys' property, displayed as 'Design Foreign Keys' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DesignForeignKey} objects.
     */
    protected ReferenceList design_foreign_keys;

    /**
     * The 'referenced_by_foreign_key_design' property, displayed as 'Referenced By Foreign Key Design' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DesignForeignKey} objects.
     */
    protected ReferenceList referenced_by_foreign_key_design;

    /**
     * The 'expression' property, displayed as 'Expression' in the IGC UI.
     */
    protected String expression;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'imported_from' property, displayed as 'Imported From' in the IGC UI.
     */
    protected String imported_from;

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

    /** @see #physical_data_model */ @JsonProperty("physical_data_model")  public Reference getPhysicalDataModel() { return this.physical_data_model; }
    /** @see #physical_data_model */ @JsonProperty("physical_data_model")  public void setPhysicalDataModel(Reference physical_data_model) { this.physical_data_model = physical_data_model; }

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

    /** @see #implements_logical_entities */ @JsonProperty("implements_logical_entities")  public ReferenceList getImplementsLogicalEntities() { return this.implements_logical_entities; }
    /** @see #implements_logical_entities */ @JsonProperty("implements_logical_entities")  public void setImplementsLogicalEntities(ReferenceList implements_logical_entities) { this.implements_logical_entities = implements_logical_entities; }

    /** @see #implemented_by_data_file_elements */ @JsonProperty("implemented_by_data_file_elements")  public ReferenceList getImplementedByDataFileElements() { return this.implemented_by_data_file_elements; }
    /** @see #implemented_by_data_file_elements */ @JsonProperty("implemented_by_data_file_elements")  public void setImplementedByDataFileElements(ReferenceList implemented_by_data_file_elements) { this.implemented_by_data_file_elements = implemented_by_data_file_elements; }

    /** @see #implemented_by_database_tables_views */ @JsonProperty("implemented_by_database_tables_views")  public ReferenceList getImplementedByDatabaseTablesViews() { return this.implemented_by_database_tables_views; }
    /** @see #implemented_by_database_tables_views */ @JsonProperty("implemented_by_database_tables_views")  public void setImplementedByDatabaseTablesViews(ReferenceList implemented_by_database_tables_views) { this.implemented_by_database_tables_views = implemented_by_database_tables_views; }

    /** @see #design_columns */ @JsonProperty("design_columns")  public ReferenceList getDesignColumns() { return this.design_columns; }
    /** @see #design_columns */ @JsonProperty("design_columns")  public void setDesignColumns(ReferenceList design_columns) { this.design_columns = design_columns; }

    /** @see #design_keys */ @JsonProperty("design_keys")  public ReferenceList getDesignKeys() { return this.design_keys; }
    /** @see #design_keys */ @JsonProperty("design_keys")  public void setDesignKeys(ReferenceList design_keys) { this.design_keys = design_keys; }

    /** @see #design_foreign_keys */ @JsonProperty("design_foreign_keys")  public ReferenceList getDesignForeignKeys() { return this.design_foreign_keys; }
    /** @see #design_foreign_keys */ @JsonProperty("design_foreign_keys")  public void setDesignForeignKeys(ReferenceList design_foreign_keys) { this.design_foreign_keys = design_foreign_keys; }

    /** @see #referenced_by_foreign_key_design */ @JsonProperty("referenced_by_foreign_key_design")  public ReferenceList getReferencedByForeignKeyDesign() { return this.referenced_by_foreign_key_design; }
    /** @see #referenced_by_foreign_key_design */ @JsonProperty("referenced_by_foreign_key_design")  public void setReferencedByForeignKeyDesign(ReferenceList referenced_by_foreign_key_design) { this.referenced_by_foreign_key_design = referenced_by_foreign_key_design; }

    /** @see #expression */ @JsonProperty("expression")  public String getExpression() { return this.expression; }
    /** @see #expression */ @JsonProperty("expression")  public void setExpression(String expression) { this.expression = expression; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #imported_from */ @JsonProperty("imported_from")  public String getImportedFrom() { return this.imported_from; }
    /** @see #imported_from */ @JsonProperty("imported_from")  public void setImportedFrom(String imported_from) { this.imported_from = imported_from; }

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
        "expression",
        "alias_(business_name)",
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
        "expression",
        "alias_(business_name)",
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
        "implements_logical_entities",
        "implemented_by_data_file_elements",
        "implemented_by_database_tables_views",
        "design_columns",
        "design_keys",
        "design_foreign_keys",
        "referenced_by_foreign_key_design",
        "blueprint_elements",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "physical_data_model",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "implements_logical_entities",
        "implemented_by_data_file_elements",
        "implemented_by_database_tables_views",
        "design_columns",
        "design_keys",
        "design_foreign_keys",
        "referenced_by_foreign_key_design",
        "expression",
        "alias_(business_name)",
        "imported_from",
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
    public static Boolean isDesignView(Object obj) { return (obj.getClass() == DesignView.class); }

}
