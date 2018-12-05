/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'propdescriptor' asset type in IGC, displayed as 'PropDescriptor' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Propdescriptor extends MainObject {

    public static final String IGC_TYPE_ID = "propdescriptor";

    /**
     * The 'value_expression' property, displayed as 'Value Expression' in the IGC UI.
     */
    protected String value_expression;

    /**
     * The 'of_class_descriptor' property, displayed as 'Of Class Descriptor' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Classdescriptor} object.
     */
    protected Reference of_class_descriptor;

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'is_attribute' property, displayed as 'Is Attribute' in the IGC UI.
     */
    protected Boolean is_attribute;

    /**
     * The 'value_type' property, displayed as 'Value Type' in the IGC UI.
     */
    protected String value_type;

    /**
     * The 'attribute_name' property, displayed as 'Attribute Name' in the IGC UI.
     */
    protected String attribute_name;

    /**
     * The 'display_name' property, displayed as 'Display Name' in the IGC UI.
     */
    protected String display_name;


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


    public static final Boolean isPropdescriptor(Object obj) { return (obj.getClass() == Propdescriptor.class); }

}
