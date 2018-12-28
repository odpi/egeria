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
 * POJO for the 'olaphierarchyassociation' asset type in IGC, displayed as 'OLAPHierarchyAssociation' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Olaphierarchyassociation extends Reference {

    public static String getIgcTypeId() { return "olaphierarchyassociation"; }
    public static String getIgcTypeDisplayName() { return "OLAPHierarchyAssociation"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'long_description' property, displayed as 'Long Description' in the IGC UI.
     */
    protected String long_description;

    /**
     * The 'labels' property, displayed as 'Labels' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The 'stewards' property, displayed as 'Stewards' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The 'assigned_to_terms' property, displayed as 'Assigned to Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The 'implements_rules' property, displayed as 'Implements Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The 'governed_by_rules' property, displayed as 'Governed by Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

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


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #labels */ @JsonProperty("labels")  public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels")  public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards")  public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards")  public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #implements_rules */ @JsonProperty("implements_rules")  public ReferenceList getImplementsRules() { return this.implements_rules; }
    /** @see #implements_rules */ @JsonProperty("implements_rules")  public void setImplementsRules(ReferenceList implements_rules) { this.implements_rules = implements_rules; }

    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public ReferenceList getGovernedByRules() { return this.governed_by_rules; }
    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public void setGovernedByRules(ReferenceList governed_by_rules) { this.governed_by_rules = governed_by_rules; }

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

    public static final Boolean canBeCreated() { return false; }
    public static final Boolean includesModificationDetails() { return false; }
    public static final ArrayList<String> NON_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("name");
        add("short_description");
        add("long_description");
        add("sequence");
        add("business_name");
    }};
    public static final ArrayList<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("labels");
        add("stewards");
        add("assigned_to_terms");
        add("implements_rules");
        add("governed_by_rules");
        add("x_of_olap_level");
    }};
    public static final ArrayList<String> ALL_PROPERTIES = new ArrayList<String>() {{
        add("name");
        add("short_description");
        add("long_description");
        add("labels");
        add("stewards");
        add("assigned_to_terms");
        add("implements_rules");
        add("governed_by_rules");
        add("of_olap_hierarchy_member");
        add("has_parent_olap_level");
        add("has_child_olap_level");
        add("x_of_olap_level");
        add("sequence");
        add("business_name");
    }};
    public static final List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static final List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static final List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static final Boolean isOlaphierarchyassociation(Object obj) { return (obj.getClass() == Olaphierarchyassociation.class); }

}
