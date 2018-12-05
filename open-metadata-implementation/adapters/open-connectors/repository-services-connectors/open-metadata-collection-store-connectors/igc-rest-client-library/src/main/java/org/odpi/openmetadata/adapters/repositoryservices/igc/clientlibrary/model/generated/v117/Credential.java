/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'credential' asset type in IGC, displayed as 'Credential' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Credential extends MainObject {

    public static final String IGC_TYPE_ID = "credential";

    /**
     * The 'password' property, displayed as 'Password' in the IGC UI.
     */
    protected String password;

    /**
     * The 'username' property, displayed as 'Username' in the IGC UI.
     */
    protected String username;

    /**
     * The 'for_application_install' property, displayed as 'For Application Install' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link ApplicationInstall} object.
     */
    protected Reference for_application_install;

    /**
     * The 'used_by_user_credentials' property, displayed as 'Used By User Credentials' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Credentials} object.
     */
    protected Reference used_by_user_credentials;

    /**
     * The 'of_user_credentials' property, displayed as 'Of User Credentials' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Credentials} object.
     */
    protected Reference of_user_credentials;

    /**
     * The 'default_for_application_install' property, displayed as 'Default For Application Install' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link ApplicationInstall} object.
     */
    protected Reference default_for_application_install;


    /** @see #password */ @JsonProperty("password")  public String getPassword() { return this.password; }
    /** @see #password */ @JsonProperty("password")  public void setPassword(String password) { this.password = password; }

    /** @see #username */ @JsonProperty("username")  public String getUsername() { return this.username; }
    /** @see #username */ @JsonProperty("username")  public void setUsername(String username) { this.username = username; }

    /** @see #for_application_install */ @JsonProperty("for_application_install")  public Reference getForApplicationInstall() { return this.for_application_install; }
    /** @see #for_application_install */ @JsonProperty("for_application_install")  public void setForApplicationInstall(Reference for_application_install) { this.for_application_install = for_application_install; }

    /** @see #used_by_user_credentials */ @JsonProperty("used_by_user_credentials")  public Reference getUsedByUserCredentials() { return this.used_by_user_credentials; }
    /** @see #used_by_user_credentials */ @JsonProperty("used_by_user_credentials")  public void setUsedByUserCredentials(Reference used_by_user_credentials) { this.used_by_user_credentials = used_by_user_credentials; }

    /** @see #of_user_credentials */ @JsonProperty("of_user_credentials")  public Reference getOfUserCredentials() { return this.of_user_credentials; }
    /** @see #of_user_credentials */ @JsonProperty("of_user_credentials")  public void setOfUserCredentials(Reference of_user_credentials) { this.of_user_credentials = of_user_credentials; }

    /** @see #default_for_application_install */ @JsonProperty("default_for_application_install")  public Reference getDefaultForApplicationInstall() { return this.default_for_application_install; }
    /** @see #default_for_application_install */ @JsonProperty("default_for_application_install")  public void setDefaultForApplicationInstall(Reference default_for_application_install) { this.default_for_application_install = default_for_application_install; }


    public static final Boolean isCredential(Object obj) { return (obj.getClass() == Credential.class); }

}
