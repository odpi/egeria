/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;

import java.util.Objects;

/**
 * The AssetElementType provides details of the type information associated with the element.  Most consumers
 * of the properties do not need this information.  It is provided to asset consumers primarily as diagnostic
 * information.
 */
public class AssetElementType extends AssetPropertyElementBase
{
    private static final long     serialVersionUID = 1L;

    private ElementType elementTypeBean;

    /**
     * Bean constructor accepts bean version of the AssetElementType to provide the values
     *
     * @param elementTypeBean bean containing properties
     */
    public AssetElementType(ElementType elementTypeBean)
    {
        super();

        if (elementTypeBean == null)
        {
            this.elementTypeBean = new ElementType();
        }
        else
        {
            this.elementTypeBean = elementTypeBean;
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param templateType type to clone
     */
    public AssetElementType(AssetElementType templateType)
    {
        super(templateType);

        if (templateType == null)
        {
            this.elementTypeBean = new ElementType();
        }
        else
        {
            ElementType  elementTypeBean = templateType.getElementTypeBean();

            this.elementTypeBean = elementTypeBean;
        }
    }


    /**
     * Clone the bean as part of the deep clone constructor.
     *
     * @return cloned element bean
     */
    protected ElementType getElementTypeBean()
    {
        return new ElementType(elementTypeBean);
    }


    /**
     * Return unique identifier for the element's type.
     *
     * @return element type id
     */
    public String getElementTypeId()
    {
        return elementTypeBean.getElementTypeId();
    }


    /**
     * Return name of element's type.
     *
     * @return elementTypeName
     */
    public String getElementTypeName()
    {
        return elementTypeBean.getElementTypeName();
    }


    /**
     * Return the version number for this element's type.
     *
     * @return elementTypeVersion version number for the element type.
     */
    public long getElementTypeVersion()
    {
        return elementTypeBean.getElementTypeVersion();
    }


    /**
     * Return the description for this element's type.
     *
     * @return elementTypeDescription String description for the element type
     */
    public String getElementTypeDescription()
    {
        return elementTypeBean.getElementTypeDescription();
    }


    /**
     * Return the URL of the server where the element was retrieved from.  Typically this is
     * a server where the OMAS interfaces are activated.  If no URL is known for the server then null is returned.
     *
     * @return elementSourceServerURL the url of the server where the element came from
     */
    public String getElementSourceServer()
    {
        return elementTypeBean.getElementSourceServer();
    }


    /**
     * Return the origin of the metadata element.
     *
     * @return ElementOrigin enum
     */
    public ElementOrigin getElementOrigin() { return elementTypeBean.getElementOrigin(); }


    /**
     * Returns the unique identifier for the metadata collection that is managed by the repository
     * where the element originates (its home repository).
     *
     * @return String metadata collection id
     */
    public String getElementHomeMetadataCollectionId()
    {
        return elementTypeBean.getElementMetadataCollectionId();
    }


    /**
     * Return the name of the metadata collection that this asset belongs to.
     *
     * @return name string
     */
    public String getElementHomeMetadataCollectionName() { return elementTypeBean.getElementMetadataCollectionName(); }

    /**
     * Return the license associated with this metadata element (null means none).
     *
     * @return string license name
     */
    public String getElementLicense()
    {
        return elementTypeBean.getElementLicense();
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return elementTypeBean.toString();
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
        if (!(objectToCompare instanceof AssetElementType))
        {
            return false;
        }

        AssetElementType that = (AssetElementType) objectToCompare;
        return Objects.equals(getElementTypeBean(), that.getElementTypeBean());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return elementTypeBean.hashCode();
    }
}
