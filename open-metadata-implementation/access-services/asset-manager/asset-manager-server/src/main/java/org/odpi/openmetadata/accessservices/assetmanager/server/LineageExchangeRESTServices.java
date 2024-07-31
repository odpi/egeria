/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.handlers.ProcessExchangeHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.DataFlowElementResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DataFlowElementsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ControlFlowElementResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ControlFlowElementsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ProcessCallElementResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ProcessCallElementsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.LineageMappingElementResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.LineageMappingElementsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessContainmentProperties;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.ControlFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.DataFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.LineageMappingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.ProcessCallProperties;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * LineageExchangeRESTServices is the server-side implementation of the Asset Manager OMAS's
 * support for processes, ports and lineage mapping.  It matches the LineageExchangeClient.
 */
public class LineageExchangeRESTServices
{
    private static final AssetManagerInstanceHandler instanceHandler = new AssetManagerInstanceHandler();
    private static final RESTCallLogger              restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(LineageExchangeRESTServices.class),
                                                                                          instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public LineageExchangeRESTServices()
    {
    }


    /* =====================================================================================================================
     * A process describes a well defined series of steps that gets something done.
     */

    /**
     * Create a new metadata element to represent a process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param requestBody properties about the process to store
     *
     * @return unique identifier of the new process or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createProcess(String             serverName,
                                      String             userId,
                                      boolean            assetManagerIsHome,
                                      ProcessRequestBody requestBody)
    {
        final String methodName = "createProcess";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createProcess(userId,
                                                       requestBody.getMetadataCorrelationProperties(),
                                                       assetManagerIsHome,
                                                       requestBody.getElementProperties(),
                                                       requestBody.getProcessStatus(),
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
     * Create a new metadata element to represent a process using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new process or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createProcessFromTemplate(String              serverName,
                                                  String              userId,
                                                  boolean             assetManagerIsHome,
                                                  String              templateGUID,
                                                  TemplateRequestBody requestBody)
    {
        final String methodName = "createProcessFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createProcessFromTemplate(userId,
                                                                   requestBody.getMetadataCorrelationProperties(),
                                                                   assetManagerIsHome,
                                                                   templateGUID,
                                                                   requestBody.getElementProperties(),
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
     * Update the metadata element representing a process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateProcess(String             serverName,
                                      String             userId,
                                      String             processGUID,
                                      boolean            isMergeUpdate,
                                      boolean            forLineage,
                                      boolean            forDuplicateProcessing,
                                      ProcessRequestBody requestBody)
    {
        final String methodName = "updateProcess";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

                handler.updateProcess(userId,
                                      requestBody.getMetadataCorrelationProperties(),
                                      processGUID,
                                      isMergeUpdate,
                                      requestBody.getElementProperties(),
                                      forLineage,
                                      forDuplicateProcessing,
                                      requestBody.getEffectiveTime(),
                                      methodName);
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
     * Update the status of the metadata element representing a process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the process to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody new status for the process
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateProcessStatus(String                   serverName,
                                            String                   userId,
                                            String                   processGUID,
                                            boolean                  forLineage,
                                            boolean                  forDuplicateProcessing,
                                            ProcessStatusRequestBody requestBody)
    {
        final String methodName = "updateProcessStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

                handler.updateProcessStatus(userId,
                                            requestBody.getMetadataCorrelationProperties(),
                                            processGUID,
                                            requestBody.getProcessStatus(),
                                            forLineage,
                                            forDuplicateProcessing,
                                            requestBody.getEffectiveTime(),
                                            methodName);
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
     * Create a parent-child relationship between two processes.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param parentProcessGUID unique identifier of the process in the external process manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external process manager that is to be the nested sub-process
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupProcessParent(String                  serverName,
                                           String                  userId,
                                           String                  parentProcessGUID,
                                           String                  childProcessGUID,
                                           boolean                 assetManagerIsHome,
                                           boolean                 forLineage,
                                           boolean                 forDuplicateProcessing,
                                           RelationshipRequestBody requestBody)
    {
        final String methodName = "setupProcessParent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ProcessContainmentProperties properties)
                {
                    handler.setupProcessParent(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               assetManagerIsHome,
                                               parentProcessGUID,
                                               childProcessGUID,
                                               properties.getProcessContainmentType(),
                                               properties.getEffectiveFrom(),
                                               properties.getEffectiveTo(),
                                               forLineage,
                                               forDuplicateProcessing,
                                               requestBody.getEffectiveTime(),
                                               methodName);
                }
                else
                {
                    handler.setupProcessParent(userId,
                                               null,
                                               null,
                                               assetManagerIsHome,
                                               parentProcessGUID,
                                               childProcessGUID,
                                               null,
                                               null,
                                               null,
                                               forLineage,
                                               forDuplicateProcessing,
                                               requestBody.getEffectiveTime(),
                                               methodName);
                }
            }
            else
            {
                handler.setupProcessParent(userId,
                                           null,
                                           null,
                                           assetManagerIsHome,
                                           parentProcessGUID,
                                           childProcessGUID,
                                           null,
                                           null,
                                           null,
                                           forLineage,
                                           forDuplicateProcessing,
                                           new Date(),
                                           methodName);
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
     * Remove a parent-child relationship between two processes.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param parentProcessGUID unique identifier of the process in the external process manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external process manager that is to be the nested sub-process
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProcessParent(String                        serverName,
                                           String                        userId,
                                           String                        parentProcessGUID,
                                           String                        childProcessGUID,
                                           boolean                       forLineage,
                                           boolean                       forDuplicateProcessing,
                                           EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearProcessParent";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearProcessParent(userId,
                                           requestBody.getAssetManagerGUID(),
                                           requestBody.getAssetManagerName(),
                                           parentProcessGUID,
                                           childProcessGUID,
                                           forLineage,
                                           forDuplicateProcessing,
                                           requestBody.getEffectiveTime(),
                                           methodName);
            }
            else
            {
                handler.clearProcessParent(userId,
                                           null,
                                           null,
                                           parentProcessGUID,
                                           childProcessGUID,
                                           forLineage,
                                           forDuplicateProcessing,
                                           new Date(),
                                           methodName);
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
     * Update the zones for the process so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to publish
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse publishProcess(String                        serverName,
                                       String                        userId,
                                       String                        processGUID,
                                       boolean                       forLineage,
                                       boolean                       forDuplicateProcessing,
                                       EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "publishProcess";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.publishProcess(userId,
                                       processGUID,
                                       forLineage,
                                       forDuplicateProcessing,
                                       requestBody.getEffectiveTime(),
                                       methodName);
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
     * Update the zones for the process so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the process is first created).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to withdraw
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse withdrawProcess(String                        serverName,
                                        String                        userId,
                                        String                        processGUID,
                                        boolean                       forLineage,
                                        boolean                       forDuplicateProcessing,
                                        EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "withdrawProcess";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.withdrawProcess(userId,
                                        processGUID,
                                        forLineage,
                                        forDuplicateProcessing,
                                        requestBody.getEffectiveTime(),
                                        methodName);
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
     * Remove the metadata element representing a process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeProcess(String            serverName,
                                      String            userId,
                                      String            processGUID,
                                      boolean           forLineage,
                                      boolean           forDuplicateProcessing,
                                      UpdateRequestBody requestBody)
    {
        final String methodName = "removeProcess";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeProcess(userId,
                                      requestBody.getMetadataCorrelationProperties(),
                                      processGUID,
                                      forLineage,
                                      forDuplicateProcessing,
                                      requestBody.getEffectiveTime(),
                                      methodName);
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
     * Retrieve the list of process metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElementsResponse findProcesses(String                  serverName,
                                                 String                  userId,
                                                 int                     startFrom,
                                                 int                     pageSize,
                                                 boolean                 forLineage,
                                                 boolean                 forDuplicateProcessing,
                                                 SearchStringRequestBody requestBody)
    {
        final String methodName = "findProcesses";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProcessElementsResponse response = new ProcessElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.findProcesses(userId,
                                                              requestBody.getAssetManagerGUID(),
                                                              requestBody.getAssetManagerName(),
                                                              requestBody.getSearchString(),
                                                              requestBody.getSearchStringParameterName(),
                                                              startFrom,
                                                              pageSize,
                                                              forLineage,
                                                              forDuplicateProcessing,
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
     * Return the list of processes associated with the process manager.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return list of metadata elements describing the processes associated with the requested asset manager or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElementsResponse getProcessesForAssetManager(String                        serverName,
                                                               String                        userId,
                                                               int                           startFrom,
                                                               int                           pageSize,
                                                               boolean                       forLineage,
                                                               boolean                       forDuplicateProcessing,
                                                               EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getProcessesForAssetManager";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProcessElementsResponse response = new ProcessElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getProcessesForAssetManager(userId,
                                                                            requestBody.getAssetManagerGUID(),
                                                                            requestBody.getAssetManagerName(),
                                                                            startFrom,
                                                                            pageSize,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
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
     * Retrieve the list of process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElementsResponse getProcessesByName(String          serverName,
                                                      String          userId,
                                                      int             startFrom,
                                                      int             pageSize,
                                                      boolean         forLineage,
                                                      boolean         forDuplicateProcessing,
                                                      NameRequestBody requestBody)
    {
        final String methodName = "getProcessesByName";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProcessElementsResponse response = new ProcessElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getProcessesByName(userId,
                                                                   requestBody.getAssetManagerGUID(),
                                                                   requestBody.getAssetManagerName(),
                                                                   requestBody.getName(),
                                                                   requestBody.getNameParameterName(),
                                                                   startFrom,
                                                                   pageSize,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
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
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElementResponse getProcessByGUID(String                        serverName,
                                                   String                        userId,
                                                   String                        processGUID,
                                                   boolean                       forLineage,
                                                   boolean                       forDuplicateProcessing,
                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getProcessByGUID";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProcessElementResponse response = new ProcessElementResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getProcessByGUID(userId,
                                                             requestBody.getAssetManagerGUID(),
                                                             requestBody.getAssetManagerName(),
                                                             processGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             requestBody.getEffectiveTime(),
                                                             methodName));
            }
            else
            {
                response.setElement(handler.getProcessByGUID(userId,
                                                             null,
                                                             null,
                                                             processGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             new Date(),
                                                             methodName));
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
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return parent process element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElementResponse getProcessParent(String                        serverName,
                                                   String                        userId,
                                                   String                        processGUID,
                                                   boolean                       forLineage,
                                                   boolean                       forDuplicateProcessing,
                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getProcessParent";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProcessElementResponse response = new ProcessElementResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getProcessParent(userId,
                                                             requestBody.getAssetManagerGUID(),
                                                             requestBody.getAssetManagerName(),
                                                             processGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             requestBody.getEffectiveTime(),
                                                             methodName));
            }
            else
            {
                response.setElement(handler.getProcessParent(userId,
                                                             null,
                                                             null,
                                                             processGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             new Date(),
                                                             methodName));
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
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return list of process elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessElementsResponse getSubProcesses(String                        serverName,
                                                   String                        userId,
                                                   String                        processGUID,
                                                   int                           startFrom,
                                                   int                           pageSize,
                                                   boolean                       forLineage,
                                                   boolean                       forDuplicateProcessing,
                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSubProcesses";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProcessElementsResponse response = new ProcessElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getSubProcesses(userId,
                                                                requestBody.getAssetManagerGUID(),
                                                                requestBody.getAssetManagerName(),
                                                                processGUID,
                                                                startFrom,
                                                                pageSize,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                requestBody.getEffectiveTime(),
                                                                methodName));
            }
            else
            {
                response.setElementList(handler.getSubProcesses(userId,
                                                                null,
                                                                null,
                                                                processGUID,
                                                                startFrom,
                                                                pageSize,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                new Date(),
                                                                methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /* ===============================================================================
     * A process typically contains ports that show the flow of data and control to and from it.
     */

