/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.metadataelements;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.communityprofile.properties.AssetProperties;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetCollectionMember describes an asset that is a member of an individual's my-assets collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetCollectionMember extends CollectionMemberHeader
{
    private static final long    serialVersionUID = 1L;

    private AssetProperties properties = null;


    /**
     * Default constructor
     */
    public AssetCollectionMember()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetCollectionMember(AssetCollectionMember template)
    {
        super(template);

        if (template != null)
        {
            properties = template.getProperties();
        }
    }


    /**
     * Return the properties of the asset.
     *
     * @return properties
     */
    public AssetProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the asset properties.
     *
     * @param properties  properties
     */
    public void setProperties(AssetProperties properties)
    {
        this.properties = properties;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetCollectionMember{" +
                       "properties=" + properties +
                       ", elementHeader=" + getElementHeader() +
                       ", dateAddedToCollection=" + getDateAddedToCollection() +
                       ", membershipRationale='" + getMembershipRationale() + '\'' +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        AssetCollectionMember that = (AssetCollectionMember) objectToCompare;
        return Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties);
    }
}
