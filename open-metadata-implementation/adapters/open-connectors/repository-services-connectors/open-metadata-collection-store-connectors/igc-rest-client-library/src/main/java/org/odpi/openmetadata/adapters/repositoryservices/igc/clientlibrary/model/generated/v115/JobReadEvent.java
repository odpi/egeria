/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'job_read_event' asset type in IGC, displayed as 'Job Read Event' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class JobReadEvent extends MainObject {

    public static final String IGC_TYPE_ID = "job_read_event";

    /**
     * The 'reads_from' property, displayed as 'Reads From' in the IGC UI.
     */
    protected ArrayList<String> reads_from;

    /**
     * The 'job_run_activity' property, displayed as 'Job Run Activity' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationAsset} object.
     */
    protected Reference job_run_activity;

    /**
     * The 'row_count' property, displayed as 'Rows Processed' in the IGC UI.
     */
    protected Number row_count;

    /**
     * The 'time' property, displayed as 'Time' in the IGC UI.
     */
    protected Date time;


    /** @see #reads_from */ @JsonProperty("reads_from")  public ArrayList<String> getReadsFrom() { return this.reads_from; }
    /** @see #reads_from */ @JsonProperty("reads_from")  public void setReadsFrom(ArrayList<String> reads_from) { this.reads_from = reads_from; }

    /** @see #job_run_activity */ @JsonProperty("job_run_activity")  public Reference getJobRunActivity() { return this.job_run_activity; }
    /** @see #job_run_activity */ @JsonProperty("job_run_activity")  public void setJobRunActivity(Reference job_run_activity) { this.job_run_activity = job_run_activity; }

    /** @see #row_count */ @JsonProperty("row_count")  public Number getRowCount() { return this.row_count; }
    /** @see #row_count */ @JsonProperty("row_count")  public void setRowCount(Number row_count) { this.row_count = row_count; }

    /** @see #time */ @JsonProperty("time")  public Date getTime() { return this.time; }
    /** @see #time */ @JsonProperty("time")  public void setTime(Date time) { this.time = time; }


    public static final Boolean isJobReadEvent(Object obj) { return (obj.getClass() == JobReadEvent.class); }

}
