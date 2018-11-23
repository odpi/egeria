/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'xsd_complex_type' asset type in IGC, displayed as 'XSD Complex Type' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class XsdComplexType extends MainObject {

    public static final String IGC_TYPE_ID = "xsd_complex_type";

    /**
     * The 'xsd_schema' property, displayed as 'XSD Schema' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XmlSchemaDefinition} object.
     */
    protected Reference xsd_schema;

    /**
     * The 'contains_xsd_attributes' property, displayed as 'Contains XSD Attributes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdAttribute} objects.
     */
    protected ReferenceList contains_xsd_attributes;

    /**
     * The 'contains_xsd_elements' property, displayed as 'Contains XSD Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList contains_xsd_elements;

    /**
     * The 'defines_xsd_elements' property, displayed as 'Defines XSD Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList defines_xsd_elements;

    /**
     * The 'references_xsd_elements' property, displayed as 'References XSD Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdElementReference} objects.
     */
    protected ReferenceList references_xsd_elements;

    /**
     * The 'references_xsd_attributes' property, displayed as 'References XSD Attributes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdAttributeReference} objects.
     */
    protected ReferenceList references_xsd_attributes;

    /**
     * The 'references_xsd_element_group' property, displayed as 'References XSD Element Groups' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdElementGroupReference} objects.
     */
    protected ReferenceList references_xsd_element_group;

    /**
     * The 'references_xsd_attribute_groups' property, displayed as 'References XSD Attribute Groups' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdAttributeGroup} objects.
     */
    protected ReferenceList references_xsd_attribute_groups;

    /**
     * The 'extends_xsd_complex_type' property, displayed as 'Extends XSD Complex Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XsdComplexType} object.
     */
    protected Reference extends_xsd_complex_type;

    /**
     * The 'extended_by_xsd_complex_type' property, displayed as 'Extended by XSD Complex Types' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdComplexType} objects.
     */
    protected ReferenceList extended_by_xsd_complex_type;

    /**
     * The 'restricts_xsd_complex_type' property, displayed as 'Restricts XSD Complex Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XsdComplexType} object.
     */
    protected Reference restricts_xsd_complex_type;

    /**
     * The 'restricted_by_xsd_complex_type' property, displayed as 'Restricted by XSD Complex Types' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdComplexType} objects.
     */
    protected ReferenceList restricted_by_xsd_complex_type;

    /**
     * The 'extends_xsd_simple_type' property, displayed as 'Extends XSD Simple Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XsdSimpleType} object.
     */
    protected Reference extends_xsd_simple_type;

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
     * The 'type' property, displayed as 'Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>All (displayed in the UI as 'All')</li>
     *     <li>Choice (displayed in the UI as 'Choice')</li>
     *     <li>Sequence (displayed in the UI as 'Sequence')</li>
     * </ul>
     */
    protected String type;

    /**
     * The 'base_type' property, displayed as 'Data Type' in the IGC UI.
     */
    protected String base_type;

    /**
     * The 'abstract' property, displayed as 'Abstract' in the IGC UI.
     */
    @JsonProperty("abstract") protected Boolean __abstract;

    /**
     * The 'mixed' property, displayed as 'Mixed' in the IGC UI.
     */
    protected Boolean mixed;

    /**
     * The 'default' property, displayed as 'Default' in the IGC UI.
     */
    @JsonProperty("default") protected Boolean __default;

    /**
     * The 'block' property, displayed as 'Block' in the IGC UI.
     */
    protected String block;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #xsd_schema */ @JsonProperty("xsd_schema")  public Reference getXsdSchema() { return this.xsd_schema; }
    /** @see #xsd_schema */ @JsonProperty("xsd_schema")  public void setXsdSchema(Reference xsd_schema) { this.xsd_schema = xsd_schema; }

    /** @see #contains_xsd_attributes */ @JsonProperty("contains_xsd_attributes")  public ReferenceList getContainsXsdAttributes() { return this.contains_xsd_attributes; }
    /** @see #contains_xsd_attributes */ @JsonProperty("contains_xsd_attributes")  public void setContainsXsdAttributes(ReferenceList contains_xsd_attributes) { this.contains_xsd_attributes = contains_xsd_attributes; }

    /** @see #contains_xsd_elements */ @JsonProperty("contains_xsd_elements")  public ReferenceList getContainsXsdElements() { return this.contains_xsd_elements; }
    /** @see #contains_xsd_elements */ @JsonProperty("contains_xsd_elements")  public void setContainsXsdElements(ReferenceList contains_xsd_elements) { this.contains_xsd_elements = contains_xsd_elements; }

    /** @see #defines_xsd_elements */ @JsonProperty("defines_xsd_elements")  public ReferenceList getDefinesXsdElements() { return this.defines_xsd_elements; }
    /** @see #defines_xsd_elements */ @JsonProperty("defines_xsd_elements")  public void setDefinesXsdElements(ReferenceList defines_xsd_elements) { this.defines_xsd_elements = defines_xsd_elements; }

    /** @see #references_xsd_elements */ @JsonProperty("references_xsd_elements")  public ReferenceList getReferencesXsdElements() { return this.references_xsd_elements; }
    /** @see #references_xsd_elements */ @JsonProperty("references_xsd_elements")  public void setReferencesXsdElements(ReferenceList references_xsd_elements) { this.references_xsd_elements = references_xsd_elements; }

    /** @see #references_xsd_attributes */ @JsonProperty("references_xsd_attributes")  public ReferenceList getReferencesXsdAttributes() { return this.references_xsd_attributes; }
    /** @see #references_xsd_attributes */ @JsonProperty("references_xsd_attributes")  public void setReferencesXsdAttributes(ReferenceList references_xsd_attributes) { this.references_xsd_attributes = references_xsd_attributes; }

    /** @see #references_xsd_element_group */ @JsonProperty("references_xsd_element_group")  public ReferenceList getReferencesXsdElementGroup() { return this.references_xsd_element_group; }
    /** @see #references_xsd_element_group */ @JsonProperty("references_xsd_element_group")  public void setReferencesXsdElementGroup(ReferenceList references_xsd_element_group) { this.references_xsd_element_group = references_xsd_element_group; }

    /** @see #references_xsd_attribute_groups */ @JsonProperty("references_xsd_attribute_groups")  public ReferenceList getReferencesXsdAttributeGroups() { return this.references_xsd_attribute_groups; }
    /** @see #references_xsd_attribute_groups */ @JsonProperty("references_xsd_attribute_groups")  public void setReferencesXsdAttributeGroups(ReferenceList references_xsd_attribute_groups) { this.references_xsd_attribute_groups = references_xsd_attribute_groups; }

    /** @see #extends_xsd_complex_type */ @JsonProperty("extends_xsd_complex_type")  public Reference getExtendsXsdComplexType() { return this.extends_xsd_complex_type; }
    /** @see #extends_xsd_complex_type */ @JsonProperty("extends_xsd_complex_type")  public void setExtendsXsdComplexType(Reference extends_xsd_complex_type) { this.extends_xsd_complex_type = extends_xsd_complex_type; }

    /** @see #extended_by_xsd_complex_type */ @JsonProperty("extended_by_xsd_complex_type")  public ReferenceList getExtendedByXsdComplexType() { return this.extended_by_xsd_complex_type; }
    /** @see #extended_by_xsd_complex_type */ @JsonProperty("extended_by_xsd_complex_type")  public void setExtendedByXsdComplexType(ReferenceList extended_by_xsd_complex_type) { this.extended_by_xsd_complex_type = extended_by_xsd_complex_type; }

    /** @see #restricts_xsd_complex_type */ @JsonProperty("restricts_xsd_complex_type")  public Reference getRestrictsXsdComplexType() { return this.restricts_xsd_complex_type; }
    /** @see #restricts_xsd_complex_type */ @JsonProperty("restricts_xsd_complex_type")  public void setRestrictsXsdComplexType(Reference restricts_xsd_complex_type) { this.restricts_xsd_complex_type = restricts_xsd_complex_type; }

    /** @see #restricted_by_xsd_complex_type */ @JsonProperty("restricted_by_xsd_complex_type")  public ReferenceList getRestrictedByXsdComplexType() { return this.restricted_by_xsd_complex_type; }
    /** @see #restricted_by_xsd_complex_type */ @JsonProperty("restricted_by_xsd_complex_type")  public void setRestrictedByXsdComplexType(ReferenceList restricted_by_xsd_complex_type) { this.restricted_by_xsd_complex_type = restricted_by_xsd_complex_type; }

    /** @see #extends_xsd_simple_type */ @JsonProperty("extends_xsd_simple_type")  public Reference getExtendsXsdSimpleType() { return this.extends_xsd_simple_type; }
    /** @see #extends_xsd_simple_type */ @JsonProperty("extends_xsd_simple_type")  public void setExtendsXsdSimpleType(Reference extends_xsd_simple_type) { this.extends_xsd_simple_type = extends_xsd_simple_type; }

    /** @see #extends_xsd_element */ @JsonProperty("extends_xsd_element")  public ReferenceList getExtendsXsdElement() { return this.extends_xsd_element; }
    /** @see #extends_xsd_element */ @JsonProperty("extends_xsd_element")  public void setExtendsXsdElement(ReferenceList extends_xsd_element) { this.extends_xsd_element = extends_xsd_element; }

    /** @see #restricts_xsd_element */ @JsonProperty("restricts_xsd_element")  public ReferenceList getRestrictsXsdElement() { return this.restricts_xsd_element; }
    /** @see #restricts_xsd_element */ @JsonProperty("restricts_xsd_element")  public void setRestrictsXsdElement(ReferenceList restricts_xsd_element) { this.restricts_xsd_element = restricts_xsd_element; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #base_type */ @JsonProperty("base_type")  public String getBaseType() { return this.base_type; }
    /** @see #base_type */ @JsonProperty("base_type")  public void setBaseType(String base_type) { this.base_type = base_type; }

    /** @see #__abstract */ @JsonProperty("abstract")  public Boolean getAbstract() { return this.__abstract; }
    /** @see #__abstract */ @JsonProperty("abstract")  public void setAbstract(Boolean __abstract) { this.__abstract = __abstract; }

    /** @see #mixed */ @JsonProperty("mixed")  public Boolean getMixed() { return this.mixed; }
    /** @see #mixed */ @JsonProperty("mixed")  public void setMixed(Boolean mixed) { this.mixed = mixed; }

    /** @see #__default */ @JsonProperty("default")  public Boolean getDefault() { return this.__default; }
    /** @see #__default */ @JsonProperty("default")  public void setDefault(Boolean __default) { this.__default = __default; }

    /** @see #block */ @JsonProperty("block")  public String getBlock() { return this.block; }
    /** @see #block */ @JsonProperty("block")  public void setBlock(String block) { this.block = block; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isXsdComplexType(Object obj) { return (obj.getClass() == XsdComplexType.class); }

}
