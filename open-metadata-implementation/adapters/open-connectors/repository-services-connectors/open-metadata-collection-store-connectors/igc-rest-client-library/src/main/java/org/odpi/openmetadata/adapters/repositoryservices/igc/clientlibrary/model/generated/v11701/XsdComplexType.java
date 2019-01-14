/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'xsd_complex_type' asset type in IGC, displayed as 'XSD Complex Type' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class XsdComplexType extends Reference {

    public static String getIgcTypeId() { return "xsd_complex_type"; }
    public static String getIgcTypeDisplayName() { return "XSD Complex Type"; }

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
     * The 'xsd_schema' property, displayed as 'XSD Schema' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XmlSchemaDefinition} object.
     */
    protected Reference xsd_schema;

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

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created On' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Modified On' in the IGC UI.
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
        "type",
        "base_type",
        "abstract",
        "mixed",
        "default",
        "block",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "contains_xsd_attributes",
        "contains_xsd_elements",
        "defines_xsd_elements",
        "references_xsd_elements",
        "references_xsd_attributes",
        "references_xsd_element_group",
        "references_xsd_attribute_groups",
        "extended_by_xsd_complex_type",
        "restricted_by_xsd_complex_type",
        "extends_xsd_element",
        "restricts_xsd_element",
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
        "contains_xsd_attributes",
        "contains_xsd_elements",
        "defines_xsd_elements",
        "references_xsd_elements",
        "references_xsd_attributes",
        "references_xsd_element_group",
        "references_xsd_attribute_groups",
        "extends_xsd_complex_type",
        "extended_by_xsd_complex_type",
        "restricts_xsd_complex_type",
        "restricted_by_xsd_complex_type",
        "extends_xsd_simple_type",
        "extends_xsd_element",
        "restricts_xsd_element",
        "type",
        "base_type",
        "abstract",
        "mixed",
        "default",
        "block",
        "in_collections",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isXsdComplexType(Object obj) { return (obj.getClass() == XsdComplexType.class); }

}
