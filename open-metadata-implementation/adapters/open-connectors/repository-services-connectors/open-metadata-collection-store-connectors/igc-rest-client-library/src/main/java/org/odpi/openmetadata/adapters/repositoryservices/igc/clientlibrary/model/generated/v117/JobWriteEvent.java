/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.InformationAsset;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * POJO for the 'job_write_event' asset type in IGC, displayed as 'Job Write Event' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class JobWriteEvent extends MainObject {

    public static final String IGC_TYPE_ID = "job_write_event";

    /**
     * The 'job_run_activity' property, displayed as 'Emitted By Activity' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationAsset} object.
     */
    protected Reference job_run_activity;

    /**
     * The 'time' property, displayed as 'Time' in the IGC UI.
     */
    protected Date time;

    /**
     * The 'writes_to' property, displayed as 'Writes To' in the IGC UI.
     */
    protected String writes_to;

    /**
     * The 'row_count' property, displayed as 'Rows Processed' in the IGC UI.
     */
    protected Number row_count;


    /** @see #job_run_activity */ @JsonProperty("job_run_activity")  public Reference getJobRunActivity() { return this.job_run_activity; }
    /** @see #job_run_activity */ @JsonProperty("job_run_activity")  public void setJobRunActivity(Reference job_run_activity) { this.job_run_activity = job_run_activity; }

    /** @see #time */ @JsonProperty("time")  public Date getTime() { return this.time; }
    /** @see #time */ @JsonProperty("time")  public void setTime(Date time) { this.time = time; }

    /** @see #writes_to */ @JsonProperty("writes_to")  public String getWritesTo() { return this.writes_to; }
    /** @see #writes_to */ @JsonProperty("writes_to")  public void setWritesTo(String writes_to) { this.writes_to = writes_to; }

    /** @see #row_count */ @JsonProperty("row_count")  public Number getRowCount() { return this.row_count; }
    /** @see #row_count */ @JsonProperty("row_count")  public void setRowCount(Number row_count) { this.row_count = row_count; }


    public static final Boolean isJobWriteEvent(Object obj) { return (obj.getClass() == JobWriteEvent.class); }

}
