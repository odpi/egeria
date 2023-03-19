/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.client;

import org.odpi.openmetadata.accessservices.assetmanager.api.LineageExchangeInterface;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;


/**
 * LineageExchangeClient is the client for managing processes and lineage linkage.
 */
public class LineageExchangeClient extends SchemaExchangeClientBase implements LineageExchangeInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public LineageExchangeClient(String   serverName,
                                 String   serverPlatformURLRoot,
                                 AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public LineageExchangeClient(String serverName,
                                 String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public LineageExchangeClient(String   serverName,
                                 String   serverPlatformURLRoot,
                                 String   userId,
                                 String   password,
                                 AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public LineageExchangeClient(String                 serverName,
                                 String                 serverPlatformURLRoot,
                                 AssetManagerRESTClient restClient,
                                 int                    maxPageSize,
                                 AuditLog               auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public LineageExchangeClient(String serverName,
                                 String serverPlatformURLRoot,
                                 String userId,
                                 String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }



    /* =====================================================================================================================
     * A process describes a well defined series of steps that gets something done.
     */

    /**
     * Create a new metadata element to represent a process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param processStatus initial status of the process
     * @param processProperties properties about the process to store
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createProcess(String                       userId,
                                String                       assetManagerGUID,
                                String                       assetManagerName,
                                boolean                      assetManagerIsHome,
                                ExternalIdentifierProperties externalIdentifierProperties,
                                ProcessStatus                processStatus,
                                ProcessProperties            processProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                  = "createProcess";
        final String propertiesParameterName     = "processProperties";
        final String qualifiedNameParameterName  = "processProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(processProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(processProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        ProcessRequestBody requestBody = new ProcessRequestBody();
        requestBody.setElementProperties(processProperties);
        requestBody.setProcessStatus(processStatus);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes?assetManagerIsHome={2}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a process using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param templateGUID unique identifier of the metadata element to copy
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createProcessFromTemplate(String                       userId,
                                            String                       assetManagerGUID,
                                            String                       assetManagerName,
                                            boolean                      assetManagerIsHome,
                                            String                       templateGUID,
                                            ExternalIdentifierProperties externalIdentifierProperties,
                                            TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName                  = "createProcessFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        TemplateRequestBody requestBody = new TemplateRequestBody();
        requestBody.setElementProperties(templateProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/from-template/{2}assetManagerIsHome={3}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  assetManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the metadata element to update
     * @param processExternalIdentifier unique identifier of the process in the external process manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param processProperties new properties for the metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateProcess(String            userId,
                              String            assetManagerGUID,
                              String            assetManagerName,
                              String            processGUID,
                              String            processExternalIdentifier,
                              boolean           isMergeUpdate,
                              ProcessProperties processProperties,
                              Date              effectiveTime,
                              boolean           forLineage,
                              boolean           forDuplicateProcessing) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                 = "updateProcess";
        final String processGUIDParameterName   = "processGUID";
        final String propertiesParameterName    = "processProperties";
        final String qualifiedNameParameterName = "processProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(processProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(processProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        ProcessRequestBody requestBody = new ProcessRequestBody();
        requestBody.setElementProperties(processProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   processExternalIdentifier,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}?isMergeUpdate={3}&forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        processGUID,
                                        isMergeUpdate,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Update the status of the metadata element representing a process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the process to update
     * @param processExternalIdentifier unique identifier of the process in the external process manager
     * @param processStatus new status for the process
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateProcessStatus(String        userId,
                                    String        assetManagerGUID,
                                    String        assetManagerName,
                                    String        processGUID,
                                    String        processExternalIdentifier,
                                    ProcessStatus processStatus,
                                    Date          effectiveTime,
                                    boolean       forLineage,
                                    boolean       forDuplicateProcessing) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName              = "updateProcessStatus";
        final String portGUIDParameterName   = "processGUID";
        final String propertiesParameterName = "processStatus";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, portGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(processStatus, propertiesParameterName, methodName);

        ProcessStatusRequestBody requestBody = new ProcessStatusRequestBody();
        requestBody.setProcessStatus(processStatus);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   processExternalIdentifier,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/status?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        processGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Create a parent-child relationship between two processes.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param parentProcessGUID unique identifier of the process in the external process manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external process manager that is to be the nested sub-process
     * @param containmentProperties describes the ownership of the sub-process
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupProcessParent(String                       userId,
                                   String                       assetManagerGUID,
                                   String                       assetManagerName,
                                   boolean                      assetManagerIsHome,
                                   String                       parentProcessGUID,
                                   String                       childProcessGUID,
                                   ProcessContainmentProperties containmentProperties,
                                   Date                         effectiveTime,
                                   boolean                      forLineage,
                                   boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName                     = "setupProcessParent";
        final String parentProcessGUIDParameterName = "parentProcessGUID";
        final String childProcessGUIDParameterName  = "childProcessGUID";
        final String propertiesParameterName        = "containmentProperties";
        final String containmentTypeParameterName   = "containmentProperties.containmentType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentProcessGUID, parentProcessGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(childProcessGUID, childProcessGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(containmentProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateEnum(containmentProperties.getProcessContainmentType(), containmentTypeParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/parent/{2}/child/{3}?assetManagerIsHome={4}&forLineage={5}&forDuplicateProcessing={6}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, containmentProperties),
                                        serverName,
                                        userId,
                                        parentProcessGUID,
                                        childProcessGUID,
                                        assetManagerIsHome,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove a parent-child relationship between two processes.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param parentProcessGUID unique identifier of the process in the external process manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external process manager that is to be the nested sub-process
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearProcessParent(String  userId,
                                   String  assetManagerGUID,
                                   String  assetManagerName,
                                   String  parentProcessGUID,
                                   String  childProcessGUID,
                                   Date    effectiveTime,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName                     = "clearProcessParent";
        final String parentProcessGUIDParameterName = "parentProcessGUID";
        final String childProcessGUIDParameterName  = "childProcessGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentProcessGUID, parentProcessGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(childProcessGUID, childProcessGUIDParameterName, methodName);


        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/parent/{2}/children/{3}/remove?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        parentProcessGUID,
                                        childProcessGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Update the zones for the process so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the metadata element to publish
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void publishProcess(String  userId,
                               String  assetManagerGUID,
                               String  assetManagerName,
                               String  processGUID,
                               Date    effectiveTime,
                               boolean forLineage,
                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName               = "publishProcess";
        final String processGUIDParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/publish?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        processGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Update the zones for the process so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the process is first created).
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the metadata element to withdraw
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void withdrawProcess(String  userId,
                                String  assetManagerGUID,
                                String  assetManagerName,
                                String  processGUID,
                                Date    effectiveTime,
                                boolean forLineage,
                                boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName               = "withdrawProcess";
        final String processGUIDParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/withdraw?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        processGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the metadata element representing a process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the metadata element to remove
     * @param processExternalIdentifier unique identifier of the process in the external process manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeProcess(String  userId,
                              String  assetManagerGUID,
                              String  assetManagerName,
                              String  processGUID,
                              String  processExternalIdentifier,
                              Date    effectiveTime,
                              boolean forLineage,
                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName               = "removeProcess";
        final String processGUIDParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, processExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        processGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the list of process metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessElement> findProcesses(String  userId,
                                              String  assetManagerGUID,
                                              String  assetManagerName,
                                              String  searchString,
                                              int     startFrom,
                                              int     pageSize,
                                              Date    effectiveTime,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "findProcesses";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/by-search-string?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        ProcessElementsResponse restResult = restClient.callProcessesPostRESTCall(methodName,
                                                                                  urlTemplate,
                                                                                  getSearchStringRequestBody(assetManagerGUID, assetManagerName, searchString, effectiveTime, methodName),
                                                                                  serverName,
                                                                                  userId,
                                                                                  startFrom,
                                                                                  validatedPageSize,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Return the list of processes associated with the process manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of metadata elements describing the processes associated with the requested process manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessElement>   getProcessesForAssetManager(String  userId,
                                                              String  assetManagerGUID,
                                                              String  assetManagerName,
                                                              int     startFrom,
                                                              int     pageSize,
                                                              Date    effectiveTime,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "getProcessesForAssetManager";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/by-asset-manager?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        ProcessElementsResponse restResult = restClient.callProcessesPostRESTCall(methodName,
                                                                                  urlTemplate,
                                                                                  getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                  serverName,
                                                                                  userId,
                                                                                  startFrom,
                                                                                  validatedPageSize,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessElement>   getProcessesByName(String  userId,
                                                     String  assetManagerGUID,
                                                     String  assetManagerName,
                                                     String  name,
                                                     int     startFrom,
                                                     int     pageSize,
                                                     Date    effectiveTime,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "getProcessesByName";

        invalidParameterHandler.validateUserId(userId, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/by-name?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        ProcessElementsResponse restResult = restClient.callProcessesPostRESTCall(methodName,
                                                                                  urlTemplate,
                                                                                  getNameRequestBody(assetManagerGUID, assetManagerName, name, effectiveTime, methodName),
                                                                                  serverName,
                                                                                  userId,
                                                                                  startFrom,
                                                                                  validatedPageSize,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ProcessElement getProcessByGUID(String  userId,
                                           String  assetManagerGUID,
                                           String  assetManagerName,
                                           String  processGUID,
                                           Date    effectiveTime,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getProcessByGUID";
        final String guidParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/retrieve?forLineage={3}&forDuplicateProcessing={4}";

        ProcessElementResponse restResult = restClient.callProcessPostRESTCall(methodName,
                                                                               urlTemplate,
                                                                               getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                               serverName,
                                                                               userId,
                                                                               processGUID,
                                                                               forLineage,
                                                                               forDuplicateProcessing);

        return restResult.getElement();
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return parent process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ProcessElement getProcessParent(String  userId,
                                           String  assetManagerGUID,
                                           String  assetManagerName,
                                           String  processGUID,
                                           Date    effectiveTime,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getProcessParent";
        final String guidParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/parent/retrieve?forLineage={3}&forDuplicateProcessing={4}";

        ProcessElementResponse restResult = restClient.callProcessPostRESTCall(methodName,
                                                                               urlTemplate,
                                                                               getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                               serverName,
                                                                               userId,
                                                                               processGUID,
                                                                               forLineage,
                                                                               forDuplicateProcessing);

        return restResult.getElement();
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the requested metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessElement> getSubProcesses(String  userId,
                                                String  assetManagerGUID,
                                                String  assetManagerName,
                                                String  processGUID,
                                                int     startFrom,
                                                int     pageSize,
                                                Date    effectiveTime,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName        = "getSubProcesses";
        final String guidParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/children?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        ProcessElementsResponse restResult = restClient.callProcessesPostRESTCall(methodName,
                                                                                  urlTemplate,
                                                                                  getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                  serverName,
                                                                                  userId,
                                                                                  startFrom,
                                                                                  validatedPageSize,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing);

        return restResult.getElementList();
    }


    /* ===============================================================================
     * A process typically contains ports that show the flow of data and control to and from it.
     */

    /**
     * Create a new metadata element to represent a port.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this port
     * @param processGUID unique identifier of the process where the port is located
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param portProperties properties for the port
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier of the new metadata element for the port
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createPort(String                       userId,
                             String                       assetManagerGUID,
                             String                       assetManagerName,
                             boolean                      assetManagerIsHome,
                             String                       processGUID,
                             ExternalIdentifierProperties externalIdentifierProperties,
                             PortProperties               portProperties,
                             Date                         effectiveTime,
                             boolean                      forLineage,
                             boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName                  = "createPort";
        final String processGUIDParameterName    = "processGUID";
        final String propertiesParameterName     = "portProperties";
        final String qualifiedNameParameterName  = "portProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(portProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(portProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        PortRequestBody requestBody = new PortRequestBody();
        requestBody.setElementProperties(portProperties);
        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "processes/{2}/ports?assetManagerIsHome={3}&forLineage={4}&forDuplicateProcessing={5}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  processGUID,
                                                                  assetManagerIsHome,
                                                                  forLineage,
                                                                  forDuplicateProcessing);

        return restResult.getGUID();
    }


    /**
     * Update the properties of the metadata element representing a port.  This call replaces
     * all existing properties with the supplied properties.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the port to update
     * @param portProperties new properties for the port
     * @param portExternalIdentifier unique identifier of the port in the external process manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updatePort(String         userId,
                           String         assetManagerGUID,
                           String         assetManagerName,
                           String         portGUID,
                           String         portExternalIdentifier,
                           PortProperties portProperties,
                           Date           effectiveTime,
                           boolean        forLineage,
                           boolean        forDuplicateProcessing) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                  = "updatePort";
        final String portGUIDParameterName       = "portGUID";
        final String propertiesParameterName     = "portProperties";
        final String qualifiedNameParameterName  = "portProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(portProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(portProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        PortRequestBody requestBody = new PortRequestBody();
        requestBody.setElementProperties(portProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   portExternalIdentifier,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/update?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        portGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Link a port to a process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param processGUID unique identifier of the process
     * @param portGUID unique identifier of the port
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupProcessPort(String  userId,
                                 String  assetManagerGUID,
                                 String  assetManagerName,
                                 boolean assetManagerIsHome,
                                 String  processGUID,
                                 String  portGUID,
                                 Date    effectiveTime,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName               = "setupProcessPort";
        final String processGUIDParameterName = "processGUID";
        final String portGUIDParameterName    = "portGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/ports/{3}?assetManagerIsHome={4}&forLineage={5}&forDuplicateProcessing={6}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, null),
                                        serverName,
                                        userId,
                                        processGUID,
                                        portGUID,
                                        assetManagerIsHome,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Unlink a port from a process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the process
     * @param portGUID unique identifier of the port
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearProcessPort(String  userId,
                                 String  assetManagerGUID,
                                 String  assetManagerName,
                                 String  processGUID,
                                 String  portGUID,
                                 Date    effectiveTime,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName               = "clearProcessPort";
        final String processGUIDParameterName = "processGUID";
        final String portGUIDParameterName    = "portGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/ports/{3}/remove?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        processGUID,
                                        portGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Link two ports together to show that portTwo is an implementation of portOne. (That is, portOne delegates to
     * portTwo.)
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param portOneGUID unique identifier of the port at end 1
     * @param portTwoGUID unique identifier of the port at end 2
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupPortDelegation(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    boolean assetManagerIsHome,
                                    String  portOneGUID,
                                    String  portTwoGUID,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName               = "setupPortDelegation";
        final String portOneGUIDParameterName = "portOneGUID";
        final String portTwoGUIDParameterName = "portTwoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portOneGUID, portOneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(portTwoGUID, portTwoGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/port-delegations/{3}?assetManagerIsHome={4}&forLineage={5}&forDuplicateProcessing={6}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, null),
                                        serverName,
                                        userId,
                                        portOneGUID,
                                        portTwoGUID,
                                        assetManagerIsHome,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the port delegation relationship between two ports.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portOneGUID unique identifier of the port at end 1
     * @param portTwoGUID unique identifier of the port at end 2
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearPortDelegation(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  portOneGUID,
                                    String  portTwoGUID,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName               = "clearPortDelegation";
        final String portOneGUIDParameterName = "portOneGUID";
        final String portTwoGUIDParameterName = "portTwoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portOneGUID, portOneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(portTwoGUID, portTwoGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/port-delegations/{3}/remove?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        portOneGUID,
                                        portTwoGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Link a schema type to a port to show the structure of data it accepts.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param portGUID unique identifier of the port
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupPortSchemaType(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    boolean assetManagerIsHome,
                                    String  portGUID,
                                    String  schemaTypeGUID,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName                  = "setupPortSchemaType";
        final String portGUIDParameterName       = "portGUID";
        final String schemaTypeGUIDParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/schemaType/{3}?assetManagerIsHome={4}&forLineage={5}&forDuplicateProcessing={6}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, null),
                                        serverName,
                                        userId,
                                        portGUID,
                                        schemaTypeGUID,
                                        assetManagerIsHome,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the schema type from a port.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the port
     * @param schemaTypeGUID unique identifier of the schemaType
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearPortSchemaType(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  portGUID,
                                    String  schemaTypeGUID,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName                  = "clearPortSchemaType";
        final String portGUIDParameterName       = "portGUID";
        final String schemaTypeGUIDParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/schemaType/{3}/remove?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        portGUID,
                                        schemaTypeGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the metadata element representing a port.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the metadata element to remove
     * @param portExternalIdentifier unique identifier of the port in the external process manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removePort(String  userId,
                           String  assetManagerGUID,
                           String  assetManagerName,
                           String  portGUID,
                           String  portExternalIdentifier,
                           Date    effectiveTime,
                           boolean forLineage,
                           boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName            = "removePort";
        final String portGUIDParameterName = "portGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, portExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        portGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the list of port metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<PortElement>   findPorts(String  userId,
                                         String  assetManagerGUID,
                                         String  assetManagerName,
                                         String  searchString,
                                         int     startFrom,
                                         int     pageSize,
                                         Date    effectiveTime,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "findPorts";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/by-search-string?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        PortElementsResponse restResult = restClient.callPortsPostRESTCall(methodName,
                                                                           urlTemplate,
                                                                           getSearchStringRequestBody(assetManagerGUID, assetManagerName, searchString, effectiveTime, methodName),
                                                                           serverName,
                                                                           userId,
                                                                           startFrom,
                                                                           validatedPageSize,
                                                                           forLineage,
                                                                           forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of ports associated with a process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the process of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<PortElement> getPortsForProcess(String  userId,
                                                String  assetManagerGUID,
                                                String  assetManagerName,
                                                String  processGUID,
                                                int     startFrom,
                                                int     pageSize,
                                                Date    effectiveTime,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName        = "getPortsForProcess";
        final String guidParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/ports/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        PortElementsResponse restResult = restClient.callPortsPostRESTCall(methodName,
                                                                           urlTemplate,
                                                                           getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                           serverName,
                                                                           userId,
                                                                           processGUID,
                                                                           startFrom,
                                                                           validatedPageSize,
                                                                           forLineage,
                                                                           forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of ports that delegate to this port.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the starting port
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<PortElement>  getPortUse(String  userId,
                                         String  assetManagerGUID,
                                         String  assetManagerName,
                                         String  portGUID,
                                         int     startFrom,
                                         int     pageSize,
                                         Date    effectiveTime,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName        = "getPortUse";
        final String guidParameterName = "portGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/used-by/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        PortElementsResponse restResult = restClient.callPortsPostRESTCall(methodName,
                                                                           urlTemplate,
                                                                           getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                           serverName,
                                                                           userId,
                                                                           portGUID,
                                                                           startFrom,
                                                                           validatedPageSize,
                                                                           forLineage,
                                                                           forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the port that this port delegates to.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the starting port alias
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public PortElement getPortDelegation(String  userId,
                                         String  assetManagerGUID,
                                         String  assetManagerName,
                                         String  portGUID,
                                         Date    effectiveTime,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName        = "getPortDelegation";
        final String guidParameterName = "portGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/port-delegations/retrieve?forLineage={3}&forDuplicateProcessing={4}";

        PortElementResponse restResult = restClient.callPortPostRESTCall(methodName,
                                                                         urlTemplate,
                                                                         getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                         serverName,
                                                                         userId,
                                                                         portGUID,
                                                                         forLineage,
                                                                         forDuplicateProcessing);

        return restResult.getElement();
    }


    /**
     * Retrieve the list of port metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<PortElement>   getPortsByName(String  userId,
                                              String  assetManagerGUID,
                                              String  assetManagerName,
                                              String  name,
                                              int     startFrom,
                                              int     pageSize,
                                              Date    effectiveTime,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getPortsByName";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/by-name?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        PortElementsResponse restResult = restClient.callPortsPostRESTCall(methodName,
                                                                           urlTemplate,
                                                                           getNameRequestBody(assetManagerGUID, assetManagerName, name, effectiveTime, methodName),
                                                                           serverName,
                                                                           userId,
                                                                           startFrom,
                                                                           validatedPageSize,
                                                                           forLineage,
                                                                           forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the port metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public PortElement getPortByGUID(String  userId,
                                     String  assetManagerGUID,
                                     String  assetManagerName,
                                     String  portGUID,
                                     Date    effectiveTime,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "getPortByGUID";
        final String guidParameterName = "portGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/retrieve?forLineage={3}&forDuplicateProcessing={3}";

        PortElementResponse restResult = restClient.callPortPostRESTCall(methodName,
                                                                         urlTemplate,
                                                                         getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                         serverName,
                                                                         userId,
                                                                         portGUID,
                                                                         forLineage,
                                                                         forDuplicateProcessing);

        return restResult.getElement();
    }


    /* ===============================================================================
     * General linkage and classifications
     */


    /**
     * Classify a port, process or process as "BusinessSignificant" (this may affect the way that lineage is displayed).
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the port in the external process manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setBusinessSignificant(String  userId,
                                       String  assetManagerGUID,
                                       String  assetManagerName,
                                       String  elementGUID,
                                       String  elementExternalIdentifier,
                                       Date    effectiveTime,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "setBusinessSignificant";
        final String elementGUIDParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/is-business-significant?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, elementExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        elementGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the "BusinessSignificant" designation from the element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the element in the external process manager (can be null)
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearBusinessSignificant(String  userId,
                                         String  assetManagerGUID,
                                         String  assetManagerName,
                                         String  elementGUID,
                                         String  elementExternalIdentifier,
                                         Date    effectiveTime,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "clearBusinessSignificant";
        final String elementGUIDParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/is-business-significant/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, elementExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        elementGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Link two elements together to show that data flows from one to the other.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String setupDataFlow(String             userId,
                                String             assetManagerGUID,
                                String             assetManagerName,
                                boolean            assetManagerIsHome,
                                String             dataSupplierGUID,
                                String             dataConsumerGUID,
                                DataFlowProperties properties,
                                Date               effectiveTime,
                                boolean            forLineage,
                                boolean            forDuplicateProcessing) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName                    = "setupDataFlow";
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataSupplierGUID, dataSupplierGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(dataConsumerGUID, dataConsumerGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-flows/suppliers/{2}/consumers/{3}?assetManagerIsHome={4}&forLineage={5}&forDuplicateProcessing={6}";

        GUIDResponse results = restClient.callGUIDPostRESTCall(methodName,
                                                               urlTemplate,
                                                               getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, properties),
                                                               serverName,
                                                               userId,
                                                               dataSupplierGUID,
                                                               dataConsumerGUID,
                                                               assetManagerIsHome,
                                                               forLineage,
                                                               forDuplicateProcessing);

        return results.getGUID();
    }


    /**
     * Retrieve the data flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one data flow relationships between these two elements since it is used to disambiguate
     * the request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public DataFlowElement getDataFlow(String  userId,
                                       String  assetManagerGUID,
                                       String  assetManagerName,
                                       String  dataSupplierGUID,
                                       String  dataConsumerGUID,
                                       String  qualifiedName,
                                       Date    effectiveTime,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName                    = "getDataFlow";
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataSupplierGUID, dataSupplierGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(dataConsumerGUID, dataConsumerGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-flows/suppliers/{2}/consumers/{3}/retrieve?forLineage={4}&forDuplicateProcessing={5}";

        DataFlowElementResponse restResult = restClient.callDataFlowPostRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 getQualifiedNameRequestBody(assetManagerGUID, assetManagerName, qualifiedName, effectiveTime),
                                                                                 serverName,
                                                                                 userId,
                                                                                 dataSupplierGUID,
                                                                                 dataConsumerGUID,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing);

        return restResult.getElement();
    }


    /**
     * Update relationship between two elements that shows that data flows from one to the other.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void   updateDataFlow(String             userId,
                                 String             assetManagerGUID,
                                 String             assetManagerName,
                                 String             dataFlowGUID,
                                 DataFlowProperties properties,
                                 Date               effectiveTime,
                                 boolean            forLineage,
                                 boolean            forDuplicateProcessing) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName                    = "updateDataFlow";
        final String dataFlowGUIDParameterName     = "dataFlowGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFlowGUID, dataFlowGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-flows/{2}/update?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, properties),
                                        serverName,
                                        userId,
                                        dataFlowGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the data flow relationship between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearDataFlow(String  userId,
                              String  assetManagerGUID,
                              String  assetManagerName,
                              String  dataFlowGUID,
                              Date    effectiveTime,
                              boolean forLineage,
                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName                = "clearDataFlow";
        final String dataFlowGUIDParameterName = "dataFlowGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFlowGUID, dataFlowGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-flows/{2}/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        dataFlowGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the data flow relationships linked from a specific element to the downstream consumers.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<DataFlowElement> getDataFlowConsumers(String  userId,
                                                      String  assetManagerGUID,
                                                      String  assetManagerName,
                                                      String  dataSupplierGUID,
                                                      int     startFrom,
                                                      int     pageSize,
                                                      Date    effectiveTime,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName                    = "getDataFlowConsumers";
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataSupplierGUID, dataSupplierGUIDParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-flows/suppliers/{2}/consumers/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        DataFlowElementsResponse restResult = restClient.callDataFlowsPostRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                   serverName,
                                                                                   userId,
                                                                                   dataSupplierGUID,
                                                                                   startFrom,
                                                                                   validatedPageSize,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the data flow relationships linked from a specific element to the upstream suppliers.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<DataFlowElement> getDataFlowSuppliers(String  userId,
                                                      String  assetManagerGUID,
                                                      String  assetManagerName,
                                                      String  dataConsumerGUID,
                                                      int     startFrom,
                                                      int     pageSize,
                                                      Date    effectiveTime,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName                    = "getDataFlowSuppliers";
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataConsumerGUID, dataConsumerGUIDParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-flows/consumers/{2}/suppliers/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        DataFlowElementsResponse restResult = restClient.callDataFlowsPostRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                   serverName,
                                                                                   userId,
                                                                                   dataConsumerGUID,
                                                                                   startFrom,
                                                                                   validatedPageSize,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Link two elements to show that when one completes the next is started.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier for the control flow relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String setupControlFlow(String                userId,
                                   String                assetManagerGUID,
                                   String                assetManagerName,
                                   boolean               assetManagerIsHome,
                                   String                currentStepGUID,
                                   String                nextStepGUID,
                                   ControlFlowProperties properties,
                                   Date                  effectiveTime,
                                   boolean               forLineage,
                                   boolean               forDuplicateProcessing) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName                   = "setupControlFlow";
        final String currentStepGUIDParameterName = "currentStepGUID";
        final String nextStepGUIDParameterName    = "nextStepGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nextStepGUID, nextStepGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/control-flows/current-steps/{2}/next-steps/{3}?assetManagerIsHome={4}&forLineage={5}&forDuplicateProcessing={6}";

        GUIDResponse results = restClient.callGUIDPostRESTCall(methodName,
                                                               urlTemplate,
                                                               getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, properties),
                                                               serverName,
                                                               userId,
                                                               currentStepGUID,
                                                               nextStepGUID,
                                                               assetManagerIsHome,
                                                               forLineage,
                                                               forDuplicateProcessing);

        return results.getGUID();
    }


    /**
     * Retrieve the control flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one control flow relationships between these two elements since it is used to disambiguate
     * the request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ControlFlowElement getControlFlow(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  currentStepGUID,
                                             String  nextStepGUID,
                                             String  qualifiedName,
                                             Date    effectiveTime,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName                   = "getControlFlow";
        final String currentStepGUIDParameterName = "currentStepGUID";
        final String nextStepGUIDParameterName    = "nextStepGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nextStepGUID, nextStepGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/control-flows/current-steps/{2}/next-steps/{3}/retrieve?forLineage={4}&forDuplicateProcessing={5}";

        ControlFlowElementResponse restResult = restClient.callControlFlowPostRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       getQualifiedNameRequestBody(assetManagerGUID, assetManagerName, qualifiedName, effectiveTime),
                                                                                       serverName,
                                                                                       userId,
                                                                                       currentStepGUID,
                                                                                       nextStepGUID,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing);

        return restResult.getElement();
    }


    /**
     * Update the relationship between two elements that shows that when one completes the next is started.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateControlFlow(String                 userId,
                                  String                 assetManagerGUID,
                                  String                 assetManagerName,
                                  String                 controlFlowGUID,
                                  ControlFlowProperties  properties,
                                  Date                   effectiveTime,
                                  boolean                forLineage,
                                  boolean                forDuplicateProcessing) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName                   = "updateControlFlow";
        final String controlFlowGUIDParameterName = "controlFlowGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(controlFlowGUID, controlFlowGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/control-flows/{2}/update?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, properties),
                                        serverName,
                                        userId,
                                        controlFlowGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the control flow relationship between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearControlFlow(String  userId,
                                 String  assetManagerGUID,
                                 String  assetManagerName,
                                 String  controlFlowGUID,
                                 Date    effectiveTime,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName                   = "clearControlFlow";
        final String controlFlowGUIDParameterName = "controlFlowGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(controlFlowGUID, controlFlowGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/control-flows/{2}/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        controlFlowGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the control relationships linked from a specific element to the possible next elements in the process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param currentStepGUID unique identifier of the current step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ControlFlowElement> getControlFlowNextSteps(String  userId,
                                                            String  assetManagerGUID,
                                                            String  assetManagerName,
                                                            String  currentStepGUID,
                                                            int     startFrom,
                                                            int     pageSize,
                                                            Date    effectiveTime,
                                                            boolean forLineage,
                                                            boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName                   = "getControlFlowNextSteps";
        final String currentStepGUIDParameterName = "currentStepGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/control-flows/current-steps/{2}/next-steps/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        ControlFlowElementsResponse restResult = restClient.callControlFlowsPostRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                         serverName,
                                                                                         userId,
                                                                                         currentStepGUID,
                                                                                         startFrom,
                                                                                         validatedPageSize,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the control relationships linked from a specific element to the possible previous elements in the process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param currentStepGUID unique identifier of the previous step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ControlFlowElement> getControlFlowPreviousSteps(String  userId,
                                                                String  assetManagerGUID,
                                                                String  assetManagerName,
                                                                String  currentStepGUID,
                                                                int     startFrom,
                                                                int     pageSize,
                                                                Date    effectiveTime,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName                   = "getControlFlowPreviousSteps";
        final String currentStepGUIDParameterName = "currentStepGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/control-flows/current-steps/{2}/previous-steps/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        ControlFlowElementsResponse restResult = restClient.callControlFlowsPostRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                         serverName,
                                                                                         userId,
                                                                                         currentStepGUID,
                                                                                         startFrom,
                                                                                         validatedPageSize,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Link two elements together to show a request-response call between them.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String setupProcessCall(String                userId,
                                   String                assetManagerGUID,
                                   String                assetManagerName,
                                   boolean               assetManagerIsHome,
                                   String                callerGUID,
                                   String                calledGUID,
                                   ProcessCallProperties properties,
                                   Date                  effectiveTime,
                                   boolean               forLineage,
                                   boolean               forDuplicateProcessing) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName              = "setupProcessCall";
        final String callerGUIDParameterName = "callerGUID";
        final String calledGUIDParameterName = "calledGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(callerGUID, callerGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(calledGUID, calledGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/process-calls/callers/{2}/called/{3}?assetManagerIsHome={4}&forLineage={5}&forDuplicateProcessing={6}";

        GUIDResponse results = restClient.callGUIDPostRESTCall(methodName,
                                                               urlTemplate,
                                                               getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, properties),
                                                               serverName,
                                                               userId,
                                                               callerGUID,
                                                               calledGUID,
                                                               assetManagerIsHome,
                                                               forLineage,
                                                               forDuplicateProcessing);

        return results.getGUID();
    }


    /**
     * Retrieve the process call relationship between two elements.  The qualifiedName is optional unless there
     * is more than one process call relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ProcessCallElement getProcessCall(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  callerGUID,
                                             String  calledGUID,
                                             String  qualifiedName,
                                             Date    effectiveTime,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName                 = "getProcessCall";
        final String callerGUIDParameterName    = "callerGUID";
        final String calledGUIDParameterName    = "calledGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(callerGUID, callerGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(calledGUID, calledGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/process-calls/callers/{2}/called/{3}/retrieve?forLineage={4}&forDuplicateProcessing={5}";

        ProcessCallElementResponse restResult = restClient.callProcessCallPostRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       getQualifiedNameRequestBody(assetManagerGUID, assetManagerName, qualifiedName, effectiveTime),
                                                                                       serverName,
                                                                                       userId,
                                                                                       callerGUID,
                                                                                       calledGUID,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing);

        return restResult.getElement();
    }


    /**
     * Update the relationship between two elements that shows a request-response call between them.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processCallGUID unique identifier of the process call relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateProcessCall(String                userId,
                                  String                assetManagerGUID,
                                  String                assetManagerName,
                                  String                processCallGUID,
                                  ProcessCallProperties properties,
                                  Date                  effectiveTime,
                                  boolean               forLineage,
                                  boolean               forDuplicateProcessing) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                   = "updateProcessCall";
        final String processCallGUIDParameterName = "processCallGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processCallGUID, processCallGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/process-calls/{2}/update?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, properties),
                                        serverName,
                                        userId,
                                        processCallGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the process call relationship.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processCallGUID unique identifier of the process call relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearProcessCall(String  userId,
                                 String  assetManagerGUID,
                                 String  assetManagerName,
                                 String  processCallGUID,
                                 Date    effectiveTime,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName                   = "clearProcessCall";
        final String processCallGUIDParameterName = "processCallGUID";


        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processCallGUID, processCallGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/process-calls/{2}/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        processCallGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the process call relationships linked from a specific element to the elements it calls.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param callerGUID unique identifier of the element that is making the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessCallElement> getProcessCalled(String  userId,
                                                     String  assetManagerGUID,
                                                     String  assetManagerName,
                                                     String  callerGUID,
                                                     int     startFrom,
                                                     int     pageSize,
                                                     Date    effectiveTime,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName                 = "getProcessCalled";
        final String callerGUIDParameterName    = "callerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(callerGUID, callerGUIDParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/process-calls/callers/{2}/called/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        ProcessCallElementsResponse restResult = restClient.callProcessCallsPostRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                         serverName,
                                                                                         userId,
                                                                                         callerGUID,
                                                                                         startFrom,
                                                                                         validatedPageSize,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the process call relationships linked from a specific element to its callers.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param calledGUID unique identifier of the element that is processing the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessCallElement> getProcessCallers(String  userId,
                                                      String  assetManagerGUID,
                                                      String  assetManagerName,
                                                      String  calledGUID,
                                                      int     startFrom,
                                                      int     pageSize,
                                                      Date    effectiveTime,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName                 = "getProcessCallers";
        final String calledGUIDParameterName    = "calledGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(calledGUID, calledGUIDParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/process-calls/called/{2}/callers/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        ProcessCallElementsResponse restResult = restClient.callProcessCallsPostRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                         serverName,
                                                                                         userId,
                                                                                         calledGUID,
                                                                                         startFrom,
                                                                                         validatedPageSize,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Link to elements together to show that they are part of the lineage of the data that is moving
     * between the processes.  Typically, the lineage relationships stitch together processes and data assets
     * supported by different technologies.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupLineageMapping(String                   userId,
                                    String                   assetManagerGUID,
                                    String                   assetManagerName,
                                    String                   sourceElementGUID,
                                    String                   destinationElementGUID,
                                    LineageMappingProperties properties,
                                    Date                     effectiveTime,
                                    boolean                  forLineage,
                                    boolean                  forDuplicateProcessing) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName                          = "setupLineageMapping";
        final String sourceElementGUIDParameterName      = "sourceElementGUID";
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(destinationElementGUID, destinationElementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/lineage-mappings/sources/{2}/destinations/{3}?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, properties),
                                        serverName,
                                        userId,
                                        sourceElementGUID,
                                        destinationElementGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the relationship between two elements.  The qualifiedName is optional unless there
     * is more than one relationship between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param sourceElementGUID unique identifier of the element that is making the call
     * @param destinationElementGUID unique identifier of the element that is processing the call
     * @param qualifiedName unique identifier for this relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public LineageMappingElement getLineageMapping(String  userId,
                                                   String  assetManagerGUID,
                                                   String  assetManagerName,
                                                   String  sourceElementGUID,
                                                   String  destinationElementGUID,
                                                   String  qualifiedName,
                                                   Date    effectiveTime,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName                          = "getLineageMapping";
        final String sourceElementGUIDParameterName      = "sourceElementGUID";
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(destinationElementGUID, destinationElementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/lineage-mappings/sources/{2}/destinations/{3}/retrieve?forLineage={4}&forDuplicateProcessing={5}";

        LineageMappingElementResponse restResult = restClient.callLineageMappingPostRESTCall(methodName,
                                                                                             urlTemplate,
                                                                                             getQualifiedNameRequestBody(assetManagerGUID, assetManagerName, qualifiedName, effectiveTime),
                                                                                             serverName,
                                                                                             userId,
                                                                                             sourceElementGUID,
                                                                                             destinationElementGUID,
                                                                                             forLineage,
                                                                                             forDuplicateProcessing);

        return restResult.getElement();
    }


    /**
     * Update the lineage mapping relationship between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param lineageMappingGUID unique identifier of the lineage mapping relationship
     * @param properties unique identifier for this relationship along with description and/or additional relevant properties
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateLineageMapping(String                   userId,
                                     String                   assetManagerGUID,
                                     String                   assetManagerName,
                                     String                   lineageMappingGUID,
                                     LineageMappingProperties properties,
                                     Date                     effectiveTime,
                                     boolean                  forLineage,
                                     boolean                  forDuplicateProcessing) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName                   = "updateProcessCall";
        final String processCallGUIDParameterName = "lineageMappingGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(lineageMappingGUID, processCallGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/lineage-mappings/{2}/update?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, properties),
                                        serverName,
                                        userId,
                                        lineageMappingGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the lineage mapping between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param lineageMappingGUID unique identifier of the source
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearLineageMapping(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  lineageMappingGUID,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName                          = "clearLineageMapping";
        final String lineageMappingGUIDParameterName     = "lineageMappingGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(lineageMappingGUID, lineageMappingGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/lineage-mappings/{2}/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        lineageMappingGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the lineage mapping relationships linked from a specific source element to its destinations.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param sourceElementGUID unique identifier of the source
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of lineage mapping relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<LineageMappingElement> getDestinationLineageMappings(String  userId,
                                                                     String  assetManagerGUID,
                                                                     String  assetManagerName,
                                                                     String  sourceElementGUID,
                                                                     int     startFrom,
                                                                     int     pageSize,
                                                                     Date    effectiveTime,
                                                                     boolean forLineage,
                                                                     boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String methodName                          = "getDestinationLineageMappings";
        final String sourceElementGUIDParameterName      = "sourceElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/lineage-mappings/sources/{2}/destinations/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        LineageMappingElementsResponse results = restClient.callLineageMappingsPostRESTCall(methodName,
                                                                                            urlTemplate,
                                                                                            getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                            serverName,
                                                                                            userId,
                                                                                            sourceElementGUID,
                                                                                            startFrom,
                                                                                            validatedPageSize,
                                                                                            forLineage,
                                                                                            forDuplicateProcessing);

        return results.getElementList();
    }


    /**
     * Retrieve the lineage mapping relationships linked from a specific destination element to its sources.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param destinationElementGUID unique identifier of the destination
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of lineage mapping relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<LineageMappingElement> getSourceLineageMappings(String  userId,
                                                                String  assetManagerGUID,
                                                                String  assetManagerName,
                                                                String  destinationElementGUID,
                                                                int     startFrom,
                                                                int     pageSize,
                                                                Date    effectiveTime,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName                          = "getSourceLineageMappings";
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(destinationElementGUID, destinationElementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/lineage-mappings/destinations/{2}/sources/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        LineageMappingElementsResponse results = restClient.callLineageMappingsPostRESTCall(methodName,
                                                                                            urlTemplate,
                                                                                            getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                            serverName,
                                                                                            userId,
                                                                                            destinationElementGUID,
                                                                                            startFrom,
                                                                                            validatedPageSize,
                                                                                            forLineage,
                                                                                            forDuplicateProcessing);

        return results.getElementList();
    }
}
