/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.wso2mi.survey;


import org.odpi.openmetadata.adapters.connectors.wso2mi.properties.APIInfo;
import org.odpi.openmetadata.adapters.connectors.wso2mi.resource.WSO2MIResourceConnector;
import org.odpi.openmetadata.adapters.connectors.wso2mi.survey.controls.WSO2MIAnnotationType;
import org.odpi.openmetadata.adapters.connectors.wso2mi.survey.ffdc.WSO2MISurveyErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceProfileAnnotationProperties;
import org.odpi.openmetadata.frameworks.opensurvey.AnnotationStore;
import org.odpi.openmetadata.frameworks.opensurvey.SurveyActionServiceConnector;
import org.odpi.openmetadata.frameworks.opensurvey.controls.AnalysisStep;

import java.util.ArrayList;
import java.util.List;

/**
 * SurveyWSO2MIServerConnector authenticates with a WSO2 Micro Integrator's Management API (via the embedded
 * {@link WSO2MIResourceConnector}) and profiles the REST APIs it has deployed.
 *
 * <p><b>v1 scope (odpi/egeria#9245):</b> this runs before the integration connector, exercising the same
 * authentication and REST call path against a real instance so the connector's behaviour is proven before
 * any cataloguing configuration exists.  It profiles the deployed API list only; the other artifact kinds
 * a Micro Integrator hosts (proxy services, sequences, endpoints, ...) are follow-up increments, matching
 * the integration connector's own v1 scope.</p>
 */
public class SurveyWSO2MIServerConnector extends SurveyActionServiceConnector
{
    /**
     * Indicates that the survey action service is completely configured and can begin processing.
     * This is where the function of the survey action service is implemented.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException a problem within the survey action service.
     * @throws UserNotAuthorizedException the service was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        try
        {
            surveyContext.getAnnotationStore().setAnalysisStep(AnalysisStep.CHECK_ASSET.getName());

            connector = super.performCheckAssetAnalysisStep(WSO2MIResourceConnector.class,
                                                            DeployedImplementationType.SOFTWARE_SERVER.getAssociatedTypeName());

            WSO2MIResourceConnector wso2MIResourceConnector = (WSO2MIResourceConnector) connector;

            AnnotationStore annotationStore = surveyContext.getAnnotationStore();

            annotationStore.setAnalysisStep(AnalysisStep.PROFILING_ASSOCIATED_RESOURCES.getName());

            List<APIInfo> deployedAPIs = wso2MIResourceConnector.listAPIs();
            List<String>  apiNames     = new ArrayList<>();

            for (APIInfo apiInfo : deployedAPIs)
            {
                apiNames.add(apiInfo.getName());
            }

            ResourceProfileAnnotationProperties resourceProfileAnnotation = new ResourceProfileAnnotationProperties();

            setUpAnnotation(resourceProfileAnnotation, WSO2MIAnnotationType.API_LIST);

            resourceProfileAnnotation.setValueList(apiNames);

            annotationStore.addAnnotation(resourceProfileAnnotation, surveyContext.getAssetGUID());

            annotationStore.setAnalysisStep(AnalysisStep.PRODUCE_INVENTORY.getName());

            super.writeNameListInventory(WSO2MIAnnotationType.API_INVENTORY,
                                         "apiList",
                                         apiNames,
                                         surveyContext.getAnnotationStore().getSurveyReportGUID());
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(WSO2MISurveyErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(surveyActionServiceName,
                                                                                                                error.getClass().getName(),
                                                                                                                methodName,
                                                                                                                error.getMessage()),
                                                this.getClass().getName(),
                                                methodName);
        }
    }


    /**
     * Transfer common properties into the annotation.
     *
     * @param annotation output annotation
     * @param wso2MIAnnotationType annotation type definition
     */
    private void setUpAnnotation(AnnotationProperties  annotation,
                                 WSO2MIAnnotationType wso2MIAnnotationType)
    {
        annotation.setAnnotationType(wso2MIAnnotationType.getName());
        annotation.setAnalysisStep(wso2MIAnnotationType.getAnalysisStep());
        annotation.setSummary(wso2MIAnnotationType.getSummary());
        annotation.setExplanation(wso2MIAnnotationType.getExplanation());
    }
}
