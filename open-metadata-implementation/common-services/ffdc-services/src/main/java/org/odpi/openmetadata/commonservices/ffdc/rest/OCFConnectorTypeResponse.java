/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * OCFConnectorTypeResponse is the response structure used on REST API calls that return a
 * ConnectorType object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OCFConnectorTypeResponse extends FFDCResponseBase
{
    private ConnectorType connectorType = null;

    /**
     * Default constructor
     */
    public OCFConnectorTypeResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OCFConnectorTypeResponse(OCFConnectorTypeResponse template)
    {
        super(template);

        if (template != null)
        {
            this.connectorType = template.getConnectorType();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OCFConnectorTypeResponse(ConnectorType template)
    {
        super(null);

        if (template != null)
        {
            this.connectorType = template;
        }
    }

    /**
     * Return the ConnectorType object.
     *
     * @return connectorType
     */
    public ConnectorType getConnectorType()
    {
        return connectorType;
    }


    /**
     * Set up the ConnectorType object.
     *
     * @param connectorType - connectorType object
     */
    public void setConnectorType(ConnectorType connectorType)
    {
        this.connectorType = connectorType;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "OCFConnectorTypeResponse{" +
                "connectorType=" + connectorType +
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
        if (! (objectToCompare instanceof OCFConnectorTypeResponse response))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }

        return connectorType != null ? connectorType.equals(response.connectorType) : response.connectorType == null;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), connectorType);
    }
}
