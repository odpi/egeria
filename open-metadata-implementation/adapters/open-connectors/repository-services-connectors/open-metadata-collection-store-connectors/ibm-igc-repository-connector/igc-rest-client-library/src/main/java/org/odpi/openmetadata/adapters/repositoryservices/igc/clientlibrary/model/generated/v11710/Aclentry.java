/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11710;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code aclentry} asset type in IGC, displayed as '{@literal AclEntry}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("aclentry")
public class Aclentry extends Reference {

    public static String getIgcTypeDisplayName() { return "AclEntry"; }

    /**
     * The {@code permissions} property, displayed as '{@literal Permissions}' in the IGC UI.
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
     * The {@code used_by_acl} property, displayed as '{@literal Used By Acl}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Acl} object.
     */
    protected Reference used_by_acl;

    /**
     * The {@code has_system_role} property, displayed as '{@literal Has System Role}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link SystemRole} object.
     */
    protected Reference has_system_role;

    /**
     * The {@code has_principal} property, displayed as '{@literal Has Principal}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Steward} object.
     */
    protected Reference has_principal;

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


    /** @see #permissions */ @JsonProperty("permissions")  public ArrayList<String> getPermissions() { return this.permissions; }
    /** @see #permissions */ @JsonProperty("permissions")  public void setPermissions(ArrayList<String> permissions) { this.permissions = permissions; }

    /** @see #used_by_acl */ @JsonProperty("used_by_acl")  public Reference getUsedByAcl() { return this.used_by_acl; }
    /** @see #used_by_acl */ @JsonProperty("used_by_acl")  public void setUsedByAcl(Reference used_by_acl) { this.used_by_acl = used_by_acl; }

    /** @see #has_system_role */ @JsonProperty("has_system_role")  public Reference getHasSystemRole() { return this.has_system_role; }
    /** @see #has_system_role */ @JsonProperty("has_system_role")  public void setHasSystemRole(Reference has_system_role) { this.has_system_role = has_system_role; }

    /** @see #has_principal */ @JsonProperty("has_principal")  public Reference getHasPrincipal() { return this.has_principal; }
    /** @see #has_principal */ @JsonProperty("has_principal")  public void setHasPrincipal(Reference has_principal) { this.has_principal = has_principal; }

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
        "permissions",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "permissions",
        "used_by_acl",
        "has_system_role",
        "has_principal",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isAclentry(Object obj) { return (obj.getClass() == Aclentry.class); }

}
