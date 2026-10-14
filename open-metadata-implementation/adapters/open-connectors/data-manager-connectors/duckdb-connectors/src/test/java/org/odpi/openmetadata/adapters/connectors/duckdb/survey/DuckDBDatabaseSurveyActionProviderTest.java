/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.duckdb.survey;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnalysisStepType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestParameterType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyRequestParameter;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


/**
 * Verify that the DuckDB database survey advertises the analysis steps a caller can name in finalAnalysisStep,
 * in the order the survey runs them.
 */
public class DuckDBDatabaseSurveyActionProviderTest
{
    /**
     * The valid values of finalAnalysisStep match the supported analysis steps, in run order.
     */
    @Test public void testFinalAnalysisStepValidValues()
    {
        DuckDBDatabaseSurveyActionProvider provider = new DuckDBDatabaseSurveyActionProvider();

        List<String> supportedSteps = new ArrayList<>();

        for (AnalysisStepType analysisStepType : provider.getSupportedAnalysisSteps())
        {
            supportedSteps.add(analysisStepType.getName());
        }

        assertEquals(supportedSteps, List.of(AnalysisStep.CHECK_ASSET.getName(),
                                             AnalysisStep.MEASURE_RESOURCE.getName(),
                                             AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName()));

        RequestParameterType finalAnalysisStep = null;

        for (RequestParameterType requestParameterType : provider.getSupportedRequestParameters())
        {
            if (SurveyRequestParameter.FINAL_ANALYSIS_STEP.getName().equals(requestParameterType.getName()))
            {
                finalAnalysisStep = requestParameterType;
            }
        }

        assertNotNull(finalAnalysisStep);
        assertEquals(finalAnalysisStep.getOtherPropertyValues().get(RequestParameterType.VALID_VALUES),
                     String.join(",", supportedSteps));
    }
}
