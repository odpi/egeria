/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is the base class for a connected asset.  It is passed to all the embedded property objects so the name
 * and type can be used for error messages and other diagnostics.  It also carries the URL of the asset in the
 * metadata repository where this is known to enable properties to be retrieved on request.
 */
public abstract class AssetDescriptor extends AssetPropertyElementBase
{
    protected Asset assetBean;

    /*
     * Derived name and type for use by nested property object for messages/debug.  If these default values
     * are seen it is a sign that the asset properties are not being populated from the metadata repository.
     */
    private String       assetName = "<Unknown>";
    private String       assetTypeName = "<Unknown>";
    private List<String> assetSuperTypeNames = null;


    /**
     * Default constructor only for subclasses
     */
    protected AssetDescriptor()
    {
        super();

        this.setAssetBean(null);
    }


    /**
     * Bean constructor - the asset descriptor is effectively empty - and the protected
     * set methods need to be called to add useful content to it.
     *
     * @param assetBean bean containing all the properties
     */
    public AssetDescriptor(Asset assetBean)
    {
        super();

        this.setAssetBean(assetBean);
    }


    /**
     * Copy/clone Constructor - used to copy the asset descriptor for a new consumer.
     *
     * @param templateAssetDescriptor template asset descriptor to copy.
     */
    public AssetDescriptor(AssetDescriptor templateAssetDescriptor)
    {
        super();

        if (templateAssetDescriptor != null)
        {
            this.setAssetBean(templateAssetDescriptor.getAssetBean());
            this.assetName = templateAssetDescriptor.getAssetName();
            this.assetTypeName = templateAssetDescriptor.getAssetTypeName();
            this.assetSuperTypeNames = templateAssetDescriptor.getAssetSuperTypeNames();
        }
        else
        {
            this.setAssetBean(null);
        }
    }


    /**
     * Set up private attributes based on the supplied asset bean.
     *
     * @param assetBean bean containing all the properties
     */
    protected void setAssetBean(Asset assetBean)
    {
        if (assetBean == null)
        {
            this.assetBean = new Asset();
        }
        else
        {
            this.assetBean = assetBean;

            String qualifiedName = assetBean.getQualifiedName();
            String displayName = assetBean.getDisplayName();

            /*
             * Use the qualified name as the asset name if it is not null or the empty string.
             * Otherwise use display name (unless it is null or the empty string).
             */
            if ((qualifiedName == null) || (qualifiedName.equals("")))
            {
                if ((displayName != null) && (!displayName.equals("")))
                {
                    /*
                     * Good display name
                     */
                    assetName = displayName;
                }
            }
            else /* good qualified name */
            {
                assetName = qualifiedName;
            }

            ElementType elementType = assetBean.getType();

            if (elementType != null)
            {
                String typeName = elementType.getTypeName();

                if ((typeName != null) && (! typeName.equals("")))
                {
                    assetTypeName = typeName;
                }

                assetSuperTypeNames = elementType.getSuperTypeNames();
            }
        }
    }


    /**
     * Return the asset bean for this element.
     *
     * @return Asset bean
     */
    protected Asset getAssetBean()
    {
        return assetBean;
    }


    /**
     * Return the name of the asset - for use in messages and other diagnostics.
     *
     * @return String asset name
     */
    public String getAssetName()
    {
        return assetName;
    }


    /**
     * Return the name of the asset's type - for use in messages and other diagnostics.
     *
     * @return String asset type name
     */
    public String getAssetTypeName()
    {
        return assetTypeName;
    }


    /**
     * Return the list of type names for this type's supertypes.
     *
     * @return list of type names
     */
    public List<String> getAssetSuperTypeNames()
    {
        if (assetSuperTypeNames == null)
        {
            return null;
        }
        else if (assetSuperTypeNames.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(assetSuperTypeNames);
        }
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
        AssetDescriptor that = (AssetDescriptor) objectToCompare;
        return Objects.equals(assetBean, that.assetBean) &&
                       Objects.equals(assetName, that.assetName) &&
                       Objects.equals(assetTypeName, that.assetTypeName) &&
                       Objects.equals(assetSuperTypeNames, that.assetSuperTypeNames);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), assetBean, assetName, assetTypeName, assetSuperTypeNames);
    }
}