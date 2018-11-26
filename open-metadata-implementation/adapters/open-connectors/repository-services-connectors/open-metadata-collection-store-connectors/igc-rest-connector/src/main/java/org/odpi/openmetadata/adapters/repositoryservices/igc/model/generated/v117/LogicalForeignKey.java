/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'logical_foreign_key' asset type in IGC, displayed as 'Logical Foreign Key' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class LogicalForeignKey extends MainObject {

    public static final String IGC_TYPE_ID = "logical_foreign_key";

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
     * The 'referenced_by_entity_attributes' property, displayed as 'Parent Entity Attribute' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link EntityAttribute} objects.
     */
    protected ReferenceList referenced_by_entity_attributes;

    /**
     * The 'child_entity_attributes' property, displayed as 'Child Entity Attributes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link EntityAttribute} objects.
     */
    protected ReferenceList child_entity_attributes;


    /** @see #logical_entity */ @JsonProperty("logical_entity")  public Reference getLogicalEntity() { return this.logical_entity; }
    /** @see #logical_entity */ @JsonProperty("logical_entity")  public void setLogicalEntity(Reference logical_entity) { this.logical_entity = logical_entity; }

    /** @see #physical_name */ @JsonProperty("physical_name")  public String getPhysicalName() { return this.physical_name; }
    /** @see #physical_name */ @JsonProperty("physical_name")  public void setPhysicalName(String physical_name) { this.physical_name = physical_name; }

    /** @see #referenced_by_entity_attributes */ @JsonProperty("referenced_by_entity_attributes")  public ReferenceList getReferencedByEntityAttributes() { return this.referenced_by_entity_attributes; }
    /** @see #referenced_by_entity_attributes */ @JsonProperty("referenced_by_entity_attributes")  public void setReferencedByEntityAttributes(ReferenceList referenced_by_entity_attributes) { this.referenced_by_entity_attributes = referenced_by_entity_attributes; }

    /** @see #child_entity_attributes */ @JsonProperty("child_entity_attributes")  public ReferenceList getChildEntityAttributes() { return this.child_entity_attributes; }
    /** @see #child_entity_attributes */ @JsonProperty("child_entity_attributes")  public void setChildEntityAttributes(ReferenceList child_entity_attributes) { this.child_entity_attributes = child_entity_attributes; }


    public static final Boolean isLogicalForeignKey(Object obj) { return (obj.getClass() == LogicalForeignKey.class); }

}
