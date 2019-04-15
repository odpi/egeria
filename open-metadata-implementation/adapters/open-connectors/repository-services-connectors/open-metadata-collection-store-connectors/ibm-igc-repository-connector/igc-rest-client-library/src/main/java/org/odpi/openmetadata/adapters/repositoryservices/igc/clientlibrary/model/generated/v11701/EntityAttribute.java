/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701;

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
 * POJO for the {@code entity_attribute} asset type in IGC, displayed as '{@literal Entity Attribute}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("entity_attribute")
public class EntityAttribute extends Reference {

    public static String getIgcTypeDisplayName() { return "Entity Attribute"; }

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
     * The {@code logical_entity} property, displayed as '{@literal Logical Entity}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LogicalEntity} object.
     */
    protected Reference logical_entity;

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
     * The {@code implemented_by_design_columns} property, displayed as '{@literal Implemented by Design Columns}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DesignColumn} objects.
     */
    protected ReferenceList implemented_by_design_columns;

    /**
     * The {@code implemented_by_database_columns} property, displayed as '{@literal Implemented by Database Columns}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList implemented_by_database_columns;

    /**
     * The {@code primary_key} property, displayed as '{@literal Primary Key}' in the IGC UI.
     */
    protected Boolean primary_key;

    /**
     * The {@code logical_keys} property, displayed as '{@literal Logical Keys}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalKey} objects.
     */
    protected ReferenceList logical_keys;

    /**
     * The {@code parent_logical_foreignKey} property, displayed as '{@literal Parent Logical Foreign Key}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalForeignKey} objects.
     */
    protected ReferenceList parent_logical_foreignKey;

    /**
     * The {@code child_logical_foreign_keys} property, displayed as '{@literal Child Logical Foreign Keys}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link EntityAttribute} objects.
     */
    protected ReferenceList child_logical_foreign_keys;

    /**
     * The {@code validation_rule} property, displayed as '{@literal Validation Rule}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LogicalValidationRule} object.
     */
    protected Reference validation_rule;

    /**
     * The {@code validation_range} property, displayed as '{@literal Validation Range}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LogicalValidationRange} object.
     */
    protected Reference validation_range;

    /**
     * The {@code validation_list} property, displayed as '{@literal Validation List}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LogicalValidationList} object.
     */
    protected Reference validation_list;

    /**
     * The {@code physical_name} property, displayed as '{@literal Physical Name}' in the IGC UI.
     */
    protected String physical_name;

    /**
     * The {@code native_type} property, displayed as '{@literal Native Type}' in the IGC UI.
     */
    protected String native_type;

    /**
     * The {@code data_type} property, displayed as '{@literal Data Type}' in the IGC UI.
     */
    protected String data_type;

    /**
     * The {@code length} property, displayed as '{@literal Length}' in the IGC UI.
     */
    protected Number length;

    /**
     * The {@code scale} property, displayed as '{@literal Scale}' in the IGC UI.
     */
    protected Number scale;

    /**
     * The {@code required} property, displayed as '{@literal Required}' in the IGC UI.
     */
    protected Boolean required;

    /**
     * The {@code sequence} property, displayed as '{@literal Sequence}' in the IGC UI.
     */
    protected Number sequence;

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

    /** @see #logical_entity */ @JsonProperty("logical_entity")  public Reference getLogicalEntity() { return this.logical_entity; }
    /** @see #logical_entity */ @JsonProperty("logical_entity")  public void setLogicalEntity(Reference logical_entity) { this.logical_entity = logical_entity; }

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

    /** @see #implemented_by_design_columns */ @JsonProperty("implemented_by_design_columns")  public ReferenceList getImplementedByDesignColumns() { return this.implemented_by_design_columns; }
    /** @see #implemented_by_design_columns */ @JsonProperty("implemented_by_design_columns")  public void setImplementedByDesignColumns(ReferenceList implemented_by_design_columns) { this.implemented_by_design_columns = implemented_by_design_columns; }

    /** @see #implemented_by_database_columns */ @JsonProperty("implemented_by_database_columns")  public ReferenceList getImplementedByDatabaseColumns() { return this.implemented_by_database_columns; }
    /** @see #implemented_by_database_columns */ @JsonProperty("implemented_by_database_columns")  public void setImplementedByDatabaseColumns(ReferenceList implemented_by_database_columns) { this.implemented_by_database_columns = implemented_by_database_columns; }

