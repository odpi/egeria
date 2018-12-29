/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'term_history' asset type in IGC, displayed as 'Term History' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TermHistory extends Reference {

    public static String getIgcTypeId() { return "term_history"; }
    public static String getIgcTypeDisplayName() { return "Term History"; }

    /**
     * The 'term' property, displayed as 'Term' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationAsset} object.
     */
    protected Reference term;

    /**
     * The 'date' property, displayed as 'Date' in the IGC UI.
     */
    protected Date date;

    /**
     * The 'comment' property, displayed as 'Comment' in the IGC UI.
     */
    protected ArrayList<String> comment;

    /**
     * The 'edited_by' property, displayed as 'Edited By' in the IGC UI.
     */
    protected String edited_by;

    /**
     * The 'changed_properties' property, displayed as 'Changed Properties' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ChangedProperties} objects.
     */
    protected ReferenceList changed_properties;

    /**
     * The 'user_task_name' property, displayed as 'User Task Name' in the IGC UI.
     */
    protected String user_task_name;

    /**
     * The 'user_task_key' property, displayed as 'User Task Key' in the IGC UI.
     */
    protected String user_task_key;

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


    /** @see #term */ @JsonProperty("term")  public Reference getTerm() { return this.term; }
    /** @see #term */ @JsonProperty("term")  public void setTerm(Reference term) { this.term = term; }

    /** @see #date */ @JsonProperty("date")  public Date getDate() { return this.date; }
    /** @see #date */ @JsonProperty("date")  public void setDate(Date date) { this.date = date; }

    /** @see #comment */ @JsonProperty("comment")  public ArrayList<String> getComment() { return this.comment; }
    /** @see #comment */ @JsonProperty("comment")  public void setComment(ArrayList<String> comment) { this.comment = comment; }

    /** @see #edited_by */ @JsonProperty("edited_by")  public String getEditedBy() { return this.edited_by; }
    /** @see #edited_by */ @JsonProperty("edited_by")  public void setEditedBy(String edited_by) { this.edited_by = edited_by; }

    /** @see #changed_properties */ @JsonProperty("changed_properties")  public ReferenceList getChangedProperties() { return this.changed_properties; }
    /** @see #changed_properties */ @JsonProperty("changed_properties")  public void setChangedProperties(ReferenceList changed_properties) { this.changed_properties = changed_properties; }

    /** @see #user_task_name */ @JsonProperty("user_task_name")  public String getUserTaskName() { return this.user_task_name; }
    /** @see #user_task_name */ @JsonProperty("user_task_name")  public void setUserTaskName(String user_task_name) { this.user_task_name = user_task_name; }

    /** @see #user_task_key */ @JsonProperty("user_task_key")  public String getUserTaskKey() { return this.user_task_key; }
    /** @see #user_task_key */ @JsonProperty("user_task_key")  public void setUserTaskKey(String user_task_key) { this.user_task_key = user_task_key; }

    /** @see #workflow_instance_id */ @JsonProperty("workflow_instance_id")  public String getWorkflowInstanceId() { return this.workflow_instance_id; }
    /** @see #workflow_instance_id */ @JsonProperty("workflow_instance_id")  public void setWorkflowInstanceId(String workflow_instance_id) { this.workflow_instance_id = workflow_instance_id; }

    /** @see #workflow_event */ @JsonProperty("workflow_event")  public String getWorkflowEvent() { return this.workflow_event; }
    /** @see #workflow_event */ @JsonProperty("workflow_event")  public void setWorkflowEvent(String workflow_event) { this.workflow_event = workflow_event; }

    /** @see #workflow_new_state */ @JsonProperty("workflow_new_state")  public String getWorkflowNewState() { return this.workflow_new_state; }
    /** @see #workflow_new_state */ @JsonProperty("workflow_new_state")  public void setWorkflowNewState(String workflow_new_state) { this.workflow_new_state = workflow_new_state; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "date",
        "comment",
        "edited_by",
        "user_task_name",
        "user_task_key",
        "workflow_instance_id",
        "workflow_event",
        "workflow_new_state"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "changed_properties"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "term",
        "date",
        "comment",
        "edited_by",
        "changed_properties",
        "user_task_name",
        "user_task_key",
        "workflow_instance_id",
        "workflow_event",
        "workflow_new_state"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isTermHistory(Object obj) { return (obj.getClass() == TermHistory.class); }

}
