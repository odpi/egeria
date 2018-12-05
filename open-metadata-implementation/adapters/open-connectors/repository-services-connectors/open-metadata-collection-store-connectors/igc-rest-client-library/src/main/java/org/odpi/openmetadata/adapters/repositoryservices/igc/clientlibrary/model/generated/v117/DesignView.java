/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'design_view' asset type in IGC, displayed as 'Design View' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DesignView extends MainObject {

    public static final String IGC_TYPE_ID = "design_view";

    /**
     * The 'physical_data_model' property, displayed as 'Physical Data Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link PhysicalDataModel} object.
     */
    protected Reference physical_data_model;

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
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #physical_data_model */ @JsonProperty("physical_data_model")  public Reference getPhysicalDataModel() { return this.physical_data_model; }
    /** @see #physical_data_model */ @JsonProperty("physical_data_model")  public void setPhysicalDataModel(Reference physical_data_model) { this.physical_data_model = physical_data_model; }

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

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isDesignView(Object obj) { return (obj.getClass() == DesignView.class); }

}
