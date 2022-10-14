/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.auditlog;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSAuditLogStoreReport is a bean to describe one of the audit log store connectors registered with a server.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMRSAuditLogStoreReport implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String       destinationName     = null;
    private List<String> supportedSeverities = null;
    private String       implementationClass = null;


    /**
     * Default constructor
     */
    public OMRSAuditLogStoreReport()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OMRSAuditLogStoreReport(OMRSAuditLogStoreReport template)
    {
        if (template != null)
        {
            destinationName = template.getDestinationName();
            supportedSeverities = template.getSupportedSeverities();
            implementationClass = template.getImplementationClass();
        }
    }


    /**
     * Return the name of this audit log store destination.
     *
     * @return string name
     */
    public String getDestinationName()
    {
        return destinationName;
    }


    /**
     * Set up the name of this audit log store destination.
     *
     * @param destinationName string name
     */
    public void setDestinationName(String destinationName)
    {
        this.destinationName = destinationName;
    }


    /**
     * Return the list of supported severities - null means all.
     *
     * @return list of severity names
     */
    public List<String> getSupportedSeverities()
    {
        if (supportedSeverities == null)
        {
            return null;
        }
        else if (supportedSeverities.isEmpty())
        {
            return null;
        }

        return new ArrayList<>(supportedSeverities);
    }


    /**
     * Set up the list of supported severities - null means all.
     *
     * @param supportedSeverities list of severity names
     */
    public void setSupportedSeverities(List<String> supportedSeverities)
    {
        this.supportedSeverities = supportedSeverities;
    }


    /**
     * Return the name of the class that is providing the implementation of this audit log store.
     *
     * @return string name
     */
    public String getImplementationClass()
    {
        return implementationClass;
    }


    /**
     * Set up the name of the class that is providing the implementation of this audit log store.
     *
     * @param implementationClass string name
     */
    public void setImplementationClass(String implementationClass)
    {
        this.implementationClass = implementationClass;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OMRSAuditLogStoreReport{" +
                "destinationName='" + destinationName + '\'' +
                ", supportedSeverities=" + supportedSeverities +
                ", implementationClass='" + implementationClass + '\'' +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        OMRSAuditLogStoreReport that = (OMRSAuditLogStoreReport) objectToCompare;
        return Objects.equals(destinationName, that.destinationName) &&
                Objects.equals(supportedSeverities, that.supportedSeverities) &&
                Objects.equals(implementationClass, that.implementationClass);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(destinationName, supportedSeverities, implementationClass);
    }
}
