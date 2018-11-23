/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'job' asset type in IGC, displayed as 'Job' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Job extends MainObject {

    public static final String IGC_TYPE_ID = "job";

    /**
     * The 'host_system' property, displayed as 'Host System' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Host} object.
     */
    protected Reference host_system;

    /**
     * The 'has_job_def' property, displayed as 'Has Job Def' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Jobdef} object.
     */
    protected Reference has_job_def;

    /**
     * The 'release_status' property, displayed as 'Release Status' in the IGC UI.
     */
    protected String release_status;


    /** @see #host_system */ @JsonProperty("host_system")  public Reference getHostSystem() { return this.host_system; }
    /** @see #host_system */ @JsonProperty("host_system")  public void setHostSystem(Reference host_system) { this.host_system = host_system; }

    /** @see #has_job_def */ @JsonProperty("has_job_def")  public Reference getHasJobDef() { return this.has_job_def; }
    /** @see #has_job_def */ @JsonProperty("has_job_def")  public void setHasJobDef(Reference has_job_def) { this.has_job_def = has_job_def; }

    /** @see #release_status */ @JsonProperty("release_status")  public String getReleaseStatus() { return this.release_status; }
    /** @see #release_status */ @JsonProperty("release_status")  public void setReleaseStatus(String release_status) { this.release_status = release_status; }


    public static final Boolean isJob(Object obj) { return (obj.getClass() == Job.class); }

}
