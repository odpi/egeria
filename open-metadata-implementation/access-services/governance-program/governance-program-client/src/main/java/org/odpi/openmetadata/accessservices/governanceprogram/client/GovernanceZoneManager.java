/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.GovernanceZonesInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceZoneDefinition;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceZoneElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceZoneProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceZoneDefinitionResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceZoneListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceZoneResponse;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * GovernanceZoneManager is the Java client used to manage governance zones.
 */
public class GovernanceZoneManager implements GovernanceZonesInterface
{
    private String                      serverName;               /* Initialized in constructor */
    private String                      serverPlatformURLRoot;    /* Initialized in constructor */
    private GovernanceProgramRESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private NullRequestBody         nullRequestBody         = new NullRequestBody();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceZoneManager(String serverName,
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
    public GovernanceZoneManager(String serverName,
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
    public GovernanceZoneManager(String   serverName,
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
    public GovernanceZoneManager(String   serverName,
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
     * Create a new client that uses the supplied rest client.  This is typically used when called fro manother OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient internal client for rest calls
     * @param maxPageSize pre-initialized parameter limit
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceZoneManager(String                      serverName,
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
     * Create a definition of a governance zone.  The qualified name of these governance zones can be added
     * to the supportedZones, publishedZones and defaultZones properties of an OMAS to control which assets are processed
     * and how they are set up.  In addition the qualified names of zones can be added to Asset definitions
     * to indicate which zone(s) they belong to.
     *
     * @param userId calling user
     * @param properties  properties for a governance zone
     *
     * @return unique identifier of the governance zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String createGovernanceZone(String                   userId,
                                       GovernanceZoneProperties properties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "createGovernanceZone";

        final String propertiesParameter = "properties";
        final String qualifiedNameParameter = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-zones";

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
     * Update the definition of a zone.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier of zone
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void updateGovernanceZone(String                   userId,
                                     String                   zoneGUID,
                                     boolean                  isMergeUpdate,
                                     GovernanceZoneProperties properties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "updateGovernanceZone";

        final String guidParameter = "zoneGUID";
        final String qualifiedNameParameter = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-zones/{2}?isMergeUpdate={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(zoneGUID, guidParameter, methodName);
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
                                        zoneGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove the definition of a zone.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier of zone
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void deleteGovernanceZone(String userId,
                                     String zoneGUID) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName = "deleteGovernanceZone";

        final String guidParameter = "zoneGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-zones/{2}/delete}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(zoneGUID, guidParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        zoneGUID);
    }


    /**
     * Link two related governance zones together as part of a hierarchy.
     * A zone can only have one parent but many child zones.
     *
     * @param userId calling user
     * @param parentZoneGUID unique identifier of the parent zone
     * @param childZoneGUID unique identifier of the child zone
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void linkZonesInHierarchy(String userId,
                                     String parentZoneGUID,
                                     String childZoneGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "linkZonesInHierarchy";

        final String parentZoneGUIDParameterName = "parentZoneGUID";
        final String childZoneGUIDParameterName = "childZoneGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-zones/{2}/nested-zone/{3}/link";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentZoneGUID, parentZoneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(childZoneGUID, childZoneGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        parentZoneGUID,
                                        childZoneGUID);
    }


    /**
     * Remove the link between two zones in the zone hierarchy.
     *
     * @param userId calling user
     * @param parentZoneGUID unique identifier of the parent zone
     * @param childZoneGUID unique identifier of the child zone
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void unlinkZonesInHierarchy(String userId,
                                       String parentZoneGUID,
                                       String childZoneGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "unlinkZonesInHierarchy";

        final String parentZoneGUIDParameterName = "parentZoneGUID";
        final String childZoneGUIDParameterName = "childZoneGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-zones/{2}/nested-zone/{3}/unlink";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentZoneGUID, parentZoneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(childZoneGUID, childZoneGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        parentZoneGUID,
                                        childZoneGUID);
    }


    /**
     * Link a governance zone to a governance definition that controls how the assets in the zone should be governed.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier of the zone
     * @param definitionGUID unique identifier of the governance definition
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void linkZoneToGovernanceDefinition(String userId,
                                               String zoneGUID,
                                               String definitionGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "linkZoneToGovernanceDefinition";

        final String zoneGUIDParameterName = "zoneGUID";
        final String definitionGUIDParameterName = "definitionGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-zones/{2}/governed-by/{3}/link";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(zoneGUID, zoneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(definitionGUID, definitionGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        zoneGUID,
                                        definitionGUID);
    }


    /**
     * Remove the link between a zone and a governance definition.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier of the zone
     * @param definitionGUID unique identifier of the governance definition
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void unlinkZoneFromGovernanceDefinition(String userId,
                                                   String zoneGUID,
                                                   String definitionGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "unlinkZoneToGovernanceDefinition";

        final String zoneGUIDParameterName = "zoneGUID";
        final String definitionGUIDParameterName = "definitionGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-zones/{2}/governed-by/{3}/unlink";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(zoneGUID, zoneGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(definitionGUID, definitionGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        zoneGUID,
                                        definitionGUID);
    }


    /**
     * Return information about a specific governance zone.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier for the zone
     *
     * @return properties of the governance zone
     *
     * @throws InvalidParameterException zoneGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public GovernanceZoneElement getGovernanceZoneByGUID(String userId,
                                                         String zoneGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "getGovernanceZoneByGUID";

        final String guidParameter = "zoneGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-zones/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(zoneGUID, guidParameter, methodName);

        GovernanceZoneResponse restResult = restClient.callGovernanceZoneGetRESTCall(methodName,
                                                                                     serverPlatformURLRoot + urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     zoneGUID);
        return restResult.getElement();
    }


    /**
     * Return information about a specific governance zone.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the zone
     *
     * @return properties of the governance zone
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public GovernanceZoneElement getGovernanceZoneByName(String userId,
                                                         String qualifiedName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "getGovernanceZoneByName";

        final String qualifiedNameParameter = "qualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-zones/name/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        GovernanceZoneResponse restResult = restClient.callGovernanceZoneGetRESTCall(methodName,
                                                                                     serverPlatformURLRoot + urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     qualifiedName);

        return restResult.getElement();
    }


    /**
     * Return information about the defined governance zones.
     *
     * @param userId calling user
     * @param domainIdentifier identifier for the desired governance domain - 0 for all
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the governance zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<GovernanceZoneElement> getGovernanceZonesForDomain(String userId,
                                                                   int    domainIdentifier,
                                                                   int    startFrom,
                                                                   int    pageSize) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "getGovernanceZonesForDomain";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-zones/for-domain?domainIdentifier={2}&startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        GovernanceZoneListResponse restResult = restClient.callGovernanceZoneListGetRESTCall(methodName,
                                                                                             serverPlatformURLRoot + urlTemplate,
                                                                                             serverName,
                                                                                             userId,
                                                                                             domainIdentifier,
                                                                                             startFrom,
                                                                                             queryPageSize);

        return restResult.getElementList();
    }


    /**
     * Return information about a specific governance zone and its linked governance definitions.
     *
     * @param userId calling user
     * @param zoneGUID unique identifier for the zone
     *
     * @return properties of the governance zone linked to the associated governance definitions
     *
     * @throws InvalidParameterException zoneGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public GovernanceZoneDefinition getGovernanceZoneDefinitionByGUID(String userId,
                                                                      String zoneGUID) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "getGovernanceZoneDefinitionByGUID";

        final String guidParameter = "zoneGUID";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-zones/{2}/with-definitions";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(zoneGUID, guidParameter, methodName);

        GovernanceZoneDefinitionResponse restResult = restClient.callGovernanceZoneDefinitionGetRESTCall(methodName,
                                                                                                         serverPlatformURLRoot + urlTemplate,
                                                                                                         serverName,
                                                                                                         userId,
                                                                                                         zoneGUID);
        return restResult.getProperties();
    }
}
