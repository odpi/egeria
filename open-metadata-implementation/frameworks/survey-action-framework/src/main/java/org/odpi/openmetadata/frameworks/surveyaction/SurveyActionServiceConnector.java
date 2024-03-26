/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.Connections;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.surveyaction.ffdc.SAFAuditCode;
import org.odpi.openmetadata.frameworks.surveyaction.ffdc.SAFErrorCode;

import java.util.ArrayList;
import java.util.List;


/**
 * SurveyActionServiceConnector describes a specific type of connector that is responsible for analyzing the content
 * of a specific asset.  Information about the asset to analyze is passed in the survey context.
 * The returned discovery context also contains the results.
 * Some discovery services manage the invocation of other discovery services.  These discovery services are called
 * discovery pipelines.
 */
public abstract class SurveyActionServiceConnector extends ConnectorBase implements SurveyActionService,
                                                                                    AuditLoggingComponent,
                                                                                    VirtualConnectorExtension
{
    protected String          surveyActionServiceName = "<Unknown>";
    protected SurveyContext   surveyContext           = null;
    protected AuditLog        auditLog                = null;
    protected List<Connector> embeddedConnectors      = null;


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    @Override
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
    }


    /**
     * Set up the list of survey action services connectors that will be invoked as part of this survey action pipeline.
     * The connectors are initialized waiting to start.  After start() is called on the
     * survey action pipeline, it will choreograph the invocation of its embedded survey action services by calling
     * start() to each of them when they are to run. Similar processing is needed for the disconnect() method.
     *
     * @param embeddedConnectors  list of embedded connectors that are hopefully survey action services
     */
    @Override
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        this.embeddedConnectors = embeddedConnectors;
    }


    /**
     * Set up details of the asset to analyze and the results of any previous analysis.
     *
     * @param surveyContext information about the asset to analyze and the results of analysis of
     *                         other survey action service request.  Partial results from other survey action
     *                         services run as part of the same survey action service request may also be
     *                         stored in the newAnnotations list.
     */
    public synchronized void setSurveyContext(SurveyContext surveyContext)
    {
        this.surveyContext = surveyContext;
    }


    /**
     * Set up the survey action service name.  This is used in error messages.
     *
     * @param surveyActionServiceName name of the survey action service
     */
    public void setSurveyActionServiceName(String surveyActionServiceName)
    {
        this.surveyActionServiceName = surveyActionServiceName;
    }


    /**
     * Return the survey context for this survey action service.  This is typically called after the disconnect()
     * method is called.  If called before disconnect(), it may only contain partial results.
     *
     * @return survey context containing the results discovered (so far) by the survey action service.
     * @throws ConnectorCheckedException the service is no longer active
     */
    protected synchronized SurveyContext getSurveyContext() throws ConnectorCheckedException
    {
        final String methodName = "getSurveyContext";

        validateIsActive(methodName);
        return surveyContext;
    }


    protected synchronized AnnotationStore getAnnotationStore() throws ConnectorCheckedException
    {
        final String methodName = "getAnnotationStore";

        validateIsActive(methodName);
        return surveyContext.getAnnotationStore();
    }


    /**
     * Log that the survey action service can not process the type of asset it has been passed.
     *
     * @param assetGUID identifier of the asset
     * @param assetType type of the asset
     * @param supportedAssetType supported asset types
     * @param methodName calling method
     * @throws ConnectorCheckedException resulting exception
     */
    protected void throwWrongTypeOfAsset(String    assetGUID,
                                         String    assetType,
                                         String    supportedAssetType,
                                         String    methodName) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(SAFErrorCode.INVALID_ASSET_TYPE.getMessageDefinition(assetGUID,
                                                                                                 assetType,
                                                                                                 surveyActionServiceName,
                                                                                                 supportedAssetType),
                                            this.getClass().getName(),
                                            methodName);
    }



    /**
     * Verify that the connector is still active.
     *
     * @param methodName calling method
     * @throws ConnectorCheckedException exception thrown if connector is no longer active
     */
    private void validateIsActive(String methodName) throws ConnectorCheckedException
    {
        if (! isActive())
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    SAFAuditCode.DISCONNECT_DETECTED.getMessageDefinition(surveyActionServiceName));
            }

            throw new ConnectorCheckedException(SAFErrorCode.DISCONNECT_DETECTED.getMessageDefinition(surveyActionServiceName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }



    /**
     * Retrieve and validate the list of embedded connectors and cast them to survey action service connector.
     * This is used by SurveyPipelines and SurveyScanningServices.
     *
     * @return list of survey action service connectors
     *
     * @throws ConnectorCheckedException one of the embedded connectors is not a survey action service
     */
    protected List<SurveyActionServiceConnector> getEmbeddedSurveyActionServices() throws ConnectorCheckedException
    {
        final String methodName = "getEmbeddedSurveyActionServices";
        
        List<SurveyActionServiceConnector> surveyActionServiceConnectors = null;

        if (embeddedConnectors != null)
        {
            surveyActionServiceConnectors = new ArrayList<>();

            for (Connector embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector != null)
                {
                    if (embeddedConnector instanceof SurveyActionServiceConnector)
                    {
                        surveyActionServiceConnectors.add((SurveyActionServiceConnector)embeddedConnector);
                    }
                    else
                    {
                        throw new ConnectorCheckedException(SAFErrorCode.INVALID_EMBEDDED_SURVEY_ACTION_SERVICE.getMessageDefinition(surveyActionServiceName),
                                this.getClass().getName(),
                                methodName);
                    }
                }
            }

            if (surveyActionServiceConnectors.isEmpty())
            {
                surveyActionServiceConnectors = null;
            }
        }

        return surveyActionServiceConnectors;
    }


    /**
     * Indicates that the survey action service is completely configured and can begin processing.
     * This is where the function of the survey action service is implemented.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the survey action service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        if (surveyContext == null)
        {
            throw new ConnectorCheckedException(SAFErrorCode.NULL_SURVEY_CONTEXT.getMessageDefinition(surveyActionServiceName),
                    this.getClass().getName(),
                    methodName);
        }
    }


    /**
     * Provide a common exception for unexpected errors.
     *
     * @param methodName calling method
     * @param error caught exception
     * @throws ConnectorCheckedException wrapped exception
     */
    protected void handleUnexpectedException(String      methodName,
                                             Exception   error) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(SAFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(surveyActionServiceName,
                                                                                                   error.getClass().getName(),
                                                                                                   methodName,
                                                                                                   error.getMessage()),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  synchronized void disconnect() throws ConnectorCheckedException
    {
        if (surveyContext!= null)
        {
            surveyContext.disconnect();
        }

        super.disconnectConnectors(this.embeddedConnectors);
        super.disconnect();
    }
}
