/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * POJO for the 'data_rule_results' asset type in IGC, displayed as 'Data Rule Results' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataRuleResults extends MainObject {

    public static final String IGC_TYPE_ID = "data_rule_results";

    /**
     * The 'start_time' property, displayed as 'Start Time' in the IGC UI.
     */
    protected Date start_time;

    /**
     * The 'end_time' property, displayed as 'End Time' in the IGC UI.
     */
    protected Date end_time;

    /**
     * The 'number_of_records_tested' property, displayed as 'Number of Records Tested' in the IGC UI.
     */
    protected Number number_of_records_tested;

    /**
     * The 'number_of_records_met' property, displayed as 'Number of Records Met' in the IGC UI.
     */
    protected Number number_of_records_met;

    /**
     * The 'number_of_records_not_met' property, displayed as 'Number of Records Not Met' in the IGC UI.
     */
    protected Number number_of_records_not_met;

    /**
     * The 'benchmark' property, displayed as 'Benchmark' in the IGC UI.
     */
    protected String benchmark;

    /**
     * The 'rule_results' property, displayed as 'Rule Results' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link RuleExecutionResult} objects.
     */
    protected ReferenceList rule_results;


    /** @see #start_time */ @JsonProperty("start_time")  public Date getStartTime() { return this.start_time; }
    /** @see #start_time */ @JsonProperty("start_time")  public void setStartTime(Date start_time) { this.start_time = start_time; }

    /** @see #end_time */ @JsonProperty("end_time")  public Date getEndTime() { return this.end_time; }
    /** @see #end_time */ @JsonProperty("end_time")  public void setEndTime(Date end_time) { this.end_time = end_time; }

    /** @see #number_of_records_tested */ @JsonProperty("number_of_records_tested")  public Number getNumberOfRecordsTested() { return this.number_of_records_tested; }
    /** @see #number_of_records_tested */ @JsonProperty("number_of_records_tested")  public void setNumberOfRecordsTested(Number number_of_records_tested) { this.number_of_records_tested = number_of_records_tested; }

    /** @see #number_of_records_met */ @JsonProperty("number_of_records_met")  public Number getNumberOfRecordsMet() { return this.number_of_records_met; }
    /** @see #number_of_records_met */ @JsonProperty("number_of_records_met")  public void setNumberOfRecordsMet(Number number_of_records_met) { this.number_of_records_met = number_of_records_met; }

    /** @see #number_of_records_not_met */ @JsonProperty("number_of_records_not_met")  public Number getNumberOfRecordsNotMet() { return this.number_of_records_not_met; }
    /** @see #number_of_records_not_met */ @JsonProperty("number_of_records_not_met")  public void setNumberOfRecordsNotMet(Number number_of_records_not_met) { this.number_of_records_not_met = number_of_records_not_met; }

    /** @see #benchmark */ @JsonProperty("benchmark")  public String getBenchmark() { return this.benchmark; }
    /** @see #benchmark */ @JsonProperty("benchmark")  public void setBenchmark(String benchmark) { this.benchmark = benchmark; }

    /** @see #rule_results */ @JsonProperty("rule_results")  public ReferenceList getRuleResults() { return this.rule_results; }
    /** @see #rule_results */ @JsonProperty("rule_results")  public void setRuleResults(ReferenceList rule_results) { this.rule_results = rule_results; }


    public static final Boolean isDataRuleResults(Object obj) { return (obj.getClass() == DataRuleResults.class); }

}
