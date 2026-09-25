/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.wso2mi.controls;

/**
 * WSO2MIPlaceholderProperty defines the placeholder properties used when the WSO2 Micro Integrator
 * connectors are configured from a template.
 *
 * <p>The Micro Integrator Management API authenticates with a user name and password that are exchanged
 * for a short-lived bearer token (see {@code WSO2MIResourceConnector}); those credentials are supplied
 * through {@link #MI_USER_ID} / {@link #MI_PASSWORD} and stored via the connection's secrets store.</p>
 */
public enum WSO2MIPlaceholderProperty
{
    /**
     * The host name or IP address of the machine running the WSO2 Micro Integrator.
     */
    HOST_IDENTIFIER("hostIdentifier",
                    "The host name or IP address of the machine running the WSO2 Micro Integrator.",
                    "string",
                    "localhost"),

    /**
     * The port that the WSO2 Micro Integrator Management API is listening on.
     */
    MANAGEMENT_PORT("managementPort",
                    "The port that the WSO2 Micro Integrator Management API is listening on.",
                    "string",
                    "9164"),

    /**
     * The name to give the WSO2 Micro Integrator instance in the open metadata ecosystem.
     */
    SERVER_NAME("serverName",
                "The name to give the WSO2 Micro Integrator instance in the open metadata ecosystem.",
                "string",
                "myMicroIntegrator"),

    /**
     * The Management API user name.
     */
    MI_USER_ID("miUserId",
               "The user name used to authenticate with the WSO2 Micro Integrator Management API.",
               "string",
               "admin"),

    /**
     * The Management API password.
     */
    MI_PASSWORD("miPassword",
                "The password used to authenticate with the WSO2 Micro Integrator Management API.",
                "string",
                "admin"),

    ;

    private final String name;
    private final String description;
    private final String dataType;
    private final String example;


    /**
     * Constructor.
     *
     * @param name        the name of the placeholder property
     * @param description  a short description of what the property is for
     * @param dataType     the open metadata data type of the property
     * @param example      an example value
     */
    WSO2MIPlaceholderProperty(String name, String description, String dataType, String example)
    {
        this.name        = name;
        this.description  = description;
        this.dataType     = dataType;
        this.example      = example;
    }


    /**
     * Return the name of the placeholder property.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the placeholder property.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the open metadata data type of the placeholder property.
     *
     * @return string data type
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example value for the placeholder property.
     *
     * @return string example
     */
    public String getExample()
    {
        return example;
    }
}
