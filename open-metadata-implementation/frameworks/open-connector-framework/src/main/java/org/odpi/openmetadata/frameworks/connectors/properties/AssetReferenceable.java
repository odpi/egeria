/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;

import java.util.Map;
import java.util.Objects;

/**
 * Many open metadata entities are referenceable.  It means that they have a qualified name and additional
 * properties.  In addition the Referenceable class adds support for the parent asset, guid, url and type
 * for the entity through extending ElementHeader.
 */
public class AssetReferenceable extends AssetElementHeader
{
    protected Referenceable referenceableBean;

    /**
     * Bean constructor
     */
    protected AssetReferenceable(Referenceable referenceableBean)
    {
        super(referenceableBean);

        if (referenceableBean == null)
        {
            this.referenceableBean = new Referenceable();
        }
        else
        {
            this.referenceableBean = referenceableBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param referenceableBean bean containing properties
     */
    protected AssetReferenceable(AssetDescriptor      parentAsset,
                                 Referenceable        referenceableBean)
    {
        super(parentAsset, referenceableBean);

        if (referenceableBean == null)
        {
            this.referenceableBean = new Referenceable();
        }
        else
        {
            this.referenceableBean = referenceableBean;
        }
    }


    /**
     * Copy/clone constructor with parent asset.
     *
     * @param parentAsset descriptor for parent asset
     * @param templateReferenceableElement element to copy
     */
    public AssetReferenceable(AssetDescriptor parentAsset, AssetReferenceable templateReferenceableElement)
    {
        /*
         * Save the parent asset description.
         */
        super(parentAsset, templateReferenceableElement);

        if (templateReferenceableElement == null)
        {
            this.referenceableBean = new Referenceable();
        }
        else
        {
            this.referenceableBean = templateReferenceableElement.getReferenceableBean();
        }
    }


    /**
     * Return the bean for this referenceable.
     *
     * @return Referenceable object with all of the properties
     */
    protected Referenceable  getReferenceableBean()
    {
        return referenceableBean;
    }


    /**
     * Returns the stored qualified name property for the metadata entity.
     * If no qualified name is available then the empty string is returned.
     *
     * @return qualifiedName
     */
    public String getQualifiedName()
    {
        return referenceableBean.getQualifiedName();
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public AdditionalProperties getAdditionalProperties()
    {
        Map<String, Object>   additionalProperties = referenceableBean.getAdditionalProperties();

        if (additionalProperties == null)
        {
            return null;
        }
        else
        {
            return new AdditionalProperties(super.getParentAsset(), additionalProperties);
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
        return referenceableBean.toString();
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
        if (!(objectToCompare instanceof AssetReferenceable))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetReferenceable that = (AssetReferenceable) objectToCompare;
        return Objects.equals(getReferenceableBean(), that.getReferenceableBean());
    }
}