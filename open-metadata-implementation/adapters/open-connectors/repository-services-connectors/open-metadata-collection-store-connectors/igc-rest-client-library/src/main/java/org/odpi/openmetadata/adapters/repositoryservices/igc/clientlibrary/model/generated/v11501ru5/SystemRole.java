/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11501ru5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'system_role' asset type in IGC, displayed as 'System Role' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class SystemRole extends Reference {

    public static String getIgcTypeId() { return "system_role"; }
    public static String getIgcTypeDisplayName() { return "System Role"; }

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
     * The 'product' property, displayed as 'Product' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>ASB (displayed in the UI as 'ASB')</li>
     *     <li>DATASTAGE (displayed in the UI as 'DATASTAGE')</li>
     *     <li>SORCERER (displayed in the UI as 'SORCERER')</li>
     *     <li>SOA (displayed in the UI as 'SOA')</li>
     *     <li>ENTERPRISE_MANAGER (displayed in the UI as 'ENTERPRISE_MANAGER')</li>
     *     <li>OTHER (displayed in the UI as 'OTHER')</li>
     *     <li>GLOSSARY (displayed in the UI as 'GLOSSARY')</li>
     *     <li>OMD (displayed in the UI as 'OMD')</li>
     *     <li>WISD (displayed in the UI as 'WISD')</li>
     * </ul>
     */
    protected String product;

    /**
     * The 'of_principal' property, displayed as 'Of Principal' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Steward} objects.
     */
    protected ReferenceList of_principal;

    /**
     * The 'of_acl_entry' property, displayed as 'Of Acl Entry' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Aclentry} objects.
     */
    protected ReferenceList of_acl_entry;

    /**
     * The 'role_type' property, displayed as 'Role Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>SUITE (displayed in the UI as 'SUITE')</li>
     *     <li>PRODUCT (displayed in the UI as 'PRODUCT')</li>
     *     <li>PROJECT (displayed in the UI as 'PROJECT')</li>
     * </ul>
     */
    protected String role_type;

    /**
     * The 'role_id' property, displayed as 'Role Id' in the IGC UI.
     */
    protected String role_id;

    /**
     * The 'defines_role_assignment' property, displayed as 'Defines Role Assignment' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link RoleAssignment} objects.
     */
    protected ReferenceList defines_role_assignment;


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

    /** @see #product */ @JsonProperty("product")  public String getProduct() { return this.product; }
    /** @see #product */ @JsonProperty("product")  public void setProduct(String product) { this.product = product; }

    /** @see #of_principal */ @JsonProperty("of_principal")  public ReferenceList getOfPrincipal() { return this.of_principal; }
    /** @see #of_principal */ @JsonProperty("of_principal")  public void setOfPrincipal(ReferenceList of_principal) { this.of_principal = of_principal; }

    /** @see #of_acl_entry */ @JsonProperty("of_acl_entry")  public ReferenceList getOfAclEntry() { return this.of_acl_entry; }
    /** @see #of_acl_entry */ @JsonProperty("of_acl_entry")  public void setOfAclEntry(ReferenceList of_acl_entry) { this.of_acl_entry = of_acl_entry; }

    /** @see #role_type */ @JsonProperty("role_type")  public String getRoleType() { return this.role_type; }
    /** @see #role_type */ @JsonProperty("role_type")  public void setRoleType(String role_type) { this.role_type = role_type; }

    /** @see #role_id */ @JsonProperty("role_id")  public String getRoleId() { return this.role_id; }
    /** @see #role_id */ @JsonProperty("role_id")  public void setRoleId(String role_id) { this.role_id = role_id; }

    /** @see #defines_role_assignment */ @JsonProperty("defines_role_assignment")  public ReferenceList getDefinesRoleAssignment() { return this.defines_role_assignment; }
    /** @see #defines_role_assignment */ @JsonProperty("defines_role_assignment")  public void setDefinesRoleAssignment(ReferenceList defines_role_assignment) { this.defines_role_assignment = defines_role_assignment; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "product",
        "role_type",
        "role_id"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "of_principal",
        "of_acl_entry",
        "defines_role_assignment"
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
        "product",
        "of_principal",
        "of_acl_entry",
        "role_type",
        "role_id",
        "defines_role_assignment"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isSystemRole(Object obj) { return (obj.getClass() == SystemRole.class); }

}