    /** @see #primary_key */ @JsonProperty("primary_key")  public Boolean getPrimaryKey() { return this.primary_key; }
    /** @see #primary_key */ @JsonProperty("primary_key")  public void setPrimaryKey(Boolean primary_key) { this.primary_key = primary_key; }

    /** @see #logical_keys */ @JsonProperty("logical_keys")  public ReferenceList getLogicalKeys() { return this.logical_keys; }
    /** @see #logical_keys */ @JsonProperty("logical_keys")  public void setLogicalKeys(ReferenceList logical_keys) { this.logical_keys = logical_keys; }

    /** @see #parent_logical_foreignKey */ @JsonProperty("parent_logical_foreignKey")  public ReferenceList getParentLogicalForeignkey() { return this.parent_logical_foreignKey; }
    /** @see #parent_logical_foreignKey */ @JsonProperty("parent_logical_foreignKey")  public void setParentLogicalForeignkey(ReferenceList parent_logical_foreignKey) { this.parent_logical_foreignKey = parent_logical_foreignKey; }

    /** @see #child_logical_foreign_keys */ @JsonProperty("child_logical_foreign_keys")  public ReferenceList getChildLogicalForeignKeys() { return this.child_logical_foreign_keys; }
    /** @see #child_logical_foreign_keys */ @JsonProperty("child_logical_foreign_keys")  public void setChildLogicalForeignKeys(ReferenceList child_logical_foreign_keys) { this.child_logical_foreign_keys = child_logical_foreign_keys; }

    /** @see #validation_rule */ @JsonProperty("validation_rule")  public Reference getValidationRule() { return this.validation_rule; }
    /** @see #validation_rule */ @JsonProperty("validation_rule")  public void setValidationRule(Reference validation_rule) { this.validation_rule = validation_rule; }

    /** @see #validation_range */ @JsonProperty("validation_range")  public Reference getValidationRange() { return this.validation_range; }
    /** @see #validation_range */ @JsonProperty("validation_range")  public void setValidationRange(Reference validation_range) { this.validation_range = validation_range; }

    /** @see #validation_list */ @JsonProperty("validation_list")  public Reference getValidationList() { return this.validation_list; }
    /** @see #validation_list */ @JsonProperty("validation_list")  public void setValidationList(Reference validation_list) { this.validation_list = validation_list; }

    /** @see #physical_name */ @JsonProperty("physical_name")  public String getPhysicalName() { return this.physical_name; }
    /** @see #physical_name */ @JsonProperty("physical_name")  public void setPhysicalName(String physical_name) { this.physical_name = physical_name; }

    /** @see #native_type */ @JsonProperty("native_type")  public String getNativeType() { return this.native_type; }
    /** @see #native_type */ @JsonProperty("native_type")  public void setNativeType(String native_type) { this.native_type = native_type; }

    /** @see #data_type */ @JsonProperty("data_type")  public String getDataType() { return this.data_type; }
    /** @see #data_type */ @JsonProperty("data_type")  public void setDataType(String data_type) { this.data_type = data_type; }

    /** @see #length */ @JsonProperty("length")  public Number getLength() { return this.length; }
    /** @see #length */ @JsonProperty("length")  public void setLength(Number length) { this.length = length; }

    /** @see #scale */ @JsonProperty("scale")  public Number getScale() { return this.scale; }
    /** @see #scale */ @JsonProperty("scale")  public void setScale(Number scale) { this.scale = scale; }

    /** @see #required */ @JsonProperty("required")  public Boolean getRequired() { return this.required; }
    /** @see #required */ @JsonProperty("required")  public void setRequired(Boolean required) { this.required = required; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }

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
        "primary_key",
        "physical_name",
        "native_type",
        "data_type",
        "length",
        "scale",
        "required",
        "sequence",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "physical_name",
        "native_type",
        "data_type",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "implemented_by_design_columns",
        "implemented_by_database_columns",
        "logical_keys",
        "parent_logical_foreignKey",
        "child_logical_foreign_keys",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "logical_entity",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "implemented_by_design_columns",
        "implemented_by_database_columns",
        "primary_key",
        "logical_keys",
        "parent_logical_foreignKey",
        "child_logical_foreign_keys",
        "validation_rule",
        "validation_range",
        "validation_list",
        "physical_name",
        "native_type",
        "data_type",
        "length",
        "scale",
        "required",
        "sequence",
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
    public static Boolean isEntityAttribute(Object obj) { return (obj.getClass() == EntityAttribute.class); }

}
