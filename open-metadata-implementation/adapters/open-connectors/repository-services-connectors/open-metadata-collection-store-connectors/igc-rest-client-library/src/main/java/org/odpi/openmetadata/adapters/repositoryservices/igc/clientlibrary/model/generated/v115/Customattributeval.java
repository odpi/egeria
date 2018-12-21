/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'customattributeval' asset type in IGC, displayed as 'CustomAttributeVal' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Customattributeval extends Reference {

    public static String getIgcTypeId() { return "customattributeval"; }
    public static String getIgcTypeDisplayName() { return "CustomAttributeVal"; }

    /**
     * The 'x_custom_attribute_name' property, displayed as 'X Custom Attribute Name' in the IGC UI.
     */
    protected String x_custom_attribute_name;

    /**
     * The 'value' property, displayed as 'Value' in the IGC UI.
     */
    protected String value;


    /** @see #x_custom_attribute_name */ @JsonProperty("x_custom_attribute_name")  public String getXCustomAttributeName() { return this.x_custom_attribute_name; }
    /** @see #x_custom_attribute_name */ @JsonProperty("x_custom_attribute_name")  public void setXCustomAttributeName(String x_custom_attribute_name) { this.x_custom_attribute_name = x_custom_attribute_name; }

    /** @see #value */ @JsonProperty("value")  public String getValue() { return this.value; }
    /** @see #value */ @JsonProperty("value")  public void setValue(String value) { this.value = value; }

    public static final Boolean canBeCreated() { return false; }
    public static final Boolean includesModificationDetails() { return false; }
    public static final ArrayList<String> NON_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("x_custom_attribute_name");
        add("value");
    }};
    public static final List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static final Boolean isCustomattributeval(Object obj) { return (obj.getClass() == Customattributeval.class); }

}
