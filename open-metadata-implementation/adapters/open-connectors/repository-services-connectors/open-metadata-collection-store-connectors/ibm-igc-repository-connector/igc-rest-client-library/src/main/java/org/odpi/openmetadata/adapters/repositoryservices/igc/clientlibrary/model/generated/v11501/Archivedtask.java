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
 * POJO for the {@code archivedtask} asset type in IGC, displayed as '{@literal ArchivedTask}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Archivedtask extends Reference {

    public static String getIgcTypeId() { return "archivedtask"; }
    public static String getIgcTypeDisplayName() { return "ArchivedTask"; }

    /**
     * The {@code status} property, displayed as '{@literal Status}' in the IGC UI.
     */
    protected String status;

    /**
     * The {@code message} property, displayed as '{@literal Status Message}' in the IGC UI.
     */
    protected String message;

    /**
     * The {@code requested_on} property, displayed as '{@literal Date Requested}' in the IGC UI.
     */
    protected Date requested_on;

    /**
     * The {@code completion_date} property, displayed as '{@literal Date Completed}' in the IGC UI.
     */
    protected Date completion_date;

    /**
     * The {@code name} property, displayed as '{@literal Asset Name}' in the IGC UI.
     */
    protected String name;


    /** @see #status */ @JsonProperty("status")  public String getStatus() { return this.status; }
    /** @see #status */ @JsonProperty("status")  public void setStatus(String status) { this.status = status; }

    /** @see #message */ @JsonProperty("message")  public String getMessage() { return this.message; }
    /** @see #message */ @JsonProperty("message")  public void setMessage(String message) { this.message = message; }

    /** @see #requested_on */ @JsonProperty("requested_on")  public Date getRequestedOn() { return this.requested_on; }
    /** @see #requested_on */ @JsonProperty("requested_on")  public void setRequestedOn(Date requested_on) { this.requested_on = requested_on; }

    /** @see #completion_date */ @JsonProperty("completion_date")  public Date getCompletionDate() { return this.completion_date; }
    /** @see #completion_date */ @JsonProperty("completion_date")  public void setCompletionDate(Date completion_date) { this.completion_date = completion_date; }

    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "status",
        "message",
        "requested_on",
        "completion_date",
        "name"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "status",
        "message",
        "name"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "status",
        "message",
        "requested_on",
        "completion_date",
        "name"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isArchivedtask(Object obj) { return (obj.getClass() == Archivedtask.class); }

}
