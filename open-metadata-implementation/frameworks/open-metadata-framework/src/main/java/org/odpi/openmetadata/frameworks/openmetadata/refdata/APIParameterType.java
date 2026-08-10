/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * APIParameterType provides an initial set of values for the parameterType property of the APIParameter schema
 * attribute.  It identifies which part of an API's schema the parameter is a part of, to help in searches of API
 * data.  These can be extended as required.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum APIParameterType implements OpenMetadataRefData
{
    /**
     * A parameter passed as part of the URL of a request, such as a path or query parameter.
     */
    REQUEST_PARAMETER (0, "requestParameter", "A parameter passed as part of the URL of a request, such as a path or query parameter.", "7ec6ef4c-485c-45e9-a6b2-d175b8b65191", true),

    /**
     * The body of a request.
     */
    REQUEST_BODY (1, "requestBody", "The body of a request.", "8e7550de-4117-4694-84f2-27158fbd6366", false),

    /**
     * The status code (eg 200) returned in a response.
     */
    RESPONSE_STATUS (2, "responseStatus", "The status code (eg 200) returned in a response.", "17185bcf-af57-413f-8708-11fb15360d07", false),

    /**
     * The body of a response.
     */
    RESPONSE_BODY (3, "responseBody", "The body of a response.", "77fea8e6-acfa-4b0f-8836-9301fffcd4b9", false),

    /**
     * A header used to pass security information, such as an authorization token.
     */
    SECURITY_HEADER (4, "securityHeader", "A header used to pass security information, such as an authorization token.", "3f2fad7f-9b50-439e-beaa-d53ce19ec91f", false),

    /**
     * An attribute nested within the structure of a request body, response body or header.
     */
    NESTED_ATTRIBUTE (5, "nestedAttribute", "An attribute nested within the structure of a request body, response body or header.", "a99557c8-f689-4704-9523-704e13bc30ba", false),

    ;

    private final int     ordinal;
    private final String  name;
    private final String  description;
    private final String  descriptionGUID;
    private final boolean isDefault;


    /**
     * Default constructor for the enum.
     *
     * @param ordinal numerical representation of the enumeration
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param isDefault is this the default value for the enum?
     */
    APIParameterType(int     ordinal,
                     String  name,
                     String  description,
                     String  descriptionGUID,
                     boolean isDefault)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
        this.descriptionGUID = descriptionGUID;
        this.isDefault = isDefault;
    }


    /**
     * Return the numeric representation of the enum value.
     *
     * @return int ordinal
     */
    @Override
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enum value.
     *
     * @return String name
     */
    @Override
    public String getDisplayName() { return name; }


    /**
     * Return the default description of the enum value.
     *
     * @return String description
     */
    @Override
    public String getDescription() { return description; }


    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return guid
     */
    @Override
    public String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Return whether the enum is the default value or not.
     *
     * @return boolean
     */
    @Override
    public boolean isDefault()
    {
        return isDefault;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "APIParameterType{name='" + name + '\'' + "}" ;
    }
}
