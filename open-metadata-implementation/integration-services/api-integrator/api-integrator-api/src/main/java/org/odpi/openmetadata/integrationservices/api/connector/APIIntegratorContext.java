/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.api.connector;

import org.odpi.openmetadata.accessservices.datamanager.api.DataManagerEventListener;
import org.odpi.openmetadata.accessservices.datamanager.client.APIManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.ConnectionManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.DataManagerEventClient;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.util.List;


/**
 * APIIntegratorContext is the context for managing resources from a relational api server.
 */
public class APIIntegratorContext
{
    private final APIManagerClient        apiManagerClient;
    private final ConnectionManagerClient connectionManagerClient;
    private final DataManagerEventClient  eventClient;
    private final String                  userId;
    private final String                  apiManagerGUID;
    private final String                  apiManagerName;

    private boolean                apiManagerIsHome = true;

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param apiManagerClient client to map request to
     * @param connectionManagerClient client to manage connections in the metadata server
     * @param eventClient client to register for events
     * @param userId integration daemon's userId
     * @param apiManagerGUID unique identifier of the software server capability for the api manager
     * @param apiManagerName unique name of the software server capability for the api manager
     */
    public APIIntegratorContext(APIManagerClient        apiManagerClient,
                                ConnectionManagerClient connectionManagerClient,
                                DataManagerEventClient  eventClient,
                                String                  userId,
                                String                  apiManagerGUID,
                                String                  apiManagerName)
    {
        this.apiManagerClient        = apiManagerClient;
        this.connectionManagerClient = connectionManagerClient;
        this.eventClient             = eventClient;
        this.userId                  = userId;
        this.apiManagerGUID          = apiManagerGUID;
        this.apiManagerName          = apiManagerName;
    }



    /* ========================================================
     * Returning the API manager name from the configuration
     */


    /**
     * Return the qualified name of the API manager that is supplied in the configuration
     * document.
     *
     * @return string name
     */
    public String getAPIManagerName()
    {
        return apiManagerName;
    }


    /* ========================================================
     * Set up whether API metadata is owned by the API manager
     */


    /**
     * Set up the flag that controls the ownership of metadata created for this API manager. Default is true.
     *
     * @param apiManagerIsHome should the API metadata be marked as owned by the API manager so others can not update?
     */
    public void setAPIManagerIsHome(boolean apiManagerIsHome)
    {
        this.apiManagerIsHome = apiManagerIsHome;
    }


    /* ========================================================
     * Register for inbound events from the Data Manager OMAS OutTopic
     */


    /**
     * Register a listener object that will be passed each of the events published by
     * the Data Manager OMAS.
     *
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(DataManagerEventListener listener) throws InvalidParameterException,
                                                                           ConnectionCheckedException,
                                                                           ConnectorCheckedException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        eventClient.registerListener(userId, listener);
    }


    /*
     * ========================================================
     * APIs are connected to an endpoint
     */


    /**
     * Create a new metadata element to represent an endpoint
     *
     * @param endpointProperties properties about the endpoint to store
     *
     * @return unique identifier of the new endpoint
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpoint(EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return connectionManagerClient.createEndpoint(userId, null, null, endpointProperties);
    }


    /**
     * Create a new metadata element to represent a endpoint using an existing metadata element as a template.
     *
     * @param networkAddress location of the endpoint
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties descriptive properties that override the template
     *
     * @return unique identifier of the new endpoint
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpointFromTemplate(String             networkAddress,
                                             String             templateGUID,
                                             TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return connectionManagerClient.createEndpointFromTemplate(userId, null, null, networkAddress, templateGUID, templateProperties);
    }


    /**
     * Update the metadata element representing a endpoint.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param endpointGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param endpointProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateEndpoint(boolean            isMergeUpdate,
                               String             endpointGUID,
                               EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        connectionManagerClient.updateEndpoint(userId, apiManagerGUID, apiManagerName, isMergeUpdate, endpointGUID, endpointProperties);
    }




    /**
     * Remove the metadata element representing a endpoint.
     *
     * @param endpointGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeEndpoint(String endpointGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        connectionManagerClient.removeEndpoint(userId, apiManagerGUID, apiManagerName, endpointGUID);
    }


    /**
     * Retrieve the list of endpoint metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
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
    public List<EndpointElement> findEndpoints(String searchString,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return connectionManagerClient.findEndpoints(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of endpoint metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
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
    public List<EndpointElement> getEndpointsByName(String name,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return connectionManagerClient.getEndpointsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the endpoint metadata element with the supplied unique identifier.
     *
     * @param endpointGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EndpointElement getEndpointByGUID(String endpointGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return connectionManagerClient.getEndpointByGUID(userId, endpointGUID);
    }


    /* ========================================================
     * The api is the top level asset on an API server
     */


