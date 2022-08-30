/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.AssetProperties;

import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * Asset holds asset properties that are used for displaying details of
 * an asset in summary lists or hover text.  It includes the following properties:
 * <ul>
 *     <li>qualifiedName - The official (unique) name for the asset. This is often defined by the IT systems
 *     management organization and should be used (when available) on audit logs and error messages.
 *     (qualifiedName from Referenceable - model 0010)</li>
 *     <li>displayName - A consumable name for the asset.  Often a shortened form of the assetQualifiedName
 *     for use on user interfaces and messages.   The assetDisplayName should only be used for audit logs and error
 *     messages if the assetQualifiedName is not set. (Sourced from attribute name within Asset - model 0010)</li>
 *     <li>shortDescription - short description about the asset.
 *     (Sourced from assetSummary within ConnectionsToAsset - model 0205)</li>
 *     <li>description - full description of the asset.
 *     (Sourced from attribute description within Asset - model 0010)</li>
 *     <li>owner - name of the person or organization that owns the asset.
 *     (Sourced from attribute owner within Asset - model 0010)</li>
 *     <li>ownerCategory - type of the person or organization that owns the asset.
 *     (Sourced from classification AssetOwnership attached to Asset - model 0445)</li>
 *     <li>zoneMembership - name of the person or organization that owns the asset.
 *     (Sourced from classification AssetZoneMemberShip attached to Asset - model 0424)</li>
 *     <li>origin - origin identifiers describing the source of the asset.
 *     (Sourced from classification AssetOrigin attached to Asset - model 0440)</li>
 *     <li>classifications - list of all classifications assigned to the asset</li>
 *     <li>extendedProperties - list of properties assigned to the asset from the Asset subclasses</li>
 *     <li>additionalProperties - list of properties assigned to the asset as additional properties</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetElement implements MetadataElement, Serializable
{
    private static final long     serialVersionUID = 1L;

    private ElementHeader   elementHeader = null;
    private AssetProperties properties    = null;
    

    /**
     * Default constructor
     */
    public AssetElement()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public AssetElement(AssetElement template)
    {
       if (template != null)
        {
            this.elementHeader = template.getElementHeader();
            this.properties = template.getProperties();
        }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    @Override
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    @Override
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the description of the asset.
     *
     * @return list of external references
     */
    public AssetProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the description of the asset.
     *
     * @param properties of external references
     */
    public void setProperties(AssetProperties properties)
    {
        this.properties = properties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "AssetElement{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        AssetElement that = (AssetElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties, elementHeader);
    }
}