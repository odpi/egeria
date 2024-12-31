/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.actionauthor.server;

import org.odpi.openmetadata.accessservices.governanceprogram.client.OpenGovernanceClient;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionProcessProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionProcessStepProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionTypeProperties;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The ActionAuthorRESTServices provides the implementation of the Action Author Open Metadata View Service (OMVS).
 */

public class ActionAuthorRESTServices extends TokenController
{
    private static final ActionAuthorInstanceHandler instanceHandler = new ActionAuthorInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ActionAuthorRESTServices.class),
                                                                            instanceHandler.getServiceName());
    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Default constructor
     */
    public ActionAuthorRESTServices()
    {
    }

    /* =====================================================================================================================
     * A governance action type describes a template to call a single engine action.
     */

    /**
     * Create a new metadata element to represent a governance action type.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody properties about the process to store
     *
     * @return unique identifier of the new governance action type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGovernanceActionType(String                         serverName,
                                                   GovernanceActionTypeProperties requestBody)
    {
        final String methodName = "createGovernanceActionType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setGUID(handler.createGovernanceActionType(userId, requestBody));
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
     * Update the metadata element representing a governance action type.
     *
     * @param serverName name of the service to route the request to
     * @param governanceActionTypeGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGovernanceActionType(String                                serverName,
                                                   String                                governanceActionTypeGUID,
                                                   UpdateGovernanceActionTypeRequestBody requestBody)
    {
        final String methodName = "updateGovernanceActionType";
        final String propertiesParameterName = "requestBody.getProperties";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);
        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                GovernanceActionTypeProperties properties = requestBody.getProperties();

                invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

                handler.updateGovernanceActionType(userId,
                                                   governanceActionTypeGUID,
                                                   requestBody.getMergeUpdate(),
                                                   properties);
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
     * Remove the metadata element representing a governance action type.
     *
     * @param serverName name of the service to route the request to
     * @param governanceActionTypeGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeGovernanceActionType(String          serverName,
                                                   String          governanceActionTypeGUID,
                                                   NullRequestBody requestBody)
    {
        final String methodName = "removeGovernanceActionType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            handler.removeGovernanceActionType(userId, governanceActionTypeGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of governance action type metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypesResponse findGovernanceActionTypes(String            serverName,
                                                                   boolean           startsWith,
                                                                   boolean           endsWith,
                                                                   boolean           ignoreCase,
                                                                   int               startFrom,
                                                                   int               pageSize,
                                                                   FilterRequestBody requestBody)
    {
        final String methodName = "findGovernanceActionTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionTypesResponse response = new GovernanceActionTypesResponse();
        AuditLog                             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setElements(handler.findGovernanceActionTypes(userId,
                                                                       instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                       startFrom,
                                                                       pageSize,
                                                                       requestBody.getEffectiveTime()));
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
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypesResponse getGovernanceActionTypesByName(String            serverName,
                                                                        int               startFrom,
                                                                        int               pageSize,
                                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionTypesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionTypesResponse response = new GovernanceActionTypesResponse();
        AuditLog                             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setElements(handler.getGovernanceActionTypesByName(userId,
                                                                            requestBody.getFilter(),
                                                                            startFrom,
                                                                            pageSize,
                                                                            requestBody.getEffectiveTime()));
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
     * @param governanceActionTypeGUID unique identifier of the governance action type
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypeResponse getGovernanceActionTypeByGUID(String serverName,
                                                                      String governanceActionTypeGUID)
    {
        final String methodName = "getGovernanceActionTypeByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionTypeResponse response = new GovernanceActionTypeResponse();
        AuditLog                            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            response.setElement(handler.getGovernanceActionTypeByGUID(userId, governanceActionTypeGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /* =====================================================================================================================
     * A governance action process describes a well-defined series of steps that gets something done.
     * The steps are defined using GovernanceActionProcessSteps.
     */

    /**
     * Create a new metadata element to represent a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody properties about the process to store and status value for the new process (default = ACTIVE)
     *
     * @return unique identifier of the new process or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public GUIDResponse createGovernanceActionProcess(String                                serverName,
                                                      NewGovernanceActionProcessRequestBody requestBody)
    {
        final String  methodName = "createGovernanceActionProcess";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                GovernanceActionProcessProperties processProperties = requestBody.getProperties();

                response.setGUID(handler.createGovernanceActionProcess(userId, processProperties, requestBody.getProcessStatus()));
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
     * Update the metadata element representing a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGovernanceActionProcess(String                                   serverName,
                                                      String                                   processGUID,
                                                      UpdateGovernanceActionProcessRequestBody requestBody)
    {
        final String methodName = "updateGovernanceActionProcess";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                handler.updateGovernanceActionProcess(userId,
                                                      processGUID,
                                                      requestBody.getMergeUpdate(),
                                                      requestBody.getProcessStatus(),
                                                      requestBody.getProperties());
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
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the metadata element to publish
     * @param requestBody null request body
     *
     * @return
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse publishGovernanceActionProcess(String          serverName,
                                                       String          processGUID,
                                                       NullRequestBody requestBody)
    {
        final String methodName = "publishGovernanceActionProcess";
        final String processGUIDParameterName = "processGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            handler.publishGovernanceActionProcess(userId, processGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the process is first created).
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the metadata element to withdraw
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawGovernanceActionProcess(String          serverName,
                                                        String          processGUID,
                                                        NullRequestBody requestBody)
    {
        final String methodName = "withdrawGovernanceActionProcess";
        final String processGUIDParameterName = "processGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            handler.withdrawGovernanceActionProcess(userId, processGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the metadata element representing a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeGovernanceActionProcess(String          serverName,
                                                      String          processGUID,
                                                      NullRequestBody requestBody)
    {
        final String methodName = "removeGovernanceActionProcess";
        final String processGUIDParameterName = "processGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            handler.removeGovernanceActionProcess(userId, processGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of governance action process metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
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
                                                                                 boolean                 startsWith,
                                                                                 boolean                 endsWith,
                                                                                 boolean                 ignoreCase,
                                                                                 int                     startFrom,
                                                                                 int                     pageSize,
                                                                                 FilterRequestBody       requestBody)
    {
        final String methodName = "findGovernanceActionProcesses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessElementsResponse response = new GovernanceActionProcessElementsResponse();
        AuditLog                                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setElements(handler.getGovernanceActionProcessesByName(userId,
                                                                                instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                                startFrom,
                                                                                pageSize,
                                                                                requestBody.getEffectiveTime()));
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
                                                                                      int             startFrom,
                                                                                      int             pageSize,
                                                                                      FilterRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionProcessesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessElementsResponse response = new GovernanceActionProcessElementsResponse();
        AuditLog                                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setElements(handler.getGovernanceActionProcessesByName(userId,
                                                                                requestBody.getFilter(),
                                                                                startFrom,
                                                                                pageSize,
                                                                                requestBody.getEffectiveTime()));
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
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessElementResponse getGovernanceActionProcessByGUID(String serverName,
                                                                                   String processGUID)
    {
        final String methodName = "getGovernanceActionProcessByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessElementResponse response = new GovernanceActionProcessElementResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            response.setElement(handler.getGovernanceActionProcessByGUID(userId, processGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier
     * along with the flow definition describing its implementation.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the requested metadata element
     * @param requestBody effectiveTime
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessGraphResponse getGovernanceActionProcessGraph(String                   serverName,
                                                                                String                   processGUID,
                                                                                ResultsRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionProcessGraph";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessGraphResponse response = new GovernanceActionProcessGraphResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGovernanceActionProcessGraph(userId, processGUID, requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getGovernanceActionProcessGraph(userId, processGUID, new Date()));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /* =====================================================================================================================
     * A governance action process step describes a step in a governance action process
     */

    /**
     * Create a new metadata element to represent a governance action process step.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody properties about the process to store
     *
     * @return unique identifier of the new governance action process step or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGovernanceActionProcessStep(String                                serverName,
                                                          GovernanceActionProcessStepProperties requestBody)
    {
        final String methodName = "createGovernanceActionProcessStep";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setGUID(handler.createGovernanceActionProcessStep(userId, requestBody));
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
     * Update the metadata element representing a governance action process step.
     *
     * @param serverName name of the service to route the request to
     * @param processStepGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGovernanceActionProcessStep(String                                       serverName,
                                                          String                                       processStepGUID,
                                                          UpdateGovernanceActionProcessStepRequestBody requestBody)
    {
        final String methodName = "updateGovernanceActionProcessStep";
        final String propertiesParameterName = "requestBody.getProperties";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                invalidParameterHandler.validateObject(requestBody.getProperties(), propertiesParameterName, methodName);

                handler.updateGovernanceActionProcessStep(userId,
                                                          processStepGUID,
                                                          requestBody.getMergeUpdate(),
                                                          requestBody.getProperties());
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
     * Remove the metadata element representing a governance action process step.
     *
     * @param serverName name of the service to route the request to
     * @param processStepGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeGovernanceActionProcessStep(String          serverName,
                                                          String          processStepGUID,
                                                          NullRequestBody requestBody)
    {
        final String methodName = "removeGovernanceActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            handler.removeGovernanceActionProcessStep(userId, processStepGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of governance action process step metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessStepsResponse findGovernanceActionProcessSteps(String                  serverName,
                                                                                 boolean                 startsWith,
                                                                                 boolean                 endsWith,
                                                                                 boolean                 ignoreCase,
                                                                                 int                     startFrom,
                                                                                 int                     pageSize,
                                                                                 FilterRequestBody       requestBody)
    {
        final String methodName = "findGovernanceActionProcessSteps";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessStepsResponse response = new GovernanceActionProcessStepsResponse();
        AuditLog                             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setElements(handler.findGovernanceActionProcessSteps(userId,
                                                                              instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                              startFrom,
                                                                              pageSize,
                                                                              requestBody.getEffectiveTime()));
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
     * Retrieve the list of governance action process step metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessStepsResponse getGovernanceActionProcessStepsByName(String            serverName,
                                                                                      int               startFrom,
                                                                                      int               pageSize,
                                                                                      FilterRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionProcessStepsByName";
        
        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessStepsResponse response = new GovernanceActionProcessStepsResponse();
        AuditLog                             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setElements(handler.getGovernanceActionProcessStepsByName(userId,
                                                                                   requestBody.getFilter(),
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   requestBody.getEffectiveTime()));
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
     * Retrieve the governance action process step metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param processStepGUID unique identifier of the governance action process step
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessStepResponse getGovernanceActionProcessStepByGUID(String serverName,
                                                                                    String processStepGUID)
    {
        final String methodName = "getGovernanceActionProcessStepByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessStepResponse response = new GovernanceActionProcessStepResponse();
        AuditLog                            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            response.setElement(handler.getGovernanceActionProcessStepByGUID(userId, processStepGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Set up a link between a governance action process and a governance action process step.  This defines the first
     * step in the process.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the governance action process
     * @param processStepGUID unique identifier of the governance action process step
     * @param requestBody optional guard
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupFirstActionProcessStep(String            serverName,
                                                    String            processGUID,
                                                    String            processStepGUID,
                                                    FilterRequestBody requestBody)
    {
        final String methodName = "setupFirstActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.setupFirstActionProcessStep(userId, processGUID, processStepGUID, requestBody.getFilter());
            }
            else
            {
                handler.setupFirstActionProcessStep(userId, processGUID, processStepGUID, null);
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
     * Return the governance action process step that is the first step in a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the governance action process
     *
     * @return properties of the governance action process step or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public FirstGovernanceActionProcessStepResponse getFirstActionProcessStep(String serverName,
                                                                              String processGUID)
    {
        final String methodName = "getFirstActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        FirstGovernanceActionProcessStepResponse response = new FirstGovernanceActionProcessStepResponse();
        AuditLog                            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            response.setElement(handler.getFirstActionProcessStep(userId, processGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the link between a governance process and that governance action process step that defines its first step.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the governance action process
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeFirstProcessStep(String          serverName,
                                               String          processGUID,
                                               NullRequestBody requestBody)
    {
        final String methodName = "removeFirstActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            handler.removeFirstActionProcessStep(userId, processGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Add a link between two governance action process steps to show that one follows on from the other when a governance action process
     * is executing.
     *
     * @param serverName name of the service to route the request to
     * @param currentProcessStepGUID unique identifier of the governance action process step that defines the previous step in the governance action process
     * @param nextProcessStepGUID unique identifier of the governance action process step that defines the next step in the governance action process
     * @param requestBody guard required for this next step to proceed - or null for always run the next step plus flags.
     *
     * @return unique identifier of the new link or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse setupNextActionProcessStep(String                                     serverName,
                                                   String                                     currentProcessStepGUID,
                                                   String                                     nextProcessStepGUID,
                                                   NextGovernanceActionProcessStepRequestBody requestBody)
    {
        final String methodName = "setupNextActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setGUID(handler.setupNextActionProcessStep(userId,
                                                                    currentProcessStepGUID,
                                                                    nextProcessStepGUID,
                                                                    requestBody.getGuard(),
                                                                    requestBody.getMandatoryGuard()));
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
     * Update the properties of the link between two governance action process steps that shows that one follows on from the other when a governance
     * action process is executing.
     *
     * @param serverName name of the service to route the request to
     * @param nextProcessStepLinkGUID unique identifier of the relationship between the governance action process steps
     * @param requestBody guard required for this next step to proceed - or null for always run the next step - and flags
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse updateNextActionProcessStep(String                                     serverName,
                                                    String                                     nextProcessStepLinkGUID,
                                                    NextGovernanceActionProcessStepRequestBody requestBody)
    {
        final String methodName = "updateNextActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                handler.updateNextActionProcessStep(userId,
                                                    nextProcessStepLinkGUID,
                                                    requestBody.getGuard(),
                                                    requestBody.getMandatoryGuard());
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
     * Return the list of next action process step defined for the governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param processStepGUID unique identifier of the current governance action process step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return return the list of relationships and attached governance action process steps or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NextGovernanceActionProcessStepsResponse getNextProcessSteps(String serverName,
                                                                        String processStepGUID,
                                                                        int    startFrom,
                                                                        int    pageSize)
    {
        final String methodName = "getNextGovernanceActionProcessSteps";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        NextGovernanceActionProcessStepsResponse response = new NextGovernanceActionProcessStepsResponse();
        AuditLog                                 auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            response.setElements(handler.getNextGovernanceActionProcessSteps(userId,
                                                                             processStepGUID,
                                                                             startFrom,
                                                                             pageSize));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove a follow-on step from a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param actionLinkGUID unique identifier of the relationship between the governance action process steps
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid or
     *  UserNotAuthorizedException the user is not authorized to issue this request or
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeNextActionProcessStep(String          serverName,
                                                    String          actionLinkGUID,
                                                    NullRequestBody requestBody)
    {
        final String methodName = "removeNextActionProcessStep";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            handler.removeNextActionProcessStep(userId, actionLinkGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
