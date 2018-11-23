/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'data_rule' asset type in IGC, displayed as 'Data Rule' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataRule extends MainObject {

    public static final String IGC_TYPE_ID = "data_rule";

    /**
     * The 'implemented_bindings' property, displayed as 'Implemented Bindings' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataField} objects.
     */
    protected ReferenceList implemented_bindings;

    /**
     * The 'data_rule_definitions' property, displayed as 'Data Rule Definitions' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link NonPublishedDataRuleDefinition} object.
     */
    protected Reference data_rule_definitions;

    /**
     * The 'project' property, displayed as 'Project' in the IGC UI.
     */
    protected ArrayList<String> project;

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
     * The 'expression' property, displayed as 'Expression' in the IGC UI.
     */
    protected ArrayList<String> expression;

    /**
     * The 'contact' property, displayed as 'Contact' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Steward} objects.
     */
    protected ReferenceList contact;

    /**
     * The 'metrics' property, displayed as 'Metrics' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Metric} objects.
     */
    protected ReferenceList metrics;

    /**
     * The 'benchmark' property, displayed as 'Benchmark' in the IGC UI.
     */
    protected ArrayList<String> benchmark;

    /**
     * The 'execution_history' property, displayed as 'Execution History' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataRuleResults} objects.
     */
    protected ReferenceList execution_history;

    /**
     * The 'data_policies' property, displayed as 'Data Policies' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList data_policies;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #implemented_bindings */ @JsonProperty("implemented_bindings")  public ReferenceList getImplementedBindings() { return this.implemented_bindings; }
    /** @see #implemented_bindings */ @JsonProperty("implemented_bindings")  public void setImplementedBindings(ReferenceList implemented_bindings) { this.implemented_bindings = implemented_bindings; }

    /** @see #data_rule_definitions */ @JsonProperty("data_rule_definitions")  public Reference getDataRuleDefinitions() { return this.data_rule_definitions; }
    /** @see #data_rule_definitions */ @JsonProperty("data_rule_definitions")  public void setDataRuleDefinitions(Reference data_rule_definitions) { this.data_rule_definitions = data_rule_definitions; }

    /** @see #project */ @JsonProperty("project")  public ArrayList<String> getProject() { return this.project; }
    /** @see #project */ @JsonProperty("project")  public void setProject(ArrayList<String> project) { this.project = project; }

    /** @see #status */ @JsonProperty("status")  public String getStatus() { return this.status; }
    /** @see #status */ @JsonProperty("status")  public void setStatus(String status) { this.status = status; }

    /** @see #expression */ @JsonProperty("expression")  public ArrayList<String> getExpression() { return this.expression; }
    /** @see #expression */ @JsonProperty("expression")  public void setExpression(ArrayList<String> expression) { this.expression = expression; }

    /** @see #contact */ @JsonProperty("contact")  public ReferenceList getContact() { return this.contact; }
    /** @see #contact */ @JsonProperty("contact")  public void setContact(ReferenceList contact) { this.contact = contact; }

    /** @see #metrics */ @JsonProperty("metrics")  public ReferenceList getMetrics() { return this.metrics; }
    /** @see #metrics */ @JsonProperty("metrics")  public void setMetrics(ReferenceList metrics) { this.metrics = metrics; }

    /** @see #benchmark */ @JsonProperty("benchmark")  public ArrayList<String> getBenchmark() { return this.benchmark; }
    /** @see #benchmark */ @JsonProperty("benchmark")  public void setBenchmark(ArrayList<String> benchmark) { this.benchmark = benchmark; }

    /** @see #execution_history */ @JsonProperty("execution_history")  public ReferenceList getExecutionHistory() { return this.execution_history; }
    /** @see #execution_history */ @JsonProperty("execution_history")  public void setExecutionHistory(ReferenceList execution_history) { this.execution_history = execution_history; }

    /** @see #data_policies */ @JsonProperty("data_policies")  public ReferenceList getDataPolicies() { return this.data_policies; }
    /** @see #data_policies */ @JsonProperty("data_policies")  public void setDataPolicies(ReferenceList data_policies) { this.data_policies = data_policies; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isDataRule(Object obj) { return (obj.getClass() == DataRule.class); }

}
