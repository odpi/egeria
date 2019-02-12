/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
public class CertificationType extends GovernanceDefinition
{
    private  String   details = null;


    /**
     * Default Constructor
     */
    public CertificationType()
    {
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public CertificationType(CertificationType  template)
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
        return "CertificationType{" +
                "details='" + details + '\'' +
                ", description='" + getDescription() + '\'' +
                ", scope='" + getScope() + '\'' +
                ", status=" + getStatus() +
                ", priority='" + getPriority() + '\'' +
                ", implications=" + getImplications() +
                ", outcomes=" + getOutcomes() +
                ", externalReferences=" + getExternalReferences() +
                ", additionalProperties=" + getAdditionalProperties() +
                ", governanceMetrics=" + getGovernanceMetrics() +
                ", GUID='" + getGUID() + '\'' +
                ", type='" + getType() + '\'' +
                ", documentId='" + getDocumentId() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", summary='" + getSummary() + '\'' +
                '}';
    }


    /**
     * Test the properties of the CertificationType to determine if the supplied object is equal to this one.
     *
     * @param objectToCompare object
     * @return boolean evaluation
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof CertificationType))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        CertificationType that = (CertificationType) objectToCompare;
        return Objects.equals(getDetails(), that.getDetails());
    }
}
