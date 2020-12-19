/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;


/**
 * The AdditionalProperties class provides support for arbitrary properties to be added to a properties object.
 * It wraps a java.util.Map map object built around HashMap.
 */
public class AdditionalProperties extends AssetPropertyBase
{
    private static final long     serialVersionUID = 1L;

    protected Map<String, String>  additionalProperties = new HashMap<>();


    /**
     * Constructor for a new set of additional properties that are not connected either directly or indirectly to an asset.
     *
     * @param additionalProperties   map of properties for the metadata element.
     */
    public AdditionalProperties(Map<String, String>  additionalProperties)
    {
        this(null, additionalProperties);
    }


    /**
     * Constructor for a new set of additional properties that are connected either directly or indirectly to an asset.
     *
     * @param parentAsset   description of the asset that these additional properties are attached to.
     * @param additionalProperties   map of properties for the metadata element.
     */
    public AdditionalProperties(AssetDescriptor     parentAsset,
                                Map<String, String> additionalProperties)
    {
        super(parentAsset);

        if (additionalProperties != null)
        {
            this.additionalProperties = new HashMap<>(additionalProperties);
        }
    }


    /**
     * Copy/clone Constructor for additional properties that are connected to an asset.
     *
     * @param parentAsset   description of the asset that these additional properties are attached to.
     * @param templateProperties   template object to copy.
     */
    public AdditionalProperties(AssetDescriptor   parentAsset, AdditionalProperties templateProperties)
    {
        super(parentAsset, templateProperties);

        /*
         * An empty properties object is created in the private variable declaration so nothing to do.
         */
        if (templateProperties != null)
        {
            /*
             * Process templateProperties if they are not null
             */
            Iterator<String> propertyNames = templateProperties.getPropertyNames();

            if (propertyNames != null)
            {
                while (propertyNames.hasNext())
                {
                    String newPropertyName = propertyNames.next();
                    String newPropertyValue = templateProperties.getProperty(newPropertyName);

                    additionalProperties.put(newPropertyName, newPropertyValue);
                }
            }
        }
    }


    /**
     * Returns a list of the additional stored properties for the element.
     * If no stored properties are present then null is returned.
     *
     * @return list of additional properties
     */
    public Iterator<String> getPropertyNames()
    {
        return additionalProperties.keySet().iterator();
    }


    /**
     * Returns the requested additional stored property for the element.
     * If no stored property with that name is present then null is returned.
     *
     * @param name   String name of the property to return.
     * @return requested property value.
     */
    public String getProperty(String name)
    {
        return additionalProperties.get(name);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AdditionalProperties{" +
                "additionalProperties=" + additionalProperties +
                ", parentAsset=" + parentAsset +
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
        AdditionalProperties that = (AdditionalProperties) objectToCompare;
        return Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), additionalProperties);
    }
}