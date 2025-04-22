/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.api.connector;

import org.odpi.openmetadata.accessservices.datamanager.api.DataManagerEventListener;
import org.odpi.openmetadata.accessservices.datamanager.client.APIManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.ConnectionManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.DataManagerEventClient;
import org.odpi.openmetadata.accessservices.datamanager.client.ValidValueManagement;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.client.ActionControlInterface;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.apis.APIProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIOperationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIParameterListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.APIParameterListType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIParameterProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ReferenceValueAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;

import java.util.List;


/**
 * APIIntegratorContext is the context for managing resources from a relational api server.
 */
public class APIIntegratorContext extends IntegrationContext
{
    private final APIManagerClient        apiManagerClient;
    private final ConnectionManagerClient connectionManagerClient;
    private final DataManagerEventClient eventClient;
    private final ValidValueManagement   validValueManagement;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param serverName name of the integration daemon
     * @param openIntegrationClient client for calling the metadata server
     * @param governanceConfiguration client for managing catalog targets
     * @param openMetadataStoreClient client for calling the metadata server
     * @param actionControlInterface client for initiating governance actions
     * @param apiManagerClient client to map request to
     * @param connectionManagerClient client to manage connections in the metadata server
     * @param validValueManagement client for managing valid value sets and definitions
     * @param eventClient client to register for events
     * @param generateIntegrationReport should an integration report be generated?
     * @param permittedSynchronization the direction of integration permitted by the integration connector
     * @param integrationConnectorGUID unique identifier for the integration connector if it is started via an integration group (otherwise it is
     *                                 null).
     * @param externalSourceGUID unique identifier of the software server capability for the api manager
     * @param externalSourceName unique name of the software server capability for the api manager
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public APIIntegratorContext(String                       connectorId,
                                String                       connectorName,
                                String                       connectorUserId,
                                String                       serverName,
                                OpenIntegrationClient        openIntegrationClient,
                                GovernanceConfiguration      governanceConfiguration,
                                OpenMetadataClient           openMetadataStoreClient,
                                ActionControlInterface       actionControlInterface,
                                APIManagerClient             apiManagerClient,
                                ConnectionManagerClient      connectionManagerClient,
                                ValidValueManagement         validValueManagement,
                                DataManagerEventClient       eventClient,
                                boolean                      generateIntegrationReport,
                                PermittedSynchronization     permittedSynchronization,
                                String                       integrationConnectorGUID,
                                String                       externalSourceGUID,
                                String                       externalSourceName,
                                AuditLog                     auditLog,
                                int                          maxPageSize)
    {
        super(connectorId,
              connectorName,
              connectorUserId,
              serverName,
              openIntegrationClient,
              governanceConfiguration,
              openMetadataStoreClient,
              actionControlInterface,
              generateIntegrationReport,
              permittedSynchronization,
              externalSourceGUID,
              externalSourceName,
              integrationConnectorGUID,
              auditLog,
              maxPageSize);

        this.apiManagerClient        = apiManagerClient;
        this.connectionManagerClient = connectionManagerClient;
        this.validValueManagement    = validValueManagement;
        this.eventClient             = eventClient;
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
        return externalSourceName;
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
        this.externalSourceIsHome = apiManagerIsHome;
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
        String endpointGUID =  connectionManagerClient.createEndpoint(userId, null, null, endpointProperties);

        if ((endpointGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(endpointGUID);
        }

        return endpointGUID;
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
        String endpointGUID = connectionManagerClient.createEndpointFromTemplate(userId,
                                                                                 null,
                                                                                 null,
                                                                                 networkAddress,
                                                                                 templateGUID,
                                                                                 templateProperties);

        if ((endpointGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(endpointGUID);
        }

        return endpointGUID;
    }


    /**
     * Update the metadata element representing an endpoint.  It is possible to use the subtype property classes or
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
        connectionManagerClient.updateEndpoint(userId, externalSourceGUID, externalSourceName, isMergeUpdate, endpointGUID, endpointProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(endpointGUID);
        }
    }


    /**
     * Remove the metadata element representing an endpoint.
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
        connectionManagerClient.removeEndpoint(userId, externalSourceGUID, externalSourceName, endpointGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(endpointGUID);
        }
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
        String apiGUID = apiManagerClient.createAPI(userId,
                                                    externalSourceGUID,
                                                    externalSourceName,
                                                    externalSourceIsHome,
                                                    endpointGUID,
                                                    apiProperties);

        if ((apiGUID != null) && (integrationReportWriter != null))
        {
            if ((apiProperties != null) && (apiProperties.getTypeName() != null))
            {
                integrationReportWriter.reportElementCreation(apiGUID);
            }
            else
            {
                integrationReportWriter.reportElementCreation(apiGUID);
            }
        }

        return apiGUID;
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
        String apiGUID = apiManagerClient.createAPIFromTemplate(userId,
                                                                externalSourceGUID,
                                                                externalSourceName,
                                                                externalSourceIsHome,
                                                                endpointGUID,
                                                                templateGUID,
                                                                templateProperties);

        if ((apiGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(apiGUID);
        }

        return apiGUID;
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
        apiManagerClient.updateAPI(userId, externalSourceGUID, externalSourceName, apiGUID, isMergeUpdate, apiProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(apiGUID);
        }
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

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(apiGUID);
        }
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

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(apiGUID);
        }
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
        apiManagerClient.removeAPI(userId, externalSourceGUID, externalSourceName, apiGUID, qualifiedName);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(apiGUID);
        }
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
        return apiManagerClient.getAPIsForAPIManager(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     startFrom,
                                                     pageSize);
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
        String apiOperationGUID;
        if (externalSourceIsHome)
        {
            apiOperationGUID =  apiManagerClient.createAPIOperation(userId, externalSourceGUID, externalSourceName, apiGUID, apiOperationProperties);
        }
        else
        {
            apiOperationGUID = apiManagerClient.createAPIOperation(userId, null, null, apiGUID, apiOperationProperties);
        }

        if ((apiOperationGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(apiOperationGUID);
        }

        return apiOperationGUID;
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
        String apiOperationGUID;
        if (externalSourceIsHome)
        {
            apiOperationGUID = apiManagerClient.createAPIOperationFromTemplate(userId,
                                                                               externalSourceGUID,
                                                                               externalSourceName,
                                                                               templateGUID,
                                                                               apiGUID,
                                                                               templateProperties);
        }
        else
        {
            apiOperationGUID = apiManagerClient.createAPIOperationFromTemplate(userId,
                                                                               null,
                                                                               null,
                                                                               templateGUID,
                                                                               apiGUID,
                                                                               templateProperties);
        }

        if ((apiOperationGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(apiOperationGUID);
        }

        return apiOperationGUID;
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
        apiManagerClient.updateAPIOperation(userId, externalSourceGUID, externalSourceName, apiOperationGUID, isMergeUpdate, apiOperationProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(apiOperationGUID);
        }
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
        apiManagerClient.removeAPIOperation(userId, externalSourceGUID, externalSourceName, apiOperationGUID, qualifiedName);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(apiOperationGUID);
        }
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
        String apiParameterListGUID;
        if (externalSourceIsHome)
        {
            apiParameterListGUID = apiManagerClient.createAPIParameterList(userId,
                                                                           externalSourceGUID,
                                                                           externalSourceName,
                                                                           apiOperationGUID,
                                                                           parameterListType,
                                                                           properties);
        }
        else
        {
            apiParameterListGUID = apiManagerClient.createAPIParameterList(userId,
                                                                           null,
                                                                           null,
                                                                           apiOperationGUID,
                                                                           parameterListType,
                                                                           properties);
        }

        return apiParameterListGUID;
    }


    /**
     * Create a new metadata element to represent an API Parameter List using an existing API Parameter List as a template.
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
        String apiParameterListGUID;

        if (externalSourceIsHome)
        {
            apiParameterListGUID = apiManagerClient.createAPIParameterListFromTemplate(userId,
                                                                                       externalSourceGUID,
                                                                                       externalSourceName,
                                                                                       templateGUID,
                                                                                       apiOperationGUID,
                                                                                       parameterListType,
                                                                                       templateProperties);
        }
        else
        {
            apiParameterListGUID = apiManagerClient.createAPIParameterListFromTemplate(userId,
                                                                                       null,
                                                                                       null,
                                                                                       templateGUID,
                                                                                       apiOperationGUID,
                                                                                       parameterListType,
                                                                                       templateProperties);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(apiParameterListGUID);
        }

        return apiParameterListGUID;
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
        apiManagerClient.updateAPIParameterList(userId, externalSourceGUID, externalSourceName, apiParameterListGUID, isMergeUpdate, properties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(apiParameterListGUID);
        }
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
        apiManagerClient.removeAPIParameterList(userId, externalSourceGUID, externalSourceName, apiParameterListGUID, qualifiedName);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(apiParameterListGUID);
        }
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
        String apiParameterGUID;

        if (externalSourceIsHome)
        {
            apiParameterGUID =  apiManagerClient.createAPIParameter(userId,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    schemaElementGUID,
                                                                    apiParameterProperties);
        }
        else
        {
            apiParameterGUID = apiManagerClient.createAPIParameter(userId,
                                                                   null,
                                                                   null,
                                                                   schemaElementGUID,
                                                                   apiParameterProperties);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(apiParameterGUID);
        }

        return apiParameterGUID;
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
        String apiParameterGUID;

        if (externalSourceIsHome)
        {
            apiParameterGUID = apiManagerClient.createAPIParameterFromTemplate(userId,
                                                                               externalSourceGUID,
                                                                               externalSourceName,
                                                                               schemaElementGUID,
                                                                               templateGUID,
                                                                               templateProperties);
        }
        else
        {
            apiParameterGUID = apiManagerClient.createAPIParameterFromTemplate(userId,
                                                                               null,
                                                                               null,
                                                                               schemaElementGUID,
                                                                               templateGUID,
                                                                               templateProperties);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(apiParameterGUID);
        }

        return apiParameterGUID;
    }


    /**
     * Connect a schema type to an API parameter.
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
        if (externalSourceIsHome)
        {
            apiManagerClient.setupSchemaType(userId, externalSourceGUID, externalSourceName, relationshipTypeName, apiParameterGUID, schemaTypeGUID);
        }
        else
        {
            apiManagerClient.setupSchemaType(userId, null, null, relationshipTypeName, apiParameterGUID, schemaTypeGUID);
        }
    }


    /**
     * Remove the linked schema types from an API parameter.
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
        apiManagerClient.clearSchemaTypes(userId, externalSourceGUID, externalSourceName, apiParameterGUID);
    }


    /**
     * Update the properties of the metadata element representing an API parameter.
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
        apiManagerClient.updateAPIParameter(userId, externalSourceGUID, externalSourceName, apiParameterGUID, isMergeUpdate, apiParameterProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(apiParameterGUID);
        }
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
        apiManagerClient.removeAPIParameter(userId, externalSourceGUID, externalSourceName, apiParameterGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(apiParameterGUID);
        }
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
        String schemaTypeGUID;
        if (externalSourceIsHome)
        {
            schemaTypeGUID = apiManagerClient.createPrimitiveSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeProperties);
        }
        else
        {
            schemaTypeGUID = apiManagerClient.createPrimitiveSchemaType(userId, null, null, schemaTypeProperties);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
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
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = apiManagerClient.createLiteralSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeProperties);
        }
        else
        {
            schemaTypeGUID = apiManagerClient.createLiteralSchemaType(userId, null, null, schemaTypeProperties);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
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
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = apiManagerClient.createEnumSchemaType(userId,
                                                                   externalSourceGUID,
                                                                   externalSourceName,
                                                                   schemaTypeProperties,
                                                                   validValuesSetGUID);
        }
        else
        {
            schemaTypeGUID = apiManagerClient.createEnumSchemaType(userId,
                                                                   null,
                                                                   null,
                                                                   schemaTypeProperties,
                                                                   validValuesSetGUID);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
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
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = apiManagerClient.createStructSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeProperties);
        }
        else
        {
            schemaTypeGUID =  apiManagerClient.createStructSchemaType(userId, null, null, schemaTypeProperties);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
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
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = apiManagerClient.createSchemaTypeChoice(userId, externalSourceGUID, externalSourceName, schemaTypeProperties,
                                                                     schemaTypeOptionGUIDs);
        }
        else
        {
            schemaTypeGUID = apiManagerClient.createSchemaTypeChoice(userId, null, null, schemaTypeProperties, schemaTypeOptionGUIDs);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
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
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = apiManagerClient.createMapSchemaType(userId,
                                                                  externalSourceGUID,
                                                                  externalSourceName,
                                                                  schemaTypeProperties,
                                                                  mapFromSchemaTypeGUID,
                                                                  mapToSchemaTypeGUID);
        }
        else
        {
            schemaTypeGUID = apiManagerClient.createMapSchemaType(userId,
                                                                  null,
                                                                  null,
                                                                  schemaTypeProperties,
                                                                  mapFromSchemaTypeGUID,
                                                                  mapToSchemaTypeGUID);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
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
        String schemaTypeGUID;

        if (externalSourceIsHome)
        {
            schemaTypeGUID = apiManagerClient.createSchemaTypeFromTemplate(userId,
                                                                           externalSourceGUID,
                                                                           externalSourceName,
                                                                           templateGUID,
                                                                           templateProperties);
        }
        else
        {
            schemaTypeGUID = apiManagerClient.createSchemaTypeFromTemplate(userId,
                                                                           null,
                                                                           null,
                                                                           templateGUID,
                                                                           templateProperties);
        }

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(schemaTypeGUID);
        }

        return schemaTypeGUID;
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
        apiManagerClient.updateSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeGUID, isMergeUpdate, schemaTypeProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(schemaTypeGUID);
        }
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
        apiManagerClient.removeSchemaType(userId, externalSourceGUID, externalSourceName, schemaTypeGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(schemaTypeGUID);
        }
    }


    /**
     * Create a relationship between two schema elements.  The name of the desired relationship, and any properties (including effectivity dates)
     * are passed on the API.
     *
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to create
     * @param properties relationship properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSchemaElementRelationship(String                 endOneGUID,
                                               String                 endTwoGUID,
                                               String                 relationshipTypeName,
                                               RelationshipProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        apiManagerClient.setupSchemaElementRelationship(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        endOneGUID,
                                                        endTwoGUID,
                                                        relationshipTypeName,
                                                        properties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(endOneGUID);
        }
    }


    /**
     * Remove a relationship between two schema elements.  The name of the desired relationship is passed on the API.
     *
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to delete
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaElementRelationship(String endOneGUID,
                                               String endTwoGUID,
                                               String relationshipTypeName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        apiManagerClient.clearSchemaElementRelationship(userId, externalSourceGUID, externalSourceName, endOneGUID, endTwoGUID, relationshipTypeName);
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

    /* =====================================================================================================================
     * A ValidValue is the top level object for working with valid values
     */

    /**
     * Create a new metadata element to represent a valid value.
     *
     * @param validValueProperties properties about the valid value to store
     *
     * @return unique identifier of the new valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createValidValue(ValidValueProperties validValueProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        String validValueGUID = validValueManagement.createValidValue(userId, externalSourceGUID, externalSourceName, validValueProperties);

        if ((validValueGUID != null) && (integrationReportWriter != null))
        {
            integrationReportWriter.reportElementCreation(validValueGUID);
        }

        return validValueGUID;
    }


    /**
     * Update the metadata element representing a valid value.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param validValueGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param validValueProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateValidValue(String               validValueGUID,
                                 boolean              isMergeUpdate,
                                 ValidValueProperties validValueProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        validValueManagement.updateValidValue(userId, externalSourceGUID, externalSourceName, validValueGUID, isMergeUpdate, validValueProperties);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(validValueGUID);
        }
    }


    /**
     * Create a membership relationship between a validValue and a validValueSet that it belongs to.
     *
     * @param validValueSetGUID unique identifier of the valid value set
     * @param properties describes the properties of the membership
     * @param validValueMemberGUID unique identifier of the member
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupValidValueMember(String                         validValueSetGUID,
                                      ValidValueMembershipProperties properties,
                                      String                         validValueMemberGUID) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        validValueManagement.setupValidValueMember(userId, externalSourceGUID, externalSourceName, validValueSetGUID, properties, validValueMemberGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(validValueSetGUID);
        }
    }


    /**
     * Remove a membership relationship between a validValue and a validValueSet that it belongs to.
     *
     * @param validValueSetGUID unique identifier of the valid value set
     * @param validValueMemberGUID unique identifier of the member
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearValidValueMember(String validValueSetGUID,
                                      String validValueMemberGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        validValueManagement.clearValidValueMember(userId, externalSourceGUID, externalSourceName, validValueSetGUID, validValueMemberGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(validValueSetGUID);
        }
    }


    /**
     * Create a valid value assignment relationship between an element and a valid value (typically, a valid value set) to show that
     * the valid value defines the values that can be stored in the data item that the element represents.
     *
     * @param elementGUID unique identifier of the element
     * @param properties describes the permissions that the role has in the validValue
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupValidValues(String                         elementGUID,
                                 ValidValueAssignmentProperties properties,
                                 String                         validValueGUID) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        validValueManagement.setupValidValues(userId, externalSourceGUID, externalSourceName, elementGUID, properties, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Remove a valid value assignment relationship between an element and a valid value.
     *
     * @param elementGUID unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearValidValues(String elementGUID,
                                 String validValueGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        validValueManagement.clearValidValues(userId, externalSourceGUID, externalSourceName, elementGUID, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Create a reference value assignment relationship between an element and a valid value to show that
     * the valid value is a semiformal tag/classification.
     *
     * @param elementGUID unique identifier of the element
     * @param properties describes the quality of the assignment
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupReferenceValueTag(String                             elementGUID,
                                       ReferenceValueAssignmentProperties properties,
                                       String                             validValueGUID) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        validValueManagement.setupReferenceValueTag(userId, externalSourceGUID, externalSourceName, elementGUID, properties, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Remove a reference value assignment relationship between an element and a valid value.
     *
     * @param elementGUID unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearReferenceValueTag(String elementGUID,
                                       String validValueGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        validValueManagement.clearReferenceValueTag(userId, externalSourceGUID, externalSourceName, elementGUID, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Remove the metadata element representing a valid value.
     *
     * @param validValueGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeValidValue(String validValueGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        validValueManagement.removeValidValue(userId, externalSourceGUID, externalSourceName, validValueGUID);

        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(validValueGUID);
        }
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
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
    public List<ValidValueElement> findValidValues(String searchString,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return validValueManagement.findValidValues(userId, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
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
    public List<ValidValueElement> getValidValuesByName(String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return validValueManagement.getValidValuesByName(userId, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of valid values.
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
    public List<ValidValueElement> getAllValidValues(int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return validValueManagement.getAllValidValues(userId, startFrom, pageSize);
    }


    /**
     * Page through the members of a valid value set.
     *
     * @param validValueSetGUID          unique identifier of the valid value set
     * @param startFrom                  paging starting point
     * @param pageSize                   maximum number of return values.
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<ValidValueElement> getValidValueSetMembers(String  validValueSetGUID,
                                                           int     startFrom,
                                                           int     pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return validValueManagement.getValidValueSetMembers(userId, validValueSetGUID, startFrom, pageSize);
    }


    /**
     * Page through the list of valid value sets that a valid value definition/set belongs to.
     *
     * @param validValueGUID          unique identifier of valid value to query
     * @param startFrom               paging starting point
     * @param pageSize                maximum number of return values.
     * @return list of valid value beans
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<ValidValueElement> getSetsForValidValue(String  validValueGUID,
                                                        int     startFrom,
                                                        int     pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return validValueManagement.getSetsForValidValue(userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Return information about the valid value set linked to an element as its set of valid values.
     *
     * @param elementGUID unique identifier for the element using the valid value set
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public ValidValueElement getValidValuesForConsumer(String elementGUID) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return validValueManagement.getValidValuesForConsumer(userId, elementGUID);
    }


    /**
     * Return information about the consumers linked to a validValue.
     *
     * @param validValueGUID unique identifier for the validValue
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<RelatedElementStub> getConsumersOfValidValue(String validValueGUID,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return validValueManagement.getConsumersOfValidValue(userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Return information about the valid values linked as reference value tags to an element.
     *
     * @param elementGUID unique identifier for the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of valid values
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<ValidValueElement> getReferenceValues(String elementGUID,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return validValueManagement.getReferenceValues(userId, elementGUID, startFrom, pageSize);
    }


    /**
     * Return information about the person roles linked to a validValue.
     *
     * @param validValueGUID unique identifier for the validValue
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException guid is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<RelatedElementStub> getAssigneesOfReferenceValue(String validValueGUID,
                                                                 int    startFrom,
                                                                 int    pageSize) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return validValueManagement.getAssigneesOfReferenceValue(userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param validValueGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ValidValueElement getValidValueByGUID(String validValueGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return validValueManagement.getValidValueByGUID(userId, validValueGUID);
    }
}
