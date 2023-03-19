/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.client;

import org.odpi.openmetadata.accessservices.assetmanager.api.GovernanceExchangeInterface;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceActionElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceActionProcessElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceActionTypeElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.NextGovernanceActionTypeElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GovernanceDefinitionProperties;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionProcessElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionProcessElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionTypeElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceActionTypeElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NextGovernanceActionTypeElementsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;


/**
 * GovernanceExchangeClient is the client for managing governance policies and rules.
 */
public class GovernanceExchangeClient extends ExchangeClientBase implements GovernanceExchangeInterface
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
    public GovernanceExchangeClient(String   serverName,
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
    public GovernanceExchangeClient(String serverName,
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
    public GovernanceExchangeClient(String   serverName,
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
    public GovernanceExchangeClient(String                 serverName,
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
    public GovernanceExchangeClient(String serverName,
                                    String serverPlatformURLRoot,
                                    String userId,
                                    String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new definition.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param typeName type of definition
     * @param definitionProperties properties of the definition
     *
     * @return unique identifier of the definition
     *
     * @throws InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createGovernanceDefinition(String                         userId,
                                             String                         assetManagerGUID,
                                             String                         assetManagerName,
                                             String                         typeName,
                                             ExternalIdentifierProperties   externalIdentifierProperties,
                                             GovernanceDefinitionProperties definitionProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        return null;
    }


    /**
     * Update an existing definition.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID unique identifier of the definition to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param definitionProperties properties to update
     *
     * @throws InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  updateGovernanceDefinition(String                         userId,
                                            String                         assetManagerGUID,
                                            String                         assetManagerName,
                                            String                         definitionGUID,
                                            boolean                        isMergeUpdate,
                                            GovernanceDefinitionProperties definitionProperties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {

    }


    /**
     * Delete a specific governance definition.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID unique identifier of the definition to remove
     *
     * @throws InvalidParameterException guid is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  deleteGovernanceDefinition(String userId,
                                            String assetManagerGUID,
                                            String assetManagerName,
                                            String definitionGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {

    }


    /**
     * Link two related definitions together.
     * If the link already exists the description is updated.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionOneGUID unique identifier of the first definition
     * @param definitionTwoGUID unique identifier of the second definition
     * @param description description of their relationship
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void linkDefinitions(String userId,
                                String assetManagerGUID,
                                String assetManagerName,
                                String definitionOneGUID,
                                String definitionTwoGUID,
                                String description) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {

    }


    /**
     * Remove the link between two definitions.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionOneGUID unique identifier of the first definition
     * @param definitionTwoGUID unique identifier of the second definition
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void unlinkDefinitions(String userId,
                                  String assetManagerGUID,
                                  String assetManagerName,
                                  String definitionOneGUID,
                                  String definitionTwoGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {

    }


    /**
     * Create a link to show that a governance definition supports the requirements of one of the governance drivers.
     * If the link already exists the rationale is updated.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID unique identifier of the governance definition
     * @param delegatedToDefinitionGUID unique identifier of the governance definition that is delegated to
     * @param rationale description of how the delegation supports the definition
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void setupGovernanceDelegation(String userId,
                                          String assetManagerGUID,
                                          String assetManagerName,
                                          String definitionGUID,
                                          String delegatedToDefinitionGUID,
                                          String rationale) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {

    }


    /**
     * Remove the link between a governance definition and a governance definition that is delegated to (ie provides an implementation of).
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID unique identifier of the governance definition
     * @param delegatedToDefinitionGUID unique identifier of the governance definition that is delegated to
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearGovernanceDelegation(String userId,
                                          String assetManagerGUID,
                                          String assetManagerName,
                                          String definitionGUID,
                                          String delegatedToDefinitionGUID) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {

    }


    /**
     * Retrieve the governance definition by the unique identifier assigned by this service when it was created.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID identifier of the governance definition to retrieve
     *
     * @return properties of the matching definition
     *
     * @throws InvalidParameterException documentIdentifier or userId is null; guid is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public GovernanceDefinitionElement getGovernanceDefinitionByGUID(String userId,
                                                                     String assetManagerGUID,
                                                                     String assetManagerName,
                                                                     String definitionGUID) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return null;
    }


    /**
     * Retrieve the governance definition by its assigned unique document identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param documentIdentifier identifier to search for
     *
     * @return properties of the matching definition
     *
     * @throws InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public GovernanceDefinitionElement getGovernanceDefinitionByDocId(String userId,
                                                                      String assetManagerGUID,
                                                                      String assetManagerName,
                                                                      String documentIdentifier) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        return null;
    }


    /**
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to link
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addGovernanceDefinitionToElement(String userId,
                                                 String assetManagerGUID,
                                                 String assetManagerName,
                                                 String definitionGUID,
                                                 String elementGUID,
                                                 String methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {

    }


    /**
     * Remove the GovernedBy relationship between a governance definition and an element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to update
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGovernanceDefinitionFromElement(String userId,
                                                      String assetManagerGUID,
                                                      String assetManagerName,
                                                      String definitionGUID,
                                                      String elementGUID,
                                                      String methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {

    }


    /**
     * Classify the element to asset that the definitions it represents are part of a subject area definition.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the glossary category in the external asset manager
     * @param subjectAreaName qualified name of subject area
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void addElementToSubjectArea(String userId,
                                        String assetManagerGUID,
                                        String assetManagerName,
                                        String elementGUID,
                                        String elementExternalIdentifier,
                                        String subjectAreaName,
                                        String methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {

    }


    /**
     * Remove the subject area designation from the identified element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param externalElementIdentifier unique identifier of the equivalent element in the external asset manager
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeElementFromSubjectArea(String userId,
                                             String assetManagerGUID,
                                             String assetManagerName,
                                             String elementGUID,
                                             String externalElementIdentifier,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {

    }


    /**
     * Retrieve the list of governance action process metadata elements that contain the search string.
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
    public List<GovernanceActionProcessElement> findGovernanceActionProcesses(String userId,
                                                                              String searchString,
                                                                              int    startFrom,
                                                                              int    pageSize) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName = "findGovernanceActionProcesses";
        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-manager/users/{1}/governance-action-processes/by-search-string?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceActionProcessElementsResponse restResult = restClient.callGovernanceActionProcessElementsPostRESTCall(methodName,
                                                                                                                        urlTemplate,
                                                                                                                        requestBody,
                                                                                                                        serverName,
                                                                                                                        userId,
                                                                                                                        Integer.toString(startFrom),
                                                                                                                        Integer.toString(pageSize));

        return restResult.getElements();
    }



    /**
     * Retrieve the list of governance action process metadata elements with a matching qualified or display name.
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
    public List<GovernanceActionProcessElement> getGovernanceActionProcessesByName(String userId,
                                                                                   String name,
                                                                                   int    startFrom,
                                                                                   int    pageSize) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName = "getGovernanceActionProcessesByName";
        final String nameParameterName = "name";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-manager/users/{1}/governance-action-processes/by-name?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        GovernanceActionProcessElementsResponse restResult = restClient.callGovernanceActionProcessElementsPostRESTCall(methodName,
                                                                                                                        urlTemplate,
                                                                                                                        requestBody,
                                                                                                                        serverName,
                                                                                                                        userId,
                                                                                                                        Integer.toString(startFrom),
                                                                                                                        Integer.toString(pageSize));

        return restResult.getElements();
    }



    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceActionProcessElement getGovernanceActionProcessByGUID(String userId,
                                                                           String processGUID) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName = "getGovernanceActionProcessByGUID";
        final String guidParameterName = "processGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-manager/users/{1}/governance-action-processes/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        GovernanceActionProcessElementResponse restResult = restClient.callGovernanceActionProcessGetRESTCall(methodName,
                                                                                                              urlTemplate,
                                                                                                              serverName,
                                                                                                              userId,
                                                                                                              processGUID);

        return restResult.getElement();
    }


    /* =====================================================================================================================
     * A governance action type describes a step in a governance action process
     */


    /**
     * Retrieve the list of governance action type metadata elements that contain the search string.
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
    public List<GovernanceActionTypeElement> findGovernanceActionTypes(String userId,
                                                                       String searchString,
                                                                       int    startFrom,
                                                                       int    pageSize) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "findGovernanceActionTypes";
        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-manager/users/{1}/governance-action-types/by-search-string?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceActionTypeElementsResponse restResult = restClient.callGovernanceActionTypeElementsPostRESTCall(methodName,
                                                                                                                  urlTemplate,
                                                                                                                  requestBody,
                                                                                                                  serverName,
                                                                                                                  userId,
                                                                                                                  Integer.toString(startFrom),
                                                                                                                  Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the list of governance action type metadata elements with a matching qualified or display name.
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
    public List<GovernanceActionTypeElement> getGovernanceActionTypesByName(String userId,
                                                                            String name,
                                                                            int    startFrom,
                                                                            int    pageSize) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "getGovernanceActionTypesByName";
        final String nameParameterName = "name";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-manager/users/{1}/governance-action-types/by-name?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);

        GovernanceActionTypeElementsResponse restResult = restClient.callGovernanceActionTypeElementsPostRESTCall(methodName,
                                                                                                                  urlTemplate,
                                                                                                                  requestBody,
                                                                                                                  serverName,
                                                                                                                  userId,
                                                                                                                  Integer.toString(startFrom),
                                                                                                                  Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the governance action type
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceActionTypeElement getGovernanceActionTypeByGUID(String userId,
                                                                     String actionTypeGUID) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getGovernanceActionTypeByGUID";
        final String guidParameterName = "actionTypeGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-manager/users/{1}/governance-action-types/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTypeGUID, guidParameterName, methodName);

        GovernanceActionTypeElementResponse restResult = restClient.callGovernanceActionTypeGetRESTCall(methodName,
                                                                                                        urlTemplate,
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        actionTypeGUID);

        return restResult.getElement();
    }


    /**
     * Return the governance action type that is the first step in a governance action process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     *
     * @return properties of the governance action type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public GovernanceActionTypeElement getFirstActionType(String userId,
                                                          String processGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getFirstActionType";
        final String guidParameterName = "processGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-manager/users/{1}/governance-action-processes/{2}/first-action-type";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        GovernanceActionTypeElementResponse restResult = restClient.callGovernanceActionTypeGetRESTCall(methodName,
                                                                                                        urlTemplate,
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        processGUID);

        return restResult.getElement();
    }


    /**
     * Return the lust of next action type defined for the governance action process.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the current governance action type
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return return the list of relationships and attached governance action types.
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<NextGovernanceActionTypeElement> getNextGovernanceActionTypes(String userId,
                                                                              String actionTypeGUID,
                                                                              int    startFrom,
                                                                              int    pageSize) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName = "getNextGovernanceActionTypes";
        final String guidParameterName = "actionTypeGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-manager/users/{1}/governance-action-types/{2}/next-action-type?startFrom={4}&pageSize={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTypeGUID, guidParameterName, methodName);

        NextGovernanceActionTypeElementsResponse restResult = restClient.callNextGovernanceActionTypesGetRESTCall(methodName,
                                                                                                                  urlTemplate,
                                                                                                                  serverName,
                                                                                                                  userId,
                                                                                                                  actionTypeGUID,
                                                                                                                  Integer.toString(startFrom),
                                                                                                                  Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Request the status of an executing governance action request.
     *
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request.
     *
     * @return status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public GovernanceActionElement getGovernanceAction(String userId,
                                                       String governanceActionGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "getGovernanceAction";
        final String guidParameterName = "governanceActionGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-manager/users/{1}/governance-actions/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionGUID, guidParameterName, methodName);

        GovernanceActionElementResponse restResult = restClient.callGovernanceActionGetRESTCall(methodName,
                                                                                                urlTemplate,
                                                                                                serverName,
                                                                                                userId,
                                                                                                governanceActionGUID);

        return restResult.getElement();
    }


    /**
     * Retrieve the governance actions known to the server.
     *
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @return list of governance action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public List<GovernanceActionElement>  getGovernanceActions(String userId,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "getGovernanceActions";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-manager/users/{1}/governance-actions?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceActionElementsResponse restResult = restClient.callGovernanceActionsGetRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  Integer.toString(startFrom),
                                                                                                  Integer.toString(pageSize));

        return restResult.getElements();
    }


    /**
     * Retrieve the governance actions that are still in process.
     *
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @return list of governance action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    @Override
    public List<GovernanceActionElement>  getActiveGovernanceActions(String userId,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "getActiveGovernanceActions";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-manager/users/{1}/governance-actions/active?startFrom={2}&pageSize={3}";

        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceActionElementsResponse restResult = restClient.callGovernanceActionsGetRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  Integer.toString(startFrom),
                                                                                                  Integer.toString(pageSize));

        return restResult.getElements();
    }
}
