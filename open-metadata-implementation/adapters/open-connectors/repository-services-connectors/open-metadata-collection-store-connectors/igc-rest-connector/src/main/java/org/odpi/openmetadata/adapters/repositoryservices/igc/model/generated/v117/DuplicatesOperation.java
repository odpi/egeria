/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'duplicates_operation' asset type in IGC, displayed as 'Duplicates Operation' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DuplicatesOperation extends MainObject {

    public static final String IGC_TYPE_ID = "duplicates_operation";

    /**
     * The 'infoset' property, displayed as 'InfoSet' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Infoset} object.
     */
    protected Reference infoset;

    /**
     * The 'primary_input' property, displayed as 'Primary Input' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Infoset} object.
     */
    protected Reference primary_input;

    /**
     * The 'secondary_input' property, displayed as 'Secondary Input' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Infoset} object.
     */
    protected Reference secondary_input;


    /** @see #infoset */ @JsonProperty("infoset")  public Reference getInfoset() { return this.infoset; }
    /** @see #infoset */ @JsonProperty("infoset")  public void setInfoset(Reference infoset) { this.infoset = infoset; }

    /** @see #primary_input */ @JsonProperty("primary_input")  public Reference getPrimaryInput() { return this.primary_input; }
    /** @see #primary_input */ @JsonProperty("primary_input")  public void setPrimaryInput(Reference primary_input) { this.primary_input = primary_input; }

    /** @see #secondary_input */ @JsonProperty("secondary_input")  public Reference getSecondaryInput() { return this.secondary_input; }
    /** @see #secondary_input */ @JsonProperty("secondary_input")  public void setSecondaryInput(Reference secondary_input) { this.secondary_input = secondary_input; }


    public static final Boolean isDuplicatesOperation(Object obj) { return (obj.getClass() == DuplicatesOperation.class); }

}
