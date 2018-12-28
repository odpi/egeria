/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'credentials' asset type in IGC, displayed as 'Credentials' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Credentials extends Reference {

    public static String getIgcTypeId() { return "credentials"; }
    public static String getIgcTypeDisplayName() { return "Credentials"; }

    /**
     * The 'asb_credential' property, displayed as 'Asb Credential' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Credential} object.
     */
    protected Reference asb_credential;

    /**
     * The 'of_user' property, displayed as 'Of User' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link User} object.
     */
    protected Reference of_user;

    /**
     * The 'external_credential' property, displayed as 'External Credential' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Credential} objects.
     */
    protected ReferenceList external_credential;

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


    /** @see #asb_credential */ @JsonProperty("asb_credential")  public Reference getAsbCredential() { return this.asb_credential; }
    /** @see #asb_credential */ @JsonProperty("asb_credential")  public void setAsbCredential(Reference asb_credential) { this.asb_credential = asb_credential; }

    /** @see #of_user */ @JsonProperty("of_user")  public Reference getOfUser() { return this.of_user; }
    /** @see #of_user */ @JsonProperty("of_user")  public void setOfUser(Reference of_user) { this.of_user = of_user; }

    /** @see #external_credential */ @JsonProperty("external_credential")  public ReferenceList getExternalCredential() { return this.external_credential; }
    /** @see #external_credential */ @JsonProperty("external_credential")  public void setExternalCredential(ReferenceList external_credential) { this.external_credential = external_credential; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static final Boolean canBeCreated() { return false; }
    public static final Boolean includesModificationDetails() { return true; }
    public static final ArrayList<String> NON_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("created_by");
        add("created_on");
        add("modified_by");
        add("modified_on");
    }};
    public static final ArrayList<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("external_credential");
    }};
    public static final ArrayList<String> ALL_PROPERTIES = new ArrayList<String>() {{
        add("asb_credential");
        add("of_user");
        add("external_credential");
        add("created_by");
        add("created_on");
        add("modified_by");
        add("modified_on");
    }};
    public static final List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static final List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static final List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static final Boolean isCredentials(Object obj) { return (obj.getClass() == Credentials.class); }

}
