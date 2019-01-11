/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * The supertype of many IGC objects -- in particular, any OpenIGC objects.
 * <br><br>
 * Simply define a new POJO as extending this base class to inherit the attributes that are found
 * on virtually all IGC asset types.
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = InformationAsset.class, name = "information_asset"),
})
public class MainObject extends Reference {

    public static String getIgcTypeId() { return "main_object"; }
    public static String getIgcTypeDisplayName() { return "Main Object"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     * <br><br>
     * Equivalent to the '_name' property of a Reference, but only populated when an asset has been retrieved
     * rather than just its reference.
     */
    protected String name = null;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description = null;

    /**
     * The 'long_description' property, displayed as 'Long Description' in the IGC UI.
     */
    protected String long_description = null;

    /**
     * The 'labels' property, displayed as 'Labels' in the IGC UI.
     * <br><br>
     * Will be a ReferenceList of {@see Label} objects.
     */
    protected ReferenceList labels = null;

    /**
     * The 'stewards' property, displayed as 'Stewards' in the IGC UI.
     * <br><br>
     * Will be a ReferenceList of {@see Steward} objects.
     */
    protected ReferenceList stewards = null;

    /**
     * The 'assigned_to_terms' property, displayed as 'Assigned to Terms' in the IGC UI.
     * <br><br>
     * Will be a ReferenceList of {@see Term} objects.
     */
    protected ReferenceList assigned_to_terms = null;

    /**
     * The 'implements_rules' property, displayed as 'Implements Rules' in the IGC UI.
     * <br><br>
     * Will be a ReferenceList of {@see InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules = null;

    /**
     * The 'governed_by_rules' property, displayed as 'Governed by Rules' in the IGC UI.
     * <br><br>
     * Will be a ReferenceList of {@see InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules = null;

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     * <br><br>
     * Provides the name of the user who created this asset.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created On' in the IGC UI.
     * <br><br>
     * Provides the date and time on which this asset was created.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     * <br><br>
     * Provides the name of the user who last modified this asset.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Modified On' in the IGC UI.
     * <br><br>
     * Provides the date and time on which this asset was last modified.
     */
    protected Date modified_on;

    // TODO: add notes object reference

    /** @see #name */ @JsonProperty("name") public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name") public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description") public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description") public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description") public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description") public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #labels */ @JsonProperty("labels") public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels") public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards") public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards") public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms") public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms") public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #implements_rules */ @JsonProperty("implements_rules") public ReferenceList getImplementsRules() { return this.implements_rules; }
    /** @see #implements_rules */ @JsonProperty("implements_rules") public void setImplementsRules(ReferenceList implements_rules) { this.implements_rules = implements_rules; }

    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules") public ReferenceList getGovernedByRules() { return this.governed_by_rules; }
    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules") public void setGovernedByRules(ReferenceList governed_by_rules) { this.governed_by_rules = governed_by_rules; }

    /** @see #created_by */ @JsonProperty("created_by") public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by") public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on") public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on") public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by") public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by") public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on") public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on") public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static final Boolean isMainObject(Object obj) { return (obj.getClass() == MainObject.class); }

}
