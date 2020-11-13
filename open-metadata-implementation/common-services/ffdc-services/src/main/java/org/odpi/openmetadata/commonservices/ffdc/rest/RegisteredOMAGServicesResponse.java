/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RegisteredOMAGServicesResponse provides a response object for returning information about a
 * list of services that are registered in an OMAG Server Platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RegisteredOMAGServicesResponse extends FFDCResponseBase
{
    private static final long    serialVersionUID = 1L;

    private List<RegisteredOMAGService> services;


    /**
     * Default constructor
     */
    public RegisteredOMAGServicesResponse()
    {
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public RegisteredOMAGServicesResponse(RegisteredOMAGServicesResponse template)
    {
        super(template);

        if (template != null)
        {
            this.services = template.getServices();
        }
    }


    /**
     * Return the list of services
     *
     * @return service descriptions
     */
    public List<RegisteredOMAGService> getServices()
    {
        if (services == null)
        {
            return null;
        }
        else if (services.isEmpty())
        {
            return null;
        }
        else
        {
            return services;
        }
    }


    /**
     * Set up the list of services
     *
     * @param services service descriptions
     */
    public void setServices(List<RegisteredOMAGService> services)
    {
        this.services = services;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RegisteredOMAGServicesResponse{" +
                "services=" + services +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
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
        RegisteredOMAGServicesResponse that = (RegisteredOMAGServicesResponse) objectToCompare;
        return Objects.equals(getServices(), that.getServices());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getServices());
    }
}
