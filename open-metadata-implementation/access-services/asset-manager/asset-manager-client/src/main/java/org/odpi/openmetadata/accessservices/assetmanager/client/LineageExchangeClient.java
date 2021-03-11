/* SPDX-License-Identifier: Apache 2.0 */
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

import java.util.Map;
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
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
     * @param processExternalIdentifier unique identifier of the process in the external process manager
     * @param processExternalIdentifierName name of property for the external identifier in the external process manager
     * @param processExternalIdentifierUsage optional usage description for the external identifier when calling the external process manager
     * @param processExternalIdentifierSource component that issuing this request.
     * @param processExternalIdentifierKeyPattern pattern for the external identifier within the external process manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the external process manager and open metadata
     * @param processProperties properties about the process to store
     * @param initialStatus status value for the new process (default = ACTIVE)
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createProcess(String              userId,
                                String              assetManagerGUID,
                                String              assetManagerName,
                                boolean             assetManagerIsHome,
                                String              processExternalIdentifier,
                                String              processExternalIdentifierName,
                                String              processExternalIdentifierUsage,
                                String              processExternalIdentifierSource,
                                KeyPattern          processExternalIdentifierKeyPattern,
                                Map<String, String> mappingProperties,
                                ProcessProperties   processProperties,
                                ProcessStatus       initialStatus) throws InvalidParameterException,
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
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   processExternalIdentifier,
                                                                                   processExternalIdentifierName,
                                                                                   processExternalIdentifierUsage,
                                                                                   processExternalIdentifierSource,
                                                                                   processExternalIdentifierKeyPattern,
                                                                                   mappingProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes?assetManagerIsHome={2}&initialStatus={3}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetManagerIsHome,
                                                                  initialStatus);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a process using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param templateGUID unique identifier of the metadata element to copy
     * @param processExternalIdentifier unique identifier of the process in the external process manager
     * @param processExternalIdentifierName name of property for the external identifier in the external process manager
     * @param processExternalIdentifierUsage optional usage description for the external identifier when calling the external process manager
     * @param processExternalIdentifierSource component that issuing this request.
     * @param processExternalIdentifierKeyPattern pattern for the external identifier within the external process manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the
     *                          external process manager and open metadata
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createProcessFromTemplate(String              userId,
                                            String              assetManagerGUID,
                                            String              assetManagerName,
                                            boolean             assetManagerIsHome,
                                            String              templateGUID,
                                            String              processExternalIdentifier,
                                            String              processExternalIdentifierName,
                                            String              processExternalIdentifierUsage,
                                            String              processExternalIdentifierSource,
                                            KeyPattern          processExternalIdentifierKeyPattern,
                                            Map<String, String> mappingProperties,
                                            TemplateProperties templateProperties) throws InvalidParameterException,
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
                                                                                   processExternalIdentifier,
                                                                                   processExternalIdentifierName,
                                                                                   processExternalIdentifierUsage,
                                                                                   processExternalIdentifierSource,
                                                                                   processExternalIdentifierKeyPattern,
                                                                                   mappingProperties,
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
                              ProcessProperties processProperties) throws InvalidParameterException,
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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}?isMergeUpdate={3}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        processGUID,
                                        isMergeUpdate);
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
                                    ProcessStatus processStatus) throws InvalidParameterException,
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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/status";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        processGUID);
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
     * @param containmentType describes the ownership of the sub-process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupProcessParent(String                 userId,
                                   String                 assetManagerGUID,
                                   String                 assetManagerName,
                                   boolean                assetManagerIsHome,
                                   String                 parentProcessGUID,
                                   String                 childProcessGUID,
                                   ProcessContainmentType containmentType) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName                     = "setupProcessParent";
        final String parentProcessGUIDParameterName = "parentProcessGUID";
        final String childProcessGUIDParameterName  = "childProcessGUID";
        final String containmentTypeParameterName   = "containmentType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentProcessGUID, parentProcessGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(childProcessGUID, childProcessGUIDParameterName, methodName);
        invalidParameterHandler.validateEnum(containmentType, containmentTypeParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/parent/{2}/child/{3}?assetManagerIsHome={4}&containmentType={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                        serverName,
                                        userId,
                                        parentProcessGUID,
                                        childProcessGUID,
                                        assetManagerIsHome,
                                        containmentType);
    }


    /**
     * Remove a parent-child relationship between two processes.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param parentProcessGUID unique identifier of the process in the external process manager that is to be the parent process
     * @param childProcessGUID unique identifier of the process in the external process manager that is to be the nested sub-process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearProcessParent(String userId,
                                   String assetManagerGUID,
                                   String assetManagerName,
                                   String parentProcessGUID,
                                   String childProcessGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName                     = "clearProcessParent";
        final String parentProcessGUIDParameterName = "parentProcessGUID";
        final String childProcessGUIDParameterName  = "childProcessGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentProcessGUID, parentProcessGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(childProcessGUID, childProcessGUIDParameterName, methodName);


        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/parent/{2}/children/{3}/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                        serverName,
                                        userId,
                                        parentProcessGUID,
                                        childProcessGUID);
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void publishProcess(String userId,
                               String assetManagerGUID,
                               String assetManagerName,
                               String processGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String methodName               = "publishProcess";
        final String processGUIDParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/publish";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                        serverName,
                                        userId,
                                        processGUID);
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void withdrawProcess(String userId,
                                String assetManagerGUID,
                                String assetManagerName,
                                String processGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName               = "withdrawProcess";
        final String processGUIDParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/withdraw";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                        serverName,
                                        userId,
                                        processGUID);
    }


    /**
     * Remove the metadata element representing a process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the metadata element to remove
     * @param processExternalIdentifier unique identifier of the process in the external process manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeProcess(String userId,
                              String assetManagerGUID,
                              String assetManagerName,
                              String processGUID,
                              String processExternalIdentifier) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName               = "removeProcess";
        final String processGUIDParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        this.getCorrelationProperties(assetManagerGUID,
                                                                      assetManagerName,
                                                                      processExternalIdentifier,
                                                                      methodName),
                                        serverName,
                                        userId,
                                        processGUID);
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
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessElement> findProcesses(String userId,
                                              String assetManagerGUID,
                                              String assetManagerName,
                                              String searchString,
                                              int    startFrom,
                                              int    pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName                = "findProcesses";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/by-search-string?startFrom={2}&pageSize={3}";

        ProcessElementsResponse restResult = restClient.callProcessesPostRESTCall(methodName,
                                                                                  urlTemplate,
                                                                                  requestBody,
                                                                                  serverName,
                                                                                  userId,
                                                                                  startFrom,
                                                                                  validatedPageSize);

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
     *
     * @return list of metadata elements describing the processes associated with the requested process manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessElement>   getProcessesForAssetManager(String userId,
                                                              String assetManagerGUID,
                                                              String assetManagerName,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "getProcessesForAssetManager";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/by-asset-manager?startFrom={2}&pageSize={3}";

        ProcessElementsResponse restResult = restClient.callProcessesPostRESTCall(methodName,
                                                                                  urlTemplate,
                                                                                  getAssetManagerIdentifiersRequestBody(assetManagerGUID,
                                                                                                                        assetManagerName),
                                                                                  serverName,
                                                                                  userId,
                                                                                  startFrom,
                                                                                  validatedPageSize);

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
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessElement>   getProcessesByName(String userId,
                                                     String assetManagerGUID,
                                                     String assetManagerName,
                                                     String name,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName        = "getProcessesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/by-name?startFrom={2}&pageSize={3}";

        ProcessElementsResponse restResult = restClient.callProcessesPostRESTCall(methodName,
                                                                                  urlTemplate,
                                                                                  requestBody,
                                                                                  serverName,
                                                                                  userId,
                                                                                  startFrom,
                                                                                  validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ProcessElement getProcessByGUID(String userId,
                                           String assetManagerGUID,
                                           String assetManagerName,
                                           String processGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "getProcessByGUID";
        final String guidParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/retrieve";

        ProcessElementResponse restResult = restClient.callProcessPostRESTCall(methodName,
                                                                               urlTemplate,
                                                                               getAssetManagerIdentifiersRequestBody(assetManagerGUID,
                                                                                                                     assetManagerName),
                                                                               serverName,
                                                                               userId,
                                                                               processGUID);

        return restResult.getElement();
    }


    /**
     * Retrieve the process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return parent process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ProcessElement getProcessParent(String userId,
                                           String assetManagerGUID,
                                           String assetManagerName,
                                           String processGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "getProcessParent";
        final String guidParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/parent/retrieve";

        ProcessElementResponse restResult = restClient.callProcessPostRESTCall(methodName,
                                                                               urlTemplate,
                                                                               getAssetManagerIdentifiersRequestBody(assetManagerGUID,
                                                                                                                     assetManagerName),
                                                                               serverName,
                                                                               userId,
                                                                               processGUID);

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
     *
     * @return list of process element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessElement> getSubProcesses(String userId,
                                                String assetManagerGUID,
                                                String assetManagerName,
                                                String processGUID,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName        = "getSubProcesses";
        final String guidParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);


        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/children?startFrom={2}&pageSize={3}";

        ProcessElementsResponse restResult = restClient.callProcessesPostRESTCall(methodName,
                                                                                  urlTemplate,
                                                                                  getAssetManagerIdentifiersRequestBody(assetManagerGUID,
                                                                                                                        assetManagerName),
                                                                                  serverName,
                                                                                  userId,
                                                                                  startFrom,
                                                                                  validatedPageSize);

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
     * @param portExternalIdentifier unique identifier of the port in the external process manager
     * @param portExternalIdentifierName name of property for the external identifier in the external process manager
     * @param portExternalIdentifierUsage optional usage description for the external identifier when calling the external process manager
     * @param portExternalIdentifierSource component that issuing this request.
     * @param portExternalIdentifierKeyPattern pattern for the external identifier within the external process manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the
     *                          external process manager and open metadata
     * @param portProperties properties for the port
     *
     * @return unique identifier of the new metadata element for the port
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createPort(String              userId,
                             String              assetManagerGUID,
                             String              assetManagerName,
                             boolean             assetManagerIsHome,
                             String              processGUID,
                             String              portExternalIdentifier,
                             String              portExternalIdentifierName,
                             String              portExternalIdentifierUsage,
                             String              portExternalIdentifierSource,
                             KeyPattern          portExternalIdentifierKeyPattern,
                             Map<String, String> mappingProperties,
                             PortProperties      portProperties) throws InvalidParameterException,
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
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   portExternalIdentifier,
                                                                                   portExternalIdentifierName,
                                                                                   portExternalIdentifierUsage,
                                                                                   portExternalIdentifierSource,
                                                                                   portExternalIdentifierKeyPattern,
                                                                                   mappingProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "processes/{2}/ports?assetManagerIsHome={3}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  processGUID,
                                                                  assetManagerIsHome);

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
                           PortProperties portProperties) throws InvalidParameterException,
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

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/update";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        portGUID);
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
                                 String  portGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String methodName               = "setupProcessPort";
        final String processGUIDParameterName = "processGUID";
        final String portGUIDParameterName    = "portGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/ports/{3}?assetManagerIsHome={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                        serverName,
                                        userId,
                                        processGUID,
                                        portGUID,
                                        assetManagerIsHome);
    }


    /**
     * Unlink a port from a process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processGUID unique identifier of the process
     * @param portGUID unique identifier of the port

     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearProcessPort(String userId,
                                 String assetManagerGUID,
                                 String assetManagerName,
                                 String processGUID,
                                 String portGUID) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String methodName               = "clearProcessPort";
        final String processGUIDParameterName = "processGUID";
        final String portGUIDParameterName    = "portGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/ports/{3}/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                        serverName,
                                        userId,
                                        processGUID,
                                        portGUID);
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
                                    String  portTwoGUID) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName               = "setupPortDelegation";
        final String portOneGUIDParameterName = "portOneGUID";
        final String portTwoGUIDParameterName = "portTwoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portOneGUID, portOneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(portTwoGUID, portTwoGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/port-delegations/{3}?assetManagerIsHome={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                        serverName,
                                        userId,
                                        portOneGUID,
                                        portTwoGUID,
                                        assetManagerIsHome);
    }


    /**
     * Remove the port delegation relationship between two ports.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portOneGUID unique identifier of the port at end 1
     * @param portTwoGUID unique identifier of the port at end 2

     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearPortDelegation(String userId,
                                    String assetManagerGUID,
                                    String assetManagerName,
                                    String portOneGUID,
                                    String portTwoGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName               = "clearPortDelegation";
        final String portOneGUIDParameterName = "portOneGUID";
        final String portTwoGUIDParameterName = "portTwoGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portOneGUID, portOneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(portTwoGUID, portTwoGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/port-delegations/{3}/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                        serverName,
                                        userId,
                                        portOneGUID,
                                        portTwoGUID);
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
                                    String  schemaTypeGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName                  = "setupPortSchemaType";
        final String portGUIDParameterName       = "portGUID";
        final String schemaTypeGUIDParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/schemaType/{3}?assetManagerIsHome={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                        serverName,
                                        userId,
                                        portGUID,
                                        schemaTypeGUID,
                                        assetManagerIsHome);
    }


    /**
     * Remove the schema type from a port.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the port
     * @param schemaTypeGUID unique identifier of the schemaType

     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearPortSchemaType(String userId,
                                    String assetManagerGUID,
                                    String assetManagerName,
                                    String portGUID,
                                    String schemaTypeGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName                  = "clearPortSchemaType";
        final String portGUIDParameterName       = "portGUID";
        final String schemaTypeGUIDParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/schemaType/{3}/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                        serverName,
                                        userId,
                                        portGUID,
                                        schemaTypeGUID);
    }


    /**
     * Remove the metadata element representing a port.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the metadata element to remove
     * @param portExternalIdentifier unique identifier of the port in the external process manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removePort(String userId,
                           String assetManagerGUID,
                           String assetManagerName,
                           String portGUID,
                           String portExternalIdentifier) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName            = "removePort";
        final String portGUIDParameterName = "portGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, portGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        this.getCorrelationProperties(assetManagerGUID,
                                                                      assetManagerName,
                                                                      portExternalIdentifier,
                                                                      methodName),
                                        serverName,
                                        userId,
                                        portGUID);
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
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<PortElement>   findPorts(String userId,
                                         String assetManagerGUID,
                                         String assetManagerName,
                                         String searchString,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName                = "findPorts";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/by-search-string?startFrom={2}&pageSize={3}";

        PortElementsResponse restResult = restClient.callPortsPostRESTCall(methodName,
                                                                           urlTemplate,
                                                                           requestBody,
                                                                           serverName,
                                                                           userId,
                                                                           startFrom,
                                                                           validatedPageSize);

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
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<PortElement> getPortsForProcess(String userId,
                                                String assetManagerGUID,
                                                String assetManagerName,
                                                String processGUID,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName        = "getPortsForProcess";
        final String guidParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/processes/{2}/ports/retrieve?startFrom={3}&pageSize={4}";

        PortElementsResponse restResult = restClient.callPortsPostRESTCall(methodName,
                                                                           urlTemplate,
                                                                           getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                                                           serverName,
                                                                           userId,
                                                                           processGUID,
                                                                           startFrom,
                                                                           validatedPageSize);

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
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<PortElement>  getPortUse(String userId,
                                         String assetManagerGUID,
                                         String assetManagerName,
                                         String portGUID,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName        = "getPortUse";
        final String guidParameterName = "portGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/used-by/retrieve?startFrom={3}&pageSize={4}";

        PortElementsResponse restResult = restClient.callPortsPostRESTCall(methodName,
                                                                           urlTemplate,
                                                                           getAssetManagerIdentifiersRequestBody(assetManagerGUID,
                                                                                                                 assetManagerName),
                                                                           serverName,
                                                                           userId,
                                                                           portGUID,
                                                                           startFrom,
                                                                           validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the port that this port delegates to.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the starting port alias
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public PortElement getPortDelegation(String userId,
                                         String assetManagerGUID,
                                         String assetManagerName,
                                         String portGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName        = "getPortDelegation";
        final String guidParameterName = "portGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/port-delegations/retrieve";

        PortElementResponse restResult = restClient.callPortPostRESTCall(methodName,
                                                                         urlTemplate,
                                                                         getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                                                         serverName,
                                                                         userId,
                                                                         portGUID);

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
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<PortElement>   getPortsByName(String userId,
                                              String assetManagerGUID,
                                              String assetManagerName,
                                              String name,
                                              int    startFrom,
                                              int    pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName        = "getPortsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/by-name?startFrom={2}&pageSize={3}";

        PortElementsResponse restResult = restClient.callPortsPostRESTCall(methodName,
                                                                           urlTemplate,
                                                                           requestBody,
                                                                           serverName,
                                                                           userId,
                                                                           startFrom,
                                                                           validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the port metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param portGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public PortElement getPortByGUID(String userId,
                                     String assetManagerGUID,
                                     String assetManagerName,
                                     String portGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName = "getPortByGUID";
        final String guidParameterName = "portGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/ports/{2}/retrieve";

        PortElementResponse restResult = restClient.callPortPostRESTCall(methodName,
                                                                         urlTemplate,
                                                                         getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                                                         serverName,
                                                                         userId,
                                                                         portGUID);

        return restResult.getElement();
    }


    /* ===============================================================================
     * General linkage and classifications
     */


    /**
     * Classify a port, process or process as "BusinessSignificant" (this may effect the way that lineage is displayed).
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the port in the external process manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setBusinessSignificant(String userId,
                                       String assetManagerGUID,
                                       String assetManagerName,
                                       String elementGUID,
                                       String elementExternalIdentifier) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "setBusinessSignificant";
        final String elementGUIDParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/is-business-significant";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        this.getCorrelationProperties(assetManagerGUID,
                                                                      assetManagerName,
                                                                      elementExternalIdentifier,
                                                                      methodName),
                                        serverName,
                                        userId,
                                        elementGUID);
    }


    /**
     * Remove the "BusinessSignificant" designation from the element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the element in the external process manager (can be null)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearBusinessSignificant(String userId,
                                         String assetManagerGUID,
                                         String assetManagerName,
                                         String elementGUID,
                                         String elementExternalIdentifier) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "clearBusinessSignificant";
        final String elementGUIDParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/is-business-significant/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        this.getCorrelationProperties(assetManagerGUID,
                                                                      assetManagerName,
                                                                      elementExternalIdentifier,
                                                                      methodName),
                                        serverName,
                                        userId,
                                        elementGUID);
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
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that determines the subset of the data that flows
     *
     * @return unique identifier of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String setupDataFlow(String  userId,
                                String  assetManagerGUID,
                                String  assetManagerName,
                                boolean assetManagerIsHome,
                                String  dataSupplierGUID,
                                String  dataConsumerGUID,
                                String  qualifiedName,
                                String  description,
                                String  formula) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        final String methodName                    = "setupDataFlow";
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataSupplierGUID, dataSupplierGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(dataConsumerGUID, dataConsumerGUIDParameterName, methodName);

        DataFlowRequestBody requestBody = new DataFlowRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);

        DataFlowProperties properties = new DataFlowProperties();
        properties.setQualifiedName(qualifiedName);
        properties.setDescription(description);
        properties.setFormula(formula);

        requestBody.setProperties(properties);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-flows/suppliers/{2}/consumers/{3}?assetManagerIsHome={4}";

        GUIDResponse results = restClient.callGUIDPostRESTCall(methodName,
                                                               urlTemplate,
                                                               requestBody,
                                                               serverName,
                                                               userId,
                                                               dataSupplierGUID,
                                                               dataConsumerGUID,
                                                               assetManagerIsHome);

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
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public DataFlowElement getDataFlow(String userId,
                                       String assetManagerGUID,
                                       String assetManagerName,
                                       String dataSupplierGUID,
                                       String dataConsumerGUID,
                                       String qualifiedName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName                    = "getDataFlow";
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";
        final String qualifiedNameParameterName    = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataSupplierGUID, dataSupplierGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(dataConsumerGUID, dataConsumerGUIDParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setName(qualifiedName);
        requestBody.setNameParameterName(qualifiedNameParameterName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-flows/suppliers/{2}/consumers/{3}/retrieve";

        DataFlowElementResponse restResult = restClient.callDataFlowPostRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 requestBody,
                                                                                 serverName,
                                                                                 userId,
                                                                                 dataSupplierGUID,
                                                                                 dataConsumerGUID);

        return restResult.getElement();
    }


    /**
     * Update relationship between two elements that shows that data flows from one to the other.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that determines the subset of the data that flows
     *
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void   updateDataFlow(String userId,
                                 String assetManagerGUID,
                                 String assetManagerName,
                                 String dataFlowGUID,
                                 String qualifiedName,
                                 String description,
                                 String formula) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        final String methodName                    = "updateDataFlow";
        final String dataFlowGUIDParameterName     = "dataFlowGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFlowGUID, dataFlowGUIDParameterName, methodName);

        DataFlowRequestBody requestBody = new DataFlowRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);

        DataFlowProperties properties = new DataFlowProperties();
        properties.setQualifiedName(qualifiedName);
        properties.setDescription(description);
        properties.setFormula(formula);

        requestBody.setProperties(properties);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-flows/{2}/update";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        dataFlowGUID);
    }


    /**
     * Remove the data flow relationship between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param dataFlowGUID unique identifier of the data flow relationship

     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearDataFlow(String userId,
                              String assetManagerGUID,
                              String assetManagerName,
                              String dataFlowGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String methodName                = "clearDataFlow";
        final String dataFlowGUIDParameterName = "dataFlowGUID";


        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataFlowGUID, dataFlowGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-flows/{2}/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                        serverName,
                                        userId,
                                        dataFlowGUID);
    }


    /**
     * Retrieve the data flow relationships linked from an specific element to the downstream consumers.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param dataSupplierGUID unique identifier of the data supplier
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<DataFlowElement> getDataFlowConsumers(String userId,
                                                      String assetManagerGUID,
                                                      String assetManagerName,
                                                      String dataSupplierGUID) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                    = "getDataFlowConsumers";
        final String dataSupplierGUIDParameterName = "dataSupplierGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataSupplierGUID, dataSupplierGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-flows/suppliers/{2}/consumers/retrieve";

        DataFlowElementsResponse restResult = restClient.callDataFlowsPostRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   getAssetManagerIdentifiersRequestBody(assetManagerGUID,
                                                                                                                         assetManagerName),
                                                                                   serverName,
                                                                                   userId,
                                                                                   dataSupplierGUID);

        return restResult.getElementList();
    }


    /**
     * Retrieve the data flow relationships linked from an specific element to the upstream suppliers.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param dataConsumerGUID unique identifier of the data consumer
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<DataFlowElement> getDataFlowSuppliers(String userId,
                                                      String assetManagerGUID,
                                                      String assetManagerName,
                                                      String dataConsumerGUID) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                    = "getDataFlowSuppliers";
        final String dataConsumerGUIDParameterName = "dataConsumerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(dataConsumerGUID, dataConsumerGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-flows/consumers/{2}/suppliers/retrieve";

        DataFlowElementsResponse restResult = restClient.callDataFlowsPostRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   getAssetManagerIdentifiersRequestBody(assetManagerGUID,
                                                                                                                         assetManagerName),
                                                                                   serverName,
                                                                                   userId,
                                                                                   dataConsumerGUID);

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
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param guard function that must be true to travel down this control flow
     *
     * @return unique identifier for the control flow relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String setupControlFlow(String  userId,
                                   String  assetManagerGUID,
                                   String  assetManagerName,
                                   boolean assetManagerIsHome,
                                   String  currentStepGUID,
                                   String  nextStepGUID,
                                   String  qualifiedName,
                                   String  description,
                                   String  guard) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String methodName                   = "setupControlFlow";
        final String currentStepGUIDParameterName = "currentStepGUID";
        final String nextStepGUIDParameterName    = "nextStepGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nextStepGUID, nextStepGUIDParameterName, methodName);

        ControlFlowRequestBody requestBody = new ControlFlowRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);

        ControlFlowProperties properties = new ControlFlowProperties();
        properties.setQualifiedName(qualifiedName);
        properties.setDescription(description);
        properties.setGuard(guard);

        requestBody.setProperties(properties);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/control-flows/current-steps/{2}/next-steps/{3}?assetManagerIsHome={4}";

        GUIDResponse results = restClient.callGUIDPostRESTCall(methodName,
                                                               urlTemplate,
                                                               requestBody,
                                                               serverName,
                                                               userId,
                                                               currentStepGUID,
                                                               nextStepGUID,
                                                               assetManagerIsHome);

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
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ControlFlowElement getControlFlow(String userId,
                                             String assetManagerGUID,
                                             String assetManagerName,
                                             String currentStepGUID,
                                             String nextStepGUID,
                                             String qualifiedName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName                   = "getControlFlow";
        final String currentStepGUIDParameterName = "currentStepGUID";
        final String nextStepGUIDParameterName    = "nextStepGUID";
        final String qualifiedNameParameterName   = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nextStepGUID, nextStepGUIDParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setName(qualifiedName);
        requestBody.setNameParameterName(qualifiedNameParameterName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/control-flows/current-steps/{2}/next-steps/{3}/retrieve";

        ControlFlowElementResponse restResult = restClient.callControlFlowPostRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       requestBody,
                                                                                       serverName,
                                                                                       userId,
                                                                                       currentStepGUID,
                                                                                       nextStepGUID);

        return restResult.getElement();
    }


    /**
     * Update the relationship between two elements that shows that when one completes the next is started.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param guard function that must be true to travel down this control flow
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateControlFlow(String userId,
                                  String assetManagerGUID,
                                  String assetManagerName,
                                  String controlFlowGUID,
                                  String qualifiedName,
                                  String description,
                                  String guard) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        final String methodName                   = "updateControlFlow";
        final String controlFlowGUIDParameterName = "controlFlowGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(controlFlowGUID, controlFlowGUIDParameterName, methodName);

        ControlFlowRequestBody requestBody = new ControlFlowRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);

        ControlFlowProperties properties = new ControlFlowProperties();
        properties.setQualifiedName(qualifiedName);
        properties.setDescription(description);
        properties.setGuard(guard);

        requestBody.setProperties(properties);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/control-flows/{2}/update";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        controlFlowGUID);
    }


    /**
     * Remove the control flow relationship between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param controlFlowGUID unique identifier of the  control flow relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearControlFlow(String userId,
                                 String assetManagerGUID,
                                 String assetManagerName,
                                 String controlFlowGUID) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName                   = "clearControlFlow";
        final String controlFlowGUIDParameterName = "controlFlowGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(controlFlowGUID, controlFlowGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/control-flows/{2}/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                        serverName,
                                        userId,
                                        controlFlowGUID);
    }


    /**
     * Retrieve the control relationships linked from an specific element to the possible next elements in the process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param currentStepGUID unique identifier of the current step
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ControlFlowElement> getControlFlowNextSteps(String userId,
                                                            String assetManagerGUID,
                                                            String assetManagerName,
                                                            String currentStepGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName                   = "getControlFlowNextSteps";
        final String currentStepGUIDParameterName = "currentStepGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/control-flows/current-steps/{2}/next-steps/retrieve";

        ControlFlowElementsResponse restResult = restClient.callControlFlowsPostRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         getAssetManagerIdentifiersRequestBody(assetManagerGUID,
                                                                                                                               assetManagerName),
                                                                                         serverName,
                                                                                         userId,
                                                                                         currentStepGUID);

        return restResult.getElementList();
    }


    /**
     * Retrieve the control relationships linked from an specific element to the possible previous elements in the process.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param currentStepGUID unique identifier of the previous step
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ControlFlowElement> getControlFlowPreviousSteps(String userId,
                                                                String assetManagerGUID,
                                                                String assetManagerName,
                                                                String currentStepGUID) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName                   = "getControlFlowPreviousSteps";
        final String currentStepGUIDParameterName = "currentStepGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentStepGUID, currentStepGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/control-flows/current-steps/{2}/previous-steps/retrieve";

        ControlFlowElementsResponse restResult = restClient.callControlFlowsPostRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         getAssetManagerIdentifiersRequestBody(assetManagerGUID,
                                                                                                                               assetManagerName),
                                                                                         serverName,
                                                                                         userId,
                                                                                         currentStepGUID);

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
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that determines the subset of the data that flows
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String setupProcessCall(String  userId,
                                   String  assetManagerGUID,
                                   String  assetManagerName,
                                   boolean assetManagerIsHome,
                                   String  callerGUID,
                                   String  calledGUID,
                                   String  qualifiedName,
                                   String  description,
                                   String  formula) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName              = "setupProcessCall";
        final String callerGUIDParameterName = "callerGUID";
        final String calledGUIDParameterName = "calledGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(callerGUID, callerGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(calledGUID, calledGUIDParameterName, methodName);

        ProcessCallRequestBody requestBody = new ProcessCallRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);

        ProcessCallProperties properties = new ProcessCallProperties();
        properties.setQualifiedName(qualifiedName);
        properties.setDescription(description);
        properties.setFormula(formula);

        requestBody.setProperties(properties);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/process-calls/callers/{2}/called/{3}?assetManagerIsHome={4}";

        GUIDResponse results = restClient.callGUIDPostRESTCall(methodName,
                                                               urlTemplate,
                                                               requestBody,
                                                               serverName,
                                                               userId,
                                                               callerGUID,
                                                               calledGUID,
                                                               assetManagerIsHome);

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
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ProcessCallElement getProcessCall(String userId,
                                             String assetManagerGUID,
                                             String assetManagerName,
                                             String callerGUID,
                                             String calledGUID,
                                             String qualifiedName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName                 = "getProcessCall";
        final String callerGUIDParameterName    = "callerGUID";
        final String calledGUIDParameterName    = "calledGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(callerGUID, callerGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(calledGUID, calledGUIDParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setName(qualifiedName);
        requestBody.setNameParameterName(qualifiedNameParameterName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/process-calls/callers/{2}/called/{3}/retrieve";

        ProcessCallElementResponse restResult = restClient.callProcessCallPostRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       requestBody,
                                                                                       serverName,
                                                                                       userId,
                                                                                       callerGUID,
                                                                                       calledGUID);

        return restResult.getElement();
    }


    /**
     * Update the relationship between two elements that shows a request-response call between them.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processCallGUID unique identifier of the process call relationship
     * @param qualifiedName unique identifier for this relationship
     * @param description description and/or purpose of the data flow
     * @param formula function that determines the subset of the data that flows
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateProcessCall(String userId,
                                  String assetManagerGUID,
                                  String assetManagerName,
                                  String processCallGUID,
                                  String qualifiedName,
                                  String description,
                                  String formula) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String methodName                   = "updateProcessCall";
        final String processCallGUIDParameterName = "processCallGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processCallGUID, processCallGUIDParameterName, methodName);

        ProcessCallRequestBody requestBody = new ProcessCallRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);

        ProcessCallProperties properties = new ProcessCallProperties();
        properties.setQualifiedName(qualifiedName);
        properties.setDescription(description);
        properties.setFormula(formula);

        requestBody.setProperties(properties);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/process-calls/{2}/update";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        processCallGUID);
    }


    /**
     * Remove the process call relationship.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param processCallGUID unique identifier of the process call relationship

     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearProcessCall(String userId,
                                 String assetManagerGUID,
                                 String assetManagerName,
                                 String processCallGUID) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName                   = "clearProcessCall";
        final String processCallGUIDParameterName = "processCallGUID";


        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processCallGUID, processCallGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/process-calls/{2}/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                        serverName,
                                        userId,
                                        processCallGUID);
    }


    /**
     * Retrieve the process call relationships linked from an specific element to the elements it calls.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param callerGUID unique identifier of the element that is making the call
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessCallElement> getProcessCalled(String userId,
                                                     String assetManagerGUID,
                                                     String assetManagerName,
                                                     String callerGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                 = "getProcessCalled";
        final String callerGUIDParameterName    = "callerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(callerGUID, callerGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/process-calls/callers/{2}/called/retrieve";

        ProcessCallElementsResponse restResult = restClient.callProcessCallsPostRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         getAssetManagerIdentifiersRequestBody(assetManagerGUID,
                                                                                                                               assetManagerName),
                                                                                         serverName,
                                                                                         userId,
                                                                                         callerGUID);

        return restResult.getElementList();
    }


    /**
     * Retrieve the process call relationships linked from an specific element to its callers.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param calledGUID unique identifier of the element that is processing the call
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ProcessCallElement> getProcessCallers(String userId,
                                                      String assetManagerGUID,
                                                      String assetManagerName,
                                                      String calledGUID) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName                 = "getProcessCallers";
        final String calledGUIDParameterName    = "calledGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(calledGUID, calledGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/process-calls/called/{2}/callers/retrieve";

        ProcessCallElementsResponse restResult = restClient.callProcessCallsPostRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         getAssetManagerIdentifiersRequestBody(assetManagerGUID,
                                                                                                                               assetManagerName),
                                                                                         serverName,
                                                                                         userId,
                                                                                         calledGUID);

        return restResult.getElementList();
    }


    /**
     * Link to elements together to show that they are part of the lineage of the data that is moving
     * between the processes.  Typically the lineage relationships stitch together processes and data assets
     * supported by different technologies.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupLineageMapping(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  sourceElementGUID,
                                    String  destinationElementGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName                          = "setupLineageMapping";
        final String sourceElementGUIDParameterName      = "sourceElementGUID";
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(destinationElementGUID, destinationElementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/lineage-mappings/sources/{2}/destinations/{3}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                        serverName,
                                        userId,
                                        sourceElementGUID,
                                        destinationElementGUID);
    }


    /**
     * Remove the lineage mapping between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearLineageMapping(String userId,
                                    String assetManagerGUID,
                                    String assetManagerName,
                                    String sourceElementGUID,
                                    String destinationElementGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName                          = "clearLineageMapping";
        final String sourceElementGUIDParameterName      = "sourceElementGUID";
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(destinationElementGUID, destinationElementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/lineage-mappings/sources/{2}/destinations/{3}/remove";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                        serverName,
                                        userId,
                                        sourceElementGUID,
                                        destinationElementGUID);
    }


    /**
     * Retrieve the lineage mapping relationships linked from an specific source element to its destinations.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param sourceElementGUID unique identifier of the source
     *
     * @return list of lineage mapping relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<LineageMappingElement> getDestinationLineageMappings(String userId,
                                                                     String assetManagerGUID,
                                                                     String assetManagerName,
                                                                     String sourceElementGUID) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName                          = "getDestinationLineageMappings";
        final String sourceElementGUIDParameterName      = "sourceElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/lineage-mappings/sources/{2}/destinations/retrieve";

        LineageMappingElementsResponse results = restClient.callLineageMappingsPostRESTCall(methodName,
                                                                                            urlTemplate,
                                                                                            getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                                                                            serverName,
                                                                                            userId,
                                                                                            sourceElementGUID);

        return results.getElementList();
    }


    /**
     * Retrieve the lineage mapping relationships linked from an specific destination element to its sources.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param destinationElementGUID unique identifier of the destination
     *
     * @return list of lineage mapping relationships
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<LineageMappingElement> getSourceLineageMappings(String userId,
                                                                String assetManagerGUID,
                                                                String assetManagerName,
                                                                String destinationElementGUID) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName                          = "getSourceLineageMappings";
        final String destinationElementGUIDParameterName = "destinationElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(destinationElementGUID, destinationElementGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/lineage-mappings/destinations/{2}/sources/retrieve";

        LineageMappingElementsResponse results = restClient.callLineageMappingsPostRESTCall(methodName,
                                                                                            urlTemplate,
                                                                                            getAssetManagerIdentifiersRequestBody(assetManagerGUID, assetManagerName),
                                                                                            serverName,
                                                                                            userId,
                                                                                            destinationElementGUID);

        return results.getElementList();
    }
}
