/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction.controls;

import org.odpi.openmetadata.frameworks.governanceaction.controls.GuardType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;

/**
 * The SurveyActionGuard enum describes the standard guards that are automatically set up for all
 * survey action services.
 */
public enum SurveyActionGuard
{
    SURVEY_COMPLETED("survey-completed", CompletionStatus.ACTIONED,  "The survey completed successfully.  The survey report is attached to the asset."),
    SURVEY_FAILED(  "survey-failed", CompletionStatus.FAILED, "An unexpected error occurred during the survey process.  The survey report is incomplete.");

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
    SurveyActionGuard(String name, CompletionStatus completionStatus, String description)
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
        return "SurveyActionGuard{ name='" + name + "}";
    }
}
