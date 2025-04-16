/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Provides a response object for returning information about a
 * service that is registered in an OMAG Server Platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RegisteredOMAGServiceResponse extends FFDCResponseBase
{
    private RegisteredOMAGService service;


    /**
     * Default constructor
     */
    public RegisteredOMAGServiceResponse()
    {
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public RegisteredOMAGServiceResponse(RegisteredOMAGServiceResponse template)
    {
        super(template);

        if (template != null)
        {
            this.service = template.getService();
        }
    }


    /**
     * Return the description of the service
     *
     * @return service description
     */
    public RegisteredOMAGService getService()
    {
        return service;
    }


    /**
     * Set up the description of the service
     *
     * @param service service description
     */
    public void setService(RegisteredOMAGService service)
    {
        this.service = service;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RegisteredOMAGServiceResponse{" +
                "service=" + service +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        RegisteredOMAGServiceResponse that = (RegisteredOMAGServiceResponse) objectToCompare;
        return Objects.equals(getService(), that.getService());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getService());
    }
}
