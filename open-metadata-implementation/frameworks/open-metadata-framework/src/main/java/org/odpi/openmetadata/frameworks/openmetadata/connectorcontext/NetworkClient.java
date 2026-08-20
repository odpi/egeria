/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.NetworkHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.NetworkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.NetworkGatewayLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.NetworkGatewayProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.VisibleEndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with networks and the network gateways that connect them.
 */
public class NetworkClient extends ConnectorContextClientBase
{
    private final NetworkHandler networkHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public NetworkClient(ConnectorContextBase     parentContext,
                         String                   localServerName,
                         String                   localServiceName,
                         String                   connectorUserId,
                         String                   connectorGUID,
                         String                   externalSourceGUID,
                         String                   externalSourceName,
                         OpenMetadataClient       openMetadataClient,
                         AuditLog                 auditLog,
                         int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.networkHandler = new NetworkHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new network.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createNetwork(NewElementOptions                     newElementOptions,
                              Map<String, ClassificationProperties> initialClassifications,
                              NetworkProperties             properties,
                              RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        String elementGUID = networkHandler.createNetwork(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a network using an existing element as a template.
     *
     * @param templateOptions              details of the element to create
     * @param templateGUID                 the unique identifier of the existing element to copy
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createNetworkFromTemplate(TemplateOptions                       templateOptions,
                                          String                                templateGUID,
                                          EntityProperties                      replacementProperties,
                                          Map<String, ClassificationProperties> replacementClassifications,
                                          Map<String, String>                   placeholderProperties,
                                          RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        String elementGUID = networkHandler.createNetworkFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, replacementClassifications, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a network.
     *
     * @param networkGUID unique identifier of the network (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateNetwork(String        networkGUID,
                               UpdateOptions updateOptions,
                               NetworkProperties             properties) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        boolean updateOccurred = networkHandler.updateNetwork(connectorUserId, networkGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getActivityReportWriter() != null))
        {
            parentContext.getActivityReportWriter().reportElementUpdate(networkGUID);
        }

        return updateOccurred;
    }


    /**
     * Delete a network.
     *
     * @param networkGUID unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteNetwork(String        networkGUID,
                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        networkHandler.deleteNetwork(connectorUserId, networkGUID, deleteOptions);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementDelete(networkGUID);
        }
    }


    /**
     * Returns the list of networks with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getNetworksByName(String       name,
                                                         QueryOptions queryOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        return networkHandler.getNetworksByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific network.
     *
     * @param networkGUID unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getNetworkByGUID(String     networkGUID,
                                                  GetOptions getOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return networkHandler.getNetworkByGUID(connectorUserId, networkGUID, getOptions);
    }


    /**
     * Retrieve the list of network metadata elements that contain the search string.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findNetworks(String        searchString,
                                                    SearchOptions searchOptions) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return networkHandler.findNetworks(connectorUserId, searchString, searchOptions);
    }


    /**
     * Create a new network gateway.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createNetworkGateway(NewElementOptions                     newElementOptions,
                              Map<String, ClassificationProperties> initialClassifications,
                              NetworkGatewayProperties      properties,
                              RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        String elementGUID = networkHandler.createNetworkGateway(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a network gateway using an existing element as a template.
     *
     * @param templateOptions              details of the element to create
     * @param templateGUID                 the unique identifier of the existing element to copy
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createNetworkGatewayFromTemplate(TemplateOptions                       templateOptions,
                                          String                                templateGUID,
                                          EntityProperties                      replacementProperties,
                                          Map<String, ClassificationProperties> replacementClassifications,
                                          Map<String, String>                   placeholderProperties,
                                          RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        String elementGUID = networkHandler.createNetworkGatewayFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, replacementClassifications, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a network gateway.
     *
     * @param networkGatewayGUID unique identifier of the network gateway (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateNetworkGateway(String        networkGatewayGUID,
                               UpdateOptions updateOptions,
                               NetworkGatewayProperties      properties) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        boolean updateOccurred = networkHandler.updateNetworkGateway(connectorUserId, networkGatewayGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getActivityReportWriter() != null))
        {
            parentContext.getActivityReportWriter().reportElementUpdate(networkGatewayGUID);
        }

        return updateOccurred;
    }


    /**
     * Delete a network gateway.
     *
     * @param networkGatewayGUID unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteNetworkGateway(String        networkGatewayGUID,
                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        networkHandler.deleteNetworkGateway(connectorUserId, networkGatewayGUID, deleteOptions);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementDelete(networkGatewayGUID);
        }
    }


    /**
     * Returns the list of network gateways with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getNetworkGatewaysByName(String       name,
                                                         QueryOptions queryOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        return networkHandler.getNetworkGatewaysByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific network gateway.
     *
     * @param networkGatewayGUID unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getNetworkGatewayByGUID(String     networkGatewayGUID,
                                                  GetOptions getOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return networkHandler.getNetworkGatewayByGUID(connectorUserId, networkGatewayGUID, getOptions);
    }


    /**
     * Retrieve the list of network gateway metadata elements that contain the search string.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findNetworkGateways(String        searchString,
                                                    SearchOptions searchOptions) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return networkHandler.findNetworkGateways(connectorUserId, searchString, searchOptions);
    }


    /**
     * Attach an endpoint to the network that it is visible in.
     *
     * @param endpointGUID           unique identifier of the endpoint
     * @param networkGUID            unique identifier of the network
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkVisibleEndpoint(String                    endpointGUID,
                                    String                    networkGUID,
                                    MakeAnchorOptions         makeAnchorOptions,
                                    VisibleEndpointProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        networkHandler.linkVisibleEndpoint(connectorUserId, endpointGUID, networkGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach an endpoint from the network that it was visible in.
     *
     * @param endpointGUID           unique identifier of the endpoint
     * @param networkGUID            unique identifier of the network
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachVisibleEndpoint(String        endpointGUID,
                                      String        networkGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        networkHandler.detachVisibleEndpoint(connectorUserId, endpointGUID, networkGUID, deleteOptions);
    }


    /**
     * Attach a network gateway to a network that it connects to.  NetworkGatewayLink is a multi-link
     * relationship so this always creates a new relationship and returns its unique identifier.
     *
     * @param networkGatewayGUID     unique identifier of the network gateway
     * @param networkGUID            unique identifier of the network
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String linkNetworkGateway(String                       networkGatewayGUID,
                                     String                       networkGUID,
                                     MakeAnchorOptions            makeAnchorOptions,
                                     NetworkGatewayLinkProperties relationshipProperties) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        return networkHandler.linkNetworkGateway(connectorUserId, networkGatewayGUID, networkGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Update the properties of a network gateway link.
     *
     * @param networkGatewayLinkGUID unique identifier of the network gateway link relationship
     * @param updateOptions provides a structure for the additional options when updating a relationship.
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateNetworkGatewayLink(String                       networkGatewayLinkGUID,
                                         UpdateOptions                updateOptions,
                                         NetworkGatewayLinkProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        networkHandler.updateNetworkGatewayLink(connectorUserId, networkGatewayLinkGUID, updateOptions, relationshipProperties);
    }


    /**
     * Detach a network gateway from a network that it connected to.
     *
     * @param networkGatewayLinkGUID unique identifier of the network gateway link relationship
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNetworkGateway(String        networkGatewayLinkGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        networkHandler.detachNetworkGateway(connectorUserId, networkGatewayLinkGUID, deleteOptions);
    }
}
