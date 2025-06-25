/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CertificationProperties describe the details of a certificate that shows that an element is certified with a particular certification type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CertificationProperties extends RelationshipProperties
{
    private String certificateId           = null;
    private Date   startDate               = null;
    private Date   endDate                 = null;
    private String conditions              = null;
    private String certifiedBy             = null;
    private String certifiedByTypeName     = null;
    private String certifiedByPropertyName = null;
    private String custodian               = null;
    private String custodianTypeName       = null;
    private String custodianPropertyName   = null;
    private String recipient               = null;
    private String recipientTypeName       = null;
    private String recipientPropertyName   = null;
    private String notes                   = null;


    /**
     * Default Constructor
     */
    public CertificationProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public CertificationProperties(CertificationProperties template)
    {
        super(template);

        if (template != null)
        {
            this.certificateId = template.getCertificateId();
            this.startDate = template.getStartDate();
            this.endDate = template.getEndDate();
            this.conditions = template.getConditions();
            this.certifiedBy = template.getCertifiedBy();
            this.certifiedByTypeName = template.getCertifiedByTypeName();
            this.certifiedByPropertyName = template.getCertifiedByPropertyName();
            this.custodian = template.getCustodian();
            this.custodianTypeName = template.getCustodianTypeName();
            this.custodianPropertyName = template.getCustodianPropertyName();
            this.recipient = template.getRecipient();
            this.recipientTypeName = template.getRecipientTypeName();
            this.recipientPropertyName = template.getRecipientPropertyName();
            this.notes = template.getNotes();
        }
    }


    /**
     * Return the unique identifier of the certificate.  This value comes from the certificate authority.
     *
     * @return string
     */
    public String getCertificateId()
    {
        return certificateId;
    }


    /**
     * Set up the unique identifier of the certificate.  This value comes from the certificate authority.
     *
     * @param certificateId string
     */
    public void setCertificateId(String certificateId)
    {
        this.certificateId = certificateId;
    }


    /**
     * Return the date/time that this certificate is valid from.
     *
     * @return date/time
     */
    public Date getStartDate()
    {
        return startDate;
    }


    /**
     * Set up the date/time that this certificate is valid from.
     *
     * @param startDate date/time
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    /**
     * Return the date/time that this certificate is no longer valid.
     *
     * @return date/time
     */
    public Date getEndDate()
    {
        return endDate;
    }


    /**
     * Set up the date/time that this certificate is no longer valid.
     *
     * @param endDate date/time
     */
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }


    /**
     * Return any conditions or endorsements to this certificate.
     *
     * @return string text
     */
    public String getConditions()
    {
        return conditions;
    }


    /**
     * Set up any conditions or endorsements to this certificate.
     *
     * @param conditions string text
     */
    public void setConditions(String conditions)
    {
        this.conditions = conditions;
    }


    /**
     * Return the name of the person in the certification authority that granted this certificate.
     *
     * @return string name/id
     */
    public String getCertifiedBy()
    {
        return certifiedBy;
    }


    /**
     * Set up the name of the person in the certification authority that granted this certificate.
     *
     * @param certifiedBy string name/id
     */
    public void setCertifiedBy(String certifiedBy)
    {
        this.certifiedBy = certifiedBy;
    }


    /**
     * Return the name of the type of the element supplying the certifiedBy property.
     *
     * @return string type name
     */
    public String getCertifiedByTypeName()
    {
        return certifiedByTypeName;
    }


    /**
     * Set up the name of the type of the element supplying the certifiedBy property.
     *
     * @param certifiedByTypeName string type name
     */
    public void setCertifiedByTypeName(String certifiedByTypeName)
    {
        this.certifiedByTypeName = certifiedByTypeName;
    }


    /**
     * Return the name of the property from the element supplying the certifiedBy property.
     *
     * @return string property name
     */
    public String getCertifiedByPropertyName()
    {
        return certifiedByPropertyName;
    }


    /**
     * Set up the name of the property from the element supplying the certifiedBy property.
     *
     * @param certifiedByPropertyName string property name
     */
    public void setCertifiedByPropertyName(String certifiedByPropertyName)
    {
        this.certifiedByPropertyName = certifiedByPropertyName;
    }



    /**
     * Return the person/team responsible for ensuring that the certificate conditions are adhered to.
     *
     * @return string name/id
     */
    public String getCustodian()
    {
        return custodian;
    }


    /**
     * Set up the person/team responsible for ensuring that the certificate conditions are adhered to.
     *
     * @param custodian string name/id
     */
    public void setCustodian(String custodian)
    {
        this.custodian = custodian;
    }


    /**
     * Return the name of the type of the element supplying the custodian property.
     *
     * @return string type name
     */
    public String getCustodianTypeName()
    {
        return custodianTypeName;
    }


    /**
     * Set up the name of the type of the element supplying the custodian property.
     *
     * @param custodianTypeName string type name
     */
    public void setCustodianTypeName(String custodianTypeName)
    {
        this.custodianTypeName = custodianTypeName;
    }


    /**
     * Return the name of the property from the element supplying the custodian property.
     *
     * @return string property name
     */
    public String getCustodianPropertyName()
    {
        return custodianPropertyName;
    }


    /**
     * Set up the name of the property from the element supplying the custodian property.
     *
     * @param custodianPropertyName string property name
     */
    public void setCustodianPropertyName(String custodianPropertyName)
    {
        this.custodianPropertyName = custodianPropertyName;
    }


    /**
     * Return the person/team that received the certification.
     *
     * @return string name/id
     */
    public String getRecipient()
    {
        return recipient;
    }


    /**
     * Set up the person/team that received the certification.
     *
     * @param recipient string name/id
     */
    public void setRecipient(String recipient)
    {
        this.recipient = recipient;
    }


    /**
     * Return the name of the type of the element supplying the recipient property.
     *
     * @return string type name
     */
    public String getRecipientTypeName()
    {
        return recipientTypeName;
    }


    /**
     * Set up the name of the type of the element supplying the recipient property.
     *
     * @param recipientTypeName string type name
     */
    public void setRecipientTypeName(String recipientTypeName)
    {
        this.recipientTypeName = recipientTypeName;
    }


    /**
     * Return the name of the property from the element supplying the recipient property.
     *
     * @return string property name
     */
    public String getRecipientPropertyName()
    {
        return recipientPropertyName;
    }


    /**
     * Set up the name of the property from the element supplying the recipient property.
     *
     * @param recipientPropertyName string property name
     */
    public void setRecipientPropertyName(String recipientPropertyName)
    {
        this.recipientPropertyName = recipientPropertyName;
    }


    /**
     * Return any notes associated with the certificate.
     *
     * @return string text
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up any notes associated with the certificate.
     *
     * @param notes string text
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "CertificationProperties{" +
                "certificateId='" + certificateId + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", conditions='" + conditions + '\'' +
                ", certifiedBy='" + certifiedBy + '\'' +
                ", certifiedByTypeName='" + certifiedByTypeName + '\'' +
                ", certifiedByPropertyName='" + certifiedByPropertyName + '\'' +
                ", custodian='" + custodian + '\'' +
                ", custodianTypeName='" + custodianTypeName + '\'' +
                ", custodianPropertyName='" + custodianPropertyName + '\'' +
                ", recipient='" + recipient + '\'' +
                ", recipientTypeName='" + recipientTypeName + '\'' +
                ", recipientPropertyName='" + recipientPropertyName + '\'' +
                ", notes='" + notes + '\'' +
                "} " + super.toString();
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
        if (! (objectToCompare instanceof CertificationProperties))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        CertificationProperties that = (CertificationProperties) objectToCompare;
        return Objects.equals(certificateId, that.certificateId) && Objects.equals(startDate,
                                                                                   that.startDate) && Objects.equals(
                endDate, that.endDate) && Objects.equals(conditions, that.conditions) && Objects.equals(certifiedBy,
                                                                                                        that.certifiedBy) && Objects.equals(
                certifiedByTypeName, that.certifiedByTypeName) && Objects.equals(certifiedByPropertyName,
                                                                                 that.certifiedByPropertyName) && Objects.equals(
                custodian, that.custodian) && Objects.equals(custodianTypeName, that.custodianTypeName) && Objects.equals(
                custodianPropertyName, that.custodianPropertyName) && Objects.equals(recipient, that.recipient) && Objects.equals(
                recipientTypeName, that.recipientTypeName) && Objects.equals(recipientPropertyName,
                                                                             that.recipientPropertyName) && Objects.equals(notes,
                                                                                                                           that.notes);
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), certificateId, startDate, endDate, conditions, certifiedBy, certifiedByTypeName,
                            certifiedByPropertyName,
                            custodian, custodianTypeName, custodianPropertyName, recipient, recipientTypeName, recipientPropertyName, notes);
    }
}
