/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.users;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ZoneAccessType enum defines the type of access granted to assets in a particular Governance Zone.
 * <ul>
 *     <li>ReadAssets: The user may read assets in the zone (supportedZones).</li>
 *     <li>UpdateAssets: The user may change the metadata description of the asset.</li>
 *     <li>DeleteAssets: The user may remove the asset from the catalog (archive, soft-delete or purge).</li>
 *     <li>ChangeAssetMembership: The user may update the zone membership of an asset in this zone.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ZoneAccessType
{
    /**
     * The user may read assets in the zone.
     */
    READ_ASSETS             (0,    "ReadAssets",         "The user may read assets in the zone."),

    /**
     * The user may change the metadata description of the asset.
     */
    CREATE_ASSETS           (1,    "CreateAssets",        "The user may create new assets metadata description of the asset."),

    /**
     * The user may change the metadata description of the asset.
     */
    UPDATE_ASSETS           (2,    "UpdateAssets",        "The user may change the metadata description of the asset."),

    /**
     * The user may remove the asset from the catalog (archive, soft-delete or purge).
     */
    DELETE_ASSETS           (3,   "DeleteAssets",          "The user may remove the asset from the catalog (archive, soft-delete or purge)."),

    /**
     * The user may update the zone membership of an asset in this zone.
     */
    CHANGE_ASSET_MEMBERSHIP (4,   "ChangeAssetMembership",  "The user may update the zone membership of an asset in this zone.");


    private  final int    ordinal;
    private  final String name;
    private  final String description;


    /**
     * Default constructor sets up the specific values for an enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param name     String name
     * @param description String description
     */
    ZoneAccessType(int     ordinal,
                   String  name,
                   String  description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numerical value for the enum.
     *
     * @return int enum value ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the descriptive name for the enum.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the description for the enum.
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
        return "UserAccountStatus{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
