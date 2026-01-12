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
import org.odpi.openmetadata.frameworks.openmetadata.properties.propertyfacets.PropertyFacetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.propertyfacets.ReferenceableFacetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * PropertyFacetHandler is the handler for managing property facets.  These elements are anchored to their
 * parent entity so that they are included in an anchor based search.
 */
public class PropertyFacetHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public PropertyFacetHandler(String             localServerName,
                                AuditLog           auditLog,
                                String             serviceName,
                                OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.PROPERTY_FACET.typeName);
    }


    /**
     * Adds a property facet to the element.
     *
     * @param userId        userId of user making request
     * @param elementGUID     unique identifier for the element
     * @param metadataSourceOptions options for the request
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties   properties of the property facet
     * @param relationshipProperties properties for the ReferenceableFacet relationship
     *
     * @return guid of new property facet.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addPropertyFacetToElement(String                                userId,
                                            String                                elementGUID,
                                            MetadataSourceOptions                 metadataSourceOptions,
                                            Map<String, ClassificationProperties> initialClassifications,
                                            PropertyFacetProperties               properties,
                                            ReferenceableFacetProperties          relationshipProperties) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        final String methodName = "addPropertyFacetToElement";
        final String propertiesParameterName   = "properties";
        final String qualifiedNameParameterName = "properties.qualifiedName";

        propertyHelper.validateObject(properties, propertiesParameterName, methodName);
        propertyHelper.validateMandatoryName(properties.getQualifiedName(), qualifiedNameParameterName, methodName);

        NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

        newElementOptions.setAnchorGUID(elementGUID);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setParentGUID(elementGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName);
        newElementOptions.setParentAtEnd1(true);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               OpenMetadataType.PROPERTY_FACET.typeName,
                                                               newElementOptions,
                                                               classificationBuilder.getInitialClassifications(initialClassifications),
                                                               elementBuilder.getNewElementProperties(properties),
                                                               relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Update an existing property facet.
     *
     * @param userId        userId of user making request.
     * @param propertyFacetGUID   unique identifier for the property facet to change.
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties   properties of the propertyFacet
     *
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updatePropertyFacet(String                  userId,
                                       String                  propertyFacetGUID,
                                       UpdateOptions           updateOptions,
                                       PropertyFacetProperties properties) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "updatePropertyFacet";
        final String propertiesParameterName   = "properties";

        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        return openMetadataClient.updateMetadataElementInStore(userId, propertyFacetGUID, updateOptions, elementBuilder.getNewElementProperties(properties));
    }


    /**
     * Removes a property facet added to the element by this user.
     *
     * @param userId       userId of user making request.
     * @param propertyFacetGUID  unique identifier for the property facet object.
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void deletePropertyFacet(String        userId,
                                    String        propertyFacetGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        openMetadataClient.deleteMetadataElementInStore(userId, propertyFacetGUID, deleteOptions);
    }


    /**
     * Return the requested property facet.
     *
     * @param userId       userId of user making request.
     * @param propertyFacetGUID  unique identifier for the property facet object.
     * @param getOptions multiple options to control the query
     * @return property facet properties
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElement getPropertyFacetByGUID(String     userId,
                                                          String     propertyFacetGUID,
                                                          GetOptions getOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getPropertyFacetByGUID";

        return super.getRootElementByGUID(userId, propertyFacetGUID, getOptions, methodName);
    }


    /**
     * Return the list of keywords exactly matching the supplied name.
     *
     * @param userId the name of the calling user
     * @param name name to search for
     * @param queryOptions multiple options to control the query
     *
     * @return name list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getPropertyFacetsByName(String       userId,
                                                                 String       name,
                                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "getPropertyFacetsByName";
        final String nameParameterName = "name";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(name, nameParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        List<String> propertyNames = Collections.singletonList(OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }



    /**
     * Retrieve the list of property facet metadata elements that contain the search string.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findPropertyFacets(String        userId,
                                                            String        searchString,
                                                            SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "findPropertyFacets";

        return super.findRootElements(userId, searchString, searchOptions,methodName);
    }
}
