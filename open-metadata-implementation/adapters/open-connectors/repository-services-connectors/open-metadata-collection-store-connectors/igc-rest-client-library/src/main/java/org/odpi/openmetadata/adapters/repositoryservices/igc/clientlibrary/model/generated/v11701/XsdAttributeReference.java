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
 * POJO for the 'xsd_attribute_reference' asset type in IGC, displayed as 'XSD Attribute Reference' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class XsdAttributeReference extends Reference {

    public static String getIgcTypeId() { return "xsd_attribute_reference"; }
    public static String getIgcTypeDisplayName() { return "XSD Attribute Reference"; }

    /**
     * The 'usage' property, displayed as 'Usage' in the IGC UI.
     */
    protected String usage;

    /**
     * The 'xsd_element' property, displayed as 'XSD Element' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList xsd_element;

    /**
     * The 'xsd_attribute_group' property, displayed as 'Attribute Group' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdAttributeGroup} objects.
     */
    protected ReferenceList xsd_attribute_group;

    /**
     * The 'xsd_complex_type' property, displayed as 'XSD Complex Type' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdComplexType} objects.
     */
    protected ReferenceList xsd_complex_type;

    /**
     * The 'references_xsd_attribute' property, displayed as 'XSD Attribute' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XsdAttribute} object.
     */
    protected Reference references_xsd_attribute;


    /** @see #usage */ @JsonProperty("usage")  public String getUsage() { return this.usage; }
    /** @see #usage */ @JsonProperty("usage")  public void setUsage(String usage) { this.usage = usage; }

    /** @see #xsd_element */ @JsonProperty("xsd_element")  public ReferenceList getXsdElement() { return this.xsd_element; }
    /** @see #xsd_element */ @JsonProperty("xsd_element")  public void setXsdElement(ReferenceList xsd_element) { this.xsd_element = xsd_element; }

    /** @see #xsd_attribute_group */ @JsonProperty("xsd_attribute_group")  public ReferenceList getXsdAttributeGroup() { return this.xsd_attribute_group; }
    /** @see #xsd_attribute_group */ @JsonProperty("xsd_attribute_group")  public void setXsdAttributeGroup(ReferenceList xsd_attribute_group) { this.xsd_attribute_group = xsd_attribute_group; }

    /** @see #xsd_complex_type */ @JsonProperty("xsd_complex_type")  public ReferenceList getXsdComplexType() { return this.xsd_complex_type; }
    /** @see #xsd_complex_type */ @JsonProperty("xsd_complex_type")  public void setXsdComplexType(ReferenceList xsd_complex_type) { this.xsd_complex_type = xsd_complex_type; }

    /** @see #references_xsd_attribute */ @JsonProperty("references_xsd_attribute")  public Reference getReferencesXsdAttribute() { return this.references_xsd_attribute; }
    /** @see #references_xsd_attribute */ @JsonProperty("references_xsd_attribute")  public void setReferencesXsdAttribute(Reference references_xsd_attribute) { this.references_xsd_attribute = references_xsd_attribute; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "usage"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "xsd_element",
        "xsd_attribute_group",
        "xsd_complex_type"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "usage",
        "xsd_element",
        "xsd_attribute_group",
        "xsd_complex_type",
        "references_xsd_attribute"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isXsdAttributeReference(Object obj) { return (obj.getClass() == XsdAttributeReference.class); }

}
