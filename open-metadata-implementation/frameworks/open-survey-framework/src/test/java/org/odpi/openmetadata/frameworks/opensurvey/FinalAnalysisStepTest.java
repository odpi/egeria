/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opensurvey;

import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestParameterType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyRequestParameter;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * FinalAnalysisStepTest covers the finalAnalysisStep control that every survey action service supports.
 * <br><br>
 * A survey sets its current analysis step as it goes and, at the end of each step, asks whether that step is the
 * final one requested.  If it is, the survey stops; if the requested step never comes up, the survey runs to
 * completion.  The value comes from the request parameter, or failing that the configuration property.  These
 * tests drive a survey that runs three steps over a mocked survey context, and check which steps ran.
 */
public class FinalAnalysisStepTest
{
    private static final AnalysisStep[] STEPS = new AnalysisStep[] {AnalysisStep.CHECK_ASSET,
                                                                    AnalysisStep.MEASURE_RESOURCE,
                                                                    AnalysisStep.PROFILE_DATA};


    /**
     * Without a final analysis step, every step runs.
     *
     * @throws Exception not expected
     */
    @Test public void testWholeSurveyRunsByDefault() throws Exception
    {
        List<String> stepsRun = this.runSurvey(null, null);

        assertEquals(stepsRun, List.of("Check Asset", "Measure Resource", "Profile Data"));
    }


    /**
     * The survey stops at the end of the requested step.
     *
     * @throws Exception not expected
     */
    @Test public void testSurveyStopsAfterRequestedStep() throws Exception
    {
        assertEquals(this.runSurvey(AnalysisStep.CHECK_ASSET.getName(), null), List.of("Check Asset"));
        assertEquals(this.runSurvey(AnalysisStep.MEASURE_RESOURCE.getName(), null), List.of("Check Asset", "Measure Resource"));
    }


    /**
     * A step the survey never reaches lets it run to completion.
     *
     * @throws Exception not expected
     */
    @Test public void testUnreachedStepRunsWholeSurvey() throws Exception
    {
        assertEquals(this.runSurvey(AnalysisStep.SCHEMA_VALIDATION.getName(), null).size(), 3);
        assertEquals(this.runSurvey("Not A Step", null).size(), 3);
    }


    /**
     * The configuration property sets the final step, and the request parameter overrides it.
     *
     * @throws Exception not expected
     */
    @Test public void testRequestParameterOverridesConfigurationProperty() throws Exception
    {
        assertEquals(this.runSurvey(null, AnalysisStep.CHECK_ASSET.getName()), List.of("Check Asset"));
        assertEquals(this.runSurvey(AnalysisStep.MEASURE_RESOURCE.getName(), AnalysisStep.CHECK_ASSET.getName()),
                     List.of("Check Asset", "Measure Resource"));
    }


    /**
     * Once one service in a sequential pipeline completes the final step, the services after it are not run.
     *
     * @throws Exception not expected
     */
    @Test public void testPipelineStopsAtFinalStep() throws Exception
    {
        List<String> stepsRun = new ArrayList<>();

        SurveyContext surveyContext = this.getSurveyContext(AnalysisStep.MEASURE_RESOURCE.getName());

        ThreeStepSurvey first  = new ThreeStepSurvey(stepsRun);
        ThreeStepSurvey second = new ThreeStepSurvey(stepsRun);

        first.initialize("first", new Connection());
        second.initialize("second", new Connection());

        SequentialSurveyPipeline pipeline = new SequentialSurveyPipeline();

        pipeline.initialize("pipeline", new Connection());
        pipeline.setSurveyContext(surveyContext);
        pipeline.embeddedConnectors = List.<Connector>of(first, second);
        pipeline.start();

        assertEquals(stepsRun, List.of("Check Asset", "Measure Resource"));
    }


    /**
     * A provider advertises the steps a survey can stop after, in run order, on both the request parameter and
     * the configuration property, and recognizes the configuration property.
     */
    @Test public void testProviderAdvertisesValidValues()
    {
        TestProvider provider = new TestProvider();

        RequestParameterType requestParameter = null;
        int                  count            = 0;

        for (RequestParameterType supportedRequestParameter : provider.getSupportedRequestParameters())
        {
            if (SurveyRequestParameter.FINAL_ANALYSIS_STEP.getName().equals(supportedRequestParameter.getName()))
            {
                requestParameter = supportedRequestParameter;
                count++;
            }
        }

        assertEquals(count, 1);
        assertNotNull(requestParameter);
        assertEquals(requestParameter.getOtherPropertyValues().get(RequestParameterType.VALID_VALUES),
                     "Check Asset,Measure Resource,Profile Data");

        ConfigurationPropertyType configurationProperty = null;

        for (ConfigurationPropertyType supportedConfigurationProperty : provider.getSupportedConfigurationProperties())
        {
            if (SurveyRequestParameter.FINAL_ANALYSIS_STEP.getName().equals(supportedConfigurationProperty.getName()))
            {
                configurationProperty = supportedConfigurationProperty;
            }
        }

        assertNotNull(configurationProperty);
        assertEquals(configurationProperty.getOtherPropertyValues().get(RequestParameterType.VALID_VALUES),
                     "Check Asset,Measure Resource,Profile Data");
        assertTrue(provider.getConnectorType().getRecognizedConfigurationProperties().contains(SurveyRequestParameter.FINAL_ANALYSIS_STEP.getName()));
    }


