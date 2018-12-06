/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'customattributedef' asset type in IGC, displayed as 'CustomAttributeDef' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Customattributedef extends MainObject {

    public static final String IGC_TYPE_ID = "customattributedef";

    /**
     * The 'has_valid_values' property, displayed as 'Has Valid Values' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Validvalues} object.
     */
    protected Reference has_valid_values;

    /**
     * The 'of_class_descriptor' property, displayed as 'Of Class Descriptor' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Classdescriptor} object.
     */
    protected Reference of_class_descriptor;

    /**
     * The 'has_custom_attribute_val' property, displayed as 'Has Custom Attribute Val' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Customattributeval} objects.
     */
    protected ReferenceList has_custom_attribute_val;

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'data_type' property, displayed as 'Data Type' in the IGC UI.
     */
    protected String data_type;

    /**
     * The 'has_data_values' property, displayed as 'Has Data Values' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataItemValue} object.
     */
    protected Reference has_data_values;


    /** @see #has_valid_values */ @JsonProperty("has_valid_values")  public Reference getHasValidValues() { return this.has_valid_values; }
    /** @see #has_valid_values */ @JsonProperty("has_valid_values")  public void setHasValidValues(Reference has_valid_values) { this.has_valid_values = has_valid_values; }

    /** @see #of_class_descriptor */ @JsonProperty("of_class_descriptor")  public Reference getOfClassDescriptor() { return this.of_class_descriptor; }
    /** @see #of_class_descriptor */ @JsonProperty("of_class_descriptor")  public void setOfClassDescriptor(Reference of_class_descriptor) { this.of_class_descriptor = of_class_descriptor; }

    /** @see #has_custom_attribute_val */ @JsonProperty("has_custom_attribute_val")  public ReferenceList getHasCustomAttributeVal() { return this.has_custom_attribute_val; }
    /** @see #has_custom_attribute_val */ @JsonProperty("has_custom_attribute_val")  public void setHasCustomAttributeVal(ReferenceList has_custom_attribute_val) { this.has_custom_attribute_val = has_custom_attribute_val; }

    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #data_type */ @JsonProperty("data_type")  public String getDataType() { return this.data_type; }
    /** @see #data_type */ @JsonProperty("data_type")  public void setDataType(String data_type) { this.data_type = data_type; }

    /** @see #has_data_values */ @JsonProperty("has_data_values")  public Reference getHasDataValues() { return this.has_data_values; }
    /** @see #has_data_values */ @JsonProperty("has_data_values")  public void setHasDataValues(Reference has_data_values) { this.has_data_values = has_data_values; }


    public static final Boolean isCustomattributedef(Object obj) { return (obj.getClass() == Customattributedef.class); }

}
