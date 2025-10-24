/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * An APIParameterListType defines which parameter list relationship to use when attaching an APIParameterList to an operation.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum APIParameterListType
{
    HEADER   (0,  "APIHeader",   "API Header",           "The structure of the properties passed as a header on this API"),
    REQUEST  (1,  "APIRequest",  "API Request Payload",  "The structure of the properties passed as a request on this API."),
    RESPONSE (2,  "APIResponse", "API Response Payload", "The structure of the properties passed as a response on this API.");

    private final String openTypeRelationshipName;

    private final int    ordinal;
    private final String name;
    private final String description;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param openTypeRelationshipName code number from the equivalent Enum Type
     * @param name default name
     * @param description default description
     */
    APIParameterListType(int    ordinal,
                         String openTypeRelationshipName,
                         String name,
                         String description)
    {
        this.ordinal                  = ordinal;
        this.openTypeRelationshipName = openTypeRelationshipName;
        this.name                     = name;
        this.description              = description;
    }

    /**
     * Return the code for this enum instance
     *
     * @return int key pattern code
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for the key pattern for this enum instance.
     *
     * @return String default description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the Open Metadata Type Name used on the relationship that links the APIParameterList to an APIOperation.
     *
     * @return string type name
     */
    public String getOpenTypeRelationshipName()
    {
        return openTypeRelationshipName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "APIParameterListType{" +
                       "openTypeRelationshipName='" + openTypeRelationshipName + '\'' +
                       ", ordinal=" + ordinal +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       '}';
    }
}