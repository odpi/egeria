/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ExternalReference;

import java.util.Objects;

/**
 * ExternalReference stores information about an link to an external resource that is relevant to this asset.
 */
public class AssetExternalReference extends AssetReferenceable
{
    private static final long     serialVersionUID = 1L;

    protected ExternalReference   externalReferenceBean;

    /**
     * Bean constructor
     *
     * @param externalReferenceBean bean with all of the properties in it
     */
    public AssetExternalReference(ExternalReference    externalReferenceBean)
    {
        super(externalReferenceBean);

        if (externalReferenceBean == null)
        {
            this.externalReferenceBean = new ExternalReference();
        }
        else
        {
            this.externalReferenceBean = externalReferenceBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param externalReferenceBean bean with all of the properties in it
     */
    public AssetExternalReference(AssetDescriptor      parentAsset,
                                  ExternalReference    externalReferenceBean)
    {
        super(parentAsset, externalReferenceBean);

        if (externalReferenceBean == null)
        {
            this.externalReferenceBean = new ExternalReference();
        }
        else
        {
            this.externalReferenceBean = externalReferenceBean;
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset descriptor for parent asset
     * @param templateExternalReference element to copy
     */
    public AssetExternalReference(AssetDescriptor parentAsset, AssetExternalReference templateExternalReference)
    {
        super(parentAsset, templateExternalReference);

        if (templateExternalReference == null)
        {
            this.externalReferenceBean = new ExternalReference();
        }
        else
        {
            this.externalReferenceBean = templateExternalReference.getExternalReferenceBean();
        }
    }


    /**
     * Return the bean - used in the cloning process.
     *
     * @return bean with all of the properties in
     */
    protected ExternalReference  getExternalReferenceBean()
    {
        return externalReferenceBean;
    }


    /**
     * Return the identifier given to this reference (with respect to this asset).
     *
     * @return String referenceId
     */
    public String getReferenceId() { return externalReferenceBean.getReferenceId(); }


    /**
     * Return the description of the reference (with respect to this asset).
     *
     * @return String link description.
     */
    public String getLinkDescription() { return externalReferenceBean.getLinkDescription(); }


    /**
     * Return the display name of this external reference.
     *
     * @return String display name.
     */
    public String getDisplayName() { return externalReferenceBean.getDisplayName(); }


    /**
     * Return the URI used to retrieve the resource that this external reference represents.
     *
     * @return String URI
     */
    public String getURI() { return externalReferenceBean.getURI(); }


    /**
     * Return the description of the resource that this external reference represents.
     *
     * @return String resource description
     */
    public String getResourceDescription() { return externalReferenceBean.getResourceDescription(); }


    /**
     * Return the version of the resource that this external reference represents.
     *
     * @return String version
     */
    public String getVersion() { return externalReferenceBean.getVersion(); }


    /**
     * Return the name of the organization that owns the resource that this external reference represents.
     *
     * @return String organization name
     */
    public String getOrganization() { return externalReferenceBean.getOrganization(); }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return externalReferenceBean.toString();
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
        AssetExternalReference that = (AssetExternalReference) objectToCompare;
        return Objects.equals(externalReferenceBean, that.externalReferenceBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalReferenceBean);
    }
}