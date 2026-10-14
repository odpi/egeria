/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opensurvey;

import org.odpi.openmetadata.frameworks.connectors.OpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceServiceProviderBase;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnalysisStepType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnnotationTypeType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestParameterType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.*;

import java.util.ArrayList;
import java.util.List;


/**
 * SurveyActionServiceProvider implements the base class for the connector provider for a survey action service.
 */
public abstract class SurveyActionServiceProvider extends GovernanceServiceProviderBase
{
    protected List<AnalysisStepType>   supportedAnalysisSteps  = null;
    protected List<AnnotationTypeType> producedAnnotationTypes = null;


    /**
     * Base provider for all survey action services.
     */
    public SurveyActionServiceProvider()
    {
        super.supportedRequestParameters = SurveyRequestParameter.getRequestParameterTypes(null);
        super.producedGuards = SurveyActionGuard.getSimpleSurveyGuardTypes();
        super.producedActionTargetTypes = SurveyActionTarget.getActionTargetTypes();
    }

    /**
     * Constructor for an open connector provider.
     *
     * @param openConnectorDescription             connector definition
     * @param connectorClassName                   connector class name
     * @param recognizedConfigurationPropertyNames list of recognized configuration property names
     */
    public SurveyActionServiceProvider(OpenConnectorDefinition openConnectorDescription,
                                       String                  connectorClassName,
                                       List<String>            recognizedConfigurationPropertyNames)
    {
        super(openConnectorDescription, connectorClassName, recognizedConfigurationPropertyNames);

        super.supportedRequestParameters = SurveyRequestParameter.getRequestParameterTypes(null);
        super.producedGuards = SurveyActionGuard.getSimpleSurveyGuardTypes();
        super.producedActionTargetTypes = SurveyActionTarget.getActionTargetTypes();
    }


    /**
     * Declare the analysis steps that the survey action service can stop after, in the order it runs them.  They
     * are advertised as the valid values of the finalAnalysisStep request parameter and configuration property,
     * which this method adds to the provider's supported request parameters, supported configuration properties
     * and recognized configuration properties.  Call it at the end of the subclass's constructor, after those lists
     * have been set up.
     *
     * @param finalAnalysisSteps analysis steps, in the order the service runs them
     */
    protected void setFinalAnalysisSteps(AnalysisStep[] finalAnalysisSteps)
    {
        final String propertyName = SurveyRequestParameter.FINAL_ANALYSIS_STEP.getName();

        List<RequestParameterType> requestParameters = new ArrayList<>();

        if (super.supportedRequestParameters != null)
        {
            for (RequestParameterType requestParameter : super.supportedRequestParameters)
            {
                if ((requestParameter != null) && (! propertyName.equals(requestParameter.getName())))
                {
                    requestParameters.add(requestParameter);
                }
            }
        }

        requestParameters.add(SurveyRequestParameter.getFinalAnalysisStepType(finalAnalysisSteps));
        super.supportedRequestParameters = requestParameters;

        List<ConfigurationPropertyType> configurationProperties = new ArrayList<>();

        if (super.supportedConfigurationProperties != null)
        {
            for (ConfigurationPropertyType configurationProperty : super.supportedConfigurationProperties)
            {
                if ((configurationProperty != null) && (! propertyName.equals(configurationProperty.getName())))
                {
                    configurationProperties.add(configurationProperty);
                }
            }
        }

        configurationProperties.add(SurveyRequestParameter.getFinalAnalysisStepConfigurationPropertyType(finalAnalysisSteps));
        super.supportedConfigurationProperties = configurationProperties;

        if (super.connectorTypeBean != null)
        {
            List<String> recognizedConfigurationProperties = new ArrayList<>();

            if (super.connectorTypeBean.getRecognizedConfigurationProperties() != null)
            {
                recognizedConfigurationProperties.addAll(super.connectorTypeBean.getRecognizedConfigurationProperties());
            }

            if (! recognizedConfigurationProperties.contains(propertyName))
            {
                recognizedConfigurationProperties.add(propertyName);
            }

            super.connectorTypeBean.setRecognizedConfigurationProperties(recognizedConfigurationProperties);
        }
    }


    /**
     * Return the analysis steps that the service supports.
     *
     * @return list of analysis steps
     */
    public List<AnalysisStepType> getSupportedAnalysisSteps()
    {
        return supportedAnalysisSteps;
    }


    /**
     * Return the supported annotation types.
     *
     * @return list of annotation types
     */
    public List<AnnotationTypeType> getProducedAnnotationTypes()
    {
        return producedAnnotationTypes;
    }
}
