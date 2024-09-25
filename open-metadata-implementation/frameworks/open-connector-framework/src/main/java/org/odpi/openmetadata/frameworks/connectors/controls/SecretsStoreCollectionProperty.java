/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.controls;

import org.odpi.openmetadata.frameworks.openmetadata.enums.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * SecretsStoreConfigurationProperty provides definitions for typical properties used with the secrets store collections.
 */
public enum SecretsStoreCollectionProperty
{
    /**
     * The name used to identify the collection of properties that a particular connector is using.
     */
    USER_ID("userId",
            "The name used to identify the connector to the remote system.",
            DataType.STRING.getName(),
            "connectornpa",
            true),

    /**
     * A password used to prove a user's identity - in clear text.
     */
    CLEAR_PASSWORD("clearPassword",
                   "A password used to prove a user's identity - in clear text.",
                   DataType.STRING.getName(),
                   "secret",
                   true),


    /**
     * A password used to prove a user's identity - encrypted.
     */
    ENCRYPTED_PASSWORD("encryptedPassword",
                       "A password used to prove a user's identity - encrypted.",
                       DataType.STRING.getName(),
                       null,
                       true),

    /**
     * An encrypted token to provide access to a remote digital resource.
     */
    TOKEN("accessToken",
          "An encrypted token to provide access to a remote digital resource.",
          DataType.STRING.getName(),
          null,
          true),


    /**
     * A URL to retrieve an access token.
     */
    TOKEN_URL("accessTokenURL",
          "A URL to retrieve an access token.",
          DataType.STRING.getName(),
          null,
          true),

    ;

    public final String           name;
    public final String           description;
    public final String           dataType;
    public final String           example;
    public final boolean          isPlaceholder;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the request parameter
     * @param description description of the request parameter
     * @param dataType type of value of the request parameter
     * @param example example of the request parameter
     * @param isPlaceholder is this also used as a placeholder property?
     */
    SecretsStoreCollectionProperty(String  name,
                                   String  description,
                                   String  dataType,
                                   String  example,
                                   boolean isPlaceholder)
    {
        this.name          = name;
        this.description   = description;
        this.dataType      = dataType;
        this.example       = example;
        this.isPlaceholder = isPlaceholder;
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
     * Return the data type for the configuration property.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the configuration property to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Return whether this value is also used as a placeholder property.
     *
     * @return boolean
     */
    public boolean isPlaceholder()
    {
        return isPlaceholder;
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
