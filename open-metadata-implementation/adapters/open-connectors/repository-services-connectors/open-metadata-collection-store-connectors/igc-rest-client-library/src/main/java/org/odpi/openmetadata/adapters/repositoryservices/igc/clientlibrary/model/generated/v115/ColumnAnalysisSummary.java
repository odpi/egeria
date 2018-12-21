/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'column_analysis_summary' asset type in IGC, displayed as 'Column Analysis Summary' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ColumnAnalysisSummary extends Reference {

    public static String getIgcTypeId() { return "column_analysis_summary"; }
    public static String getIgcTypeDisplayName() { return "Column Analysis Summary"; }

    /**
     * The 'short_&amp;_long_description' property, displayed as 'Short &amp; Long Description' in the IGC UI.
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
     * The 'assigned_to_terms' property, displayed as 'Assigned to Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The 'steward' property, displayed as 'Steward' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Steward} objects.
     */
    protected ReferenceList steward;

    /**
     * The 'analyzed_column' property, displayed as 'Analyzed Column' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataItem} object.
     */
    protected Reference analyzed_column;

    /**
     * The 'project_name' property, displayed as 'Project Name' in the IGC UI.
     */
    protected String project_name;

    /**
     * The 'review_date' property, displayed as 'Review Date' in the IGC UI.
     */
    protected Date review_date;

    /**
     * The 'promoted_by_principal' property, displayed as 'Promoted By Principal' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Steward} object.
     */
    protected Reference promoted_by_principal;

    /**
     * The 'reviewed_by_principal' property, displayed as 'Reviewed By Principal' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Steward} object.
     */
    protected Reference reviewed_by_principal;

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

    /**
     * The 'allow_null_values' property, displayed as 'Allow Null Values' in the IGC UI.
     */
    protected Boolean allow_null_values;

    /**
     * The 'require_unique_values' property, displayed as 'Require Unique Values' in the IGC UI.
     */
    protected Boolean require_unique_values;

    /**
     * The 'number_values' property, displayed as 'Number Values' in the IGC UI.
     */
    protected Number number_values;

    /**
     * The 'number_of_complete_values' property, displayed as 'Number of Complete Values' in the IGC UI.
     */
    protected Number number_of_complete_values;

    /**
     * The 'number_of_valid_values' property, displayed as 'Number of Valid Values' in the IGC UI.
     */
    protected Number number_of_valid_values;

    /**
     * The 'number_of_empty_values' property, displayed as 'Number of Empty Values' in the IGC UI.
     */
    protected Number number_of_empty_values;

    /**
     * The 'number_of_null_values' property, displayed as 'Number of Null Values' in the IGC UI.
     */
    protected Number number_of_null_values;

    /**
     * The 'number_of_distinct_values' property, displayed as 'Number of Distinct Values' in the IGC UI.
     */
    protected Number number_of_distinct_values;

    /**
     * The 'number_of_distinct_patterns' property, displayed as 'Number of Distinct Patterns' in the IGC UI.
     */
    protected Number number_of_distinct_patterns;

    /**
     * The 'number_of_distinct_formats' property, displayed as 'Number of Distinct Formats' in the IGC UI.
     */
    protected Number number_of_distinct_formats;

    /**
     * The 'inferred_data_type' property, displayed as 'Inferred Data Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>INT8 (displayed in the UI as 'INT8')</li>
     *     <li>INT16 (displayed in the UI as 'INT16')</li>
     *     <li>INT32 (displayed in the UI as 'INT32')</li>
     *     <li>INT64 (displayed in the UI as 'INT64')</li>
     *     <li>SFLOAT (displayed in the UI as 'SFLOAT')</li>
     *     <li>DFLOAT (displayed in the UI as 'DFLOAT')</li>
     *     <li>QFLOAT (displayed in the UI as 'QFLOAT')</li>
     *     <li>DECIMAL (displayed in the UI as 'DECIMAL')</li>
     *     <li>STRING (displayed in the UI as 'STRING')</li>
     *     <li>BINARY (displayed in the UI as 'BINARY')</li>
     *     <li>BOOLEAN (displayed in the UI as 'BOOLEAN')</li>
     *     <li>DATE (displayed in the UI as 'DATE')</li>
     *     <li>TIME (displayed in the UI as 'TIME')</li>
     *     <li>DATETIME (displayed in the UI as 'DATETIME')</li>
     *     <li>DURATION (displayed in the UI as 'DURATION')</li>
     *     <li>CHOICE (displayed in the UI as 'CHOICE')</li>
     *     <li>ORDERED_GROUP (displayed in the UI as 'ORDERED_GROUP')</li>
     *     <li>UNORDERED_GROUP (displayed in the UI as 'UNORDERED_GROUP')</li>
     *     <li>GUID (displayed in the UI as 'GUID')</li>
     *     <li>UNKNOWN (displayed in the UI as 'UNKNOWN')</li>
     *     <li>JSON (displayed in the UI as 'JSON')</li>
     *     <li>XML (displayed in the UI as 'XML')</li>
     * </ul>
     */
    protected String inferred_data_type;

    /**
     * The 'inferred_format' property, displayed as 'Inferred Format' in the IGC UI.
     */
    protected String inferred_format;

    /**
     * The 'mask' property, displayed as 'Mask' in the IGC UI.
     */
    protected String mask;

    /**
     * The 'constant' property, displayed as 'Constant' in the IGC UI.
     */
    protected Boolean constant;

    /**
     * The 'domain_type' property, displayed as 'Domain Type' in the IGC UI.
     */
    protected String domain_type;

    /**
     * The 'average_length' property, displayed as 'Average Length' in the IGC UI.
     */
    protected String average_length;

    /**
     * The 'longest_length' property, displayed as 'Longest Length' in the IGC UI.
     */
    protected String longest_length;

    /**
     * The 'shortest_length' property, displayed as 'Shortest Length' in the IGC UI.
     */
    protected String shortest_length;

    /**
     * The 'inferred_length' property, displayed as 'Inferred Length' in the IGC UI.
     */
    protected Number inferred_length;

    /**
     * The 'inferred_scale' property, displayed as 'Inferred Scale' in the IGC UI.
     */
    protected Number inferred_scale;

    /**
     * The 'inferred_precision' property, displayed as 'Inferred Precision' in the IGC UI.
     */
    protected Number inferred_precision;


    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public String getShortLongDescription() { return this.short___long_description; }
    /** @see #short___long_description */ @JsonProperty("short_&_long_description")  public void setShortLongDescription(String short___long_description) { this.short___long_description = short___long_description; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #steward */ @JsonProperty("steward")  public ReferenceList getSteward() { return this.steward; }
    /** @see #steward */ @JsonProperty("steward")  public void setSteward(ReferenceList steward) { this.steward = steward; }

    /** @see #analyzed_column */ @JsonProperty("analyzed_column")  public Reference getAnalyzedColumn() { return this.analyzed_column; }
    /** @see #analyzed_column */ @JsonProperty("analyzed_column")  public void setAnalyzedColumn(Reference analyzed_column) { this.analyzed_column = analyzed_column; }

    /** @see #project_name */ @JsonProperty("project_name")  public String getProjectName() { return this.project_name; }
    /** @see #project_name */ @JsonProperty("project_name")  public void setProjectName(String project_name) { this.project_name = project_name; }

    /** @see #review_date */ @JsonProperty("review_date")  public Date getReviewDate() { return this.review_date; }
    /** @see #review_date */ @JsonProperty("review_date")  public void setReviewDate(Date review_date) { this.review_date = review_date; }

    /** @see #promoted_by_principal */ @JsonProperty("promoted_by_principal")  public Reference getPromotedByPrincipal() { return this.promoted_by_principal; }
    /** @see #promoted_by_principal */ @JsonProperty("promoted_by_principal")  public void setPromotedByPrincipal(Reference promoted_by_principal) { this.promoted_by_principal = promoted_by_principal; }

    /** @see #reviewed_by_principal */ @JsonProperty("reviewed_by_principal")  public Reference getReviewedByPrincipal() { return this.reviewed_by_principal; }
    /** @see #reviewed_by_principal */ @JsonProperty("reviewed_by_principal")  public void setReviewedByPrincipal(Reference reviewed_by_principal) { this.reviewed_by_principal = reviewed_by_principal; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    /** @see #allow_null_values */ @JsonProperty("allow_null_values")  public Boolean getAllowNullValues() { return this.allow_null_values; }
    /** @see #allow_null_values */ @JsonProperty("allow_null_values")  public void setAllowNullValues(Boolean allow_null_values) { this.allow_null_values = allow_null_values; }

    /** @see #require_unique_values */ @JsonProperty("require_unique_values")  public Boolean getRequireUniqueValues() { return this.require_unique_values; }
    /** @see #require_unique_values */ @JsonProperty("require_unique_values")  public void setRequireUniqueValues(Boolean require_unique_values) { this.require_unique_values = require_unique_values; }

    /** @see #number_values */ @JsonProperty("number_values")  public Number getNumberValues() { return this.number_values; }
    /** @see #number_values */ @JsonProperty("number_values")  public void setNumberValues(Number number_values) { this.number_values = number_values; }

    /** @see #number_of_complete_values */ @JsonProperty("number_of_complete_values")  public Number getNumberOfCompleteValues() { return this.number_of_complete_values; }
    /** @see #number_of_complete_values */ @JsonProperty("number_of_complete_values")  public void setNumberOfCompleteValues(Number number_of_complete_values) { this.number_of_complete_values = number_of_complete_values; }

    /** @see #number_of_valid_values */ @JsonProperty("number_of_valid_values")  public Number getNumberOfValidValues() { return this.number_of_valid_values; }
    /** @see #number_of_valid_values */ @JsonProperty("number_of_valid_values")  public void setNumberOfValidValues(Number number_of_valid_values) { this.number_of_valid_values = number_of_valid_values; }

    /** @see #number_of_empty_values */ @JsonProperty("number_of_empty_values")  public Number getNumberOfEmptyValues() { return this.number_of_empty_values; }
    /** @see #number_of_empty_values */ @JsonProperty("number_of_empty_values")  public void setNumberOfEmptyValues(Number number_of_empty_values) { this.number_of_empty_values = number_of_empty_values; }

    /** @see #number_of_null_values */ @JsonProperty("number_of_null_values")  public Number getNumberOfNullValues() { return this.number_of_null_values; }
    /** @see #number_of_null_values */ @JsonProperty("number_of_null_values")  public void setNumberOfNullValues(Number number_of_null_values) { this.number_of_null_values = number_of_null_values; }

    /** @see #number_of_distinct_values */ @JsonProperty("number_of_distinct_values")  public Number getNumberOfDistinctValues() { return this.number_of_distinct_values; }
    /** @see #number_of_distinct_values */ @JsonProperty("number_of_distinct_values")  public void setNumberOfDistinctValues(Number number_of_distinct_values) { this.number_of_distinct_values = number_of_distinct_values; }

    /** @see #number_of_distinct_patterns */ @JsonProperty("number_of_distinct_patterns")  public Number getNumberOfDistinctPatterns() { return this.number_of_distinct_patterns; }
    /** @see #number_of_distinct_patterns */ @JsonProperty("number_of_distinct_patterns")  public void setNumberOfDistinctPatterns(Number number_of_distinct_patterns) { this.number_of_distinct_patterns = number_of_distinct_patterns; }

    /** @see #number_of_distinct_formats */ @JsonProperty("number_of_distinct_formats")  public Number getNumberOfDistinctFormats() { return this.number_of_distinct_formats; }
    /** @see #number_of_distinct_formats */ @JsonProperty("number_of_distinct_formats")  public void setNumberOfDistinctFormats(Number number_of_distinct_formats) { this.number_of_distinct_formats = number_of_distinct_formats; }

    /** @see #inferred_data_type */ @JsonProperty("inferred_data_type")  public String getInferredDataType() { return this.inferred_data_type; }
    /** @see #inferred_data_type */ @JsonProperty("inferred_data_type")  public void setInferredDataType(String inferred_data_type) { this.inferred_data_type = inferred_data_type; }

    /** @see #inferred_format */ @JsonProperty("inferred_format")  public String getInferredFormat() { return this.inferred_format; }
    /** @see #inferred_format */ @JsonProperty("inferred_format")  public void setInferredFormat(String inferred_format) { this.inferred_format = inferred_format; }

    /** @see #mask */ @JsonProperty("mask")  public String getMask() { return this.mask; }
    /** @see #mask */ @JsonProperty("mask")  public void setMask(String mask) { this.mask = mask; }

    /** @see #constant */ @JsonProperty("constant")  public Boolean getConstant() { return this.constant; }
    /** @see #constant */ @JsonProperty("constant")  public void setConstant(Boolean constant) { this.constant = constant; }

    /** @see #domain_type */ @JsonProperty("domain_type")  public String getDomainType() { return this.domain_type; }
    /** @see #domain_type */ @JsonProperty("domain_type")  public void setDomainType(String domain_type) { this.domain_type = domain_type; }

    /** @see #average_length */ @JsonProperty("average_length")  public String getAverageLength() { return this.average_length; }
    /** @see #average_length */ @JsonProperty("average_length")  public void setAverageLength(String average_length) { this.average_length = average_length; }

    /** @see #longest_length */ @JsonProperty("longest_length")  public String getLongestLength() { return this.longest_length; }
    /** @see #longest_length */ @JsonProperty("longest_length")  public void setLongestLength(String longest_length) { this.longest_length = longest_length; }

    /** @see #shortest_length */ @JsonProperty("shortest_length")  public String getShortestLength() { return this.shortest_length; }
    /** @see #shortest_length */ @JsonProperty("shortest_length")  public void setShortestLength(String shortest_length) { this.shortest_length = shortest_length; }

    /** @see #inferred_length */ @JsonProperty("inferred_length")  public Number getInferredLength() { return this.inferred_length; }
    /** @see #inferred_length */ @JsonProperty("inferred_length")  public void setInferredLength(Number inferred_length) { this.inferred_length = inferred_length; }

    /** @see #inferred_scale */ @JsonProperty("inferred_scale")  public Number getInferredScale() { return this.inferred_scale; }
    /** @see #inferred_scale */ @JsonProperty("inferred_scale")  public void setInferredScale(Number inferred_scale) { this.inferred_scale = inferred_scale; }

    /** @see #inferred_precision */ @JsonProperty("inferred_precision")  public Number getInferredPrecision() { return this.inferred_precision; }
    /** @see #inferred_precision */ @JsonProperty("inferred_precision")  public void setInferredPrecision(Number inferred_precision) { this.inferred_precision = inferred_precision; }

    public static final Boolean canBeCreated() { return false; }
    public static final Boolean includesModificationDetails() { return true; }
    public static final ArrayList<String> NON_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("short_&_long_description");
        add("short_description");
        add("long_description");
        add("project_name");
        add("review_date");
        add("created_by");
        add("created_on");
        add("modified_by");
        add("modified_on");
        add("allow_null_values");
        add("require_unique_values");
        add("number_values");
        add("number_of_complete_values");
        add("number_of_valid_values");
        add("number_of_empty_values");
        add("number_of_null_values");
        add("number_of_distinct_values");
        add("number_of_distinct_patterns");
        add("number_of_distinct_formats");
        add("inferred_data_type");
        add("inferred_format");
        add("mask");
        add("constant");
        add("domain_type");
        add("average_length");
        add("longest_length");
        add("shortest_length");
        add("inferred_length");
        add("inferred_scale");
        add("inferred_precision");
    }};
    public static final List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static final Boolean isColumnAnalysisSummary(Object obj) { return (obj.getClass() == ColumnAnalysisSummary.class); }

}
