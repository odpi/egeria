/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.surveyaction.controls;

import java.util.ArrayList;
import java.util.List;

/**
 * The AnalysisStep enum describes an analysis step in a survey.  Not all survey action services support analysis steps.
 * but those that do often allow the desired analysis steps to be selected.
 */
public enum AnalysisStep
{
    CHECK_ASSET("Check Asset",  "The survey action service is checking that the asset is of the correct type and the connection defines the correct type of connector."),
    CHECK_ACTION_TARGETS("Check Action Targets",  "The survey action service is checking that additional information passed by the action targets is available."),
    CHECK_REQUEST_PARAMETERS("Check Request Parameters",  "The survey action service is checking that additional information passed by the request parameters is available."),
    MEASURE_RESOURCE("Measure Resource", "The survey action service is taking measurements from the resource."),
    SCHEMA_EXTRACTION(  "Schema Extraction", "The survey action service is extracting the schema from the resource."),
    PROFILE_DATA(  "Profile Data", "The survey action service is profiling the data associated with the resource."),
    PROFILING_ASSOCIATED_RESOURCES(  "Profiling Associated Resources", "The survey action service is profiling other resources associated with the surveyed resource."),
    PRODUCE_INVENTORY("Produce Inventory", "The survey action service is writing an inventory of the contents of the surveyed resource."),
    PRODUCE_ACTIONS("Produce Exceptions", "The survey action service is writing out information about issues that is discovered during its analysis."),
    SCHEMA_VALIDATION("Schema Validation", "The survey action service is validating that the schema attached to the asset matches the structure of the data stored in the asset."),
    DATA_VALIDATION("Data Validation", "The survey action service is validating that the data stored in the resource matches the specification."),
    ;

    public final String           name;
    public final String           description;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the analysis step
     * @param description description of the analysis step
     */
    AnalysisStep(String name, String description)
    {
        this.name             = name;
        this.description      = description;
    }


    /**
     * Return the name of the analysis step.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }



    /**
     * Return the description of the analysis step.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * convert the list of Analysis Steps into a list of analysis step types.
     *
     * @param analysisSteps analysis step enums
     * @return list of analysis step types for the service provider
     */
    public static List<AnalysisStepType> getAnalysisStepTypes(AnalysisStep[] analysisSteps)
    {
        if ((analysisSteps == null) || (analysisSteps.length == 0))
        {
            return null;
        }

        List<AnalysisStepType> analysisStepTypes = new ArrayList<>();

        for (AnalysisStep analysisStep : analysisSteps)
        {
            analysisStepTypes.add(analysisStep.getAnalysisStepType());
        }

        return analysisStepTypes;
    }


    /**
     * Return the analysis step type for the connector provider for this enum value.
     *
     * @return analysis step type
     */
    public AnalysisStepType getAnalysisStepType()
    {
        AnalysisStepType analysisStepType = new AnalysisStepType();

        analysisStepType.setName(name);
        analysisStepType.setDescription(description);

        return analysisStepType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "AnalysisStep{ name='" + name + "}";
    }
}
