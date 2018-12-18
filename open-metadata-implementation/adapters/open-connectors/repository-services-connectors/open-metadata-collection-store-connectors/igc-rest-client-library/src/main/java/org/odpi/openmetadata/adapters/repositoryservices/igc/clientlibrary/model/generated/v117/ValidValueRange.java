/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'valid_value_range' asset type in IGC, displayed as 'Valid Value Range' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueRange extends Reference {

    public static String getIgcTypeId() { return "valid_value_range"; }

    /**
     * The 'is_max_inclusive' property, displayed as 'Is Max Inclusive' in the IGC UI.
     */
    protected Boolean is_max_inclusive;

    /**
     * The 'minimum_value' property, displayed as 'Minimum Value' in the IGC UI.
     */
    protected String minimum_value;

    /**
     * The 'is_min_inclusive' property, displayed as 'Is Min Inclusive' in the IGC UI.
     */
    protected Boolean is_min_inclusive;

    /**
     * The 'maximum_value' property, displayed as 'Maximum Value' in the IGC UI.
     */
    protected String maximum_value;

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'valid_value_list' property, displayed as 'Valid Value List' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link ValidValueList} object.
     */
    protected Reference valid_value_list;

    /**
     * The 'design_column' property, displayed as 'Design Column' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItem} objects.
     */
    protected ReferenceList design_column;


    /** @see #is_max_inclusive */ @JsonProperty("is_max_inclusive")  public Boolean getIsMaxInclusive() { return this.is_max_inclusive; }
    /** @see #is_max_inclusive */ @JsonProperty("is_max_inclusive")  public void setIsMaxInclusive(Boolean is_max_inclusive) { this.is_max_inclusive = is_max_inclusive; }

    /** @see #minimum_value */ @JsonProperty("minimum_value")  public String getMinimumValue() { return this.minimum_value; }
    /** @see #minimum_value */ @JsonProperty("minimum_value")  public void setMinimumValue(String minimum_value) { this.minimum_value = minimum_value; }

    /** @see #is_min_inclusive */ @JsonProperty("is_min_inclusive")  public Boolean getIsMinInclusive() { return this.is_min_inclusive; }
    /** @see #is_min_inclusive */ @JsonProperty("is_min_inclusive")  public void setIsMinInclusive(Boolean is_min_inclusive) { this.is_min_inclusive = is_min_inclusive; }

    /** @see #maximum_value */ @JsonProperty("maximum_value")  public String getMaximumValue() { return this.maximum_value; }
    /** @see #maximum_value */ @JsonProperty("maximum_value")  public void setMaximumValue(String maximum_value) { this.maximum_value = maximum_value; }

    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #valid_value_list */ @JsonProperty("valid_value_list")  public Reference getValidValueList() { return this.valid_value_list; }
    /** @see #valid_value_list */ @JsonProperty("valid_value_list")  public void setValidValueList(Reference valid_value_list) { this.valid_value_list = valid_value_list; }

    /** @see #design_column */ @JsonProperty("design_column")  public ReferenceList getDesignColumn() { return this.design_column; }
    /** @see #design_column */ @JsonProperty("design_column")  public void setDesignColumn(ReferenceList design_column) { this.design_column = design_column; }

    public static final Boolean includesModificationDetails() { return false; }
    public static final Boolean isValidValueRange(Object obj) { return (obj.getClass() == ValidValueRange.class); }

}
