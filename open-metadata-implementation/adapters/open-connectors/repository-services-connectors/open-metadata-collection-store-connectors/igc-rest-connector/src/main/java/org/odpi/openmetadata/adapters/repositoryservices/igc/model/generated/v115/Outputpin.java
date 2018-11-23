/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'outputpin' asset type in IGC, displayed as 'OutputPin' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Outputpin extends MainObject {

    public static final String IGC_TYPE_ID = "outputpin";

    /**
     * The 'of_job_component' property, displayed as 'Of Job Component' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference of_job_component;

    /**
     * The 'is_source_of_link' property, displayed as 'Is Source Of Link' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference is_source_of_link;

    /**
     * The 'sequence' property, displayed as 'Sequence' in the IGC UI.
     */
    protected Number sequence;


    /** @see #of_job_component */ @JsonProperty("of_job_component")  public Reference getOfJobComponent() { return this.of_job_component; }
    /** @see #of_job_component */ @JsonProperty("of_job_component")  public void setOfJobComponent(Reference of_job_component) { this.of_job_component = of_job_component; }

    /** @see #is_source_of_link */ @JsonProperty("is_source_of_link")  public Reference getIsSourceOfLink() { return this.is_source_of_link; }
    /** @see #is_source_of_link */ @JsonProperty("is_source_of_link")  public void setIsSourceOfLink(Reference is_source_of_link) { this.is_source_of_link = is_source_of_link; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }


    public static final Boolean isOutputpin(Object obj) { return (obj.getClass() == Outputpin.class); }

}
