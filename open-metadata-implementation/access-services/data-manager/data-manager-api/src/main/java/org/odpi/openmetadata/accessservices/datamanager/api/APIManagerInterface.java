/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.api;
;
import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.APIElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.APIOperationElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.APIParameterListElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.APIProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIOperationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIParameterListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.APIParameterListType;

import java.util.List;

/**
 * APIManagerInterface defines the client side interface for the Data Manager OMAS that is
 * relevant for API assets that provide call-based services.   It provides the ability to
 * define and maintain the metadata about an API and the
 * APIOperations that define the operations and parameters of the API.
 */
public interface APIManagerInterface
{

    /*
     * The API is the top level asset in API manager
     */

    /**
     * Create a new metadata element to represent an API.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiManagerIsHome should the API be marked as owned by the API manager so others can not update?
     * @param endpointGUID unique identifier of the endpoint where this API is located
     * @param apiProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createAPI(String        userId,
                     String        apiManagerGUID,
                     String        apiManagerName,
                     boolean       apiManagerIsHome,
                     String        endpointGUID,
                     APIProperties apiProperties) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException;


    /**
     * Create a new metadata element to represent an API using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiManagerIsHome should the API be marked as owned by the API manager so others can not update?
     * @param endpointGUID unique identifier of the endpoint where this API is located
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createAPIFromTemplate(String             userId,
                                 String             apiManagerGUID,
                                 String             apiManagerName,
                                 boolean            apiManagerIsHome,
                                 String             endpointGUID,
                                 String             templateGUID,
                                 TemplateProperties templateProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Update the metadata element representing an API.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param apiProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateAPI(String        userId,
                   String        apiManagerGUID,
                   String        apiManagerName,
                   String        apiGUID,
                   boolean       isMergeUpdate,
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
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeAPI(String userId,
                   String apiManagerGUID,
                   String apiManagerName,
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
                              int    startFrom,
                              int    pageSize) throws InvalidParameterException,
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
                                   int    startFrom,
                                   int    pageSize) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException;


    /**
     * Retrieve the list of APIs created by this caller.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the API manager (API manager)
     * @param apiManagerName unique name of software server capability representing the API manager (API manager)
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
                                          String apiManagerGUID,
                                          String apiManagerName,
                                          int    startFrom,
                                          int    pageSize) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Retrieve the list of APIs connected to the requested endpoint.
     *
     * @param userId calling user
     * @param endpointGUID unique identifier of the endpoint
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<APIElement> getAPIsForEndpoint(String userId,
                                        String endpointGUID,
                                        int    startFrom,
                                        int    pageSize) throws InvalidParameterException,
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
     * A API may support one or more types of operations depending on its capability
     */

    /**
     * Create a new metadata element to represent an API Operation.  This describes the structure of an operation supported by
     * the API. The structure of this API Operation is added using API Parameter Lists.   These parameter lists can have
     * a simple type or a nested structure.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiGUID unique identifier of an API
     * @param properties properties about the API Operation
     *
     * @return unique identifier of the new API Operation
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createAPIOperation(String                 userId,
                              String                 apiManagerGUID,
                              String                 apiManagerName,
                              String                 apiGUID,
                              APIOperationProperties properties) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Create a new metadata element to represent a an API Operation using an existing API Operation as a template.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
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
    String createAPIOperationFromTemplate(String             userId,
                                          String             apiManagerGUID,
                                          String             apiManagerName,
                                          String             templateGUID,
                                          String             apiGUID,
                                          TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;



    /**
     * Update the metadata element representing an API Operation.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiOperationGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateAPIOperation(String                 userId,
                            String                 apiManagerGUID,
                            String                 apiManagerName,
                            String                 apiOperationGUID,
                            boolean                isMergeUpdate,
                            APIOperationProperties properties) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Remove an API Operation.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiOperationGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeAPIOperation(String userId,
                            String apiManagerGUID,
                            String apiManagerName,
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
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Return the list of API Parameter Lists associated with an API Operation.
     *
     * @param userId calling user
     * @param apiGUID unique identifier of the API to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the API Parameter Lists associated with the requested API Operation
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<APIOperationElement> getOperationsForAPI(String userId,
                                                  String apiGUID,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
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
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
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

    /*
     * An API Operation may support a header, a request and a response parameter list of depending on its capability
     */

    /**
     * Create a new metadata element to represent an API Operation's Parameter list.  This describes the structure of the payload supported by
     * the API's operation. The structure of this API Operation is added using API Parameter schema attributes.   These parameters can have
     * a simple type or a nested structure.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiOperationGUID unique identifier of an APIOperation
     * @param parameterListType is this a header, request or response
     * @param properties properties about the API parameter list
     *
     * @return unique identifier of the new API parameter list
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createAPIParameterList(String                     userId,
                                  String                     apiManagerGUID,
                                  String                     apiManagerName,
                                  String                     apiOperationGUID,
                                  APIParameterListType parameterListType,
                                  APIParameterListProperties properties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Create a new metadata element to represent a an API Parameter List using an existing API Parameter List as a template.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiOperationGUID unique identifier of the API where the API Operation is located
     * @param parameterListType is this a header, request or response
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new API Parameter List
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createAPIParameterListFromTemplate(String               userId,
                                              String               apiManagerGUID,
                                              String               apiManagerName,
                                              String               templateGUID,
                                              String               apiOperationGUID,
                                              APIParameterListType parameterListType,
                                              TemplateProperties   templateProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;



    /**
     * Update the metadata element representing an API ParameterList.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiParameterListGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateAPIParameterList(String                     userId,
                                String                     apiManagerGUID,
                                String                     apiManagerName,
                                String                     apiParameterListGUID,
                                boolean                    isMergeUpdate,
                                APIParameterListProperties properties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Remove an API Parameter List and all of its parameters.
     *
     * @param userId calling user
     * @param apiManagerGUID unique identifier of software server capability representing the caller
     * @param apiManagerName unique name of software server capability representing the caller
     * @param apiParameterListGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeAPIParameterList(String userId,
                                String apiManagerGUID,
                                String apiManagerName,
                                String apiParameterListGUID,
                                String qualifiedName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;




    /**
     * Retrieve the list of API Parameter List metadata elements that contain the search string.
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
    List<APIParameterListElement> findAPIParameterLists(String userId,
                                                        String searchString,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Return the list of API Parameter Lists associated with an API Operation.
     *
     * @param userId calling user
     * @param apiOperationGUID unique identifier of the API Operation to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the API Parameter Lists associated with the requested API Operation
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<APIParameterListElement> getParameterListsForAPIOperation(String userId,
                                                                   String apiOperationGUID,
                                                                   int    startFrom,
                                                                   int    pageSize) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


    /**
     * Retrieve the list of API Parameter List metadata elements with a matching qualified or display name.
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
    List<APIParameterListElement> getAPIParameterListsByName(String userId,
                                                             String name,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Retrieve the API Parameter List metadata element with the supplied unique identifier.
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
    APIParameterListElement getAPIParameterListByGUID(String userId,
                                                      String guid) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;
}
