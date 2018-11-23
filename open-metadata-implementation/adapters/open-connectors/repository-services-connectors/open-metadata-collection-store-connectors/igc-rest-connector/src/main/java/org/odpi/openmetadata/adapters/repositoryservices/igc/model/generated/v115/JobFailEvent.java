/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'job_fail_event' asset type in IGC, displayed as 'Job Fail Event' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class JobFailEvent extends MainObject {

    public static final String IGC_TYPE_ID = "job_fail_event";

    /**
     * The 'job_run_activity' property, displayed as 'Job Run Activity' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationAsset} object.
     */
    protected Reference job_run_activity;

    /**
     * The 'time' property, displayed as 'Time' in the IGC UI.
     */
    protected Date time;

    /**
     * The 'message' property, displayed as 'Message' in the IGC UI.
     */
    protected String message;

    /**
     * The 'row_count' property, displayed as 'Row Count' in the IGC UI.
     */
    protected Number row_count;


    /** @see #job_run_activity */ @JsonProperty("job_run_activity")  public Reference getJobRunActivity() { return this.job_run_activity; }
    /** @see #job_run_activity */ @JsonProperty("job_run_activity")  public void setJobRunActivity(Reference job_run_activity) { this.job_run_activity = job_run_activity; }

    /** @see #time */ @JsonProperty("time")  public Date getTime() { return this.time; }
    /** @see #time */ @JsonProperty("time")  public void setTime(Date time) { this.time = time; }

    /** @see #message */ @JsonProperty("message")  public String getMessage() { return this.message; }
    /** @see #message */ @JsonProperty("message")  public void setMessage(String message) { this.message = message; }

    /** @see #row_count */ @JsonProperty("row_count")  public Number getRowCount() { return this.row_count; }
    /** @see #row_count */ @JsonProperty("row_count")  public void setRowCount(Number row_count) { this.row_count = row_count; }


    public static final Boolean isJobFailEvent(Object obj) { return (obj.getClass() == JobFailEvent.class); }

}
