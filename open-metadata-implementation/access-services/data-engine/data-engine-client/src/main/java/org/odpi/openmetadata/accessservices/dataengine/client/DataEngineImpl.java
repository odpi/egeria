/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.client;

import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataengine.model.UpdateSemantic;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineOMASAPIRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.LineageMappingsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortAliasRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortListRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessListResponse;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessesRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * The Data Engine Open Metadata Access Service (OMAS) provides an interface for data engine tools to create
 * processes with ports, schemas and relationships. See interface definition for more explanation.
 */
public class DataEngineImpl extends OCFRESTClient implements DataEngineClient {
    private static final String QUALIFIED_NAME_PARAMETER = "qualifiedName";
    private static final String PROCESS_URL_TEMPLATE = "/servers/{0}/open-metadata/access-services" +
            "/data-engine/users/{1}/processes";
    private static final String DATA_ENGINE_REGISTRATION_URL_TEMPLATE = "/servers/{0}/open-metadata/access-services" +
            "/data-engine/users/{1}/registration";
    private static final String SCHEMA_TYPE_URL_TEMPLATE = "/servers/{0}/open-metadata/access-services" +
            "/data-engine/users/{1}/schema-types";
    private static final String PORT_IMPLEMENTATION_URL_TEMPLATE = "/servers/{0}/open-metadata/access-services" +
            "/data-engine/users/{1}/port-implementations";
    private static final String PORT_ALIAS_URL_TEMPLATE = "/servers/{0}/open-metadata/access-services" +
            "/data-engine/users/{1}/port-aliases";
    private static final String LINEAGE_MAPPINGS_URL_TEMPLATE = "/servers/{0}/open-metadata/access-services" +
            "/data-engine/users/{1}/lineage-mappings";
    private static final String PORTS_TO_PROCESS_URL_TEMPLATE = "/servers/{0}/open-metadata/access-services" +
            "/data-engine/users/{1}/processes/{2}/ports";

    private String serverName;
    private String serverPlatformRootURL;

    private String externalSourceName;

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler exceptionHandler = new RESTExceptionHandler();


    /**
     * Create a new client without authentication.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     *
     * @throws InvalidParameterException null URL or server name
     */
    public DataEngineImpl(String serverName, String serverPlatformRootURL) throws InvalidParameterException {
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
    public DataEngineImpl(String serverName, String serverPlatformRootURL, String userId, String password) throws
                                                                                                           InvalidParameterException {
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

    @Override
    public String createOrUpdateProcess(String userId, String qualifiedName, String processName, String description,
                                        String latestChange, List<String> zoneMembership, String displayName,
                                        String formula, String owner, OwnerType ownerType,
                                        List<PortImplementation> portImplementations, List<PortAlias> portAliases,
                                        List<LineageMapping> lineageMappings, UpdateSemantic updateSemantic) throws
                                                                                                             PropertyServerException,
                                                                                                             InvalidParameterException,
                                                                                                             UserNotAuthorizedException {
        final String methodName = "createOrUpdateProcesses";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PARAMETER, methodName);

        ProcessesRequestBody requestBody = new ProcessesRequestBody();
        requestBody.setProcesses(Collections.singletonList(new Process(qualifiedName, processName, description,
                latestChange, zoneMembership, displayName, formula, owner, ownerType, portImplementations,
                portAliases, lineageMappings, updateSemantic)));
        requestBody.setExternalSourceName(externalSourceName);

        return callProcessListPostRESTCall(userId, methodName, PROCESS_URL_TEMPLATE, requestBody).get(0);
    }

    @Override
    public String createOrUpdateProcess(String userId, String qualifiedName, String processName, String description,
                                        String latestChange, List<String> zoneMembership, String displayName,
                                        String formula, String owner, OwnerType ownerType,
                                        List<PortImplementation> portImplementations, List<PortAlias> portAliases,
                                        List<LineageMapping> lineageMappings) throws PropertyServerException,
                                                                                     InvalidParameterException,
                                                                                     UserNotAuthorizedException {
        final String methodName = "createOrUpdateProcesses";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PARAMETER, methodName);

        ProcessesRequestBody requestBody = new ProcessesRequestBody();
        requestBody.setProcesses(Collections.singletonList(new Process(qualifiedName, processName, description,
                latestChange, zoneMembership, displayName, formula, owner, ownerType, portImplementations,
                portAliases, lineageMappings, UpdateSemantic.REPLACE)));
        requestBody.setExternalSourceName(externalSourceName);

        return callProcessListPostRESTCall(userId, methodName, PROCESS_URL_TEMPLATE, requestBody).get(0);
    }

