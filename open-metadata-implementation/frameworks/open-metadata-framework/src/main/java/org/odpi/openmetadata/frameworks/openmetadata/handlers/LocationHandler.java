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

import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.AdjacentLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.KnownLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.LocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.NestedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * LocationHandler provides methods to define locations and their relationships.
 */
public class LocationHandler extends OpenMetadataHandlerBase
{

    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public LocationHandler(String             localServerName,
                           AuditLog           auditLog,
                           String             localServiceName,
                           OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.LOCATION.typeName);
    }


    /**
     * Create a new location.
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
    public String createLocation(String                                userId,
                                 NewElementOptions                     newElementOptions,
                                 Map<String, ClassificationProperties> initialClassifications,
                                 LocationProperties                    properties,
                                 RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        final String methodName = "createLocation";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a location using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new location.
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
    public String createLocationFromTemplate(String                 userId,
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
     * Update the properties of a location.
     *
     * @param userId                 userId of user making request.
     * @param locationGUID       unique identifier of the location (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateLocation(String             userId,
                               String             locationGUID,
                               UpdateOptions      updateOptions,
                               LocationProperties properties) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName        = "updateLocation";
        final String guidParameterName = "locationGUID";

        super.updateElement(userId,
                            locationGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }



    /**
     * Attach a location to one of its peers.
     *
     * @param userId                 userId of user making request
     * @param locationOneGUID          unique identifier of the first location
     * @param locationTwoGUID          unique identifier of the second location
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPeerLocations(String                     userId,
                                  String                     locationOneGUID,
                                  String                     locationTwoGUID,
                                  MetadataSourceOptions      metadataSourceOptions,
                                  AdjacentLocationProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName            = "linkPeerLocations";
        final String end1GUIDParameterName = "locationOneGUID";
        final String end2GUIDParameterName = "locationTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(locationOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(locationTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ADJACENT_LOCATION_RELATIONSHIP.typeName,
                                                        locationOneGUID,
                                                        locationTwoGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a location from one of its peers.
     *
     * @param userId                 userId of user making request.
     * @param locationOneGUID          unique identifier of the first location
     * @param locationTwoGUID          unique identifier of the second location
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachPeerLocations(String        userId,
                                    String        locationOneGUID,
                                    String        locationTwoGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "detachPeerLocations";
        final String end1GUIDParameterName = "locationOneGUID";
        final String end2GUIDParameterName = "locationTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(locationOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(locationTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.ADJACENT_LOCATION_RELATIONSHIP.typeName,
                                                        locationOneGUID,
                                                        locationTwoGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a super location to a nested location.
     *
     * @param userId                 userId of user making request
     * @param locationGUID          unique identifier of the super location
     * @param nestedLocationGUID            unique identifier of the nested location
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNestedLocation(String                  userId,
                                   String                  locationGUID,
                                   String                  nestedLocationGUID,
                                   MetadataSourceOptions   metadataSourceOptions,
                                   NestedLocationProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName            = "linkNestedLocation";
        final String end1GUIDParameterName = "locationGUID";
        final String end2GUIDParameterName = "nestedLocationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(locationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedLocationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.NESTED_LOCATION_RELATIONSHIP.typeName,
                                                        locationGUID,
                                                        nestedLocationGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a super location from a nested location.
     *
     * @param userId                 userId of user making request.
     * @param locationGUID          unique identifier of the super location
     * @param nestedLocationGUID            unique identifier of the nested location
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNestedLocation(String        userId,
                                     String        locationGUID,
                                     String        nestedLocationGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "detachNestedLocation";
        final String end1GUIDParameterName = "locationGUID";
        final String end2GUIDParameterName = "nestedLocationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(locationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedLocationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.NESTED_LOCATION_RELATIONSHIP.typeName,
                                                        locationGUID,
                                                        nestedLocationGUID,
                                                        deleteOptions);
    }


    /**
     * Attach an element to its location.
     *
     * @param userId                 userId of user making request
     * @param elementGUID       unique identifier of the element to connect
     * @param locationGUID            unique identifier of the location
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkKnownLocation(String                  userId,
                                  String                  elementGUID,
                                  String                  locationGUID,
                                  MetadataSourceOptions   metadataSourceOptions,
                                  KnownLocationProperties relationshipProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName            = "linkKnownLocation";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "locationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(locationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.KNOWN_LOCATION_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        locationGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an element from its location.
     *
     * @param userId                 userId of user making request.
     * @param elementGUID              unique identifier of the element
     * @param locationGUID          unique identifier of the location
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachKnownLocation(String        userId,
                                    String        elementGUID,
                                    String        locationGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "detachKnownLocation";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "locationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(locationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.KNOWN_LOCATION_RELATIONSHIP.typeName,
                                                        locationGUID,
                                                        locationGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a location.
     *
     * @param userId                 userId of user making request.
     * @param locationGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteLocation(String        userId,
                               String        locationGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName        = "deleteLocation";
        final String guidParameterName = "locationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(locationGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, locationGUID, deleteOptions);
    }


    /**
     * Returns the list of locations with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getLocationsByName(String       userId,
                                                            String       name,
                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "getLocationsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Return the properties of a specific location.
     *
     * @param userId                 userId of user making request
     * @param locationGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getLocationByGUID(String     userId,
                                                     String     locationGUID,
                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "getLocationByGUID";

        return super.getRootElementByGUID(userId,
                                          locationGUID,
                                          getOptions,
                                          methodName);
    }



    /**
     * Retrieve the list of locations metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findLocations(String        userId,
                                                       String        searchString,
                                                       SearchOptions searchOptions) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName  = "findLocations";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
