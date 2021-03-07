/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DataAssetProperties is a java bean used to create assets associated with the external asset manager.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataAssetProperties extends AssetProperties
{
    private static final long     serialVersionUID = 1L;

    private boolean isReferenceAsset = false;


    /**
     * Default constructor
     */
    public DataAssetProperties()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public DataAssetProperties(DataAssetProperties template)
    {
        super(template);

        if (template != null)
        {
            isReferenceAsset = template.getIsReferenceAsset();
        }
    }


    /**
     * Return whether this being used as a reference data set.
     *
     * @return boolean flag
     */
    public boolean getIsReferenceAsset()
    {
        return isReferenceAsset;
    }


    /**
     * Set up whether this asset is being used as a reference data set.
     *
     * @param referenceAsset boolean flag
     */
    public void setIsReferenceAsset(boolean referenceAsset)
    {
        isReferenceAsset = referenceAsset;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataAssetProperties{" +
                       "isReferenceAsset=" + isReferenceAsset +
                       ", technicalName='" + getTechnicalName() + '\'' +
                       ", technicalDescription='" + getTechnicalDescription() + '\'' +
                       ", owner='" + getOwner() + '\'' +
                       ", ownerCategory=" + getOwnerCategory() +
                       ", zoneMembership=" + getZoneMembership() +
                       ", originOrganizationGUID='" + getOriginOrganizationGUID() + '\'' +
                       ", originBusinessCapabilityGUID='" + getOriginBusinessCapabilityGUID() + '\'' +
                       ", otherOriginValues=" + getOtherOriginValues() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", summary='" + getSummary() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", abbreviation='" + getAbbreviation() + '\'' +
                       ", usage='" + getUsage() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DataAssetProperties that = (DataAssetProperties) objectToCompare;
        return isReferenceAsset == that.isReferenceAsset;
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), isReferenceAsset);
    }
}
