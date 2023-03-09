/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.rest.AssetManagerIdentifiersRequestBody;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AssetConnectionProperties describes the properties used when creating an AssetConnection relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetConnectionProperties extends RelationshipProperties
{
    private static final long    serialVersionUID = 1L;

    private String assetSummary = null;


    /**
     * Default constructor
     */
    public AssetConnectionProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public AssetConnectionProperties(AssetConnectionProperties template)
    {
        super(template);

        if (template != null)
        {
            assetSummary = template.getAssetSummary();
        }
    }


    /**
     * Return the properties for the relationship.
     *
     * @return properties object
     */
    public String getAssetSummary()
    {
        return assetSummary;
    }


    /**
     * Set up the properties for the relationship.
     *
     * @param assetSummary properties object
     */
    public void setAssetSummary(String assetSummary)
    {
        this.assetSummary = assetSummary;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetConnectionProperties{" +
                       "assetSummary='" + assetSummary + '\'' +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
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
        AssetConnectionProperties that = (AssetConnectionProperties) objectToCompare;
        return Objects.equals(getAssetSummary(), that.getAssetSummary());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), assetSummary);
    }
}
