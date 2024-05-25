/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.CertificationTypeProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDefinitionStatus;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CertificationTypeRequestBody provides a structure used when creating certification types.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CertificationTypeRequestBody extends GovernanceProgramOMASAPIRequestBody
{
    private CertificationTypeProperties properties = null;
    private GovernanceDefinitionStatus  initialStatus = null;


    /**
     * Default constructor
     */
    public CertificationTypeRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CertificationTypeRequestBody(CertificationTypeRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.properties = template.getProperties();
            this.initialStatus = template.getInitialStatus();
        }
    }


    /**
     * Return the properties of the certification type.
     *
     * @return properties
     */
    public CertificationTypeProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the certification type.
     *
     * @param properties properties
     */
    public void setProperties(CertificationTypeProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the initial status of the certification type.
     *
     * @return instance status
     */
    public GovernanceDefinitionStatus getInitialStatus()
    {
        return initialStatus;
    }


    /**
     * Set up the initial status of the certification type.
     *
     * @param initialStatus instance status
     */
    public void setInitialStatus(GovernanceDefinitionStatus initialStatus)
    {
        this.initialStatus = initialStatus;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "CertificationTypeRequestBody{" +
                       "properties=" + properties +
                       ", initialStatus=" + initialStatus +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        CertificationTypeRequestBody that = (CertificationTypeRequestBody) objectToCompare;
        return Objects.equals(properties, that.properties) &&
                       initialStatus == that.initialStatus;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(properties, initialStatus);
    }
}
