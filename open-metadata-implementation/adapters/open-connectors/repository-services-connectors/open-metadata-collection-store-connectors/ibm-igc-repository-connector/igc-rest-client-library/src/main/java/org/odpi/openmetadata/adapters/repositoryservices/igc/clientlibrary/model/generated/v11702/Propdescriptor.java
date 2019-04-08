/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11702;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code propdescriptor} asset type in IGC, displayed as '{@literal PropDescriptor}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Propdescriptor extends Reference {

    public static String getIgcTypeId() { return "propdescriptor"; }
    public static String getIgcTypeDisplayName() { return "PropDescriptor"; }

    /**
     * The {@code value_expression} property, displayed as '{@literal Value Expression}' in the IGC UI.
     */
    protected String value_expression;

    /**
     * The {@code of_class_descriptor} property, displayed as '{@literal Of Class Descriptor}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Classdescriptor} object.
     */
    protected Reference of_class_descriptor;

    /**
     * The {@code description} property, displayed as '{@literal Description}' in the IGC UI.
     */
    protected String description;

    /**
     * The {@code is_attribute} property, displayed as '{@literal Is Attribute}' in the IGC UI.
     */
    protected Boolean is_attribute;

    /**
     * The {@code value_type} property, displayed as '{@literal Value Type}' in the IGC UI.
     */
    protected String value_type;

    /**
     * The {@code attribute_name} property, displayed as '{@literal Attribute Name}' in the IGC UI.
     */
    protected String attribute_name;

    /**
     * The {@code display_name} property, displayed as '{@literal Display Name}' in the IGC UI.
     */
    protected String display_name;

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

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


    /** @see #value_expression */ @JsonProperty("value_expression")  public String getValueExpression() { return this.value_expression; }
    /** @see #value_expression */ @JsonProperty("value_expression")  public void setValueExpression(String value_expression) { this.value_expression = value_expression; }

    /** @see #of_class_descriptor */ @JsonProperty("of_class_descriptor")  public Reference getOfClassDescriptor() { return this.of_class_descriptor; }
    /** @see #of_class_descriptor */ @JsonProperty("of_class_descriptor")  public void setOfClassDescriptor(Reference of_class_descriptor) { this.of_class_descriptor = of_class_descriptor; }

    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #is_attribute */ @JsonProperty("is_attribute")  public Boolean getIsAttribute() { return this.is_attribute; }
    /** @see #is_attribute */ @JsonProperty("is_attribute")  public void setIsAttribute(Boolean is_attribute) { this.is_attribute = is_attribute; }

    /** @see #value_type */ @JsonProperty("value_type")  public String getValueType() { return this.value_type; }
    /** @see #value_type */ @JsonProperty("value_type")  public void setValueType(String value_type) { this.value_type = value_type; }

    /** @see #attribute_name */ @JsonProperty("attribute_name")  public String getAttributeName() { return this.attribute_name; }
    /** @see #attribute_name */ @JsonProperty("attribute_name")  public void setAttributeName(String attribute_name) { this.attribute_name = attribute_name; }

    /** @see #display_name */ @JsonProperty("display_name")  public String getDisplayName() { return this.display_name; }
    /** @see #display_name */ @JsonProperty("display_name")  public void setDisplayName(String display_name) { this.display_name = display_name; }

    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

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
        "value_expression",
        "description",
        "is_attribute",
        "value_type",
        "attribute_name",
        "display_name",
        "name",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "value_expression",
        "description",
        "value_type",
        "attribute_name",
        "display_name",
        "name",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "value_expression",
        "of_class_descriptor",
        "description",
        "is_attribute",
        "value_type",
        "attribute_name",
        "display_name",
        "name",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isPropdescriptor(Object obj) { return (obj.getClass() == Propdescriptor.class); }

}
