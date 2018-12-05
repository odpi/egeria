/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'index_member' asset type in IGC, displayed as 'Index Member' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class IndexMember extends MainObject {

    public static final String IGC_TYPE_ID = "index_member";

    /**
     * The 'uses_data_field' property, displayed as 'Uses Data Field' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataItem} object.
     */
    protected Reference uses_data_field;

    /**
     * The 'of_index' property, displayed as 'Of Index' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DatabaseIndex} object.
     */
    protected Reference of_index;

    /**
     * The 'sequence' property, displayed as 'Sequence' in the IGC UI.
     */
    protected Number sequence;


    /** @see #uses_data_field */ @JsonProperty("uses_data_field")  public Reference getUsesDataField() { return this.uses_data_field; }
    /** @see #uses_data_field */ @JsonProperty("uses_data_field")  public void setUsesDataField(Reference uses_data_field) { this.uses_data_field = uses_data_field; }

    /** @see #of_index */ @JsonProperty("of_index")  public Reference getOfIndex() { return this.of_index; }
    /** @see #of_index */ @JsonProperty("of_index")  public void setOfIndex(Reference of_index) { this.of_index = of_index; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }


    public static final Boolean isIndexMember(Object obj) { return (obj.getClass() == IndexMember.class); }

}
