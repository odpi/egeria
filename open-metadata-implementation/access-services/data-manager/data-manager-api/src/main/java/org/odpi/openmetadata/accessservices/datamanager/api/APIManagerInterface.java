/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.api;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.APIOperationElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.APIElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.APIOperationProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.APIProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * APIManagerInterface defines the client side interface for the Data Manager OMAS that is
 * relevant for API assets that provide event-based services.   It provides the ability to
 * define and maintain the metadata about an API and the
 * APIOperations that define the operations and parameters of the API.
 */
public interface APIManagerInterface
{

    /*
     * The API is the top level asset in an event manager
     */

    /**
     * Create a new metadata element to represent an API.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param apiProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createAPI(String userId,
                       String eventBrokerGUID,
                       String eventBrokerName,
                       APIProperties apiProperties) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


    /**
     * Create a new metadata element to represent an API using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createAPIFromTemplate(String userId,
                                   String eventBrokerGUID,
                                   String eventBrokerName,
                                   String templateGUID,
                                   TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Update the metadata element representing an API.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param apiGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param apiProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateAPI(String userId,
                     String eventBrokerGUID,
                     String eventBrokerName,
                     String apiGUID,
                     boolean isMergeUpdate,
                     APIProperties apiProperties) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;


    /**
     * Update the zones for the API asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param userId calling user
     * @param apiGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void publishAPI(String userId,
                      String apiGUID) throws InvalidParameterException,
                                               UserNotAuthorizedException,
                                               PropertyServerException;


    /**
     * Update the zones for the API asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the API is first created).
     *
     * @param userId calling user
     * @param apiGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void withdrawAPI(String userId,
                       String apiGUID) throws InvalidParameterException,
                                                UserNotAuthorizedException,
                                                PropertyServerException;


    /**
     * Remove the metadata element representing an API.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param apiGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeAPI(String userId,
                     String eventBrokerGUID,
                     String eventBrokerName,
                     String apiGUID,
                     String qualifiedName) throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException;


    /**
     * Retrieve the list of API metadata elements that contain the search string.
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
    List<APIElement> findAPIs(String userId,
                                  String searchString,
                                  int startFrom,
                                  int pageSize) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException;


    /**
     * Retrieve the list of API metadata elements with a matching qualified or display name.
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
    List<APIElement> getAPIsByName(String userId,
                                       String name,
                                       int startFrom,
                                       int pageSize) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


    /**
     * Retrieve the list of APIs created by this caller.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the API manager (event broker)
     * @param eventBrokerName unique name of software server capability representing the API manager (event broker)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<APIElement> getAPIsForAPIManager(String userId,
                                               String eventBrokerGUID,
                                               String eventBrokerName,
                                               int startFrom,
                                               int pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Retrieve the API metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    APIElement getAPIByGUID(String userId,
                                String guid) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException;


    /*
     * A API may support one or more types of event depending on its capability
     */

    /**
     * Create a new metadata element to represent an API Operation.  This describes the structure of an event supported by
     * the API. The structure of this API Operation is added using SchemaAttributes.   These SchemaAttributes can have
     * a simple type or a nested structure.
     *
     * The API Operation is then linked directly to the API if it is the only event structure supported by the
     * API, or it is added to the API's API Operation list.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param apiGUID unique identifier of an API
     * @param properties properties about the API schema
     *
     * @return unique identifier of the new API schema
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createAPIOperation(String userId,
                           String eventBrokerGUID,
                           String eventBrokerName,
                           String apiGUID,
                           APIOperationProperties properties) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Create a new metadata element to represent a an API Operation using an existing API Operation as a template.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiGUID unique identifier of the API where the API Operation is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new API Operation
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createAPIOperationFromTemplate(String userId,
                                       String eventBrokerGUID,
                                       String eventBrokerName,
                                       String templateGUID,
                                       String apiGUID,
                                       TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;



    /**
     * Update the metadata element representing an API Operation.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param apiOperationGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateAPIOperation(String userId,
                         String eventBrokerGUID,
                         String eventBrokerName,
                         String apiOperationGUID,
                         boolean isMergeUpdate,
                         APIOperationProperties properties) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Remove an API Operation.
     *
     * @param userId calling user
     * @param eventBrokerGUID unique identifier of software server capability representing the caller
     * @param eventBrokerName unique name of software server capability representing the caller
     * @param apiOperationGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeAPIOperation(String userId,
                         String eventBrokerGUID,
                         String eventBrokerName,
                         String apiOperationGUID,
                         String qualifiedName) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException;


    /**
     * Retrieve the list of API Operations metadata elements that contain the search string.
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
    List<APIOperationElement> findAPIOperations(String userId,
                                          String searchString,
                                          int startFrom,
                                          int pageSize) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Return the list of API Operations associated with an EvenSet.  This is a collection of APIOperation definitions.
     * These API Operations can be used as a template for adding the API Operations to an API.
     *
     * @param userId calling user
     * @param eventSetGUID unique identifier of the API to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the API Operations associated with the requested EventSet
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<APIOperationElement> getAPIOperationsForEventSet(String userId,
                                                    String eventSetGUID,
                                                    int startFrom,
                                                    int pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;

    /**
     * Return the list of API Operations associated with an API.
     *
     * @param userId calling user
     * @param apiGUID unique identifier of the API to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the API Operations associated with the requested API
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<APIOperationElement> getOperationsForAPI(String userId,
                                                  String apiGUID,
                                                  int startFrom,
                                                  int pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Retrieve the list of API Operations metadata elements with a matching qualified or display name.
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
    List<APIOperationElement> getAPIOperationsByName(String userId,
                                               String name,
                                               int startFrom,
                                               int pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Retrieve the API Operation metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    APIOperationElement getAPIOperationByGUID(String userId,
                                        String guid) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;
}
