/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * OCFConnectionResponse is the response structure used on REST API calls that return a
 * Connection object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OCFConnectionResponse extends FFDCResponseBase
{
    private Connection connection = null;

    /**
     * Default constructor
     */
    public OCFConnectionResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OCFConnectionResponse(OCFConnectionResponse template)
    {
        super(template);

        if (template != null)
        {
            this.connection = template.getConnection();
        }
    }


    /**
     * Return the Connection object.
     *
     * @return connection
     */
    public Connection getConnection()
    {
        return connection;
    }


    /**
     * Set up the Connection object.
     *
     * @param connection - connection object
     */
    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "OCFConnectionResponse{" +
                "connection=" + connection +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof OCFConnectionResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        OCFConnectionResponse that = (OCFConnectionResponse) objectToCompare;
        return Objects.equals(getConnection(), that.getConnection());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        if (connection == null)
        {
            return super.hashCode();
        }
        else
        {
            return connection.hashCode();
        }
    }
}
