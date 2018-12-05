/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'olaphierarchyassociation' asset type in IGC, displayed as 'OLAPHierarchyAssociation' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Olaphierarchyassociation extends MainObject {

    public static final String IGC_TYPE_ID = "olaphierarchyassociation";

    /**
     * The 'of_olap_hierarchy_member' property, displayed as 'Of OLAP Hierarchy Member' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiHierarchyMember} object.
     */
    protected Reference of_olap_hierarchy_member;

    /**
     * The 'has_parent_olap_level' property, displayed as 'Has Parent OLAP Level' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiLevel} object.
     */
    protected Reference has_parent_olap_level;

    /**
     * The 'has_child_olap_level' property, displayed as 'Has Child OLAP Level' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiLevel} object.
     */
    protected Reference has_child_olap_level;

    /**
     * The 'x_of_olap_level' property, displayed as 'X Of OLAP Level' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiLevel} objects.
     */
    protected ReferenceList x_of_olap_level;

    /**
     * The 'sequence' property, displayed as 'Sequence' in the IGC UI.
     */
    protected Number sequence;

    /**
     * The 'business_name' property, displayed as 'Business Name' in the IGC UI.
     */
    protected String business_name;


    /** @see #of_olap_hierarchy_member */ @JsonProperty("of_olap_hierarchy_member")  public Reference getOfOlapHierarchyMember() { return this.of_olap_hierarchy_member; }
    /** @see #of_olap_hierarchy_member */ @JsonProperty("of_olap_hierarchy_member")  public void setOfOlapHierarchyMember(Reference of_olap_hierarchy_member) { this.of_olap_hierarchy_member = of_olap_hierarchy_member; }

    /** @see #has_parent_olap_level */ @JsonProperty("has_parent_olap_level")  public Reference getHasParentOlapLevel() { return this.has_parent_olap_level; }
    /** @see #has_parent_olap_level */ @JsonProperty("has_parent_olap_level")  public void setHasParentOlapLevel(Reference has_parent_olap_level) { this.has_parent_olap_level = has_parent_olap_level; }

    /** @see #has_child_olap_level */ @JsonProperty("has_child_olap_level")  public Reference getHasChildOlapLevel() { return this.has_child_olap_level; }
    /** @see #has_child_olap_level */ @JsonProperty("has_child_olap_level")  public void setHasChildOlapLevel(Reference has_child_olap_level) { this.has_child_olap_level = has_child_olap_level; }

    /** @see #x_of_olap_level */ @JsonProperty("x_of_olap_level")  public ReferenceList getXOfOlapLevel() { return this.x_of_olap_level; }
    /** @see #x_of_olap_level */ @JsonProperty("x_of_olap_level")  public void setXOfOlapLevel(ReferenceList x_of_olap_level) { this.x_of_olap_level = x_of_olap_level; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }

    /** @see #business_name */ @JsonProperty("business_name")  public String getBusinessName() { return this.business_name; }
    /** @see #business_name */ @JsonProperty("business_name")  public void setBusinessName(String business_name) { this.business_name = business_name; }


    public static final Boolean isOlaphierarchyassociation(Object obj) { return (obj.getClass() == Olaphierarchyassociation.class); }

}
