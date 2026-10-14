/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.survey;

import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnalysisStepType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestParameterType;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyRequestParameter;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;


/**
 * Verify that the Apache Atlas survey advertises the analysis steps a caller can name in
 * finalAnalysisStep, in the order the survey runs them.
 */
public class SurveyApacheAtlasProviderTest
{
    /**
     * The survey measures the server, extracts the type definitions and then profiles the instances.
     */
    @Test public void testFinalAnalysisSteps()
    {
        validateFinalAnalysisSteps(new SurveyApacheAtlasProvider(),
                                   List.of("Check Asset", "Measure Resource", "Schema Extraction", "Profile Data"));
    }


    /**
     * Check that a provider advertises the expected analysis steps, and offers the same steps, in the same order,
     * as the valid values of the finalAnalysisStep request parameter and configuration property.
     *
     * @param provider provider to test
     * @param expectedSteps names of the analysis steps in the order the survey runs them
     */
    private void validateFinalAnalysisSteps(SurveyActionServiceProvider provider,
                                            List<String>                expectedSteps)
    {
        final String propertyName = SurveyRequestParameter.FINAL_ANALYSIS_STEP.getName();

        List<String> supportedSteps = new ArrayList<>();

        for (AnalysisStepType analysisStepType : provider.getSupportedAnalysisSteps())
        {
            supportedSteps.add(analysisStepType.getName());
        }

        assertEquals(supportedSteps, expectedSteps);

        RequestParameterType finalAnalysisStep = null;

        for (RequestParameterType requestParameterType : provider.getSupportedRequestParameters())
        {
            if (propertyName.equals(requestParameterType.getName()))
            {
                finalAnalysisStep = requestParameterType;
            }
        }

        assertNotNull(finalAnalysisStep);
        assertEquals(finalAnalysisStep.getOtherPropertyValues().get(RequestParameterType.VALID_VALUES),
                     String.join(",", expectedSteps));

        ConfigurationPropertyType finalAnalysisStepProperty = null;

        for (ConfigurationPropertyType configurationPropertyType : provider.getSupportedConfigurationProperties())
        {
            if (propertyName.equals(configurationPropertyType.getName()))
            {
                finalAnalysisStepProperty = configurationPropertyType;
            }
        }

        assertNotNull(finalAnalysisStepProperty);
        assertEquals(finalAnalysisStepProperty.getOtherPropertyValues().get(RequestParameterType.VALID_VALUES),
                     String.join(",", expectedSteps));
        assertTrue(provider.getConnectorType().getRecognizedConfigurationProperties().contains(propertyName));
    }
}
