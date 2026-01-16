/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opensurvey;

import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.opensurvey.ffdc.OSFErrorCode;

import java.util.List;

/**
 * DiscoveryPipeline is a discovery service that is responsible for choreographing the discovery services
 * passed on initializeEmbeddedConnectors.
 */
public abstract class SurveyActionPipelineConnector extends SurveyActionServiceConnector implements VirtualConnectorExtension
{
    protected List<SurveyActionServiceConnector> embeddedSurveyActionServices = null;


    /**
     * Start the pipeline.
     *
     * @throws ConnectorCheckedException a problem within the discovery service.
     * @throws UserNotAuthorizedException the service was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName   = "start";

        /*
         * Check that the discovery context is not null and anything else is set up correctly
         */
        super.start();

        embeddedSurveyActionServices = getEmbeddedSurveyActionServices();

        if (embeddedSurveyActionServices == null)
        {
            throw new ConnectorCheckedException(OSFErrorCode.NO_EMBEDDED_SURVEY_ACTION_SERVICES.getMessageDefinition(surveyActionServiceName),
                                                this.getClass().getName(),
                                                methodName);
        }

        runSurveyPipeline();
    }


    /**
     * This implementation provides an inline sequential invocation of the supplied discovery services.
     *
     * @throws ConnectorCheckedException a problem within the discovery service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    protected abstract void runSurveyPipeline() throws ConnectorCheckedException, UserNotAuthorizedException;


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
