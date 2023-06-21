/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConnectionRequestBody carries the parameters for created a new asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectionRequestBody extends OCFOMASAPIRequestBody
{
    protected String       shortDescription   = null;
    protected Connection   connection         = null;

    /**
     * Default constructor
     */
    public ConnectionRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConnectionRequestBody(ConnectionRequestBody template)
    {
        super(template);

        if (template != null)
        {
            shortDescription = template.getShortDescription();
            connection = template.getConnection();
        }
    }


    /**
     * Returns the short description of the asset from relationship with Connection.
     *
     * @return shortDescription String
     */
    public String getShortDescription()
    {
        return shortDescription;
    }


    /**
     * Set up the short description of the asset from relationship with Connection.
     *
     * @param shortDescription String text
     */
    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }


    /**
     * Return the Connection object.
     *
     * @return connection
     */
    public Connection getConnection()
    {
        if (connection == null)
        {
            return null;
        }
        else
        {
            return connection;
        }
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConnectionRequestBody{" +
                "shortDescription='" + shortDescription + '\'' +
                ", connection=" + connection +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        ConnectionRequestBody that = (ConnectionRequestBody) objectToCompare;
        return Objects.equals(getShortDescription(), that.getShortDescription()) &&
               Objects.equals(getConnection(), that.getConnection());
    }



    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getShortDescription(), getConnection());
    }
}
