/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'credentials' asset type in IGC, displayed as 'Credentials' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Credentials extends MainObject {

    public static final String IGC_TYPE_ID = "credentials";

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


    /** @see #asb_credential */ @JsonProperty("asb_credential")  public Reference getAsbCredential() { return this.asb_credential; }
    /** @see #asb_credential */ @JsonProperty("asb_credential")  public void setAsbCredential(Reference asb_credential) { this.asb_credential = asb_credential; }

    /** @see #of_user */ @JsonProperty("of_user")  public Reference getOfUser() { return this.of_user; }
    /** @see #of_user */ @JsonProperty("of_user")  public void setOfUser(Reference of_user) { this.of_user = of_user; }

    /** @see #external_credential */ @JsonProperty("external_credential")  public ReferenceList getExternalCredential() { return this.external_credential; }
    /** @see #external_credential */ @JsonProperty("external_credential")  public void setExternalCredential(ReferenceList external_credential) { this.external_credential = external_credential; }


    public static final Boolean isCredentials(Object obj) { return (obj.getClass() == Credentials.class); }

}
