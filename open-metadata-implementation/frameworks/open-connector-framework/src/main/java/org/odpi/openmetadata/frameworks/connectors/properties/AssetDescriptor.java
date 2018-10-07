/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;

/**
 * This is the base class for a connected asset.  It is passed to all of the embedded property objects so the name
 * and type can be used for error messages and other diagnostics.  It also carries the URL of the asset in the
 * metadata repository where this is known to enable properties to be retrieved on request.
 */
public abstract class AssetDescriptor extends AssetPropertyElementBase
{
    protected   Asset     assetBean;

    /*
     * Derived name and type for use by nested property object for messages/debug.  If these default values
     * are seen it is a sign that the asset properties are not being populated from the metadata repository.
     */
    private String assetName = "<Unknown>";
    private String assetTypeName = "<Unknown>";


    /**
     * Default constructor only for subclasses
     */
    protected AssetDescriptor()
    {
        super();
    }


    /**
     * Bean constructor - the asset descriptor is effectively empty - and the protected
     * set methods need to be called to add useful content to it.
     *
     * @param assetBean bean containing all of the properties
     */
    public AssetDescriptor(Asset assetBean)
    {
        super();

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

            ElementType  elementType = assetBean.getType();

            if (elementType != null)
            {
                String typeName = elementType.getElementTypeName();

                if ((typeName != null) && (! typeName.equals("")))
                {
                    assetTypeName = typeName;
                }
            }
        }
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
            this.assetBean = templateAssetDescriptor.getAssetBean();
            this.assetName = templateAssetDescriptor.getAssetName();
            this.assetTypeName = templateAssetDescriptor.getAssetTypeName();
        }
        else
        {
            this.assetBean = new Asset();
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
}