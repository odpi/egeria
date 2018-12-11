/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'session_configuration' asset type in IGC, displayed as 'Session Configuration' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class SessionConfiguration extends Reference {

    @JsonIgnore public static final String IGC_TYPE_ID = "session_configuration";

    /**
     * The 'max_sessions' property, displayed as 'Max Sessions' in the IGC UI.
     */
    protected Number max_sessions;

    /**
     * The 'time_to_live' property, displayed as 'Time To Live' in the IGC UI.
     */
    protected Number time_to_live;

    /**
     * The 'wake_up_interval' property, displayed as 'Wake Up Interval' in the IGC UI.
     */
    protected Number wake_up_interval;

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created On' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Modified On' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #max_sessions */ @JsonProperty("max_sessions")  public Number getMaxSessions() { return this.max_sessions; }
    /** @see #max_sessions */ @JsonProperty("max_sessions")  public void setMaxSessions(Number max_sessions) { this.max_sessions = max_sessions; }

    /** @see #time_to_live */ @JsonProperty("time_to_live")  public Number getTimeToLive() { return this.time_to_live; }
    /** @see #time_to_live */ @JsonProperty("time_to_live")  public void setTimeToLive(Number time_to_live) { this.time_to_live = time_to_live; }

    /** @see #wake_up_interval */ @JsonProperty("wake_up_interval")  public Number getWakeUpInterval() { return this.wake_up_interval; }
    /** @see #wake_up_interval */ @JsonProperty("wake_up_interval")  public void setWakeUpInterval(Number wake_up_interval) { this.wake_up_interval = wake_up_interval; }

    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }


    public static final Boolean isSessionConfiguration(Object obj) { return (obj.getClass() == SessionConfiguration.class); }

}
