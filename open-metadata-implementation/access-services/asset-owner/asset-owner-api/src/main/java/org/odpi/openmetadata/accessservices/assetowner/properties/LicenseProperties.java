/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LicenseProperties describe the details of a license that shows that an element is licensed with a particular license type.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicenseProperties extends RelationshipProperties
{
    private String              licenseId              = null;
    private Date                startDate              = null;
    private Date                endDate                = null;
    private String              conditions             = null;
    private String              licensedBy             = null;
    private String              licensedByTypeName     = null;
    private String              licensedByPropertyName = null;
    private String              custodian              = null;
    private String              custodianTypeName      = null;
    private String              custodianPropertyName  = null;
    private String              licensee               = null;
    private String              licenseeTypeName       = null;
    private String              licenseePropertyName   = null;
    private Map<String, String> entitlements           = null;
    private Map<String, String> restrictions           = null;
    private Map<String, String> obligations            = null;
    private String              notes                  = null;


    /**
     * Default Constructor
     */
    public LicenseProperties()
    {
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public LicenseProperties(LicenseProperties template)
    {
        super(template);

        if (template != null)
        {
            this.licenseId = template.getLicenseId();
            this.startDate = template.getStartDate();
            this.endDate = template.getEndDate();
            this.conditions = template.getConditions();
            this.licensedBy = template.getLicensedBy();
            this.licensedByTypeName = template.getLicensedByTypeName();
            this.licensedByPropertyName = template.getLicensedByPropertyName();
            this.custodian = template.getCustodian();
            this.custodianTypeName = template.getCustodianTypeName();
            this.custodianPropertyName = template.getCustodianPropertyName();
            this.licensee = template.getLicensee();
            this.licenseeTypeName = template.getLicenseeTypeName();
            this.licenseePropertyName = template.getLicenseePropertyName();
            this.entitlements = template.getEntitlements();
            this.restrictions = template.getRestrictions();
            this.obligations = template.getObligations();
            this.notes = template.getNotes();
        }
    }


    /**
     * Return the unique identifier of the license.  This value comes from the license authority.
     *
     * @return string
     */
    public String getLicenseId()
    {
        return licenseId;
    }


    /**
     * Set up the unique identifier of the license.  This value comes from the license authority.
     *
     * @param licenseId string
     */
    public void setLicenseId(String licenseId)
    {
        this.licenseId = licenseId;
    }


    /**
     * Return the date/time that this license is valid from.
     *
     * @return date/time
     */
    public Date getStartDate()
    {
        return startDate;
    }


    /**
     * Set up the date/time that this license is valid from.
     *
     * @param startDate date/time
     */
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    /**
     * Return the date/time that this license is no longer valid.
     *
     * @return date/time
     */
    public Date getEndDate()
    {
        return endDate;
    }


    /**
     * Set up the date/time that this license is no longer valid.
     *
     * @param endDate date/time
     */
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }


    /**
     * Return any conditions or endorsements to this license.
     *
     * @return string text
     */
    public String getConditions()
    {
        return conditions;
    }


    /**
     * Set up any conditions or endorsements to this license.
     *
     * @param conditions string text
     */
    public void setConditions(String conditions)
    {
        this.conditions = conditions;
    }


    /**
     * Return the name of the person in the license authority that granted this license.
     *
     * @return string name/id
     */
    public String getLicensedBy()
    {
        return licensedBy;
    }


    /**
     * Set up the name of the person in the license authority that granted this license.
     *
     * @param licensedBy string name/id
     */
    public void setLicensedBy(String licensedBy)
    {
        this.licensedBy = licensedBy;
    }


    /**
     * Return the name of the type of the element supplying the licensedBy property.
     *
     * @return string type name
     */
    public String getLicensedByTypeName()
    {
        return licensedByTypeName;
    }


    /**
     * Set up the name of the type of the element supplying the licensedBy property.
     *
     * @param licensedByTypeName string type name
     */
    public void setLicensedByTypeName(String licensedByTypeName)
    {
        this.licensedByTypeName = licensedByTypeName;
    }


    /**
     * Return the name of the property from the element supplying the licensedBy property.
     *
     * @return string property name
     */
    public String getLicensedByPropertyName()
    {
        return licensedByPropertyName;
    }


    /**
     * Set up the name of the property from the element supplying the licensedBy property.
     *
     * @param licensedByPropertyName string property name
     */
    public void setLicensedByPropertyName(String licensedByPropertyName)
    {
        this.licensedByPropertyName = licensedByPropertyName;
    }


    /**
     * Return the person/team responsible for ensuring that the license conditions are adhered to.
     *
     * @return string name/id
     */
    public String getCustodian()
    {
        return custodian;
    }


    /**
     * Set up the person/team responsible for ensuring that the license conditions are adhered to.
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
     * Return the person/team that received the license.
     *
     * @return string name/id
     */
    public String getLicensee()
    {
        return licensee;
    }


    /**
     * Set up the person/team that received the license.
     *
     * @param licensee string name/id
     */
    public void setLicensee(String licensee)
    {
        this.licensee = licensee;
    }


    /**
     * Return the name of the type of the element supplying the licensee property.
     *
     * @return string type name
     */
    public String getLicenseeTypeName()
    {
        return licenseeTypeName;
    }


    /**
     * Set up the name of the type of the element supplying the licensee property.
     *
     * @param licenseeTypeName string type name
     */
    public void setLicenseeTypeName(String licenseeTypeName)
    {
        this.licenseeTypeName = licenseeTypeName;
    }


    /**
     * Return the name of the property from the element supplying the licensee property.
     *
     * @return string property name
     */
    public String getLicenseePropertyName()
    {
        return licenseePropertyName;
    }


    /**
     * Set up the name of the property from the element supplying the licensee property.
     *
     * @param licenseePropertyName string property name
     */
    public void setLicenseePropertyName(String licenseePropertyName)
    {
        this.licenseePropertyName = licenseePropertyName;
    }


    /**
     * Return the entitlements granted by the license.
     *
     * @return name value pairs
     */
    public Map<String, String> getEntitlements()
    {
        return entitlements;
    }


    /**
     * Set up the entitlements granted by the license.
     *
     * @param entitlements name value pairs
     */
    public void setEntitlements(Map<String, String> entitlements)
    {
        this.entitlements = entitlements;
    }


    /**
     * Return the restrictions imposed by the license.
     *
     * @return name value pairs
     */
    public Map<String, String> getRestrictions()
    {
        return restrictions;
    }


    /**
     * Set up the restrictions imposed by the license.
     *
     * @param restrictions name value pairs
     */
    public void setRestrictions(Map<String, String> restrictions)
    {
        this.restrictions = restrictions;
    }


    /**
     * Return the obligations stipulated by the license.
     *
     * @return name value pairs
     */
    public Map<String, String> getObligations()
    {
        return obligations;
    }


    /**
     * Set up the obligations stipulated by the license.
     *
     * @param obligations name value pairs
     */
    public void setObligations(Map<String, String> obligations)
    {
        this.obligations = obligations;
    }


    /**
     * Return any notes associated with the license.
     *
     * @return string text
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up any notes associated with the license.
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
        return "LicenseProperties{" +
                       "licenseId='" + licenseId + '\'' +
                       ", startDate=" + startDate +
                       ", endDate=" + endDate +
                       ", conditions='" + conditions + '\'' +
                       ", licensedBy='" + licensedBy + '\'' +
                       ", licensedByTypeName='" + licensedByTypeName + '\'' +
                       ", licensedByPropertyName='" + licensedByPropertyName + '\'' +
                       ", custodian='" + custodian + '\'' +
                       ", custodianTypeName='" + custodianTypeName + '\'' +
                       ", custodianPropertyName='" + custodianPropertyName + '\'' +
                       ", licensee='" + licensee + '\'' +
                       ", licenseeTypeName='" + licenseeTypeName + '\'' +
                       ", licenseePropertyName='" + licenseePropertyName + '\'' +
                       ", entitlements=" + entitlements +
                       ", restrictions=" + restrictions +
                       ", obligations=" + obligations +
                       ", notes='" + notes + '\'' +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     *
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof LicenseProperties))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        LicenseProperties that = (LicenseProperties) objectToCompare;
        return Objects.equals(licenseId, that.licenseId) &&
                       Objects.equals(startDate, that.startDate) &&
                       Objects.equals(endDate, that.endDate) &&
                       Objects.equals(conditions, that.conditions) &&
                       Objects.equals(licensedBy, that.licensedBy) &&
                       Objects.equals(licensedByTypeName, that.licensedByTypeName) &&
                       Objects.equals(licensedByPropertyName, that.licensedByPropertyName) &&
                       Objects.equals(custodian, that.custodian) &&
                       Objects.equals(custodianTypeName, that.custodianTypeName) &&
                       Objects.equals(custodianPropertyName, that.custodianPropertyName) &&
                       Objects.equals(licensee, that.licensee) &&
                       Objects.equals(licenseeTypeName, that.licenseeTypeName) &&
                       Objects.equals(licenseePropertyName, that.licenseePropertyName) &&
                       Objects.equals(entitlements, that.entitlements) &&
                       Objects.equals(restrictions, that.restrictions) &&
                       Objects.equals(obligations, that.obligations) &&
                       Objects.equals(notes, that.notes);
    }


    /**
     * Just use the GUID for the hash code as it should be unique.
     *
     * @return int code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), licenseId, startDate, endDate, conditions, licensedBy, licensedByTypeName, licensedByPropertyName,
                            custodian, custodianTypeName, custodianPropertyName, licensee, licenseeTypeName, licenseePropertyName,
                            obligations, restrictions, entitlements, notes);
    }
}
