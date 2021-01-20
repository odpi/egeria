/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.MapSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.Objects;


/**
 * AssetMapSchemaType describes a schema element of type map.  It stores the type of schema element for the domain
 * (eg property name) for the map and the schema element for the range (eg property value) for the map.
 */
public class AssetMapSchemaType extends AssetSchemaType
{
    private static final long     serialVersionUID = 1L;

    protected MapSchemaType   mapSchemaTypeBean = null;


    /**
     * Constructor used by the subclasses
     *
     * @param parentAsset descriptor of asset that this property relates to.
     */
    protected AssetMapSchemaType(AssetDescriptor parentAsset)
    {
        super(parentAsset);
    }


    /**
     * Constructor used by the subclasses
     *
     * @param parentAsset descriptor of asset that this property relates to.
     * @param schemaType bean containing the schema properties
     */
    public AssetMapSchemaType(AssetDescriptor parentAsset,
                              MapSchemaType   schemaType)
    {
        super(parentAsset, schemaType);
        mapSchemaTypeBean = schemaType;
    }


    /**
     * Bean constructor
     *
     * @param schemaType bean containing the schema properties
     */
    public AssetMapSchemaType(MapSchemaType   schemaType)
    {
        super(schemaType);

        mapSchemaTypeBean = schemaType;
    }

    /**
     * Copy/clone Constructor the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this map is attached to.
     * @param template template object to copy.
     */
    public AssetMapSchemaType(AssetDescriptor parentAsset, AssetMapSchemaType template)
    {
        super(parentAsset, template);

        if (template != null)
        {
            mapSchemaTypeBean = template.getMapSchemaTypeBean();
        }
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return map element bean
     */
    protected MapSchemaType getMapSchemaTypeBean()
    {
        return mapSchemaTypeBean;
    }



    /**
     * Return the type of schema element that represents the key or property name for the map.
     * This is also called the domain of the map.
     *
     * @return SchemaElement
     */
    public AssetSchemaType getMapFromElement()
    {
        if (mapSchemaTypeBean == null)
        {
            return null;
        }
        else
        {
            return AssetSchemaType.createAssetSchemaType(parentAsset, mapSchemaTypeBean.getMapFromElement());
        }
    }


    /**
     * Set up the bean that contains the properties of the schema.
     *
     * @param bean bean containing the schema properties
     */
    protected void  setBean(MapSchemaType bean)
    {
        super.setBean(bean);
        mapSchemaTypeBean = bean;
    }


    /**
     * Return the type of schema element that represents the property value for the map.
     * This is also called the range of the map.
     *
     * @return SchemaElement
     */
    public AssetSchemaType getMapToElement()
    {
        if (mapSchemaTypeBean == null)
        {
            return null;
        }
        else
        {
            return AssetSchemaType.createAssetSchemaType(parentAsset, mapSchemaTypeBean.getMapToElement());
        }
    }


    /**
     * Return a clone of this schema element.  This method is needed because AssetSchemaType
     * is abstract.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @return An instance of the this object's subclass
     */
    @Override
    protected AssetSchemaType cloneAssetSchemaType(AssetDescriptor parentAsset)
    {
        return new AssetMapSchemaType(parentAsset, this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetMapSchemaType{" +
                "parentAsset=" + parentAsset +
                ", mapFromElement=" + getMapFromElement() +
                ", mapToElement=" + getMapToElement() +
                ", versionNumber='" + getVersionNumber() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", formula='" + getFormula() + '\'' +
                ", queries=" + getQueries() +
                ", deprecated=" + isDeprecated() +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", meanings=" + getMeanings() +
                ", securityTags=" + getSecurityTags() +
                ", additionalProperties=" + getAdditionalProperties() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
                ", assetClassifications=" + getAssetClassifications() +
                ", extendedProperties=" + getExtendedProperties() +
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
        if (!(objectToCompare instanceof AssetMapSchemaType))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetMapSchemaType that = (AssetMapSchemaType) objectToCompare;
        return  Objects.equals(getMapFromElement(), that.getMapFromElement()) &&
                Objects.equals(getMapToElement(), that.getMapToElement());
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMapFromElement(), getMapToElement());
    }
}