    /**
     * Create a new metadata element to represent an API.
     *
     * @param endpointGUID unique identifier of the endpoint where this API is located
     * @param apiProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPI(String        endpointGUID,
                            APIProperties apiProperties) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        return apiManagerClient.createAPI(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, endpointGUID, apiProperties);
    }


    /**
     * Create a new metadata element to represent an API using an existing metadata element as a template.
     *
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
    public String createAPIFromTemplate(String             endpointGUID,
                                        String             templateGUID,
                                        TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return apiManagerClient.createAPIFromTemplate(userId, apiManagerGUID, apiManagerName, apiManagerIsHome, endpointGUID, templateGUID, templateProperties);
    }


    /**
     * Update the metadata element representing an API.
     *
     * @param apiGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param apiProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAPI(String        apiGUID,
                          boolean       isMergeUpdate,
                          APIProperties apiProperties) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        apiManagerClient.updateAPI(userId, apiManagerGUID, apiManagerName, apiGUID, isMergeUpdate, apiProperties);
    }


    /**
     * Update the zones for the API asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Data Manager OMAS).
     *
     * @param apiGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishAPI(String apiGUID) throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException
    {
        apiManagerClient.publishAPI(userId, apiGUID);
    }


    /**
     * Update the zones for the API asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Data Manager OMAS.  This is the setting when the API is first created).
     *
     * @param apiGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawAPI(String apiGUID) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        apiManagerClient.withdrawAPI(userId, apiGUID);
    }


    /**
     * Remove the metadata element representing an API.
     *
     * @param apiGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeAPI(String apiGUID,
                          String qualifiedName) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        apiManagerClient.removeAPI(userId, apiManagerGUID, apiManagerName, apiGUID, qualifiedName);
    }


    /**
     * Retrieve the list of API metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
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
    public List<APIElement> findAPIs(String searchString,
                                     int    startFrom,
                                     int    pageSize) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        return apiManagerClient.findAPIs(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of API metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
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
    public List<APIElement>   getAPIsByName(String name,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return apiManagerClient.getAPIsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of apis created by this caller.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIElement>   getMyAPIs(int    startFrom,
                                        int    pageSize) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        return apiManagerClient.getAPIsForAPIManager(userId, apiManagerGUID, apiManagerName, startFrom, pageSize);
    }


    /**
     * Retrieve the API metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIElement getAPIByGUID(String guid) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        return apiManagerClient.getAPIByGUID(userId, guid);
    }


    /* ============================================================================
     * An API typically has a request, a responses and optionally a header
     */

