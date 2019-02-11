/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701sp1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code xsd_attribute} asset type in IGC, displayed as '{@literal XSD Attribute}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class XsdAttribute extends Reference {

    public static String getIgcTypeId() { return "xsd_attribute"; }
    public static String getIgcTypeDisplayName() { return "XSD Attribute"; }

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
     * The {@code context} property, displayed as '{@literal Parent XSD Object}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList context;

    /**
     * The {@code namespace} property, displayed as '{@literal Namespace}' in the IGC UI.
     */
    protected String namespace;

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
     * The {@code referenced_by_xsd_elements} property, displayed as '{@literal Referenced by XSD Elements}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList referenced_by_xsd_elements;

    /**
     * The {@code referenced_by_xsd_complex_type} property, displayed as '{@literal Referenced by XSD Complex Types}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdComplexType} objects.
     */
    protected ReferenceList referenced_by_xsd_complex_type;

    /**
     * The {@code referenced_by_xsd_attribute_groups} property, displayed as '{@literal Referenced by XSD Attribute Groups}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdAttributeGroup} objects.
     */
    protected ReferenceList referenced_by_xsd_attribute_groups;

    /**
     * The {@code xsd_simple_type_definition} property, displayed as '{@literal XSD Simple Type Definition}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XsdSimpleType} object.
     */
    protected Reference xsd_simple_type_definition;

    /**
     * The {@code data_type} property, displayed as '{@literal Data Type}' in the IGC UI.
     */
    protected String data_type;

    /**
     * The {@code default_value} property, displayed as '{@literal Default Value}' in the IGC UI.
     */
    protected String default_value;

    /**
     * The {@code usage} property, displayed as '{@literal Usage}' in the IGC UI.
     */
    protected String usage;

    /**
     * The {@code inheritable} property, displayed as '{@literal Inheritable}' in the IGC UI.
     */
    protected Boolean inheritable;

    /**
     * The {@code fixed_value} property, displayed as '{@literal Fixed Value}' in the IGC UI.
     */
    protected String fixed_value;

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
     * The {@code fraction_digits} property, displayed as '{@literal Fraction}' in the IGC UI.
     */
    protected Number fraction_digits;

    /**
     * The {@code total_digits} property, displayed as '{@literal Total Digits}' in the IGC UI.
     */
    protected Number total_digits;

    /**
     * The {@code white_space} property, displayed as '{@literal Total Whitespace}' in the IGC UI.
     */
    protected String white_space;

    /**
     * The {@code timezone} property, displayed as '{@literal Timezone}' in the IGC UI.
     */
    protected String timezone;

    /**
     * The {@code name_form} property, displayed as '{@literal Form}' in the IGC UI.
     */
    protected String name_form;

    /**
     * The {@code pattern_expression} property, displayed as '{@literal Pattern}' in the IGC UI.
     */
    protected ArrayList<String> pattern_expression;

    /**
     * The {@code minimum_range} property, displayed as '{@literal Minimum Range}' in the IGC UI.
     */
    protected ArrayList<String> minimum_range;

    /**
     * The {@code is_minimum_range_inclusive} property, displayed as '{@literal Minimum Range Inclusive}' in the IGC UI.
     */
    protected Boolean is_minimum_range_inclusive;

    /**
     * The {@code maximum_range} property, displayed as '{@literal Maximum Range}' in the IGC UI.
     */
    protected ArrayList<String> maximum_range;

    /**
     * The {@code is_maximum_range_inclusive} property, displayed as '{@literal Maximum Range Inclusive}' in the IGC UI.
     */
    protected Boolean is_maximum_range_inclusive;

    /**
     * The {@code enumeration_value} property, displayed as '{@literal Enumeration Values}' in the IGC UI.
     */
    protected ArrayList<String> enumeration_value;

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

    /** @see #context */ @JsonProperty("context")  public ReferenceList getTheContext() { return this.context; }
    /** @see #context */ @JsonProperty("context")  public void setTheContext(ReferenceList context) { this.context = context; }

    /** @see #namespace */ @JsonProperty("namespace")  public String getNamespace() { return this.namespace; }
    /** @see #namespace */ @JsonProperty("namespace")  public void setNamespace(String namespace) { this.namespace = namespace; }

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

    /** @see #referenced_by_xsd_elements */ @JsonProperty("referenced_by_xsd_elements")  public ReferenceList getReferencedByXsdElements() { return this.referenced_by_xsd_elements; }
    /** @see #referenced_by_xsd_elements */ @JsonProperty("referenced_by_xsd_elements")  public void setReferencedByXsdElements(ReferenceList referenced_by_xsd_elements) { this.referenced_by_xsd_elements = referenced_by_xsd_elements; }

    /** @see #referenced_by_xsd_complex_type */ @JsonProperty("referenced_by_xsd_complex_type")  public ReferenceList getReferencedByXsdComplexType() { return this.referenced_by_xsd_complex_type; }
    /** @see #referenced_by_xsd_complex_type */ @JsonProperty("referenced_by_xsd_complex_type")  public void setReferencedByXsdComplexType(ReferenceList referenced_by_xsd_complex_type) { this.referenced_by_xsd_complex_type = referenced_by_xsd_complex_type; }

    /** @see #referenced_by_xsd_attribute_groups */ @JsonProperty("referenced_by_xsd_attribute_groups")  public ReferenceList getReferencedByXsdAttributeGroups() { return this.referenced_by_xsd_attribute_groups; }
    /** @see #referenced_by_xsd_attribute_groups */ @JsonProperty("referenced_by_xsd_attribute_groups")  public void setReferencedByXsdAttributeGroups(ReferenceList referenced_by_xsd_attribute_groups) { this.referenced_by_xsd_attribute_groups = referenced_by_xsd_attribute_groups; }

    /** @see #xsd_simple_type_definition */ @JsonProperty("xsd_simple_type_definition")  public Reference getXsdSimpleTypeDefinition() { return this.xsd_simple_type_definition; }
    /** @see #xsd_simple_type_definition */ @JsonProperty("xsd_simple_type_definition")  public void setXsdSimpleTypeDefinition(Reference xsd_simple_type_definition) { this.xsd_simple_type_definition = xsd_simple_type_definition; }

    /** @see #data_type */ @JsonProperty("data_type")  public String getDataType() { return this.data_type; }
    /** @see #data_type */ @JsonProperty("data_type")  public void setDataType(String data_type) { this.data_type = data_type; }

    /** @see #default_value */ @JsonProperty("default_value")  public String getDefaultValue() { return this.default_value; }
    /** @see #default_value */ @JsonProperty("default_value")  public void setDefaultValue(String default_value) { this.default_value = default_value; }

    /** @see #usage */ @JsonProperty("usage")  public String getUsage() { return this.usage; }
    /** @see #usage */ @JsonProperty("usage")  public void setUsage(String usage) { this.usage = usage; }

    /** @see #inheritable */ @JsonProperty("inheritable")  public Boolean getInheritable() { return this.inheritable; }
    /** @see #inheritable */ @JsonProperty("inheritable")  public void setInheritable(Boolean inheritable) { this.inheritable = inheritable; }

    /** @see #fixed_value */ @JsonProperty("fixed_value")  public String getFixedValue() { return this.fixed_value; }
    /** @see #fixed_value */ @JsonProperty("fixed_value")  public void setFixedValue(String fixed_value) { this.fixed_value = fixed_value; }

    /** @see #length */ @JsonProperty("length")  public Number getLength() { return this.length; }
    /** @see #length */ @JsonProperty("length")  public void setLength(Number length) { this.length = length; }

    /** @see #max_length */ @JsonProperty("max_length")  public Number getMaxLength() { return this.max_length; }
    /** @see #max_length */ @JsonProperty("max_length")  public void setMaxLength(Number max_length) { this.max_length = max_length; }

    /** @see #min_length */ @JsonProperty("min_length")  public Number getMinLength() { return this.min_length; }
    /** @see #min_length */ @JsonProperty("min_length")  public void setMinLength(Number min_length) { this.min_length = min_length; }

    /** @see #fraction_digits */ @JsonProperty("fraction_digits")  public Number getFractionDigits() { return this.fraction_digits; }
    /** @see #fraction_digits */ @JsonProperty("fraction_digits")  public void setFractionDigits(Number fraction_digits) { this.fraction_digits = fraction_digits; }

    /** @see #total_digits */ @JsonProperty("total_digits")  public Number getTotalDigits() { return this.total_digits; }
    /** @see #total_digits */ @JsonProperty("total_digits")  public void setTotalDigits(Number total_digits) { this.total_digits = total_digits; }

    /** @see #white_space */ @JsonProperty("white_space")  public String getWhiteSpace() { return this.white_space; }
    /** @see #white_space */ @JsonProperty("white_space")  public void setWhiteSpace(String white_space) { this.white_space = white_space; }

    /** @see #timezone */ @JsonProperty("timezone")  public String getTimezone() { return this.timezone; }
    /** @see #timezone */ @JsonProperty("timezone")  public void setTimezone(String timezone) { this.timezone = timezone; }

    /** @see #name_form */ @JsonProperty("name_form")  public String getNameForm() { return this.name_form; }
    /** @see #name_form */ @JsonProperty("name_form")  public void setNameForm(String name_form) { this.name_form = name_form; }

    /** @see #pattern_expression */ @JsonProperty("pattern_expression")  public ArrayList<String> getPatternExpression() { return this.pattern_expression; }
    /** @see #pattern_expression */ @JsonProperty("pattern_expression")  public void setPatternExpression(ArrayList<String> pattern_expression) { this.pattern_expression = pattern_expression; }

    /** @see #minimum_range */ @JsonProperty("minimum_range")  public ArrayList<String> getMinimumRange() { return this.minimum_range; }
    /** @see #minimum_range */ @JsonProperty("minimum_range")  public void setMinimumRange(ArrayList<String> minimum_range) { this.minimum_range = minimum_range; }

    /** @see #is_minimum_range_inclusive */ @JsonProperty("is_minimum_range_inclusive")  public Boolean getIsMinimumRangeInclusive() { return this.is_minimum_range_inclusive; }
    /** @see #is_minimum_range_inclusive */ @JsonProperty("is_minimum_range_inclusive")  public void setIsMinimumRangeInclusive(Boolean is_minimum_range_inclusive) { this.is_minimum_range_inclusive = is_minimum_range_inclusive; }

    /** @see #maximum_range */ @JsonProperty("maximum_range")  public ArrayList<String> getMaximumRange() { return this.maximum_range; }
    /** @see #maximum_range */ @JsonProperty("maximum_range")  public void setMaximumRange(ArrayList<String> maximum_range) { this.maximum_range = maximum_range; }

    /** @see #is_maximum_range_inclusive */ @JsonProperty("is_maximum_range_inclusive")  public Boolean getIsMaximumRangeInclusive() { return this.is_maximum_range_inclusive; }
    /** @see #is_maximum_range_inclusive */ @JsonProperty("is_maximum_range_inclusive")  public void setIsMaximumRangeInclusive(Boolean is_maximum_range_inclusive) { this.is_maximum_range_inclusive = is_maximum_range_inclusive; }

    /** @see #enumeration_value */ @JsonProperty("enumeration_value")  public ArrayList<String> getEnumerationValue() { return this.enumeration_value; }
    /** @see #enumeration_value */ @JsonProperty("enumeration_value")  public void setEnumerationValue(ArrayList<String> enumeration_value) { this.enumeration_value = enumeration_value; }

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
        "namespace",
        "data_type",
        "default_value",
        "usage",
        "inheritable",
        "fixed_value",
        "length",
        "max_length",
        "min_length",
        "fraction_digits",
        "total_digits",
        "white_space",
        "timezone",
        "name_form",
        "pattern_expression",
        "minimum_range",
        "is_minimum_range_inclusive",
        "maximum_range",
        "is_maximum_range_inclusive",
        "enumeration_value",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "namespace",
        "data_type",
        "default_value",
        "usage",
        "fixed_value",
        "white_space",
        "timezone",
        "name_form",
        "pattern_expression",
        "minimum_range",
        "maximum_range",
        "enumeration_value",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "context",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "referenced_by_xsd_elements",
        "referenced_by_xsd_complex_type",
        "referenced_by_xsd_attribute_groups",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "context",
        "namespace",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "referenced_by_xsd_elements",
        "referenced_by_xsd_complex_type",
        "referenced_by_xsd_attribute_groups",
        "xsd_simple_type_definition",
        "data_type",
        "default_value",
        "usage",
        "inheritable",
        "fixed_value",
        "length",
        "max_length",
        "min_length",
        "fraction_digits",
        "total_digits",
        "white_space",
        "timezone",
        "name_form",
        "pattern_expression",
        "minimum_range",
        "is_minimum_range_inclusive",
        "maximum_range",
        "is_maximum_range_inclusive",
        "enumeration_value",
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
    public static Boolean isXsdAttribute(Object obj) { return (obj.getClass() == XsdAttribute.class); }

}
