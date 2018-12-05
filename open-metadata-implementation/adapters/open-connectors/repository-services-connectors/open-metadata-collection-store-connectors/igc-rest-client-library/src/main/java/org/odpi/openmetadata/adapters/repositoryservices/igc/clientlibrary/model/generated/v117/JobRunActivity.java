/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'job_run_activity' asset type in IGC, displayed as 'Job Run Activity' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class JobRunActivity extends MainObject {

    public static final String IGC_TYPE_ID = "job_run_activity";

    /**
     * The 'job_run' property, displayed as 'Job Run' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link JobRun} object.
     */
    protected Reference job_run;

    /**
     * The 'x_emits_read_event_display_name' property, displayed as 'X Emits Read Event Display Name' in the IGC UI.
     */
    protected ArrayList<String> x_emits_read_event_display_name;

    /**
     * The 'x_emits_write_event_display_name' property, displayed as 'X Emits Write Event Display Name' in the IGC UI.
     */
    protected ArrayList<String> x_emits_write_event_display_name;

    /**
     * The 'x_emits_fail_events_display_name' property, displayed as 'X Emits Fail Events Display Name' in the IGC UI.
     */
    protected ArrayList<String> x_emits_fail_events_display_name;


    /** @see #job_run */ @JsonProperty("job_run")  public Reference getJobRun() { return this.job_run; }
    /** @see #job_run */ @JsonProperty("job_run")  public void setJobRun(Reference job_run) { this.job_run = job_run; }

    /** @see #x_emits_read_event_display_name */ @JsonProperty("x_emits_read_event_display_name")  public ArrayList<String> getXEmitsReadEventDisplayName() { return this.x_emits_read_event_display_name; }
    /** @see #x_emits_read_event_display_name */ @JsonProperty("x_emits_read_event_display_name")  public void setXEmitsReadEventDisplayName(ArrayList<String> x_emits_read_event_display_name) { this.x_emits_read_event_display_name = x_emits_read_event_display_name; }

    /** @see #x_emits_write_event_display_name */ @JsonProperty("x_emits_write_event_display_name")  public ArrayList<String> getXEmitsWriteEventDisplayName() { return this.x_emits_write_event_display_name; }
    /** @see #x_emits_write_event_display_name */ @JsonProperty("x_emits_write_event_display_name")  public void setXEmitsWriteEventDisplayName(ArrayList<String> x_emits_write_event_display_name) { this.x_emits_write_event_display_name = x_emits_write_event_display_name; }

    /** @see #x_emits_fail_events_display_name */ @JsonProperty("x_emits_fail_events_display_name")  public ArrayList<String> getXEmitsFailEventsDisplayName() { return this.x_emits_fail_events_display_name; }
    /** @see #x_emits_fail_events_display_name */ @JsonProperty("x_emits_fail_events_display_name")  public void setXEmitsFailEventsDisplayName(ArrayList<String> x_emits_fail_events_display_name) { this.x_emits_fail_events_display_name = x_emits_fail_events_display_name; }


    public static final Boolean isJobRunActivity(Object obj) { return (obj.getClass() == JobRunActivity.class); }

}
