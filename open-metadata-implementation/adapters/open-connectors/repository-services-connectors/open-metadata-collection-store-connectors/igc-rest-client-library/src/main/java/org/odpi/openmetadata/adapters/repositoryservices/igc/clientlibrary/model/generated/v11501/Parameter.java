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
 * POJO for the 'parameter' asset type in IGC, displayed as 'Parameter' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Parameter extends Reference {

    public static String getIgcTypeId() { return "parameter"; }
    public static String getIgcTypeDisplayName() { return "Parameter"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'long_description' property, displayed as 'Long Description' in the IGC UI.
     */
    protected String long_description;

    /**
     * The 'labels' property, displayed as 'Labels' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The 'stewards' property, displayed as 'Stewards' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The 'assigned_to_terms' property, displayed as 'Assigned to Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The 'implements_rules' property, displayed as 'Implements Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The 'governed_by_rules' property, displayed as 'Governed by Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The 'is_return_of_function' property, displayed as 'Is Return Of Function' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Function} object.
     */
    protected Reference is_return_of_function;

    /**
     * The 'of_container_def' property, displayed as 'Of Container Def' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link ReferencedContainer} object.
     */
    protected Reference of_container_def;

    /**
     * The 'of_parameter_set' property, displayed as 'Of Parameter Set' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link ParameterSet2} object.
     */
    protected Reference of_parameter_set;

    /**
     * The 'is_parameter_set' property, displayed as 'Is Parameter Set' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link ParameterSet2} object.
     */
    protected Reference is_parameter_set;

    /**
     * The 'defines_parameter_val' property, displayed as 'Defines Parameter Val' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Parameterval} objects.
     */
    protected ReferenceList defines_parameter_val;

    /**
     * The 'of_stage_type' property, displayed as 'Of Stage Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link StageType} object.
     */
    protected Reference of_stage_type;

    /**
     * The 'of_job_def' property, displayed as 'Of Job Def' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Jobdef} object.
     */
    protected Reference of_job_def;

    /**
     * The 'of_stored_procedure' property, displayed as 'Of Stored Procedure' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Datagroup} object.
     */
    protected Reference of_stored_procedure;

    /**
     * The 'usage' property, displayed as 'Usage' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>DEFAULT (displayed in the UI as 'DEFAULT')</li>
     *     <li>IN (displayed in the UI as 'IN')</li>
     *     <li>OUT (displayed in the UI as 'OUT')</li>
     *     <li>INOUT (displayed in the UI as 'INOUT')</li>
     *     <li>RETURN (displayed in the UI as 'RETURN')</li>
     * </ul>
     */
    protected String usage;

    /**
     * The 'connection_property' property, displayed as 'Connection Property' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>CONNECTIONSTRING (displayed in the UI as 'CONNECTIONSTRING')</li>
     *     <li>USERNAME (displayed in the UI as 'USERNAME')</li>
     *     <li>PASSWORD (displayed in the UI as 'PASSWORD')</li>
     *     <li>PRIVATE (displayed in the UI as 'PRIVATE')</li>
     * </ul>
     */
    protected String connection_property;

    /**
     * The 'is_connection_property' property, displayed as 'Is Connection Property' in the IGC UI.
     */
    protected Boolean is_connection_property;

    /**
     * The 'of_function' property, displayed as 'Of Function' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Function} object.
     */
    protected Reference of_function;

    /**
     * The 'data_item_definition' property, displayed as 'Data Item Definition' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataItemDefinition} object.
     */
    protected Reference data_item_definition;

    /**
     * The 'is_signed' property, displayed as 'Is Signed' in the IGC UI.
     */
    protected Boolean is_signed;

    /**
     * The 'validated_by_data_values' property, displayed as 'Validated By Data Values' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItemValue} objects.
     */
    protected ReferenceList validated_by_data_values;

    /**
     * The 'type' property, displayed as 'Native Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'is_computed' property, displayed as 'Is Computed' in the IGC UI.
     */
    protected Boolean is_computed;

    /**
     * The 'allows_empty_value' property, displayed as 'Allows Empty Value' in the IGC UI.
     */
    protected Boolean allows_empty_value;

    /**
     * The 'odbc_type' property, displayed as 'ODBC Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>CHAR (displayed in the UI as 'CHAR')</li>
     *     <li>VARCHAR (displayed in the UI as 'VARCHAR')</li>
     *     <li>LONGVARCHAR (displayed in the UI as 'LONGVARCHAR')</li>
     *     <li>WCHAR (displayed in the UI as 'WCHAR')</li>
     *     <li>WVARCHAR (displayed in the UI as 'WVARCHAR')</li>
     *     <li>WLONGVARCHAR (displayed in the UI as 'WLONGVARCHAR')</li>
     *     <li>DECIMAL (displayed in the UI as 'DECIMAL')</li>
     *     <li>NUMERIC (displayed in the UI as 'NUMERIC')</li>
     *     <li>SMALLINT (displayed in the UI as 'SMALLINT')</li>
     *     <li>INTEGER (displayed in the UI as 'INTEGER')</li>
     *     <li>REAL (displayed in the UI as 'REAL')</li>
     *     <li>FLOAT (displayed in the UI as 'FLOAT')</li>
     *     <li>DOUBLE (displayed in the UI as 'DOUBLE')</li>
     *     <li>BIT (displayed in the UI as 'BIT')</li>
     *     <li>TINYINT (displayed in the UI as 'TINYINT')</li>
     *     <li>BIGINT (displayed in the UI as 'BIGINT')</li>
     *     <li>BINARY (displayed in the UI as 'BINARY')</li>
     *     <li>VARBINARY (displayed in the UI as 'VARBINARY')</li>
     *     <li>LONGVARBINARY (displayed in the UI as 'LONGVARBINARY')</li>
     *     <li>DATE (displayed in the UI as 'DATE')</li>
     *     <li>TIME (displayed in the UI as 'TIME')</li>
     *     <li>TIMESTAMP (displayed in the UI as 'TIMESTAMP')</li>
     *     <li>GUID (displayed in the UI as 'GUID')</li>
     *     <li>UNKNOWN (displayed in the UI as 'UNKNOWN')</li>
     * </ul>
     */
    protected String odbc_type;

    /**
     * The 'based_on' property, displayed as 'Based On ' in the IGC UI.
     */
    protected String based_on;

    /**
     * The 'position' property, displayed as 'Position' in the IGC UI.
     */
    protected Number position;

    /**
     * The 'allow_null_values' property, displayed as 'Null Value' in the IGC UI.
     */
    protected String allow_null_values;

    /**
     * The 'fraction' property, displayed as 'Scale' in the IGC UI.
     */
    protected Number fraction;

    /**
     * The 'allows_null_values' property, displayed as 'Allow Null Values' in the IGC UI.
     */
    protected Boolean allows_null_values;

    /**
     * The 'calendar' property, displayed as 'Calendar' in the IGC UI.
     */
    protected String calendar;

    /**
     * The 'display_size' property, displayed as 'Display Size' in the IGC UI.
     */
    protected Number display_size;

    /**
     * The 'minimum_length' property, displayed as 'Minimum Length' in the IGC UI.
     */
    protected Number minimum_length;

    /**
     * The 'item_kind' property, displayed as 'Item Kind' in the IGC UI.
     */
    protected String item_kind;

    /**
     * The 'has_dimension' property, displayed as 'Has Dimension' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Array} objects.
     */
    protected ReferenceList has_dimension;

    /**
     * The 'unique' property, displayed as 'Unique' in the IGC UI.
     */
    protected Boolean unique;

    /**
     * The 'length' property, displayed as 'Maximum Length' in the IGC UI.
     */
    protected Number length;

    /**
     * The 'data_type' property, displayed as 'Type Code' in the IGC UI.
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
    protected String data_type;

    /**
     * The 'default_value' property, displayed as 'Default Value' in the IGC UI.
     */
    protected String default_value;

    /**
     * The 'level' property, displayed as 'Level Number' in the IGC UI.
     */
    protected Number level;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

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

    /** @see #is_return_of_function */ @JsonProperty("is_return_of_function")  public Reference getIsReturnOfFunction() { return this.is_return_of_function; }
    /** @see #is_return_of_function */ @JsonProperty("is_return_of_function")  public void setIsReturnOfFunction(Reference is_return_of_function) { this.is_return_of_function = is_return_of_function; }

    /** @see #of_container_def */ @JsonProperty("of_container_def")  public Reference getOfContainerDef() { return this.of_container_def; }
    /** @see #of_container_def */ @JsonProperty("of_container_def")  public void setOfContainerDef(Reference of_container_def) { this.of_container_def = of_container_def; }

    /** @see #of_parameter_set */ @JsonProperty("of_parameter_set")  public Reference getOfParameterSet() { return this.of_parameter_set; }
    /** @see #of_parameter_set */ @JsonProperty("of_parameter_set")  public void setOfParameterSet(Reference of_parameter_set) { this.of_parameter_set = of_parameter_set; }

    /** @see #is_parameter_set */ @JsonProperty("is_parameter_set")  public Reference getIsParameterSet() { return this.is_parameter_set; }
    /** @see #is_parameter_set */ @JsonProperty("is_parameter_set")  public void setIsParameterSet(Reference is_parameter_set) { this.is_parameter_set = is_parameter_set; }

    /** @see #defines_parameter_val */ @JsonProperty("defines_parameter_val")  public ReferenceList getDefinesParameterVal() { return this.defines_parameter_val; }
    /** @see #defines_parameter_val */ @JsonProperty("defines_parameter_val")  public void setDefinesParameterVal(ReferenceList defines_parameter_val) { this.defines_parameter_val = defines_parameter_val; }

    /** @see #of_stage_type */ @JsonProperty("of_stage_type")  public Reference getOfStageType() { return this.of_stage_type; }
    /** @see #of_stage_type */ @JsonProperty("of_stage_type")  public void setOfStageType(Reference of_stage_type) { this.of_stage_type = of_stage_type; }

    /** @see #of_job_def */ @JsonProperty("of_job_def")  public Reference getOfJobDef() { return this.of_job_def; }
    /** @see #of_job_def */ @JsonProperty("of_job_def")  public void setOfJobDef(Reference of_job_def) { this.of_job_def = of_job_def; }

    /** @see #of_stored_procedure */ @JsonProperty("of_stored_procedure")  public Reference getOfStoredProcedure() { return this.of_stored_procedure; }
    /** @see #of_stored_procedure */ @JsonProperty("of_stored_procedure")  public void setOfStoredProcedure(Reference of_stored_procedure) { this.of_stored_procedure = of_stored_procedure; }

    /** @see #usage */ @JsonProperty("usage")  public String getUsage() { return this.usage; }
    /** @see #usage */ @JsonProperty("usage")  public void setUsage(String usage) { this.usage = usage; }

    /** @see #connection_property */ @JsonProperty("connection_property")  public String getConnectionProperty() { return this.connection_property; }
    /** @see #connection_property */ @JsonProperty("connection_property")  public void setConnectionProperty(String connection_property) { this.connection_property = connection_property; }

    /** @see #is_connection_property */ @JsonProperty("is_connection_property")  public Boolean getIsConnectionProperty() { return this.is_connection_property; }
    /** @see #is_connection_property */ @JsonProperty("is_connection_property")  public void setIsConnectionProperty(Boolean is_connection_property) { this.is_connection_property = is_connection_property; }

    /** @see #of_function */ @JsonProperty("of_function")  public Reference getOfFunction() { return this.of_function; }
    /** @see #of_function */ @JsonProperty("of_function")  public void setOfFunction(Reference of_function) { this.of_function = of_function; }

    /** @see #data_item_definition */ @JsonProperty("data_item_definition")  public Reference getDataItemDefinition() { return this.data_item_definition; }
    /** @see #data_item_definition */ @JsonProperty("data_item_definition")  public void setDataItemDefinition(Reference data_item_definition) { this.data_item_definition = data_item_definition; }

    /** @see #is_signed */ @JsonProperty("is_signed")  public Boolean getIsSigned() { return this.is_signed; }
    /** @see #is_signed */ @JsonProperty("is_signed")  public void setIsSigned(Boolean is_signed) { this.is_signed = is_signed; }

    /** @see #validated_by_data_values */ @JsonProperty("validated_by_data_values")  public ReferenceList getValidatedByDataValues() { return this.validated_by_data_values; }
    /** @see #validated_by_data_values */ @JsonProperty("validated_by_data_values")  public void setValidatedByDataValues(ReferenceList validated_by_data_values) { this.validated_by_data_values = validated_by_data_values; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #is_computed */ @JsonProperty("is_computed")  public Boolean getIsComputed() { return this.is_computed; }
    /** @see #is_computed */ @JsonProperty("is_computed")  public void setIsComputed(Boolean is_computed) { this.is_computed = is_computed; }

    /** @see #allows_empty_value */ @JsonProperty("allows_empty_value")  public Boolean getAllowsEmptyValue() { return this.allows_empty_value; }
    /** @see #allows_empty_value */ @JsonProperty("allows_empty_value")  public void setAllowsEmptyValue(Boolean allows_empty_value) { this.allows_empty_value = allows_empty_value; }

    /** @see #odbc_type */ @JsonProperty("odbc_type")  public String getOdbcType() { return this.odbc_type; }
    /** @see #odbc_type */ @JsonProperty("odbc_type")  public void setOdbcType(String odbc_type) { this.odbc_type = odbc_type; }

    /** @see #based_on */ @JsonProperty("based_on")  public String getBasedOn() { return this.based_on; }
    /** @see #based_on */ @JsonProperty("based_on")  public void setBasedOn(String based_on) { this.based_on = based_on; }

    /** @see #position */ @JsonProperty("position")  public Number getPosition() { return this.position; }
    /** @see #position */ @JsonProperty("position")  public void setPosition(Number position) { this.position = position; }

    /** @see #allow_null_values */ @JsonProperty("allow_null_values")  public String getAllowNullValues() { return this.allow_null_values; }
    /** @see #allow_null_values */ @JsonProperty("allow_null_values")  public void setAllowNullValues(String allow_null_values) { this.allow_null_values = allow_null_values; }

    /** @see #fraction */ @JsonProperty("fraction")  public Number getFraction() { return this.fraction; }
    /** @see #fraction */ @JsonProperty("fraction")  public void setFraction(Number fraction) { this.fraction = fraction; }

    /** @see #allows_null_values */ @JsonProperty("allows_null_values")  public Boolean getAllowsNullValues() { return this.allows_null_values; }
    /** @see #allows_null_values */ @JsonProperty("allows_null_values")  public void setAllowsNullValues(Boolean allows_null_values) { this.allows_null_values = allows_null_values; }

    /** @see #calendar */ @JsonProperty("calendar")  public String getCalendar() { return this.calendar; }
    /** @see #calendar */ @JsonProperty("calendar")  public void setCalendar(String calendar) { this.calendar = calendar; }

    /** @see #display_size */ @JsonProperty("display_size")  public Number getDisplaySize() { return this.display_size; }
    /** @see #display_size */ @JsonProperty("display_size")  public void setDisplaySize(Number display_size) { this.display_size = display_size; }

    /** @see #minimum_length */ @JsonProperty("minimum_length")  public Number getMinimumLength() { return this.minimum_length; }
    /** @see #minimum_length */ @JsonProperty("minimum_length")  public void setMinimumLength(Number minimum_length) { this.minimum_length = minimum_length; }

    /** @see #item_kind */ @JsonProperty("item_kind")  public String getItemKind() { return this.item_kind; }
    /** @see #item_kind */ @JsonProperty("item_kind")  public void setItemKind(String item_kind) { this.item_kind = item_kind; }

    /** @see #has_dimension */ @JsonProperty("has_dimension")  public ReferenceList getHasDimension() { return this.has_dimension; }
    /** @see #has_dimension */ @JsonProperty("has_dimension")  public void setHasDimension(ReferenceList has_dimension) { this.has_dimension = has_dimension; }

    /** @see #unique */ @JsonProperty("unique")  public Boolean getUnique() { return this.unique; }
    /** @see #unique */ @JsonProperty("unique")  public void setUnique(Boolean unique) { this.unique = unique; }

    /** @see #length */ @JsonProperty("length")  public Number getLength() { return this.length; }
    /** @see #length */ @JsonProperty("length")  public void setLength(Number length) { this.length = length; }

    /** @see #data_type */ @JsonProperty("data_type")  public String getDataType() { return this.data_type; }
    /** @see #data_type */ @JsonProperty("data_type")  public void setDataType(String data_type) { this.data_type = data_type; }

    /** @see #default_value */ @JsonProperty("default_value")  public String getDefaultValue() { return this.default_value; }
    /** @see #default_value */ @JsonProperty("default_value")  public void setDefaultValue(String default_value) { this.default_value = default_value; }

    /** @see #level */ @JsonProperty("level")  public Number getLevel() { return this.level; }
    /** @see #level */ @JsonProperty("level")  public void setLevel(Number level) { this.level = level; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "usage",
        "connection_property",
        "is_connection_property",
        "is_signed",
        "type",
        "is_computed",
        "allows_empty_value",
        "odbc_type",
        "based_on",
        "position",
        "allow_null_values",
        "fraction",
        "allows_null_values",
        "calendar",
        "display_size",
        "minimum_length",
        "item_kind",
        "unique",
        "length",
        "data_type",
        "default_value",
        "level"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "type",
        "based_on",
        "allow_null_values",
        "calendar",
        "item_kind",
        "default_value"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "defines_parameter_val",
        "validated_by_data_values",
        "has_dimension"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "is_return_of_function",
        "of_container_def",
        "of_parameter_set",
        "is_parameter_set",
        "defines_parameter_val",
        "of_stage_type",
        "of_job_def",
        "of_stored_procedure",
        "usage",
        "connection_property",
        "is_connection_property",
        "of_function",
        "data_item_definition",
        "is_signed",
        "validated_by_data_values",
        "type",
        "is_computed",
        "allows_empty_value",
        "odbc_type",
        "based_on",
        "position",
        "allow_null_values",
        "fraction",
        "allows_null_values",
        "calendar",
        "display_size",
        "minimum_length",
        "item_kind",
        "has_dimension",
        "unique",
        "length",
        "data_type",
        "default_value",
        "level"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isParameter(Object obj) { return (obj.getClass() == Parameter.class); }

}
