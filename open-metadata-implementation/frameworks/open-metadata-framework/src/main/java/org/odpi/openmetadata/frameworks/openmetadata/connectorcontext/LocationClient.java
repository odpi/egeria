/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.LocationHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.AdjacentLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.KnownLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.LocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.NestedLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with location elements.
 */
public class LocationClient extends ConnectorContextClientBase
{
    private final LocationHandler locationHandler;


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
    public LocationClient(ConnectorContextBase     parentContext,
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

        this.locationHandler = new LocationHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new location.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createLocation(NewElementOptions                     newElementOptions,
                                 Map<String, ClassificationProperties> initialClassifications,
                                 LocationProperties                    properties,
                                 RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        String elementGUID = locationHandler.createLocation(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a location using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new location.
     *
     * @param templateOptions              details of the element to create
     * @param templateGUID                 the unique identifier of the existing location to copy (this will copy all the attachments such as nested content, schema
     *                                     connection etc)
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createLocationFromTemplate(TemplateOptions templateOptions,
                                             String templateGUID,
                                             ElementProperties replacementProperties,
                                             Map<String, String> placeholderProperties,
                                             RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        String elementGUID = locationHandler.createLocationFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a location.
     *
     * @param locationGUID       unique identifier of the location (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateLocation(String             locationGUID,
                                  UpdateOptions      updateOptions,
                                  LocationProperties properties) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        boolean updateOccurred = locationHandler.updateLocation(connectorUserId, locationGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(locationGUID);
        }

        return updateOccurred;
    }


    /**
     * Attach a location to one of its peers.
     *
     * @param locationOneGUID          unique identifier of the first location
     * @param locationTwoGUID          unique identifier of the second location
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkPeerLocations(String                     locationOneGUID,
                                  String                     locationTwoGUID,
                                  MakeAnchorOptions          makeAnchorOptions,
                                  AdjacentLocationProperties relationshipProperties) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        locationHandler.linkPeerLocations(connectorUserId, locationOneGUID, locationTwoGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a location from one of its peers.
     *
     * @param locationOneGUID          unique identifier of the first location
     * @param locationTwoGUID          unique identifier of the second location
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachPeerLocations(String        locationOneGUID,
                                    String        locationTwoGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        locationHandler.detachPeerLocations(connectorUserId, locationOneGUID, locationTwoGUID, deleteOptions);
    }


    /**
     * Attach a super location to a nested location.
     *
     * @param locationGUID          unique identifier of the super location
     * @param nestedLocationGUID            unique identifier of the nested location
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNestedLocation(String                   locationGUID,
                                   String                   nestedLocationGUID,
                                   MakeAnchorOptions        makeAnchorOptions,
                                   NestedLocationProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        locationHandler.linkNestedLocation(connectorUserId, locationGUID, nestedLocationGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a super location from a nested location.
     *
     * @param locationGUID          unique identifier of the super location
     * @param nestedLocationGUID            unique identifier of the nested location
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNestedLocation(String        locationGUID,
                                     String        nestedLocationGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        locationHandler.detachNestedLocation(connectorUserId, locationGUID, nestedLocationGUID, deleteOptions);
    }


    /**
     * Attach an element to its location.
     *
     * @param elementGUID       unique identifier of the element to connect
     * @param locationGUID            unique identifier of the location
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkKnownLocation(String                  elementGUID,
                                  String                  locationGUID,
                                  MakeAnchorOptions       makeAnchorOptions,
                                  KnownLocationProperties relationshipProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        locationHandler.linkKnownLocation(connectorUserId, elementGUID, locationGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach an element from its location.
     *
     * @param elementGUID              unique identifier of the element
     * @param locationGUID          unique identifier of the location
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachKnownLocation(String        elementGUID,
                                    String        locationGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        locationHandler.detachKnownLocation(connectorUserId, elementGUID, locationGUID, deleteOptions);
    }


    /**
     * Delete a location.
     *
     * @param locationGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteLocation(String        locationGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        locationHandler.deleteLocation(connectorUserId, locationGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(locationGUID);
        }
    }


    /**
     * Returns the list of locations with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getLocationsByName(String       name,
                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        return locationHandler.getLocationsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific location.
     *
     * @param locationGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getLocationByGUID(String     locationGUID,
                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        return locationHandler.getLocationByGUID(connectorUserId, locationGUID, getOptions);
    }


    /**
     * Retrieve the list of locations metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned locations include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findLocations(String        searchString,
                                                       SearchOptions searchOptions) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return locationHandler.findLocations(connectorUserId, searchString, searchOptions);
    }
}
