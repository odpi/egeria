/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'development_log' asset type in IGC, displayed as 'DevelopmentLog' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DevelopmentLog extends Reference {

    public static String getIgcTypeId() { return "development_log"; }
    public static String getIgcTypeDisplayName() { return "DevelopmentLog"; }

    /**
     * The 'date' property, displayed as 'Date' in the IGC UI.
     */
    protected Date date;

    /**
     * The 'workflow_task' property, displayed as 'Workflow Task' in the IGC UI.
     */
    protected String workflow_task;

    /**
     * The 'activity' property, displayed as 'Activity' in the IGC UI.
     */
    protected String activity;

    /**
     * The 'new_state' property, displayed as 'New State' in the IGC UI.
     */
    protected String new_state;

    /**
     * The 'person' property, displayed as 'Person' in the IGC UI.
     */
    protected String person;

    /**
     * The 'comment' property, displayed as 'Comment' in the IGC UI.
     */
    protected String comment;


    /** @see #date */ @JsonProperty("date")  public Date getDate() { return this.date; }
    /** @see #date */ @JsonProperty("date")  public void setDate(Date date) { this.date = date; }

    /** @see #workflow_task */ @JsonProperty("workflow_task")  public String getWorkflowTask() { return this.workflow_task; }
    /** @see #workflow_task */ @JsonProperty("workflow_task")  public void setWorkflowTask(String workflow_task) { this.workflow_task = workflow_task; }

    /** @see #activity */ @JsonProperty("activity")  public String getActivity() { return this.activity; }
    /** @see #activity */ @JsonProperty("activity")  public void setActivity(String activity) { this.activity = activity; }

    /** @see #new_state */ @JsonProperty("new_state")  public String getNewState() { return this.new_state; }
    /** @see #new_state */ @JsonProperty("new_state")  public void setNewState(String new_state) { this.new_state = new_state; }

    /** @see #person */ @JsonProperty("person")  public String getPerson() { return this.person; }
    /** @see #person */ @JsonProperty("person")  public void setPerson(String person) { this.person = person; }

    /** @see #comment */ @JsonProperty("comment")  public String getComment() { return this.comment; }
    /** @see #comment */ @JsonProperty("comment")  public void setComment(String comment) { this.comment = comment; }

    public static final Boolean canBeCreated() { return false; }
    public static final Boolean includesModificationDetails() { return false; }
    public static final ArrayList<String> NON_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("date");
        add("workflow_task");
        add("activity");
        add("new_state");
        add("person");
        add("comment");
    }};
    public static final List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static final Boolean isDevelopmentLog(Object obj) { return (obj.getClass() == DevelopmentLog.class); }

}
