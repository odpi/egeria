/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'role_context' asset type in IGC, displayed as 'Role Context' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class RoleContext extends MainObject {

    public static final String IGC_TYPE_ID = "role_context";

    /**
     * The 'context_id' property, displayed as 'Context Id' in the IGC UI.
     */
    protected String context_id;

    /**
     * The 'defines_role_assignment' property, displayed as 'Defines Role Assignment' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link RoleAssignment} objects.
     */
    protected ReferenceList defines_role_assignment;


    /** @see #context_id */ @JsonProperty("context_id")  public String getContextId() { return this.context_id; }
    /** @see #context_id */ @JsonProperty("context_id")  public void setContextId(String context_id) { this.context_id = context_id; }

    /** @see #defines_role_assignment */ @JsonProperty("defines_role_assignment")  public ReferenceList getDefinesRoleAssignment() { return this.defines_role_assignment; }
    /** @see #defines_role_assignment */ @JsonProperty("defines_role_assignment")  public void setDefinesRoleAssignment(ReferenceList defines_role_assignment) { this.defines_role_assignment = defines_role_assignment; }


    public static final Boolean isRoleContext(Object obj) { return (obj.getClass() == RoleContext.class); }

}
