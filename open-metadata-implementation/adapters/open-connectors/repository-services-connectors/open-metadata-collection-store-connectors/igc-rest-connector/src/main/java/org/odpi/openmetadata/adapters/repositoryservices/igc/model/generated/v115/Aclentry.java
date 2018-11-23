/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'aclentry' asset type in IGC, displayed as 'AclEntry' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Aclentry extends MainObject {

    public static final String IGC_TYPE_ID = "aclentry";

    /**
     * The 'permissions' property, displayed as 'Permissions' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>READ (displayed in the UI as 'READ')</li>
     *     <li>UPDATE (displayed in the UI as 'UPDATE')</li>
     *     <li>DELETE (displayed in the UI as 'DELETE')</li>
     *     <li>VIEW (displayed in the UI as 'VIEW')</li>
     *     <li>EXECUTE (displayed in the UI as 'EXECUTE')</li>
     *     <li>OWNER (displayed in the UI as 'OWNER')</li>
     * </ul>
     */
    protected ArrayList<String> permissions;

    /**
     * The 'used_by_acl' property, displayed as 'Used By Acl' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Acl} object.
     */
    protected Reference used_by_acl;

    /**
     * The 'has_system_role' property, displayed as 'Has System Role' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link SystemRole} object.
     */
    protected Reference has_system_role;

    /**
     * The 'has_principal' property, displayed as 'Has Principal' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Steward} object.
     */
    protected Reference has_principal;


    /** @see #permissions */ @JsonProperty("permissions")  public ArrayList<String> getPermissions() { return this.permissions; }
    /** @see #permissions */ @JsonProperty("permissions")  public void setPermissions(ArrayList<String> permissions) { this.permissions = permissions; }

    /** @see #used_by_acl */ @JsonProperty("used_by_acl")  public Reference getUsedByAcl() { return this.used_by_acl; }
    /** @see #used_by_acl */ @JsonProperty("used_by_acl")  public void setUsedByAcl(Reference used_by_acl) { this.used_by_acl = used_by_acl; }

    /** @see #has_system_role */ @JsonProperty("has_system_role")  public Reference getHasSystemRole() { return this.has_system_role; }
    /** @see #has_system_role */ @JsonProperty("has_system_role")  public void setHasSystemRole(Reference has_system_role) { this.has_system_role = has_system_role; }

    /** @see #has_principal */ @JsonProperty("has_principal")  public Reference getHasPrincipal() { return this.has_principal; }
    /** @see #has_principal */ @JsonProperty("has_principal")  public void setHasPrincipal(Reference has_principal) { this.has_principal = has_principal; }


    public static final Boolean isAclentry(Object obj) { return (obj.getClass() == Aclentry.class); }

}
