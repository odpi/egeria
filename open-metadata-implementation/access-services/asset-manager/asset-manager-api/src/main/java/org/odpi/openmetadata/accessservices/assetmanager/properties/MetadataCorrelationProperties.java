/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MetadataCorrelationProperties describes the common properties used to pass the properties of metadata elements
 * to the metadata repositories (aka properties server).  It includes details of the external source of the
 * metadata and any properties that assists in the mapping of the open metadata elements to the external
 * asset manager's copy.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataCorrelationProperties extends ExternalIdentifierProperties
{
    private String assetManagerGUID = null;
    private String assetManagerName = null;


    /**
     * Default constructor
     */
    public MetadataCorrelationProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public MetadataCorrelationProperties(MetadataCorrelationProperties template)
    {
        super(template);
        if (template != null)
        {
            assetManagerGUID = template.getAssetManagerGUID();
            assetManagerName = template.getAssetManagerName();
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public MetadataCorrelationProperties(ExternalIdentifierProperties template)
    {
        super(template);
    }


    /**
     * Return the unique identifier of the software server capability that represents the asset manager.
     *
     * @return string guid
     */
    public String getAssetManagerGUID()
    {
        return assetManagerGUID;
    }


    /**
     * Set up the unique identifier of the software server capability that represents the asset manager.
     *
     * @param assetManagerGUID string guid
     */
    public void setAssetManagerGUID(String assetManagerGUID)
    {
        this.assetManagerGUID = assetManagerGUID;
    }


    /**
     * Return the qualified name of the software server capability that represents the asset manager.
     *
     * @return string name
     */
    public String getAssetManagerName()
    {
        return assetManagerName;
    }


    /**
     * Set up the qualified name of the software server capability that represents the asset manager.
     *
     * @param assetManagerName string name
     */
    public void setAssetManagerName(String assetManagerName)
    {
        this.assetManagerName = assetManagerName;
    }

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MetadataCorrelationProperties{" +
                       "synchronizationDirection=" + getSynchronizationDirection() +
                       ", synchronizationDescription='" + getSynchronizationDescription() + '\'' +
                       ", externalIdentifier='" + getExternalIdentifier() + '\'' +
                       ", externalIdentifierName='" + getExternalIdentifierName() + '\'' +
                       ", externalIdentifierUsage='" + getExternalIdentifierUsage() + '\'' +
                       ", externalIdentifierSource='" + getExternalIdentifierSource() + '\'' +
                       ", keyPattern=" + getKeyPattern() +
                       ", mappingProperties=" + getMappingProperties() +
                       ", assetManagerGUID='" + assetManagerGUID + '\'' +
                       ", assetManagerName='" + assetManagerName + '\'' +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        MetadataCorrelationProperties that = (MetadataCorrelationProperties) objectToCompare;
        return Objects.equals(assetManagerGUID, that.assetManagerGUID) && Objects.equals(assetManagerName, that.assetManagerName);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), assetManagerGUID, assetManagerName);
    }
}
