/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'array' asset type in IGC, displayed as 'Array' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Array extends MainObject {

    public static final String IGC_TYPE_ID = "array";

    /**
     * The 'next_array' property, displayed as 'Next Array' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Array} object.
     */
    protected Reference next_array;

    /**
     * The 'lower_bound' property, displayed as 'Lower Bound' in the IGC UI.
     */
    protected Number lower_bound;

    /**
     * The 'previous_array' property, displayed as 'Previous Array' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Array} object.
     */
    protected Reference previous_array;

    /**
     * The 'belonging_to_data_item' property, displayed as 'Belonging to Data Item' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataItem} object.
     */
    protected Reference belonging_to_data_item;

    /**
     * The 'maximum_size' property, displayed as 'Maximum Size' in the IGC UI.
     */
    protected Number maximum_size;

    /**
     * The 'minimum_size' property, displayed as 'Minimum Size' in the IGC UI.
     */
    protected Number minimum_size;


    /** @see #next_array */ @JsonProperty("next_array")  public Reference getNextArray() { return this.next_array; }
    /** @see #next_array */ @JsonProperty("next_array")  public void setNextArray(Reference next_array) { this.next_array = next_array; }

    /** @see #lower_bound */ @JsonProperty("lower_bound")  public Number getLowerBound() { return this.lower_bound; }
    /** @see #lower_bound */ @JsonProperty("lower_bound")  public void setLowerBound(Number lower_bound) { this.lower_bound = lower_bound; }

    /** @see #previous_array */ @JsonProperty("previous_array")  public Reference getPreviousArray() { return this.previous_array; }
    /** @see #previous_array */ @JsonProperty("previous_array")  public void setPreviousArray(Reference previous_array) { this.previous_array = previous_array; }

    /** @see #belonging_to_data_item */ @JsonProperty("belonging_to_data_item")  public Reference getBelongingToDataItem() { return this.belonging_to_data_item; }
    /** @see #belonging_to_data_item */ @JsonProperty("belonging_to_data_item")  public void setBelongingToDataItem(Reference belonging_to_data_item) { this.belonging_to_data_item = belonging_to_data_item; }

    /** @see #maximum_size */ @JsonProperty("maximum_size")  public Number getMaximumSize() { return this.maximum_size; }
    /** @see #maximum_size */ @JsonProperty("maximum_size")  public void setMaximumSize(Number maximum_size) { this.maximum_size = maximum_size; }

    /** @see #minimum_size */ @JsonProperty("minimum_size")  public Number getMinimumSize() { return this.minimum_size; }
    /** @see #minimum_size */ @JsonProperty("minimum_size")  public void setMinimumSize(Number minimum_size) { this.minimum_size = minimum_size; }


    public static final Boolean isArray(Object obj) { return (obj.getClass() == Array.class); }

}
