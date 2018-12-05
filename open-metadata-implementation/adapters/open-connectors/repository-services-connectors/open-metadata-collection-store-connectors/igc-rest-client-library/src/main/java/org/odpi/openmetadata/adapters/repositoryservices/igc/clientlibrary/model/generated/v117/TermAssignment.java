/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'term_assignment' asset type in IGC, displayed as 'Suggested Term Assignment' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermAssignment extends MainObject {

    public static final String IGC_TYPE_ID = "term_assignment";

    /**
     * The 'assign_to_term' property, displayed as 'Term' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference assign_to_term;

    /**
     * The 'assign_to_object' property, displayed as 'Asset' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationAsset} object.
     */
    protected Reference assign_to_object;

    /**
     * The 'confidence_percent' property, displayed as 'Confidence (Percent)' in the IGC UI.
     */
    protected Number confidence_percent;


    /** @see #assign_to_term */ @JsonProperty("assign_to_term")  public Reference getAssignToTerm() { return this.assign_to_term; }
    /** @see #assign_to_term */ @JsonProperty("assign_to_term")  public void setAssignToTerm(Reference assign_to_term) { this.assign_to_term = assign_to_term; }

    /** @see #assign_to_object */ @JsonProperty("assign_to_object")  public Reference getAssignToObject() { return this.assign_to_object; }
    /** @see #assign_to_object */ @JsonProperty("assign_to_object")  public void setAssignToObject(Reference assign_to_object) { this.assign_to_object = assign_to_object; }

    /** @see #confidence_percent */ @JsonProperty("confidence_percent")  public Number getConfidencePercent() { return this.confidence_percent; }
    /** @see #confidence_percent */ @JsonProperty("confidence_percent")  public void setConfidencePercent(Number confidence_percent) { this.confidence_percent = confidence_percent; }


    public static final Boolean isTermAssignment(Object obj) { return (obj.getClass() == TermAssignment.class); }

}
