/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701sp1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code field_analysis} asset type in IGC, displayed as '{@literal Field Analysis}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("field_analysis")
public class FieldAnalysis extends Reference {

    public static String getIgcTypeDisplayName() { return "Field Analysis"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code table_analysis} property, displayed as '{@literal Table Analysis}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference table_analysis;

    /**
     * The {@code project} property, displayed as '{@literal Project}' in the IGC UI.
     */
    protected String project;

    /**
     * The {@code data_file_field} property, displayed as '{@literal Data File Field}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataFileField} object.
     */
    protected Reference data_file_field;

    /**
     * The {@code require_unique_values} property, displayed as '{@literal Require Unique Values}' in the IGC UI.
     */
    protected Boolean require_unique_values;

    /**
     * The {@code include_null_values} property, displayed as '{@literal Include Null Values}' in the IGC UI.
     */
    protected Boolean include_null_values;

    /**
     * The {@code include_constant_values} property, displayed as '{@literal Include Constant Values}' in the IGC UI.
     */
    protected Boolean include_constant_values;

    /**
     * The {@code domain} property, displayed as '{@literal Domain}' in the IGC UI.
     */
    protected String domain;

    /**
     * The {@code number_of_complete_values} property, displayed as '{@literal Number of Complete Values}' in the IGC UI.
     */
    protected Number number_of_complete_values;

    /**
     * The {@code number_of_valid_values} property, displayed as '{@literal Number of Valid Values}' in the IGC UI.
     */
    protected Number number_of_valid_values;

    /**
     * The {@code number_of_empty_values} property, displayed as '{@literal Number of Empty Values}' in the IGC UI.
     */
    protected Number number_of_empty_values;

    /**
     * The {@code number_of_null_values} property, displayed as '{@literal Number of Null Values}' in the IGC UI.
     */
    protected Number number_of_null_values;

    /**
     * The {@code number_of_distinct_values} property, displayed as '{@literal Number of Distinct Values}' in the IGC UI.
     */
    protected Number number_of_distinct_values;

    /**
     * The {@code number_of_distinct_formats} property, displayed as '{@literal Number of Distinct Formats}' in the IGC UI.
     */
    protected Number number_of_distinct_formats;

    /**
     * The {@code number_of_zero_values} property, displayed as '{@literal Number of Zero Values}' in the IGC UI.
     */
    protected Number number_of_zero_values;

    /**
     * The {@code inferred_data_type} property, displayed as '{@literal Inferred Data Type}' in the IGC UI.
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
     * The {@code inferred_length} property, displayed as '{@literal Inferred Length}' in the IGC UI.
     */
    protected Number inferred_length;

    /**
     * The {@code inferred_format} property, displayed as '{@literal Inferred Format}' in the IGC UI.
     */
    protected String inferred_format;

    /**
     * The {@code inferred_scale} property, displayed as '{@literal Inferred Scale}' in the IGC UI.
     */
    protected Number inferred_scale;

    /**
     * The {@code inferred_precision} property, displayed as '{@literal Inferred Precision}' in the IGC UI.
     */
    protected Number inferred_precision;

    /**
     * The {@code average_value} property, displayed as '{@literal Average Value}' in the IGC UI.
     */
    protected String average_value;

    /**
     * The {@code inferred_foreign_key} property, displayed as '{@literal Inferred Foreign Key}' in the IGC UI.
     */
    protected Boolean inferred_foreign_key;

    /**
     * The {@code inferred_primary_key} property, displayed as '{@literal Inferred Primary Key}' in the IGC UI.
     */
    protected Boolean inferred_primary_key;

    /**
     * The {@code classification} property, displayed as '{@literal Detected Data Classifications}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Classification} objects.
     */
    protected ReferenceList classification;

    /**
     * The {@code selected_primary_key} property, displayed as '{@literal User Selected Primary Key}' in the IGC UI.
     */
    protected Boolean selected_primary_key;

    /**
     * The {@code selected_natural_key} property, displayed as '{@literal User Selected Natural Key}' in the IGC UI.
     */
    protected Boolean selected_natural_key;

    /**
     * The {@code selected_foreign_key} property, displayed as '{@literal User Selected Foreign Key}' in the IGC UI.
     */
    protected Boolean selected_foreign_key;

