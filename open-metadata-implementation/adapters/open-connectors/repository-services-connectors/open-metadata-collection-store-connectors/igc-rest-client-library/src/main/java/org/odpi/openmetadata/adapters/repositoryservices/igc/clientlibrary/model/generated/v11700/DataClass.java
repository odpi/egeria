/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11700;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code data_class} asset type in IGC, displayed as '{@literal Data Class}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataClass extends Reference {

    public static String getIgcTypeId() { return "data_class"; }
    public static String getIgcTypeDisplayName() { return "Data Class"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code long_description} property, displayed as '{@literal Long Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code parent_data_class} property, displayed as '{@literal Parent Data Class}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataClass} object.
     */
    protected Reference parent_data_class;

    /**
     * The {@code data_class_type_single} property, displayed as '{@literal Type}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>Undefined (displayed in the UI as 'Unspecified')</li>
     *     <li>Regex (displayed in the UI as 'Regex')</li>
     *     <li>Java (displayed in the UI as 'Java')</li>
     *     <li>ValidValues (displayed in the UI as 'Valid Values')</li>
     *     <li>Script (displayed in the UI as 'Script')</li>
     *     <li>ColumnSimilarity (displayed in the UI as 'Column Similarity')</li>
     *     <li>UnstructuredFilter (displayed in the UI as 'Unstructured Filter')</li>
     * </ul>
     */
    protected String data_class_type_single;

    /**
     * The {@code labels} property, displayed as '{@literal Labels}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The {@code stewards} property, displayed as '{@literal Stewards}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The {@code assigned_to_terms} property, displayed as '{@literal Assigned to Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The {@code implements_rules} property, displayed as '{@literal Implements Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The {@code governed_by_rules} property, displayed as '{@literal Governed by Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The {@code contains_data_classes} property, displayed as '{@literal Contains Data Classes}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataClass} objects.
     */
    protected ReferenceList contains_data_classes;

    /**
     * The {@code classifications_selected} property, displayed as '{@literal Selected Data Classifications}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Classificationenabledgroup} objects.
     */
    protected ReferenceList classifications_selected;

    /**
     * The {@code classified_assets_detected} property, displayed as '{@literal Detected Data Classifications}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Classification} objects.
     */
    protected ReferenceList classified_assets_detected;

    /**
     * The {@code data_type_filter_elements_enum} property, displayed as '{@literal Data Type}' in the IGC UI.
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
     * The {@code default_threshold} property, displayed as '{@literal Threshold (Percent)}' in the IGC UI.
     */
    protected Number default_threshold;

    /**
     * The {@code length_filter_min} property, displayed as '{@literal Minimum Data Length}' in the IGC UI.
     */
    protected Number length_filter_min;

    /**
     * The {@code length_filter_max} property, displayed as '{@literal Maximum Data Length}' in the IGC UI.
     */
    protected Number length_filter_max;

    /**
     * The {@code java_class_name_single} property, displayed as '{@literal JAVA Class}' in the IGC UI.
     */
    protected String java_class_name_single;

    /**
     * The {@code properties} property, displayed as '{@literal Properties}' in the IGC UI.
     */
    protected String properties;

    /**
     * The {@code scope} property, displayed as '{@literal Scope}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>value (displayed in the UI as 'Value')</li>
     *     <li>column (displayed in the UI as 'Column')</li>
     *     <li>dataset_columns (displayed in the UI as 'Dataset Columns')</li>
     *     <li>dataset (displayed in the UI as 'Dataset')</li>
     * </ul>
     */
    protected String scope;

    /**
     * The {@code regular_expression_single} property, displayed as '{@literal Regular Expression}' in the IGC UI.
     */
    protected String regular_expression_single;

    /**
     * The {@code applicable_for_single} property, displayed as '{@literal Applicable For}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>structured_data_only (displayed in the UI as 'Structured Data Only')</li>
     *     <li>unstructured_data_only (displayed in the UI as 'Unstructured Data Only')</li>
     *     <li>all_data (displayed in the UI as 'All Data')</li>
     * </ul>
     */
    protected String applicable_for_single;

    /**
     * The {@code additional_regular_expression} property, displayed as '{@literal Additional Regular Expression}' in the IGC UI.
     */
    protected String additional_regular_expression;

    /**
     * The {@code additionial_applicable_for} property, displayed as '{@literal Additional Applicable For}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>structured_data_only (displayed in the UI as 'Structured Data Only')</li>
     *     <li>unstructured_data_only (displayed in the UI as 'Unstructured Data Only')</li>
     *     <li>all_data (displayed in the UI as 'All Data')</li>
     * </ul>
     */
    protected String additionial_applicable_for;

    /**
     * The {@code valid_value_strings} property, displayed as '{@literal Valid Values}' in the IGC UI.
     */
    protected ArrayList<String> valid_value_strings;

    /**
     * The {@code valid_values_case_sensitive} property, displayed as '{@literal Case Sensitive}' in the IGC UI.
     */
    protected Boolean valid_values_case_sensitive;

    /**
     * The {@code allowSubstringMatch} property, displayed as '{@literal Allow Substring Match}' in the IGC UI.
     */
    protected Boolean allowSubstringMatch;

    /**
     * The {@code squeezeConsecutiveWhiteSpaces} property, displayed as '{@literal Collapse Consecutive White Spaces}' in the IGC UI.
     */
    protected Boolean squeezeConsecutiveWhiteSpaces;

    /**
     * The {@code columnNameMatch} property, displayed as '{@literal Column Name Match}' in the IGC UI.
     */
    protected String columnNameMatch;

    /**
     * The {@code expression} property, displayed as '{@literal Expression}' in the IGC UI.
     */
    protected String expression;

    /**
     * The {@code script} property, displayed as '{@literal Script}' in the IGC UI.
     */
    protected String script;

    /**
     * The {@code language} property, displayed as '{@literal Language}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>JavaScript (displayed in the UI as 'JavaScript')</li>
     *     <li>DataRule (displayed in the UI as 'Data Rule')</li>
     * </ul>
     */
    protected String language;

    /**
     * The {@code reference_columns_metadata} property, displayed as '{@literal Reference Columns Metadata}' in the IGC UI.
     */
    protected String reference_columns_metadata;

    /**
     * The {@code example} property, displayed as '{@literal Example}' in the IGC UI.
     */
    protected String example;

    /**
     * The {@code enabled} property, displayed as '{@literal Enabled}' in the IGC UI.
     */
    protected Boolean enabled;

    /**
     * The {@code class_code} property, displayed as '{@literal Class Code}' in the IGC UI.
     */
    protected String class_code;

    /**
     * The {@code validValueReferenceFile} property, displayed as '{@literal Valid Value Reference File}' in the IGC UI.
     */
    protected String validValueReferenceFile;

    /**
     * The {@code active} property, displayed as '{@literal Active}' in the IGC UI.
     */
    protected Boolean active;

    /**
     * The {@code provider} property, displayed as '{@literal Provider}' in the IGC UI.
     */
    protected String provider;

    /**
     * The {@code filters} property, displayed as '{@literal Filters}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Filter} objects.
     */
    protected ReferenceList filters;

    /**
     * The {@code in_collections} property, displayed as '{@literal In Collections}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

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

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #parent_data_class */ @JsonProperty("parent_data_class")  public Reference getParentDataClass() { return this.parent_data_class; }
    /** @see #parent_data_class */ @JsonProperty("parent_data_class")  public void setParentDataClass(Reference parent_data_class) { this.parent_data_class = parent_data_class; }

    /** @see #data_class_type_single */ @JsonProperty("data_class_type_single")  public String getDataClassTypeSingle() { return this.data_class_type_single; }
    /** @see #data_class_type_single */ @JsonProperty("data_class_type_single")  public void setDataClassTypeSingle(String data_class_type_single) { this.data_class_type_single = data_class_type_single; }

    /** @see #labels */ @JsonProperty("labels")  public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels")  public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards")  public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards")  public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #implements_rules */ @JsonProperty("implements_rules")  public ReferenceList getImplementsRules() { return this.implements_rules; }
    /** @see #implements_rules */ @JsonProperty("implements_rules")  public void setImplementsRules(ReferenceList implements_rules) { this.implements_rules = implements_rules; }

    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public ReferenceList getGovernedByRules() { return this.governed_by_rules; }
    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public void setGovernedByRules(ReferenceList governed_by_rules) { this.governed_by_rules = governed_by_rules; }

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

    /** @see #properties */ @JsonProperty("properties")  public String getProperties() { return this.properties; }
    /** @see #properties */ @JsonProperty("properties")  public void setProperties(String properties) { this.properties = properties; }

    /** @see #scope */ @JsonProperty("scope")  public String getScope() { return this.scope; }
    /** @see #scope */ @JsonProperty("scope")  public void setScope(String scope) { this.scope = scope; }

    /** @see #regular_expression_single */ @JsonProperty("regular_expression_single")  public String getRegularExpressionSingle() { return this.regular_expression_single; }
    /** @see #regular_expression_single */ @JsonProperty("regular_expression_single")  public void setRegularExpressionSingle(String regular_expression_single) { this.regular_expression_single = regular_expression_single; }

    /** @see #applicable_for_single */ @JsonProperty("applicable_for_single")  public String getApplicableForSingle() { return this.applicable_for_single; }
    /** @see #applicable_for_single */ @JsonProperty("applicable_for_single")  public void setApplicableForSingle(String applicable_for_single) { this.applicable_for_single = applicable_for_single; }

    /** @see #additional_regular_expression */ @JsonProperty("additional_regular_expression")  public String getAdditionalRegularExpression() { return this.additional_regular_expression; }
    /** @see #additional_regular_expression */ @JsonProperty("additional_regular_expression")  public void setAdditionalRegularExpression(String additional_regular_expression) { this.additional_regular_expression = additional_regular_expression; }

    /** @see #additionial_applicable_for */ @JsonProperty("additionial_applicable_for")  public String getAdditionialApplicableFor() { return this.additionial_applicable_for; }
    /** @see #additionial_applicable_for */ @JsonProperty("additionial_applicable_for")  public void setAdditionialApplicableFor(String additionial_applicable_for) { this.additionial_applicable_for = additionial_applicable_for; }

    /** @see #valid_value_strings */ @JsonProperty("valid_value_strings")  public ArrayList<String> getValidValueStrings() { return this.valid_value_strings; }
    /** @see #valid_value_strings */ @JsonProperty("valid_value_strings")  public void setValidValueStrings(ArrayList<String> valid_value_strings) { this.valid_value_strings = valid_value_strings; }

    /** @see #valid_values_case_sensitive */ @JsonProperty("valid_values_case_sensitive")  public Boolean getValidValuesCaseSensitive() { return this.valid_values_case_sensitive; }
    /** @see #valid_values_case_sensitive */ @JsonProperty("valid_values_case_sensitive")  public void setValidValuesCaseSensitive(Boolean valid_values_case_sensitive) { this.valid_values_case_sensitive = valid_values_case_sensitive; }

    /** @see #allowSubstringMatch */ @JsonProperty("allowSubstringMatch")  public Boolean getAllowsubstringmatch() { return this.allowSubstringMatch; }
    /** @see #allowSubstringMatch */ @JsonProperty("allowSubstringMatch")  public void setAllowsubstringmatch(Boolean allowSubstringMatch) { this.allowSubstringMatch = allowSubstringMatch; }

    /** @see #squeezeConsecutiveWhiteSpaces */ @JsonProperty("squeezeConsecutiveWhiteSpaces")  public Boolean getSqueezeconsecutivewhitespaces() { return this.squeezeConsecutiveWhiteSpaces; }
    /** @see #squeezeConsecutiveWhiteSpaces */ @JsonProperty("squeezeConsecutiveWhiteSpaces")  public void setSqueezeconsecutivewhitespaces(Boolean squeezeConsecutiveWhiteSpaces) { this.squeezeConsecutiveWhiteSpaces = squeezeConsecutiveWhiteSpaces; }

    /** @see #columnNameMatch */ @JsonProperty("columnNameMatch")  public String getColumnnamematch() { return this.columnNameMatch; }
    /** @see #columnNameMatch */ @JsonProperty("columnNameMatch")  public void setColumnnamematch(String columnNameMatch) { this.columnNameMatch = columnNameMatch; }

    /** @see #expression */ @JsonProperty("expression")  public String getExpression() { return this.expression; }
    /** @see #expression */ @JsonProperty("expression")  public void setExpression(String expression) { this.expression = expression; }

    /** @see #script */ @JsonProperty("script")  public String getScript() { return this.script; }
    /** @see #script */ @JsonProperty("script")  public void setScript(String script) { this.script = script; }

    /** @see #language */ @JsonProperty("language")  public String getLanguage() { return this.language; }
    /** @see #language */ @JsonProperty("language")  public void setLanguage(String language) { this.language = language; }

    /** @see #reference_columns_metadata */ @JsonProperty("reference_columns_metadata")  public String getReferenceColumnsMetadata() { return this.reference_columns_metadata; }
    /** @see #reference_columns_metadata */ @JsonProperty("reference_columns_metadata")  public void setReferenceColumnsMetadata(String reference_columns_metadata) { this.reference_columns_metadata = reference_columns_metadata; }

    /** @see #example */ @JsonProperty("example")  public String getExample() { return this.example; }
    /** @see #example */ @JsonProperty("example")  public void setExample(String example) { this.example = example; }

    /** @see #enabled */ @JsonProperty("enabled")  public Boolean getEnabled() { return this.enabled; }
    /** @see #enabled */ @JsonProperty("enabled")  public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    /** @see #class_code */ @JsonProperty("class_code")  public String getClassCode() { return this.class_code; }
    /** @see #class_code */ @JsonProperty("class_code")  public void setClassCode(String class_code) { this.class_code = class_code; }

    /** @see #validValueReferenceFile */ @JsonProperty("validValueReferenceFile")  public String getValidvaluereferencefile() { return this.validValueReferenceFile; }
    /** @see #validValueReferenceFile */ @JsonProperty("validValueReferenceFile")  public void setValidvaluereferencefile(String validValueReferenceFile) { this.validValueReferenceFile = validValueReferenceFile; }

    /** @see #active */ @JsonProperty("active")  public Boolean getActive() { return this.active; }
    /** @see #active */ @JsonProperty("active")  public void setActive(Boolean active) { this.active = active; }

    /** @see #provider */ @JsonProperty("provider")  public String getProvider() { return this.provider; }
    /** @see #provider */ @JsonProperty("provider")  public void setProvider(String provider) { this.provider = provider; }

    /** @see #filters */ @JsonProperty("filters")  public ReferenceList getFilters() { return this.filters; }
    /** @see #filters */ @JsonProperty("filters")  public void setFilters(ReferenceList filters) { this.filters = filters; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static Boolean canBeCreated() { return true; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "data_class_type_single",
        "data_type_filter_elements_enum",
        "default_threshold",
        "length_filter_min",
        "length_filter_max",
        "java_class_name_single",
        "properties",
        "scope",
        "regular_expression_single",
        "applicable_for_single",
        "additional_regular_expression",
        "additionial_applicable_for",
        "valid_value_strings",
        "valid_values_case_sensitive",
        "allowSubstringMatch",
        "squeezeConsecutiveWhiteSpaces",
        "columnNameMatch",
        "expression",
        "script",
        "language",
        "reference_columns_metadata",
        "example",
        "enabled",
        "class_code",
        "validValueReferenceFile",
        "active",
        "provider",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "java_class_name_single",
        "properties",
        "regular_expression_single",
        "additional_regular_expression",
        "valid_value_strings",
        "columnNameMatch",
        "expression",
        "script",
        "reference_columns_metadata",
        "example",
        "class_code",
        "validValueReferenceFile",
        "provider",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "contains_data_classes",
        "classifications_selected",
        "classified_assets_detected",
        "filters",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "parent_data_class",
        "data_class_type_single",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "contains_data_classes",
        "classifications_selected",
        "classified_assets_detected",
        "data_type_filter_elements_enum",
        "default_threshold",
        "length_filter_min",
        "length_filter_max",
        "java_class_name_single",
        "properties",
        "scope",
        "regular_expression_single",
        "applicable_for_single",
        "additional_regular_expression",
        "additionial_applicable_for",
        "valid_value_strings",
        "valid_values_case_sensitive",
        "allowSubstringMatch",
        "squeezeConsecutiveWhiteSpaces",
        "columnNameMatch",
        "expression",
        "script",
        "language",
        "reference_columns_metadata",
        "example",
        "enabled",
        "class_code",
        "validValueReferenceFile",
        "active",
        "provider",
        "filters",
        "in_collections",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isDataClass(Object obj) { return (obj.getClass() == DataClass.class); }

}
