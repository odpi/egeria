/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetCollectionStatus specifies which subset of an asset collection should be returned.  The relationship
 * that links an asset into an asset collection has an start and end date (called the effective dates).  The
 * Asset Consumer OMAS uses the AssetCollectionStatus and the effective dates to determine which assets to return.
 * <ul>
 *     <li>ACTIVE - All of the assets with a current effective dates</li>
 *     <li>PAST - All of the assets that have effective dates in the past.</li>
 *     <li>FUTURE - All of the assets that become effective in the future.</li>
 *     <li>ALL - All of the assets linked to the collection irrespective of their effective dates.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AssetCollectionStatus implements Serializable
{
    ACTIVE   (0,  "Active",   "Order the collection by the qualified name of the assets in the collection."),
    PAST     (1,  "Past",     "Order the collection by the owner of the assets in the collection."),
    FUTURE   (2,  "Future",   "Order the collection by the date that the assets were added to the collection."),
    ALL      (99, "All",      "Order the collection by a named property of the assets in the collection.");

    private static final long serialVersionUID = 1L;

    private int            ordinal;
    private String         name;
    private String         description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     */
    AssetCollectionStatus(int  ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numeric representation of the enumeration.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "AssetCollectionStatus{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
