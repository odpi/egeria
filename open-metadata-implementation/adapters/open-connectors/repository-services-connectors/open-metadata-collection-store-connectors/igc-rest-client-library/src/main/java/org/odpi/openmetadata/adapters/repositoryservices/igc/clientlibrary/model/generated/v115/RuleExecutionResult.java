/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
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


    /** @see #nbRecordsTested */ @JsonProperty("nbRecordsTested")  public Number getNbRecordsTested() { return this.nbRecordsTested; }
    /** @see #nbRecordsTested */ @JsonProperty("nbRecordsTested")  public void setNbRecordsTested(Number nbRecordsTested) { this.nbRecordsTested = nbRecordsTested; }

    /** @see #nbPassed */ @JsonProperty("nbPassed")  public Number getNbPassed() { return this.nbPassed; }
    /** @see #nbPassed */ @JsonProperty("nbPassed")  public void setNbPassed(Number nbPassed) { this.nbPassed = nbPassed; }

    /** @see #nbFailed */ @JsonProperty("nbFailed")  public Number getNbFailed() { return this.nbFailed; }
    /** @see #nbFailed */ @JsonProperty("nbFailed")  public void setNbFailed(Number nbFailed) { this.nbFailed = nbFailed; }

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

    public static final Boolean includesModificationDetails() { return true; }
    public static final Boolean isRuleExecutionResult(Object obj) { return (obj.getClass() == RuleExecutionResult.class); }

}
