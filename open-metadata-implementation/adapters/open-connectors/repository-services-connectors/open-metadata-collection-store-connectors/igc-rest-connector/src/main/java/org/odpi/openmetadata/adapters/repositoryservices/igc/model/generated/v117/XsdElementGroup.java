/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'xsd_element_group' asset type in IGC, displayed as 'XSD Element Group' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class XsdElementGroup extends MainObject {

    public static final String IGC_TYPE_ID = "xsd_element_group";

    /**
     * The 'xsd_schema' property, displayed as 'XSD Schema' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XmlSchemaDefinition} object.
     */
    protected Reference xsd_schema;

    /**
     * The 'contains_xsd_elements' property, displayed as 'Contains XSD Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList contains_xsd_elements;

    /**
     * The 'references_xsd_elements' property, displayed as 'References XSD Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdElementReference} objects.
     */
    protected ReferenceList references_xsd_elements;

    /**
     * The 'references_xsd_element_groups' property, displayed as 'References XSD Element Groups' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdElementGroupReference} objects.
     */
    protected ReferenceList references_xsd_element_groups;

    /**
     * The 'references_by_xsd_elements' property, displayed as 'Referenced by XSD Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList references_by_xsd_elements;

    /**
     * The 'referenced_by_xsd_element_groups' property, displayed as 'Referenced by XSD Element Groups' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdElementGroup} objects.
     */
    protected ReferenceList referenced_by_xsd_element_groups;

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
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #xsd_schema */ @JsonProperty("xsd_schema")  public Reference getXsdSchema() { return this.xsd_schema; }
    /** @see #xsd_schema */ @JsonProperty("xsd_schema")  public void setXsdSchema(Reference xsd_schema) { this.xsd_schema = xsd_schema; }

    /** @see #contains_xsd_elements */ @JsonProperty("contains_xsd_elements")  public ReferenceList getContainsXsdElements() { return this.contains_xsd_elements; }
    /** @see #contains_xsd_elements */ @JsonProperty("contains_xsd_elements")  public void setContainsXsdElements(ReferenceList contains_xsd_elements) { this.contains_xsd_elements = contains_xsd_elements; }

    /** @see #references_xsd_elements */ @JsonProperty("references_xsd_elements")  public ReferenceList getReferencesXsdElements() { return this.references_xsd_elements; }
    /** @see #references_xsd_elements */ @JsonProperty("references_xsd_elements")  public void setReferencesXsdElements(ReferenceList references_xsd_elements) { this.references_xsd_elements = references_xsd_elements; }

    /** @see #references_xsd_element_groups */ @JsonProperty("references_xsd_element_groups")  public ReferenceList getReferencesXsdElementGroups() { return this.references_xsd_element_groups; }
    /** @see #references_xsd_element_groups */ @JsonProperty("references_xsd_element_groups")  public void setReferencesXsdElementGroups(ReferenceList references_xsd_element_groups) { this.references_xsd_element_groups = references_xsd_element_groups; }

    /** @see #references_by_xsd_elements */ @JsonProperty("references_by_xsd_elements")  public ReferenceList getReferencesByXsdElements() { return this.references_by_xsd_elements; }
    /** @see #references_by_xsd_elements */ @JsonProperty("references_by_xsd_elements")  public void setReferencesByXsdElements(ReferenceList references_by_xsd_elements) { this.references_by_xsd_elements = references_by_xsd_elements; }

    /** @see #referenced_by_xsd_element_groups */ @JsonProperty("referenced_by_xsd_element_groups")  public ReferenceList getReferencedByXsdElementGroups() { return this.referenced_by_xsd_element_groups; }
    /** @see #referenced_by_xsd_element_groups */ @JsonProperty("referenced_by_xsd_element_groups")  public void setReferencedByXsdElementGroups(ReferenceList referenced_by_xsd_element_groups) { this.referenced_by_xsd_element_groups = referenced_by_xsd_element_groups; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isXsdElementGroup(Object obj) { return (obj.getClass() == XsdElementGroup.class); }

}