    @Override
    public String createOrUpdateProcess(String userId, Process process) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException {
        final String methodName = "createOrUpdateProcesses";

        invalidParameterHandler.validateUserId(userId, methodName);

        ProcessesRequestBody requestBody = new ProcessesRequestBody();
        requestBody.setProcesses(Collections.singletonList(process));
        requestBody.setExternalSourceName(externalSourceName);

        List<String> result = callProcessListPostRESTCall(userId, methodName, PROCESS_URL_TEMPLATE, requestBody);

        if (CollectionUtils.isEmpty(result)) {
            return null;
        }

        return result.get(0);
    }

    @Override
    public List<String> createOrUpdateProcesses(String userId, List<Process> processes) throws
                                                                                        InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException {
        final String methodName = "createOrUpdateProcesses";

        invalidParameterHandler.validateUserId(userId, methodName);

        ProcessesRequestBody requestBody = new ProcessesRequestBody();
        requestBody.setProcesses(processes);
        requestBody.setExternalSourceName(externalSourceName);

        return callProcessListPostRESTCall(userId, methodName, PROCESS_URL_TEMPLATE, requestBody);
    }

    @Override
    public String createExternalDataEngine(String userId, String qualifiedName, String name,
                                           String description, String type, String version,
                                           String patchLevel, String source) throws
                                                                             InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException {

        final String methodName = "createExternalDataEngine";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PARAMETER, methodName);

        DataEngineRegistrationRequestBody requestBody = new DataEngineRegistrationRequestBody();
        requestBody.setSoftwareServerCapability(new SoftwareServerCapability(qualifiedName, name, description, type,
                version, patchLevel, source));

        setExternalSourceName(qualifiedName);

