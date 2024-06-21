/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Credentials for accessing storage on AWS.  Maps to AwsCredentials.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AWSCredentials
{
    private String access_key_id     = null;
    private String secret_access_key = null;
    private String session_token     = null;


    /**
     * Constructor
     */
    public AWSCredentials()
    {
    }

    /**
     * Return the access key ID that identifies the temporary credentials.
     *
     * @return string
     */
    public String getAccess_key_id()
    {
        return access_key_id;
    }


    /**
     * Set up the access key ID that identifies the temporary credentials.
     *
     * @param access_key_id string
     */
    public void setAccess_key_id(String access_key_id)
    {
        this.access_key_id = access_key_id;
    }


    /**
     * Return the secret access key that can be used to sign AWS API requests.
     *
     * @return string
     */
    public String getSecret_access_key()
    {
        return secret_access_key;
    }


    /**
     * Set up the secret access key that can be used to sign AWS API requests.
     *
     * @param secret_access_key string
     */
    public void setSecret_access_key(String secret_access_key)
    {
        this.secret_access_key = secret_access_key;
    }


    /**
     * Return the token that users must pass to AWS API to use the temporary credentials.
     *
     * @return  string p
     */
    public String getSession_token()
    {
        return session_token;
    }


    /**
     * Set up the token that users must pass to AWS API to use the temporary credentials.
     *
     * @param session_token  string
     */
    public void setSession_token(String session_token)
    {
        this.session_token = session_token;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AWSCredentials{" +
                "access_key_id='" + access_key_id + '\'' +
                ", secret_access_key='" + secret_access_key + '\'' +
                ", session_token='" + session_token + '\'' +
                '}';
    }

    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        AWSCredentials that = (AWSCredentials) objectToCompare;
        return Objects.equals(access_key_id, that.access_key_id) && Objects.equals(secret_access_key, that.secret_access_key) && Objects.equals(session_token, that.session_token);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(access_key_id, secret_access_key, session_token);
    }
}
