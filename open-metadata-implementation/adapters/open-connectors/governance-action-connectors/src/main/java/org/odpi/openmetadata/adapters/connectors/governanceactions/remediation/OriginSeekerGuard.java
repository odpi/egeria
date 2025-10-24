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
public enum OriginSeekerGuard
{
    ORIGIN_ASSIGNED("origin-assigned", CompletionStatus.ACTIONED, "The AssetOrigin classification has been successfully assigned to the asset."),
    ORIGIN_ALREADY_ASSIGNED("origin-already-assigned", CompletionStatus.ACTIONED, "The asset already has an AssetOrigin classification so no action was required."),
    MULTIPLE_ORIGINS_DETECTED("multiple-origins-detected", CompletionStatus.INVALID, "The lineage graph showed that the data in the asset came from multiple origins.  A steward needs to assign the AssetOrigin because it is not obvious which values to use."),
    NO_ORIGINS_DETECTED( "no-origins-detected", CompletionStatus.INVALID, "None of the assets in the lineage graph have an AssetOrigin classification."),
    ORIGIN_SEEKING_FAILED("origin-seeking-failed", CompletionStatus.FAILED, "An unexpected error occurred during the origin seeking process.  Check the audit log for reported errors."),

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
    OriginSeekerGuard(String name, CompletionStatus completionStatus, String description)
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

        for (OriginSeekerGuard guard : OriginSeekerGuard.values())
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
