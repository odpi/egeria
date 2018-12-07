/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'xsd_attribute_group' asset type in IGC, displayed as 'XSD Attribute Group' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class XsdAttributeGroup extends MainObject {

    public static final String IGC_TYPE_ID = "xsd_attribute_group";

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
     * The 'references_xsd_attributes' property, displayed as 'References XSD Attributes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdAttributeReference} objects.
     */
    protected ReferenceList references_xsd_attributes;

    /**
     * The 'references_xsd_attribute_groups' property, displayed as 'References XSD Attribute Groups' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdAttributeGroup} objects.
     */
    protected ReferenceList references_xsd_attribute_groups;

    /**
     * The 'referenced_by_xsd_elements' property, displayed as 'Referenced by XSD Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList referenced_by_xsd_elements;

    /**
     * The 'referenced_by_xsd_complex_types' property, displayed as 'Referenced by XSD Complex Types' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdComplexType} objects.
     */
    protected ReferenceList referenced_by_xsd_complex_types;

    /**
     * The 'referenced_by_xsd_attribute_groups' property, displayed as 'Referenced by XSD Attribute Groups' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdAttributeGroup} objects.
     */
    protected ReferenceList referenced_by_xsd_attribute_groups;

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

    /** @see #references_xsd_attributes */ @JsonProperty("references_xsd_attributes")  public ReferenceList getReferencesXsdAttributes() { return this.references_xsd_attributes; }
    /** @see #references_xsd_attributes */ @JsonProperty("references_xsd_attributes")  public void setReferencesXsdAttributes(ReferenceList references_xsd_attributes) { this.references_xsd_attributes = references_xsd_attributes; }

    /** @see #references_xsd_attribute_groups */ @JsonProperty("references_xsd_attribute_groups")  public ReferenceList getReferencesXsdAttributeGroups() { return this.references_xsd_attribute_groups; }
    /** @see #references_xsd_attribute_groups */ @JsonProperty("references_xsd_attribute_groups")  public void setReferencesXsdAttributeGroups(ReferenceList references_xsd_attribute_groups) { this.references_xsd_attribute_groups = references_xsd_attribute_groups; }

    /** @see #referenced_by_xsd_elements */ @JsonProperty("referenced_by_xsd_elements")  public ReferenceList getReferencedByXsdElements() { return this.referenced_by_xsd_elements; }
    /** @see #referenced_by_xsd_elements */ @JsonProperty("referenced_by_xsd_elements")  public void setReferencedByXsdElements(ReferenceList referenced_by_xsd_elements) { this.referenced_by_xsd_elements = referenced_by_xsd_elements; }

    /** @see #referenced_by_xsd_complex_types */ @JsonProperty("referenced_by_xsd_complex_types")  public ReferenceList getReferencedByXsdComplexTypes() { return this.referenced_by_xsd_complex_types; }
    /** @see #referenced_by_xsd_complex_types */ @JsonProperty("referenced_by_xsd_complex_types")  public void setReferencedByXsdComplexTypes(ReferenceList referenced_by_xsd_complex_types) { this.referenced_by_xsd_complex_types = referenced_by_xsd_complex_types; }

    /** @see #referenced_by_xsd_attribute_groups */ @JsonProperty("referenced_by_xsd_attribute_groups")  public ReferenceList getReferencedByXsdAttributeGroups() { return this.referenced_by_xsd_attribute_groups; }
    /** @see #referenced_by_xsd_attribute_groups */ @JsonProperty("referenced_by_xsd_attribute_groups")  public void setReferencedByXsdAttributeGroups(ReferenceList referenced_by_xsd_attribute_groups) { this.referenced_by_xsd_attribute_groups = referenced_by_xsd_attribute_groups; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isXsdAttributeGroup(Object obj) { return (obj.getClass() == XsdAttributeGroup.class); }

}
