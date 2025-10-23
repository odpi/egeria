/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.GuardType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CompletionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * The Guard enum describes some common guards that can be used when implementing governance services.
 * Using the common guard names where possible saves time when coding the provider class and improves the
 * ability of the service to be incorporated into governance action processes.
 */
public enum ZonePublisherGuard
{
    ZONE_ASSIGNED("zones-assigned", CompletionStatus.ACTIONED,  "The requested published zones have been added to the AssetZoneMembership classification for the asset."),
    NO_ZONES_DETECTED("no-zones-detected", CompletionStatus.ACTIONED, "The requested published zones are null so any AssetZoneMembership classification attached to the asset has been removed so the asset is now visible to all."),
    ZONE_PUBLISHING_FAILED("zone-publishing-failed", CompletionStatus.FAILED, "An unexpected exception occurred while assigning zones to the action target asset(s)."),
    ;


    public final String           name;
    public final CompletionStatus completionStatus;
    public final String           description;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the guard
     * @param completionStatus associated completion status
     * @param description description of the guard
     */
    ZonePublisherGuard(String name, CompletionStatus completionStatus, String description)
    {
        this.name             = name;
        this.completionStatus = completionStatus;
        this.description      = description;
    }


    /**
     * Return the name of the guard.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the typical completion status used with this guard.
     *
     * @return completion status
     */
    public CompletionStatus getCompletionStatus()
    {
        return completionStatus;
    }


    /**
     * Return the description of the guard.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return details of all defined guards.
     *
     * @return guard types
     */
    public static List<GuardType> getGuardTypes()
    {
        List<GuardType> guardTypes = new ArrayList<>();

        for (ZonePublisherGuard guard : ZonePublisherGuard.values())
        {
            GuardType guardType = new GuardType();

            guardType.setName(guard.getName());
            guardType.setDescription(guard.getDescription());
            guardType.setCompletionStatus(guard.getCompletionStatus());

            guardTypes.add(guardType);
        }

        return guardTypes;
    }


    /**
     * Return details of a specific guard.
     *
     * @return guard type
     */
    public GuardType getGuardType()
    {
        GuardType guardType = new GuardType();

        guardType.setName(name);
        guardType.setDescription(description);
        guardType.setCompletionStatus(completionStatus);

        return guardType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "Guard{ name='" + name + "}";
    }
}
