/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.frameworks.opengovernance.controls.GuardType;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CompletionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * The Guard enum describes some common guards that can be used when implementing governance services.
 * Using the common guard names where possible saves time when coding the provider class and improves the
 * ability of the service to be incorporated into governance action processes.
 */
public enum QualifiedNamePeerDuplicateGuard
{
    DUPLICATE_ASSIGNED("duplicate-detected", CompletionStatus.ACTIONED, "A new duplicate was detected and linked to the element."),
    DUPLICATE_ALREADY_ASSIGNED("duplicate-already-assigned", CompletionStatus.ACTIONED, "This element already is linked to its duplicate(s)."),
    NO_DUPLICATION_DETECTED("no-duplication-detected", CompletionStatus.ACTIONED, "No duplicates were detected."),
    DUPLICATE_DETECTION_FAILED("duplicate-detection-failed", CompletionStatus.FAILED, "An unexpected error occurred during duplicate detection."),
    NO_TARGETS_DETECTED("no-targets-detected", CompletionStatus.INVALID, "There is no supplied action target and so the governance service does not know which element to work on."),
    MULTIPLE_TARGETS_DETECTED("multiple-targets-detected", CompletionStatus.INVALID, "Multiple action targets supplied.  This governance service does not support multiple action targets because the result of the origin search could be different for each action target making it difficult to automate the response."),

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
    QualifiedNamePeerDuplicateGuard(String name, CompletionStatus completionStatus, String description)
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

        for (QualifiedNamePeerDuplicateGuard guard : QualifiedNamePeerDuplicateGuard.values())
        {
            GuardType guardType = new GuardType();

            guardType.setGuard(guard.getName());
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

        guardType.setGuard(name);
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
