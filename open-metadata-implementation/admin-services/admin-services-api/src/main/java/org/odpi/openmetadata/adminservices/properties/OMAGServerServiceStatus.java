/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMAGServerServiceStatus contains the status of each of the services running in the server.
 * It is useful in determining which services are active and which service is causing a server to be stuck starting or stopping.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMAGServerServiceStatus implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String             serviceName   = null;
    private ServerActiveStatus serviceStatus = ServerActiveStatus.UNKNOWN;


    /**
     * Default constructor for Jackson
     */
    public OMAGServerServiceStatus()
    {
    }


    /**
     * Return the name of the service
     *
     * @return string name
     */
    public String getServiceName()
    {
        return serviceName;
    }


    /**
     * Set up the name of the service.
     *
     * @param serviceName string name
     */
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }


    /**
     * Return the current status.
     *
     * @return server instance status enum value
     */
    public ServerActiveStatus getServiceStatus()
    {
        return serviceStatus;
    }


    /**
     * Set up the current status.
     *
     * @param serviceStatus server instance status enum value
     */
    public void setServiceStatus(ServerActiveStatus serviceStatus)
    {
        this.serviceStatus = serviceStatus;
    }


    /**
     * JSON like toString method
     *
     * @return string representing the local variables
     */
    @Override
    public String toString()
    {
        return "OMAGServerServiceStatus{" +
                       "serviceName='" + serviceName + '\'' +
                       ", serviceStatus=" + serviceStatus +
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
        OMAGServerServiceStatus that = (OMAGServerServiceStatus) objectToCompare;
        return Objects.equals(serviceName, that.serviceName) &&
                       serviceStatus == that.serviceStatus;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(serviceName, serviceStatus);
    }
}