    /**
     * The {@code selected_foreign_key_references} property, displayed as '{@literal User Selected Foreign Key References}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList selected_foreign_key_references;

    /**
     * The {@code selected_foreign_key_referenced} property, displayed as '{@literal User Selected Foreign Key Referenced}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList selected_foreign_key_referenced;

    /**
     * The {@code validation_type} property, displayed as '{@literal Validation Type}' in the IGC UI.
     */
    protected ArrayList<String> validation_type;

    /**
     * The {@code validation_properties} property, displayed as '{@literal Validation Properties}' in the IGC UI.
     */
    protected ArrayList<String> validation_properties;

    /**
     * The {@code quality_score_percent} property, displayed as '{@literal Quality Score}' in the IGC UI.
     */
    protected String quality_score_percent;

    /**
     * The {@code nb_records_tested} property, displayed as '{@literal Number of Records Tested}' in the IGC UI.
     */
    protected Number nb_records_tested;

    /**
     * The {@code quality_score_problems} property, displayed as '{@literal Quality Score Problems}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link QualityProblem} objects.
     */
    protected ReferenceList quality_score_problems;

    /**
     * The {@code created_by} property, displayed as '{@literal Created By}' in the IGC UI.
     */
    protected String created_by;

    /**
     * The {@code created_on} property, displayed as '{@literal Created On}' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The {@code modified_by} property, displayed as '{@literal Modified By}' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The {@code modified_on} property, displayed as '{@literal Modified On}' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #table_analysis */ @JsonProperty("table_analysis")  public Reference getTableAnalysis() { return this.table_analysis; }
    /** @see #table_analysis */ @JsonProperty("table_analysis")  public void setTableAnalysis(Reference table_analysis) { this.table_analysis = table_analysis; }

    /** @see #project */ @JsonProperty("project")  public String getProject() { return this.project; }
    /** @see #project */ @JsonProperty("project")  public void setProject(String project) { this.project = project; }

    /** @see #data_file_field */ @JsonProperty("data_file_field")  public Reference getDataFileField() { return this.data_file_field; }
    /** @see #data_file_field */ @JsonProperty("data_file_field")  public void setDataFileField(Reference data_file_field) { this.data_file_field = data_file_field; }

    /** @see #require_unique_values */ @JsonProperty("require_unique_values")  public Boolean getRequireUniqueValues() { return this.require_unique_values; }
    /** @see #require_unique_values */ @JsonProperty("require_unique_values")  public void setRequireUniqueValues(Boolean require_unique_values) { this.require_unique_values = require_unique_values; }

    /** @see #include_null_values */ @JsonProperty("include_null_values")  public Boolean getIncludeNullValues() { return this.include_null_values; }
    /** @see #include_null_values */ @JsonProperty("include_null_values")  public void setIncludeNullValues(Boolean include_null_values) { this.include_null_values = include_null_values; }

    /** @see #include_constant_values */ @JsonProperty("include_constant_values")  public Boolean getIncludeConstantValues() { return this.include_constant_values; }
    /** @see #include_constant_values */ @JsonProperty("include_constant_values")  public void setIncludeConstantValues(Boolean include_constant_values) { this.include_constant_values = include_constant_values; }

    /** @see #domain */ @JsonProperty("domain")  public String getDomain() { return this.domain; }
    /** @see #domain */ @JsonProperty("domain")  public void setDomain(String domain) { this.domain = domain; }

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

    /** @see #number_of_distinct_formats */ @JsonProperty("number_of_distinct_formats")  public Number getNumberOfDistinctFormats() { return this.number_of_distinct_formats; }
    /** @see #number_of_distinct_formats */ @JsonProperty("number_of_distinct_formats")  public void setNumberOfDistinctFormats(Number number_of_distinct_formats) { this.number_of_distinct_formats = number_of_distinct_formats; }

    /** @see #number_of_zero_values */ @JsonProperty("number_of_zero_values")  public Number getNumberOfZeroValues() { return this.number_of_zero_values; }
    /** @see #number_of_zero_values */ @JsonProperty("number_of_zero_values")  public void setNumberOfZeroValues(Number number_of_zero_values) { this.number_of_zero_values = number_of_zero_values; }

