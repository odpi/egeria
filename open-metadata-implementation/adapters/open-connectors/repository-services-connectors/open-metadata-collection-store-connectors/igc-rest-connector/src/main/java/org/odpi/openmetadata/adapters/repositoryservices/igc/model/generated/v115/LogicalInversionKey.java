/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'logical_inversion_key' asset type in IGC, displayed as 'Logical Inversion Key' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class LogicalInversionKey extends MainObject {

    public static final String IGC_TYPE_ID = "logical_inversion_key";

    /**
     * The 'logical_entity' property, displayed as 'Logical Entity' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LogicalEntity} object.
     */
    protected Reference logical_entity;

    /**
     * The 'physical_name' property, displayed as 'Physical Name' in the IGC UI.
     */
    protected String physical_name;

    /**
     * The 'defined_on_entity_attributes' property, displayed as 'Referenced by Entity Attributes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link EntityAttribute} objects.
     */
    protected ReferenceList defined_on_entity_attributes;

    /**
     * The 'expression' property, displayed as 'Expression' in the IGC UI.
     */
    protected ArrayList<String> expression;


    /** @see #logical_entity */ @JsonProperty("logical_entity")  public Reference getLogicalEntity() { return this.logical_entity; }
    /** @see #logical_entity */ @JsonProperty("logical_entity")  public void setLogicalEntity(Reference logical_entity) { this.logical_entity = logical_entity; }

    /** @see #physical_name */ @JsonProperty("physical_name")  public String getPhysicalName() { return this.physical_name; }
    /** @see #physical_name */ @JsonProperty("physical_name")  public void setPhysicalName(String physical_name) { this.physical_name = physical_name; }

    /** @see #defined_on_entity_attributes */ @JsonProperty("defined_on_entity_attributes")  public ReferenceList getDefinedOnEntityAttributes() { return this.defined_on_entity_attributes; }
    /** @see #defined_on_entity_attributes */ @JsonProperty("defined_on_entity_attributes")  public void setDefinedOnEntityAttributes(ReferenceList defined_on_entity_attributes) { this.defined_on_entity_attributes = defined_on_entity_attributes; }

    /** @see #expression */ @JsonProperty("expression")  public ArrayList<String> getExpression() { return this.expression; }
    /** @see #expression */ @JsonProperty("expression")  public void setExpression(ArrayList<String> expression) { this.expression = expression; }


    public static final Boolean isLogicalInversionKey(Object obj) { return (obj.getClass() == LogicalInversionKey.class); }

}