    /**
     * Create a new metadata element to represent a port.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the process manager can update this port
     * @param processGUID unique identifier of the process where the port is located
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the port
     *
     * @return unique identifier of the new metadata element for the port or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createPort(String          serverName,
                                   String          userId,
                                   boolean         assetManagerIsHome,
                                   String          processGUID,
                                   boolean         forLineage,
                                   boolean         forDuplicateProcessing,
                                   PortRequestBody requestBody)
    {
        final String methodName = "createPort";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createPort(userId,
                                                    requestBody.getMetadataCorrelationProperties(),
                                                    assetManagerIsHome,
                                                    processGUID,
                                                    requestBody.getElementProperties(),
                                                    forLineage,
                                                    forDuplicateProcessing,
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
     * Update the properties of the metadata element representing a port.  This call replaces
     * all existing properties with the supplied properties.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param portGUID unique identifier of the port to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody new properties for the port
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updatePort(String          serverName,
                                   String          userId,
                                   String          portGUID,
                                   boolean         forLineage,
                                   boolean         forDuplicateProcessing,
                                   PortRequestBody requestBody)
    {
        final String methodName = "updatePort";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

                handler.updatePort(userId,
                                   requestBody.getMetadataCorrelationProperties(),
                                   portGUID,
                                   requestBody.getElementProperties(),
                                   forLineage,
                                   forDuplicateProcessing,
                                   requestBody.getEffectiveTime(),
                                   methodName);
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
     * Link a port to a process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param processGUID unique identifier of the process
     * @param portGUID unique identifier of the port
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupProcessPort(String                  serverName,
                                         String                  userId,
                                         boolean                 assetManagerIsHome,
                                         String                  processGUID,
                                         String                  portGUID,
                                         boolean                 forLineage,
                                         boolean                 forDuplicateProcessing,
                                         RelationshipRequestBody requestBody)
    {
        final String methodName = "setupProcessPort";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    handler.setupProcessPort(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             assetManagerIsHome,
                                             processGUID,
                                             portGUID,
                                             requestBody.getProperties().getEffectiveFrom(),
                                             requestBody.getProperties().getEffectiveTo(),
                                             forLineage,
                                             forDuplicateProcessing,
                                             requestBody.getEffectiveTime(),
                                             methodName);
                }
                else
                {
                    handler.setupProcessPort(userId,
                                             null,
                                             null,
                                             assetManagerIsHome,
                                             processGUID,
                                             portGUID,
                                             null,
                                             null,
                                             forLineage,
                                             forDuplicateProcessing,
                                             requestBody.getEffectiveTime(),
                                             methodName);
                }
            }
            else
            {
                handler.setupProcessPort(userId,
                                         null,
                                         null,
                                         assetManagerIsHome,
                                         processGUID,
                                         portGUID,
                                         null,
                                         null,
                                         forLineage,
                                         forDuplicateProcessing,
                                         new Date(),
                                         methodName);
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
     * Unlink a port from a process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the process
     * @param portGUID unique identifier of the port
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProcessPort(String                        serverName,
                                         String                        userId,
                                         String                        processGUID,
                                         String                        portGUID,
                                         boolean                       forLineage,
                                         boolean                       forDuplicateProcessing,
                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearProcessPort";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearProcessPort(userId,
                                         requestBody.getAssetManagerGUID(),
                                         requestBody.getAssetManagerName(),
                                         processGUID,
                                         portGUID,
                                         forLineage,
                                         forDuplicateProcessing,
                                         requestBody.getEffectiveTime(),
                                         methodName);
            }
            else
            {
                handler.clearProcessPort(userId,
                                         null,
                                         null,
                                         processGUID,
                                         portGUID,
                                         forLineage,
                                         forDuplicateProcessing,
                                         new Date(),
                                         methodName);
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
     * Link two ports together to show that portTwo is an implementation of portOne. (That is, portOne delegates to
     * portTwo.)
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param portOneGUID unique identifier of the port at end 1
     * @param portTwoGUID unique identifier of the port at end 2
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupPortDelegation(String                  serverName,
                                            String                  userId,
                                            boolean                 assetManagerIsHome,
                                            String                  portOneGUID,
                                            String                  portTwoGUID,
                                            boolean                 forLineage,
                                            boolean                 forDuplicateProcessing,
                                            RelationshipRequestBody requestBody)
    {
        final String methodName = "setupPortDelegation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    handler.setupPortDelegation(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                assetManagerIsHome,
                                                portOneGUID,
                                                portTwoGUID,
                                                requestBody.getProperties().getEffectiveFrom(),
                                                requestBody.getProperties().getEffectiveTo(),
                                                forLineage,
                                                forDuplicateProcessing,
                                                new Date(),
                                                methodName);
                }
                else
                {
                    handler.setupPortDelegation(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                assetManagerIsHome,
                                                portOneGUID,
                                                portTwoGUID,
                                                null,
                                                null,
                                                forLineage,
                                                forDuplicateProcessing,
                                                new Date(),
                                                methodName);
                }
            }
            else
            {
                handler.setupPortDelegation(userId,
                                            null,
                                            null,
                                            assetManagerIsHome,
                                            portOneGUID,
                                            portTwoGUID,
                                            null,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            new Date(),
                                            methodName);
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
     * Remove the port delegation relationship between two ports.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param portOneGUID unique identifier of the port at end 1
     * @param portTwoGUID unique identifier of the port at end 2
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearPortDelegation(String                        serverName,
                                            String                        userId,
                                            String                        portOneGUID,
                                            String                        portTwoGUID,
                                            boolean                       forLineage,
                                            boolean                       forDuplicateProcessing,
                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearPortDelegation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearPortDelegation(userId,
                                            requestBody.getAssetManagerGUID(),
                                            requestBody.getAssetManagerName(),
                                            portOneGUID,
                                            portTwoGUID,
                                            forLineage,
                                            forDuplicateProcessing,
                                            requestBody.getEffectiveTime(),
                                            methodName);
            }
            else
            {
                handler.clearPortDelegation(userId,
                                            null,
                                            null,
                                            portOneGUID,
                                            portTwoGUID,
                                            forLineage,
                                            forDuplicateProcessing,
                                            new Date(),
                                            methodName);
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
     * Link a schema type to a port to show the structure of data it accepts.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param portGUID unique identifier of the port
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupPortSchemaType(String                  serverName,
                                            String                  userId,
                                            boolean                 assetManagerIsHome,
                                            String                  portGUID,
                                            String                  schemaTypeGUID,
                                            boolean                 forLineage,
                                            boolean                 forDuplicateProcessing,
                                            RelationshipRequestBody requestBody)
    {
        final String methodName = "setupPortSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    handler.setupPortSchemaType(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                assetManagerIsHome,
                                                portGUID,
                                                schemaTypeGUID,
                                                requestBody.getProperties().getEffectiveFrom(),
                                                requestBody.getProperties().getEffectiveTo(),
                                                forLineage,
                                                forDuplicateProcessing,
                                                requestBody.getEffectiveTime(),
                                                methodName);
                }
                else
                {
                    handler.setupPortSchemaType(userId,
                                                null,
                                                null,
                                                assetManagerIsHome,
                                                portGUID,
                                                schemaTypeGUID,
                                                null,
                                                null,
                                                forLineage,
                                                forDuplicateProcessing,
                                                new Date(),
                                                methodName);
                }
            }
            else
            {
                handler.setupPortSchemaType(userId,
                                            null,
                                            null,
                                            assetManagerIsHome,
                                            portGUID,
                                            schemaTypeGUID,
                                            null,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            new Date(),
                                            methodName);
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
     * Remove the schema type from a port.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param portGUID unique identifier of the port
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearPortSchemaType(String                        serverName,
                                            String                        userId,
                                            String                        portGUID,
                                            String                        schemaTypeGUID,
                                            boolean                       forLineage,
                                            boolean                       forDuplicateProcessing,
                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearPortSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearPortSchemaType(userId,
                                            requestBody.getAssetManagerGUID(),
                                            requestBody.getAssetManagerName(),
                                            portGUID,
                                            schemaTypeGUID,
                                            forLineage,
                                            forDuplicateProcessing,
                                            requestBody.getEffectiveTime(),
                                            methodName);
            }
            else
            {
                handler.clearPortSchemaType(userId,
                                            null,
                                            null,
                                            portGUID,
                                            schemaTypeGUID,
                                            forLineage,
                                            forDuplicateProcessing,
                                            new Date(),
                                            methodName);
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
     * Remove the metadata element representing a port.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param portGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removePort(String            serverName,
                                   String            userId,
                                   String            portGUID,
                                   boolean           forLineage,
                                   boolean           forDuplicateProcessing,
                                   UpdateRequestBody requestBody)
    {
        final String methodName = "removePort";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

                handler.removePort(userId, requestBody.getMetadataCorrelationProperties(), portGUID, forLineage, forDuplicateProcessing,
                                   requestBody.getEffectiveTime(), methodName);
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
     * Retrieve the list of port metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PortElementsResponse findPorts(String                  serverName,
                                          String                  userId,
                                          int                     startFrom,
                                          int                     pageSize,
                                          boolean               forLineage,
                                          boolean               forDuplicateProcessing,
                                          SearchStringRequestBody requestBody)
    {
        final String methodName = "findPorts";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        PortElementsResponse response = new PortElementsResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.findPorts(userId,
                                                          requestBody.getAssetManagerGUID(),
                                                          requestBody.getAssetManagerName(),
                                                          requestBody.getSearchString(),
                                                          requestBody.getSearchStringParameterName(),
                                                          startFrom,
                                                          pageSize,
                                                          forLineage,
                                                          forDuplicateProcessing,
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
     * Retrieve the list of ports associated with a process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the process of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PortElementsResponse getPortsForProcess(String                        serverName,
                                                   String                        userId,
                                                   String                        processGUID,
                                                   int                           startFrom,
                                                   int                           pageSize,
                                                   boolean                       forLineage,
                                                   boolean                       forDuplicateProcessing,
                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getPortsForProcess";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        PortElementsResponse response = new PortElementsResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getPortsForProcess(userId,
                                                                   requestBody.getAssetManagerGUID(),
                                                                   requestBody.getAssetManagerName(),
                                                                   processGUID,
                                                                   startFrom,
                                                                   pageSize,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   requestBody.getEffectiveTime(),
                                                                   methodName));
            }
            else
            {
                response.setElementList(handler.getPortsForProcess(userId,
                                                                   null,
                                                                   null,
                                                                   processGUID,
                                                                   startFrom,
                                                                   pageSize,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   new Date(),
                                                                   methodName));
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
     * Retrieve the list of ports that delegate to this port.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param portGUID unique identifier of the starting port
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return list of associated metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PortElementsResponse getPortUse(String                        serverName,
                                           String                        userId,
                                           String                        portGUID,
                                           int                           startFrom,
                                           int                           pageSize,
                                           boolean                       forLineage,
                                           boolean                       forDuplicateProcessing,
                                           EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getPortUse";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        PortElementsResponse response = new PortElementsResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getPortUse(userId,
                                                           requestBody.getAssetManagerGUID(),
                                                           requestBody.getAssetManagerName(),
                                                           portGUID,
                                                           startFrom,
                                                           pageSize,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           requestBody.getEffectiveTime(),
                                                           methodName));
            }
            else
            {
                response.setElementList(handler.getPortUse(userId,
                                                           null,
                                                           null,
                                                           portGUID,
                                                           startFrom,
                                                           pageSize,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           new Date(),
                                                           methodName));
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
     * Retrieve the port that this port delegates to.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param portGUID unique identifier of the starting port alias
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PortElementResponse getPortDelegation(String                        serverName,
                                                 String                        userId,
                                                 String                        portGUID,
                                                 boolean                       forLineage,
                                                 boolean                       forDuplicateProcessing,
                                                 EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getPortDelegation";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        PortElementResponse response = new PortElementResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {

                response.setElement(handler.getPortDelegation(userId,
                                                              requestBody.getAssetManagerGUID(),
                                                              requestBody.getAssetManagerName(),
                                                              portGUID,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              requestBody.getEffectiveTime(),
                                                              methodName));
            }
            else
            {
                response.setElement(handler.getPortDelegation(userId,
                                                              null,
                                                              null,
                                                              portGUID,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              new Date(),
                                                              methodName));
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
     * Retrieve the list of port metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PortElementsResponse getPortsByName(String          serverName,
                                               String          userId,
                                               int             startFrom,
                                               int             pageSize,
                                               boolean         forLineage,
                                               boolean         forDuplicateProcessing,
                                               NameRequestBody requestBody)
    {
        final String methodName = "getPortsByName";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        PortElementsResponse response = new PortElementsResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getPortsByName(userId,
                                                               requestBody.getAssetManagerGUID(),
                                                               requestBody.getAssetManagerName(),
                                                               requestBody.getName(),
                                                               requestBody.getNameParameterName(),
                                                               startFrom,
                                                               pageSize,
                                                               forLineage,
                                                               forDuplicateProcessing,
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
     * Retrieve the port metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param portGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public PortElementResponse getPortByGUID(String                        serverName,
                                             String                        userId,
                                             String                        portGUID,
                                             boolean                       forLineage,
                                             boolean                       forDuplicateProcessing,
                                             EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getPortByGUID";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        PortElementResponse response = new PortElementResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {

                response.setElement(handler.getPortByGUID(userId,
                                                          requestBody.getAssetManagerGUID(),
                                                          requestBody.getAssetManagerName(),
                                                          portGUID,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          requestBody.getEffectiveTime(),
                                                          methodName));
            }
            else
            {
                response.setElement(handler.getPortByGUID(userId,
                                                          null,
                                                          null,
                                                          portGUID,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          new Date(),
                                                          methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /* ===============================================================================
     * General linkage and classifications
     */


