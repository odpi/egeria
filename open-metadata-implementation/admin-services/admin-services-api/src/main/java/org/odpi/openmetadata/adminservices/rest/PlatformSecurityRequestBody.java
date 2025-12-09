/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PlatformSecurityRequestBody passes information to set up a security connector to protect
 * requests to the platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PlatformSecurityRequestBody extends URLRequestBody
{
    private Connection  platformSecurityConnection = null;


    /**
     * Default constructor
     */
    public PlatformSecurityRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template source
     */
    public PlatformSecurityRequestBody(PlatformSecurityRequestBody   template)
    {
        super(template);

        if (template != null)
        {
            platformSecurityConnection = template.getPlatformSecurityConnection();
        }
    }


    public Connection getPlatformSecurityConnection()
    {
        return platformSecurityConnection;
    }


    public void setPlatformSecurityConnection(Connection platformSecurityConnection)
    {
        this.platformSecurityConnection = platformSecurityConnection;
    }


    /**
     * JSON-style toString
     *
     * @return description of the object values
     */
    @Override
    public String toString()
    {
        return "PlatformSecurityRequestBody{" +
                "platformSecurityConnection=" + platformSecurityConnection +
                "} " + super.toString();
    }


    /**
     * Compare objects
     *
     * @param objectToCompare object
     * @return boolean
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        PlatformSecurityRequestBody that = (PlatformSecurityRequestBody) objectToCompare;
        return Objects.equals(getPlatformSecurityConnection(), that.getPlatformSecurityConnection());
    }


    /**
     * Simple hash for the object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getPlatformSecurityConnection());
    }
}
