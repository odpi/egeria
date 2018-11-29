/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'steward' asset type in IGC, displayed as 'Steward' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Steward extends MainObject {

    public static final String IGC_TYPE_ID = "steward";

    /**
     * The 'assigned_by_contact_assignment' property, displayed as 'Assigned By Contact Assignment' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Associativeobject} objects.
     */
    protected ReferenceList assigned_by_contact_assignment;


    /** @see #assigned_by_contact_assignment */ @JsonProperty("assigned_by_contact_assignment")  public ReferenceList getAssignedByContactAssignment() { return this.assigned_by_contact_assignment; }
    /** @see #assigned_by_contact_assignment */ @JsonProperty("assigned_by_contact_assignment")  public void setAssignedByContactAssignment(ReferenceList assigned_by_contact_assignment) { this.assigned_by_contact_assignment = assigned_by_contact_assignment; }


    public static final Boolean isSteward(Object obj) { return (obj.getClass() == Steward.class); }

}