        return callGUIDPostRESTCall(userId, methodName, DATA_ENGINE_REGISTRATION_URL_TEMPLATE, requestBody);
    }

    @Override
    public String createExternalDataEngine(String userId,
                                           SoftwareServerCapability softwareServerCapability) throws
                                                                                              InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException {
        final String methodName = "createExternalDataEngine";

        invalidParameterHandler.validateUserId(userId, methodName);

        DataEngineRegistrationRequestBody requestBody = new DataEngineRegistrationRequestBody();
        requestBody.setSoftwareServerCapability(softwareServerCapability);

        setExternalSourceName(softwareServerCapability.getQualifiedName());

        return callGUIDPostRESTCall(userId, methodName, DATA_ENGINE_REGISTRATION_URL_TEMPLATE, requestBody);
    }

    @Override
    public String createOrUpdateSchemaType(String userId, String qualifiedName, String displayName, String author,
                                           String encodingStandard, String usage, String versionNumber,
                                           List<Attribute> attributeList) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException {
        final String methodName = "createOrUpdateSchemaType";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PARAMETER, methodName);

        SchemaTypeRequestBody requestBody = new SchemaTypeRequestBody();
        requestBody.setSchemaType(new SchemaType(qualifiedName, displayName, author, usage, encodingStandard,
                versionNumber, attributeList));

        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, methodName, SCHEMA_TYPE_URL_TEMPLATE, requestBody);
    }

    @Override
    public String createOrUpdateSchemaType(String userId, SchemaType schemaType) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException {
        final String methodName = "createOrUpdateSchemaType";

        invalidParameterHandler.validateUserId(userId, methodName);

        SchemaTypeRequestBody requestBody = new SchemaTypeRequestBody();
        requestBody.setSchemaType(schemaType);

        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, methodName, SCHEMA_TYPE_URL_TEMPLATE, requestBody);
    }

    @Override
    public String createOrUpdatePortImplementation(String userId, String qualifiedName, String displayName,
                                                   PortType portType, SchemaType schemaType) throws
                                                                                             InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException {
        final String methodName = "createOrUpdatePortImplementation";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PARAMETER, methodName);

        PortImplementationRequestBody requestBody = new PortImplementationRequestBody();
        requestBody.setPortImplementation(new PortImplementation(qualifiedName, displayName, portType, schemaType,
                UpdateSemantic.REPLACE));

        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, methodName, PORT_IMPLEMENTATION_URL_TEMPLATE, requestBody);
    }

    @Override
    public String createOrUpdatePortImplementation(String userId, String qualifiedName, String displayName,
                                                   PortType portType, SchemaType schemaType,
                                                   UpdateSemantic updateSemantic) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException {
        final String methodName = "createOrUpdatePortImplementation";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PARAMETER, methodName);

        PortImplementationRequestBody requestBody = new PortImplementationRequestBody();
        requestBody.setPortImplementation(new PortImplementation(qualifiedName, displayName, portType, schemaType,
                updateSemantic));

        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, methodName, PORT_IMPLEMENTATION_URL_TEMPLATE, requestBody);
    }

    @Override
    public String createOrUpdatePortImplementation(String userId, PortImplementation portImplementation) throws
                                                                                                         InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException {
        final String methodName = "createOrUpdatePortImplementation";

        invalidParameterHandler.validateUserId(userId, methodName);

        PortImplementationRequestBody requestBody = new PortImplementationRequestBody();
        requestBody.setPortImplementation(portImplementation);

        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, methodName, PORT_IMPLEMENTATION_URL_TEMPLATE, requestBody);
    }

    @Override
    public String createOrUpdatePortAlias(String userId, String qualifiedName, String displayName, PortType portType,
                                          String delegatesTo) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException {
        final String methodName = "createOrUpdatePortAlias";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PARAMETER, methodName);

        PortAliasRequestBody requestBody = new PortAliasRequestBody();
        requestBody.setPortAlias(new PortAlias(qualifiedName, displayName, portType, delegatesTo));

        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, methodName, PORT_ALIAS_URL_TEMPLATE, requestBody);
    }

    @Override
    public String createOrUpdatePortAlias(String userId, PortAlias portAlias) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException {
        final String methodName = "createOrUpdatePortAlias";

        invalidParameterHandler.validateUserId(userId, methodName);

        PortAliasRequestBody requestBody = new PortAliasRequestBody();
        requestBody.setPortAlias(portAlias);

        requestBody.setExternalSourceName(externalSourceName);


        return callGUIDPostRESTCall(userId, methodName, PORT_ALIAS_URL_TEMPLATE, requestBody);
    }

    @Override
    public void addLineageMappings(String userId, List<LineageMapping> lineageMappings) throws
                                                                                        InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException {
        final String methodName = "addLineageMappings";

        invalidParameterHandler.validateUserId(userId, methodName);

        LineageMappingsRequestBody requestBody = new LineageMappingsRequestBody();
        requestBody.setLineageMappings(lineageMappings);

        requestBody.setExternalSourceName(externalSourceName);

        callVoidPostRESTCall(userId, methodName, LINEAGE_MAPPINGS_URL_TEMPLATE, requestBody);
    }

    @Override
    public void addPortsToProcess(String userId, List<String> portGUIDs, String processGUID) throws
                                                                                             InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException {
        final String methodName = "addPortsToProcess";

        invalidParameterHandler.validateUserId(userId, methodName);

        PortListRequestBody requestBody = new PortListRequestBody();
        requestBody.setPorts(portGUIDs);

        requestBody.setExternalSourceName(externalSourceName);

        callVoidPostRESTCall(userId, methodName, PORTS_TO_PROCESS_URL_TEMPLATE, requestBody, processGUID);
    }

    private void callVoidPostRESTCall(String userId, String methodName, String urlTemplate,
                                      DataEngineOMASAPIRequestBody requestBody, Object... params) throws
                                                                                                  PropertyServerException,
                                                                                                  InvalidParameterException,
                                                                                                  UserNotAuthorizedException {
        VoidResponse restResult = super.callVoidPostRESTCall(methodName, serverPlatformRootURL
                + urlTemplate, requestBody, serverName, userId, params);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);
    }

    private String callGUIDPostRESTCall(String userId, String methodName, String urlTemplate,
                                        DataEngineOMASAPIRequestBody requestBody, Object... params) throws
                                                                                                    PropertyServerException,
                                                                                                    InvalidParameterException,
                                                                                                    UserNotAuthorizedException {
        GUIDResponse restResult = super.callGUIDPostRESTCall(methodName,
                serverPlatformRootURL + urlTemplate, requestBody, serverName, userId, params);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUID();
    }

    private List<String> callProcessListPostRESTCall(String userId, String methodName, String urlTemplate,
                                                     ProcessesRequestBody requestBody, Object... params) throws
                                                                                                         PropertyServerException,
                                                                                                         InvalidParameterException,
                                                                                                         UserNotAuthorizedException {
        ProcessListResponse restResult = super.callPostRESTCall(methodName, ProcessListResponse.class,
                serverPlatformRootURL + urlTemplate, requestBody, serverName, userId, params);

        exceptionHandler.detectAndThrowInvalidParameterException(methodName, restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(methodName, restResult);
        exceptionHandler.detectAndThrowPropertyServerException(methodName, restResult);

        return restResult.getGUIDs();
    }
}
