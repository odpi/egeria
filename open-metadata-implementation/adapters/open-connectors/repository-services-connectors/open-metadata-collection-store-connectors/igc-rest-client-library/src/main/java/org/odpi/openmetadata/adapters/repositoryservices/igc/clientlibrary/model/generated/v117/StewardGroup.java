/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'steward_group' asset type in IGC, displayed as 'Steward Group' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class StewardGroup extends Reference {

    public static String getIgcTypeId() { return "steward_group"; }

    /**
     * The 'group_name' property, displayed as 'Group Name' in the IGC UI.
     */
    protected String group_name;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'email_address' property, displayed as 'Email Address' in the IGC UI.
     */
    protected String email_address;

    /**
     * The 'web_address' property, displayed as 'Web Address' in the IGC UI.
     */
    protected String web_address;

    /**
     * The 'organization' property, displayed as 'Organization' in the IGC UI.
     */
    protected String organization;

    /**
     * The 'location' property, displayed as 'Location' in the IGC UI.
     */
    protected String location;

    /**
     * The 'principal_id' property, displayed as 'Principal ID' in the IGC UI.
     */
    protected String principal_id;

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


    /** @see #group_name */ @JsonProperty("group_name")  public String getGroupName() { return this.group_name; }
    /** @see #group_name */ @JsonProperty("group_name")  public void setGroupName(String group_name) { this.group_name = group_name; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #email_address */ @JsonProperty("email_address")  public String getEmailAddress() { return this.email_address; }
    /** @see #email_address */ @JsonProperty("email_address")  public void setEmailAddress(String email_address) { this.email_address = email_address; }

    /** @see #web_address */ @JsonProperty("web_address")  public String getWebAddress() { return this.web_address; }
    /** @see #web_address */ @JsonProperty("web_address")  public void setWebAddress(String web_address) { this.web_address = web_address; }

    /** @see #organization */ @JsonProperty("organization")  public String getOrganization() { return this.organization; }
    /** @see #organization */ @JsonProperty("organization")  public void setOrganization(String organization) { this.organization = organization; }

    /** @see #location */ @JsonProperty("location")  public String getLocation() { return this.location; }
    /** @see #location */ @JsonProperty("location")  public void setLocation(String location) { this.location = location; }

    /** @see #principal_id */ @JsonProperty("principal_id")  public String getPrincipalId() { return this.principal_id; }
    /** @see #principal_id */ @JsonProperty("principal_id")  public void setPrincipalId(String principal_id) { this.principal_id = principal_id; }

    /** @see #managed_assets */ @JsonProperty("managed_assets")  public ReferenceList getManagedAssets() { return this.managed_assets; }
    /** @see #managed_assets */ @JsonProperty("managed_assets")  public void setManagedAssets(ReferenceList managed_assets) { this.managed_assets = managed_assets; }

    /** @see #managed_assets_basic */ @JsonProperty("managed_assets_basic")  public ReferenceList getManagedAssetsBasic() { return this.managed_assets_basic; }
    /** @see #managed_assets_basic */ @JsonProperty("managed_assets_basic")  public void setManagedAssetsBasic(ReferenceList managed_assets_basic) { this.managed_assets_basic = managed_assets_basic; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }


    public static final Boolean isStewardGroup(Object obj) { return (obj.getClass() == StewardGroup.class); }

}
