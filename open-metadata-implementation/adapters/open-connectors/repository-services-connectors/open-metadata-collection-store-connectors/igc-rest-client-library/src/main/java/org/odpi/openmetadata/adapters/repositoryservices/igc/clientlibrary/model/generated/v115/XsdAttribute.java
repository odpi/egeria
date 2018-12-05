/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'xsd_attribute' asset type in IGC, displayed as 'XSD Attribute' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class XsdAttribute extends MainObject {

    public static final String IGC_TYPE_ID = "xsd_attribute";

    /**
     * The 'context' property, displayed as 'Parent XSD Object' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList context;

    /**
     * The 'namespace' property, displayed as 'Namespace' in the IGC UI.
     */
    protected String namespace;

    /**
     * The 'referenced_by_xsd_elements' property, displayed as 'Referenced by XSD Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList referenced_by_xsd_elements;

    /**
     * The 'referenced_by_xsd_complex_type' property, displayed as 'Referenced by XSD Complex Types' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdComplexType} objects.
     */
    protected ReferenceList referenced_by_xsd_complex_type;

    /**
     * The 'referenced_by_xsd_attribute_groups' property, displayed as 'Referenced by XSD Attribute Groups' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdAttributeGroup} objects.
     */
    protected ReferenceList referenced_by_xsd_attribute_groups;

    /**
     * The 'xsd_simple_type_definition' property, displayed as 'XSD Simple Type Definition' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XsdSimpleType} object.
     */
    protected Reference xsd_simple_type_definition;

    /**
     * The 'data_type' property, displayed as 'Data Type' in the IGC UI.
     */
    protected String data_type;

    /**
     * The 'default_value' property, displayed as 'Default Value' in the IGC UI.
     */
    protected String default_value;

    /**
     * The 'usage' property, displayed as 'Usage' in the IGC UI.
     */
    protected String usage;

    /**
     * The 'inheritable' property, displayed as 'Inheritable' in the IGC UI.
     */
    protected Boolean inheritable;

    /**
     * The 'fixed_value' property, displayed as 'Fixed Value' in the IGC UI.
     */
    protected String fixed_value;

    /**
     * The 'length' property, displayed as 'Length' in the IGC UI.
     */
    protected Number length;

    /**
     * The 'max_length' property, displayed as 'Maximum Length' in the IGC UI.
     */
    protected Number max_length;

    /**
     * The 'min_length' property, displayed as 'Minimum Length' in the IGC UI.
     */
    protected Number min_length;

    /**
     * The 'fraction_digits' property, displayed as 'Fraction' in the IGC UI.
     */
    protected Number fraction_digits;

    /**
     * The 'total_digits' property, displayed as 'Total Digits' in the IGC UI.
     */
    protected Number total_digits;

    /**
     * The 'white_space' property, displayed as 'Total Whitespace' in the IGC UI.
     */
    protected String white_space;

    /**
     * The 'timezone' property, displayed as 'Timezone' in the IGC UI.
     */
    protected String timezone;

    /**
     * The 'name_form' property, displayed as 'Form' in the IGC UI.
     */
    protected String name_form;

    /**
     * The 'pattern_expression' property, displayed as 'Pattern' in the IGC UI.
     */
    protected ArrayList<String> pattern_expression;

    /**
     * The 'minimum_range' property, displayed as 'Minimum Range' in the IGC UI.
     */
    protected ArrayList<String> minimum_range;

    /**
     * The 'is_minimum_range_inclusive' property, displayed as 'Minimum Range Inclusive' in the IGC UI.
     */
    protected Boolean is_minimum_range_inclusive;

    /**
     * The 'maximum_range' property, displayed as 'Maximum Range' in the IGC UI.
     */
    protected ArrayList<String> maximum_range;

    /**
     * The 'is_maximum_range_inclusive' property, displayed as 'Maximum Range Inclusive' in the IGC UI.
     */
    protected Boolean is_maximum_range_inclusive;

    /**
     * The 'enumeration_value' property, displayed as 'Enumeration Values' in the IGC UI.
     */
    protected ArrayList<String> enumeration_value;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #context */ @JsonProperty("context")  public ReferenceList getTheContext() { return this.context; }
    /** @see #context */ @JsonProperty("context")  public void setTheContext(ReferenceList context) { this.context = context; }

    /** @see #namespace */ @JsonProperty("namespace")  public String getNamespace() { return this.namespace; }
    /** @see #namespace */ @JsonProperty("namespace")  public void setNamespace(String namespace) { this.namespace = namespace; }

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


    public static final Boolean isXsdAttribute(Object obj) { return (obj.getClass() == XsdAttribute.class); }

}
