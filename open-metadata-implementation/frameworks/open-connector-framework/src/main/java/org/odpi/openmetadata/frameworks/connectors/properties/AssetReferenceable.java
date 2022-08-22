/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Many open metadata entities are referenceable.  It means that they have a qualified name and additional
 * properties.  In addition, the Referenceable class adds support for the parent asset, guid, url and type
 * for the entity through extending ElementBase.  There is also the possibility that referenceable may have
 * meanings (glossary terms) and security tags assigned.
 *
 * Asset meanings are extracted on demand by the caller.
 */
public class AssetReferenceable extends AssetElementHeader
{
    private static final long     serialVersionUID = 1L;

    protected Referenceable referenceableBean;


    /**
     * Constructor used by the subclasses
     */
    protected AssetReferenceable()
    {
        super();
        this.referenceableBean = new Referenceable();
    }


    /**
     * Bean constructor
     *
     * @param referenceableBean bean containing all the properties
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
     * Copy/clone constructor with parent asset.
     *
     * @param template element to copy
     */
    public AssetReferenceable(AssetReferenceable template)
    {
        super(template);

        if (template == null)
        {
            this.referenceableBean = new Referenceable();
        }
        else
        {
            this.referenceableBean = template.getReferenceableBean();
        }
    }


    /**
     * Set up the bean that contains the properties of the referenceable.
     *
     * @param referenceableBean bean containing all the properties
     */
    protected void  setBean(Referenceable referenceableBean)
    {
        super.setBean(referenceableBean);
        this.referenceableBean = referenceableBean;
    }


    /**
     * Return the bean for this referenceable.
     *
     * @return Referenceable object with all the properties
     */
    protected Referenceable getReferenceableBean()
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
    public Map<String, String>  getAdditionalProperties()
    {
        Map<String, String>   additionalProperties = referenceableBean.getAdditionalProperties();

        if (additionalProperties == null)
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetReferenceable that = (AssetReferenceable) objectToCompare;
        return Objects.equals(referenceableBean, that.referenceableBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), referenceableBean);
    }
}