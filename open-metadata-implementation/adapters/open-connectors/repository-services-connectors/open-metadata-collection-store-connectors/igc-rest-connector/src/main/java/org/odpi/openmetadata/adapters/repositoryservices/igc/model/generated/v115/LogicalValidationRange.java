/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'logical_validation_range' asset type in IGC, displayed as 'Logical Validation Range' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class LogicalValidationRange extends MainObject {

    public static final String IGC_TYPE_ID = "logical_validation_range";

    /**
     * The 'logical_data_model' property, displayed as 'Logical Data Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LogicalDataModel} object.
     */
    protected Reference logical_data_model;

    /**
     * The 'used_by_entity_attributes' property, displayed as 'Used by Entity Attributes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link EntityAttribute} objects.
     */
    protected ReferenceList used_by_entity_attributes;

    /**
     * The 'maximum_value' property, displayed as 'Maximum Value' in the IGC UI.
     */
    protected String maximum_value;

    /**
     * The 'minimum_value' property, displayed as 'Minimum Value' in the IGC UI.
     */
    protected String minimum_value;


    /** @see #logical_data_model */ @JsonProperty("logical_data_model")  public Reference getLogicalDataModel() { return this.logical_data_model; }
    /** @see #logical_data_model */ @JsonProperty("logical_data_model")  public void setLogicalDataModel(Reference logical_data_model) { this.logical_data_model = logical_data_model; }

    /** @see #used_by_entity_attributes */ @JsonProperty("used_by_entity_attributes")  public ReferenceList getUsedByEntityAttributes() { return this.used_by_entity_attributes; }
    /** @see #used_by_entity_attributes */ @JsonProperty("used_by_entity_attributes")  public void setUsedByEntityAttributes(ReferenceList used_by_entity_attributes) { this.used_by_entity_attributes = used_by_entity_attributes; }

    /** @see #maximum_value */ @JsonProperty("maximum_value")  public String getMaximumValue() { return this.maximum_value; }
    /** @see #maximum_value */ @JsonProperty("maximum_value")  public void setMaximumValue(String maximum_value) { this.maximum_value = maximum_value; }

    /** @see #minimum_value */ @JsonProperty("minimum_value")  public String getMinimumValue() { return this.minimum_value; }
    /** @see #minimum_value */ @JsonProperty("minimum_value")  public void setMinimumValue(String minimum_value) { this.minimum_value = minimum_value; }


    public static final Boolean isLogicalValidationRange(Object obj) { return (obj.getClass() == LogicalValidationRange.class); }

}
