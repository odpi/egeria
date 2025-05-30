/*  SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CertificationElement contains the properties and header for a certification for an element.
 * It includes the details of the specific element's certification and details of the certification type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CertificationElement extends RelatedMetadataElementSummary
{
    private MetadataElementSummary certifiedBy = null;
    private MetadataElementSummary custodian = null;
    private MetadataElementSummary recipient = null;

    /**
     * Default constructor
     */
    public CertificationElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CertificationElement(CertificationElement template)
    {
        super(template);

        if (template != null)
        {
            certifiedBy = template.getCertifiedBy();
            custodian = template.getCustodian();
            recipient = template.getRecipient();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CertificationElement(RelatedMetadataElementSummary template)
    {
        super(template);
    }


    /**
     * Return the person in the certification authority that granted this certificate.
     *
     * @return element
     */
    public MetadataElementSummary getCertifiedBy()
    {
        return certifiedBy;
    }


    /**
     * Set up the person in the certification authority that granted this certificate.
     *
     * @param certifiedBy element
     */
    public void setCertifiedBy(MetadataElementSummary certifiedBy)
    {
        this.certifiedBy = certifiedBy;
    }


    /**
     * Return the person/team responsible for ensuring that the certificate conditions are adhered to.
     *
     * @return element
     */
    public MetadataElementSummary getCustodian()
    {
        return custodian;
    }


    /**
     * Set up the person/team responsible for ensuring that the certificate conditions are adhered to.
     *
     * @param custodian element
     */
    public void setCustodian(MetadataElementSummary custodian)
    {
        this.custodian = custodian;
    }


    /**
     * Return the person/team that received the certification.
     *
     * @return element
     */
    public MetadataElementSummary getRecipient()
    {
        return recipient;
    }


    /**
     * Set up the person/team that received the certification.
     *
     * @param recipient element
     */
    public void setRecipient(MetadataElementSummary recipient)
    {
        this.recipient = recipient;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CertificationElement{" +
                "certifiedBy=" + certifiedBy +
                ", custodian=" + custodian +
                ", recipient=" + recipient +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        CertificationElement that = (CertificationElement) objectToCompare;
        return Objects.equals(certifiedBy, that.certifiedBy) &&
                Objects.equals(custodian, that.custodian) &&
                Objects.equals(recipient, that.recipient);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), certifiedBy, custodian, recipient);
    }
}