    /**
     * Create a new metadata element to represent an API operation.
     *
     * @param apiGUID unique identifier of the API where the operation is located
     * @param apiOperationProperties properties about the API operation
     *
     * @return unique identifier of the new API operation
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPIOperation(String                 apiGUID,
                                     APIOperationProperties apiOperationProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        if (apiManagerIsHome)
        {
            return apiManagerClient.createAPIOperation(userId, apiManagerGUID, apiManagerName, apiGUID, apiOperationProperties);
        }
        else
        {
            return apiManagerClient.createAPIOperation(userId, null, null, apiGUID, apiOperationProperties);
        }
    }


    /**
     * Create a new metadata element to represent an API operation using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiGUID unique identifier of the API where the operation is located
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new API operation
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPIOperationFromTemplate(String             templateGUID,
                                                 String             apiGUID,
                                                 TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        if (apiManagerIsHome)
        {
            return apiManagerClient.createAPIOperationFromTemplate(userId, apiManagerGUID, apiManagerName, templateGUID, apiGUID, templateProperties);
        }
        else
        {
            return apiManagerClient.createAPIOperationFromTemplate(userId, null, null, templateGUID, apiGUID, templateProperties);
        }
    }


    /**
     * Update the metadata element representing an API operation.
     *
     * @param apiOperationGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param apiOperationProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAPIOperation(String                 apiOperationGUID,
                                   boolean                isMergeUpdate,
                                   APIOperationProperties apiOperationProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        apiManagerClient.updateAPIOperation(userId, apiManagerGUID, apiManagerName, apiOperationGUID, isMergeUpdate, apiOperationProperties);
    }


    /**
     * Remove the metadata element representing an API operation.
     *
     * @param apiOperationGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeAPIOperation(String apiOperationGUID,
                                   String qualifiedName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        apiManagerClient.removeAPIOperation(userId, apiManagerGUID, apiManagerName, apiOperationGUID, qualifiedName);
    }


    /**
     * Retrieve the list of API operation metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
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
    public List<APIOperationElement>   findAPIOperations(String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return apiManagerClient.findAPIOperations(userId, searchString, startFrom, pageSize);
    }


    /**
     * Return the list of operations associated with an API.
     *
     * @param apiGUID unique identifier of the API to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the operations associated with the requested api
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIOperationElement>   getOperationsForAPI(String apiGUID,
                                                           int    startFrom,
                                                           int    pageSize) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return apiManagerClient.getOperationsForAPI(userId, apiGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of API operation metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
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
    public List<APIOperationElement>   getAPIOperationsByName(String name,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return apiManagerClient.getAPIOperationsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the API operation metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIOperationElement getAPIOperationByGUID(String guid) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return apiManagerClient.getAPIOperationByGUID(userId, guid);
    }


    /* =====================================================================================================================
     * A API Operation may support a header, a request and a response parameter list of operations depending on its capability
     */


