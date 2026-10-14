/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.oracle.survey;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnalysisStepType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestParameterType;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyRequestParameter;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;


/**
 * Verify that the Oracle server and database surveys advertise the analysis steps a caller can name in
 * finalAnalysisStep, in the order the surveys run them.
 */
public class OracleSurveyActionProviderTest
{
    /**
     * The server survey measures each database from the server and then surveys each database's schemas.
     */
    @Test public void testServerSurveyFinalAnalysisSteps()
    {
        checkFinalAnalysisSteps(new OracleServerSurveyActionProvider(),
                                List.of(AnalysisStep.CHECK_ASSET.getName(),
                                        AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName(),
                                        AnalysisStep.PRODUCE_INVENTORY.getName()));
    }


    /**
     * The database survey measures the database and then surveys its schemas.
     */
    @Test public void testDatabaseSurveyFinalAnalysisSteps()
    {
        checkFinalAnalysisSteps(new OracleDatabaseSurveyActionProvider(),
                                List.of(AnalysisStep.CHECK_ASSET.getName(),
                                        AnalysisStep.MEASURE_RESOURCE.getName(),
                                        AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName()));
    }


    /**
     * Check that the supported analysis steps are the expected ones in run order, that the valid values of the
     * finalAnalysisStep request parameter list the same steps in the same order, and that finalAnalysisStep is
     * also a recognized configuration property.
     *
     * @param provider provider to test
     * @param expectedSteps analysis step names in run order
     */
    private void checkFinalAnalysisSteps(SurveyActionServiceProvider provider,
                                         List<String>                expectedSteps)
    {
        List<String> supportedSteps = new ArrayList<>();

        for (AnalysisStepType analysisStepType : provider.getSupportedAnalysisSteps())
        {
            supportedSteps.add(analysisStepType.getName());
        }

        assertEquals(supportedSteps, expectedSteps);

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
                     String.join(",", expectedSteps));

        boolean configurationPropertyFound = false;

        for (ConfigurationPropertyType configurationPropertyType : provider.getSupportedConfigurationProperties())
        {
            if (SurveyRequestParameter.FINAL_ANALYSIS_STEP.getName().equals(configurationPropertyType.getName()))
            {
                configurationPropertyFound = true;
            }
        }

        assertTrue(configurationPropertyFound);
        assertTrue(provider.getConnectorType().getRecognizedConfigurationProperties().contains(SurveyRequestParameter.FINAL_ANALYSIS_STEP.getName()));
    }
}
