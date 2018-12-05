/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'system_role' asset type in IGC, displayed as 'System Role' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class SystemRole extends MainObject {

    public static final String IGC_TYPE_ID = "system_role";

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


    public static final Boolean isSystemRole(Object obj) { return (obj.getClass() == SystemRole.class); }

}