    /**
     * Create a new metadata element to represent an API Operation's Parameter list.  This describes the structure of the payload supported by
     * the API's operation. The structure of this API Operation is added using API Parameter API parameters.   These parameters can have
     * a simple type or a nested structure.
     *
     * @param apiOperationGUID unique identifier of an APIOperation
     * @param parameterListType is this is a header, request of response
     * @param properties properties about the API parameter list
     *
     * @return unique identifier of the new API parameter list
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPIParameterList(String                     apiOperationGUID,
                                         APIParameterListType       parameterListType,
                                         APIParameterListProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        if (apiManagerIsHome)
        {
            return apiManagerClient.createAPIParameterList(userId, apiManagerGUID, apiManagerName, apiOperationGUID, parameterListType, properties);
        }
        else
        {
            return apiManagerClient.createAPIParameterList(userId, null, null, apiOperationGUID, parameterListType, properties);
        }
    }


    /**
     * Create a new metadata element to represent a an API Parameter List using an existing API Parameter List as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param apiOperationGUID unique identifier of the API where the API Operation is located
     * @param parameterListType is this is a header, request of response
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new API Parameter List
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPIParameterListFromTemplate(String               templateGUID,
                                                     String               apiOperationGUID,
                                                     APIParameterListType parameterListType,
                                                     TemplateProperties   templateProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        if (apiManagerIsHome)
        {
            return apiManagerClient.createAPIParameterListFromTemplate(userId, apiManagerGUID, apiManagerName, templateGUID, apiOperationGUID, parameterListType, templateProperties);
        }
        else
        {
            return apiManagerClient.createAPIParameterListFromTemplate(userId, null, null, templateGUID, apiOperationGUID, parameterListType, templateProperties);
        }
    }



    /**
     * Update the metadata element representing an API Parameter List.
     *
     * @param apiParameterListGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAPIParameterList(String                     apiParameterListGUID,
                                       boolean                    isMergeUpdate,
                                       APIParameterListProperties properties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        apiManagerClient.updateAPIParameterList(userId, apiManagerGUID, apiManagerName, apiParameterListGUID, isMergeUpdate, properties);
    }


    /**
     * Remove an API Parameter List and all of its parameters.
     *
     * @param apiParameterListGUID unique identifier of the metadata element to remove
     * @param qualifiedName unique name of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeAPIParameterList(String apiParameterListGUID,
                                       String qualifiedName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        apiManagerClient.removeAPIParameterList(userId, apiManagerGUID, apiManagerName, apiParameterListGUID, qualifiedName);
    }


    /**
     * Retrieve the list of API Parameter List metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
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
    public List<APIParameterListElement> findAPIParameterLists(String searchString,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return apiManagerClient.findAPIParameterLists(userId, searchString, startFrom, pageSize);
    }


    /**
     * Return the list of API Parameter Lists associated with an API Operation.
     *
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
    public List<APIParameterListElement> getParameterListsForAPIOperation(String apiOperationGUID,
                                                                          int    startFrom,
                                                                          int    pageSize) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return apiManagerClient.getParameterListsForAPIOperation(userId, apiOperationGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of API Parameter List metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
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
    public List<APIParameterListElement> getAPIParameterListsByName(String name,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return apiManagerClient.getAPIParameterListsByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the API Parameter List metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIParameterListElement getAPIParameterListByGUID(String guid) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return apiManagerClient.getAPIParameterListByGUID(userId, guid);
    }



    /* ===============================================================================
     * An API Parameter List typically contains many API parameters, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a API parameter.
     *
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the API parameter is nested underneath
     * @param apiParameterProperties properties for the API parameter
     *
     * @return unique identifier of the new metadata element for the API parameter
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPIParameter(String                 schemaElementGUID,
                                     APIParameterProperties apiParameterProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        if (apiManagerIsHome)
        {
            return apiManagerClient.createAPIParameter(userId, apiManagerGUID, apiManagerName, schemaElementGUID, apiParameterProperties);
        }
        else
        {
            return apiManagerClient.createAPIParameter(userId, null, null, schemaElementGUID, apiParameterProperties);
        }
    }


    /**
     * Create a new metadata element to represent a API parameter using an existing metadata element as a template.
     *
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the API parameter is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the API parameter
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAPIParameterFromTemplate(String             schemaElementGUID,
                                                 String             templateGUID,
                                                 TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        if (apiManagerIsHome)
        {
            return apiManagerClient.createAPIParameterFromTemplate(userId, apiManagerGUID, apiManagerName, schemaElementGUID, templateGUID, templateProperties);
        }
        else
        {
            return apiManagerClient.createAPIParameterFromTemplate(userId, null, null, schemaElementGUID, templateGUID, templateProperties);
        }
    }


    /**
     * Connect a schema type to a API parameter.
     *
     * @param relationshipTypeName name of relationship to create
     * @param apiParameterGUID unique identifier of the API parameter
     * @param schemaTypeGUID unique identifier of the schema type to connect
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSchemaType(String relationshipTypeName,
                                String apiParameterGUID,
                                String schemaTypeGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        if (apiManagerIsHome)
        {
            apiManagerClient.setupSchemaType(userId, apiManagerGUID, apiManagerName, relationshipTypeName, apiParameterGUID, schemaTypeGUID);
        }
        else
        {
            apiManagerClient.setupSchemaType(userId, null, null, relationshipTypeName, apiParameterGUID, schemaTypeGUID);
        }
    }


    /**
     * Remove the linked schema types from a API parameter.
     *
     * @param apiParameterGUID unique identifier of the API parameter
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaTypes(String apiParameterGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        apiManagerClient.clearSchemaTypes(userId, apiManagerGUID, apiManagerName, apiParameterGUID);
    }


    /**
     * Update the properties of the metadata element representing a API parameter.
     *
     * @param apiParameterGUID unique identifier of the API parameter to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param apiParameterProperties new properties for the API parameter
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAPIParameter(String                 apiParameterGUID,
                                   boolean                isMergeUpdate,
                                   APIParameterProperties apiParameterProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        apiManagerClient.updateAPIParameter(userId, apiManagerGUID, apiManagerName, apiParameterGUID, isMergeUpdate, apiParameterProperties);
    }


    /**
     * Remove the metadata element representing a API parameter.
     *
     * @param apiParameterGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeAPIParameter(String apiParameterGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        apiManagerClient.removeAPIParameter(userId, apiManagerGUID, apiManagerName, apiParameterGUID);
    }


    /**
     * Retrieve the list of API parameter metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param typeName optional type name for the API parameter - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIParameterElement> findAPIParameters(String searchString,
                                                       String typeName,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return apiManagerClient.findAPIParameters(userId, searchString, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the list of API parameters associated with a parameter list or nested underneath another parameter.
     *
     * @param parentElementGUID unique identifier of the element of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIParameterElement> getNestedAPIParameters(String parentElementGUID,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return apiManagerClient.getNestedAPIParameters(userId, parentElementGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of API parameter metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<APIParameterElement> getAPIParametersByName(String name,
                                                            String typeName,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return apiManagerClient.getAPIParametersByName(userId, name, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the API parameter metadata element with the supplied unique identifier.
     *
     * @param apiParameterGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public APIParameterElement getAPIParameterByGUID(String apiParameterGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return apiManagerClient.getAPIParameterByGUID(userId, apiParameterGUID);
    }


    /* =====================================================================================================================
     * A schemaType is used to describe complex structures found in the schema of an API's payload (Header, Request or Response)
     */


