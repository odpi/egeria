/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'column_definition' asset type in IGC, displayed as 'Column Definition' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ColumnDefinition extends MainObject {

    public static final String IGC_TYPE_ID = "column_definition";

    /**
     * The 'table_definition' property, displayed as 'Table Definition' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TableDefinition} objects.
     */
    protected ReferenceList table_definition;

    /**
     * The 'used_by_stage_columns' property, displayed as 'Used by Stage Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItem} objects.
     */
    protected ReferenceList used_by_stage_columns;

    /**
     * The 'key' property, displayed as 'Key' in the IGC UI.
     */
    protected Boolean key;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'length' property, displayed as 'Length' in the IGC UI.
     */
    protected Number length;

    /**
     * The 'fraction' property, displayed as 'Fraction' in the IGC UI.
     */
    protected Number fraction;

    /**
     * The 'allow_null_values' property, displayed as 'Allow Null Values' in the IGC UI.
     */
    protected String allow_null_values;

    /**
     * The 'data_item_definition' property, displayed as 'Data Item Definition' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataField} object.
     */
    protected Reference data_item_definition;

    /**
     * The 'apt_field_properties' property, displayed as 'APT Field Properties' in the IGC UI.
     */
    protected String apt_field_properties;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #table_definition */ @JsonProperty("table_definition")  public ReferenceList getTableDefinition() { return this.table_definition; }
    /** @see #table_definition */ @JsonProperty("table_definition")  public void setTableDefinition(ReferenceList table_definition) { this.table_definition = table_definition; }

    /** @see #used_by_stage_columns */ @JsonProperty("used_by_stage_columns")  public ReferenceList getUsedByStageColumns() { return this.used_by_stage_columns; }
    /** @see #used_by_stage_columns */ @JsonProperty("used_by_stage_columns")  public void setUsedByStageColumns(ReferenceList used_by_stage_columns) { this.used_by_stage_columns = used_by_stage_columns; }

    /** @see #key */ @JsonProperty("key")  public Boolean getKey() { return this.key; }
    /** @see #key */ @JsonProperty("key")  public void setKey(Boolean key) { this.key = key; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #length */ @JsonProperty("length")  public Number getLength() { return this.length; }
    /** @see #length */ @JsonProperty("length")  public void setLength(Number length) { this.length = length; }

    /** @see #fraction */ @JsonProperty("fraction")  public Number getFraction() { return this.fraction; }
    /** @see #fraction */ @JsonProperty("fraction")  public void setFraction(Number fraction) { this.fraction = fraction; }

    /** @see #allow_null_values */ @JsonProperty("allow_null_values")  public String getAllowNullValues() { return this.allow_null_values; }
    /** @see #allow_null_values */ @JsonProperty("allow_null_values")  public void setAllowNullValues(String allow_null_values) { this.allow_null_values = allow_null_values; }

    /** @see #data_item_definition */ @JsonProperty("data_item_definition")  public Reference getDataItemDefinition() { return this.data_item_definition; }
    /** @see #data_item_definition */ @JsonProperty("data_item_definition")  public void setDataItemDefinition(Reference data_item_definition) { this.data_item_definition = data_item_definition; }

    /** @see #apt_field_properties */ @JsonProperty("apt_field_properties")  public String getAptFieldProperties() { return this.apt_field_properties; }
    /** @see #apt_field_properties */ @JsonProperty("apt_field_properties")  public void setAptFieldProperties(String apt_field_properties) { this.apt_field_properties = apt_field_properties; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isColumnDefinition(Object obj) { return (obj.getClass() == ColumnDefinition.class); }

}
