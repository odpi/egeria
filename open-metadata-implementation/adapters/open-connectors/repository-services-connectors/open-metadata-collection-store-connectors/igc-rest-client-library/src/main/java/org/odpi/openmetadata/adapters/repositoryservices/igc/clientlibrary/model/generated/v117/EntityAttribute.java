/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'entity_attribute' asset type in IGC, displayed as 'Entity Attribute' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntityAttribute extends MainObject {

    public static final String IGC_TYPE_ID = "entity_attribute";

    /**
     * The 'logical_entity' property, displayed as 'Logical Entity' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LogicalEntity} object.
     */
    protected Reference logical_entity;

    /**
     * The 'implemented_by_design_columns' property, displayed as 'Implemented by Design Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DesignColumn} objects.
     */
    protected ReferenceList implemented_by_design_columns;

    /**
     * The 'implemented_by_database_columns' property, displayed as 'Implemented by Database Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList implemented_by_database_columns;

    /**
     * The 'primary_key' property, displayed as 'Primary Key' in the IGC UI.
     */
    protected Boolean primary_key;

    /**
     * The 'logical_keys' property, displayed as 'Logical Keys' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalKey} objects.
     */
    protected ReferenceList logical_keys;

    /**
     * The 'parent_logical_foreignKey' property, displayed as 'Parent Logical Foreign Key' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalForeignKey} objects.
     */
    protected ReferenceList parent_logical_foreignKey;

    /**
     * The 'child_logical_foreign_keys' property, displayed as 'Child Logical Foreign Keys' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link EntityAttribute} objects.
     */
    protected ReferenceList child_logical_foreign_keys;

    /**
     * The 'validation_rule' property, displayed as 'Validation Rule' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LogicalValidationRule} object.
     */
    protected Reference validation_rule;

    /**
     * The 'validation_range' property, displayed as 'Validation Range' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LogicalValidationRange} object.
     */
    protected Reference validation_range;

    /**
     * The 'validation_list' property, displayed as 'Validation List' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LogicalValidationList} object.
     */
    protected Reference validation_list;

    /**
     * The 'physical_name' property, displayed as 'Physical Name' in the IGC UI.
     */
    protected String physical_name;

    /**
     * The 'native_type' property, displayed as 'Native Type' in the IGC UI.
     */
    protected String native_type;

    /**
     * The 'data_type' property, displayed as 'Data Type' in the IGC UI.
     */
    protected String data_type;

    /**
     * The 'length' property, displayed as 'Length' in the IGC UI.
     */
    protected Number length;

    /**
     * The 'scale' property, displayed as 'Scale' in the IGC UI.
     */
    protected Number scale;

    /**
     * The 'required' property, displayed as 'Required' in the IGC UI.
     */
    protected Boolean required;

    /**
     * The 'sequence' property, displayed as 'Sequence' in the IGC UI.
     */
    protected Number sequence;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #logical_entity */ @JsonProperty("logical_entity")  public Reference getLogicalEntity() { return this.logical_entity; }
    /** @see #logical_entity */ @JsonProperty("logical_entity")  public void setLogicalEntity(Reference logical_entity) { this.logical_entity = logical_entity; }

    /** @see #implemented_by_design_columns */ @JsonProperty("implemented_by_design_columns")  public ReferenceList getImplementedByDesignColumns() { return this.implemented_by_design_columns; }
    /** @see #implemented_by_design_columns */ @JsonProperty("implemented_by_design_columns")  public void setImplementedByDesignColumns(ReferenceList implemented_by_design_columns) { this.implemented_by_design_columns = implemented_by_design_columns; }

    /** @see #implemented_by_database_columns */ @JsonProperty("implemented_by_database_columns")  public ReferenceList getImplementedByDatabaseColumns() { return this.implemented_by_database_columns; }
    /** @see #implemented_by_database_columns */ @JsonProperty("implemented_by_database_columns")  public void setImplementedByDatabaseColumns(ReferenceList implemented_by_database_columns) { this.implemented_by_database_columns = implemented_by_database_columns; }

    /** @see #primary_key */ @JsonProperty("primary_key")  public Boolean getPrimaryKey() { return this.primary_key; }
    /** @see #primary_key */ @JsonProperty("primary_key")  public void setPrimaryKey(Boolean primary_key) { this.primary_key = primary_key; }

    /** @see #logical_keys */ @JsonProperty("logical_keys")  public ReferenceList getLogicalKeys() { return this.logical_keys; }
    /** @see #logical_keys */ @JsonProperty("logical_keys")  public void setLogicalKeys(ReferenceList logical_keys) { this.logical_keys = logical_keys; }

    /** @see #parent_logical_foreignKey */ @JsonProperty("parent_logical_foreignKey")  public ReferenceList getParentLogicalForeignKey() { return this.parent_logical_foreignKey; }
    /** @see #parent_logical_foreignKey */ @JsonProperty("parent_logical_foreignKey")  public void setParentLogicalForeignKey(ReferenceList parent_logical_foreignKey) { this.parent_logical_foreignKey = parent_logical_foreignKey; }

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


    public static final Boolean isEntityAttribute(Object obj) { return (obj.getClass() == EntityAttribute.class); }

}
