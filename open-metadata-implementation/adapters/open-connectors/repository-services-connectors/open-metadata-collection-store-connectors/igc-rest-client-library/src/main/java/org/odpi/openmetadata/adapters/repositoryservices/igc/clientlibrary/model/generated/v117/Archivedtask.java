/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'archivedtask' asset type in IGC, displayed as 'ArchivedTask' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Archivedtask extends MainObject {

    public static final String IGC_TYPE_ID = "archivedtask";

    /**
     * The 'status' property, displayed as 'Status' in the IGC UI.
     */
    protected String status;

    /**
     * The 'message' property, displayed as 'Status Message' in the IGC UI.
     */
    protected String message;

    /**
     * The 'requested_on' property, displayed as 'Date Requested' in the IGC UI.
     */
    protected Date requested_on;

    /**
     * The 'completion_date' property, displayed as 'Date Completed' in the IGC UI.
     */
    protected Date completion_date;


    /** @see #status */ @JsonProperty("status")  public String getStatus() { return this.status; }
    /** @see #status */ @JsonProperty("status")  public void setStatus(String status) { this.status = status; }

    /** @see #message */ @JsonProperty("message")  public String getMessage() { return this.message; }
    /** @see #message */ @JsonProperty("message")  public void setMessage(String message) { this.message = message; }

    /** @see #requested_on */ @JsonProperty("requested_on")  public Date getRequestedOn() { return this.requested_on; }
    /** @see #requested_on */ @JsonProperty("requested_on")  public void setRequestedOn(Date requested_on) { this.requested_on = requested_on; }

    /** @see #completion_date */ @JsonProperty("completion_date")  public Date getCompletionDate() { return this.completion_date; }
    /** @see #completion_date */ @JsonProperty("completion_date")  public void setCompletionDate(Date completion_date) { this.completion_date = completion_date; }


    public static final Boolean isArchivedtask(Object obj) { return (obj.getClass() == Archivedtask.class); }

}
