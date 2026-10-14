/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.unitycatalog.survey;

import org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder.FolderSurveyServiceProvider;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnalysisStepType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestParameterType;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceProvider;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyRequestParameter;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


/**
 * Verify that each Unity Catalog survey provider advertises the analysis steps a caller can name in
 * finalAnalysisStep, in the order the survey runs them.
 */
public class OSSUnityCatalogSurveyProviderTest
{
    private static final List<String> unityCatalogSteps = List.of(AnalysisStep.CHECK_ASSET.getName(),
                                                                  AnalysisStep.MEASURE_RESOURCE.getName(),
                                                                  AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName(),
                                                                  AnalysisStep.PRODUCE_INVENTORY.getName());

    private static final List<String> folderSteps = List.of(AnalysisStep.CHECK_ASSET.getName(),
                                                            AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName(),
                                                            AnalysisStep.PRODUCE_ACTIONS.getName(),
                                                            AnalysisStep.PRODUCE_INVENTORY.getName());


    /**
     * The server survey stops after any of its four steps.
     */
    @Test public void testServerSurveyFinalAnalysisSteps()
    {
        validateFinalAnalysisSteps(new OSSUnityCatalogServerSurveyProvider(), unityCatalogSteps);
    }


    /**
     * The inside-catalog survey stops after any of its four steps.
     */
    @Test public void testInsideCatalogSurveyFinalAnalysisSteps()
    {
        validateFinalAnalysisSteps(new OSSUnityCatalogInsideCatalogSurveyProvider(), unityCatalogSteps);
    }


    /**
     * The inside-schema survey stops after any of its four steps.
     */
    @Test public void testInsideSchemaSurveyFinalAnalysisSteps()
    {
        validateFinalAnalysisSteps(new OSSUnityCatalogInsideSchemaSurveyProvider(), unityCatalogSteps);
    }


    /**
     * The inside-volume survey runs the folder survey service, so it advertises that service's steps.
     */
    @Test public void testInsideVolumeSurveyFinalAnalysisSteps()
    {
        validateFinalAnalysisSteps(new OSSUnityCatalogInsideVolumeSurveyProvider(), folderSteps);
        validateFinalAnalysisSteps(new FolderSurveyServiceProvider(), folderSteps);
    }


    /**
     * Check that the provider's supported analysis steps are the expected ones, and that the valid values of
     * its finalAnalysisStep request parameter list them in the same order.
     *
     * @param provider provider to test
     * @param expectedSteps names of the analysis steps, in run order
     */
    private void validateFinalAnalysisSteps(SurveyActionServiceProvider provider,
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
                assertEquals(finalAnalysisStep, null, "finalAnalysisStep is listed more than once");

                finalAnalysisStep = requestParameterType;
            }
        }

        assertNotNull(finalAnalysisStep);
        assertEquals(finalAnalysisStep.getOtherPropertyValues().get(RequestParameterType.VALID_VALUES),
                     String.join(",", expectedSteps));
    }
}
