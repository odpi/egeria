/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.GuardType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * The guards produced by the AnchorsZoneResynchronizer governance action service.
 */
public enum AnchorsZoneResynchronizerGuard
{
    ZONES_CORRECTED("anchors-zones-corrected", CompletionStatus.ACTIONED, "The governance zones in one or more Anchors classifications were out of date and have been corrected."),
    ZONES_UP_TO_DATE("anchors-zones-up-to-date", CompletionStatus.ACTIONED, "The governance zones in the Anchors classifications that were checked were already correct."),
    RESYNC_FAILED("anchors-zones-resync-failed", CompletionStatus.FAILED, "Some of the Anchors classifications could not be checked or corrected; the corrections that could be made were saved."),

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
    AnchorsZoneResynchronizerGuard(String name, CompletionStatus completionStatus, String description)
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

        for (AnchorsZoneResynchronizerGuard guard : AnchorsZoneResynchronizerGuard.values())
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
        return "AnchorsZoneResynchronizerGuard{ name='" + name + "}";
    }
}
