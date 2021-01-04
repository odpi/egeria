/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ExternalReference;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.License;

import java.util.Date;
import java.util.Objects;

/**
 * <p>
 *     The data economy brings licensing to data and metadata.  Even open data typically has a license.
 *     License stores the license permission associated with the asset.
 * </p>
 * <p>
 *     The license will define the permitted uses and other requirements for using the asset.
 * </p>
 *
 */
public class AssetLicense extends AssetReferenceable
{
    private static final long     serialVersionUID = 1L;

    protected License   licenseBean;


    /**
     * Bean constructor
     *
     * @param licenseBean bean with all of the properties in it
     */
    public AssetLicense(License   licenseBean)
    {
        super(licenseBean);

        if (licenseBean  == null)
        {
            this.licenseBean = new License();
        }
        else
        {
            this.licenseBean = licenseBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param licenseBean bean with all of the properties in it
     */
    public AssetLicense(AssetDescriptor     parentAsset,
                        License             licenseBean)
    {
        super(parentAsset, licenseBean);

        if (licenseBean  == null)
        {
            this.licenseBean = new License();
        }
        else
        {
            this.licenseBean = licenseBean;
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset descriptor for parent asset
     * @param templateLicense element to copy
     */
    public AssetLicense(AssetDescriptor   parentAsset, AssetLicense templateLicense)
    {
        super(parentAsset, templateLicense);

        if (templateLicense == null)
        {
            this.licenseBean = new License();
        }
        else
        {
            this.licenseBean = templateLicense.getLicenseBean();
        }
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return license bean
     */
    protected License getLicenseBean()
    {
        return licenseBean;
    }


    /**
     * Return the unique id for the type of license.
     *
     * @return String license type GUID
     */
    public String getLicenseTypeGUID() { return licenseBean.getLicenseGUID(); }


    /**
     * Return the type of the license.
     *
     * @return String license type
     */
    public String getLicenseTypeName() { return licenseBean.getLicenseTypeName(); }


    /**
     * Get the name of the organization or person that issued the license.
     *
     * @return String name
     */
    public String getLicensee() { return licenseBean.getLicensee(); }


    /**
     * Return a brief summary of the license.
     *
     * @return String summary
     */
    public String getSummary() { return licenseBean.getSummary(); }


    /**
     * Return the link to the full text of the license.
     *
     * @return ExternalReference for full text
     */
    public AssetExternalReference getLink()
    {
        ExternalReference link = licenseBean.getLink();

        if (link == null)
        {
            return null;
        }
        else
        {
            return new AssetExternalReference(super.getParentAsset(), link);
        }
    }


    /**
     * Return the start date for the license.
     *
     * @return Date
     */
    public Date getStartDate()
    {
        Date   startDate = licenseBean.getStartDate();

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
     * Return the end date for the license.
     *
     * @return Date
     */
    public Date getEndDate()
    {
        Date endDate = licenseBean.getEndDate();

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
     * Return any special conditions that apply to the license - such as endorsements.
     *
     * @return String license conditions
     */
    public String getLicenseConditions() { return licenseBean.getLicenseConditions(); }


    /**
     * Return the name of the person or organization that set up the license agreement for this asset.
     *
     * @return String name
     */
    public String getCreatedBy() { return licenseBean.getCreatedBy(); }


    /**
     * Return the name of the person or organization that is responsible for the correct management of the asset
     * according to the license.
     *
     * @return String name
     */
    public String getCustodian() { return licenseBean.getCustodian(); }


    /**
     * Return the notes for the custodian.
     *
     * @return String notes
     */
    public String getNotes() { return licenseBean.getNotes(); }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return licenseBean.toString();
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
        AssetLicense that = (AssetLicense) objectToCompare;
        return Objects.equals(licenseBean, that.licenseBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), licenseBean);
    }
}