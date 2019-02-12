/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502sp5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code credentials} asset type in IGC, displayed as '{@literal Credentials}' in the IGC UI.
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
     * The {@code asb_credential} property, displayed as '{@literal Asb Credential}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Credential} object.
     */
    protected Reference asb_credential;

    /**
     * The {@code of_user} property, displayed as '{@literal Of User}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link User} object.
     */
    protected Reference of_user;

    /**
     * The {@code external_credential} property, displayed as '{@literal External Credential}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Credential} objects.
     */
    protected ReferenceList external_credential;

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

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "external_credential"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "asb_credential",
        "of_user",
        "external_credential",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isCredentials(Object obj) { return (obj.getClass() == Credentials.class); }

}
