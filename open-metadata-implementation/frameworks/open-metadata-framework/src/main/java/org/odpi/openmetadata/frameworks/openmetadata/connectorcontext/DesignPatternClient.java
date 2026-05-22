/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DesignPatternHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designpatterns.DesignPatternProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designpatterns.NestedDesignPatternProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with design pattern elements.
 */
public class DesignPatternClient extends ConnectorContextClientBase
{
    private final DesignPatternHandler designPatternHandler;


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
    public DesignPatternClient(ConnectorContextBase     parentContext,
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

        this.designPatternHandler = new DesignPatternHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new design pattern.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createDesignPattern(NewElementOptions                     newElementOptions,
                                      Map<String, ClassificationProperties> initialClassifications,
                                      DesignPatternProperties               properties,
                                      RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        return designPatternHandler.createDesignPattern(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);
    }


    /**
     * Create a new metadata element to represent a design pattern using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new design pattern.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing element to copy
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties map of placeholder names to replacement values for the template's properties
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createDesignPatternFromTemplate(TemplateOptions                       templateOptions,
                                                  String                                templateGUID,
                                                  EntityProperties                      replacementProperties,
                                                  Map<String, ClassificationProperties> replacementClassifications,
                                                  Map<String, String>                   placeholderProperties,
                                                  RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                             PropertyServerException,
                                                                                                                             UserNotAuthorizedException
    {
        return designPatternHandler.createDesignPatternFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, replacementClassifications, placeholderProperties, parentRelationshipProperties);
    }


    /**
     * Update the properties of a design pattern.
     *
     * @param designPatternGUID unique identifier of the element to update
     * @param updateOptions options for update
     * @param properties properties for the update.
     * @return boolean indicates if the properties have actually changed.
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateDesignPattern(String                  designPatternGUID,
                                       UpdateOptions           updateOptions,
                                       DesignPatternProperties properties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        return designPatternHandler.updateDesignPattern(connectorUserId, designPatternGUID, updateOptions, properties);
    }


    /**
     * Connect two design patterns together as parent and child.
     *
     * @param parentDesignPatternGUID unique identifier of the parent design pattern
     * @param nestedDesignPatternGUID unique identifier of the nested design pattern
     * @param makeAnchorOptions should the nested design pattern be anchored to the parent?
     * @param relationshipProperties properties of the relationship
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNestedDesignPatterns(String                        parentDesignPatternGUID,
                                         String                        nestedDesignPatternGUID,
                                         MakeAnchorOptions             makeAnchorOptions,
                                         NestedDesignPatternProperties relationshipProperties) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        designPatternHandler.linkNestedDesignPatterns(connectorUserId, parentDesignPatternGUID, nestedDesignPatternGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Remove the link between two design patterns.
     *
     * @param parentDesignPatternGUID unique identifier of the parent design pattern
     * @param nestedDesignPatternGUID unique identifier of the nested design pattern
     * @param deleteOptions options for delete
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNestedDesignPatterns(String        parentDesignPatternGUID,
                                           String        nestedDesignPatternGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        designPatternHandler.detachNestedDesignPatterns(connectorUserId, parentDesignPatternGUID, nestedDesignPatternGUID, deleteOptions);
    }


    /**
     * Connect two design patterns together as general and specialized.
     *
     * @param generalizedDesignPatternGUID unique identifier of the generalized design pattern
     * @param specializedDesignPatternGUID unique identifier of the specialized design pattern
     * @param makeAnchorOptions should the specialized design pattern be anchored to the generalized?
     * @param relationshipProperties properties of the relationship
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSpecializedDesignPatterns(String                        generalizedDesignPatternGUID,
                                              String                        specializedDesignPatternGUID,
                                              MakeAnchorOptions             makeAnchorOptions,
                                              NestedDesignPatternProperties relationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        designPatternHandler.linkSpecializedDesignPatterns(connectorUserId, generalizedDesignPatternGUID, specializedDesignPatternGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Remove the link between two design patterns.
     *
     * @param generalizedDesignPatternGUID unique identifier of the generalized design pattern
     * @param specializedDesignPatternGUID unique identifier of the specialized design pattern
     * @param deleteOptions options for delete
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSpecializedDesignPatterns(String        generalizedDesignPatternGUID,
                                                String        specializedDesignPatternGUID,
                                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        designPatternHandler.detachSpecializedDesignPatterns(connectorUserId, generalizedDesignPatternGUID, specializedDesignPatternGUID, deleteOptions);
    }


    /**
     * Connect two design patterns together as related.
     *
     * @param designPatternOneGUID unique identifier of the first design pattern
     * @param designPatternTwoGUID unique identifier of the second design pattern
     * @param makeAnchorOptions should the second design pattern be anchored to the first?
     * @param relationshipProperties properties of the relationship
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkRelatedDesignPatterns(String                        designPatternOneGUID,
                                          String                        designPatternTwoGUID,
                                          MakeAnchorOptions             makeAnchorOptions,
                                          NestedDesignPatternProperties relationshipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        designPatternHandler.linkRelatedDesignPatterns(connectorUserId, designPatternOneGUID, designPatternTwoGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Remove the link between two design patterns.
     *
     * @param designPatternOneGUID unique identifier of the first design pattern
     * @param designPatternTwoGUID unique identifier of the second design pattern
     * @param deleteOptions options for delete
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachRelatedDesignPatterns(String        designPatternOneGUID,
                                            String        designPatternTwoGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        designPatternHandler.detachRelatedDesignPatterns(connectorUserId, designPatternOneGUID, designPatternTwoGUID, deleteOptions);
    }


    /**
     * Delete a design pattern.
     *
     * @param designPatternGUID unique identifier of the element to delete
     * @param deleteOptions options for delete
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteDesignPattern(String        designPatternGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        designPatternHandler.deleteDesignPattern(connectorUserId, designPatternGUID, deleteOptions);
    }


    /**
     * Returns the list of design patterns with a particular name.
     *
     * @param name name to find
     * @param queryOptions options for query
     * @return list of matching metadata elements
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getDesignPatternsByName(String       name,
                                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        return designPatternHandler.getDesignPatternsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific design pattern.
     *
     * @param designPatternGUID unique identifier of the element to query
     * @param getOptions options for query
     * @return matching metadata element
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getDesignPatternByGUID(String     designPatternGUID,
                                                          GetOptions getOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        return designPatternHandler.getDesignPatternByGUID(connectorUserId, designPatternGUID, getOptions);
    }


    /**
     * Retrieve the list of design pattern metadata elements that contain the search string.
     *
     * @param searchString string to find in the properties
     * @param searchOptions options for search
     * @return list of matching metadata elements
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> findDesignPatterns(String        searchString,
                                                            SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        return designPatternHandler.findDesignPatterns(connectorUserId, searchString, searchOptions);
    }
}
