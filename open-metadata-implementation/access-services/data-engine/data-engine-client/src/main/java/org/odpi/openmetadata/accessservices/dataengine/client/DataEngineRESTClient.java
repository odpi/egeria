/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.client;

import org.odpi.openmetadata.accessservices.dataengine.model.*;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * The Data Engine Open Metadata Access Service (OMAS) provides an interface for data engine tools to create
 * processes with ports, schemas and relationships. See interface definition for more explanation.
 */
public class DataEngineRESTClient extends OCFRESTClient implements DataEngineClient {
    private static final String DATA_ENGINE_PATH = "/servers/{0}/open-metadata/access-services/data-engine/users/{1}/";
    private static final String PROCESS_URL_TEMPLATE = DATA_ENGINE_PATH + "processes";
    private static final String DATA_ENGINE_REGISTRATION_URL_TEMPLATE = DATA_ENGINE_PATH + "registration";
    private static final String SCHEMA_TYPE_URL_TEMPLATE = DATA_ENGINE_PATH + "schema-types";
    private static final String PORT_IMPLEMENTATION_URL_TEMPLATE = DATA_ENGINE_PATH + "port-implementations";
    private static final String PORT_ALIAS_URL_TEMPLATE = DATA_ENGINE_PATH + "port-aliases";
    private static final String PROCESS_HIERARCHY_URL_TEMPLATE = DATA_ENGINE_PATH + "process-hierarchies";
    private static final String LINEAGE_MAPPINGS_URL_TEMPLATE = DATA_ENGINE_PATH + "lineage-mappings";

    private static final String PROCESSES_METHOD_NAME = "createOrUpdateProcesses";
    private static final String PROCESSES_DELETE_METHOD_NAME = "deleteProcesses";
    private static final String EXTERNAL_DATA_ENGINE_METHOD_NAME = "createExternalDataEngine";
    private static final String EXTERNAL_DATA_ENGINE_DELETE_METHOD_NAME = "createExternalDataEngine";
    private static final String SCHEMA_TYPE_METHOD_NAME = "createOrUpdateSchemaType";
    private static final String SCHEMA_TYPE_DELETE_METHOD_NAME = "deleteSchemaType";
    private static final String PORT_IMPLEMENTATION_METHOD_NAME = "createOrUpdatePortImplementation";
    private static final String PORT_IMPLEMENTATION_DELETE_METHOD_NAME = "deletePortImplementation";
    private static final String PORT_ALIAS_METHOD_NAME = "createOrUpdatePortAlias";
    private static final String PORT_ALIAS_DELETE_METHOD_NAME = "deletePortAlias";
    private static final String PROCESS_HIERARCHY_METHOD_NAME = "createOrUpdateProcessHierarchy";
    private static final String LINEAGE_MAPPINGS_METHOD_NAME = "addLineageMappings";

    private String serverPlatformRootURL;
    private String externalSourceName;
    private DeleteSemantic deleteSemantic = DeleteSemantic.HARD;
    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Create a new client without authentication.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     *
     * @throws InvalidParameterException null URL or server name
     */
    public DataEngineRESTClient(String serverName, String serverPlatformRootURL) throws InvalidParameterException {
        super(serverName, serverPlatformRootURL);

        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;
    }

    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException null URL or server name
     */
    public DataEngineRESTClient(String serverName, String serverPlatformRootURL, String userId, String password) throws InvalidParameterException {
        super(serverName, serverPlatformRootURL, userId, password);

        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;
    }

    public String getExternalSourceName() {
        return externalSourceName;
    }

    public void setExternalSourceName(String externalSourceName) {
        this.externalSourceName = externalSourceName;
    }

    public DeleteSemantic getDeleteSemantic() {
        return deleteSemantic;
    }

