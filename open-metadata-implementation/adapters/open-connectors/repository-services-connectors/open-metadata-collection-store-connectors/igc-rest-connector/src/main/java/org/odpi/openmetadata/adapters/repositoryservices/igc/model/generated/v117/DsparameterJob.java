/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'dsparameter_job' asset type in IGC, displayed as 'Parameter' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DsparameterJob extends MainObject {

    public static final String IGC_TYPE_ID = "dsparameter_job";

    /**
     * The 'of_job_def' property, displayed as 'Job' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Jobdef} object.
     */
    protected Reference of_job_def;

    /**
     * The 'display_caption' property, displayed as 'Display Caption' in the IGC UI.
     */
    protected String display_caption;

    /**
     * The 'default_value' property, displayed as 'Default Value' in the IGC UI.
     */
    protected ArrayList<String> default_value;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     */
    protected String type;


    /** @see #of_job_def */ @JsonProperty("of_job_def")  public Reference getOfJobDef() { return this.of_job_def; }
    /** @see #of_job_def */ @JsonProperty("of_job_def")  public void setOfJobDef(Reference of_job_def) { this.of_job_def = of_job_def; }

    /** @see #display_caption */ @JsonProperty("display_caption")  public String getDisplayCaption() { return this.display_caption; }
    /** @see #display_caption */ @JsonProperty("display_caption")  public void setDisplayCaption(String display_caption) { this.display_caption = display_caption; }

    /** @see #default_value */ @JsonProperty("default_value")  public ArrayList<String> getDefaultValue() { return this.default_value; }
    /** @see #default_value */ @JsonProperty("default_value")  public void setDefaultValue(ArrayList<String> default_value) { this.default_value = default_value; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }


    public static final Boolean isDsparameterJob(Object obj) { return (obj.getClass() == DsparameterJob.class); }

}
