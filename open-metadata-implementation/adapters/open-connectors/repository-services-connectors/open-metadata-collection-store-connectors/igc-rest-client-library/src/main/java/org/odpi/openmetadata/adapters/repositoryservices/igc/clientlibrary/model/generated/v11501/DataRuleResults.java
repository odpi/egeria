/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11501;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'data_rule_results' asset type in IGC, displayed as 'Data Rule Results' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataRuleResults extends Reference {

    public static String getIgcTypeId() { return "data_rule_results"; }
    public static String getIgcTypeDisplayName() { return "Data Rule Results"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

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
     * The 'rule_results' property, displayed as 'Rule Results' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList rule_results;

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

    /** @see #rule_results */ @JsonProperty("rule_results")  public ReferenceList getRuleResults() { return this.rule_results; }
    /** @see #rule_results */ @JsonProperty("rule_results")  public void setRuleResults(ReferenceList rule_results) { this.rule_results = rule_results; }

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
        "start_time",
        "end_time",
        "number_of_records_tested",
        "number_of_records_met",
        "number_of_records_not_met",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "rule_results"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "start_time",
        "end_time",
        "number_of_records_tested",
        "number_of_records_met",
        "number_of_records_not_met",
        "rule_results",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isDataRuleResults(Object obj) { return (obj.getClass() == DataRuleResults.class); }

}
