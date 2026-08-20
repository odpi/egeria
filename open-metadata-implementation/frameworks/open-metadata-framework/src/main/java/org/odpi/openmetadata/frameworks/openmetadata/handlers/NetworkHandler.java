/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.NetworkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.NetworkGatewayLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.NetworkGatewayProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.VisibleEndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * NetworkHandler provides methods to define networks and the network gateways that connect them, along with
 * their relationships.  Network is a type of IT infrastructure asset; NetworkGateway is a type of software
 * capability, so the principle type of this handler is Referenceable - their nearest common super type.  The
 * retrieval methods set the specific type that they are interested in.
 */
public class NetworkHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public NetworkHandler(String             localServerName,
                          AuditLog           auditLog,
                          String             localServiceName,
                          OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.REFERENCEABLE.typeName);
    }


    /**
     * Return query options that are scoped to the requested type.  The supplied options win if the caller has
     * already set a type name.
     *
     * @param suppliedQueryOptions options supplied by the caller
     * @param typeName type to scope the request to
     * @return query options
     */
    private QueryOptions getScopedQueryOptions(QueryOptions suppliedQueryOptions,
                                               String       typeName)
    {
        QueryOptions queryOptions = new QueryOptions(suppliedQueryOptions);

        if (queryOptions.getMetadataElementTypeName() == null)
        {
            queryOptions.setMetadataElementTypeName(typeName);
        }

        return queryOptions;
    }


    /**
     * Return search options that are scoped to the requested type.  The supplied options win if the caller has
     * already set a type name.
     *
     * @param suppliedSearchOptions options supplied by the caller
     * @param typeName type to scope the request to
     * @return search options
     */
    private SearchOptions getScopedSearchOptions(SearchOptions suppliedSearchOptions,
                                                 String        typeName)
    {
        SearchOptions searchOptions = new SearchOptions(suppliedSearchOptions);

        if (searchOptions.getMetadataElementTypeName() == null)
        {
            searchOptions.setMetadataElementTypeName(typeName);
        }

        return searchOptions;
    }


    /**
     * Return get options that are scoped to the requested type.  The supplied options win if the caller has
     * already set a type name.
     *
     * @param suppliedGetOptions options supplied by the caller
     * @param typeName type to scope the request to
     * @return get options
     */
    private GetOptions getScopedGetOptions(GetOptions suppliedGetOptions,
                                           String     typeName)
    {
        GetOptions getOptions = new GetOptions(suppliedGetOptions);

        if (getOptions.getMetadataElementTypeName() == null)
        {
            getOptions.setMetadataElementTypeName(typeName);
        }

        return getOptions;
    }


    /*
     * Networks
     */

    /**
     * Create a new network.
     *
     * @param userId                       userId of the user making the request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createNetwork(String                                userId,
                                NewElementOptions                     newElementOptions,
                                Map<String, ClassificationProperties> initialClassifications,
                                NetworkProperties                     properties,
                                RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        final String methodName = "createNetwork";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a network using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new network.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing element to copy
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createNetworkFromTemplate(String                                userId,
                                            TemplateOptions                       templateOptions,
                                            String                                templateGUID,
                                            EntityProperties                      replacementProperties,
                                            Map<String, ClassificationProperties> replacementClassifications,
                                            Map<String, String>                   placeholderProperties,
                                            RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               replacementClassifications,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of a network.
     *
     * @param userId                 userId of the user making the request.
     * @param networkGUID unique identifier of the network (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateNetwork(String            userId,
                                 String            networkGUID,
                                 UpdateOptions     updateOptions,
                                 NetworkProperties properties) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String methodName        = "updateNetwork";
        final String guidParameterName = "networkGUID";

        return super.updateElement(userId,
                                   networkGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Delete a network.
     *
     * @param userId                 userId of the user making the request.
     * @param networkGUID unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteNetwork(String        userId,
                              String        networkGUID,
                              DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName        = "deleteNetwork";
        final String guidParameterName = "networkGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(networkGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, networkGUID, deleteOptions);
    }


    /**
     * Returns the list of networks with a particular name.
     *
     * @param userId                 userId of the user making the request
     * @param name                   name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getNetworksByName(String       userId,
                                                           String       name,
                                                           QueryOptions queryOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "getNetworksByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           this.getScopedQueryOptions(queryOptions, OpenMetadataType.NETWORK.typeName),
                                           methodName);
    }


    /**
     * Return the properties of a specific network.
     *
     * @param userId                 userId of the user making the request
     * @param networkGUID unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getNetworkByGUID(String     userId,
                                                    String     networkGUID,
                                                    GetOptions getOptions) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "getNetworkByGUID";

        return super.getRootElementByGUID(userId,
                                          networkGUID,
                                          this.getScopedGetOptions(getOptions, OpenMetadataType.NETWORK.typeName),
                                          methodName);
    }


    /**
     * Retrieve the list of network metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findNetworks(String        userId,
                                                      String        searchString,
                                                      SearchOptions searchOptions) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName  = "findNetworks";

        return super.findRootElements(userId,
                                      searchString,
                                      this.getScopedSearchOptions(searchOptions, OpenMetadataType.NETWORK.typeName),
                                      methodName);
    }


    /*
     * Network gateways
     */

    /**
     * Create a new network gateway.
     *
     * @param userId                       userId of the user making the request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createNetworkGateway(String                                userId,
                                       NewElementOptions                     newElementOptions,
                                       Map<String, ClassificationProperties> initialClassifications,
                                       NetworkGatewayProperties              properties,
                                       RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                  PropertyServerException,
                                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "createNetworkGateway";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a network gateway using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new network gateway.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing element to copy
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createNetworkGatewayFromTemplate(String                                userId,
                                                   TemplateOptions                       templateOptions,
                                                   String                                templateGUID,
                                                   EntityProperties                      replacementProperties,
                                                   Map<String, ClassificationProperties> replacementClassifications,
                                                   Map<String, String>                   placeholderProperties,
                                                   RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                              UserNotAuthorizedException,
                                                                                                                              PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               replacementClassifications,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of a network gateway.
     *
     * @param userId                 userId of the user making the request.
     * @param networkGatewayGUID unique identifier of the network gateway (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateNetworkGateway(String                   userId,
                                        String                   networkGatewayGUID,
                                        UpdateOptions            updateOptions,
                                        NetworkGatewayProperties properties) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName        = "updateNetworkGateway";
        final String guidParameterName = "networkGatewayGUID";

        return super.updateElement(userId,
                                   networkGatewayGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Delete a network gateway.
     *
     * @param userId                 userId of the user making the request.
     * @param networkGatewayGUID unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteNetworkGateway(String        userId,
                                     String        networkGatewayGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName        = "deleteNetworkGateway";
        final String guidParameterName = "networkGatewayGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(networkGatewayGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, networkGatewayGUID, deleteOptions);
    }


    /**
     * Returns the list of network gateways with a particular name.
     *
     * @param userId                 userId of the user making the request
     * @param name                   name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getNetworkGatewaysByName(String       userId,
                                                                  String       name,
                                                                  QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName = "getNetworkGatewaysByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           this.getScopedQueryOptions(queryOptions, OpenMetadataType.NETWORK_GATEWAY.typeName),
                                           methodName);
    }


    /**
     * Return the properties of a specific network gateway.
     *
     * @param userId                 userId of the user making the request
     * @param networkGatewayGUID unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getNetworkGatewayByGUID(String     userId,
                                                           String     networkGatewayGUID,
                                                           GetOptions getOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName = "getNetworkGatewayByGUID";

        return super.getRootElementByGUID(userId,
                                          networkGatewayGUID,
                                          this.getScopedGetOptions(getOptions, OpenMetadataType.NETWORK_GATEWAY.typeName),
                                          methodName);
    }


    /**
     * Retrieve the list of network gateway metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findNetworkGateways(String        userId,
                                                             String        searchString,
                                                             SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName  = "findNetworkGateways";

        return super.findRootElements(userId,
                                      searchString,
                                      this.getScopedSearchOptions(searchOptions, OpenMetadataType.NETWORK_GATEWAY.typeName),
                                      methodName);
    }


    /*
     * VisibleEndpoint is a uni-link relationship - a particular endpoint is either visible in a network or it is not.
     */

    /**
     * Attach an endpoint to the network that it is visible in.
     *
     * @param userId                 userId of the user making the request
     * @param endpointGUID           unique identifier of the endpoint
     * @param networkGUID            unique identifier of the network
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkVisibleEndpoint(String                    userId,
                                    String                    endpointGUID,
                                    String                    networkGUID,
                                    MakeAnchorOptions         makeAnchorOptions,
                                    VisibleEndpointProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName            = "linkVisibleEndpoint";
        final String end1GUIDParameterName = "endpointGUID";
        final String end2GUIDParameterName = "networkGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(endpointGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(networkGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.VISIBLE_ENDPOINT.typeName,
                                                        endpointGUID,
                                                        networkGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an endpoint from the network that it was visible in.
     *
     * @param userId                 userId of the user making the request.
     * @param endpointGUID           unique identifier of the endpoint
     * @param networkGUID            unique identifier of the network
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachVisibleEndpoint(String        userId,
                                      String        endpointGUID,
                                      String        networkGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName            = "detachVisibleEndpoint";
        final String end1GUIDParameterName = "endpointGUID";
        final String end2GUIDParameterName = "networkGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(endpointGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(networkGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.VISIBLE_ENDPOINT.typeName,
                                                        endpointGUID,
                                                        networkGUID,
                                                        deleteOptions);
    }


    /*
     * NetworkGatewayLink is a multi-link relationship - the same gateway may connect to the same network more
     * than once, each connection with its own pair of endpoint addresses.  Each link therefore creates a new
     * relationship, and the update and delete operations work on the relationship's own unique identifier.
     */

    /**
     * Attach a network gateway to a network that it connects to.  This always creates a new relationship.
     *
     * @param userId                 userId of the user making the request
     * @param networkGatewayGUID     unique identifier of the network gateway
     * @param networkGUID            unique identifier of the network
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String linkNetworkGateway(String                       userId,
                                     String                       networkGatewayGUID,
                                     String                       networkGUID,
                                     MakeAnchorOptions            makeAnchorOptions,
                                     NetworkGatewayLinkProperties relationshipProperties) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String methodName            = "linkNetworkGateway";
        final String end1GUIDParameterName = "networkGatewayGUID";
        final String end2GUIDParameterName = "networkGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(networkGatewayGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(networkGUID, end2GUIDParameterName, methodName);

        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               OpenMetadataType.NETWORK_GATEWAY_LINK_RELATIONSHIP.typeName,
                                                               networkGatewayGUID,
                                                               networkGUID,
                                                               makeAnchorOptions,
                                                               relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Update the properties of a network gateway link.
     *
     * @param userId                 userId of the user making the request
     * @param networkGatewayLinkGUID unique identifier of the network gateway link relationship
     * @param updateOptions provides a structure for the additional options when updating a relationship.
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateNetworkGatewayLink(String                       userId,
                                         String                       networkGatewayLinkGUID,
                                         UpdateOptions                updateOptions,
                                         NetworkGatewayLinkProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName        = "updateNetworkGatewayLink";
        final String guidParameterName = "networkGatewayLinkGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(networkGatewayLinkGUID, guidParameterName, methodName);

        openMetadataClient.updateRelationshipInStore(userId,
                                                     networkGatewayLinkGUID,
                                                     updateOptions,
                                                     relationshipBuilder.getElementProperties(relationshipProperties));
    }


    /**
     * Detach a network gateway from a network that it connected to.
     *
     * @param userId                 userId of the user making the request.
     * @param networkGatewayLinkGUID unique identifier of the network gateway link relationship
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNetworkGateway(String        userId,
                                     String        networkGatewayLinkGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName        = "detachNetworkGateway";
        final String guidParameterName = "networkGatewayLinkGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(networkGatewayLinkGUID, guidParameterName, methodName);

        openMetadataClient.deleteRelationshipInStore(userId, networkGatewayLinkGUID, deleteOptions);
    }
}
