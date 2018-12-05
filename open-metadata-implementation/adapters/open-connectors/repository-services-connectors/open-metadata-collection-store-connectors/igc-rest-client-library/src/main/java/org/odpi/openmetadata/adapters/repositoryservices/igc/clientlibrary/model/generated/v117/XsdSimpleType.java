/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'xsd_simple_type' asset type in IGC, displayed as 'XSD Simple Type' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class XsdSimpleType extends MainObject {

    public static final String IGC_TYPE_ID = "xsd_simple_type";

    /**
     * The 'xsd_schema' property, displayed as 'XSD Schema' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XmlSchemaDefinition} object.
     */
    protected Reference xsd_schema;

    /**
     * The 'defines_xsd_elements' property, displayed as 'Defines XSD Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList defines_xsd_elements;

    /**
     * The 'defines_xsd_attributes' property, displayed as 'Defines XSD Attributes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdAttribute} objects.
     */
    protected ReferenceList defines_xsd_attributes;

    /**
     * The 'extends_xsd_simple_type' property, displayed as 'Extends XSD Simple Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XsdSimpleType} object.
     */
    protected Reference extends_xsd_simple_type;

    /**
     * The 'extended_by_xsd_simple_type' property, displayed as 'Extended by XSD Simple Types' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdSimpleType} objects.
     */
    protected ReferenceList extended_by_xsd_simple_type;

    /**
     * The 'restricts_xsd_simple_type' property, displayed as 'Restricts XSD Simple Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XsdSimpleType} object.
     */
    protected Reference restricts_xsd_simple_type;

    /**
     * The 'restricted_by_xsd_simple_type' property, displayed as 'Restricted by XSD Simple Types' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdSimpleType} objects.
     */
    protected ReferenceList restricted_by_xsd_simple_type;

    /**
     * The 'extends_xsd_element' property, displayed as 'Extends XSD Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList extends_xsd_element;

    /**
     * The 'restricts_xsd_element' property, displayed as 'Restricts XSD Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList restricts_xsd_element;

    /**
     * The 'extended_by_xsd_complex_type' property, displayed as 'Extended by XSD Complex Types' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdComplexType} objects.
     */
    protected ReferenceList extended_by_xsd_complex_type;

    /**
     * The 'base_type' property, displayed as 'Data Type' in the IGC UI.
     */
    protected String base_type;

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
     * The 'fraction' property, displayed as 'Fraction' in the IGC UI.
     */
    protected Number fraction;

    /**
     * The 'total_digits' property, displayed as 'Total Digits' in the IGC UI.
     */
    protected Number total_digits;

    /**
     * The 'whitespace' property, displayed as 'Whitespace' in the IGC UI.
     */
    protected String whitespace;

    /**
     * The 'timezone' property, displayed as 'Timezone' in the IGC UI.
     */
    protected String timezone;

    /**
     * The 'final' property, displayed as 'Final' in the IGC UI.
     */
    @JsonProperty("final") protected String __final;

    /**
     * The 'restriction_pattern' property, displayed as 'Pattern' in the IGC UI.
     */
    protected String restriction_pattern;

    /**
     * The 'restriction_minimum_value' property, displayed as 'Minimum Range' in the IGC UI.
     */
    protected String restriction_minimum_value;

    /**
     * The 'restriction_minimum_inclusive' property, displayed as 'Minimum Range Inclusive' in the IGC UI.
     */
    protected Boolean restriction_minimum_inclusive;

    /**
     * The 'restriction_maximum_value' property, displayed as 'Maximum Range' in the IGC UI.
     */
    protected String restriction_maximum_value;

    /**
     * The 'restriction_maximum_inclusive' property, displayed as 'Maximum Range Inclusive' in the IGC UI.
     */
    protected Boolean restriction_maximum_inclusive;

    /**
     * The 'restriction_enumeration_values' property, displayed as 'Enumeration Values' in the IGC UI.
     */
    protected ArrayList<String> restriction_enumeration_values;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #xsd_schema */ @JsonProperty("xsd_schema")  public Reference getXsdSchema() { return this.xsd_schema; }
    /** @see #xsd_schema */ @JsonProperty("xsd_schema")  public void setXsdSchema(Reference xsd_schema) { this.xsd_schema = xsd_schema; }

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


    public static final Boolean isXsdSimpleType(Object obj) { return (obj.getClass() == XsdSimpleType.class); }

}
