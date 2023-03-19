/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.GovernanceDomainInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDomainElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDomainSetElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomain;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomainProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomainSetProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * GovernanceDomainManager sets up the governance domains that are part of an organization governance.
 * Each governance domain describes a focus for governance.
 */
public class GovernanceDomainManager extends GovernanceProgramBaseClient implements GovernanceDomainInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceDomainManager(String serverName,
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
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceDomainManager(String serverName,
                                   String serverPlatformURLRoot,
                                   String userId,
                                   String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }



    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceDomainManager(String   serverName,
                                   String   serverPlatformURLRoot,
                                   int      maxPageSize,
                                   AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceDomainManager(String   serverName,
                                   String   serverPlatformURLRoot,
                                   String   userId,
                                   String   password,
                                   int      maxPageSize,
                                   AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize, auditLog);
    }


    /**
     * Create a new client that uses the supplied rest client.  This is typically used when called from another OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient internal client for rest calls
     * @param maxPageSize pre-initialized parameter limit
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceDomainManager(String                      serverName,
                                   String                      serverPlatformURLRoot,
                                   GovernanceProgramRESTClient restClient,
                                   int                         maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /* =====================================================================================================================
     * Egeria defines a default set of governance domains for the governance program.  The method below sets them up.
     */

    /**
     * Create a governance domain set called "EgeriaStandardDomains" containing governance domain definitions for the following governance domains.
     *
     * <ul>
     *     <li>Unclassified - The governance domain is not specified - that is the definition applies to all domains - this is the default value for governance definitions within the governance program.</li>
     *     <li>Data - The data (information) governance domain</li>
     *     <li>Privacy - The data privacy governance domain</li>
     *     <li>Security - The security governance domain.</li>
     *     <li>IT Infrastructure - The IT infrastructure management governance domain.</li>
     *     <li>Software Development - The software development lifecycle (SDLC) governance domain.</li>
     *     <li>Corporate - The corporate governance domain.</li>
     *     <li>Asset Management - The physical asset management governance domain.</li>
     * </ul>
     *
     * @param userId calling user
     * @return unique identifier of the governance domain set
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createStandardGovernanceDomains(String userId) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName     = "createStandardGovernanceDomains";
        final String setName        = "EgeriaStandardDomains";
        final String setDescription = "Initial list of governance domain definitions for the following governance domains.\n" +
                                              "\n" +
                                              "* Unclassified - The governance domain is not specified - that is the definition applies to all domains - this is the default value for governance definitions within the governance program.\n" +
                                              "* Data - The data (information) governance domain\n" +
                                              "* Privacy - The data privacy governance domain\n" +
                                              "* Security - The security governance domain.\n" +
                                              "* IT Infrastructure - The IT infrastructure management governance domain.\n" +
                                              "* Software Development - The software development lifecycle (SDLC) governance domain.\n" +
                                              "* Corporate - The corporate governance domain.\n" +
                                              "* Asset Management - The physical asset management governance domain.\n";

        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceDomainSetProperties governanceDomainSetProperties = new GovernanceDomainSetProperties();
        governanceDomainSetProperties.setQualifiedName("GovernanceDomainSet:" + setName);
        governanceDomainSetProperties.setDisplayName(setName);
        governanceDomainSetProperties.setDescription(setDescription);

        String setGUID = this.createGovernanceDomainSet(userId, governanceDomainSetProperties);

        for (GovernanceDomain governanceDomain : GovernanceDomain.values())
        {
            if (governanceDomain != GovernanceDomain.OTHER)
            {
                GovernanceDomainProperties governanceDomainProperties = new GovernanceDomainProperties();

                governanceDomainProperties.setQualifiedName("GovernanceDomain:" + governanceDomain.getName());
                governanceDomainProperties.setDisplayName(governanceDomain.getName());
                governanceDomainProperties.setDomainIdentifier(governanceDomain.getOrdinal());
                governanceDomainProperties.setDescription(governanceDomain.getDescription());

                this.createGovernanceDomain(userId, setGUID, governanceDomainProperties);
            }
        }

        return setGUID;
    }


    /* =====================================================================================================================
     * The GovernanceDomainSet entity is the top level element in a collection of related governance domains.
     */


    /**
     * Create a new metadata element to represent the root of a Governance Domain Set.
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
    public String createGovernanceDomainSet(String                        userId,
                                            GovernanceDomainSetProperties properties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "createGovernanceDomainSet";
        final String propertiesParameter = "properties";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets";

        return super.createReferenceable(userId, properties, propertiesParameter, urlTemplate, methodName);
    }


    /**
     * Update the metadata element representing a Governance Domain Set.
     *
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the metadata element to remove
     * @param properties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateGovernanceDomainSet(String                        userId,
                                          String                        governanceDomainSetGUID,
                                          GovernanceDomainSetProperties properties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "updateGovernanceDomainSet";

        final String guidParameter = "governanceDomainSetGUID";
        final String propertiesParameter = "properties";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/{2}/update";

        super.updateReferenceable(userId, governanceDomainSetGUID, guidParameter, false, properties, propertiesParameter, urlTemplate, methodName);
    }


    /**
     * Remove the metadata element representing a governanceDomainSet.  The governance domains are not deleted.
     *
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeGovernanceDomainSet(String userId,
                                          String governanceDomainSetGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "removeGovernanceDomainSet";

        final String guidParameter = "governanceDomainSetGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/{2}/delete";

        super.removeReferenceable(userId, governanceDomainSetGUID, guidParameter, urlTemplate, methodName);
    }


    /**
     * Retrieve the list of governanceDomainSet metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
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
    public List<GovernanceDomainSetElement> findGovernanceDomainSets(String userId,
                                                                     String searchString,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "findGovernanceDomainSets";

        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/by-search-string?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceDomainSetListResponse restResult = restClient.callGovernanceDomainSetListPostRESTCall(methodName,
                                                                                                        urlTemplate,
                                                                                                        requestBody,
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        startFrom,
                                                                                                        queryPageSize);
        return restResult.getElements();
    }


    /**
     * Retrieve the list of governanceDomainSet metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
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
    public List<GovernanceDomainSetElement> getGovernanceDomainSetsByName(String userId,
                                                                          String name,
                                                                          int    startFrom,
                                                                          int    pageSize) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName = "getGovernanceDomainSetsByName";

        final String nameParameterName = "name";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/by-name?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        GovernanceDomainSetListResponse restResult = restClient.callGovernanceDomainSetListPostRESTCall(methodName,
                                                                                                        urlTemplate,
                                                                                                        requestBody,
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        startFrom,
                                                                                                        queryPageSize);
        return restResult.getElements();
    }


    /**
     * Retrieve the governanceDomainSet metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceDomainSetElement getGovernanceDomainSetByGUID(String userId,
                                                                   String governanceDomainSetGUID) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String methodName = "getGovernanceDomainSetByGUID";

        final String guidParameterName = "governanceDomainSetGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(governanceDomainSetGUID, guidParameterName, methodName);

        GovernanceDomainSetResponse restResult = restClient.callGovernanceDomainSetGetRESTCall(methodName,
                                                                                               urlTemplate,
                                                                                               serverName,
                                                                                               userId,
                                                                                               governanceDomainSetGUID);
        return restResult.getElement();
    }


    /* =====================================================================================================================
     * A Governance Domain describes an area of focus in the governance program.
     */

    /**
     * Create a new metadata element to represent a governance domain.
     *
     * @param userId calling user
     * @param setGUID unique identifier of the set that this identifier belongs
     * @param properties properties about the Governance Domain to store
     *
     * @return unique identifier of the new Governance Domain
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createGovernanceDomain(String                     userId,
                                         String                     setGUID,
                                         GovernanceDomainProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "createGovernanceDomain";

        final String guidParameter = "setGUID";
        final String propertiesParameter = "properties";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/governance-domains";

        return super.createReferenceableWithAnchor(userId, setGUID, guidParameter, properties, propertiesParameter, urlTemplate, methodName);
    }


    /**
     * Update the metadata element representing a Governance Domain.
     *
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the metadata element to update
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateGovernanceDomain(String                     userId,
                                       String                     governanceDomainGUID,
                                       GovernanceDomainProperties properties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "updateGovernanceDomain";

        final String guidParameter = "governanceDomainGUID";
        final String propertiesParameter = "properties";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domains/{2}/update";

        super.updateReferenceable(userId, governanceDomainGUID, guidParameter, false, properties, propertiesParameter, urlTemplate, methodName);
    }


    /**
     * Remove the metadata element representing a Governance Domain.
     *
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void deleteGovernanceDomain(String userId,
                                       String governanceDomainGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "deleteGovernanceDomain";

        final String guidParameter = "governanceDomainGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domains/{2}/delete";

        super.removeReferenceable(userId, governanceDomainGUID, guidParameter, urlTemplate, methodName);
    }


    /**
     * Create a parent-child relationship between a governance domain set and a governance domain.
     *
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the governance domain set
     * @param governanceDomainGUID unique identifier of the governance domain
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void addDomainToSet(String userId,
                               String governanceDomainSetGUID,
                               String governanceDomainGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName = "addDomainToSet";

        final String guid1Parameter = "governanceDomainSetGUID";
        final String guid2Parameter = "governanceDomainGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/{2}/governance-domains/{3}";

        super.setupRelationship(userId,
                                governanceDomainSetGUID,
                                guid1Parameter,
                                null,
                                null,
                                governanceDomainGUID,
                                guid2Parameter,
                                urlTemplate,
                                methodName);
    }


    /**
     * Remove a parent-child relationship between a governance domain set and a governance domain.
     *
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the governance domain set
     * @param governanceDomainGUID unique identifier of the governance domain
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeDomainFromSet(String userId,
                                    String governanceDomainSetGUID,
                                    String governanceDomainGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "removeDomainFromSet";

        final String guid1Parameter = "governanceDomainSetGUID";
        final String guid2Parameter = "governanceDomainGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/{2}/governance-domains/{3}/delete";

        super.clearRelationship(userId,
                                governanceDomainSetGUID,
                                guid1Parameter,
                                null,
                                governanceDomainGUID,
                                guid2Parameter,
                                urlTemplate,
                                methodName);
    }


    /**
     * Retrieve the list of Governance Domain metadata elements defined for the governance program.
     *
     * @param userId calling user
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
    public List<GovernanceDomainElement> getGovernanceDomains(String userId,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "getGovernanceDomains";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domains?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        GovernanceDomainListResponse restResult = restClient.callGovernanceDomainListGetRESTCall(methodName,
                                                                                                 urlTemplate,
                                                                                                 serverName,
                                                                                                 userId,
                                                                                                 startFrom,
                                                                                                 queryPageSize);
        return restResult.getElements();
    }


    /**
     * Retrieve the list of Governance Domain metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
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
    public List<GovernanceDomainElement> findGovernanceDomains(String userId,
                                                               String searchString,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "findGovernanceDomains";

        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domains/by-search-string?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceDomainListResponse restResult = restClient.callGovernanceDomainListPostRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  requestBody,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  startFrom,
                                                                                                  queryPageSize);
        return restResult.getElements();
    }


    /**
     * Return the list of governance domain sets that a governance domain belong.
     *
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the governance domain to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the sets associated with the requested governanceDomainSet
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GovernanceDomainSetElement> getSetsForGovernanceDomain(String userId,
                                                                       String governanceDomainGUID,
                                                                       int    startFrom,
                                                                       int    pageSize) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "getSetsForGovernanceDomain";

        final String guidParameterName = "governanceDomainGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domain-sets/by-governance-domains/{2}?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDomainGUID, guidParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        GovernanceDomainSetListResponse restResult = restClient.callGovernanceDomainSetListGetRESTCall(methodName,
                                                                                                       urlTemplate,
                                                                                                       serverName,
                                                                                                       userId,
                                                                                                       governanceDomainGUID,
                                                                                                       startFrom,
                                                                                                       queryPageSize);
        return restResult.getElements();
    }


    /**
     * Retrieve the list of Governance Domain metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
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
    public List<GovernanceDomainElement> getGovernanceDomainsByName(String userId,
                                                                    String name,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "getGovernanceDomainsByName";

        final String nameParameterName = "name";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domains/by-name?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        GovernanceDomainListResponse restResult = restClient.callGovernanceDomainListPostRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  requestBody,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  startFrom,
                                                                                                  queryPageSize);
        return restResult.getElements();
    }


    /**
     * Retrieve the Governance Domain metadata element with the supplied unique identifier assigned when the domain description was stored in
     * the metadata repository.
     *
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceDomainElement getGovernanceDomainByGUID(String userId,
                                                             String governanceDomainGUID) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "getGovernanceDomainByGUID";
        final String guidParameterName = "governanceDomainGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domains/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDomainGUID, guidParameterName, methodName);

        GovernanceDomainResponse restResult = restClient.callGovernanceDomainGetRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         serverName,
                                                                                         userId,
                                                                                         governanceDomainGUID);
        return restResult.getElement();
    }


    /**
     * Retrieve the Governance Domain metadata element with the supplied domain identifier.
     *
     * @param userId calling user
     * @param domainIdentifier identifier used to identify the domain
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceDomainElement getGovernanceDomainByIdentifier(String userId,
                                                                   int    domainIdentifier) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getGovernanceDomainByIdentifier";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-domains/by-identifier/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceDomainResponse restResult = restClient.callGovernanceDomainGetRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         serverName,
                                                                                         userId,
                                                                                         domainIdentifier);
        return restResult.getElement();
    }
}
