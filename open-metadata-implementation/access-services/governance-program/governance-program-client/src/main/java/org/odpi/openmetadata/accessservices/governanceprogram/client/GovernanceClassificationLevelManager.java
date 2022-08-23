/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.GovernanceClassificationLevelInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceLevelIdentifierElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceLevelIdentifierSetElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceLevelIdentifierProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceLevelIdentifierSetProperties;
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
 * GovernanceClassificationLevelManager is the Java client used to manage the definitions of the level values used in governance classifications.
 */
public class GovernanceClassificationLevelManager implements GovernanceClassificationLevelInterface
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
    public GovernanceClassificationLevelManager(String serverName,
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
    public GovernanceClassificationLevelManager(String serverName,
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
    public GovernanceClassificationLevelManager(String   serverName,
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
    public GovernanceClassificationLevelManager(String   serverName,
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
    public GovernanceClassificationLevelManager(String                      serverName,
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


    /* =====================================================================================================================
     * Egeria defines a default set of classification levels for each governance classification.  The method below sets them up.
     */

    /**
     * Create a governance level identifier set for a specific governance classification:
     *
     * <ul>
     *     <li>Impact - classification used to document the impact of an issue or situation.</li>
     *     <li>Criticality - classification used to document how critical an asset or activity is.</li>
     *     <li>Retention - classification used to identify the basis that an asset should be retained.</li>
     *     <li>Confidence - classification use to document an assessment of the quality of an asset or element with an asset.</li>
     *     <li>Confidentiality - classification use to define how much access to an asset should be restricted.</li>
     * </ul>
     *
     * @param userId calling user
     * @param classificationName name of the classification level to set up
     * @return unique identifier of the governance level identifier set
     */
    @Override
    public String createStandardGovernanceClassificationLevels(String userId,
                                                               String classificationName) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "createStandardGovernanceClassificationLevels";

        final String classificationNameParameter = "classificationName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/classification-level-sets/standard-set/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(classificationName, classificationNameParameter, methodName);

        GUIDResponse response = restClient.callGUIDPostRESTCall(methodName,
                                                                serverPlatformURLRoot + urlTemplate,
                                                                nullRequestBody,
                                                                serverName,
                                                                userId,
                                                                classificationName);

        return response.getGUID();
    }


    /* =====================================================================================================================
     * The GovernanceLevelIdentifierSet entity is the top level element in a collection of related governance domains.
     */


    /**
     * Create a new metadata element to represent the root of a Governance Level Classification Identifier Set.
     *
     * @param userId calling user
     * @param properties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createGovernanceLevelIdentifierSet(String                                 userId,
                                                     GovernanceLevelIdentifierSetProperties properties) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        final String methodName = "createGovernanceLevelIdentifierSet";

        final String classificationNameParameter = "classificationName";
        final String identifierPropertyNameParameter = "identifierPropertyName";
        final String propertiesParameter = "properties";
        final String qualifiedNameParameter = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/classification-level-sets";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameter, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameter, methodName);
        invalidParameterHandler.validateName(properties.getClassificationName(), classificationNameParameter, methodName);
        invalidParameterHandler.validateName(properties.getIdentifierPropertyName(), identifierPropertyNameParameter, methodName);

        GUIDResponse response = restClient.callGUIDPostRESTCall(methodName,
                                                                serverPlatformURLRoot + urlTemplate,
                                                                properties,
                                                                serverName,
                                                                userId);

        return response.getGUID();
    }


    /**
     * Update the metadata element representing a Governance Classification Level Identifier Set.
     *
     * @param userId calling user
     * @param setGUID unique identifier of the metadata element to remove
     * @param properties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateGovernanceLevelIdentifierSet(String                                 userId,
                                                   String                                 setGUID,
                                                   GovernanceLevelIdentifierSetProperties properties) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String methodName = "updateGovernanceLevelIdentifierSet";

        final String classificationNameParameter = "classificationName";
        final String identifierPropertyNameParameter = "identifierPropertyName";
        final String guidParameter = "setGUID";
        final String propertiesParameter = "properties";
        final String qualifiedNameParameter = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/classification-level-sets/{2}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(setGUID, guidParameter, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameter, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameter, methodName);
        invalidParameterHandler.validateName(properties.getClassificationName(), classificationNameParameter, methodName);
        invalidParameterHandler.validateName(properties.getIdentifierPropertyName(), identifierPropertyNameParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        properties,
                                        serverName,
                                        userId,
                                        setGUID);
    }


    /**
     * Remove the metadata element representing a Governance Classification Level Identifier Set and all linked level identifiers.
     *
     * @param userId calling user
     * @param setGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeGovernanceLevelIdentifierSet(String userId,
                                                   String setGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "removeGovernanceLevelIdentifierSet";

        final String guidParameter = "setGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/classification-level-sets/{2}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(setGUID, guidParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        setGUID);
    }


    /**
     * Retrieve the list of defined Governance Classification Level Identifier Sets.
     *
     * @param userId calling user
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GovernanceLevelIdentifierSetElement> getGovernanceLevelIdentifierSets(String userId) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String methodName = "getGovernanceLevelIdentifierSets";

        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/classification-level-sets";

        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceLevelIdentifierSetListResponse restResult = restClient.callLevelIdentifierSetListGetRESTCall(methodName,
                                                                                                               serverPlatformURLRoot + urlTemplate,
                                                                                                               serverName,
                                                                                                               userId);
        return restResult.getElements();
    }


    /**
     * Retrieve the Governance Level Identifier Set for a requested governance classification.
     *
     * @param userId calling user
     * @param classificationName classificationName to search for
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceLevelIdentifierSetElement getGovernanceLevelIdentifierSet(String userId,
                                                                               String classificationName) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {
        final String methodName = "getGovernanceLevelIdentifierSet";

        final String classificationNameParameter = "classificationName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/classification-level-sets/classification-name/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(classificationName, classificationNameParameter, methodName);

        GovernanceLevelIdentifierSetResponse restResult = restClient.callLevelIdentifierSetGetRESTCall(methodName,
                                                                                                       serverPlatformURLRoot + urlTemplate,
                                                                                                       serverName,
                                                                                                       userId,
                                                                                                       classificationName);
        return restResult.getElement();
    }




    /**
     * Retrieve the Governance Level Identifier Set metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param setGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceLevelIdentifierSetElement getGovernanceLevelIdentifierSetByGUID(String userId,
                                                                                     String setGUID) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String methodName = "getGovernanceLevelIdentifierSet";

        final String guidParameter = "setGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/classification-level-sets/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(setGUID, guidParameter, methodName);

        GovernanceLevelIdentifierSetResponse restResult = restClient.callLevelIdentifierSetGetRESTCall(methodName,
                                                                                                       serverPlatformURLRoot + urlTemplate,
                                                                                                       serverName,
                                                                                                       userId,
                                                                                                       setGUID);
        return restResult.getElement();
    }


    /* =====================================================================================================================
     * A GovernanceLevelIdentifier describes an area of focus in the governance program.
     */

    /**
     * Create a new metadata element to represent a governance classification level identifier.
     *
     * @param userId calling user
     * @param setGUID unique identifier of the set that this identifier belongs
     * @param properties properties about the GovernanceClassificationLevelIdentifier to store
     *
     * @return unique identifier of the new GovernanceClassifierLevelIdentifier
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createGovernanceLevelIdentifier(String                              userId,
                                                  String                              setGUID,
                                                  GovernanceLevelIdentifierProperties properties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "createGovernanceLevelIdentifier";

        final String guidParameter = "setGUID";
        final String qualifiedNameParameter = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/classification-level-sets/{2}/identifier";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(setGUID, guidParameter, methodName);
        invalidParameterHandler.validateObject(properties, qualifiedNameParameter, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameter, methodName);

        GUIDResponse response = restClient.callGUIDPostRESTCall(methodName,
                                                                serverPlatformURLRoot + urlTemplate,
                                                                properties,
                                                                serverName,
                                                                userId,
                                                                setGUID);

        return response.getGUID();
    }


    /**
     * Update the metadata element representing a governance classification level identifier.
     *
     * @param userId calling user
     * @param identifierGUID unique identifier of the metadata element to update
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateGovernanceLevelIdentifier(String                              userId,
                                                String                              identifierGUID,
                                                GovernanceLevelIdentifierProperties properties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "updateGovernanceLevelIdentifier";

        final String guidParameter = "identifierGUID";
        final String qualifiedNameParameter = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/classification-level-sets/identifier/{2}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(identifierGUID, guidParameter, methodName);
        invalidParameterHandler.validateObject(properties, qualifiedNameParameter, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), qualifiedNameParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        properties,
                                        serverName,
                                        userId,
                                        identifierGUID);
    }


    /**
     * Remove the metadata element representing a governance classification level identifier.
     *
     * @param userId calling user
     * @param identifierGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeGovernanceLevelIdentifier(String userId,
                                                String identifierGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "removeGovernanceLevelIdentifier";

        final String guidParameter = "identifierGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/classification-level-sets/identifier/{2}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(identifierGUID, guidParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        identifierGUID);
    }


    /**
     * Retrieve the governance classification level identifier metadata element for the requested level within a specific governance classification.
     *
     * @param userId calling user
     * @param classificationName string to find in the properties
     * @param levelIdentifier level value to retrieve
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceLevelIdentifierElement getGovernanceLevelIdentifier(String userId,
                                                                         String classificationName,
                                                                         int    levelIdentifier) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName = "getGovernanceLevelIdentifier";

        final String classificationNameParameter = "classificationName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/classification-level-sets/classification-name/{2}/identifier/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(classificationName, classificationNameParameter, methodName);

        GovernanceLevelIdentifierResponse restResult = restClient.callLevelIdentifierGetRESTCall(methodName,
                                                                                                 serverPlatformURLRoot + urlTemplate,
                                                                                                 serverName,
                                                                                                 userId,
                                                                                                 classificationName,
                                                                                                 levelIdentifier);
        return restResult.getElement();
    }
}
