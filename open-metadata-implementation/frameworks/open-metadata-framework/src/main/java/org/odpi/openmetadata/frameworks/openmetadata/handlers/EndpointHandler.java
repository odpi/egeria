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
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ServerEndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * EndpointHandler describes how to maintain and query endpoints.
 */
public class EndpointHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public EndpointHandler(String             localServerName,
                           AuditLog           auditLog,
                           String             serviceName,
                           OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName,openMetadataClient, OpenMetadataType.ENDPOINT.typeName);
    }


    /**
     * Create a new endpoint.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createEndpoint(String                                userId,
                                 NewElementOptions                     newElementOptions,
                                 Map<String, ClassificationProperties> initialClassifications,
                                 EndpointProperties                    properties,
                                 RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        final String methodName = "createEndpoint";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent an endpoint using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new endpoint.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpointFromTemplate(String                 userId,
                                             TemplateOptions        templateOptions,
                                             String                 templateGUID,
                                             ElementProperties      replacementProperties,
                                             Map<String, String>    placeholderProperties,
                                             RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of an endpoint.
     *
     * @param userId                 userId of user making request.
     * @param endpointGUID      unique identifier of the endpoint (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateEndpoint(String             userId,
                               String             endpointGUID,
                               UpdateOptions      updateOptions,
                               EndpointProperties properties) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "updateEndpoint";
        final String guidParameterName = "endpointGUID";

        super.updateElement(userId,
                            endpointGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Attach an endpoint to an infrastructure asset.
     *
     * @param userId                  userId of user making request
     * @param endpointGUID            unique identifier of the endpoint
     * @param itAssetGUID             unique identifier of the infrastructure asset
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkEndpointToITAsset(String                   userId,
                                      String                   itAssetGUID,
                                      String                   endpointGUID,
                                      MetadataSourceOptions    metadataSourceOptions,
                                      ServerEndpointProperties relationshipProperties) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "linkEndpointToITAsset";
        final String end2GUIDParameterName = "endpointGUID";
        final String end1GUIDParameterName = "itAssetGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(endpointGUID, end2GUIDParameterName, methodName);
        propertyHelper.validateGUID(itAssetGUID, end1GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName,
                                                        itAssetGUID,
                                                        endpointGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an endpoint from an infrastructure asset.
     *
     * @param userId                 userId of user making request.
     * @param itAssetGUID            unique identifier of the infrastructure asset
     * @param endpointGUID       unique identifier of the endpoint
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachEndpointFromITAsset(String        userId,
                                          String        itAssetGUID,
                                          String        endpointGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "detachEndpointFromITAsset";

        final String end2GUIDParameterName = "endpointGUID";
        final String end1GUIDParameterName = "itAssetGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(endpointGUID, end2GUIDParameterName, methodName);
        propertyHelper.validateGUID(itAssetGUID, end1GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName,
                                                        itAssetGUID,
                                                        endpointGUID,
                                                        deleteOptions);
    }


    /**
     * Delete an endpoint.
     *
     * @param userId                 userId of user making request.
     * @param endpointGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteEndpoint(String        userId,
                               String        endpointGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName = "deleteEndpoint";
        final String guidParameterName = "endpointGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(endpointGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, endpointGUID, deleteOptions);
    }


    /**
     * Returns the list of endpoints with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getEndpointsByName(String       userId,
                                                            String       name,
                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "getEndpointsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.USER_ID.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.NETWORK_ADDRESS.name,
                                                   OpenMetadataProperty.DISTINGUISHED_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific endpoint.
     *
     * @param userId                 userId of user making request
     * @param endpointGUID      unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getEndpointByGUID(String     userId,
                                                     String     endpointGUID,
                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "getEndpointByGUID";

        return super.getRootElementByGUID(userId, endpointGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of endpoint metadata elements with a matching networkAddress.
     * There are no wildcards supported on this request.
     *
     * @param userId         userId of user making request
     * @param networkAddress networkAddress to search for
     * @param queryOptions           multiple options to control the query
     *
     * @return a list of elements
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getEndpointsByNetworkAddress(String       userId,
                                                                      String       networkAddress,
                                                                      QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName = "getEndpointsByNetworkAddress";
        final String nameParameterName = "networkAddress";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(networkAddress, nameParameterName, methodName);

        List<String> propertyNames = Collections.singletonList(OpenMetadataProperty.NETWORK_ADDRESS.name);

        return super.getRootElementsByName(userId, networkAddress, propertyNames, queryOptions, methodName);
    }


    /**
     * Retrieve the list of endpoint metadata elements that are attached to a specific infrastructure element.
     *
     * @param userId         userId of user making request
     * @param infrastructureGUID element to search for
     * @param queryOptions multiple options to control the query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> getEndpointsForInfrastructure(String       userId,
                                                                       String       infrastructureGUID,
                                                                       QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "getEndpointsForInfrastructure";
        final String parentGUIDParameterName = "infrastructureGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(infrastructureGUID, parentGUIDParameterName, methodName);

        return super.getRelatedRootElements(userId,
                                            infrastructureGUID,
                                            parentGUIDParameterName,
                                            1,
                                            OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Retrieve the list of endpoints metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findEndpoints(String        userId,
                                                       String        searchString,
                                                       SearchOptions searchOptions) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "findEndpoints";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
