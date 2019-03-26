/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11501;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code job_run_activity} asset type in IGC, displayed as '{@literal Job Run Activity}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class JobRunActivity extends Reference {

    public static String getIgcTypeId() { return "job_run_activity"; }
    public static String getIgcTypeDisplayName() { return "Job Run Activity"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code job_run} property, displayed as '{@literal Job Run}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link JobRun} object.
     */
    protected Reference job_run;

    /**
     * The {@code x_emits_read_event_display_name} property, displayed as '{@literal X Emits Read Event Display Name}' in the IGC UI.
     */
    protected ArrayList<String> x_emits_read_event_display_name;

    /**
     * The {@code x_emits_write_event_display_name} property, displayed as '{@literal X Emits Write Event Display Name}' in the IGC UI.
     */
    protected ArrayList<String> x_emits_write_event_display_name;

    /**
     * The {@code x_emits_fail_events_display_name} property, displayed as '{@literal X Emits Fail Events Display Name}' in the IGC UI.
     */
    protected ArrayList<String> x_emits_fail_events_display_name;

    /**
     * The {@code created_by} property, displayed as '{@literal Created By}' in the IGC UI.
     */
    protected String created_by;

    /**
     * The {@code created_on} property, displayed as '{@literal Created On}' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The {@code modified_by} property, displayed as '{@literal Modified By}' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The {@code modified_on} property, displayed as '{@literal Modified On}' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #job_run */ @JsonProperty("job_run")  public Reference getJobRun() { return this.job_run; }
    /** @see #job_run */ @JsonProperty("job_run")  public void setJobRun(Reference job_run) { this.job_run = job_run; }

    /** @see #x_emits_read_event_display_name */ @JsonProperty("x_emits_read_event_display_name")  public ArrayList<String> getXEmitsReadEventDisplayName() { return this.x_emits_read_event_display_name; }
    /** @see #x_emits_read_event_display_name */ @JsonProperty("x_emits_read_event_display_name")  public void setXEmitsReadEventDisplayName(ArrayList<String> x_emits_read_event_display_name) { this.x_emits_read_event_display_name = x_emits_read_event_display_name; }

    /** @see #x_emits_write_event_display_name */ @JsonProperty("x_emits_write_event_display_name")  public ArrayList<String> getXEmitsWriteEventDisplayName() { return this.x_emits_write_event_display_name; }
    /** @see #x_emits_write_event_display_name */ @JsonProperty("x_emits_write_event_display_name")  public void setXEmitsWriteEventDisplayName(ArrayList<String> x_emits_write_event_display_name) { this.x_emits_write_event_display_name = x_emits_write_event_display_name; }

    /** @see #x_emits_fail_events_display_name */ @JsonProperty("x_emits_fail_events_display_name")  public ArrayList<String> getXEmitsFailEventsDisplayName() { return this.x_emits_fail_events_display_name; }
    /** @see #x_emits_fail_events_display_name */ @JsonProperty("x_emits_fail_events_display_name")  public void setXEmitsFailEventsDisplayName(ArrayList<String> x_emits_fail_events_display_name) { this.x_emits_fail_events_display_name = x_emits_fail_events_display_name; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "x_emits_read_event_display_name",
        "x_emits_write_event_display_name",
        "x_emits_fail_events_display_name",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "x_emits_read_event_display_name",
        "x_emits_write_event_display_name",
        "x_emits_fail_events_display_name",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "job_run",
        "x_emits_read_event_display_name",
        "x_emits_write_event_display_name",
        "x_emits_fail_events_display_name",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isJobRunActivity(Object obj) { return (obj.getClass() == JobRunActivity.class); }

}