    /** @see #inferred_data_type */ @JsonProperty("inferred_data_type")  public String getInferredDataType() { return this.inferred_data_type; }
    /** @see #inferred_data_type */ @JsonProperty("inferred_data_type")  public void setInferredDataType(String inferred_data_type) { this.inferred_data_type = inferred_data_type; }

    /** @see #inferred_length */ @JsonProperty("inferred_length")  public Number getInferredLength() { return this.inferred_length; }
    /** @see #inferred_length */ @JsonProperty("inferred_length")  public void setInferredLength(Number inferred_length) { this.inferred_length = inferred_length; }

    /** @see #inferred_format */ @JsonProperty("inferred_format")  public String getInferredFormat() { return this.inferred_format; }
    /** @see #inferred_format */ @JsonProperty("inferred_format")  public void setInferredFormat(String inferred_format) { this.inferred_format = inferred_format; }

    /** @see #inferred_scale */ @JsonProperty("inferred_scale")  public Number getInferredScale() { return this.inferred_scale; }
    /** @see #inferred_scale */ @JsonProperty("inferred_scale")  public void setInferredScale(Number inferred_scale) { this.inferred_scale = inferred_scale; }

    /** @see #inferred_precision */ @JsonProperty("inferred_precision")  public Number getInferredPrecision() { return this.inferred_precision; }
    /** @see #inferred_precision */ @JsonProperty("inferred_precision")  public void setInferredPrecision(Number inferred_precision) { this.inferred_precision = inferred_precision; }

    /** @see #average_value */ @JsonProperty("average_value")  public String getAverageValue() { return this.average_value; }
    /** @see #average_value */ @JsonProperty("average_value")  public void setAverageValue(String average_value) { this.average_value = average_value; }

    /** @see #inferred_foreign_key */ @JsonProperty("inferred_foreign_key")  public Boolean getInferredForeignKey() { return this.inferred_foreign_key; }
    /** @see #inferred_foreign_key */ @JsonProperty("inferred_foreign_key")  public void setInferredForeignKey(Boolean inferred_foreign_key) { this.inferred_foreign_key = inferred_foreign_key; }

    /** @see #inferred_primary_key */ @JsonProperty("inferred_primary_key")  public Boolean getInferredPrimaryKey() { return this.inferred_primary_key; }
    /** @see #inferred_primary_key */ @JsonProperty("inferred_primary_key")  public void setInferredPrimaryKey(Boolean inferred_primary_key) { this.inferred_primary_key = inferred_primary_key; }

    /** @see #classification */ @JsonProperty("classification")  public ReferenceList getClassification() { return this.classification; }
    /** @see #classification */ @JsonProperty("classification")  public void setClassification(ReferenceList classification) { this.classification = classification; }

    /** @see #selected_primary_key */ @JsonProperty("selected_primary_key")  public Boolean getSelectedPrimaryKey() { return this.selected_primary_key; }
    /** @see #selected_primary_key */ @JsonProperty("selected_primary_key")  public void setSelectedPrimaryKey(Boolean selected_primary_key) { this.selected_primary_key = selected_primary_key; }

    /** @see #selected_natural_key */ @JsonProperty("selected_natural_key")  public Boolean getSelectedNaturalKey() { return this.selected_natural_key; }
    /** @see #selected_natural_key */ @JsonProperty("selected_natural_key")  public void setSelectedNaturalKey(Boolean selected_natural_key) { this.selected_natural_key = selected_natural_key; }

    /** @see #selected_foreign_key */ @JsonProperty("selected_foreign_key")  public Boolean getSelectedForeignKey() { return this.selected_foreign_key; }
    /** @see #selected_foreign_key */ @JsonProperty("selected_foreign_key")  public void setSelectedForeignKey(Boolean selected_foreign_key) { this.selected_foreign_key = selected_foreign_key; }

    /** @see #selected_foreign_key_references */ @JsonProperty("selected_foreign_key_references")  public ReferenceList getSelectedForeignKeyReferences() { return this.selected_foreign_key_references; }
    /** @see #selected_foreign_key_references */ @JsonProperty("selected_foreign_key_references")  public void setSelectedForeignKeyReferences(ReferenceList selected_foreign_key_references) { this.selected_foreign_key_references = selected_foreign_key_references; }

