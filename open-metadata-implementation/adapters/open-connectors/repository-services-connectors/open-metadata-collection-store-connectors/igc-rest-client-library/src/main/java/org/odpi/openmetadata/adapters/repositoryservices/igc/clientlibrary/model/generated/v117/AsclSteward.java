/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'ascl_steward' asset type in IGC, displayed as 'Steward' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class AsclSteward extends MainObject {

    public static final String IGC_TYPE_ID = "ascl_steward";

    /**
     * The 'email_address' property, displayed as 'Email Address' in the IGC UI.
     */
    protected String email_address;

    /**
     * The 'organization' property, displayed as 'Organization' in the IGC UI.
     */
    protected String organization;

    /**
     * The 'managed_assets' property, displayed as 'Managed Assets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList managed_assets;

    /**
     * The 'managed_assets_basic' property, displayed as 'Managed Assets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList managed_assets_basic;

    /**
     * The 'steward_user' property, displayed as 'Steward User' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link StewardUser} object.
     */
    protected Reference steward_user;

    /**
     * The 'steward_group' property, displayed as 'Steward Group' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link StewardGroup} object.
     */
    protected Reference steward_group;


    /** @see #email_address */ @JsonProperty("email_address")  public String getEmailAddress() { return this.email_address; }
    /** @see #email_address */ @JsonProperty("email_address")  public void setEmailAddress(String email_address) { this.email_address = email_address; }

    /** @see #organization */ @JsonProperty("organization")  public String getOrganization() { return this.organization; }
    /** @see #organization */ @JsonProperty("organization")  public void setOrganization(String organization) { this.organization = organization; }

    /** @see #managed_assets */ @JsonProperty("managed_assets")  public ReferenceList getManagedAssets() { return this.managed_assets; }
    /** @see #managed_assets */ @JsonProperty("managed_assets")  public void setManagedAssets(ReferenceList managed_assets) { this.managed_assets = managed_assets; }

    /** @see #managed_assets_basic */ @JsonProperty("managed_assets_basic")  public ReferenceList getManagedAssetsBasic() { return this.managed_assets_basic; }
    /** @see #managed_assets_basic */ @JsonProperty("managed_assets_basic")  public void setManagedAssetsBasic(ReferenceList managed_assets_basic) { this.managed_assets_basic = managed_assets_basic; }

    /** @see #steward_user */ @JsonProperty("steward_user")  public Reference getStewardUser() { return this.steward_user; }
    /** @see #steward_user */ @JsonProperty("steward_user")  public void setStewardUser(Reference steward_user) { this.steward_user = steward_user; }

    /** @see #steward_group */ @JsonProperty("steward_group")  public Reference getStewardGroup() { return this.steward_group; }
    /** @see #steward_group */ @JsonProperty("steward_group")  public void setStewardGroup(Reference steward_group) { this.steward_group = steward_group; }


    public static final Boolean isAsclSteward(Object obj) { return (obj.getClass() == AsclSteward.class); }

}
