/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502sp5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code xsd_element_group} asset type in IGC, displayed as '{@literal XSD Element Group}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class XsdElementGroup extends Reference {

    public static String getIgcTypeId() { return "xsd_element_group"; }
    public static String getIgcTypeDisplayName() { return "XSD Element Group"; }

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
     * The {@code xsd_schema} property, displayed as '{@literal XSD Schema}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link XmlSchemaDefinition} object.
     */
    protected Reference xsd_schema;

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
     * The {@code contains_xsd_elements} property, displayed as '{@literal Contains XSD Elements}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList contains_xsd_elements;

    /**
     * The {@code references_xsd_elements} property, displayed as '{@literal References XSD Elements}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdElementReference} objects.
     */
    protected ReferenceList references_xsd_elements;

    /**
     * The {@code references_xsd_element_groups} property, displayed as '{@literal References XSD Element Groups}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdElementGroupReference} objects.
     */
    protected ReferenceList references_xsd_element_groups;

    /**
     * The {@code references_by_xsd_elements} property, displayed as '{@literal Referenced by XSD Elements}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList references_by_xsd_elements;

    /**
     * The {@code referenced_by_xsd_element_groups} property, displayed as '{@literal Referenced by XSD Element Groups}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XsdElementGroup} objects.
     */
    protected ReferenceList referenced_by_xsd_element_groups;

    /**
     * The {@code type} property, displayed as '{@literal Type}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>All (displayed in the UI as 'All')</li>
     *     <li>Choice (displayed in the UI as 'Choice')</li>
     *     <li>Sequence (displayed in the UI as 'Sequence')</li>
     * </ul>
     */
    protected String type;

    /**
     * The {@code in_collections} property, displayed as '{@literal In Collections}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

    /**
     * The {@code created_by} property, displayed as '{@literal Created By}' in the IGC UI.
     */
    protected String created_by;

    /**
     * The {@code created_on} property, displayed as '{@literal Created On}' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The {@code modified_by} property, displayed as '{@literal Modified By}' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The {@code modified_on} property, displayed as '{@literal Modified On}' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #xsd_schema */ @JsonProperty("xsd_schema")  public Reference getXsdSchema() { return this.xsd_schema; }
    /** @see #xsd_schema */ @JsonProperty("xsd_schema")  public void setXsdSchema(Reference xsd_schema) { this.xsd_schema = xsd_schema; }

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

    /** @see #contains_xsd_elements */ @JsonProperty("contains_xsd_elements")  public ReferenceList getContainsXsdElements() { return this.contains_xsd_elements; }
    /** @see #contains_xsd_elements */ @JsonProperty("contains_xsd_elements")  public void setContainsXsdElements(ReferenceList contains_xsd_elements) { this.contains_xsd_elements = contains_xsd_elements; }

    /** @see #references_xsd_elements */ @JsonProperty("references_xsd_elements")  public ReferenceList getReferencesXsdElements() { return this.references_xsd_elements; }
    /** @see #references_xsd_elements */ @JsonProperty("references_xsd_elements")  public void setReferencesXsdElements(ReferenceList references_xsd_elements) { this.references_xsd_elements = references_xsd_elements; }

    /** @see #references_xsd_element_groups */ @JsonProperty("references_xsd_element_groups")  public ReferenceList getReferencesXsdElementGroups() { return this.references_xsd_element_groups; }
    /** @see #references_xsd_element_groups */ @JsonProperty("references_xsd_element_groups")  public void setReferencesXsdElementGroups(ReferenceList references_xsd_element_groups) { this.references_xsd_element_groups = references_xsd_element_groups; }

    /** @see #references_by_xsd_elements */ @JsonProperty("references_by_xsd_elements")  public ReferenceList getReferencesByXsdElements() { return this.references_by_xsd_elements; }
    /** @see #references_by_xsd_elements */ @JsonProperty("references_by_xsd_elements")  public void setReferencesByXsdElements(ReferenceList references_by_xsd_elements) { this.references_by_xsd_elements = references_by_xsd_elements; }

    /** @see #referenced_by_xsd_element_groups */ @JsonProperty("referenced_by_xsd_element_groups")  public ReferenceList getReferencedByXsdElementGroups() { return this.referenced_by_xsd_element_groups; }
    /** @see #referenced_by_xsd_element_groups */ @JsonProperty("referenced_by_xsd_element_groups")  public void setReferencedByXsdElementGroups(ReferenceList referenced_by_xsd_element_groups) { this.referenced_by_xsd_element_groups = referenced_by_xsd_element_groups; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "type",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "contains_xsd_elements",
        "references_xsd_elements",
        "references_xsd_element_groups",
        "references_by_xsd_elements",
        "referenced_by_xsd_element_groups",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "xsd_schema",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "contains_xsd_elements",
        "references_xsd_elements",
        "references_xsd_element_groups",
        "references_by_xsd_elements",
        "referenced_by_xsd_element_groups",
        "type",
        "in_collections",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isXsdElementGroup(Object obj) { return (obj.getClass() == XsdElementGroup.class); }

}
