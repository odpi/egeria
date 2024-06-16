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
 * <p>
 *     The data economy brings licensing to data and metadata.  Even open data typically has a license.
 *     License stores the license permission associated with an asset.
 * </p>
 * <p>
 *     The license defines the permitted uses and other requirements for using the asset.
 * </p>
 *
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class License extends Referenceable
{
    /*
     * properties of a license
     */
    protected String            licenseGUID       = null;
    protected String            licenseTypeName   = null;
    protected String            licensee          = null;
    protected String            summary           = null;
    protected ExternalReference link              = null;
    protected Date              startDate         = null;
    protected Date              endDate           = null;
    protected String            licenseConditions = null;
    protected String            createdBy         = null;
    protected String            custodian         = null;
    protected String            notes             = null;


    /**
     * Default constructor
     */
    public License()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public License(License template)
    {
        super(template);

        if (template != null)
        {
            licenseGUID = template.getLicenseGUID();
            licenseTypeName = template.getLicenseTypeName();
            licensee = template.getLicensee();
            summary = template.getSummary();

            ExternalReference templateLink = template.getLink();
            if (templateLink != null)
            {
                link = new ExternalReference(templateLink);
            }

            Date templateStartDate = template.getStartDate();
            if (templateStartDate != null)
            {
                startDate = new Date(templateStartDate.getTime());
            }

            Date templateEndDate = template.getEndDate();
            if (templateEndDate != null)
            {
                endDate = new Date(templateEndDate.getTime());
            }

            licenseConditions = template.getLicenseConditions();
            createdBy = template.getCreatedBy();
            custodian = template.getCustodian();
            notes = template.getNotes();
        }
    }


    /**
     * Return the unique id for the type of license.
     *
     * @return String license type GUID
     */
    public String getLicenseGUID() { return licenseGUID; }


    /**
     * Set up the unique id for the type of license.
     *
     * @param licenseGUID String license type GUID
     */
    public void setLicenseGUID(String licenseGUID)
    {
        this.licenseGUID = licenseGUID;
    }


    /**
     * Return the name for the type of the license.
     *
     * @return String license type name
     */
    public String getLicenseTypeName() { return licenseTypeName; }


    /**
     * Set up the name for the type of the license.
     *
     * @param licenseTypeName String license type name
     */
    public void setLicenseTypeName(String licenseTypeName)
    {
        this.licenseTypeName = licenseTypeName;
    }


    /**
     * Return the name of the organization or person that issued the license.
     *
     * @return String name of person or organization
     */
    public String getLicensee() { return licensee; }


    /**
     * Set up the name of the organization or person that issued the license.
     *
     * @param licensee String name of person or organization
     */
    public void setLicensee(String licensee)
    {
        this.licensee = licensee;
    }


    /**
     * Return a brief summary of the license.
     *
     * @return String summary text
     */
    public String getSummary() { return summary; }


    /**
     * Set up a brief summary of the license.
     *
     * @param summary String summary text
     */
    public void setSummary(String summary)
    {
        this.summary = summary;
    }


    /**
     * Return the link to the full text of the license.
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
     * Set up the link to the full text of the license.
     *
     * @param link ExternalReference for full text
     */
    public void setLink(ExternalReference link)
    {
        this.link = link;
    }


    /**
     * Return the start date for the license.
     *
     * @return Date
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
     * Set up the start date for the license.
     *
     * @param startDate Date
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    /**
     * Return the end date for the license.
     *
     * @return Date
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
     * Set up the end data for the license.
     *
     * @param endDate Date
     */
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }


    /**
     * Return any special conditions that apply to the license - such as endorsements.
     *
     * @return String license conditions
     */
    public String getLicenseConditions() { return licenseConditions; }


    /**
     * Set up any special conditions that apply to the license - such as endorsements.
     *
     * @param licenseConditions String license conditions
     */
    public void setLicenseConditions(String licenseConditions)
    {
        this.licenseConditions = licenseConditions;
    }


    /**
     * Return the name of the person or organization that set up the license agreement for this asset.
     *
     * @return String name
     */
    public String getCreatedBy() { return createdBy; }


    /**
     * Set up the name of the person or organization that set up the license agreement for this asset.
     *
     * @param createdBy String name
     */
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    /**
     * Return the name of the person or organization that is responsible for the correct management of the asset
     * according to the license.
     *
     * @return String name
     */
    public String getCustodian() { return custodian; }


    /**
     * Set up the name of the person or organization that is responsible for the correct management of the asset
     * according to the license.
     *
     * @param custodian String name
     */
    public void setCustodian(String custodian)
    {
        this.custodian = custodian;
    }


    /**
     * Return the notes for the custodian.
     *
     * @return String notes
     */
    public String getNotes() { return notes; }


    /**
     * Set up the notes for the custodian.
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
        return "License{" +
                       "extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", licenseGUID='" + licenseGUID + '\'' +
                       ", licenseTypeName='" + licenseTypeName + '\'' +
                       ", licensee='" + licensee + '\'' +
                       ", summary='" + summary + '\'' +
                       ", link=" + link +
                       ", startDate=" + startDate +
                       ", endDate=" + endDate +
                       ", licenseConditions='" + licenseConditions + '\'' +
                       ", createdBy='" + createdBy + '\'' +
                       ", custodian='" + custodian + '\'' +
                       ", notes='" + notes + '\'' +
                       ", headerVersion=" + getHeaderVersion() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        License license = (License) objectToCompare;
        return Objects.equals(getLicenseGUID(), license.getLicenseGUID()) &&
                       Objects.equals(getLicenseTypeName(), license.getLicenseTypeName()) &&
                       Objects.equals(getLicensee(), license.getLicensee()) &&
                       Objects.equals(getSummary(), license.getSummary()) &&
                       Objects.equals(getLink(), license.getLink()) &&
                       Objects.equals(getStartDate(), license.getStartDate()) &&
                       Objects.equals(getEndDate(), license.getEndDate()) &&
                       Objects.equals(getLicenseConditions(), license.getLicenseConditions()) &&
                       Objects.equals(getCreatedBy(), license.getCreatedBy()) &&
                       Objects.equals(getCustodian(), license.getCustodian()) &&
                       Objects.equals(getNotes(), license.getNotes());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getLicenseGUID(), getLicenseTypeName(), getLicensee(), getSummary(), getLink(), getStartDate(),
                            getEndDate(), getLicenseConditions(), getCreatedBy(), getCustodian(), getNotes());
    }
}