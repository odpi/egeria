/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'inv_data_rule' asset type in IGC, displayed as 'Data Rule' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InvDataRule extends MainObject {

    public static final String IGC_TYPE_ID = "inv_data_rule";

    /**
     * The 'short_&amp;_long_description' property, displayed as 'Short &amp; Long Description' in the IGC UI.
     */
    @JsonProperty("short_&_long_description") protected String short___long_description;

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
     * The 'data_rule_definitions' property, displayed as 'Data Rule Definitions' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InvDataRuleDefinition} object.
     */
    protected Reference data_rule_definitions;

    /**
     * The 'expression' property, displayed as 'Expression' in the IGC UI.
     */
    protected ArrayList<String> expression;


    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public String getShortLongDescription() { return this.short___long_description; }
    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public void setShortLongDescription(String short___long_description) { this.short___long_description = short___long_description; }

    /** @see #project */ @JsonProperty("project")  public String getProject() { return this.project; }
    /** @see #project */ @JsonProperty("project")  public void setProject(String project) { this.project = project; }

    /** @see #status */ @JsonProperty("status")  public String getStatus() { return this.status; }
    /** @see #status */ @JsonProperty("status")  public void setStatus(String status) { this.status = status; }

    /** @see #steward */ @JsonProperty("steward")  public ReferenceList getSteward() { return this.steward; }
    /** @see #steward */ @JsonProperty("steward")  public void setSteward(ReferenceList steward) { this.steward = steward; }

    /** @see #data_rule_definitions */ @JsonProperty("data_rule_definitions")  public Reference getDataRuleDefinitions() { return this.data_rule_definitions; }
    /** @see #data_rule_definitions */ @JsonProperty("data_rule_definitions")  public void setDataRuleDefinitions(Reference data_rule_definitions) { this.data_rule_definitions = data_rule_definitions; }

    /** @see #expression */ @JsonProperty("expression")  public ArrayList<String> getExpression() { return this.expression; }
    /** @see #expression */ @JsonProperty("expression")  public void setExpression(ArrayList<String> expression) { this.expression = expression; }


    public static final Boolean isInvDataRule(Object obj) { return (obj.getClass() == InvDataRule.class); }

}
