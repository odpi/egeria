/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttributeRelationship;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *     SchemaAttribute describes a single attribute within a schema.  The attribute has a name, order in the
 *     schema and cardinality.
 *     Its type is another SchemaElement (either StructSchemaType, MapSchemaType or PrimitiveSchemaType).
 * </p>
 * <p>
 *     If it is a PrimitiveSchemaType it may have an override for the default value within.
 * </p>
 */
public class AssetSchemaAttribute extends AssetReferenceable
{
    protected SchemaAttribute schemaAttributeBean;
    protected AssetSchemaType localSchemaType    = null;
    protected AssetSchemaLink externalSchemaLink = null;


    /**
     * Bean constructor used by subclasses
     *
     * @param schemaAttributeBean bean containing all of the properties
     */
    protected AssetSchemaAttribute(SchemaAttribute schemaAttributeBean)
    {
        super(schemaAttributeBean);

        if (schemaAttributeBean == null)
        {
            this.schemaAttributeBean = new SchemaAttribute();
        }
        else
        {
            this.schemaAttributeBean = schemaAttributeBean;
        }
    }


    /**
     * Bean constructor with fully constructed local schema type.
     *
     * @param schemaAttributeBean bean containing all of the properties
     * @param localSchemaType type for this schema attribute
     */
    public AssetSchemaAttribute(SchemaAttribute schemaAttributeBean,
                                AssetSchemaType localSchemaType)
    {
        super(schemaAttributeBean);

        if (schemaAttributeBean == null)
        {
            this.schemaAttributeBean = new SchemaAttribute();
        }
        else
        {
            this.schemaAttributeBean = schemaAttributeBean;
        }

        this.localSchemaType = localSchemaType;
    }


