/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Certification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ExternalReference;

import java.util.Date;
import java.util.Objects;

/**
 * <p>
 *     Certification stores the certifications awarded to the asset.
 * </p>
 * <p>
 *     Many regulations and industry bodies define certifications that can confirm a level of support,
 *     capability or competence in an aspect of a digital organization's operation.
 *     Having certifications may be necessary to operating legally or may be a business advantage.
 * </p>
 * <p>
 *     The certifications awarded to an asset can be captured in the metadata repository to enable both
 *     effective use and management of the certification process.
 * </p>
 */
public class AssetCertification extends AssetReferenceable
{
    private static final long     serialVersionUID = 1L;

    protected Certification certificationBean;


    /**
     * Bean constructor
     *
     * @param certificationBean bean containing all of the parameters
     */
    public AssetCertification(Certification certificationBean)
    {
        super(certificationBean);

        if (certificationBean == null)
        {
            this.certificationBean = new Certification();
        }
        else
        {
            this.certificationBean = certificationBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param certificationBean bean with all of the properties in it
     */
    public AssetCertification(AssetDescriptor parentAsset,
                              Certification   certificationBean)
    {
        super(parentAsset, certificationBean);

        if (certificationBean == null)
        {
            this.certificationBean = new Certification();
        }
        else
        {
            this.certificationBean = certificationBean;
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset descriptor for parent asset
     * @param templateCertification element to copy
     */
    public AssetCertification(AssetDescriptor parentAsset, AssetCertification templateCertification)
    {
        super(parentAsset, templateCertification);

        if (templateCertification == null)
        {
            certificationBean = new Certification();
        }
        else
        {
            this.certificationBean = templateCertification.getCertificationBean();
        }
    }


    /**
     * Return the bean with all of the properties in it.
     *
     * @return certification bean
     */
    protected Certification getCertificationBean()
    {
        return certificationBean;
    }


    /**
     * Return the unique id for the type of certification.
     *
     * @return String certification type GUID
     */
    public String getCertificationTypeGUID() { return certificationBean.getCertificateGUID(); }


    /**
     * Return the type of the certification.
     *
     * @return String certification type
     */
    public String getCertificationTypeName() { return certificationBean.getCertificationTypeName(); }


    /**
     * Return the name of the organization or person that issued the certification.
     *
     * @return String name
     */
    public String getExaminer() { return certificationBean.getExaminer(); }


    /**
     * Return a brief summary of the certification.
     *
     * @return String summary
     */
    public String getSummary() { return certificationBean.getSummary(); }


    /**
     * Return the link to the full text of the certification.
     *
     * @return ExternalReference for full text
     */
    public AssetExternalReference getLink()
    {
        ExternalReference link  = certificationBean.getLink();

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
     * Return the start date for the certification.  Null means unknown or not relevant.
     *
     * @return Date start date for the certification
     */
    public Date getStartDate()
    {
        return certificationBean.getStartDate();
    }


    /**
     * Return the end date for the certification.   Null means it does not expire.
     *
     * @return Date end date for the certification
     */
    public Date getEndDate()
    {
        return certificationBean.getEndDate();
    }


    /**
     * Return any special conditions that apply to the certification - such as endorsements.
     *
     * @return String certification conditions
     */
    public String getCertificationConditions() { return certificationBean.getCertificationConditions(); }


    /**
     * Return the name of the person or organization that set up the certification for this asset.
     *
     * @return String name
     */
    public String getCreatedBy() { return certificationBean.getCreatedBy(); }


    /**
     * Return the name of the person or organization that is responsible for the correct management of the asset
     * according to the certification.
     *
     * @return String name
     */
    public String getCustodian() { return certificationBean.getCustodian(); }


    /**
     * Return the notes from the custodian.
     *
     * @return String notes
     */
    public String getNotes() { return certificationBean.getNotes(); }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return certificationBean.toString();
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
        AssetCertification that = (AssetCertification) objectToCompare;
        return Objects.equals(getCertificationBean(), that.getCertificationBean());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getCertificationBean());
    }
}