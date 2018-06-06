/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

/**
 * Location describes where the asset is located.  The model allows a very flexible definition of location
 * that can be set up at different levels of granularity.
 */
public class Location extends Referenceable
{
    /*
     * Properties that make up the location of the asset.
     */
    private String displayName = null;
    private String description = null;


    /**
     * Typical constructor
     *
     * @param parentAsset - descriptor for parent asset
     * @param type - details of the metadata type for this properties object
     * @param guid - String - unique id
     * @param url - String - URL
     * @param classifications - enumeration of classifications
     * @param qualifiedName - unique name
     * @param additionalProperties - additional properties for the referenceable object.
     * @param meanings - list of glossary terms (summary)
     * @param displayName - consumable name
     * @param description - description property stored for the location.
     */
    public Location(AssetDescriptor      parentAsset,
                    ElementType          type,
                    String               guid,
                    String               url,
                    Classifications classifications,
                    String               qualifiedName,
                    AdditionalProperties additionalProperties,
                    Meanings meanings,
                    String               displayName,
                    String               description)
    {
        super(parentAsset, type, guid, url, classifications, qualifiedName, additionalProperties, meanings);

        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Copy/clone constructor
     *
     * @param parentAsset - description of the asset that this location is attached to.
     * @param templateLocation - template object to copy.
     */
    public Location(AssetDescriptor  parentAsset, Location templateLocation)
    {
        super(parentAsset, templateLocation);
        if (templateLocation != null)
        {
            displayName = templateLocation.getDisplayName();
            description = templateLocation.getDescription();
        }
    }


    /**
     * Returns the stored display name property for the location.
     * If no display name is available then null is returned.
     *
     * @return displayName
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Returns the stored description property for the location.
     * If no description is provided then null is returned.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Location{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", meanings=" + meanings +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}