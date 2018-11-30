/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'valid_value_list' asset type in IGC, displayed as 'Valid Value List' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueList extends MainObject {

    public static final String IGC_TYPE_ID = "valid_value_list";

    /**
     * The 'valid_values' property, displayed as 'Valid Values' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItemValue} objects.
     */
    protected ReferenceList valid_values;

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


    /** @see #valid_values */ @JsonProperty("valid_values")  public ReferenceList getValidValues() { return this.valid_values; }
    /** @see #valid_values */ @JsonProperty("valid_values")  public void setValidValues(ReferenceList valid_values) { this.valid_values = valid_values; }

    /** @see #valid_value_list */ @JsonProperty("valid_value_list")  public Reference getValidValueList() { return this.valid_value_list; }
    /** @see #valid_value_list */ @JsonProperty("valid_value_list")  public void setValidValueList(Reference valid_value_list) { this.valid_value_list = valid_value_list; }

    /** @see #design_column */ @JsonProperty("design_column")  public ReferenceList getDesignColumn() { return this.design_column; }
    /** @see #design_column */ @JsonProperty("design_column")  public void setDesignColumn(ReferenceList design_column) { this.design_column = design_column; }


    public static final Boolean isValidValueList(Object obj) { return (obj.getClass() == ValidValueList.class); }

}