    /**
     * Without known steps, the request parameter carries no valid values.
     */
    @Test public void testUnknownStepsHaveNoValidValues()
    {
        assertNull(SurveyRequestParameter.getFinalAnalysisStepType(null).getOtherPropertyValues());
    }


    /**
     * Run the three-step survey.
     *
     * @param requestParameterValue finalAnalysisStep request parameter (may be null)
     * @param configurationPropertyValue finalAnalysisStep configuration property (may be null)
     * @return names of the steps that ran
     * @throws Exception not expected
     */
    private List<String> runSurvey(String requestParameterValue,
                                   String configurationPropertyValue) throws Exception
    {
        List<String> stepsRun = new ArrayList<>();

        Connection connection = new Connection();

        if (configurationPropertyValue != null)
        {
            Map<String, Object> configurationProperties = new HashMap<>();

            configurationProperties.put(SurveyRequestParameter.FINAL_ANALYSIS_STEP.getName(), configurationPropertyValue);
            connection.setConfigurationProperties(configurationProperties);
        }

        ThreeStepSurvey survey = new ThreeStepSurvey(stepsRun);

        survey.initialize("survey", connection);
        survey.setSurveyContext(this.getSurveyContext(requestParameterValue));
        survey.start();

        return stepsRun;
    }


    /**
     * A mocked survey context whose annotation store remembers the current analysis step.
     *
     * @param requestParameterValue finalAnalysisStep request parameter (may be null)
     * @return survey context
     * @throws Exception not expected
     */
    private SurveyContext getSurveyContext(String requestParameterValue) throws Exception
    {
        SurveyContext   surveyContext   = mock(SurveyContext.class);
        AnnotationStore annotationStore = mock(AnnotationStore.class);
        String[]        currentStep     = new String[1];

        doAnswer(invocation -> { currentStep[0] = invocation.getArgument(0); return null; }).when(annotationStore).setAnalysisStep(anyString());
        doAnswer(invocation -> currentStep[0]).when(annotationStore).getAnalysisStep();

        Map<String, String> requestParameters = new HashMap<>();

        if (requestParameterValue != null)
        {
            requestParameters.put(SurveyRequestParameter.FINAL_ANALYSIS_STEP.getName(), requestParameterValue);
        }

        when(surveyContext.getAnnotationStore()).thenReturn(annotationStore);
        when(surveyContext.getRequestParameters()).thenReturn(requestParameters);
        when(surveyContext.getAssetGUID()).thenReturn("asset-guid");

        return surveyContext;
    }


    /**
     * A survey that runs three analysis steps, checking for the final step at the end of each one.
     */
    static class ThreeStepSurvey extends SurveyActionServiceConnector
    {
        private final List<String> stepsRun;

        /**
         * Constructor.
         *
         * @param stepsRun records the steps that ran
         */
        ThreeStepSurvey(List<String> stepsRun)
        {
            this.stepsRun = stepsRun;
        }


        /**
         * Run the steps.
         *
         * @throws ConnectorCheckedException not expected
         * @throws UserNotAuthorizedException not expected
         */
        @Override
        public void start() throws ConnectorCheckedException, UserNotAuthorizedException
        {
            super.start();

            try
            {
                for (AnalysisStep analysisStep : STEPS)
                {
                    surveyContext.getAnnotationStore().setAnalysisStep(analysisStep.getName());
                    stepsRun.add(analysisStep.getName());

                    if (super.isFinalAnalysisStep())
                    {
                        return;
                    }
                }
            }
            catch (UserNotAuthorizedException error)
            {
                throw error;
            }
            catch (Exception error)
            {
                super.handleUnexpectedException("start", error);
            }
        }


        /**
         * Nothing to release in the test.
         */
        @Override
        public synchronized void disconnect()
        {
        }
    }


    /**
     * A provider for the three-step survey.
     */
    static class TestProvider extends SurveyActionServiceProvider
    {
        /**
         * Constructor declares the survey's steps.
         */
        TestProvider()
        {
            super();

            super.connectorTypeBean = new org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType();
            super.connectorTypeBean.setRecognizedConfigurationProperties(List.of("otherProperty"));
            super.supportedAnalysisSteps = AnalysisStep.getAnalysisStepTypes(STEPS);
            super.setFinalAnalysisSteps(STEPS);
        }
    }
}
