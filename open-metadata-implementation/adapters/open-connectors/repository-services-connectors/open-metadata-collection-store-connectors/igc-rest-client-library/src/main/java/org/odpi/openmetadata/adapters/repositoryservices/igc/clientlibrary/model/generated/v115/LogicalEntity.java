/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'logical_entity' asset type in IGC, displayed as 'Logical Entity' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class LogicalEntity extends MainObject {

    public static final String IGC_TYPE_ID = "logical_entity";

    /**
     * The 'logical_data_model' property, displayed as 'Logical Data Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LogicalDataModel} object.
     */
    protected Reference logical_data_model;

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


    /** @see #logical_data_model */ @JsonProperty("logical_data_model")  public Reference getLogicalDataModel() { return this.logical_data_model; }
    /** @see #logical_data_model */ @JsonProperty("logical_data_model")  public void setLogicalDataModel(Reference logical_data_model) { this.logical_data_model = logical_data_model; }

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


    public static final Boolean isLogicalEntity(Object obj) { return (obj.getClass() == LogicalEntity.class); }

}
