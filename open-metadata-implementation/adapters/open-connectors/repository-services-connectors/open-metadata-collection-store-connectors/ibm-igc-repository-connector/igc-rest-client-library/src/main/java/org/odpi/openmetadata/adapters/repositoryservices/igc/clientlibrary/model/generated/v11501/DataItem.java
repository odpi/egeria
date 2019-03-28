/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11501;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code data_item} asset type in IGC, displayed as '{@literal Data Item}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataItem extends Reference {

    public static String getIgcTypeId() { return "data_item"; }
    public static String getIgcTypeDisplayName() { return "Data Item"; }

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
     * The {@code data_item_definition} property, displayed as '{@literal Data Item Definition}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataItemDefinition} object.
     */
    protected Reference data_item_definition;

    /**
     * The {@code is_signed} property, displayed as '{@literal Is Signed}' in the IGC UI.
     */
    protected Boolean is_signed;

    /**
     * The {@code validated_by_data_values} property, displayed as '{@literal Validated By Data Values}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItemValue} objects.
     */
    protected ReferenceList validated_by_data_values;

    /**
     * The {@code type} property, displayed as '{@literal Native Type}' in the IGC UI.
     */
    protected String type;

    /**
     * The {@code is_computed} property, displayed as '{@literal Is Computed}' in the IGC UI.
     */
    protected Boolean is_computed;

    /**
     * The {@code allows_empty_value} property, displayed as '{@literal Allows Empty Value}' in the IGC UI.
     */
    protected Boolean allows_empty_value;

    /**
     * The {@code odbc_type} property, displayed as '{@literal ODBC Type}' in the IGC UI.
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
     * The {@code based_on} property, displayed as '{@literal Based On }' in the IGC UI.
     */
    protected String based_on;

    /**
     * The {@code position} property, displayed as '{@literal Position}' in the IGC UI.
     */
    protected Number position;

    /**
     * The {@code allow_null_values} property, displayed as '{@literal Null Value}' in the IGC UI.
     */
    protected String allow_null_values;

    /**
     * The {@code fraction} property, displayed as '{@literal Scale}' in the IGC UI.
     */
    protected Number fraction;

    /**
     * The {@code allows_null_values} property, displayed as '{@literal Allow Null Values}' in the IGC UI.
     */
    protected Boolean allows_null_values;

    /**
     * The {@code calendar} property, displayed as '{@literal Calendar}' in the IGC UI.
     */
    protected String calendar;

    /**
     * The {@code display_size} property, displayed as '{@literal Display Size}' in the IGC UI.
     */
    protected Number display_size;

    /**
     * The {@code minimum_length} property, displayed as '{@literal Minimum Length}' in the IGC UI.
     */
    protected Number minimum_length;

    /**
     * The {@code item_kind} property, displayed as '{@literal Item Kind}' in the IGC UI.
     */
    protected String item_kind;

    /**
     * The {@code has_dimension} property, displayed as '{@literal Has Dimension}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Array} objects.
     */
    protected ReferenceList has_dimension;

    /**
     * The {@code unique} property, displayed as '{@literal Unique}' in the IGC UI.
     */
    protected Boolean unique;

    /**
     * The {@code length} property, displayed as '{@literal Maximum Length}' in the IGC UI.
     */
    protected Number length;

    /**
     * The {@code data_type} property, displayed as '{@literal Type Code}' in the IGC UI.
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
     * The {@code default_value} property, displayed as '{@literal Default Value}' in the IGC UI.
     */
    protected String default_value;

    /**
     * The {@code level} property, displayed as '{@literal Level Number}' in the IGC UI.
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
    public static Boolean isDataItem(Object obj) { return (obj.getClass() == DataItem.class); }

}
