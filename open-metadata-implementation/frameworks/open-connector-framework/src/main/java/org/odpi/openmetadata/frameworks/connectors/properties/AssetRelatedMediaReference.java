/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.RelatedMediaReference;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.RelatedMediaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.RelatedMediaUsage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * RelatedMediaReference stores information about an link to an external media file that
 * is relevant to this asset.
 */
public class AssetRelatedMediaReference extends AssetExternalReference
{
    private static final long     serialVersionUID = 1L;

    protected RelatedMediaReference  relatedMediaReferenceBean;


    /**
     * Bean constructor
     *
     * @param relatedMediaReferenceBean bean containing the properties
     */
    public AssetRelatedMediaReference(RelatedMediaReference  relatedMediaReferenceBean)
    {
        super(relatedMediaReferenceBean);

        if (relatedMediaReferenceBean == null)
        {
            this.relatedMediaReferenceBean = new RelatedMediaReference();
        }
        else
        {
            this.relatedMediaReferenceBean = relatedMediaReferenceBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param relatedMediaReferenceBean bean containing the properties
     */
    public AssetRelatedMediaReference(AssetDescriptor        parentAsset,
                                      RelatedMediaReference  relatedMediaReferenceBean)
    {
        super(parentAsset, relatedMediaReferenceBean);

        if (relatedMediaReferenceBean == null)
        {
            this.relatedMediaReferenceBean = new RelatedMediaReference();
        }
        else
        {
            this.relatedMediaReferenceBean = relatedMediaReferenceBean;
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset descriptor for parent asset
     * @param templateRelatedMediaReference element to copy
     */
    public AssetRelatedMediaReference(AssetDescriptor            parentAsset,
                                      AssetRelatedMediaReference templateRelatedMediaReference)
    {
        super(parentAsset, templateRelatedMediaReference);

        if (templateRelatedMediaReference == null)
        {
            relatedMediaReferenceBean = new RelatedMediaReference();
        }
        else
        {
            this.relatedMediaReferenceBean = templateRelatedMediaReference.getRelatedMediaReferenceBean();
        }
    }


    /**
     * Return the bean that contains the properties.
     *
     * @return related media reference bean
     */
    protected  RelatedMediaReference  getRelatedMediaReferenceBean()
    {
        return relatedMediaReferenceBean;
    }


    /**
     * Return the type of media referenced.
     *
     * @return RelatedMediaType
     */
    public RelatedMediaType getMediaType() { return relatedMediaReferenceBean.getMediaType(); }


    /**
     * Return the list of recommended usage for the related media.  Null means no usage guidance is available.
     *
     * @return List of RelatedMediaUsage
     */
    public List<RelatedMediaUsage> getMediaUsageList()
    {
        List<RelatedMediaUsage>  mediaUsageList = relatedMediaReferenceBean.getMediaUsageList();

        if (mediaUsageList == null)
        {
            return null;
        }
        else if (mediaUsageList.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(mediaUsageList);
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return relatedMediaReferenceBean.toString();
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
        AssetRelatedMediaReference that = (AssetRelatedMediaReference) objectToCompare;
        return Objects.equals(relatedMediaReferenceBean, that.relatedMediaReferenceBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relatedMediaReferenceBean);
    }
}