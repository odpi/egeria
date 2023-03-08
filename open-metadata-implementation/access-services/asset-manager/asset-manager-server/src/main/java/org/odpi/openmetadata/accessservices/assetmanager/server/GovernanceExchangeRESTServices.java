/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server;


import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceActionElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceActionProcessElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceActionTypeElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.NextGovernanceActionTypeElement;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionProcessElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionProcessElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionTypeElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionTypeElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NextGovernanceActionTypeElementsResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceActionTypeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceActionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The GovernanceActionProcessRESTServices provides the server-side implementation of the services used by an external user to
 * define governance action processes.
 */
public class GovernanceExchangeRESTServices
{
    private static final AssetManagerInstanceHandler instanceHandler = new AssetManagerInstanceHandler();

    private static final RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(GovernanceExchangeRESTServices.class),
                                                                                        instanceHandler.getServiceName());

    private final RESTExceptionHandler    restExceptionHandler    = new RESTExceptionHandler();


    /**
     * Default constructor
     */
    public GovernanceExchangeRESTServices()
    {
    }


    /* =====================================================================================================================
     * A governance action process describes a well defined series of steps that gets something done.
     * The steps are defined using GovernanceActionTypes.
     */


    /**
     * Retrieve the list of governance action process metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessElementsResponse findGovernanceActionProcesses(String                  serverName,
                                                                                 String                  userId,
                                                                                 int                     startFrom,
                                                                                 int                     pageSize,
                                                                                 SearchStringRequestBody requestBody)
    {
        final String methodName = "findGovernanceActionProcesses";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionProcessElementsResponse response = new GovernanceActionProcessElementsResponse();
        AuditLog                                auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId,
                                                                                                                         serverName,
                                                                                                                         methodName);

                response.setElements(handler.findAssets(userId,
                                                        OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_GUID,
                                                        OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                        requestBody.getSearchString(),
                                                        searchStringParameterName,
                                                        startFrom,
                                                        pageSize,
                                                        false,
                                                        false,
                                                        requestBody.getEffectiveTime(),
                                                        methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Retrieve the list of governance action process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessElementsResponse getGovernanceActionProcessesByName(String          serverName,
                                                                                      String          userId,
                                                                                      int             startFrom,
                                                                                      int             pageSize,
                                                                                      NameRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionProcessesByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionProcessElementsResponse response = new GovernanceActionProcessElementsResponse();
        AuditLog                                auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId,
                                                                                                                         serverName,
                                                                                                                         methodName);

                response.setElements(handler.findAssetsByName(userId,
                                                              OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_GUID,
                                                              OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                              requestBody.getName(),
                                                              nameParameterName,
                                                              startFrom,
                                                              pageSize,
                                                              false,
                                                              false,
                                                              requestBody.getEffectiveTime(),
                                                              methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessElementResponse getGovernanceActionProcessByGUID(String serverName,
                                                                                   String userId,
                                                                                   String processGUID)
    {
        final String methodName = "getGovernanceActionProcessByGUID";
        final String processGUIDParameterName = "processGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionProcessElementResponse response = new GovernanceActionProcessElementResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId, serverName, methodName);

            handler.getBeanFromRepository(userId,
                                          processGUID,
                                          processGUIDParameterName,
                                          OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /* =====================================================================================================================
     * A governance action type describes a step in a governance action process
     */


    /**
     * Retrieve the list of governance action type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypeElementsResponse findGovernanceActionTypes(String                  serverName,
                                                                          String                  userId,
                                                                          int                     startFrom,
                                                                          int                     pageSize,
                                                                          SearchStringRequestBody requestBody)
    {
        final String methodName = "findGovernanceActionTypes";

        String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionTypeElementsResponse response = new GovernanceActionTypeElementsResponse();
        AuditLog                             auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceActionTypeHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionTypeHandler(userId,
                                                                                                                                  serverName,
                                                                                                                                  methodName);

                if (requestBody.getSearchStringParameterName() != null)
                {
                    searchStringParameterName = requestBody.getSearchStringParameterName();
                }

                response.setElements(handler.findGovernanceActionTypes(userId,
                                                                       requestBody.getSearchString(),
                                                                       searchStringParameterName,
                                                                       startFrom,
                                                                       pageSize,
                                                                       false,
                                                                       false,
                                                                       requestBody.getEffectiveTime(),
                                                                       methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of governance action type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypeElementsResponse getGovernanceActionTypesByName(String          serverName,
                                                                               String          userId,
                                                                               int             startFrom,
                                                                               int             pageSize,
                                                                               NameRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionTypesByName";

        String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionTypeElementsResponse response = new GovernanceActionTypeElementsResponse();
        AuditLog                             auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceActionTypeHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionTypeHandler(userId,
                                                                                                                                  serverName,
                                                                                                                                  methodName);

                if (requestBody.getNameParameterName() != null)
                {
                    nameParameterName = requestBody.getNameParameterName();
                }

                response.setElements(handler.getGovernanceActionTypesByName(userId,
                                                                            requestBody.getName(),
                                                                            nameParameterName,
                                                                            startFrom,
                                                                            pageSize,
                                                                            null,
                                                                            methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the governance action type
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypeElementResponse getGovernanceActionTypeByGUID(String serverName,
                                                                             String userId,
                                                                             String actionTypeGUID)
    {
        final String methodName = "getGovernanceActionTypeByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionTypeElementResponse response = new GovernanceActionTypeElementResponse();
        AuditLog                            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceActionTypeHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionTypeHandler(userId,
                                                                                                                              serverName,
                                                                                                                              methodName);

            response.setElement(handler.getGovernanceActionTypeByGUID(userId, actionTypeGUID, null, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the governance action type that is the first step in a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     *
     * @return properties of the governance action type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypeElementResponse getFirstActionType(String serverName,
                                                                  String userId,
                                                                  String processGUID)
    {
        final String methodName = "getFirstActionType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceActionTypeElementResponse response = new GovernanceActionTypeElementResponse();
        AuditLog                            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceActionTypeHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionTypeHandler(userId,
                                                                                                                              serverName,
                                                                                                                              methodName);

            response.setElement(handler.getFirstActionType(userId, processGUID, null, null, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the lust of next action type defined for the governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the current governance action type
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return return the list of relationships and attached governance action types or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NextGovernanceActionTypeElementsResponse getNextGovernanceActionTypes(String serverName,
                                                                                 String userId,
                                                                                 String actionTypeGUID,
                                                                                 int    startFrom,
                                                                                 int    pageSize)
    {
        final String methodName = "getNextGovernanceActionTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NextGovernanceActionTypeElementsResponse response = new NextGovernanceActionTypeElementsResponse();
        AuditLog                                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceActionTypeHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionTypeHandler(userId,
                                                                                                                              serverName,
                                                                                                                              methodName);

            List<Relationship> relationships = handler.getNextGovernanceActionTypes(userId, actionTypeGUID, startFrom, pageSize, null, methodName);

            if (relationships != null)
            {
                OMRSRepositoryHelper repositoryHelper = instanceHandler.getRepositoryHelper(userId, serverName, methodName);

                List<NextGovernanceActionTypeElement> elements = new ArrayList<>();

                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        NextGovernanceActionTypeElement element = new NextGovernanceActionTypeElement();

                        element.setNextActionLinkGUID(relationship.getGUID());
                        element.setGuard(repositoryHelper.getStringProperty(instanceHandler.getServiceName(),
                                                                            OpenMetadataAPIMapper.GUARD_PROPERTY_NAME,
                                                                            relationship.getProperties(),
                                                                            methodName));
                        element.setMandatoryGuard(repositoryHelper.getBooleanProperty(instanceHandler.getServiceName(),
                                                                                      OpenMetadataAPIMapper.MANDATORY_GUARD_PROPERTY_NAME,
                                                                                      relationship.getProperties(),
                                                                                      methodName));
                        element.setIgnoreMultipleTriggers(repositoryHelper.getBooleanProperty(instanceHandler.getServiceName(),
                                                                                              OpenMetadataAPIMapper.IGNORE_MULTIPLE_TRIGGERS_PROPERTY_NAME,
                                                                                              relationship.getProperties(),
                                                                                              methodName));

                        element.setNextActionType(handler.getGovernanceActionTypeByGUID(userId, relationship.getEntityTwoProxy().getGUID(), null, methodName));

                        elements.add(element);
                    }
                }

                response.setElements(elements);
            }

        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Request the status and properties of an executing governance action request.
     *
     * @param serverName     name of server instance to route request to
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request.
     *
     * @return governance action properties and status or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public GovernanceActionElementResponse getGovernanceAction(String serverName,
                                                               String userId,
                                                               String governanceActionGUID)
    {
        final String methodName = "getGovernanceAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog                        auditLog = null;
        GovernanceActionElementResponse response = new GovernanceActionElementResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceActionHandler<GovernanceActionElement> handler = instanceHandler.getGovernanceActionHandler(userId, serverName, methodName);

            response.setElement(handler.getGovernanceAction(userId, governanceActionGUID, new Date(), methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the governance actions that are known to the server.
     *
     * @param serverName     name of server instance to route request to
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of governance action elements or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public GovernanceActionElementsResponse getGovernanceActions(String serverName,
                                                                 String userId,
                                                                 int    startFrom,
                                                                 int    pageSize)
    {
        final String methodName = "getGovernanceActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        GovernanceActionElementsResponse response = new GovernanceActionElementsResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceActionHandler<GovernanceActionElement> handler = instanceHandler.getGovernanceActionHandler(userId, serverName, methodName);

            response.setElements(handler.getGovernanceActions(userId, startFrom, pageSize, new Date(), methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the governance actions that are still in process.
     *
     * @param serverName     name of server instance to route request to
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of governance action elements or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public GovernanceActionElementsResponse getActiveGovernanceActions(String serverName,
                                                                       String userId,
                                                                       int    startFrom,
                                                                       int    pageSize)
    {
        final String methodName = "getActiveGovernanceActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        GovernanceActionElementsResponse response = new GovernanceActionElementsResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceActionHandler<GovernanceActionElement> handler = instanceHandler.getGovernanceActionHandler(userId, serverName, methodName);

            response.setElements(handler.getActiveGovernanceActions(userId, startFrom, pageSize, new Date(), methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
