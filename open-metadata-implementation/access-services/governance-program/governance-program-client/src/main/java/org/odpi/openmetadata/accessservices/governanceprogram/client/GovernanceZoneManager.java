/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.GovernanceZoneManagerInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceZoneElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceZoneProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ZoneListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ZoneResponse;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

/**
 * GovernanceZoneManager is the client used to manage governance zones.
 */
public class GovernanceZoneManager implements GovernanceZoneManagerInterface
{
    private String                      serverName;               /* Initialized in constructor */
    private String                      serverPlatformURLRoot;    /* Initialized in constructor */
    private GovernanceProgramRESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private AuditLog                auditLog                = null;


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
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot);
        this.auditLog              = auditLog;
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
        this.restClient            = new GovernanceProgramRESTClient(serverName, serverPlatformURLRoot, userId, password);
        this.auditLog              = auditLog;
    }


    /**
     * Create a definition of a governance zone.  The qualified name of these governance zones can be added
     * to the supportedZones and defaultZones properties of an OMAS to control which assets are processed
     * and how they are set up.  In addition the qualified names of zones can be added to Asset definitions
     * to indicate which zone(s) they belong to.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the zone - used in other configuration
     * @param displayName short display name for the zone
     * @param description description of the governance zone
     * @param criteria the criteria for inclusion in a governance zone
     * @param scope scope of the organization that this some applies to
     * @param domainIdentifier the identifier of the governance domain where the zone is managed - 0 means ALL
     * @param additionalProperties additional properties for a governance zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  createGovernanceZone(String               userId,
                                      String               qualifiedName,
                                      String               displayName,
                                      String               description,
                                      String               criteria,
                                      String               scope,
                                      int                  domainIdentifier,
                                      Map<String, String>  additionalProperties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String   methodName = "createGovernanceZone";

        final String   qualifiedNameParameter = "qualifiedName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-zone-manager/governance-zones";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        GovernanceZoneProperties requestBody = new GovernanceZoneProperties();

        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);
        requestBody.setCriteria(criteria);
        requestBody.setScope(scope);
        requestBody.setDomainIdentifier(domainIdentifier);
        requestBody.setAdditionalProperties(additionalProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformURLRoot + urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId);
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
    public GovernanceZoneElement getGovernanceZone(String   userId,
                                                   String   qualifiedName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String   methodName = "getGovernanceZone";

        final String   qualifiedNameParameter = "qualifiedName";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-zone-manager/governance-zones/name/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameter, methodName);

        ZoneResponse restResult = restClient.callZoneGetRESTCall(methodName,
                                                                 serverPlatformURLRoot + urlTemplate,
                                                                 serverName,
                                                                 userId,
                                                                 qualifiedName);

        return restResult.getGovernanceZone();
    }


    /**
     * Return information about the defined governance zones.
     *
     * @param userId calling user
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param maximumResults maximum number of elements to return an this call
     *
     * @return properties of the governance zone
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<GovernanceZoneElement> getGovernanceZones(String   userId,
                                                          int      startingFrom,
                                                          int      maximumResults) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String   methodName = "getGovernanceZones";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-zone-manager/governance-zones?startingFrom={4}&maximumResults={5}";

        invalidParameterHandler.validateUserId(userId, methodName);

        ZoneListResponse restResult = restClient.callZoneListGetRESTCall(methodName,
                                                                         serverPlatformURLRoot + urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         Integer.toString(startingFrom),
                                                                         Integer.toString(maximumResults));

        return restResult.getGovernanceZones();
    }

}
