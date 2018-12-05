/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'user_role' asset type in IGC, displayed as 'User Role' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserRole extends MainObject {

    public static final String IGC_TYPE_ID = "user_role";

    /**
     * The 'system_role' property, displayed as 'System Role' in the IGC UI.
     */
    protected Boolean system_role;

    /**
     * The 'users' property, displayed as 'Users' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Steward} objects.
     */
    protected ReferenceList users;


    /** @see #system_role */ @JsonProperty("system_role")  public Boolean getSystemRole() { return this.system_role; }
    /** @see #system_role */ @JsonProperty("system_role")  public void setSystemRole(Boolean system_role) { this.system_role = system_role; }

    /** @see #users */ @JsonProperty("users")  public ReferenceList getUsers() { return this.users; }
    /** @see #users */ @JsonProperty("users")  public void setUsers(ReferenceList users) { this.users = users; }


    public static final Boolean isUserRole(Object obj) { return (obj.getClass() == UserRole.class); }

}
