/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RegisteredOMAGServicesResponse provides an object for returning information about a
 * service that is registered with an OMAG Server Platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RegisteredOMAGService implements Serializable
{
    private static final long     serialVersionUID    = 1L;

    private String serviceName;
    private String serviceURLMarker;
    private String serviceDescription;
    private String serviceWiki;


    /**
     * Default constructor
     */
    public RegisteredOMAGService()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RegisteredOMAGService(RegisteredOMAGService template)
    {
        if (template != null)
        {
            this.serviceName = template.getServiceName();
            this.serviceURLMarker = template.getServiceURLMarker();
            this.serviceDescription = template.getServiceDescription();
            this.serviceWiki = template.getServiceWiki();
        }
    }



    /**
     * Return the default name for this service.
     *
     * @return String default name
     */
    public String getServiceName()
    {
        return serviceName;
    }


    /**
     * Set up the default name for this service
     *
     * @param serviceName name
     */
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }


    /**
     * Return the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @return url fragment
     */
    public String getServiceURLMarker()
    {
        return serviceURLMarker;
    }


    /**
     * Set up the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @param serviceURLMarker url fragment
     */
    public void setServiceURLMarker(String serviceURLMarker)
    {
        this.serviceURLMarker = serviceURLMarker;
    }



    /**
     * Return the default description for this service.
     *
     * @return String default description
     */
    public String getServiceDescription()
    {
        return serviceDescription;
    }


    /**
     * Set up the default description for this service.
     *
     * @param serviceDescription text
     */
    public void setServiceDescription(String serviceDescription)
    {
        this.serviceDescription = serviceDescription;
    }



    /**
     * Return the URL for the wiki page describing this service.
     *
     * @return String URL name for the wiki page
     */
    public String getServiceWiki()
    {
        return serviceWiki;
    }


    /**
     * Set up the URL for the wiki page describing this service.
     *
     * @param serviceWiki URL
     */
    public void setServiceWiki(String serviceWiki)
    {
        this.serviceWiki = serviceWiki;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "RegisteredOMAGService{" +
                       ", serviceName='" + serviceName + '\'' +
                       ", serviceURLMarker='" + serviceURLMarker + '\'' +
                       ", serviceDescription='" + serviceDescription + '\'' +
                       ", serviceWiki='" + serviceWiki + '\'' +
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
        RegisteredOMAGService that = (RegisteredOMAGService) objectToCompare;
        return Objects.equals(getServiceName(), that.getServiceName()) &&
                       Objects.equals(getServiceURLMarker(), that.getServiceURLMarker()) &&
                       Objects.equals(getServiceDescription(), that.getServiceDescription()) &&
                       Objects.equals(getServiceWiki(), that.getServiceWiki());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getServiceName(), getServiceURLMarker(), getServiceDescription(),
                            getServiceWiki());
    }
}

