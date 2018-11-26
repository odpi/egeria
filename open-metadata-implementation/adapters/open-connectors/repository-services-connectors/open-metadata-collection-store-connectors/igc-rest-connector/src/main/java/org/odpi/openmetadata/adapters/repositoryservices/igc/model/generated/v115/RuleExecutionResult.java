/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class RuleExecutionResult extends MainObject {

    public static final String IGC_TYPE_ID = "Rule_Execution_Result";

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


    /** @see #nbRecordsTested */ @JsonProperty("nbRecordsTested")  public Number getNbRecordsTested() { return this.nbRecordsTested; }
    /** @see #nbRecordsTested */ @JsonProperty("nbRecordsTested")  public void setNbRecordsTested(Number nbRecordsTested) { this.nbRecordsTested = nbRecordsTested; }

    /** @see #nbPassed */ @JsonProperty("nbPassed")  public Number getNbPassed() { return this.nbPassed; }
    /** @see #nbPassed */ @JsonProperty("nbPassed")  public void setNbPassed(Number nbPassed) { this.nbPassed = nbPassed; }

    /** @see #nbFailed */ @JsonProperty("nbFailed")  public Number getNbFailed() { return this.nbFailed; }
    /** @see #nbFailed */ @JsonProperty("nbFailed")  public void setNbFailed(Number nbFailed) { this.nbFailed = nbFailed; }

    /** @see #benchmark */ @JsonProperty("benchmark")  public ArrayList<String> getBenchmark() { return this.benchmark; }
    /** @see #benchmark */ @JsonProperty("benchmark")  public void setBenchmark(ArrayList<String> benchmark) { this.benchmark = benchmark; }


    public static final Boolean isRuleExecutionResult(Object obj) { return (obj.getClass() == RuleExecutionResult.class); }

}
