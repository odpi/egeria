/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701sp1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code olaphierarchyassociation} asset type in IGC, displayed as '{@literal OLAPHierarchyAssociation}' in the IGC UI.
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
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code long_description} property, displayed as '{@literal Long Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code labels} property, displayed as '{@literal Labels}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The {@code stewards} property, displayed as '{@literal Stewards}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The {@code assigned_to_terms} property, displayed as '{@literal Assigned to Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The {@code implements_rules} property, displayed as '{@literal Implements Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The {@code governed_by_rules} property, displayed as '{@literal Governed by Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The {@code of_olap_hierarchy_member} property, displayed as '{@literal Of OLAP Hierarchy Member}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiHierarchyMember} object.
     */
    protected Reference of_olap_hierarchy_member;

    /**
     * The {@code has_parent_olap_level} property, displayed as '{@literal Has Parent OLAP Level}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiLevel} object.
     */
    protected Reference has_parent_olap_level;

    /**
     * The {@code has_child_olap_level} property, displayed as '{@literal Has Child OLAP Level}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiLevel} object.
     */
    protected Reference has_child_olap_level;

    /**
     * The {@code x_of_olap_level} property, displayed as '{@literal X Of OLAP Level}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiLevel} objects.
     */
    protected ReferenceList x_of_olap_level;

    /**
     * The {@code sequence} property, displayed as '{@literal Sequence}' in the IGC UI.
     */
    protected Number sequence;

    /**
     * The {@code business_name} property, displayed as '{@literal Business Name}' in the IGC UI.
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

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "sequence",
        "business_name"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "business_name"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "x_of_olap_level"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "of_olap_hierarchy_member",
        "has_parent_olap_level",
        "has_child_olap_level",
        "x_of_olap_level",
        "sequence",
        "business_name"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isOlaphierarchyassociation(Object obj) { return (obj.getClass() == Olaphierarchyassociation.class); }

}
