/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.automatedcuration.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.opengovernance.client.OpenGovernanceClient;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataElementBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataRelationshipBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EngineActionElementsResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.InitiateEngineActionRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.InitiateGovernanceActionProcessRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.InitiateGovernanceActionTypeRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.automatedcuration.handlers.TechnologyTypeHandler;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeHierarchyResponse;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeReportResponse;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeSummaryListResponse;
import org.slf4j.LoggerFactory;


/**
 * The AutomatedCurationRESTServices provides the implementation of the Automated Curation Open Metadata View Service (OMVS).
 */

public class AutomatedCurationRESTServices extends TokenController
{
    private static final AutomatedCurationInstanceHandler instanceHandler = new AutomatedCurationInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(AutomatedCurationRESTServices.class),
                                                                            instanceHandler.getServiceName());

    private final OpenMetadataElementBuilder      entityBuilder = new OpenMetadataElementBuilder();
    private final OpenMetadataRelationshipBuilder relationshipBuilder = new OpenMetadataRelationshipBuilder();

    /**
     * Default constructor
     */
    public AutomatedCurationRESTServices()
    {
    }

    /* =====================================================================================================================
     * The technology types provide the reference data to guide the curator.  They are extracted from the valid
     * values for deployedImplementationType
     */

    /**
     * Retrieve the list of deployed implementation type metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public TechnologyTypeSummaryListResponse findTechnologyTypes(String                  serverName,
                                                                 String                  urlMarker,
                                                                 SearchStringRequestBody requestBody)
    {
        final String methodName = "findTechnologyTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TechnologyTypeSummaryListResponse response = new TechnologyTypeSummaryListResponse();
        AuditLog                          auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            TechnologyTypeHandler handler = instanceHandler.getTechnologyTypeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findTechnologyTypes(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findTechnologyTypes(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of deployed implementation type metadata elements linked to a particular open metadata type.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param typeName the type name to search for
     * @param requestBody does the value start with the supplied string?
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */

    public TechnologyTypeSummaryListResponse getTechnologyTypesForOpenMetadataType(String             serverName,
                                                                                   String             urlMarker,
                                                                                   String             typeName,
                                                                                   ResultsRequestBody requestBody)
    {
        final String methodName = "getTechnologyTypesForOpenMetadataType";
        final String parameterName = "typeName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TechnologyTypeSummaryListResponse response = new TechnologyTypeSummaryListResponse();
        AuditLog                          auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (typeName != null)
            {
                TechnologyTypeHandler handler = instanceHandler.getTechnologyTypeHandler(userId, serverName, urlMarker, methodName);

                if (requestBody != null)
                {
                    response.setElements(handler.getTechnologyTypesForOpenMetadataType(userId,
                                                                                       typeName,
                                                                                       requestBody));
                }
                else
                {
                    response.setElements(handler.getTechnologyTypesForOpenMetadataType(userId,
                                                                                       typeName,
                                                                                       null));
                }
            }
            else
            {
                restExceptionHandler.handleMissingValue(parameterName, methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the requested deployed implementation type metadata element.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public TechnologyTypeReportResponse getTechnologyTypeDetail(String            serverName,
                                                                String            urlMarker,
                                                                FilterRequestBody requestBody)
    {
        final String methodName = "getTechnologyTypeDetail";
        final String parameterName = "requestBody.filter";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TechnologyTypeReportResponse response = new TechnologyTypeReportResponse();
        AuditLog                     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getFilter() != null)
                {
                    TechnologyTypeHandler    handler = instanceHandler.getTechnologyTypeHandler(userId, serverName, urlMarker, methodName);

                    response.setElement(handler.getTechnologyTypeDetail(userId,
                                                                        requestBody.getFilter(),
                                                                        requestBody));
                }
                else
                {
                    restExceptionHandler.handleMissingValue(parameterName, methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the requested deployed implementation type metadata element and its subtypes.  A mermaid version if the hierarchy is also returned.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public TechnologyTypeHierarchyResponse getTechnologyTypeHierarchy(String            serverName,
                                                                      String            urlMarker,
                                                                      FilterRequestBody requestBody)
    {
        final String methodName = "getTechnologyTypeHierarchy";
        final String parameterName = "requestBody.filter";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TechnologyTypeHierarchyResponse response = new TechnologyTypeHierarchyResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getFilter() != null)
                {
                    TechnologyTypeHandler    handler = instanceHandler.getTechnologyTypeHandler(userId, serverName, urlMarker, methodName);

                    response.setElement(handler.getTechnologyTypeHierarchy(userId,
                                                                           requestBody.getFilter(),
                                                                           requestBody));

                    response.setMermaidGraph(handler.getTechnologyTypeHierarchyMermaidString(response.getElement()));
                }
                else
                {
                    restExceptionHandler.handleMissingValue(parameterName, methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the requested deployed implementation type metadata element. There are no wildcards allowed in the name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getTechnologyTypeElements(String            serverName,
                                                                      String            urlMarker,
                                                                      FilterRequestBody requestBody)
    {
        final String methodName = "getTechnologyTypeElements";
        final String parameterName = "requestBody.filter";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getFilter() != null)
                {
                    TechnologyTypeHandler technologyTypeHandler = instanceHandler.getTechnologyTypeHandler(userId, serverName, urlMarker, methodName);

                    response.setElements(technologyTypeHandler.getTechnologyTypeElements(userId,
                                                                                         requestBody.getFilter(),
                                                                                         requestBody));
                }
                else
                {
                    restExceptionHandler.handleMissingValue(parameterName, methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /* =====================================================================================================================
     * Catalog templates make it easy to create new complex object such as assets.
     */

    /**
     * Create a new element from a template.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody information about the template
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GUIDResponse createElementFromTemplate(String              serverName,
                                                  String              urlMarker,
                                                  TemplateRequestBody requestBody)
    {
        final String methodName = "createElementFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient openHandler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, urlMarker, methodName);

                response.setGUID(openHandler.createMetadataElementFromTemplate(userId,
                                                                               requestBody.getMetadataElementSubtypeName(),
                                                                               requestBody,
                                                                               requestBody.getTemplateGUID(),
                                                                               entityBuilder.getElementProperties(requestBody.getReplacementProperties()),
                                                                               requestBody.getPlaceholderPropertyValues(),
                                                                               relationshipBuilder.getNewElementProperties(requestBody.getParentRelationshipProperties())));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * Engine Actions
     */

    /**
     * Request that an engine action is stopped.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param engineActionGUID identifier of the engine action request.
     *
     * @return engine action properties and status or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public VoidResponse cancelEngineAction(String serverName,
                                           String urlMarker,
                                           String engineActionGUID)
    {
        final String methodName = "cancelEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, urlMarker, methodName);

            handler.cancelEngineAction(userId, engineActionGUID);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the engine actions that are still in process.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementsResponse getActiveEngineActions(String serverName,
                                                               String urlMarker,
                                                               int    startFrom,
                                                               int    pageSize)
    {
        final String methodName = "getActiveEngineActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        EngineActionElementsResponse response = new EngineActionElementsResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, urlMarker, methodName);

            response.setElements(handler.getActiveEngineActions(userId, startFrom, pageSize));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /* =====================================================================================================================
     * Execute governance actions
     */

    /**
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestBody properties for the governance action and to pass to the governance action service
     *
     * @return unique identifier of the governance action or
     *  InvalidParameterException null qualified name
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action
     *  PropertyServerException a problem with the metadata store
     */
    public GUIDResponse initiateEngineAction(String                          serverName,
                                             String                urlMarker,
                                             String                          governanceEngineName,
                                             InitiateEngineActionRequestBody requestBody)
    {
        final String methodName = "initiateEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.initiateEngineAction(userId,
                                                              requestBody.getQualifiedName(),
                                                              requestBody.getDomainIdentifier(),
                                                              requestBody.getDisplayName(),
                                                              requestBody.getDescription(),
                                                              requestBody.getActionSourceGUIDs(),
                                                              requestBody.getActionCauseGUIDs(),
                                                              requestBody.getActionTargets(),
                                                              requestBody.getReceivedGuards(),
                                                              requestBody.getStartDate(),
                                                              governanceEngineName,
                                                              requestBody.getRequestType(),
                                                              requestBody.getRequestParameters(),
                                                              requestBody.getProcessName(),
                                                              requestBody.getRequestSourceName(),
                                                              requestBody.getOriginatorServiceName(),
                                                              requestBody.getOriginatorEngineName()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Using the named governance action type as a template, initiate an engine action.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties to initiate the new instance of the engine action
     *
     * @return unique identifier of the first governance action of the process or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     *  PropertyServerException a problem with the metadata store
     */
    public GUIDResponse initiateGovernanceActionType(String                          serverName,
                                                     String                urlMarker,
                                                     InitiateGovernanceActionTypeRequestBody requestBody)
    {
        final String methodName = "initiateGovernanceActionType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.initiateGovernanceActionType(userId,
                                                                      requestBody.getGovernanceActionTypeQualifiedName(),
                                                                      requestBody.getActionSourceGUIDs(),
                                                                      requestBody.getActionCauseGUIDs(),
                                                                      requestBody.getActionTargets(),
                                                                      requestBody.getStartDate(),
                                                                      requestBody.getRequestParameters(),
                                                                      requestBody.getOriginatorServiceName(),
                                                                      requestBody.getOriginatorEngineName()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties to initiate the new instance of the process
     *
     * @return unique identifier of the governance action process instance or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     *  PropertyServerException a problem with the metadata store
     */
    public GUIDResponse initiateGovernanceActionProcess(String                             serverName,
                                                        String                urlMarker,
                                                        InitiateGovernanceActionProcessRequestBody requestBody)
    {
        final String methodName = "initiateGovernanceActionProcess";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.initiateGovernanceActionProcess(userId,
                                                                         requestBody.getProcessQualifiedName(),
                                                                         requestBody.getActionSourceGUIDs(),
                                                                         requestBody.getActionCauseGUIDs(),
                                                                         requestBody.getActionTargets(),
                                                                         requestBody.getStartDate(),
                                                                         requestBody.getRequestParameters(),
                                                                         requestBody.getOriginatorServiceName(),
                                                                         requestBody.getOriginatorEngineName()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


}
