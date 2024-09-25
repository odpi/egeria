/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.controls;

import org.odpi.openmetadata.frameworks.openmetadata.enums.DataType;

/**
 * SecretsStorePurpose defines the purpose of a secrets connector.  These values are set into the "displayName"
 * property of the EmbeddedConnection entity (model 0205).
 */
public enum SecretsStorePurpose
{
    /**
     * The secrets connector can supply a bearer token to send in the authorization header of a REST API call.
     */
    REST_BEARER_TOKEN("restAPIBearerToken",
                      "The secrets connector can supply a bearer token to send in the authorization header of a REST API call."),

    /**
     * A password used to prove a user's identity - in clear text.
     */
    REST_BASIC_AUTHENTICATION("basic",
                              "A password used to prove a user's identity - in clear text."),

    ;

    public final String           name;
    public final String           description;

    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request parameter
     * @param description description of the request parameter
     */
    SecretsStorePurpose(String  name,
                        String  description)
    {
        this.name          = name;
        this.description   = description;
    }


    /**
     * Return the name of the request parameter.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the configuration property.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SecretsStoreCollectionProperty{ name=" + name + "}";
    }
}
