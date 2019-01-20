/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502sp5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'inv_data_rule' asset type in IGC, displayed as 'Data Rule' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InvDataRule extends Reference {

    public static String getIgcTypeId() { return "inv_data_rule"; }
    public static String getIgcTypeDisplayName() { return "Data Rule"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_&_long_description' property, displayed as 'Short & Long Description' in the IGC UI.
     */
    @JsonProperty("short_&_long_description") protected String short___long_description;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'long_description' property, displayed as 'Long Description' in the IGC UI.
     */
    protected String long_description;

    /**
     * The 'project' property, displayed as 'Project' in the IGC UI.
     */
    protected String project;

    /**
     * The 'status' property, displayed as 'Status' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>CANDIDATE (displayed in the UI as 'CANDIDATE')</li>
     *     <li>ACCEPTED (displayed in the UI as 'ACCEPTED')</li>
     *     <li>STANDARD (displayed in the UI as 'STANDARD')</li>
     *     <li>DEPRECATED (displayed in the UI as 'DEPRECATED')</li>
     *     <li>DRAFT (displayed in the UI as 'DRAFT')</li>
     *     <li>IN_PROCESS (displayed in the UI as 'IN_PROCESS')</li>
     *     <li>REJECTED (displayed in the UI as 'REJECTED')</li>
     *     <li>ERROR (displayed in the UI as 'ERROR')</li>
     * </ul>
     */
    protected String status;

    /**
     * The 'steward' property, displayed as 'Steward' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList steward;

    /**
     * The 'assigned_to_terms' property, displayed as 'Assigned to Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The 'data_rule_definitions' property, displayed as 'Data Rule Definitions' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InvDataRuleDefinition} object.
     */
    protected Reference data_rule_definitions;

    /**
     * The 'expression' property, displayed as 'Expression' in the IGC UI.
     */
    protected ArrayList<String> expression;

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


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public String getShortLongDescription() { return this.short___long_description; }
    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public void setShortLongDescription(String short___long_description) { this.short___long_description = short___long_description; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #project */ @JsonProperty("project")  public String getProject() { return this.project; }
    /** @see #project */ @JsonProperty("project")  public void setProject(String project) { this.project = project; }

    /** @see #status */ @JsonProperty("status")  public String getStatus() { return this.status; }
    /** @see #status */ @JsonProperty("status")  public void setStatus(String status) { this.status = status; }

    /** @see #steward */ @JsonProperty("steward")  public ReferenceList getSteward() { return this.steward; }
    /** @see #steward */ @JsonProperty("steward")  public void setSteward(ReferenceList steward) { this.steward = steward; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #data_rule_definitions */ @JsonProperty("data_rule_definitions")  public Reference getDataRuleDefinitions() { return this.data_rule_definitions; }
    /** @see #data_rule_definitions */ @JsonProperty("data_rule_definitions")  public void setDataRuleDefinitions(Reference data_rule_definitions) { this.data_rule_definitions = data_rule_definitions; }

    /** @see #expression */ @JsonProperty("expression")  public ArrayList<String> getExpression() { return this.expression; }
    /** @see #expression */ @JsonProperty("expression")  public void setExpression(ArrayList<String> expression) { this.expression = expression; }

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
        "short_&_long_description",
        "short_description",
        "long_description",
        "project",
        "status",
        "expression",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_&_long_description",
        "short_description",
        "long_description",
        "project",
        "expression",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "steward",
        "assigned_to_terms"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_&_long_description",
        "short_description",
        "long_description",
        "project",
        "status",
        "steward",
        "assigned_to_terms",
        "data_rule_definitions",
        "expression",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isInvDataRule(Object obj) { return (obj.getClass() == InvDataRule.class); }

}
