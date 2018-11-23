/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
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
public class SessionConfiguration extends MainObject {

    public static final String IGC_TYPE_ID = "session_configuration";

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


    /** @see #max_sessions */ @JsonProperty("max_sessions")  public Number getMaxSessions() { return this.max_sessions; }
    /** @see #max_sessions */ @JsonProperty("max_sessions")  public void setMaxSessions(Number max_sessions) { this.max_sessions = max_sessions; }

    /** @see #time_to_live */ @JsonProperty("time_to_live")  public Number getTimeToLive() { return this.time_to_live; }
    /** @see #time_to_live */ @JsonProperty("time_to_live")  public void setTimeToLive(Number time_to_live) { this.time_to_live = time_to_live; }

    /** @see #wake_up_interval */ @JsonProperty("wake_up_interval")  public Number getWakeUpInterval() { return this.wake_up_interval; }
    /** @see #wake_up_interval */ @JsonProperty("wake_up_interval")  public void setWakeUpInterval(Number wake_up_interval) { this.wake_up_interval = wake_up_interval; }


    public static final Boolean isSessionConfiguration(Object obj) { return (obj.getClass() == SessionConfiguration.class); }

}
