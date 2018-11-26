/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'role_assignment' asset type in IGC, displayed as 'Role Assignment' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class RoleAssignment extends MainObject {

    public static final String IGC_TYPE_ID = "role_assignment";

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


    /** @see #assigns_principal */ @JsonProperty("assigns_principal")  public ReferenceList getAssignsPrincipal() { return this.assigns_principal; }
    /** @see #assigns_principal */ @JsonProperty("assigns_principal")  public void setAssignsPrincipal(ReferenceList assigns_principal) { this.assigns_principal = assigns_principal; }

    /** @see #defined_by_system_role */ @JsonProperty("defined_by_system_role")  public Reference getDefinedBySystemRole() { return this.defined_by_system_role; }
    /** @see #defined_by_system_role */ @JsonProperty("defined_by_system_role")  public void setDefinedBySystemRole(Reference defined_by_system_role) { this.defined_by_system_role = defined_by_system_role; }

    /** @see #has_role_context */ @JsonProperty("has_role_context")  public Reference getHasRoleContext() { return this.has_role_context; }
    /** @see #has_role_context */ @JsonProperty("has_role_context")  public void setHasRoleContext(Reference has_role_context) { this.has_role_context = has_role_context; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }


    public static final Boolean isRoleAssignment(Object obj) { return (obj.getClass() == RoleAssignment.class); }

}