    /** @see #selected_foreign_key_referenced */ @JsonProperty("selected_foreign_key_referenced")  public ReferenceList getSelectedForeignKeyReferenced() { return this.selected_foreign_key_referenced; }
    /** @see #selected_foreign_key_referenced */ @JsonProperty("selected_foreign_key_referenced")  public void setSelectedForeignKeyReferenced(ReferenceList selected_foreign_key_referenced) { this.selected_foreign_key_referenced = selected_foreign_key_referenced; }

    /** @see #validation_type */ @JsonProperty("validation_type")  public ArrayList<String> getValidationType() { return this.validation_type; }
    /** @see #validation_type */ @JsonProperty("validation_type")  public void setValidationType(ArrayList<String> validation_type) { this.validation_type = validation_type; }

    /** @see #validation_properties */ @JsonProperty("validation_properties")  public ArrayList<String> getValidationProperties() { return this.validation_properties; }
    /** @see #validation_properties */ @JsonProperty("validation_properties")  public void setValidationProperties(ArrayList<String> validation_properties) { this.validation_properties = validation_properties; }

    /** @see #quality_score_percent */ @JsonProperty("quality_score_percent")  public String getQualityScorePercent() { return this.quality_score_percent; }
    /** @see #quality_score_percent */ @JsonProperty("quality_score_percent")  public void setQualityScorePercent(String quality_score_percent) { this.quality_score_percent = quality_score_percent; }

    /** @see #nb_records_tested */ @JsonProperty("nb_records_tested")  public Number getNbRecordsTested() { return this.nb_records_tested; }
    /** @see #nb_records_tested */ @JsonProperty("nb_records_tested")  public void setNbRecordsTested(Number nb_records_tested) { this.nb_records_tested = nb_records_tested; }

    /** @see #quality_score_problems */ @JsonProperty("quality_score_problems")  public ReferenceList getQualityScoreProblems() { return this.quality_score_problems; }
    /** @see #quality_score_problems */ @JsonProperty("quality_score_problems")  public void setQualityScoreProblems(ReferenceList quality_score_problems) { this.quality_score_problems = quality_score_problems; }

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
        "project",
        "require_unique_values",
        "include_null_values",
        "include_constant_values",
        "domain",
        "number_of_complete_values",
        "number_of_valid_values",
        "number_of_empty_values",
        "number_of_null_values",
        "number_of_distinct_values",
        "number_of_distinct_formats",
        "number_of_zero_values",
        "inferred_data_type",
        "inferred_length",
        "inferred_format",
        "inferred_scale",
        "inferred_precision",
        "average_value",
        "inferred_foreign_key",
        "inferred_primary_key",
        "selected_primary_key",
        "selected_natural_key",
        "selected_foreign_key",
        "validation_type",
        "validation_properties",
        "quality_score_percent",
        "nb_records_tested",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "project",
        "domain",
        "inferred_format",
        "average_value",
        "validation_type",
        "validation_properties",
        "quality_score_percent",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "classification",
        "selected_foreign_key_references",
        "selected_foreign_key_referenced",
        "quality_score_problems"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "table_analysis",
        "project",
        "data_file_field",
        "require_unique_values",
        "include_null_values",
        "include_constant_values",
        "domain",
        "number_of_complete_values",
        "number_of_valid_values",
        "number_of_empty_values",
        "number_of_null_values",
        "number_of_distinct_values",
        "number_of_distinct_formats",
        "number_of_zero_values",
        "inferred_data_type",
        "inferred_length",
        "inferred_format",
        "inferred_scale",
        "inferred_precision",
        "average_value",
        "inferred_foreign_key",
        "inferred_primary_key",
        "classification",
        "selected_primary_key",
        "selected_natural_key",
        "selected_foreign_key",
        "selected_foreign_key_references",
        "selected_foreign_key_referenced",
        "validation_type",
        "validation_properties",
        "quality_score_percent",
        "nb_records_tested",
        "quality_score_problems",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isFieldAnalysis(Object obj) { return (obj.getClass() == FieldAnalysis.class); }

}
