/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LicenseTypeProperties defines a license that the organization recognizes and has governance
 * definitions to support it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LicenseTypeProperties extends GovernanceDefinitionProperties
{
    private static final long    serialVersionUID = 1L;

    private  String   details = null;


    /**
     * Default Constructor
     */
    public LicenseTypeProperties()
    {
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public LicenseTypeProperties(LicenseTypeProperties template)
    {
        super(template);

        if (template != null)
        {
            this.details = template.getDetails();
        }
    }


    /**
     * Return the specific details of the license.
     *
     * @return string description
     */
    public String getDetails()
    {
        return details;
    }


    /**
     * Set up the specific details of the license.
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
        return "LicenseTypeProperties{" +
                       "title='" + getTitle() + '\'' +
                       ", summary='" + getSummary() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", scope='" + getScope() + '\'' +
                       ", domainIdentifier=" + getDomainIdentifier() +
                       ", priority='" + getPriority() + '\'' +
                       ", implications=" + getImplications() +
                       ", outcomes=" + getOutcomes() +
                       ", results=" + getResults() +
                       ", details='" + details + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        LicenseTypeProperties that = (LicenseTypeProperties) objectToCompare;
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
