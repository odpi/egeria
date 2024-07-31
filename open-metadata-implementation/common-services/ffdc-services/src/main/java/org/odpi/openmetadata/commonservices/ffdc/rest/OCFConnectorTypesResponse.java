/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * OCFConnectorTypesResponse is the response structure used on the OMAS REST API calls that return a
 * list of connector types as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OCFConnectorTypesResponse extends FFDCResponseBase
{
    private List<ConnectorType> connectorTypes = null;


    /**
     * Default constructor
     */
    public OCFConnectorTypesResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OCFConnectorTypesResponse(OCFConnectorTypesResponse template)
    {
        super(template);

        if (template != null)
        {
            this.connectorTypes = template.getConnectorTypes();
        }
    }


    /**
     * Return the list result.
     *
     * @return list of connector types
     */
    public List<ConnectorType> getConnectorTypes()
    {
        if (connectorTypes == null)
        {
            return null;
        }
        else if (connectorTypes.isEmpty())
        {
            return null;
        }
        else
        {
            return connectorTypes;
        }
    }


    /**
     * Set up the list result.
     *
     * @param connectorTypes list of connector types
     */
    public void setConnectorTypes(List<ConnectorType> connectorTypes)
    {
        this.connectorTypes = connectorTypes;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "OCFConnectorTypesResponse{" +
                "connectorTypes=" + connectorTypes +
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
        if (!(objectToCompare instanceof OCFConnectorTypesResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(connectorTypes, that.connectorTypes);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(connectorTypes);
    }
}
