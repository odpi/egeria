/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'data_class' asset type in IGC, displayed as 'Data Class' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataClass extends MainObject {

    public static final String IGC_TYPE_ID = "data_class";

    /**
     * The 'parent_data_class' property, displayed as 'Parent Data Class' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataClass} object.
     */
    protected Reference parent_data_class;

    /**
     * The 'data_class_type_single' property, displayed as 'Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>Undefined (displayed in the UI as 'Unspecified')</li>
     *     <li>Regex (displayed in the UI as 'Regex')</li>
     *     <li>Java (displayed in the UI as 'Java')</li>
     *     <li>ValidValues (displayed in the UI as 'Valid Values')</li>
     * </ul>
     */
    protected String data_class_type_single;

    /**
     * The 'contains_data_classes' property, displayed as 'Contains Data Classes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataClass} objects.
     */
    protected ReferenceList contains_data_classes;

    /**
     * The 'classifications_selected' property, displayed as 'Selected Data Classifications' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Classificationenabledgroup} objects.
     */
    protected ReferenceList classifications_selected;

    /**
     * The 'classified_assets_detected' property, displayed as 'Detected Data Classifications' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Classification} objects.
     */
    protected ReferenceList classified_assets_detected;

    /**
     * The 'data_type_filter_elements_enum' property, displayed as 'Data Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>numeric (displayed in the UI as 'Numeric')</li>
     *     <li>string (displayed in the UI as 'String')</li>
     *     <li>date (displayed in the UI as 'Date')</li>
     *     <li>time (displayed in the UI as 'Time')</li>
     *     <li>timestamp (displayed in the UI as 'Timestamp')</li>
     * </ul>
     */
    protected ArrayList<String> data_type_filter_elements_enum;

    /**
     * The 'default_threshold' property, displayed as 'Threshold (Percent)' in the IGC UI.
     */
    protected Number default_threshold;

    /**
     * The 'length_filter_min' property, displayed as 'Minimum Data Length' in the IGC UI.
     */
    protected Number length_filter_min;

    /**
     * The 'length_filter_max' property, displayed as 'Maximum Data Length' in the IGC UI.
     */
    protected Number length_filter_max;

    /**
     * The 'java_class_name_single' property, displayed as 'JAVA Class' in the IGC UI.
     */
    protected String java_class_name_single;

    /**
     * The 'regular_expression_single' property, displayed as 'Regular Expression' in the IGC UI.
     */
    protected String regular_expression_single;

    /**
     * The 'valid_value_strings' property, displayed as 'Valid Values' in the IGC UI.
     */
    protected ArrayList<String> valid_value_strings;

    /**
     * The 'valid_values_case_sensitive' property, displayed as 'Case Sensitive' in the IGC UI.
     */
    protected Boolean valid_values_case_sensitive;

    /**
     * The 'squeezeConsecutiveWhiteSpaces' property, displayed as 'Collapse Consecutive White Spaces' in the IGC UI.
     */
    protected Boolean squeezeConsecutiveWhiteSpaces;

    /**
     * The 'columnNameMatch' property, displayed as 'Column Name Match' in the IGC UI.
     */
    protected String columnNameMatch;

    /**
     * The 'example' property, displayed as 'Example' in the IGC UI.
     */
    protected String example;

    /**
     * The 'enabled' property, displayed as 'Enabled' in the IGC UI.
     */
    protected Boolean enabled;

    /**
     * The 'class_code' property, displayed as 'Class Code' in the IGC UI.
     */
    protected String class_code;

    /**
     * The 'validValueReferenceFile' property, displayed as 'Valid Value Reference File' in the IGC UI.
     */
    protected String validValueReferenceFile;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #parent_data_class */ @JsonProperty("parent_data_class")  public Reference getParentDataClass() { return this.parent_data_class; }
    /** @see #parent_data_class */ @JsonProperty("parent_data_class")  public void setParentDataClass(Reference parent_data_class) { this.parent_data_class = parent_data_class; }

    /** @see #data_class_type_single */ @JsonProperty("data_class_type_single")  public String getDataClassTypeSingle() { return this.data_class_type_single; }
    /** @see #data_class_type_single */ @JsonProperty("data_class_type_single")  public void setDataClassTypeSingle(String data_class_type_single) { this.data_class_type_single = data_class_type_single; }

    /** @see #contains_data_classes */ @JsonProperty("contains_data_classes")  public ReferenceList getContainsDataClasses() { return this.contains_data_classes; }
    /** @see #contains_data_classes */ @JsonProperty("contains_data_classes")  public void setContainsDataClasses(ReferenceList contains_data_classes) { this.contains_data_classes = contains_data_classes; }

    /** @see #classifications_selected */ @JsonProperty("classifications_selected")  public ReferenceList getClassificationsSelected() { return this.classifications_selected; }
    /** @see #classifications_selected */ @JsonProperty("classifications_selected")  public void setClassificationsSelected(ReferenceList classifications_selected) { this.classifications_selected = classifications_selected; }

    /** @see #classified_assets_detected */ @JsonProperty("classified_assets_detected")  public ReferenceList getClassifiedAssetsDetected() { return this.classified_assets_detected; }
    /** @see #classified_assets_detected */ @JsonProperty("classified_assets_detected")  public void setClassifiedAssetsDetected(ReferenceList classified_assets_detected) { this.classified_assets_detected = classified_assets_detected; }

    /** @see #data_type_filter_elements_enum */ @JsonProperty("data_type_filter_elements_enum")  public ArrayList<String> getDataTypeFilterElementsEnum() { return this.data_type_filter_elements_enum; }
    /** @see #data_type_filter_elements_enum */ @JsonProperty("data_type_filter_elements_enum")  public void setDataTypeFilterElementsEnum(ArrayList<String> data_type_filter_elements_enum) { this.data_type_filter_elements_enum = data_type_filter_elements_enum; }

    /** @see #default_threshold */ @JsonProperty("default_threshold")  public Number getDefaultThreshold() { return this.default_threshold; }
    /** @see #default_threshold */ @JsonProperty("default_threshold")  public void setDefaultThreshold(Number default_threshold) { this.default_threshold = default_threshold; }

    /** @see #length_filter_min */ @JsonProperty("length_filter_min")  public Number getLengthFilterMin() { return this.length_filter_min; }
    /** @see #length_filter_min */ @JsonProperty("length_filter_min")  public void setLengthFilterMin(Number length_filter_min) { this.length_filter_min = length_filter_min; }

    /** @see #length_filter_max */ @JsonProperty("length_filter_max")  public Number getLengthFilterMax() { return this.length_filter_max; }
    /** @see #length_filter_max */ @JsonProperty("length_filter_max")  public void setLengthFilterMax(Number length_filter_max) { this.length_filter_max = length_filter_max; }

    /** @see #java_class_name_single */ @JsonProperty("java_class_name_single")  public String getJavaClassNameSingle() { return this.java_class_name_single; }
    /** @see #java_class_name_single */ @JsonProperty("java_class_name_single")  public void setJavaClassNameSingle(String java_class_name_single) { this.java_class_name_single = java_class_name_single; }

    /** @see #regular_expression_single */ @JsonProperty("regular_expression_single")  public String getRegularExpressionSingle() { return this.regular_expression_single; }
    /** @see #regular_expression_single */ @JsonProperty("regular_expression_single")  public void setRegularExpressionSingle(String regular_expression_single) { this.regular_expression_single = regular_expression_single; }

    /** @see #valid_value_strings */ @JsonProperty("valid_value_strings")  public ArrayList<String> getValidValueStrings() { return this.valid_value_strings; }
    /** @see #valid_value_strings */ @JsonProperty("valid_value_strings")  public void setValidValueStrings(ArrayList<String> valid_value_strings) { this.valid_value_strings = valid_value_strings; }

    /** @see #valid_values_case_sensitive */ @JsonProperty("valid_values_case_sensitive")  public Boolean getValidValuesCaseSensitive() { return this.valid_values_case_sensitive; }
    /** @see #valid_values_case_sensitive */ @JsonProperty("valid_values_case_sensitive")  public void setValidValuesCaseSensitive(Boolean valid_values_case_sensitive) { this.valid_values_case_sensitive = valid_values_case_sensitive; }

    /** @see #squeezeConsecutiveWhiteSpaces */ @JsonProperty("squeezeConsecutiveWhiteSpaces")  public Boolean getSqueezeConsecutiveWhiteSpaces() { return this.squeezeConsecutiveWhiteSpaces; }
    /** @see #squeezeConsecutiveWhiteSpaces */ @JsonProperty("squeezeConsecutiveWhiteSpaces")  public void setSqueezeConsecutiveWhiteSpaces(Boolean squeezeConsecutiveWhiteSpaces) { this.squeezeConsecutiveWhiteSpaces = squeezeConsecutiveWhiteSpaces; }

    /** @see #columnNameMatch */ @JsonProperty("columnNameMatch")  public String getColumnNameMatch() { return this.columnNameMatch; }
    /** @see #columnNameMatch */ @JsonProperty("columnNameMatch")  public void setColumnNameMatch(String columnNameMatch) { this.columnNameMatch = columnNameMatch; }

    /** @see #example */ @JsonProperty("example")  public String getExample() { return this.example; }
    /** @see #example */ @JsonProperty("example")  public void setExample(String example) { this.example = example; }

    /** @see #enabled */ @JsonProperty("enabled")  public Boolean getEnabled() { return this.enabled; }
    /** @see #enabled */ @JsonProperty("enabled")  public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    /** @see #class_code */ @JsonProperty("class_code")  public String getClassCode() { return this.class_code; }
    /** @see #class_code */ @JsonProperty("class_code")  public void setClassCode(String class_code) { this.class_code = class_code; }

    /** @see #validValueReferenceFile */ @JsonProperty("validValueReferenceFile")  public String getValidValueReferenceFile() { return this.validValueReferenceFile; }
    /** @see #validValueReferenceFile */ @JsonProperty("validValueReferenceFile")  public void setValidValueReferenceFile(String validValueReferenceFile) { this.validValueReferenceFile = validValueReferenceFile; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isDataClass(Object obj) { return (obj.getClass() == DataClass.class); }

}
