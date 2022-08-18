/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.SubjectAreasInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.SubjectAreaElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.SubjectAreaProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;


/**
 * The SubjectAreasInterface is used by the governance team to define the subject area for topic related governance definitions.
 */
public class SubjectAreaManager implements SubjectAreasInterface
{
    private final String                      serverName;               /* Initialized in constructor */
    private final String                      serverPlatformURLRoot;    /* Initialized in constructor */
    private final GovernanceProgramRESTClient restClient;               /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private final NullRequestBody         nullRequestBody         = new NullRequestBody();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public SubjectAreaManager(String serverName,
                              String serverPlatformURLRoot) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot);
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
    public SubjectAreaManager(String serverName,
                              String serverPlatformURLRoot,
                              String userId,
                              String password) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot, userId, password);
    }



    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public SubjectAreaManager(String   serverName,
                              String   serverPlatformURLRoot,
                              int      maxPageSize,
                              AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public SubjectAreaManager(String   serverName,
                              String   serverPlatformURLRoot,
                              String   userId,
                              String   password,
                              int      maxPageSize,
                              AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that uses the supplied rest client.  This is typically used when called from another OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient internal client for rest calls
     * @param maxPageSize pre-initialized parameter limit
     *
     * @throws InvalidParameterException bad input parameters
     */
    public SubjectAreaManager(String                      serverName,
                              String                      serverPlatformURLRoot,
                              GovernanceProgramRESTClient restClient,
                              int                         maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.restClient            = restClient;
    }


    /**
     * Create a definition of a subject area.
     *
     * @param userId calling user
     * @param properties properties for a subject area
     *
     * @return unique identifier of subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String createSubjectArea(String                userId,
                                    SubjectAreaProperties properties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "createSubjectArea";

        final String propertiesParameter = "properties";
        final String qualifiedNameParameter = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameter, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameter, methodName);

        GUIDResponse response = restClient.callGUIDPostRESTCall(methodName,
                                                                serverPlatformURLRoot + urlTemplate,
                                                                properties,
                                                                serverName,
                                                                userId);

        return response.getGUID();
    }


    /**
     * Update the definition of a subject area.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of subject area
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateSubjectArea(String                userId,
                                  String                subjectAreaGUID,
                                  boolean               isMergeUpdate,
                                  SubjectAreaProperties properties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "updateSubjectArea";

        final String guidParameter = "subjectAreaGUID";
        final String qualifiedNameParameter = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/{2}?isMergeUpdate={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(subjectAreaGUID, guidParameter, methodName);
        invalidParameterHandler.validateObject(properties, qualifiedNameParameter, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameter, methodName);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        properties,
                                        serverName,
                                        userId,
                                        subjectAreaGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove the definition of a subject area.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of subject area
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void deleteSubjectArea(String userId,
                                  String subjectAreaGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "deleteSubjectArea";

        final String guidParameter = "subjectAreaGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/{2}/delete}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(subjectAreaGUID, guidParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        subjectAreaGUID);
    }


    /**
     * Link two related subject areas together as part of a hierarchy.
     * A subject area can only have one parent but many child subject areas.
     *
     * @param userId calling user
     * @param parentSubjectAreaGUID unique identifier of the parent subject area
     * @param childSubjectAreaGUID unique identifier of the child subject area
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void linkSubjectAreasInHierarchy(String userId,
                                            String parentSubjectAreaGUID,
                                            String childSubjectAreaGUID) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "linkSubjectAreasInHierarchy";

        final String parentSubjectAreaGUIDParameterName = "parentSubjectAreaGUID";
        final String childSubjectAreaGUIDParameterName = "childSubjectAreaGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/{2}/nested-subject-area/{3}/link";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentSubjectAreaGUID, parentSubjectAreaGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(childSubjectAreaGUID, childSubjectAreaGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        parentSubjectAreaGUID,
                                        childSubjectAreaGUID);
    }


    /**
     * Remove the link between two subject areas in the subject area hierarchy.
     *
     * @param userId calling user
     * @param parentSubjectAreaGUID unique identifier of the parent subject area
     * @param childSubjectAreaGUID unique identifier of the child subject area
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void unlinkSubjectAreasInHierarchy(String userId,
                                              String parentSubjectAreaGUID,
                                              String childSubjectAreaGUID) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "unlinkSubjectAreasInHierarchy";

        final String parentSubjectAreaGUIDParameterName = "parentSubjectAreaGUID";
        final String childSubjectAreaGUIDParameterName = "childSubjectAreaGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/{2}/nested-subject-area/{3}/unlink";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentSubjectAreaGUID, parentSubjectAreaGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(childSubjectAreaGUID, childSubjectAreaGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        parentSubjectAreaGUID,
                                        childSubjectAreaGUID);
    }


    /**
     * Link a subject area to a governance definition that controls how the definitions in the subject area should be governed.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of the subject area
     * @param definitionGUID unique identifier of the governance definition
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void linkSubjectAreaToGovernanceDefinition(String userId,
                                                      String subjectAreaGUID,
                                                      String definitionGUID) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "linkSubjectAreaToGovernanceDefinition";

        final String subjectAreaGUIDParameterName = "subjectAreaGUID";
        final String definitionGUIDParameterName = "definitionGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/{2}/governed-by/{3}/link";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(subjectAreaGUID, subjectAreaGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(definitionGUID, definitionGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        subjectAreaGUID,
                                        definitionGUID);
    }


    /**
     * Remove the link between a subject area and a governance definition.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of the subject area
     * @param definitionGUID unique identifier of the governance definition
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void unlinkSubjectAreaFromGovernanceDefinition(String userId,
                                                          String subjectAreaGUID,
                                                          String definitionGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "unlinkSubjectAreaFromGovernanceDefinition";

        final String subjectAreaGUIDParameterName = "subjectAreaGUID";
        final String definitionGUIDParameterName = "definitionGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/{2}/governed-by/{3}/unlink";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(subjectAreaGUID, subjectAreaGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(definitionGUID, definitionGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        subjectAreaGUID,
                                        definitionGUID);
    }


    /**
     * Return information about a specific subject area.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier for the subject area
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException subjectAreaGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public SubjectAreaElement getSubjectAreaByGUID(String userId,
                                                   String subjectAreaGUID) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getSubjectAreaByGUID";

        final String guidParameter = "subjectAreaGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(subjectAreaGUID, guidParameter, methodName);

        SubjectAreaResponse restResult = restClient.callSubjectAreaGetRESTCall(methodName,
                                                                               serverPlatformURLRoot + urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               subjectAreaGUID);
        return restResult.getElement();
    }


    /**
     * Return information about a specific subject area.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the subject area
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public SubjectAreaElement getSubjectAreaByName(String userId,
                                                   String qualifiedName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "getSubjectAreaByName";

        final String qualifiedNameParameter = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/name/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        SubjectAreaResponse restResult = restClient.callSubjectAreaGetRESTCall(methodName,
                                                                               serverPlatformURLRoot + urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               qualifiedName);

        return restResult.getElement();
    }


    /**
     * Return information about the defined subject areas.
     *
     * @param userId calling user
     * @param domainIdentifier identifier for the desired governance domain - zero for all
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<SubjectAreaElement> getSubjectAreasForDomain(String userId,
                                                             int    domainIdentifier,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getSubjectAreasForDomain";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/for-domain?domainIdentifier={2}&startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SubjectAreaListResponse restResult = restClient.callSubjectAreaListGetRESTCall(methodName,
                                                                                       serverPlatformURLRoot + urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       domainIdentifier,
                                                                                       startFrom,
                                                                                       queryPageSize);

        return restResult.getElementList();
    }


    /**
     * Return information about a specific subject area and its linked governance definitions.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier for the subject area
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException subjectAreaGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public SubjectAreaDefinition getSubjectAreaDefinitionByGUID(String userId,
                                                                String subjectAreaGUID) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "getSubjectAreaDefinitionByGUID";

        final String guidParameter = "subjectAreaGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/subject-areas/{2}/with-definitions";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(subjectAreaGUID, guidParameter, methodName);

        SubjectAreaDefinitionResponse restResult = restClient.callSubjectAreaDefinitionGetRESTCall(methodName,
                                                                                                   serverPlatformURLRoot + urlTemplate,
                                                                                                   serverName,
                                                                                                   userId,
                                                                                                   subjectAreaGUID);
        return restResult.getProperties();
    }
}
