/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementVersions;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ElementBase provides the common identifier and type information for all properties objects
 * that link off of the asset and have a guid associated with them.  This typically means it is
 * represented by an entity in the metadata repository.
 */
public abstract class AssetElementHeader extends AssetPropertyElementBase
{
    private static final long     serialVersionUID = 1L;

    private ElementBase elementBaseBean = null;


    /**
     * Constructor used by the subclasses
     */
    protected AssetElementHeader()
    {
        super();
    }


    /**
     * Bean constructor with parent asset
     *
     * @param elementBaseBean bean containing properties
     */
    protected AssetElementHeader(ElementBase elementBaseBean)
    {
        super();

        if (elementBaseBean == null)
        {
            this.elementBaseBean = new ElementBase();
        }
        else
        {
            this.elementBaseBean = elementBaseBean;
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param assetElementHeader element to copy
     */
    protected AssetElementHeader(AssetElementHeader assetElementHeader)
    {
        super(assetElementHeader);

        if (assetElementHeader == null)
        {
            elementBaseBean = new ElementBase();
        }
        else
        {
            this.elementBaseBean = assetElementHeader.getElementHeaderBean();
        }
    }


    /**
     * Set up the bean that contains the properties of the element header.
     *
     * @param elementBaseBean bean containing all the properties
     */
    protected void  setBean(ElementBase elementBaseBean)
    {
        this.elementBaseBean = elementBaseBean;
    }


    /**
     * Return the element header bean - used during cloning
     *
     * @return bean
     */
    protected ElementBase getElementHeaderBean()
    {
        return elementBaseBean;
    }


    /**
     * Return the element type properties for this properties object.  These values are set up by the metadata repository
     * and define details of the metadata entity used to represent this element.
     *
     * @return ElementType type information.
     */
    public ElementType getType()
    {
        if (elementBaseBean == null)
        {
            return null;
        }

        ElementType elementTypeBean = elementBaseBean.getType();

        if (elementTypeBean == null)
        {
            return null;
        }
        else
        {
            return new ElementType(elementTypeBean);
        }
    }


    /**
     * Return the element type properties for this properties object.  These values are set up by the metadata repository
     * and define details of the metadata entity used to represent this element.
     *
     * @return AssetElementOrigin origin information.
     */
    public ElementOrigin getOrigin()
    {
        if (elementBaseBean == null)
        {
            return null;
        }
        else
        {
            return elementBaseBean.getOrigin();
        }
    }


    /**
     * Return the element version properties for this properties object.  These values are set up by the metadata repository
     * and define details of the metadata entity used to represent this element.
     *
     * @return ElementVersions version information.
     */
    public ElementVersions getVersions()
    {
        if (elementBaseBean == null)
        {
            return null;
        }
        else
        {
            return elementBaseBean.getVersions();
        }
    }


    /**
     * Return the unique id for the properties object.  Null means that no guid is assigned.
     *
     * @return String unique id
     */
    public String getGUID()
    {
        if (elementBaseBean == null)
        {
            return null;
        }

        return elementBaseBean.getGUID();
    }


    /**
     * Return the status of the element.
     *
     * @return status enum
     */
    public ElementStatus getStatus()
    {
        if (elementBaseBean == null)
        {
            return null;
        }

        return elementBaseBean.getStatus();
    }


    /**
     * Returns the URL to access the properties object in the metadata repository.
     * If no url is available then null is returned.
     *
     * @return String URL
     */
    public String getURL()
    {
        if (elementBaseBean == null)
        {
            return null;
        }

        return elementBaseBean.getURL();
    }


    /**
     * Return the list of classifications associated with the asset.
     *
     * @return Classifications  list of classifications
     */
    public List<ElementClassification> getClassifications()
    {
        if (elementBaseBean == null)
        {
            return null;
        }

        List<ElementClassification> classifications = elementBaseBean.getClassifications();

        if (classifications == null)
        {
            return null;
        }
        else if (classifications.isEmpty())
        {
            return null;
        }
        else
        {
            return classifications;
        }
    }


    /**
     * Return any properties defined for a subtype that are not explicitly supported by the connected
     * asset API.
     *
     * @return property map
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (elementBaseBean == null)
        {
            return null;
        }

        return elementBaseBean.getExtendedProperties();
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        if (elementBaseBean == null)
        {
            return new ElementBase().toString();
        }

        return elementBaseBean.toString();
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
        if (!(objectToCompare instanceof AssetElementHeader))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetElementHeader that = (AssetElementHeader) objectToCompare;
        return Objects.equals(getElementHeaderBean(), that.getElementHeaderBean());
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        if (elementBaseBean == null)
        {
            return new ElementBase().hashCode();
        }

        return elementBaseBean.hashCode();
    }
}