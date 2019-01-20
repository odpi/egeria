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
 * POJO for the 'Rule_Execution_Result' asset type in IGC, displayed as 'Rule Execution Result' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class RuleExecutionResult extends Reference {

    public static String getIgcTypeId() { return "Rule_Execution_Result"; }
    public static String getIgcTypeDisplayName() { return "Rule Execution Result"; }

    /**
     * The 'nbRecordsTested' property, displayed as 'Number of Records Tested' in the IGC UI.
     */
    protected Number nbRecordsTested;

    /**
     * The 'nbPassed' property, displayed as 'Number of Records Met' in the IGC UI.
     */
    protected Number nbPassed;

    /**
     * The 'nbFailed' property, displayed as 'Number of Records Not Met' in the IGC UI.
     */
    protected Number nbFailed;

    /**
     * The 'benchmark' property, displayed as 'Benchmark' in the IGC UI.
     */
    protected ArrayList<String> benchmark;

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


    /** @see #nbRecordsTested */ @JsonProperty("nbRecordsTested")  public Number getNbrecordstested() { return this.nbRecordsTested; }
    /** @see #nbRecordsTested */ @JsonProperty("nbRecordsTested")  public void setNbrecordstested(Number nbRecordsTested) { this.nbRecordsTested = nbRecordsTested; }

    /** @see #nbPassed */ @JsonProperty("nbPassed")  public Number getNbpassed() { return this.nbPassed; }
    /** @see #nbPassed */ @JsonProperty("nbPassed")  public void setNbpassed(Number nbPassed) { this.nbPassed = nbPassed; }

    /** @see #nbFailed */ @JsonProperty("nbFailed")  public Number getNbfailed() { return this.nbFailed; }
    /** @see #nbFailed */ @JsonProperty("nbFailed")  public void setNbfailed(Number nbFailed) { this.nbFailed = nbFailed; }

    /** @see #benchmark */ @JsonProperty("benchmark")  public ArrayList<String> getBenchmark() { return this.benchmark; }
    /** @see #benchmark */ @JsonProperty("benchmark")  public void setBenchmark(ArrayList<String> benchmark) { this.benchmark = benchmark; }

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
        "nbRecordsTested",
        "nbPassed",
        "nbFailed",
        "benchmark",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "benchmark",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "nbRecordsTested",
        "nbPassed",
        "nbFailed",
        "benchmark",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isRuleExecutionResult(Object obj) { return (obj.getClass() == RuleExecutionResult.class); }

}
