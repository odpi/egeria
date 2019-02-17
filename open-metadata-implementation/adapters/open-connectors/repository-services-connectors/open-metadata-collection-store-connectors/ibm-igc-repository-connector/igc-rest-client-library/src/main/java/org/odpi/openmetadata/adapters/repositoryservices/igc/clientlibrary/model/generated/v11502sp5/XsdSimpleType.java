/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502sp5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code xsd_simple_type} asset type in IGC, displayed as '{@literal XSD Simple Type}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class XsdSimpleType extends Reference {

    public static String getIgcTypeId() { return "xsd_simple_type"; }
    public static String getIgcTypeDisplayName() { return "XSD Simple Type"; }

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
     * The {@code xsd_schema} property, displayed as '{@literal XSD Schema}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XmlSchemaDefinition} object.
     */
    protected Reference xsd_schema;

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
     * The {@code defines_xsd_elements} property, displayed as '{@literal Defines XSD Elements}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList defines_xsd_elements;

    /**
     * The {@code defines_xsd_attributes} property, displayed as '{@literal Defines XSD Attributes}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdAttribute} objects.
     */
    protected ReferenceList defines_xsd_attributes;

    /**
     * The {@code extends_xsd_simple_type} property, displayed as '{@literal Extends XSD Simple Type}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XsdSimpleType} object.
     */
    protected Reference extends_xsd_simple_type;

    /**
     * The {@code extended_by_xsd_simple_type} property, displayed as '{@literal Extended by XSD Simple Types}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdSimpleType} objects.
     */
    protected ReferenceList extended_by_xsd_simple_type;

    /**
     * The {@code restricts_xsd_simple_type} property, displayed as '{@literal Restricts XSD Simple Type}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XsdSimpleType} object.
     */
    protected Reference restricts_xsd_simple_type;

    /**
     * The {@code restricted_by_xsd_simple_type} property, displayed as '{@literal Restricted by XSD Simple Types}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdSimpleType} objects.
     */
    protected ReferenceList restricted_by_xsd_simple_type;

    /**
     * The {@code extends_xsd_element} property, displayed as '{@literal Extends XSD Elements}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList extends_xsd_element;

    /**
     * The {@code restricts_xsd_element} property, displayed as '{@literal Restricts XSD Elements}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList restricts_xsd_element;

    /**
     * The {@code extended_by_xsd_complex_type} property, displayed as '{@literal Extended by XSD Complex Types}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdComplexType} objects.
     */
    protected ReferenceList extended_by_xsd_complex_type;

    /**
     * The {@code base_type} property, displayed as '{@literal Data Type}' in the IGC UI.
     */
    protected String base_type;

    /**
     * The {@code length} property, displayed as '{@literal Length}' in the IGC UI.
     */
    protected Number length;

    /**
     * The {@code max_length} property, displayed as '{@literal Maximum Length}' in the IGC UI.
     */
    protected Number max_length;

    /**
     * The {@code min_length} property, displayed as '{@literal Minimum Length}' in the IGC UI.
     */
    protected Number min_length;

    /**
     * The {@code fraction} property, displayed as '{@literal Fraction}' in the IGC UI.
     */
    protected Number fraction;

    /**
     * The {@code total_digits} property, displayed as '{@literal Total Digits}' in the IGC UI.
     */
    protected Number total_digits;

    /**
     * The {@code whitespace} property, displayed as '{@literal Whitespace}' in the IGC UI.
     */
    protected String whitespace;

    /**
     * The {@code timezone} property, displayed as '{@literal Timezone}' in the IGC UI.
     */
    protected String timezone;

    /**
     * The {@code final} property, displayed as '{@literal Final}' in the IGC UI.
     */
    @JsonProperty("final") protected String __final;

    /**
     * The {@code restriction_pattern} property, displayed as '{@literal Pattern}' in the IGC UI.
     */
    protected String restriction_pattern;

    /**
     * The {@code restriction_minimum_value} property, displayed as '{@literal Minimum Range}' in the IGC UI.
     */
    protected String restriction_minimum_value;

    /**
     * The {@code restriction_minimum_inclusive} property, displayed as '{@literal Minimum Range Inclusive}' in the IGC UI.
     */
    protected Boolean restriction_minimum_inclusive;

    /**
     * The {@code restriction_maximum_value} property, displayed as '{@literal Maximum Range}' in the IGC UI.
     */
    protected String restriction_maximum_value;

    /**
     * The {@code restriction_maximum_inclusive} property, displayed as '{@literal Maximum Range Inclusive}' in the IGC UI.
     */
    protected Boolean restriction_maximum_inclusive;

    /**
     * The {@code restriction_enumeration_values} property, displayed as '{@literal Enumeration Values}' in the IGC UI.
     */
    protected ArrayList<String> restriction_enumeration_values;

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

    /** @see #xsd_schema */ @JsonProperty("xsd_schema")  public Reference getXsdSchema() { return this.xsd_schema; }
    /** @see #xsd_schema */ @JsonProperty("xsd_schema")  public void setXsdSchema(Reference xsd_schema) { this.xsd_schema = xsd_schema; }

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

    /** @see #defines_xsd_elements */ @JsonProperty("defines_xsd_elements")  public ReferenceList getDefinesXsdElements() { return this.defines_xsd_elements; }
    /** @see #defines_xsd_elements */ @JsonProperty("defines_xsd_elements")  public void setDefinesXsdElements(ReferenceList defines_xsd_elements) { this.defines_xsd_elements = defines_xsd_elements; }

    /** @see #defines_xsd_attributes */ @JsonProperty("defines_xsd_attributes")  public ReferenceList getDefinesXsdAttributes() { return this.defines_xsd_attributes; }
    /** @see #defines_xsd_attributes */ @JsonProperty("defines_xsd_attributes")  public void setDefinesXsdAttributes(ReferenceList defines_xsd_attributes) { this.defines_xsd_attributes = defines_xsd_attributes; }

    /** @see #extends_xsd_simple_type */ @JsonProperty("extends_xsd_simple_type")  public Reference getExtendsXsdSimpleType() { return this.extends_xsd_simple_type; }
    /** @see #extends_xsd_simple_type */ @JsonProperty("extends_xsd_simple_type")  public void setExtendsXsdSimpleType(Reference extends_xsd_simple_type) { this.extends_xsd_simple_type = extends_xsd_simple_type; }

    /** @see #extended_by_xsd_simple_type */ @JsonProperty("extended_by_xsd_simple_type")  public ReferenceList getExtendedByXsdSimpleType() { return this.extended_by_xsd_simple_type; }
    /** @see #extended_by_xsd_simple_type */ @JsonProperty("extended_by_xsd_simple_type")  public void setExtendedByXsdSimpleType(ReferenceList extended_by_xsd_simple_type) { this.extended_by_xsd_simple_type = extended_by_xsd_simple_type; }

    /** @see #restricts_xsd_simple_type */ @JsonProperty("restricts_xsd_simple_type")  public Reference getRestrictsXsdSimpleType() { return this.restricts_xsd_simple_type; }
    /** @see #restricts_xsd_simple_type */ @JsonProperty("restricts_xsd_simple_type")  public void setRestrictsXsdSimpleType(Reference restricts_xsd_simple_type) { this.restricts_xsd_simple_type = restricts_xsd_simple_type; }

    /** @see #restricted_by_xsd_simple_type */ @JsonProperty("restricted_by_xsd_simple_type")  public ReferenceList getRestrictedByXsdSimpleType() { return this.restricted_by_xsd_simple_type; }
    /** @see #restricted_by_xsd_simple_type */ @JsonProperty("restricted_by_xsd_simple_type")  public void setRestrictedByXsdSimpleType(ReferenceList restricted_by_xsd_simple_type) { this.restricted_by_xsd_simple_type = restricted_by_xsd_simple_type; }

    /** @see #extends_xsd_element */ @JsonProperty("extends_xsd_element")  public ReferenceList getExtendsXsdElement() { return this.extends_xsd_element; }
    /** @see #extends_xsd_element */ @JsonProperty("extends_xsd_element")  public void setExtendsXsdElement(ReferenceList extends_xsd_element) { this.extends_xsd_element = extends_xsd_element; }

    /** @see #restricts_xsd_element */ @JsonProperty("restricts_xsd_element")  public ReferenceList getRestrictsXsdElement() { return this.restricts_xsd_element; }
    /** @see #restricts_xsd_element */ @JsonProperty("restricts_xsd_element")  public void setRestrictsXsdElement(ReferenceList restricts_xsd_element) { this.restricts_xsd_element = restricts_xsd_element; }

    /** @see #extended_by_xsd_complex_type */ @JsonProperty("extended_by_xsd_complex_type")  public ReferenceList getExtendedByXsdComplexType() { return this.extended_by_xsd_complex_type; }
    /** @see #extended_by_xsd_complex_type */ @JsonProperty("extended_by_xsd_complex_type")  public void setExtendedByXsdComplexType(ReferenceList extended_by_xsd_complex_type) { this.extended_by_xsd_complex_type = extended_by_xsd_complex_type; }

    /** @see #base_type */ @JsonProperty("base_type")  public String getBaseType() { return this.base_type; }
    /** @see #base_type */ @JsonProperty("base_type")  public void setBaseType(String base_type) { this.base_type = base_type; }

    /** @see #length */ @JsonProperty("length")  public Number getLength() { return this.length; }
    /** @see #length */ @JsonProperty("length")  public void setLength(Number length) { this.length = length; }

    /** @see #max_length */ @JsonProperty("max_length")  public Number getMaxLength() { return this.max_length; }
    /** @see #max_length */ @JsonProperty("max_length")  public void setMaxLength(Number max_length) { this.max_length = max_length; }

    /** @see #min_length */ @JsonProperty("min_length")  public Number getMinLength() { return this.min_length; }
    /** @see #min_length */ @JsonProperty("min_length")  public void setMinLength(Number min_length) { this.min_length = min_length; }

    /** @see #fraction */ @JsonProperty("fraction")  public Number getFraction() { return this.fraction; }
    /** @see #fraction */ @JsonProperty("fraction")  public void setFraction(Number fraction) { this.fraction = fraction; }

    /** @see #total_digits */ @JsonProperty("total_digits")  public Number getTotalDigits() { return this.total_digits; }
    /** @see #total_digits */ @JsonProperty("total_digits")  public void setTotalDigits(Number total_digits) { this.total_digits = total_digits; }

    /** @see #whitespace */ @JsonProperty("whitespace")  public String getWhitespace() { return this.whitespace; }
    /** @see #whitespace */ @JsonProperty("whitespace")  public void setWhitespace(String whitespace) { this.whitespace = whitespace; }

    /** @see #timezone */ @JsonProperty("timezone")  public String getTimezone() { return this.timezone; }
    /** @see #timezone */ @JsonProperty("timezone")  public void setTimezone(String timezone) { this.timezone = timezone; }

    /** @see #__final */ @JsonProperty("final")  public String getFinal() { return this.__final; }
    /** @see #__final */ @JsonProperty("final")  public void setFinal(String __final) { this.__final = __final; }

    /** @see #restriction_pattern */ @JsonProperty("restriction_pattern")  public String getRestrictionPattern() { return this.restriction_pattern; }
    /** @see #restriction_pattern */ @JsonProperty("restriction_pattern")  public void setRestrictionPattern(String restriction_pattern) { this.restriction_pattern = restriction_pattern; }

    /** @see #restriction_minimum_value */ @JsonProperty("restriction_minimum_value")  public String getRestrictionMinimumValue() { return this.restriction_minimum_value; }
    /** @see #restriction_minimum_value */ @JsonProperty("restriction_minimum_value")  public void setRestrictionMinimumValue(String restriction_minimum_value) { this.restriction_minimum_value = restriction_minimum_value; }

    /** @see #restriction_minimum_inclusive */ @JsonProperty("restriction_minimum_inclusive")  public Boolean getRestrictionMinimumInclusive() { return this.restriction_minimum_inclusive; }
    /** @see #restriction_minimum_inclusive */ @JsonProperty("restriction_minimum_inclusive")  public void setRestrictionMinimumInclusive(Boolean restriction_minimum_inclusive) { this.restriction_minimum_inclusive = restriction_minimum_inclusive; }

    /** @see #restriction_maximum_value */ @JsonProperty("restriction_maximum_value")  public String getRestrictionMaximumValue() { return this.restriction_maximum_value; }
    /** @see #restriction_maximum_value */ @JsonProperty("restriction_maximum_value")  public void setRestrictionMaximumValue(String restriction_maximum_value) { this.restriction_maximum_value = restriction_maximum_value; }

    /** @see #restriction_maximum_inclusive */ @JsonProperty("restriction_maximum_inclusive")  public Boolean getRestrictionMaximumInclusive() { return this.restriction_maximum_inclusive; }
    /** @see #restriction_maximum_inclusive */ @JsonProperty("restriction_maximum_inclusive")  public void setRestrictionMaximumInclusive(Boolean restriction_maximum_inclusive) { this.restriction_maximum_inclusive = restriction_maximum_inclusive; }

    /** @see #restriction_enumeration_values */ @JsonProperty("restriction_enumeration_values")  public ArrayList<String> getRestrictionEnumerationValues() { return this.restriction_enumeration_values; }
    /** @see #restriction_enumeration_values */ @JsonProperty("restriction_enumeration_values")  public void setRestrictionEnumerationValues(ArrayList<String> restriction_enumeration_values) { this.restriction_enumeration_values = restriction_enumeration_values; }

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

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "base_type",
        "length",
        "max_length",
        "min_length",
        "fraction",
        "total_digits",
        "whitespace",
        "timezone",
        "final",
        "restriction_pattern",
        "restriction_minimum_value",
        "restriction_minimum_inclusive",
        "restriction_maximum_value",
        "restriction_maximum_inclusive",
        "restriction_enumeration_values",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "base_type",
        "whitespace",
        "timezone",
        "final",
        "restriction_pattern",
        "restriction_minimum_value",
        "restriction_maximum_value",
        "restriction_enumeration_values",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "defines_xsd_elements",
        "defines_xsd_attributes",
        "extended_by_xsd_simple_type",
        "restricted_by_xsd_simple_type",
        "extends_xsd_element",
        "restricts_xsd_element",
        "extended_by_xsd_complex_type",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "xsd_schema",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "defines_xsd_elements",
        "defines_xsd_attributes",
        "extends_xsd_simple_type",
        "extended_by_xsd_simple_type",
        "restricts_xsd_simple_type",
        "restricted_by_xsd_simple_type",
        "extends_xsd_element",
        "restricts_xsd_element",
        "extended_by_xsd_complex_type",
        "base_type",
        "length",
        "max_length",
        "min_length",
        "fraction",
        "total_digits",
        "whitespace",
        "timezone",
        "final",
        "restriction_pattern",
        "restriction_minimum_value",
        "restriction_minimum_inclusive",
        "restriction_maximum_value",
        "restriction_maximum_inclusive",
        "restriction_enumeration_values",
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
    public static Boolean isXsdSimpleType(Object obj) { return (obj.getClass() == XsdSimpleType.class); }

}
