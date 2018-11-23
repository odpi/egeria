/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'data_policy' asset type in IGC, displayed as 'Data Policy' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataPolicy extends MainObject {

    public static final String IGC_TYPE_ID = "data_policy";

    /**
     * The 'policy_number' property, displayed as 'Policy Number' in the IGC UI.
     */
    protected String policy_number;

    /**
     * The 'effective_date' property, displayed as 'Effective Date' in the IGC UI.
     */
    protected Date effective_date;

    /**
     * The 'termination_date' property, displayed as 'Termination Date' in the IGC UI.
     */
    protected Date termination_date;

    /**
     * The 'contacts' property, displayed as 'Contacts' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Steward} objects.
     */
    protected ReferenceList contacts;

    /**
     * The 'applied_to_assets' property, displayed as 'Applied to Assets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList applied_to_assets;


    /** @see #policy_number */ @JsonProperty("policy_number")  public String getPolicyNumber() { return this.policy_number; }
    /** @see #policy_number */ @JsonProperty("policy_number")  public void setPolicyNumber(String policy_number) { this.policy_number = policy_number; }

    /** @see #effective_date */ @JsonProperty("effective_date")  public Date getEffectiveDate() { return this.effective_date; }
    /** @see #effective_date */ @JsonProperty("effective_date")  public void setEffectiveDate(Date effective_date) { this.effective_date = effective_date; }

    /** @see #termination_date */ @JsonProperty("termination_date")  public Date getTerminationDate() { return this.termination_date; }
    /** @see #termination_date */ @JsonProperty("termination_date")  public void setTerminationDate(Date termination_date) { this.termination_date = termination_date; }

    /** @see #contacts */ @JsonProperty("contacts")  public ReferenceList getContacts() { return this.contacts; }
    /** @see #contacts */ @JsonProperty("contacts")  public void setContacts(ReferenceList contacts) { this.contacts = contacts; }

    /** @see #applied_to_assets */ @JsonProperty("applied_to_assets")  public ReferenceList getAppliedToAssets() { return this.applied_to_assets; }
    /** @see #applied_to_assets */ @JsonProperty("applied_to_assets")  public void setAppliedToAssets(ReferenceList applied_to_assets) { this.applied_to_assets = applied_to_assets; }


    public static final Boolean isDataPolicy(Object obj) { return (obj.getClass() == DataPolicy.class); }

}
