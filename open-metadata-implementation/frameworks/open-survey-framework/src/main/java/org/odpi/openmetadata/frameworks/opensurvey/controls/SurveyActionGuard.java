/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opensurvey.controls;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.GuardType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CompletionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * The SurveyActionGuard enum describes the standard guards that are automatically set up for all
 * survey action services.
 */
public enum SurveyActionGuard
{
    SURVEY_COMPLETED("survey-completed", CompletionStatus.ACTIONED,  "The survey completed successfully.  The survey report is attached to the asset."),
    SURVEY_INVALID("survey-invalid", CompletionStatus.INVALID,  "The survey did not run because the supplied information (such as the asset) is invalid for this type of survey."),
    SURVEY_FAILED(  "survey-failed", CompletionStatus.FAILED, "An unexpected error occurred during the survey process.  The survey report is incomplete."),

    DATA_CERTIFIED("data-certified", CompletionStatus.ACTIONED,  "All of the quality checks on the data succeeded.  The data has been certified."),
    DATA_NOT_CERTIFIED("data-not-certified", CompletionStatus.ACTIONED,  "One or more of the quality checks on the data failed.  The data has not been certified and a request for action has been raised."),
    MISSING_CERTIFICATION_TYPE("missing-certification-type", CompletionStatus.INVALID, "The certification type is not supplied in the action targets.  This means the survey is not able to certify the data if it is correct."),
    MISSING_SCHEMA_TYPE("missing-schema-type", CompletionStatus.INVALID, "The surveyed asset does not have a schema type attached.  This means the survey is not able to certify that the data is stored in the correct structure."),
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


    /**
     * Return details of the guards used on a simple survey.  These guards are set automatically if the survey does not
     * set a guard itself.
     *
     * @return guard types
     */
    public static List<GuardType> getSimpleSurveyGuardTypes()
    {
        List<GuardType> guardTypes = new ArrayList<>();

        guardTypes.add(SURVEY_COMPLETED.getGuardType());
        guardTypes.add(SURVEY_INVALID.getGuardType());
        guardTypes.add(SURVEY_FAILED.getGuardType());

        return guardTypes;
    }



    /**
     * Return details of the guards used on a survey that is validating the structure and content of data.
     * Each check produces a Qualify AnnotationProperties.  If all checks pass, the certification is added to the asset.
     * If any checks fail, a request for action is created for the asset, linking the failing quality annotations.
     *
     * @return guard types
     */
    public static List<GuardType> getDataValidationSurveyGuardTypes()
    {
        List<GuardType> guardTypes = new ArrayList<>();

        guardTypes.add(DATA_CERTIFIED.getGuardType());
        guardTypes.add(DATA_NOT_CERTIFIED.getGuardType());
        guardTypes.add(MISSING_CERTIFICATION_TYPE.getGuardType());
        guardTypes.add(MISSING_SCHEMA_TYPE.getGuardType());
        guardTypes.add(SURVEY_FAILED.getGuardType());

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
        return "SurveyActionGuard{ name='" + name + "}";
    }
}
