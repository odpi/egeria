/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AssetSummary holds asset properties that are used for displaying details of
 * an asset in summary lists or hover text.  It includes the following properties:
 * <ul>
 *     <li>type - metadata type information for the asset</li>
 *     <li>guid - globally unique identifier for the asset</li>
 *     <li>url - external link for the asset</li>
 *     <li>qualifiedName - The official (unique) name for the asset. This is often defined by the IT systems
 *     management organization and should be used (when available) on audit logs and error messages.
 *     (Sourced from qualifiedName from Referenceable - model 0010)</li>
 *     <li>displayName - A consumable name for the asset.  Often a shortened form of the asset's qualifiedName
 *     for use on user interfaces and messages.   The asset's displayName should be only be used for audit logs and error
 *     messages if the qualifiedName is not set. (Sourced from displayName attribute name within Asset - model 0010)</li>
 *     <li>shortDescription - short description about the asset.
 *     (Sourced from assetSummary within ConnectionsToAsset - model 0205)</li>
 *     <li>description - full description of the asset.
 *     (Sourced from attribute description within Asset - model 0010)</li>
 *     <li>owner - name of the person or organization that owns the asset.
 *     (Sourced from attribute owner within AssetOwnershipClassification - model 0445)</li>
 *     <li>zoneMembership - list of governance zones assigned to the asset</li>
 *     <li>classifications - list of classifications assigned to the asset</li>
 * </ul>
 */
public class AssetSummary extends AssetDescriptor
{
    private static final long     serialVersionUID = 1L;

    /**
     * Default constructor only for subclasses
     */
    protected AssetSummary()
    {
        super();
    }


    /**
     * Bean constructor - initializes AssetSummary using a bean returned by the REST interface.
     *
     * @param assetBean asset properties bean
     */
    public AssetSummary(Asset assetBean)
    {
        super(assetBean);
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param templateAssetSummary template values for asset summary
     */
    public AssetSummary(AssetSummary templateAssetSummary)
    {
        /*
         * Initialize super class
         */
        super(templateAssetSummary);
    }


    /**
     * Return the element type properties for this asset.  These values are set up by the metadata repository
     * and define details to the metadata entity used to represent this element.
     *
     * @return AssetElementType type information.
     */
    public AssetElementType getType()
    {
        ElementType elementType = assetBean.getType();

        if (elementType == null)
        {
            return null;
        }
        else
        {
            return new AssetElementType(elementType);
        }
    }


    /**
     * Return the unique id for the properties object.  Null means no guid is assigned.
     *
     * @return String unique id
     */
    public String getGUID()
    {
        return assetBean.getGUID();
    }


    /**
     * Returns the URL to access the properties object in the metadata repository.
     * If no url is available then null is returned.
     *
     * @return String URL
     */
    public String getURL()
    {
        return assetBean.getURL();
    }


    /**
     * Returns the stored qualified name property for the asset.
     * If no qualified name is provided then null is returned.
     *
     * @return qualifiedName
     */
    public String getQualifiedName()
    {
        return assetBean.getQualifiedName();
    }


    /**
     * Returns the stored display name property for the asset.
     * If no display name is available then null is returned.
     *
     * @return displayName
     */
    public String getDisplayName()
    {
        return assetBean.getDisplayName();
    }


    /**
     * Returns the short description of the asset from relationship with Connection.
     *
     * @return shortDescription String
     */
    public String getShortDescription()
    {
        return assetBean.getShortDescription();
    }


    /**
     * Returns the stored description property for the asset.
     * If no description is provided then null is returned.
     *
     * @return description String
     */
    public String getDescription()
    {
        return assetBean.getDescription();
    }


    /**
     * Returns the name of the owner for this asset.
     *
     * @return owner String
     */
    public String getOwner()
    {
        return assetBean.getOwner();
    }


    /**
     * Return the type of owner stored in the owner property.
     *
     * @return OwnerType enum
     */
    public OwnerType getOwnerType()
    {
        return assetBean.getOwnerType();
    }


    /**
     * Return the names of the zones that this asset is a member of.
     *
     * @return list of zone names
     */
    public List<String> getZoneMembership() { return assetBean.getZoneMembership(); }


    /**
     * Return the list of classifications associated with the asset.
     *
     * @return list of classifications
     */
    public List<AssetClassification> getAssetClassifications()
    {
        List<ElementClassification> classifications = assetBean.getClassifications();

        if (classifications == null)
        {
            return null;
        }
        else
        {
            List<AssetClassification> assetClassifications = new ArrayList<>();

            for (ElementClassification classification : classifications)
            {
                if (classification != null)
                {
                    assetClassifications.add(new AssetClassification(this, classification));
                }
            }

            if (assetClassifications.isEmpty())
            {
                return null;
            }
            else
            {
                return assetClassifications;
            }
        }
    }


    /**
     * Return the properties that characterize where this asset is from.
     *
     * @return map of name value pairs, all strings
     */
    public Map<String, String> getOrigins()
    {
        return assetBean.getOrigin();
    }


    /**
     * Return a short description of the last change to the asset.
     *
     * @return string description
     */
    public String getLatestChange()
    {
        return assetBean.getLatestChange();
    }


    /**
     * Return the set of properties that are specific to the particular type of asset.  The caller is given their
     * own copy of the property object.  The properties are named entityName.attributeName. The values are all strings.
     *
     * @return  asset properties using the name of attributes from the model.
     */
    public Map<String, Object> getExtendedProperties()
    {
        return assetBean.getExtendedProperties();
    }


    /**
     * Return the set of additional properties.
     *
     * @return AdditionalProperties object.
     */
    public AdditionalProperties getAdditionalProperties()
    {
        Map<String, String>   additionalProperties = assetBean.getAdditionalProperties();

        if (additionalProperties == null)
        {
            return null;
        }
        else
        {
            return new AdditionalProperties(this, additionalProperties);
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
        return assetBean.toString();
    }
}