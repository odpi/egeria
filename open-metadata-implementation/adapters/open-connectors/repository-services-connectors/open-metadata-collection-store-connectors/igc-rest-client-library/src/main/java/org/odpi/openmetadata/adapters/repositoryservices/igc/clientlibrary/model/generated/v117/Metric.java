/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'metric' asset type in IGC, displayed as 'Metric' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Metric extends MainObject {

    public static final String IGC_TYPE_ID = "metric";

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
     * The 'measure' property, displayed as 'Expression' in the IGC UI.
     */
    protected String measure;

    /**
     * The 'references_data_rules_or_data_rule_sets' property, displayed as 'References Data Rules or Data Rule Sets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList references_data_rules_or_data_rule_sets;

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


    /** @see #project */ @JsonProperty("project")  public String getProject() { return this.project; }
    /** @see #project */ @JsonProperty("project")  public void setProject(String project) { this.project = project; }

    /** @see #status */ @JsonProperty("status")  public String getStatus() { return this.status; }
    /** @see #status */ @JsonProperty("status")  public void setStatus(String status) { this.status = status; }

    /** @see #measure */ @JsonProperty("measure")  public String getMeasure() { return this.measure; }
    /** @see #measure */ @JsonProperty("measure")  public void setMeasure(String measure) { this.measure = measure; }

    /** @see #references_data_rules_or_data_rule_sets */ @JsonProperty("references_data_rules_or_data_rule_sets")  public ReferenceList getReferencesDataRulesOrDataRuleSets() { return this.references_data_rules_or_data_rule_sets; }
    /** @see #references_data_rules_or_data_rule_sets */ @JsonProperty("references_data_rules_or_data_rule_sets")  public void setReferencesDataRulesOrDataRuleSets(ReferenceList references_data_rules_or_data_rule_sets) { this.references_data_rules_or_data_rule_sets = references_data_rules_or_data_rule_sets; }

    /** @see #benchmark */ @JsonProperty("benchmark")  public ArrayList<String> getBenchmark() { return this.benchmark; }
    /** @see #benchmark */ @JsonProperty("benchmark")  public void setBenchmark(ArrayList<String> benchmark) { this.benchmark = benchmark; }

    /** @see #execution_history */ @JsonProperty("execution_history")  public ReferenceList getExecutionHistory() { return this.execution_history; }
    /** @see #execution_history */ @JsonProperty("execution_history")  public void setExecutionHistory(ReferenceList execution_history) { this.execution_history = execution_history; }

    /** @see #data_policies */ @JsonProperty("data_policies")  public ReferenceList getDataPolicies() { return this.data_policies; }
    /** @see #data_policies */ @JsonProperty("data_policies")  public void setDataPolicies(ReferenceList data_policies) { this.data_policies = data_policies; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isMetric(Object obj) { return (obj.getClass() == Metric.class); }

}
