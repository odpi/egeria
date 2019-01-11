/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'role_assignment' asset type in IGC, displayed as 'Role Assignment' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class RoleAssignment extends Reference {

    public static String getIgcTypeId() { return "role_assignment"; }
    public static String getIgcTypeDisplayName() { return "Role Assignment"; }

    /**
     * The 'assigns_principal' property, displayed as 'Assigns Principal' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Steward} objects.
     */
    protected ReferenceList assigns_principal;

    /**
     * The 'defined_by_system_role' property, displayed as 'Defined By System Role' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link SystemRole} object.
     */
    protected Reference defined_by_system_role;

    /**
     * The 'has_role_context' property, displayed as 'Has Role Context' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link RoleContext} object.
     */
    protected Reference has_role_context;

    /**
     * The 'sequence' property, displayed as 'Sequence' in the IGC UI.
     */
    protected Number sequence;

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created On' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Modified On' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #assigns_principal */ @JsonProperty("assigns_principal")  public ReferenceList getAssignsPrincipal() { return this.assigns_principal; }
    /** @see #assigns_principal */ @JsonProperty("assigns_principal")  public void setAssignsPrincipal(ReferenceList assigns_principal) { this.assigns_principal = assigns_principal; }

    /** @see #defined_by_system_role */ @JsonProperty("defined_by_system_role")  public Reference getDefinedBySystemRole() { return this.defined_by_system_role; }
    /** @see #defined_by_system_role */ @JsonProperty("defined_by_system_role")  public void setDefinedBySystemRole(Reference defined_by_system_role) { this.defined_by_system_role = defined_by_system_role; }

    /** @see #has_role_context */ @JsonProperty("has_role_context")  public Reference getHasRoleContext() { return this.has_role_context; }
    /** @see #has_role_context */ @JsonProperty("has_role_context")  public void setHasRoleContext(Reference has_role_context) { this.has_role_context = has_role_context; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }

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
        "sequence",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "assigns_principal"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "assigns_principal",
        "defined_by_system_role",
        "has_role_context",
        "sequence",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isRoleAssignment(Object obj) { return (obj.getClass() == RoleAssignment.class); }

}
