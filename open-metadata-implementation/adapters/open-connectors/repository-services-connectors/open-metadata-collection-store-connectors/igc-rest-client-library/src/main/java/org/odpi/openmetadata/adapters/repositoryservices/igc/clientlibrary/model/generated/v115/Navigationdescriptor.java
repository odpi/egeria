/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'navigationdescriptor' asset type in IGC, displayed as 'NavigationDescriptor' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Navigationdescriptor extends MainObject {

    public static final String IGC_TYPE_ID = "navigationdescriptor";

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
     * The 'expression' property, displayed as 'Expression' in the IGC UI.
     */
    protected String expression;

    /**
     * The 'display_name' property, displayed as 'Display Name' in the IGC UI.
     */
    protected String display_name;


    /** @see #of_class_descriptor */ @JsonProperty("of_class_descriptor")  public Reference getOfClassDescriptor() { return this.of_class_descriptor; }
    /** @see #of_class_descriptor */ @JsonProperty("of_class_descriptor")  public void setOfClassDescriptor(Reference of_class_descriptor) { this.of_class_descriptor = of_class_descriptor; }

    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #expression */ @JsonProperty("expression")  public String getExpression() { return this.expression; }
    /** @see #expression */ @JsonProperty("expression")  public void setExpression(String expression) { this.expression = expression; }

    /** @see #display_name */ @JsonProperty("display_name")  public String getDisplayName() { return this.display_name; }
    /** @see #display_name */ @JsonProperty("display_name")  public void setDisplayName(String display_name) { this.display_name = display_name; }


    public static final Boolean isNavigationdescriptor(Object obj) { return (obj.getClass() == Navigationdescriptor.class); }

}
