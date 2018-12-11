/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'logical_entity' asset type in IGC, displayed as 'Logical Entity' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class LogicalEntity extends Reference {

    @JsonIgnore public static final String IGC_TYPE_ID = "logical_entity";

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
     * The 'logical_data_model' property, displayed as 'Logical Data Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LogicalDataModel} object.
     */
    protected Reference logical_data_model;

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
     * The 'implemented_by_design_tables_or_views' property, displayed as 'Implemented by Design Tables or Design Views' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Datagroup} objects.
     */
    protected ReferenceList implemented_by_design_tables_or_views;

    /**
     * The 'implemented_by_database_tables_or_views' property, displayed as 'Implemented by Database Tables or Views' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Datagroup} objects.
     */
    protected ReferenceList implemented_by_database_tables_or_views;

    /**
     * The 'subject_areas' property, displayed as 'Subject Areas' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link SubjectArea} objects.
     */
    protected ReferenceList subject_areas;

    /**
     * The 'entity_attributes' property, displayed as 'Entity Attributes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link EntityAttribute} objects.
     */
    protected ReferenceList entity_attributes;

    /**
     * The 'logical_keys' property, displayed as 'Logical Keys' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalKey} objects.
     */
    protected ReferenceList logical_keys;

    /**
     * The 'logical_inversion_keys' property, displayed as 'Logical Inversion Keys' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalInversionKey} objects.
     */
    protected ReferenceList logical_inversion_keys;

    /**
     * The 'logical_foreign_keys' property, displayed as 'Logical Foreign Keys' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalForeignKey} objects.
     */
    protected ReferenceList logical_foreign_keys;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'physical_name' property, displayed as 'Physical Name' in the IGC UI.
     */
    protected String physical_name;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'subtypes' property, displayed as 'Subtypes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalEntity} objects.
     */
    protected ReferenceList subtypes;

    /**
     * The 'supertypes' property, displayed as 'Supertypes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalEntity} objects.
     */
    protected ReferenceList supertypes;

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

    /** @see #logical_data_model */ @JsonProperty("logical_data_model")  public Reference getLogicalDataModel() { return this.logical_data_model; }
    /** @see #logical_data_model */ @JsonProperty("logical_data_model")  public void setLogicalDataModel(Reference logical_data_model) { this.logical_data_model = logical_data_model; }

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

    /** @see #implemented_by_design_tables_or_views */ @JsonProperty("implemented_by_design_tables_or_views")  public ReferenceList getImplementedByDesignTablesOrViews() { return this.implemented_by_design_tables_or_views; }
    /** @see #implemented_by_design_tables_or_views */ @JsonProperty("implemented_by_design_tables_or_views")  public void setImplementedByDesignTablesOrViews(ReferenceList implemented_by_design_tables_or_views) { this.implemented_by_design_tables_or_views = implemented_by_design_tables_or_views; }

    /** @see #implemented_by_database_tables_or_views */ @JsonProperty("implemented_by_database_tables_or_views")  public ReferenceList getImplementedByDatabaseTablesOrViews() { return this.implemented_by_database_tables_or_views; }
    /** @see #implemented_by_database_tables_or_views */ @JsonProperty("implemented_by_database_tables_or_views")  public void setImplementedByDatabaseTablesOrViews(ReferenceList implemented_by_database_tables_or_views) { this.implemented_by_database_tables_or_views = implemented_by_database_tables_or_views; }

    /** @see #subject_areas */ @JsonProperty("subject_areas")  public ReferenceList getSubjectAreas() { return this.subject_areas; }
    /** @see #subject_areas */ @JsonProperty("subject_areas")  public void setSubjectAreas(ReferenceList subject_areas) { this.subject_areas = subject_areas; }

    /** @see #entity_attributes */ @JsonProperty("entity_attributes")  public ReferenceList getEntityAttributes() { return this.entity_attributes; }
    /** @see #entity_attributes */ @JsonProperty("entity_attributes")  public void setEntityAttributes(ReferenceList entity_attributes) { this.entity_attributes = entity_attributes; }

    /** @see #logical_keys */ @JsonProperty("logical_keys")  public ReferenceList getLogicalKeys() { return this.logical_keys; }
    /** @see #logical_keys */ @JsonProperty("logical_keys")  public void setLogicalKeys(ReferenceList logical_keys) { this.logical_keys = logical_keys; }

    /** @see #logical_inversion_keys */ @JsonProperty("logical_inversion_keys")  public ReferenceList getLogicalInversionKeys() { return this.logical_inversion_keys; }
    /** @see #logical_inversion_keys */ @JsonProperty("logical_inversion_keys")  public void setLogicalInversionKeys(ReferenceList logical_inversion_keys) { this.logical_inversion_keys = logical_inversion_keys; }

    /** @see #logical_foreign_keys */ @JsonProperty("logical_foreign_keys")  public ReferenceList getLogicalForeignKeys() { return this.logical_foreign_keys; }
    /** @see #logical_foreign_keys */ @JsonProperty("logical_foreign_keys")  public void setLogicalForeignKeys(ReferenceList logical_foreign_keys) { this.logical_foreign_keys = logical_foreign_keys; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #physical_name */ @JsonProperty("physical_name")  public String getPhysicalName() { return this.physical_name; }
    /** @see #physical_name */ @JsonProperty("physical_name")  public void setPhysicalName(String physical_name) { this.physical_name = physical_name; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #subtypes */ @JsonProperty("subtypes")  public ReferenceList getSubtypes() { return this.subtypes; }
    /** @see #subtypes */ @JsonProperty("subtypes")  public void setSubtypes(ReferenceList subtypes) { this.subtypes = subtypes; }

    /** @see #supertypes */ @JsonProperty("supertypes")  public ReferenceList getSupertypes() { return this.supertypes; }
    /** @see #supertypes */ @JsonProperty("supertypes")  public void setSupertypes(ReferenceList supertypes) { this.supertypes = supertypes; }

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


    public static final Boolean isLogicalEntity(Object obj) { return (obj.getClass() == LogicalEntity.class); }

}
