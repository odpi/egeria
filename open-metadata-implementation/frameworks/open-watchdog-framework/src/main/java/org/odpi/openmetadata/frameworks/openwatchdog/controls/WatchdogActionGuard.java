/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openwatchdog.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.GuardType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CompletionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * The WatchdogActionGuard enum describes the standard guards that are automatically set up for all
 * watchdog action services.
 */
public enum WatchdogActionGuard
{
    MONITORING_COMPLETED("monitoring-completed", CompletionStatus.ACTIONED, "The monitoring completed successfully."),
    MONITORING_INVALID("monitoring-invalid", CompletionStatus.INVALID, "The monitoring watchdog did not run because the supplied information (such as the notification type) is invalid for this type of watchdog."),
    MONITORING_FAILED("monitoring-failed", CompletionStatus.FAILED, "An unexpected error occurred during the monitoring process.  The notification process is incomplete."),

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
    WatchdogActionGuard(String name, CompletionStatus completionStatus, String description)
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
     * Return details of the guards used on a simple survey.  These guards are set automatically if the survey does not
     * set a guard itself.
     *
     * @return guard types
     */
    public static List<GuardType> getSimpleWatchdogGuardTypes()
    {
        List<GuardType> guardTypes = new ArrayList<>();

        guardTypes.add(MONITORING_COMPLETED.getGuardType());
        guardTypes.add(MONITORING_INVALID.getGuardType());
        guardTypes.add(MONITORING_FAILED.getGuardType());

        return guardTypes;
    }



    /**
     * Return the details of a specific guard.
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
        return "WatchdogActionGuard{ name='" + name + "}";
    }
}
