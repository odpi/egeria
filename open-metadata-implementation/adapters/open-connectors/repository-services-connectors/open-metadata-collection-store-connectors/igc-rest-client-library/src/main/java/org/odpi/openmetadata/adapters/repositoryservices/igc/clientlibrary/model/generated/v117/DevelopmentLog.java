/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
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

    /**
     * The 'date' property, displayed as 'Date' in the IGC UI.
     */
    protected Date date;

    /**
     * The 'workflow_task' property, displayed as 'Workflow Task' in the IGC UI.
     */
    protected String workflow_task;

    /**
     * The 'development_log' property, displayed as 'Header' in the IGC UI.
     */
    protected ArrayList<String> development_log;

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

    /**
     * The 'user_task_key' property, displayed as 'User Task Key' in the IGC UI.
     */
    protected String user_task_key;

    /**
     * The 'user_task_name' property, displayed as 'User Task Name' in the IGC UI.
     */
    protected String user_task_name;

    /**
     * The 'workflow_instance_id' property, displayed as 'Workflow Instance Id' in the IGC UI.
     */
    protected String workflow_instance_id;

    /**
     * The 'workflow_event' property, displayed as 'Workflow Event' in the IGC UI.
     */
    protected String workflow_event;

    /**
     * The 'workflow_new_state' property, displayed as 'Workflow New State' in the IGC UI.
     */
    protected String workflow_new_state;


    /** @see #date */ @JsonProperty("date")  public Date getDate() { return this.date; }
    /** @see #date */ @JsonProperty("date")  public void setDate(Date date) { this.date = date; }

    /** @see #workflow_task */ @JsonProperty("workflow_task")  public String getWorkflowTask() { return this.workflow_task; }
    /** @see #workflow_task */ @JsonProperty("workflow_task")  public void setWorkflowTask(String workflow_task) { this.workflow_task = workflow_task; }

    /** @see #development_log */ @JsonProperty("development_log")  public ArrayList<String> getDevelopmentLog() { return this.development_log; }
    /** @see #development_log */ @JsonProperty("development_log")  public void setDevelopmentLog(ArrayList<String> development_log) { this.development_log = development_log; }

    /** @see #activity */ @JsonProperty("activity")  public String getActivity() { return this.activity; }
    /** @see #activity */ @JsonProperty("activity")  public void setActivity(String activity) { this.activity = activity; }

    /** @see #new_state */ @JsonProperty("new_state")  public String getNewState() { return this.new_state; }
    /** @see #new_state */ @JsonProperty("new_state")  public void setNewState(String new_state) { this.new_state = new_state; }

    /** @see #person */ @JsonProperty("person")  public String getPerson() { return this.person; }
    /** @see #person */ @JsonProperty("person")  public void setPerson(String person) { this.person = person; }

    /** @see #comment */ @JsonProperty("comment")  public String getComment() { return this.comment; }
    /** @see #comment */ @JsonProperty("comment")  public void setComment(String comment) { this.comment = comment; }

    /** @see #user_task_key */ @JsonProperty("user_task_key")  public String getUserTaskKey() { return this.user_task_key; }
    /** @see #user_task_key */ @JsonProperty("user_task_key")  public void setUserTaskKey(String user_task_key) { this.user_task_key = user_task_key; }

    /** @see #user_task_name */ @JsonProperty("user_task_name")  public String getUserTaskName() { return this.user_task_name; }
    /** @see #user_task_name */ @JsonProperty("user_task_name")  public void setUserTaskName(String user_task_name) { this.user_task_name = user_task_name; }

    /** @see #workflow_instance_id */ @JsonProperty("workflow_instance_id")  public String getWorkflowInstanceId() { return this.workflow_instance_id; }
    /** @see #workflow_instance_id */ @JsonProperty("workflow_instance_id")  public void setWorkflowInstanceId(String workflow_instance_id) { this.workflow_instance_id = workflow_instance_id; }

    /** @see #workflow_event */ @JsonProperty("workflow_event")  public String getWorkflowEvent() { return this.workflow_event; }
    /** @see #workflow_event */ @JsonProperty("workflow_event")  public void setWorkflowEvent(String workflow_event) { this.workflow_event = workflow_event; }

    /** @see #workflow_new_state */ @JsonProperty("workflow_new_state")  public String getWorkflowNewState() { return this.workflow_new_state; }
    /** @see #workflow_new_state */ @JsonProperty("workflow_new_state")  public void setWorkflowNewState(String workflow_new_state) { this.workflow_new_state = workflow_new_state; }


    public static final Boolean isDevelopmentLog(Object obj) { return (obj.getClass() == DevelopmentLog.class); }

}