    /**
     * Create a new metadata element to represent a primitive schema type such as a string, integer or character.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createPrimitiveSchemaType(PrimitiveSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        if (apiManagerIsHome)
        {
            return apiManagerClient.createPrimitiveSchemaType(userId, apiManagerGUID, apiManagerName, schemaTypeProperties);
        }
        else
        {
            return apiManagerClient.createPrimitiveSchemaType(userId, null, null, schemaTypeProperties);
        }
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed value.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createLiteralSchemaType(LiteralSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        if (apiManagerIsHome)
        {
            return apiManagerClient.createLiteralSchemaType(userId, apiManagerGUID, apiManagerName, schemaTypeProperties);
        }
        else
        {
            return apiManagerClient.createLiteralSchemaType(userId, null, null, schemaTypeProperties);
        }
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed set of values that are described by a valid value set.
     *
     * @param schemaTypeProperties properties about the schema type to store
     * @param validValuesSetGUID unique identifier of the valid values set to used
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEnumSchemaType(EnumSchemaTypeProperties schemaTypeProperties,
                                       String                   validValuesSetGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        if (apiManagerIsHome)
        {
            return apiManagerClient.createEnumSchemaType(userId, apiManagerGUID, apiManagerName, schemaTypeProperties, validValuesSetGUID);
        }
        else
        {
            return apiManagerClient.createEnumSchemaType(userId, null, null, schemaTypeProperties, validValuesSetGUID);
        }
    }


    /**
     * Retrieve the list of valid value set metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
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
    public List<ValidValueSetElement> getValidValueSetByName(String name,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return apiManagerClient.getValidValueSetByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of valid value set metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
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
    public List<ValidValueSetElement> findValidValueSet(String searchString,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return apiManagerClient.findValidValueSet(userId, searchString, startFrom, pageSize);
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createStructSchemaType(StructSchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        if (apiManagerIsHome)
        {
            return apiManagerClient.createStructSchemaType(userId, apiManagerGUID, apiManagerName, schemaTypeProperties);
        }
        else
        {
            return apiManagerClient.createStructSchemaType(userId, null, null, schemaTypeProperties);
        }
    }


    /**
     * Create a new metadata element to represent a list of possible schema types that can be used for the attached API parameter.
     *
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     * @param schemaTypeOptionGUIDs list of unique identifiers for schema types to link to
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeChoice(SchemaTypeChoiceProperties schemaTypeProperties,
                                         List<String>               schemaTypeOptionGUIDs) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        if (apiManagerIsHome)
        {
            return apiManagerClient.createSchemaTypeChoice(userId, apiManagerGUID, apiManagerName, schemaTypeProperties, schemaTypeOptionGUIDs);
        }
        else
        {
            return apiManagerClient.createSchemaTypeChoice(userId, null, null, schemaTypeProperties, schemaTypeOptionGUIDs);
        }
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param schemaTypeProperties properties about the schema type to store
     * @param mapFromSchemaTypeGUID unique identifier of the domain of the map
     * @param mapToSchemaTypeGUID unique identifier of the range of the map
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createMapSchemaType(MapSchemaTypeProperties schemaTypeProperties,
                                      String                  mapFromSchemaTypeGUID,
                                      String                  mapToSchemaTypeGUID) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        if (apiManagerIsHome)
        {
            return apiManagerClient.createMapSchemaType(userId, apiManagerGUID, apiManagerName, schemaTypeProperties, mapFromSchemaTypeGUID, mapToSchemaTypeGUID);
        }
        else
        {
            return apiManagerClient.createMapSchemaType(userId, null, null, schemaTypeProperties, mapFromSchemaTypeGUID, mapToSchemaTypeGUID);
        }
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeFromTemplate(String             templateGUID,
                                               TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        if (apiManagerIsHome)
        {
            return apiManagerClient.createSchemaTypeFromTemplate(userId, apiManagerGUID, apiManagerName, templateGUID, templateProperties);
        }
        else
        {
            return apiManagerClient.createSchemaTypeFromTemplate(userId, null, null, templateGUID, templateProperties);
        }
    }


    /**
     * Update the metadata element representing a schema type.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaTypeProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaType(String               schemaTypeGUID,
                                 boolean              isMergeUpdate,
                                 SchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        apiManagerClient.updateSchemaType(userId, apiManagerGUID, apiManagerName, schemaTypeGUID, isMergeUpdate, schemaTypeProperties);
    }


    /**
     * Remove the metadata element representing a schema type.
     *
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaType(String schemaTypeGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        apiManagerClient.removeSchemaType(userId, apiManagerGUID, apiManagerName, schemaTypeGUID);
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement> findSchemaType(String searchString,
                                                  String typeName,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return apiManagerClient.findSchemaType(userId, searchString, typeName, startFrom, pageSize);
    }


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is connected to
     *
     * @return metadata element describing the schema type associated with the requested parent element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeForElement(String parentElementGUID,
                                                     String parentElementTypeName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return apiManagerClient.getSchemaTypeForElement(userId, parentElementGUID, parentElementTypeName);
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement>   getSchemaTypeByName(String name,
                                                         String typeName,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return apiManagerClient.getSchemaTypeByName(userId, name, typeName, startFrom, pageSize);
    }


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeByGUID(String schemaTypeGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return apiManagerClient.getSchemaTypeByGUID(userId, schemaTypeGUID);
    }


    /**
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return header for parent element (data asset, process, port)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementHeader getSchemaTypeParent(String schemaTypeGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return apiManagerClient.getSchemaTypeParent(userId, schemaTypeGUID);
    }
}