    public void setDeleteSemantic(DeleteSemantic deleteSemantic) {
        this.deleteSemantic = deleteSemantic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> createOrUpdateProcesses(String userId, List<Process> processes) throws
                                                                                        InvalidParameterException,
                                                                                        PropertyServerException {

        invalidParameterHandler.validateUserId(userId, PROCESSES_METHOD_NAME);

        ProcessesRequestBody requestBody = new ProcessesRequestBody();
        requestBody.setProcesses(processes);
        requestBody.setExternalSourceName(externalSourceName);

        return callProcessListPostRESTCall(userId, PROCESSES_METHOD_NAME, PROCESS_URL_TEMPLATE, requestBody);
    }

    @Override
    public void deleteProcesses(String userId, List<String> qualifiedNames, List<String> guids) throws InvalidParameterException,
                                                                                                       PropertyServerException {
        invalidParameterHandler.validateUserId(userId, PROCESSES_DELETE_METHOD_NAME);

        ProcessesDeleteRequestBody requestBody = new ProcessesDeleteRequestBody();
        requestBody.setQualifiedNames(qualifiedNames);
        requestBody.setGuids(guids);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setDeleteSemantic(deleteSemantic);

        callVoidDeleteRESTCall(userId, PROCESSES_METHOD_NAME, PROCESS_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createExternalDataEngine(String userId, SoftwareServerCapability softwareServerCapability) throws
                                                                                                             InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException {

        invalidParameterHandler.validateUserId(userId, EXTERNAL_DATA_ENGINE_METHOD_NAME);

        DataEngineRegistrationRequestBody requestBody = new DataEngineRegistrationRequestBody();
        requestBody.setSoftwareServerCapability(softwareServerCapability);

        setExternalSourceName(softwareServerCapability.getQualifiedName());

        return callGUIDPostRESTCall(userId, EXTERNAL_DATA_ENGINE_METHOD_NAME, DATA_ENGINE_REGISTRATION_URL_TEMPLATE, requestBody);
    }

    @Override
    public void deleteExternalDataEngine(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                                  PropertyServerException {
        invalidParameterHandler.validateUserId(userId, EXTERNAL_DATA_ENGINE_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, EXTERNAL_DATA_ENGINE_DELETE_METHOD_NAME, DATA_ENGINE_REGISTRATION_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrUpdateSchemaType(String userId, SchemaType schemaType) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException {

        invalidParameterHandler.validateUserId(userId, SCHEMA_TYPE_METHOD_NAME);

        SchemaTypeRequestBody requestBody = new SchemaTypeRequestBody();
        requestBody.setSchemaType(schemaType);
        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, SCHEMA_TYPE_METHOD_NAME, SCHEMA_TYPE_URL_TEMPLATE, requestBody);
    }

    @Override
    public void deleteSchemaType(String userId, String qualifiedName, String guid) throws InvalidParameterException, PropertyServerException {
        invalidParameterHandler.validateUserId(userId, SCHEMA_TYPE_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, SCHEMA_TYPE_DELETE_METHOD_NAME, SCHEMA_TYPE_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrUpdatePortImplementation(String userId, PortImplementation portImplementation, String processQualifiedName) throws
                                                                                                                                      InvalidParameterException,
                                                                                                                                      UserNotAuthorizedException,
                                                                                                                                      PropertyServerException {
        invalidParameterHandler.validateUserId(userId, PORT_IMPLEMENTATION_METHOD_NAME);

        PortImplementationRequestBody requestBody = new PortImplementationRequestBody();
        requestBody.setPortImplementation(portImplementation);
        requestBody.setProcessQualifiedName(processQualifiedName);
        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, PORT_IMPLEMENTATION_METHOD_NAME, PORT_IMPLEMENTATION_URL_TEMPLATE, requestBody);
    }

    @Override
    public void deletePortImplementation(String userId, String qualifiedName, String guid) throws InvalidParameterException, PropertyServerException {
        invalidParameterHandler.validateUserId(userId, PORT_IMPLEMENTATION_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody  = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, PORT_IMPLEMENTATION_DELETE_METHOD_NAME, PORT_IMPLEMENTATION_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrUpdatePortAlias(String userId, PortAlias portAlias, String processQualifiedName) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException {
        final String methodName = PORT_ALIAS_METHOD_NAME;

        invalidParameterHandler.validateUserId(userId, methodName);

        PortAliasRequestBody requestBody = new PortAliasRequestBody();
        requestBody.setPortAlias(portAlias);
        requestBody.setProcessQualifiedName(processQualifiedName);
        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, methodName, PORT_ALIAS_URL_TEMPLATE, requestBody);
    }

    @Override
    public void deletePortAlias(String userId, String qualifiedName, String guid) throws InvalidParameterException, PropertyServerException{
        invalidParameterHandler.validateUserId(userId, PORT_ALIAS_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody  = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, PORT_ALIAS_DELETE_METHOD_NAME, PORT_ALIAS_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String addProcessHierarchy(String userId, ProcessHierarchy processHierarchy) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException {
        final String methodName = PROCESS_HIERARCHY_METHOD_NAME;

        invalidParameterHandler.validateUserId(userId, methodName);

        ProcessHierarchyRequestBody requestBody = new ProcessHierarchyRequestBody();
        requestBody.setProcessHierarchy(processHierarchy);
        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, methodName, PROCESS_HIERARCHY_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLineageMappings(String userId, List<LineageMapping> lineageMappings) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException {
        final String methodName = LINEAGE_MAPPINGS_METHOD_NAME;

        invalidParameterHandler.validateUserId(userId, methodName);

        LineageMappingsRequestBody requestBody = new LineageMappingsRequestBody();
        requestBody.setLineageMappings(lineageMappings);
        requestBody.setExternalSourceName(externalSourceName);

        callVoidPostRESTCall(userId, methodName, LINEAGE_MAPPINGS_URL_TEMPLATE, requestBody);
    }

    private void callVoidPostRESTCall(String userId, String methodName, String urlTemplate, DataEngineOMASAPIRequestBody requestBody,
                                      Object... params) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        super.callVoidPostRESTCall(methodName, serverPlatformRootURL + urlTemplate, requestBody, serverName, userId, params);
    }

    private String callGUIDPostRESTCall(String userId, String methodName, String urlTemplate, DataEngineOMASAPIRequestBody requestBody,
                                        Object... params) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        GUIDResponse restResult = super.callGUIDPostRESTCall(methodName, serverPlatformRootURL + urlTemplate, requestBody, serverName,
                userId, params);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);

        return restResult.getGUID();
    }

    private List<String> callProcessListPostRESTCall(String userId, String methodName, String urlTemplate, ProcessesRequestBody requestBody,
                                                     Object... params) throws PropertyServerException {
        ProcessListResponse restResult = super.callPostRESTCall(methodName, ProcessListResponse.class, serverPlatformRootURL + urlTemplate,
                requestBody, serverName, userId, params);

        return restResult.getGUIDs();
    }

    private void callVoidDeleteRESTCall(String userId, String methodName, String urlTemplate, DataEngineOMASAPIRequestBody requestBody,
                                        Object... params) throws PropertyServerException {
        super.callDeleteRESTCall(methodName, VoidResponse.class, serverPlatformRootURL + urlTemplate, requestBody, serverName, userId, params);
    }

    private DeleteRequestBody getDeleteRequestBody(String qualifiedName, String guid) {
        DeleteRequestBody requestBody = new DeleteRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setGuid(guid);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setDeleteSemantic(deleteSemantic);

        return requestBody;
    }
}