    /**
     * Classify a port, process or process as "BusinessSignificant" (this may affect the way that lineage is displayed).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setBusinessSignificant(String            serverName,
                                               String            userId,
                                               String            elementGUID,
                                               boolean           forLineage,
                                               boolean           forDuplicateProcessing,
                                               UpdateRequestBody requestBody)
    {
        final String methodName = "setBusinessSignificant";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.setBusinessSignificant(userId,
                                               requestBody.getMetadataCorrelationProperties(),
                                               elementGUID,
                                               forLineage,
                                               forDuplicateProcessing,
                                               requestBody.getEffectiveTime(),
                                               methodName);
            }
            else
            {
                handler.setBusinessSignificant(userId,
                                               null,
                                               elementGUID,
                                               forLineage,
                                               forDuplicateProcessing,
                                               new Date(),
                                               methodName);
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
     * Remove the "BusinessSignificant" designation from the element.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearBusinessSignificant(String            serverName,
                                                 String            userId,
                                                 String            elementGUID,
                                                 boolean           forLineage,
                                                 boolean           forDuplicateProcessing,
                                                 UpdateRequestBody requestBody)
    {
        final String methodName = "clearBusinessSignificant";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearBusinessSignificant(userId,
                                                 requestBody.getMetadataCorrelationProperties(),
                                                 elementGUID,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 requestBody.getEffectiveTime(), methodName);
            }
            else
            {
                handler.clearBusinessSignificant(userId,
                                                 null,
                                                 elementGUID,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 new Date(),
                                                 methodName);
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
     * Link two elements together to show that data flows from one to the other.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties of the relationship
     *
     * @return unique identifier of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse setupDataFlow(String                  serverName,
                                      String                  userId,
                                      String                  dataSupplierGUID,
                                      String                  dataConsumerGUID,
                                      boolean                 assetManagerIsHome,
                                      boolean                 forLineage,
                                      boolean                 forDuplicateProcessing,
                                      RelationshipRequestBody requestBody)
    {
        final String methodName = "setupDataFlow";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getProperties() instanceof DataFlowProperties dataFlowProperties))
            {
                response.setGUID(handler.setupDataFlow(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       assetManagerIsHome,
                                                       dataSupplierGUID,
                                                       dataConsumerGUID,
                                                       dataFlowProperties.getQualifiedName(),
                                                       dataFlowProperties.getDescription(),
                                                       dataFlowProperties.getFormula(),
                                                       dataFlowProperties.getEffectiveFrom(),
                                                       dataFlowProperties.getEffectiveTo(),
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       requestBody.getEffectiveTime(),
                                                       methodName));
            }
            else
            {
                response.setGUID(handler.setupDataFlow(userId,
                                                       null,
                                                       null,
                                                       assetManagerIsHome,
                                                       dataSupplierGUID,
                                                       dataConsumerGUID,
                                                       null,
                                                       null,
                                                       null,
                                                       null,
                                                       null,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       new Date(),
                                                       methodName));
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
     * Retrieve the data flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one data flow relationships between these two elements since it is used to disambiguate
     * the request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody optional name to search for
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataFlowElementResponse getDataFlow(String          serverName,
                                               String          userId,
                                               String          dataSupplierGUID,
                                               String          dataConsumerGUID,
                                               boolean         forLineage,
                                               boolean         forDuplicateProcessing,
                                               NameRequestBody requestBody)
    {
        final String methodName = "getDataFlow";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataFlowElementResponse response = new DataFlowElementResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getDataFlow(userId,
                                                        dataSupplierGUID,
                                                        dataConsumerGUID,
                                                        requestBody.getName(),
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        requestBody.getEffectiveTime(),
                                                        methodName));
            }
            else
            {
                response.setElement(handler.getDataFlow(userId,
                                                        dataSupplierGUID,
                                                        dataConsumerGUID,
                                                        null,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        new Date(),
                                                        methodName));
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
     * Update relationship between two elements that shows that data flows from one to the other.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties of the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateDataFlow(String                  serverName,
                                       String                  userId,
                                       String                  dataFlowGUID,
                                       boolean                 forLineage,
                                       boolean                 forDuplicateProcessing,
                                       RelationshipRequestBody requestBody)
    {
        final String methodName = "updateDataFlow";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getProperties() instanceof DataFlowProperties properties))
            {
                ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

                handler.updateDataFlow(userId,
                                       requestBody.getExternalSourceGUID(),
                                       requestBody.getExternalSourceName(),
                                       dataFlowGUID,
                                       properties.getQualifiedName(),
                                       properties.getDescription(),
                                       properties.getFormula(),
                                       properties.getEffectiveFrom(),
                                       properties.getEffectiveTo(),
                                       forLineage,
                                       forDuplicateProcessing,
                                       requestBody.getEffectiveTime(),
                                       methodName);
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
     * Remove the data flow relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearDataFlow(String                        serverName,
                                      String                        userId,
                                      String                        dataFlowGUID,
                                      boolean                       forLineage,
                                      boolean                       forDuplicateProcessing,
                                      EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearDataFlow";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearDataFlow(userId,
                                      requestBody.getAssetManagerGUID(),
                                      requestBody.getAssetManagerName(),
                                      dataFlowGUID,
                                      forLineage,
                                      forDuplicateProcessing,
                                      requestBody.getEffectiveTime(),
                                      methodName);
            }
            else
            {
                handler.clearDataFlow(userId,
                                      null,
                                      null,
                                      dataFlowGUID,
                                      forLineage,
                                      forDuplicateProcessing,
                                      new Date(),
                                      methodName);
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
     * Retrieve the data flow relationships linked from a specific element to the downstream consumers.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataFlowElementsResponse getDataFlowConsumers(String                        serverName,
                                                         String                        userId,
                                                         String                        dataSupplierGUID,
                                                         int                           startFrom,
                                                         int                           pageSize,
                                                         boolean                       forLineage,
                                                         boolean                       forDuplicateProcessing,
                                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getDataFlowConsumers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataFlowElementsResponse response = new DataFlowElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getDataFlowConsumers(userId,
                                                                  dataSupplierGUID,
                                                                  startFrom,
                                                                  pageSize,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName));
            }
            else
            {
                response.setElements(handler.getDataFlowConsumers(userId,
                                                                  dataSupplierGUID,
                                                                  startFrom,
                                                                  pageSize,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  new Date(),
                                                                  methodName));
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
     * Retrieve the data flow relationships linked from a specific element to the upstream suppliers.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataFlowElementsResponse getDataFlowSuppliers(String                        serverName,
                                                         String                        userId,
                                                         String                        dataConsumerGUID,
                                                         int                           startFrom,
                                                         int                           pageSize,
                                                         boolean                       forLineage,
                                                         boolean                       forDuplicateProcessing,
                                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getDataFlowSuppliers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DataFlowElementsResponse response = new DataFlowElementsResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getDataFlowSuppliers(userId,
                                                                  dataConsumerGUID,
                                                                  startFrom,
                                                                  pageSize,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName));
            }
            else
            {
                response.setElements(handler.getDataFlowSuppliers(userId,
                                                                  dataConsumerGUID,
                                                                  startFrom,
                                                                  pageSize,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  new Date(),
                                                                  methodName));
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
     * Link two elements to show that when one completes the next is started.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties of the relationship
     *
     * @return unique identifier for the control flow relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse setupControlFlow(String                  serverName,
                                         String                  userId,
                                         String                  currentStepGUID,
                                         String                  nextStepGUID,
                                         boolean                 assetManagerIsHome,
                                         boolean                 forLineage,
                                         boolean                 forDuplicateProcessing,
                                         RelationshipRequestBody requestBody)
    {
        final String methodName = "setupControlFlow";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getProperties() instanceof ControlFlowProperties properties))
            {
                response.setGUID(handler.setupControlFlow(userId,
                                                          requestBody.getExternalSourceGUID(),
                                                          requestBody.getExternalSourceName(),
                                                          assetManagerIsHome,
                                                          currentStepGUID,
                                                          nextStepGUID,
                                                          properties.getQualifiedName(),
                                                          properties.getDescription(),
                                                          properties.getGuard(),
                                                          properties.getEffectiveFrom(),
                                                          properties.getEffectiveTo(),
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          requestBody.getEffectiveTime(),
                                                          methodName));
            }
            else
            {
                response.setGUID(handler.setupControlFlow(userId,
                                                          null,
                                                          null,
                                                          assetManagerIsHome,
                                                          currentStepGUID,
                                                          nextStepGUID,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          new Date(),
                                                          methodName));
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
     * Retrieve the control flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one control flow relationships between these two elements since it is used to disambiguate
     * the request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier for this relationship
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ControlFlowElementResponse getControlFlow(String          serverName,
                                                     String          userId,
                                                     String          currentStepGUID,
                                                     String          nextStepGUID,
                                                     boolean         forLineage,
                                                     boolean         forDuplicateProcessing,
                                                     NameRequestBody requestBody)
    {
        final String methodName = "getControlFlow";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ControlFlowElementResponse response = new ControlFlowElementResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getControlFlow(userId,
                                                           currentStepGUID,
                                                           nextStepGUID,
                                                           requestBody.getName(),
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           requestBody.getEffectiveTime(),
                                                           methodName));
            }
            else
            {
                response.setElement(handler.getControlFlow(userId,
                                                           currentStepGUID,
                                                           nextStepGUID,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           new Date(),
                                                           methodName));
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
     * Update the relationship between two elements that shows that when one completes the next is started.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties of the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateControlFlow(String                  serverName,
                                          String                  userId,
                                          String                  controlFlowGUID,
                                          boolean                 forLineage,
                                          boolean                 forDuplicateProcessing,
                                          RelationshipRequestBody requestBody)
    {
        final String methodName = "updateControlFlow";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getProperties() instanceof ControlFlowProperties properties))
            {
                handler.updateControlFlow(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          controlFlowGUID,
                                          properties.getQualifiedName(),
                                          properties.getDescription(),
                                          properties.getGuard(),
                                          properties.getEffectiveFrom(),
                                          properties.getEffectiveTo(),
                                          forLineage,
                                          forDuplicateProcessing,
                                          requestBody.getEffectiveTime(),
                                          methodName);
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
     * Remove the control flow relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearControlFlow(String                        serverName,
                                         String                        userId,
                                         String                        controlFlowGUID,
                                         boolean                       forLineage,
                                         boolean                       forDuplicateProcessing,
                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearControlFlow";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearControlFlow(userId,
                                         requestBody.getAssetManagerGUID(),
                                         requestBody.getAssetManagerName(),
                                         controlFlowGUID,
                                         forLineage,
                                         forDuplicateProcessing,
                                         requestBody.getEffectiveTime(),
                                         methodName);
            }
            else
            {
                handler.clearControlFlow(userId,
                                         null,
                                         null,
                                         controlFlowGUID,
                                         forLineage,
                                         forDuplicateProcessing,
                                         new Date(),
                                         methodName);
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
     * Retrieve the control relationships linked from a specific element to the possible next elements in the process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the current step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ControlFlowElementsResponse getControlFlowNextSteps(String                        serverName,
                                                               String                        userId,
                                                               String                        currentStepGUID,
                                                               int                           startFrom,
                                                               int                           pageSize,
                                                               boolean                       forLineage,
                                                               boolean                       forDuplicateProcessing,
                                                               EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getControlFlowNextSteps";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ControlFlowElementsResponse response = new ControlFlowElementsResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getControlFlowNextSteps(userId,
                                                                     currentStepGUID,
                                                                     startFrom,
                                                                     pageSize,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     requestBody.getEffectiveTime(),
                                                                     methodName));
            }
            else
            {
                response.setElements(handler.getControlFlowNextSteps(userId,
                                                                     currentStepGUID,
                                                                     startFrom,
                                                                     pageSize,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     new Date(),
                                                                     methodName));
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
     * Retrieve the control relationships linked from a specific element to the possible previous elements in the process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the previous step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ControlFlowElementsResponse getControlFlowPreviousSteps(String                        serverName,
                                                                   String                        userId,
                                                                   String                        currentStepGUID,
                                                                   int                           startFrom,
                                                                   int                           pageSize,
                                                                   boolean                       forLineage,
                                                                   boolean                       forDuplicateProcessing,
                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getControlFlowPreviousSteps";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ControlFlowElementsResponse response = new ControlFlowElementsResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getControlFlowPreviousSteps(userId,
                                                                         currentStepGUID,
                                                                         startFrom,
                                                                         pageSize,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         requestBody.getEffectiveTime(),
                                                                         methodName));
            }
            else
            {
                response.setElements(handler.getControlFlowPreviousSteps(userId,
                                                                         currentStepGUID,
                                                                         startFrom,
                                                                         pageSize,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         new Date(),
                                                                         methodName));
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
     * Link two elements together to show a request-response call between them.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties of the relationship
     *
     * @return unique identifier of the new relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse setupProcessCall(String                  serverName,
                                         String                  userId,
                                         String                  callerGUID,
                                         String                  calledGUID,
                                         boolean                 assetManagerIsHome,
                                         boolean                 forLineage,
                                         boolean                 forDuplicateProcessing,
                                         RelationshipRequestBody requestBody)
    {
        final String methodName = "setupProcessCall";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getProperties() instanceof ProcessCallProperties properties))
            {
                response.setGUID(handler.setupProcessCall(userId,
                                                          requestBody.getExternalSourceGUID(),
                                                          requestBody.getExternalSourceName(),
                                                          assetManagerIsHome,
                                                          callerGUID,
                                                          calledGUID,
                                                          properties.getQualifiedName(),
                                                          properties.getDescription(),
                                                          properties.getFormula(),
                                                          properties.getEffectiveFrom(),
                                                          properties.getEffectiveTo(),
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          requestBody.getEffectiveTime(),
                                                          methodName));
            }
            else
            {
                response.setGUID(handler.setupProcessCall(userId,
                                                          null,
                                                          null,
                                                          assetManagerIsHome,
                                                          callerGUID,
                                                          calledGUID,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          new Date(),
                                                          methodName));
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
     * Retrieve the process call relationship between two elements.  The qualifiedName is optional unless there
     * is more than one process call relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier for this relationship
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessCallElementResponse getProcessCall(String          serverName,
                                                     String          userId,
                                                     String          callerGUID,
                                                     String          calledGUID,
                                                     boolean         forLineage,
                                                     boolean         forDuplicateProcessing,
                                                     NameRequestBody requestBody)
    {
        final String methodName = "getProcessCall";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProcessCallElementResponse response = new ProcessCallElementResponse();
        AuditLog                  auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getProcessCall(userId,
                                                           callerGUID,
                                                           calledGUID,
                                                           requestBody.getName(),
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           requestBody.getEffectiveTime(),
                                                           methodName));
            }
            else
            {
                response.setElement(handler.getProcessCall(userId,
                                                           callerGUID,
                                                           calledGUID,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           new Date(),
                                                           methodName));
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
     * Update the relationship between two elements that shows a request-response call between them.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processCallGUID unique identifier of the process call relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties of the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateProcessCall(String                  serverName,
                                          String                  userId,
                                          String                  processCallGUID,
                                          boolean                 forLineage,
                                          boolean                 forDuplicateProcessing,
                                          RelationshipRequestBody requestBody)
    {
        final String methodName = "updateProcessCall";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getProperties() instanceof ProcessCallProperties properties))
            {
                handler.updateProcessCall(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          processCallGUID,
                                          properties.getQualifiedName(),
                                          properties.getDescription(),
                                          properties.getFormula(),
                                          properties.getEffectiveFrom(),
                                          properties.getEffectiveTo(),
                                          forLineage,
                                          forDuplicateProcessing,
                                          requestBody.getEffectiveTime(),
                                          methodName);
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
     * Remove the process call relationship.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processCallGUID unique identifier of the process call relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearProcessCall(String                        serverName,
                                         String                        userId,
                                         String                        processCallGUID,
                                         boolean                       forLineage,
                                         boolean                       forDuplicateProcessing,
                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearProcessCall";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearProcessCall(userId,
                                         requestBody.getAssetManagerGUID(),
                                         requestBody.getAssetManagerName(),
                                         processCallGUID,
                                         forLineage,
                                         forDuplicateProcessing,
                                         requestBody.getEffectiveTime(),
                                         methodName);
            }
            else
            {
                handler.clearProcessCall(userId,
                                         null,
                                         null,
                                         processCallGUID,
                                         forLineage,
                                         forDuplicateProcessing,
                                         new Date(),
                                         methodName);
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
     * Retrieve the process call relationships linked from a specific element to the elements it calls.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessCallElementsResponse getProcessCalled(String                        serverName,
                                                        String                        userId,
                                                        String                        callerGUID,
                                                        int                           startFrom,
                                                        int                           pageSize,
                                                        boolean                       forLineage,
                                                        boolean                       forDuplicateProcessing,
                                                        EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getProcessCalled";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProcessCallElementsResponse response = new ProcessCallElementsResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getProcessCalled(userId,
                                                              callerGUID,
                                                              startFrom,
                                                              pageSize,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              requestBody.getEffectiveTime(),
                                                              methodName));
            }
            else
            {
                response.setElements(handler.getProcessCalled(userId,
                                                              callerGUID,
                                                              startFrom,
                                                              pageSize,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              new Date(),
                                                              methodName));
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
     * Retrieve the process call relationships linked from a specific element to its callers.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param calledGUID unique identifier of the element that is processing the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ProcessCallElementsResponse getProcessCallers(String                        serverName,
                                                         String                        userId,
                                                         String                        calledGUID,
                                                         int                           startFrom,
                                                         int                           pageSize,
                                                         boolean                       forLineage,
                                                         boolean                       forDuplicateProcessing,
                                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getProcessCallers";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ProcessCallElementsResponse response = new ProcessCallElementsResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getProcessCallers(userId,
                                                               calledGUID,
                                                               startFrom,
                                                               pageSize,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               requestBody.getEffectiveTime(),
                                                               methodName));
            }
            else
            {
                response.setElements(handler.getProcessCallers(userId,
                                                               calledGUID,
                                                               startFrom,
                                                               pageSize,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               new Date(),
                                                               methodName));
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
     * Link to elements together to show that they are part of the lineage of the data that is moving
     * between the processes.  Typically, the lineage relationships stitch together processes and data assets
     * supported by different technologies.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse setupLineageMapping(String                  serverName,
                                            String                  userId,
                                            String                  sourceElementGUID,
                                            String                  destinationElementGUID,
                                            boolean                 forLineage,
                                            boolean                 forDuplicateProcessing,
                                            RelationshipRequestBody requestBody)
    {
        final String methodName = "setupLineageMapping";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            String guid;
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataFlowProperties properties)
                {
                    guid = handler.setupLineageMapping(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       sourceElementGUID,
                                                       destinationElementGUID,
                                                       properties.getQualifiedName(),
                                                       properties.getDescription(),
                                                       properties.getEffectiveFrom(),
                                                       properties.getEffectiveTo(),
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       requestBody.getEffectiveTime(),
                                                       methodName);
                }
                else
                {
                    guid = handler.setupLineageMapping(userId,
                                                       null,
                                                       null,
                                                       sourceElementGUID,
                                                       destinationElementGUID,
                                                       null,
                                                       null,
                                                       null,
                                                       null,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       requestBody.getEffectiveTime(),
                                                       methodName);
                }
            }
            else
            {
                guid = handler.setupLineageMapping(userId,
                                                   null,
                                                   null,
                                                   sourceElementGUID,
                                                   destinationElementGUID,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   new Date(),
                                                   methodName);
            }

            response.setGUID(guid);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the process call relationship between two elements.  The qualifiedName is optional unless there
     * is more than one process call relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier for this relationship
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public LineageMappingElementResponse getLineageMapping(String          serverName,
                                                           String          userId,
                                                           String          sourceElementGUID,
                                                           String          destinationElementGUID,
                                                           boolean         forLineage,
                                                           boolean         forDuplicateProcessing,
                                                           NameRequestBody requestBody)
    {
        final String methodName = "getLineageMapping";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LineageMappingElementResponse response = new LineageMappingElementResponse();
        AuditLog                      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getLineageMapping(userId,
                                                              sourceElementGUID,
                                                              destinationElementGUID,
                                                              requestBody.getName(),
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              requestBody.getEffectiveTime(),
                                                              methodName));
            }
            else
            {
                response.setElement(handler.getLineageMapping(userId,
                                                              sourceElementGUID,
                                                              destinationElementGUID,
                                                              null,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              new Date(),
                                                              methodName));
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
     * Update the lineage mapping between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param lineageMappingGUID unique identifier of the relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateLineageMapping(String                  serverName,
                                             String                  userId,
                                             String                  lineageMappingGUID,
                                             boolean                 forLineage,
                                             boolean                 forDuplicateProcessing,
                                             RelationshipRequestBody requestBody)
    {
        final String methodName = "updateLineageMapping";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if ((requestBody != null) && (requestBody.getProperties() instanceof LineageMappingProperties properties))
            {
                handler.updateLineageMapping(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             lineageMappingGUID,
                                             properties.getQualifiedName(),
                                             properties.getDescription(),
                                             properties.getEffectiveFrom(),
                                             properties.getEffectiveTo(),
                                             forLineage,
                                             forDuplicateProcessing,
                                             requestBody.getEffectiveTime(),
                                             methodName);
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
     * Remove the lineage mapping between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param lineageMappingGUID unique identifier of the relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearLineageMapping(String                        serverName,
                                            String                        userId,
                                            String                        lineageMappingGUID,
                                            boolean                       forLineage,
                                            boolean                       forDuplicateProcessing,
                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearLineageMapping";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearLineageMapping(userId,
                                            requestBody.getAssetManagerGUID(),
                                            requestBody.getAssetManagerName(),
                                            lineageMappingGUID,
                                            forLineage,
                                            forDuplicateProcessing,
                                            requestBody.getEffectiveTime(),
                                            methodName);
            }
            else
            {
                handler.clearLineageMapping(userId,
                                            null,
                                            null,
                                            lineageMappingGUID,
                                            forLineage,
                                            forDuplicateProcessing,
                                            new Date(),
                                            methodName);
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
     * Retrieve the lineage mapping relationships linked from a specific source element to its destinations.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public LineageMappingElementsResponse getDestinationLineageMappings(String                        serverName,
                                                                        String                        userId,
                                                                        String                        sourceElementGUID,
                                                                        int                           startFrom,
                                                                        int                           pageSize,
                                                                        boolean                       forLineage,
                                                                        boolean                       forDuplicateProcessing,
                                                                        EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getDestinationLineageMappings";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LineageMappingElementsResponse response = new LineageMappingElementsResponse();
        AuditLog                       auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getDestinationLineageMappings(userId,
                                                                           sourceElementGUID,
                                                                           startFrom,
                                                                           pageSize,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           requestBody.getEffectiveTime(),
                                                                           methodName));
            }
            else
            {
                response.setElements(handler.getDestinationLineageMappings(userId,
                                                                           sourceElementGUID,
                                                                           startFrom,
                                                                           pageSize,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           new Date(),
                                                                           methodName));
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
     * Retrieve the lineage mapping relationships linked from a specific destination element to its sources.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param destinationElementGUID unique identifier of the destination
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of software capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public LineageMappingElementsResponse getSourceLineageMappings(String                        serverName,
                                                                   String                        userId,
                                                                   String                        destinationElementGUID,
                                                                   int                           startFrom,
                                                                   int                           pageSize,
                                                                   boolean                       forLineage,
                                                                   boolean                       forDuplicateProcessing,
                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSourceLineageMappings";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        LineageMappingElementsResponse response = new LineageMappingElementsResponse();
        AuditLog                       auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ProcessExchangeHandler handler = instanceHandler.getProcessExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSourceLineageMappings(userId,
                                                                      destinationElementGUID,
                                                                      startFrom,
                                                                      pageSize,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      requestBody.getEffectiveTime(),
                                                                      methodName));
            }
            else
            {
                response.setElements(handler.getSourceLineageMappings(userId,
                                                                      destinationElementGUID,
                                                                      startFrom,
                                                                      pageSize,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      new Date(),
                                                                      methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
