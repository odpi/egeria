/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataEventProtocolVersion provides the identifier for the version number of the event payload.  There is
 * only one version at the moment which is why it looks a little sad.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OpenMetadataEventProtocolVersion
{
    /**
     * Version 1
     */
    V1 (1, "V1", "Version 1");


    private final int    ordinal;
    private final String name;
    private final String description;


    /**
     * Typical Constructor
     *
     * @param ordinal index
     * @param name short description
     * @param description longer definition
     */
    OpenMetadataEventProtocolVersion(int ordinal, String name, String description)
    {
        /*
         * Save the values supplied
         */
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int  code
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
     * Return the default description for this enum instance.
     *
     * @return String default description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenMetadataEventProtocolVersion{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
