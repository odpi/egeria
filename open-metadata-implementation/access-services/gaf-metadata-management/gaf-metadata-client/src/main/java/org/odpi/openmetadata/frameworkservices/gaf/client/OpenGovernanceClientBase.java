/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.opengovernance.client.OpenGovernanceClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OpenGovernanceClientBase provides common governance services that originate in the Open Survey Framework (OGF).
 * This includes the ability to define and execute governance action processes as well as manage duplicates.
 */
public class OpenGovernanceClientBase extends OpenGovernanceClient
{
    protected final GAFRESTClient           restClient;               /* Initialized in constructor */

    protected final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    protected final NullRequestBody         nullRequestBody = new NullRequestBody();



    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize           pre-initialized parameter limit
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenGovernanceClientBase(String   serverName,
                                    String   serverPlatformURLRoot,
                                    String   localServerSecretsStoreProvider,
                                    String   localServerSecretsStoreLocation,
                                    String   localServerSecretsStoreCollection,
                                    int      maxPageSize,
                                    AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);

        final String methodName = "Constructor (no security)";

        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.restClient = new GAFRESTClient(serverName, serverPlatformURLRoot, localServerSecretsStoreProvider, localServerSecretsStoreLocation, localServerSecretsStoreCollection, auditLog);
    }


    /**
     * Create an engine action in the metadata store which will trigger the governance service
     * associated with the supplied request type.  The engine action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param userId caller's userId
     * @param qualifiedName unique identifier to give this engine action
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param displayName display name for this action
     * @param description description for this action
     * @param actionSourceGUIDs  request source elements for the resulting engine action
     * @param actionCauseGUIDs  request cause elements for the resulting engine action
     * @param actionTargets list of action target names to GUIDs for the resulting engine action
     * @param receivedGuards list of guards to initiate the engine action
     * @param startTime future start time or null for "as soon as possible"
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestType governance request type from the caller
     * @param requestParameters properties to pass to the governance service
     * @param processName name of the process that this action is a part of
     * @param requestSourceName source of the request
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the requesting governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the engine action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException the caller is not authorized to create an engine action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateEngineAction(String                userId,
                                       String                qualifiedName,
                                       int                   domainIdentifier,
                                       String                displayName,
                                       String                description,
                                       List<String>          actionSourceGUIDs,
                                       List<String>          actionCauseGUIDs,
                                       List<NewActionTarget> actionTargets,
                                       List<String>          receivedGuards,
                                       Date                  startTime,
                                       String                governanceEngineName,
                                       String                requestType,
                                       Map<String, String>   requestParameters,
                                       String                processName,
                                       String                requestSourceName,
                                       String                originatorServiceName,
                                       String                originatorEngineName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "initiateEngineAction";
        final String qualifiedNameParameterName = "qualifiedName";
        final String engineNameParameterName = "governanceEngineName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-governance-service/users/{1}/governance-engines/{2}/engine-actions/initiate";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        invalidParameterHandler.validateName(governanceEngineName, engineNameParameterName, methodName);

        InitiateEngineActionRequestBody requestBody = new InitiateEngineActionRequestBody();

        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDomainIdentifier(domainIdentifier);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);
        requestBody.setActionSourceGUIDs(actionSourceGUIDs);
        requestBody.setActionCauseGUIDs(actionCauseGUIDs);
        requestBody.setActionTargets(actionTargets);
        requestBody.setReceivedGuards(receivedGuards);
        requestBody.setStartDate(startTime);
        requestBody.setRequestType(requestType);
        requestBody.setRequestParameters(requestParameters);
        requestBody.setProcessName(processName);
        requestBody.setRequestSourceName(requestSourceName);
        requestBody.setOriginatorServiceName(originatorServiceName);
        requestBody.setOriginatorEngineName(originatorEngineName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  governanceEngineName);

        return restResult.getGUID();
    }


    /**
     * Using the named governance action type as a template, initiate an engine action.
     *
     * @param userId caller's userId
     * @param governanceActionTypeQualifiedName unique name of the governance action type to use
     * @param actionSourceGUIDs  request source elements for the resulting engine action
     * @param actionCauseGUIDs  request cause elements for the resulting engine action
     * @param actionTargets list of action target names to GUIDs for the resulting engine action
     * @param startTime future start time or null for "as soon as possible".
     * @param requestParameters request properties to be passed to the engine action
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the engine action
     * @throws InvalidParameterException null or unrecognized qualified name of the type
     * @throws UserNotAuthorizedException the caller is not authorized to create an engine action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateGovernanceActionType(String                userId,
                                               String                governanceActionTypeQualifiedName,
                                               List<String>          actionSourceGUIDs,
                                               List<String>          actionCauseGUIDs,
                                               List<NewActionTarget> actionTargets,
                                               Date                  startTime,
                                               Map<String, String>   requestParameters,
                                               String                originatorServiceName,
                                               String                originatorEngineName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName = "initiateGovernanceActionType";
        final String qualifiedNameParameterName = "governanceActionTypeQualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-governance-service/users/{1}/governance-action-types/initiate";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(governanceActionTypeQualifiedName, qualifiedNameParameterName, methodName);

        InitiateGovernanceActionTypeRequestBody requestBody = new InitiateGovernanceActionTypeRequestBody();

        requestBody.setGovernanceActionTypeQualifiedName(governanceActionTypeQualifiedName);
        requestBody.setActionSourceGUIDs(actionSourceGUIDs);
        requestBody.setActionCauseGUIDs(actionCauseGUIDs);
        requestBody.setActionTargets(actionTargets);
        requestBody.setStartDate(startTime);
        requestBody.setRequestParameters(requestParameters);
        requestBody.setOriginatorServiceName(originatorServiceName);
        requestBody.setOriginatorEngineName(originatorEngineName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Using the named governance action process as a template, initiate a chain of engine actions.
     *
     * @param userId caller's userId
     * @param processQualifiedName unique name of the governance action process to use
     * @param actionSourceGUIDs  request source elements for the resulting engine action
     * @param actionCauseGUIDs  request cause elements for the resulting engine action
     * @param actionTargets list of action target names to GUIDs for the resulting engine action
     * @param startTime future start time or null for "as soon as possible".
     * @param requestParameters request properties to be passed to the first engine action
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the governance action process instance
     * @throws InvalidParameterException null or unrecognized qualified name of the process
     * @throws UserNotAuthorizedException the caller is not authorized to create a governance action process
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateGovernanceActionProcess(String                userId,
                                                  String                processQualifiedName,
                                                  List<String>          actionSourceGUIDs,
                                                  List<String>          actionCauseGUIDs,
                                                  List<NewActionTarget> actionTargets,
                                                  Date                  startTime,
                                                  Map<String, String>   requestParameters,
                                                  String                originatorServiceName,
                                                  String                originatorEngineName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "initiateGovernanceActionProcess";
        final String qualifiedNameParameterName = "processQualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-governance-service/users/{1}/governance-action-processes/initiate";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(processQualifiedName, qualifiedNameParameterName, methodName);

        InitiateGovernanceActionProcessRequestBody requestBody = new InitiateGovernanceActionProcessRequestBody();

        requestBody.setProcessQualifiedName(processQualifiedName);
        requestBody.setActionSourceGUIDs(actionSourceGUIDs);
        requestBody.setActionCauseGUIDs(actionCauseGUIDs);
        requestBody.setActionTargets(actionTargets);
        requestBody.setStartDate(startTime);
        requestBody.setRequestParameters(requestParameters);
        requestBody.setOriginatorServiceName(originatorServiceName);
        requestBody.setOriginatorEngineName(originatorEngineName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }



    /**
     * Request the status of an executing engine action request.
     *
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request.
     *
     * @return status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public EngineActionElement getEngineAction(String userId,
                                               String engineActionGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "getEngineAction";
        final String guidParameterName = "engineActionGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-governance-service/users/{1}/engine-actions/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(engineActionGUID, guidParameterName, methodName);

        EngineActionElementResponse restResult = restClient.callEngineActionGetRESTCall(methodName,
                                                                                        urlTemplate,
                                                                                        serverName,
                                                                                        userId,
                                                                                        engineActionGUID);

        return restResult.getElement();
    }



    /**
     * Request that execution of an engine action is stopped.
     *
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public void cancelEngineAction(String userId,
                                   String engineActionGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName = "cancelEngineAction";
        final String guidParameterName = "engineActionGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-governance-service/users/{1}/engine-actions/{2}/cancel";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(engineActionGUID, guidParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        engineActionGUID);
    }


    /**
     * Retrieve the engine actions that are still in process.
     *
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @return list of engine action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public List<EngineActionElement>  getActiveEngineActions(String userId,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getActiveEngineActions";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-governance-service/users/{1}/engine-actions/active?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);

        EngineActionElementsResponse restResult = restClient.callEngineActionsGetRESTCall(methodName,
                                                                                          urlTemplate,
                                                                                          serverName,
                                                                                          userId,
                                                                                          Integer.toString(startFrom),
                                                                                          Integer.toString(pageSize));

        return restResult.getElements();
    }


    /* =====================================================================================================================
     * A governance action process describes a well-defined series of steps that gets something done.
     * The steps are defined using GovernanceActionProcessSteps.
     */



    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier
     * along with its process flow implementation.  This includes the process steps and the links
     * between them
     *
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     * @param effectiveTime effective date/time for query
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceActionProcessGraph getGovernanceActionProcessGraph(String userId,
                                                                        String processGUID,
                                                                        Date   effectiveTime) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "getGovernanceActionProcessGraph";
        final String guidParameterName = "processGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-governance-service/users/{1}/governance-action-processes/{2}/graph";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        ResultsRequestBody requestBody = new ResultsRequestBody();
        requestBody.setEffectiveTime(effectiveTime);

        GovernanceActionProcessGraphResponse restResult = restClient.callGovernanceActionProcessGraphPostRESTCall(methodName,
                                                                                                                  urlTemplate,
                                                                                                                  requestBody,
                                                                                                                  serverName,
                                                                                                                  userId,
                                                                                                                  processGUID);

        return restResult.getElement();
    }
}
