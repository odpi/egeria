/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.frameworks.opengovernance.controls.Guard;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.GuardType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * OwnerSetterGuard describes the guards produced by the Owner Setter Governance Action Service.
 */
public enum OwnerSetterGuard
{
    OWNER_ASSIGNED("owner-assigned", CompletionStatus.ACTIONED, "The requested owner has been set in the Ownership classification of the action target element(s)."),
    MISSING_OWNER("missing-owner", CompletionStatus.INVALID, "No owner has been supplied in either the configuration properties or the request parameters."),
    INVALID_ACTION_TARGET_TYPE(Guard.INVALID_ACTION_TARGET_TYPE.getName(),
                               Guard.INVALID_ACTION_TARGET_TYPE.getCompletionStatus(),
                               Guard.INVALID_ACTION_TARGET_TYPE.getDescription()),
    NO_TARGETS(Guard.NO_TARGETS_DETECTED.getName(),
               Guard.NO_TARGETS_DETECTED.getCompletionStatus(),
               Guard.NO_TARGETS_DETECTED.getDescription()),
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
    OwnerSetterGuard(String name, CompletionStatus completionStatus, String description)
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

        for (OwnerSetterGuard guard : OwnerSetterGuard.values())
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
        return "OwnerSetterGuard{ name='" + name + "}";
    }
}
