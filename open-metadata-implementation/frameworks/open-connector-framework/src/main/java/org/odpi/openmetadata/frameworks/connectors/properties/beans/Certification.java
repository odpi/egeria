/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Certification bean extends the Certification from the OCF properties package with a default constructor and
 * setter methods.  This means it can be used for REST calls and other JSON based functions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Certification extends Referenceable
{
    private static final long     serialVersionUID = 1L;

    /*
     * Properties of a certification
     */
    protected String            certificateGUID         = null;
    protected String            certificationTypeName   = null;
    protected String            examiner                = null;
    protected String            summary                 = null;
    protected ExternalReference link                    = null;
    protected Date              startDate               = null;
    protected Date              endDate                 = null;
    protected String            certificationConditions = null;
    protected String            createdBy               = null;
    protected String            custodian               = null;
    protected String            recipient               = null;
    protected String            notes                   = null;


    /**
     * Default constructor
     */
    public Certification()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateCertification element to copy
     */
    public Certification(Certification templateCertification)
    {
        super(templateCertification);

        if (templateCertification != null)
        {
            certificateGUID = templateCertification.getCertificateGUID();
            certificationTypeName = templateCertification.getCertificationTypeName();
            examiner = templateCertification.getExaminer();
            summary = templateCertification.getSummary();

            ExternalReference  templateLink = templateCertification.getLink();
            if (templateLink != null)
            {
                link = new ExternalReference(templateLink);
            }

            Date               templateStartDate = templateCertification.getStartDate();
            if (templateStartDate != null)
            {
                startDate = new Date(templateStartDate.getTime());
            }

            Date               templateEndDate = templateCertification.getEndDate();
            if (templateEndDate != null)
            {
                endDate = new Date(templateEndDate.getTime());
            }

            certificationConditions = templateCertification.getCertificationConditions();
            createdBy = templateCertification.getCreatedBy();
            custodian = templateCertification.getCustodian();
            recipient = templateCertification.getRecipient();
            notes = templateCertification.getNotes();
        }
    }


    /**
     * Return the unique id for the type of certification.
     *
     * @return String certification type GUID
     */
    public String getCertificateGUID() { return certificateGUID; }


    /**
     * Set up the unique id for the type of certification.
     *
     * @param certificateGUID  String certification type GUID
     */
    public void setCertificateGUID(String certificateGUID)
    {
        this.certificateGUID = certificateGUID;
    }


    /**
     * Return the type of the certification.
     *
     * @return String certification type
     */
    public String getCertificationTypeName() { return certificationTypeName; }


    /**
     * Set up the type of the certification.
     *
     * @param certificationTypeName  String certification type
     */
    public void setCertificationTypeName(String certificationTypeName)
    {
        this.certificationTypeName = certificationTypeName;
    }


    /**
     * Return the name of the organization or person that issued the certification.
     *
     * @return String name
     */
    public String getExaminer() { return examiner; }


    /**
     * Set up the name of the organization or person that issued the certification.
     *
     * @param examiner  String name
     */
    public void setExaminer(String examiner)
    {
        this.examiner = examiner;
    }



    /**
     * Return a brief summary of the certification.
     *
     * @return String summary
     */
    public String getSummary() { return summary; }


    /**
     * Set up a brief summary of the certification.
     *
     * @param summary  String summary
     */
    public void setSummary(String summary)
    {
        this.summary = summary;
    }


    /**
     * Set up the link to the full text of the certification.
     *
     * @param link  ExternalReference for full text
     */
    public void setLink(ExternalReference link)
    {
        this.link = link;
    }


    /**
     * Return the link to the full text of the certification.
     *
     * @return ExternalReference for full text
     */
    public ExternalReference getLink()
    {
        if (link == null)
        {
            return null;
        }
        else
        {
            return new ExternalReference(link);
        }
    }


    /**
     * Set up the start date for the certification.  Null means unknown or not relevant.
     *
     * @param startDate start date for the certification
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    /**
     * Return the start date for the certification.  Null means unknown or not relevant.
     *
     * @return Date start date for the certification
     */
    public Date getStartDate()
    {
        if (startDate == null)
        {
            return null;
        }
        else
        {
            return new Date(startDate.getTime());
        }
    }


    /**
     * Return the end date for the certification.   Null means it does not expire.
     *
     * @return Date end date for the certification
     */
    public Date getEndDate()
    {
        if (endDate == null)
        {
            return null;
        }
        else
        {
            return new Date(endDate.getTime());
        }
    }


    /**
     * Set up the end date for the certification.   Null means it does not expire.
     *
     * @param endDate  end date for the certification
     */
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }


    /**
     * Return any special conditions that apply to the certification - such as endorsements.
     *
     * @return String certification conditions
     */
    public String getCertificationConditions() { return certificationConditions; }


    /**
     * Set up any special conditions that apply to the certification - such as endorsements.
     *
     * @param certificationConditions  String certification conditions
     */
    public void setCertificationConditions(String certificationConditions)
    {
        this.certificationConditions = certificationConditions;
    }


    /**
     * Return the name of the person or organization that set up the certification for this asset.
     *
     * @return String name
     */
    public String getCreatedBy() { return createdBy; }


    /**
     * Set up the name of the person or organization that set up the certification for this asset.
     *
     * @param createdBy  String name
     */
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    /**
     * Return the name of the person or organization that is responsible for the correct management of the asset
     * according to the certification.
     *
     * @return String name
     */
    public String getCustodian() { return custodian; }


    /**
     * Set up the name of the person or organization that is responsible for the correct management of the asset
     * according to the certification.
     *
     * @param custodian  String name
     */
    public void setCustodian(String custodian)
    {
        this.custodian = custodian;
    }


    /**
     * Return the identifier of the person or organization that received the certification.
     *
     * @return string identifier
     */
    public String getRecipient()
    {
        return recipient;
    }


    /**
     * Set up the identifier of the person or organization that received the certification.
     *
     * @param recipient string identifier
     */
    public void setRecipient(String recipient)
    {
        this.recipient = recipient;
    }

    /**
     * Return the notes from the custodian.
     *
     * @return String notes
     */
    public String getNotes() { return notes; }


    /**
     * Set up the notes from the custodian.
     *
     * @param notes String notes
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Certification{" +
                "certificateGUID='" + certificateGUID + '\'' +
                ", certificationTypeName='" + certificationTypeName + '\'' +
                ", examiner='" + examiner + '\'' +
                ", summary='" + summary + '\'' +
                ", link=" + link +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", certificationConditions='" + certificationConditions + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", custodian='" + custodian + '\'' +
                ", recipient='" + recipient + '\'' +
                ", notes='" + notes + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
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
        Certification that = (Certification) objectToCompare;
        return Objects.equals(getCertificateGUID(), that.getCertificateGUID()) &&
                       Objects.equals(getCertificationTypeName(), that.getCertificationTypeName()) &&
                       Objects.equals(getExaminer(), that.getExaminer()) &&
                       Objects.equals(getSummary(), that.getSummary()) &&
                       Objects.equals(getLink(), that.getLink()) &&
                       Objects.equals(getStartDate(), that.getStartDate()) &&
                       Objects.equals(getEndDate(), that.getEndDate()) &&
                       Objects.equals(getCertificationConditions(), that.getCertificationConditions()) &&
                       Objects.equals(getCreatedBy(), that.getCreatedBy()) &&
                       Objects.equals(getCustodian(), that.getCustodian()) &&
                       Objects.equals(getRecipient(), that.getRecipient()) &&
                       Objects.equals(getNotes(), that.getNotes());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getCertificateGUID(), getCertificationTypeName(), getExaminer(), getSummary(), getLink(),
                            getStartDate(),
                            getEndDate(), getCertificationConditions(), getCreatedBy(), getCustodian(), getRecipient(), getNotes());
    }
}
