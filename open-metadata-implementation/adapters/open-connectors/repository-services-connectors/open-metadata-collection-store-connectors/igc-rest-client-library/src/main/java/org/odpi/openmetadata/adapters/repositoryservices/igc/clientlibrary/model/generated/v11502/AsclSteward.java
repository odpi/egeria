/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'ascl_steward' asset type in IGC, displayed as 'Steward' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class AsclSteward extends Reference {

    public static String getIgcTypeId() { return "ascl_steward"; }
    public static String getIgcTypeDisplayName() { return "Steward"; }

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

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    /** @see #steward_user */ @JsonProperty("steward_user")  public Reference getStewardUser() { return this.steward_user; }
    /** @see #steward_user */ @JsonProperty("steward_user")  public void setStewardUser(Reference steward_user) { this.steward_user = steward_user; }

    /** @see #steward_group */ @JsonProperty("steward_group")  public Reference getStewardGroup() { return this.steward_group; }
    /** @see #steward_group */ @JsonProperty("steward_group")  public void setStewardGroup(Reference steward_group) { this.steward_group = steward_group; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "email_address",
        "organization",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "managed_assets",
        "managed_assets_basic"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "email_address",
        "organization",
        "managed_assets",
        "managed_assets_basic",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on",
        "steward_user",
        "steward_group"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isAsclSteward(Object obj) { return (obj.getClass() == AsclSteward.class); }

}
