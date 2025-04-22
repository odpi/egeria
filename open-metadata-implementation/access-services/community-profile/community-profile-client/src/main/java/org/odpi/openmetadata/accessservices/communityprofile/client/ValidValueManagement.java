/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.accessservices.communityprofile.api.ValidValueManagementInterface;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ReferenceValueAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueProperties;

import java.util.List;

/**
 * ValidValueManagerClient supports the APIs to maintain valid values and their related objects.
 */
public class ValidValueManagement extends CommunityProfileBaseClient implements ValidValueManagementInterface
{
    private static final String validValueURLTemplatePrefix = baseURLTemplatePrefix + "/valid-values";


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ValidValueManagement(String   serverName,
                                String   serverPlatformURLRoot,
                                AuditLog auditLog,
                                int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog, maxPageSize);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ValidValueManagement(String serverName,
                                String serverPlatformURLRoot,
                                int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ValidValueManagement(String serverName,
                                String serverPlatformURLRoot,
                                String userId,
                                String password,
                                int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
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
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ValidValueManagement(String   serverName,
                                String   serverPlatformURLRoot,
                                String   userId,
                                String   password,
                                AuditLog auditLog,
                                int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog, maxPageSize);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ValidValueManagement(String                     serverName,
                                String                     serverPlatformURLRoot,
                                CommunityProfileRESTClient restClient,
                                int                        maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /* =====================================================================================================================
     * A ValidValue is the top level object for working with connectors
     */

    /**
     * Create a new metadata element to represent a valid value.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param validValueProperties properties about the valid value to store
     *
     * @return unique identifier of the new valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createValidValue(String               userId,
                                   String               externalSourceGUID,
                                   String               externalSourceName,
                                   ValidValueProperties validValueProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName                  = "createValidValue";
        final String propertiesParameterName     = "validValueProperties";
        final String urlTemplate = serverPlatformURLRoot + validValueURLTemplatePrefix;

        return super.createReferenceable(userId, externalSourceGUID, externalSourceName, validValueProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Update the metadata element representing a valid value.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param validValueGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param validValueProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateValidValue(String              userId,
                                 String              externalSourceGUID,
                                 String              externalSourceName,
                                 String              validValueGUID,
                                 boolean             isMergeUpdate,
                                 ValidValueProperties validValueProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName                  = "updateValidValue";
        final String elementGUIDParameterName    = "validValueGUID";
        final String propertiesParameterName     = "validValueProperties";
        final String urlTemplate = serverPlatformURLRoot + validValueURLTemplatePrefix + "/{2}?isMergeUpdate={3}";

        super.updateReferenceable(userId, externalSourceGUID, externalSourceName, validValueGUID, elementGUIDParameterName, isMergeUpdate, validValueProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Create a membership relationship between a validValue and a validValueSet that it belongs to.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param validValueSetGUID unique identifier of the valid value set
     * @param properties describes the properties of the membership
     * @param validValueMemberGUID unique identifier of the member
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupValidValueMember(String                         userId,
                                      String                         externalSourceGUID,
                                      String                         externalSourceName,
                                      String                         validValueSetGUID,
                                      ValidValueMembershipProperties properties,
                                      String                         validValueMemberGUID) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName                        = "setupValidValueMember";
        final String validValueSetGUIDParameterName    = "validValueSetGUID";
        final String validValueMemberGUIDParameterName = "validValueMemberGUID";
        final String urlTemplate = serverPlatformURLRoot + validValueURLTemplatePrefix + "/{2}/valid-value-members/{3}";

        super.setupRelationship(userId, externalSourceGUID, externalSourceName, validValueSetGUID, validValueSetGUIDParameterName, properties, validValueMemberGUID, validValueMemberGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Remove a membership relationship between a validValue and a validValueSet that it belongs to.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param validValueSetGUID unique identifier of the valid value set
     * @param validValueMemberGUID unique identifier of the member
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearValidValueMember(String userId,
                                      String externalSourceGUID,
                                      String externalSourceName,
                                      String validValueSetGUID,
                                      String validValueMemberGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName                  = "clearValidValueMember";
        final String validValueSetGUIDParameterName    = "validValueSetGUID";
        final String validValueMemberGUIDParameterName = "validValueMemberGUID";
        final String urlTemplate  = serverPlatformURLRoot + validValueURLTemplatePrefix + "/{2}/valid-value-members/{3}/delete";

        super.clearRelationship(userId,
                                externalSourceGUID,
                                externalSourceName,
                                validValueSetGUID,
                                validValueSetGUIDParameterName,
                                validValueMemberGUID,
                                validValueMemberGUIDParameterName,
                                urlTemplate,
                                methodName);
    }


    /**
     * Create a valid value assignment relationship between an element and a valid value (typically, a valid value set) to show that
     * the valid value defines the values that can be stored in the data item that the element represents.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the element
     * @param properties describes the permissions that the role has in the validValue
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupValidValues(String                         userId,
                                 String                         externalSourceGUID,
                                 String                         externalSourceName,
                                 String                         elementGUID,
                                 ValidValueAssignmentProperties properties,
                                 String                         validValueGUID) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                  = "setupValidValues";
        final String elementGUIDParameterName    = "elementGUID";
        final String validValueGUIDParameterName = "validValueGUID";

        final String urlTemplate = serverPlatformURLRoot + validValueURLTemplatePrefix + "/{2}/valid-values/{3}";

        super.setupRelationship(userId, externalSourceGUID, externalSourceName, elementGUID, elementGUIDParameterName, properties, validValueGUID, validValueGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Remove a valid value assignment relationship between an element and a valid value.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearValidValues(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String elementGUID,
                                 String validValueGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName                  = "clearValidValues";
        final String elementGUIDParameterName    = "elementGUID";
        final String validValueGUIDParameterName = "validValueGUID";
        final String urlTemplate                 = serverPlatformURLRoot + validValueURLTemplatePrefix + "/{2}/valid-values/{3}/delete";

        super.clearRelationship(userId,
                                externalSourceGUID,
                                externalSourceName,
                                elementGUID,
                                elementGUIDParameterName,
                                validValueGUID,
                                validValueGUIDParameterName,
                                urlTemplate,
                                methodName);
    }


    /**
     * Create a reference value assignment relationship between an element and a valid value to show that
     * the valid value is a semiformal tag/classification.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the element
     * @param properties describes the quality of the assignment
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupReferenceValueTag(String                             userId,
                                       String                             externalSourceGUID,
                                       String                             externalSourceName,
                                       String                             elementGUID,
                                       ReferenceValueAssignmentProperties properties,
                                       String                             validValueGUID) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName                  = "setupReferenceValueTag";
        final String elementGUIDParameterName    = "elementGUID";
        final String validValueGUIDParameterName = "validValueGUID";

        final String urlTemplate = serverPlatformURLRoot + validValueURLTemplatePrefix + "/{2}/reference-value-tags/{3}";

        super.setupRelationship(userId, externalSourceGUID, externalSourceName, elementGUID, elementGUIDParameterName, properties, validValueGUID, validValueGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Remove a reference value assignment relationship between an element and a valid value.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearReferenceValueTag(String userId,
                                       String externalSourceGUID,
                                       String externalSourceName,
                                       String elementGUID,
                                       String validValueGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName                  = "clearReferenceValueTag";
        final String elementGUIDParameterName    = "elementGUID";
        final String validValueGUIDParameterName = "validValueGUID";
        final String urlTemplate                 = serverPlatformURLRoot + validValueURLTemplatePrefix + "/{2}/reference-value-tags/{3}/delete";

        super.clearRelationship(userId,
                                externalSourceGUID,
                                externalSourceName,
                                elementGUID,
                                elementGUIDParameterName,
                                validValueGUID,
                                validValueGUIDParameterName,
                                urlTemplate,
                                methodName);
    }


    /**
     * Remove the metadata element representing a valid value.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param validValueGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeValidValue(String userId,
                                 String externalSourceGUID,
                                 String externalSourceName,
                                 String validValueGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName               = "removeValidValue";
        final String elementGUIDParameterName = "validValueGUID";
        final String urlTemplate              = serverPlatformURLRoot + validValueURLTemplatePrefix + "/{2}/delete";

        super.removeReferenceable(userId, externalSourceGUID, externalSourceName, validValueGUID, elementGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
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
    public List<ValidValueElement> findValidValues(String userId,
                                                   String searchString,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName                = "findValidValue";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + validValueURLTemplatePrefix + "/by-search-string?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        ValidValuesResponse restResult = restClient.callValidValuesPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                serverName,
                                                                                userId,
                                                                                startFrom,
                                                                                validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
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
    public List<ValidValueElement> getValidValuesByName(String userId,
                                                        String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName        = "getValidValueByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + validValueURLTemplatePrefix + "/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameterName);

        ValidValuesResponse restResult = restClient.callValidValuesPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                serverName,
                                                                                userId,
                                                                                startFrom,
                                                                                validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the list of valid values.
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
    public List<ValidValueElement> getAllValidValues(String userId,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "getValidValues";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + validValueURLTemplatePrefix + "?startFrom={2}&pageSize={3}";

        ValidValuesResponse restResult = restClient.callValidValuesGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               startFrom,
                                                                               validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Page through the members of a valid value set.
     *
     * @param userId                     calling user
     * @param validValueSetGUID          unique identifier of the valid value set
     * @param startFrom                  paging starting point
     * @param pageSize                   maximum number of return values.
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    @Override
    public List<ValidValueElement> getValidValueSetMembers(String  userId,
                                                           String  validValueSetGUID,
                                                           int     startFrom,
                                                           int     pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "getValidValueSetMembers";
        final String guidParameterName = "validValueSetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueSetGUID, guidParameterName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + validValueURLTemplatePrefix + "/members/{2}?startFrom={3}&pageSize={4}";

        ValidValuesResponse restResult = restClient.callValidValuesGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               validValueSetGUID,
                                                                               startFrom,
                                                                               validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Page through the list of valid value sets that a valid value definition/set belongs to.
     *
     * @param userId                  calling user
     * @param validValueGUID          unique identifier of valid value to query
     * @param startFrom               paging starting point
     * @param pageSize                maximum number of return values.
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    @Override
    public List<ValidValueElement> getSetsForValidValue(String  userId,
                                                        String  validValueGUID,
                                                        int     startFrom,
                                                        int     pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "getValidValueSetMembers";
        final String guidParameterName = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + validValueURLTemplatePrefix + "/sets/{2}?startFrom={3}&pageSize={4}";

        ValidValuesResponse restResult = restClient.callValidValuesGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               validValueGUID,
                                                                               startFrom,
                                                                               validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Return information about the valid value set linked to an element as its
     * set of valid values.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the element using the valid value set
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public ValidValueElement getValidValuesForConsumer(String userId,
                                                       String elementGUID) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getValidValuesForConsumer";
        final String guidParameterName = "elementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + validValueURLTemplatePrefix + "/by-consumer/{2}";

        ValidValueResponse restResult = restClient.callValidValueGetRESTCall(methodName,
                                                                             urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             elementGUID);

        return restResult.getElement();
    }


    /**
     * Return information about the consumers linked to a validValue.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier for the validValue
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<RelatedElementStub> getConsumersOfValidValue(String userId,
                                                             String validValueGUID,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName        = "getConsumersOfValidValue";
        final String guidPropertyName  = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, guidPropertyName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + validValueURLTemplatePrefix + "/{2}/consumers?startFrom={3}&pageSize={4}";

        RelatedElementsResponse restResult = restClient.callRelatedElementsGetRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       validValueGUID,
                                                                                       startFrom,
                                                                                       validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Return information about the valid values linked as reference value tags to an element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of valid values
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ValidValueElement> getReferenceValues(String userId,
                                                      String elementGUID,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "getReferenceValues";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + validValueURLTemplatePrefix + "/by-reference-value-tags/{2}?startFrom={3}&pageSize={4}";

        ValidValuesResponse restResult = restClient.callValidValuesGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               startFrom,
                                                                               validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Return information about the person roles linked to a validValue.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier for the validValue
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<RelatedElementStub> getAssigneesOfReferenceValue(String userId,
                                                                 String validValueGUID,
                                                                 int    startFrom,
                                                                 int    pageSize) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName        = "getAssigneesOfReferenceValue";
        final String guidPropertyName  = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, guidPropertyName, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + validValueURLTemplatePrefix + "/{2}/by-reference-value-tag-assignees?startFrom={3}&pageSize={4}";

        RelatedElementsResponse restResult = restClient.callRelatedElementsGetRESTCall(methodName,
                                                                                       urlTemplate,
                                                                                       serverName,
                                                                                       userId,
                                                                                       validValueGUID,
                                                                                       startFrom,
                                                                                       validatedPageSize);

        return restResult.getElements();
    }


    /**
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ValidValueElement getValidValueByGUID(String userId,
                                                 String validValueGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "getValidValueByGUID";
        final String guidParameterName = "validValueGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(validValueGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + validValueURLTemplatePrefix + "/{2}";

        ValidValueResponse restResult = restClient.callValidValueGetRESTCall(methodName,
                                                                             urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             validValueGUID);

        return restResult.getElement();
    }
}