    /**
     * Bean constructor with fully constructed link to external schema type.
     *
     * @param schemaAttributeBean bean containing all of the properties
     * @param externalSchemaLink indirect link to the type for this schema attribute
     */
    public AssetSchemaAttribute(SchemaAttribute schemaAttributeBean,
                                AssetSchemaLink externalSchemaLink)
    {
        super(schemaAttributeBean);

        if (schemaAttributeBean == null)
        {
            this.schemaAttributeBean = new SchemaAttribute();
        }
        else
        {
            this.schemaAttributeBean = schemaAttributeBean;
        }

        this.externalSchemaLink = externalSchemaLink;
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset description of the asset that this schema attribute is attached to.
     * @param schemaAttributeBean bean containing all of the properties
     */
    protected AssetSchemaAttribute(AssetDescriptor parentAsset,
                                   SchemaAttribute schemaAttributeBean)
    {
        super(parentAsset, schemaAttributeBean);

        if (schemaAttributeBean == null)
        {
            this.schemaAttributeBean = new SchemaAttribute();
        }
        else
        {
            this.schemaAttributeBean = schemaAttributeBean;
        }
    }


    /**
     * Bean constructor with parent asset and fully constructed local schema type.
     *
     * @param parentAsset description of the asset that this schema attribute is attached to.
     * @param schemaAttributeBean bean containing all of the properties
     * @param localSchemaType type for this schema attribute
     */
    public AssetSchemaAttribute(AssetDescriptor parentAsset,
                                SchemaAttribute schemaAttributeBean,
                                AssetSchemaType localSchemaType)
    {
        super(parentAsset, schemaAttributeBean);

        if (schemaAttributeBean == null)
        {
            this.schemaAttributeBean = new SchemaAttribute();
        }
        else
        {
            this.schemaAttributeBean = schemaAttributeBean;
        }

        this.localSchemaType = localSchemaType;
    }


    /**
     * Bean constructor with parent asset and fully constructed link to external schema type.
     *
     * @param parentAsset description of the asset that this schema attribute is attached to.
     * @param schemaAttributeBean bean containing all of the properties
     * @param externalSchemaLink indirect link to the type for this schema attribute
     */
    public AssetSchemaAttribute(AssetDescriptor parentAsset,
                                SchemaAttribute schemaAttributeBean,
                                AssetSchemaLink externalSchemaLink)
    {
        super(parentAsset, schemaAttributeBean);

        if (schemaAttributeBean == null)
        {
            this.schemaAttributeBean = new SchemaAttribute();
        }
        else
        {
            this.schemaAttributeBean = schemaAttributeBean;
        }

        this.externalSchemaLink = externalSchemaLink;
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset description of the asset that this schema attribute is attached to.
     * @param template template schema attribute to copy.
     */
    public AssetSchemaAttribute(AssetDescriptor parentAsset, AssetSchemaAttribute template)
    {
        super(parentAsset, template);

        if (template == null)
        {
            this.schemaAttributeBean = new SchemaAttribute();
        }
        else
        {
            this.schemaAttributeBean = template.getSchemaAttributeBean();
            this.localSchemaType = template.getLocalSchemaType();
            this.externalSchemaLink = template.getExternalSchemaLink();

        }
    }


    /**
     * Returns a clone of this object.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @return clone of this object
     */
    public AssetSchemaAttribute cloneAssetSchemaAttribute(AssetDescriptor parentAsset)
    {
        return new AssetSchemaAttribute(parentAsset, this);
    }


    /**
     * Return the bean with all of the properties.
     *
     * @return schema attribute bean
     */
    protected SchemaAttribute getSchemaAttributeBean()
    {
        return schemaAttributeBean;
    }


    /**
     * Return the name of this schema attribute.
     *
     * @return String attribute name
     */
    public String getAttributeName() { return schemaAttributeBean.getAttributeName(); }


    /**
     * Return the position of this schema attribute in its parent schema.
     *
     * @return int position in schema 0 means first
     */
    public int getElementPosition() { return schemaAttributeBean.getElementPosition(); }


    /**
     * Return the cardinality defined for this schema attribute.
     *
     * @return String cardinality defined for this schema attribute.
     */
    public String getCardinality() { return schemaAttributeBean.getCardinality(); }


    /**
     * Return any default value for this attribute that would override the default defined in the
     * schema element for this attribute's type (note only used is type is primitive).
     *
     * @return String default value override
     */
    public String getDefaultValueOverride() { return schemaAttributeBean.getDefaultValueOverride(); }


    /**
     * Return the SchemaType that relates to the type of this attribute.
     *
     * @return SchemaElement
     */
    public AssetSchemaType getLocalSchemaType()
    {
        if (localSchemaType == null)
        {
            return null;
        }
        else
        {
            return localSchemaType.cloneAssetSchemaType(super.getParentAsset());
        }
    }


    /**
     * Return the SchemaType that relates to the type of this attribute.
     *
     * @return SchemaElement
     */
    public AssetSchemaLink getExternalSchemaLink()
    {
        if (externalSchemaLink == null)
        {
            return null;
        }
        else
        {
            return new AssetSchemaLink(super.getParentAsset(), externalSchemaLink);
        }
    }


    /**
     * Return any relationships to other schema attributes.
     *
     * @return list of attribute relationships
     */
    public List<SchemaAttributeRelationship> getAttributeRelationships()
    {
        return schemaAttributeBean.getAttributeRelationships();
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetSchemaAttribute{" +
                "schemaAttributeBean=" + schemaAttributeBean +
                ", localSchemaType=" + localSchemaType +
                ", externalSchemaLink=" + externalSchemaLink +
                ", parentAsset=" + parentAsset +
                ", attributeName='" + getAttributeName() + '\'' +
                ", elementPosition=" + getElementPosition() +
                ", cardinality='" + getCardinality() + '\'' +
                ", defaultValueOverride='" + getDefaultValueOverride() + '\'' +
                ", attributeRelationships=" + getAttributeRelationships() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
                ", assetClassifications=" + getAssetClassifications() +
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
        if (!(objectToCompare instanceof AssetSchemaAttribute))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetSchemaAttribute that = (AssetSchemaAttribute) objectToCompare;
        return Objects.equals(getSchemaAttributeBean(), that.getSchemaAttributeBean()) &&
                Objects.equals(localSchemaType, that.localSchemaType);
    }
}