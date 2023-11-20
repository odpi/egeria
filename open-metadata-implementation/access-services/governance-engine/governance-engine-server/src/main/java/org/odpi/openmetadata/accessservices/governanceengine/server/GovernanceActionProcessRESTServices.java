/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server;


import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceActionTypeElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.NextGovernanceActionTypeElement;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceActionTypeProperties;
import org.odpi.openmetadata.accessservices.governanceengine.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;

import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceActionProcessStepHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionProcessElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionProcessProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ProcessStatus;
import org.odpi.openmetadata.frameworkservices.gaf.rest.NewGovernanceActionProcessRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.UpdateGovernanceActionProcessRequestBody;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The GovernanceActionProcessRESTServices provides the server-side implementation of the services used by an external user to
 * define governance action processes.
 */
public class GovernanceActionProcessRESTServices
{
    private static final GovernanceEngineInstanceHandler instanceHandler = new GovernanceEngineInstanceHandler();

    private static final RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(GovernanceEngineRESTServices.class),
                                                                                        instanceHandler.getServiceName());

    private final RESTExceptionHandler    restExceptionHandler    = new RESTExceptionHandler();
    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();


    /**
     * Default constructor
     */
    public GovernanceActionProcessRESTServices()
    {
    }


    /* =====================================================================================================================
     * A governance action process describes a well defined series of steps that gets something done.
     * The steps are defined using GovernanceActionTypes.
     */

    /**
     * Create a new metadata element to represent a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param requestBody properties about the process to store and status value for the new process (default = ACTIVE)
     *
     * @return unique identifier of the new process or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGovernanceActionProcess(String                                serverName,
                                                      String                                userId,
                                                      NewGovernanceActionProcessRequestBody requestBody)
    {
        final String  methodName = "createGovernanceActionProcess";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId, serverName, methodName);

                GovernanceActionProcessProperties processProperties = requestBody.getProperties();

                Map<String, Object> extendedProperties = new HashMap<>();
                extendedProperties.put(OpenMetadataAPIMapper.FORMULA_PROPERTY_NAME, processProperties.getFormula());
                extendedProperties.put(OpenMetadataAPIMapper.IMPLEMENTATION_LANGUAGE_PROPERTY_NAME, processProperties.getImplementationLanguage());

                Date effectiveTime = new Date();

                response.setGUID(handler.createAssetInRepository(userId,
                                                                 null,
                                                                 null,
                                                                 processProperties.getQualifiedName(),
                                                                 processProperties.getTechnicalName(),
                                                                 processProperties.getVersionIdentifier(),
                                                                 processProperties.getTechnicalDescription(),
                                                                 processProperties.getAdditionalProperties(),
                                                                 OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                 extendedProperties,
                                                                 this.getProcessStatus(requestBody.getProcessStatus()),
                                                                 null,
                                                                 null,
                                                                 effectiveTime,
                                                                 methodName));

                final String guidParameter = "processGUID";

                if (response.getGUID() != null)
                {
                    handler.maintainSupplementaryProperties(userId,
                                                            response.getGUID(),
                                                            guidParameter,
                                                            OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                            processProperties.getQualifiedName(),
                                                            processProperties.getDisplayName(),
                                                            processProperties.getSummary(),
                                                            processProperties.getDescription(),
                                                            processProperties.getAbbreviation(),
                                                            processProperties.getUsage(),
                                                            false,
                                                            false,
                                                            false,
                                                            effectiveTime,
                                                            methodName);
                }
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
     * Translate the Process Status into a Repository Services' InstanceStatus.
     *
     * @param processStatus value from the caller
     * @return InstanceStatus enum
     */
    private InstanceStatus getProcessStatus(ProcessStatus processStatus)
    {
        if (processStatus != null)
        {
            switch (processStatus)
            {
                case UNKNOWN:
                    return InstanceStatus.UNKNOWN;

                case DRAFT:
                    return InstanceStatus.DRAFT;

                case PROPOSED:
                    return InstanceStatus.PROPOSED;

                case APPROVED:
                    return InstanceStatus.APPROVED;

                case ACTIVE:
                    return InstanceStatus.ACTIVE;
            }
        }

        return InstanceStatus.ACTIVE;
    }


    /**
     * Update the metadata element representing a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGovernanceActionProcess(String                                   serverName,
                                                      String                                   userId,
                                                      String                                   processGUID,
                                                      UpdateGovernanceActionProcessRequestBody requestBody)
    {
        final String methodName = "updateGovernanceActionProcess";
        final String processGUIDParameterName = "processGUID";
        final String newStatusParameterName = "requestBody.getProcessStatus";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if ((requestBody != null) && (requestBody.getProperties() != null))
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId, serverName, methodName);

                GovernanceActionProcessProperties processProperties = requestBody.getProperties();

                Map<String, Object> extendedProperties = new HashMap<>();
                extendedProperties.put(OpenMetadataAPIMapper.FORMULA_PROPERTY_NAME, processProperties.getFormula());
                extendedProperties.put(OpenMetadataAPIMapper.IMPLEMENTATION_LANGUAGE_PROPERTY_NAME, processProperties.getImplementationLanguage());

                Date effectiveTime = new Date();

                handler.updateAsset(userId,
                                    null,
                                    null,
                                    processGUID,
                                    processGUIDParameterName,
                                    processProperties.getQualifiedName(),
                                    processProperties.getVersionIdentifier(),
                                    processProperties.getTechnicalName(),
                                    processProperties.getTechnicalDescription(),
                                    processProperties.getAdditionalProperties(),
                                    OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_GUID,
                                    OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                    extendedProperties,
                                    null,
                                    null,
                                    requestBody.getMergeUpdate(),
                                    false,
                                    false,
                                    effectiveTime,
                                    methodName);

                if (requestBody.getProcessStatus() != null)
                {
                    handler.updateBeanStatusInRepository(userId,
                                                         null,
                                                         null,
                                                         processGUID,
                                                         processGUIDParameterName,
                                                         OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_GUID,
                                                         OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                         false,
                                                         false,
                                                         this.getProcessStatus(requestBody.getProcessStatus()),
                                                         newStatusParameterName,
                                                         effectiveTime,
                                                         methodName);
                }

                handler.maintainSupplementaryProperties(userId,
                                                        processGUID,
                                                        processGUIDParameterName,
                                                        OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                        processProperties.getQualifiedName(),
                                                        processProperties.getDisplayName(),
                                                        processProperties.getSummary(),
                                                        processProperties.getDescription(),
                                                        processProperties.getAbbreviation(),
                                                        processProperties.getUsage(),
                                                        requestBody.getMergeUpdate(),
                                                        false,
                                                        false,
                                                        effectiveTime,
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
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
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
                                                       String          userId,
                                                       String          processGUID,
                                                       NullRequestBody requestBody)
    {
        final String methodName = "publishGovernanceActionProcess";
        final String processGUIDParameterName = "processGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId, serverName, methodName);

            handler.publishAsset(userId, processGUID, processGUIDParameterName, false, false, new Date(), methodName);
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
     * @param userId calling user
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
                                                        String          userId,
                                                        String          processGUID,
                                                        NullRequestBody requestBody)
    {
        final String methodName = "withdrawGovernanceActionProcess";
        final String processGUIDParameterName = "processGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId, serverName, methodName);

            handler.withdrawAsset(userId, processGUID, processGUIDParameterName, false, false, new Date(), methodName);
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
     * @param userId calling user
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
                                                      String          userId,
                                                      String          processGUID,
                                                      NullRequestBody requestBody)
    {
        final String methodName = "removeGovernanceActionProcess";
        final String processGUIDParameterName = "processGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<GovernanceActionProcessElement> handler = instanceHandler.getGovernanceActionProcessHandler(userId, serverName, methodName);

            handler.deleteBeanInRepository(userId,
                                           null,
                                           null,
                                           processGUID,
                                           processGUIDParameterName,
                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_GUID,
                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                           null,
                                           null,
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
                                                        new Date(),
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
     * Create a new metadata element to represent a governance action type.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param requestBody properties about the process to store
     *
     * @return unique identifier of the new governance action type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGovernanceActionType(String                         serverName,
                                                   String                         userId,
                                                   GovernanceActionTypeProperties requestBody)
    {
        final String methodName = "createGovernanceActionType";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceActionProcessStepHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                serverName,
                                                                                                                                                methodName);

                response.setGUID(handler.createGovernanceActionProcessStep(userId,
                                                                           requestBody.getQualifiedName(),
                                                                           requestBody.getDomainIdentifier(),
                                                                           requestBody.getDisplayName(),
                                                                           requestBody.getDescription(),
                                                                           requestBody.getSupportedGuards(),
                                                                           requestBody.getAdditionalProperties(),
                                                                           requestBody.getGovernanceEngineGUID(),
                                                                           requestBody.getRequestType(),
                                                                           requestBody.getRequestParameters(),
                                                                           requestBody.getIgnoreMultipleTriggers(),
                                                                           requestBody.getWaitTime(),
                                                                           null,
                                                                           null,
                                                                           false,
                                                                           false,
                                                                           instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                           new Date(),
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
     * Update the metadata element representing a governance action type.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGovernanceActionType(String                                serverName,
                                                   String                                userId,
                                                   String                                actionTypeGUID,
                                                   UpdateGovernanceActionTypeRequestBody requestBody)
    {
        final String methodName = "updateGovernanceActionProcess";
        final String propertiesParameterName = "requestBody.getProperties";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceActionProcessStepHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                serverName,
                                                                                                                                                methodName);

                GovernanceActionTypeProperties properties = requestBody.getProperties();

                invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

                handler.updateGovernanceActionProcessStep(userId,
                                                          actionTypeGUID,
                                                          requestBody.getMergeUpdate(),
                                                          properties.getQualifiedName(),
                                                          properties.getDomainIdentifier(),
                                                          properties.getDisplayName(),
                                                          properties.getDescription(),
                                                          properties.getSupportedGuards(),
                                                          properties.getAdditionalProperties(),
                                                          properties.getGovernanceEngineGUID(),
                                                          properties.getRequestType(),
                                                          properties.getRequestParameters(),
                                                          properties.getIgnoreMultipleTriggers(),
                                                          properties.getWaitTime(),
                                                          null,
                                                          null,
                                                          false,
                                                          false,
                                                          instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                          new Date(),
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
     * Remove the metadata element representing a governance action type.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeGovernanceActionType(String          serverName,
                                                   String          userId,
                                                   String          actionTypeGUID,
                                                   NullRequestBody requestBody)
    {
        final String methodName = "removeGovernanceActionType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceActionProcessStepHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                            serverName,
                                                                                                                                            methodName);

            handler.removeGovernanceActionProcessStep(userId,
                                                      actionTypeGUID,
                                                      false,
                                                      false,
                                                      instanceHandler.getSupportedZones(userId, serverName, methodName),
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
                GovernanceActionProcessStepHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                serverName,
                                                                                                                                                methodName);

                if (requestBody.getSearchStringParameterName() != null)
                {
                    searchStringParameterName = requestBody.getSearchStringParameterName();
                }

                response.setElements(handler.findGovernanceActionProcessSteps(userId,
                                                                              requestBody.getSearchString(),
                                                                              searchStringParameterName,
                                                                              startFrom,
                                                                              pageSize,
                                                                              false,
                                                                              false,
                                                                              instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                              new Date(),
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
                GovernanceActionProcessStepHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                serverName,
                                                                                                                                                methodName);

                if (requestBody.getNameParameterName() != null)
                {
                    nameParameterName = requestBody.getNameParameterName();
                }

                response.setElements(handler.getGovernanceActionProcessStepsByName(userId,
                                                                                   requestBody.getName(),
                                                                                   nameParameterName,
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   instanceHandler.getSupportedZones(userId, serverName, methodName), null,
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
            GovernanceActionProcessStepHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                            serverName,
                                                                                                                                            methodName);

            response.setElement(handler.getGovernanceActionProcessStepByGUID(userId,
                                                                             actionTypeGUID,
                                                                             instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                             null,
                                                                             methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Set up a link between a governance action process and a governance action type.  This defines the first
     * step in the process.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param actionTypeGUID unique identifier of the governance action type
     * @param requestBody optional guard
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse setupFirstActionType(String serverName,
                                             String userId,
                                             String processGUID,
                                             String actionTypeGUID,
                                             String requestBody)
    {
        final String methodName = "setupFirstActionType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceActionProcessStepHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                            serverName,
                                                                                                                                            methodName);

            handler.setupFirstProcessStep(userId,
                                          processGUID,
                                          actionTypeGUID,
                                          requestBody,
                                          null,
                                          null,
                                          false,
                                          false,
                                          instanceHandler.getSupportedZones(userId, serverName, methodName),
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
            GovernanceActionProcessStepHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                            serverName,
                                                                                                                                            methodName);

            response.setElement(handler.getFirstProcessStep(userId,
                                                            processGUID,
                                                            null,
                                                            instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                            null,
                                                            methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the link between a governance process and that governance action type that defines its first step.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeFirstActionType(String          serverName,
                                              String          userId,
                                              String          processGUID,
                                              NullRequestBody requestBody)
    {
        final String methodName = "removeFirstActionType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceActionProcessStepHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                            serverName,
                                                                                                                                            methodName);

            handler.removeFirstProcessStep(userId,
                                           processGUID,
                                           instanceHandler.getSupportedZones(userId, serverName, methodName),
                                           null,
                                           methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Add a link between two governance action types to show that one follows on from the other when a governance action process
     * is executing.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param currentActionTypeGUID unique identifier of the governance action type that defines the previous step in the governance action process
     * @param nextActionTypeGUID unique identifier of the governance action type that defines the next step in the governance action process
     * @param requestBody guard required for this next step to proceed - or null for always run the next step plus flags.
     *
     * @return unique identifier of the new link or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse setupNextActionType(String                              serverName,
                                            String                              userId,
                                            String                              currentActionTypeGUID,
                                            String                              nextActionTypeGUID,
                                            NextGovernanceActionTypeRequestBody requestBody)
    {
        final String methodName = "setupNextActionType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceActionProcessStepHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                serverName,
                                                                                                                                                methodName);
                response.setGUID(handler.setupNextProcessStep(userId,
                                                              currentActionTypeGUID,
                                                              nextActionTypeGUID,
                                                              requestBody.getGuard(),
                                                              requestBody.getMandatoryGuard(),
                                                              null,
                                                              null,
                                                              false,
                                                              false,
                                                              instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                              new Date(),
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
     * Update the properties of the link between two governance action types that shows that one follows on from the other when a governance
     * action process is executing.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param nextActionLinkGUID unique identifier of the relationship between the governance action types
     * @param requestBody guard required for this next step to proceed - or null for always run the next step - and flags
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateNextActionType(String                              serverName,
                                             String                              userId,
                                             String                              nextActionLinkGUID,
                                             NextGovernanceActionTypeRequestBody requestBody)
    {
        final String methodName = "updateNextActionType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceActionProcessStepHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                                serverName,
                                                                                                                                                methodName);
                handler.updateNextProcessStep(userId,
                                              nextActionLinkGUID,
                                              requestBody.getGuard(),
                                              requestBody.getMandatoryGuard(),
                                              null,
                                              null,
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
            GovernanceActionProcessStepHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                            serverName,
                                                                                                                                            methodName);

            List<Relationship> relationships = handler.getNextGovernanceActionProcessSteps(userId,
                                                                                           actionTypeGUID,
                                                                                           startFrom,
                                                                                           pageSize,
                                                                                           instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                                           null,
                                                                                           methodName);

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

                        element.setNextActionType(handler.getGovernanceActionProcessStepByGUID(userId,
                                                                                               relationship.getEntityTwoProxy().getGUID(),
                                                                                               instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                                               null,
                                                                                               methodName));

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
     * Remove a follow-on step from a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param actionLinkGUID unique identifier of the relationship between the governance action types
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid or
     *  UserNotAuthorizedException the user is not authorized to issue this request or
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeNextActionType(String          serverName,
                                             String          userId,
                                             String          actionLinkGUID,
                                             NullRequestBody requestBody)
    {
        final String methodName = "removeNextActionType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceActionProcessStepHandler<GovernanceActionTypeElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId,
                                                                                                                                            serverName,
                                                                                                                                            methodName);

            handler.removeNextProcessStep(userId, actionLinkGUID, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
