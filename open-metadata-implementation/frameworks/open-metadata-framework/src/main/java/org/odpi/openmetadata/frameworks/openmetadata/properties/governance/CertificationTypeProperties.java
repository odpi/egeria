/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Many regulations and industry bodies define certifications that can confirm a level of support, capability
 * or competence in an aspect of a digital organization's operation.  Having certifications may be
 * necessary to operating legally or may be a business advantage.   The certifications awarded can be captured
 * in the metadata repository to enable both use and management of the certification process.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CertificationTypeProperties extends GovernanceDefinitionProperties
{
    private String details = null;


    /**
     * Default Constructor
     */
    public CertificationTypeProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.CERTIFICATION_TYPE.typeName);
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public CertificationTypeProperties(CertificationTypeProperties template)
    {
        super(template);

        if (template != null)
        {
            this.details = template.getDetails();
        }
    }


    /**
     * Return the specific details of the certification.
     *
     * @return string description
     */
    public String getDetails()
    {
        return details;
    }


    /**
     * Set up the specific details of the certification.
     *
     * @param details string description
     */
    public void setDetails(String details)
    {
        this.details = details;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "CertificationTypeProperties{" +
                "details='" + details + '\'' +
                "} " + super.toString();
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        CertificationTypeProperties that = (CertificationTypeProperties) objectToCompare;
        return Objects.equals(details, that.details);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), details);
    }
}
