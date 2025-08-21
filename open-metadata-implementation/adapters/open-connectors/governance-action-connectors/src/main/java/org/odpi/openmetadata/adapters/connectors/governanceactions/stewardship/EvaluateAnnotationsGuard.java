/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.frameworks.opengovernance.controls.GuardType;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CompletionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * The Guard enum describes some common guards that can be used when implementing governance services.
 * Using the common guard names where possible saves time when coding the provider class and improves the
 * ability of the service to be incorporated into governance action processes.
 */
public enum EvaluateAnnotationsGuard
{
    ACTIONS_DETECTED("actions-detected", CompletionStatus.INVALID, "The survey report does contain at least one Request For Action (RfA) but no template is provided to action them."),
    ACTIONS_ACTIONED("actions-actioned", CompletionStatus.ACTIONED, "The survey report contains at least one Request For Action and ToDos have been created to resolve any issues they report."),
    MISSING_STEWARD("missing-steward", CompletionStatus.INVALID, "No steward has been provided in the action targets."),
    NO_SURVEY_REPORT("no-survey-report", CompletionStatus.INVALID, "There is no survey report to process.  Details of the survey report should be provided as an action target."),
    EVALUATION_FAILED("evaluation-failed", CompletionStatus.FAILED, "An unexpected error occurred during the evaluation and it is not able to complete."),

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
    EvaluateAnnotationsGuard(String name, CompletionStatus completionStatus, String description)
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

        for (EvaluateAnnotationsGuard guard : EvaluateAnnotationsGuard.values())
        {
            guardTypes.add(guard.getGuardType());
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
